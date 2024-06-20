<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:regexp="http://exslt.org/regular-expressions"
	xmlns:gemfunc="http://www.sunat.gob.pe/gem/functions" xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns="urn:oasis:names:specification:ubl:schema:xsd:Invoice-2"
	xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
	xmlns:ext="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2"
	xmlns:sac="urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1"
	xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
	xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
	xmlns:dp="http://www.datapower.com/extensions"
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
		<!-- 6. Fecha de inicio de ciclo de facturación -->
		<xsl:variable name="cbcStartDate" select="cac:InvoicePeriod/cbc:StartDate" />	
		<!-- 6. Fecha de fin de ciclo de facturación -->
		<xsl:variable name="cbcEndDate" select="cac:InvoicePeriod/cbc:EndDate" />			
		<!-- 7. Tipo de documento -->
		<xsl:variable name="tipoDocumento" select="cbc:InvoiceTypeCode"/>		
		<!-- 8. Tipo de moneda -->
		<xsl:variable name="tipoMoneda" select="cbc:DocumentCurrencyCode"/>		
		<!-- 9. Fecha de Vencimiento -->
		<xsl:variable name="fechaVencimiento" select="cac:PaymentMeans/cbc:PaymentDueDate"/>				
		<xsl:variable name="cbcReferenceDate" select="cbc:ReferenceDate" />
		<xsl:variable name="currentdate" select="fn:current-date()"></xsl:variable>				

		<!-- Datos del Emisor -->			
		<xsl:variable name="emisor" select="cac:AccountingSupplierParty"/>		
		<!-- 11. Número de RUC -->
		<xsl:variable name="emisorNumeroDocumento" select="$emisor/cac:Party/cac:PartyIdentification/cbc:ID"/>	
		<!-- 11. Tipo de documento de identidad del emisor -->
		<xsl:variable name="emisorTipoDocumento" select="$emisor/cac:Party/cac:PartyIdentification/cbc:ID/@schemeID"/>			
		<!-- 12. Apellidos y nombres, denominación o razón social -->
<!-- 		<xsl:variable name="emisorRazonSocial" select="$emisor/cac:Party/cac:PartyName/cbc:Name"/> -->
		
		<!-- Datos del Cliente o receptor -->			
		<xsl:variable name="cliente" select="cac:AccountingCustomerParty"/>		
		<!-- 13. Número de documento de identidad del adquirente o usuario -->
		<xsl:variable name="clienteNumeroDocumento" select="$cliente/cac:Party/cac:PartyIdentification/cbc:ID"/>	
		<!-- 13. Tipo de documento de identidad del adquirente o usuario -->
		<xsl:variable name="clienteTipoDocumento" select="$cliente/cac:Party/cac:PartyIdentification/cbc:ID/@schemeID"/>					
		<!-- 14. Apellidos y nombres, denominación o razón social del adquirente o usuario -->
		<xsl:variable name="clienteRazonSocial" select="$cliente/cac:Party/cac:PartyName/cbc:Name"/>
		<!-- 15. Código de distrito - Ubigeo, excepto cuando tenga información en el campo 23 -->
		<xsl:variable name="clienteUbigeo" select="$cliente/cac:Party/cac:PostalAddress/cbc:District"/>	
		
		<!-- Otros datos relativos al servicio -->
		<!-- 16. Tipo de Servicio Público -->
		<xsl:variable name="tipoServicioPublico" select="cac:ContractDocumentReference/cbc:DocumentTypeCode"/>
		<!-- 17. Código de Servicios de Telecomunicaciones (De corresponder) -->
		<xsl:variable name="codigoServiciosTelecomunicaciones" select="cac:ContractDocumentReference/cbc:LocaleCode"/>
		<!-- 18. Número de suministro -->
		<!-- 19. Número de teléfono -->
		<xsl:variable name="numeroServicio" select="cac:ContractDocumentReference/cbc:ID"/>
		<!-- 20. Código de Tipo de Tarifa contratada -->
		<xsl:variable name="codigoTipoTarifaContratada" select="cac:ContractDocumentReference/cbc:DocumentStatusCode"/>
		<!-- 21. Code Potencia contratada en kW -->
		<xsl:variable name="codePotenciaContratadakW" select="cac:Delivery/cbc:MaximumQuantity/@unitCode"/>
		<!-- 21. Potencia contratada en kW -->
		<xsl:variable name="potenciaContratadakW" select="cac:Delivery/cbc:MaximumQuantity"/>
		<!-- 22. Tipo de medidor (trifásico, monofásico) -->
		<xsl:variable name="tipoMedidor" select="cac:Delivery/cbc:ID/@schemeID"/>
		<!-- 23. Número de medidor -->
		<xsl:variable name="numeroMedidor" select="cac:Delivery/cbc:ID"/>
		<!-- 24. Ubicación espacial del medidor (coordenadas georeferenciales), cuando tenga el equipo para ello -->
		<xsl:variable name="ubicacionEspacialMedidor" select="cac:Delivery/cac:DeliveryLocation/cac:LocationCoordinate"/>
		<!-- 25. Code Consumo del periodo -->
		<xsl:variable name="codeConsumoPeriodo" select="cac:Delivery/cbc:Quantity/@unitCode"/>
		<!-- 25. Consumo del periodo -->
		<xsl:variable name="consumoPeriodo" select="cac:Delivery/cbc:Quantity"/>

		<!-- EXT -->
		<!-- 43. Total de valor de venta -->
		<xsl:variable name="totalValorVenta" select="cac:LegalMonetaryTotal/cbc:LineExtensionAmount"/>
		<!-- 44. Total de precio de venta (incluye impuestos) -->
		<xsl:variable name="totalValorVentaIncluyeImpuestos" select="cac:LegalMonetaryTotal/cbc:TaxInclusiveAmount"/>
		<!-- 45. Total descuentos -->
		<xsl:variable name="totalDescuentos" select="cac:LegalMonetaryTotal/cbc:AllowanceTotalAmount"/>
		<!-- 46. Total cargos -->
		<xsl:variable name="totalCargos" select="cac:LegalMonetaryTotal/cbc:ChargeTotalAmount"/>
		<!-- 47. Importe total de la venta, cesión en uso o del servicio prestado -->
		<xsl:variable name="importeTotalVenta" select="cac:LegalMonetaryTotal/cbc:PayableAmount"/>
		
		<!-- Validaciones -->		
		<!-- 1. Version del UBL -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2075'" />
			<xsl:with-param name="errorCodeValidate" select="'2074'" />
			<xsl:with-param name="node" select="$cbcUBLVersionID" />
			<xsl:with-param name="regexp" select="'^(2.1)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cbcUBLVersionID)"/>
		</xsl:call-template>

		<!-- 2. Version de la Estructura del Documento -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2073'" />
			<xsl:with-param name="errorCodeValidate" select="'2072'" />
			<xsl:with-param name="node" select="$cbcCustomizationID" />
			<xsl:with-param name="regexp" select="'^(1.0)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cbcCustomizationID)"/>
		</xsl:call-template>
		
		<!-- 3. Numero de Serie del nombre del archivo no coincide con el consignado en el contenido del archivo XML -->
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
		<xsl:if
			test="($t2 &lt; -2)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2329'" />
				<xsl:with-param name="errorMessage" 
					select="concat('Error en la linea: ', $currentdate, ' - ', $cbcIssueDate, ' - ', $t1, ' - ', $t2)" />
			</xsl:call-template>
		</xsl:if>
		
		<!-- 6. Fecha de fin de ciclo de facturación -->
		<xsl:variable name="t3" select="xs:date($cbcEndDate)-xs:date($cbcStartDate)" />
		<xsl:variable name="t4" select="fn:days-from-duration(xs:duration($t3))" />			
		<xsl:if
			test="($t4 &lt; 0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2892'" />
				<xsl:with-param name="errorMessage" 
					select="concat('Error en la linea: ', $cbcEndDate, ' - ', $cbcStartDate, ' - ', $t3, ' - ', $t4)" />
			</xsl:call-template>
		</xsl:if>
	
		<!-- 7. Tipo de documento -->		
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'1004'" />
			<xsl:with-param name="node" select="$tipoDocumento" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoDocumento)"/>
		</xsl:call-template>
		
		<xsl:if test="$tipoComprobante != $tipoDocumento">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'1003'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $tipoComprobante, '  ', $tipoDocumento)" />
			</xsl:call-template>
		</xsl:if>
		
		<!-- 8. Tipo de moneda -->		
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2070'"/>
			<xsl:with-param name="errorCodeValidate" select="'2069'" />
			<xsl:with-param name="node" select="$tipoMoneda"/>
			<xsl:with-param name="regexp" select="'^[\w]{3}$'"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoMoneda)"/>
		</xsl:call-template>		
		
		<!-- Datos del ciente o receptor -->		
		<!-- 11. Numero de RUC del nombre del archivo no coincide con el consignado en el contenido del archivo XML -->		
		<xsl:if
			test="$numeroRuc != $emisorNumeroDocumento">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'1034'" />
				<xsl:with-param name="errorMessage"
					select="concat('ruc del xml diferente al nombre del archivo ', $emisorNumeroDocumento, ' diff ', $numeroRuc)" />
			</xsl:call-template>
		</xsl:if>

		<!-- 11. Tipo de documento de identidad del emisor - RUC -->			
		<xsl:for-each select="cac:AccountingSupplierParty">	
			<xsl:variable name="countAccountingSupplierParty" select="count(./cac:Party)"/>
			<xsl:if test="$countAccountingSupplierParty &gt; 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2362'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $countAccountingSupplierParty)"/>
				</xsl:call-template>
			</xsl:if>
			<xsl:for-each select="./cac:Party">
				<xsl:variable name="countAccountingSupplierPartyIdentificationID" select="count(./cac:PartyIdentification/cbc:ID)"/>
				<xsl:if test="($countAccountingSupplierPartyIdentificationID &gt; 1)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2362'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $countAccountingSupplierPartyIdentificationID)"/>
					</xsl:call-template>
				</xsl:if>				
			</xsl:for-each>	
								
		</xsl:for-each>
		
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'1008'"/>
			<xsl:with-param name="node" select="$emisorTipoDocumento"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $emisorTipoDocumento)"/>
		</xsl:call-template>
				
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'1007'" />
			<xsl:with-param name="errorCodeValidate" select="'1007'" />
			<xsl:with-param name="node" select="$emisorTipoDocumento" />
			<xsl:with-param name="regexp" select="'^[6]{1}$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $emisorTipoDocumento)"/>
		</xsl:call-template>

		<!-- 12. Apellidos y nombres o denominacion o razon social Emisor -->
		<xsl:variable name="countEmisorRazonSocial" select="count(cac:AccountingSupplierParty/cac:Party/cac:PartyName)"/>
		<xsl:if test="$countEmisorRazonSocial = 0">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'1037'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $countEmisorRazonSocial)" />
			</xsl:call-template>
		</xsl:if>
				
		<xsl:for-each select="cac:AccountingSupplierParty/cac:Party/cac:PartyName">
			<!-- 12. Apellidos y nombres, denominación o razón social -->
			<xsl:variable name="emisorRazonSocial" select="./cbc:Name"/>
			<xsl:call-template name="existValidateElementIfExist">
				<xsl:with-param name="errorCodeNotExist" select="'1037'" />
				<xsl:with-param name="node" select="$emisorRazonSocial" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $emisorRazonSocial)"/>
			</xsl:call-template>
		
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'1038'"/>
				<xsl:with-param name="node" select="$emisorRazonSocial"/>
				<xsl:with-param name="regexp" select="'^[\w\s].{1,99}$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $emisorRazonSocial)"/>
			</xsl:call-template>
		
