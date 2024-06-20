<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet  version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" 
	xmlns:regexp="http://exslt.org/regular-expressions"
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
	
	<!-- key Documentos Relacionados Duplicados -->
	<xsl:key name="by-document-despatch-reference" match="cac:DespatchDocumentReference"
		use="concat(cbc:DocumentTypeCode,' ', cbc:ID)" />

	<xsl:key name="by-document-additional-reference" match="cac:AdditionalDocumentReference"
		use="concat(cbc:DocumentTypeCode,' ', cbc:ID)" />

	<xsl:key name="by-invoiceLine-id" match="*[local-name()='DebitNote']/cac:DebitNoteLine" use="number(cbc:ID)" />

	<xsl:key name="by-document-billing-reference" match="cac:BillingReference"
		use="concat(cac:InvoiceDocumentReference/cbc:DocumentTypeCode,' ', cac:InvoiceDocumentReference/cbc:ID)" />
		
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
		<!-- 6. Numeración, conformada por serie y número correlativo -->
		<xsl:variable name="serieNumeroDocumentoAfectado" select="cac:DiscrepancyResponse" />	
		<xsl:variable name="serieNumeroDocumentoAfectadoID" select="$serieNumeroDocumentoAfectado/cbc:ReferenceID" />		
		<!-- 6. Código de tipo de nota de débito -->
		<xsl:variable name="codigoTipoNotaDebito" select="$serieNumeroDocumentoAfectado/cbc:ResponseCode" />	
		<!-- 7. Tipo de moneda -->
		<xsl:variable name="tipoMoneda" select="cbc:DocumentCurrencyCode" />
		
		<xsl:variable name="currentdate" select="fn:current-date()"></xsl:variable>				

		<!-- Datos del Emisor Electrónico -->			
		<xsl:variable name="emisor" select="cac:AccountingSupplierParty"/>		
		<!-- Mandatorio -->
		<!-- 9. Número de RUC -->
		<xsl:variable name="emisorNumeroDocumento" select="$emisor/cbc:CustomerAssignedAccountID"/>	
		<!-- Mandatorio -->
		<!-- 9. Tipo de documento de identidad del emisor -->
		<xsl:variable name="emisorTipoDocumento" select="$emisor/cbc:AdditionalAccountID"/>			
		<!-- Opcional -->
		<!-- 11. Apellidos y nombres, denominación o razón social -->
		<xsl:variable name="emisorRazonSocial" select="$emisor/cac:Party/cac:PartyLegalEntity/cbc:RegistrationName"/>

		<!-- Datos del Cliente o receptor -->			
		<xsl:variable name="cliente" select="cac:AccountingCustomerParty"/>		
		<!-- Mandatorio -->
		<!-- 13. Número de documento de identidad del adquirente o usuario -->
		<xsl:variable name="clienteNumeroDocumento" select="$cliente/cbc:CustomerAssignedAccountID"/>	
		<!-- Mandatorio -->
		<!-- 13. Tipo de documento de identidad del adquirente o usuario -->
		<xsl:variable name="clienteTipoDocumento" select="$cliente/cbc:AdditionalAccountID"/>			
		<!-- Opcional -->
		<!-- 14. Apellidos y nombres, denominación o razón social -->
		<xsl:variable name="clienteRazonSocial" select="$cliente/cac:Party/cac:PartyLegalEntity/cbc:RegistrationName"/>
				
		<!-- Datos del detalle o Ítem de la Factura -->		
		<!-- 19. Motivo o Sustento -->	
		<xsl:variable name="motivoSustento" select="cac:DiscrepancyResponse/cbc:Description"/>			
		
		<!-- EXT -->
		<xsl:variable name="sacAdditionalInformation" select="ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/sac:AdditionalInformation" />		
		<xsl:variable name="additionalMonetaryTotal" select="$sacAdditionalInformation/sac:AdditionalMonetaryTotal" />
		<!-- 37. Código de tipo de monto -->
		<xsl:variable name="additionalMonetaryTotalID" select="$additionalMonetaryTotal/cbc:ID" />
		<!-- 39. Sumatoria otros Cargos -->
		<xsl:variable name="sumatoriaOtrosCargos" select="cac:RequestedMonetaryTotal/cbc:ChargeTotalAmount" />
		<!-- 40. Importe total -->
		<xsl:variable name="importeTotal" select="cac:RequestedMonetaryTotal/cbc:PayableAmount" />
		
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
			<xsl:with-param name="regexp" select="'^[B][A-Z0-9]{3}-[0-9]{1,8}$|^[F][A-Z0-9]{3}-[0-9]{1,8}$|^[S][A-Z0-9]{3}-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'"/>
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
				
		<!-- 6. Numeración, conformada por serie y número correlativo -->
		<xsl:variable name="countSerieNumeroDocumentoAfectado" select="count($serieNumeroDocumentoAfectado)" />
		<xsl:if test="($countSerieNumeroDocumentoAfectado>1)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2415'" />
				<xsl:with-param name="errorMessage" 
					select="concat('Error en la linea: ', $countSerieNumeroDocumentoAfectado)" />
			</xsl:call-template>
		</xsl:if>
		
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'2171'"/>
			<xsl:with-param name="node" select="$serieNumeroDocumentoAfectadoID"/>
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $serieNumeroDocumentoAfectadoID)"/>
		</xsl:call-template>
		
		<!-- 6. Código de tipo de nota de débito -->
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'2173'"/>
			<xsl:with-param name="node" select="$codigoTipoNotaDebito"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $codigoTipoNotaDebito)"/>
		</xsl:call-template>
		
		<xsl:if test="($codigoTipoNotaDebito!='03')">
			<xsl:if test="(substring($cbcID, 1, 1)='F')">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2170'" />
					<xsl:with-param name="node" select="$serieNumeroDocumentoAfectadoID" />
					<xsl:with-param name="regexp" select="'^[F][A-Z0-9]{3}-[0-9]{1,8}$|^(E001)-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $serieNumeroDocumentoAfectadoID, ' ', $cbcID)"/>
				</xsl:call-template>
			</xsl:if>	
				
			<xsl:if test="(substring($cbcID, 1, 1)='B')">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2170'" />
					<xsl:with-param name="node" select="$serieNumeroDocumentoAfectadoID" />
					<xsl:with-param name="regexp" select="'^[B][A-Z0-9]{3}-[0-9]{1,8}$|(EB01)-[0-9]{1,8}|^[0-9]{1,4}-[0-9]{1,8}$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $serieNumeroDocumentoAfectadoID, ' ', $cbcID)"/>
				</xsl:call-template>
			</xsl:if>	
			
			<xsl:if test="(substring($cbcID, 1, 1)='S')">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2170'" />
					<xsl:with-param name="node" select="$serieNumeroDocumentoAfectadoID" />
					<xsl:with-param name="regexp" select="'^[S][A-Z0-9]{3}-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $serieNumeroDocumentoAfectadoID, ' ', $cbcID)"/>
				</xsl:call-template>
			</xsl:if>	
		</xsl:if>	

		<!-- 7. Tipo de moneda en la cual se emite la factura electronica -->		
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'2070'"/>
			<xsl:with-param name="node" select="$tipoMoneda"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoMoneda)"/>
		</xsl:call-template>		
		
		<xsl:if
			test="(cbc:DocumentCurrencyCode!=cac:TaxTotal/cbc:TaxAmount/@currencyID) or (cbc:DocumentCurrencyCode!=cac:TaxTotal/cac:TaxSubtotal/cbc:TaxAmount/@currencyID) or (cbc:DocumentCurrencyCode!=cac:LegalMonetaryTotal/cbc:ChargeTotalAmount/@currencyID) or (cbc:DocumentCurrencyCode!=cac:DebitNoteLine/cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceAmount/@currencyID) or (cbc:DocumentCurrencyCode!=cac:DebitNoteLine/cac:Price/cbc:PriceAmount/@currencyID) or (cbc:DocumentCurrencyCode!=cac:DebitNoteLine/cbc:LineExtensionAmount/@currencyID) or (cbc:DocumentCurrencyCode!=cac:DebitNoteLine/cac:TaxTotal/cbc:TaxAmount/@currencyID) or (cbc:DocumentCurrencyCode!=cac:DebitNoteLine/cac:TaxTotal/cac:TaxSubtotal/cbc:TaxAmount/@currencyID)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2071'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $tipoMoneda)"/>
			</xsl:call-template>
		</xsl:if>
		
		<xsl:if
			test="(cbc:DocumentCurrencyCode!=ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/sac:AdditionalInformation/sac:AdditionalMonetaryTotal/cbc:PayableAmount/@currencyID)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2071'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $tipoMoneda)"/>
			</xsl:call-template>
		</xsl:if>
		
		
		<!-- Datos del ciente o receptor -->
		
		<!-- 10. Numero de RUC del nombre del archivo no coincide con el consignado en el contenido del archivo XML -->
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

		<!-- 10. Tipo de documento de identidad del emisor - RUC -->
		<xsl:variable name="countEmisorTipoDocumento" select="count(cac:AccountingSupplierParty/cbc:AdditionalAccountID)" />
		<xsl:if test="$countEmisorTipoDocumento>1">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2362'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $countEmisorTipoDocumento)"/>
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
			<xsl:with-param name="regexp" select="'^[^\n].{1,1000}$'"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $emisorRazonSocial)"/>
		</xsl:call-template>
				
		<!-- Datos del ciente o receptor -->	
		<!-- 13. Valida que el tipo de documento del adquiriente exista y sea solo uno -->		
		<xsl:variable name="countClienteTipoDocumento" select="count(cac:AccountingCustomerParty/cbc:AdditionalAccountID)"/>	
		<xsl:if 
			test="$countClienteTipoDocumento>1">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2363'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $countClienteTipoDocumento)"/>
			</xsl:call-template>
		</xsl:if>
				
		<!-- 13. Valida que el tipo de documento del adquiriente exista -->
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'2015'"/>
			<xsl:with-param name="node" select="$clienteTipoDocumento"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $clienteTipoDocumento)"/>
		</xsl:call-template>

		<!-- 13. Número de documento de identidad del adquirente o usuario -->
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'2014'"/>
			<xsl:with-param name="node" select="$clienteNumeroDocumento"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $clienteNumeroDocumento)"/>
		</xsl:call-template>
		
		<xsl:if test="$clienteTipoDocumento='6'">
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2017'"/>
				<xsl:with-param name="node" select="$clienteNumeroDocumento"/>
				<xsl:with-param name="regexp" select="'^[0-9]{11}$|^[-]{1}$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $clienteNumeroDocumento)"/>
			</xsl:call-template>				
		</xsl:if>		
				
		<!-- 13.Tipo de documento de identidad del adquirente o usuario -->
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'2015'"/>
			<xsl:with-param name="node" select="$clienteTipoDocumento"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $clienteTipoDocumento)"/>
		</xsl:call-template>
		
		<xsl:variable name="countAfectacionIGVLinea40" select="count(cac:DebitNoteLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cbc:TaxExemptionReasonCode[text()='40'])" />
		<xsl:if test="($countAfectacionIGVLinea40>0) or (substring($cbcID, 1, 1)='B')  or (substring($cbcID, 1, 1)='S')">
