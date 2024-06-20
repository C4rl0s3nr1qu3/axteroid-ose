<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" 
	xmlns="urn:oasis:names:specification:ubl:schema:xsd:Invoice-2"
	xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
	xmlns:ext="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2"
	xmlns:sac="urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1"
	xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
	xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
	xmlns:dp="http://www.datapower.com/extensions"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:date="http://exslt.org/dates-and-times"
	extension-element-prefixes="dp" exclude-result-prefixes="dp">

<!-- INICIO: AXTEROID -->
  <xsl:include href="../util/validate_utils.xsl" dp:ignore-multiple="yes" />
<!-- FIN: AXTEROID -->	

	<!-- key Duplicados -->
	<xsl:key name="by-document-despatch-reference"
		match="*[local-name()='Invoice']/cac:DespatchDocumentReference" use="concat(cbc:DocumentTypeCode,' ', cbc:ID)" />
	<xsl:key name="by-document-additional-reference"
		match="*[local-name()='Invoice']/cac:AdditionalDocumentReference" use="concat(cbc:DocumentTypeCode,' ', cbc:ID)" />
	<xsl:key name="by-pricingReference-alternativeConditionPrice-priceTypeCode"
		match="*[local-name()='Invoice']/cac:PricingReference/cac:AlternativeConditionPrice"
		use="cbc:PriceTypeCode" />
	<xsl:key name="by-invoiceLine-id" match="*[local-name()='Invoice']/cac:InvoiceLine" use="number(cbc:ID)" />
	<xsl:key name="by-prepaidPayment-schemeID" match="*[local-name()='Invoice']/cac:PrepaidPayment" use="cbc:ID/@schemeID" />
	
	<xsl:param name="nombreArchivoEnviado" />

	<xsl:template match="/*">
		<!-- Ini validacion del nombre del archivo vs el nombre del cbc:ID -->	
		<xsl:variable name="rucFilename" select="substring($nombreArchivoEnviado, 1, 11)" />
		<xsl:variable name="idFilename" select="substring($nombreArchivoEnviado, 13, string-length($nombreArchivoEnviado) - 16)" />
		<xsl:variable name="fechaEnvioFile" select="substring($nombreArchivoEnviado, 16, 8)" />
		<xsl:variable name="tipoComprobante" select="substring($nombreArchivoEnviado, 13, 2)" />
		<xsl:variable name="numeroSerie" select="substring($nombreArchivoEnviado, 16, 4)" />
		<xsl:variable name="numeroComprobante" select="substring($nombreArchivoEnviado, 21, string-length($nombreArchivoEnviado) - 24)" />		
		<xsl:variable name="numeroRuc" select="substring($nombreArchivoEnviado, 1, 11)" />		
		<!-- Fin validacion del nombre del archivo vs el nombre del cbc:ID -->
		
		<!-- Variables -->
		<!-- 1. Versión del UBL -->
		<xsl:variable name="cbcUBLVersionID" select="cbc:UBLVersionID" />
		<!-- 2. Versión de la estructura del documento -->
		<xsl:variable name="cbcCustomizationID" select="cbc:CustomizationID" />
		<!-- 3. Numeración, conformada por serie y número correlativo -->
		<xsl:variable name="cbcID" select="cbc:ID" />		
		<!-- 4. Fecha de emisión -->
		<xsl:variable name="cbcIssueDate" select="cbc:IssueDate" />	
		<!-- 6. Tipo de documento -->
		<xsl:variable name="tipoDocumento" select="cbc:InvoiceTypeCode"/>		
		<!-- 7. Tipo de moneda -->
		<xsl:variable name="tipoMoneda" select="cbc:DocumentCurrencyCode"/>		
		<!-- 8. Fecha de Vencimiento -->
		<xsl:variable name="fechaVencimiento" select="cac:PaymentMeans/cbc:PaymentDueDate"/>		
		
		<xsl:variable name="cbcReferenceDate" select="cbc:ReferenceDate" />
		<xsl:variable name="currentdate" select="fn:current-date()"></xsl:variable>				

		<!-- Datos del Emisor Electrónico -->			
		<xsl:variable name="emisor" select="cac:AccountingSupplierParty"/>		
		<!-- Mandatorio -->
		<!-- 9. Número de RUC -->
		<xsl:variable name="emisorNumeroDocumento" select="$emisor/cbc:CustomerAssignedAccountID"/>	
		<!-- Mandatorio -->
		<xsl:variable name="emisorTipoDocumento" select="$emisor/cbc:AdditionalAccountID"/>			
		<!-- Opcional -->
		<!-- 11. Apellidos y nombres, denominación o razón social -->
		<xsl:variable name="emisorRazonSocial" select="$emisor/cac:Party/cac:PartyLegalEntity/cbc:RegistrationName"/>
		<!-- Opcional -->
		<!-- 12. Dirección del lugar en el que se entrega el bien o se presta el servicio -->
		<xsl:variable name="emisorCodigoPais" select="cac:SellerSupplierParty/cac:Party/cac:PostalAddress/cac:Country/cbc:IdentificationCode" />
		<!-- Fin Datos del Emisor Electrónico -->

		<!-- Datos del Cliente o receptor -->			
		<xsl:variable name="cliente" select="cac:AccountingCustomerParty"/>		
		<!-- Mandatorio -->
		<!-- 14. Número de RUC -->
		<xsl:variable name="clienteNumeroDocumento" select="$cliente/cbc:CustomerAssignedAccountID"/>	
		<!-- 14. -->
		<xsl:variable name="clienteTipoDocumento" select="$cliente/cbc:AdditionalAccountID"/>			
		<!-- Opcional -->
		<!-- 15. Apellidos y nombres, denominación o razón social -->
		<xsl:variable name="clienteRazonSocial" select="$cliente/cac:Party/cac:PartyLegalEntity/cbc:RegistrationName"/>
						
		<!-- EXT -->
		<xsl:variable name="sacAdditionalInformation" select="ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/sac:AdditionalInformation" />		
		<!-- 25. Código de precio -->
		<xsl:variable name="codigoPrecio" select="cac:InvoiceLine/cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode" />
		<!-- 28. Monto de ISC de la línea -->
		<xsl:variable name="montoISCLinea" select="sum(cac:InvoiceLine/cac:TaxTotal[cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxAmount)"/>
		<!-- 29. Valor de venta por línea -->
		<xsl:variable name="valorVentaLinea" select="cbc:LineExtensionAmount"/>		
		<!-- AdditionalInformation -->
		<xsl:variable name="additionalMonetaryTotal" select="$sacAdditionalInformation/sac:AdditionalMonetaryTotal" />
		<!-- 31. Código de tipo de monto -->
		<xsl:variable name="codigoTipoMonto" select="$additionalMonetaryTotal/cbc:ID" />
		<!-- 37. Sumatoria ISC -->
		<xsl:variable name="sumatoriaISC" select="sum(cac:TaxTotal[cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxAmount)"/>				
		<!-- 38. Sumatoria otros tributos -->
		<xsl:variable name="sumatoriaOtrosTributos" select="sum(cac:TaxTotal[cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxAmount)"/>				
		<!-- 40. Sumatoria otros Cargos -->
		<xsl:variable name="sumatoriaOtrosCargos">
			<xsl:choose>
    			<xsl:when test="count(cac:LegalMonetaryTotal/cbc:ChargeTotalAmount) &gt; 0">
    			<xsl:value-of select="cac:LegalMonetaryTotal/cbc:ChargeTotalAmount"/></xsl:when>
    			<xsl:otherwise>0</xsl:otherwise>
  			</xsl:choose>       
		</xsl:variable>
		<!-- 41. Importe total -->
		<xsl:variable name="importeTotal">	
			<xsl:choose>
    			<xsl:when test="count(cac:LegalMonetaryTotal/cbc:PayableAmount) &gt; 0">
    				<xsl:value-of select="cac:LegalMonetaryTotal/cbc:PayableAmount"/></xsl:when>
    			<xsl:otherwise>0</xsl:otherwise>
  			</xsl:choose>       
		</xsl:variable>		
		<xsl:variable name="sumIGV" select="sum(cac:TaxTotal[cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxAmount)"/>		
		<xsl:variable name="sumTVVOGravadas" select="sum($additionalMonetaryTotal[cbc:ID='1001']/cbc:PayableAmount)"/>		
		<xsl:variable name="sumTVVOInafectas" select="sum($additionalMonetaryTotal[cbc:ID='1002']/cbc:PayableAmount)"/>		
		<xsl:variable name="sumTVVOExoneradas" select="sum($additionalMonetaryTotal[cbc:ID='1003']/cbc:PayableAmount)"/>	
		<!-- 43. Total Anticipos -->
		<xsl:variable name="totalAnticipos" select="cac:LegalMonetaryTotal/cbc:PrepaidAmount" />				
		<!-- 45. Tipo de operación -->
		<xsl:variable name="tipoOperacion">	
			<xsl:choose>
    			<xsl:when test="count($sacAdditionalInformation/sac:SUNATTransaction/cbc:ID) &gt; 0">
    				<xsl:value-of select="$sacAdditionalInformation/sac:SUNATTransaction/cbc:ID"/></xsl:when>
    			<xsl:otherwise>0</xsl:otherwise>
  			</xsl:choose>       
		</xsl:variable>			
		<!-- 46. Código de leyenda -->
		<xsl:variable name="codigoLeyenda" select="$sacAdditionalInformation/sac:AdditionalProperty/cbc:ID" />

		<!-- Validaciones -->
		
		<!-- 1. Version del UBL -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2075'" />
			<xsl:with-param name="errorCodeValidate" select="'2074'" />
			<xsl:with-param name="node" select="$cbcUBLVersionID" />
			<xsl:with-param name="regexp" select="'^(2.0)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cbcUBLVersionID)"/>
		</xsl:call-template>

		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'4292'" />
			<xsl:with-param name="node" select="$cbcUBLVersionID" />
			<xsl:with-param name="regexp" select="'^(2.0)$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cbcUBLVersionID)"/>
		</xsl:call-template>

		<!-- 2. Version de la Estructura del Documento -->	
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'2072'"/>
			<xsl:with-param name="node" select="$cbcCustomizationID"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $cbcCustomizationID)"/>
		</xsl:call-template>		
			
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2072'" />
			<xsl:with-param name="node" select="$cbcCustomizationID" />
			<xsl:with-param name="regexp" select="'^(1.0)$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cbcCustomizationID)"/>
		</xsl:call-template>
		
		<!-- 3. Numero de Serie del nombre del archivo no coincide con el consignado en el contenido del archivo XML -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'1001'"/>
			<xsl:with-param name="node" select="$cbcID"/>
			<xsl:with-param name="regexp" select="'^[B][A-Z0-9]{3}-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cbcID)"/>
		</xsl:call-template>		
		
		<xsl:if test="$numeroSerie != substring($cbcID, 1, 4)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'1035'" />
				<xsl:with-param name="errorMessage"
					select="concat('numero de serie del xml diferente al numero de serie del archivo ', substring($cbcID, 1, 4), ' diff ', $numeroSerie)" />
			</xsl:call-template>
		</xsl:if>

		<!-- 3. Numero de documento en el nombre del archivo no coincide con el consignado en el contenido del XML -->
		<xsl:if test="$numeroComprobante != substring($cbcID, 6)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'1036'" />
				<xsl:with-param name="errorMessage"
					select="concat('numero de comprobante del xml diferente al numero del archivo ', substring($cbcID, 6), ' diff ', $numeroComprobante)" />
			</xsl:call-template>
		</xsl:if>
		
		<!-- 4. Fecha de emisión -->
		<xsl:variable name="t1" select="xs:date($currentdate)-xs:date($cbcIssueDate)" />
		<xsl:variable name="t2" select="fn:days-from-duration(xs:duration($t1))" />			
		<xsl:if test="($t2 &lt; -2)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2329'" />
				<xsl:with-param name="errorMessage" 
					select="concat('Error en la linea: ', $cbcIssueDate, ' - ', $currentdate, ' - ', $t1, ' - ', $t2)" />
			</xsl:call-template>
		</xsl:if>
				
		<!-- 6.Tipo de documento -->		
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'1004'" />
			<xsl:with-param name="errorCodeValidate" select="'1003'" />
			<xsl:with-param name="node" select="$tipoDocumento" />
			<xsl:with-param name="regexp" select="'^(03)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoDocumento)"/>
		</xsl:call-template>
		
		<!-- 7. Tipo de moneda en la cual se emite la factura electronica -->		
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'2070'"/>
			<xsl:with-param name="node" select="$tipoMoneda"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoMoneda)"/>
		</xsl:call-template>		
		
		<xsl:call-template name="findElementInCatalog">
			<xsl:with-param name="errorCodeValidate" select="'3088'"/>
