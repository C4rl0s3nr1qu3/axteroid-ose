<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns="urn:oasis:names:specification:ubl:schema:xsd:Invoice-2"
	xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
	xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
	xmlns:ext="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2"
	xmlns:ccts="urn:un:unece:uncefact:documentation:2"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:dp="http://www.datapower.com/extensions"
	extension-element-prefixes="dp" exclude-result-prefixes="dp">

	<xsl:include href="../util/validate_utils.xsl" dp:ignore-multiple="yes" />

	<!-- key Duplicados -->
	<xsl:key name="by-document-despatch-reference" match="*[local-name()='Invoice']/cac:DespatchDocumentReference"
		use="concat(cbc:DocumentTypeCode,' ', cbc:ID)" />
	<xsl:key name="by-document-additional-reference" match="*[local-name()='Invoice']/cac:AdditionalDocumentReference"
		use="concat(cbc:DocumentTypeCode,' ', cbc:ID)" />
	<xsl:key name="by-invoiceLine-id" match="*[local-name()='Invoice']/cac:InvoiceLine" use="number(cbc:ID)" />
	<xsl:key name="by-prepaidPayment-PaidAmount" match="*[local-name()='Invoice']/cac:PrepaidPayment"
		use="cbc:PaidAmount" />
	<xsl:key name="by-additionalDocumentReference-DocumentStatusCode" match="*[local-name()='Invoice']/cac:AdditionalDocumentReference"
		use="cbc:DocumentStatusCode" />
	<xsl:key name="by-document-additional-anticipo" match="*[local-name()='Invoice']/cac:AdditionalDocumentReference[cbc:DocumentTypeCode[text() = '02' or text() = '03']]"
		use="cbc:DocumentStatusCode" />
	<xsl:key name="by-idprepaid-in-root" match="*[local-name()='Invoice']/cac:PrepaidPayment" use="cbc:ID" />

	<!-- Parameter -->
	<xsl:param name="nombreArchivoEnviado" />
	<xsl:param name="porcentajeIGV" />

	<xsl:template match="/*">
		<!-- Ini validacion del nombre del archivo vs el nombre del cbc:ID -->
		<xsl:variable name="rucFilename" select="substring($nombreArchivoEnviado, 1, 11)" />
		<xsl:variable name="idFilename" select="substring($nombreArchivoEnviado, 13, string-length($nombreArchivoEnviado) - 16)" />
		<xsl:variable name="fechaEnvioFile" select="substring($nombreArchivoEnviado, 16, 8)" />
		<xsl:variable name="tipoComprobante" select="substring($nombreArchivoEnviado, 13, 2)" />
		<xsl:variable name="numeroSerie" select="substring($nombreArchivoEnviado, 16, 4)" />
		<xsl:variable name="numeroComprobante" select="substring($nombreArchivoEnviado, 21, string-length($nombreArchivoEnviado) - 24)" />
		<xsl:variable name="numeroRuc" select="substring($nombreArchivoEnviado, 1, 11)" />
		
		<xsl:variable name="porcentajeIGVVigente">	
			<xsl:choose>
				<xsl:when test="number($porcentajeIGV) = 0.10">
					<xsl:value-of select="10"/></xsl:when>
				<xsl:when test="number($porcentajeIGV) = 0.18">
					<xsl:value-of select="18"/></xsl:when>				    										    										    										    						
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>       
		</xsl:variable>	
		
		<xsl:variable name="porcentajeIGVVigenteLinea" select="max(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory[cac:TaxScheme/cbc:ID[text() = '1000' or text() = '9996']]/cbc:Percent)"/>
		<xsl:variable name="porcentajeIGVVigenteLineaDecimal">	
			<xsl:choose>
				<xsl:when test="number($porcentajeIGVVigenteLinea) = 10">
					<xsl:value-of select="0.10"/></xsl:when>
				<xsl:when test="number($porcentajeIGVVigenteLinea) = 18">
					<xsl:value-of select="0.18"/></xsl:when>				    										    										    										    						
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>       
		</xsl:variable>			
		<!-- Fin validacion del nombre del archivo vs el nombre del cbc:ID -->

		<!-- Variables -->
		<!-- 1. Versión del UBL -->
		<xsl:variable name="cbcUBLVersionID" select="cbc:UBLVersionID" />
		<!-- 2. Versión de la estructura del documento -->
		<xsl:variable name="cbcCustomizationID" select="cbc:CustomizationID" />
		<xsl:variable name="cbcCustomizationID_SchemeAgencyName" select="cbc:CustomizationID/@schemeAgencyName" />
		<!-- 3. Numeración, conformada por serie y número correlativo -->
		<xsl:variable name="cbcID" select="cbc:ID" />
		<!-- 4. Fecha de emisión -->
		<xsl:variable name="cbcIssueDate" select="cbc:IssueDate" />
		<xsl:variable name="currentdate" select="fn:current-date()"></xsl:variable>
		<xsl:variable name="yearIssueDate" select="substring($cbcIssueDate, 1, 4)" />										
		<xsl:variable name="montoBolsaPlasticoLinea">	
			<xsl:choose>
				<xsl:when test="number($yearIssueDate) = 2019">
					<xsl:value-of select="0.10"/></xsl:when>
				<xsl:when test="number($yearIssueDate) = 2020">
					<xsl:value-of select="0.20"/></xsl:when>
				<xsl:when test="number($yearIssueDate) = 2021">
					<xsl:value-of select="0.30"/></xsl:when>
				<xsl:when test="number($yearIssueDate) = 2022">
					<xsl:value-of select="0.40"/></xsl:when>
				<xsl:when test="number($yearIssueDate) &gt;= 2023">
					<xsl:value-of select="0.50"/></xsl:when>				    										    										    										    						
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>       
		</xsl:variable>
		<!-- 6. Tipo de documento -->
		<xsl:variable name="tipoDocumento" select="cbc:InvoiceTypeCode" />
		<xsl:variable name="tipoDocumento_listAgencyName" select="cbc:InvoiceTypeCode/@listAgencyName" />
		<xsl:variable name="tipoDocumento_listName" select="cbc:InvoiceTypeCode/@listName" />
		<xsl:variable name="tipoDocumento_listURI" select="cbc:InvoiceTypeCode/@listURI" />
		<!-- 7. Tipo de moneda -->
		<xsl:variable name="tipoMoneda" select="cbc:DocumentCurrencyCode" />
		<xsl:variable name="tipoMoneda_ListID" select="cbc:DocumentCurrencyCode/@listID" />
		<xsl:variable name="tipoMoneda_ListName" select="cbc:DocumentCurrencyCode/@listName" />
		<xsl:variable name="tipoMoneda_ListAgencyName" select="cbc:DocumentCurrencyCode/@listAgencyName" />
		<!-- 8. Fecha de Vencimiento -->
		<xsl:variable name="fechaVencimiento" select="cbc:DueDate" />
		<!-- 12. Domicilio Fiscal -->
		<!-- <xsl:variable name="emisorDireccionCompleta" select="cac:AccountingSupplierParty/cac:Party/cac:PartyLegalEntity/cac:RegistrationAddress/cac:AddressLine/cbc:Line"/> -->
		<!-- 13. Dirección del lugar en el que se entrega el bien -->
		<xsl:variable name="emisorDeliveryDireccionCompleta"
			select="cac:Delivery/cac:DeliveryLocation/cac:Address/cac:AddressLine/cbc:Line" />

		<!-- 28. Número de placa del vehículo (Información Adicional - Gastos art.37° 
			Renta) -->
		<!-- 28. Fecha -->
		<xsl:variable name="fechaCodigoConcepto4003For" select="cac:InvoiceLine/cac:Item/cac:AdditionalItemProperty[cbc:NameCode='4003']/cac:UsabilityPeriod/cbc:StartDate" />
		<!-- 30. Valor de venta por ítem -->
		<xsl:variable name="sumValorVentaItem1000For" select="sum(cac:InvoiceLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:LineExtensionAmount)" />
		<xsl:variable name="sumValorVentaItem1016For" select="sum(cac:InvoiceLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:LineExtensionAmount)" />
		<xsl:variable name="sumValorVentaItem9995For" select="sum(cac:InvoiceLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='9995']/cbc:LineExtensionAmount)" />
		<xsl:variable name="sumValorVentaItem9996For" select="sum(cac:InvoiceLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='9996']/cbc:LineExtensionAmount)" />
		<xsl:variable name="sumValorVentaItem9997For" select="sum(cac:InvoiceLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='9997']/cbc:LineExtensionAmount)" />
		<xsl:variable name="sumValorVentaItem9998For" select="sum(cac:InvoiceLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='9998']/cbc:LineExtensionAmount)" />

		<!-- 31. Código de precio -->
		<xsl:variable name="countCodigoPrecio01"
			select="count(cac:InvoiceLine/cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='01'])" />
		<xsl:variable name="countCodigoPrecio02"
			select="count(cac:InvoiceLine/cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='02'])" />

		<!-- 34. Monto base -->
		<xsl:variable name="sumMontoBaseFor"
			select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cbc:TaxableAmount)" />
		<!-- 34. Monto base por Código de tributo por línea -->
		<xsl:variable name="sumMontoBase1000Linea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxableAmount)" />
		<xsl:variable name="sumMontoBase1016Linea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:TaxableAmount)" />
		<xsl:variable name="sumMontoBase2000Linea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxableAmount)" />
		<xsl:variable name="sumMontoBase9995Linea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9995']/cbc:TaxableAmount)" />
		<xsl:variable name="sumMontoBase9996Linea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9996']/cbc:TaxableAmount)" />
		<xsl:variable name="sumMontoBase9997Linea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9997']/cbc:TaxableAmount)" />
		<xsl:variable name="sumMontoBase9998Linea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9998']/cbc:TaxableAmount)" />
		<xsl:variable name="sumMontoBase9999Linea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxableAmount)" />

		<xsl:variable name="sumMontoBase2000_9996Linea">	
			<xsl:choose>
				<xsl:when test="($sumMontoBase2000Linea &gt; 0)">					
					<xsl:for-each select="cac:InvoiceLine/cac:TaxTotal">
						<xsl:variable name="count9996" select="count(./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9996'])"/>
				 		<xsl:if test="($count9996 &gt; 0)"> 
							<xsl:value-of select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000']]/cbc:TaxableAmount)"/>
						</xsl:if>	
						<xsl:if test="not($count9996 &gt; 0)">
							<xsl:value-of select="0"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:when>	
				<xsl:otherwise>0</xsl:otherwise>								
			</xsl:choose>       
		</xsl:variable>
					
		<xsl:variable name="sumMontoBase2000sin9996Linea" select="number($sumMontoBase2000Linea) - number($sumMontoBase2000_9996Linea)"/>
		<xsl:variable name="sumMontoBaseAfectaIGV17For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cbc:TaxExemptionReasonCode='17']/cbc:TaxableAmount)" />
		<xsl:variable name="sumMontoBaseAfectaIGVNot17For" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cbc:TaxExemptionReasonCode!='17']/cbc:TaxableAmount)" />

		<!-- 34. Monto de IGV/IVAP de la línea -->
		<!-- 35. Monto del tributo de la línea -->
		<xsl:variable name="sumMontoTributo1000Linea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxAmount )" />
		<xsl:variable name="sumMontoTributo1016Linea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:TaxAmount )" />
		<xsl:variable name="sumMontoTributo2000Linea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxAmount )" />
		<xsl:variable name="sumMontoTributo9995Linea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9995']/cbc:TaxAmount )" />
		<xsl:variable name="sumMontoTributo9996Linea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9996']/cbc:TaxAmount )" />
		<xsl:variable name="sumMontoTributo9997Linea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9997']/cbc:TaxAmount )" />
		<xsl:variable name="sumMontoTributo9998Linea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9998']/cbc:TaxAmount )" />
		<xsl:variable name="sumMontoTributo9999Linea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxAmount )" />
		<xsl:variable name="sumMontoTributo7152Linea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='7152']/cbc:TaxAmount )" />

		<xsl:variable name="sumMontoTributo2000_9996Linea">	
			<xsl:choose>
				<xsl:when test="($sumMontoTributo2000Linea &gt; 0)">					
					<xsl:for-each select="cac:InvoiceLine/cac:TaxTotal">
						<xsl:variable name="count9996" select="count(./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9996'])"/>
				 		<xsl:if test="($count9996 &gt; 0)"> 
							<xsl:value-of select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000']]/cbc:TaxAmount)"/>
						</xsl:if>	
						<xsl:if test="not($count9996 &gt; 0)">
							<xsl:value-of select="0"/>
						</xsl:if>
<!-- 						<xsl:call-template name="addWarning"> -->
<!-- 							<xsl:with-param name="warningCode" select="'9998'" /> -->
<!-- 							<xsl:with-param name="warningMessage" -->
<!-- 								select="concat('Error en la linea ', position(), ' ', $count9996)"/> -->
<!-- 						</xsl:call-template> -->
					</xsl:for-each>
				</xsl:when>	
				<xsl:otherwise>0</xsl:otherwise>								
			</xsl:choose>       
		</xsl:variable>		
		
		<xsl:variable name="sumMontoTributo2000sin9996Linea" select="number($sumMontoTributo2000Linea) - number($sumMontoTributo2000_9996Linea)"/>

		<!-- 37. Monto de cargo/descuento -->
		<xsl:variable name="sumMontoCargoDescuento00For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='00']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento01For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='01']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento02For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='02']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento03For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='03']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento04For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='04']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento05For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='05']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento06For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='06']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento45For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='45']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento46For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='46']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento47For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='47']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento48For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='48']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento49For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='49']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento50For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='50']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento51For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='51']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento52For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='52']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento53For" select="sum(cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='53']/cbc:Amount)" />

		<!-- 38. Monto total de impuestos -->
		<xsl:variable name="sumMontoTotalImpuestos"
			select="sum(cac:TaxTotal/cbc:TaxAmount)" />

		<xsl:variable name="importeTributo_1000" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxAmount)" />
		<xsl:variable name="importeTributo_1016" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:TaxAmount)" />
		<xsl:variable name="importeTributo_2000" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxAmount)" />
		<xsl:variable name="importeTributo_9995" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9995']/cbc:TaxAmount)" />
		<xsl:variable name="importeTributo_9996" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9996']/cbc:TaxAmount)" />
		<xsl:variable name="importeTributo_9997" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9997']/cbc:TaxAmount)" />
		<xsl:variable name="importeTributo_9998" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9998']/cbc:TaxAmount)" />
		<xsl:variable name="importeTributo_9999" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxAmount)" />
		<xsl:variable name="importeTributo_7152" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='7152']/cbc:TaxAmount)"/>
		
		<!-- 39. Total valor de venta -->
		<xsl:variable name="totalValorVenta_1000" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxableAmount)" />
		<xsl:variable name="totalValorVenta_1016" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:TaxableAmount)" />
		<xsl:variable name="totalValorVenta_2000" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxableAmount)" />
		<xsl:variable name="totalValorVenta_9995" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9995']/cbc:TaxableAmount)" />
		<xsl:variable name="totalValorVenta_9996" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9996']/cbc:TaxableAmount)" />
		<xsl:variable name="totalValorVenta_9997" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9997']/cbc:TaxableAmount)" />
		<xsl:variable name="totalValorVenta_9998" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9998']/cbc:TaxableAmount)" />
		<xsl:variable name="totalValorVenta_9999" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxableAmount)" />

		<!-- 39. Count Total valor de venta -->
		<xsl:variable name="countCodigoTributo1000" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])" />
		<xsl:variable name="countCodigoTributo1016" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1016'])" />
		<xsl:variable name="countCodigoTributo2000" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000'])" />
		<xsl:variable name="countCodigoTributo9995" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9995'])" />
		<xsl:variable name="countCodigoTributo9996" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9996'])" />
		<xsl:variable name="countCodigoTributo9997" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9997'])" />
		<xsl:variable name="countCodigoTributo9998" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9998'])" />
		<xsl:variable name="countCodigoTributo9999" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9999'])" />
		<xsl:variable name="countCodigoTributo7152" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='7152'])" />

		<!-- 47. Cargos y Descuentos Globales -->
		<xsl:variable name="sumMontoCargoDescuento00" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='00']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento01" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='01']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento02" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='02']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento03" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='03']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento04" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='04']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento05" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='05']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento06" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='06']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento45" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='45']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento46" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='46']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento47" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='47']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento48" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='48']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento49" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='49']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento50" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='50']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento51" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='51']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento52" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='52']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento53" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='53']/cbc:Amount)" />
		<xsl:variable name="sumMontoCargoDescuento20" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode[text() = '20']]/cbc:Amount)"/>	
		
		<!-- 50. Importe total -->
		<xsl:variable name="sumImporteTotal">
			<xsl:choose>
				<xsl:when test="count(cac:LegalMonetaryTotal/cbc:PayableAmount) &gt; 0">
					<xsl:value-of select="sum(cac:LegalMonetaryTotal/cbc:PayableAmount)" />
				</xsl:when>
				<xsl:otherwise>
					0
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<!-- 54. Código de leyenda -->
		<xsl:variable name="countCodigoLeyenda1000" select="count(cbc:Note[@languageLocaleID='1000'])" />
		<xsl:variable name="countCodigoLeyenda1002" select="count(cbc:Note[@languageLocaleID='1002'])" />
		<xsl:variable name="countCodigoLeyenda2000" select="count(cbc:Note[@languageLocaleID='2000'])" />
		<xsl:variable name="countCodigoLeyenda2001" select="count(cbc:Note[@languageLocaleID='2001'])" />
		<xsl:variable name="countCodigoLeyenda2002" select="count(cbc:Note[@languageLocaleID='2002'])" />
		<xsl:variable name="countCodigoLeyenda2003" select="count(cbc:Note[@languageLocaleID='2003'])" />
		<xsl:variable name="countCodigoLeyenda2004" select="count(cbc:Note[@languageLocaleID='2004'])" />
		<xsl:variable name="countCodigoLeyenda2005" select="count(cbc:Note[@languageLocaleID='2005'])" />
		<xsl:variable name="countCodigoLeyenda2006" select="count(cbc:Note[@languageLocaleID='2006'])" />
		<xsl:variable name="countCodigoLeyenda2007" select="count(cbc:Note[@languageLocaleID='2007'])" />
		<xsl:variable name="countCodigoLeyenda2008" select="count(cbc:Note[@languageLocaleID='2008'])" />
		<xsl:variable name="countCodigoLeyenda2009" select="count(cbc:Note[@languageLocaleID='2009'])" />

		<!-- 52. Tipo de operación -->
		<xsl:variable name="tipoOperacion">
			<xsl:choose>
				<xsl:when test="count(cbc:InvoiceTypeCode/@listID) &gt; 0">
					<xsl:value-of select="cbc:InvoiceTypeCode/@listID" />
				</xsl:when>
				<xsl:otherwise>
					0
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="tipoOperacion_name" select="cbc:InvoiceTypeCode/@name" />
		<xsl:variable name="tipoOperacion_listSchemeURI" select="cbc:InvoiceTypeCode/@listSchemeURI" />

		<!-- 56. Total Anticipos -->
		<xsl:variable name="sumTotalAnticipos">
			<xsl:choose>
				<xsl:when
					test="count(cac:LegalMonetaryTotal/cbc:PrepaidAmount) &gt; 0">
					<xsl:value-of
						select="sum(cac:LegalMonetaryTotal/cbc:PrepaidAmount)" />
				</xsl:when>
				<xsl:otherwise>
					0
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<!-- 89. Código del Bien o Servicio Sujeto a Detracción -->
		<xsl:variable name="codigoBienServicioSujetoDetraccion" select="cac:PaymentTerms/cbc:PaymentMeansID" />

		<!-- 132. Codigo unico de identificación del SPP (CUSPP) -->
		<!-- 133. Periodo -->
		<xsl:variable name="countCodigoConcepto7017Global" select="count(cac:InvoiceLine/cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7017'])"/>
		<xsl:variable name="countCodigoConcepto7018Global" select="count(cac:InvoiceLine/cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7018'])"/>
		<xsl:variable name="countCodigoConcepto7019Global" select="count(cac:InvoiceLine/cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7019'])"/>

		<!-- Validaciones -->
		<!-- 1. Version del UBL -->
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'2075'" />
			<xsl:with-param name="node" select="$cbcUBLVersionID" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $cbcUBLVersionID)" />
		</xsl:call-template>

		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2074'" />
			<xsl:with-param name="node" select="$cbcUBLVersionID" />
			<xsl:with-param name="regexp" select="'^(2.1)$'" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $cbcUBLVersionID)" />
		</xsl:call-template>

		<!-- 2. Version de la Estructura del Documento -->
		<xsl:call-template
			name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist"
				select="'2073'" />
			<xsl:with-param name="node"
				select="$cbcCustomizationID" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $cbcCustomizationID)" />
		</xsl:call-template>

		<xsl:call-template
			name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate"
				select="'2072'" />
			<xsl:with-param name="node"
				select="$cbcCustomizationID" />
			<xsl:with-param name="regexp" select="'^(2.0)$'" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $cbcCustomizationID)" />
		</xsl:call-template>

		<xsl:call-template
			name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate"
				select="'4256'" />
			<xsl:with-param name="node"
				select="$cbcCustomizationID_SchemeAgencyName" />
			<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
			<xsl:with-param name="isError" select="false()" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: 1) ', $cbcCustomizationID_SchemeAgencyName)" />
		</xsl:call-template>

		<!-- 3. Numeración, conformada por serie y número correlativo -->
		<xsl:if test="not($numeroSerie = substring($cbcID, 1, 4))">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'1035'" />
				<xsl:with-param name="errorMessage"
					select="concat('numero de serie del xml diferente al numero de serie del archivo ', substring($cbcID, 1, 4), ' diff ', $numeroSerie)" />
			</xsl:call-template>
		</xsl:if>

		<!-- 3. Numero de documento en el nombre del archivo no coincide con el 
			consignado en el contenido del XML -->
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
			<xsl:with-param name="regexp" select="'^[B][A-Z0-9]{3}-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $cbcID)" />
		</xsl:call-template>

		<!-- 4. Fecha de emisión = Comunicado SUNAT 01/06/2020 - RS 099-2020/SUNAT-->
		<xsl:variable name="t1" select="xs:date($currentdate)-xs:date($cbcIssueDate)" />
		<xsl:variable name="t2" select="fn:days-from-duration(xs:duration($t1))" />				
		<xsl:variable name="t3" select="xs:date($cbcIssueDate)-xs:date('2020-03-09')" />
		<xsl:variable name="t4" select="fn:days-from-duration(xs:duration($t3))" />
		<xsl:variable name="t5" select="xs:date($cbcIssueDate)-xs:date('2020-06-30')" />				
		<xsl:variable name="t6" select="fn:days-from-duration(xs:duration($t5))" />
		<xsl:variable name="t7" select="xs:date($currentdate)-xs:date('2020-03-09')" />					
		<xsl:variable name="t8" select="fn:days-from-duration(xs:duration($t7))" />
		<xsl:variable name="t9" select="xs:date($currentdate)-xs:date('2020-07-10')" />
		<xsl:variable name="t10" select="fn:days-from-duration(xs:duration($t9))" />
		<xsl:variable name="s1C" select="substring($cbcID, 1, 1)" />
	
		<xsl:choose>
			<xsl:when test="(($t10 &lt;= 0) and ($t8 &gt;= 0))">
				<xsl:if test="(($t4 &lt; 0) or ($t6 &gt; 0))">
					<xsl:if test="not(($s1C = '0') or ($s1C = '1') or ($s1C = '2') or 
						($s1C = '3') or ($s1C = '4') or ($s1C = '5') or ($s1C = '6') or ($s1C = '7') or 
						($s1C = '8') or ($s1C = '9'))">			
						<xsl:if test="($t2 &gt; 7)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'1079'" />
								<xsl:with-param name="errorMessage" 
									select="concat('Error en la linea 1) ', $t4, ' ', $t6, ' ', $t8, ' ', $t10, ' : ', $s1C, ' - ', $cbcIssueDate, ' - ', $currentdate, ' - ', $t1, ' - ', $t2)" />
							</xsl:call-template>
						</xsl:if>				
					</xsl:if>			
				</xsl:if>
			</xsl:when>			
			<xsl:otherwise>	
				<xsl:if test="not(($s1C = '0') or ($s1C = '1') or ($s1C = '2') or 
					($s1C = '3') or ($s1C = '4') or ($s1C = '5') or ($s1C = '6') or ($s1C = '7') or 
					($s1C = '8') or ($s1C = '9'))">			
					<xsl:if test="($t2 &gt; 7)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'1079'" />
							<xsl:with-param name="errorMessage" 
								select="concat('Error en la linea 2) ', $t4, ' ', $t6, ' ', $t8, ' ', $t10, ' : ', $s1C, ' - ', $cbcIssueDate, ' - ', $currentdate, ' - ', $t1, ' - ', $t2)" />
						</xsl:call-template>
					</xsl:if>				
				</xsl:if>			
			</xsl:otherwise>
		</xsl:choose>		
		
		<xsl:if test="($t2 &lt; -2)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2329'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $cbcIssueDate, ' - ', $currentdate, ' - ', $t1, ' - ', $t2)" />
			</xsl:call-template>
		</xsl:if>

		<!-- 6. Tipo de documento -->
		<xsl:call-template
			name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist"
				select="'1004'" />
			<xsl:with-param name="errorCodeValidate"
				select="'1003'" />
			<xsl:with-param name="node" select="$tipoDocumento" />
			<xsl:with-param name="regexp" select="'^(03)$'" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $tipoDocumento)" />
		</xsl:call-template>

		<xsl:call-template
			name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate"
				select="'4251'" />
			<xsl:with-param name="node"
				select="$tipoDocumento_listAgencyName" />
			<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
			<xsl:with-param name="isError" select="false()" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: 1) ', $tipoDocumento_listAgencyName)" />
		</xsl:call-template>

		<xsl:call-template
			name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate"
				select="'4252'" />
			<xsl:with-param name="node"
				select="$tipoDocumento_listName" />
			<xsl:with-param name="regexp"
				select="'^(Tipo de Documento)$'" />
			<xsl:with-param name="isError" select="false()" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: 1) ', $tipoDocumento_listName)" />
		</xsl:call-template>

		<xsl:call-template
			name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate"
				select="'4253'" />
			<xsl:with-param name="node"
				select="$tipoDocumento_listURI" />
			<xsl:with-param name="regexp"
				select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo01)$'" />
			<xsl:with-param name="isError" select="false()" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: 1) ', $tipoDocumento_listURI)" />
		</xsl:call-template>

		<!-- 7. Tipo de moneda en la cual se emite la factura electronica -->
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'2070'" />
			<xsl:with-param name="node" select="$tipoMoneda" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $tipoMoneda)" />
		</xsl:call-template>

		<xsl:call-template
			name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate"
				select="'4254'" />
			<xsl:with-param name="node"
				select="$tipoMoneda_ListID" />
			<xsl:with-param name="regexp"
				select="'^(ISO 4217 Alpha)$'" />
			<xsl:with-param name="isError" select="false()" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: 1) ', $tipoMoneda_ListID)" />
		</xsl:call-template>

		<xsl:call-template
			name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate"
				select="'4252'" />
			<xsl:with-param name="node"
				select="$tipoMoneda_ListName" />
			<xsl:with-param name="regexp" select="'^(Currency)$'" />
			<xsl:with-param name="isError" select="false()" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: 2) ', $tipoMoneda_ListName)" />
		</xsl:call-template>

		<xsl:call-template
			name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate"
				select="'4251'" />
			<xsl:with-param name="node"
				select="$tipoMoneda_ListAgencyName" />
			<xsl:with-param name="regexp"
				select="'^(United Nations Economic Commission for Europe)$'" />
			<xsl:with-param name="isError" select="false()" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: 2) ', $tipoMoneda_ListAgencyName)" />
		</xsl:call-template>

		<!-- Datos del ciente o receptor -->
		<xsl:variable name="countEmisorPartyIdentification" 
			select="count(cac:AccountingSupplierParty/cac:Party/cac:PartyIdentification)" />
		<xsl:if test="$countEmisorPartyIdentification>1">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3089'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countEmisorPartyIdentification)" />
			</xsl:call-template>
		</xsl:if>

		<xsl:if test="$countEmisorPartyIdentification = 0">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'1034'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countEmisorPartyIdentification)" />
			</xsl:call-template>
		</xsl:if>

		<!-- 9. Numero de documento de identidad del emisor - RUC -->
		<xsl:for-each select="cac:AccountingSupplierParty">
			<!-- Datos del Emisor -->
			<xsl:for-each select="./cac:Party">
				
				<xsl:for-each select="./cac:PartyIdentification">
					<!-- 9. Número de RUC -->
					<xsl:variable name="emisorNumeroDocumento" select="./cbc:ID" />
					<!-- 9. Tipo de documento de identidad del emisor -->
					<xsl:variable name="emisorTipoDocumento" select="./cbc:ID/@schemeID" />
					<xsl:variable name="emisorNumeroDocumento_SchemeName" select="./cbc:ID/@schemeName" />
					<xsl:variable name="emisorNumeroDocumento_SchemeAgencyName" select="./cbc:ID/@schemeAgencyName" />
					<xsl:variable name="emisorNumeroDocumento_SchemeURI" select="./cbc:ID/@schemeURI" />

					<!-- 9. Número de RUC -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'1008'" />
						<xsl:with-param name="node" select="$emisorTipoDocumento" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $emisorTipoDocumento)" />
					</xsl:call-template>

					<xsl:if test="not($numeroRuc = $emisorNumeroDocumento)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'1034'" />
							<xsl:with-param name="errorMessage"
								select="concat('ruc del xml diferente al nombre del archivo ', $emisorNumeroDocumento, ' diff ', $numeroRuc)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 10. Tipo de documento de identidad del emisor - RUC -->
					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'1007'" />
						<xsl:with-param name="node"
							select="$emisorTipoDocumento" />
						<xsl:with-param name="regexp" select="'^[6]$'" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $emisorTipoDocumento)" />
					</xsl:call-template>

					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'4255'" />
						<xsl:with-param name="node"
							select="$emisorNumeroDocumento_SchemeName" />
						<xsl:with-param name="regexp"
							select="'^(Documento de Identidad)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 1) ', position(), ' ', $emisorNumeroDocumento_SchemeName)" />
					</xsl:call-template>

					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'4256'" />
						<xsl:with-param name="node"
							select="$emisorNumeroDocumento_SchemeAgencyName" />
						<xsl:with-param name="regexp"
							select="'^(PE:SUNAT)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 2) ', position(), ' ', $emisorNumeroDocumento_SchemeAgencyName)" />
					</xsl:call-template>

					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'4257'" />
						<xsl:with-param name="node"
							select="$emisorNumeroDocumento_SchemeURI" />
						<xsl:with-param name="regexp"
							select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo06)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 1) ', position(), ' ', $emisorNumeroDocumento_SchemeURI)" />
					</xsl:call-template>
				</xsl:for-each>

				<xsl:for-each select="./cac:PartyName">
					<!-- 10. Nombre Comercial -->
					<xsl:variable name="emisorNombreComercial"
						select="./cbc:Name" />

					<!-- 10. Nombre Comercial -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4092'" />
						<xsl:with-param name="node" select="$emisorNombreComercial" />
						<!-- <xsl:with-param name="regexp" select="'^[\w\s].{0,1500}$'"/> -->
						<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,1499}$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $emisorNombreComercial)" />
					</xsl:call-template>
				</xsl:for-each>
				
				<!-- 16. Código asignado por SUNAT para el establecimiento anexo declarado en el RUC -->	
				<xsl:variable name="countCodigoAsignadoSUNAT" select="count(cac:PartyLegalEntity/cac:RegistrationAddress/cbc:AddressTypeCode)" />			
				<xsl:if test="($countCodigoAsignadoSUNAT = 0)"> 						
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4198'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), ' ', $countCodigoAsignadoSUNAT)"/>
					</xsl:call-template>																										
				</xsl:if>				

				<xsl:for-each select="./cac:PartyLegalEntity">
					<!-- 11. Apellidos y nombres, denominación o razón social -->
					<xsl:variable name="emisorRazonSocial" select="./cbc:RegistrationName" />

					<!-- 11. Apellidos y nombres o denominacion o razon social Emisor -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'1037'" />
						<xsl:with-param name="node" select="$emisorRazonSocial" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $emisorRazonSocial)" />
					</xsl:call-template>

					<xsl:call-template name="regexpValidateElementIfExist">
<!-- 						<xsl:with-param name="errorCodeValidate" select="'1038'" /> -->
						<xsl:with-param name="errorCodeValidate" select="'4338'" />
						<xsl:with-param name="node" select="$emisorRazonSocial" />
<!-- 						<xsl:with-param name="regexp" select="'^[\w\s].{1,1500}$'" /> -->
						<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,1499}$'"/>
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea 1: ', position(), ' ', $emisorRazonSocial)" />
					</xsl:call-template>

					<!-- 12. Domicilio Fiscal -->
					<xsl:for-each select="./cac:RegistrationAddress">
						<xsl:for-each select="./cac:AddressLine">
							<!-- 12. Dirección completa y detallada -->
							<xsl:variable name="emisorDireccionCompletaItem" select="./cbc:Line" />

							<!-- 12. Domicilio Fiscal -->
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4094'" />
								<xsl:with-param name="node" select="$emisorDireccionCompletaItem" />
<!-- 								<xsl:with-param name="regexp" select="'^[\w\s].{2,200}$'" /> -->
								<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,199}$'"/>			
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: ', position(), ' ', $emisorDireccionCompletaItem)" />
							</xsl:call-template>
						</xsl:for-each>

						<!-- 12. Urbanización -->
						<xsl:variable name="emisorUrbanizacion"
							select="./cbc:CitySubdivisionName" />
						<!-- 12. Provincia -->
						<xsl:variable name="emisorProvincia"
							select="./cbc:CityName" />
						<!-- 12. Código de ubigeo -->
						<xsl:variable name="emisorUbigeo" select="./cbc:ID" />
						<xsl:variable name="emisorUbigeo_SchemeAgencyName"
							select="./cbc:ID/@schemeAgencyName" />
						<xsl:variable name="emisorUbigeo_SchemeName"
							select="./cbc:ID/@schemeName" />
						<!-- 12. Departamento -->
						<xsl:variable name="emisorDepartamento"
							select="./cbc:CountrySubentity" />
						<!-- 12. Distrito -->
						<xsl:variable name="emisorDistrito"
							select="./cbc:District" />
						<!-- 15. Código asignado por SUNAT para el establecimiento anexo declarado 
							en el RUC -->
						<!-- 15. Código de país -->
						<xsl:variable name="emisorAnexoCodigoPais"
							select="./cbc:AddressTypeCode" />
						<xsl:variable
							name="emisorAnexoCodigoPais_ListAgencyName"
							select="./cbc:AddressTypeCode/@listAgencyName" />
						<xsl:variable name="emisorAnexoCodigoPais_ListName"
							select="./cbc:AddressTypeCode/@listName" />

						<!-- 12. Urbanización -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4095'" />
							<xsl:with-param name="node" select="$emisorUrbanizacion" />
<!-- 							<xsl:with-param name="regexp" select="'^[\w\s].{1,25}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,24}$'"/>
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $emisorUrbanizacion)" />
						</xsl:call-template>

						<!-- 12. Provincia -->
						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4096'" />
							<xsl:with-param name="node" select="$emisorProvincia" />