<!-- 			<xsl:call-template name="findElementInCatalog"> -->
<!-- 				<xsl:with-param name="catalogo" select="'06'" /> -->
<!-- 				<xsl:with-param name="idCatalogo" select="$clienteTipoDocumento" /> -->
<!-- 				<xsl:with-param name="errorCodeValidate" select="'2016'" /> -->
<!-- 				<xsl:with-param name="isError" select="false()"/>  -->
<!-- 			</xsl:call-template> 		 -->

			<xsl:if test="not(($clienteTipoDocumento='0') or ($clienteTipoDocumento='1') or ($clienteTipoDocumento='4') or
				($clienteTipoDocumento='6') or ($clienteTipoDocumento='7') or ($clienteTipoDocumento='A') or ($clienteTipoDocumento='-'))">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2016'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $clienteTipoDocumento)"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:if>		
			
		<!-- 13. Valida que el tipo de documento del adquiriente exista y sea solo uno -->		
		<xsl:variable name="countAccountingCustomerPartyAdditionalAccountID" select="count(cac:AccountingCustomerParty/cbc:AdditionalAccountID)"/>	
		<xsl:if 
			test="$countAccountingCustomerPartyAdditionalAccountID>1">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2363'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $countAccountingCustomerPartyAdditionalAccountID)"/>
			</xsl:call-template>
		</xsl:if>
		
		
		<!-- 14. Apellidos y nombres, denominación o razón social del adquirente o usuario --> 
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2021'" />
			<xsl:with-param name="errorCodeValidate" select="'2022'" />
			<xsl:with-param name="node" select="$clienteRazonSocial" />			