<!-- 		<xsl:call-template name="regexpValidateElementIfExist"> -->
<!-- 			<xsl:with-param name="errorCodeValidate" select="'1038'"/> -->
<!-- 			<xsl:with-param name="node" select="$emisorRazonSocial"/> -->
<!-- 			<xsl:with-param name="regexp" select="'[^\s]'"/> -->
<!-- 			<xsl:with-param name="descripcion"  -->
<!-- 				select="concat('Error en la linea: ', $emisorRazonSocial)"/> -->
<!-- 		</xsl:call-template> -->
		</xsl:for-each>
		
		<!-- Datos del ciente o receptor -->	
		<!-- 13. Valida que el tipo de documento del adquiriente exista y sea solo uno -->	
		<xsl:for-each select="cac:AccountingCustomerParty">
			<xsl:variable name="countAccountingCustomerParty" select="count(./cac:Party)"/>
			<xsl:if test="$countAccountingCustomerParty &gt; 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2362'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $countAccountingCustomerParty)"/>
				</xsl:call-template>
			</xsl:if>	
			
			<xsl:for-each select="./cac:Party">
				<xsl:variable name="countClienteTipoDocumento" select="count(./cac:PartyIdentification/cbc:ID)"/>	
				<xsl:if test="$countClienteTipoDocumento>1">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2363'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $countClienteTipoDocumento)"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:for-each>
		</xsl:for-each>
		
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
		
		<xsl:if
			test="$clienteTipoDocumento='6'">
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2017'"/>
				<xsl:with-param name="node" select="$clienteNumeroDocumento"/>
				<xsl:with-param name="regexp" select="'^[\d]{11}$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $clienteNumeroDocumento)"/>
			</xsl:call-template>				
		</xsl:if>

		<xsl:if	test="$clienteTipoDocumento='1' and not(fn:matches($clienteNumeroDocumento,'^[0-9]{8}$'))">
			<xsl:call-template name="addWarning">
				<xsl:with-param name="warningCode" select="'4207'" />
				<xsl:with-param name="warningMessage"
					select="concat('Error en la linea: ', $clienteTipoDocumento)"/>
			</xsl:call-template>
		</xsl:if>		
					
		<xsl:if test="($clienteTipoDocumento='4' or $clienteTipoDocumento='7')">
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4208'"/>
				<xsl:with-param name="node" select="$clienteNumeroDocumento"/>
				<xsl:with-param name="regexp" select="'^[\w\s].{0,14}$'"/>
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $clienteTipoDocumento, ' - ', $clienteNumeroDocumento)"/>
			</xsl:call-template>
		</xsl:if>		
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2016'" />
			<xsl:with-param name="node" select="$clienteTipoDocumento" />
			<xsl:with-param name="regexp" select="'^(0|1|4|6|7|A|B|C|D|E)$'" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $clienteTipoDocumento)" />
		</xsl:call-template>
		
		<!-- 14. Apellidos y nombres, denominación o razón social del adquirente o usuario  -->
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'2021'" />
			<xsl:with-param name="node" select="$clienteRazonSocial" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $clienteRazonSocial)"/>
		</xsl:call-template>
				
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2022'"/>
			<xsl:with-param name="node" select="$clienteRazonSocial"/>
			<xsl:with-param name="regexp" select="'^[\w].{2,99}$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $clienteRazonSocial)"/>
		</xsl:call-template>	
		