<!-- 							<xsl:with-param name="regexp" select="'^[\w\s].{1,30}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,29}$'"/>
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $emisorProvincia)" />
						</xsl:call-template>

						<!-- 12. Código de ubigeo -->
						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'4256'" />
							<xsl:with-param name="node"
								select="$emisorUbigeo_SchemeAgencyName" />
							<xsl:with-param name="regexp"
								select="'^(PE:INEI)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 3) ', position(), ' ', $emisorUbigeo_SchemeAgencyName)" />
						</xsl:call-template>

						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'4255'" />
							<xsl:with-param name="node"
								select="$emisorUbigeo_SchemeName" />
							<xsl:with-param name="regexp"
								select="'^(Ubigeos)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 2) ', position(), ' ', $emisorUbigeo_SchemeName)" />
						</xsl:call-template>

						<!-- 12. Departamento -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4097'" />
							<xsl:with-param name="node" select="$emisorDepartamento" />
<!-- 							<xsl:with-param name="regexp" select="'^[\w\s].{1,30}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,29}$'"/>
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $emisorDepartamento)" />
						</xsl:call-template>

						<!-- 12. Distrito -->
						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4098'" />
							<xsl:with-param name="node" select="$emisorDistrito" />
<!-- 							<xsl:with-param name="regexp" select="'^[\w\s].{1,30}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,29}$'"/>
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $emisorDistrito)" />
						</xsl:call-template>

						<!-- 12. Código de país -->
						<xsl:for-each select="./cac:Country">
							<xsl:variable name="emisorCodigoPais"
								select="./cbc:IdentificationCode" />
							<xsl:variable name="emisorCodigoPais_ListID"
								select="./cbc:IdentificationCode/@listID" />
							<xsl:variable name="emisorCodigoPais_ListAgencyName"
								select="./cbc:IdentificationCode/@listAgencyName" />
							<xsl:variable name="emisorCodigoPais_ListName"
								select="./cbc:IdentificationCode/@listName" />

							<!-- 12. Código de país -->
							<xsl:call-template
								name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate"
									select="'4041'" />
								<xsl:with-param name="node"
									select="$emisorCodigoPais" />
								<xsl:with-param name="regexp" select="'^(PE)$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: 2) ', position(), ' ', $emisorCodigoPais)" />
							</xsl:call-template>

							<xsl:call-template
								name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate"
									select="'4254'" />
								<xsl:with-param name="node"
									select="$emisorCodigoPais_ListID" />
								<xsl:with-param name="regexp"
									select="'^(ISO 3166-1)$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: 2) ', position(), ' ', $emisorCodigoPais_ListID)" />
							</xsl:call-template>

							<xsl:call-template
								name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate"
									select="'4251'" />
								<xsl:with-param name="node"
									select="$emisorCodigoPais_ListAgencyName" />
								<xsl:with-param name="regexp"
									select="'^(United Nations Economic Commission for Europe)$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: 3) ', position(), ' ', $emisorCodigoPais_ListAgencyName)" />
							</xsl:call-template>

							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4252'" />
								<xsl:with-param name="node" select="$emisorCodigoPais_ListName" />
								<xsl:with-param name="regexp" select="'^(Country)$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: 3) ', position(), ' ', $emisorCodigoPais_ListName)" />
							</xsl:call-template>
						</xsl:for-each>

						<!-- 15. Código asignado por SUNAT para el establecimiento anexo declarado 
							en el RUC -->
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'4198'" />
							<xsl:with-param name="node" select="$emisorAnexoCodigoPais" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $emisorAnexoCodigoPais)" />
						</xsl:call-template>

						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4242'" />
							<xsl:with-param name="node" select="$emisorAnexoCodigoPais" />
							<xsl:with-param name="regexp" select="'^[\d]{4}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $emisorAnexoCodigoPais)" />
						</xsl:call-template>

						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'4251'" />
							<xsl:with-param name="node"
								select="$emisorAnexoCodigoPais_ListAgencyName" />
							<xsl:with-param name="regexp"
								select="'^(PE:SUNAT)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 4) ', position(), ' ', $emisorAnexoCodigoPais_ListAgencyName)" />
						</xsl:call-template>

						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'4252'" />
							<xsl:with-param name="node"
								select="$emisorAnexoCodigoPais_ListName" />
							<xsl:with-param name="regexp"
								select="'^(Establecimientos anexos)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 4) ', position(), ' ', $emisorAnexoCodigoPais_ListName)" />
						</xsl:call-template>
					</xsl:for-each>
				</xsl:for-each>

				<!-- 69. Número de RUC del Agente de Ventas -->
				<xsl:variable name="numeroRUCAgenteVentas"
					select="./cac:AgentParty/cac:PartyIdentification/cbc:ID" />
				<xsl:if test="$tipoOperacion = '0302'">
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'3156'" />
						<xsl:with-param name="node" select="$numeroRUCAgenteVentas" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $numeroRUCAgenteVentas)" />
					</xsl:call-template>
				</xsl:if>

				<!-- Migración de documentos autorizados - BVME para transporte ferroviario 
					de pasajeros -->
				<xsl:for-each select="./cac:AgentParty">
					<xsl:for-each select="./cac:PartyIdentification">
						<!-- 70. Tipo de documento del Agente de Ventas -->
						<xsl:variable name="tipoDocumentoAgenteVentas"
							select="./cbc:ID/@schemeID" />
						<xsl:variable
							name="tipoDocumentoAgenteVentas_schemeName"
							select="./cbc:ID/@schemeName" />
						<xsl:variable
							name="tipoDocumentoAgenteVentas_schemeAgencyName"
							select="./cbc:ID/@schemeAgencyName" />
						<xsl:variable
							name="tipoDocumentoAgenteVentas_schemeURI"
							select="./cbc:ID/@schemeURI" />

						<!-- 70. Tipo de documento del Agente de Ventas -->
						<xsl:if test="($numeroRUCAgenteVentas)">
							<xsl:call-template
								name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist"
									select="'3157'" />
								<xsl:with-param name="node"
									select="$tipoDocumentoAgenteVentas" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $tipoDocumentoAgenteVentas)" />
							</xsl:call-template>
						</xsl:if>

						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'3158'" />
							<xsl:with-param name="node"
								select="$tipoDocumentoAgenteVentas" />
							<xsl:with-param name="regexp" select="'^(6)$'" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $tipoDocumentoAgenteVentas)" />
						</xsl:call-template>

						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'4255'" />
							<xsl:with-param name="node"
								select="$tipoDocumentoAgenteVentas_schemeName" />
							<xsl:with-param name="regexp"
								select="'^(Documento de Identidad)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 3) ', position(), ' ', $tipoDocumentoAgenteVentas_schemeName)" />
						</xsl:call-template>

						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'4256'" />
							<xsl:with-param name="node"
								select="$tipoDocumentoAgenteVentas_schemeAgencyName" />
							<xsl:with-param name="regexp"
								select="'^(PE:SUNAT)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 4) ', position(), ' ', $tipoDocumentoAgenteVentas_schemeAgencyName)" />
						</xsl:call-template>

						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'4257'" />
							<xsl:with-param name="node"
								select="$tipoDocumentoAgenteVentas_schemeURI" />
							<xsl:with-param name="regexp"
								select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo06)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 2) ', position(), ' ', $tipoDocumentoAgenteVentas_schemeURI)" />
						</xsl:call-template>
					</xsl:for-each>
				</xsl:for-each>
			</xsl:for-each>
		</xsl:for-each>

		<!-- 13. Dirección del lugar en el que se entrega el bien -->
		<xsl:for-each select="cac:Delivery">
			<xsl:for-each select="./cac:DeliveryLocation">
				<xsl:for-each select="./cac:Address">
					<xsl:for-each select="./cac:AddressLine">
						<!-- 13. Delivery Dirección completa y detallada -->
						<xsl:variable
							name="emisorDeliveryDireccionCompletaItem" select="./cbc:Line" />

						<!-- 13. Dirección del lugar en el que se entrega el bien -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4236'" />
							<xsl:with-param name="node" select="$emisorDeliveryDireccionCompletaItem" />
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,199}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $emisorDeliveryDireccionCompletaItem)" />
						</xsl:call-template>
					</xsl:for-each>

					<!-- 13. Urbanización -->
					<xsl:variable name="emisorDeliveryUrbanizacion"
						select="./cbc:CitySubdivisionName" />
					<!-- 13. Provincia -->
					<xsl:variable name="emisorDeliveryProvincia"
						select="./cbc:CityName" />
					<!-- 13. Código de ubigeo -->
					<xsl:variable name="emisorDeliveryUbigeo"
						select="./cbc:ID" />
					<xsl:variable
						name="emisorDeliveryUbigeo_SchemeAgencyName"
						select="./cbc:ID/@schemeAgencyName" />
					<xsl:variable name="emisorDeliveryUbigeo_SchemeName"
						select="./cbc:ID/@schemeName" />
					<!-- 13. Departamento -->
					<xsl:variable name="emisorDeliveryDepartamento"
						select="./cbc:CountrySubentity" />
					<!-- 13. Distrito -->
					<xsl:variable name="emisorDeliveryDistrito"
						select="./cbc:District" />

					<!-- 13. Urbanización -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4238'" />
						<xsl:with-param name="node" select="$emisorDeliveryUrbanizacion" />
<!-- 						<xsl:with-param name="regexp" select="'^[\w\s].{1,25}$'" /> -->
						<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,24}$'"/>
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $emisorDeliveryUrbanizacion)" />
					</xsl:call-template>

					<!-- 13. Provincia -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4239'" />
						<xsl:with-param name="node" select="$emisorDeliveryProvincia" />
<!-- 						<xsl:with-param name="regexp" select="'^[\w\s].{1,30}$'" /> -->
						<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,29}$'"/>
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $emisorDeliveryProvincia)" />
					</xsl:call-template>

					<!-- 13. Código de ubigeo -->
					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'4256'" />
						<xsl:with-param name="node"
							select="$emisorDeliveryUbigeo_SchemeAgencyName" />
						<xsl:with-param name="regexp"
							select="'^(PE:INEI)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 5) ', position(), ' ', $emisorDeliveryUbigeo_SchemeAgencyName)" />
					</xsl:call-template>

					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'4255'" />
						<xsl:with-param name="node"
							select="$emisorDeliveryUbigeo_SchemeName" />
						<xsl:with-param name="regexp"
							select="'^(Ubigeos)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 4) ', position(), ' ', $emisorDeliveryUbigeo_SchemeName)" />
					</xsl:call-template>

					<!-- 13. Departamento -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4240'" />
						<xsl:with-param name="node" select="$emisorDeliveryDepartamento" />
<!-- 						<xsl:with-param name="regexp" select="'^[\w\s].{1,30}$'" /> -->
						<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,29}$'"/>
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $emisorDeliveryDepartamento)" />
					</xsl:call-template>

					<!-- 13. Distrito -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4241'" />
						<xsl:with-param name="node" select="$emisorDeliveryDistrito" />
<!-- 						<xsl:with-param name="regexp" select="'^[\w\s].{1,30}$'" /> -->
						<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,29}$'"/>
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $emisorDeliveryDistrito)" />
					</xsl:call-template>

					<!-- 14. Pais del uso, explotación o aprovechamiento del servicio -->
					<xsl:for-each select="./cac:Country">
						<!-- 14. Código de país -->
						<!-- 14. Código de país -->
						<xsl:variable name="emisorDeliveryCodigoPais" select="./cbc:IdentificationCode" />
						<xsl:variable name="emisorDeliveryCodigoPais_ListID" select="./cbc:IdentificationCode/@listID" />
						<xsl:variable name="emisorDeliveryCodigoPais_ListAgencyName" select="./cbc:IdentificationCode/@listAgencyName" />
						<xsl:variable name="emisorDeliveryCodigoPais_ListName" select="./cbc:IdentificationCode/@listName" />

						<!-- 14. Código de país -->
						<xsl:if test="not(($tipoOperacion='0201') or ($tipoOperacion='0208'))">
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4041'"/>
								<xsl:with-param name="node" select="$emisorDeliveryCodigoPais"/>
								<xsl:with-param name="regexp" select="'^(PE)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $emisorDeliveryCodigoPais)"/>
							</xsl:call-template>					
						</xsl:if>

						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'4254'" />
							<xsl:with-param name="node"
								select="$emisorDeliveryCodigoPais_ListID" />
							<xsl:with-param name="regexp"
								select="'^(ISO 3166-1)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 3) ', position(), ' ', $emisorDeliveryCodigoPais_ListID)" />
						</xsl:call-template>

						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'4251'" />
							<xsl:with-param name="node"
								select="$emisorDeliveryCodigoPais_ListAgencyName" />
							<xsl:with-param name="regexp"
								select="'^(United Nations Economic Commission for Europe)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 5) ', position(), ' ', $emisorDeliveryCodigoPais_ListAgencyName)" />
						</xsl:call-template>

						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'4252'" />
							<xsl:with-param name="node"
								select="$emisorDeliveryCodigoPais_ListName" />
							<xsl:with-param name="regexp"
								select="'^(Country)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 5) ', position(), ' ', $emisorDeliveryCodigoPais_ListName)" />
						</xsl:call-template>

						<!-- 14. Pais del uso, explotación o aprovechamiento del servicio -->
						<xsl:if
							test="($tipoOperacion='0201' or $tipoOperacion='0208')">
							<xsl:call-template
								name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist"
									select="'3098'" />
								<xsl:with-param name="node"
									select="$emisorDeliveryCodigoPais" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: ', position(), ' ', $emisorDeliveryCodigoPais)" />
							</xsl:call-template>

							<xsl:if test="($emisorDeliveryCodigoPais='PE')">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3099'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $emisorDeliveryCodigoPais)" />
								</xsl:call-template>
							</xsl:if>
						</xsl:if>
					</xsl:for-each>
				</xsl:for-each>
			</xsl:for-each>
		</xsl:for-each>

		<!-- Datos del ciente o receptor -->
		<xsl:variable name="countClientePartyIdentification"
			select="count(cac:AccountingCustomerParty/cac:Party/cac:PartyIdentification)" />
		<xsl:if test="$countClientePartyIdentification>1">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3090'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countClientePartyIdentification)" />
			</xsl:call-template>
		</xsl:if>
		
		<xsl:variable name="countClientePartyIdentificationID" select="count(cac:AccountingCustomerParty/cac:Party/cac:PartyIdentification/cbc:ID)"/>
		<xsl:if test="$countClientePartyIdentificationID = 0">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2014'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countClientePartyIdentificationID)"/>
			</xsl:call-template>
		</xsl:if>	
					
		<xsl:variable name="countClientePartyIdentificationSchemeID" select="count(cac:AccountingCustomerParty/cac:Party/cac:PartyIdentification/cbc:ID/@schemeID)"/>
		<xsl:if test="$countClientePartyIdentificationSchemeID = 0">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2015'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countClientePartyIdentificationSchemeID)"/>
			</xsl:call-template>
		</xsl:if>			

		<xsl:for-each select="cac:AccountingCustomerParty">
			<!-- Datos del Cliente o receptor -->
			<xsl:for-each select="./cac:Party">
				<xsl:for-each select="./cac:PartyIdentification">
					<!-- 16. Número de documento de identidad del adquirente o usuario -->
					<xsl:variable name="clienteNumeroDocumento" select="./cbc:ID" />
					<!-- 16. Tipo de documento de identidad del adquirente o usuario -->
					<xsl:variable name="clienteTipoDocumento" select="./cbc:ID/@schemeID" />
					<xsl:variable name="clienteNumeroDocumento_SchemeName" select="./cbc:ID/@schemeName" />
					<xsl:variable name="clienteNumeroDocumento_SchemeAgencyName" select="./cbc:ID/@schemeAgencyName" />
					<xsl:variable name="clienteNumeroDocumento_SchemeURI" select="./cbc:ID/@schemeURI" />

					<!-- 16. Tipo de documento de identidad del adquirente o usuario -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'2015'" />
						<xsl:with-param name="node" select="$clienteTipoDocumento" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $clienteTipoDocumento)" />
					</xsl:call-template>

					<!-- 16. Número de documento de identidad del adquirente o usuario -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'2014'" />
						<xsl:with-param name="node" select="$clienteNumeroDocumento" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $clienteNumeroDocumento)" />
					</xsl:call-template>

					<xsl:if test="$clienteTipoDocumento='6'">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'2017'" />
							<xsl:with-param name="node" select="$clienteNumeroDocumento" />
							<xsl:with-param name="regexp" select="'^[\d]{11}$'" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $clienteNumeroDocumento)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if test="$clienteTipoDocumento='1'">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4207'" />
							<xsl:with-param name="node" select="$clienteNumeroDocumento" />
							<xsl:with-param name="regexp" select="'^[\d]{8}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $clienteNumeroDocumento)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if	test="(($clienteTipoDocumento='4') or ($clienteTipoDocumento='7') or 
						($clienteTipoDocumento='0') or ($clienteTipoDocumento='A') or 
						($clienteTipoDocumento='B') or ($clienteTipoDocumento='C') or 
						($clienteTipoDocumento='D') or ($clienteTipoDocumento='E') or 
						($clienteTipoDocumento='F') or ($clienteTipoDocumento='G'))">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4208'" />
							<xsl:with-param name="node" select="$clienteNumeroDocumento" />
							<!-- <xsl:with-param name="regexp" select="'^[\w\d\-]{1,15}$'"/> -->
<!-- 							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s]{1,15}$'" /> -->
							<xsl:with-param name="regexp" select="'^[\d\w]{1,15}$'"/>
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $clienteNumeroDocumento, ' ', $clienteTipoDocumento)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if
						test="($tipoOperacion='0200' or $tipoOperacion='0201' or $tipoOperacion='0203' or $tipoOperacion='0204'
							or $tipoOperacion='0205' or $tipoOperacion='0206' or $tipoOperacion='0207' or $tipoOperacion='0208' or $tipoOperacion='0401')">
						<xsl:if test="($clienteTipoDocumento = '6')">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2800'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea 4: ', position(), ' ', $tipoOperacion, ' ', $clienteTipoDocumento)" />
							</xsl:call-template>
						</xsl:if>
					</xsl:if>

					<!-- 16. Número de documento de identidad del adquirente o usuario -->
					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'4255'" />
						<xsl:with-param name="node"
							select="$clienteNumeroDocumento_SchemeName" />
						<xsl:with-param name="regexp"
							select="'^(Documento de Identidad)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 5) ', position(), ' ', $clienteNumeroDocumento_SchemeName)" />
					</xsl:call-template>

					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'4256'" />
						<xsl:with-param name="node"
							select="$clienteNumeroDocumento_SchemeAgencyName" />
						<xsl:with-param name="regexp"
							select="'^(PE:SUNAT)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 6) ', position(), ' ', $clienteNumeroDocumento_SchemeAgencyName)" />
					</xsl:call-template>

					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'4257'" />
						<xsl:with-param name="node"
							select="$clienteNumeroDocumento_SchemeURI" />
						<xsl:with-param name="regexp"
							select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo06)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 3) ', position(), ' ', $clienteNumeroDocumento_SchemeURI)" />
					</xsl:call-template>
				</xsl:for-each>

				<xsl:for-each select="./cac:PartyLegalEntity">
					<!-- 17. Apellidos y nombres, denominación o razón social del adquirente 
						o usuario -->
					<xsl:variable name="clienteRazonSocial"
						select="./cbc:RegistrationName" />

					<!-- 17. Apellidos y nombres, denominación o razón social del adquirente 
						o usuario -->
					<xsl:call-template
						name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist"
							select="'2021'" />
						<xsl:with-param name="node"
							select="$clienteRazonSocial" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $clienteRazonSocial)" />
					</xsl:call-template>

					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2022'" />
						<xsl:with-param name="node" select="$clienteRazonSocial" />
						<!-- <xsl:with-param name="regexp" select="'^[^\n].{2,1500}$'" /> -->
						<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,1499}$'" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $clienteRazonSocial)" />
					</xsl:call-template>
				</xsl:for-each>
			</xsl:for-each>
		</xsl:for-each>

		<!-- Documentos de referencia -->
		<!-- 18. Tipo y número de la guía de remisión relacionada -->
		<xsl:for-each select="cac:DespatchDocumentReference">
			<!-- 18. Número de documento -->
			<xsl:variable name="numeroGuiaRemisionRelacionada"
				select="./cbc:ID" />
			<!-- 18. Tipo de guía relacionado -->
			<xsl:variable name="tipoGuiaRemisionRelacionada"
				select="./cbc:DocumentTypeCode" />
			<xsl:variable
				name="tipoGuiaRemisionRelacionada_listAgencyName"
				select="./cbc:DocumentTypeCode/@listAgencyName" />
			<xsl:variable
				name="tipoGuiaRemisionRelacionada_listName"
				select="./cbc:DocumentTypeCode/@listName" />
			<xsl:variable name="tipoGuiaRemisionRelacionada_listURI"
				select="./cbc:DocumentTypeCode/@listURI" />

			<xsl:if test="string($numeroGuiaRemisionRelacionada)">
				<!-- 18. Número de la guía de remisión relacionada -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4006'" />
					<xsl:with-param name="node" select="$numeroGuiaRemisionRelacionada" />
<!-- 					<xsl:with-param name="regexp" select="'^[T][A-Z0-9]{3}-[0-9]{1,8}$|^[0-9]{4}-[0-9]{1,8}$|^[EG]{2}[0-9]{2}-[0-9]{1,8}$|^[G]{1}[0-9]{3}-[0-9]{1,8}$'" /> -->
					<xsl:with-param name="regexp" select="'^[T][A-Z0-9]{3}-[0-9]{1,8}$|^[0-9]{4}-[0-9]{1,8}$|^[EG0][1-4]{1}-[0-9]{1,8}$|^[EG07] {4}-[0-9]{1,8}$|^[G][0-9]{3}-[0-9]{1,8}$|^[V][A-Z0-9]{3}-[0-9]{1,8}$'"/>
					<xsl:with-param name="isError" select="false()" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $numeroGuiaRemisionRelacionada)" />
				</xsl:call-template>

				<!-- 18. Tipo de la guía de remisión relacionada -->
				<xsl:call-template
					name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate"
						select="'4005'" />
					<xsl:with-param name="node"
						select="$tipoGuiaRemisionRelacionada" />
					<xsl:with-param name="regexp" select="'^(09|31)$'" />
					<xsl:with-param name="isError" select="false()" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $tipoGuiaRemisionRelacionada)" />
				</xsl:call-template>

				<xsl:call-template
					name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate"
						select="'4251'" />
					<xsl:with-param name="node"
						select="$tipoGuiaRemisionRelacionada_listAgencyName" />
					<xsl:with-param name="regexp"
						select="'^(PE:SUNAT)$'" />
					<xsl:with-param name="isError" select="false()" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: 6) ', position(), ' ', $tipoGuiaRemisionRelacionada_listAgencyName)" />
				</xsl:call-template>

				<xsl:call-template
					name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate"
						select="'4252'" />
					<xsl:with-param name="node"
						select="$tipoGuiaRemisionRelacionada_listName" />
					<xsl:with-param name="regexp"
						select="'^(Tipo de Documento)$'" />
					<xsl:with-param name="isError" select="false()" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: 6) ', position(), ' ', $tipoGuiaRemisionRelacionada_listName)" />
				</xsl:call-template>

				<xsl:call-template
					name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate"
						select="'4253'" />
					<xsl:with-param name="node"
						select="$tipoGuiaRemisionRelacionada_listURI" />
					<xsl:with-param name="regexp"
						select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo01)$'" />
					<xsl:with-param name="isError" select="false()" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: 2) ', position(), ' ', $tipoGuiaRemisionRelacionada_listURI)" />
				</xsl:call-template>
			</xsl:if>

			<xsl:if
				test="count(key('by-document-despatch-reference', concat($tipoGuiaRemisionRelacionada,' ',$numeroGuiaRemisionRelacionada))) > 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2364'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoGuiaRemisionRelacionada,' ', $numeroGuiaRemisionRelacionada)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>

		<!-- 19. Tipo y número de otro documento relacionado -->
		<xsl:for-each select="cac:AdditionalDocumentReference">
			<xsl:variable name="numeroOtroDocumentoRelacionada" select="./cbc:ID" />
			<xsl:variable name="tipoOtroDocumentoRelacionada" select="./cbc:DocumentTypeCode" />
			<xsl:variable name="tipoOtroDocumentoRelacionada_listAgencyName" select="./cbc:DocumentTypeCode/@listAgencyName" />
			<xsl:variable name="tipoOtroDocumentoRelacionada_listName" select="./cbc:DocumentTypeCode/@listName" />
			<xsl:variable name="tipoOtroDocumentoRelacionada_listURI" select="./cbc:DocumentTypeCode/@listURI" />

			<xsl:if test="$numeroOtroDocumentoRelacionada">
				<!-- 19. Número de otro documento relacionado -->
				<xsl:call-template
					name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate"
						select="'4010'" />
					<xsl:with-param name="node"
						select="$numeroOtroDocumentoRelacionada" />
					<xsl:with-param name="regexp"
						select="'^[\w].{1,30}$'" />
					<xsl:with-param name="isError" select="false()" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ',$numeroOtroDocumentoRelacionada)" />
				</xsl:call-template>

				<xsl:call-template
					name="regexpValidateElementIfExistTrue">
					<xsl:with-param name="errorCodeValidate"
						select="'4010'" />
					<xsl:with-param name="node"
						select="$numeroOtroDocumentoRelacionada" />
					<xsl:with-param name="regexp" select="'[\s]'" />
					<xsl:with-param name="isError" select="false()" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ',$numeroOtroDocumentoRelacionada)" />
				</xsl:call-template>

				<!-- 19. Tipo de otro documento relacionado -->
				<!-- PARA REVISAR -->
 				<xsl:if test="($numeroOtroDocumentoRelacionada) and not(($tipoOtroDocumentoRelacionada='02') or ($tipoOtroDocumentoRelacionada='03') or 
 					($tipoOtroDocumentoRelacionada='04') or ($tipoOtroDocumentoRelacionada='05') or ($tipoOtroDocumentoRelacionada='08') or 
 					($tipoOtroDocumentoRelacionada='09') or ($tipoOtroDocumentoRelacionada='99'))"> 
					<xsl:call-template name="addWarning">
 						<xsl:with-param name="warningCode" select="'4009'" /> 
 						<xsl:with-param name="warningMessage" 
 							select="concat('Error en la linea: ', position(), ' ', $tipoOtroDocumentoRelacionada)"	/> 
 					</xsl:call-template> 
 				</xsl:if> 

				<xsl:call-template
					name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate"
						select="'4251'" />
					<xsl:with-param name="node"
						select="$tipoOtroDocumentoRelacionada_listAgencyName" />
					<xsl:with-param name="regexp"
						select="'^(PE:SUNAT)$'" />
					<xsl:with-param name="isError" select="false()" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: 7) ', position(), ' ', $tipoOtroDocumentoRelacionada_listAgencyName)" />
				</xsl:call-template>

				<!-- PARA REVISAR -->
				<xsl:call-template
					name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate"
						select="'4252'" />
					<xsl:with-param name="node"
						select="$tipoOtroDocumentoRelacionada_listName" />
					<xsl:with-param name="regexp"
						select="'^(Documento Relacionado)$'" />
					<xsl:with-param name="isError" select="false()" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: 7) ', position(), ' ', $tipoOtroDocumentoRelacionada_listName)" />
				</xsl:call-template>

				<xsl:call-template
					name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate"
						select="'4253'" />
					<xsl:with-param name="node"
						select="$tipoOtroDocumentoRelacionada_listURI" />
					<xsl:with-param name="regexp"
						select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo12)$'" />
					<xsl:with-param name="isError" select="false()" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: 3) ', position(), ' ', $tipoOtroDocumentoRelacionada_listURI)" />
				</xsl:call-template>
			</xsl:if>

			<xsl:if
				test="count(key('by-document-additional-reference', concat($tipoOtroDocumentoRelacionada,' ',$numeroOtroDocumentoRelacionada))) > 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2365'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoOtroDocumentoRelacionada,' ',$numeroOtroDocumentoRelacionada)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>

		<xsl:if test="$tipoOperacion = '2105'">					
			<xsl:if test="($countCodigoConcepto7017Global = 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2595'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error countcodigoConcepto: ', $countCodigoConcepto7017Global)"/>
				</xsl:call-template>							
			</xsl:if>						
				
			<xsl:if test="($countCodigoConcepto7018Global = 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2596'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error countcodigoConcepto: ', $countCodigoConcepto7018Global)"/>
				</xsl:call-template>							
			</xsl:if>			
			
			<xsl:if test="($countCodigoConcepto7019Global = 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2597'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error countcodigoConcepto: ', $countCodigoConcepto7019Global)"/>
				</xsl:call-template>							
			</xsl:if>					
       </xsl:if>		

		<!-- Datos del detalle o Ítem de la Factura -->
		<xsl:for-each select="cac:InvoiceLine">
			<!-- 20. Número de orden del Ítem -->
			<xsl:variable name="numeroLinea" select="./cbc:ID" />
			<!-- 21. Unidad de medida por ítem -->
			<xsl:variable name="unidadMedidaLinea" select="./cbc:InvoicedQuantity/@unitCode" />
			<xsl:variable name="unidadMedidaLinea_ListID" select="./cbc:InvoicedQuantity/@unitCodeListID" />
			<xsl:variable name="unidadMedidaLinea_ListAgencyName" select="./cbc:InvoicedQuantity/@unitCodeListAgencyName" />
			<!-- 22. Cantidad de unidades por ítem -->
			<xsl:variable name="cantidadUnidadesLinea" select="./cbc:InvoicedQuantity" />
			<!-- 33. Valor de venta por ítem -->
			<xsl:variable name="valorVentaLinea" select="./cbc:LineExtensionAmount" />
			<xsl:variable name="valorVentaLinea_currencyID" select="./cbc:LineExtensionAmount/@currencyID" />

			<!-- 28. Código de precio -->
			<xsl:variable name="countCodigoPrecio01Linea" select="count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='01'])" />
			<xsl:variable name="countCodigoPrecio02Linea" select="count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='02'])" />
			<xsl:variable name="countCodigoPrecio03Linea" select="count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='03'])" />
			<!-- 30. Monto total de tributos del ítem -->
			<xsl:variable name="montoTotalTributosLinea" select="sum(./cac:TaxTotal/cbc:TaxAmount)"/>