<!-- 			<xsl:with-param name="idCatalogo" select="cbc:DocumentCurrencyCode"/> -->
			<xsl:with-param name="idCatalogo" select="$tipoMoneda"/>
			<xsl:with-param name="catalogo" select="'02'"/>
		</xsl:call-template>
		
		<!-- Datos del ciente o receptor -->
		
		<!-- 9. Numero de RUC del nombre del archivo no coincide con el consignado en el contenido del archivo XML -->
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'1006'"/>
			<xsl:with-param name="node" select="$emisorNumeroDocumento"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $emisorNumeroDocumento)"/>
		</xsl:call-template>
		
		<xsl:if
			test="$numeroRuc != $emisorNumeroDocumento">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'1034'" />
				<xsl:with-param name="errorMessage"
					select="concat('ruc del xml diferente al nombre del archivo ', $emisorNumeroDocumento, ' diff ', $numeroRuc)" />
			</xsl:call-template>
		</xsl:if>

		<!-- 9. Tipo de documento de identidad del emisor - RUC -->
		<xsl:variable name="countAccountingSupplierPartyAdditionalAccountID" select="count(cac:AccountingSupplierParty/cbc:AdditionalAccountID)"/>
		<xsl:if test="$countAccountingSupplierPartyAdditionalAccountID &gt; 1">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2362'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $countAccountingSupplierPartyAdditionalAccountID)"/>
			</xsl:call-template>
		</xsl:if>		
		
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'1008'" />
			<xsl:with-param name="errorCodeValidate" select="'1007'" />
			<xsl:with-param name="node" select="$emisorTipoDocumento" />
			<xsl:with-param name="regexp" select="'^[6]{1}$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $emisorTipoDocumento)"/>
		</xsl:call-template>

		<!-- 11. Apellidos y nombres o denominacion o razon social Emisor -->		
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'1037'" />
			<xsl:with-param name="errorCodeValidate" select="'1038'" />
			<xsl:with-param name="node" select="$emisorRazonSocial" />
			<xsl:with-param name="regexp" select="'^[\w\s].{0,999}$'"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $emisorRazonSocial)"/>
		</xsl:call-template>
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'1038'"/>
			<xsl:with-param name="node" select="$emisorRazonSocial"/>
			<xsl:with-param name="regexp" select="'[^\s]'"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $emisorRazonSocial)"/>
		</xsl:call-template>
		
		<!-- 12. Ubicacion de pais PE -->
		<xsl:if test="$emisorCodigoPais!='PE'">
			<xsl:call-template name="addWarning">
				<xsl:with-param name="warningCode" select="'4041'" />
				<xsl:with-param name="warningMessage"
					select="concat('Error en la linea: ', $emisorCodigoPais)"/>
			</xsl:call-template>
		</xsl:if>

		<!-- Datos del ciente o receptor -->			
		<!-- 14. Valida que el tipo de documento del adquiriente exista y sea solo uno -->		
		<xsl:variable name="countClienteTipoDocumento" select="count(cac:AccountingCustomerParty/cbc:AdditionalAccountID)"/>	
		<xsl:if 
			test="($countClienteTipoDocumento &gt; 1)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2363'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $countClienteTipoDocumento)"/>
			</xsl:call-template>
		</xsl:if>
		
		<!-- 14. Valida que el tipo de documento del adquiriente exista -->			
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'2015'"/>
			<xsl:with-param name="node" select="$clienteTipoDocumento"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $clienteTipoDocumento)"/>
		</xsl:call-template>

		<!-- 14. Número de documento de identidad del adquirente o usuario -->
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'2014'"/>
			<xsl:with-param name="node" select="$clienteNumeroDocumento"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $clienteNumeroDocumento)"/>
		</xsl:call-template>
		
		<xsl:if
			test="$clienteTipoDocumento='6'">
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2017'"/>
				<xsl:with-param name="node" select="$clienteNumeroDocumento"/>
				<xsl:with-param name="regexp" select="'^[0-9]{11}$|^[-]{1}$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $clienteNumeroDocumento)"/>
			</xsl:call-template>				
		</xsl:if>		

		<xsl:if
			test="$clienteTipoDocumento='1' and not(fn:matches($clienteNumeroDocumento,'^[0-9]{8}$'))">
			<xsl:call-template name="addWarning">
				<xsl:with-param name="warningCode" select="'4207'" />
				<xsl:with-param name="warningMessage"
					select="concat('Error en la linea: ',  $clienteNumeroDocumento)"/>
			</xsl:call-template>	
		</xsl:if>
		
		<xsl:if test="($clienteTipoDocumento='4') or ($clienteTipoDocumento='7')">
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4208'"/>
				<xsl:with-param name="node" select="$clienteNumeroDocumento"/>
				<xsl:with-param name="regexp" select="'^[\w\d\-]{1,15}$'"/>
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $clienteNumeroDocumento)"/>
			</xsl:call-template>		
		</xsl:if>
		
		<!-- 15. Apellidos y nombres, denominación o razón social del adquirente o usuario  -->		
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'2021'"/>
			<xsl:with-param name="node" select="$clienteRazonSocial"/>
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $clienteRazonSocial)"/>
		</xsl:call-template>
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2022'" />
			<xsl:with-param name="node" select="$clienteRazonSocial" />
			<xsl:with-param name="regexp" select="'^[^\n].{2,999}$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $clienteRazonSocial)"/>
		</xsl:call-template>
				
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2022'"/>
			<xsl:with-param name="node" select="$clienteRazonSocial"/>
			<xsl:with-param name="regexp" select="'[^\s]'"/>
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $clienteRazonSocial)"/>
		</xsl:call-template>
						
		<!-- Documentos de referencia -->	
		<!-- 16. Número de la guía de remisión relacionada -->
		<xsl:for-each select="cac:DespatchDocumentReference">
			<xsl:variable name="numeroGuiaRemisionRelacionada" select="./cbc:ID"/>	
			<xsl:variable name="tipoGuiaRemisionRelacionada" select="./cbc:DocumentTypeCode"/>			
				
			<xsl:if test="string($tipoGuiaRemisionRelacionada)">			
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4006'"/>
					<xsl:with-param name="node" select="$numeroGuiaRemisionRelacionada"/>
					<xsl:with-param name="regexp" select="'^[T][0-9]{3}-[0-9]{1,8}$|^[0-9]{4}-[0-9]{1,8}$|^[EG]{2}[0-9]{2}-[0-9]{1,8}$|^[G]{1}[0-9]{3}-[0-9]{1,8}$'"/>
					<xsl:with-param name="isError" select="false()"/> 
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $numeroGuiaRemisionRelacionada)"/>
				</xsl:call-template>	
			
				<xsl:if
					test="count(key('by-document-despatch-reference', concat($tipoGuiaRemisionRelacionada,' ',$numeroGuiaRemisionRelacionada))) &gt; 1">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2364'" />
						<xsl:with-param name="errorMessage"
							select="'Error Expr Regular Factura  (codigo: 2364)'" />
					</xsl:call-template>
				</xsl:if>
			
				<xsl:if
					test="not(($tipoGuiaRemisionRelacionada='09') or ($tipoGuiaRemisionRelacionada='31'))">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4005'" />
						<xsl:with-param name="warningMessage"
							select="concat('documento: ', position(), ' ', $tipoGuiaRemisionRelacionada)" />
					</xsl:call-template>
				</xsl:if>			
			</xsl:if>
		</xsl:for-each>
		
		<!-- 17. Número de otro documento relacionado -->
		<xsl:for-each select="cac:AdditionalDocumentReference">		
			<xsl:variable name="numeroOtroDocumentoRelacionada" select="./cbc:ID"/>
			<xsl:variable name="tipoOtroDocumentoRelacionada" select="./cbc:DocumentTypeCode"/>	
			
			<xsl:if test="$tipoOtroDocumentoRelacionada">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4010'"/>
					<xsl:with-param name="node" select="$numeroOtroDocumentoRelacionada"/>
					<xsl:with-param name="regexp" select="'^[\w].{0,99}$'"/>
					<xsl:with-param name="isError" select="false()"/> 
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $tipoOtroDocumentoRelacionada,' ',$numeroOtroDocumentoRelacionada)"/>
				</xsl:call-template>	
			
				<xsl:call-template name="regexpValidateElementIfExistTrue">
					<xsl:with-param name="errorCodeValidate" select="'4010'"/>
					<xsl:with-param name="node" select="$numeroOtroDocumentoRelacionada"/>
					<xsl:with-param name="regexp" select="'[\s]'"/>
					<xsl:with-param name="isError" select="false()"/> 
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $tipoOtroDocumentoRelacionada,' ',$numeroOtroDocumentoRelacionada)"/>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if test="$numeroOtroDocumentoRelacionada">			
				<xsl:if
					test="not(($tipoOtroDocumentoRelacionada='04') or ($tipoOtroDocumentoRelacionada='05') or ($tipoOtroDocumentoRelacionada='99') or ($tipoOtroDocumentoRelacionada='01'))">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4009'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', $tipoOtroDocumentoRelacionada)"/>
					</xsl:call-template>
				</xsl:if>			
			</xsl:if>
		
			<xsl:if
				test="count(key('by-document-additional-reference', concat($tipoOtroDocumentoRelacionada,' ',$numeroOtroDocumentoRelacionada))) &gt; 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2365'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $tipoOtroDocumentoRelacionada,' ',$numeroOtroDocumentoRelacionada)"/>
				</xsl:call-template>
			</xsl:if>

		</xsl:for-each>
				
		<!-- Datos del detalle o Ítem de la Factura -->
		<xsl:for-each select="cac:InvoiceLine">
			<!-- 18. Número de orden del Ítem -->
			<xsl:variable name="numeroItem" select="./cbc:ID"/>
			<!-- 19. Unidad de medida por ítem -->		
			<xsl:variable name="unidadMedidaItem" select="./cbc:InvoicedQuantity/@unitCode"/>	
			<!-- 20. Cantidad de unidades por ítem -->	
			<xsl:variable name="cantidadUnidadesItem" select="./cbc:InvoicedQuantity"/>
			<!-- 23. Descripción detallada del servicio prestado, bien vendido o cedido en uso, indicando las características. -->		