<!-- 		<xsl:call-template name="regexpValidateElementIfExist"> -->
<!-- 			<xsl:with-param name="errorCodeValidate" select="'2022'"/> -->
<!-- 			<xsl:with-param name="node" select="$clienteRazonSocial"/> -->
<!-- 			<xsl:with-param name="regexp" select="'[^\s]'"/> -->
<!-- 			<xsl:with-param name="descripcion"  -->
<!-- 				select="concat('Error en la linea: ', $clienteRazonSocial)"/> -->
<!-- 		</xsl:call-template>	 -->
						
		<!-- Otros datos relativos al servicio -->	
		<!-- 16. Tipo de Servicio Público -->
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'2921'"/>
			<xsl:with-param name="node" select="$tipoServicioPublico"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoServicioPublico)"/>
		</xsl:call-template>	
			
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2922'" />
			<xsl:with-param name="node" select="$tipoServicioPublico" />
			<xsl:with-param name="regexp" select="'^(1|2|3|4|5)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoServicioPublico)"/>
		</xsl:call-template>
				
		<!-- 17. Código de Servicios de Telecomunicaciones (De corresponder) -->
		<xsl:if test="$tipoServicioPublico = '5'">				
			<xsl:call-template name="existValidateElementNotExist">
				<xsl:with-param name="errorCodeNotExist" select="'2923'"/>
				<xsl:with-param name="node" select="$codigoServiciosTelecomunicaciones"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: a ', $codigoServiciosTelecomunicaciones)"/>
			</xsl:call-template>	
		</xsl:if>
		
		<xsl:if test="not($tipoServicioPublico = '5')">				
			<xsl:call-template name="existValidateElementExist">
				<xsl:with-param name="errorCodeNotExist" select="'2924'"/>
				<xsl:with-param name="node" select="$codigoServiciosTelecomunicaciones"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $codigoServiciosTelecomunicaciones)"/>
			</xsl:call-template>		
		</xsl:if>
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2925'" />
			<xsl:with-param name="node" select="$codigoServiciosTelecomunicaciones" />
			<xsl:with-param name="regexp" select="'^(1|2|3|4)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $codigoServiciosTelecomunicaciones)"/>
		</xsl:call-template>
		
		<!-- 18. Número de suministro -->		
		<xsl:if test="($tipoServicioPublico = '1') or ($tipoServicioPublico = '2')">				
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2926'" />
				<xsl:with-param name="errorCodeValidate" select="'2928'" />
				<xsl:with-param name="node" select="$numeroServicio" />
				<xsl:with-param name="regexp" select="'^[\w]{8}$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoServicioPublico, ' - ', $numeroServicio)"/>
			</xsl:call-template>	
		</xsl:if>
		
		<!-- 19. Número de teléfono -->
		<xsl:if test="($codigoServiciosTelecomunicaciones = '2')">				
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2929'" />
				<xsl:with-param name="errorCodeValidate" select="'2931'" />
				<xsl:with-param name="node" select="$numeroServicio" />
				<xsl:with-param name="regexp" select="'^[\d]{9}$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $codigoServiciosTelecomunicaciones, ' - ', $numeroServicio)"/>
			</xsl:call-template>	
		</xsl:if>
						
		<!-- 20. Código de Tipo de Tarifa contratada -->		
		<xsl:if test="($tipoServicioPublico = '1') or ($tipoServicioPublico = '2')">			
			<xsl:call-template name="existValidateElementIfNoExistCount">
				<xsl:with-param name="errorCodeNotExist" select="'2932'"/>
				<xsl:with-param name="node" select="$codigoTipoTarifaContratada"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoServicioPublico, ' ', $codigoTipoTarifaContratada)"/>
			</xsl:call-template>
		</xsl:if>
						
		<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2934'" />
				<xsl:with-param name="node" select="$codigoTipoTarifaContratada" />
				<xsl:with-param name="regexp" select="'^(L001|L002|L003|L004|L005|L006|L007|L008|L009|L010|L011|L012|L013|L014|L015|A011|A012|A014|A015)$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoServicioPublico, ' - ', $codigoTipoTarifaContratada)"/>
		</xsl:call-template>	
		
		<xsl:if test="not(($tipoServicioPublico = '1') or ($tipoServicioPublico = '2'))">				
			<xsl:call-template name="existValidateElementExist">
				<xsl:with-param name="errorCodeNotExist" select="'2933'"/>
				<xsl:with-param name="node" select="$codigoTipoTarifaContratada"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoServicioPublico, ' ', $codigoTipoTarifaContratada)"/>
			</xsl:call-template>		
		</xsl:if>
								
		<xsl:if test="($tipoServicioPublico = '1')">		
			<!-- 21. Potencia contratada en kW -->	
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2937'" />
				<xsl:with-param name="errorCodeValidate" select="'2939'" />
				<xsl:with-param name="node" select="$potenciaContratadakW"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,3}(\.[0-9]{1,2})?$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoServicioPublico, ' ', $potenciaContratadakW)"/>
			</xsl:call-template>		
			
			<!-- 21. Code Potencia contratada en kW -->		
			<xsl:call-template name="existValidateElementIfNoExistCount">
				<xsl:with-param name="errorCodeNotExist" select="'2935'"/>
				<xsl:with-param name="node" select="$codePotenciaContratadakW"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoServicioPublico, ' ', $codePotenciaContratadakW)"/>
			</xsl:call-template>		
		</xsl:if>
		
		<xsl:if test="not($tipoServicioPublico = '1')">				
			<xsl:call-template name="existValidateElementExist">
				<xsl:with-param name="errorCodeNotExist" select="'2936'"/>
				<xsl:with-param name="node" select="$codePotenciaContratadakW"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoServicioPublico, ' ', $codePotenciaContratadakW)"/>
			</xsl:call-template>		
		</xsl:if>	
		
		<xsl:if test="not($tipoServicioPublico = '1')">				
			<xsl:call-template name="existValidateElementExist">
				<xsl:with-param name="errorCodeNotExist" select="'2938'"/>
				<xsl:with-param name="node" select="$potenciaContratadakW"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoServicioPublico, ' ', $potenciaContratadakW)"/>
			</xsl:call-template>		
		</xsl:if>				
						
		<!-- 22. Tipo de medidor (trifásico, monofásico) -->
		<xsl:if test="$tipoServicioPublico = '1'">				
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2940'" />
				<xsl:with-param name="errorCodeValidate" select="'2942'" />
				<xsl:with-param name="node" select="$tipoMedidor"/>
				<xsl:with-param name="regexp" select="'^(1|2)$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoServicioPublico, ' ', $tipoMedidor)"/>
			</xsl:call-template>		
		</xsl:if>
		
		<xsl:if test="not($tipoServicioPublico = '1')">				
			<xsl:call-template name="existValidateElementExist">
				<xsl:with-param name="errorCodeNotExist" select="'2941'"/>
				<xsl:with-param name="node" select="$tipoMedidor"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoServicioPublico, ' ', $tipoMedidor)"/>
			</xsl:call-template>		
		</xsl:if>				
			
		<!-- 23. Número de medidor -->
		<xsl:if test="$tipoServicioPublico = '1'">				
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2943'" />
				<xsl:with-param name="errorCodeValidate" select="'2945'" />
				<xsl:with-param name="node" select="$numeroMedidor"/>
				<xsl:with-param name="regexp" select="'^[\d]{6}$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoServicioPublico, ' ', $numeroMedidor)"/>
			</xsl:call-template>		
		</xsl:if>
		
		<xsl:if test="not($tipoServicioPublico = '1')">				
			<xsl:call-template name="existValidateElementExist">
				<xsl:with-param name="errorCodeNotExist" select="'2944'"/>
				<xsl:with-param name="node" select="$numeroMedidor"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoServicioPublico, ' ', $numeroMedidor)"/>
			</xsl:call-template>		
		</xsl:if>			
			
		<!-- 25. Code Consumo del periodo -->
		<xsl:if test="($tipoServicioPublico = '1') or ($tipoServicioPublico = '2')">				
			<xsl:call-template name="existValidateElementIfExist">
				<xsl:with-param name="errorCodeNotExist" select="'2947'"/>
				<xsl:with-param name="node" select="$codeConsumoPeriodo"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoServicioPublico, ' - ', $codeConsumoPeriodo)"/>
			</xsl:call-template>	
		</xsl:if>
		
		<xsl:if test="not(($tipoServicioPublico = '1') or ($tipoServicioPublico = '2'))">				
			<xsl:call-template name="existValidateElementExist">
				<xsl:with-param name="errorCodeNotExist" select="'2948'"/>
				<xsl:with-param name="node" select="$codeConsumoPeriodo"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoServicioPublico, ' ', $codeConsumoPeriodo)"/>
			</xsl:call-template>		
			
			<xsl:call-template name="existValidateElementExist">
				<xsl:with-param name="errorCodeNotExist" select="'2951'"/>
				<xsl:with-param name="node" select="$consumoPeriodo"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoServicioPublico, ' ', $consumoPeriodo)"/>
			</xsl:call-template>
		</xsl:if>			
		
		<!-- 25. Consumo del periodo -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2952'" />
			<xsl:with-param name="node" select="$consumoPeriodo" />
			<xsl:with-param name="regexp" select="'^[\d]{10}$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $consumoPeriodo)"/>
		</xsl:call-template>
				
		<!-- Datos del detalle o Ítem de la Factura -->
		<xsl:for-each select="cac:InvoiceLine">
			<!-- 26. Número de orden del Ítem -->
			<xsl:variable name="numeroItem" select="./cbc:ID"/>
			<!-- 27. Unidad de medida por ítem -->		
			<xsl:variable name="unidadMedidaItem" select="./cbc:InvoicedQuantity/@unitCode"/>	
			<!-- 28. Cantidad de unidades por ítem -->	
			<xsl:variable name="cantidadUnidadesItem" select="./cbc:InvoicedQuantity"/>
			<!-- 29. Descripción detallada del servicio prestado, bien vendido o cedido en uso, indicando las características. -->		
			<xsl:variable name="descripcionDetalladaServicioPrestado" select="./cac:Item/cbc:Description"/>
			<!-- 30. Valor unitario por ítem -->		
			<xsl:variable name="valorUnitarioItem" select="./cac:Price/cbc:PriceAmount"/>	
			<!-- 30. Moneda de Valor unitario por ítem -->		
			<xsl:variable name="monedaValorUnitarioItem" select="./cac:Price/cbc:PriceAmount/@currencyID"/>											
			<!-- 33. Valor de venta por línea -->		
			<xsl:variable name="valorVentaLineaFor" select="./cbc:LineExtensionAmount"/>	
			<!-- 33. Moneda Valor de venta por línea -->		
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

			<!-- 26. Número de orden del Ítem -->
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
			
			<xsl:if test="count(key('by-invoiceLine-id', number($numeroItem))) > 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2752'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position())" />
				</xsl:call-template>
			</xsl:if>
			
			<!-- 28. Cantidad de unidades por item -->	
			<xsl:call-template name="existValidateElementIfExist">
				<xsl:with-param name="errorCodeNotExist" select="'2024'"/>
				<xsl:with-param name="node" select="$cantidadUnidadesItem"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $cantidadUnidadesItem)"/>
			</xsl:call-template>
						
			<!-- 27. Unidad de medida por ítem -->		
			<xsl:call-template name="existValidateElementIfExist">
				<xsl:with-param name="errorCodeNotExist" select="'2953'"/>
				<xsl:with-param name="node" select="$unidadMedidaItem"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $unidadMedidaItem)"/>
			</xsl:call-template>
									
			<!-- 28. Cantidad de unidades por item -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2025'" />
				<xsl:with-param name="node" select="$cantidadUnidadesItem" />
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $cantidadUnidadesItem)"/>
			</xsl:call-template>
						
			<!-- 29. Descripcion detallada del servicio prestado, bien vendido o cedido en uso, indicando las caracteristicas -->
			<xsl:call-template name="existAndRegexpValidateElementCount">
				<xsl:with-param name="errorCodeNotExist" select="'2026'" />
				<xsl:with-param name="errorCodeValidate" select="'2027'" />
				<xsl:with-param name="node" select="$descripcionDetalladaServicioPrestado" />
				<xsl:with-param name="regexp" select="'[^\n].{0,249}'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $descripcionDetalladaServicioPrestado)"/>
			</xsl:call-template>
			
			<xsl:variable name="countDDSP" select="string-length(string($descripcionDetalladaServicioPrestado))" />
			<xsl:if test="($countDDSP>250)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2027'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $descripcionDetalladaServicioPrestado)" />
				</xsl:call-template>
			</xsl:if>
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2027'"/>
				<xsl:with-param name="node" select="$descripcionDetalladaServicioPrestado"/>
				<xsl:with-param name="regexp" select="'[^\s]'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $descripcionDetalladaServicioPrestado)"/>
			</xsl:call-template>
			
			<!-- 30. Valor unitario por ítem -->
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2068'" />
				<xsl:with-param name="errorCodeValidate" select="'2369'" />
				<xsl:with-param name="node" select="$valorUnitarioItem" />
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $valorUnitarioItem)"/>
			</xsl:call-template>				
			
			<!-- 31. Precio de venta unitario por item -->
			<xsl:variable name="countPrecioVentaItem" select="count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceAmount)"/>
			<xsl:if test="($countPrecioVentaItem=0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2028'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countPrecioVentaItem)" />
				</xsl:call-template>
			</xsl:if>
			<!-- 31. Valor referencial unitario por ítem en operaciones no onerosas -->
			<xsl:for-each select="./cac:PricingReference/cac:AlternativeConditionPrice">
				<!-- 31. Precio de venta unitario por item -->		
				<xsl:variable name="montoPrecioVentaItem" select="./cbc:PriceAmount"/>
				<!-- 31. Moneda Precio de venta unitario por item -->		
				<xsl:variable name="monedaPrecioVentaUnitarioItem" select="./cbc:PriceAmount/@currencyID"/>	
				<!-- 31. Código de precio -->		
				<xsl:variable name="codigoTipoPrecioItem" select="./cbc:PriceTypeCode"/>

				<!-- Moneda debe ser la misma en todo el documento -->	
				<xsl:if test="($monedaPrecioVentaUnitarioItem) and ($tipoMoneda!=$monedaPrecioVentaUnitarioItem)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$monedaPrecioVentaUnitarioItem)" />
					</xsl:call-template>
				</xsl:if>
																	
				<!-- 31. Precio de venta unitario por item -->
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2028'" />
					<xsl:with-param name="errorCodeValidate" select="'2367'" />
					<xsl:with-param name="node" select="$montoPrecioVentaItem" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $montoPrecioVentaItem)"/>
				</xsl:call-template>
						
				<!-- 31. Precio de venta unitario por item -->	