<!-- 			<xsl:variable name="montoTributoLinea9996Linea" select="./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9996']/cbc:TaxAmount" /> -->
<!-- 			<xsl:variable name="montoTributoLinea9996" select="./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9996']/cbc:TaxAmount" /> -->
			
			<!-- 31. Monto base -->
			<xsl:variable name="sumMontoBaseLinea" select="sum(./cac:TaxTotal/cac:TaxSubtotal/cbc:TaxableAmount)" />
			<xsl:variable name="sumMontoBase1000LineaSub" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxableAmount)"/>		
			<xsl:variable name="sumMontoBase1016LineaSub" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:TaxableAmount)"/>
			<xsl:variable name="sumMontoBase2000LineaSub" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxableAmount)"/>
			<xsl:variable name="sumMontoBase9995LineaSub" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9995']/cbc:TaxableAmount)"/>
			<xsl:variable name="sumMontoBase9996LineaSub" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9996']/cbc:TaxableAmount)"/>
			<xsl:variable name="sumMontoBase9997LineaSub" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9997']/cbc:TaxableAmount)"/>
			<xsl:variable name="sumMontoBase9998LineaSub" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9998']/cbc:TaxableAmount)"/>
			<xsl:variable name="sumMontoBase9999LineaSub" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxableAmount)"/>
			<!-- 31. Código de tributo por línea -->
			<xsl:variable name="countCodigoTributoLinea1000" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])" />
			<xsl:variable name="countCodigoTributoLinea1016" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1016'])" />
			<xsl:variable name="countCodigoTributoLinea2000" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000'])" />
			<xsl:variable name="countCodigoTributoLinea7152" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='7152'])" />
			<xsl:variable name="countCodigoTributoLinea9995" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9995'])" />
			<xsl:variable name="countCodigoTributoLinea9996" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9996'])" />
			<xsl:variable name="countCodigoTributoLinea9997" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9997'])" />
			<xsl:variable name="countCodigoTributoLinea9998" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9998'])" />
			<xsl:variable name="countCodigoTributoLinea9999" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9999'])" />


			<!-- 36. Cargo/descuento por ítem -->
			<xsl:variable name="montoDescuentosLinea01">	
				<xsl:choose>
	    			<xsl:when test="count(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='01']/cbc:Amount) &gt; 0">
	    				<xsl:value-of select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='01']/cbc:Amount)"/></xsl:when>
	    			<xsl:otherwise>0</xsl:otherwise>
	  			</xsl:choose>       
			</xsl:variable>	

			<xsl:variable name="montoCargosLinea48">	
				<xsl:choose>
	    			<xsl:when test="count(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='48']/cbc:Amount) &gt; 0">
	    				<xsl:value-of select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='48']/cbc:Amount)"/></xsl:when>
	    			<xsl:otherwise>0</xsl:otherwise>
	  			</xsl:choose>       
			</xsl:variable>	

			<!-- 20. Número de orden del Ítem -->
			<xsl:choose>
				<xsl:when test="not($numeroLinea)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2023'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position())" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:if
						test='not(fn:matches($numeroLinea,"^[0-9]{1,3}?$")) or $numeroLinea &lt;= 0'>
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2023'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position())" />
						</xsl:call-template>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:if
				test="count(key('by-invoiceLine-id', number($numeroLinea))) &gt; 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2752'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position())" />
				</xsl:call-template>
			</xsl:if>

			<!-- 22. Cantidad de unidades por item -->
			<xsl:call-template
				name="existValidateElementNotExist">
				<xsl:with-param name="errorCodeNotExist"
					select="'2024'" />
				<xsl:with-param name="node"
					select="$cantidadUnidadesLinea" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', position(), ' ', $cantidadUnidadesLinea)" />
			</xsl:call-template>

			<xsl:call-template name="isTrueExpresion">
				<xsl:with-param name="errorCodeValidate"
					select="'2024'" />
				<xsl:with-param name="node"
					select="$cantidadUnidadesLinea" />
				<xsl:with-param name="expresion"
					select="$cantidadUnidadesLinea = 0" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea:', position(), ' ', $cantidadUnidadesLinea)" />
			</xsl:call-template>

			<!-- 21. Unidad de medida por ítem -->
			<xsl:call-template name="existValidateElementNotExist">
				<xsl:with-param name="errorCodeNotExist" select="'2883'" />
				<xsl:with-param name="node" select="$unidadMedidaLinea" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', position(), ' ', $unidadMedidaLinea)" />
			</xsl:call-template>

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'4258'" />
				<xsl:with-param name="node"
					select="$unidadMedidaLinea_ListID" />
				<xsl:with-param name="regexp"
					select="'^(UN/ECE rec 20)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', position(), ' ', $unidadMedidaLinea_ListID)" />
			</xsl:call-template>

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'4259'" />
				<xsl:with-param name="node"
					select="$unidadMedidaLinea_ListAgencyName" />
				<xsl:with-param name="regexp"
					select="'^(United Nations Economic Commission for Europe)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', position(), ' ', $unidadMedidaLinea_ListAgencyName)" />
			</xsl:call-template>

			<!-- 22. Cantidad de unidades por item -->
			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'2025'" />
				<xsl:with-param name="node"
					select="$cantidadUnidadesLinea" />
				<xsl:with-param name="regexp"
					select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', position(), ' ', $cantidadUnidadesLinea)" />
			</xsl:call-template>


			<!-- 25. Codigo producto de SUNAT -->
			<xsl:variable name="codigoProductoSUNATItem_X"
				select="./cac:Item/cac:CommodityClassification/cbc:ItemClassificationCode" />
			<xsl:variable name="codigoProductoSUNATItem">
				<xsl:choose>
					<xsl:when
						test="count(./cac:Item/cac:CommodityClassification/cbc:ItemClassificationCode) &gt; 0">
						<xsl:value-of
							select="./cac:Item/cac:CommodityClassification/cbc:ItemClassificationCode" />
					</xsl:when>
					<xsl:otherwise>
						0
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>

			<xsl:variable name="countCodigoConcepto_3001" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text()='3001'])" />
			<xsl:variable name="countCodigoConcepto_3002" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text()='3002'])" />
			<xsl:variable name="countCodigoConcepto_3003" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text()='3003'])" />
			<xsl:variable name="countCodigoConcepto_3004" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text()='3004'])" />
			<xsl:variable name="countCodigoConcepto_3005" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text()='3005'])" />
			<xsl:variable name="countCodigoConcepto_3006" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text()='3006'])" />

			<xsl:if test="($tipoOperacion='1002')">
				<xsl:if test="not($countCodigoConcepto_3001 > 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3063'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $countCodigoConcepto_3001)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="not($countCodigoConcepto_3002 > 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3130'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $countCodigoConcepto_3001)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="not($countCodigoConcepto_3003 > 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3131'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $countCodigoConcepto_3001)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="not($countCodigoConcepto_3004 > 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3132'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $countCodigoConcepto_3001)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="not($countCodigoConcepto_3005 > 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3134'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $countCodigoConcepto_3001)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="not($countCodigoConcepto_3006 > 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3133'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $countCodigoConcepto_3001)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>

			<!-- 28. Código del concepto -->
			<xsl:variable name="countCodigoConcepto4000For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4000'])" />
			<xsl:variable name="countCodigoConcepto4001For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4001'])" />
			<xsl:variable name="countCodigoConcepto4002For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4002'])" />
			<xsl:variable name="countCodigoConcepto4003For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4003'])" />
			<xsl:variable name="countCodigoConcepto4004For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4004'])" />
			<xsl:variable name="countCodigoConcepto4005For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4005'])" />
			<xsl:variable name="countCodigoConcepto4006For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4006'])" />
			<xsl:variable name="countCodigoConcepto4007For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4007'])" />
			<xsl:variable name="countCodigoConcepto4008For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4008'])" />
			<xsl:variable name="countCodigoConcepto4009For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4009'])" />
			<xsl:variable name="countCodigoConcepto4030For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4030'])" />
			<xsl:variable name="countCodigoConcepto4031For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4031'])" />
			<xsl:variable name="countCodigoConcepto4032For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4032'])" />
			<xsl:variable name="countCodigoConcepto4033For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4033'])" />
			<xsl:variable name="countCodigoConcepto4040For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4040'])" />
			<xsl:variable name="countCodigoConcepto4041For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4041'])" />
			<xsl:variable name="countCodigoConcepto4042For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4042'])" />
			<xsl:variable name="countCodigoConcepto4043For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4043'])" />
			<xsl:variable name="countCodigoConcepto4044For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4044'])" />
			<xsl:variable name="countCodigoConcepto4045For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4045'])" />
			<xsl:variable name="countCodigoConcepto4046For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4046'])" />
			<xsl:variable name="countCodigoConcepto4047For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4047'])" />
			<xsl:variable name="countCodigoConcepto4048For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4048'])" />
			<xsl:variable name="countCodigoConcepto4049For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4049'])" />
			<xsl:variable name="countCodigoConcepto4060For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4060'])" />
			<xsl:variable name="countCodigoConcepto4061For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4061'])" />
			<xsl:variable name="countCodigoConcepto4062For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4062'])" />
			<xsl:variable name="countCodigoConcepto4063For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '4063'])" />
			<xsl:variable name="countCodigoConcepto5000For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '5000'])" />
			<xsl:variable name="countCodigoConcepto5001For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '5001'])" />
			<xsl:variable name="countCodigoConcepto5002For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '5002'])" />
			<xsl:variable name="countCodigoConcepto5003For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '5003'])" />
			<xsl:variable name="countCodigoConcepto5004For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '5004'])" />
			<xsl:variable name="countCodigoConcepto7001For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7001'])" />
			<xsl:variable name="countCodigoConcepto7002For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7002'])" />
			<xsl:variable name="countCodigoConcepto7003For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7003'])" />
			<xsl:variable name="countCodigoConcepto7004For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7004'])" />
			<xsl:variable name="countCodigoConcepto7005For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7005'])" />
			<xsl:variable name="countCodigoConcepto7006For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7006'])" />
			<xsl:variable name="countCodigoConcepto7007For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7007'])" />
			<xsl:variable name="countCodigoConcepto7012For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7012'])"/>	
			<xsl:variable name="countCodigoConcepto7013For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7013'])"/>
			<xsl:variable name="countCodigoConcepto7014For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7014'])"/>
			<xsl:variable name="countCodigoConcepto7015For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7015'])"/>
			<xsl:variable name="countCodigoConcepto7016For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7016'])"/>
			<xsl:variable name="countCodigoConcepto7017For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7017'])"/>
			<xsl:variable name="countCodigoConcepto7018For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7018'])"/>

			<xsl:if test="($tipoOperacion='0301')">
				<!-- 67 / 68. Código del concepto -->
				<xsl:if test="($countCodigoConcepto4030For=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3168'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4030For)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="($countCodigoConcepto4031For=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3169'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4031For)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="($countCodigoConcepto4032For=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3170'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4032For)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="($countCodigoConcepto4033For=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3171'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4033For)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>

			<xsl:if test="($tipoOperacion='0302')">
				<!-- 71 / 75. Código del concepto -->
				<xsl:if test="($countCodigoConcepto4040For=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3159'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4040For)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="($countCodigoConcepto4041For=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3160'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4041For)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="($countCodigoConcepto4049For=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3204'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4049For)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="($countCodigoConcepto4042For=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3161'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4042For)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="($countCodigoConcepto4043For=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3162'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4043For)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="($countCodigoConcepto4044For=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3163'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4044For)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="($countCodigoConcepto4045For=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3164'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4045For)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="($countCodigoConcepto4046For=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3165'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4046For)" />
					</xsl:call-template>
				</xsl:if>

				<!-- 76. Código del concepto -->
				<xsl:if test="($countCodigoConcepto4048For=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3167'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4048For)" />
					</xsl:call-template>
				</xsl:if>

				<!-- 77. Código del concepto -->
				<xsl:if test="($countCodigoConcepto4047For=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3166'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4047For)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>

			<!-- 85. Código del concepto -->
			<xsl:if
				test="(($countCodigoConcepto5001For &gt; 0) or ($countCodigoConcepto5002For &gt; 0) or ($countCodigoConcepto5003For &gt; 0))
							and ($countCodigoConcepto5000For = 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3146'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoConcepto5001For, ' ', $countCodigoConcepto5002For, ' ', $countCodigoConcepto5003For, ' ', $countCodigoConcepto5004For)" />
				</xsl:call-template>
			</xsl:if>

			<!-- 86. Código del concepto -->
			<xsl:if
				test="(($countCodigoConcepto5000For &gt; 0) or ($countCodigoConcepto5002For &gt; 0) or ($countCodigoConcepto5003For &gt; 0))
							and ($countCodigoConcepto5001For = 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3147'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoConcepto5001For, ' ', $countCodigoConcepto5002For, ' ', $countCodigoConcepto5003For, ' ', $countCodigoConcepto5004For)" />
				</xsl:call-template>
			</xsl:if>

			<!-- 87. Código del concepto -->
			<xsl:if
				test="(($countCodigoConcepto5000For &gt; 0) or ($countCodigoConcepto5001For &gt; 0) or ($countCodigoConcepto5003For &gt; 0))
							and ($countCodigoConcepto5002For = 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3148'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoConcepto5001For, ' ', $countCodigoConcepto5002For, ' ', $countCodigoConcepto5003For, ' ', $countCodigoConcepto5004For)" />
				</xsl:call-template>
			</xsl:if>

			<!-- 88. Código del concepto -->
			<xsl:if
				test="(($countCodigoConcepto5000For &gt; 0) or ($countCodigoConcepto5001For &gt; 0) or ($countCodigoConcepto5002For &gt; 0))
							and ($countCodigoConcepto5003For = 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3149'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoConcepto5001For, ' ', $countCodigoConcepto5002For, ' ', $countCodigoConcepto5003For, ' ', $countCodigoConcepto5004For)" />
				</xsl:call-template>
			</xsl:if>

<!-- 			<xsl:if test="($tipoOperacion='0202')"> -->
				<!-- 114 / 118. Código del concepto -->
<!-- 				<xsl:if test="($countCodigoConcepto4009For = 0)"> -->
<!-- 					<xsl:call-template name="rejectCall"> -->
<!-- 						<xsl:with-param name="errorCode" select="'3136'" /> -->
<!-- 						<xsl:with-param name="errorMessage" -->
<!-- 							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4009For)" /> -->
<!-- 					</xsl:call-template> -->
<!-- 				</xsl:if> -->

<!-- 				<xsl:if test="($countCodigoConcepto4008For = 0)"> -->
<!-- 					<xsl:call-template name="rejectCall"> -->
<!-- 						<xsl:with-param name="errorCode" select="'3137'" /> -->
<!-- 						<xsl:with-param name="errorMessage" -->
<!-- 							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4008For)" /> -->
<!-- 					</xsl:call-template> -->
<!-- 				</xsl:if> -->

<!-- 				<xsl:if test="($countCodigoConcepto4000For = 0)"> -->
<!-- 					<xsl:call-template name="rejectCall"> -->
<!-- 						<xsl:with-param name="errorCode" select="'3138'" /> -->
<!-- 						<xsl:with-param name="errorMessage" -->
<!-- 							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4000For)" /> -->
<!-- 					</xsl:call-template> -->
<!-- 				</xsl:if> -->

<!-- 				<xsl:if test="($countCodigoConcepto4007For = 0)"> -->
<!-- 					<xsl:call-template name="rejectCall"> -->
<!-- 						<xsl:with-param name="errorCode" select="'3139'" /> -->
<!-- 						<xsl:with-param name="errorMessage" -->
<!-- 							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4007For)" /> -->
<!-- 					</xsl:call-template> -->
<!-- 				</xsl:if> -->

<!-- 				<xsl:if test="($countCodigoConcepto4001For = 0)"> -->
<!-- 					<xsl:call-template name="rejectCall"> -->
<!-- 						<xsl:with-param name="errorCode" select="'3140'" /> -->
<!-- 						<xsl:with-param name="errorMessage" -->
<!-- 							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4001For)" /> -->
<!-- 					</xsl:call-template> -->
<!-- 				</xsl:if> -->

<!-- 				119 / 122. Código del concepto -->
<!-- 				<xsl:if test="($countCodigoConcepto4002For = 0)"> -->
<!-- 					<xsl:call-template name="rejectCall"> -->
<!-- 						<xsl:with-param name="errorCode" select="'3141'" /> -->
<!-- 						<xsl:with-param name="errorMessage" -->
<!-- 							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4002For)" /> -->
<!-- 					</xsl:call-template> -->
<!-- 				</xsl:if> -->

<!-- 				<xsl:if test="($countCodigoConcepto4003For = 0)"> -->
<!-- 					<xsl:call-template name="rejectCall"> -->
<!-- 						<xsl:with-param name="errorCode" select="'3142'" /> -->
<!-- 						<xsl:with-param name="errorMessage" -->
<!-- 							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4003For)" /> -->
<!-- 					</xsl:call-template> -->
<!-- 				</xsl:if> -->

<!-- 				<xsl:if test="($countCodigoConcepto4004For = 0)"> -->
<!-- 					<xsl:call-template name="rejectCall"> -->
<!-- 						<xsl:with-param name="errorCode" select="'3143'" /> -->
<!-- 						<xsl:with-param name="errorMessage" -->
<!-- 							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4004For)" /> -->
<!-- 					</xsl:call-template> -->
<!-- 				</xsl:if> -->

<!-- 				<xsl:if test="($countCodigoConcepto4006For = 0)"> -->
<!-- 					<xsl:call-template name="rejectCall"> -->
<!-- 						<xsl:with-param name="errorCode" select="'3144'" /> -->
<!-- 						<xsl:with-param name="errorMessage" -->
<!-- 							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4006For)" /> -->
<!-- 					</xsl:call-template> -->
<!-- 				</xsl:if> -->
<!-- 				123. Código del concepto -->
<!-- 				<xsl:if test="($countCodigoConcepto4005For = 0)"> -->
<!-- 					<xsl:call-template name="rejectCall"> -->
<!-- 						<xsl:with-param name="errorCode" select="'3145'" /> -->
<!-- 						<xsl:with-param name="errorMessage" -->
<!-- 							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4005For)" /> -->
<!-- 					</xsl:call-template> -->
<!-- 				</xsl:if> -->
<!-- 			</xsl:if> -->

			<xsl:if test="($tipoOperacion='0205')">
				<!-- 124 / 127. Código del concepto -->
				<xsl:if test="($countCodigoConcepto4000For = 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3138'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4000For)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="($countCodigoConcepto4007For = 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3139'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4007For)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="($countCodigoConcepto4008For = 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3137'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4008For)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="($countCodigoConcepto4009For = 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3136'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto4009For)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>

			<!-- 26. Descripcion detallada del servicio prestado, bien vendido o cedido en uso, indicando las caracteristicas -->					
			<xsl:variable name="countDescripcionDetallada" select="count(./cac:Item/cbc:Description)" />			
            <xsl:if test='($countDescripcionDetallada = 0)'>
                <xsl:call-template name="rejectCall"> 
                	<xsl:with-param name="errorCode" select="'2026'" /> 
                	<xsl:with-param name="errorMessage" 
                		select="concat('Error en la linea: ', $countDescripcionDetallada)"/> 
                </xsl:call-template>
            </xsl:if>

			<!-- 120. Código del concepto  -->
			<!-- ////  -->
			<!-- 
			<xsl:if test="(($tipoOperacion='2100') or ($tipoOperacion='2101') or ($tipoOperacion='2102'))">
				<xsl:if test="(($countCodigoConcepto7004For = 0) and ($countCodigoConcepto7005For = 0) and
					($countCodigoConcepto7012For = 0))">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3241'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoConcepto7004For, ' ', $countCodigoConcepto7005For, ' ', $countCodigoConcepto7012For)"/>
					</xsl:call-template>	
				</xsl:if>	
			</xsl:if>	 
			-->
			<!-- Item -->
			<xsl:for-each select="./cac:Item">		
				<!-- 29. Descripción detallada del servicio prestado, bien vendido o cedido en uso, indicando las características. -->																							
				<xsl:for-each select="./cbc:Description">					
					<xsl:variable name="descripcionDetalladaServicioPrestado" select="."/>				
					<!-- 29. Descripción detallada del servicio prestado, bien vendido o cedido en uso, indicando las características. -->		
					<xsl:choose>
						<xsl:when test="string-length($descripcionDetalladaServicioPrestado) &gt; 500">
							<xsl:call-template name="isTrueExpresion">
								<xsl:with-param name="errorCodeValidate" select="'2027'"/>
								<xsl:with-param name="node" select="$descripcionDetalladaServicioPrestado"/>
								<xsl:with-param name="expresion" select="true()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(),' | ',$descripcionDetalladaServicioPrestado)"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'2027'"/>
								<xsl:with-param name="node" select="$descripcionDetalladaServicioPrestado"/>
								<xsl:with-param name="regexp" select="'^(?!\s*$)[\s\S].{0,}'"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' | ',$descripcionDetalladaServicioPrestado)"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>																
				</xsl:for-each>			
														
				<xsl:for-each select="./cac:SellersItemIdentification">
					<!-- 23. Código de producto -->
					<xsl:variable name="codigoProducto" select="./cbc:ID" />

					<!-- 23. Código de producto -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4269'" />
						<xsl:with-param name="node" select="$codigoProducto" />
<!-- 						<xsl:with-param name="regexp" select="'[^\n].{1,30}'" /> -->
						<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,29}$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $codigoProducto)" />
					</xsl:call-template>
				</xsl:for-each>

				<xsl:for-each select="./cac:CommodityClassification">
					<!-- 24. Codigo producto de SUNAT -->
					<xsl:variable name="codigoProductoSUNAT" select="./cbc:ItemClassificationCode" />
					<xsl:variable name="codigoProductoSUNAT_listID" select="./cbc:ItemClassificationCode/@listID" />
					<xsl:variable name="codigoProductoSUNAT_listAgencyName" select="./cbc:ItemClassificationCode/@listAgencyName" />
					<xsl:variable name="codigoProductoSUNAT_listName" select="./cbc:ItemClassificationCode/@listName" />

					<!-- 24. Codigo producto de SUNAT -->
					<xsl:if
						test="($tipoOperacion='0200' or $tipoOperacion='0201' or $tipoOperacion='0202' or $tipoOperacion='0203' or $tipoOperacion='0204'
							or $tipoOperacion='0205' or $tipoOperacion='0206' or $tipoOperacion='0207' or $tipoOperacion='0208')">
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'4331'" />
							<xsl:with-param name="node" select="$codigoProductoSUNAT" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $codigoProductoSUNAT)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'4254'" />
						<xsl:with-param name="node"
							select="$codigoProductoSUNAT_listID" />
						<xsl:with-param name="regexp" select="'^(UNSPSC)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 4) ', position(), ' ', $codigoProductoSUNAT_listID)" />
					</xsl:call-template>

					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'4251'" />
						<xsl:with-param name="node"
							select="$codigoProductoSUNAT_listAgencyName" />
						<xsl:with-param name="regexp" select="'^(GS1 US)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 8) ', position(), ' ', $codigoProductoSUNAT_listAgencyName)" />
					</xsl:call-template>

					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'4252'" />
						<xsl:with-param name="node"
							select="$codigoProductoSUNAT_listName" />
						<xsl:with-param name="regexp"
							select="'^(Item Classification)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 8) ', position(), ' ', $codigoProductoSUNAT_listName)" />
					</xsl:call-template>
				</xsl:for-each>

				<xsl:for-each select="./cac:StandardItemIdentification">
					<!-- 25. Código de producto GS1 -->
					<xsl:variable name="codigoProductoGS1"
						select="./cbc:ID" />
					<!-- 25. Tipo de estructura GTIN -->
					<xsl:variable name="codigoProductoGS1_schemeID"
						select="./cbc:ID/@schemeID" />

					<!-- 25. Código de producto GS1 -->
					<xsl:if test="($codigoProductoGS1)">
						<xsl:call-template
							name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist"
								select="'3199'" />
							<xsl:with-param name="node"
								select="$codigoProductoGS1_schemeID" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $codigoProductoGS1_schemeID)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:variable name="lengthCodigoProductoGS1">
						<xsl:choose>
							<xsl:when test="count($codigoProductoGS1) &gt; 0">
								<xsl:value-of
									select="string-length(string($codigoProductoGS1))" />
							</xsl:when>
							<xsl:otherwise>0</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:if
						test="($codigoProductoGS1_schemeID='GTIN-8') and not($lengthCodigoProductoGS1=8)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3201'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea 1: ', position(), ' ', $codigoProductoGS1_schemeID, ' ', $lengthCodigoProductoGS1)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if
						test="($codigoProductoGS1_schemeID='GTIN-13' and not($lengthCodigoProductoGS1=13))">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3201'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea 2: ', position(), ' ', $codigoProductoGS1_schemeID, ' ', $lengthCodigoProductoGS1)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if
						test="($codigoProductoGS1_schemeID='GTIN-14' and not($lengthCodigoProductoGS1=14))">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3201'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea 3: ', position(), ' ', $codigoProductoGS1_schemeID, ' ', $lengthCodigoProductoGS1)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'3200'" />
						<xsl:with-param name="node"
							select="$codigoProductoGS1_schemeID" />
						<xsl:with-param name="regexp"
							select="'^(GTIN-8|GTIN-13|GTIN-14|GS1-128|DataBar GS1|DataMatrix GS1)$'" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $codigoProductoGS1_schemeID)" />
					</xsl:call-template>
				</xsl:for-each>

				<!-- Información Adicional - Transporte terrestre de pasajeros -->
				<!-- 58. Numero de asiento -->
				<!-- 59. Información de manifiesto de pasajeros -->
				<!-- 60. Número de documento de identidad del pasajero -->
				<!-- 61. Tipo de documento de identidad del pasajero -->
				<!-- 62. Nombres y apellidos del pasajero -->
				<!-- 63. Ciudad o lugar de destino -->
				<!-- 64. Ciudad o lugar de origen -->
				<!-- 65. Fecha de inicio programado -->
				<!-- 66. Hora de inicio programado -->

				<!-- Migración de documentos autorizados - Carta Porte Aéreo -->
				<!-- 67. Lugar de origen -->
				<!-- 68. Lugar de destino -->

				<!-- Migración de documentos autorizados - BVME para transporte ferroviario 
					de pasajeros -->
				<!-- 71. Pasajero - Apellidos y Nombres -->
				<!-- 72. Pasajero - Tipo y número de documento de identidad -->
				<!-- 73. Servicio de transporte: Ciudad o lugar de origen -->
				<!-- 74. Servicio de transporte: Ciudad o lugar de destino -->
				<!-- 75. Servicio de transporte:Número de asiento -->
				<!-- 76. Servicio de transporte: Fecha programado de inicio de viaje -->
				<!-- 77. Servicio de transporte: Hora programada de inicio de viaje -->

				<!-- Migración de documentos autorizados - Pago de regalía petrolera -->
				<!-- 80. Decreto Supremo de aprobación del contrato -->
				<!-- 81. Area de contrato (Lote) -->
				<!-- 82. Periodo de pago - Fecha de inicio -->
				<!-- 83. Periodo de pago - Fecha de fin -->

				<!-- Ventas Sector Público -->
				<!-- 85. Numero de Expediente -->
				<!-- 86. Código de unidad ejecutora -->
				<!-- 87. N° de contrato -->
				<!-- 88. N° de proceso de selección -->

				<!-- Detracciones - Recursos Hidrobiológicos -->
				<!-- 92. Matrícula de la Embarcación Pesquera -->
				<!-- 93. Nombre de la Embarcación Pesquera -->
				<!-- 94. Descripción del Tipo de la Especie vendida -->
				<!-- 95. Lugar de descarga -->
				<!-- 96. Cantidad de la Especie vendida -->
				<!-- 97. Fecha de descarga -->

				<!-- Información Adicional - Beneficio de hospedaje -->
				<!-- 114. Número de documento del huesped -->
				<!-- 115. Código de tipo de documento de identidad del huesped -->
				<!-- 116. Código país de emisión del pasaporte -->
				<!-- 117. Apellidos y Nombres o denominación o razón social del huesped -->
				<!-- 118. Código del país de residencia del sujeto no domiciliado -->
				<!-- 119. Fecha de Ingreso al país -->
				<!-- 120. Fecha de Ingreso al Establecimiento -->
				<!-- 121. Fecha de salida del Establecimiento -->
				<!-- 122. Fecha de consumo -->
				<!-- 123. Número de Días de Permanencia -->

				<!-- Información Adicional - Paquete Turístico -->
				<!-- 124. Apellidos y Nombres o denominación o razón social del huesped -->
				<!-- 125. Número de documento del huesped -->
				<!-- 126. Código de tipo de documento de identidad del huesped -->
				<!-- 127. Código país de emisión del pasaporte -->
				<!-- ////  -->
				<!-- 
				<xsl:if test="$tipoOperacion = '2104'">					
					<xsl:if test="($countCodigoConcepto7015For = 0)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3242'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error : ',$numeroLinea,') ',$tipoOperacion,' | ', $countCodigoConcepto7015For)"/>
						</xsl:call-template>							
					</xsl:if>						
		       </xsl:if>
		       -->
				<xsl:for-each select="./cac:AdditionalItemProperty">
					<!-- XX. Nombre del concepto -->
					<xsl:variable name="nombreConcepto" select="./cbc:Name" />
					<!-- XX. Código del concepto -->
					<xsl:variable name="codigoConcepto" select="./cbc:NameCode" />
					<xsl:variable name="codigoConcepto_listName" select="./cbc:NameCode/@listName" />
					<xsl:variable name="codigoConcepto_listAgencyName" select="./cbc:NameCode/@listAgencyName" />
					<xsl:variable name="codigoConcepto_listURI" select="./cbc:NameCode/@listURI" />
					<!-- 58. Número de asiento -->
					<xsl:variable name="codigoConcepto_Value" select="./cbc:Value" />
					<!-- 96. Cantidad de la Especie vendida -->
					<xsl:variable name="cantidadEspecieVendida" select="./cbc:ValueQuantity" />
					<xsl:variable name="cantidadEspecieVendida_unitCode"
						select="./cbc:ValueQuantity/@unitCode" />

					<!-- XX. Nombre del concepto -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'4235'" />
						<xsl:with-param name="node" select="$nombreConcepto" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $nombreConcepto)" />
					</xsl:call-template>

					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4252'" />
						<xsl:with-param name="node" select="$codigoConcepto_listName" />
						<xsl:with-param name="regexp" select="'^(Propiedad del item)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 9) ', position(), ' ', $codigoConcepto_listName)" />
					</xsl:call-template>

					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4251'" />
						<xsl:with-param name="node" select="$codigoConcepto_listAgencyName" />
						<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 9) ', position(), ' ', $codigoConcepto_listAgencyName)" />
					</xsl:call-template>

					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4253'" />
						<xsl:with-param name="node" select="$codigoConcepto_listURI" />
						<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo55)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 4) ', position(), ' ', $codigoConcepto_listURI)" />
					</xsl:call-template>

					<!-- 58 / 64. Información Adicional - Transporte terrestre de pasajeros -->
					<xsl:if test="($codigoConcepto='3050') or ($codigoConcepto='3051') or ($codigoConcepto='3052') or 
							  ($codigoConcepto='3053') or ($codigoConcepto='3054') or ($codigoConcepto='3055') or 
							  ($codigoConcepto='3056') or ($codigoConcepto='3057') or ($codigoConcepto='3058') or 
							  ($codigoConcepto='7017') or ($codigoConcepto='7018') ">
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3064'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 1) ', position(), ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 58. Numero de asiento -->
					<xsl:if test="($codigoConcepto='3050')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
<!-- 							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,14}$'"/> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,19}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 1) ', position(), ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 59. Información de manifiesto de pasajeros -->
					<xsl:if test="($codigoConcepto='3051')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'[\w].{2,20}'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,19}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 2) ', position(), ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 60. Número de documento de identidad del pasajero -->
					<xsl:if test="($codigoConcepto='3052')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'[\w].{2,15}'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,14}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 3) ', position(), ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 61. Tipo de documento de identidad del pasajero -->
					<xsl:if test="($codigoConcepto='3053')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<xsl:with-param name="regexp" select="'(0|1|4|6|7|A|B|C|D|E)'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 4) ', position(), ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 62. Nombres y apellidos del pasajero -->
					<xsl:if test="($codigoConcepto='3054')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'[\w].{2,200}'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,199}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 5) ', position(), ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 63. Ciudad o lugar de destino - Dirección detallada -->
					<xsl:if test="($codigoConcepto='3056')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'[\w].{2,200}'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,199}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 6) ', position(), ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 64. Ciudad o lugar de origen - Dirección detallada -->
					<xsl:if test="($codigoConcepto='3058')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'[\w].{2,200}'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,199}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 7) ', position(), ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 67 / 68. -->
					<xsl:if
						test="($codigoConcepto='4030') or ($codigoConcepto='4031') or ($codigoConcepto='4032') or ($codigoConcepto='4033')">
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3064'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 2) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if
						test="($codigoConcepto='4031') or ($codigoConcepto='4033')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'^[\w].{2,200}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,199}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 8) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 71 / 77. -->
					<xsl:if
						test="($codigoConcepto='4040') or ($codigoConcepto='4041') or ($codigoConcepto='4042') or 
							($codigoConcepto='4043') or ($codigoConcepto='4044') or ($codigoConcepto='4045') or ($codigoConcepto='4046')">
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3064'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 3) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if
						test="($codigoConcepto='4040') or ($codigoConcepto='4043') or ($codigoConcepto='4045')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'^[\w].{2,200}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,199}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 9) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if test="($codigoConcepto='4041')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<xsl:with-param name="regexp" select="'^(0|1|4|6|7|A|B|C|D|E)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 10) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if test="($codigoConcepto='4046')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'^[\w].{1,100}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,99}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 11) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if test="($codigoConcepto='4049')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'^[\w].{1,20}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,19}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 12) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 80 / 81. -->
<!-- 					<xsl:if -->
<!-- 						test="($codigoConcepto='4060') or ($codigoConcepto='4061')"> -->
<!-- 						<xsl:call-template name="existValidateElementNotExist"> -->
<!-- 							<xsl:with-param name="errorCodeNotExist" select="'3064'" /> -->
<!-- 							<xsl:with-param name="node" select="$codigoConcepto_Value" /> -->
<!-- 							<xsl:with-param name="descripcion" -->
<!-- 								select="concat('Error en la linea: ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" /> -->
<!-- 						</xsl:call-template> -->
<!-- 					</xsl:if> -->

					<xsl:if test="($codigoConcepto='4060')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'^[\w].{1,30}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,29}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 13) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if test="($codigoConcepto='4061')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'^[\w].{1,10}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,9}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 14) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 85 / 88. Ventas Sector Público -->
					<xsl:if
						test="($codigoConcepto='5000') or ($codigoConcepto='5001') or ($codigoConcepto='5002') or ($codigoConcepto='5003')">
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3064'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 5) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if test="($codigoConcepto='5000')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'^[\w].{1,20}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,19}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 15) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if test="($codigoConcepto='5001')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'^[\w].{1,10}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,9}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 16) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if
						test="($codigoConcepto='5002') or ($codigoConcepto='5003')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'^[\w].{1,30}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,29}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 17) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 92 / 95. Detracciones - Recursos Hidrobiológicos -->
					<xsl:if
						test="($codigoConcepto='3001') or ($codigoConcepto='3002') or ($codigoConcepto='3003') or ($codigoConcepto='3004')">
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3064'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 6) ', position(), ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 92. Matrícula de la Embarcación Pesquera -->
					<xsl:if test="($codigoConcepto='3001')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'^[^\n].{1,15}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,14}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 18) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 93. Nombre de la Embarcación Pesquera -->
					<xsl:if test="($codigoConcepto='3002')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'^[^\n].{1,100}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,99}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 19) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 94. Descripción del Tipo de la Especie vendida -->
					<xsl:if test="($codigoConcepto='3003')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'^[^\n].{1,150}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,149}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 20) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 95. Lugar de descarga -->
					<xsl:if test="($codigoConcepto='3004')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'^[^\n].{1,100}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,99}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 21) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 96. Cantidad de la Especie vendida -->
					<xsl:if test="($codigoConcepto='3006')">
						<xsl:call-template
							name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist"
								select="'3135'" />
							<xsl:with-param name="node"
								select="$cantidadEspecieVendida" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ',  $codigoConcepto, ' ', $cantidadEspecieVendida)" />
						</xsl:call-template>

						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'4281'" />
							<xsl:with-param name="node"
								select="$cantidadEspecieVendida" />
							<xsl:with-param name="regexp"
								select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ',  $codigoConcepto, ' ', $cantidadEspecieVendida)" />
						</xsl:call-template>

						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'3115'" />
							<xsl:with-param name="node"
								select="$cantidadEspecieVendida_unitCode" />
							<xsl:with-param name="regexp" select="'^(TNE)$'" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ',  $codigoConcepto, ' ', $cantidadEspecieVendida_unitCode)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 114 / 118. Información Adicional - Beneficio de hospedaje -->
					<!-- 124 / 127. Información Adicional - Beneficio de hospedaje -->
<!-- 					<xsl:if -->
<!-- 						test="($codigoConcepto='4000') or ($codigoConcepto='4001') or  -->
<!-- 							($codigoConcepto='4007') or ($codigoConcepto='4008') or ($codigoConcepto='4009')"> -->
<!-- 						<xsl:call-template name="existValidateElementNotExist"> -->
<!-- 							<xsl:with-param name="errorCodeNotExist" select="'3064'" /> -->
<!-- 							<xsl:with-param name="node" select="$codigoConcepto_Value" /> -->
<!-- 							<xsl:with-param name="descripcion" -->
<!-- 								select="concat('Error en la linea: 7) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" /> -->
<!-- 						</xsl:call-template> -->
<!-- 					</xsl:if> -->

					<xsl:if test="($codigoConcepto='4008')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<xsl:with-param name="regexp" select="'^(0|1|4|6|7|A|B|C|D|E)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 22) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if test="($codigoConcepto='4007')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'^[\w].{2,200}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,199}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 23) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if test="($codigoConcepto='4009')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<!-- <xsl:with-param name="regexp" select="'^[\w].{2,20}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,19}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 24) ', position(), ' ',  $codigoConcepto, ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 66. Hora de inicio -->
					<!-- 77 Servicio de transporte: Hora programada de inicio de viaje -->
					<xsl:variable name="horaInicio" select="./cac:UsabilityPeriod/cbc:StartTime" />
					<xsl:if test="($codigoConcepto='3060') or ($codigoConcepto='4047')">
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3172'" />
							<xsl:with-param name="node" select="$horaInicio" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ',  $codigoConcepto, ' ', $horaInicio)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 65. Fecha de inicio -->
					<!-- 76. Servicio de transporte: Hora programada de inicio de viaje -->
					<!-- 82. Periodo de pago - Fecha de inicio -->
					<!-- 97. Fecha de descarga -->
					<!-- 119 - 122. Fecha -->
					<xsl:variable name="fechaInicioLinea" select="./cac:UsabilityPeriod/cbc:StartDate" />
					<xsl:if test="($codigoConcepto='3005') or ($codigoConcepto='4002') or ($codigoConcepto='4003') or 
							($codigoConcepto='4004') or ($codigoConcepto='4006') or ($codigoConcepto='4048') or 
							($codigoConcepto='3059')">
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3065'" />
							<xsl:with-param name="node" select="$fechaInicioLinea" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 6) ', position(), ' ',  $codigoConcepto, ' ', $fechaInicioLinea)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 83. Periodo de pago - Fecha de fin -->
					<!-- 83. Periodo de pago - Fecha de fin -->