<!-- 			<xsl:with-param name="regexp" select="'[^$].{1000}|[\n]'" /> -->
			<xsl:with-param name="regexp" select="'^[^\n].{2,1000}$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $clienteRazonSocial)"/>
		</xsl:call-template>
				
		<!-- Datos del documento que se modifica  -->	
		<xsl:for-each select="cac:BillingReference">
			<!-- 15. Serie y número del documento que modifica -->	
			<xsl:variable name="serieNumeroDocumentoModifica" select="./cac:InvoiceDocumentReference/cbc:ID"/>			
			<!-- 16. Tipo de documento del documento que modifica -->	
			<xsl:variable name="tipoDocumentoDelDocumentoModifica" select="./cac:InvoiceDocumentReference/cbc:DocumentTypeCode"/>			
		
			<!-- 15. Serie y número del documento que modifica -->	
			<xsl:if
				test="(substring($cbcID, 1, 1)='F') and ($tipoDocumentoDelDocumentoModifica='01')">
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2205'" />
					<xsl:with-param name="errorCodeValidate" select="'2205'"/>
					<xsl:with-param name="node" select="$serieNumeroDocumentoModifica"/>
					<xsl:with-param name="regexp" select="'^[F][A-Z0-9]{3}-[0-9]{1,8}$|^(E001)-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $tipoDocumentoDelDocumentoModifica,' ', $serieNumeroDocumentoModifica)"/>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if
				test="(substring($cbcID, 1, 1)='B') and ($tipoDocumentoDelDocumentoModifica='03')">
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2205'" />
					<xsl:with-param name="errorCodeValidate" select="'2205'"/>
					<xsl:with-param name="node" select="$serieNumeroDocumentoModifica"/>
					<xsl:with-param name="regexp" select="'^[B][A-Z0-9]{3}-[0-9]{1,8}$|^(EB01)-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $tipoDocumentoDelDocumentoModifica,' ', $serieNumeroDocumentoModifica)"/>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if
				test="($tipoDocumentoDelDocumentoModifica='12')">
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2205'" />
					<xsl:with-param name="errorCodeValidate" select="'2205'"/>
					<xsl:with-param name="node" select="$serieNumeroDocumentoModifica"/>
					<xsl:with-param name="regexp" select="'^[a-zA-Z0-9-]{1,20}-[0-9]{1,10}$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $tipoDocumentoDelDocumentoModifica,' ', $serieNumeroDocumentoModifica)"/>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if
				test="($tipoDocumentoDelDocumentoModifica='14')">
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2205'" />
					<xsl:with-param name="errorCodeValidate" select="'2205'"/>
					<xsl:with-param name="node" select="$serieNumeroDocumentoModifica"/>
					<xsl:with-param name="regexp" select="'^[S][A-Z0-9]{3}-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $tipoDocumentoDelDocumentoModifica,' ', $serieNumeroDocumentoModifica)"/>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if
				test="($tipoDocumentoDelDocumentoModifica='56')">
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2205'" />
					<xsl:with-param name="errorCodeValidate" select="'2205'"/>
					<xsl:with-param name="node" select="$serieNumeroDocumentoModifica"/>
					<xsl:with-param name="regexp" select="'^[\w\-]{0,20}$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $tipoDocumentoDelDocumentoModifica,' ', $serieNumeroDocumentoModifica)"/>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if
				test="count(key('by-document-billing-reference', concat($tipoDocumentoDelDocumentoModifica,' ', $serieNumeroDocumentoModifica))) > 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2365'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $serieNumeroDocumentoModifica)"/>
				</xsl:call-template>
			</xsl:if>
			
			<!-- 16. Tipo de documento del documento que modifica -->
			<xsl:if
				test="(substring($cbcID, 1, 1)='F') and 
					not($tipoDocumentoDelDocumentoModifica='01' or $tipoDocumentoDelDocumentoModifica='12' or $tipoDocumentoDelDocumentoModifica='56' )">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2204'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $serieNumeroDocumentoModifica, ' ', $tipoDocumentoDelDocumentoModifica)"/>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if
				test="(substring($cbcID, 1, 1)='B') and (not($tipoDocumentoDelDocumentoModifica) or ($tipoDocumentoDelDocumentoModifica!='03'))">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2400'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $cbcID, ' ', $tipoDocumentoDelDocumentoModifica)"/>
				</xsl:call-template>
			</xsl:if>
						
			<xsl:if
				test="(substring($cbcID, 1, 1)='S') and (not($tipoDocumentoDelDocumentoModifica) or ($tipoDocumentoDelDocumentoModifica!='14'))">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2930'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $cbcID, ' ', $tipoDocumentoDelDocumentoModifica)"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>
				
		<!-- Guía de remisión relacionada -->
		<xsl:for-each select="cac:DespatchDocumentReference">	
			<!-- 17. Número de la guía de remisión relacionada -->	
			<xsl:variable name="numeroGuiaRemisionRelacionada" select="./cbc:ID"/>
			<!-- 17. Tipo de la guía de remisión relacionada -->
			<xsl:variable name="tipoGuiaRemisionRelacionada" select="./cbc:DocumentTypeCode"/>	
			
			<!-- 17. Número de la guía de remisión relacionada -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4006'"/>
				<xsl:with-param name="node" select="$numeroGuiaRemisionRelacionada"/>