<!-- 				<xsl:if test="not($codigoTipoPrecioItem='01' or $codigoTipoPrecioItem='02')"> -->
<!-- 					<xsl:call-template name="rejectCall"> -->
<!-- 						<xsl:with-param name="errorCode" select="'2410'" /> -->
<!-- 						<xsl:with-param name="errorMessage" -->
<!-- 							select="concat('Error en la linea: ', position(), ' ', $codigoTipoPrecioItem)"/> -->
<!-- 					</xsl:call-template> -->
<!-- 				</xsl:if> -->

				<!-- 31. Código de precio -->		
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2410'" />
					<xsl:with-param name="node" select="$codigoTipoPrecioItem"/>
					<xsl:with-param name="regexp" select="'^(01|02)$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codigoTipoPrecioItem)"/>
				</xsl:call-template>									
			</xsl:for-each>
															
			<!-- 31. Código de precio -->
			<xsl:variable name="countCodigoPrecio01" select="count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='01'])"/>
			<xsl:variable name="countCodigoPrecio02" select="count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='02'])"/>	
			<xsl:if test="($countCodigoPrecio01>1) or ($countCodigoPrecio02>1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2409'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoPrecio01, ' ', $countCodigoPrecio02)" />
				</xsl:call-template>
			</xsl:if>
										
			<xsl:variable name="countCodigoTributoLinea1000For" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])" />
			<xsl:variable name="countCodigoTributoLinea1016For" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1016'])" />
			<xsl:variable name="countCodigoTributoLinea2000For" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000'])" />
			<xsl:variable name="countCodigoTributoLinea9995For" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9995'])" />
			<xsl:variable name="countCodigoTributoLinea9996For" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9996'])" />
			<xsl:variable name="countCodigoTributoLinea9997For" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9997'])" />
			<xsl:variable name="countCodigoTributoLinea9998For" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9998'])" />
			<xsl:variable name="countCodigoTributoLinea9999For" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9999'])" />
																							
			<!-- Afectacion al IGV por item - Sistema de ISC por item -->
			<xsl:for-each select="./cac:TaxTotal">			
				<!-- 32. Monto de IGV de la línea -->
				<!-- 32. Monto de IGV -->
				<xsl:variable name="montoIGVLinea" select="./cbc:TaxAmount"/>	
				<!-- 32. Moneda Monto de IGV de la línea -->		
				<xsl:variable name="monedaMontoIGVLinea" select="./cbc:TaxAmount/@currencyID"/>		
				<!-- 32. Moneda TaxableAmount -->		
				<xsl:variable name="monedaTaxableAmount" select="./cac:TaxSubtotal/cbc:TaxableAmount/@currencyID"/>				
				<!-- 32. SubTotal del Monto de IGV de la línea -->		
				<xsl:variable name="subTotalMontoIGVLínea" select="./cac:TaxSubtotal/cbc:TaxAmount"/>	
				<!-- 32. Moneda SubMonto de IGV de la línea -->		
				<xsl:variable name="monedaSubTotalMontoIGVLínea" select="./cac:TaxSubtotal/cbc:TaxAmount/@currencyID"/>	
				<!-- 32. Afectación al IGV por la línea -->		
				<xsl:variable name="afectacionIGVLinea" select="./cac:TaxSubtotal/cac:TaxCategory/cbc:TaxExemptionReasonCode"/>	
				<!-- 32. Código de tributo por línea - Catálogo No. 05 -->		
				<xsl:variable name="codigoTributoLinea" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID"/>	
				<!-- 32. Nombre de tributo por línea - Catálogo No. 05 -->		
				<xsl:variable name="nombreTributoLinea" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:Name"/>	
				<!-- 32. Código internacional tributo por línea - Catálogo No. 05 -->		
				<xsl:variable name="codigoInternacionalTributoLinea" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:TaxTypeCode"/>	
				
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
																								
				<xsl:if test='$montoIGVLinea'>	
					<!-- 32. Monto de IGV de la línea -->	
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2033'"/>
						<xsl:with-param name="node" select="$montoIGVLinea"/>
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $montoIGVLinea)"/>
					</xsl:call-template>
					
					<!-- 32. SubTotal del Monto de IGV de la línea -->					
					<xsl:if test="$montoIGVLinea!=$subTotalMontoIGVLínea">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2372'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $montoIGVLinea, ' ', $subTotalMontoIGVLínea)"/>
						</xsl:call-template>
					</xsl:if>					
				</xsl:if>
												
				<!-- 32. Afectación al IGV por la línea - Catálogo No. 07 -->	
				<xsl:call-template name="existValidateElementIfExist">
					<xsl:with-param name="errorCodeNotExist" select="'2037'"/>
					<xsl:with-param name="node" select="$codigoTributoLinea"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea)"/>
				</xsl:call-template>	
							
				<xsl:if test='$codigoTributoLinea=1000'>								