<!-- 					<xsl:variable name="fechaFinLinea" select="./cac:UsabilityPeriod/cbc:EndDate" /> -->
<!-- 					<xsl:if test="($codigoConcepto='4063')"> -->
<!-- 						<xsl:call-template 	name="existValidateElementNotExist"> -->
<!-- 							<xsl:with-param name="errorCodeNotExist" select="'3065'" /> -->
<!-- 							<xsl:with-param name="node" select="$fechaFinLinea" /> -->
<!-- 							<xsl:with-param name="descripcion" -->
<!-- 								select="concat('Error en la linea: 4) ', position(), ' ',  $codigoConcepto, ' ', $fechaFinLinea)" /> -->
<!-- 						</xsl:call-template> -->
<!-- 					</xsl:if> -->

			    	<!-- 118. Declaración Aduanera de Mercancías (DAM) -->
					<xsl:if test="($codigoConcepto='7021')">					
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4202'"/>
							<xsl:with-param name="node" select="$codigoConcepto_Value"/>
							<xsl:with-param name="regexp" select="'^[0-9]{4}-[0-9]{2}-[0-9]{3}-[0-9]{6}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $codigoConcepto, ' ',$codigoConcepto_Value)"/>
						</xsl:call-template>	
					</xsl:if>	

			    	<!-- 120.Número  de contrato -->
					<xsl:if test="($codigoConcepto='7004') or ($codigoConcepto = '7005') or ($codigoConcepto = '7012')">					
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3064'"/>
							<xsl:with-param name="node" select="$codigoConcepto_Value"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 8) ', position(), ' ', $codigoConcepto, ' ',$codigoConcepto_Value)"/>
						</xsl:call-template>							
					</xsl:if>						

			    	<!-- 127.Numero de póliza -->
					<xsl:if test="($codigoConcepto='7013') or ($codigoConcepto = '7015') or ($codigoConcepto = '7016')">					
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3064'"/>
							<xsl:with-param name="node" select="$codigoConcepto_Value"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 9) ', position(), ' ', $codigoConcepto, ' ',$codigoConcepto_Value)"/>
						</xsl:call-template>							
					</xsl:if>	
					
			    	<!-- 120. Número  de contrato -->
					<xsl:if test="($codigoConcepto='7004')">					
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'"/>
							<xsl:with-param name="node" select="$codigoConcepto_Value"/>
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{3,50}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 25) ', position(), ' ', $codigoConcepto, ' ',$codigoConcepto_Value)"/>
						</xsl:call-template>	
							
						<xsl:call-template name="regexpValidateElementIfExistTrue">
							<xsl:with-param name="errorCodeValidate" select="'4280'"/>
							<xsl:with-param name="node" select="$codigoConcepto_Value"/>
							<xsl:with-param name="regexp" select="'[\t\s]'"/>
							<xsl:with-param name="isError" select="false()"/> 
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 26) ', position(), ' ', $codigoConcepto, ' ',$codigoConcepto_Value)"/>
						</xsl:call-template>					
					</xsl:if>								

					<!-- 121. Fecha del otorgamiento del crédito -->
					<xsl:if test="($codigoConcepto='7005')">					
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'"/>
							<xsl:with-param name="node" select="$codigoConcepto_Value"/>
							<xsl:with-param name="regexp" select="'^\d{4}\-(0[1-9]|1[012])\-(0[1-9]|[12][0-9]|3[01])$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 27) ', position(), ' ', $codigoConcepto, ' ',$codigoConcepto_Value)"/>
						</xsl:call-template>	
					</xsl:if>		
					
					<!-- 126. Monto del crédito otorgado  -->
					<xsl:if test="($codigoConcepto='7012')">					
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'"/>
							<xsl:with-param name="node" select="$codigoConcepto_Value"/>
							<xsl:with-param name="regexp" select="'^[0-9]{1,15}(\.[0-9]{1,2})?$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 28) ', position(), ' ', $codigoConcepto, ' ',$codigoConcepto_Value)"/>
						</xsl:call-template>	
					</xsl:if>							

			    	<!-- 127. Numero de póliza -->
					<xsl:if test="($codigoConcepto='7013')">					
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'"/>
							<xsl:with-param name="node" select="$codigoConcepto_Value"/>
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{1,50}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 29) ', position(), ' ', $codigoConcepto, ' ',$codigoConcepto_Value)"/>
						</xsl:call-template>	
					</xsl:if>		
					
			    	<!-- 128.Tipo de seguro  -->
					<xsl:if test="($codigoConcepto='7015')">									
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'"/>
							<xsl:with-param name="node" select="$codigoConcepto_Value"/>
							<xsl:with-param name="regexp" select="'^(1|2|3)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 30) ', position(), ' ', $codigoConcepto, ' ',$codigoConcepto_Value)"/>
						</xsl:call-template>	

						<xsl:if test="($tipoOperacion='2104')">	
							<xsl:if test="(($countCodigoConcepto7013For = 0) and
								($countCodigoConcepto7014For = 0) and ($countCodigoConcepto7016For = 0))">
								<xsl:call-template name="regexpValidateElementIfExist">
									<xsl:with-param name="errorCodeValidate" select="'2898'"/>
									<xsl:with-param name="node" select="$codigoConcepto_Value"/>
									<xsl:with-param name="regexp" select="'^(1|2)$'" />
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', position(), ' ', $codigoConcepto, ' ',$codigoConcepto_Value, ' ',$countCodigoConcepto7013For)"/>
								</xsl:call-template>		
							</xsl:if>	

							<xsl:if test="(($countCodigoConcepto7013For = 0))">
								<xsl:call-template name="regexpValidateElementIfExist">
									<xsl:with-param name="errorCodeValidate" select="'2899'"/>
									<xsl:with-param name="node" select="$codigoConcepto_Value"/>
									<xsl:with-param name="regexp" select="'^(3)$'" />
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', position(), ' ', $codigoConcepto, ' ',$codigoConcepto_Value, ' ',$countCodigoConcepto7013For)"/>
								</xsl:call-template>		
							</xsl:if>																		
						</xsl:if>
					</xsl:if>												
					
					<!-- 129. comprobante emitido por empresas de seguros  -->
					<xsl:if test="($codigoConcepto='7016')">					
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'"/>
							<xsl:with-param name="node" select="$codigoConcepto_Value"/>
							<xsl:with-param name="regexp" select="'^[0-9]{1,15}(\.[0-9]{1,2})?$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 31) ', position(), ' ', $codigoConcepto, ' ',$codigoConcepto_Value)"/>
						</xsl:call-template>	
					</xsl:if>	
										
					<!-- 132. Codigo unico de identificación del SPP (CUSPP) -->
					<xsl:if test="($codigoConcepto='7017')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,12}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 32) ', position(), ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>
					
					<xsl:if test="($codigoConcepto='7018')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<xsl:with-param name="regexp" select="'^[12]\d{3}\-(0[1-9]|1[0-2])$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 33) ', position(), ' ', $codigoConcepto_Value)" />
						</xsl:call-template>
					</xsl:if>	
									
					<xsl:if test="($codigoConcepto='7019')">					
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3064'"/>
							<xsl:with-param name="node" select="$cantidadEspecieVendida"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 10) ', position(), ' ', $codigoConcepto, ' ',$cantidadEspecieVendida)"/>
						</xsl:call-template>		
						
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4281'"/>
							<xsl:with-param name="node" select="$cantidadEspecieVendida"/>
							<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $cantidadEspecieVendida)"/>
						</xsl:call-template>											
					</xsl:if>	

					<xsl:if test="$codigoConcepto = '7014'">
						<xsl:variable name="countFechaInicio" select="count(./cac:UsabilityPeriod/cbc:StartDate)"/>
						<xsl:if test="$countFechaInicio = 0">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3243'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoConcepto, ' ', $countFechaInicio)"/>
							</xsl:call-template>
						</xsl:if>
						<xsl:variable name="countFechaFin" select="count(./cac:UsabilityPeriod/cbc:EndDate)"/>
						<xsl:if test="$countFechaFin = 0">	
							<xsl:call-template name="addWarning">
								<xsl:with-param name="warningCode" select="'4366'" />
								<xsl:with-param name="warningMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoConcepto, ' ', $countFechaFin)"/>
							</xsl:call-template>							
						</xsl:if>	
					</xsl:if>	
															
					<xsl:for-each select="./cac:UsabilityPeriod">
						<!-- 65. Fecha de inicio -->
						<!-- 97. Fecha de descarga -->
						<!-- 119 - 122. Fecha -->
						<xsl:variable name="fechaInicio" select="./cbc:StartDate" />
						<!-- 83. Periodo de pago - Fecha de fin -->
						<xsl:variable name="fechaFin" select="./cbc:EndDate" />
						<!-- 123 Número de días de permanencia -->
						<xsl:variable name="numeroDiasPermanencia" select="./cbc:DurationMeasure" />
						<xsl:variable name="numeroDiasPermanencia_unitCode" select="./cbc:DurationMeasure/@unitCode" />

						<!-- 131. Fecha de inicio de vigencia de cobertura -->					
						<xsl:if test="$codigoConcepto = '7014'">
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'3243'"/>
								<xsl:with-param name="node" select="$fechaInicio" />
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $codigoConcepto, ' ', $fechaInicio)"/>
							</xsl:call-template>							
							
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4280'" />
								<xsl:with-param name="node" select="$fechaInicio" />
								<xsl:with-param name="regexp" select="'^\d{4}\-(0[1-9]|1[012])\-(0[1-9]|[12][0-9]|3[01])$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: 34) ', position(), ' ', $codigoConcepto, ' ', $fechaInicio)"/>
							</xsl:call-template>
							
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'4366'"/>
								<xsl:with-param name="node" select="$fechaFin" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $codigoConcepto, ' ', $fechaFin)"/>
							</xsl:call-template>			
							
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4280'" />
								<xsl:with-param name="node" select="$fechaFin" />
								<xsl:with-param name="regexp" select="'^\d{4}\-(0[1-9]|1[012])\-(0[1-9]|[12][0-9]|3[01])$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: 35) ', position(), ' ', $codigoConcepto, ' ', $fechaFin)"/>
							</xsl:call-template>														
						</xsl:if>	

						<!-- 123 Número de Días de Permanencia -->
						<xsl:if test="($codigoConcepto='4005')">
<!-- 							<xsl:call-template name="existValidateElementNotExist"> -->
<!-- 								<xsl:with-param name="errorCodeNotExist" select="'3135'" /> -->
<!-- 								<xsl:with-param name="node" select="$numeroDiasPermanencia" /> -->
<!-- 								<xsl:with-param name="descripcion" -->
<!-- 									select="concat('Error en la linea: ', position(), ' ',  $codigoConcepto, ' ', $numeroDiasPermanencia)" /> -->
<!-- 							</xsl:call-template> -->

							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4281'" />
								<xsl:with-param name="node" select="$numeroDiasPermanencia" />
								<xsl:with-param name="regexp" select="'^[0-9]{1,4}$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: ', position(), ' ',  $codigoConcepto, ' ', $numeroDiasPermanencia)" />								
							</xsl:call-template>

							<xsl:call-template
								name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4313'" />
								<!-- <xsl:with-param name="node" select="./cbc:DurationMeasure/@unitCode"/> -->
								<xsl:with-param name="node" select="$numeroDiasPermanencia_unitCode" />
								<xsl:with-param name="regexp" select="'^(DAY)$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: ', position(), ' ', $numeroDiasPermanencia_unitCode)" />
							</xsl:call-template>
						</xsl:if>

						<xsl:if test="($codigoConcepto='3006')">
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4281'" />
								<xsl:with-param name="node" select="$numeroDiasPermanencia" />
								<xsl:with-param name="regexp" select="'^[\d].{0,3}$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: ', position(), ' ', $numeroDiasPermanencia)" />
							</xsl:call-template>
						</xsl:if>

						<xsl:if test="($codigoConcepto='7014')">							
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4280'" />
								<xsl:with-param name="node" select="$fechaInicio" />
								<xsl:with-param name="regexp" select="'^\d{4}\-(0[1-9]|1[012])\-(0[1-9]|[12][0-9]|3[01])$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: 36) ', position(), ' ', $codigoConcepto, ' ', $fechaInicio)"/>
							</xsl:call-template>
							
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4280'" />
								<xsl:with-param name="node" select="$fechaFin" />
								<xsl:with-param name="regexp" select="'^\d{4}\-(0[1-9]|1[012])\-(0[1-9]|[12][0-9]|3[01])$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: 37) ', position(), ' ', $codigoConcepto, ' ', $fechaFin)"/>
							</xsl:call-template>							
						</xsl:if>								
					</xsl:for-each>
				</xsl:for-each>            					
			</xsl:for-each>

			<xsl:variable name="sumValorUnitarioItem"
				select="sum(./cac:Price/cbc:PriceAmount)" />
			<!-- 27. Valor unitario por ítem -->
			<xsl:variable name="valorUnitarioItem"
				select="./cac:Price/cbc:PriceAmount" />
			<xsl:variable name="valorUnitarioItem_currencyID"
				select="./cac:Price/cbc:PriceAmount/@currencyID" />

			<!-- 27. Valor unitario por ítem -->
			<xsl:call-template
				name="existValidateElementNotExist">
				<xsl:with-param name="errorCodeNotExist"
					select="'2068'" />
				<xsl:with-param name="node"
					select="$valorUnitarioItem" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', position(), ' ', $valorUnitarioItem)" />
			</xsl:call-template>

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'2369'" />
				<xsl:with-param name="node"
					select="$valorUnitarioItem" />
				<xsl:with-param name="regexp"
					select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', position(), ' ', $valorUnitarioItem)" />
			</xsl:call-template>

			<xsl:if
				test="($sumMontoBase9996LineaSub &gt; 0) and ($valorUnitarioItem &gt; 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2640'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $sumMontoBase9996LineaSub, ' ',$valorUnitarioItem)" />
				</xsl:call-template>
			</xsl:if>

			<xsl:if
				test="($valorUnitarioItem_currencyID) and not($tipoMoneda = $valorUnitarioItem_currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: 1) ', position(), ' ', $tipoMoneda, ' ',$valorUnitarioItem_currencyID)" />
				</xsl:call-template>
			</xsl:if>

			<!-- 28. Precio de venta unitario por item -->
			<!-- 29. Valor referencial unitario por ítem en operaciones no onerosas -->
			<xsl:variable name="countPrecioVentaUnitarioItem"
				select="count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceAmount)" />
			<xsl:if test="($countPrecioVentaUnitarioItem=0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2028'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countPrecioVentaUnitarioItem)" />
				</xsl:call-template>
			</xsl:if>

			<xsl:for-each select="./cac:PricingReference">
				<xsl:for-each select="./cac:AlternativeConditionPrice">
					<!-- 28. Precio de venta unitario por item -->
					<!-- 29. Valor referencial unitario por ítem en operaciones no onerosas -->
					<xsl:variable name="precioVentaUnitarioItem"
						select="./cbc:PriceAmount" />
					<xsl:variable
						name="precioVentaUnitarioItem_currencyID"
						select="./cbc:PriceAmount/@currencyID" />
					<!-- 28 / 29. Código de precio -->
					<xsl:variable name="codigoPrecio"
						select="./cbc:PriceTypeCode" />
					<xsl:variable name="codigoPrecio_listName"
						select="./cbc:PriceTypeCode/@listName" />
					<xsl:variable name="codigoPrecio_listAgencyName"
						select="./cbc:PriceTypeCode/@listAgencyName" />
					<xsl:variable name="codigoPrecio_listURI"
						select="./cbc:PriceTypeCode/@listURI" />

					<!-- Moneda debe ser la misma en todo el documento -->
					<xsl:if
						test="($precioVentaUnitarioItem_currencyID) and not($tipoMoneda=$precioVentaUnitarioItem_currencyID)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2071'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: 2) ', position(), ' ', $tipoMoneda, ' ',$precioVentaUnitarioItem_currencyID)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 28 / 29. Precio de venta unitario por item -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2367'" />
						<xsl:with-param name="node" select="$precioVentaUnitarioItem" />
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $precioVentaUnitarioItem)" />
					</xsl:call-template>
					
					<xsl:if test="not($sumMontoBase9996LineaSub &gt; 0)">	
						<xsl:variable name="totalCalculado" select="number($valorVentaLinea) + number($montoTotalTributosLinea) - number($montoDescuentosLinea01) + number($montoCargosLinea48)"/>	
						<xsl:variable name="totalImporteCalculado" select="number($totalCalculado) div number($cantidadUnidadesLinea)"/>	
						<xsl:variable name="dif_TotalImporte" select="$precioVentaUnitarioItem - $totalImporteCalculado" />			
						<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
							<xsl:call-template name="addWarning">
								<xsl:with-param name="warningCode" select="'4287'" />
								<xsl:with-param name="warningMessage"
									select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $precioVentaUnitarioItem, ' ', $totalImporteCalculado,' ', $valorVentaLinea, ' ',$montoTotalTributosLinea, ' ',$montoDescuentosLinea01, ' ',$montoCargosLinea48, ' ',$cantidadUnidadesLinea)"/>
							</xsl:call-template>
						</xsl:if>	
					</xsl:if>					

					<xsl:if test="not($sumMontoBase9996LineaSub &gt; 0) and ($codigoPrecio = '02') and ($precioVentaUnitarioItem &gt; 0)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3224'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $codigoPrecio, ' ',$precioVentaUnitarioItem)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if test="($sumMontoBase9996LineaSub &gt; 0) and not($codigoPrecio = '02') and ($precioVentaUnitarioItem &gt; 0)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3234'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $sumMontoBase9996LineaSub, ' ',$codigoPrecio)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 28 / 29. Código de precio -->
					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'2410'" />
						<xsl:with-param name="node" select="$codigoPrecio" />
						<xsl:with-param name="regexp" select="'^(01|02|03)$'" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $codigoPrecio)" />
					</xsl:call-template>

					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'4252'" />
						<xsl:with-param name="node"
							select="$codigoPrecio_listName" />
						<xsl:with-param name="regexp"
							select="'^(Tipo de Precio)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 10) ', position(), ' ', $codigoPrecio_listName)" />
					</xsl:call-template>

					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'4251'" />
						<xsl:with-param name="node"
							select="$codigoPrecio_listAgencyName" />
						<xsl:with-param name="regexp"
							select="'^(PE:SUNAT)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 10) ', position(), ' ', $codigoPrecio_listAgencyName)" />
					</xsl:call-template>

					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'4253'" />
						<xsl:with-param name="node"
							select="$codigoPrecio_listURI" />
						<xsl:with-param name="regexp"
							select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo16)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 5) ', position(), ' ', $codigoPrecio_listURI)" />
					</xsl:call-template>
				</xsl:for-each>
			</xsl:for-each>

			<!-- 28 / 29. Código de precio -->
			<xsl:if
				test="($countCodigoPrecio01Linea &gt; 1) or ($countCodigoPrecio02Linea &gt; 1) or ($countCodigoPrecio03Linea &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2409'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoPrecio01Linea, ' ', $countCodigoPrecio02Linea, ' ', $countCodigoPrecio03Linea)" />
				</xsl:call-template>
			</xsl:if>

			<!-- 30. Monto total de impuestos por línea -->
			<xsl:variable name="countTaxTotal"
				select="count(./cac:TaxTotal)" />
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

			<!-- 31. Código de tributo por línea -->
			<xsl:if
				test="($countCodigoTributoLinea1000 &gt; 1) or ($countCodigoTributoLinea1016 &gt; 1) or ($countCodigoTributoLinea2000 &gt; 1) 
					or ($countCodigoTributoLinea9995 &gt; 1) or ($countCodigoTributoLinea9996 &gt; 1) or ($countCodigoTributoLinea9997 &gt; 1)   
 					or ($countCodigoTributoLinea9998 &gt; 1) or ($countCodigoTributoLinea9999 &gt; 1) or ($countCodigoTributoLinea7152 &gt; 1) ">  
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3067'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoTributoLinea1000, ' ', $countCodigoTributoLinea2000, ' ', $countCodigoTributoLinea9999)" />
				</xsl:call-template>
			</xsl:if>

			<xsl:variable name="countCodigoTributoLineaFor" select="$countCodigoTributoLinea1000 + $countCodigoTributoLinea1016 + $countCodigoTributoLinea2000 + $countCodigoTributoLinea9995 + $countCodigoTributoLinea9996 + $countCodigoTributoLinea9997 + $countCodigoTributoLinea9998 + $countCodigoTributoLinea9999" />
			<xsl:variable name="sumaMontoBaseFor" select="sum(./cac:TaxTotal/cac:TaxSubtotal/cbc:TaxableAmount)" />
			<xsl:if test="($countCodigoTributoLineaFor>1)">
				<xsl:if
					test="($sumaMontoBaseFor>0) and not((($countCodigoTributoLinea1000=1) and (($countCodigoTributoLinea2000=1) or ($countCodigoTributoLinea9999=1)))
					or (($countCodigoTributoLinea1016=1) and ($countCodigoTributoLinea9999=1))
					or (($countCodigoTributoLinea9995=1) and ($countCodigoTributoLinea9999=1))
					or (($countCodigoTributoLinea9996=1) and (($countCodigoTributoLinea2000=1) or ($countCodigoTributoLinea9999=1)))
					or (($countCodigoTributoLinea9997=1) and (($countCodigoTributoLinea2000=1) or ($countCodigoTributoLinea9999=1)))
					or (($countCodigoTributoLinea9998=1) and (($countCodigoTributoLinea2000=1) or ($countCodigoTributoLinea9999=1))))">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3223'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $sumaMontoBaseFor, ' ', $countCodigoTributoLineaFor)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>		
			
			<xsl:if  test="not(($tipoOperacion = '2100') or ($tipoOperacion = '2101') or 
					($tipoOperacion = '2102') or ($tipoOperacion = '2103') or ($tipoOperacion = '2104'))">						
				<xsl:if  test="($sumMontoBase1000LineaSub=0) and ($sumMontoBase1016LineaSub=0) and 
						($sumMontoBase9995LineaSub=0) and ($sumMontoBase9996LineaSub=0) and 
						($sumMontoBase9997LineaSub=0) and ($sumMontoBase9998LineaSub=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3105'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $sumMontoBase1000LineaSub, ' ', $sumMontoBase1016LineaSub, ' ', $sumMontoBase9995LineaSub, ' ', $sumMontoBase9996LineaSub, ' ', $sumMontoBase9997LineaSub, ' ', $sumMontoBase9998LineaSub)"/>
					</xsl:call-template>
				</xsl:if>							
			</xsl:if>	
			
			<!-- Afectacion al IGV por item - Sistema de ISC por item -->
			<xsl:for-each select="./cac:TaxTotal">
				<!-- 30. Monto total de impuestos del ítem -->
				<xsl:variable name="montoTotalImpuestosLinea" select="./cbc:TaxAmount" />
				<xsl:variable name="montoTotalImpuestosLinea_currencyID" select="./cbc:TaxAmount/@currencyID" />

				<!-- 31. Monto de IGV/IVAP de la línea -->
				<!-- 32. Monto del tributo de la línea -->
				<xsl:variable name="montoTributoLinea_1000" select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxAmount)" />
				<xsl:variable name="montoTributoLinea_1016" select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:TaxAmount)" />
				<xsl:variable name="montoTributoLinea_2000" select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxAmount)" />
				<xsl:variable name="montoTributoLinea_7152" select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='7152']/cbc:TaxAmount)"/>
				<xsl:variable name="montoTributoLinea_9999" select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxAmount)" />

				<xsl:variable name="countTributoLinea_1000" select="count(./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])"/>	
				<xsl:variable name="countTributoLinea_1016" select="count(./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1016'])"/>	
				<xsl:variable name="countTributoLinea_9995" select="count(./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9995'])"/>	
				<xsl:variable name="countTributoLinea_9996" select="count(./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9996'])"/>	
				<xsl:variable name="countTributoLinea_2000" select="count(./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000'])"/>	
				<xsl:variable name="countTributoLinea_7152" select="count(./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='7152'])"/>	
				<xsl:variable name="countTributoLinea_9999" select="count(./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9999'])"/>	

				<!-- Moneda debe ser la misma en todo el documento -->
				<xsl:if
					test="($montoTotalImpuestosLinea_currencyID) and not($tipoMoneda = $montoTotalImpuestosLinea_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 3) ', position(), ' ', $tipoMoneda, ' ',$montoTotalImpuestosLinea_currencyID)" />
					</xsl:call-template>
				</xsl:if>

				<!-- 30. Monto total de impuestos del ítem -->
				<xsl:if test="($montoTotalImpuestosLinea)">
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'3021'" />
						<xsl:with-param name="node" select="$montoTotalImpuestosLinea" />
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $montoTotalImpuestosLinea)" />
					</xsl:call-template>
					
					<xsl:if test="(($countTributoLinea_1000 &gt; 0) or ($countTributoLinea_1016 &gt; 0) or 
								($countTributoLinea_2000 &gt; 0) or ($countTributoLinea_7152 &gt; 0) or ($countTributoLinea_9999 &gt; 0))">											
					    <xsl:variable name="sumaMontoTributoLinea" select="$montoTributoLinea_1000 + $montoTributoLinea_1016 + $montoTributoLinea_2000 + montoTributoLinea_7152 + $montoTributoLinea_9999"/>
					    <xsl:variable name="dif_MontoTotalImpuestosLinea" select="$montoTotalImpuestosLinea - $sumaMontoTributoLinea" />
						<xsl:if test="($dif_MontoTotalImpuestosLinea &lt; -1) or ($dif_MontoTotalImpuestosLinea &gt; 1)">
							<xsl:call-template name="addWarning">
								<xsl:with-param name="warningCode" select="'4293'" />
								<xsl:with-param name="warningMessage"
									select="concat('Error en la linea: ', position(), ' ', $dif_MontoTotalImpuestosLinea,'-', $montoTotalImpuestosLinea,'-',$sumaMontoTributoLinea, '-', $montoTributoLinea_1000,'-', $montoTributoLinea_1016,'-', $montoTributoLinea_2000,'-', $montoTributoLinea_7152,'-', $montoTributoLinea_9999)"/>
							</xsl:call-template>
						</xsl:if>
					</xsl:if>
				</xsl:if>

				<!-- 31. Afectación al IGV por la línea -->
				<!-- 31. Afectación IVAP por la línea -->
				<!-- 32. Afectación del ISC por la línea -->
				<!-- 32. Afectacion Otros Tributos -->
				<xsl:for-each select="./cac:TaxSubtotal">
					<!-- 31. Monto base -->
					<xsl:variable name="montoBase" select="./cbc:TaxableAmount" />
					<xsl:variable name="monedaBase" select="./cbc:TaxableAmount/@currencyID" />
					<!-- 31. Monto de IGV/IVAP de la línea -->
					<!-- 32. Monto del tributo de la línea -->
					<xsl:variable name="montoIGV_IVAPLinea" select="./cbc:TaxAmount" />
					<xsl:variable name="montoIGV_IVAPLinea_currencyID" select="./cbc:TaxAmount/@currencyID" />
					<!-- 38. Impuesto al consumo de bolsas de plástico por ítem -->
					<xsl:variable name="cantidadBolsaPlasticoLinea" select="./cbc:BaseUnitMeasure"/>
					<xsl:variable name="cantidadBolsaPlasticoLinea_unitCode" select="./cbc:BaseUnitMeasure/@unitCode"/>
					
					<xsl:if test="($monedaBase) and not($tipoMoneda = $monedaBase)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2071'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: 4) ', position(), ' ', $tipoMoneda, ' ',$monedaBase)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if
						test="($montoIGV_IVAPLinea_currencyID) and not($tipoMoneda = $montoIGV_IVAPLinea_currencyID)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2071'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: 5) ', position(), ' ', $tipoMoneda, ' ',$montoIGV_IVAPLinea_currencyID)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 31. Monto base -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'3031'" />
						<xsl:with-param name="node" select="$montoBase" />
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $montoBase)" />
					</xsl:call-template>

					<!-- 31. Monto de IGV/IVAP de la línea -->
					<xsl:call-template name="existAndRegexpValidateElement">
						<xsl:with-param name="errorCodeNotExist" select="'2033'" />
						<xsl:with-param name="errorCodeValidate" select="'2033'" />
						<xsl:with-param name="node" select="$montoIGV_IVAPLinea" />
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: ', position(), ' ', $montoIGV_IVAPLinea)" />
					</xsl:call-template>	

					<!-- 31. Monto de IGV/IVAP de la línea -->					
					<xsl:if
						test="(($countCodigoTributoLinea1000 &gt; 0) or ($countCodigoTributoLinea1016 &gt; 0)) and ($montoBase &gt; 0.06) and ($montoIGV_IVAPLinea=0)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3111'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $montoBase, ' ', $montoIGV_IVAPLinea)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:for-each select="./cac:TaxCategory">
						<!-- 31. Tasa del IGV o Tasa del IVAP -->
						<!-- 32. Tasa del tributo -->
						<xsl:variable name="tasaIGVoIVAP" select="./cbc:Percent" />
						<!-- 31. Afectación al IGV o IVAP cuando corresponda -->
						<xsl:variable name="afectacionIGVoIVAP" select="./cbc:TaxExemptionReasonCode" />
						<xsl:variable name="afectacionIGVoIVAP_listAgencyName" select="./cbc:TaxExemptionReasonCode/@listAgencyName" />
						<xsl:variable name="afectacionIGVoIVAP_listName" select="./cbc:TaxExemptionReasonCode/@listName" />
						<xsl:variable name="afectacionIGVoIVAP_listURI" select="./cbc:TaxExemptionReasonCode/@listURI" />
						<!-- 31. Tipo de sistema de ISC -->
						<xsl:variable name="tipoSistemaISC" select="./cbc:TierRange" />
						<!-- 38. Impuesto al consumo de bolsas de plástico por ítem -->	
						<xsl:variable name="montoUnitarioLinea" select="./cbc:PerUnitAmount"/>

						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'3102'" />
							<xsl:with-param name="node" select="$tasaIGVoIVAP" />
							<xsl:with-param name="regexp" select="'^[0-9]{1,3}(\.[0-9]{1,5})?$'" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $tasaIGVoIVAP)" />
						</xsl:call-template>

						<!-- 31. Monto de IGV/IVAP de la línea -->
						<xsl:if
							test="($countCodigoTributoLinea9996 &gt; 0) and ($montoBase &gt; 0.06) and (($afectacionIGVoIVAP='11') or ($afectacionIGVoIVAP='12') or ($afectacionIGVoIVAP='13')
								 or ($afectacionIGVoIVAP='14') or ($afectacionIGVoIVAP='15') or ($afectacionIGVoIVAP='16') or ($afectacionIGVoIVAP='17')) and ($montoIGV_IVAPLinea=0)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3111'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $montoBase, ' ', $montoIGV_IVAPLinea, ' ', $countCodigoTributoLinea9996)" />
							</xsl:call-template>
						</xsl:if>

						<xsl:if
							test="($countCodigoTributoLinea9996 &gt; 0) and ($montoBase &gt; 0) and (($afectacionIGVoIVAP='21') or($afectacionIGVoIVAP='31') 
								or ($afectacionIGVoIVAP='32') or ($afectacionIGVoIVAP='33') or ($afectacionIGVoIVAP='34') or ($afectacionIGVoIVAP='35') 
								or ($afectacionIGVoIVAP='36') or ($afectacionIGVoIVAP='37') or ($afectacionIGVoIVAP='40')) and not($montoIGV_IVAPLinea=0)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3110'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea b) ', position(), ' ', $montoBase, ' ', $montoIGV_IVAPLinea, ' ', $countCodigoTributoLinea9996)" />
							</xsl:call-template>
						</xsl:if>

						<xsl:if
							test="(($afectacionIGVoIVAP='10') or($afectacionIGVoIVAP='11') or ($afectacionIGVoIVAP='12') or ($afectacionIGVoIVAP='13') or ($afectacionIGVoIVAP='14') 
								or ($afectacionIGVoIVAP='15') or ($afectacionIGVoIVAP='16') or ($afectacionIGVoIVAP='17'))">
							<xsl:variable name="montoTributo"  select="($montoBase * $tasaIGVoIVAP) * 0.01" />
							<xsl:variable name="dif_MontoIGV_IVAPLinea"  select="$montoIGV_IVAPLinea - $montoTributo" />
							<xsl:if
								test="($dif_MontoIGV_IVAPLinea &lt; -1) or ($dif_MontoIGV_IVAPLinea &gt; 1)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3103'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_MontoIGV_IVAPLinea, ' ', $montoIGV_IVAPLinea, ' ', $montoTributo, ' ', $montoBase,' ', $tasaIGVoIVAP)" />
								</xsl:call-template>
							</xsl:if>
						</xsl:if>

						<!-- 31. Tasa del IGV o Tasa del IVAP -->