<!-- 			<xsl:variable name="descripcionDetalladaServicioPrestado" select="./cac:Item/cbc:Description"/> -->
			<!-- 24. Valor unitario por ítem -->		
			<xsl:variable name="valorUnitarioItem" select="./cac:Price/cbc:PriceAmount"/>	
			<!-- 24. Moneda de Valor unitario por ítem -->		
			<xsl:variable name="monedaValorUnitarioItem" select="./cac:Price/cbc:PriceAmount/@currencyID"/>				
			<!-- 29. Valor de venta por línea -->		
			<xsl:variable name="valorVentaLinea" select="./cbc:LineExtensionAmount"/>	
			<!-- 29. Moneda Valor de venta por línea -->		
			<xsl:variable name="monedaValorVentaLinea" select="./cbc:LineExtensionAmount/@currencyID"/>	
			
			<!-- Moneda debe ser la misma en todo el documento -->		
			<xsl:if test="($monedaValorVentaLinea) and ($tipoMoneda!=$monedaValorVentaLinea)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$monedaValorVentaLinea)" />
				</xsl:call-template>
			</xsl:if>		
			
			<xsl:if test="($monedaValorUnitarioItem) and ($tipoMoneda!=$monedaValorUnitarioItem)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$monedaValorUnitarioItem)" />
				</xsl:call-template>
			</xsl:if>					
						
			<!-- 18. Número de orden del Ítem -->
			<xsl:choose>
				<xsl:when test="not($numeroItem)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2023'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position())" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:if
						test='not(fn:matches($numeroItem,"^[0-9]{1,3}?$")) or $numeroItem &lt;= 0'>
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2023'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position())" />
						</xsl:call-template>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:if test="count(key('by-invoiceLine-id', number($numeroItem))) &gt; 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2752'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position())" />
				</xsl:call-template>
			</xsl:if>
			
			<!-- 20. Cantidad de unidades por item -->	
			<xsl:call-template name="existValidateElementIfExist">
				<xsl:with-param name="errorCodeNotExist" select="'2024'"/>
				<xsl:with-param name="node" select="$cantidadUnidadesItem"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $cantidadUnidadesItem)"/>
			</xsl:call-template>
			
			<!-- 19. Unidad de medida por ítem -->		
			<xsl:call-template name="existValidateElementIfExist">
				<xsl:with-param name="errorCodeNotExist" select="'2883'"/>
				<xsl:with-param name="node" select="$unidadMedidaItem"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $unidadMedidaItem)"/>
			</xsl:call-template>
						
			<!-- 20. Cantidad de unidades por item -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2025'" />
				<xsl:with-param name="node" select="$cantidadUnidadesItem" />
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $cantidadUnidadesItem)"/>
			</xsl:call-template>
						
			<!-- 23. Descripcion detallada del servicio prestado, bien vendido o cedido en uso, indicando las caracteristicas -->
			<xsl:choose>
                <xsl:when test="not(./cac:Item/cbc:Description)">
                    <xsl:call-template name="rejectCall"> 
                    	<xsl:with-param name="errorCode" select="'2026'" /> 
                    	<xsl:with-param name="errorMessage" select="'Error Expr Regular Factura (codigo: 2026)'" /> 
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:if test='not((./cac:Item/cbc:Description,"^(?!\s*$).{1,250}"))'>
                        <xsl:call-template name="rejectCall"> 
                        	<xsl:with-param name="errorCode" select="'2027'" /> 
                        	<xsl:with-param name="errorMessage" select="'Error Expr Regular Factura (codigo: 2027)'" /> 
                        </xsl:call-template>
                    </xsl:if>
                </xsl:otherwise>
            </xsl:choose>

