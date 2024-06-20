<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns="urn:oasis:names:specification:ubl:schema:xsd:Invoice-2"
	xmlns:ext="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2"
	xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
	xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
	xmlns:ccts="urn:un:unece:uncefact:documentation:2" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"	
	xmlns:xs="http://www.w3.org/2001/XMLSchema" 
	xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"	
	xmlns:dp="http://www.datapower.com/extensions"	
	extension-element-prefixes="dp" exclude-result-prefixes="dp">

<!-- INICIO: AXTEROID -->
  <xsl:include href="../util/validate_utils.xsl" dp:ignore-multiple="yes" />
<!-- FIN: AXTEROID -->	
	
	<!-- key Duplicados -->
	<xsl:key name="by-invoiceLine-id" match="*[local-name()='Invoice']/cac:InvoiceLine" use="number(cbc:ID)" />
	
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
		<xsl:variable name="cbcCustomizationID_schemeAgencyName" select="cbc:CustomizationID/@schemeAgencyName" />
		<!-- 3. Numeración, conformada por serie y número correlativo -->
		<xsl:variable name="cbcID" select="cbc:ID" />		
		<!-- 4. Fecha de emisión -->
		<xsl:variable name="fechaEmision" select="cbc:IssueDate" />	
		<!-- 6. Fecha de inicio de ciclo de facturación -->	
		<xsl:variable name="fechaInicioCicloFacturacion" select="cac:InvoicePeriod/cbc:StartDate" />
		<!-- 6. Fecha de fin de ciclo de facturación -->
		<xsl:variable name="fechaFinCicloFacturacion" select="cac:InvoicePeriod/cbc:EndDate" />		
		<!-- 7. Tipo de documento -->
		<xsl:variable name="tipoDocumento" select="cbc:InvoiceTypeCode"/>		
		<xsl:variable name="tipoDocumento_ListAgencyName" select="cbc:InvoiceTypeCode/@listAgencyName"/>	
		<xsl:variable name="tipoDocumento_ListName" select="cbc:InvoiceTypeCode/@listName"/>	
		<xsl:variable name="tipoDocumento_ListURI" select="cbc:InvoiceTypeCode/@listURI"/>	
		<!-- 8. Tipo de moneda -->
		<xsl:variable name="tipoMoneda" select="cbc:DocumentCurrencyCode"/>		
		<xsl:variable name="tipoMoneda_ListID" select="cbc:DocumentCurrencyCode/@listID"/>		
		<xsl:variable name="tipoMoneda_ListName" select="cbc:DocumentCurrencyCode/@listName"/>		
		<xsl:variable name="tipoMoneda_ListAgencyName" select="cbc:DocumentCurrencyCode/@listAgencyName"/>		
		<!-- 9. Fecha de Vencimiento -->
		<xsl:variable name="fechaVencimiento" select="cbc:DueDate"/>				
		<xsl:variable name="currentdate" select="fn:current-date()"></xsl:variable>				
		<!-- 18. Tipo de Servicio Público -->
		<xsl:variable name="tipoServicioPublico" select="cac:ContractDocumentReference/cbc:DocumentTypeCode"/>

		<!-- 30. Valor de venta por ítem -->
		<xsl:variable name="sumValorVentaItem1000For" select="sum(cac:InvoiceLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:LineExtensionAmount)"/>	
		<xsl:variable name="sumValorVentaItem1016For" select="sum(cac:InvoiceLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:LineExtensionAmount)"/>	
		<xsl:variable name="sumValorVentaItem9995For" select="sum(cac:InvoiceLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='9995']/cbc:LineExtensionAmount)"/>	
		<xsl:variable name="sumValorVentaItem9997For" select="sum(cac:InvoiceLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='9997']/cbc:LineExtensionAmount)"/>	
		<xsl:variable name="sumValorVentaItem9998For" select="sum(cac:InvoiceLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='9998']/cbc:LineExtensionAmount)"/>	

		<!-- 33. Código de precio -->
		<xsl:variable name="countCodigoPrecio01For" select="count(cac:InvoiceLine/cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='01'])"/>
		<xsl:variable name="countCodigoPrecio02For" select="count(cac:InvoiceLine/cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='02'])"/>	
			
		<!-- 34. Monto base -->
		<xsl:variable name="sumMontoBaseFor" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cbc:TaxableAmount)"/>		
		<!-- 34. Monto base por Código de tributo por línea-->
		<xsl:variable name="sumMontoBase1000For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxableAmount)"/>		
		<xsl:variable name="sumMontoBase1016For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumMontoBase2000For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumMontoBase9995For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9995']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumMontoBase9996For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9995']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumMontoBase9997For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9997']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumMontoBase9998For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9998']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumMontoBase9999For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxableAmount)"/>

		<!-- 34. Monto de IGV/IVAP de la línea -->
		<!-- 35. Monto del tributo de la línea -->
		<xsl:variable name="sumMontoTributo1000For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo1016For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo2000For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo9995For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9995']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo9996For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9996']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo9997For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9997']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo9998For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9998']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo9999For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxAmount )"/>

		<!-- 37. Monto de cargo/descuento -->
		<xsl:variable name="sumMontoCargoDescuento00For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='00']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento01For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='01']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento02For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='02']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento03For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='03']/cbc:Amount)"/>	
		<xsl:variable name="sumMontoCargoDescuento04For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='04']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento05For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='05']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento06For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='06']/cbc:Amount)"/>			
		<xsl:variable name="sumMontoCargoDescuento47For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='47']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento48For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='48']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento49For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='49']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento50For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='50']/cbc:Amount)"/>	
		<xsl:variable name="sumMontoCargoDescuento51For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='51']/cbc:Amount)"/>	
		<xsl:variable name="sumMontoCargoDescuento52For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='52']/cbc:Amount)"/>	
		<xsl:variable name="sumMontoCargoDescuento53For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='53']/cbc:Amount)"/>	
						
		<!-- 39. Count Total valor de venta -->
		<xsl:variable name="countCodigoTributo1000" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])" />
		<xsl:variable name="countCodigoTributo1016" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1016'])" />
		<xsl:variable name="countCodigoTributo2000" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000'])" />
		<xsl:variable name="countCodigoTributo9995" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9995'])" />
		<xsl:variable name="countCodigoTributo9996" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9996'])" />
		<xsl:variable name="countCodigoTributo9997" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9997'])" />
		<xsl:variable name="countCodigoTributo9998" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9998'])" />
		<xsl:variable name="countCodigoTributo9999" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9999'])" />		
		
		<!-- 38. Monto total de impuestos -->
		<xsl:variable name="sumMontoTotalImpuestos" select="sum(cac:TaxTotal/cbc:TaxAmount)"/>
  		
		<xsl:variable name="importeTributo_1000" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxAmount)"/>
		<xsl:variable name="importeTributo_1016" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:TaxAmount)"/>
		<xsl:variable name="importeTributo_2000" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxAmount)"/>
		<xsl:variable name="importeTributo_9995" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9995']/cbc:TaxAmount)"/>
		<xsl:variable name="importeTributo_9996" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9996']/cbc:TaxAmount)"/>
		<xsl:variable name="importeTributo_9997" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9997']/cbc:TaxAmount)"/>
		<xsl:variable name="importeTributo_9998" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9998']/cbc:TaxAmount)"/>
		<xsl:variable name="importeTributo_9999" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxAmount)"/>

		<!-- 47. Cargos y Descuentos Globales -->
		<xsl:variable name="sumMontoCargoDescuento00" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='00']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento01" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='01']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento02" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='02']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento03" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='03']/cbc:Amount)"/>	
		<xsl:variable name="sumMontoCargoDescuento04" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='04']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento05" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='05']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento06" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='06']/cbc:Amount)"/>		
		<xsl:variable name="sumMontoCargoDescuento45" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='45']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento46" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='46']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento47" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='47']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento48" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='48']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento49" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='49']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento50" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='50']/cbc:Amount)"/>	
		<xsl:variable name="sumMontoCargoDescuento51" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='51']/cbc:Amount)"/>	
		<xsl:variable name="sumMontoCargoDescuento52" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='52']/cbc:Amount)"/>	
		<xsl:variable name="sumMontoCargoDescuento53" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='53']/cbc:Amount)"/>
				
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
			<xsl:with-param name="regexp" select="'^(2.0)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cbcCustomizationID)"/>
		</xsl:call-template>
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'4250'" />
			<xsl:with-param name="node" select="$cbcCustomizationID_schemeAgencyName" />
			<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cbcCustomizationID_schemeAgencyName)"/>
		</xsl:call-template>		
		
		<!-- 3. Numeración, conformada por serie y número correlativo -->
		<xsl:if test="not($numeroSerie = substring($cbcID, 1, 4))">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'1035'" />
				<xsl:with-param name="errorMessage"
					select="concat('numero de serie del xml diferente al numero de serie del archivo ', substring($cbcID, 1, 4), ' diff ', $numeroSerie)" />
			</xsl:call-template>
		</xsl:if>

		<!-- 3. Numero de documento en el nombre del archivo no coincide con el consignado en el contenido del XML -->
		<xsl:if test="not($numeroComprobante = substring($cbcID, 6))">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'1036'" />
				<xsl:with-param name="errorMessage"
					select="concat('numero de comprobante del xml diferente al numero del archivo ', substring($cbcID, 6), ' diff ', $numeroComprobante)" />
			</xsl:call-template>
		</xsl:if>		
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'1001'" />
			<xsl:with-param name="node" select="$cbcID" />
			<xsl:with-param name="regexp" select="'^[S][A-Z0-9]{3}-[0-9]{1,8}$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cbcID)"/>
		</xsl:call-template>		
		
		<!-- 4. Fecha de emisión -->
		<xsl:variable name="t1" select="xs:date($currentdate)-xs:date($fechaEmision)" />
		<xsl:variable name="t2" select="fn:days-from-duration(xs:duration($t1))" />			
		<xsl:if test="($t2 &lt; -2)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2329'" />
				<xsl:with-param name="errorMessage" 
					select="concat('Error en la linea: ', $currentdate, ' - ', $fechaEmision, ' - ', $t1, ' - ', $t2)" />
			</xsl:call-template>
		</xsl:if>
		
		<!-- 6. Fecha de fin de ciclo de facturación -->
		<xsl:variable name="t3" select="xs:date($fechaFinCicloFacturacion)-xs:date($fechaInicioCicloFacturacion)" />
		<xsl:variable name="t4" select="fn:days-from-duration(xs:duration($t3))" />			
		<xsl:if test="($t4 &lt; 0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3198'" />
				<xsl:with-param name="errorMessage" 
					select="concat('Error en la linea: ', $fechaFinCicloFacturacion, ' - ', $fechaInicioCicloFacturacion, ' - ', $t3, ' - ', $t4)" />
			</xsl:call-template>
		</xsl:if>
	
		<!-- 7. Tipo de documento -->		
		<xsl:call-template name="existValidateElementNotExist">
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

		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'4251'" />
			<xsl:with-param name="node" select="$tipoDocumento_ListAgencyName" />
			<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoDocumento_ListAgencyName)"/>
		</xsl:call-template>		
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'4252'" />
			<xsl:with-param name="node" select="$tipoDocumento_ListName" />
			<xsl:with-param name="regexp" select="'^(Tipo de Documento)$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoDocumento_ListName)"/>
		</xsl:call-template>
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'4253'" />
			<xsl:with-param name="node" select="$tipoDocumento_ListURI" />
			<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo01)$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoDocumento_ListURI)"/>
		</xsl:call-template>
		
		<!-- 8. Tipo de moneda -->		
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'2070'"/>
			<xsl:with-param name="node" select="$tipoMoneda"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $tipoMoneda)"/>
		</xsl:call-template>	
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'4254'" />
			<xsl:with-param name="node" select="$tipoMoneda_ListID" />
			<xsl:with-param name="regexp" select="'^(ISO 4217 Alpha)$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoMoneda_ListID)"/>
		</xsl:call-template>		
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'4252'" />
			<xsl:with-param name="node" select="$tipoMoneda_ListName" />
			<xsl:with-param name="regexp" select="'^(Currency)$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoMoneda_ListName)"/>
		</xsl:call-template>
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'4251'" />
			<xsl:with-param name="node" select="$tipoMoneda_ListAgencyName" />
			<xsl:with-param name="regexp" select="'^(United Nations Economic Commission for Europe)$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoMoneda_ListAgencyName)"/>
		</xsl:call-template>			
		
		<!-- Datos del ciente o receptor -->	
		<xsl:variable name="countEmisorPartyIdentification" select="count(cac:AccountingSupplierParty/cac:Party/cac:PartyIdentification)"/>
		<xsl:if test="$countEmisorPartyIdentification &gt; 1">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3089'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countEmisorPartyIdentification)"/>
			</xsl:call-template>
		</xsl:if>
								
		<!-- 11. Numero de RUC del nombre del archivo no coincide con el consignado en el contenido del archivo XML -->	
		<xsl:for-each select="cac:AccountingSupplierParty">		
			<!-- Datos del Emisor -->						
			<xsl:for-each select="./cac:Party">	
				<xsl:for-each select="./cac:PartyIdentification">		
					<!-- 11. Número de RUC -->
					<xsl:variable name="emisorNumeroDocumento" select="./cbc:ID"/>	
					<!-- 11. Tipo de documento de identidad del emisor -->
					<xsl:variable name="emisorTipoDocumento" select="./cbc:ID/@schemeID"/>	
					<xsl:variable name="emisorNumeroDocumento_SchemeName" select="./cbc:ID/@schemeName"/>
					<xsl:variable name="emisorNumeroDocumento_SchemeAgencyName" select="./cbc:ID/@schemeAgencyName"/>
					<xsl:variable name="emisorNumeroDocumento_SchemeURI" select="./cbc:ID/@schemeURI"/>		
			
					<!-- 11. Número de RUC -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'1008'"/>
						<xsl:with-param name="node" select="$emisorNumeroDocumento"/>
						<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $emisorNumeroDocumento)"/>
					</xsl:call-template>
					
					<xsl:if test="not($numeroRuc = $emisorNumeroDocumento)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'1034'" />
							<xsl:with-param name="errorMessage"
								select="concat('ruc del xml diferente al nombre del archivo ', $emisorNumeroDocumento, ' diff ', $numeroRuc)" />
						</xsl:call-template>
					</xsl:if>	
					
					<!-- 11. Tipo de documento de identidad del emisor - RUC -->				
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'1007'" />
						<xsl:with-param name="node" select="$emisorTipoDocumento" />
						<xsl:with-param name="regexp" select="'^[6]$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $emisorTipoDocumento)"/>
					</xsl:call-template>
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4255'" />
						<xsl:with-param name="node" select="$emisorNumeroDocumento_SchemeName" />
						<xsl:with-param name="regexp" select="'^(Documento de Identidad)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $emisorNumeroDocumento_SchemeName)"/>
					</xsl:call-template>		
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4256'" />
						<xsl:with-param name="node" select="$emisorNumeroDocumento_SchemeAgencyName" />
						<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $emisorNumeroDocumento_SchemeAgencyName)"/>
					</xsl:call-template>
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4251'" />
						<xsl:with-param name="node" select="$emisorNumeroDocumento_SchemeURI" />
						<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo06)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $emisorNumeroDocumento_SchemeURI)"/>
					</xsl:call-template>									
				</xsl:for-each>
				
				<xsl:for-each select="./cac:PartyName">
					<!-- 12. Nombre Comercial -->
					<xsl:variable name="emisorNombreComercial" select="./cbc:Name"/>	
									
					<!-- 12. Nombre Comercial -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4092'"/>
						<xsl:with-param name="node" select="$emisorNombreComercial"/>
						<xsl:with-param name="regexp" select="'^[\w\s].{1,1500}$'"/>
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $emisorNombreComercial)"/>
					</xsl:call-template>								
				</xsl:for-each>					
				
				<xsl:for-each select="./cac:PartyLegalEntity">
					<!-- 13. Apellidos y nombres, denominación o razón social -->
					<xsl:variable name="emisorRazonSocial" select="./cbc:RegistrationName"/>

					<!-- 13. Apellidos y nombres o denominacion o razon social Emisor -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'1037'" />
						<xsl:with-param name="node" select="$emisorRazonSocial" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $emisorRazonSocial)"/>
					</xsl:call-template>
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'1038'"/>
						<xsl:with-param name="node" select="$emisorRazonSocial"/>
						<xsl:with-param name="regexp" select="'^[\w\s].{1,1500}$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea 1: ', position(), ' ', $emisorRazonSocial)"/>
					</xsl:call-template>
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'1038'"/>
						<xsl:with-param name="node" select="$emisorRazonSocial"/>
						<xsl:with-param name="regexp" select="'[^\s]'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea 2: ', position(), ' ', $emisorRazonSocial)"/>
					</xsl:call-template>	
								
					<!-- 14. Domicilio Fiscal -->					
					<xsl:for-each select="./cac:RegistrationAddress">						
						<xsl:for-each select="./cac:AddressLine">
							<!-- 14. Dirección completa y detallada -->
							<xsl:variable name="emisorDireccionCompletaItem" select="./cbc:Line"/>
							
							<!-- 14. Domicilio Fiscal -->
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4094'"/>
								<xsl:with-param name="node" select="$emisorDireccionCompletaItem"/>
								<xsl:with-param name="regexp" select="'^[\w\s].{2,200}$'"/>
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $emisorDireccionCompletaItem)"/>
							</xsl:call-template>														
						</xsl:for-each>	
						
						<!-- 14. Urbanización -->
						<xsl:variable name="emisorUrbanizacion" select="./cbc:CitySubdivisionName"/>
						<!-- 14. Provincia -->
						<xsl:variable name="emisorProvincia" select="./cbc:CityName"/>
						<!-- 14. Código de ubigeo -->
						<xsl:variable name="emisorUbigeo" select="./cbc:ID"/>
						<xsl:variable name="emisorUbigeo_SchemeAgencyName" select="./cbc:ID/@schemeAgencyName"/>
						<xsl:variable name="emisorUbigeo_SchemeName" select="./cbc:ID/@schemeName"/>
						<!-- 14. Departamento -->
						<xsl:variable name="emisorDepartamento" select="./cbc:CountrySubentity"/>
						<!-- 14. Distrito -->
						<xsl:variable name="emisorDistrito" select="./cbc:District"/>
		
						<!-- 14. Urbanización -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4095'"/>
							<xsl:with-param name="node" select="$emisorUrbanizacion"/>
							<xsl:with-param name="regexp" select="'^[\w\s].{1,25}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $emisorUrbanizacion)"/>
						</xsl:call-template>
						
						<!-- 14. Provincia -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4096'"/>
							<xsl:with-param name="node" select="$emisorProvincia"/>
							<xsl:with-param name="regexp" select="'^[\w\s].{1,30}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $emisorProvincia)"/>
						</xsl:call-template>
				
						<!-- 14. Código de ubigeo -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4256'" />
							<xsl:with-param name="node" select="$emisorUbigeo_SchemeAgencyName" />
							<xsl:with-param name="regexp" select="'^(PE:INEI)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $emisorUbigeo_SchemeAgencyName)"/>
						</xsl:call-template>			
						
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4255'" />
							<xsl:with-param name="node" select="$emisorUbigeo_SchemeName" />
							<xsl:with-param name="regexp" select="'^(Ubigeos)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $emisorUbigeo_SchemeName)"/>
						</xsl:call-template>		
						
						<!-- 14. Departamento -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4097'"/>
							<xsl:with-param name="node" select="$emisorDepartamento"/>
							<xsl:with-param name="regexp" select="'^[\w\s].{1,30}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $emisorDepartamento)"/>
						</xsl:call-template>
				
						<!-- 14. Distrito -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4098'"/>
							<xsl:with-param name="node" select="$emisorDistrito"/>
							<xsl:with-param name="regexp" select="'^[\w\s].{1,30}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $emisorDistrito)"/>
						</xsl:call-template>						
						
						<!-- 14. Código de país -->
						<xsl:for-each select="./cac:Country">
							<xsl:variable name="emisorCodigoPais" select="./cbc:IdentificationCode"/>
							<xsl:variable name="emisorCodigoPais_ListID" select="./cbc:IdentificationCode/@listID"/>
							<xsl:variable name="emisorCodigoPais_ListAgencyName" select="./cbc:IdentificationCode/@listAgencyName"/>
							<xsl:variable name="emisorCodigoPais_ListName" select="./cbc:IdentificationCode/@listName"/>				
				
							<!-- 14. Código de país -->
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4041'"/>
								<xsl:with-param name="node" select="$emisorCodigoPais"/>
								<xsl:with-param name="regexp" select="'^(PE)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $emisorCodigoPais)"/>
							</xsl:call-template>					
							
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4254'" />
								<xsl:with-param name="node" select="$emisorCodigoPais_ListID" />
								<xsl:with-param name="regexp" select="'^(ISO 3166-1)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $emisorCodigoPais_ListID)"/>
							</xsl:call-template>			
							
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4251'" />
								<xsl:with-param name="node" select="$emisorCodigoPais_ListAgencyName" />
								<xsl:with-param name="regexp" select="'^(United Nations Economic Commission for Europe)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $emisorCodigoPais_ListAgencyName)"/>
							</xsl:call-template>	
							
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4252'" />
								<xsl:with-param name="node" select="$emisorCodigoPais_ListName" />
								<xsl:with-param name="regexp" select="'^(Country)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $emisorCodigoPais_ListName)"/>
							</xsl:call-template>				
						</xsl:for-each>												
					</xsl:for-each>						
				</xsl:for-each>										
			</xsl:for-each>
		</xsl:for-each>			

		<!-- Datos del cliente o receptor -->	
		<xsl:variable name="countClientePartyIdentification" select="count(cac:AccountingCustomerParty/cac:Party/cac:PartyIdentification)"/>
		<xsl:if test="$countClientePartyIdentification &gt; 1">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2363'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countClientePartyIdentification)"/>
			</xsl:call-template>
		</xsl:if>		
							
		<xsl:for-each select="cac:AccountingCustomerParty">
			<!-- Datos del Cliente o receptor -->			
			<xsl:for-each select="./cac:Party">
				<xsl:for-each select="./cac:PartyIdentification">
					<!-- 15. Número de documento de identidad del adquirente o usuario -->
					<xsl:variable name="clienteNumeroDocumento" select="./cbc:ID"/>	
					<!-- 15. Tipo de documento de identidad del adquirente o usuario -->
					<xsl:variable name="clienteTipoDocumento" select="./cbc:ID/@schemeID"/>
					<xsl:variable name="clienteNumeroDocumento_SchemeName" select="./cbc:ID/@schemeName"/>	
					<xsl:variable name="clienteNumeroDocumento_SchemeAgencyName" select="./cbc:ID/@schemeAgencyName"/>	
					<xsl:variable name="clienteNumeroDocumento_SchemeURI" select="./cbc:ID/@schemeURI"/>	

					<!-- 15. Tipo de documento de identidad del adquirente o usuario -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'2015'"/>
						<xsl:with-param name="node" select="$clienteTipoDocumento"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $clienteTipoDocumento)"/>
					</xsl:call-template>
					
					<!-- 15. Número de documento de identidad del adquirente o usuario -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'2014'"/>
						<xsl:with-param name="node" select="$clienteNumeroDocumento"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $clienteNumeroDocumento)"/>
					</xsl:call-template>										
				
					<xsl:if test="$clienteTipoDocumento='6'">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'2017'" />
							<xsl:with-param name="node" select="$clienteNumeroDocumento" />
							<xsl:with-param name="regexp" select="'^[\d]{11}$'"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $clienteNumeroDocumento)"/>
						</xsl:call-template>		
					</xsl:if>
					
					<xsl:if test="$clienteTipoDocumento='4' or $clienteTipoDocumento='7'">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4208'" />
							<xsl:with-param name="node" select="$clienteNumeroDocumento" />
							<xsl:with-param name="regexp" select="'^[\w\d\-]{1,15}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $clienteNumeroDocumento, ' ', $clienteTipoDocumento)"/>
						</xsl:call-template>		
					</xsl:if>
					
					<xsl:if test="$clienteTipoDocumento='1'">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4207'" />
							<xsl:with-param name="node" select="$clienteNumeroDocumento" />
							<xsl:with-param name="regexp" select="'^[\d]{8}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $clienteNumeroDocumento)"/>
						</xsl:call-template>		
					</xsl:if>					
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2016'"/>
						<xsl:with-param name="node" select="$clienteTipoDocumento"/>
						<xsl:with-param name="regexp" select="'^(0|1|4|6|7|A|B|C|D|E)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $clienteTipoDocumento)"/>
					</xsl:call-template>																						
					
					<!-- 15. Número de documento de identidad del adquirente o usuario -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4255'" />
						<xsl:with-param name="node" select="$clienteNumeroDocumento_SchemeName" />
						<xsl:with-param name="regexp" select="'^(Documento de Identidad)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $clienteNumeroDocumento_SchemeName)"/>
					</xsl:call-template>			
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4256'" />
						<xsl:with-param name="node" select="$clienteNumeroDocumento_SchemeAgencyName" />
						<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $clienteNumeroDocumento_SchemeAgencyName)"/>
					</xsl:call-template>	
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4257'" />
						<xsl:with-param name="node" select="$clienteNumeroDocumento_SchemeURI" />
						<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo06)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $clienteNumeroDocumento_SchemeURI)"/>
					</xsl:call-template>													
				</xsl:for-each>

				<xsl:for-each select="./cac:PartyLegalEntity">
					<!-- 16. Apellidos y nombres, denominación o razón social del adquirente o usuario -->
					<xsl:variable name="clienteRazonSocial" select="./cbc:RegistrationName"/>		
					
					<!-- 16. Apellidos y nombres, denominación o razón social del adquirente o usuario -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'2021'"/>
						<xsl:with-param name="node" select="$clienteRazonSocial"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $clienteRazonSocial)"/>
					</xsl:call-template>
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2022'" />
						<xsl:with-param name="node" select="$clienteRazonSocial" />
						<xsl:with-param name="regexp" select="'^[^\n].{2,1500}$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $clienteRazonSocial)"/>
					</xsl:call-template>														
				</xsl:for-each>	
			</xsl:for-each>				
		</xsl:for-each>

		<!-- Otros datos relativos al servicio -->			
		<xsl:for-each select="./cac:ContractDocumentReference">
			<!-- 18. Tipo de Servicio Público -->