<!-- 						<xsl:if test="not($afectacionIGVoIVAP = '40')">																 -->
<!-- 							<xsl:if test="($tipoOperacion='0200' or $tipoOperacion='0201' or $tipoOperacion='0202' or  -->
<!-- 								$tipoOperacion='0203' or $tipoOperacion='0204' or $tipoOperacion='0205' or  -->
<!-- 								$tipoOperacion='0206' or $tipoOperacion='0207' or $tipoOperacion='0208')">																 -->
<!-- 								<xsl:call-template name="rejectCall"> -->
<!-- 									<xsl:with-param name="errorCode" select="'2642'" /> -->
<!-- 									<xsl:with-param name="errorMessage" -->
<!-- 										select="concat('Error en la linea: ', position(), ' | ', $afectacionIGVoIVAP, ' | ', $tipoOperacion)"/> -->
<!-- 								</xsl:call-template> -->
<!-- 							</xsl:if> -->
<!-- 						</xsl:if> -->

						<xsl:if
							test="($afectacionIGVoIVAP='17') and ($montoBase &gt; 0) and ($sumMontoBaseAfectaIGVNot17For &gt; 0)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2644'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', $numeroLinea, ') ', $afectacionIGVoIVAP, ' ', $montoBase, ' ', $sumMontoBaseAfectaIGVNot17For)" />
							</xsl:call-template>
						</xsl:if>

						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4251'" />
							<xsl:with-param name="node" select="$afectacionIGVoIVAP_listAgencyName" />
							<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 11) ', $afectacionIGVoIVAP_listAgencyName)" />
						</xsl:call-template>

						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4252'" />
							<xsl:with-param name="node" select="$afectacionIGVoIVAP_listName" />
							<xsl:with-param name="regexp" select="'^(Afectacion del IGV)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 11) ', $afectacionIGVoIVAP_listName)" />
						</xsl:call-template>

						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4253'" />
							<xsl:with-param name="node" select="$afectacionIGVoIVAP_listURI" />
							<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo07)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 6) ', $afectacionIGVoIVAP_listURI)" />
						</xsl:call-template>

						<xsl:for-each select="./cac:TaxScheme">
							<!-- 31 / 32. Código de tributo por línea -->
							<xsl:variable name="codigoTributoLinea" select="./cbc:ID" />
							<xsl:variable name="codigoTributoLinea_schemeName" select="./cbc:ID/@schemeName" />
							<xsl:variable name="codigoTributoLinea_schemeAgencyName" select="./cbc:ID/@schemeAgencyName" />
							<xsl:variable name="codigoTributoLinea_schemeURI" select="./cbc:ID/@schemeURI" />
							<!-- 31 / 32. Nombre de tributo por línea -->
							<xsl:variable name="nombreTributoLinea" select="./cbc:Name" />
							<!-- 31 / 32. Código internacional tributo por línea -->
							<xsl:variable name="codigoInternacionalTributoLinea" select="./cbc:TaxTypeCode" />

							<!-- 31. Tasa del IGV o Tasa del IVAP -->
							<xsl:if test="(($codigoTributoLinea = 1000) or ($codigoTributoLinea = 1016))">						
								<xsl:if test="not($afectacionIGVoIVAP='40')">																
									<xsl:if test="($tipoOperacion='0200' or $tipoOperacion='0201' or $tipoOperacion='0202' or 
										$tipoOperacion='0203' or $tipoOperacion='0204' or $tipoOperacion='0205' or 
										$tipoOperacion='0206' or $tipoOperacion='0207' or $tipoOperacion='0208')">																
										<xsl:call-template name="rejectCall">
											<xsl:with-param name="errorCode" select="'2642'" />
											<xsl:with-param name="errorMessage"
												select="concat('Error en la linea: ', position(), ' | ', $afectacionIGVoIVAP, ' | ', $tipoOperacion)"/>
										</xsl:call-template>
									</xsl:if>	
								</xsl:if>	
							</xsl:if>
							
							<xsl:if test="not($tipoOperacion = '0113')"> 
								<xsl:if test="((($codigoTributoLinea = 1000) and ($montoBase > 0)) or 
										(($codigoTributoLinea = 9996) and ($montoBase > 0) and 
										(($afectacionIGVoIVAP='11') or ($afectacionIGVoIVAP='12') or ($afectacionIGVoIVAP='13') or
										($afectacionIGVoIVAP='14') or ($afectacionIGVoIVAP='15') or ($afectacionIGVoIVAP='16'))))">						
									<xsl:if test="not(number($tasaIGVoIVAP) = number($porcentajeIGVVigenteLinea))">	
										<xsl:call-template name="rejectCall">
											<xsl:with-param name="errorCode" select="'3462'" />
											<xsl:with-param name="errorMessage"
												select="concat('Error en la linea: ', position(), ' | ', $codigoTributoLinea, ' | ', $tasaIGVoIVAP, ' | ', $porcentajeIGVVigenteLinea)"/>
										</xsl:call-template>																																																						 -->
									</xsl:if>
								</xsl:if>							
							</xsl:if>
							<xsl:if test="(($codigoTributoLinea = 1000) and (number($tasaIGVoIVAP) = 10))">						
								<xsl:if test="not(number($tasaIGVoIVAP) = number($porcentajeIGVVigente))">	
									<xsl:call-template name="addWarning">
										<xsl:with-param name="warningCode" select="'4439'" />
										<xsl:with-param name="warningMessage"
											select="concat('Error en la linea: ', position(), ' | ', $codigoTributoLinea, ' | ', $tasaIGVoIVAP, ' | ', $porcentajeIGVVigente)"/>
									</xsl:call-template>																																							
								</xsl:if>	
							</xsl:if>							

							<xsl:if
								test="(($codigoTributoLinea = 9995) or ($codigoTributoLinea = 9997) or ($codigoTributoLinea = 9998)) and not($montoIGV_IVAPLinea=0)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3110'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea a) ', position(), ' ', $codigoTributoLinea, ' ', $montoIGV_IVAPLinea)" />
								</xsl:call-template>
							</xsl:if>

							<xsl:if
								test="($codigoTributoLinea = 7152)">
								<xsl:if test="(($tipoOperacion='0113') and ($montoIGV_IVAPLinea &gt; 0))">						
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'3240'" />
										<xsl:with-param name="errorMessage"
											select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $montoIGV_IVAPLinea)"/>
									</xsl:call-template>										
								</xsl:if>	
							</xsl:if>
							
							<!-- 31 / 32. Código de tributo por línea -->
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'2037'" />
								<xsl:with-param name="node" select="$codigoTributoLinea" />
								<xsl:with-param name="descripcion" select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea)" />
							</xsl:call-template>

							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'2036'" />
								<xsl:with-param name="node" select="$codigoTributoLinea" />
								<xsl:with-param name="regexp" select="'^(1000|1016|2000|7152|9995|9996|9997|9998|9999)$'" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea)" />
							</xsl:call-template>
							
							<!-- 33. Tasa del IGV o  Tasa del IVAP -->
							<xsl:if test="not($codigoTributoLinea='7152')">
								<xsl:call-template name="existValidateElementNotExist">
									<xsl:with-param name="errorCodeNotExist" select="'2992'"/>
									<xsl:with-param name="node" select="$tasaIGVoIVAP" />
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', position(), ' ', $tasaIGVoIVAP)"/>
								</xsl:call-template>				
							</xsl:if>							

							<!-- 32. Monto del tributo de la línea -->
							<xsl:if 
								test="($codigoTributoLinea='2000') and ($montoBase &gt; 0)">
								<xsl:variable name="montoTributo" select="($montoBase * $tasaIGVoIVAP) * 0.01" />
								<xsl:variable name="dif_MontoIGV_IVAPLinea" select="$montoIGV_IVAPLinea - $montoTributo" />
								<xsl:if test="($dif_MontoIGV_IVAPLinea &lt; -1) or ($dif_MontoIGV_IVAPLinea &gt; 1)">
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'3108'" />
										<xsl:with-param name="errorMessage" select="concat('Error en la linea: ', position(), ' ', $dif_MontoIGV_IVAPLinea, ' ', $montoIGV_IVAPLinea, ' ', $montoTributo, ' ', $montoBase,' ', $tasaIGVoIVAP)" />
									</xsl:call-template>
								</xsl:if>
							</xsl:if>

							<xsl:if
								test="($codigoTributoLinea='9999') and ($montoBase &gt; 0)">
								<xsl:variable name="montoTributo"  select="($montoBase * $tasaIGVoIVAP) * 0.01" />
								<xsl:variable name="dif_MontoIGV_IVAPLinea"  select="$montoIGV_IVAPLinea - $montoTributo" />
								<xsl:if
									test="($dif_MontoIGV_IVAPLinea &lt; -1) or ($dif_MontoIGV_IVAPLinea &gt; 1)">
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'3109'" />
										<xsl:with-param name="errorMessage"
											select="concat('Error en la linea: ', position(), ' ', $dif_MontoIGV_IVAPLinea, ' ', $montoIGV_IVAPLinea, ' ', $montoTributo, ' ', $montoBase,' ', $tasaIGVoIVAP)" />
									</xsl:call-template>
								</xsl:if>
							</xsl:if>

							<!-- 31 -->
							<xsl:if
								test="($codigoTributoLinea='9996') and ($montoBase>0) and (($afectacionIGVoIVAP='11') or ($afectacionIGVoIVAP='12') or ($afectacionIGVoIVAP='13')
									 or ($afectacionIGVoIVAP='14') or ($afectacionIGVoIVAP='15') or ($afectacionIGVoIVAP='16') or ($afectacionIGVoIVAP='17')) and ($tasaIGVoIVAP=0)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'2993'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ', $montoBase, ' ', $tasaIGVoIVAP)" />
								</xsl:call-template>
							</xsl:if>

							<xsl:if
								test="(($codigoTributoLinea='1000') or ($codigoTributoLinea='1016')) and ($montoBase &gt; 0) and ($tasaIGVoIVAP=0)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'2993'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ', $montoBase, ' ', $tasaIGVoIVAP)" />
								</xsl:call-template>
							</xsl:if>

							<!-- 32. Tasa del tributo -->
							<xsl:if
								test="($codigoTributoLinea='2000') and ($montoBase &gt; 0) and ($tasaIGVoIVAP=0)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3104'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ', $montoBase, ' ', $tasaIGVoIVAP)" />
								</xsl:call-template>
							</xsl:if>

							<!-- 31. Afectación al IGV o IVAP cuando corresponda -->
							<xsl:if
								test="not(($codigoTributoLinea='2000') or ($codigoTributoLinea='9999')) and ($montoBase &gt; 0) ">
								<xsl:call-template
									name="existValidateElementNotExist">
									<xsl:with-param name="errorCodeNotExist"
										select="'2371'" />
									<xsl:with-param name="node"
										select="$afectacionIGVoIVAP" />
									<xsl:with-param name="descripcion"
										select="concat('Error en la linea: ', position(), ' ', $afectacionIGVoIVAP)" />
								</xsl:call-template>
							</xsl:if>

							<xsl:if
								test="($codigoTributoLinea='2000') or ($codigoTributoLinea='9999')">
								<xsl:call-template
									name="existValidateElementExist">
									<xsl:with-param name="errorCodeNotExist"
										select="'3050'" />
									<xsl:with-param name="node"
										select="$afectacionIGVoIVAP" />
									<xsl:with-param name="descripcion"
										select="concat('Error en la linea: ', position(), ' ', $afectacionIGVoIVAP)" />
								</xsl:call-template>
							</xsl:if>

							<xsl:if test="not(($codigoTributoLinea='2000') or ($codigoTributoLinea='9999')) and ($montoBase &gt; 0) ">
								<xsl:call-template name="regexpValidateElementIfExist">
									<xsl:with-param name="errorCodeValidate" select="'2040'" />
									<xsl:with-param name="node" select="$afectacionIGVoIVAP" />
									<xsl:with-param name="regexp"
										select="'^(10|11|12|13|14|15|16|17|20|21|30|31|32|33|34|35|36|37|40)$'" />
									<xsl:with-param name="descripcion"
										select="concat('Error en la linea: ', position(), ' ', $afectacionIGVoIVAP, ' ',$codigoTributoLinea, ' ',$montoBase)" />
								</xsl:call-template>
								
								<xsl:if test="($codigoTributoLinea = '1000')">	
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'2040'"/>
										<xsl:with-param name="node" select="$afectacionIGVoIVAP"/>
										<xsl:with-param name="regexp" select="'^(10)$'" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', position(), ' ', $afectacionIGVoIVAP, ' ',$codigoTributoLinea, ' ',$montoBase)"/>
									</xsl:call-template>
								</xsl:if>		
								
								<xsl:if test="($codigoTributoLinea = '9996')">	
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'2040'"/>
										<xsl:with-param name="node" select="$afectacionIGVoIVAP"/>
										<xsl:with-param name="regexp" select="'^(11|12|13|14|15|16|21|31|32|33|34|35|36|37|40)$'" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', position(), ' ', $afectacionIGVoIVAP, ' ',$codigoTributoLinea, ' ',$montoBase)"/>
									</xsl:call-template>
								</xsl:if>	

								<xsl:if test="($codigoTributoLinea = '1016')">	
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'2040'"/>
										<xsl:with-param name="node" select="$afectacionIGVoIVAP"/>
										<xsl:with-param name="regexp" select="'^(17)$'" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', position(), ' ', $afectacionIGVoIVAP, ' ',$codigoTributoLinea, ' ',$montoBase)"/>
									</xsl:call-template>
								</xsl:if>										

								<xsl:if test="($codigoTributoLinea = '9997')">	
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'2040'"/>
										<xsl:with-param name="node" select="$afectacionIGVoIVAP"/>
										<xsl:with-param name="regexp" select="'^(20)$'" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', position(), ' ', $afectacionIGVoIVAP, ' ',$codigoTributoLinea, ' ',$montoBase)"/>
									</xsl:call-template>
								</xsl:if>		

								<xsl:if test="($codigoTributoLinea = '9998')">	
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'2040'"/>
										<xsl:with-param name="node" select="$afectacionIGVoIVAP"/>
										<xsl:with-param name="regexp" select="'^(30)$'" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', position(), ' ', $afectacionIGVoIVAP, ' ',$codigoTributoLinea, ' ',$montoBase)"/>
									</xsl:call-template>
								</xsl:if>		

								<xsl:if test="($codigoTributoLinea = '9995')">	
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'2040'"/>
										<xsl:with-param name="node" select="$afectacionIGVoIVAP"/>
										<xsl:with-param name="regexp" select="'^(40)$'" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', position(), ' ', $afectacionIGVoIVAP, ' ',$codigoTributoLinea, ' ',$montoBase)"/>
									</xsl:call-template>
								</xsl:if>									
								
								
							</xsl:if>

							<!-- 32.Tipo de sistema de ISC -->
							<xsl:if test="($codigoTributoLinea='2000') and ($montoBase &gt; 0) ">
								<xsl:call-template name="existValidateElementNotExist">
									<xsl:with-param name="errorCodeNotExist" select="'2373'" />
									<xsl:with-param name="node" select="$tipoSistemaISC" />
									<xsl:with-param name="descripcion"
										select="concat('Error en la linea: ', position(), ' ', $tipoSistemaISC)" />
								</xsl:call-template>

								<xsl:call-template name="regexpValidateElementIfExist">
									<xsl:with-param name="errorCodeValidate" select="'2041'" />
									<xsl:with-param name="node" select="$tipoSistemaISC" />
									<xsl:with-param name="regexp" select="'^(01|02|03)$'" />
									<xsl:with-param name="descripcion"
										select="concat('Error en la linea: ', position(), ' ', $tipoSistemaISC)" />
								</xsl:call-template>
							</xsl:if>

							<xsl:if test="not($codigoTributoLinea = '2000')">
								<xsl:call-template name="existValidateElementExist">
									<xsl:with-param name="errorCodeNotExist" select="'3210'" />
									<xsl:with-param name="node" select="$tipoSistemaISC" />
									<xsl:with-param name="descripcion"
										select="concat('Error en la linea: ', position(), ' ', $tipoSistemaISC)" />
								</xsl:call-template>
							</xsl:if>
							
							<!-- 38. Impuesto al consumo de bolsas de plástico por ítem -->									
							<xsl:if test="($codigoTributoLinea='7152')">						
								<xsl:call-template name="existAndRegexpValidateElement">
									<xsl:with-param name="errorCodeNotExist" select="'3237'" />
									<xsl:with-param name="errorCodeValidate" select="'2892'"/>
										<xsl:with-param name="node" select="$cantidadBolsaPlasticoLinea"/>
									<xsl:with-param name="regexp" select="'^[0-9]{1,5}?$'"/>
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', position(), ' ', $cantidadBolsaPlasticoLinea)"/>
								</xsl:call-template>					
								
								<xsl:if test="(($cantidadBolsaPlasticoLinea) and ($cantidadBolsaPlasticoLinea &gt; 0) and not(number($cantidadBolsaPlasticoLinea) = number($cantidadUnidadesLinea)))">										
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'3236'" />
										<xsl:with-param name="errorMessage"
											select="concat('Error en la linea: ', position(), ' ', number($cantidadBolsaPlasticoLinea), ' ', number($cantidadUnidadesLinea))"/>
									</xsl:call-template>
								</xsl:if>						
		
								<xsl:if test="not($cantidadBolsaPlasticoLinea_unitCode = 'NIU')">										
									<xsl:call-template name="addWarning">
										<xsl:with-param name="warningCode" select="'4320'" />
										<xsl:with-param name="warningMessage"
											select="concat('Error en la linea: ', position(), ' ', $cantidadBolsaPlasticoLinea_unitCode)" />
									</xsl:call-template>
								</xsl:if>						
							
								<xsl:call-template name="existAndRegexpValidateElement">
									<xsl:with-param name="errorCodeNotExist" select="'2892'" />
									<xsl:with-param name="errorCodeValidate" select="'2892'"/>
									<xsl:with-param name="node" select="$montoUnitarioLinea"/>
									<xsl:with-param name="regexp" select="'^[0-9]{1,3}(\.[0-9]{1,5})?$'"/>
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', position(), ' ', $montoUnitarioLinea)"/>
								</xsl:call-template>		
								
								<xsl:if test="($cantidadBolsaPlasticoLinea &gt; 0)">
									<xsl:variable name="calcularImpuestosBolsas" select="$cantidadBolsaPlasticoLinea * $montoUnitarioLinea" />
									<xsl:variable name="dif_MontoUnitarioLinea" select="$montoIGV_IVAPLinea - $calcularImpuestosBolsas" />
										<xsl:if test="($dif_MontoUnitarioLinea &lt; -0.01) or ($dif_MontoUnitarioLinea &gt; 0.01)">									
										<xsl:call-template name="addWarning"> 
											<xsl:with-param name="warningCode" select="'4318'" />
											<xsl:with-param name="warningMessage"
												select="concat('Error en la linea: ', position(), ' ', $dif_MontoUnitarioLinea, ' ', $cantidadBolsaPlasticoLinea,' ', $montoUnitarioLinea,' ', $calcularImpuestosBolsas,' ', $montoIGV_IVAPLinea)"/>
										</xsl:call-template>
									</xsl:if>
								</xsl:if>									
								
								<xsl:if test="(($cantidadBolsaPlasticoLinea &gt; 0) and ($montoUnitarioLinea = 0))">										
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'3238'" />
										<xsl:with-param name="errorMessage"
											select="concat('Error en la linea: ', position(), ' ', $cantidadBolsaPlasticoLinea, ' ', $montoUnitarioLinea)"/>
									</xsl:call-template>
								</xsl:if>						
				
								<xsl:if test="(($cantidadBolsaPlasticoLinea &gt; 0) and ($tipoMoneda = 'PEN') and not(number($montoUnitarioLinea) = number($montoBolsaPlasticoLinea)))">										
									<xsl:call-template name="addWarning"> 
										<xsl:with-param name="warningCode" select="'4237'" />
										<xsl:with-param name="warningMessage"
											select="concat('Error en la linea: ', position(), ' ', $cantidadBolsaPlasticoLinea,' ', $tipoMoneda, ' ', number($montoUnitarioLinea), ' ', number($montoBolsaPlasticoLinea))"/>
									</xsl:call-template>
								</xsl:if>																										
							</xsl:if>	

							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4255'" />
								<xsl:with-param name="node" select="$codigoTributoLinea_schemeName" />
								<xsl:with-param name="regexp" select="'^(Codigo de tributos)$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: 6) ', $codigoTributoLinea_schemeName)" />
							</xsl:call-template>

							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4256'" />
								<xsl:with-param name="node" select="$codigoTributoLinea_schemeAgencyName" />
								<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: 7) ', $codigoTributoLinea_schemeAgencyName)" />
							</xsl:call-template>

							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4257'" />
								<xsl:with-param name="node" select="$codigoTributoLinea_schemeURI" />
								<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo05)$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: 4) ', $codigoTributoLinea_schemeURI)" />
							</xsl:call-template>

							<!-- 31. Nombre de tributo por línea -->
							<!-- 32. Nombre de tributo -->
							<xsl:if test="$codigoTributoLinea!='2000'">
								<xsl:call-template
									name="existValidateElementNotExist">
									<xsl:with-param name="errorCodeNotExist"
										select="'2996'" />
									<xsl:with-param name="node"
										select="$nombreTributoLinea" />
									<xsl:with-param name="descripcion"
										select="concat('Error en la linea: ', position(), ' ', $nombreTributoLinea)" />
								</xsl:call-template>
							</xsl:if>

							<xsl:if
								test="($codigoTributoLinea='1000' and not($nombreTributoLinea='IGV')) or ($codigoTributoLinea='1016' and not($nombreTributoLinea='IVAP'))
									or ($codigoTributoLinea='2000' and not($nombreTributoLinea='ISC')) or ($codigoTributoLinea='9995' and not($nombreTributoLinea='EXP'))
									or ($codigoTributoLinea='9996' and not($nombreTributoLinea='GRA')) or ($codigoTributoLinea='9997' and not($nombreTributoLinea='EXO'))
									or ($codigoTributoLinea='9998' and not($nombreTributoLinea='INA')) or ($codigoTributoLinea='9999' and not($nombreTributoLinea='OTROS'))
									or ($codigoTributoLinea='7152' and not($nombreTributoLinea='ICBPER'))">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3051'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ', $nombreTributoLinea)" />
								</xsl:call-template>
							</xsl:if>

							<!-- 31. Código internacional tributo por línea -->
							<!-- 32. Código internacional de tributo -->
							<xsl:if
								test="($codigoTributoLinea='1000' and not($codigoInternacionalTributoLinea='VAT')) or ($codigoTributoLinea='1016' and not($codigoInternacionalTributoLinea='VAT'))
									or ($codigoTributoLinea='2000' and not($codigoInternacionalTributoLinea='EXC')) or ($codigoTributoLinea='9995' and not($codigoInternacionalTributoLinea='FRE'))
									or ($codigoTributoLinea='9996' and not($codigoInternacionalTributoLinea='FRE')) or ($codigoTributoLinea='9997' and not($codigoInternacionalTributoLinea='VAT'))
									or ($codigoTributoLinea='9998' and not($codigoInternacionalTributoLinea='FRE')) or ($codigoTributoLinea='9999' and not($codigoInternacionalTributoLinea='OTH'))
									or ($codigoTributoLinea='7152' and not($codigoInternacionalTributoLinea='OTH'))">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'2377'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ', $codigoInternacionalTributoLinea)" />
								</xsl:call-template>
							</xsl:if>
						</xsl:for-each>
					</xsl:for-each>
				</xsl:for-each>

				<xsl:variable name="tributoISCxLinea">
			      <xsl:choose>
			        <xsl:when test="./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID = '2000']/cbc:TaxAmount">
			          <xsl:value-of select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID = '2000']/cbc:TaxAmount)"/>
			        </xsl:when>
			        <xsl:otherwise>
			          <xsl:value-of select="'0'"/>
			        </xsl:otherwise>
			      </xsl:choose>
			    </xsl:variable>
			    
				<xsl:variable name="montoBaseISCxLinea">
			      <xsl:choose>
			        <xsl:when test="./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID = '2000']/cbc:TaxableAmount">
			          <xsl:value-of select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID = '2000']/cbc:TaxableAmount)"/>
			        </xsl:when>
			        <xsl:otherwise>
			          <xsl:value-of select="'0'"/>
			        </xsl:otherwise>
			      </xsl:choose>
			    </xsl:variable>		
			    	    			    
				<xsl:variable name="montoBase1000">
			      <xsl:choose>
			        <xsl:when test="./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID = '1000']/cbc:TaxableAmount">
			          <xsl:value-of select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID = '1000']/cbc:TaxableAmount)"/>
			        </xsl:when>
			        <xsl:otherwise>
			          <xsl:value-of select="'0'"/>
			        </xsl:otherwise>
			      </xsl:choose>
			    </xsl:variable>				

				<xsl:variable name="montoBase1016">
			      <xsl:choose>
			        <xsl:when test="./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID = '1016']/cbc:TaxableAmount">
			          <xsl:value-of select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID = '1016']/cbc:TaxableAmount)"/>
			        </xsl:when>
			        <xsl:otherwise>
			          <xsl:value-of select="'0'"/>
			        </xsl:otherwise>
			      </xsl:choose>			    
			    </xsl:variable>		
			    		    			
				<xsl:variable name="montoBase9996">
			      <xsl:choose>
			        <xsl:when test="./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID = '9996']/cbc:TaxableAmount">
			          	<xsl:variable name="afectacion9996" select="./cac:TaxSubtotal/cac:TaxCategory[cac:TaxScheme/cbc:ID = '9996']/cbc:TaxExemptionReasonCode"/>    
			          	<xsl:if test="($afectacion9996 ='11') or ($afectacion9996 ='12') or ($afectacion9996 ='13') or 
			          			($afectacion9996 ='14') or ($afectacion9996 ='15') or ($afectacion9996 ='16') or ($afectacion9996 ='17')">
			          		<xsl:value-of select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID = '9996']/cbc:TaxableAmount)"/>
			        	</xsl:if>
			          	<xsl:if test="not(($afectacion9996 ='11') or ($afectacion9996 ='12') or ($afectacion9996 ='13') or 
			          			($afectacion9996 ='14') or ($afectacion9996 ='15') or ($afectacion9996 ='16') or ($afectacion9996 ='17'))">
			          		<xsl:value-of select="'0'"/>
			        	</xsl:if>					        	
			        </xsl:when>
			        <xsl:otherwise>
			          <xsl:value-of select="'0'"/>
			        </xsl:otherwise>
			      </xsl:choose>
			    </xsl:variable>				    
			    <xsl:variable name="montoBaseIGVxLinea" select="$montoBase1000 + $montoBase1016 + $montoBase9996"/>    	    	    
			    	    
			    <xsl:if test="$montoBaseISCxLinea &gt; 0">			    
				    <xsl:variable name="baseISCxLineaCalculado" select="$valorVentaLinea + $tributoISCxLinea"/>
					<xsl:variable name="dif_BaseIGVIVAPxLinea" select="$baseISCxLineaCalculado - $montoBaseIGVxLinea" />
					<xsl:if test="($dif_BaseIGVIVAPxLinea &lt; -1) or ($dif_BaseIGVIVAPxLinea &gt; 1)">
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="'4294'" />
							<xsl:with-param name="warningMessage"
								select="concat('Error en la linea  1) ', position(), ' ', $tributoISCxLinea, '-', $valorVentaLinea, '-', $montoBaseIGVxLinea, '-', $baseISCxLineaCalculado, '-', $dif_BaseIGVIVAPxLinea)"/>
						</xsl:call-template>
					</xsl:if>					
			    </xsl:if> 
			    
			    <xsl:if test="$montoBaseISCxLinea = 0">		
			    	<xsl:if test="(($countTributoLinea_1000 &gt; 0) or ($countTributoLinea_1016 &gt; 0))">	
						<xsl:variable name="sumBaseIGV_IVAPLinea" select="$montoBase1000 + $montoBase1016"/>
						<xsl:variable name="dif_BaseISCLinea" select="$sumBaseIGV_IVAPLinea - $valorVentaLinea" />
						<xsl:if test="($dif_BaseISCLinea &lt; -0.001) or ($dif_BaseISCLinea &gt; 0.001)">
							<xsl:call-template name="addWarning">
								<xsl:with-param name="warningCode" select="'4294'" />
								<xsl:with-param name="warningMessage"
									select="concat('Error en la linea 2) ', position(), ' ', $dif_BaseISCLinea, ' ', $sumBaseIGV_IVAPLinea, ' ', $valorVentaLinea)"/>
							</xsl:call-template>
						</xsl:if>	
					</xsl:if>		    
			    </xsl:if> 	
			    																
			</xsl:for-each>

			<!-- 33. Valor de venta por ítem -->
			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'2370'" />
				<xsl:with-param name="node" select="$valorVentaLinea" />
				<xsl:with-param name="regexp"
					select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', position(), ' ', $valorVentaLinea)" />
			</xsl:call-template>

			<!-- <xsl:variable name="ValorVentaxItem" select="cbc:LineExtensionAmount"/> -->
			<xsl:variable name="ValorVentaUnitarioxItem"
				select="./cac:Price/cbc:PriceAmount" />
			<xsl:variable name="ImpuestosItem"
				select="./cac:TaxTotal/cbc:TaxAmount" />
			<xsl:variable name="DsctosNoAfectanBI"
				select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode = '01']/cbc:Amount)" />
			<xsl:variable name="DsctosAfectanBI"
				select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode = '00']/cbc:Amount)" />
			<xsl:variable name="CargosNoAfectanBI"
				select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode = '48']/cbc:Amount)" />
			<xsl:variable name="CargosAfectanBI"
				select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode = '47']/cbc:Amount)" />
			<xsl:variable name="PrecioUnitarioxItem"
				select="sum(./cac:PricingReference/cac:AlternativeConditionPrice[cbc:PriceTypeCode = '01']/cbc:PriceAmount)" />
			<xsl:variable name="PrecioReferencialUnitarioxItem"
				select="sum(./cac:PricingReference/cac:AlternativeConditionPrice[cbc:PriceTypeCode = '02']/cbc:PriceAmount)" />
			<xsl:variable name="PrecioUnitarioCalculado"
				select="($valorVentaLinea + $ImpuestosItem - $DsctosNoAfectanBI + $CargosNoAfectanBI) div ( $cantidadUnidadesLinea)" />
			<xsl:variable name="ValorVentaReferencialxItemCalculado"
				select="($PrecioReferencialUnitarioxItem * $cantidadUnidadesLinea) - $DsctosAfectanBI + $CargosAfectanBI" />
			<xsl:variable name="ValorVentaxItemCalculado"
				select="($ValorVentaUnitarioxItem * $cantidadUnidadesLinea) - $DsctosAfectanBI + $CargosAfectanBI" />

			<xsl:if
				test="($valorVentaLinea_currencyID) and not($tipoMoneda=$valorVentaLinea_currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: 6) ', position(), ' ', $tipoMoneda, ' ',$valorVentaLinea_currencyID)" />
				</xsl:call-template>
			</xsl:if>

			<!-- 34. Cargo/descuento por ítem -->
			<xsl:for-each select="./cac:AllowanceCharge">
				<!-- 34. Indicador de cargo/descuento -->
				<xsl:variable name="indicadorCargoDescuento"
					select="./cbc:ChargeIndicator" />
				<!-- 34. Código de cargo/descuento -->
				<xsl:variable name="codigoCargoDescuento"
					select="./cbc:AllowanceChargeReasonCode" />
				<xsl:variable
					name="codigoCargoDescuento_listAgencyName"
					select="./cbc:AllowanceChargeReasonCode/@listAgencyName" />
				<xsl:variable name="codigoCargoDescuento_listName"
					select="./cbc:AllowanceChargeReasonCode/@listName" />
				<xsl:variable name="codigoCargoDescuento_listURI"
					select="./cbc:AllowanceChargeReasonCode/@listURI" />
				<!-- 34. Factor de cargo/descuento -->
				<xsl:variable name="factorCargoDescuento"
					select="./cbc:MultiplierFactorNumeric" />
				<!-- 34. Monto de cargo/descuento -->
				<xsl:variable name="montoCargoDescuento"
					select="./cbc:Amount" />
				<xsl:variable name="montoCargoDescuento_currencyID"
					select="./cbc:Amount/@currencyID" />
				<!-- 34. Monto base del cargo/descuento -->
				<xsl:variable name="montoBaseCargoDescuento"
					select="./cbc:BaseAmount" />
				<xsl:variable name="montoBaseCargoDescuento_currencyID"
					select="./cbc:BaseAmount/@currencyID" />

				<!-- 34. Indicador de cargo/descuento -->
				<xsl:if
					test="$codigoCargoDescuento = '47' or $codigoCargoDescuento = '48'">
					<xsl:call-template
						name="regexpValidateElementIfExistTrue">
						<xsl:with-param name="errorCodeValidate"
							select="'3114'" />
						<xsl:with-param name="node"
							select="$indicadorCargoDescuento" />
						<xsl:with-param name="regexp" select="'^(false)$'" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 1) ', position(), ' ', $indicadorCargoDescuento, ' ',$codigoCargoDescuento)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if
					test="$codigoCargoDescuento = '00' or $codigoCargoDescuento = '01'">
					<xsl:call-template
						name="regexpValidateElementIfExistTrue">
						<xsl:with-param name="errorCodeValidate"
							select="'3114'" />
						<xsl:with-param name="node"
							select="$indicadorCargoDescuento" />
						<xsl:with-param name="regexp" select="'^(true)$'" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 2) ', position(), ' ', $indicadorCargoDescuento, ' ',$codigoCargoDescuento)" />
					</xsl:call-template>
				</xsl:if>

				<!-- 34. Código de cargo/descuento -->
				<xsl:call-template
					name="existValidateElementNotExist">
					<xsl:with-param name="errorCodeNotExist"
						select="'3073'" />
					<xsl:with-param name="node"
						select="$codigoCargoDescuento" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento)" />
				</xsl:call-template>

				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2954'" />
					<xsl:with-param name="node" select="$codigoCargoDescuento" />
					<xsl:with-param name="regexp" select="'^(00|01|02|03|04|05|06|07|20|45|46|47|48|49|50|51|52|53|54|61|62|63)$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento)" />
				</xsl:call-template>

				<xsl:call-template
					name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate"
						select="'4268'" />
					<xsl:with-param name="node"
						select="$codigoCargoDescuento" />
					<xsl:with-param name="regexp"
						select="'^(00|01|47|48)$'" />
					<xsl:with-param name="isError" select="false()" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento)" />
				</xsl:call-template>

				<xsl:call-template
					name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate"
						select="'4251'" />
					<xsl:with-param name="node"
						select="$codigoCargoDescuento_listAgencyName" />
					<xsl:with-param name="regexp"
						select="'^(PE:SUNAT)$'" />
					<xsl:with-param name="isError" select="false()" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: 12) ', position(), ' ', $codigoCargoDescuento_listAgencyName)" />
				</xsl:call-template>

				<xsl:call-template
					name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate"
						select="'4252'" />
					<xsl:with-param name="node"
						select="$codigoCargoDescuento_listName" />
					<xsl:with-param name="regexp"
						select="'^(Cargo/descuento)$'" />
					<xsl:with-param name="isError" select="false()" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: 12) ', position(), ' ', $codigoCargoDescuento_listName)" />
				</xsl:call-template>

				<xsl:call-template
					name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate"
						select="'4253'" />
					<xsl:with-param name="node"
						select="$codigoCargoDescuento_listURI" />
					<xsl:with-param name="regexp"
						select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo53)$'" />
					<xsl:with-param name="isError" select="false()" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: 7) ', position(), ' ', $codigoCargoDescuento_listURI)" />
				</xsl:call-template>

				<!-- 34. Factor de cargo/descuento -->
				<xsl:call-template
					name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate"
						select="'3052'" />
					<xsl:with-param name="node"
						select="$factorCargoDescuento" />
					<xsl:with-param name="regexp"
						select="'^[0-9]{1,3}(\.[0-9]{1,5})?$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $factorCargoDescuento)" />
				</xsl:call-template>

				<!-- 34. Monto de cargo/descuento -->
				<xsl:call-template
					name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate"
						select="'2955'" />
					<xsl:with-param name="node"
						select="$montoCargoDescuento" />
					<xsl:with-param name="regexp"
						select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $montoCargoDescuento)" />
				</xsl:call-template>

				<xsl:if test="($codigoCargoDescuento)">
					<xsl:variable name="calculoMontoCargoDescuento"
						select="($montoBaseCargoDescuento * $factorCargoDescuento)" />
					<xsl:variable name="dif_MontoCargoDescuento"
						select="$montoCargoDescuento - $calculoMontoCargoDescuento" />
					<xsl:if
						test="($dif_MontoCargoDescuento &lt; -1) or ($dif_MontoCargoDescuento &gt; 1)">
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="'4289'" />
							<xsl:with-param name="warningMessage"
								select="concat('Error en la linea: ', position(), ' ', $dif_MontoCargoDescuento, ' ', $montoCargoDescuento, ' ', $montoBaseCargoDescuento)" />
						</xsl:call-template>
					</xsl:if>
				</xsl:if>

				<xsl:if
					test="($montoCargoDescuento_currencyID) and not($tipoMoneda=$montoCargoDescuento_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 7) ', position(), ' ', $tipoMoneda, ' ',$montoCargoDescuento_currencyID)" />
					</xsl:call-template>
				</xsl:if>

				<!-- 34. Monto base del cargo/descuento -->
				<xsl:call-template
					name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate"
						select="'3053'" />
					<xsl:with-param name="node"
						select="$montoBaseCargoDescuento" />
					<xsl:with-param name="regexp"
						select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $montoBaseCargoDescuento)" />
				</xsl:call-template>

				<xsl:if
					test="($montoBaseCargoDescuento_currencyID) and not($tipoMoneda=$montoBaseCargoDescuento_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 8) ', position(), ' ', $tipoMoneda, ' ',$montoBaseCargoDescuento_currencyID)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:for-each>

			<xsl:if test="($tipoOperacion='1004')">
				<xsl:variable name="countPuntoOrigen"
					select="count(./cac:Delivery/cac:Despatch/cac:DespatchAddress/cbc:ID)" />
				<!-- 97. Código de ubigeo -->
				<xsl:if test="$countPuntoOrigen = 0 ">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3116'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countPuntoOrigen)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>

			<xsl:for-each select="./cac:Delivery">
				<!-- Detracciones - Servicio de transporte de Carga -->
				<xsl:for-each select="./cac:Despatch">
					<!-- 100. Detalle del viaje -->
					<xsl:variable name="detalleViaje" select="./cbc:Instructions" />

					<!-- 98. Punto de origen -->
					<xsl:for-each select="./cac:DespatchAddress">
						<!-- 98. Código de ubigeo -->
						<xsl:variable name="puntoOrigen" select="./cbc:ID" />
						<xsl:variable name="puntoOrigen_schemeAgencyName" select="./cbc:ID/@schemeAgencyName" />
						<xsl:variable name="puntoOrigen_schemeName" select="./cbc:ID/@schemeName" />
						<!-- 98. Dirección detallada del origen -->
						<xsl:variable name="puntoOrigenDireccionDetallada" select="./cac:AddressLine/cbc:Line" />

						<xsl:if test="($tipoOperacion='1004')">
							<!-- 99. Dirección detallada del origen	 -->
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'3116'"/>
								<xsl:with-param name="node" select="$puntoOrigen"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $puntoOrigen)" />
							</xsl:call-template>		
														
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'3117'"/>
								<xsl:with-param name="node" select="$puntoOrigenDireccionDetallada"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $puntoOrigenDireccionDetallada)" />
							</xsl:call-template>						
						</xsl:if>
						
						<xsl:if test="($codigoBienServicioSujetoDetraccion='027')">
							<!-- 98. Código de ubigeo -->
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4256'" />
								<xsl:with-param name="node" select="$puntoOrigen_schemeAgencyName" />
								<xsl:with-param name="regexp" select="'^(PE:INEI)$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: 8) ', position(), ' ', $puntoOrigen_schemeAgencyName)" />
							</xsl:call-template>

							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4255'" />
								<xsl:with-param name="node" select="$puntoOrigen_schemeName" />
								<xsl:with-param name="regexp" select="'^(Ubigeos)$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: 7) ', position(), ' ', $puntoOrigen_schemeName)" />
							</xsl:call-template>

							<!-- 99. Dirección detallada del origen -->
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4236'" />
								<xsl:with-param name="node" select="$puntoOrigenDireccionDetallada" />
								<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,199}$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: ', $puntoOrigenDireccionDetallada)" />
							</xsl:call-template>
						</xsl:if>
					</xsl:for-each>

					<!-- 100. Detalle del viaje -->
					<xsl:if test="($tipoOperacion='1004')">
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3120'"/>
							<xsl:with-param name="node" select="$detalleViaje"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $detalleViaje)" />
						</xsl:call-template>					
					</xsl:if>	
										
					<xsl:if test="($codigoBienServicioSujetoDetraccion='027')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" 	select="'4270'" />
							<xsl:with-param name="node" select="$detalleViaje" />
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,499}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', $detalleViaje)" />
						</xsl:call-template>
					</xsl:if>
				</xsl:for-each>

				<!-- 99. Punto de destino -->
				<xsl:for-each select="./cac:DeliveryLocation">
					<xsl:for-each select="./cac:Address">
						<!-- 99. Código de ubigeo -->
						<xsl:variable name="puntoDestino" select="./cbc:ID" />
						<xsl:variable name="puntoDestino_schemeAgencyName" select="./cbc:ID/@schemeAgencyName" />
						<xsl:variable name="puntoDestino_schemeName" select="./cbc:ID/@schemeName" />
						<!-- 99. Dirección detallada del destino -->
						<xsl:variable name="puntoDestinoDireccionDetallada" select="./cac:AddressLine/cbc:Line" />

						<xsl:if test="($tipoOperacion='1004')">
							<!-- 96. Código de ubigeo -->
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'3118'"/>
								<xsl:with-param name="node" select="$puntoDestino"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $puntoDestino)" />
							</xsl:call-template>	
							
							<!-- 96. Dirección detallada del origen	 -->
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'3119'"/>
								<xsl:with-param name="node" select="$puntoDestinoDireccionDetallada"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $puntoDestinoDireccionDetallada)" />
							</xsl:call-template>												
						</xsl:if>

						<xsl:if test="($codigoBienServicioSujetoDetraccion='027')">
							<!-- 99. Código de ubigeo -->
							<xsl:call-template
								name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4256'" />
								<xsl:with-param name="node" select="$puntoDestino_schemeAgencyName" />
								<xsl:with-param name="regexp" select="'^(PE:INEI)$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: 9) ', position(), ' ', $puntoDestino_schemeAgencyName)" />
							</xsl:call-template>

							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4255'" />
								<xsl:with-param name="node" select="$puntoDestino_schemeName" />
								<xsl:with-param name="regexp" select="'^(Ubigeos)$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: 8) ', position(), ' ', $puntoDestino_schemeName)" />
							</xsl:call-template>

							<!-- 99. Dirección detallada del origen -->
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4236'" />
								<xsl:with-param name="node" select="$puntoDestinoDireccionDetallada" />
								<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,199}$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: ', position(), ' ', $puntoDestinoDireccionDetallada)" />
							</xsl:call-template>
						</xsl:if>
					</xsl:for-each>
				</xsl:for-each>

				<!-- 101. Valor referencial del servicio de transporte -->
				<!-- 102. Valor referencial sobre la carga efectiva -->
				<!-- 103. Valor referencial sobre la carga útil nominal -->
				<xsl:variable name="countTipoValorReferencial01For" select="count(./cac:DeliveryTerms/cbc:ID[text()='01'])" />
				<xsl:variable name="countTipoValorReferencial02For" select="count(./cac:DeliveryTerms/cbc:ID[text()='02'])" />
				<xsl:variable name="countTipoValorReferencial03For" select="count(./cac:DeliveryTerms/cbc:ID[text()='03'])" />

				<xsl:if test="($tipoOperacion='1004')">
					<xsl:if test="not($countTipoValorReferencial01For = 1)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3124'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countTipoValorReferencial01For)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if test="not($countTipoValorReferencial02For = 1)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3125'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countTipoValorReferencial02For)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:if test="not($countTipoValorReferencial03For = 1)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3126'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countTipoValorReferencial03For)" />
						</xsl:call-template>
					</xsl:if>
				</xsl:if>

				<xsl:for-each select="./cac:DeliveryTerms">
					<!-- 101 / 103. Tipo valor Referencial -->
					<xsl:variable name="tipoValorReferencial" select="./cbc:ID" />
					<!-- 101 / 103. Valor referencial -->
					<xsl:variable name="valorReferencial" 	select="./cbc:Amount" />
					<xsl:variable name="valorReferencial_currency" select="./cbc:Amount/@currencyID" />
					
					<!-- 101 / 103. Valor referencial -->
					<xsl:if test="($tipoOperacion='1004')">		
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3122'" />
							<xsl:with-param name="node" select="$valorReferencial" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $valorReferencial)" />
						</xsl:call-template>

						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'3123'" />
							<xsl:with-param name="node" select="$valorReferencial" />
							<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $valorReferencial)" />
						</xsl:call-template>
					</xsl:if>
						
					<xsl:if test="($codigoBienServicioSujetoDetraccion='027')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'3208'" />
							<xsl:with-param name="node" select="$valorReferencial_currency" />
							<xsl:with-param name="regexp" select="'^(PEN)$'" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 1) ', position(), ' ', $valorReferencial_currency)" />
						</xsl:call-template>
					</xsl:if>
				</xsl:for-each>

				<xsl:for-each select="./cac:Shipment">
					<xsl:for-each select="./cac:Consignment">
						<!-- Detracciones - Servicio de transporte de Carga - Detalle de tramos 
							(De corresponder) -->
						<!-- 106. Descripción del tramo -->
						<xsl:variable name="descripcionTramo"
							select="./cbc:CarrierServiceInstructions" />
						<!-- 112. Valor Preliminar Referencial por Carga Útil Nominal (Tratándose 
							de más de 1 vehículo) -->
						<xsl:variable
							name="valorPreliminarReferencialCargaUtilNominal"
							select="./cbc:DeclaredForCarriageValueAmount" />
						<xsl:variable
							name="valorPreliminarReferencialCargaUtilNominal_currency"
							select="./cbc:DeclaredForCarriageValueAmount/@currencyID" />

						<xsl:for-each
							select="./cac:PlannedPickupTransportEvent">
							<xsl:for-each select="./cac:Location">
								<!-- 104. Punto de origen del viaje -->
								<xsl:variable name="puntoOrigenViaje"
									select="./cbc:ID" />
								<xsl:variable
									name="puntoOrigenViaje_schemeAgencyName"
									select="./cbc:ID/@schemeAgencyName" />
								<xsl:variable name="puntoOrigenViaje_schemeName"
									select="./cbc:ID/@schemeName" />

								<xsl:if test="($codigoBienServicioSujetoDetraccion='027')">
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'4256'" />
										<xsl:with-param name="node" select="$puntoOrigenViaje_schemeAgencyName" />
										<xsl:with-param name="regexp" select="'^(PE:INEI)$'" />
										<xsl:with-param name="isError" select="false()" />
										<xsl:with-param name="descripcion"
											select="concat('Error en la linea: 10) ', position(), ' ', $puntoOrigenViaje_schemeAgencyName)" />
									</xsl:call-template>

									<xsl:call-template
										name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate"
											select="'4255'" />
										<xsl:with-param name="node"
											select="$puntoOrigenViaje_schemeName" />
										<xsl:with-param name="regexp"
											select="'^(Ubigeos)$'" />
										<xsl:with-param name="isError" select="false()" />
										<xsl:with-param name="descripcion"
											select="concat('Error en la linea: 9) ', position(), ' ', $puntoOrigenViaje_schemeName)" />
									</xsl:call-template>
								</xsl:if>
							</xsl:for-each>
						</xsl:for-each>

						<xsl:for-each
							select="./cac:PlannedDeliveryTransportEvent">
							<xsl:for-each select="./cac:Location">
								<!-- 105. Punto de destino del viaje -->
								<xsl:variable name="puntoDestinoViaje"
									select="./cbc:ID" />
								<xsl:variable
									name="puntoDestinoViaje_schemeAgencyName"
									select="./cbc:ID/@schemeAgencyName" />
								<xsl:variable name="puntoDestinoViaje_schemeName"
									select="./cbc:ID/@schemeName" />

								<xsl:if
									test="($codigoBienServicioSujetoDetraccion='027')">
									<xsl:call-template
										name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate"
											select="'4256'" />
										<xsl:with-param name="node"
											select="$puntoDestinoViaje_schemeAgencyName" />
										<xsl:with-param name="regexp"
											select="'^(PE:INEI)$'" />
										<xsl:with-param name="isError" select="false()" />
										<xsl:with-param name="descripcion"
											select="concat('Error en la linea: 11) ', position(), ' ', $puntoDestinoViaje_schemeAgencyName)" />
									</xsl:call-template>

									<xsl:call-template
										name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate"
											select="'4255'" />
										<xsl:with-param name="node"
											select="$puntoDestinoViaje_schemeName" />
										<xsl:with-param name="regexp"
											select="'^(Ubigeos)$'" />
										<xsl:with-param name="isError" select="false()" />
										<xsl:with-param name="descripcion"
											select="concat('Error en la linea: 10) ', position(), ' ', $puntoDestinoViaje_schemeName)" />
									</xsl:call-template>
								</xsl:if>
							</xsl:for-each>
						</xsl:for-each>

						<!-- 106. Descripción del tramo -->
						<xsl:if test="($tipoOperacion='1004')">
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4271'" />
								<xsl:with-param name="node" select="$descripcionTramo" />
								<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,99}$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $descripcionTramo)" />
							</xsl:call-template>
						</xsl:if>

						<xsl:for-each select="./cac:DeliveryTerms">
							<!-- 107. Valor preliminar referencial sobre la carga efectiva (Por el tramo virtual recorrido) -->
							<xsl:variable name="valorPreliminarReferencialCargaEfectiva" select="./cbc:Amount" />
							<xsl:variable name="valorPreliminarReferencialCargaEfectiva_currency" select="./cbc:Amount/@currencyID" />

							<xsl:if test="($tipoOperacion='1004')">
								<xsl:call-template name="regexpValidateElementIfExist">
									<xsl:with-param name="errorCodeValidate" select="'4272'" />
									<xsl:with-param name="node" select="$valorPreliminarReferencialCargaEfectiva" />
									<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
									<xsl:with-param name="isError" select="false()" />
									<xsl:with-param name="descripcion"
										select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $valorPreliminarReferencialCargaEfectiva)" />
								</xsl:call-template>
							</xsl:if>
								
							<xsl:if test="($codigoBienServicioSujetoDetraccion='027')">
								<xsl:call-template name="regexpValidateElementIfExist">
									<xsl:with-param name="errorCodeValidate" select="'3208'" />
									<xsl:with-param name="node" select="$valorPreliminarReferencialCargaEfectiva_currency" />
									<xsl:with-param name="regexp" select="'^(PEN)$'" />
									<xsl:with-param name="descripcion"
										select="concat('Error en la linea: 2) ', position(), ' ', $valorPreliminarReferencialCargaEfectiva_currency)" />
								</xsl:call-template>
							</xsl:if>
						</xsl:for-each>

						<!-- Detracciones - Servicio de transporte de Carga - Detalle del (os) 
							Vehículo (s) -->
						<xsl:for-each select="./cac:TransportHandlingUnit">
							<xsl:for-each select="./cac:TransportEquipment">
								<!-- 108. Configuracion vehicular del vehículo -->
								<xsl:variable name="configuracionVehicularVehiculo" select="./cbc:SizeTypeCode" />
								<xsl:variable name="configuracionVehicularVehiculo_listAgencyName" select="./cbc:SizeTypeCode/@listAgencyName" />
								<xsl:variable name="configuracionVehicularVehiculo_listName" select="./cbc:SizeTypeCode/@listName" />

								<xsl:if test="($tipoOperacion='1004')">
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'4273'" />
										<xsl:with-param name="node" select="$configuracionVehicularVehiculo" />
										<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,14}$'" />
										<xsl:with-param name="isError" select="false()" />
										<xsl:with-param name="descripcion"
											select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $configuracionVehicularVehiculo)" />
									</xsl:call-template>
								</xsl:if>

 								<xsl:if test="($codigoBienServicioSujetoDetraccion='027')">
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'4251'" />
										<xsl:with-param name="node" select="$configuracionVehicularVehiculo_listAgencyName" />
										<xsl:with-param name="regexp" select="'^(PE:MTC)$'" />
										<xsl:with-param name="isError" select="false()" />
										<xsl:with-param name="descripcion"
											select="concat('Error en la linea: 3) ', position(), ' ', $configuracionVehicularVehiculo_listAgencyName)" />
									</xsl:call-template>

									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'4252'" />
										<xsl:with-param name="node" select="$configuracionVehicularVehiculo_listName" />
										<xsl:with-param name="regexp" select="'^(Configuracion Vehícular)$'" />
										<xsl:with-param name="isError" select="false()" />
										<xsl:with-param name="descripcion"
											select="concat('Error en la linea: 13) ', position(), ' ', $configuracionVehicularVehiculo_listName)" />
									</xsl:call-template>
								</xsl:if>

								<xsl:for-each select="./cac:Delivery">
									<xsl:for-each select="./cac:DeliveryTerms">
										<!-- 111. Valor Referencial por TM -->
										<xsl:variable name="valorReferencialTM" select="./cbc:Amount" />
										<xsl:variable name="valorReferencialTM_currency" select="./cbc:Amount/@currencyID" />

										<!-- 111. Valor Referencial por TM -->
										<xsl:call-template name="regexpValidateElementIfExist">
											<xsl:with-param name="errorCodeValidate" select="'3208'" />
											<xsl:with-param name="node" select="$valorReferencialTM_currency" />
											<xsl:with-param name="regexp" select="'^(PEN)$'" />
											<xsl:with-param name="descripcion"
												select="concat('Error en la linea: 3) ', position(), ' ', $valorReferencialTM_currency)" />
										</xsl:call-template>
									</xsl:for-each>
								</xsl:for-each>
							</xsl:for-each>

							<xsl:for-each select="./cac:MeasurementDimension">
								<!-- 109. Carga Util en TM del vehículo -->
								<!-- 110. Carga Efectiva en TM del vehículo -->
								<xsl:variable name="tipoCargaCargaUtil" select="./cbc:AttributeID" />
								<!-- 109 / 110. Valor de la carga en TM -->
								<xsl:variable name="valorCargaTM" select="./cbc:Measure" />
								<xsl:variable name="valorCargaTM_unitCode" select="./cbc:Measure/@unitCode" />

								<xsl:if test="($tipoOperacion='1004')">
									<!-- 109 / 110. Tipo de carga -->
									<xsl:if test="not(($tipoCargaCargaUtil = '01') or ($tipoCargaCargaUtil = '02'))">
										<xsl:call-template name="existValidateElementNotExist">
											<xsl:with-param name="errorCodeNotExist" select="'4274'" />
											<xsl:with-param name="node" select="$valorCargaTM" />
											<xsl:with-param name="descripcion"
												select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $tipoCargaCargaUtil)" />
										</xsl:call-template>
									</xsl:if>

									<!-- 109 / 110. Valor de la carga en TM -->
									<xsl:if test="($tipoCargaCargaUtil)">
										<xsl:call-template name="existValidateElementNotExist">
											<xsl:with-param name="errorCodeNotExist" select="'4275'" />
											<xsl:with-param name="node" select="$valorCargaTM" />
											<xsl:with-param name="isError" select="false()"/>
											<xsl:with-param name="descripcion"
												select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $valorCargaTM)" />
										</xsl:call-template>
									</xsl:if>

									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'4276'" />
										<xsl:with-param name="node" select="$valorCargaTM" />
										<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
										<xsl:with-param name="isError" select="false()" />
										<xsl:with-param name="descripcion"
											select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $valorCargaTM)" />
									</xsl:call-template>

									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'4277'" />
										<xsl:with-param name="node" select="$valorCargaTM_unitCode" />
										<xsl:with-param name="regexp" select="'^(TNE)$'" />
										<xsl:with-param name="isError" select="false()" />
										<xsl:with-param name="descripcion"
											select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $valorCargaTM_unitCode)" />
									</xsl:call-template>
								</xsl:if>
							</xsl:for-each>
						</xsl:for-each>

						<!-- 112. Valor Preliminar Referencial por Carga Útil Nominal (Tratándose 
							de más de 1 vehículo) -->
						<xsl:if test="($tipoOperacion='1004')">
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4278'" />
								<xsl:with-param name="node" select="$valorPreliminarReferencialCargaUtilNominal" />
								<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $valorPreliminarReferencialCargaUtilNominal)" />
							</xsl:call-template>
						</xsl:if>
							
						<xsl:if test="($codigoBienServicioSujetoDetraccion='027')">
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'3208'" />
								<xsl:with-param name="node" select="$valorPreliminarReferencialCargaUtilNominal_currency" />
								<xsl:with-param name="regexp" select="'^(PEN)$'" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: 4) ', position(), ' ', $valorPreliminarReferencialCargaUtilNominal_currency)" />
							</xsl:call-template>
						</xsl:if>
					</xsl:for-each>
				</xsl:for-each>
			</xsl:for-each>
		</xsl:for-each>

		<!-- Totales de la Factura -->
		<xsl:variable name="countTaxTotalLine"
			select="count(cac:TaxTotal)" />
		<xsl:if test="($countTaxTotalLine=0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2956'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countTaxTotalLine)" />
			</xsl:call-template>
		</xsl:if>

		<xsl:variable name="countTaxTotal" select="count(cac:TaxTotal)" />
		<xsl:if test="($countTaxTotal &gt; 1)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3024'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countTaxTotal)" />
			</xsl:call-template>
		</xsl:if>

		<xsl:if
			test="(($sumMontoBase1000Linea &gt; 0) and ($countCodigoTributo1000 = 0)) or (($sumMontoBase1016Linea &gt; 0) and ($countCodigoTributo1016 = 0)) or 
 				(($sumMontoBase9995Linea &gt; 0) and ($countCodigoTributo9995 = 0)) or (($sumMontoBase9996Linea &gt; 0) and ($countCodigoTributo9996 = 0)) or 
 				(($sumMontoBase9997Linea &gt; 0) and ($countCodigoTributo9997 = 0)) or (($sumMontoBase9998Linea &gt; 0) and ($countCodigoTributo9998 = 0))"> 
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2638'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countCodigoTributo1000, ' ', $countCodigoTributo1016, ' ', $countCodigoTributo9995, ' ', $countCodigoTributo9996, ' ', $countCodigoTributo9997, ' ', $countCodigoTributo9998)"/>
			</xsl:call-template>
		</xsl:if>

		<xsl:if
			test="($countCodigoTributo1000 &gt; 1) or ($countCodigoTributo1016 &gt; 1) or ($countCodigoTributo2000 &gt; 1) or ($countCodigoTributo9995 &gt; 1) 
				or ($countCodigoTributo9996 &gt; 1) or ($countCodigoTributo9997 &gt; 1) or ($countCodigoTributo9998 &gt; 1) or ($countCodigoTributo9999 &gt; 1)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3068'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countCodigoTributo1000, ' ', $countCodigoTributo2000, ' ', $countCodigoTributo9999)" />
			</xsl:call-template>
		</xsl:if>

		<xsl:for-each select="cac:TaxTotal">
			<!-- 35. Monto total de impuestos -->
			<xsl:variable name="montoTotalImpuestos"
				select="./cbc:TaxAmount" />
			<xsl:variable name="montoTotalImpuestos_currencyID"
				select="./cbc:TaxAmount/@currencyID" />
			<!-- 36. Total Valor de Venta - Exportación -->
			<!-- 37. Total valor de venta - operaciones inafectas -->
			<!-- 38. Total valor de venta - operaciones exoneradas -->
			<!-- 39. Total valor de venta - operaciones gratuitas -->
			<!-- 39. Sumatoria de impuestos de operaciones gratuitas -->
			<!-- 40. Total valor de venta - operaciones gravadas (IGV o IVAP) -->
			<!-- 41. Total Importe IGV o IVAP -->
			<!-- 42. Sumatoria ISC -->
			<!-- 43. Sumatoria Otros Tributos -->

			<!-- 35. Monto total de impuestos -->
			<xsl:call-template name="regexpValidateElementIfExist"> 
				<xsl:with-param name="errorCodeValidate" select="'3020'" />
				<xsl:with-param name="node" select="$montoTotalImpuestos" />
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', position(), ' ', $montoTotalImpuestos)" />
			</xsl:call-template>

			<xsl:if test="($montoTotalImpuestos)">
				<xsl:variable name="montoImporteTributoFor" select="$importeTributo_1000 + $importeTributo_1016 + $importeTributo_2000 + $importeTributo_7152 + $importeTributo_9999" />
				<xsl:variable name="dif_MontoTotalImpuestos" select="$montoTotalImpuestos - $montoImporteTributoFor" />
				<xsl:if test="($dif_MontoTotalImpuestos &lt; -1) or ($dif_MontoTotalImpuestos &gt; 1)">
 					<xsl:call-template name="addWarning"> 
					<xsl:with-param name="warningCode" select="'4301'" /> 
 					<xsl:with-param name="warningMessage" 
 						select="concat('Error en la linea: ', position(), ' ', $dif_MontoTotalImpuestos,' ', $montoTotalImpuestos, ' ', $montoImporteTributoFor)"/> 
 					</xsl:call-template> 
				</xsl:if>
			</xsl:if>

			<xsl:if
				test="($montoTotalImpuestos_currencyID) and not($tipoMoneda=$montoTotalImpuestos_currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: 9) ', position(), ' ', $tipoMoneda, ' ',$montoTotalImpuestos_currencyID)" />
				</xsl:call-template>
			</xsl:if>

			<xsl:for-each select="./cac:TaxSubtotal">
				<!-- 36 / 39. Total Valor de Venta -->
				<!-- 40 / 41. Total valor de venta operaciones gravadas -->
				<!-- 42 / 43. Monto base -->
				<xsl:variable name="totalValorVentaItem" select="./cbc:TaxableAmount" />
				<xsl:variable name="totalValorVentaItem_currencyID" select="./cbc:TaxableAmount/@currencyID" />
				<!-- 36 / 39. Importe del tributo -->
				<!-- 40 / 41. Total Importe de IGV o IVAP, según corresponda -->
				<!-- 42 / 43. Monto de la Sumatoria -->
				<xsl:variable name="importeTributo" select="./cbc:TaxAmount" />
				<xsl:variable name="importeTributo_currencyID" select="./cbc:TaxAmount/@currencyID" />

				<xsl:call-template
					name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate"
						select="'2999'" />
					<xsl:with-param name="node"
						select="$totalValorVentaItem" />
					<xsl:with-param name="regexp"
						select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $totalValorVentaItem)" />
				</xsl:call-template>

				<xsl:if
					test="($totalValorVentaItem_currencyID) and not($tipoMoneda=$totalValorVentaItem_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 10) ', position(), ' ', $tipoMoneda, ' ',$totalValorVentaItem_currencyID)" />
					</xsl:call-template>
				</xsl:if>

				<!-- 36 / 43. -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2048'" />
					<xsl:with-param name="node" select="$importeTributo" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $importeTributo)" />
				</xsl:call-template>

				<xsl:if
					test="($importeTributo_currencyID) and not($tipoMoneda=$importeTributo_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 11) ', position(), ' ', $tipoMoneda, ' ',$importeTributo_currencyID)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:for-each select="./cac:TaxCategory">
					<xsl:for-each select="./cac:TaxScheme">
						<!-- 36 / 43. Código de tributo -->
						<xsl:variable name="codigoTributo" select="./cbc:ID" />
						<xsl:variable name="codigoTributo_schemeName" select="./cbc:ID/@schemeName" />
						<xsl:variable name="codigoTributo_schemeAgencyName" select="./cbc:ID/@schemeAgencyName" />
						<xsl:variable name="codigoTributo_schemeURI" select="./cbc:ID/@schemeURI" />
						<!-- 36 / 43. Nombre de tributo -->
						<xsl:variable name="nombreTributo" select="./cbc:Name" />
						<!-- 36 / 43. Código internacional tributo -->
						<xsl:variable name="codigoInternacionalTributo" select="./cbc:TaxTypeCode" />

						<!-- 36 / 43. Código de tributo -->
						<xsl:call-template
							name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist"
								select="'3059'" />
							<xsl:with-param name="node"
								select="$codigoTributo" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $codigoTributo)" />
						</xsl:call-template>

						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'3007'" />
							<xsl:with-param name="node" select="$codigoTributo" />
							<xsl:with-param name="regexp" select="'^(1000|1016|2000|7152|9995|9996|9997|9998|9999)$'" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $codigoTributo)" />
						</xsl:call-template>
												
						<!-- 39 / 42. -->
						<xsl:if test="not($codigoTributo='7152')">
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'3003'"/>
								<xsl:with-param name="node" select="$totalValorVentaItem" />
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $totalValorVentaItem)"/>
							</xsl:call-template>
						</xsl:if>
						
						<xsl:if test="($codigoTributo='7152')">
							<xsl:variable name="t11" select="xs:date($cbcIssueDate)-xs:date('2019-08-01')" />
							<xsl:variable name="t12" select="fn:days-from-duration(xs:duration($t11))" />			
							<xsl:if
								test="($t12 &lt; 0)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'2949'" />
									<xsl:with-param name="errorMessage" 
										select="concat('Error en la linea: ', $cbcIssueDate, ' - ', $t11, ' - ', $t12)" />
								</xsl:call-template>
							</xsl:if>						
						</xsl:if>						

						<xsl:if test="($codigoTributo='9995') and ($sumMontoBase9995Linea &gt; 0)">
							<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumValorVentaItem9995For" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4295'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumValorVentaItem9995For)" />
								</xsl:call-template>
							</xsl:if>
						</xsl:if>

						<xsl:if test="($codigoTributo='9997') and ($sumMontoBase9997Linea &gt; 0)">
							<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - ($sumValorVentaItem9997For - $sumMontoCargoDescuento05)" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4297'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumValorVentaItem9997For, ' ', $sumMontoCargoDescuento05)"/>
								</xsl:call-template>
							</xsl:if>	
						</xsl:if>

						<xsl:if test="($codigoTributo='9997') and ($countCodigoLeyenda2001 &gt; 0)">						
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'4022'"/>
								<xsl:with-param name="node" select="$totalValorVentaItem"/>
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $totalValorVentaItem)"/>
							</xsl:call-template>
							
							<xsl:if test="(number($totalValorVentaItem) = 0)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4022'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $totalValorVentaItem)"/>
								</xsl:call-template>							
							</xsl:if>						
						</xsl:if>
						
						<xsl:if test="($codigoTributo='9997') and ($countCodigoLeyenda2002 &gt; 0)">							
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'4023'"/>
								<xsl:with-param name="node" select="$totalValorVentaItem"/>
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $totalValorVentaItem)"/>
							</xsl:call-template>
							
							<xsl:if test="(number($totalValorVentaItem) = 0)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4023'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $totalValorVentaItem)"/>
								</xsl:call-template>							
							</xsl:if>
						</xsl:if>
						
						<xsl:if test="($codigoTributo='9997') and ($countCodigoLeyenda2003 &gt; 0)">
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'4024'"/>
								<xsl:with-param name="node" select="$totalValorVentaItem"/>
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $totalValorVentaItem)"/>
							</xsl:call-template>
							
							<xsl:if test="(number($totalValorVentaItem) = 0)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4024'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $totalValorVentaItem)"/>
								</xsl:call-template>							
							</xsl:if>
						</xsl:if>
						
						<xsl:if test="($codigoTributo='9997') and ($countCodigoLeyenda2008 &gt; 0)">
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'4244'"/>
								<xsl:with-param name="node" select="$totalValorVentaItem"/>
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $totalValorVentaItem)"/>
							</xsl:call-template>
							
							<xsl:if test="(number($totalValorVentaItem) = 0)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4244'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $totalValorVentaItem)"/>
								</xsl:call-template>							
							</xsl:if>
						</xsl:if>

						<!-- 39. Total Valor de Venta -->
						<xsl:if test="($codigoTributo='9996') and ($sumMontoBase9996Linea &gt; 0)">
							<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumValorVentaItem9996For" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4298'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumValorVentaItem9996For)"/>
								</xsl:call-template>
							</xsl:if>	
						</xsl:if>

						<xsl:if test="($codigoTributo='9996') and ($countCodigoPrecio02 &gt; 0) and ($totalValorVentaItem=0)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2641'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $countCodigoPrecio02, ' ', $totalValorVentaItem)" />
							</xsl:call-template>
						</xsl:if>

						<xsl:if test="($codigoTributo='9996') and ($countCodigoLeyenda1002 &gt; 0) and ($totalValorVentaItem=0)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2416'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $countCodigoLeyenda1002, ' ', $totalValorVentaItem)" />
							</xsl:call-template>
						</xsl:if>

						<!-- 40. Total Valor de Venta -->
						<xsl:if test="($codigoTributo='9998') and ($sumMontoBase9998Linea &gt; 0)">
							<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - ($sumValorVentaItem9998For - $sumMontoCargoDescuento06)" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4296'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumValorVentaItem9998For, ' - ',$sumMontoCargoDescuento06 )"/>
								</xsl:call-template>
							</xsl:if>		
						</xsl:if>	

						<!-- 42 / 43. Monto base -->
						<xsl:if test="($codigoTributo='2000') and ($totalValorVentaItem)">
							<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumMontoBase2000sin9996Linea" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
  								<xsl:call-template name="addWarning">  
  								<xsl:with-param name="warningCode" select="'4303'" />  
  								<xsl:with-param name="warningMessage"  
									select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumMontoBase2000sin9996Linea)"/>  
  								</xsl:call-template>  
							</xsl:if>
						</xsl:if>

						<xsl:if
							test="($codigoTributo='9999') and ($totalValorVentaItem)">
							<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumMontoBase9999Linea" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4304'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumMontoBase9999Linea)" />
								</xsl:call-template>
							</xsl:if>
						</xsl:if>

						<!-- 36 / 38. Importe del tributo -->
						<xsl:if
							test="($importeTributo) and not($importeTributo=0) and (($codigoTributo='9995') or ($codigoTributo='9997') or ($codigoTributo='9998'))">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3000'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $importeTributo, ' ',$codigoTributo)" />
							</xsl:call-template>
						</xsl:if>

						<xsl:if test="($codigoTributo='9996')">
							<xsl:variable name="dif_ImporteTributo" select="$importeTributo - $sumMontoTributo9996Linea" />
							<xsl:if test="($dif_ImporteTributo &lt; -1) or ($dif_ImporteTributo &gt; 1)">
 								<xsl:call-template name="addWarning"> 
 								<xsl:with-param name="warningCode" select="'4311'" /> 
 								<xsl:with-param name="warningMessage" 
									select="concat('Error en la linea: ', position(), ' ', $dif_ImporteTributo,' ', $importeTributo, ' ', $sumMontoTributo9996Linea)"/> 
 								</xsl:call-template> 
							</xsl:if>
						</xsl:if>

						<!-- 40 / 41. Total Importe de IGV o IVAP, según corresponda -->
						<!-- 47 / 48. Monto de la Sumatoria -->
						<xsl:if test="($codigoTributo='2000')">
							<xsl:variable name="dif_ImporteTributo" select="$importeTributo - ($sumMontoTributo2000sin9996Linea - $sumMontoCargoDescuento20)" />
							<xsl:if test="($dif_ImporteTributo &lt; -1) or ($dif_ImporteTributo &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4305'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $dif_ImporteTributo, ' ', $importeTributo, ' ', $sumMontoTributo2000sin9996Linea)"/>
								</xsl:call-template>
							</xsl:if>
						</xsl:if>
						
						<!-- 47 / 48. Monto de la Sumatoria -->
						<xsl:if test="($countCodigoTributo7152 &gt; 0)">				
							<xsl:variable name="dif_ImporteTributo" select="$importeTributo_7152 - $sumMontoTributo7152Linea" />
							<xsl:if test="($dif_ImporteTributo &lt; -0.001) or ($dif_ImporteTributo &gt; 0.001)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4321'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $countCodigoTributo7152, ' ', $dif_ImporteTributo, ' ', $importeTributo_7152, ' ', $sumMontoTributo7152Linea)"/>
								</xsl:call-template>
							</xsl:if>			
						</xsl:if>						

						<!-- 36 / 38 - 39 - 40 / 41 - 42 / 43. Código de tributo -->
						<xsl:if
							test="($tipoOperacion='0200' or $tipoOperacion='0201' or $tipoOperacion='0202' or $tipoOperacion='0203' or $tipoOperacion='0204'
							or $tipoOperacion='0205' or $tipoOperacion='0206' or $tipoOperacion='0207' or $tipoOperacion='0208')">
							<xsl:call-template name="regexpValidateElementIfExistTrue">
								<xsl:with-param name="errorCodeValidate" select="'3107'" />
								<xsl:with-param name="node" select="$codigoTributo" />
								<xsl:with-param name="regexp" select="'^(1000|1016|2000|9997|9998|9999)$'" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $tipoOperacion)" />
							</xsl:call-template>
						</xsl:if>

						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4255'" />
							<xsl:with-param name="node" select="$codigoTributo_schemeName" />
							<xsl:with-param name="regexp" select="'^(Codigo de tributos)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 11) ', $codigoTributo_schemeName)" />
						</xsl:call-template>

						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'4256'" />
							<xsl:with-param name="node"
								select="$codigoTributo_schemeAgencyName" />
							<xsl:with-param name="regexp"
								select="'^(PE:SUNAT)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 12) ', $codigoTributo_schemeAgencyName)" />
						</xsl:call-template>

						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'4257'" />
							<xsl:with-param name="node"
								select="$codigoTributo_schemeURI" />
							<xsl:with-param name="regexp"
								select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo05)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 5) ', $codigoTributo_schemeURI)" />
						</xsl:call-template>

						<!-- 42 / 43 -->
						<xsl:if
							test="(($codigoTributo='2000') and ($totalValorVentaItem &gt; 0) and ($sumMontoBaseAfectaIGV17For &gt; 0))">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2650'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $totalValorVentaItem, ' ', $sumMontoBaseAfectaIGV17For)" />
							</xsl:call-template>
						</xsl:if>

						<!-- 36 / 38 - 39 - 40 / 41 - 42 / 43. Nombre de tributo -->
						<xsl:call-template
							name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist"
								select="'2054'" />
							<xsl:with-param name="node"
								select="$nombreTributo" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $nombreTributo)" />
						</xsl:call-template>

						<xsl:if
							test="($codigoTributo='1000' and not($nombreTributo='IGV')) or ($codigoTributo='1016' and not($nombreTributo='IVAP'))
								or ($codigoTributo='2000' and not($nombreTributo='ISC')) or ($codigoTributo='9995' and not($nombreTributo='EXP'))
								or ($codigoTributo='9996' and not($nombreTributo='GRA')) or ($codigoTributo='9997' and not($nombreTributo='EXO'))
								or ($codigoTributo='9998' and not($nombreTributo='INA')) or ($codigoTributo='9999' and not($nombreTributo='OTROS'))">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2964'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $nombreTributo)" />
							</xsl:call-template>
						</xsl:if>

						<!-- 36 / 38 - 39 - 40 / 41 - 42 / 43. Código internacional tributo -->
						<xsl:call-template
							name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist"
								select="'2052'" />
							<xsl:with-param name="node"
								select="$codigoInternacionalTributo" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $codigoInternacionalTributo)" />
						</xsl:call-template>

						<xsl:if
							test="($codigoTributo='1000' and not($codigoInternacionalTributo='VAT')) or ($codigoTributo='1016' and not($codigoInternacionalTributo='VAT'))
								or ($codigoTributo='2000' and not($codigoInternacionalTributo='EXC')) or ($codigoTributo='9995' and not($codigoInternacionalTributo='FRE'))
								or ($codigoTributo='9996' and not($codigoInternacionalTributo='FRE')) or ($codigoTributo='9997' and not($codigoInternacionalTributo='VAT'))
								or ($codigoTributo='9998' and not($codigoInternacionalTributo='FRE')) or ($codigoTributo='9999' and not($codigoInternacionalTributo='OTH'))">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2961'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $codigoInternacionalTributo)" />
							</xsl:call-template>
						</xsl:if>
					</xsl:for-each>
				</xsl:for-each>
			</xsl:for-each>
		</xsl:for-each>

		<!-- CALCULOS -->
	    <xsl:variable name="totalBaseIGVxLinea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1000']]/cbc:TaxableAmount)"/>
	    <xsl:variable name="totalBaseIVAPxLinea" select="sum(cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1016']]/cbc:TaxableAmount)"/>
	    
	    <xsl:variable name="sumatoriaBaseIGV" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1000']]/cbc:TaxableAmount)"/>
	    <xsl:variable name="sumatoriaBaseIVAP" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1016']]/cbc:TaxableAmount)"/>	    
    	    	     	
    	<xsl:variable name="sumatoriaIGV" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1000']]/cbc:TaxAmount)"/>
    	<xsl:variable name="sumatoriaIVAP" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1016']]/cbc:TaxAmount)"/>
    	<xsl:variable name="sumatoriaISC" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '2000']]/cbc:TaxAmount)"/> 
    	<xsl:variable name="sumatoriaICBPER" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '7152']]/cbc:TaxAmount)"/>				    	
    	<xsl:variable name="sumatoriaOtrosTributos" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '9999']]/cbc:TaxAmount)"/>
	    	
	    <xsl:variable name="sumatoriaDescuentosGlobales" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode [text() = '02' or text() = '04']]/cbc:Amount)"/>
		<xsl:variable name="sumatoriaDescuentosGlobales_02" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode[text() = '02']]/cbc:Amount)"/>	    
	    <xsl:variable name="sumatoriaCargosGobales_49" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode [text() = '49']]/cbc:Amount)"/>	    	    
	    <xsl:variable name="sumatoriaAnticipos_20" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode [text() = '20']]/cbc:Amount)"/>	 	    
	    <!-- 55. Total precio de venta (Subtotal de la factura) -->
	    <xsl:variable name="sumatoriaTotalValorVenta" select="sum(cac:LegalMonetaryTotal/cbc:LineExtensionAmount)"/>
	    <xsl:variable name="sumatoriaTotalPrecioVentaSubTotal" select="sum(cac:LegalMonetaryTotal/cbc:TaxInclusiveAmount)"/>

	    <xsl:if test="(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1000']]) and 
 	    		not ($tipoOperacion = '0113')"> 
			<!-- <xsl:variable name="totalImporteCalculado" select="($totalBaseIGVxLinea - $sumatoriaDescuentosGlobales + $sumatoriaCargosGobales_49 - $sumatoriaAnticipos_20) * number($porcentajeIGV)"/>-->	
			<xsl:variable name="totalImporteCalculado" select="($totalBaseIGVxLinea - $sumatoriaDescuentosGlobales + $sumatoriaCargosGobales_49 - $sumatoriaAnticipos_20) * number($porcentajeIGVVigenteLineaDecimal)"/>					
			<xsl:variable name="dif_TotalImporte" select="$sumatoriaIGV - $totalImporteCalculado" />
			<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4290'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea 1): ', $dif_TotalImporte, ' ', $sumatoriaIGV, ' ', $totalImporteCalculado, ' ', $totalBaseIGVxLinea, ' ', $sumatoriaDescuentosGlobales, ' ', $sumatoriaCargosGobales_49, ' ', $sumatoriaAnticipos_20, ' ',$porcentajeIGVVigenteLineaDecimal)"/>
				</xsl:call-template>
			</xsl:if>
	    </xsl:if>
	    
	    <xsl:if test="(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1000']]) and 
	    		($tipoOperacion = '0113') and ($sumatoriaIGV &gt; 0)">
			<!-- <xsl:variable name="totalImporteCalculado" select="($totalBaseIGVxLinea - $sumatoriaDescuentosGlobales + $sumatoriaCargosGobales_49) * number($porcentajeIGV)"/>-->	
			<xsl:variable name="totalImporteCalculado" select="($totalBaseIGVxLinea - $sumatoriaDescuentosGlobales + $sumatoriaCargosGobales_49) * number($porcentajeIGVVigenteLineaDecimal)"/>		
			<xsl:variable name="dif_TotalImporte" select="$sumatoriaIGV - $totalImporteCalculado" />
			<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4290'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea 2): ', $dif_TotalImporte, ' ', $sumatoriaIGV, ' ', $totalImporteCalculado, ' ', $totalBaseIGVxLinea, ' ', $sumatoriaDescuentosGlobales, ' ', $sumatoriaCargosGobales_49, ' ', $porcentajeIGVVigenteLineaDecimal)"/>
				</xsl:call-template>
			</xsl:if>
	    </xsl:if>
		
	    <xsl:if test="cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1016']]">
			<xsl:variable name="totalImporteCalculado" select="($totalBaseIVAPxLinea - $sumatoriaDescuentosGlobales + $sumatoriaCargosGobales_49) * 0.04"/>			
			<xsl:variable name="dif_TotalImporte" select="$sumatoriaIVAP - $totalImporteCalculado" />
			<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4302'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea 3): ', $dif_TotalImporte, ' ', $sumatoriaIVAP, ' ', $totalImporteCalculado, ' ', $totalBaseIVAPxLinea, ' ', $sumatoriaDescuentosGlobales, ' ', $sumatoriaCargosGobales_49)"/>
				</xsl:call-template>
			</xsl:if>
	    </xsl:if>

	    <xsl:if test="(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1000']]/cbc:TaxableAmount) 
	    		and ($sumatoriaBaseIGV &gt; 0)">
			<xsl:variable name="totalImporteCalculado" select="($sumValorVentaItem1000For - $sumatoriaDescuentosGlobales + $sumatoriaCargosGobales_49)"/>			
			<xsl:variable name="dif_TotalImporte" select="$sumatoriaBaseIGV - $totalImporteCalculado" />
			<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4299'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $sumatoriaBaseIGV, ' ', $totalImporteCalculado, ' ', $sumValorVentaItem1000For, ' ', $sumatoriaDescuentosGlobales, ' ', $sumatoriaCargosGobales_49)"/>
				</xsl:call-template>
			</xsl:if>	  
	   	</xsl:if> 
	    
	    <xsl:if test="(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1016']]/cbc:TaxableAmount) 
	    		and ($sumatoriaBaseIVAP &gt; 0)">
			<xsl:variable name="totalImporteCalculado" select="($sumValorVentaItem1016For - $sumatoriaDescuentosGlobales + $sumatoriaCargosGobales_49)"/>			
			<xsl:variable name="dif_TotalImporte" select="$sumatoriaBaseIVAP - $totalImporteCalculado" />
			<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4300'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $sumatoriaBaseIVAP, ' ', $totalImporteCalculado, ' ', $sumValorVentaItem1016For, ' ', $sumatoriaDescuentosGlobales, ' ', $sumatoriaCargosGobales_49)"/>
				</xsl:call-template>
			</xsl:if>	  
	   	</xsl:if>  	    
	   			 		   
		<xsl:if test="cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '9999']]">			
			<xsl:variable name="dif_TotalImporte" select="$sumatoriaOtrosTributos - $sumMontoTributo9999Linea" />
			<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4306'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $sumatoriaOtrosTributos, ' ', $sumMontoTributo9999Linea)"/>
				</xsl:call-template>
			</xsl:if>			
		</xsl:if>		
			   			 		   
	    <xsl:if test="not($tipoOperacion = '0113')">	   			 		   	   			 		    		
			<xsl:if test="($sumatoriaTotalPrecioVentaSubTotal) and ($countCodigoTributo1016 = 0)">	
				<xsl:variable name="sumaTotalPrecioVenta" select="$sumatoriaTotalValorVenta + $sumatoriaISC + $sumatoriaOtrosTributos + $sumatoriaICBPER + $sumatoriaAnticipos_20"/>		
				<!-- <xsl:variable name="totalImpuestoCalculado" select="(($totalBaseIGVxLinea - $sumatoriaDescuentosGlobales_02 + $sumatoriaCargosGobales_49) * number($porcentajeIGV))"/> -->
				<xsl:variable name="totalImpuestoCalculado" select="(($totalBaseIGVxLinea - $sumatoriaDescuentosGlobales_02 + $sumatoriaCargosGobales_49) * number($porcentajeIGVVigenteLineaDecimal))"/>
				<xsl:variable name="totalImporteCalculado" select="$sumaTotalPrecioVenta + $totalImpuestoCalculado"/>			
				<xsl:variable name="dif_TotalImporte" select="$sumatoriaTotalPrecioVentaSubTotal - $totalImporteCalculado" />
				<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4310'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: a) ', $dif_TotalImporte, ' | ', $sumatoriaTotalPrecioVentaSubTotal, ' | ', $totalImporteCalculado, ' | ', $totalImpuestoCalculado, ' | ', $sumaTotalPrecioVenta, ' | ',$totalBaseIGVxLinea, ' | ',$sumatoriaDescuentosGlobales_02, ' | ',$sumatoriaCargosGobales_49, ' | ',$porcentajeIGVVigenteLineaDecimal)"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:if>	
		</xsl:if>   		 
		
		<xsl:if test="($sumatoriaTotalPrecioVentaSubTotal) and ($countCodigoTributo1016 &gt; 0)">
			<xsl:variable name="sumaTotalPrecioVenta" select="$sumatoriaTotalValorVenta + $sumatoriaOtrosTributos + $sumatoriaICBPER"/>		
			<xsl:variable name="totalImpuestoCalculado" select="(($totalBaseIVAPxLinea - $sumatoriaDescuentosGlobales_02 + $sumatoriaCargosGobales_49)* 0.04)"/>
			<xsl:variable name="totalImporteCalculado" select="$sumaTotalPrecioVenta + $totalImpuestoCalculado"/>					
			<xsl:variable name="dif_TotalImporte" select="$sumatoriaTotalPrecioVentaSubTotal - $totalImporteCalculado" />
			<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4310'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: b) | ', $dif_TotalImporte, ' | ', $sumatoriaTotalPrecioVentaSubTotal, ' | ', $totalImporteCalculado, ' | ', $totalImpuestoCalculado, ' | ', $sumaTotalPrecioVenta, ' | ',$totalBaseIVAPxLinea, ' | ',$sumatoriaDescuentosGlobales_02, ' | ',$sumatoriaCargosGobales_49)"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:if>	  		

		<!-- 44. Cargos y Descuentos Globales -->
		<!-- 53. FISE (Ley 29852) Fondo Inclusión Social Energético -->
		<!-- Información Adicional - Percepciones -->

		<xsl:variable name="countCodigoCargoDescuento51" select="count(cac:AllowanceCharge/cbc:AllowanceChargeReasonCode[text()='51'])" />
		<xsl:variable name="countCodigoCargoDescuento52" select="count(cac:AllowanceCharge/cbc:AllowanceChargeReasonCode[text()='52'])" />
		<xsl:variable name="countCodigoCargoDescuento53" select="count(cac:AllowanceCharge/cbc:AllowanceChargeReasonCode[text()='53'])" />
		
		<!-- 57. Código del motivo del cargo/descuento 51 o 52 o 53 -->
		<xsl:if test="($tipoOperacion='2001') and ($countCodigoCargoDescuento51 = 0 and $countCodigoCargoDescuento52 = 0 and $countCodigoCargoDescuento53 = 0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3093'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoCargoDescuento51, ' ', $countCodigoCargoDescuento52, ' ', $countCodigoCargoDescuento53)" />
			</xsl:call-template>
		</xsl:if>
		
		<xsl:if test="not($tipoOperacion='2001') and not($countCodigoCargoDescuento51 = 0 and $countCodigoCargoDescuento52 = 0 and $countCodigoCargoDescuento53 = 0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3308'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $tipoOperacion, ' ', $countCodigoCargoDescuento51, ' ', $countCodigoCargoDescuento52, ' ', $countCodigoCargoDescuento53)"/>
			</xsl:call-template>
		</xsl:if>
				
		<xsl:for-each select="cac:AllowanceCharge">
			<!-- 44. Indicador de cargo/descuento -->
			<!-- 53. Indicador de cargo -->
			<!-- 54. Indicador de cargo/descuento -->
			<xsl:variable name="indicadorCargoDescuento" select="./cbc:ChargeIndicator" />
			<!-- 44. Código de cargo/descuento -->
			<!-- 53. Código del motivo del cargo -->
			<!-- 54. Código del motivo del cargo/descuento 51 o 52 o 53 -->
			<xsl:variable name="codigoCargoDescuento" select="./cbc:AllowanceChargeReasonCode" />
			<xsl:variable name="codigoCargoDescuento_listAgencyName" select="./cbc:AllowanceChargeReasonCode/@listAgencyName" />
			<xsl:variable name="codigoCargoDescuento_listName" select="./cbc:AllowanceChargeReasonCode/@listName" />
			<xsl:variable name="codigoCargoDescuento_listURI" select="./cbc:AllowanceChargeReasonCode/@listURI" />
			<!-- 44. Porcentaje aplicado -->
			<!-- 53. Factor del cargo/descuento -->
			<xsl:variable name="porcentajeAplicado" select="./cbc:MultiplierFactorNumeric" />
			<!-- 44. Monto de cargo/descuento -->
			<!-- 53. Monto del cargo -->
			<!-- 54. Monto de la percepción -->
			<xsl:variable name="montoCargoDescuento" select="./cbc:Amount" />
			<xsl:variable name="montoCargoDescuento_currencyID" select="./cbc:Amount/@currencyID" />
			<!-- 44. Monto base del cargo/descuento -->
			<!-- 53. Monto base del cargo -->
			<!-- 54. Base imponible de la percepción -->
			<xsl:variable name="montoBaseCargoDescuento" select="./cbc:BaseAmount" />
			<xsl:variable name="montoBaseCargoDescuento_currencyID" select="./cbc:BaseAmount/@currencyID" />

			<!-- 44. Indicador de cargo/descuento -->
			<!-- 53. Indicador de cargo -->
			<!-- 54. Indicador de cargo/descuento -->

			<xsl:if
				test="$codigoCargoDescuento = '45' or $codigoCargoDescuento = '46' or $codigoCargoDescuento = '49' or $codigoCargoDescuento = '50' or $codigoCargoDescuento = '51' or $codigoCargoDescuento = '52' or $codigoCargoDescuento = '53'">
				<xsl:call-template name="regexpValidateElementIfExistTrue">
					<xsl:with-param name="errorCodeValidate" select="'3114'" />
					<xsl:with-param name="node" select="$indicadorCargoDescuento" />
					<xsl:with-param name="regexp" select="'^(false)$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: 3) ', position(), ' ', $indicadorCargoDescuento, ' ',$codigoCargoDescuento)" />
				</xsl:call-template>
			</xsl:if>

			<xsl:if test="$codigoCargoDescuento = '02' or $codigoCargoDescuento = '03' or $codigoCargoDescuento = '04' or $codigoCargoDescuento = '05' 
					or $codigoCargoDescuento = '06' or $codigoCargoDescuento = '20'">
				<xsl:call-template name="regexpValidateElementIfExistTrue">
					<xsl:with-param name="errorCodeValidate" select="'3114'" />
					<xsl:with-param name="node" select="$indicadorCargoDescuento" />
					<xsl:with-param name="regexp" select="'^(true)$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: 4) ', position(), ' ', $indicadorCargoDescuento, ' ',$codigoCargoDescuento)" />
				</xsl:call-template>
			</xsl:if>

			<!-- 44. Código de cargo/descuento -->
			<xsl:if test="($indicadorCargoDescuento)">
				<xsl:call-template
					name="existValidateElementNotExist">
					<xsl:with-param name="errorCodeNotExist"
						select="'3072'" />
					<xsl:with-param name="node"
						select="$codigoCargoDescuento" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento)" />
				</xsl:call-template>
			</xsl:if>

			<xsl:if
				test="($codigoCargoDescuento='00') or ($codigoCargoDescuento='01') or ($codigoCargoDescuento='47') or ($codigoCargoDescuento='48')">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4291'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento)"/>
				</xsl:call-template>
			</xsl:if>

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'3071'" />
				<xsl:with-param name="node" select="$codigoCargoDescuento" />
				<xsl:with-param name="regexp" select="'^(00|01|02|03|04|05|06|07|20|45|46|47|48|49|50|51|52|53|54|61|62|63)$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento)" />
			</xsl:call-template>

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4251'" />
				<xsl:with-param name="node" select="$codigoCargoDescuento_listAgencyName" />
				<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 14) ', $codigoCargoDescuento_listAgencyName)" />
			</xsl:call-template>

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'4252'" />
				<xsl:with-param name="node"
					select="$codigoCargoDescuento_listName" />
				<xsl:with-param name="regexp"
					select="'^(Cargo/descuento)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 14) ', $codigoCargoDescuento_listName)" />
			</xsl:call-template>

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'4253'" />
				<xsl:with-param name="node"
					select="$codigoCargoDescuento_listURI" />
				<xsl:with-param name="regexp"
					select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo53)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 8) ', $codigoCargoDescuento_listURI)" />
			</xsl:call-template>

			<!-- 44. Factor de cargo/descuento -->
			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'3025'" />
				<xsl:with-param name="node"
					select="$porcentajeAplicado" />
				<xsl:with-param name="regexp"
					select="'^[0-9]{1,3}(\.[0-9]{1,5})?$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', position(), ' ', $porcentajeAplicado)" />
			</xsl:call-template>

			<!-- 44. Monto de cargo/descuento -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2968'" />
				<xsl:with-param name="node" select="$montoCargoDescuento" />
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', position(), ' ', $montoCargoDescuento)" />
			</xsl:call-template>
			
			<xsl:if test="($codigoCargoDescuento) and ($porcentajeAplicado &gt; 0)">	
				<xsl:variable name="calculoMontoCargoDescuento" select="($montoBaseCargoDescuento * $porcentajeAplicado)" />
				<xsl:variable name="dif_MontoCargoDescuento" select="$montoCargoDescuento - $calculoMontoCargoDescuento" />
				<xsl:if test="($dif_MontoCargoDescuento &lt; -1) or ($dif_MontoCargoDescuento &gt; 1)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4322'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), ' ', $dif_MontoCargoDescuento, ' ', $montoCargoDescuento, ' ', $calculoMontoCargoDescuento)"/>
					</xsl:call-template>				
				</xsl:if>
			</xsl:if>
			
			<xsl:if test="($codigoCargoDescuento = '04') or ($codigoCargoDescuento = '05')
					or ($codigoCargoDescuento = '06') or ($codigoCargoDescuento = '20')">
				<xsl:if test="($montoCargoDescuento &gt; 0) and ($sumTotalAnticipos = 0)">	
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3282'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento, ' ',$montoCargoDescuento, ' ',$sumTotalAnticipos)" />
					</xsl:call-template>					
				</xsl:if>	
			</xsl:if>				

			<xsl:if test="(not(($codigoCargoDescuento='51') or ($codigoCargoDescuento='52') or ($codigoCargoDescuento='53')))">
				<xsl:if test="($montoCargoDescuento_currencyID) and not($tipoMoneda=$montoCargoDescuento_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 12) ', position(), ' ', $tipoMoneda, ' ',$montoCargoDescuento_currencyID)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>

			<!-- 53. Monto del cargo -->
			<xsl:if
				test="($montoCargoDescuento) and ($montoCargoDescuento = 0 ) and ($codigoCargoDescuento='45')">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3074'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $montoCargoDescuento, ' ',$codigoCargoDescuento)" />
				</xsl:call-template>
			</xsl:if>

			<!-- 54. Monto de la percepción -->
			<xsl:if
				test="(($codigoCargoDescuento='51') or ($codigoCargoDescuento='52') or ($codigoCargoDescuento='53'))">
				<xsl:variable name="calculoPercepcion"
					select="($montoBaseCargoDescuento * $porcentajeAplicado)" />
				<xsl:variable name="dif_MontoCargoDescuento"
					select="$montoCargoDescuento - $calculoPercepcion" />
				<xsl:if
					test="($dif_MontoCargoDescuento &lt; -1) or ($dif_MontoCargoDescuento &gt; 1)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2798'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $dif_MontoCargoDescuento, ' ', $montoCargoDescuento, ' ', $calculoPercepcion)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:call-template
					name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate"
						select="'2792'" />
					<xsl:with-param name="node"
						select="$montoCargoDescuento_currencyID" />
					<xsl:with-param name="regexp" select="'^(PEN)$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $montoCargoDescuento_currencyID)" />
				</xsl:call-template>
			</xsl:if>

			<!-- 44. Monto base del cargo/descuento -->
			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'3016'" />
				<xsl:with-param name="node"
					select="$montoBaseCargoDescuento" />
				<xsl:with-param name="regexp"
					select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', position(), ' ', $montoBaseCargoDescuento)" />
			</xsl:call-template>

			<xsl:if test="(not(($codigoCargoDescuento='51') or ($codigoCargoDescuento='52') or ($codigoCargoDescuento='53')))">
				<xsl:if test="($montoBaseCargoDescuento_currencyID) and not($tipoMoneda=$montoBaseCargoDescuento_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 13) ', position(), ' ', $tipoMoneda, ' ',$montoBaseCargoDescuento_currencyID)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>
			<!-- 53. Monto del cargo -->
			<xsl:if
				test="($codigoCargoDescuento='45') and (not($montoBaseCargoDescuento) or ($montoBaseCargoDescuento = 0))">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3092'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $montoBaseCargoDescuento, ' ', $codigoCargoDescuento)" />
				</xsl:call-template>
			</xsl:if>

			<!-- 54. Base imponible de la percepción -->
			<xsl:if test="(($codigoCargoDescuento='51') or ($codigoCargoDescuento='52') or ($codigoCargoDescuento='53'))">
				<xsl:if test="($tipoMoneda = 'PEN') and (number($montoBaseCargoDescuento) &gt; number($sumImporteTotal))">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2797'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento, ' ', $tipoMoneda, ' ',$montoBaseCargoDescuento, ' ',$sumImporteTotal)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="not($montoBaseCargoDescuento) or ($montoBaseCargoDescuento = 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3233'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $codigoCargoDescuento, ' ',$montoBaseCargoDescuento)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2788'" />
					<xsl:with-param name="node" select="$montoBaseCargoDescuento_currencyID" />
					<xsl:with-param name="regexp" select="'^(PEN)$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $montoBaseCargoDescuento_currencyID)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>

		<!-- Información Adicional - Anticipos -->
		<xsl:for-each select="cac:PrepaidPayment">
			<!-- 55. Identificador del pago -->
			<xsl:variable name="identificadorPago" select="./cbc:ID" />
			<xsl:variable name="identificadorPago_schemeName" select="./cbc:ID/@schemeName" />
			<xsl:variable name="identificadorPago_schemeAgencyName" select="./cbc:ID/@schemeAgencyName" />
			<!-- 55. Monto anticipado -->
			<xsl:variable name="montoAnticipado" select="./cbc:PaidAmount" />
			<xsl:variable name="monedaMontoAnticipado" select="./cbc:PaidAmount/@currencyID" />

			<!-- 55. Identificador del pago -->
			<xsl:if test="($montoAnticipado)">
				<xsl:call-template
					name="existValidateElementNotExist">
					<xsl:with-param name="errorCodeNotExist"
						select="'3211'" />
					<xsl:with-param name="node"
						select="$identificadorPago" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', $identificadorPago)" />
				</xsl:call-template>
			</xsl:if>

			<xsl:if
				test="count(key('by-prepaidPayment-PaidAmount', number($montoAnticipado))) > 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3212'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $montoAnticipado)" />
				</xsl:call-template>
			</xsl:if>

			<xsl:call-template name="isTrueExpresion">
				<xsl:with-param name="errorCodeValidate"
					select="'3213'" />
				<xsl:with-param name="node"
					select="$identificadorPago" />
				<xsl:with-param name="expresion"
					select="count(key('by-document-additional-anticipo', $identificadorPago)) = 0" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', position(), ' ', $identificadorPago)" />
			</xsl:call-template>

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'4255'" />
				<xsl:with-param name="node"
					select="$identificadorPago_schemeName" />
				<xsl:with-param name="regexp" select="'^(Anticipo)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 12) ', $identificadorPago_schemeName)" />
			</xsl:call-template>

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'4256'" />
				<xsl:with-param name="node"
					select="$identificadorPago_schemeAgencyName" />
				<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 13) ', $identificadorPago_schemeAgencyName)" />
			</xsl:call-template>

			<!-- 55. Monto anticipado -->
			<xsl:if
				test="($montoAnticipado) and (number($montoAnticipado) &lt;= 0) ">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2503'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', number($montoAnticipado))" />
				</xsl:call-template>				
			</xsl:if>

			<xsl:if
				test="($montoAnticipado &gt; 0) and not($sumTotalAnticipos &gt; 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3220'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $montoAnticipado, ' ',$sumTotalAnticipos)" />
				</xsl:call-template>
			</xsl:if>

			<!-- Moneda debe ser la misma en todo el documento -->
			<xsl:if
				test="($monedaMontoAnticipado) and not($tipoMoneda=$monedaMontoAnticipado)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: 14) ', position(), ' ', $tipoMoneda, ' ',$monedaMontoAnticipado)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>

		<!-- LegalMonetaryTotal -->
		<xsl:variable name="sumMontoAnticipadoLinea" select="sum(cac:PrepaidPayment/cbc:PaidAmount)" />
		<xsl:for-each select="cac:LegalMonetaryTotal">
			<!-- 48. Total Descuentos  Globales (Que no afectan la base)-->	