<!-- 				<xsl:with-param name="regexp" select="'^[T][0-9]{3}-[0-9]{1,8}$|^[0-9]{4}-[0-9]{1,8}$|^[EG][0-9]{2}-[0-9]{1,8}$|^[G][0-9]{3}-[0-9]{1,8}$'"/> -->
<!-- 				<xsl:with-param name="regexp" select="'^[T][0-9]{3}-[0-9]{1,8}$|^[0-9]{4}-[0-9]{1,8}$|^[EG]{2}[0-9]{2}-[0-9]{1,8}$|^[G][0-9]{3}-[0-9]{1,8}$'"/> -->
				<xsl:with-param name="regexp" select="'^[T][0-9]{3}-[0-9]{1,8}$|^[0-9]{4}-[0-9]{1,8}$|^(EG)[0-9]{2}-[0-9]{1,8}$|^[G][0-9]{3}-[0-9]{1,8}$'"/>
				<xsl:with-param name="isError" select="false()"/> 
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $numeroGuiaRemisionRelacionada)"/>
			</xsl:call-template>
			
			<xsl:if
				test="count(key('by-document-despatch-reference', concat($tipoGuiaRemisionRelacionada,' ',$numeroGuiaRemisionRelacionada))) > 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2364'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $tipoGuiaRemisionRelacionada, ' ', $numeroGuiaRemisionRelacionada)"/>
				</xsl:call-template>
			</xsl:if>
			
			<!-- 17. Tipo de la guía de remisión relacionada -->			
			<xsl:if test="$tipoGuiaRemisionRelacionada">			
				<xsl:if
					test="not(($tipoGuiaRemisionRelacionada='09') or ($tipoGuiaRemisionRelacionada='31'))">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4005'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', $tipoGuiaRemisionRelacionada)"/>
					</xsl:call-template>
				</xsl:if>			
			</xsl:if>
		</xsl:for-each>

		<!-- Otro documento relacionado -->
		<xsl:for-each select="cac:AdditionalDocumentReference">	
			<!-- 18. Número de otro documento relacionado -->	
			<xsl:variable name="numeroOtroDocumentoRelacionado" select="./cbc:ID"/>
			<!-- 18. Tipo de otro documento relacionado -->
			<xsl:variable name="tipoOtroDocumentoRelacionado" select="./cbc:DocumentTypeCode"/>	
			
			<!-- 18. Número de la guía de remisión relacionada -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4010'"/>
				<xsl:with-param name="node" select="$numeroOtroDocumentoRelacionado"/>