<!-- 					<xsl:call-template name="existValidateElementIfExist"> -->
<!-- 						<xsl:with-param name="errorCodeNotExist" select="'2371'"/> -->
<!-- 						<xsl:with-param name="node" select="$afectacionIGVLinea"/> -->
<!-- 						<xsl:with-param name="descripcion"  -->
<!-- 							select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea)"/> -->
<!-- 					</xsl:call-template> -->
					
					<xsl:call-template name="existAndRegexpValidateElementCount">
						<xsl:with-param name="errorCodeNotExist" select="'2371'"/>
						<xsl:with-param name="errorCodeValidate" select="'2040'" />
						<xsl:with-param name="node" select="$afectacionIGVLinea"/>
						<xsl:with-param name="regexp" select="'^(10|11|12|13|14|15|16|17|20|21|30|31|32|33|34|35|36|40)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $tipoServicioPublico, ' ', $afectacionIGVLinea)"/>
					</xsl:call-template>														
				</xsl:if>
								
				<!-- 32. Código de tributo por línea - Catálogo No. 05  -->					
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2036'" />
					<xsl:with-param name="node" select="$codigoTributoLinea"/>
					<xsl:with-param name="regexp" select="'^(1000|1016|2000|9995|9996|9997|9998|9999)$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $tipoServicioPublico, ' ', $codigoTributoLinea)"/>
				</xsl:call-template>	
														
				<!-- 32. Nombre de tributo por línea - Catálogo No. 05 -->			
				<xsl:call-template name="existValidateElementIfExist">
					<xsl:with-param name="errorCodeNotExist" select="'2038'"/>
					<xsl:with-param name="node" select="$nombreTributoLinea"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $nombreTributoLinea)"/>
				</xsl:call-template>	
				
				<xsl:if test="(($codigoTributoLinea='1000') and ($codigoInternacionalTributoLinea!='VAT')) or
						(($codigoTributoLinea='1016') and ($codigoInternacionalTributoLinea!='VAT')) or 
						(($codigoTributoLinea='2000') and ($codigoInternacionalTributoLinea!='EXC')) or
						(($codigoTributoLinea='9995') and ($codigoInternacionalTributoLinea!='FRE')) or
						(($codigoTributoLinea='9996') and ($codigoInternacionalTributoLinea!='FRE')) or
						(($codigoTributoLinea='9997') and ($codigoInternacionalTributoLinea!='VAT')) or
						(($codigoTributoLinea='9998') and ($codigoInternacionalTributoLinea!='FRE')) or
						(($codigoTributoLinea='9999') and ($codigoInternacionalTributoLinea!='OTH'))">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2378'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $codigoInternacionalTributoLinea)"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:for-each>
			
			<!-- 32. Código de tributo por línea -->	
			<xsl:if test="($countCodigoTributoLinea1000For=0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2042'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoTributoLinea1000For)"/>
				</xsl:call-template>
			</xsl:if>
			
			<!-- 32. Código de tributo por línea  -->
			<xsl:if
				test="($countCodigoTributoLinea1000For>1) or ($countCodigoTributoLinea1016For>1) or ($countCodigoTributoLinea2000For>1) or ($countCodigoTributoLinea9995For>1) or
				($countCodigoTributoLinea9996For>1) or ($countCodigoTributoLinea9997For>1) or ($countCodigoTributoLinea9998For>1) or ($countCodigoTributoLinea9999For>1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2355'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoTributoLinea1000For, ' ', $countCodigoTributoLinea2000For, ' ', $countCodigoTributoLinea9999For)"/>
				</xsl:call-template>
			</xsl:if>
						
			<!-- 34. Valor de venta por línea -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2370'"/>
				<xsl:with-param name="node" select="$valorVentaLineaFor"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $valorVentaLineaFor)"/>
			</xsl:call-template>	
			
			<xsl:for-each select="./cac:AllowanceCharge">
				<!-- 34. Otros cargos Relacionados al Servicio por línea -->		