<!-- 			<xsl:variable name="totalDescuentos" select="./cbc:AllowanceTotalAmount"/> -->
			<xsl:variable name="totalDescuentos">	
				<xsl:choose>
    				<xsl:when test="count(./cbc:AllowanceTotalAmount) &gt; 0">
    					<xsl:value-of select="sum(./cbc:AllowanceTotalAmount)"/></xsl:when>
    				<xsl:otherwise>0</xsl:otherwise>
  				</xsl:choose>       
			</xsl:variable>			
			<xsl:variable name="totalDescuentos_currencyID" select="./cbc:AllowanceTotalAmount/@currencyID"/>
			<!-- 49. Total otros Cargos (Que no afectan la base) -->
<!-- 			<xsl:variable name="totalOtrosCargos" select="./cbc:ChargeTotalAmount"/> -->
			<xsl:variable name="totalOtrosCargos">	
				<xsl:choose>
    				<xsl:when test="count(./cbc:ChargeTotalAmount) &gt; 0">
    					<xsl:value-of select="sum(./cbc:ChargeTotalAmount)"/></xsl:when>
    				<xsl:otherwise>0</xsl:otherwise>
  				</xsl:choose>       
			</xsl:variable>				
			<xsl:variable name="totalOtrosCargos_currencyID" select="./cbc:ChargeTotalAmount/@currencyID"/>
			<!-- 50. Importe total -->
			<xsl:variable name="importeTotal" select="./cbc:PayableAmount"/>								
			<xsl:variable name="importeTotal_currencyID" select="./cbc:PayableAmount/@currencyID"/>		
			<!-- 51. Total Valor de Venta -->
			<xsl:variable name="totalValorVentaExiste" select="./cbc:LineExtensionAmount"/>