<!-- 				<xsl:with-param name="regexp" select="'[\s].{6,30}'"/> -->
				<xsl:with-param name="regexp" select="'^[\w\d\-]{6,30}$'"/>
				<xsl:with-param name="isError" select="false()"/> 
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $numeroOtroDocumentoRelacionado)"/>
			</xsl:call-template>
			
			<xsl:if
				test="count(key('by-document-additional-reference', concat($tipoOtroDocumentoRelacionado,' ', $numeroOtroDocumentoRelacionado))) > 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2426'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $tipoOtroDocumentoRelacionado, ' ', $numeroOtroDocumentoRelacionado)"/>
				</xsl:call-template>
			</xsl:if>
			
			<!-- 18. Tipo de la guía de remisión relacionada -->					
			<xsl:if
				test="not(($tipoOtroDocumentoRelacionado='04') or ($tipoOtroDocumentoRelacionado='05') or ($tipoOtroDocumentoRelacionado='99') or ($tipoOtroDocumentoRelacionado='01'))">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4009'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', $tipoOtroDocumentoRelacionado)"/>
				</xsl:call-template>
			</xsl:if>			
		</xsl:for-each>

		<!-- 19. Motivo o Sustento -->	
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2136'" />
			<xsl:with-param name="errorCodeValidate" select="'2135'" />
			<xsl:with-param name="node" select="$motivoSustento" />