<!-- 			<xsl:call-template name="existAndRegexpValidateElement"> -->
<!-- 				<xsl:with-param name="errorCodeNotExist" select="'2026'" /> -->
<!-- 				<xsl:with-param name="errorCodeValidate" select="'2027'" /> -->
<!-- 				<xsl:with-param name="node" select="$descripcionDetalladaServicioPrestado" /> -->
<!-- 				<xsl:with-param name="regexp" select="'[^\n].{0,249}'" /> -->
<!-- 				<xsl:with-param name="descripcion"  -->
<!-- 					select="concat('Error en la linea: ', position(), ' ', $descripcionDetalladaServicioPrestado)"/> -->
<!-- 			</xsl:call-template> -->
			
<!-- 			<xsl:variable name="countDDSP" select="string-length(string($descripcionDetalladaServicioPrestado))" /> -->
<!-- 			<xsl:if test="($countDDSP>250)"> -->
<!-- 				<xsl:call-template name="rejectCall"> -->
<!-- 					<xsl:with-param name="errorCode" select="'2027'" /> -->
<!-- 					<xsl:with-param name="errorMessage" -->
<!-- 						select="concat('Error en la linea: ', position(), ' ', $descripcionDetalladaServicioPrestado)" /> -->
<!-- 				</xsl:call-template> -->
<!-- 			</xsl:if> -->
			