<!-- 			<xsl:variable name="tipoServicioPublico" select="./cbc:DocumentTypeCode"/>			 -->
			<xsl:variable name="tipoServicioPublico_ListName" select="./cbc:DocumentTypeCode/@listName"/>
			<xsl:variable name="tipoServicioPublico_ListAgencyName" select="./cbc:DocumentTypeCode/@listAgencyName"/>
			<xsl:variable name="tipoServicioPublico_ListURI" select="./cbc:DocumentTypeCode/@listURI"/>
			<!-- 19. Código de Servicios de Telecomunicaciones (De corresponder) -->
			<xsl:variable name="codigoServiciosTelecomunicaciones" select="./cbc:LocaleCode"/>
			<xsl:variable name="codigoServiciosTelecomunicaciones_ListName" select="./cbc:LocaleCode/@listName"/>
			<xsl:variable name="codigoServiciosTelecomunicaciones_ListAgencyName" select="./cbc:LocaleCode/@listAgencyName"/>
			<xsl:variable name="codigoServiciosTelecomunicaciones_ListURI" select="./cbc:LocaleCode/@listURI"/>			
			<!-- 20. Número de suministro -->
			<!-- 21. Número de teléfono -->
			<xsl:variable name="numeroServicio" select="./cbc:ID"/>
			<!-- 22. Código de Tipo de Tarifa contratada -->
			<xsl:variable name="codigoTipoTarifaContratada" select="./cbc:DocumentStatusCode"/>
			<xsl:variable name="codigoTipoTarifaContratada_ListName" select="./cbc:DocumentStatusCode/@listName"/>
			<xsl:variable name="codigoTipoTarifaContratada_ListAgencyName" select="./cbc:DocumentStatusCode/@listAgencyName"/>
			<xsl:variable name="codigoTipoTarifaContratada_ListURI" select="./cbc:DocumentStatusCode/@listURI"/>
			
			<!-- 18. Tipo de Servicio Público -->
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
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4252'" />
				<xsl:with-param name="node" select="$tipoServicioPublico_ListName" />
				<xsl:with-param name="regexp" select="'^(Tipo de servicio público)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $tipoServicioPublico_ListName)"/>
			</xsl:call-template>			
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4251'" />
				<xsl:with-param name="node" select="$tipoServicioPublico_ListAgencyName" />
				<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $tipoServicioPublico_ListAgencyName)"/>
			</xsl:call-template>	
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4253'" />
				<xsl:with-param name="node" select="$tipoServicioPublico_ListURI" />
				<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo56)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $tipoServicioPublico_ListURI)"/>
			</xsl:call-template>			
					
			<!-- 19. Código de Servicios de Telecomunicaciones (De corresponder) -->
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
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4252'" />
				<xsl:with-param name="node" select="$codigoServiciosTelecomunicaciones_ListName" />
				<xsl:with-param name="regexp" select="'^(Tipo de servicio publico de telecomunicaciones)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $codigoServiciosTelecomunicaciones_ListName)"/>
			</xsl:call-template>			
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4251'" />
				<xsl:with-param name="node" select="$codigoServiciosTelecomunicaciones_ListAgencyName" />
				<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $codigoServiciosTelecomunicaciones_ListAgencyName)"/>
			</xsl:call-template>	
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4253'" />
				<xsl:with-param name="node" select="$codigoServiciosTelecomunicaciones_ListURI" />
				<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo57)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $codigoServiciosTelecomunicaciones_ListURI)"/>
			</xsl:call-template>			
			
			<!-- 20. Número de suministro -->		
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
			
			<!-- 21. Número de teléfono -->
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
							
			<!-- 22. Código de Tipo de Tarifa contratada -->		
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
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4252'" />
				<xsl:with-param name="node" select="$codigoTipoTarifaContratada_ListName" />
				<xsl:with-param name="regexp" select="'^(Tipo de tarifa de servicio publico)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $codigoTipoTarifaContratada_ListName)"/>
			</xsl:call-template>			
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4251'" />
				<xsl:with-param name="node" select="$codigoTipoTarifaContratada_ListAgencyName" />
				<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $codigoTipoTarifaContratada_ListAgencyName)"/>
			</xsl:call-template>	
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4253'" />
				<xsl:with-param name="node" select="$codigoTipoTarifaContratada_ListURI" />
				<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo24)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $codigoTipoTarifaContratada_ListURI)"/>
			</xsl:call-template>							
		</xsl:for-each>
		
		<xsl:for-each select="./cac:Delivery">
			<!-- 23. Code Potencia contratada en kW -->
			<xsl:variable name="codePotenciaContratadakW" select="./cbc:MaximumQuantity/@unitCode"/>
			<!-- 23. Potencia contratada en kW -->
			<xsl:variable name="potenciaContratadakW" select="./cbc:MaximumQuantity"/>
			<xsl:variable name="potenciaContratadakW_UnitCodeListID" select="./cbc:MaximumQuantity/@unitCodeListID"/>
			<xsl:variable name="potenciaContratadakW_UnitCodeListAgencyName" select="./cbc:MaximumQuantity/@unitCodeListAgencyName"/>
			<!-- 24. Tipo de medidor (trifásico, monofásico) -->
			<xsl:variable name="tipoMedidor" select="./cbc:ID/@schemeID"/>
			<!-- 25. Número de medidor -->
			<xsl:variable name="numeroMedidor" select="./cbc:ID"/>
			<xsl:variable name="numeroMedidor_SchemeName" select="./cbc:ID/@schemeName"/>
			<xsl:variable name="numeroMedidor_SchemeAgencyName" select="./cbc:ID/@schemeAgencyName"/>
			<xsl:variable name="numeroMedidor_SchemeURI" select="./cbc:ID/@schemeURI"/>
			<!-- 26. Ubicación espacial del medidor (coordenadas georeferenciales), cuando tenga el equipo para ello -->
			<xsl:variable name="ubicacionEspacialMedidor" select="./cac:DeliveryLocation/cac:LocationCoordinate"/>
			<!-- 27. Code Consumo del periodo -->
			<xsl:variable name="codeConsumoPeriodo" select="./cbc:Quantity/@unitCode"/>
			<!-- 27. Consumo del periodo -->
			<xsl:variable name="consumoPeriodo" select="./cbc:Quantity"/>
			<xsl:variable name="consumoPeriodo_UnitCodeListID" select="./cbc:Quantity/@unitCodeListID"/>
			<xsl:variable name="consumoPeriodo_UnitCodeListAgencyName" select="./cbc:Quantity/@unitCodeListAgencyName"/>
			
			<xsl:if test="($tipoServicioPublico = '1')">		
				<!-- 23. Potencia contratada en kW -->	
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2937'" />
					<xsl:with-param name="errorCodeValidate" select="'2939'" />
					<xsl:with-param name="node" select="$potenciaContratadakW"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,3}(\.[0-9]{1,2})?$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $tipoServicioPublico, ' ', $potenciaContratadakW)"/>
				</xsl:call-template>		
				
				<!-- 23. Code Potencia contratada en kW -->		
				<xsl:call-template name="existValidateElementIfNoExistCount">
					<xsl:with-param name="errorCodeNotExist" select="'2935'"/>
					<xsl:with-param name="node" select="$codePotenciaContratadakW"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $tipoServicioPublico, ' ', $codePotenciaContratadakW)"/>
				</xsl:call-template>		
				
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4258'" />
					<xsl:with-param name="node" select="$potenciaContratadakW_UnitCodeListID" />
					<xsl:with-param name="regexp" select="'^(UN/ECE rec 20)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $potenciaContratadakW_UnitCodeListID)"/>
				</xsl:call-template>
				
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4259'" />
					<xsl:with-param name="node" select="$potenciaContratadakW_UnitCodeListAgencyName" />
					<xsl:with-param name="regexp" select="'^(United Nations Economic Commission for Europe)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $potenciaContratadakW_UnitCodeListAgencyName)"/>
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
							
			<!-- 24. Tipo de medidor (trifásico, monofásico) -->
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
				
			<!-- 25. Número de medidor -->
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
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4255'" />
				<xsl:with-param name="node" select="$numeroMedidor_SchemeName" />
				<xsl:with-param name="regexp" select="'^(Tipo de medidor)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $numeroMedidor_SchemeName)"/>
			</xsl:call-template>			
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4256'" />
				<xsl:with-param name="node" select="$numeroMedidor_SchemeAgencyName" />
				<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $numeroMedidor_SchemeAgencyName)"/>
			</xsl:call-template>	
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4257'" />
				<xsl:with-param name="node" select="$numeroMedidor_SchemeURI" />
				<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo58)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $numeroMedidor_SchemeURI)"/>
			</xsl:call-template>				
				
			<!-- 27. Code Consumo del periodo -->
			<xsl:if test="($tipoServicioPublico = '1') or ($tipoServicioPublico = '2')">				
				<xsl:call-template name="existValidateElementNotExist">
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
			
			<!-- 27. Consumo del periodo -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2952'" />
				<xsl:with-param name="node" select="$consumoPeriodo" />
				<xsl:with-param name="regexp" select="'^[\d]{10}$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $consumoPeriodo)"/>
			</xsl:call-template>
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4258'" />
				<xsl:with-param name="node" select="$consumoPeriodo_UnitCodeListID" />
				<xsl:with-param name="regexp" select="'^(UN/ECE rec 20)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $consumoPeriodo_UnitCodeListID)"/>
			</xsl:call-template>
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4259'" />
				<xsl:with-param name="node" select="$consumoPeriodo_UnitCodeListAgencyName" />
				<xsl:with-param name="regexp" select="'^(United Nations Economic Commission for Europe)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $consumoPeriodo_UnitCodeListAgencyName)"/>
			</xsl:call-template>			
		</xsl:for-each>
	
		<!-- Datos del detalle o Ítem de la Factura -->
		<xsl:for-each select="cac:InvoiceLine">
			<!-- 28. Número de orden del Ítem -->
			<xsl:variable name="numeroItem" select="./cbc:ID"/>
			<!-- 29. Unidad de medida por ítem -->		
			<xsl:variable name="unidadMedidaItem" select="./cbc:InvoicedQuantity/@unitCode"/>	
			<!-- 30. Cantidad de unidades por ítem -->	
			<xsl:variable name="cantidadUnidadesItem" select="./cbc:InvoicedQuantity"/>
			<xsl:variable name="cantidadUnidadesItem_UnitCodeListID" select="./cbc:InvoicedQuantity/@unitCodeListID"/>
			<xsl:variable name="cantidadUnidadesItem_UnitCodeListAgencyName" select="./cbc:InvoicedQuantity/@unitCodeListAgencyName"/>
			<!-- 37. Valor de venta por línea -->		
			<xsl:variable name="valorVentaLinea" select="./cbc:LineExtensionAmount"/>		
			<xsl:variable name="valorVentaLinea_currencyID" select="./cbc:LineExtensionAmount/@currencyID"/>
						
			<!-- 34. Código de tributo por línea -->					
			<xsl:variable name="countCodigoTributoLinea1000Item" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])" />
			<xsl:variable name="countCodigoTributoLinea1016Item" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1016'])" />
			<xsl:variable name="countCodigoTributoLinea2000Item" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000'])" />
			<xsl:variable name="countCodigoTributoLinea9995Item" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9995'])" />
			<xsl:variable name="countCodigoTributoLinea9996Item" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9996'])" />
			<xsl:variable name="countCodigoTributoLinea9997Item" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9997'])" />
			<xsl:variable name="countCodigoTributoLinea9998Item" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9998'])" />
			<xsl:variable name="countCodigoTributoLinea9999Item" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9999'])" />
			
			<!-- 28. Número de orden del Ítem -->
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
						
			<!-- 29. Unidad de medida por ítem -->		
			<xsl:call-template name="existValidateElementNotExist">
				<xsl:with-param name="errorCodeNotExist" select="'2953'"/>
				<xsl:with-param name="node" select="$unidadMedidaItem"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $unidadMedidaItem)"/>
			</xsl:call-template>			
			
			<!-- 30. Cantidad de unidades por item -->	
			<xsl:call-template name="existValidateElementNotExist">
				<xsl:with-param name="errorCodeNotExist" select="'2024'"/>
				<xsl:with-param name="node" select="$cantidadUnidadesItem"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $cantidadUnidadesItem)"/>
			</xsl:call-template>
															
			<!-- 30. Cantidad de unidades por item -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2025'" />
				<xsl:with-param name="node" select="$cantidadUnidadesItem" />
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $cantidadUnidadesItem)"/>
			</xsl:call-template>
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4258'" />
				<xsl:with-param name="node" select="$cantidadUnidadesItem_UnitCodeListID" />
				<xsl:with-param name="regexp" select="'^(UN/ECE rec 20)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $cantidadUnidadesItem_UnitCodeListID)"/>
			</xsl:call-template>
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4259'" />
				<xsl:with-param name="node" select="$cantidadUnidadesItem_UnitCodeListAgencyName" />
				<xsl:with-param name="regexp" select="'^(United Nations Economic Commission for Europe)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $cantidadUnidadesItem_UnitCodeListAgencyName)"/>
			</xsl:call-template>			
			
			<xsl:for-each select="./cac:Item">
				<!-- 31. Descripción detallada del servicio prestado, bien vendido o cedido en uso, indicando las características. -->		
				<xsl:variable name="descripcionDetalladaServicioPrestado" select="./cbc:Description"/>
	
				<!-- 31. Descripcion detallada del servicio prestado, bien vendido o cedido en uso, indicando las caracteristicas -->
				<xsl:call-template name="existAndRegexpValidateElementCount">
					<xsl:with-param name="errorCodeNotExist" select="'2026'" />
					<xsl:with-param name="errorCodeValidate" select="'2027'" />
					<xsl:with-param name="node" select="$descripcionDetalladaServicioPrestado" />
					<xsl:with-param name="regexp" select="'[^\n].{0,500}'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $descripcionDetalladaServicioPrestado)"/>
				</xsl:call-template>
				
				<xsl:variable name="countDDSP" select="string-length(string($descripcionDetalladaServicioPrestado))" />
				<xsl:if test="($countDDSP>500)">
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
			</xsl:for-each>			
			
			<xsl:for-each select="./cac:Price">
				<!-- 32. Valor unitario por ítem -->		
				<xsl:variable name="valorUnitarioItem" select="./cbc:PriceAmount"/>	
				<!-- 32. Moneda de Valor unitario por ítem -->		
				<xsl:variable name="valorUnitarioItem_currencyID" select="./cbc:PriceAmount/@currencyID"/>	
				
				<!-- 32. Valor unitario por ítem -->
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2068'" />
					<xsl:with-param name="errorCodeValidate" select="'2369'" />
					<xsl:with-param name="node" select="$valorUnitarioItem" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $valorUnitarioItem)"/>
				</xsl:call-template>		
				
				<xsl:if test="($valorUnitarioItem_currencyID) and ($tipoMoneda!=$valorUnitarioItem_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$valorUnitarioItem_currencyID)" />
					</xsl:call-template>
				</xsl:if>			
			</xsl:for-each>
	
			<!-- 33. Precio de venta unitario por item -->
			<xsl:variable name="countPrecioVentaItem" select="count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceAmount)"/>
			<xsl:if test="($countPrecioVentaItem=0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2028'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countPrecioVentaItem)" />
				</xsl:call-template>
			</xsl:if>
			
			<!-- 33. Valor referencial unitario por ítem en operaciones no onerosas -->
			<xsl:for-each select="./cac:PricingReference">
				<xsl:for-each select="./cac:AlternativeConditionPrice">
					<!-- 33. Precio de venta unitario por item -->		
					<xsl:variable name="precioVentaUnitarioItem" select="./cbc:PriceAmount"/>
					<!-- 33. Moneda Precio de venta unitario por item -->		
					<xsl:variable name="precioVentaUnitarioItem_currencyID" select="./cbc:PriceAmount/@currencyID"/>	
					<!-- 33. Código de precio -->		
					<xsl:variable name="codigoTipoPrecioItem" select="./cbc:PriceTypeCode"/>
					<xsl:variable name="codigoTipoPrecioItem_ListName" select="./cbc:PriceTypeCode/@listName"/>
					<xsl:variable name="codigoTipoPrecioItem_ListAgencyName" select="./cbc:PriceTypeCode/@listAgencyName"/>
					<xsl:variable name="codigoTipoPrecioItem_ListURI" select="./cbc:PriceTypeCode/@listURI"/>
	
					<!-- Moneda debe ser la misma en todo el documento -->	
					<xsl:if test="($precioVentaUnitarioItem_currencyID) and ($tipoMoneda!=$precioVentaUnitarioItem_currencyID)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2071'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$precioVentaUnitarioItem_currencyID)" />
						</xsl:call-template>
					</xsl:if>
																		
					<!-- 31. Precio de venta unitario por item -->
					<xsl:call-template name="existAndRegexpValidateElement">
						<xsl:with-param name="errorCodeNotExist" select="'2028'" />
						<xsl:with-param name="errorCodeValidate" select="'2367'" />
						<xsl:with-param name="node" select="$precioVentaUnitarioItem" />
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $precioVentaUnitarioItem)"/>
					</xsl:call-template>
	
					<!-- 31. Código de precio -->		
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2410'" />
						<xsl:with-param name="node" select="$codigoTipoPrecioItem"/>
						<xsl:with-param name="regexp" select="'^(01|02)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $codigoTipoPrecioItem)"/>
					</xsl:call-template>
						
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4252'" />
						<xsl:with-param name="node" select="$codigoTipoPrecioItem_ListName" />
						<xsl:with-param name="regexp" select="'^(Tipo de precio)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $codigoTipoPrecioItem_ListName)"/>
					</xsl:call-template>			
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4251'" />
						<xsl:with-param name="node" select="$codigoTipoPrecioItem_ListAgencyName" />
						<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $codigoTipoPrecioItem_ListAgencyName)"/>
					</xsl:call-template>	
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4253'" />
						<xsl:with-param name="node" select="$codigoTipoPrecioItem_ListURI" />
						<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo16)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $codigoTipoPrecioItem_ListURI)"/>
					</xsl:call-template>							
				</xsl:for-each>					
			</xsl:for-each>
															
			<!-- 33. Código de precio -->
			<xsl:variable name="countCodigoPrecio01" select="count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='01'])"/>
			<xsl:variable name="countCodigoPrecio02" select="count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='02'])"/>	
			<xsl:if test="($countCodigoPrecio01>1) or ($countCodigoPrecio02>1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2409'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoPrecio01, ' ', $countCodigoPrecio02)" />
				</xsl:call-template>
			</xsl:if>
																				
			<!-- 34. Monto total de impuestos por linea -->
			<xsl:variable name="countTaxTotal" select="count(./cac:TaxTotal)"/>
			<xsl:if test="($countTaxTotal=0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3195'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countTaxTotal)" />
				</xsl:call-template>
			</xsl:if>	
																							
			<xsl:if test="($countTaxTotal &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3026'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countTaxTotal)" />
				</xsl:call-template>
			</xsl:if>	
			
			<!-- 35.  -->
			<!-- 36. Código de tributo por linea -->
			<xsl:if
				test="($countCodigoTributoLinea1000Item &gt; 1) or ($countCodigoTributoLinea1016Item &gt; 1) or ($countCodigoTributoLinea2000Item &gt; 1) or ($countCodigoTributoLinea9995Item &gt; 1) 
					or ($countCodigoTributoLinea9996Item &gt; 1) or ($countCodigoTributoLinea9997Item &gt; 1) or ($countCodigoTributoLinea9998Item &gt; 1) or ($countCodigoTributoLinea9999Item &gt; 1)"> 
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3067'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoTributoLinea1000Item, ' ', $countCodigoTributoLinea2000Item, ' ', $countCodigoTributoLinea9999Item)"/>
				</xsl:call-template>
			</xsl:if>	
					
			<xsl:if 
				test="($countCodigoTributoLinea1000Item = 0) and ($countCodigoTributoLinea9996Item = 0) and ($countCodigoTributoLinea9997Item = 0) and ($countCodigoTributoLinea9998Item = 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3105'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoTributoLinea1000Item, ' ', $countCodigoTributoLinea9996Item, ' ', $countCodigoTributoLinea9998Item)"/>
				</xsl:call-template>
			</xsl:if>	
			
			<xsl:variable name="sumaCoutCodigoTributo" select="$countCodigoTributoLinea1000Item + $countCodigoTributoLinea9995Item + $countCodigoTributoLinea9996Item + $countCodigoTributoLinea9997Item + $countCodigoTributoLinea9998Item"/>
			<xsl:if test="(sumaCoutCodigoTributo &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3106'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $sumaCoutCodigoTributo, ' ', $countCodigoTributoLinea1000Item, ' ', $countCodigoTributoLinea9995Item)"/>
				</xsl:call-template>
			</xsl:if>					

			<xsl:if
				test="($countCodigoTributoLinea1016Item &gt; 0) or ($countCodigoTributoLinea9995Item &gt; 0) or ($countCodigoTributoLinea2000Item &gt; 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3066'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoTributoLinea1016Item, ' ', $countCodigoTributoLinea2000Item, ' ', $countCodigoTributoLinea9995Item)"/>
				</xsl:call-template>
			</xsl:if>
																																												
			<!-- Afectacion al IGV por item - Sistema de ISC por item -->
			<xsl:for-each select="./cac:TaxTotal">			
				<!-- 34. Monto total de impuestos del ítem -->
				<!-- 34. Monto total de impuestos por linea -->
				<xsl:variable name="montoTotalImpuestosLinea" select="./cbc:TaxAmount"/>
				<!-- 34. Moneda Monto de IGV de la línea -->			
				<xsl:variable name="montoTotalImpuestosLinea_currencyID" select="./cbc:TaxAmount/@currencyID"/>
				
				<xsl:variable name="montoTributoLinea_1000" select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxAmount)"/>	
	    		<xsl:variable name="montoTributoLinea_9999" select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxAmount)"/>
								
				<!-- 34. Moneda debe ser la misma en todo el documento -->	
				<xsl:if test="($montoTotalImpuestosLinea_currencyID) and ($tipoMoneda!=$montoTotalImpuestosLinea_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$montoTotalImpuestosLinea_currencyID)" />
					</xsl:call-template>
				</xsl:if>
				
				<xsl:if test="($montoTotalImpuestosLinea)">		
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'3021'"/>
						<xsl:with-param name="node" select="$montoTotalImpuestosLinea"/>
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $montoTotalImpuestosLinea)"/>
					</xsl:call-template>
								
				    <xsl:variable name="sumaMontoTributoLinea" select="$montoTributoLinea_1000 + $montoTributoLinea_9999"/>
				    <xsl:variable name="dif_MontoTotalImpuestosLinea" select="$montoTotalImpuestosLinea - $sumaMontoTributoLinea" />
					<xsl:if test="($dif_MontoTotalImpuestosLinea &lt; -1) or ($dif_MontoTotalImpuestosLinea &gt; 1)">
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="'4293'" />
							<xsl:with-param name="warningMessage"
								select="concat('Error en la linea: ', position(), ' ', $dif_MontoTotalImpuestosLinea,'-', $montoTotalImpuestosLinea,'-',$sumaMontoTributoLinea, '-', $montoTributoLinea_9999)"/>
						</xsl:call-template>
					</xsl:if>	
				</xsl:if>
				
				<!-- 35. Afectación al IGV por la línea -->	
				<!-- 36. Afectacion Otros Tributos -->					
				<xsl:for-each select="./cac:TaxSubtotal">	
					<!-- 35. Monto Base -->		
					<!-- 36. Monto Base -->	
					<xsl:variable name="montoBase" select="./cbc:TaxableAmount"/>
					<!-- 35. Moneda TaxableAmount -->		
					<xsl:variable name="montoBase_currencyID" select="./cbc:TaxableAmount/@currencyID"/>	
					<!-- 35. Monto de IGV de la línea -->	
					<!-- 36. Monto de Otros Tributos -->	
					<xsl:variable name="montoIGVLinea" select="./cbc:TaxAmount"/>	
					<!-- 35. Moneda Monto de IGV de la línea -->		
					<xsl:variable name="montoIGVLinea_currencyID" select="./cbc:TaxAmount/@currencyID"/>
					
					<xsl:if test="($montoIGVLinea_currencyID) and ($tipoMoneda!=$montoIGVLinea_currencyID)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2071'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$montoIGVLinea_currencyID)" />
						</xsl:call-template>
					</xsl:if>
					
					<!-- 35. Monto base -->		
					<!-- 36. Monto base -->				
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'3031'"/>
						<xsl:with-param name="node" select="$montoBase"/>
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $montoBase)"/>
					</xsl:call-template>									
					
					<!-- 35. Monto de IGV de la línea -->	
					<!-- 36. Monto de Otros Tributos -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2033'"/>
						<xsl:with-param name="node" select="$montoIGVLinea"/>
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $montoIGVLinea)"/>
					</xsl:call-template>
									
					<!-- 36. Moneda Monto base -->								
					<xsl:if test="($montoBase_currencyID) and ($tipoMoneda!=$montoBase_currencyID)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2071'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$montoBase_currencyID)" />
						</xsl:call-template>
					</xsl:if>	
				
					<xsl:for-each select="./cac:TaxCategory">
						<!-- 35. Tasa del IGV -->		
						<!-- 36. Tasa del tributo -->
						<xsl:variable name="tasaIGV" select="./cbc:Percent "/>	
						<!-- 35. Afectación al IGV por la línea -->		
						<xsl:variable name="afectacionIGVLinea" select="./cbc:TaxExemptionReasonCode"/>	
						<xsl:variable name="afectacionIGVLinea_ListAgencyName" select="./cbc:TaxExemptionReasonCode/@listAgencyName"/>
						<xsl:variable name="afectacionIGVLinea_ListName" select="./cbc:TaxExemptionReasonCode/@listName"/>
						<xsl:variable name="afectacionIGVLinea_ListURI" select="./cbc:TaxExemptionReasonCode/@listURI"/>
						
						<!-- 35. Tasa del IGV -->	
						<!-- 36. Tasa del tributo -->
						<xsl:call-template name="existAndRegexpValidateElementCount">
							<xsl:with-param name="errorCodeNotExist" select="'2992'"/>
							<xsl:with-param name="errorCodeValidate" select="'3102'"/>
							<xsl:with-param name="node" select="$tasaIGV"/>
							<xsl:with-param name="regexp" select="'^[0-9]{1,3}(\.[0-9]{1,5})?$'"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $tasaIGV)"/>
						</xsl:call-template>												
				
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4252'" />
							<xsl:with-param name="node" select="$afectacionIGVLinea_ListName" />
							<xsl:with-param name="regexp" select="'^(Afectacion del IGV)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea_ListName)"/>
						</xsl:call-template>			
						
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4251'" />
							<xsl:with-param name="node" select="$afectacionIGVLinea_ListAgencyName" />
							<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea_ListAgencyName)"/>
						</xsl:call-template>	
						
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4253'" />
							<xsl:with-param name="node" select="$afectacionIGVLinea_ListURI" />
							<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo07)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea_ListURI)"/>
						</xsl:call-template>					
				
						<xsl:for-each select="./cac:TaxScheme">						
							<!-- 35. Código de tributo por línea - Catálogo No. 05 -->	
							<!-- 36. Código de tributo por linea - Catálogo No. 05 -->	
							<xsl:variable name="codigoTributo" select="./cbc:ID"/>								
							<xsl:variable name="codigoTributo_schemeName" select="./cbc:ID/@schemeName" />
							<xsl:variable name="codigoTributo_schemeAgencyName" select="./cbc:ID/@schemeAgencyName" />
							<xsl:variable name="codigoTributo_schemeURI" select="./cbc:ID/@schemeURI" />
							<!-- 35. Nombre de tributo por línea - Catálogo No. 05 -->		
							<!-- 36. Nombre de tributo por línea - Catálogo No. 05 -->
							<xsl:variable name="nombreTributo" select="./cbc:Name"/>	
							<!-- 35. Código internacional tributo por línea - Catálogo No. 05 -->	
							<!-- 36. Código internacional tributo por línea - Catálogo No. 05 -->		
							<xsl:variable name="codigoInternacionalTributo" select="./cbc:TaxTypeCode"/>
							
							<!-- 35. Afectación al IGV por la línea - Catálogo No. 07 -->	
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'2037'"/>
								<xsl:with-param name="node" select="$codigoTributo"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $codigoTributo)"/>
							</xsl:call-template>	
																						
							<!-- 35. Código de tributo por línea - Catálogo No. 05  -->					
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'2036'" />
								<xsl:with-param name="node" select="$codigoTributo"/>
								<xsl:with-param name="regexp" select="'^(1000|1016|2000|9995|9996|9997|9998|9999)$'" />
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $codigoTributo)"/>
							</xsl:call-template>	
																	
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4255'" />
								<xsl:with-param name="node" select="$codigoTributo_schemeName" />
								<xsl:with-param name="regexp" select="'^(Codigo de tributos)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', $codigoTributo_schemeName)"/>
							</xsl:call-template>		
							
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4256'" />
								<xsl:with-param name="node" select="$codigoTributo_schemeAgencyName" />
								<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', $codigoTributo_schemeAgencyName)"/>
							</xsl:call-template>
						
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4257'" />
								<xsl:with-param name="node" select="$codigoTributo_schemeURI" />
								<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo05)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', $codigoTributo_schemeURI)"/>
							</xsl:call-template>	
							
							<!-- 35. Afectación al IGV por la línea -->	
							<xsl:if test="($codigoTributo = '1000')">
								<xsl:variable name="dif_MontoBase" select="$valorVentaLinea - $montoBase" />
								<xsl:if test="($dif_MontoBase &lt; -1) or ($dif_MontoBase &gt; 1)">
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'3225'" />
										<xsl:with-param name="errorMessage"
											select="concat('Error en la linea: ', position(), ' ', $dif_MontoBase, ' ',$valorVentaLinea, ' ', $montoBase)" />
									</xsl:call-template>
								</xsl:if>								
							</xsl:if>	
														
							<xsl:if test="not($codigoTributo='9999') and ($montoBase &gt; 0) ">													
								<xsl:call-template name="existAndRegexpValidateElementCount">
									<xsl:with-param name="errorCodeNotExist" select="'2371'"/>
									<xsl:with-param name="errorCodeValidate" select="'2040'" />
									<xsl:with-param name="node" select="$afectacionIGVLinea"/>
									<xsl:with-param name="regexp" select="'^(10|11|12|13|14|15|16|17|20|21|30|31|32|33|34|35|36|40)$'" />
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', position(), ' ', $tipoServicioPublico, ' ', $afectacionIGVLinea)"/>
								</xsl:call-template>																																			
							</xsl:if>
							
							<!-- 36. Monto de Otros Tributos -->
							<xsl:if test="($codigoTributo='9999') and ($montoBase &gt; 0) ">	
								<xsl:variable name="monto_Otros_Tributos" select="($montoBase * $tasaIGV) * 0.01" />																		
							    <xsl:variable name="dif_Monto_Otros_Tributos" select="$monto_Otros_Tributos - $montoIGVLinea" />
								<xsl:if test="($dif_Monto_Otros_Tributos &lt; -1) or ($dif_Monto_Otros_Tributos &gt; 1)">
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'3109'" />
										<xsl:with-param name="errorMessage"
											select="concat('Error en la linea: ', position(), ' ', $dif_Monto_Otros_Tributos, ' ',$montoBase, ' ',$montoIGVLinea, ' ',$monto_Otros_Tributos)" />
									</xsl:call-template>
								</xsl:if>																											
							</xsl:if>							
							
							<xsl:if test="($codigoTributo='9999') and ($afectacionIGVLinea)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3050'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ',$afectacionIGVLinea)" />
								</xsl:call-template>
							</xsl:if>
							
							<!-- 35. Monto de IGV de la línea -->
							<xsl:if test="(($codigoTributo = '9997') or ($codigoTributo = '9998')) and ($montoIGVLinea != 0)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3110'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $montoIGVLinea, ' ', $codigoTributo)"/>
								</xsl:call-template>
							</xsl:if>
							
							<xsl:if test="($codigoTributo = '9996') and ($montoBase &gt; 0) and ($montoIGVLinea = 0)">
								<xsl:if test="($afectacionIGVLinea = '11') or ($afectacionIGVLinea = '12') or ($afectacionIGVLinea = '13')
 									or ($afectacionIGVLinea = '14') or ($afectacionIGVLinea = '15') or ($afectacionIGVLinea = '16')"> 
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'3111'" />
										<xsl:with-param name="errorMessage"
											select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea, ' ', $montoIGVLinea, ' ', $montoBase, ' ', $codigoTributo)"/>
									</xsl:call-template>
								</xsl:if>
							</xsl:if>							

							<xsl:if test="($codigoTributo = '9996') and ($montoBase &gt; 0) and ($montoIGVLinea != 0)">
								<xsl:if test="($afectacionIGVLinea = '31') or ($afectacionIGVLinea = '32') or ($afectacionIGVLinea = '33')
 									or ($afectacionIGVLinea = '34') or ($afectacionIGVLinea = '35') or ($afectacionIGVLinea = '36') or ($afectacionIGVLinea = '21')"> 
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'3110'" />
										<xsl:with-param name="errorMessage"
											select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea, ' ', $montoIGVLinea, ' ', $montoBase, ' ', $codigoTributo)"/>
									</xsl:call-template>
								</xsl:if>
							</xsl:if>							

							<xsl:if test="($codigoTributo = '1000') and ($montoBase &gt; 0) and ($montoIGVLinea = 0)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3111'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea, ' ', $montoIGVLinea, ' ', $montoBase, ' ', $codigoTributo)"/>
								</xsl:call-template>
							</xsl:if>							
							
							<!-- 35. Tasa del IGV -->
							<xsl:if test="($codigoTributo = '9996') and ($montoBase &gt; 0) and ($tasaIGV = 0)">
								<xsl:if test="($afectacionIGVLinea = '11') or ($afectacionIGVLinea = '12') or ($afectacionIGVLinea = '13')
 									or ($afectacionIGVLinea = '14') or ($afectacionIGVLinea = '15') or ($afectacionIGVLinea = '16')"> 
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'2993'" />
										<xsl:with-param name="errorMessage"
											select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea, ' ', $tasaIGV, ' ', $montoBase, ' ', $codigoTributo)"/>
									</xsl:call-template>
								</xsl:if>
							</xsl:if>							
							
							<xsl:if test="($codigoTributo = '1000') and ($montoBase &gt; 0) and ($tasaIGV = 0)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'2993'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea, ' ', $tasaIGV, ' ', $montoBase, ' ', $codigoTributo)"/>
								</xsl:call-template>
							</xsl:if>								
																								
							<!-- 35. Nombre de tributo por línea - Catálogo No. 05 -->		
							<!-- 36. Nombre de tributo -->	
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'2996'"/>
								<xsl:with-param name="node" select="$nombreTributo"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $nombreTributo)"/>
							</xsl:call-template>	
							
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'3051'" />
								<xsl:with-param name="node" select="$nombreTributo"/>
								<xsl:with-param name="regexp" select="'^(IGV|IVAP|ISC|EXP|GRA|EXO|INA|OTROS)$'" />
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $tipoServicioPublico, ' ', $nombreTributo)"/>
							</xsl:call-template>	
							
							<xsl:if test="(($codigoTributo='1000') and ($codigoInternacionalTributo!='VAT')) or
 									(($codigoTributo='1016') and ($codigoInternacionalTributo!='VAT')) or  
 									(($codigoTributo='2000') and ($codigoInternacionalTributo!='EXC')) or 
 									(($codigoTributo='9995') and ($codigoInternacionalTributo!='FRE')) or 
 									(($codigoTributo='9996') and ($codigoInternacionalTributo!='FRE')) or 
 									(($codigoTributo='9997') and ($codigoInternacionalTributo!='VAT')) or 
 									(($codigoTributo='9998') and ($codigoInternacionalTributo!='FRE')) or 
 									(($codigoTributo='9999') and ($codigoInternacionalTributo!='OTH'))"> 
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'2377'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $codigoInternacionalTributo)"/>
								</xsl:call-template>
							</xsl:if>
												
						</xsl:for-each>
					</xsl:for-each>						
				</xsl:for-each>																																					
			</xsl:for-each>
			
			<!-- 37. Moneda debe ser la misma en todo el documento -->		
			<xsl:if test="($valorVentaLinea_currencyID) and ($tipoMoneda!=$valorVentaLinea_currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$valorVentaLinea_currencyID)" />
				</xsl:call-template>
			</xsl:if>		
						
			<!-- 37. Valor de venta por línea -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2370'"/>
				<xsl:with-param name="node" select="$valorVentaLinea"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $valorVentaLinea)"/>
			</xsl:call-template>	
						
			<!-- 38. Cargo/descuento por ítem -->
			<xsl:for-each select="./cac:Allowancecharge">
				<!-- 38. Indicador de cargo/descuento -->
				<xsl:variable name="indicadorCargoDescuento" select="./cbc:ChargeIndicator"/>	
				<!-- 38. Código de cargo/descuento -->
				<xsl:variable name="codigoCargoDescuento" select="./cbc:AllowanceChargeReasonCode"/>	
				<xsl:variable name="codigoCargoDescuento_listAgencyName" select="./cbc:AllowanceChargeReasonCode/@listAgencyName"/>
				<xsl:variable name="codigoCargoDescuento_listName" select="./cbc:AllowanceChargeReasonCode/@listName"/>
				<xsl:variable name="codigoCargoDescuento_listURI" select="./cbc:AllowanceChargeReasonCode/@listURI"/>				
				<!-- 38. Factor de cargo/descuento -->
				<xsl:variable name="factorCargoDescuento" select="./cbc:MultiplierFactorNumeric"/>	
				<!-- 38. Monto de cargo/descuento -->
				<xsl:variable name="montoCargoDescuento" select="./cbc:Amount"/>	
				<xsl:variable name="montoCargoDescuento_currencyID" select="./cbc:Amount/@currencyID"/>
				<!-- 38. Monto base del cargo/descuento -->
				<xsl:variable name="montoBaseCargoDescuento" select="./cbc:BaseAmount"/>
				<xsl:variable name="montoBaseCargoDescuento_currencyID" select="./cbc:BaseAmount/@currencyID"/>				
				
				<!-- 38. Indicador de cargo/descuento -->
				<xsl:if test="not($indicadorCargoDescuento='true')">
					<xsl:call-template name="regexpValidateElementIfExistTrue">
						<xsl:with-param name="errorCodeValidate" select="'3114'"/>
						<xsl:with-param name="node" select="$codigoCargoDescuento"/>
						<xsl:with-param name="regexp" select="'^(47|48)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $indicadorCargoDescuento, ' ',$codigoCargoDescuento)" />
					</xsl:call-template>
				</xsl:if>
			
				<xsl:if test="not($indicadorCargoDescuento='false()')">
					<xsl:call-template name="regexpValidateElementIfExistTrue">
						<xsl:with-param name="errorCodeValidate" select="'3114'"/>
						<xsl:with-param name="node" select="$codigoCargoDescuento"/>
						<xsl:with-param name="regexp" select="'^(00|01)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $indicadorCargoDescuento, ' ',$codigoCargoDescuento)" />
					</xsl:call-template>
				</xsl:if>
			
				<!-- 38. Código de cargo/descuento -->
				<xsl:call-template name="existValidateElementNotExist">
					<xsl:with-param name="errorCodeNotExist" select="'3073'"/>
					<xsl:with-param name="node" select="$codigoCargoDescuento" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento)"/>
				</xsl:call-template>
			
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2954'"/>
					<xsl:with-param name="node" select="$codigoCargoDescuento"/>
					<xsl:with-param name="regexp" select="'^(00|01|02|03|04|05|06|45|46|47|48|49|50|51|52|53)$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento)"/>
				</xsl:call-template>
			
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4268'"/>
					<xsl:with-param name="node" select="$codigoCargoDescuento"/>
					<xsl:with-param name="regexp" select="'^(00|01|47|48)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento)"/>
				</xsl:call-template>
			
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4251'" />
					<xsl:with-param name="node" select="$codigoCargoDescuento_listAgencyName" />
					<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento_listAgencyName)"/>
				</xsl:call-template>		
		
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4252'" />
					<xsl:with-param name="node" select="$codigoCargoDescuento_listName" />
					<xsl:with-param name="regexp" select="'^(Cargo/descuento)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento_listName)"/>
				</xsl:call-template>
	
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4253'" />
					<xsl:with-param name="node" select="$codigoCargoDescuento_listURI" />
					<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo53)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento_listURI)"/>
				</xsl:call-template>	
				
				<!-- 38. Factor de cargo/descuento -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'3052'"/>
					<xsl:with-param name="node" select="$factorCargoDescuento"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,3}(\.[0-9]{1,5})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $factorCargoDescuento)"/>
				</xsl:call-template>
				
				<!-- 38. Monto de cargo/descuento -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2955'"/>
					<xsl:with-param name="node" select="$montoCargoDescuento"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $montoCargoDescuento)"/>
				</xsl:call-template>
				
				<xsl:if test="($codigoCargoDescuento)">
					<xsl:variable name="calculoMontoCargoDescuento" select="($montoBaseCargoDescuento * $factorCargoDescuento)"/>
			   		<xsl:variable name="dif_MontoCargoDescuento" select="$montoCargoDescuento - $calculoMontoCargoDescuento" />
					<xsl:if test="($dif_MontoCargoDescuento &lt; -1) or ($dif_MontoCargoDescuento &gt; 1)">
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="'4289'" />
							<xsl:with-param name="warningMessage"
								select="concat('Error en la linea: ', position(), ' ', $dif_MontoCargoDescuento, ' ', $montoCargoDescuento, ' ', $montoBaseCargoDescuento)"/>
						</xsl:call-template>
					</xsl:if>
				</xsl:if>
				
				<xsl:if test="($montoCargoDescuento_currencyID) and not($tipoMoneda=$montoCargoDescuento_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$montoCargoDescuento_currencyID)" />
					</xsl:call-template>
				</xsl:if>	
				
				<!-- 38. Monto base del cargo/descuento -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'3053'"/>
					<xsl:with-param name="node" select="$montoBaseCargoDescuento"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $montoBaseCargoDescuento)"/>
				</xsl:call-template>
				
				<xsl:if test="($montoBaseCargoDescuento_currencyID) and not($tipoMoneda=$montoBaseCargoDescuento_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$montoBaseCargoDescuento_currencyID)" />
					</xsl:call-template>
				</xsl:if>	
			</xsl:for-each>								
		</xsl:for-each>	
			
		<!-- Totales de la Factura -->		
		<xsl:variable name="countTaxTotalLine" select="count(cac:InvoiceLine/cac:TaxTotal)"/>	
		<xsl:if test="($countTaxTotalLine=0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2956'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countTaxTotalLine)" />
			</xsl:call-template>
		</xsl:if>			
		
		<xsl:variable name="countTaxTotal" select="count(cac:TaxTotal)"/>
		<xsl:if test="($countTaxTotal &gt; 1)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3024'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countTaxTotal)" />
			</xsl:call-template>
		</xsl:if>	
															
		<xsl:if
			test="($countCodigoTributo1000 &gt; 1) or ($countCodigoTributo1016 &gt; 1) or ($countCodigoTributo2000 &gt; 1) or ($countCodigoTributo9995 &gt; 1) 
 				or ($countCodigoTributo9996 &gt; 1) or ($countCodigoTributo9997 &gt; 1) or ($countCodigoTributo9998 &gt; 1) or ($countCodigoTributo9999 &gt; 1)"> 
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3068'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countCodigoTributo1000, ' ', $countCodigoTributo2000, ' ', $countCodigoTributo9999)"/>
			</xsl:call-template>
		</xsl:if>	
				
		<xsl:for-each select="cac:TaxTotal">
			<!-- 39. Monto total de impuestos -->
			<xsl:variable name="montoTotalImpuestos" select="./cbc:TaxAmount" />
			<xsl:variable name="montoTotalImpuestos_currencyID" select="./cbc:TaxAmount/@currencyID" />	
			<!-- 40. Total valor de venta - operaciones inafectas -->
			<!-- 41. Total valor de venta - operaciones exoneradas -->
			<!-- 42. Total valor de venta - operaciones gratuitas -->
			<!-- 43. Total valor de venta - operaciones gravadas (IGV o IVAP) -->
			<!-- 44. Total Importe IGV -->
			<!-- 46. Sumatoria Otros Tributos -->	
						
			<!-- 38. Monto total de impuestos -->	
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'3020'"/>
				<xsl:with-param name="node" select="$montoTotalImpuestos"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $montoTotalImpuestos)"/>
			</xsl:call-template>
			
			<xsl:if test="($montoTotalImpuestos)">
				<xsl:variable name="montoImporteTributoFor" select="$importeTributo_1000 + $importeTributo_9999" />
				<xsl:variable name="dif_MontoTotalImpuestos" select="$montoTotalImpuestos - $montoImporteTributoFor" />
				<xsl:if test="($dif_MontoTotalImpuestos &lt; -1) or ($dif_MontoTotalImpuestos &gt; 1)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4301'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), ' ', $dif_MontoTotalImpuestos, ' ', $montoTotalImpuestos, ' ', $montoImporteTributoFor)"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:if>
			
			<xsl:if test="($montoTotalImpuestos_currencyID) and not($tipoMoneda=$montoTotalImpuestos_currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$montoTotalImpuestos_currencyID)" />
				</xsl:call-template>
			</xsl:if>		
							
			<xsl:for-each select="./cac:TaxSubtotal">	
				<!-- 40 / 41. Total Valor de Venta -->
				<!-- 42. Total valor de venta - operaciones gratuitas -->
				<!-- 43. Total valor de venta operaciones gravadas -->
				<!-- 44. Total importe IGV -->
				<!-- 45  Monto base -->
				<xsl:variable name="totalValorVentaItem" select="./cbc:TaxableAmount" />		
				<xsl:variable name="totalValorVentaItem_currencyID" select="./cbc:TaxableAmount/@currencyID" />
				<!-- 40 / 43. Importe del tributo -->
				<!-- 44. Total importe IGV -->
				<!-- 45. Monto de la Sumatoria Otros tributos -->
				<xsl:variable name="importeTributo" select="./cbc:TaxAmount" />
				<xsl:variable name="importeTributo_currencyID" select="./cbc:TaxAmount/@currencyID" />		
									
				<!-- 39 / 42. -->
				<xsl:call-template name="existValidateElementNotExist">
					<xsl:with-param name="errorCodeNotExist" select="'3003'"/>
					<xsl:with-param name="node" select="$totalValorVentaItem" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $totalValorVentaItem)"/>
				</xsl:call-template>
				
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2999'"/>
					<xsl:with-param name="node" select="$totalValorVentaItem"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $totalValorVentaItem)"/>
				</xsl:call-template>
										
				<xsl:if test="($totalValorVentaItem_currencyID) and not($tipoMoneda=$totalValorVentaItem_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$totalValorVentaItem_currencyID)" />
					</xsl:call-template>
				</xsl:if>
						
				<!-- 40 / 45. -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2048'"/>
					<xsl:with-param name="node" select="$importeTributo"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $importeTributo)"/>
				</xsl:call-template>
								
				<xsl:if test="($importeTributo_currencyID) and not($tipoMoneda=$importeTributo_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$importeTributo_currencyID)" />
					</xsl:call-template>
				</xsl:if>							
							
				<xsl:for-each select="./cac:TaxCategory">
					<xsl:for-each select="./cac:TaxScheme">						
						<!-- 40 / 45. Código de tributo -->
						<xsl:variable name="codigoTributoTotal" select="./cbc:ID" />
						<xsl:variable name="codigoTributoTotal_schemeName" select="./cbc:ID/@schemeName" />
						<xsl:variable name="codigoTributoTotal_schemeAgencyName" select="./cbc:ID/@schemeAgencyName" />
						<xsl:variable name="codigoTributoTotal_schemeURI" select="./cbc:ID/@schemeURI" />
						<!-- 40 / 45. Nombre de tributo -->
						<xsl:variable name="nombreTributoTotal" select="./cbc:Name" />
						<!-- 40 / 45. Código internacional tributo -->
						<xsl:variable name="codigoInternacionalTributoTotal" select="./cbc:TaxTypeCode" />
						
						<!-- 40 / 45. Código de tributo -->
 						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3059'"/>
							<xsl:with-param name="node" select="$codigoTributoTotal"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $codigoTributoTotal)"/>
						</xsl:call-template>
						
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'3007'"/>
							<xsl:with-param name="node" select="$codigoTributoTotal"/>
							<xsl:with-param name="regexp" select="'^(1000|1016|2000|9995|9996|9997|9998|9999)$'" />
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $codigoTributoTotal)"/>
						</xsl:call-template>
							
						<xsl:if test="($codigoTributoTotal='9997')">
							<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumMontoBase9997For" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4297'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumMontoBase9997For)"/>
								</xsl:call-template>
							</xsl:if>			
						</xsl:if>
						
						<xsl:if test="($codigoTributoTotal='9998')">
							<xsl:variable name="dif_TotalValorVenta" select="($totalValorVentaItem + $sumMontoBase9998For) - $sumMontoBase1016For" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4296'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumMontoBase9998For, ' ', $sumMontoBase1016For)"/>
								</xsl:call-template>
							</xsl:if>			
						</xsl:if>

						<!-- 42. Total Valor de Venta -->
						<xsl:if test="($codigoTributoTotal='9996')">
							<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumMontoBase9996For" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4298'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumMontoBase9996For)"/>
								</xsl:call-template>
							</xsl:if>			
						</xsl:if>
						
 						<xsl:if test="($codigoTributoTotal='9996') and ($countCodigoPrecio02For &gt; 0) and ($totalValorVentaItem=0)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2641'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTributoTotal, ' ', $countCodigoPrecio02For, ' ', $totalValorVentaItem)"/>
							</xsl:call-template>
						</xsl:if>
						
						<xsl:if test="($codigoTributoTotal='9996') and ($totalValorVentaItem=0)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2416'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTributoTotal, ' ', $totalValorVentaItem)"/>
							</xsl:call-template>
						</xsl:if>
						
						<!-- 43 / 44. Total valor de venta operaciones gravadas -->
						<xsl:if test="($codigoTributoTotal='1000') and ($totalValorVentaItem)">
							<xsl:variable name="dif_TotalValorVenta" select="($totalValorVentaItem - $sumMontoBase1000For) - ($sumMontoCargoDescuento02For + $sumMontoCargoDescuento04For) + $sumMontoCargoDescuento49For" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4299'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumMontoBase1000For)"/>
								</xsl:call-template>
							</xsl:if>			
						</xsl:if>
						
						<xsl:if test="($codigoTributoTotal='9999') and ($totalValorVentaItem)">
							<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumMontoBase9999For" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4304'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumMontoBase9999For)"/>
								</xsl:call-template>
							</xsl:if>			
						</xsl:if>
						
						<!-- 41 / 43. Importe del tributo -->
						<xsl:if test="($importeTributo) and not($importeTributo=0) and (($codigoTributoTotal='9997') or ($codigoTributoTotal='9998'))">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3000'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $importeTributo, ' ',$codigoTributoTotal)" />
							</xsl:call-template>
						</xsl:if>
				
						<xsl:if test="($codigoTributoTotal='9996')">
							<xsl:variable name="dif_ImporteTributo" select="$importeTributo - $sumMontoTributo9996For" />
							<xsl:if test="($dif_ImporteTributo &lt; -1) or ($dif_ImporteTributo &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4311'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_ImporteTributo, ' ', $importeTributo, ' ', $sumMontoTributo9996For)"/>
								</xsl:call-template>
							</xsl:if>			
						</xsl:if>
						
						<!-- 43 / 44. Total Importe de IGV o IVAP, según corresponda -->
						<xsl:if test="($codigoTributoTotal='1000')">
							<xsl:variable name="calculo_ImporteTributo" select="(($sumMontoBase1000For - ($sumMontoCargoDescuento02For + $sumMontoCargoDescuento04For)) + $sumMontoCargoDescuento49For) * 0.18" />
							<xsl:variable name="dif_ImporteTributo" select="$importeTributo - $calculo_ImporteTributo" />
							<xsl:if test="($dif_ImporteTributo &lt; -1) or ($dif_ImporteTributo &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4290'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_ImporteTributo, ' ', $importeTributo, ' ', $calculo_ImporteTributo)"/>
								</xsl:call-template>
							</xsl:if>			
						</xsl:if>			
						
						<xsl:if test="($codigoTributoTotal='9999')">				
							<xsl:variable name="dif_ImporteTributo" select="$importeTributo - $sumMontoTributo9999For" />
							<xsl:if test="($dif_ImporteTributo &lt; -1) or ($dif_ImporteTributo &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4306'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $codigoTributoTotal, ' ', $dif_ImporteTributo, ' ', $importeTributo, ' ', $sumMontoTributo9999For)"/>
								</xsl:call-template>
							</xsl:if>			
						</xsl:if>	
						
						<!-- 40 / 45. Código de tributo -->																		
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4255'" />
							<xsl:with-param name="node" select="$codigoTributoTotal_schemeName" />
							<xsl:with-param name="regexp" select="'^(Codigo de tributos)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $codigoTributoTotal_schemeName)"/>
						</xsl:call-template>		
						
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4256'" />
							<xsl:with-param name="node" select="$codigoTributoTotal_schemeAgencyName" />
							<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $codigoTributoTotal_schemeAgencyName)"/>
						</xsl:call-template>
					
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4257'" />
							<xsl:with-param name="node" select="$codigoTributoTotal_schemeURI" />
							<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo05)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $codigoTributoTotal_schemeURI)"/>
						</xsl:call-template>															
						
						<!-- 40 / 45. Nombre de tributo -->
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'2054'"/>
							<xsl:with-param name="node" select="$nombreTributoTotal"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $nombreTributoTotal)"/>
						</xsl:call-template>
						
						<xsl:if test="($codigoTributoTotal='1000' and not($nombreTributoTotal='IGV')) or ($codigoTributoTotal='1016' and not($nombreTributoTotal='IVAP'))
  								or ($codigoTributoTotal='2000' and not($nombreTributoTotal='ISC')) or ($codigoTributoTotal='9995' and not($nombreTributoTotal='EXP'))  
  								or ($codigoTributoTotal='9996' and not($nombreTributoTotal='GRA')) or ($codigoTributoTotal='9997' and not($nombreTributoTotal='EXO'))  
  								or ($codigoTributoTotal='9998' and not($nombreTributoTotal='INA')) or ($codigoTributoTotal='9999' and not($nombreTributoTotal='OTROS'))">	
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2964'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTributoTotal, ' ', $nombreTributoTotal)"/>
							</xsl:call-template>
						</xsl:if>			
						
						<!-- 40 / 45. Código internacional tributo -->
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'2052'"/>
							<xsl:with-param name="node" select="$codigoInternacionalTributoTotal"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $codigoInternacionalTributoTotal)"/>
						</xsl:call-template>
			
						<xsl:if test="($codigoTributoTotal='1000' and not($codigoInternacionalTributoTotal='VAT')) or ($codigoTributoTotal='1016' and not($codigoInternacionalTributoTotal='VAT'))
  								or ($codigoTributoTotal='2000' and not($codigoInternacionalTributoTotal='EXC')) or ($codigoTributoTotal='9995' and not($codigoInternacionalTributoTotal='FRE'))  
  								or ($codigoTributoTotal='9996' and not($codigoInternacionalTributoTotal='FRE')) or ($codigoTributoTotal='9997' and not($codigoInternacionalTributoTotal='VAT'))  
 								or ($codigoTributoTotal='9998' and not($codigoInternacionalTributoTotal='FRE')) or ($codigoTributoTotal='9999' and not($codigoInternacionalTributoTotal='OTH'))">						  
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2961'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTributoTotal, ' ', $codigoInternacionalTributoTotal)"/>
							</xsl:call-template>
 						</xsl:if>																									  
					</xsl:for-each>								
				</xsl:for-each>								
			</xsl:for-each>								
		</xsl:for-each>				

		<!-- 46. Cargos y Descuentos Globales -->
		<!-- Información Adicional - Percepciones -->	
		<xsl:for-each select="cac:Allowancecharge">
			<!-- 46. Indicador de cargo/descuento -->		
			<xsl:variable name="indicadorCargoDescuento" select="./cbc:ChargeIndicator"/>	
			<!-- 46. Código de cargo/descuento -->
			<xsl:variable name="codigoCargoDescuento" select="./cbc:AllowanceChargeReasonCode"/>	
			<xsl:variable name="codigoCargoDescuento_listAgencyName" select="./cbc:AllowanceChargeReasonCode/@listAgencyName"/>
			<xsl:variable name="codigoCargoDescuento_listName" select="./cbc:AllowanceChargeReasonCode/@listName"/>
			<xsl:variable name="codigoCargoDescuento_listURI" select="./cbc:AllowanceChargeReasonCode/@listURI"/>
			<!-- 46. Porcentaje aplicado -->
			<xsl:variable name="porcentajeAplicado" select="./cbc:MultiplierFactorNumeric"/>	
			<!-- 46. Monto de cargo/descuento -->
			<xsl:variable name="montoCargoDescuento" select="./cbc:Amount"/>	
			<xsl:variable name="montoCargoDescuento_currencyID" select="./cbc:Amount/@currencyID"/>
			<!-- 46. Monto base del cargo/descuento -->
			<xsl:variable name="montoBaseCargoDescuento" select="./cbc:BaseAmount"/>
			<xsl:variable name="montoBaseCargoDescuento_currencyID" select="./cbc:BaseAmount/@currencyID"/>
				
			<!-- 46. Indicador de cargo/descuento -->
			<xsl:if test="not($indicadorCargoDescuento='true')">
				<xsl:call-template name="regexpValidateElementIfExistTrue">
					<xsl:with-param name="errorCodeValidate" select="'3114'"/>
					<xsl:with-param name="node" select="$codigoCargoDescuento"/>
					<xsl:with-param name="regexp" select="'^(45|46|50|51|52|53)$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $indicadorCargoDescuento, ' ',$codigoCargoDescuento)" />
				</xsl:call-template>
			</xsl:if>
								
			<xsl:if test="not($indicadorCargoDescuento='false')">
				<xsl:call-template name="regexpValidateElementIfExistTrue">
					<xsl:with-param name="errorCodeValidate" select="'3114'"/>
					<xsl:with-param name="node" select="$codigoCargoDescuento"/>
					<xsl:with-param name="regexp" select="'^(02|03|04)$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $indicadorCargoDescuento, ' ',$codigoCargoDescuento)" />
				</xsl:call-template>
			</xsl:if>
				
			<!-- 46. Código de cargo/descuento -->
			<xsl:if test="($indicadorCargoDescuento)">
				<xsl:call-template name="existValidateElementNotExist">
					<xsl:with-param name="errorCodeNotExist" select="'3072'"/>
					<xsl:with-param name="node" select="$codigoCargoDescuento" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento)"/>
				</xsl:call-template>
			</xsl:if>	
				
			<xsl:call-template name="regexpValidateElementIfExistTrue">
				<xsl:with-param name="errorCodeValidate" select="'4268'"/>
				<xsl:with-param name="node" select="$codigoCargoDescuento"/>
				<xsl:with-param name="regexp" select="'^(00|01|47|48|45|46|51|52|53)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento)"/>
			</xsl:call-template>
				
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'3071'"/>
				<xsl:with-param name="node" select="$codigoCargoDescuento"/>
				<xsl:with-param name="regexp" select="'^(00|01|02|03|04|05|06|45|46|47|48|49|50|51|52|53)$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento)"/>
			</xsl:call-template>
								
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4251'" />
				<xsl:with-param name="node" select="$codigoCargoDescuento_listAgencyName" />
				<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $codigoCargoDescuento_listAgencyName)"/>
			</xsl:call-template>		
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4252'" />
				<xsl:with-param name="node" select="$codigoCargoDescuento_listName" />
				<xsl:with-param name="regexp" select="'^(Cargo/descuento)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $codigoCargoDescuento_listName)"/>
			</xsl:call-template>
		
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4253'" />
				<xsl:with-param name="node" select="$codigoCargoDescuento_listURI" />
				<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo53)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $codigoCargoDescuento_listURI)"/>
			</xsl:call-template>	
							
			<!-- 46. Factor de cargo/descuento -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'3025'"/>
				<xsl:with-param name="node" select="$porcentajeAplicado"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,3}(\.[0-9]{1,5})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $porcentajeAplicado)"/>
			</xsl:call-template>
							
			<!-- 46. Monto de cargo/descuento -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2968'"/>
				<xsl:with-param name="node" select="$montoCargoDescuento"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $montoCargoDescuento)"/>
			</xsl:call-template>
				
			<xsl:variable name="calculoMontoCargoDescuento" select="($montoBaseCargoDescuento * $porcentajeAplicado)"/>
			<xsl:variable name="dif_MontoCargoDescuento" select="$montoCargoDescuento - $calculoMontoCargoDescuento" />
			<xsl:if test="($dif_MontoCargoDescuento &lt; -1) or ($dif_MontoCargoDescuento &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'3226'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', position(), ' ', $dif_MontoCargoDescuento, ' ', $montoCargoDescuento, ' ', $calculoMontoCargoDescuento)"/>
				</xsl:call-template>
			</xsl:if>
				
			<xsl:if test="($montoCargoDescuento_currencyID) and not($tipoMoneda=$montoCargoDescuento_currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$montoCargoDescuento_currencyID)" />
				</xsl:call-template>
			</xsl:if>	
			
			<!-- 46. Monto base del cargo/descuento -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'3016'"/>
				<xsl:with-param name="node" select="$montoBaseCargoDescuento"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $montoBaseCargoDescuento)"/>
			</xsl:call-template>
				
			<xsl:if test="($montoBaseCargoDescuento_currencyID) and not($tipoMoneda=$montoBaseCargoDescuento_currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$montoBaseCargoDescuento_currencyID)" />
				</xsl:call-template>
			</xsl:if>			
		</xsl:for-each>	
		
		<!-- LegalMonetaryTotal -->
		<xsl:for-each select="cac:LegalMonetaryTotal">									
			<!-- 47. Total Valor de Venta -->
			<xsl:variable name="totalValorVenta" select="./cbc:LineExtensionAmount"/>
			<xsl:variable name="totalValorVenta_currencyID" select="./cbc:LineExtensionAmount/@currencyID"/>
			<!-- 48. Total de precio de venta (incluye impuestos) -->
			<xsl:variable name="totalPrecioVenta" select="./cbc:TaxInclusiveAmount"/>
			<xsl:variable name="totalPrecioVenta_currencyID" select="./cbc:TaxInclusiveAmount/@currencyID"/>
			<!-- 48. Total Descuentos  Globales (Que no afectan la base)-->	
			<xsl:variable name="totalDescuentos" select="./cbc:AllowanceTotalAmount"/>
			<xsl:variable name="totalDescuentos_currencyID" select="./cbc:AllowanceTotalAmount/@currencyID"/>
			<!-- 50. Total cargos (Que no afectan la base) -->
			<xsl:variable name="totalOtrosCargos" select="./cbc:ChargeTotalAmount"/>
			<xsl:variable name="totalOtrosCargos_currencyID" select="./cbc:ChargeTotalAmount/@currencyID"/>			
			<!-- 51. Importe total de la venta, cesión en uso o del servicio prestado -->
			<xsl:variable name="importeTotal" select="./cbc:PayableAmount"/>		
			<xsl:variable name="importeTotal_currencyID" select="./cbc:PayableAmount/@currencyID"/>											
			<!-- 52. Monto para Redondeo del Importe Total -->
			<xsl:variable name="montoRedondeoImporteTotal" select="./cbc:PayableRoundingAmount"/>
			<xsl:variable name="montoRedondeoImporteTotal_currencyID" select="./cbc:PayableRoundingAmount/@currencyID"/>
						
			<!-- 47. Total Valor de Venta -->
			<xsl:call-template name="existValidateElementNotExist">
				<xsl:with-param name="errorCodeNotExist" select="'3083'"/>
				<xsl:with-param name="node" select="$totalValorVenta"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $totalValorVenta)" />
			</xsl:call-template>
			
			<xsl:if test="$totalValorVenta">						
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2031'"/>
					<xsl:with-param name="node" select="$totalValorVenta"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $totalValorVenta)"/>
				</xsl:call-template>
							
				<xsl:variable name="sum_LegalMonetaryTotal" select="$sumValorVentaItem1000For + $sumValorVentaItem1016For + $sumValorVentaItem9995For + sumValorVentaItem9997For + sumValorVentaItem9998For"/>
				<xsl:variable name="dif_LegalMonetaryTotal" select="$totalValorVenta - (($sum_LegalMonetaryTotal - $sumMontoCargoDescuento02For) + $sumMontoCargoDescuento49For)" />
				<xsl:if test="($dif_LegalMonetaryTotal &lt; -1) or ($dif_LegalMonetaryTotal &gt; 1)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4309'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), ' ', $dif_LegalMonetaryTotal, ' ', $totalValorVenta, ' ', $sum_LegalMonetaryTotal, ' ', $sumMontoCargoDescuento02For, ' ', $sumMontoCargoDescuento49For)"/>
					</xsl:call-template>
				</xsl:if>
	
				<!-- Moneda debe ser la misma en todo el documento -->	
				<xsl:if test="($totalValorVenta_currencyID) and not($tipoMoneda=$totalValorVenta_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$totalValorVenta_currencyID)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>							
						
			<!-- 48. Total de precio de venta (incluye impuestos) -->
			<xsl:call-template name="existValidateElementNotExist">
				<xsl:with-param name="errorCodeNotExist" select="'3085'"/>
				<xsl:with-param name="node" select="$totalPrecioVenta"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $totalPrecioVenta)" />
			</xsl:call-template>
							
			<xsl:if test="$totalPrecioVenta">			
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'3019'"/>
					<xsl:with-param name="node" select="$totalPrecioVenta"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $totalPrecioVenta)"/>
				</xsl:call-template>
				
				<!-- 4310 -->
	
				<!-- Moneda debe ser la misma en todo el documento -->	
				<xsl:if test="($totalPrecioVenta_currencyID) and not($tipoMoneda=$totalPrecioVenta_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$totalPrecioVenta_currencyID)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>							
																												
			<!-- 49. Total Descuentos  Globales (Que no afectan la base)-->				
			<xsl:if test="$totalDescuentos">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2065'"/>
					<xsl:with-param name="node" select="$totalDescuentos"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $totalDescuentos)"/>
				</xsl:call-template>
	
				<xsl:variable name="dif_LegalMonetaryTotal" select="$totalDescuentos - ($sumMontoCargoDescuento01For + $sumMontoCargoDescuento03)" />
				<xsl:if test="($dif_LegalMonetaryTotal &lt; -1) or ($dif_LegalMonetaryTotal &gt; 1)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4307'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), ' ', $dif_LegalMonetaryTotal, ' ', $totalDescuentos, ' ', $sumMontoCargoDescuento01For, ' ', $sumMontoCargoDescuento03)"/>
					</xsl:call-template>
				</xsl:if>
	
				<!-- 49 Moneda debe ser la misma en todo el documento -->	
				<xsl:if test="($totalDescuentos_currencyID) and not($tipoMoneda=$totalDescuentos_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$totalDescuentos_currencyID)" />
					</xsl:call-template>
				</xsl:if>			
			</xsl:if>
			
			<!-- 50. Total otros Cargos (Que no afectan la base) -->
			<xsl:if test="$totalOtrosCargos">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2064'"/>
					<xsl:with-param name="node" select="$totalOtrosCargos"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $totalOtrosCargos)"/>
				</xsl:call-template>
				
				<xsl:variable name="sum_LegalMonetaryTotal" select="$sumMontoCargoDescuento45 + $sumMontoCargoDescuento46 + $sumMontoCargoDescuento50 + $sumMontoCargoDescuento51 + $sumMontoCargoDescuento52 + $sumMontoCargoDescuento53" />
				<xsl:variable name="dif_LegalMonetaryTotal" select="$totalOtrosCargos - ($sumMontoCargoDescuento48For + $sum_LegalMonetaryTotal)" />
				<xsl:if test="($dif_LegalMonetaryTotal &lt; -1) or ($dif_LegalMonetaryTotal &gt; 1)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4308'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), ' ', $dif_LegalMonetaryTotal, ' ', $totalOtrosCargos, ' ', $sumMontoCargoDescuento48For, ' ', $sum_LegalMonetaryTotal)"/>
					</xsl:call-template>
				</xsl:if>
	
				<!-- Moneda debe ser la misma en todo el documento -->	
				<xsl:if test="($totalOtrosCargos_currencyID) and not($tipoMoneda=$totalOtrosCargos_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$totalOtrosCargos_currencyID)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>
			
			<!-- 51. Importe total de la venta, cesión en uso o del servicio prestado -->
			<xsl:if test="$importeTotal">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2062'"/>
					<xsl:with-param name="node" select="$importeTotal"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $importeTotal)"/>
				</xsl:call-template>
				
				<xsl:variable name="sumTotalDescuentosItem" select="sum(./cbc:AllowanceTotalAmount)"/>
				<xsl:variable name="sumTotalOtrosCargosItem" select="sum(./cbc:ChargeTotalAmount)"/>
				<xsl:variable name="sumTotalValorVentaItem" select="sum(./cbc:LineExtensionAmount)"/>
				<xsl:variable name="sumTotalPrecioVentaItem" select="sum(./cbc:TaxInclusiveAmount)"/>
				<xsl:variable name="sumMontoRedondeoImporteTotalItem" select="sum(./cbc:PayableRoundingAmount)"/>
				<xsl:variable name="sumTotalAnticiposItem" select="sum(./cbc:PaidAmount)" />
			
				<xsl:variable name="sum_LegalMonetaryTotal" select="((($sumTotalPrecioVentaItem + $sumTotalOtrosCargosItem) - $sumTotalDescuentosItem) - $sumTotalAnticiposItem) + $sumMontoRedondeoImporteTotalItem" />
				<xsl:variable name="dif_LegalMonetaryTotal" select="($importeTotal - $sum_LegalMonetaryTotal)" />
				<xsl:if test="($dif_LegalMonetaryTotal &lt; -1) or ($dif_LegalMonetaryTotal &gt; 1)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4312'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), ' ', $dif_LegalMonetaryTotal, ' ', $importeTotal, ' ', $sum_LegalMonetaryTotal, ' ', $sumTotalPrecioVentaItem, ' ', $sumTotalOtrosCargosItem, ' ', $sumTotalDescuentosItem, ' ', $sumTotalAnticiposItem, ' ', $sumMontoRedondeoImporteTotalItem)"/>
					</xsl:call-template>
				</xsl:if>
					
				<!-- Moneda debe ser la misma en todo el documento -->	
				<xsl:if test="($importeTotal_currencyID) and not($tipoMoneda=$importeTotal_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$importeTotal_currencyID)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>
										
			<!-- 52. Monto para Redondeo del Importe Total -->
			<xsl:if test="($montoRedondeoImporteTotal)">
				<xsl:if test="($montoRedondeoImporteTotal &lt; -1) or ($montoRedondeoImporteTotal &gt; 1)">				
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4314'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), ' ', $montoRedondeoImporteTotal)" />
					</xsl:call-template>				
				</xsl:if>			
			</xsl:if>
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4315'" />
				<xsl:with-param name="node" select="$montoRedondeoImporteTotal_currencyID" />
				<xsl:with-param name="regexp" select="'^(Tipo de moneda)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $montoRedondeoImporteTotal_currencyID)"/>
			</xsl:call-template>					
		</xsl:for-each>			
		
		<xsl:copy-of select="." />
				
	</xsl:template>
	
</xsl:stylesheet>