<!-- 			<xsl:with-param name="regexp" select="'^(?!\s*$).{1,250}'" /> -->
			<xsl:with-param name="regexp" select="'^[^\n].{1,250}$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', position(), ' ', $motivoSustento)"/>
		</xsl:call-template>

		<!-- Datos del detalle o Ítem de la Factura -->
		<xsl:for-each select="cac:DebitNoteLine">
			<!-- 20. Número de orden del Ítem -->
			<xsl:variable name="numeroItem" select="./cbc:ID"/>
			<!-- 21. Unidad de medida por ítem -->		
			<xsl:variable name="unidadMedidaItem" select="./cbc:DebitedQuantity/@unitCode"/>	
			<!-- 23. Cantidad de unidades por ítem -->	
			<xsl:variable name="cantidadUnidadesItem" select="./cbc:DebitedQuantity"/>
			<!-- 26. Valor unitario por ítem -->		
			<xsl:variable name="valorUnitarioItem" select="./cac:Price/cbc:PriceAmount"/>							
			<!-- 33. Valor de venta por línea -->
			<xsl:variable name="valorVentaLinea" select="cbc:LineExtensionAmount"/>

			<!-- 20. Número de orden del Ítem -->
			<xsl:if
				test='not(fn:matches($numeroItem,"^[0-9]{1,3}?$")) or $numeroItem &lt;= 0'>
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2187'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ',$numeroItem)" />
				</xsl:call-template>
			</xsl:if>

			<xsl:if test="count(key('by-invoiceLine-id', number($numeroItem))) > 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2752'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ',$numeroItem)" />
				</xsl:call-template>
			</xsl:if>
							
			<xsl:if	test="$cantidadUnidadesItem">
				<!-- 21. Unidad de medida por ítem -->	
				<xsl:call-template name="existValidateElementIfExist">
					<xsl:with-param name="errorCodeNotExist" select="'2188'"/>
					<xsl:with-param name="node" select="$unidadMedidaItem"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $unidadMedidaItem)"/>
				</xsl:call-template>
				
				<!-- 22. Cantidad de unidades por item -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2189'" />
					<xsl:with-param name="node" select="$cantidadUnidadesItem" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $cantidadUnidadesItem)"/>
				</xsl:call-template>			
			</xsl:if>
			
			<!-- 26. Valor unitario por ítem -->			
			<xsl:if	test="$valorUnitarioItem">				
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2369'" />
					<xsl:with-param name="node" select="$valorUnitarioItem" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $valorUnitarioItem)"/>
				</xsl:call-template>				
			</xsl:if>
			
			<!-- cac:PricingReference -->
			<xsl:for-each select="./cac:PricingReference/cac:AlternativeConditionPrice">
				<!-- 27. Precio de venta unitario por item -->		
				<xsl:variable name="precioVentaUnitarioItem" select="./cbc:PriceAmount"/>	
				<!-- 27. Código de precio -->		
				<xsl:variable name="codigoPrecio" select="./cbc:PriceTypeCode"/>	
					
				<!-- 27. Precio de venta unitario por item -->	
				<xsl:if	test="$precioVentaUnitarioItem">				
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2367'" />
						<xsl:with-param name="node" select="$precioVentaUnitarioItem" />
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $precioVentaUnitarioItem)"/>
					</xsl:call-template>				
				</xsl:if>							
			</xsl:for-each>
			
			<!-- 27. Precio de venta unitario por item que modifica -->
			<xsl:if
				test="(count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='01'])>1 or count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='02'])>1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2409'" />
					<xsl:with-param name="errorMessage"
						select="'Error Expr Regular ND  (codigo: 2409)'" />
				</xsl:call-template>
			</xsl:if>
			
			<!-- Montos de la línea -->
			<xsl:variable name="countCodigoTributoLinea1000" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])"/>				
			<xsl:for-each select="./cac:TaxTotal">
				<!-- 28. Monto de IGV de la línea -->		
				<xsl:variable name="montoIGVLinea" select="./cbc:TaxAmount"/>	
				<!-- 28 -->		
				<xsl:variable name="subTotalMontoIGVLínea" select="./cac:TaxSubtotal/cbc:TaxAmount"/>	
				<!-- 28. Afectación al IGV por la línea -->		
				<xsl:variable name="afectacionIGVLinea" select="./cac:TaxSubtotal/cac:TaxCategory/cbc:TaxExemptionReasonCode"/>	
				<!-- 28. Código de tributo por línea -->		
				<xsl:variable name="codigoTributoLinea" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID"/>	
				<!-- 28. Nombre de tributo por línea -->		
				<xsl:variable name="nombreTributoLinea" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:Name"/>	
				<!-- 28. Código internacional tributo por línea -->		
				<xsl:variable name="codigoInternacionalTributoLinea" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:TaxTypeCode"/>		
				<!-- 29. Tipo de sistema de ISC -->		
				<xsl:variable name="tipoSistemaISC" select="./cac:TaxSubtotal/cac:TaxCategory/cbc:TierRange"/>	
						
				<!-- 28. Monto de IGV de la línea -->						
				<xsl:if test='$montoIGVLinea'>				
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2033'"/>
						<xsl:with-param name="node" select="$montoIGVLinea"/>
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $montoIGVLinea)"/>
					</xsl:call-template>
										
					<xsl:if test="number($montoIGVLinea)!=number($subTotalMontoIGVLínea)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2372'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $montoIGVLinea, ' ', $subTotalMontoIGVLínea)"/>
						</xsl:call-template>
					</xsl:if>					
				</xsl:if>
				
				<!-- 28. Afectación al IGV por la línea -->
				<xsl:if test='$codigoTributoLinea=1000'>
					<xsl:call-template name="existValidateElementIfExist">
						<xsl:with-param name="errorCodeNotExist" select="'2371'"/>
						<xsl:with-param name="node" select="$afectacionIGVLinea"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea)"/>
					</xsl:call-template>								
				</xsl:if>
				
				<!-- 28. Código de tributo por línea -->							
				<xsl:if test="($countCodigoTributoLinea1000>1)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2355'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ', count(./cbc:TaxAmount))"/>
					</xsl:call-template>
				</xsl:if>					
										
				<!-- 28. Nombre de tributo por línea -->			
				<xsl:call-template name="existValidateElementIfExist">
					<xsl:with-param name="errorCodeNotExist" select="'2195'"/>
					<xsl:with-param name="node" select="$nombreTributoLinea"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $nombreTributoLinea)"/>
				</xsl:call-template>	
				
				<xsl:if test="($codigoTributoLinea='1000') and ($nombreTributoLinea!='IGV')">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2377'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ',  $codigoTributoLinea, ' ', $nombreTributoLinea)"/>
					</xsl:call-template>
				</xsl:if>			
							
				<xsl:if test="($codigoTributoLinea='2000')">					
					<!-- 32. Tipo de sistema de ISC -->	
					<xsl:call-template name="existValidateElementIfExist">
						<xsl:with-param name="errorCodeNotExist" select="'2373'"/>
						<xsl:with-param name="node" select="$tipoSistemaISC"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ', $tipoSistemaISC)"/>
					</xsl:call-template>
					
					<xsl:if test="($nombreTributoLinea!='ISC')">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2378'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ', $nombreTributoLinea)"/>
						</xsl:call-template>
					</xsl:if>
										
					<xsl:if test="($codigoInternacionalTributoLinea!='EXC')">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2378'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ', $codigoInternacionalTributoLinea)"/>
						</xsl:call-template>
					</xsl:if>										
				</xsl:if>
			</xsl:for-each>
			
			<!-- 30. Valor de venta por línea -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2370'"/>
				<xsl:with-param name="node" select="$valorVentaLinea"/>