<!-- 			<xsl:call-template name="regexpValidateElementIfExist"> -->
<!-- 				<xsl:with-param name="errorCodeValidate" select="'2027'"/> -->
<!-- 				<xsl:with-param name="node" select="$descripcionDetalladaServicioPrestado"/> -->
<!-- 				<xsl:with-param name="regexp" select="'[^\s]'"/> -->
<!-- 				<xsl:with-param name="descripcion"  -->
<!-- 					select="concat('Error en la linea: ', $descripcionDetalladaServicioPrestado)"/> -->
<!-- 			</xsl:call-template> -->
			
			<!-- 24. Valor unitario por ítem -->
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2068'" />
				<xsl:with-param name="errorCodeValidate" select="'2369'" />
				<xsl:with-param name="node" select="$valorUnitarioItem" />
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $valorUnitarioItem)"/>
			</xsl:call-template>

			<!-- 25. Precio de venta unitario por item -->
			<xsl:variable name="countPrecioVentaUnitarioItem" select="count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceAmount)"/>	
			<xsl:if
				test="($countPrecioVentaUnitarioItem=0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2028'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countPrecioVentaUnitarioItem)" />
				</xsl:call-template>
			</xsl:if>

			<xsl:for-each select="./cac:PricingReference/cac:AlternativeConditionPrice">
				<!-- 25. Precio de venta unitario por item -->		
				<xsl:variable name="precioVentaUnitarioItem" select="./cbc:PriceAmount"/>	
				<!-- 25. Moneda Precio de venta unitario por item -->		
				<xsl:variable name="monedaPrecioVentaUnitarioItem" select="./cbc:PriceAmount/@currencyID"/>	
				<!-- 25. Código de precio -->		
				<xsl:variable name="codigoPrecioVentaUnitarioItem" select="./cbc:PriceTypeCode"/>	
				
				<!-- Moneda debe ser la misma en todo el documento -->	
				<xsl:if test="($monedaPrecioVentaUnitarioItem) and ($tipoMoneda!=$monedaPrecioVentaUnitarioItem)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$monedaPrecioVentaUnitarioItem)" />
					</xsl:call-template>
				</xsl:if>
				
				<!-- 25. Precio de venta unitario por item -->
				<xsl:call-template name="existValidateElementIfExist">
					<xsl:with-param name="errorCodeNotExist" select="'2028'"/>
					<xsl:with-param name="node" select="$precioVentaUnitarioItem"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $precioVentaUnitarioItem)"/>
				</xsl:call-template>		
					
				<!-- 25. Precio de venta unitario por item -->							
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2367'" />
					<xsl:with-param name="node" select="$precioVentaUnitarioItem" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $precioVentaUnitarioItem)"/>
				</xsl:call-template>
						
				<!-- 25. Código de precio -->	
				<xsl:if test="not($codigoPrecioVentaUnitarioItem='01' or $codigoPrecioVentaUnitarioItem='02')">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2410'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $codigoPrecioVentaUnitarioItem)"/>
					</xsl:call-template>
				</xsl:if>
				
				<xsl:if	test="$codigoPrecioVentaUnitarioItem='02'">
					<!-- 24. Valor unitario por ítem -->
					<xsl:if test="$valorUnitarioItem>0">
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="'2640'" />
							<xsl:with-param name="warningMessage"
								select="concat('Error en la linea: ', position(), ' ', $codigoPrecioVentaUnitarioItem, ' ',$valorUnitarioItem)"/>
						</xsl:call-template>
					</xsl:if>
					
					<!-- 26. Valor referencial unitario por ítem en operaciones no onerosas -->
					<xsl:variable name="afectacionIGVLineaVUI" select="../../cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cbc:TaxExemptionReasonCode"/>								
					<xsl:if	test="(($afectacionIGVLineaVUI='10') or ($afectacionIGVLineaVUI='20') or ($afectacionIGVLineaVUI='30')) and ($precioVentaUnitarioItem &gt; 0)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2425'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $codigoPrecioVentaUnitarioItem, ' ', $afectacionIGVLineaVUI,' ',$precioVentaUnitarioItem)"/>
						</xsl:call-template>
					</xsl:if>				
				</xsl:if>														
			</xsl:for-each>
			
			<!-- 26. Código de precio -->
			<xsl:variable name="countCodigoPrecio01" select="count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='01'])"/>
			<xsl:variable name="countCodigoPrecio02" select="count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='02'])"/>	
			<xsl:if
				test="($countCodigoPrecio01 &gt; 1) or ($countCodigoPrecio02 &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2409'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoPrecio01, ' ', $countCodigoPrecio02)" />
				</xsl:call-template>
			</xsl:if>

			<!-- 31. Código de tributo por línea -->					
			<xsl:variable name="countCodigoTributoLinea1000For" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])" />
			<xsl:variable name="countCodigoTributoLinea2000For" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000'])" />
			<xsl:variable name="countCodigoTributoLinea9999For" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9999'])" />
			<xsl:if
				test="($countCodigoTributoLinea1000For=0)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'2042'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoTributoLinea1000For)"/>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if
				test="($countCodigoTributoLinea1000For &gt; 1) or ($countCodigoTributoLinea2000For &gt; 1) or ($countCodigoTributoLinea9999For &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2355'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoTributoLinea1000For, ' ', $countCodigoTributoLinea2000For, ' ', $countCodigoTributoLinea9999For)"/>
				</xsl:call-template>
			</xsl:if>

			<!-- Afectacion al IGV por item - Sistema de ISC por item -->
			<xsl:for-each select="./cac:TaxTotal">
				<!-- 27. Monto de IGV de la línea -->		
				<xsl:variable name="montoIGVLinea" select="./cbc:TaxAmount"/>
				<!-- 27. Moneda Monto de IGV de la línea -->		
				<xsl:variable name="monedaMontoIGVLinea" select="./cbc:TaxAmount/@currencyID"/>		
				<!-- 27. Moneda TaxableAmount -->		
				<xsl:variable name="monedaTaxableAmount" select="./cac:TaxSubtotal/cbc:TaxableAmount/@currencyID"/>
				<!-- 27. SubMonto de IGV de la línea-->		
				<xsl:variable name="subTotalMontoIGVLínea" select="./cac:TaxSubtotal/cbc:TaxAmount"/>	
				<!-- 27. Moneda SubMonto de IGV de la línea -->		
				<xsl:variable name="monedaSubTotalMontoIGVLínea" select="./cac:TaxSubtotal/cbc:TaxAmount/@currencyID"/>	
				<!-- 27. Afectación al IGV por la línea -->		
				<xsl:variable name="afectacionIGVLinea" select="./cac:TaxSubtotal/cac:TaxCategory/cbc:TaxExemptionReasonCode"/>	
				<!-- 27. Código de tributo por línea -->		
				<xsl:variable name="codigoTributoLinea" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID"/>	
				<!-- 27. Nombre de tributo por línea -->		
				<xsl:variable name="nombreTributoLinea" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:Name"/>	
				<!-- 27. Código internacional tributo por línea -->		
				<xsl:variable name="codigoInternacionalTributoLinea" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:TaxTypeCode"/>	
				<!-- 28. Tipo de sistema de ISC -->		
				<xsl:variable name="tipoSistemaISC" select="./cac:TaxSubtotal/cac:TaxCategory/cbc:TierRange"/>							
				
				<!-- Moneda debe ser la misma en todo el documento -->	
				<xsl:if test="($monedaMontoIGVLinea) and ($tipoMoneda!=$monedaMontoIGVLinea)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$monedaMontoIGVLinea)" />
					</xsl:call-template>
				</xsl:if>
				
				<xsl:if test="($monedaSubTotalMontoIGVLínea) and ($tipoMoneda!=$monedaSubTotalMontoIGVLínea)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$monedaSubTotalMontoIGVLínea)" />
					</xsl:call-template>
				</xsl:if>
				
				<xsl:if test="($monedaTaxableAmount) and ($tipoMoneda!=$monedaTaxableAmount)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$monedaTaxableAmount)" />
					</xsl:call-template>
				</xsl:if>
				
				<!-- 27. Monto de IGV de la línea -->						
				<xsl:if test='$montoIGVLinea'>				
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2033'"/>
						<xsl:with-param name="node" select="$montoIGVLinea"/>
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $montoIGVLinea)"/>
					</xsl:call-template>
				
					<xsl:if test="($codigoTributoLinea='1000') and ($tipoOperacion='07') and ($subTotalMontoIGVLínea=0)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2643'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ', $tipoOperacion, ' ', $subTotalMontoIGVLínea)"/>
						</xsl:call-template>
					</xsl:if>
										
					<xsl:if test="$montoIGVLinea!=$subTotalMontoIGVLínea">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2372'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $montoIGVLinea, ' ', $subTotalMontoIGVLínea)"/>
						</xsl:call-template>
					</xsl:if>					
				</xsl:if>	

				<!-- 27. Afectación al IGV por la línea -->				
				<xsl:if test='$codigoTributoLinea=1000'>							
					<xsl:call-template name="existValidateElementIfExist">
						<xsl:with-param name="errorCodeNotExist" select="'2371'"/>
						<xsl:with-param name="node" select="$afectacionIGVLinea"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea)"/>
					</xsl:call-template>
								
					<xsl:if test="($tipoOperacion='02') and ($afectacionIGVLinea != '40')">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2642'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea)"/>
						</xsl:call-template>
					</xsl:if>
					
					<xsl:if test="($tipoOperacion='07') and ($afectacionIGVLinea != '17')">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2644'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea)"/>
						</xsl:call-template>
					</xsl:if>
					
					<xsl:if
						test="($codigoLeyenda='1002') and ($afectacionIGVLinea='10' or $afectacionIGVLinea='20' or $afectacionIGVLinea='30' or $afectacionIGVLinea='40')">
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="'4025'" />
							<xsl:with-param name="warningMessage" 
								select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea)"/>
						</xsl:call-template>
					</xsl:if>									
				</xsl:if>
				
				<!-- 27. Código de tributo por línea -->	
				<xsl:call-template name="existValidateElementIfExist">
					<xsl:with-param name="errorCodeNotExist" select="'2037'"/>
					<xsl:with-param name="node" select="$codigoTributoLinea"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea)"/>
				</xsl:call-template>
				
				<xsl:if
					test="($codigoTributoLinea='1000') and (count(./cbc:TaxAmount)=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2042'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ', count(./cbc:TaxAmount))"/>
					</xsl:call-template>
				</xsl:if>				
										
				<xsl:if test="($codigoTributoLinea!='1000')">
					<xsl:if test="($tipoOperacion='02')">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2654'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $codigoTributoLinea)"/>
						</xsl:call-template>
					</xsl:if>
				
					<xsl:if test="($tipoOperacion='07')">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2645'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $codigoTributoLinea)"/>
						</xsl:call-template>
					</xsl:if>
				</xsl:if>
										
				<!-- 27. Nombre de tributo por línea -->			
				<xsl:call-template name="existValidateElementIfExist">
					<xsl:with-param name="errorCodeNotExist" select="'2038'"/>
					<xsl:with-param name="node" select="$nombreTributoLinea"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $nombreTributoLinea)"/>
				</xsl:call-template>	
				
				<xsl:if test="($codigoTributoLinea='1000')">
					<xsl:if test="($tipoOperacion!='07') and ($nombreTributoLinea!='IGV')">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2377'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $codigoTributoLinea, ' ', $nombreTributoLinea)"/>
						</xsl:call-template>
					</xsl:if>
				
					<xsl:if test="($tipoOperacion='07') and ($nombreTributoLinea!='IVAP')">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2646'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $codigoTributoLinea, ' ', $nombreTributoLinea)"/>
						</xsl:call-template>
					</xsl:if>	
				</xsl:if>			
							
				<xsl:if test="($codigoTributoLinea='2000')">
					<!-- 28. Monto de ISC de la línea -->		
					<xsl:if test="($sumatoriaISC &gt; 0) and ($montoIGVLinea=0)">
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="'4201'" />
							<xsl:with-param name="warningMessage" 
								select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ', $sumatoriaISC, ' ', $montoIGVLinea)"/>
						</xsl:call-template>
					</xsl:if>
					
					<!-- 28. Tipo de sistema de ISC -->	
					<xsl:call-template name="existValidateElementIfExist">
						<xsl:with-param name="errorCodeNotExist" select="'2373'"/>
						<xsl:with-param name="node" select="$tipoSistemaISC"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ', $tipoSistemaISC)"/>
					</xsl:call-template>
					
					<xsl:if test="not(string($nombreTributoLinea)) or ($nombreTributoLinea!='ISC')">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2378'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ', $nombreTributoLinea)"/>
						</xsl:call-template>
					</xsl:if>
										
					<xsl:if test="not(string($codigoInternacionalTributoLinea)) or ($codigoInternacionalTributoLinea!='EXC')">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2378'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ', $codigoInternacionalTributoLinea)"/>
						</xsl:call-template>
					</xsl:if>										
				</xsl:if>
			</xsl:for-each>
			
			<!-- 29. Valor de venta por línea -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2370'"/>
				<xsl:with-param name="node" select="$valorVentaLinea"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $valorVentaLinea)"/>
			</xsl:call-template>	
			
			<xsl:if
				test="($tipoOperacion='04') and ($valorVentaLinea &lt;= 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2501'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $valorVentaLinea)"/>
				</xsl:call-template>
			</xsl:if>
			
		</xsl:for-each>

		<!-- Totales de la Factura -->

		<!-- Valida que solo exista un tag sacAdditionalInformation -->
		<xsl:if test="count($sacAdditionalInformation) &gt; 1">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2427'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', count($sacAdditionalInformation))"/>
			</xsl:call-template>
		</xsl:if>
		
		<!-- 31. Código de tipo de monto -->