<!-- 				<xsl:variable name="codeOtrosCargosRelacionadosServicioLinea" select="./cbc:AllowanceChargeReason"/> -->
				<xsl:variable name="codeOtrosCargosRelacionadosServicioLinea">	
					<xsl:choose>
    					<xsl:when test="count(./cbc:AllowanceChargeReason)>0">
    						<xsl:value-of select="./cbc:AllowanceChargeReason"/></xsl:when>
    					<xsl:otherwise><xsl:value-of select="./cbc:AllowanceChargeReasonCode"/></xsl:otherwise>
  					</xsl:choose>       
				</xsl:variable>		
				<!-- 34. Otros cargos Relacionados al Servicio por línea -->		
				<xsl:variable name="montoOtrosCargosRelacionadosServicioLinea" select="./cbc:Amount"/>	
			
				<!-- 34. Otros cargos Relacionados al Servicio por línea -->		
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2954'" />
					<xsl:with-param name="node" select="$codeOtrosCargosRelacionadosServicioLinea"/>
					<xsl:with-param name="regexp" select="'^(54|55)$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codeOtrosCargosRelacionadosServicioLinea)"/>
				</xsl:call-template>
			
				<!-- 34. Otros cargos Relacionados al Servicio por línea -->					
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2955'"/>
					<xsl:with-param name="node" select="$montoOtrosCargosRelacionadosServicioLinea"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $montoOtrosCargosRelacionadosServicioLinea)"/>
				</xsl:call-template>	
			</xsl:for-each>
		</xsl:for-each>

		<!-- Totales de la Factura -->			
		<!-- 32. suma de todos los "Monto de IGV de la línea" -->
		<xsl:variable name="sumaMontoIGVLinea">
			<xsl:choose>
    			<xsl:when test="count(cac:InvoiceLine/cac:TaxTotal[cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxAmount)>0">
    			<xsl:value-of select="sum(cac:InvoiceLine/cac:TaxTotal[cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxAmount)"/></xsl:when>
    			<xsl:otherwise>0</xsl:otherwise>
  			</xsl:choose>       
		</xsl:variable>		
		<xsl:variable name="sumaMontoISCLinea">
			<xsl:choose>
    			<xsl:when test="count(cac:InvoiceLine/cac:TaxTotal[cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxAmount)>0">
    			<xsl:value-of select="sum(cac:InvoiceLine/cac:TaxTotal[cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxAmount)"/></xsl:when>
    			<xsl:otherwise>0</xsl:otherwise>
  			</xsl:choose>       
		</xsl:variable>	
		<xsl:variable name="sumaMontoOTROSLinea">
			<xsl:choose>
    			<xsl:when test="count(cac:InvoiceLine/cac:TaxTotal[cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxAmount)>0">
    			<xsl:value-of select="sum(cac:InvoiceLine/cac:TaxTotal[cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxAmount)"/></xsl:when>
    			<xsl:otherwise>0</xsl:otherwise>
  			</xsl:choose>       
		</xsl:variable>									
		<!-- Sumatoria IGV / ISC / Otros Tributos -->	
		<!-- 35. Sumatoria de impuestos -->
		<xsl:variable name="countSumatoriaImpuestos" select="count(cac:TaxTotal/cbc:TaxAmount)" />
		<xsl:if test="($countSumatoriaImpuestos=0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2956'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countSumatoriaImpuestos)" />
			</xsl:call-template>
		</xsl:if>
		
		<xsl:variable name="countCodigoTributoLinea2000" select="count(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000'])"/>		
		<xsl:for-each select="cac:TaxTotal">
			<!-- 35. Sumatoria de impuestos -->
			<xsl:variable name="sumatoriaImpuestos" select="./cbc:TaxAmount" />
			<!-- 35. Moneda sumatoria de impuestos -->
			<xsl:variable name="monedaSumatoriaImpuestos" select="./cbc:TaxAmount/@currencyID" />
			
			<!-- Moneda debe ser la misma en todo el documento -->	
			<xsl:if test="($monedaSumatoriaImpuestos) and ($tipoMoneda!=$monedaSumatoriaImpuestos)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$monedaSumatoriaImpuestos)" />
				</xsl:call-template>
			</xsl:if>
			
			<!-- 35. Sumatoria de impuestos -->
			<xsl:call-template name="existValidateElementIfExist">
				<xsl:with-param name="errorCodeNotExist" select="'2956'"/>
				<xsl:with-param name="node" select="$sumatoriaImpuestos"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $sumatoriaImpuestos)"/>
			</xsl:call-template>
						
			<!-- 35. Sumatoria de impuestos -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2048'" />
				<xsl:with-param name="node" select="$sumatoriaImpuestos" />
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $sumatoriaImpuestos)"/>
			</xsl:call-template>
			
			<xsl:for-each select="./cac:TaxSubtotal">			
				<!-- 36. Total valor de venta - operaciones gravadas  -->
				<xsl:variable name="totalValorVentaOperacionesGravadas" select="./cbc:TaxableAmount" />	
				<!-- 36. Moneda Total valor de venta - operaciones gravadas  -->
				<xsl:variable name="monedaTotalValorVentaOperacionesGravadas" select="./cbc:TaxableAmount/@currencyID" />			
				<!-- 37. Sumatoria IGV -->
				<!-- 38. Sumatoria otros tributos -->
				<!-- 39. Total valor de venta - operaciones inafectas -->
				<!-- 40. Total valor de venta - operaciones exoneradas -->
				<xsl:variable name="taxAmount" select="./cbc:TaxAmount" />
				<xsl:variable name="monedaTaxAmount" select="./cbc:TaxAmount/@currencyID" />
				<xsl:variable name="taxCategoryID" select="./cac:TaxCategory/cbc:ID" />
				<xsl:variable name="taxCategoryIDSchemeID" select="./cac:TaxCategory/cbc:ID/@schemeID" />
				<xsl:variable name="taxCategoryIDSchemeAgencyID" select="./cac:TaxCategory/cbc:ID/@schemeAgencyID" />
				<xsl:variable name="taxCategoryPercent" select="./cac:TaxCategory/cbc:Percent" />
				<xsl:variable name="taxCategoryTaxSchemeID" select="./cac:TaxCategory/cac:TaxScheme/cbc:ID" />
				<xsl:variable name="taxCategoryTaxSchemeIDSchemeID" select="./cac:TaxCategory/cac:TaxScheme/cbc:ID/@schemeID" />
				<xsl:variable name="taxCategoryTaxSchemeIDSchemeAgencyID" select="./cac:TaxCategory/cac:TaxScheme/cbc:ID/@schemeAgencyID" />
				<xsl:variable name="taxCategoryTaxSchemeName" select="fn:upper-case(./cac:TaxCategory/cac:TaxScheme/cbc:Name)" />
<!-- 				<xsl:variable name="taxCategoryTaxSchemeName" select="./cac:TaxCategory/cac:TaxScheme/cbc:Name" /> -->
				
				<xsl:if test="($monedaTotalValorVentaOperacionesGravadas) and ($tipoMoneda!=$monedaTotalValorVentaOperacionesGravadas)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$monedaTotalValorVentaOperacionesGravadas)" />
					</xsl:call-template>
				</xsl:if>
											
				<xsl:if test="($monedaTaxAmount) and ($tipoMoneda!=$monedaTaxAmount)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$monedaTaxAmount)" />
					</xsl:call-template>
				</xsl:if>
				
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2043'" />
					<xsl:with-param name="node" select="$totalValorVentaOperacionesGravadas" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $totalValorVentaOperacionesGravadas)"/>
				</xsl:call-template>
							
								
				<xsl:if test="($taxCategoryTaxSchemeName='IGV') or ($taxCategoryTaxSchemeName='OTROS') ">			
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2048'" />
						<xsl:with-param name="node" select="$taxAmount" />
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $taxAmount)"/>
					</xsl:call-template>		
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2957'" />
						<xsl:with-param name="node" select="$taxCategoryID" />
						<xsl:with-param name="regexp" select="'^(S)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $taxCategoryID)"/>
					</xsl:call-template>								
				</xsl:if>
										
				<xsl:if test="($taxCategoryTaxSchemeName='IGV')">				
					<xsl:variable name="difSumIGV" select="$sumaMontoIGVLinea - $taxAmount" />
					<xsl:if test="($difSumIGV &lt; -1) or ($difSumIGV &gt; 1)">
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="'4232'" />
							<xsl:with-param name="warningMessage"
								select="concat('Error en la linea: ', $difSumIGV,' - ', $sumaMontoIGVLinea,' - ',$taxAmount)"/>
						</xsl:call-template>
					</xsl:if>
					
					<xsl:variable name="calculoIGV" select="(($totalValorVentaOperacionesGravadas + $sumaMontoISCLinea) * 0.18)" />
					<xsl:variable name="difPrctIGV" select="$calculoIGV - $taxAmount" />
					<xsl:if test="($difPrctIGV &lt; -1) or ($difPrctIGV &gt; 1)">
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="'4019'" />
							<xsl:with-param name="warningMessage"
								select="concat('Error en la linea: ', $difPrctIGV,' - ',$taxAmount,' - ', $calculoIGV, ' ', $totalValorVentaOperacionesGravadas, ' ', $sumaMontoISCLinea)"/>
						</xsl:call-template>
					</xsl:if>
										
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2960'" />
						<xsl:with-param name="node" select="$taxCategoryPercent" />
						<xsl:with-param name="regexp" select="'^(18|0.18)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $taxCategoryPercent)"/>
					</xsl:call-template>

					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2961'" />
						<xsl:with-param name="node" select="$taxCategoryTaxSchemeID" />
						<xsl:with-param name="regexp" select="'^(VAT)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $taxCategoryTaxSchemeID)"/>
					</xsl:call-template>
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2963'" />
						<xsl:with-param name="node" select="$taxCategoryTaxSchemeIDSchemeAgencyID" />
						<xsl:with-param name="regexp" select="'^(6)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $taxCategoryTaxSchemeIDSchemeAgencyID)"/>
					</xsl:call-template>										
				</xsl:if>
				
				<xsl:if test="not(string($taxCategoryTaxSchemeName))">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2054'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position())" />
					</xsl:call-template>				
				</xsl:if>
													