<!-- 			<xsl:variable name="totalValorVenta">	 -->
<!-- 				<xsl:choose> -->
<!--     				<xsl:when test="count(./cbc:LineExtensionAmount) &gt; 0"> -->
<!--     					<xsl:value-of select="sum(./cbc:LineExtensionAmount)"/></xsl:when> -->
<!--     				<xsl:otherwise>0</xsl:otherwise> -->
<!--   				</xsl:choose>        -->
<!-- 			</xsl:variable>				 -->
			<xsl:variable name="totalValorVenta">	
				<xsl:choose>
    				<xsl:when test="count(./cbc:LineExtensionAmount) &gt; 0">
    					<xsl:value-of select="./cbc:LineExtensionAmount"/></xsl:when>
    				<xsl:otherwise>0</xsl:otherwise>
  				</xsl:choose>       
			</xsl:variable>				
			<xsl:variable name="totalValorVenta_currencyID" select="./cbc:LineExtensionAmount/@currencyID"/>
			<!-- 52. Total Precio de Venta -->
			<xsl:variable name="totalPrecioVentaExiste" select="./cbc:TaxInclusiveAmount"/>
			<xsl:variable name="totalPrecioVenta">	
				<xsl:choose>
    				<xsl:when test="count(./cbc:TaxInclusiveAmount) &gt; 0">
    					<xsl:value-of select="./cbc:TaxInclusiveAmount"/></xsl:when>
    				<xsl:otherwise>0</xsl:otherwise>
  				</xsl:choose>       
			</xsl:variable>				
			<xsl:variable name="totalPrecioVenta_currencyID" select="./cbc:TaxInclusiveAmount/@currencyID"/>
			<!-- 53. Monto para Redondeo del Importe Total -->
<!-- 			<xsl:variable name="montoRedondeoImporteTotal" select="./cbc:PayableRoundingAmount"/> -->
			<xsl:variable name="montoRedondeoImporteTotal">	
				<xsl:choose>
    				<xsl:when test="count(./cbc:PayableRoundingAmount) &gt; 0">
    					<xsl:value-of select="sum(./cbc:PayableRoundingAmount)"/></xsl:when>
    				<xsl:otherwise>0</xsl:otherwise>
  				</xsl:choose>       
			</xsl:variable>	
			<xsl:variable name="montoRedondeoImporteTotal_currencyID" select="./cbc:PayableRoundingAmount/@currencyID"/>
			<!-- 60. Total Anticipos -->	
<!-- 			<xsl:variable name="totalAnticipos" select="./cbc:PrepaidAmount" /> -->
			<xsl:variable name="totalAnticipos">	
				<xsl:choose>
    				<xsl:when test="count(./cbc:PrepaidAmount) &gt; 0">
    					<xsl:value-of select="sum(./cbc:PrepaidAmount)"/></xsl:when>
    				<xsl:otherwise>0</xsl:otherwise>
  				</xsl:choose>       
			</xsl:variable>					
			<xsl:variable name="totalAnticipos_currencyID" select="./cbc:PrepaidAmount/@currencyID" />
			<!-- 51. Suma Total Valor de Venta -->	
<!-- 			<xsl:variable name="sumTotalPrecioVenta" select="sum(./cbc:LineExtensionAmount)"/> -->
			<xsl:variable name="sumTotalPrecioVenta">	
				<xsl:choose>
    				<xsl:when test="count(./cbc:LineExtensionAmount) &gt; 0">
    					<xsl:value-of select="sum(./cbc:LineExtensionAmount)"/></xsl:when>
    				<xsl:otherwise>0</xsl:otherwise>
  				</xsl:choose>       
			</xsl:variable>				
			
			<!-- 45. Total Descuentos Globales (Que no afectan la base) -->
			<xsl:if test="$totalDescuentos">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2065'" />
					<xsl:with-param name="node" select="$totalDescuentos" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $totalDescuentos)" />
				</xsl:call-template>