<!-- 		<xsl:if -->
<!-- 			test="not(($codigoTipoMonto[text()='1000' or text()='1001' or text()='1002' or text()='1003' or text()='1004' or text()='3001']))"> -->
<!-- 			<xsl:call-template name="rejectCall"> -->
<!-- 				<xsl:with-param name="errorCode" select="'2047'" /> -->
<!-- 				<xsl:with-param name="errorMessage" -->
<!-- 					select="concat('Error en la linea: ', position())"/> -->
<!-- 			</xsl:call-template> -->
<!-- 		</xsl:if> -->
		
		<xsl:if test="(count($codigoTipoMonto[text()='1000']) = 0 and count($codigoTipoMonto[text()='1001']) = 0 and 	
				count($codigoTipoMonto[text()='1002']) = 0 and count($codigoTipoMonto[text()='1003']) = 0 and 
				count($codigoTipoMonto[text()='1004']) = 0 and count($codigoTipoMonto[text()='3001']) = 0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2047'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position())"/>
			</xsl:call-template>
		</xsl:if>
		
		<!-- 31. No deberian de haber duplicados -->
		<xsl:if test="(count($codigoTipoMonto[text()='1001']) &gt; 1 or count($codigoTipoMonto[text()='1002']) &gt; 1 or 
				count($codigoTipoMonto[text()='1003']) &gt; 1 or count($codigoTipoMonto[text()='1004']) &gt; 1 or 
				count($codigoTipoMonto[text()='3001']) &gt; 1)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2406'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position())"/>
			</xsl:call-template>
		</xsl:if>

		<xsl:variable name="countAfectacionIGVLinea10" select="count(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cbc:TaxExemptionReasonCode[text()='10'])" />
		<xsl:variable name="countAfectacionIGVLinea17" select="count(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cbc:TaxExemptionReasonCode[text()='17'])" />
		<xsl:variable name="countAfectacionIGVLinea20" select="count(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cbc:TaxExemptionReasonCode[text()='20'])"/>		
		<xsl:variable name="countAfectacionIGVLinea30" select="count(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cbc:TaxExemptionReasonCode[text()='30'])"/>
		<xsl:variable name="countAfectacionIGVLinea40" select="count(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cbc:TaxExemptionReasonCode[text()='40'])"/>		
		<xsl:for-each select="$additionalMonetaryTotal">		
			<!-- 37. Código de tipo de monto -->
			<xsl:variable name="codigoTipoMontoFor" select="./cbc:ID" />
			<!-- 37. Total valor de venta - operaciones  -->
			<xsl:variable name="totalValorVentaOperaciones" select="./cbc:PayableAmount" />
			<!-- 37. Moneda Total valor de venta - operaciones  -->
			<xsl:variable name="monedaTotalValorVentaOperaciones" select="./cbc:PayableAmount/@currencyID" />
								
			<!-- Moneda debe ser la misma en todo el documento -->	
			<xsl:if test="($codigoTipoMontoFor != '2001')">
				<xsl:if test="($monedaTotalValorVentaOperaciones) and ($tipoMoneda!=$monedaTotalValorVentaOperaciones)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$monedaTotalValorVentaOperaciones)" />
					</xsl:call-template>
				</xsl:if>				
			</xsl:if>	
							
			<!-- 37. Total valor de venta - operaciones  -->				
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2045'"/>
				<xsl:with-param name="node" select="$codigoTipoMontoFor"/>
				<xsl:with-param name="regexp" select="'^(1000|1001|1002|1003|1004|1005|2001|2002|2003|2004|2005|3001)$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $codigoTipoMontoFor)"/>
			</xsl:call-template>
								
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2043'"/>
				<xsl:with-param name="node" select="$totalValorVentaOperaciones"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $totalValorVentaOperaciones)"/>
			</xsl:call-template>
			
			<!-- 36.Sumatoria IGV  -->
			<xsl:if
				test="($tipoOperacion='07') and ($codigoTipoMontoFor!='1001') and ($totalValorVentaOperaciones &gt; 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2648'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $codigoTipoMontoFor, ' ', $totalValorVentaOperaciones)"/>
				</xsl:call-template>
			</xsl:if>
			
			<!-- 31. Total valor de venta - operaciones gravadas  -->
			<xsl:if test="($codigoTipoMontoFor='1001')">								
				<xsl:if
					test="($tipoOperacion='07') and ($totalValorVentaOperaciones=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2649'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $codigoTipoMontoFor, ' ', $totalValorVentaOperaciones)"/>
					</xsl:call-template>
				</xsl:if>
				
				<xsl:if test="($countAfectacionIGVLinea10=0) and ($countAfectacionIGVLinea17=0)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4016'" />
						<xsl:with-param name="warningMessage" 
							select="concat('Error en la linea: ', position(), ' ', $codigoTipoMontoFor, ' ', $countAfectacionIGVLinea10)"/>
					</xsl:call-template>
				</xsl:if>
				
				<xsl:if test="($countAfectacionIGVLinea10 &gt; 0) and ($totalValorVentaOperaciones=0)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4016'" />
						<xsl:with-param name="warningMessage" 
							select="concat('Error en la linea: ', position(), ' ', $codigoTipoMontoFor, ' ', $countAfectacionIGVLinea10, ' ', $totalValorVentaOperaciones)"/>
					</xsl:call-template>
				</xsl:if>		
			</xsl:if>
			
			<!-- 32. Total valor de venta - operaciones inafectas  -->
			<xsl:if test="($codigoTipoMontoFor='1002')">
				<xsl:if test="(($countAfectacionIGVLinea30 &gt; 0) or ($countAfectacionIGVLinea40 &gt; 0))
					and ($totalValorVentaOperaciones=0)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4017'" />
						<xsl:with-param name="warningMessage" 
							select="concat('Error en la linea: ', position(), ' ', $codigoTipoMontoFor, ' ', number($totalValorVentaOperaciones), ' ', $countAfectacionIGVLinea30, ' ', $countAfectacionIGVLinea40)"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:if>

			<!-- 33. Total valor de venta - operaciones exoneradas  -->
			<xsl:if test="($codigoTipoMontoFor='1003')">
				<xsl:if test="($countAfectacionIGVLinea20 &gt; 0) and ($totalValorVentaOperaciones=0)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4018'" />
						<xsl:with-param name="warningMessage" 
							select="concat('Error en la linea: ', position(), ' ', $codigoTipoMontoFor, ' ', $totalValorVentaOperaciones)"/>
					</xsl:call-template>
				</xsl:if>
				
				<xsl:if test="($codigoLeyenda='2001') and ($totalValorVentaOperaciones=0)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4022'" />
						<xsl:with-param name="warningMessage" 
							select="concat('Error en la linea: ', position(), ' ', $codigoTipoMontoFor, ' ', $codigoLeyenda, ' ', $totalValorVentaOperaciones)"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:if>			

			<!-- 34. Total Valor de Venta - Operaciones gratuitas  -->	
			<xsl:if test="($codigoTipoMontoFor='1004')">
				<xsl:variable name="countCodigoPrecio02" select="count($codigoPrecio[text()='02'])"/>
				<xsl:if test="($countCodigoPrecio02 &gt; 0) and ($totalValorVentaOperaciones=0)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'2641'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), ' ', $codigoTipoMontoFor, ' ', $countCodigoPrecio02,' ', $totalValorVentaOperaciones)"/>
					</xsl:call-template>
				</xsl:if>
				
				<xsl:if test="($codigoLeyenda='1002') and ($totalValorVentaOperaciones=0)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'2416'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), ' ', $codigoTipoMontoFor, ' ', $totalValorVentaOperaciones)"/>
					</xsl:call-template>
				</xsl:if>								
			</xsl:if>			
		</xsl:for-each>

		<!-- Sumatoria IGV / ISC / Otros Tributos -->	
		<xsl:variable name="countCodigoTributoLinea2000" select="count(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000'])"/>					
		<xsl:for-each select="cac:TaxTotal">
			<!-- 36. Sumatoria IGV -->
			<xsl:variable name="sumatoriaIGV" select="./cbc:TaxAmount" />
			<!-- 36. Moneda Sumatoria IGV -->
			<xsl:variable name="monedaSumatoriaIGV" select="./cbc:TaxAmount/@currencyID" />
			<!-- 36. SubTotal IGV -->
			<xsl:variable name="subTotalIGV" select="./cac:TaxSubtotal/cbc:TaxAmount" />
			<!-- 36. Moneda SubTotal IGV -->
			<xsl:variable name="monedaSubTotalIGV" select="./cac:TaxSubtotal/cbc:TaxAmount/@currencyID" />
			<!-- 36. Código de tributo -->
			<xsl:variable name="codigoTributo" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID" />
			<!-- 36. Nombre de tributo -->
			<xsl:variable name="nombreTributo" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:Name" />
			<!-- 36. Código internacional tributo -->
			<xsl:variable name="codigoInternacionalTributo" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:TaxTypeCode" />

			<!-- Moneda debe ser la misma en todo el documento -->	
			<xsl:if test="($monedaSumatoriaIGV) and ($tipoMoneda!=$monedaSumatoriaIGV)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$monedaSumatoriaIGV)" />
				</xsl:call-template>
			</xsl:if>
				
			<xsl:if test="($monedaSubTotalIGV) and ($tipoMoneda!=$monedaSubTotalIGV)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$monedaSubTotalIGV)" />
				</xsl:call-template>
			</xsl:if>

			<!-- 36. Sumatoria IGV -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2048'"/>
				<xsl:with-param name="node" select="$sumatoriaIGV"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $sumatoriaIGV)"/>
			</xsl:call-template>
			
			<!-- 36. SubTotal IGV -->
			<xsl:call-template name="existValidateElementIfExist">
				<xsl:with-param name="errorCodeNotExist" select="'2061'"/>
				<xsl:with-param name="node" select="$subTotalIGV"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $subTotalIGV)"/>
			</xsl:call-template>
						
			<!-- 36. Código de tributo -->
			<xsl:call-template name="existValidateElementIfExist">
				<xsl:with-param name="errorCodeNotExist" select="'2052'"/>
				<xsl:with-param name="node" select="$codigoTributo"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $codigoTributo)"/>
			</xsl:call-template>
			
			<xsl:if test="($codigoTributo='1000') and ($tipoOperacion='04') and ($codigoTipoMonto[text()='1001']) and ($sumatoriaIGV=0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2502'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $tipoOperacion)"/>
				</xsl:call-template>
			</xsl:if>
						
			<!-- 36. SubTotal IGV -->
			<xsl:if test="($sumatoriaIGV != $subTotalIGV)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2061'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $sumatoriaIGV, ' ', $subTotalIGV)"/>
				</xsl:call-template>
			</xsl:if>
			
			<!-- 36. Nombre de tributo -->
			<xsl:call-template name="existValidateElementIfExist">
				<xsl:with-param name="errorCodeNotExist" select="'2054'"/>
				<xsl:with-param name="node" select="$nombreTributo"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $nombreTributo)"/>
			</xsl:call-template>
			
			<xsl:if test="($codigoTributo='1000') and not(($nombreTributo='IGV') or ($nombreTributo='IVAP'))">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2057'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $nombreTributo)"/>
				</xsl:call-template>
			</xsl:if>
						
			<xsl:if test="($codigoTributo='2000')">
				<!-- 37. Sumatoria ISC -->
				<xsl:if test="($countCodigoTributoLinea2000 &gt; 0) and ($montoISCLinea &gt; 0) and ($sumatoriaIGV=0)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4020'" />
						<xsl:with-param name="warningMessage" 
							select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $countCodigoTributoLinea2000, ' ', $montoISCLinea, ' ', $sumatoriaIGV)"/>
					</xsl:call-template>
				</xsl:if>			
			
				<!-- 37. Nombre de tributo -->			
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2058'" />
					<xsl:with-param name="errorCodeValidate" select="'2058'" />
					<xsl:with-param name="node" select="$nombreTributo" />
					<xsl:with-param name="regexp" select="'^(ISC)$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $nombreTributo)"/>
				</xsl:call-template>
				
				<!-- 44. Código internacional tributo -->				
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2058'" />
					<xsl:with-param name="errorCodeValidate" select="'2058'" />
					<xsl:with-param name="node" select="$codigoInternacionalTributo" />
					<xsl:with-param name="regexp" select="'^(EXC)$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $codigoInternacionalTributo)"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>

		<!-- 36. Código de tributo -->		
		<xsl:variable name="countCodigoTributo1000" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])" />
		<xsl:if test="($countCodigoTributo1000 &gt; 1)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2352'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ',$countCodigoTributo1000)"/>
			</xsl:call-template>
		</xsl:if>
		
		<!-- LegalMonetaryTotal -->
		<xsl:if test="cac:LegalMonetaryTotal/cbc:LineExtensionAmount">			
			<!-- Moneda debe ser la misma en todo el documento -->	
			<xsl:if test="($tipoMoneda!=cac:LegalMonetaryTotal/cbc:LineExtensionAmount/@currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',cac:LegalMonetaryTotal/cbc:LineExtensionAmount/@currencyID)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
		
		<xsl:if test="cac:LegalMonetaryTotal/cbc:TaxExclusiveAmount">			
			<!-- Moneda debe ser la misma en todo el documento -->	
			<xsl:if test="($tipoMoneda!=cac:LegalMonetaryTotal/cbc:TaxExclusiveAmount/@currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',cac:LegalMonetaryTotal/cbc:TaxExclusiveAmount/@currencyID)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
		
		<!-- 39. Descuentos Globales -->		
		<xsl:if test="cac:LegalMonetaryTotal/cbc:AllowanceTotalAmount">
			<xsl:if
				test='not(fn:matches(cac:LegalMonetaryTotal/cbc:AllowanceTotalAmount,"^[0-9]{1,12}(\.[0-9]{1,2})?$"))'>
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2065'" />
					<xsl:with-param name="errorMessage"
						select="'Error Expr Regular Factura (codigo: 2065)'" />
				</xsl:call-template>
			</xsl:if>
			
			<!-- Moneda debe ser la misma en todo el documento -->	
			<xsl:if test="($tipoMoneda!=cac:LegalMonetaryTotal/cbc:AllowanceTotalAmount/@currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',cac:LegalMonetaryTotal/cbc:AllowanceTotalAmount/@currencyID)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
		
		<!-- 40. Sumatoria otros Cargos -->
		<xsl:if test="cac:LegalMonetaryTotal/cbc:ChargeTotalAmount">
			<xsl:if
				test='not(fn:matches(cac:LegalMonetaryTotal/cbc:ChargeTotalAmount,"^[0-9]{1,12}(\.[0-9]{1,2})?$"))'>
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2064'" />
					<xsl:with-param name="errorMessage"
						select="'Error Expr Regular Factura (codigo: 2064)'" />
				</xsl:call-template>
			</xsl:if>
			
			<!-- Moneda debe ser la misma en todo el documento -->	
			<xsl:if test="($tipoMoneda!=cac:LegalMonetaryTotal/cbc:ChargeTotalAmount/@currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',cac:LegalMonetaryTotal/cbc:ChargeTotalAmount/@currencyID)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
		
		<!-- 41. Importe total -->
		<xsl:if
			test='not(fn:matches(cac:LegalMonetaryTotal/cbc:PayableAmount,"^[0-9]{1,12}(\.[0-9]{1,2})?$"))'>
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2062'" />
				<xsl:with-param name="errorMessage"
					select="'Error Expr Regular Factura (codigo: 2062)'" />
			</xsl:call-template>
		</xsl:if>	
		
		<xsl:if
			test="$tipoOperacion[text() = '04'] and $codigoTipoMonto[text()='1001'] and (cac:TaxTotal/cbc:TaxAmount[text() &lt;=0] or cac:LegalMonetaryTotal/cbc:PayableAmount[text() &lt;=0])">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2502'" />
				<xsl:with-param name="errorMessage"
					select="'Error Expr Regular Factura (codigo: 2502)'" />
			</xsl:call-template>
		</xsl:if>		

		<!-- Moneda debe ser la misma en todo el documento -->	
		<xsl:if test="($tipoMoneda!=cac:LegalMonetaryTotal/cbc:PayableAmount/@currencyID)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2071'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',cac:LegalMonetaryTotal/cbc:PayableAmount/@currencyID)" />
			</xsl:call-template>
		</xsl:if>

		<xsl:variable name="sumImporteTotal" select="$sumTVVOGravadas + $sumTVVOInafectas + $sumTVVOExoneradas + $sumIGV + $sumatoriaISC + $sumatoriaOtrosTributos + $sumatoriaOtrosCargos" />
		<xsl:variable name="difSumImporteTotal" select="$sumImporteTotal - $importeTotal" />
		<xsl:if test="($difSumImporteTotal &lt; -1) or ($difSumImporteTotal &gt; 1)">
			<xsl:call-template name="addWarning">
				<xsl:with-param name="warningCode" select="'4027'" />
				<xsl:with-param name="warningMessage"
					select="concat('Error en la linea: ', $difSumImporteTotal,'-', $importeTotal,'-',$sumImporteTotal, '-', $sumTVVOGravadas,'-', $sumTVVOInafectas,'-', $sumTVVOExoneradas,'-', $sumIGV,'-', $sumatoriaISC,'-', $sumatoriaOtrosTributos,'-', $sumatoriaOtrosCargos)"/>
			</xsl:call-template>
		</xsl:if>			
		
		<!-- Información Adicional  - Anticipos -->		
		<xsl:for-each select="cac:PrepaidPayment">
			<!-- 42. Serie y Número de documento que se realizo el anticipo -->
			<xsl:variable name="serieNumeroDocumentoRealizoAnticipo" select="./cbc:ID" />
			<!-- 42. Tipo de comprobante que se realizo el anticipo -->
			<xsl:variable name="tipoComprobanteRealizoAnticipo" select="./cbc:ID/@schemeID" />
			<!-- 42. Monto anticipado -->
			<xsl:variable name="montoAnticipado" select="./cbc:PaidAmount" />
			<!-- 42. Moneda Monto anticipado -->
			<xsl:variable name="monedaMontoAnticipado" select="./cbc:PaidAmount/@currencyID" />
			<!-- 42. Número de documento del emisor del anticipo -->
			<xsl:variable name="numeroDocumentoEmisorAnticipo" select="./cbc:InstructionID" />
			<!-- 42. Tipo de documento del emisor del anticipo -->
			<xsl:variable name="tipoDocumentoEmisorAnticipo" select="./cbc:InstructionID/@schemeID" />
			
			<!-- Moneda debe ser la misma en todo el documento -->	
			<xsl:if test="($monedaMontoAnticipado) and ($tipoMoneda!=$monedaMontoAnticipado)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$monedaMontoAnticipado)" />
				</xsl:call-template>
			</xsl:if>
			
			<!-- 42. Serie y Número de documento que se realizo el anticipo -->
			<xsl:if test="($montoAnticipado) and not($serieNumeroDocumentoRealizoAnticipo)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'2504'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', position(), ' ', $montoAnticipado, ' ', $serieNumeroDocumentoRealizoAnticipo)"/>
				</xsl:call-template>
			</xsl:if>
			
			<!-- 42. Serie y Número de documento que se realizo el anticipo -->				
			<xsl:if test="($tipoDocumentoEmisorAnticipo)">						
				<xsl:if test="($tipoComprobanteRealizoAnticipo='02')">
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2521'"/>
						<xsl:with-param name="node" select="$serieNumeroDocumentoRealizoAnticipo"/>
						<xsl:with-param name="regexp" select="'^[F][A-Z0-9]{3}-[0-9]{1,8}$|^(E001)-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'"/>
						<xsl:with-param name="isError" select="false()"/> 
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $tipoComprobanteRealizoAnticipo, ' ', $serieNumeroDocumentoRealizoAnticipo)"/>
					</xsl:call-template>
				</xsl:if>
				
				<xsl:if test="($tipoComprobanteRealizoAnticipo='03')">
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2521'"/>
						<xsl:with-param name="node" select="$serieNumeroDocumentoRealizoAnticipo"/>
						<xsl:with-param name="regexp" select="'^[B][A-Z0-9]{3}-[0-9]{1,8}$|^(EB01)-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'"/>
						<xsl:with-param name="isError" select="false()"/> 
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $tipoComprobanteRealizoAnticipo, ' ', $serieNumeroDocumentoRealizoAnticipo)"/>
					</xsl:call-template>
				</xsl:if>	
			</xsl:if>
			
			<!-- 42. Tipo de comprobante que se realizo el anticipo -->
			<xsl:if test="($tipoComprobanteRealizoAnticipo) and not($tipoComprobanteRealizoAnticipo='02' or $tipoComprobanteRealizoAnticipo='03') ">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'2505'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoComprobanteRealizoAnticipo)"/>
				</xsl:call-template>
			</xsl:if>
			
			<!-- 42. Monto anticipado -->
			<xsl:if test="($montoAnticipado) and ($montoAnticipado &lt;= 0) ">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'2503'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', position(), ' ', number($montoAnticipado))"/>
				</xsl:call-template>
			</xsl:if>
			
			<!-- 42. Número de documento del emisor del anticipo -->	
			<xsl:if test="($tipoDocumentoEmisorAnticipo)">								
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2529'"/>
					<xsl:with-param name="node" select="$numeroDocumentoEmisorAnticipo"/>
					<xsl:with-param name="regexp" select="'^[0-9]{11}$'"/>
					<xsl:with-param name="isError" select="false()"/> 
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $numeroDocumentoEmisorAnticipo)"/>
				</xsl:call-template>
			</xsl:if>
							
			<!-- 42. Tipo de documento del emisor del anticipo -->	
			<xsl:if test="($tipoDocumentoEmisorAnticipo) and ($tipoDocumentoEmisorAnticipo!= '6') ">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'2520'" />
					<xsl:with-param name="warningMessage"
						select="concat('documento: ', position())" />
				</xsl:call-template>
			</xsl:if>			
		</xsl:for-each>
		
		<!-- 43. Total Anticipos -->	
		<xsl:variable name="sumaMontoAnticipado" select="sum(cac:PrepaidPayment/cbc:PaidAmount)" />
		<xsl:variable name="countTipoComproRealizoAnticipado02" select="count(key('by-prepaidPayment-schemeID', '02'))" /> 
		<xsl:if test="($countTipoComproRealizoAnticipado02 &gt; 0) and ($totalAnticipos!=$sumaMontoAnticipado)" >
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2509'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' - ', $sumaMontoAnticipado, ' - ', $totalAnticipos, ' - ', $countTipoComproRealizoAnticipado02)"/>
			</xsl:call-template>
		</xsl:if>
					
		<xsl:if test="($countTipoComproRealizoAnticipado02=0) and ($totalAnticipos!=0)" >
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2508'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' - ', $sumaMontoAnticipado, ' - ', $totalAnticipos, ' - ', $countTipoComproRealizoAnticipado02)"/>
			</xsl:call-template>
		</xsl:if>
		
		<!-- Información Adicional -->		
		<!-- Leyendas -->		
		<!-- 46. Código de leyenda -->	
		<xsl:variable name="AdditionalPropertyId1000GreaterThan1" 
			select="count($sacAdditionalInformation/sac:AdditionalProperty/cbc:ID[text()='1000']) &gt; 1" />
		<xsl:variable name="AdditionalPropertyId1001GreaterThan1"
			select="count($sacAdditionalInformation/sac:AdditionalProperty/cbc:ID[text()='1001']) &gt; 1" />
		<xsl:variable name="AdditionalPropertyId1002GreaterThan1"
			select="count($sacAdditionalInformation/sac:AdditionalProperty/cbc:ID[text()='1002']) &gt; 1" />
		<xsl:variable name="AdditionalPropertyId2000GreaterThan1"
			select="count($sacAdditionalInformation/sac:AdditionalProperty/cbc:ID[text()='2000']) &gt; 1" />
		<xsl:variable name="AdditionalPropertyId2001GreaterThan1"
			select="count($sacAdditionalInformation/sac:AdditionalProperty/cbc:ID[text()='2001']) &gt; 1" />
		<xsl:variable name="AdditionalPropertyId2002GreaterThan1"
			select="count($sacAdditionalInformation/sac:AdditionalProperty/cbc:ID[text()='2002']) &gt; 1" />
		<xsl:variable name="AdditionalPropertyId2003GreaterThan1"
			select="count($sacAdditionalInformation/sac:AdditionalProperty/cbc:ID[text()='2003']) &gt; 1" />
		<xsl:if
			test="$AdditionalPropertyId1000GreaterThan1 or $AdditionalPropertyId1001GreaterThan1 
			or $AdditionalPropertyId1002GreaterThan1 or $AdditionalPropertyId2000GreaterThan1 
			or $AdditionalPropertyId2001GreaterThan1 or $AdditionalPropertyId2002GreaterThan1 
			or $AdditionalPropertyId2003GreaterThan1">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2407'" />
				<xsl:with-param name="errorMessage"
					select="'Error Expr Regular Factura (codigo: 2407)'" />
			</xsl:call-template>
		</xsl:if>
		
		<xsl:for-each select="$sacAdditionalInformation/sac:AdditionalProperty">
			<!-- 46. Código de leyenda -->
			<xsl:variable name="codigoLeyendaFor" select="./cbc:ID" />
			<!-- 46. Descripción de leyenda -->
			<xsl:variable name="descripcionLeyendaFor" select="./cbc:Value" />
			
			<!-- 46. Código de leyenda -->			
			<xsl:if test="($codigoLeyendaFor)">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2366'"/>
					<xsl:with-param name="node" select="$codigoLeyendaFor"/>
					<xsl:with-param name="regexp" select="'^[^\s]{4}$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codigoLeyendaFor)"/>
				</xsl:call-template>
			</xsl:if>
			
			<!-- 46. Descripción de leyenda -->			
			<xsl:if test="($descripcionLeyendaFor)">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2066'"/>
					<xsl:with-param name="node" select="$descripcionLeyendaFor"/>
					<xsl:with-param name="regexp" select="'^[^\n].{0,99}$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $descripcionLeyendaFor)"/>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if test="fn:starts-with($descripcionLeyendaFor, ' ')">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2066'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $descripcionLeyendaFor)"/>
				</xsl:call-template>
			</xsl:if>
			
		</xsl:for-each>
			
		<!-- 53. Código de leyenda -->	
		<xsl:variable name="countCodigoLeyenda2007"
			select="count(ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/sac:AdditionalInformation/sac:AdditionalProperty/cbc:ID[text()='2007'])" />
		<xsl:if
			test="($tipoOperacion='07') and ($countCodigoLeyenda2007=0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2651'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoLeyenda2007)"/>
			</xsl:call-template>
		</xsl:if>
		
		<xsl:copy-of select="." />
		
	</xsl:template>
</xsl:stylesheet>