<!-- 				<xsl:call-template name="existValidateElementIfExist"> -->
<!-- 					<xsl:with-param name="errorCodeNotExist" select="'2054'"/> -->
<!-- 					<xsl:with-param name="node" select="$taxCategoryTaxSchemeName"/> -->
<!-- 					<xsl:with-param name="descripcion"  -->
<!-- 						select="concat('Error en la linea: ', $taxCategoryTaxSchemeName)"/> -->
<!-- 				</xsl:call-template> -->
						
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2958'" />
					<xsl:with-param name="node" select="$taxCategoryIDSchemeID" />
					<xsl:with-param name="regexp" select="'^(UN/ECE 5305)$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $taxCategoryIDSchemeID)"/>
				</xsl:call-template>
				
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2959'" />
					<xsl:with-param name="node" select="$taxCategoryIDSchemeAgencyID" />
					<xsl:with-param name="regexp" select="'^(6)$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $taxCategoryIDSchemeAgencyID)"/>
				</xsl:call-template>			
												
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2962'" />
					<xsl:with-param name="node" select="$taxCategoryTaxSchemeIDSchemeID" />
					<xsl:with-param name="regexp" select="'^(UN/ECE 5153)$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $taxCategoryTaxSchemeIDSchemeID)"/>
				</xsl:call-template>
				
				<xsl:if test="($taxCategoryTaxSchemeID='VAT') and not($taxCategoryTaxSchemeName='IGV' or $taxCategoryTaxSchemeName='EXONERADAS')">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2964'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: a ', position(), ' ', $taxCategoryTaxSchemeID, ' ', $taxCategoryTaxSchemeName)"/>
					</xsl:call-template>
				</xsl:if>
							
				<xsl:if test="($taxCategoryTaxSchemeName='OTROS')">
					<xsl:variable name="difSumOtros" select="$sumaMontoOTROSLinea - $taxAmount" />
					<xsl:if test="($difSumOtros &lt; -1) or ($difSumOtros &gt; 1)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2965'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $difSumOtros, ' ', $sumaMontoOTROSLinea, ' ',$taxAmount)"/>
						</xsl:call-template>
					</xsl:if>
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2961'" />
						<xsl:with-param name="node" select="$taxCategoryTaxSchemeID" />
						<xsl:with-param name="regexp" select="'^(OTH)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $taxCategoryTaxSchemeID)"/>
					</xsl:call-template>								
				</xsl:if>
				
				<xsl:if test="($taxCategoryTaxSchemeID='OTH') and not($taxCategoryTaxSchemeName='OTROS')">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2964'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: b ', position(), ' ', $taxCategoryTaxSchemeID, ' ', $taxCategoryTaxSchemeName)"/>
					</xsl:call-template>
				</xsl:if>
				
				<xsl:if test="($taxCategoryTaxSchemeName='INAFECTAS')">							
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2957'" />
						<xsl:with-param name="node" select="$taxCategoryID" />
						<xsl:with-param name="regexp" select="'^(O)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $taxCategoryID)"/>
					</xsl:call-template>
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2961'" />
						<xsl:with-param name="node" select="$taxCategoryTaxSchemeID" />
						<xsl:with-param name="regexp" select="'^(FRE)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $taxCategoryTaxSchemeID)"/>
					</xsl:call-template>	
				</xsl:if>
				
				<xsl:if test="($taxCategoryTaxSchemeID='FRE') and not($taxCategoryTaxSchemeName='INAFECTAS')">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2964'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: c ', position(), ' ', $taxCategoryTaxSchemeID, ' ', $taxCategoryTaxSchemeName)"/>
					</xsl:call-template>
				</xsl:if>
				
				<xsl:if test="($taxCategoryTaxSchemeName='EXONERADAS')">							
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2957'" />
						<xsl:with-param name="node" select="$taxCategoryID" />
						<xsl:with-param name="regexp" select="'^(E)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $taxCategoryID)"/>
					</xsl:call-template>
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2961'" />
						<xsl:with-param name="node" select="$taxCategoryTaxSchemeID" />
						<xsl:with-param name="regexp" select="'^(VAT)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $taxCategoryTaxSchemeID)"/>
					</xsl:call-template>	
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2963'" />
						<xsl:with-param name="node" select="$taxCategoryTaxSchemeIDSchemeAgencyID" />
						<xsl:with-param name="regexp" select="'^(6)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $taxCategoryTaxSchemeIDSchemeAgencyID)"/>
					</xsl:call-template>						
				</xsl:if>
				
				<xsl:if test="($taxCategoryTaxSchemeID='VAT') and not(($taxCategoryTaxSchemeName='IGV') or($taxCategoryTaxSchemeName='EXONERADAS'))">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2964'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: d ', position(), ' ', $taxCategoryTaxSchemeID, ' ', $taxCategoryTaxSchemeName)"/>
					</xsl:call-template>
				</xsl:if>				
			</xsl:for-each>			
		</xsl:for-each>
		
		<xsl:variable name="sumOtrosCargosRelacionadosServicioLinea54">
			<xsl:choose>
    			<xsl:when test="count(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReason='54']/cbc:Amount)>0">
    			<xsl:value-of select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReason='54']/cbc:Amount)"/></xsl:when>
    			<xsl:otherwise>
    			<xsl:value-of select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='54']/cbc:Amount)"/></xsl:otherwise>
  			</xsl:choose>       
		</xsl:variable>	
		<xsl:variable name="sumOtrosCargosRelacionadosServicioLinea55">
			<xsl:choose>
    			<xsl:when test="count(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReason='55']/cbc:Amount)>0">
    			<xsl:value-of select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReason='55']/cbc:Amount)"/></xsl:when>
    			<xsl:otherwise>
    			<xsl:value-of select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='55']/cbc:Amount)"/></xsl:otherwise>
  			</xsl:choose>       
		</xsl:variable>	
		
		<xsl:for-each select="cac:AllowanceCharge">
			<!-- 41. Indicador Sumatoria otros cargos -->
			<xsl:variable name="indicadorSumatoriaOtrosCargosFor" select="./cbc:ChargeIndicator"/>
			<!-- 41. Code Sumatoria otros cargos -->
			<xsl:variable name="codeSumatoriaOtrosCargosFor" select="./cbc:AllowanceChargeReasonCode"/>
			
			<xsl:variable name="codeSumatoriaOtrosCargosFor">
				<xsl:choose>
    				<xsl:when test="count(./cbc:AllowanceChargeReasonCode)>0">
    				<xsl:value-of select="./cbc:AllowanceChargeReasonCode"/></xsl:when>
    				<xsl:otherwise>
    				<xsl:value-of select="./cbc:AllowanceChargeReason"/></xsl:otherwise>
  				</xsl:choose>       
			</xsl:variable>	
			<!-- 41. Sumatoria otros cargos -->
			<xsl:variable name="sumatoriaOtrosCargosFor" select="./cbc:Amount"/>
			
			<xsl:if test="($indicadorSumatoriaOtrosCargosFor = 'true')">
				<!-- 41. Sumatoria otros cargos -->
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2966'" />
					<xsl:with-param name="errorCodeValidate" select="'2966'" />
					<xsl:with-param name="node" select="$codeSumatoriaOtrosCargosFor" />
					<xsl:with-param name="regexp" select="'^(55)$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codeSumatoriaOtrosCargosFor)"/>
				</xsl:call-template>
			
				<!-- 41. Sumatoria otros cargos -->
				<xsl:if test="($codeSumatoriaOtrosCargosFor = '55')">								
					<xsl:variable name="difSumOtrosCargos" select="$sumOtrosCargosRelacionadosServicioLinea55 - $sumatoriaOtrosCargosFor" />
					<xsl:if test="($difSumOtrosCargos &lt; -1) or ($difSumOtrosCargos &gt; 1)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2967'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $difSumOtrosCargos, ' ', $sumOtrosCargosRelacionadosServicioLinea55, ' ',$sumatoriaOtrosCargosFor)"/>
						</xsl:call-template>
					</xsl:if>			
				</xsl:if>			
			</xsl:if>

			<!-- 42. Descuentos globales -->
			<xsl:if test="($indicadorSumatoriaOtrosCargosFor = 'false')">
				<xsl:if test="($sumatoriaOtrosCargosFor) and not($sumatoriaOtrosCargosFor &gt; 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2968'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $sumatoriaOtrosCargosFor)"/>
					</xsl:call-template>
				</xsl:if>				
			</xsl:if>
		</xsl:for-each>
		
		<!-- LegalMonetaryTotal -->
		<!-- 43. Total de valor de venta -->
		<xsl:if test="($totalValorVenta)">			
			<!-- Moneda debe ser la misma en todo el documento -->	
			<xsl:if test="($tipoMoneda!=cac:LegalMonetaryTotal/cbc:LineExtensionAmount/@currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $tipoMoneda, ' ',cac:LegalMonetaryTotal/cbc:LineExtensionAmount/@currencyID)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
		
		<!-- 44. Total de precio de venta (incluye impuestos) -->
		<xsl:if test="($totalValorVentaIncluyeImpuestos)">			
			<!-- Moneda debe ser la misma en todo el documento -->	
			<xsl:if test="($tipoMoneda!=cac:LegalMonetaryTotal/cbc:TaxInclusiveAmount/@currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $tipoMoneda, ' ',cac:LegalMonetaryTotal/cbc:TaxInclusiveAmount/@currencyID)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:if>		
						
		<!-- 45. Total descuentos -->		
		<xsl:if test="($totalDescuentos)">
			<xsl:if test='not(fn:matches(cac:LegalMonetaryTotal/cbc:AllowanceTotalAmount,"^[0-9]{1,12}(\.[0-9]{1,2})?$"))'>
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2065'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', cac:LegalMonetaryTotal/cbc:AllowanceTotalAmount)" />
				</xsl:call-template>
			</xsl:if>

			<!-- Moneda debe ser la misma en todo el documento -->	
			<xsl:if test="($tipoMoneda!=cac:LegalMonetaryTotal/cbc:AllowanceTotalAmount/@currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $tipoMoneda, ' ',cac:LegalMonetaryTotal/cbc:AllowanceTotalAmount/@currencyID)" />
				</xsl:call-template>
			</xsl:if>			
		</xsl:if>
		
		<!-- 46. Total cargos -->
		<!-- 41. Code Sumatoria otros cargos -->
		<xsl:variable name="codeSumatoriaOtrosCargos" select="cac:AllowanceCharge/cbc:AllowanceChargeReasonCode"/>
		<xsl:if test="($totalCargos)">
			<xsl:if test='not(fn:matches($totalCargos,"^[0-9]{1,12}(\.[0-9]{1,2})?$"))'>
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2064'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $totalCargos)" />
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if test="(($codeSumatoriaOtrosCargos = '54') or ($codeSumatoriaOtrosCargos = '55'))">
				<xsl:variable name="sumatoriaOtrosCargos" select="$sumOtrosCargosRelacionadosServicioLinea54 + $sumOtrosCargosRelacionadosServicioLinea55" />
				<xsl:variable name="difSumTotalCargos" select="$sumatoriaOtrosCargos - $totalCargos" />
				<xsl:if test="($difSumTotalCargos &lt; -1) or ($difSumTotalCargos &gt; 1)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2969'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ',  $difSumTotalCargos, ' ', $sumatoriaOtrosCargos, ' ',$totalCargos, ' ', $sumOtrosCargosRelacionadosServicioLinea54, ' ', $sumOtrosCargosRelacionadosServicioLinea55)"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:if>
					
			<!-- Moneda debe ser la misma en todo el documento -->	
			<xsl:if test="($tipoMoneda!=cac:LegalMonetaryTotal/cbc:ChargeTotalAmount/@currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $tipoMoneda, ' ',cac:LegalMonetaryTotal/cbc:ChargeTotalAmount/@currencyID)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
		
		<!-- 48. Importe total -->
		<xsl:if test='not(fn:matches($importeTotalVenta,"^[0-9]{1,12}(\.[0-9]{1,2})?$"))'>
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2062'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $importeTotalVenta)" />
			</xsl:call-template>
		</xsl:if>	
			
		<!-- Moneda debe ser la misma en todo el documento -->	
		<xsl:if test="($tipoMoneda!=cac:LegalMonetaryTotal/cbc:PayableAmount/@currencyID)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2071'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $tipoMoneda, ' ',cac:LegalMonetaryTotal/cbc:PayableAmount/@currencyID)" />
			</xsl:call-template>
		</xsl:if>
		
		<xsl:variable name="sumTVVOGravadas" select="sum(cac:TaxTotal/cac:TaxSubtotal/cbc:TaxableAmount)"/>		
		<xsl:variable name="sumTVVOInafectas" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:Name='Inafectas']/cbc:TaxAmount)"/>		
		<xsl:variable name="sumTVVOExoneradas" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:Name='Exoneradas']/cbc:TaxAmount)"/>					
		<xsl:variable name="sumIGV" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:Name='IGV']/cbc:TaxAmount)"/>		
		<xsl:variable name="sumISC" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:Name='ISC']/cbc:TaxAmount)"/>		
		<xsl:variable name="sumOtrosTributos" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:Name='Otros']/cbc:TaxAmount)"/>		
		<xsl:variable name="sumOtrosCargos" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='55']/cbc:Amount)"/>	
		
		<xsl:variable name="sumImporteTotal" select="$sumTVVOGravadas + $sumTVVOInafectas + $sumTVVOExoneradas + $sumIGV  + $sumOtrosTributos + $sumOtrosCargos" />
		<xsl:variable name="difSumImporteTotal" select="$sumImporteTotal - $importeTotalVenta" />
		<xsl:if test="($difSumImporteTotal &lt; -1) or ($difSumImporteTotal &gt; 1)">
			<xsl:call-template name="addWarning">
				<xsl:with-param name="warningCode" select="'4027'" />
				<xsl:with-param name="warningMessage"
					select="concat('Error en la linea: ', $difSumImporteTotal,'-', $importeTotalVenta,'-',$sumImporteTotal, '-', $sumTVVOGravadas,'-', $sumTVVOInafectas,'-', $sumTVVOExoneradas,'-', $sumIGV,'-', $sumISC,'-', $sumOtrosTributos,'-', $sumOtrosCargos)"/>
			</xsl:call-template>
		</xsl:if>	

		<xsl:copy-of select="." />
				
	</xsl:template>
	
</xsl:stylesheet>