<!--  				<xsl:with-param name="regexp" select="'(-?[0-9]+){1,12}(\.[0-9]{1,2})?$'"/>  -->
 				<xsl:with-param name="regexp" select="'^[\-\+\d]{1}[\d]{0,11}(\.[\d]{1,2})?$'"/> 
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $valorVentaLinea)"/>
			</xsl:call-template>				
		</xsl:for-each>

		<!-- Totales de la Factura -->

		<!-- Valida que solo exista un tag sacAdditionalInformation -->
		<xsl:if test="count($sacAdditionalInformation)>1">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2427'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', count($sacAdditionalInformation))"/>
			</xsl:call-template>
		</xsl:if>
		
		<!-- 33. No deberian de haber duplicados -->
		<xsl:if
			test="(count($additionalMonetaryTotalID[text()='1001'])>1 or
				count($additionalMonetaryTotalID[text()='1002'])>1 or 
				count($additionalMonetaryTotalID[text()='1003'])>1 or 
				count($additionalMonetaryTotalID[text()='1004'])>1 or 
				count($additionalMonetaryTotalID[text()='3001'])>1)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2406'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position())"/>
			</xsl:call-template>
		</xsl:if>

		<xsl:for-each select="$additionalMonetaryTotal">		
			<!-- 33. Código de tipo de monto -->
			<xsl:variable name="codigoTipoMontoFor" select="./cbc:ID" />
			<!-- 33. Total valor de venta - operaciones  -->
			<xsl:variable name="totalValorVentaOperaciones" select="./cbc:PayableAmount" />

			<!-- 33. Total valor de venta - operaciones  -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2340'"/>
				<xsl:with-param name="node" select="$codigoTipoMontoFor"/>