<!-- 				<xsl:variable name="totalImporteCalculado" select="$sumMontoCargoDescuento01 + $sumMontoCargoDescuento03For" /> -->
				<xsl:variable name="totalImporteCalculado" select="$sumMontoCargoDescuento01For + $sumMontoCargoDescuento03" />
				<xsl:variable name="dif_TotalImporte" select="$totalDescuentos - $totalImporteCalculado" />
				<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4307'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $totalDescuentos, ' ', $totalImporteCalculado)"/>
					</xsl:call-template>					
				</xsl:if>	

				<!-- 45 Moneda debe ser la misma en todo el documento -->
				<xsl:if
					test="($totalDescuentos_currencyID) and not($tipoMoneda=$totalDescuentos_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 15) ', position(), ' ', $tipoMoneda, ' ',$totalDescuentos_currencyID)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>

			<!-- 46. Total otros Cargos (Que no afectan la base) -->
			<xsl:if test="$totalOtrosCargos">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2064'" />
					<xsl:with-param name="node" select="$totalOtrosCargos" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $totalOtrosCargos)" />
				</xsl:call-template>
				
				<xsl:variable name="totalImporteCalculado_1" select="$sumMontoCargoDescuento48For + $sumMontoCargoDescuento45 + $sumMontoCargoDescuento46 + $sumMontoCargoDescuento50" />
				<xsl:variable name="totalImporteCalculado" select="format-number($totalImporteCalculado_1,'#.##')" />
				<xsl:variable name="dif_TotalImporte" select="$totalOtrosCargos - number($totalImporteCalculado)" />
				<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4308'" />
						<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $totalOtrosCargos, ' ', $totalImporteCalculado)"/>
					</xsl:call-template>					
				</xsl:if>					

				<!-- Moneda debe ser la misma en todo el documento -->
				<xsl:if
					test="($totalOtrosCargos_currencyID) and not($tipoMoneda=$totalOtrosCargos_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 16) ', position(), ' ', $tipoMoneda, ' ',$totalOtrosCargos_currencyID)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>

			<!-- 47. Importe total -->
			<xsl:if test="$importeTotal">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2062'" />
					<xsl:with-param name="node" select="$importeTotal" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $importeTotal)" />
				</xsl:call-template>

				<xsl:variable name="totalImporteCalculado_1" select="$totalPrecioVenta + $totalOtrosCargos - $totalDescuentos - $totalAnticipos + $montoRedondeoImporteTotal" />
				<xsl:variable name="totalImporteCalculado" select="format-number($totalImporteCalculado_1,'#.##')" />
				<xsl:variable name="dif_TotalImporte" select="$importeTotal - number($totalImporteCalculado)" />
				<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4312'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $importeTotal, ' ', $totalImporteCalculado)"/>
					</xsl:call-template>				
				</xsl:if>	

				<!-- Moneda debe ser la misma en todo el documento -->
				<xsl:if
					test="($importeTotal_currencyID) and not($tipoMoneda=$importeTotal_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 17) ', position(), ' ', $tipoMoneda, ' ',$importeTotal_currencyID)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>

			<!-- 48. Total Valor de Venta -->
			<xsl:call-template name="existValidateElementNotExist">
				<xsl:with-param name="errorCodeNotExist" select="'4212'"/>
				<xsl:with-param name="node" select="$totalValorVentaExiste"/>
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $totalValorVentaExiste)"/>
			</xsl:call-template>			
						
			<xsl:if test="$totalValorVenta">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2031'" />
					<xsl:with-param name="node" select="$totalValorVenta" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error: ', $totalValorVenta)" />
				</xsl:call-template>

				<!-- Moneda debe ser la misma en todo el documento -->
				<xsl:if
					test="($totalValorVenta_currencyID) and not($tipoMoneda=$totalValorVenta_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 18) ', position(), ' ', $tipoMoneda, ' ',$totalValorVenta_currencyID)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>

			<xsl:variable name="sumLegalMonetaryTotal" select="$sumValorVentaItem1000For + $sumValorVentaItem1016For + $sumValorVentaItem9995For + $sumValorVentaItem9997For + $sumValorVentaItem9998For"/>
			<xsl:variable name="sumMontoCargoDescuento" select="($sumLegalMonetaryTotal - $sumMontoCargoDescuento02) + $sumMontoCargoDescuento49" />			
			<xsl:variable name="difLegalMonetaryTotal" select="$sumTotalPrecioVenta - $sumMontoCargoDescuento" />
			<xsl:if test="($difLegalMonetaryTotal &lt; -1) or ($difLegalMonetaryTotal &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4309'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', position(), ' - ', $difLegalMonetaryTotal, ' - ', $sumTotalPrecioVenta, ' - ',$sumMontoCargoDescuento, ' - ', $sumLegalMonetaryTotal, ' - ', $sumMontoCargoDescuento02, ' - ', $sumMontoCargoDescuento49)"/>
				</xsl:call-template>
			</xsl:if>

			<!-- 49. Total Precio de Venta -->
			<xsl:call-template name="existValidateElementNotExist">
				<xsl:with-param name="errorCodeNotExist" select="'4317'" />
				<xsl:with-param name="node" select="$totalPrecioVentaExiste" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', position(), ' ', $totalPrecioVentaExiste)" />
			</xsl:call-template>					
						
			<xsl:if test="$totalPrecioVenta">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'3019'" />
					<xsl:with-param name="node" select="$totalPrecioVenta" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $totalPrecioVenta)" />
				</xsl:call-template>

				<!-- Moneda debe ser la misma en todo el documento -->
				<xsl:if test="($totalPrecioVenta_currencyID) and not($tipoMoneda=$totalPrecioVenta_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 19) ', position(), ' ', $tipoMoneda, ' ',$totalPrecioVenta_currencyID)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>

			<!-- 53. Monto para Redondeo del Importe Total -->
			<xsl:if test="($montoRedondeoImporteTotal)">
				<xsl:if test="($montoRedondeoImporteTotal &lt; -1) or ($montoRedondeoImporteTotal &gt; 1)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4314'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), ' ', $montoRedondeoImporteTotal)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>

			<xsl:if
				test="($montoRedondeoImporteTotal_currencyID) and not($tipoMoneda = $montoRedondeoImporteTotal_currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoMoneda, ' ',$montoRedondeoImporteTotal_currencyID)" />
				</xsl:call-template>					
			</xsl:if>

			<!-- 56. Total Anticipos -->
			<xsl:if test="($totalAnticipos)">
				<xsl:variable name="dif_TotalAnticipos" select="$totalAnticipos - $sumMontoAnticipadoLinea" />
	 			<xsl:if test="($dif_TotalAnticipos &lt; -1) or ($dif_TotalAnticipos &gt; 1)"> 
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2509'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $totalAnticipos, ' ',$sumMontoAnticipadoLinea, ' ',$dif_TotalAnticipos)" />
					</xsl:call-template>				
				</xsl:if>			
			</xsl:if>

			<xsl:if test="($totalAnticipos &gt; 0) and (($sumMontoCargoDescuento04 = 0)
					and ($sumMontoCargoDescuento05 = 0) and ($sumMontoCargoDescuento06 = 0))">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3287'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $totalAnticipos, ' ',$sumMontoCargoDescuento04, ' ',$sumMontoCargoDescuento05, ' ',$sumMontoCargoDescuento06)" />
				</xsl:call-template>					
			</xsl:if>

			<!-- Moneda debe ser la misma en todo el documento -->
			<xsl:if
				test="($totalAnticipos_currencyID) and not($tipoMoneda=$totalAnticipos_currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: 20) ', position(), ' ', $tipoMoneda, ' ',$totalAnticipos_currencyID)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>
	
		<!-- Información Adicional -->
		<!-- Leyendas -->
		<!-- 51. Código de leyenda -->
		<xsl:if
			test="($countCodigoLeyenda1000 &gt; 1) or ($countCodigoLeyenda1002 &gt; 1) or ($countCodigoLeyenda2000 &gt; 1) or ($countCodigoLeyenda2001 &gt; 1) or 
			($countCodigoLeyenda2002 &gt; 1) or ($countCodigoLeyenda2003 &gt; 1) or ($countCodigoLeyenda2004 &gt; 1) or ($countCodigoLeyenda2005 &gt; 1) or
			($countCodigoLeyenda2006 &gt; 1) or ($countCodigoLeyenda2007 &gt; 1) or ($countCodigoLeyenda2008 &gt; 1) or ($countCodigoLeyenda2009 &gt; 1)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3014'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countCodigoLeyenda1000)" />
			</xsl:call-template>
		</xsl:if>

		<xsl:if
			test="($sumMontoBaseAfectaIGV17For &gt; 0) and ($countCodigoLeyenda2007 = 0)">
			<xsl:call-template name="addWarning">
				<xsl:with-param name="warningCode" select="'4264'" />
				<xsl:with-param name="warningMessage"
					select="concat('Error en la linea: ', position(), ' ', $countCodigoLeyenda2007, ' ', $sumMontoBaseAfectaIGV17For)" />
			</xsl:call-template>
		</xsl:if>

		<xsl:for-each select="cbc:Note">
			<!-- 56. Código de leyenda -->
			<xsl:variable name="codigoLeyenda"
				select="./@languageLocaleID" />
			<!-- 56. Descripción de la leyenda -->
			<xsl:variable name="descripcionLeyenda" select="." />

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'3027'" />
				<xsl:with-param name="node" select="$codigoLeyenda" />
				<xsl:with-param name="regexp" select="'^(1000|1002|2000|2001|2002|2003|2004|2005|2006|2007|2008|2009|2010|2011)$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', position(), ' ', $codigoLeyenda)" />
			</xsl:call-template>

			<!-- 51. Descripción de la leyenda -->
			<xsl:if test="($descripcionLeyenda)">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'3006'" />
					<xsl:with-param name="node" select="$descripcionLeyenda" />
					<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,199}$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $descripcionLeyenda)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>

		<!-- 52. Tipo de operación -->
		<xsl:call-template
			name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist"
				select="'3205'" />
			<xsl:with-param name="node" select="$tipoOperacion" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', position(), ' ', $tipoOperacion)" />
		</xsl:call-template>

		<xsl:call-template
			name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'3206'" />
			<xsl:with-param name="node" select="$tipoOperacion" />
			<xsl:with-param name="regexp"
				select="'^(0101|0113|0200|0201|0203|0204|0206|0207|0208|0301|0302|0401|1001|1002|1003|1004|2001|2100|2101|2102|2103|2104|2105)$'"/>
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', position(), ' ', $tipoOperacion)" />
		</xsl:call-template>

		<xsl:call-template
			name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate"
				select="'4260'" />
			<xsl:with-param name="node"
				select="$tipoOperacion_name" />
			<xsl:with-param name="regexp"
				select="'^(Tipo de Operacion)$'" />
			<xsl:with-param name="isError" select="false()" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', position(), ' ', $tipoOperacion_name)" />
		</xsl:call-template>

		<xsl:call-template
			name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate"
				select="'4261'" />
			<xsl:with-param name="node"
				select="$tipoOperacion_listSchemeURI" />
			<xsl:with-param name="regexp"
				select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo51)$'" />
			<xsl:with-param name="isError" select="false()" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', position(), ' ', $tipoOperacion_listSchemeURI)" />
		</xsl:call-template>

		<!-- Información Adicional - Anticipos -->
		<xsl:for-each select="cac:AdditionalDocumentReference">
			<!-- 55. Identificador del pago -->
			<xsl:variable name="identificadorPago"
				select="./cbc:DocumentStatusCode" />
			<xsl:variable name="identificadorPago_listName"
				select="./cbc:DocumentStatusCode/@listName" />
			<xsl:variable name="identificadorPago_listAgencyName"
				select="./cbc:DocumentStatusCode/@listAgencyName" />
			<!-- 55. Serie y Número de documento que se realizo el anticipo -->
			<xsl:variable name="serieNumeroDocumentoRealizoAnticipo"
				select="./cbc:ID" />
			<!-- 55. Tipo de comprobante que se realizo el anticipo -->
			<xsl:variable name="tipoComprobanteRealizoAnticipo"
				select="./cbc:DocumentTypeCode" />
			<xsl:variable
				name="tipoComprobanteRealizoAnticipo_listName"
				select="./cbc:DocumentTypeCode/@listName" />
			<xsl:variable
				name="tipoComprobanteRealizoAnticipo_listAgencyName"
				select="./cbc:DocumentTypeCode/@listAgencyName" />
			<xsl:variable
				name="tipoComprobanteRealizoAnticipo_listURI"
				select="./cbc:DocumentTypeCode/@listURI" />

			<xsl:if test="($tipoComprobanteRealizoAnticipo='02' or $tipoComprobanteRealizoAnticipo='03')">
				<xsl:call-template name="existValidateElementNotExist">
					<xsl:with-param name="errorCodeNotExist" select="'3216'" />
					<xsl:with-param name="node" select="$identificadorPago" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $identificadorPago)" />
				</xsl:call-template>

				<xsl:if
					test="count(key('by-idprepaid-in-root', number($identificadorPago))) &gt; 1">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3214'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $identificadorPago)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if
					test="count(key('by-additionalDocumentReference-DocumentStatusCode', number($identificadorPago))) &gt; 1">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3215'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $identificadorPago)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'4252'" />
				<xsl:with-param name="node"
					select="$identificadorPago_listName" />
				<xsl:with-param name="regexp" select="'^(Anticipo)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 15) ', $identificadorPago_listName)" />
			</xsl:call-template>

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'4251'" />
				<xsl:with-param name="node"
					select="$identificadorPago_listAgencyName" />
				<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 15) ', $identificadorPago_listAgencyName)" />
			</xsl:call-template>

			<xsl:if test="($identificadorPago)">
				<!-- 59. Tipo de comprobante que se realizo el anticipo -->
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2505'" />
					<xsl:with-param name="errorCodeValidate" select="'2505'" />
					<xsl:with-param name="node" select="$tipoComprobanteRealizoAnticipo" />
					<xsl:with-param name="regexp" select="'^(02|03)$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $tipoComprobanteRealizoAnticipo)" />
				</xsl:call-template>
			</xsl:if>

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'4252'" />
				<xsl:with-param name="node"
					select="$tipoComprobanteRealizoAnticipo_listName" />
				<xsl:with-param name="regexp"
					select="'^(Documento Relacionado)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 16) ', $tipoComprobanteRealizoAnticipo_listName)" />
			</xsl:call-template>

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'4251'" />
				<xsl:with-param name="node"
					select="$tipoComprobanteRealizoAnticipo_listAgencyName" />
				<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 16) ', $tipoComprobanteRealizoAnticipo_listAgencyName)" />
			</xsl:call-template>

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'4253'" />
				<xsl:with-param name="node"
					select="$tipoComprobanteRealizoAnticipo_listURI" />
				<xsl:with-param name="regexp"
					select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo12)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 9) ', $tipoComprobanteRealizoAnticipo_listURI)" />
			</xsl:call-template>

			<xsl:for-each select="./cac:IssuerParty">
				<xsl:for-each select="./cac:PartyIdentification">
					<!-- 55. Número de documento del emisor del anticipo -->
					<xsl:variable name="numeroDocumentoEmisorAnticipo"
						select="./cbc:ID" />
					<!-- 55. Tipo de documento del emisor del anticipo -->
					<xsl:variable name="tipoDocumentoEmisorAnticipo"
						select="./cbc:ID/@schemeID" />
					<xsl:variable
						name="numeroDocumentoEmisorAnticipo_schemeName"
						select="./cbc:ID/@schemeName" />
					<xsl:variable
						name="numeroDocumentoEmisorAnticipo_schemeAgencyName"
						select="./cbc:ID/@schemeAgencyName" />
					<xsl:variable
						name="numeroDocumentoEmisorAnticipo_schemeURI"
						select="./cbc:ID/@schemeURI" />

					<!-- 55. Serie y Número de documento que se realizo el anticipo -->
					<xsl:if test="($tipoDocumentoEmisorAnticipo)">
						<xsl:if test="($tipoComprobanteRealizoAnticipo='02')">
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'2521'" />
								<xsl:with-param name="node" select="$serieNumeroDocumentoRealizoAnticipo" />
								<xsl:with-param name="regexp"
									select="'^[F][A-Z0-9]{3}-[0-9]{1,8}$|^(E001)-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: ', position(), ' ', $tipoComprobanteRealizoAnticipo, ' ', $serieNumeroDocumentoRealizoAnticipo)" />
							</xsl:call-template>
						</xsl:if>

						<xsl:if test="($tipoComprobanteRealizoAnticipo='03')">
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'2521'" />
								<xsl:with-param name="node" select="$serieNumeroDocumentoRealizoAnticipo" />
								<xsl:with-param name="regexp"
									select="'^[B][A-Z0-9]{3}-[0-9]{1,8}$|^(EB01)-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'" />
<!--								<xsl:with-param name="isError" select="false()" />-->
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: ', position(), ' ', $tipoComprobanteRealizoAnticipo, ' ', $serieNumeroDocumentoRealizoAnticipo)" />
							</xsl:call-template>
						</xsl:if>
					</xsl:if>

					<!-- 55. Número de documento del emisor del anticipo -->
					<xsl:if test="($identificadorPago)">
						<xsl:call-template
							name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist"
								select="'3217'" />
							<xsl:with-param name="node"
								select="$numeroDocumentoEmisorAnticipo" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', $numeroDocumentoEmisorAnticipo)" />
						</xsl:call-template>
					</xsl:if>

					<!-- 55. Tipo de documento del emisor del anticipo -->
					<xsl:if
						test="($tipoDocumentoEmisorAnticipo) and not($tipoDocumentoEmisorAnticipo='6') ">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2520'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $tipoDocumentoEmisorAnticipo)" />
						</xsl:call-template>
					</xsl:if>

					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'4255'" />
						<xsl:with-param name="node"
							select="$numeroDocumentoEmisorAnticipo_schemeName" />
						<xsl:with-param name="regexp"
							select="'^(Documento de Identidad)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 13) ', $numeroDocumentoEmisorAnticipo_schemeName)" />
					</xsl:call-template>

					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'4256'" />
						<xsl:with-param name="node"
							select="$numeroDocumentoEmisorAnticipo_schemeAgencyName" />
						<xsl:with-param name="regexp"
							select="'^(PE:SUNAT)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 14) ', $numeroDocumentoEmisorAnticipo_schemeAgencyName)" />
					</xsl:call-template>

					<xsl:call-template
						name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate"
							select="'4257'" />
						<xsl:with-param name="node"
							select="$numeroDocumentoEmisorAnticipo_schemeURI" />
						<xsl:with-param name="regexp"
							select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo06)$'" />
						<xsl:with-param name="isError" select="false()" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea: 6) ', $numeroDocumentoEmisorAnticipo_schemeURI)" />
					</xsl:call-template>
				</xsl:for-each>
			</xsl:for-each>
		</xsl:for-each>

		<!-- Información Adicional - Guía Resumen -->
		<xsl:for-each select="cac:Delivery">
			<xsl:for-each select="./cac:Shipment">
				<!-- Dirección punto de llegada -->
				<xsl:for-each select="./cac:Delivery">
					<xsl:for-each select="./cac:DeliveryAddress">
						<!-- 57. Dirección punto de llegada - Código de ubigeo -->
						<xsl:variable name="puntoLlegadaCodigoUbigeo"
							select="./cbc:ID" />
						<xsl:variable
							name="puntoLlegadaCodigoUbigeo_schemeAgencyName"
							select="./cbc:ID/@schemeAgencyName" />
						<xsl:variable
							name="puntoLlegadaCodigoUbigeo_schemeName"
							select="./cbc:ID/@schemeName" />
						<!-- 57. Dirección punto de llegada - Dirección completa y detallada -->
						<xsl:variable name="puntoLlegadaDireccionCompleta"
							select="./cac:AddressLine/cbc:Line" />
						<!-- 57. Urbanización -->
						<xsl:variable name="puntoLlegadaUrbanizacion"
							select="./cbc:CitySubdivisionName" />
						<!-- 57. Provincia -->
						<xsl:variable name="puntoLlegadaProvincia"
							select="./cbc:CityName" />
						<!-- 57. Departamento -->
						<xsl:variable name="puntoLlegadaDepartamento"
							select="./cbc:CountrySubentity" />
						<!-- 57. Distrito -->
						<xsl:variable name="puntoLlegadaDistrito"
							select="./cbc:District" />

						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'4256'" />
							<xsl:with-param name="node"
								select="$puntoLlegadaCodigoUbigeo_schemeAgencyName" />
							<xsl:with-param name="regexp"
								select="'^(PE:INEI)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 15) ', $puntoLlegadaCodigoUbigeo_schemeAgencyName)" />
						</xsl:call-template>

						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'4255'" />
							<xsl:with-param name="node"
								select="$puntoLlegadaCodigoUbigeo_schemeName" />
							<xsl:with-param name="regexp"
								select="'^(Ubigeos)$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: 14) ', $puntoLlegadaCodigoUbigeo_schemeName)" />
						</xsl:call-template>

						<!-- 57. Dirección punto de llegada - Dirección completa y detallada -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4236'" />
							<xsl:with-param name="node" select="$puntoLlegadaDireccionCompleta" />
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,199}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', $puntoLlegadaDireccionCompleta)" />
						</xsl:call-template>

						<!-- 57. Urbanización -->
						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'4238'" />
							<xsl:with-param name="node"
								select="$puntoLlegadaUrbanizacion" />
							<xsl:with-param name="regexp"
								select="'^[\w\s].{1,25}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $puntoLlegadaUrbanizacion)" />
						</xsl:call-template>

						<!-- 57. Provincia -->
						<xsl:call-template
							name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate"
								select="'4239'" />
							<xsl:with-param name="node"
								select="$puntoLlegadaProvincia" />
							<xsl:with-param name="regexp"
								select="'^[\w\s].{1,30}$'" />
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $puntoLlegadaProvincia)" />
						</xsl:call-template>

						<!-- 57. Departamento -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4240'" />
							<xsl:with-param name="node" select="$puntoLlegadaDepartamento" />
<!-- 							<xsl:with-param name="regexp" select="'^[\w\s].{1,30}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,29}$'"/>
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $puntoLlegadaDepartamento)" />
						</xsl:call-template>

						<!-- 57. Distrito -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4241'" />
							<xsl:with-param name="node" select="$puntoLlegadaDistrito" />
<!-- 							<xsl:with-param name="regexp" select="'^[\w\s].{1,30}$'" /> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,29}$'"/>
							<xsl:with-param name="isError" select="false()" />
							<xsl:with-param name="descripcion"
								select="concat('Error en la linea: ', position(), ' ', $puntoLlegadaDistrito)" />
						</xsl:call-template>

						<!-- 57. Pais del uso, explotación o aprovechamiento del servicio -->
						<xsl:for-each select="./cac:Country">
							<!-- 57. Código de país -->
							<xsl:variable name="puntoLlegadaCodigoPais"
								select="./cbc:IdentificationCode" />
							<xsl:variable name="puntoLlegadaCodigoPais_ListID"
								select="./cbc:IdentificationCode/@listID" />
							<xsl:variable
								name="puntoLlegadaCodigoPais_ListAgencyName"
								select="./cbc:IdentificationCode/@listAgencyName" />
							<xsl:variable name="puntoLlegadaCodigoPais_ListName"
								select="./cbc:IdentificationCode/@listName" />

							<!-- 14. Código de país -->
							<xsl:call-template
								name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate"
									select="'4041'" />
								<xsl:with-param name="node"
									select="$puntoLlegadaCodigoPais" />
								<xsl:with-param name="regexp" select="'^(PE)$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: 1) ', position(), ' ', $puntoLlegadaCodigoPais)" />
							</xsl:call-template>

							<xsl:call-template
								name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate"
									select="'4254'" />
								<xsl:with-param name="node"
									select="$puntoLlegadaCodigoPais_ListID" />
								<xsl:with-param name="regexp"
									select="'^(ISO 3166-1)$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: 5) ', position(), ' ', $puntoLlegadaCodigoPais_ListID)" />
							</xsl:call-template>

							<xsl:call-template
								name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate"
									select="'4251'" />
								<xsl:with-param name="node"
									select="$puntoLlegadaCodigoPais_ListAgencyName" />
								<xsl:with-param name="regexp"
									select="'^(United Nations Economic Commission for Europe)$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: 17) ', position(), ' ', $puntoLlegadaCodigoPais_ListAgencyName)" />
							</xsl:call-template>

							<xsl:call-template
								name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate"
									select="'4252'" />
								<xsl:with-param name="node"
									select="$puntoLlegadaCodigoPais_ListName" />
								<xsl:with-param name="regexp"
									select="'^(Country)$'" />
								<xsl:with-param name="isError" select="false()" />
								<xsl:with-param name="descripcion"
									select="concat('Error en la linea: 17) ', position(), ' ', $puntoLlegadaCodigoPais_ListName)" />
							</xsl:call-template>
						</xsl:for-each>
					</xsl:for-each>
				</xsl:for-each>
			</xsl:for-each>
		</xsl:for-each>
		
		<!-- 63. Monto total incluido la percepción -->
		<xsl:variable name="countPaymentTermsPercepcion" select="count(cac:PaymentTerms/cbc:ID[text()='Percepcion'])" />		
		<xsl:if test="($tipoOperacion='2001')">
			<xsl:if test="($countPaymentTermsPercepcion = 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3309'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea ', $tipoOperacion, ' - ', $countPaymentTermsPercepcion)"/>
				</xsl:call-template>
			</xsl:if>				
		</xsl:if>
		
		<xsl:if test="not($tipoOperacion='2001')">
			<xsl:if test="($countPaymentTermsPercepcion &gt; 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3308'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea ', $tipoOperacion, ' - ', $countPaymentTermsPercepcion)"/>
				</xsl:call-template>
			</xsl:if>				
		</xsl:if>				

		<!-- 93. Código del bien o servicio sujeto a detracción -->		
		<xsl:variable name="countSujetoDetraccion" select="count(cac:PaymentTerms[cbc:ID='Detraccion']/cbc:PaymentMeansID)"/>		
		<xsl:if test="($tipoOperacion='1001' or $tipoOperacion='1002' or $tipoOperacion='1003' or $tipoOperacion='1004')">
			<xsl:if test="($countSujetoDetraccion = 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3127'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error countSujetoDetraccion: ', $countSujetoDetraccion)"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:if>

		<!-- Información Adicional - Detracciones -->
		<xsl:for-each select="cac:PaymentTerms">
			<!-- 63. Monto total incluido la percepción -->
			<xsl:variable name="indicadorPaymentTerms" select="./cbc:ID" />
			<!-- 93. Código del Bien o Servicio Sujeto a Detracción -->
			<xsl:variable name="codigoBienServicio" select="./cbc:PaymentMeansID" />
			<xsl:variable name="codigoBienServicio_schemeName" select="./cbc:PaymentMeansID/@schemeName" />
			<xsl:variable name="codigoBienServicio_schemeAgencyName" select="./cbc:PaymentMeansID/@schemeAgencyName" />
			<xsl:variable name="codigoBienServicio_schemeURI" select="./cbc:PaymentMeansID/@schemeURI" />
			<!-- 63. Monto total incluido la percepción -->
			<!-- 90. Monto y Porcentaje de la detracción -->
			<!-- 90. Monto de detraccion -->
			<xsl:variable name="montoDetraccion" select="./cbc:Amount" />
			<xsl:variable name="montoDetraccion_currency" select="./cbc:Amount/@currencyID" />

			<xsl:if test="not($tipoOperacion='1001' or $tipoOperacion='1002' or $tipoOperacion='1003' or $tipoOperacion='1004')">
				<xsl:if test="($indicadorPaymentTerms='Detraccion')">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3128'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $tipoOperacion, ' ', $indicadorPaymentTerms)"/>
					</xsl:call-template>				
				</xsl:if>
			</xsl:if>
			
			<!-- 63. Monto total incluido la percepción -->
			<xsl:if test="($indicadorPaymentTerms='Percepcion')">
				<xsl:call-template name="existValidateElementNotExist">
					<xsl:with-param name="errorCodeNotExist" select="'3310'"/>
					<xsl:with-param name="node" select="$montoDetraccion"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $indicadorPaymentTerms, ' ', $montoDetraccion)" />
				</xsl:call-template>	

				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'3311'" />
					<xsl:with-param name="node" select="$montoDetraccion" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $indicadorPaymentTerms, ' ', $montoDetraccion)" />
				</xsl:call-template>	
				
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2788'" />
					<xsl:with-param name="node" select="$montoDetraccion_currency" />
					<xsl:with-param name="regexp" select="'^(PEN)$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(),' ', $indicadorPaymentTerms, ' ', $montoDetraccion_currency)"/>
				</xsl:call-template>		
			</xsl:if>

			<!-- 88. Código de bien o servicio -->
			<xsl:if test="($indicadorPaymentTerms='Detraccion')">
				<xsl:call-template name="existValidateElementNotExist">
					<xsl:with-param name="errorCodeNotExist" select="'3127'" />
					<xsl:with-param name="node" select="$codigoBienServicio" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', $indicadorPaymentTerms, ' ', $codigoBienServicio)" />
				</xsl:call-template>
				
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'3033'" />
					<xsl:with-param name="node" select="$codigoBienServicio" />
					<xsl:with-param name="regexp"
						select="'^(001|002|003|004|005|007|008|009|010|011|012|013|014|015|016|017|019|020|021|022|023|024|025|026|027|028|030|031|032|034|035|036|037|039|040|041|099)$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', $indicadorPaymentTerms, ' ', $codigoBienServicio)" />
				</xsl:call-template>		
				
				<xsl:if
					test="$tipoOperacion='1002' and not($codigoBienServicio='004')">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3129'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 1) ', $indicadorPaymentTerms, ' ', $tipoOperacion, ' ', $codigoBienServicio)" />
					</xsl:call-template>
				</xsl:if>
	
				<xsl:if
					test="$tipoOperacion='1003' and not($codigoBienServicio='028')">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3129'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 2) ', $indicadorPaymentTerms, ' ', $tipoOperacion, ' ', $codigoBienServicio)" />
					</xsl:call-template>
				</xsl:if>
	
				<xsl:if test="$tipoOperacion='1004' and not($codigoBienServicio='027')">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3129'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 3) ', $indicadorPaymentTerms, ' ', $tipoOperacion, ' ', $codigoBienServicio)" />
					</xsl:call-template>
				</xsl:if>		
				
				<xsl:call-template name="existValidateElementNotExist">
					<xsl:with-param name="errorCodeNotExist" select="'3035'" />
					<xsl:with-param name="node" select="$montoDetraccion" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', $indicadorPaymentTerms, ' ', $montoDetraccion)" />
				</xsl:call-template>				

				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'3208'" />
					<xsl:with-param name="node" select="$montoDetraccion_currency" />
					<xsl:with-param name="regexp" select="'^(PEN)$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: 5) ', $indicadorPaymentTerms, ' ', $montoDetraccion_currency)" />
				</xsl:call-template>								

				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'3037'" />
					<xsl:with-param name="node" select="$montoDetraccion" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', position(), ' ', $montoDetraccion)" />
				</xsl:call-template>

				<xsl:if test="not($montoDetraccion &gt; 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3037'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $montoDetraccion)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>
			
			<xsl:if test="($indicadorPaymentTerms = 'Percepción')">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2788'" />
					<xsl:with-param name="node" select="$montoDetraccion_currency" />
					<xsl:with-param name="regexp" select="'^(PEN)$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ',  position(), ' ', $montoDetraccion_currency)" />
				</xsl:call-template>
			</xsl:if>
			
			<xsl:call-template 	name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4255'" />
				<xsl:with-param name="node" select="$codigoBienServicio_schemeName" />
				<xsl:with-param name="regexp" select="'^(Codigo de detraccion)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 15) ', $codigoBienServicio_schemeName)" />
			</xsl:call-template>

			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4256'" />
				<xsl:with-param name="node" select="$codigoBienServicio_schemeAgencyName" />
				<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 16) ', $codigoBienServicio_schemeAgencyName)" />
			</xsl:call-template>

			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4257'" />
				<xsl:with-param name="node" select="$codigoBienServicio_schemeURI" />
				<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo54)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 7) ', $codigoBienServicio_schemeURI)" />
			</xsl:call-template>
		</xsl:for-each>

		<!-- 78. Servicio de transporte: Forma de pago -->
		<xsl:variable name="countServicioTransporteFormaPago" select="count(cac:PaymentMeans/cbc:PaymentMeansCode)"/>
		<xsl:if test="($tipoOperacion='0302')">
			<xsl:if test="($countServicioTransporteFormaPago = 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3173'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ',  $tipoOperacion, ' ', $countServicioTransporteFormaPago)"/>
				</xsl:call-template>
			</xsl:if>				
		</xsl:if>
		
		<!-- 94. Número de cuenta en el Banco de la Nación -->	
		<xsl:variable name="countPaymentMeansID" select="count(cac:PaymentMeans/cbc:ID[text()='Detraccion'])"/>		
		<xsl:if test="($tipoOperacion='1001' or $tipoOperacion='1002' or $tipoOperacion='1003' or $tipoOperacion='1004')">
			<xsl:if test="($countPaymentMeansID = 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3034'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $countPaymentMeansID)"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:if>			

		<xsl:for-each select="cac:PaymentMeans">
			<!-- 94. Número de cuenta en el Banco de la Nación -->
			<!-- 94. Indicador PaymentMeans -->
			<xsl:variable name="indicadorPaymentMeans" select="./cbc:ID"/>
			<!-- 94. Número de cuenta -->
			<xsl:variable name="numeroCuentaBancoNacion" select="./cac:PayeeFinancialAccount"/>		
			<!-- 78. Servicio de transporte: Forma de pago -->
			<!-- 90. Número de cta. en el Banco de la Nación -->
			<!-- 90. Medio de pago -->
			<xsl:variable name="medioPagoBancoNacion" select="./cbc:PaymentMeansCode" />
			<xsl:variable name="medioPagoBancoNacion_listName" select="./cbc:PaymentMeansCode/@listName" />
			<xsl:variable name="medioPagoBancoNacion_listAgencyName" select="./cbc:PaymentMeansCode/@listAgencyName" />
			<xsl:variable name="medioPagoBancoNacion_listURI" select="./cbc:PaymentMeansCode/@listURI" />
			<!-- 79. Servicio de transporte: Número de autorización de la transacción 
				y el sistema de tarjeta de crédito y/o débito -->
			<xsl:variable name="servicioTransporteNúmeroAutorizacionTransaccion" select="./cbc:PaymentID" />

			<!-- 94. Número de cuenta -->
			<xsl:if test="($indicadorPaymentMeans='Detraccion')">
				<xsl:call-template name="existValidateElementNotExist">
					<xsl:with-param name="errorCodeNotExist" select="'3127'" />
					<xsl:with-param name="node" select="$numeroCuentaBancoNacion" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', $indicadorPaymentMeans, ' ', $numeroCuentaBancoNacion)" />
				</xsl:call-template>			
			</xsl:if>	

			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'3174'" />
				<xsl:with-param name="node" select="$medioPagoBancoNacion" />
				<xsl:with-param name="regexp" select="'^(001|002|003|004|005|006|007|008|009|010|011|012|013|101|102|103|104|105|106|107|108|999)$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ',  position(), ' ', $medioPagoBancoNacion)" />
			</xsl:call-template>

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'4252'" />
				<xsl:with-param name="node"
					select="$medioPagoBancoNacion_listName" />
				<xsl:with-param name="regexp"
					select="'^(Medio de pago)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 18) ', $medioPagoBancoNacion_listName)" />
			</xsl:call-template>

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'4251'" />
				<xsl:with-param name="node"
					select="$medioPagoBancoNacion_listAgencyName" />
				<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 18) ', $medioPagoBancoNacion_listAgencyName)" />
			</xsl:call-template>

			<xsl:call-template
				name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate"
					select="'4253'" />
				<xsl:with-param name="node"
					select="$medioPagoBancoNacion_listURI" />
				<xsl:with-param name="regexp"
					select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo59)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 10) ', $medioPagoBancoNacion_listURI)" />
			</xsl:call-template>

			<!-- 90. Número de cuenta -->
			<xsl:variable name="numeroCtaBancoNacion" select="./cac:PayeeFinancialAccount/cbc:ID" />

			<xsl:if test="($codigoBienServicioSujetoDetraccion)">
				<xsl:call-template name="existValidateElementNotExist">
					<xsl:with-param name="errorCodeNotExist" select="'3034'" />
					<xsl:with-param name="node" select="$numeroCtaBancoNacion" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ',  position(), ' ', $numeroCtaBancoNacion)" />
				</xsl:call-template>
			</xsl:if>

			<!-- 79. Servicio de transporte: Número de autorización de la transacción 
				y el sistema de tarjeta de crédito y/o débito -->
			<xsl:if test="($tipoOperacion='0302')">
				<xsl:call-template name="existValidateElementNotExist">
					<xsl:with-param name="errorCodeNotExist" select="'3175'" />
					<xsl:with-param name="node" select="$servicioTransporteNúmeroAutorizacionTransaccion" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ',  position(), ' ', $tipoOperacion, ' ', $servicioTransporteNúmeroAutorizacionTransaccion)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>
		<!-- 84. Fecha de Pago -->
		<xsl:if test="($tipoOperacion='0303')">
			<xsl:call-template
				name="existValidateElementNotExist">
				<xsl:with-param name="errorCodeNotExist"
					select="'3180'" />
				<xsl:with-param name="node"
					select="$fechaVencimiento" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ',  position(), ' ', $tipoOperacion, ' ', $fechaVencimiento)" />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