<!-- 				<xsl:with-param name="regexp" select="'(1001)|(1002)|(1003)|(1004)|(1005)|(2001)|(2002)|(2003)|(2004)|(2005)|(3001)'"/>^ -->
				<xsl:with-param name="regexp" select="'^(1001|1002|1003|1004|1005|2001|2002|2003|2004|2005|3001)$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $codigoTipoMontoFor)"/>
			</xsl:call-template>

			<!-- 33. Total valor de venta - operaciones  -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2339'"/>
				<xsl:with-param name="node" select="$totalValorVentaOperaciones"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $totalValorVentaOperaciones)"/>
			</xsl:call-template>
		</xsl:for-each>

		<!-- Sumatoria IGV / ISC / Otros Tributos -->
		<!-- 36. Código de tributo -->		
		<xsl:variable name="countCodigoTributo1000" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])" />
		<xsl:if test="($countCodigoTributo1000>1)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2352'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), $countCodigoTributo1000)"/>
			</xsl:call-template>
		</xsl:if>		
		
		<xsl:for-each select="cac:TaxTotal">
			<!-- 36. Sumatoria IGV -->
			<xsl:variable name="sumatoriaIGV" select="./cbc:TaxAmount" />
			<!-- 36. SubTotal IGV -->
			<xsl:variable name="subTotalIGV" select="./cac:TaxSubtotal/cbc:TaxAmount" />
			<!-- 36. Código de tributo -->
			<xsl:variable name="codigoTributo" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID" />
			<!-- 36. Nombre de tributo -->
			<xsl:variable name="nombreTributo" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:Name" />
			<!-- 36. Código internacional tributo -->
			<xsl:variable name="codigoInternacionalTributo" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:TaxTypeCode" />
			<!-- 36. Sumatoria ISC -->

			<!-- 36. Sumatoria IGV -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2202'"/>
				<xsl:with-param name="node" select="$sumatoriaIGV"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $sumatoriaIGV)"/>
			</xsl:call-template>
					
			<!-- 36. SubTotal IGV -->
			<xsl:if test="($sumatoriaIGV != $subTotalIGV)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2061'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $sumatoriaIGV, ' ', $subTotalIGV)"/>
				</xsl:call-template>
			</xsl:if>
			
			<!-- 36. Código de tributo -->
			<xsl:call-template name="existValidateElementIfExist">
				<xsl:with-param name="errorCodeNotExist" select="'2184'"/>
				<xsl:with-param name="node" select="$codigoTributo"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $codigoTributo)"/>
			</xsl:call-template>
											
			<!-- 36. Nombre de tributo -->
			<xsl:call-template name="existValidateElementIfExist">
				<xsl:with-param name="errorCodeNotExist" select="'2186'"/>
				<xsl:with-param name="node" select="$nombreTributo"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $nombreTributo)"/>
			</xsl:call-template>
						
			<xsl:if test="($codigoTributo='1000') and not(($nombreTributo='IGV'))">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2057'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $nombreTributo)"/>
				</xsl:call-template>
			</xsl:if>
											
			<xsl:if test="($codigoTributo='2000')">						
				<!-- 37. Nombre de tributo -->
				<xsl:if test="($nombreTributo!='ISC')">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2058'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $nombreTributo)"/>
					</xsl:call-template>
				</xsl:if>
			
				<!-- 37. Código internacional tributo -->
				<xsl:if test="($codigoInternacionalTributo!='EXC')">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2058'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $codigoInternacionalTributo)"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:if>
		</xsl:for-each>
				
		<!-- 39. Sumatoria otros Cargos -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2064'"/>
			<xsl:with-param name="node" select="$sumatoriaOtrosCargos"/>
			<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', position(), ' ', $sumatoriaOtrosCargos)"/>
		</xsl:call-template>
		
		<!-- 40. Importe total -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2062'"/>
			<xsl:with-param name="node" select="$importeTotal"/>
			<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', position(), ' ', $importeTotal)"/>
		</xsl:call-template>
				
	</xsl:template>
</xsl:stylesheet>
