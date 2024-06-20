<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet  version="2.0" 
	xmlns="urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2"
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
<!-- PRODUCCION  -->
<!--   <xsl:include href="util/validate_utils.xsl" dp:ignore-multiple="yes" /> -->
<!-- BETA  -->
  <xsl:include href="../util/validate_utils.xsl" dp:ignore-multiple="yes" />
<!-- FIN: AXTEROID -->	
	
	<!-- key Documentos Relacionados Duplicados -->
	<xsl:key name="by-invoiceLine-id" match="*[local-name()='CreditNote']/cac:CreditNoteLine" use="number(cbc:ID)" />	
	<xsl:key name="by-document-billing-reference" match="cac:BillingReference" use="concat(cac:InvoiceDocumentReference/cbc:DocumentTypeCode,' ', cac:InvoiceDocumentReference/cbc:ID)" />
	<xsl:key name="by-document-despatch-reference" match="cac:DespatchDocumentReference" use="concat(cbc:DocumentTypeCode,' ', cbc:ID)" />	
	<xsl:key name="by-document-additional-reference" match="cac:AdditionalDocumentReference" use="concat(cbc:DocumentTypeCode,' ', cbc:ID)" />
				
	<xsl:key name="by-PaymentTerms-PaymentMeansID" match="*[local-name()='Invoice']/cac:PaymentTerms[cbc:ID[text()='FormaPago']]" use="cbc:PaymentMeansID"/>
					
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
		<!-- 6. Código de tipo de nota de Credito -->
		<xsl:variable name="codigoTipoNotaCredito" select="cac:DiscrepancyResponse/cbc:ResponseCode"/>
		<!-- 8. Tipo de moneda -->
		<xsl:variable name="tipoMoneda" select="cbc:DocumentCurrencyCode"/>		
		<!-- 15. Tipo y número de documento de identidad del adquirente o usuario -->
		<xsl:variable name="tipoDocumentoIdentidadReceptorComprobante" select="cac:AccountingCustomerParty/cac:Party/cac:PartyIdentification/cbc:ID/@schemeID"/>				
		<!-- 19. Tipo de documento del documento que modifica -->
 		<xsl:variable name="countTipoDocumentoModifica" select="count(cac:BillingReference/cac:InvoiceDocumentReference/cbc:DocumentTypeCode)"/> 
		<xsl:variable name="tipoDocumentoModificaGlobal">
		<xsl:choose> 
			<xsl:when test="($countTipoDocumentoModifica = 0)">	
				<xsl:value-of select="00"/>			
			</xsl:when>	
			<xsl:otherwise>			
				<xsl:for-each select="cac:BillingReference">						
					<xsl:for-each select="./cac:InvoiceDocumentReference">		
							<xsl:value-of select="./cbc:DocumentTypeCode"/>
					</xsl:for-each>					
 				</xsl:for-each>	 
 			</xsl:otherwise>
		</xsl:choose>					
		</xsl:variable>		
		<xsl:variable name="tipoDocumentoDocumentoModificaGlobal" select="substring($tipoDocumentoModificaGlobal,1,2)"/>			  
		
		<!-- 30. Valor de venta por ítem -->
		<xsl:variable name="sumValorVentaItem1000Linea" select="sum(cac:CreditNoteLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:LineExtensionAmount)"/>	
		<xsl:variable name="sumValorVentaItem1016Linea" select="sum(cac:CreditNoteLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:LineExtensionAmount)"/>	
		<xsl:variable name="sumValorVentaItem9995Linea" select="sum(cac:CreditNoteLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='9995']/cbc:LineExtensionAmount)"/>
		<xsl:variable name="sumValorVentaItem9996Linea" select="sum(cac:CreditNoteLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='9996']/cbc:LineExtensionAmount)"/>	
		<xsl:variable name="sumValorVentaItem9997Linea" select="sum(cac:CreditNoteLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='9997']/cbc:LineExtensionAmount)"/>	
		<xsl:variable name="sumValorVentaItem9998Linea" select="sum(cac:CreditNoteLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='9998']/cbc:LineExtensionAmount)"/>	

		<!-- 33. Código de precio -->
		<xsl:variable name="countCodigoPrecio01Global" select="count(cac:CreditNoteLine/cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='01'])"/>
		<xsl:variable name="countCodigoPrecio02Global" select="count(cac:CreditNoteLine/cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='02'])"/>				

		<!-- 32. Afectación al IGV por la línea -->
		<!-- 32. Afectación IVAP por la línea -->
		<!-- 33. Afectación del ISC por la línea -->
		<!-- 33. Afectacion Otros Tributos -->
		<!-- 34. Monto base -->
		<xsl:variable name="sumMontoBaseGlobal" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal/cbc:TaxableAmount)"/>		
		<!-- 34. Monto base por Código de tributo por línea-->
		<xsl:variable name="sumMontoBase1000Linea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxableAmount)"/>		
		<xsl:variable name="sumMontoBase1016Linea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumMontoBase2000Linea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumMontoBase9995Linea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9995']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumMontoBase9996Linea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9996']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumMontoBase9997Linea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9997']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumMontoBase9998Linea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9998']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumMontoBase9999Linea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxableAmount)"/>

		<xsl:variable name="sumMontoBase2000_9996Linea">	
			<xsl:choose>
				<xsl:when test="($sumMontoBase2000Linea &gt; 0)">					
					<xsl:for-each select="cac:CreditNoteLine/cac:TaxTotal">
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

		<!-- 34. Monto de IGV/IVAP de la línea -->
		<!-- 35. Monto del tributo de la línea -->
		<xsl:variable name="sumMontoTributo1000Linea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo1016Linea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo2000Linea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo9995Linea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9995']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo9996Linea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9996']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo9997Linea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9997']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo9998Linea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9998']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo9999Linea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo7152Linea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='7152']/cbc:TaxAmount )"/>

		<xsl:variable name="sumMontoTributo2000_9996Linea">	
			<xsl:choose>
				<xsl:when test="($sumMontoTributo2000Linea &gt; 0)">					
					<xsl:for-each select="cac:CreditNoteLine/cac:TaxTotal">
						<xsl:variable name="count9996" select="count(./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9996'])"/>
				 		<xsl:if test="($count9996 &gt; 0)"> 
							<xsl:value-of select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000']]/cbc:TaxAmount)"/>
						</xsl:if>	
						<xsl:if test="not($count9996 &gt; 0)">
							<xsl:value-of select="0"/>
						</xsl:if>										
					</xsl:for-each>										
				</xsl:when>							
				<xsl:otherwise>0</xsl:otherwise>																				
			</xsl:choose>  
		</xsl:variable>		
		
		<xsl:variable name="sumMontoTributo2000sin9996Linea" select="number($sumMontoTributo2000Linea) - number($sumMontoTributo2000_9996Linea)"/>

		<!-- 37. Monto de cargo/descuento -->
		<xsl:variable name="sumMontoCargoDescuento00Linea" select="sum(cac:CreditNoteLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='00']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento01Linea" select="sum(cac:CreditNoteLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='01']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento02Linea" select="sum(cac:CreditNoteLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='02']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento03Linea" select="sum(cac:CreditNoteLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='03']/cbc:Amount)"/>	
		<xsl:variable name="sumMontoCargoDescuento04Linea" select="sum(cac:CreditNoteLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='04']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento05Linea" select="sum(cac:CreditNoteLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='05']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento06Linea" select="sum(cac:CreditNoteLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='06']/cbc:Amount)"/>			
		<xsl:variable name="sumMontoCargoDescuento47Linea" select="sum(cac:CreditNoteLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='47']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento48Linea" select="sum(cac:CreditNoteLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='48']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento49Linea" select="sum(cac:CreditNoteLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='49']/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargoDescuento50Linea" select="sum(cac:CreditNoteLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='50']/cbc:Amount)"/>	
		<xsl:variable name="sumMontoCargoDescuento51Linea" select="sum(cac:CreditNoteLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='51']/cbc:Amount)"/>	
		<xsl:variable name="sumMontoCargoDescuento52Linea" select="sum(cac:CreditNoteLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='52']/cbc:Amount)"/>	
		<xsl:variable name="sumMontoCargoDescuento53Linea" select="sum(cac:CreditNoteLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='53']/cbc:Amount)"/>	
						
		<!-- 39. Count Total valor de venta -->		
		<xsl:variable name="countCodigoTributo1000Global" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])" />
		<xsl:variable name="countCodigoTributo1016Global" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1016'])" />
		<xsl:variable name="countCodigoTributo2000Global" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000'])" />
		<xsl:variable name="countCodigoTributo9995Global" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9995'])" />
		<xsl:variable name="countCodigoTributo9996Global" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9996'])" />
		<xsl:variable name="countCodigoTributo9997Global" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9997'])" />
		<xsl:variable name="countCodigoTributo9998Global" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9998'])" />
		<xsl:variable name="countCodigoTributo9999Global" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9999'])" />	
		<xsl:variable name="countCodigoTributo7152Global" select="count(cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='7152'])" />	
		
		<xsl:variable name="sumMontoTributo1000Global" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo1016Global" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo2000Global" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo9995Global" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9995']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo9996Global" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9996']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo9997Global" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9997']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo9998Global" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9998']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo9999Global" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxAmount )"/>
		<xsl:variable name="sumMontoTributo7152Global" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='7152']/cbc:TaxAmount)"/>
		
		<!-- 38. Monto total de impuestos -->
		<xsl:variable name="sumMontoTotalImpuestosGlobal" select="sum(cac:TaxTotal/cbc:TaxAmount)"/>
  		
		<xsl:variable name="sumTotalValor1000Global" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumTotalValor1016Global" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumTotalValor2000Global" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumTotalValor9995Global" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9995']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumTotalValor9996Global" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9996']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumTotalValor9997Global" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9997']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumTotalValor9998Global" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9998']/cbc:TaxableAmount)"/>
		<xsl:variable name="sumTotalValor9999Global" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxableAmount)"/>

		<!-- Validaciones -->		
		<!-- 1. Version del UBL -->
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'2075'"/>
			<xsl:with-param name="node" select="$cbcUBLVersionID"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $cbcUBLVersionID)"/>
		</xsl:call-template>		
		
		<xsl:call-template name="regexpValidateElementIfExist">
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
			<xsl:with-param name="errorCodeValidate" select="'4256'" />
			<xsl:with-param name="node" select="$cbcCustomizationID_SchemeAgencyName" />
			<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cbcCustomizationID_SchemeAgencyName)"/>
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
			<xsl:with-param name="regexp" select="'^[B][A-Z0-9]{3}-[0-9]{1,8}$|^[F][A-Z0-9]{3}-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cbcID)"/>
		</xsl:call-template>
					
		<!-- 4. Fecha de emisión = SUNAT 01/04/2021 -->
		<xsl:variable name="t1" select="xs:date($currentdate)-xs:date($cbcIssueDate)" />
		<xsl:variable name="t2" select="fn:days-from-duration(xs:duration($t1))" />						
		<xsl:variable name="t3" select="xs:date($cbcIssueDate)-xs:date('2021-01-31')" />
		<xsl:variable name="t4" select="fn:days-from-duration(xs:duration($t3))" />
		<xsl:variable name="t5" select="xs:date($cbcIssueDate)-xs:date('2021-12-31')" />				
		<xsl:variable name="t6" select="fn:days-from-duration(xs:duration($t5))" />
		<xsl:variable name="t7" select="xs:date($cbcIssueDate)-xs:date('2021-12-16')" />				
		<xsl:variable name="t8" select="fn:days-from-duration(xs:duration($t7))" />		
		<xsl:variable name="t9" select="xs:date($cbcIssueDate)-xs:date('2022-12-31')" />				
		<xsl:variable name="t10" select="fn:days-from-duration(xs:duration($t9))" />		
		<xsl:variable name="s1C" select="substring($cbcID, 1, 1)" />
		
	|	<xsl:variable name="fasefechaEmision">	
			<xsl:choose>
				<xsl:when test="$t4 &lt; 1">
					<xsl:value-of select="1"/>
				</xsl:when>	
				<xsl:when test="(($t4 &gt; 0) and ($t6 &lt; 1))">
					<xsl:value-of select="2"/>
				</xsl:when>	
				<xsl:when test="$t6 &gt; 0">
					<xsl:value-of select="3"/>
				</xsl:when>				    										    										    										    						
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>       
		</xsl:variable>			

		<xsl:variable name="fasefechaEmision2018">	
			<xsl:choose>	
<!-- 				<xsl:when test="(($t8 &gt; 0) and ($t6 &lt; 1) )"> -->
<!-- 					<xsl:value-of select="1"/> -->
<!-- 				</xsl:when>		 -->
<!-- 				<xsl:when test="($t6 &gt; 0)"> -->
<!-- 					<xsl:value-of select="2"/> -->
<!-- 				</xsl:when>		 -->
				<xsl:when test="(($t8 &gt; 0) and ($t10 &lt; 1) )">
					<xsl:value-of select="1"/>
				</xsl:when>							
				<xsl:when test="($t10 &gt; 0)">
					<xsl:value-of select="2"/>
				</xsl:when>					    										    										    										    						
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>       
		</xsl:variable>	
				
		<xsl:if test="not($s1C = 'B') and not(($s1C = '0') or ($s1C = '1') or ($s1C = '2') or 
					($s1C = '3') or ($s1C = '4') or ($s1C = '5') or ($s1C = '6') or ($s1C = '7') or 
					($s1C = '8') or ($s1C = '9'))">			 				
			<xsl:if test="($fasefechaEmision2018 = 0) ">
				<xsl:if test="($t2 &gt; 7)">	
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2108'" />
						<xsl:with-param name="errorMessage" 
							select="concat('Error ', $fasefechaEmision2018, ') ', $cbcIssueDate, ' - ', $currentdate, ' - ', $t1, ' - ', $t2, ' - ', $t4, ' - ', $t6, ' - ', $t8, ' - ', $t10)" />
					</xsl:call-template>
				</xsl:if>	
			</xsl:if>

			<xsl:if test="($fasefechaEmision2018 = 1)">
				<xsl:if test="($t2 &gt; 3)">	
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2108'" />
						<xsl:with-param name="errorMessage" 
							select="concat('Error ', $fasefechaEmision2018, ') ', $cbcIssueDate, ' - ', $currentdate, ' - ', $t1, ' - ', $t2, ' - ', $t4, ' - ', $t6, ' - ', $t8, ' - ', $t10)" />
					</xsl:call-template>
				</xsl:if>							
			</xsl:if>		
						
			<xsl:if test="($fasefechaEmision2018 = 2)">
				<xsl:if test="($t2 &gt; 1)">	
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2108'" />
						<xsl:with-param name="errorMessage" 
							select="concat('Error ', $fasefechaEmision2018, ') ', $cbcIssueDate, ' - ', $currentdate, ' - ', $t1, ' - ', $t2, ' - ', $t3, ' - ', $t4, ' - ', $t5, ' - ', $t6, ' - ', $t8, ' - ', $t10)" />
					</xsl:call-template>
				</xsl:if>	
			</xsl:if>			
		</xsl:if>			
		
		<xsl:if test="($s1C = 'B')">	
			<xsl:if test="($t2 &gt; 5)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'1079'" />
					<xsl:with-param name="errorMessage" 
						select="concat('Error en la linea ', $t4, ' ', $t6, ' : ', $s1C, ' - ', $cbcIssueDate, ' - ', $currentdate, ' - ', $t1, ' - ', $t2)" />
				</xsl:call-template>
			</xsl:if>						
		</xsl:if>
															
		<xsl:if test="($t2 &lt; -2)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2329'" />
				<xsl:with-param name="errorMessage" 
					select="concat('Error en la linea: ', $cbcIssueDate, ' - ', $currentdate, ' - ', $t1, ' - ', $t2)" />
			</xsl:call-template>
		</xsl:if>
	
		<!-- 6. Código de tipo de nota de débito -->
		<xsl:variable name="countCodigoTipoNotaCredito" select="count($codigoTipoNotaCredito)" />
		<xsl:if test="($countCodigoTipoNotaCredito = 0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2128'" />
				<xsl:with-param name="errorMessage" 
					select="concat('Error en la linea: ', $countCodigoTipoNotaCredito)" />
			</xsl:call-template>
		</xsl:if>		
		
		<xsl:if test="($countCodigoTipoNotaCredito &gt; 1)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3203'" />
				<xsl:with-param name="errorMessage" 
					select="concat('Error en la linea: ', $countCodigoTipoNotaCredito)" />
			</xsl:call-template>
		</xsl:if>
						
		<xsl:for-each select="cac:DiscrepancyResponse">	
			<!-- 6. Código de tipo de nota de débito -->	
			<xsl:variable name="codigoTipoNotaDebito_ListAgencyName" select="./cbc:ResponseCode/@listAgencyName"/>	
			<xsl:variable name="codigoTipoNotaDebito_ListName" select="./cbc:ResponseCode/@listName"/>
			<xsl:variable name="codigoTipoNotaDebito_ListURI" select="./cbc:ResponseCode/@listURI"/>
			<!-- 7. Motivo o Sustento -->	
			<xsl:variable name="motivoSustento" select="./cbc:Description"/>			
			
			<!-- 6. Código de tipo de nota de débito -->
			<xsl:call-template name="existValidateElementNotExist">
				<xsl:with-param name="errorCodeNotExist" select="'2128'"/>
				<xsl:with-param name="node" select="$codigoTipoNotaCredito"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $codigoTipoNotaCredito)"/>
			</xsl:call-template>
						
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2172'" />
				<xsl:with-param name="node" select="$codigoTipoNotaCredito" />
				<xsl:with-param name="regexp" select="'^(01|02|03|04|05|06|07|08|09|10|11|12|13)$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $codigoTipoNotaCredito)"/>
			</xsl:call-template>
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4251'" />
				<xsl:with-param name="node" select="$codigoTipoNotaDebito_ListAgencyName" />
				<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: 1) ', $codigoTipoNotaDebito_ListAgencyName)"/>
			</xsl:call-template>		
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4252'" />
				<xsl:with-param name="node" select="$codigoTipoNotaDebito_ListName" />
				<xsl:with-param name="regexp" select="'^(Tipo de nota de credito)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: 1) ', $codigoTipoNotaDebito_ListName)"/>
			</xsl:call-template>
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4253'" />
				<xsl:with-param name="node" select="$codigoTipoNotaDebito_ListURI" />
				<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo09)$'" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: 1) ', $codigoTipoNotaDebito_ListURI)"/>
			</xsl:call-template>			
			
			<!-- 7. Motivo o Sustento -->	
			<xsl:call-template name="existValidateElementNotExist">
				<xsl:with-param name="errorCodeNotExist" select="'2136'"/>
				<xsl:with-param name="node" select="$motivoSustento"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $motivoSustento)"/>
			</xsl:call-template>
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2135'" />
				<xsl:with-param name="node" select="$motivoSustento" />
				<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{1,499}$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $motivoSustento)"/>
			</xsl:call-template>				
		</xsl:for-each>	
		
		<!-- 8. Tipo de moneda -->
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'2070'"/>
			<xsl:with-param name="node" select="$tipoMoneda"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoMoneda)"/>
		</xsl:call-template>

		<!-- Datos del Emisor -->														
		<xsl:for-each select="cac:AccountingSupplierParty">								
			<xsl:for-each select="./cac:Party">	
				<xsl:for-each select="./cac:PartyIdentification">
					<!-- 10. Número de RUC -->
					<xsl:variable name="emisorNumeroDocumento" select="./cbc:ID"/>	
					<!-- 10. Tipo de documento de identidad del emisor -->
					<xsl:variable name="emisorTipoDocumento" select="./cbc:ID/@schemeID"/>	
					<xsl:variable name="emisorNumeroDocumento_SchemeName" select="./cbc:ID/@schemeName"/>
					<xsl:variable name="emisorNumeroDocumento_SchemeAgencyName" select="./cbc:ID/@schemeAgencyName"/>
					<xsl:variable name="emisorNumeroDocumento_SchemeURI" select="./cbc:ID/@schemeURI"/>		
			
					<!-- 10. Número de RUC -->				
					<xsl:if test="not($numeroRuc = $emisorNumeroDocumento)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'1034'" />
							<xsl:with-param name="errorMessage"
								select="concat('ruc del xml diferente al nombre del archivo ', $emisorNumeroDocumento, ' diff ', $numeroRuc)" />
						</xsl:call-template>
					</xsl:if>	
					
					<!-- 10. Tipo de documento de identidad del emisor - RUC -->		
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'3029'"/>
						<xsl:with-param name="node" select="$emisorTipoDocumento"/>
						<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $emisorTipoDocumento)"/>
					</xsl:call-template>					
							
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2511'" />
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
							select="concat('Error en la linea: 1) ', position(), ' ', $emisorNumeroDocumento_SchemeName)"/>
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
						<xsl:with-param name="errorCodeValidate" select="'4257'" />
						<xsl:with-param name="node" select="$emisorNumeroDocumento_SchemeURI" />
						<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo06)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: 1) ', position(), ' ', $emisorNumeroDocumento_SchemeURI)"/>
					</xsl:call-template>				
				</xsl:for-each>
								
				<xsl:for-each select="./cac:PartyName">
					<!-- 11. Nombre Comercial -->
					<xsl:variable name="emisorNombreComercial" select="./cbc:Name"/>	
									
					<!-- 11. Nombre Comercial -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4092'"/>
						<xsl:with-param name="node" select="$emisorNombreComercial"/>
<!-- 						<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,1499}$'"/> -->
						<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,1499}$'"/>
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $emisorNombreComercial)"/>
					</xsl:call-template>								
				</xsl:for-each>	
				
				<!-- 16. Código asignado por SUNAT para el establecimiento anexo declarado en el RUC -->	
				<xsl:variable name="countCodigoAsignadoSUNAT" select="count(cac:PartyLegalEntity/cac:RegistrationAddress/cbc:AddressTypeCode)" />			
				<xsl:if test="($countCodigoAsignadoSUNAT = 0)"> 					
					<xsl:choose>
						<xsl:when test="(($s1C = 'F') and ($tipoDocumentoDocumentoModificaGlobal = '01'))">				
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3030'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $s1C, ' ', $countCodigoAsignadoSUNAT)"/>
							</xsl:call-template>							
						</xsl:when>			
						<xsl:otherwise>		
							<xsl:call-template name="addWarning">
								<xsl:with-param name="warningCode" select="'4198'" />
								<xsl:with-param name="warningMessage"
									select="concat('Error en la linea: ', position(), ' ', $s1C, ' ', $countCodigoAsignadoSUNAT)"/>
							</xsl:call-template>																				
						</xsl:otherwise>
					</xsl:choose>							
				</xsl:if>				
					
				<xsl:for-each select="./cac:PartyLegalEntity">
					<!-- 12. Apellidos y nombres, denominación o razón social -->
					<xsl:variable name="emisorRazonSocial" select="./cbc:RegistrationName"/>

					<!-- 12. Apellidos y nombres o denominacion o razon social Emisor -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'1037'" />
						<xsl:with-param name="node" select="$emisorRazonSocial" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $emisorRazonSocial)"/>
					</xsl:call-template>
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4338'" />
						<xsl:with-param name="node" select="$emisorRazonSocial"/>
						<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,1499}$'"/>
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea 1: ', position(), ' ', $emisorRazonSocial)"/>
					</xsl:call-template>
								
					<!-- 13. Domicilio Fiscal -->					
					<xsl:for-each select="./cac:RegistrationAddress">						
						<xsl:for-each select="./cac:AddressLine">
							<!-- 13. Dirección completa y detallada -->
							<xsl:variable name="emisorDireccionCompletaItem" select="./cbc:Line"/>
							
							<!-- 13. Domicilio Fiscal -->
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4094'"/>
								<xsl:with-param name="node" select="$emisorDireccionCompletaItem"/>
								<!-- <xsl:with-param name="regexp" select="'^[\w\s].{2,200}$'"/> -->
								<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,199}$'"/>
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $emisorDireccionCompletaItem)"/>
							</xsl:call-template>														
						</xsl:for-each>	
						
						<!-- 13. Urbanización -->
						<xsl:variable name="emisorUrbanizacion" select="./cbc:CitySubdivisionName"/>
						<!-- 13. Provincia -->
						<xsl:variable name="emisorProvincia" select="./cbc:CityName"/>
						<!-- 13. Código de ubigeo -->
						<xsl:variable name="emisorUbigeo" select="./cbc:ID"/>
						<xsl:variable name="emisorUbigeo_SchemeAgencyName" select="./cbc:ID/@schemeAgencyName"/>
						<xsl:variable name="emisorUbigeo_SchemeName" select="./cbc:ID/@schemeName"/>
						<!-- 13. Departamento -->
						<xsl:variable name="emisorDepartamento" select="./cbc:CountrySubentity"/>
						<!-- 13. Distrito -->
						<xsl:variable name="emisorDistrito" select="./cbc:District"/>
						<!-- 16. Código asignado por SUNAT para el establecimiento anexo declarado en el RUC -->
						<!-- 16. Código de país -->
						<xsl:variable name="emisorAnexoCodigoPais" select="./cbc:AddressTypeCode"/>
						<xsl:variable name="emisorAnexoCodigoPais_ListAgencyName" select="./cbc:AddressTypeCode/@listAgencyName"/>
						<xsl:variable name="emisorAnexoCodigoPais_ListName" select="./cbc:AddressTypeCode/@listName"/>	
		
						<!-- 13. Urbanización -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4095'"/>
							<xsl:with-param name="node" select="$emisorUrbanizacion"/>
							<!-- <xsl:with-param name="regexp" select="'^[\w\s].{1,25}$'"/> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,24}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $emisorUrbanizacion)"/>
						</xsl:call-template>
						
						<!-- 13. Provincia -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4096'"/>
							<xsl:with-param name="node" select="$emisorProvincia"/>
							<!-- <xsl:with-param name="regexp" select="'^[\w\s].{1,30}$'"/> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,29}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $emisorProvincia)"/>
						</xsl:call-template>
				
						<!-- 13. Código de ubigeo -->
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
								select="concat('Error en la linea: 2) ', position(), ' ', $emisorUbigeo_SchemeName)"/>
						</xsl:call-template>		
						
						<!-- 13. Departamento -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4097'"/>
							<xsl:with-param name="node" select="$emisorDepartamento"/>
							<!-- <xsl:with-param name="regexp" select="'^[\w\s].{1,30}$'"/> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,29}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $emisorDepartamento)"/>
						</xsl:call-template>
				
						<!-- 13. Distrito -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4098'"/>
							<xsl:with-param name="node" select="$emisorDistrito"/>
							<!-- <xsl:with-param name="regexp" select="'^[\w\s].{1,30}$'"/> -->
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,29}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $emisorDistrito)"/>
						</xsl:call-template>						
						
						<!-- 13. Código de país -->
						<xsl:for-each select="./cac:Country">
							<xsl:variable name="emisorCodigoPais" select="./cbc:IdentificationCode"/>
							<xsl:variable name="emisorCodigoPais_ListID" select="./cbc:IdentificationCode/@listID"/>
							<xsl:variable name="emisorCodigoPais_ListAgencyName" select="./cbc:IdentificationCode/@listAgencyName"/>
							<xsl:variable name="emisorCodigoPais_ListName" select="./cbc:IdentificationCode/@listName"/>				
				
							<!-- 13. Código de país -->
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
									select="concat('Error en la linea: 1) ', position(), ' ', $emisorCodigoPais_ListID)"/>
							</xsl:call-template>			
							
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4251'" />
								<xsl:with-param name="node" select="$emisorCodigoPais_ListAgencyName" />
								<xsl:with-param name="regexp" select="'^(United Nations Economic Commission for Europe)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: 2) ', position(), ' ', $emisorCodigoPais_ListAgencyName)"/>
							</xsl:call-template>	
							
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4252'" />
								<xsl:with-param name="node" select="$emisorCodigoPais_ListName" />
								<xsl:with-param name="regexp" select="'^(Country)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: 2) ', position(), ' ', $emisorCodigoPais_ListName)"/>
							</xsl:call-template>				
						</xsl:for-each>
						
						<!-- 16. Código asignado por SUNAT para el establecimiento anexo declarado en el RUC -->
						<xsl:choose>
							<xsl:when test="(($s1C = 'F') and ($tipoDocumentoDocumentoModificaGlobal = '01'))">								
								<xsl:call-template name="existValidateElementNotExist">
									<xsl:with-param name="errorCodeNotExist" select="'3030'"/>
									<xsl:with-param name="node" select="$emisorAnexoCodigoPais"/>
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', position(), ' ', $emisorAnexoCodigoPais)"/>
								</xsl:call-template>
							</xsl:when>			
							<xsl:otherwise>		
								<xsl:call-template name="existValidateElementNotExist">
									<xsl:with-param name="errorCodeNotExist" select="'4198'"/>
									<xsl:with-param name="node" select="$emisorAnexoCodigoPais"/>
									<xsl:with-param name="isError" select="false()"/>
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', position(), ' ', $emisorAnexoCodigoPais)"/>
								</xsl:call-template>								
							</xsl:otherwise>
						</xsl:choose>
						
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4242'" />
							<xsl:with-param name="node" select="$emisorAnexoCodigoPais" />
							<xsl:with-param name="regexp" select="'^[\d]{4}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $emisorAnexoCodigoPais)"/>
						</xsl:call-template>
						
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4251'" />
							<xsl:with-param name="node" select="$emisorAnexoCodigoPais_ListAgencyName" />
							<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 3) ', position(), ' ', $emisorAnexoCodigoPais_ListAgencyName)"/>
						</xsl:call-template>	
						
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4252'" />
							<xsl:with-param name="node" select="$emisorAnexoCodigoPais_ListName" />
							<xsl:with-param name="regexp" select="'^(Establecimientos anexos)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 3) ', position(), ' ', $emisorAnexoCodigoPais_ListName)"/>
						</xsl:call-template>						
					</xsl:for-each>						
				</xsl:for-each>		
			</xsl:for-each>
		</xsl:for-each>			
		
		<xsl:variable name="countClientePartyIdentificationID" select="count(cac:AccountingCustomerParty/cac:Party/cac:PartyIdentification/cbc:ID)"/>
		<xsl:if test="$countClientePartyIdentificationID = 0">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2679'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countClientePartyIdentificationID)"/>
			</xsl:call-template>
		</xsl:if>	
					
		<xsl:variable name="countClientePartyIdentificationSchemeID" select="count(cac:AccountingCustomerParty/cac:Party/cac:PartyIdentification/cbc:ID/@schemeID)"/>
		<xsl:if test="$countClientePartyIdentificationSchemeID = 0">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2679'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countClientePartyIdentificationSchemeID)"/>
			</xsl:call-template>
		</xsl:if>			
				
		<!-- Datos del cliente o receptor -->								
		<xsl:for-each select="cac:AccountingCustomerParty">
			<!-- Datos del Cliente o receptor -->			
			<xsl:for-each select="./cac:Party">
			
				<xsl:for-each select="./cac:PartyIdentification">
					<!-- 17. Número de documento de identidad del adquirente o usuario -->
					<xsl:variable name="clienteNumeroDocumento" select="./cbc:ID"/>	
					<!-- 17. Tipo de documento de identidad del adquirente o usuario -->
					<xsl:variable name="clienteTipoDocumento" select="./cbc:ID/@schemeID"/>
					<xsl:variable name="clienteNumeroDocumento_SchemeName" select="./cbc:ID/@schemeName"/>	
					<xsl:variable name="clienteNumeroDocumento_SchemeAgencyName" select="./cbc:ID/@schemeAgencyName"/>	
					<xsl:variable name="clienteNumeroDocumento_SchemeURI" select="./cbc:ID/@schemeURI"/>	

					<!-- 17. Tipo de documento de identidad del adquirente o usuario -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'2679'"/>
						<xsl:with-param name="node" select="$clienteTipoDocumento"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $clienteTipoDocumento)"/>
					</xsl:call-template>
					
					<!-- 17. Número de documento de identidad del adquirente o usuario -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'2679'"/>
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
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2016'" />
						<xsl:with-param name="node" select="$clienteTipoDocumento" />
						<xsl:with-param name="regexp" select="'^(0|1|4|6|7|A|B|C|D|E|F|G|-)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $clienteTipoDocumento)"/>
					</xsl:call-template>					
					
					<!-- 17. Número de documento de identidad del adquirente o usuario -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4255'" />
						<xsl:with-param name="node" select="$clienteNumeroDocumento_SchemeName" />
						<xsl:with-param name="regexp" select="'^(Documento de Identidad)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: 3) ', position(), ' ', $clienteNumeroDocumento_SchemeName)"/>
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
							select="concat('Error en la linea: 2) ', position(), ' ', $clienteNumeroDocumento_SchemeURI)"/>
					</xsl:call-template>													
				</xsl:for-each>

				<!-- <xsl:for-each select="./cac:PartyLegalEntity"> -->
					<!-- 18. Apellidos y nombres, denominación o razón social del adquirente o usuario -->
					<xsl:variable name="clienteRazonSocial" select="./cac:PartyLegalEntity/cbc:RegistrationName"/>		
					
					<!-- 18. Apellidos y nombres, denominación o razón social del adquirente o usuario -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'2021'"/>
						<xsl:with-param name="node" select="$clienteRazonSocial"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $clienteRazonSocial)"/>
					</xsl:call-template>
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2022'" />
						<xsl:with-param name="node" select="$clienteRazonSocial" />
						<!-- <xsl:with-param name="regexp" select="'^[^\n].{2,1500}$'" /> -->
						<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,999}$'"/>
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $clienteRazonSocial)"/>
					</xsl:call-template>

				<!-- </xsl:for-each> -->	
			</xsl:for-each>				
		</xsl:for-each>
		
		<xsl:variable name="countInvoiceDocumentReference" select="count(cac:BillingReference/cac:InvoiceDocumentReference)" />
		<xsl:if test="not($codigoTipoNotaCredito='10') and ($countInvoiceDocumentReference = 0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2524'" />
				<xsl:with-param name="errorMessage" 
					select="concat('Error en la linea: ', $codigoTipoNotaCredito, ' ', $countInvoiceDocumentReference)" />
			</xsl:call-template>
		</xsl:if>
				
		<xsl:if test="($codigoTipoNotaCredito='11') and ($countInvoiceDocumentReference &gt; 1)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3194'" />
				<xsl:with-param name="errorMessage" 
					select="concat('Error en la linea: ',$codigoTipoNotaCredito, ' ', $countInvoiceDocumentReference)" />
			</xsl:call-template>
		</xsl:if>
		
		<xsl:if test="($codigoTipoNotaCredito='13')">
			<xsl:variable name="countInvoiceDocumentReference" select="count(cac:BillingReference/cac:InvoiceDocumentReference)" />		
			<xsl:if test="($countInvoiceDocumentReference &gt; 1)">									
				<xsl:if test="($fasefechaEmision = 0) or ($fasefechaEmision = 2) or ($fasefechaEmision = 3)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3261'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error countInvoiceDocumentReference: ', $countInvoiceDocumentReference)"/>
					</xsl:call-template>					
				</xsl:if>	
			</xsl:if>		
		</xsl:if>
			
		<!-- Datos del documento que se modifica  -->	
		<xsl:for-each select="cac:BillingReference">		
			<xsl:for-each select="./cac:InvoiceDocumentReference">
				<!-- 18. Serie y número del documento que modifica -->	
				<xsl:variable name="serieNumeroDocumentoModifica" select="./cbc:ID"/>
				<xsl:variable name="sNDM1C" select="substring($serieNumeroDocumentoModifica, 1, 1)" />			
				<!-- 19. Tipo de documento del documento que modifica -->	
				<xsl:variable name="tipoDocumentoDelDocumentoModifica" select="./cbc:DocumentTypeCode"/>
				<xsl:variable name="tipoDocumentoDelDocumentoModifica_ListAgencyName" select="./cbc:DocumentTypeCode/@listAgencyName"/>			
			    <xsl:variable name="tipoDocumentoDelDocumentoModifica_ListName" select="./cbc:DocumentTypeCode/@listName"/>
			    <xsl:variable name="tipoDocumentoDelDocumentoModifica_ListURI" select="./cbc:DocumentTypeCode/@listURI"/>			    
			    
				<!-- 18. Serie y número del documento que modifica -->				
				<xsl:if test="($tipoDocumentoDelDocumentoModifica='01')">
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2117'"/>
						<xsl:with-param name="node" select="$serieNumeroDocumentoModifica"/>
						<xsl:with-param name="regexp" select="'^[F][A-Z0-9]{3}-[0-9]{1,8}$|^(E001)-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea 2: ', $tipoDocumentoDelDocumentoModifica,' ', $serieNumeroDocumentoModifica)"/>
					</xsl:call-template>
				</xsl:if>				
				
				<xsl:if test="($tipoDocumentoDelDocumentoModifica='03')">
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2117'"/>
						<xsl:with-param name="node" select="$serieNumeroDocumentoModifica"/>
						<xsl:with-param name="regexp" select="'^[B][A-Z0-9]{3}-[0-9]{1,8}$|^(EB01)-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea 3: ', $tipoDocumentoDelDocumentoModifica,' ', $serieNumeroDocumentoModifica)"/>
					</xsl:call-template>
				</xsl:if>
								
				<xsl:if test="($tipoDocumentoDelDocumentoModifica='05') or ($tipoDocumentoDelDocumentoModifica='06') or ($tipoDocumentoDelDocumentoModifica='12') or 
 						($tipoDocumentoDelDocumentoModifica='13') or ($tipoDocumentoDelDocumentoModifica='15') or ($tipoDocumentoDelDocumentoModifica='16') or   
 						($tipoDocumentoDelDocumentoModifica='18') or ($tipoDocumentoDelDocumentoModifica='21') or ($tipoDocumentoDelDocumentoModifica='28') or  
  						($tipoDocumentoDelDocumentoModifica='30') or ($tipoDocumentoDelDocumentoModifica='34') or ($tipoDocumentoDelDocumentoModifica='37') or  
  						($tipoDocumentoDelDocumentoModifica='42') or ($tipoDocumentoDelDocumentoModifica='43') or ($tipoDocumentoDelDocumentoModifica='45') or   
  						($tipoDocumentoDelDocumentoModifica='55') or ($tipoDocumentoDelDocumentoModifica='11') or ($tipoDocumentoDelDocumentoModifica='17') or  
  						($tipoDocumentoDelDocumentoModifica='23') or ($tipoDocumentoDelDocumentoModifica='24') or ($tipoDocumentoDelDocumentoModifica='56')">  
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2117'"/>
						<xsl:with-param name="node" select="$serieNumeroDocumentoModifica"/>
						<xsl:with-param name="regexp" select="'^[a-zA-Z0-9-]{1,20}-[a-zA-Z0-9-]{1,20}$|^$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea 7: ', $tipoDocumentoDelDocumentoModifica,' ', $serieNumeroDocumentoModifica)"/>
					</xsl:call-template>
				</xsl:if>		

				<xsl:if test="($tipoDocumentoDelDocumentoModifica = '') or ($tipoDocumentoDelDocumentoModifica = '-')">
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2117'"/>
						<xsl:with-param name="node" select="$serieNumeroDocumentoModifica"/>
						<xsl:with-param name="regexp" select="'^[a-zA-Z0-9-]{1,20}-[a-zA-Z0-9-]{1,20}$|^$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea 3: ', $tipoDocumentoDelDocumentoModifica,' ', $serieNumeroDocumentoModifica)"/>
					</xsl:call-template>
				</xsl:if>		
														
<!-- 				<xsl:if test="($codigoTipoNotaCredito='13') and ($tipoDocumentoDelDocumentoModifica='01')">		 -->
<!-- 					<xsl:if test="($fasefechaEmision = 0) or ($fasefechaEmision = 2) or ($fasefechaEmision = 3)"> -->
<!-- 						<xsl:call-template name="regexpValidateElementIfExist"> -->
<!-- 							<xsl:with-param name="errorCodeValidate" select="'3260'" /> -->
<!-- 							<xsl:with-param name="node" select="$sNDM1C" /> -->
<!-- 							<xsl:with-param name="regexp" select="'^(F|0)$'" /> -->
<!-- 							<xsl:with-param name="descripcion"  -->
<!-- 								select="concat('Error en la linea: ', $codigoTipoNotaCredito, ' ', $tipoDocumentoDelDocumentoModifica)"/> -->
<!-- 						</xsl:call-template>											 -->
<!-- 					</xsl:if>				 -->
<!-- 				</xsl:if> -->
							
				<xsl:if test="count(key('by-document-billing-reference', concat($tipoDocumentoDelDocumentoModifica,' ', $serieNumeroDocumentoModifica))) > 1">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2365'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $tipoDocumentoDelDocumentoModifica,' ', $serieNumeroDocumentoModifica)"/>
					</xsl:call-template>
				</xsl:if>
								
				<!-- 19. Tipo de documento del documento que modifica -->
				<xsl:if test="not($codigoTipoNotaCredito='10') and ($sNDM1C='F')">
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2116'" />
						<xsl:with-param name="node" select="$tipoDocumentoDelDocumentoModifica" />
						<xsl:with-param name="regexp" select="'^(01|03|05|06|12|13|15|16|18|21|28|30|34|37|42|43|45|55|11|17|23|24|56)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $codigoTipoNotaCredito, ' ', $sNDM1C, ' ', $tipoDocumentoDelDocumentoModifica)"/>
					</xsl:call-template>					
				</xsl:if>
				
				<xsl:if test="($codigoTipoNotaCredito='10') and ($sNDM1C='F')">
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2116'" />
						<xsl:with-param name="node" select="$tipoDocumentoDelDocumentoModifica" />
						<xsl:with-param name="regexp" select="'^(01|03|05|06|12|13|15|16|18|21|28|30|34|37|42|43|45|55|11|17|23|24|56||-)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $codigoTipoNotaCredito, ' ', $sNDM1C, ' ', $tipoDocumentoDelDocumentoModifica)"/>
					</xsl:call-template>					
				</xsl:if>

				<xsl:if test="not($codigoTipoNotaCredito='10') and ($sNDM1C='B')">
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2399'" />
						<xsl:with-param name="node" select="$tipoDocumentoDelDocumentoModifica" />
						<xsl:with-param name="regexp" select="'^(03|12|16|55)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $codigoTipoNotaCredito, ' ', $sNDM1C, ' ', $tipoDocumentoDelDocumentoModifica)"/>
					</xsl:call-template>					
				</xsl:if>
				
				<xsl:if test="($codigoTipoNotaCredito='10') and ($sNDM1C='B')">
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2399'" />
						<xsl:with-param name="node" select="$tipoDocumentoDelDocumentoModifica" />
						<xsl:with-param name="regexp" select="'^(03|12|16|55||-)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $codigoTipoNotaCredito, ' ', $sNDM1C, ' ', $tipoDocumentoDelDocumentoModifica)"/>
					</xsl:call-template>					
				</xsl:if>
								
				<xsl:if test="not($codigoTipoNotaCredito='10') and (($sNDM1C = '0') or ($sNDM1C = '1') or ($sNDM1C = '2') or 
						($sNDM1C = '3') or ($sNDM1C = '4') or ($sNDM1C = '5') or ($sNDM1C = '6') or ($sNDM1C = '7') or 
						($sNDM1C = '8') or ($sNDM1C = '9'))">
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2594'" />
						<xsl:with-param name="node" select="$tipoDocumentoDelDocumentoModifica" />
						<xsl:with-param name="regexp" select="'^(01|03|05|06|12|13|15|16|18|21|28|30|34|37|42|43|45|55|11|17|23|24|56)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $codigoTipoNotaCredito, ' ', $sNDM1C, ' ', $tipoDocumentoDelDocumentoModifica)"/>
					</xsl:call-template>
				</xsl:if>				

				<xsl:if test="($codigoTipoNotaCredito='10') and (($sNDM1C = '0') or ($sNDM1C = '1') or ($sNDM1C = '2') or 
						($sNDM1C = '3') or ($sNDM1C = '4') or ($sNDM1C = '5') or ($sNDM1C = '6') or ($sNDM1C = '7') or 
						($sNDM1C = '8') or ($sNDM1C = '9'))">
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2594'" />
						<xsl:with-param name="node" select="$tipoDocumentoDelDocumentoModifica" />
						<xsl:with-param name="regexp" select="'^(01|03|05|06|12|13|15|16|18|21|28|30|34|37|42|43|45|55|11|17|23|24|56||-)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $codigoTipoNotaCredito, ' ', $sNDM1C, ' ', $tipoDocumentoDelDocumentoModifica)"/>
					</xsl:call-template>
				</xsl:if>	
				
				<xsl:if test="($codigoTipoNotaCredito='13')">		
					<xsl:if test="($fasefechaEmision = 0) or ($fasefechaEmision = 2) or ($fasefechaEmision = 3)">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'3259'" />
							<xsl:with-param name="node" select="$tipoDocumentoDelDocumentoModifica" />
							<xsl:with-param name="regexp" select="'^(01)$'" />
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $codigoTipoNotaCredito, ' ', $tipoDocumentoDelDocumentoModifica)"/>
						</xsl:call-template>											
					</xsl:if>				
				</xsl:if>					
				
				<xsl:if test="not($tipoDocumentoDelDocumentoModifica = $tipoDocumentoDocumentoModificaGlobal)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2884'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' | ', $tipoDocumentoDocumentoModificaGlobal, ' | ',$tipoDocumentoDelDocumentoModifica)"/>
					</xsl:call-template>
				</xsl:if>									

				<xsl:if test="(($codigoTipoNotaCredito = '04') or ($codigoTipoNotaCredito = '05') or ($codigoTipoNotaCredito = '08'))
						and ($tipoDocumentoDelDocumentoModifica = '03')">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4367'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), ' ', $codigoTipoNotaCredito, ' ', $tipoDocumentoDelDocumentoModifica)"/>
					</xsl:call-template>	
				</xsl:if>		

				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4251'" />
					<xsl:with-param name="node" select="$tipoDocumentoDelDocumentoModifica_ListAgencyName" />
					<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: 4) ', $tipoDocumentoDelDocumentoModifica_ListAgencyName)"/>
				</xsl:call-template>		
				
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4252'" />
					<xsl:with-param name="node" select="$tipoDocumentoDelDocumentoModifica_ListName" />
					<xsl:with-param name="regexp" select="'^(Tipo de Documento)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: 4) ', $tipoDocumentoDelDocumentoModifica_ListName)"/>
				</xsl:call-template>
				
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4253'" />
					<xsl:with-param name="node" select="$tipoDocumentoDelDocumentoModifica_ListURI" />
					<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo01)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: 2) ', $tipoDocumentoDelDocumentoModifica_ListURI)"/>
				</xsl:call-template>	
																
			</xsl:for-each>
		</xsl:for-each>		

		<!-- Documentos de referencia -->			
		<xsl:for-each select="cac:DespatchDocumentReference">
			<!-- 20. Número de la guía de remisión -->			
			<xsl:variable name="numeroGuiaRemisionRelacionada" select="./cbc:ID"/>	
			<!-- 20. Tipo de la guía de remisión -->
			<xsl:variable name="tipoGuiaRemisionRelacionada" select="./cbc:DocumentTypeCode"/>			
			<xsl:variable name="tipoGuiaRemisionRelacionada_ListAgencyName" select="./cbc:DocumentTypeCode/@listAgencyName"/>	
			<xsl:variable name="tipoGuiaRemisionRelacionada_ListName" select="./cbc:DocumentTypeCode/@listName"/>	
			<xsl:variable name="tipoGuiaRemisionRelacionada_ListURI" select="./cbc:DocumentTypeCode/@listURI"/>		
						
			<xsl:if test="string($numeroGuiaRemisionRelacionada)">	
				<!-- 20. Número de la guía de remisión relacionada -->		
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4006'"/>
					<xsl:with-param name="node" select="$numeroGuiaRemisionRelacionada"/>
					<xsl:with-param name="regexp" select="'^[T][A-Z0-9]{3}-[0-9]{1,8}$|^[0-9]{4}-[0-9]{1,8}$|^[EG]{2}[0-9]{2}-[0-9]{1,8}$|^[G]{1}[0-9]{3}-[0-9]{1,8}$'"/>
					<xsl:with-param name="isError" select="false()"/> 
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $numeroGuiaRemisionRelacionada)"/>
				</xsl:call-template>			
				
				<!-- 20. Tipo de la guía de remisión relacionada -->						
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4005'"/>
					<xsl:with-param name="node" select="$tipoGuiaRemisionRelacionada"/>
					<xsl:with-param name="regexp" select="'^(09|31)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $tipoGuiaRemisionRelacionada)"/>
				</xsl:call-template>		
				
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4251'" />
					<xsl:with-param name="node" select="$tipoGuiaRemisionRelacionada_ListAgencyName" />
					<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: 5) ', position(), ' ', $tipoGuiaRemisionRelacionada_ListAgencyName)"/>
				</xsl:call-template>			
		
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4252'" />
					<xsl:with-param name="node" select="$tipoGuiaRemisionRelacionada_ListName" />
					<xsl:with-param name="regexp" select="'^(Tipo de Documento)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: 5) ', position(), ' ', $tipoGuiaRemisionRelacionada_ListName)"/>
				</xsl:call-template>	
		
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4253'" />
					<xsl:with-param name="node" select="$tipoGuiaRemisionRelacionada_ListURI" />
					<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo01)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: 4) ', position(), ' ', $tipoGuiaRemisionRelacionada_ListURI)"/>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if
				test="count(key('by-document-despatch-reference', concat($tipoGuiaRemisionRelacionada,' ',$numeroGuiaRemisionRelacionada))) > 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2364'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoGuiaRemisionRelacionada,' ', $numeroGuiaRemisionRelacionada)"/>
				</xsl:call-template>
			</xsl:if>					
		</xsl:for-each>				
		
		<!-- 21. Tipo de documento -->
		<xsl:variable name="countTipoOtroDocumentoRelacionada" select="count(cac:AdditionalDocumentReference/cbc:DocumentTypeCode[text()='99'])"/>
		<!-- 21. Tipo y número de otro documento relacionado -->
		<xsl:for-each select="cac:AdditionalDocumentReference">		
			<!-- 21. Número de documento -->
			<xsl:variable name="numeroOtroDocumentoRelacionada" select="./cbc:ID"/>
			<!-- 21. Tipo de documento -->
			<xsl:variable name="tipoOtroDocumentoRelacionada" select="./cbc:DocumentTypeCode"/>	
			<xsl:variable name="tipoOtroDocumentoRelacionada_ListAgencyName" select="./cbc:DocumentTypeCode/@listAgencyName"/>	
			<xsl:variable name="tipoOtroDocumentoRelacionada_ListName" select="./cbc:DocumentTypeCode/@listName"/>	
			<xsl:variable name="tipoOtroDocumentoRelacionada_ListURI" select="./cbc:DocumentTypeCode/@listURI"/>		
			
			<xsl:if test="$numeroOtroDocumentoRelacionada">
				<!-- 21. Número de documento -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4010'"/>
					<xsl:with-param name="node" select="$numeroOtroDocumentoRelacionada"/>
					<!-- <xsl:with-param name="regexp" select="'^[\w].{6,30}$'"/> -->
					<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s]{6,30}$'"/>
					<xsl:with-param name="isError" select="false()"/> 
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ',$numeroOtroDocumentoRelacionada)"/>
				</xsl:call-template>	
								
			    <xsl:call-template name="isTrueExpresion">
			      <xsl:with-param name="errorCodeValidate" select="'2636'"/>
			      <xsl:with-param name="node" select="cbc:ID"/>
			      <xsl:with-param name="expresion" select="not($codigoTipoNotaCredito = '10') and ($tipoOtroDocumentoRelacionada = '99') and not($numeroOtroDocumentoRelacionada)"/>
			      <xsl:with-param name="isError" select="false()"/>
			    </xsl:call-template>
    				
				<!-- 21. Tipo de documento -->		
				<xsl:if test="not(($tipoOtroDocumentoRelacionada = '01') or ($tipoOtroDocumentoRelacionada = '04') or 
						($tipoOtroDocumentoRelacionada = '05') or ($tipoOtroDocumentoRelacionada = '99'))"> 
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4009'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOtroDocumentoRelacionada)" />
					</xsl:call-template>					
				</xsl:if>					
				
			    <xsl:call-template name="isTrueExpresion">
			      <xsl:with-param name="errorCodeValidate" select="'2635'"/>
			      <xsl:with-param name="node" select="cbc:ID"/>
			      <xsl:with-param name="expresion" select="($codigoTipoNotaCredito = '10') and ($countTipoOtroDocumentoRelacionada &gt; 1)"/>
			      <xsl:with-param name="isError" select="true()"/>
			    </xsl:call-template>
			
			    <xsl:call-template name="isTrueExpresion">
			      <xsl:with-param name="errorCodeValidate" select="'2637'"/>
			      <xsl:with-param name="node" select="cbc:DocumentTypeCode"/>
			      <xsl:with-param name="expresion" select="($codigoTipoNotaCredito = '10') and (string($tipoOtroDocumentoRelacionada)) and not($tipoOtroDocumentoRelacionada ='99') "/>
			      <xsl:with-param name="isError" select="true()"/>
			    </xsl:call-template>				
				
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4251'" />
					<xsl:with-param name="node" select="$tipoOtroDocumentoRelacionada_ListAgencyName" />
					<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: 6) ', position(), ' ', $tipoOtroDocumentoRelacionada_ListAgencyName)"/>
				</xsl:call-template>			
		
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4252'" />
					<xsl:with-param name="node" select="$tipoOtroDocumentoRelacionada_ListName" />
					<xsl:with-param name="regexp" select="'^(Documento Relacionado)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: 6) ', position(), ' ', $tipoOtroDocumentoRelacionada_ListName)"/>
				</xsl:call-template>	
		
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4253'" />
					<xsl:with-param name="node" select="$tipoOtroDocumentoRelacionada_ListURI" />
					<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo12)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: 5) ', position(), ' ', $tipoOtroDocumentoRelacionada_ListURI)"/>
				</xsl:call-template>								
			</xsl:if>
		
			<xsl:if
				test="count(key('by-document-additional-reference', concat($tipoOtroDocumentoRelacionada,' ',$numeroOtroDocumentoRelacionada))) > 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2426'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoOtroDocumentoRelacionada,' ',$numeroOtroDocumentoRelacionada)"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>
		
		<!-- Totales de la Factura -->		
		<xsl:variable name="countTaxTotalLine" select="count(cac:CreditNoteLine/cac:TaxTotal)"/>	
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
			test="(($sumMontoBase1000Linea &gt; 0) and ($countCodigoTributo1000Global = 0)) or (($sumMontoBase1016Linea &gt; 0) and ($countCodigoTributo1016Global = 0)) or 
 				(($sumMontoBase9995Linea &gt; 0) and ($countCodigoTributo9995Global = 0)) or (($sumMontoBase9996Linea &gt; 0) and ($countCodigoTributo9996Global = 0)) or 
 				(($sumMontoBase9997Linea &gt; 0) and ($countCodigoTributo9997Global = 0)) or (($sumMontoBase9998Linea &gt; 0) and ($countCodigoTributo9998Global = 0))"> 
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2638'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countCodigoTributo1000Global, ' ', $countCodigoTributo1016Global, ' ', $countCodigoTributo9995Global, ' ', $countCodigoTributo9996Global, ' ', $countCodigoTributo9997Global, ' ', $countCodigoTributo9998Global)"/>
			</xsl:call-template>
		</xsl:if>		
															
		<xsl:if
			test="($countCodigoTributo1000Global &gt; 1) or ($countCodigoTributo1016Global &gt; 1) or ($countCodigoTributo2000Global &gt; 1) or ($countCodigoTributo9995Global &gt; 1) 
 				or ($countCodigoTributo9996Global &gt; 1) or ($countCodigoTributo9997Global &gt; 1) or ($countCodigoTributo9998Global &gt; 1) or ($countCodigoTributo9999Global &gt; 1)"> 
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3068'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $countCodigoTributo1000Global, ' ', $countCodigoTributo2000Global, ' ', $countCodigoTributo9999Global)"/>
			</xsl:call-template>
		</xsl:if>		
		
		<xsl:for-each select="cac:TaxTotal">
			<!-- 35. Monto total de impuestos -->
			<xsl:variable name="montoTotalImpuestos" select="./cbc:TaxAmount" />
			<xsl:variable name="montoTotalImpuestos_currencyID" select="./cbc:TaxAmount/@currencyID" />	
			<!-- 36. Total Valor de Venta - Exportación -->
			<!-- 37. Total valor de venta - operaciones inafectas -->
			<!-- 38. Total valor de venta - operaciones exoneradas -->
			<!-- 39. Total valor de venta - operaciones gratuitas -->
			<!-- 39. Sumatoria de impuestos de operaciones gratuitas -->
			<!-- 40. Total valor de venta - operaciones gravadas (IGV o IVAP) -->
			<!-- 40. Total Importe IGV o IVAP -->
			<!-- 41. Sumatoria ISC -->
			<!-- 42. Sumatoria Otros Tributos -->	
						
			<!-- 35. Monto total de impuestos -->	
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'3020'"/>
				<xsl:with-param name="node" select="$montoTotalImpuestos"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $montoTotalImpuestos)"/>
			</xsl:call-template>
			
			<xsl:if test="($montoTotalImpuestos) and ($tipoDocumentoDocumentoModificaGlobal = '01')">
				<xsl:variable name="montoImporteTributoFor" select="$sumMontoTributo1000Global + $sumMontoTributo1016Global + $sumMontoTributo2000Global + $sumMontoTributo7152Global + $sumMontoTributo9999Global" />
				<xsl:variable name="dif_MontoTotalImpuestos" select="$montoTotalImpuestos - $montoImporteTributoFor" />
				<xsl:if test="($dif_MontoTotalImpuestos &lt; -1) or ($dif_MontoTotalImpuestos &gt; 1)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3294'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $dif_MontoTotalImpuestos, ' ', $montoTotalImpuestos, ' ', $montoImporteTributoFor)"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:if>		
			
			<xsl:if test="($montoTotalImpuestos) and not($tipoDocumentoDocumentoModificaGlobal = '01')">
				<xsl:variable name="montoImporteTributoFor" select="$sumMontoTributo1000Global + $sumMontoTributo1016Global + $sumMontoTributo2000Global + $sumMontoTributo7152Global + $sumMontoTributo9999Global" />
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
						select="concat('Error en la linea: 1) ', position(), ' ', $tipoMoneda, ' ',$montoTotalImpuestos_currencyID)" />
				</xsl:call-template>
			</xsl:if>		
							
			<xsl:for-each select="./cac:TaxSubtotal">	
				<!-- 36 / 40. Total Valor de Venta -->
				<!-- 41 / 42. Monto base -->
				<xsl:variable name="totalValorVentaItem" select="./cbc:TaxableAmount" />		
				<xsl:variable name="totalValorVentaItem_currencyID" select="./cbc:TaxableAmount/@currencyID" />
				<!-- 36 / 40. Importe del tributo -->
				<!-- 41 / 42. Monto de la Sumatoria -->
				<xsl:variable name="importeTributo" select="./cbc:TaxAmount" />
				<xsl:variable name="importeTributo_currencyID" select="./cbc:TaxAmount/@currencyID" />		
				
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
							select="concat('Error en la linea: 2) ', position(), ' ', $tipoMoneda, ' ',$totalValorVentaItem_currencyID)" />
					</xsl:call-template>
				</xsl:if>
						
				<!-- 39 / 46. -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2048'"/>
					<xsl:with-param name="node" select="$importeTributo"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $importeTributo)"/>
				</xsl:call-template>
				
				<!-- 48 / 49. Monto de la Sumatoria -->
				<xsl:if test="($tipoDocumentoDocumentoModificaGlobal = '01')">
					<xsl:if test="($countCodigoTributo7152Global &gt; 0)">				
						<xsl:variable name="dif_ImporteTributo" select="$sumMontoTributo7152Global - $sumMontoTributo7152Linea" />
						<xsl:if test="($dif_ImporteTributo &lt; -0.001) or ($dif_ImporteTributo &gt; 0.001)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3306'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $countCodigoTributo7152Global, ' ', $dif_ImporteTributo, ' ', $sumMontoTributo7152Global, ' ', $sumMontoTributo7152Linea)"/>
							</xsl:call-template>									
						</xsl:if>			
					</xsl:if>						
				</xsl:if>
				
				<xsl:if test="not($tipoDocumentoDocumentoModificaGlobal = '01')">
					<xsl:if test="($countCodigoTributo7152Global &gt; 0)">				
						<xsl:variable name="dif_ImporteTributo" select="$sumMontoTributo7152Global - $sumMontoTributo7152Linea" />
						<xsl:if test="($dif_ImporteTributo &lt; -0.001) or ($dif_ImporteTributo &gt; 0.001)">
							<xsl:call-template name="addWarning">
								<xsl:with-param name="warningCode" select="'4321'" />
								<xsl:with-param name="warningMessage"
									select="concat('Error en la linea: ', position(), ' ', $countCodigoTributo7152Global, ' ', $dif_ImporteTributo, ' ', $sumMontoTributo7152Global, ' ', $sumMontoTributo7152Linea)"/>
							</xsl:call-template>							
						</xsl:if>			
					</xsl:if>						
				</xsl:if>				
								
				<xsl:if test="($importeTributo_currencyID) and not($tipoMoneda=$importeTributo_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 3) ', position(), ' ', $tipoMoneda, ' ',$importeTributo_currencyID)" />
					</xsl:call-template>
				</xsl:if>							
							
				<xsl:for-each select="./cac:TaxCategory">
					<xsl:for-each select="./cac:TaxScheme">
						<!-- 39 / 46. Código de tributo -->
						<xsl:variable name="codigoTributo" select="./cbc:ID" />
						<xsl:variable name="codigoTributo_schemeName" select="./cbc:ID/@schemeName" />
						<xsl:variable name="codigoTributo_schemeAgencyName" select="./cbc:ID/@schemeAgencyName" />
						<xsl:variable name="codigoTributo_schemeURI" select="./cbc:ID/@schemeURI" />
						<!-- 39 / 46. Nombre de tributo -->
						<xsl:variable name="nombreTributo" select="./cbc:Name" />
						<!-- 39 / 46. Código internacional tributo -->
						<xsl:variable name="codigoInternacionalTributo" select="./cbc:TaxTypeCode" />	
						
						<!-- 39 / 46. Código de tributo -->
 						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3059'"/>
							<xsl:with-param name="node" select="$codigoTributo"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $codigoTributo)"/>
						</xsl:call-template>
						
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'3007'"/>
							<xsl:with-param name="node" select="$codigoTributo"/>
							<xsl:with-param name="regexp" select="'^(1000|1016|2000|7152|9995|9996|9997|9998|9999)$'" />
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $codigoTributo)"/>
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
							<xsl:if test="($t12 &lt; 0)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'2949'" />
									<xsl:with-param name="errorMessage" 
										select="concat('Error en la linea: ', $cbcIssueDate, ' - ', $t11, ' - ', $t12)" />
								</xsl:call-template>
							</xsl:if>						
						</xsl:if>

						<xsl:if test="($codigoTributo='9995') and ($tipoDocumentoDocumentoModificaGlobal = '01')">
							<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumValorVentaItem9995Linea" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3273'" />
									<xsl:with-param name="errorMessage" 
										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumValorVentaItem9995Linea)"/>
								</xsl:call-template>								
<!-- 								<xsl:call-template name="addWarning"> -->
<!-- 									<xsl:with-param name="warningCode" select="'4295'" /> -->
<!-- 									<xsl:with-param name="warningMessage" -->
<!-- 										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumValorVentaItem9995Linea)"/> -->
<!-- 								</xsl:call-template> -->
							</xsl:if>			
						</xsl:if>
												
						<xsl:if test="($codigoTributo='9995') and not($tipoDocumentoDocumentoModificaGlobal = '01')">
							<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumValorVentaItem9995Linea" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4295'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumValorVentaItem9995Linea)"/>
								</xsl:call-template>
							</xsl:if>			
						</xsl:if>

						<xsl:if test="($codigoTributo='9997') and ($tipoDocumentoDocumentoModificaGlobal = '01')">
							<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumValorVentaItem9997Linea" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
 								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3275'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumValorVentaItem9997Linea)"/>  
								</xsl:call-template> 								
<!--   								<xsl:call-template name="addWarning">  -->
<!--  									<xsl:with-param name="warningCode" select="'4297'" />   -->
<!--  									<xsl:with-param name="warningMessage"   -->
<!--  										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumValorVentaItem9997Linea)"/>   -->
<!--  								</xsl:call-template>   -->
							</xsl:if>			
						</xsl:if>							
						<xsl:if test="($codigoTributo='9997') and not($tipoDocumentoDocumentoModificaGlobal = '01')">
							<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumValorVentaItem9997Linea" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
  								<xsl:call-template name="addWarning"> 
 									<xsl:with-param name="warningCode" select="'4297'" />  
 									<xsl:with-param name="warningMessage"  
 										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumValorVentaItem9997Linea)"/>  
 								</xsl:call-template>  
							</xsl:if>			
						</xsl:if>

						<xsl:if test="($codigoTributo='9998') and ($tipoDocumentoDocumentoModificaGlobal = '01')">
							<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumValorVentaItem9998Linea" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3274'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumValorVentaItem9998Linea)"/>
								</xsl:call-template>									
							</xsl:if>			
						</xsl:if>
												
						<xsl:if test="($codigoTributo='9998') and not($tipoDocumentoDocumentoModificaGlobal = '01')">
							<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumValorVentaItem9998Linea" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4296'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumValorVentaItem9998Linea)"/>
								</xsl:call-template>
							</xsl:if>			
						</xsl:if>
						
						<!-- 42. Total Valor de Venta -->
						<xsl:if test="($codigoTributo='9996') and ($tipoDocumentoDocumentoModificaGlobal = '01')">
							<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumValorVentaItem9996Linea" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3276'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumValorVentaItem9996Linea)"/>
								</xsl:call-template>								
<!-- 								<xsl:call-template name="addWarning"> -->
<!-- 									<xsl:with-param name="warningCode" select="'4298'" /> -->
<!-- 									<xsl:with-param name="warningMessage" -->
<!-- 										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumValorVentaItem9996Linea)"/> -->
<!-- 								</xsl:call-template> -->
							</xsl:if>			
						</xsl:if>
												
						<xsl:if test="($codigoTributo='9996')  and not($tipoDocumentoDocumentoModificaGlobal = '01')">
							<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumValorVentaItem9996Linea" />
							<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4298'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumValorVentaItem9996Linea)"/>
								</xsl:call-template>
							</xsl:if>			
						</xsl:if>
						
 						<xsl:if test="($codigoTributo='9996') and ($countCodigoPrecio02Global &gt; 0) and ($totalValorVentaItem = 0)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2641'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $countCodigoPrecio02Global, ' ', $totalValorVentaItem)"/>
							</xsl:call-template>
						</xsl:if>
																												
						<!-- 45 / 46. Monto base -->
						<xsl:if test="($totalValorVentaItem)">
							<xsl:if test="($codigoTributo='2000') and ($tipoDocumentoDocumentoModificaGlobal = '01')">
								<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumMontoBase2000sin9996Linea" />
								<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'3296'" />
										<xsl:with-param name="errorMessage"
											select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumMontoBase2000sin9996Linea)"/>
									</xsl:call-template>								
								</xsl:if>			
							</xsl:if>
							
							<xsl:if test="($codigoTributo='2000') and not($tipoDocumentoDocumentoModificaGlobal = '01')">
								<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumMontoBase2000sin9996Linea" />
								<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
									<xsl:call-template name="addWarning">
										<xsl:with-param name="warningCode" select="'4303'" />
										<xsl:with-param name="warningMessage"
											select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumMontoBase2000sin9996Linea)"/>
									</xsl:call-template>
								</xsl:if>			
							</xsl:if>
							
							<xsl:if test="($codigoTributo='9999') and ($tipoDocumentoDocumentoModificaGlobal = '01')">
								<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumMontoBase9999Linea" />
								<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
									<xsl:call-template name="addWarning">
										<xsl:with-param name="warningCode" select="'3297'" />
										<xsl:with-param name="warningMessage"
											select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumMontoBase9999Linea)"/>
									</xsl:call-template>								
								</xsl:if>			
							</xsl:if>
							
							<xsl:if test="($codigoTributo='9999') and not($tipoDocumentoDocumentoModificaGlobal = '01')">
								<xsl:variable name="dif_TotalValorVenta" select="$totalValorVentaItem - $sumMontoBase9999Linea" />
								<xsl:if test="($dif_TotalValorVenta &lt; -1) or ($dif_TotalValorVenta &gt; 1)">
									<xsl:call-template name="addWarning">
										<xsl:with-param name="warningCode" select="'4304'" />
										<xsl:with-param name="warningMessage"
											select="concat('Error en la linea: ', position(), ' ', $dif_TotalValorVenta, ' ', $totalValorVentaItem, ' ', $sumMontoBase9999Linea)"/>
									</xsl:call-template>
								</xsl:if>			
							</xsl:if>
						</xsl:if>
						<!-- 39 / 41. Importe del tributo -->
						<xsl:if test="($importeTributo) and not($importeTributo = 0) and (($codigoTributo='9995') or ($codigoTributo='9997') or ($codigoTributo='9998'))">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3000'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $importeTributo, ' ',$codigoTributo)" />
							</xsl:call-template>
						</xsl:if>										
						
						<!-- 45 / 46. Monto de la Sumatoria -->
						<xsl:if test="($tipoDocumentoDocumentoModificaGlobal = '01') and ($codigoTributo='2000')">				
							<xsl:variable name="dif_ImporteTributo" select="$importeTributo - $sumMontoTributo2000sin9996Linea" />
							<xsl:if test="($dif_ImporteTributo &lt; -1) or ($dif_ImporteTributo &gt; 1)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3298'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(),' | ',$codigoTributo,' | ',$dif_ImporteTributo,' | ',$importeTributo,' | ',$sumMontoTributo2000sin9996Linea)"/>
								</xsl:call-template>															
							</xsl:if>																
						</xsl:if>			
												
						<xsl:if test="not($tipoDocumentoDocumentoModificaGlobal = '01') and ($codigoTributo='2000')">			
							<xsl:variable name="dif_ImporteTributo" select="$importeTributo - $sumMontoTributo2000sin9996Linea" />
							<xsl:if test="($dif_ImporteTributo &lt; -1) or ($dif_ImporteTributo &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4305'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $dif_ImporteTributo, ' ', $importeTributo, ' ', $sumMontoTributo2000sin9996Linea)"/>
								</xsl:call-template>
							</xsl:if>			
						</xsl:if>				
						
						<!-- 39 / 41 - 43 / 44 . Código de tributo codigoTipoNotaCredito -->												
						<xsl:if test="($codigoTipoNotaCredito='12') and ($sumTotalValor1000Global &gt; 0)">		
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3107'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTipoNotaCredito, ' ', $sumTotalValor1000Global)"/>
							</xsl:call-template>																
						</xsl:if>	
						
						<xsl:if test="($codigoTipoNotaCredito='11') and (($sumTotalValor1000Global &gt; 0) or ($sumTotalValor1016Global &gt; 0))">		
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3107'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTipoNotaCredito, ' ', $sumTotalValor1016Global)"/>
							</xsl:call-template>																
						</xsl:if>			
						
						<xsl:if test="($codigoTipoNotaCredito='12') and ($sumTotalValor2000Global &gt; 0)">		
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3107'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTipoNotaCredito, ' ', $sumTotalValor2000Global)"/>
							</xsl:call-template>																
						</xsl:if>	
						
						<xsl:if test="($codigoTipoNotaCredito='11') and (($sumTotalValor2000Global &gt; 0) or ($sumTotalValor9999Global &gt; 0))">		
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3107'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTipoNotaCredito, ' ', $sumTotalValor2000Global)"/>
							</xsl:call-template>																
						</xsl:if>											
						
						<xsl:if test="($codigoTipoNotaCredito='12') and (($sumTotalValor9995Global &gt; 0) or ($sumTotalValor9997Global &gt; 0) or ($sumTotalValor9998Global &gt; 0))">		
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3221'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTipoNotaCredito, ' ', $sumTotalValor9995Global, ' ', $sumTotalValor9997Global, ' ', $sumTotalValor9998Global)"/>
							</xsl:call-template>																
						</xsl:if>	
						
						<xsl:if test="($codigoTipoNotaCredito='11') and (($sumTotalValor9997Global &gt; 0) or ($sumTotalValor9998Global &gt; 0))">		
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3221'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTipoNotaCredito, ' ', $sumTotalValor9997Global, ' ', $sumTotalValor9998Global)"/>
							</xsl:call-template>																
						</xsl:if>													
						
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4255'" />
							<xsl:with-param name="node" select="$codigoTributo_schemeName" />
							<xsl:with-param name="regexp" select="'^(Codigo de tributos)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 4) ', $codigoTributo_schemeName)"/>
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
								select="concat('Error en la linea: 3) ', $codigoTributo_schemeURI)"/>
						</xsl:call-template>			
						
						<!-- 39 / 46. Nombre de tributo -->
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'2054'"/>
							<xsl:with-param name="node" select="$nombreTributo"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $nombreTributo)"/>
						</xsl:call-template>
						
						<xsl:if test="($codigoTributo='1000' and not($nombreTributo='IGV')) or ($codigoTributo='1016' and not($nombreTributo='IVAP'))
 								or ($codigoTributo='2000' and not($nombreTributo='ISC')) or ($codigoTributo='9995' and not($nombreTributo='EXP')) 
 								or ($codigoTributo='9996' and not($nombreTributo='GRA')) or ($codigoTributo='9997' and not($nombreTributo='EXO')) 
								or ($codigoTributo='9998' and not($nombreTributo='INA')) or ($codigoTributo='9999' and not($nombreTributo='OTROS'))">						 
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2964'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $nombreTributo)"/>
							</xsl:call-template>
						</xsl:if>			
						
						<!-- 39 / 46. Código internacional tributo -->
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'2052'"/>
							<xsl:with-param name="node" select="$codigoInternacionalTributo"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $codigoInternacionalTributo)"/>
						</xsl:call-template>
			
						<xsl:if test="($codigoTributo='1000' and not($codigoInternacionalTributo='VAT')) or ($codigoTributo='1016' and not($codigoInternacionalTributo='VAT'))
 								or ($codigoTributo='2000' and not($codigoInternacionalTributo='EXC')) or ($codigoTributo='9995' and not($codigoInternacionalTributo='FRE')) 
								or ($codigoTributo='9996' and not($codigoInternacionalTributo='FRE')) or ($codigoTributo='9997' and not($codigoInternacionalTributo='VAT')) 
 								or ($codigoTributo='9998' and not($codigoInternacionalTributo='FRE')) or ($codigoTributo='9999' and not($codigoInternacionalTributo='OTH'))">	
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2961'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoTributo, ' ', $codigoInternacionalTributo)"/>
							</xsl:call-template>
						</xsl:if>																									
					</xsl:for-each>								
				</xsl:for-each>								
			</xsl:for-each>								
		</xsl:for-each>			

		<!-- CALCULOS -->		
	    <xsl:variable name="totalBaseIGVxLinea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1000']]/cbc:TaxableAmount)"/>
	    <xsl:variable name="totalBaseIVAPxLinea" select="sum(cac:CreditNoteLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1016']]/cbc:TaxableAmount)"/>

	    <xsl:variable name="sumatoriaBaseIGV" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1000']]/cbc:TaxableAmount)"/>
	    <xsl:variable name="sumatoriaBaseIVAP" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1016']]/cbc:TaxableAmount)"/>	    

		<xsl:variable name="sumatoriaIGV" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1000']]/cbc:TaxAmount)"/>
		<xsl:variable name="sumatoriaIVAP" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1016']]/cbc:TaxAmount)" />
    	<xsl:variable name="sumatoriaOtrosTributos" select="sum(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '9999']]/cbc:TaxAmount)"/>

	    <xsl:variable name="sumatoriaDescuentosGlobales" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode [text() = '02' or text() = '04']]/cbc:Amount)"/>
		<xsl:variable name="sumatoriaDescuentosGlobales_02" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode[text() = '02']]/cbc:Amount)"/>	    
	    <xsl:variable name="sumatoriaCargosGobales_49" select="sum(cac:AllowanceCharge[cbc:AllowanceChargeReasonCode [text() = '49']]/cbc:Amount)"/>	    	    
		<!-- 30. Valor de venta por ítem -->
		<xsl:variable name="sumValorVentaItem1000For" select="sum(cac:CreditNoteLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:LineExtensionAmount)"/>	
		<xsl:variable name="sumValorVentaItem1016For" select="sum(cac:CreditNoteLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:LineExtensionAmount)"/>	
		<xsl:variable name="sumatoriaTotalValorVenta" select="sum(cac:LegalMonetaryTotal/cbc:LineExtensionAmount)"/>	   
		 	
		<!-- 43 / 44. Total valor de venta operaciones gravadas -->	
		<xsl:if test="($tipoDocumentoDocumentoModificaGlobal = '01') and
				(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1016']]/cbc:TaxableAmount)">
			<xsl:call-template name="isTrueExpresion">  
 				<xsl:with-param name="errorCodeValidate" select="'3293'"/>  
 				<xsl:with-param name="node" select="(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1016']]/cbc:TaxableAmount)"/>  
 				<xsl:with-param name="expresion" select="($sumTotalValor1016Global + 1 ) &lt;  $totalBaseIVAPxLinea or ($sumTotalValor1016Global - 1) &gt; $totalBaseIVAPxLinea"/>  
 				<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $sumValorVentaItem1016For, ' ', $totalBaseIVAPxLinea)"/>
			</xsl:call-template>  
		</xsl:if>	
		
		<xsl:if test="not($tipoDocumentoDocumentoModificaGlobal = '01') and
				(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1016']]/cbc:TaxableAmount)">
			<xsl:call-template name="isTrueExpresion">  
 				<xsl:with-param name="errorCodeValidate" select="'4300'"/>  
 				<xsl:with-param name="node" select="(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1016']]/cbc:TaxableAmount)"/>  
 				<xsl:with-param name="expresion" select="($sumTotalValor1016Global + 1 ) &lt;  $totalBaseIVAPxLinea or ($sumTotalValor1016Global - 1) &gt; $totalBaseIVAPxLinea"/>  
 				<xsl:with-param name="isError" select="false()"/>  
 				<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $sumValorVentaItem1016For, ' ', $totalBaseIVAPxLinea)"/>
			</xsl:call-template>  
		</xsl:if>	
											
		<!-- 43 / 44. Total Importe de IGV o IVAP, según corresponda -->
		<xsl:if test="($tipoDocumentoDocumentoModificaGlobal = '01') and 
			(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1000']])">
			<xsl:variable name="totalImporteCalculado" select="$totalBaseIGVxLinea * 0.18"/>			
			<xsl:variable name="dif_TotalImporte" select="$sumatoriaIGV - $totalImporteCalculado" />
			<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3291'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $sumatoriaIGV, ' ', $totalImporteCalculado, ' ', $totalBaseIGVxLinea)"/>
				</xsl:call-template>			
<!-- 				<xsl:call-template name="addWarning"> -->
<!-- 					<xsl:with-param name="warningCode" select="'4290'" /> -->
<!-- 					<xsl:with-param name="warningMessage" -->
<!-- 						select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $sumatoriaIGV, ' ', $totalImporteCalculado, ' ', $totalBaseIGVxLinea)"/> -->
<!-- 				</xsl:call-template> -->
			</xsl:if>	
		</xsl:if>				
		<xsl:if test="not($tipoDocumentoDocumentoModificaGlobal = '01') and 
			(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1000']])">
			<xsl:variable name="totalImporteCalculado" select="$totalBaseIGVxLinea * 0.18"/>			
			<xsl:variable name="dif_TotalImporte" select="$sumatoriaIGV - $totalImporteCalculado" />
			<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4290'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $sumatoriaIGV, ' ', $totalImporteCalculado, ' ', $totalBaseIGVxLinea)"/>
				</xsl:call-template>
			</xsl:if>	
		</xsl:if>		
		
		<!-- 43 / 44. Total valor de venta operaciones gravadas -->
		<xsl:if test="(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1000']]/cbc:TaxableAmount) 
	    		and ($tipoDocumentoDocumentoModificaGlobal = '01')">
			<xsl:variable name="totalImporteCalculado" select="($sumValorVentaItem1000For - $sumatoriaDescuentosGlobales + $sumatoriaCargosGobales_49)"/>			
			<xsl:variable name="dif_TotalImporte" select="$sumatoriaBaseIGV - $sumValorVentaItem1000For" />
			<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3277'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $sumatoriaBaseIGV, ' ', $sumValorVentaItem1000For)"/>
				</xsl:call-template>				
<!-- 				<xsl:call-template name="addWarning"> -->
<!-- 					<xsl:with-param name="warningCode" select="'4299'" /> -->
<!-- 					<xsl:with-param name="warningMessage" -->
<!-- 						select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $sumatoriaBaseIGV, ' ', $sumValorVentaItem1000For)"/> -->
<!-- 				</xsl:call-template> -->
			</xsl:if>	  
	   	</xsl:if>
	   	 
	    <xsl:if test="(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1000']]/cbc:TaxableAmount) 
	    		and not($tipoDocumentoDocumentoModificaGlobal = '01')">
			<xsl:variable name="totalImporteCalculado" select="($sumValorVentaItem1000For - $sumatoriaDescuentosGlobales + $sumatoriaCargosGobales_49)"/>			
			<xsl:variable name="dif_TotalImporte" select="$sumatoriaBaseIGV - $sumValorVentaItem1000For" />
			<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4299'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $sumatoriaBaseIGV, ' ', $sumValorVentaItem1000For)"/>
				</xsl:call-template>
			</xsl:if>	  
	   	</xsl:if> 

	    <xsl:if test="($tipoDocumentoDocumentoModificaGlobal = '01') and 
	    		(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1016']])">
			<xsl:variable name="totalImporteCalculado" select="($sumatoriaBaseIVAP) * 0.04"/>			
			<xsl:variable name="dif_TotalImporte" select="$sumatoriaIVAP - $totalImporteCalculado" />
			<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3295'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $sumatoriaIVAP, ' ', $totalImporteCalculado, ' ', $sumatoriaIVAP)"/>
				</xsl:call-template>					
<!-- 				<xsl:call-template name="addWarning"> -->
<!-- 					<xsl:with-param name="warningCode" select="'4302'" /> -->
<!-- 					<xsl:with-param name="warningMessage" -->
<!-- 						select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $sumatoriaIVAP, ' ', $totalImporteCalculado, ' ', $sumatoriaIVAP)"/> -->
<!-- 				</xsl:call-template> -->
			</xsl:if>
	    </xsl:if>
	    
	    <xsl:if test="not($tipoDocumentoDocumentoModificaGlobal = '01') and 
	    		(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '1016']])">
			<xsl:variable name="totalImporteCalculado" select="($sumatoriaBaseIVAP) * 0.04"/>			
			<xsl:variable name="dif_TotalImporte" select="$sumatoriaIVAP - $totalImporteCalculado" />
			<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4302'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $sumatoriaIVAP, ' ', $totalImporteCalculado, ' ', $sumatoriaIVAP)"/>
				</xsl:call-template>
			</xsl:if>
	    </xsl:if>
			
		<xsl:if test="($tipoDocumentoDocumentoModificaGlobal = '01') and 
				(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '9999']])">			
			<xsl:variable name="dif_TotalImporte" select="$sumatoriaOtrosTributos - $sumMontoTributo9999Linea" />
			<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3299'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $sumatoriaOtrosTributos, ' ', $sumMontoTributo9999Linea)"/>
				</xsl:call-template>				
			</xsl:if>			
		</xsl:if>			
				
		<xsl:if test="not($tipoDocumentoDocumentoModificaGlobal = '01') and 		
				(cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text() = '9999']])">			
			<xsl:variable name="dif_TotalImporte" select="$sumatoriaOtrosTributos - $sumMontoTributo9999Linea" />
			<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4306'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $sumatoriaOtrosTributos, ' ', $sumMontoTributo9999Linea)"/>
				</xsl:call-template>
			</xsl:if>			
		</xsl:if>																					
				
		<!-- Datos del detalle o Ítem de la Nota de Débito -->
		<xsl:for-each select="cac:CreditNoteLine">
			<!-- 22. Número de orden del Ítem -->
			<xsl:variable name="numeroLinea" select="./cbc:ID"/>
			<!-- 23. Unidad de medida por ítem -->		
			<xsl:variable name="unidadMedidaLinea" select="./cbc:CreditedQuantity/@unitCode"/>	
			<!-- 24. Cantidad de unidades por ítem -->	
			<xsl:variable name="cantidadUnidadesLinea" select="./cbc:CreditedQuantity"/>
			<xsl:variable name="cantidadUnidadesLinea_UnitCodeListID" select="./cbc:CreditedQuantity/@unitCodeListID"/>
			<xsl:variable name="cantidadUnidadesLinea_UnitCodeListAgencyName" select="./cbc:CreditedQuantity/@unitCodeListAgencyName"/>			
			<!-- 34. Valor de venta por línea -->		
			<xsl:variable name="valorVentaLinea" select="./cbc:LineExtensionAmount"/>		
			<xsl:variable name="valorVentaLinea_currencyID" select="./cbc:LineExtensionAmount/@currencyID"/>

			<!-- 32. Monto base -->
			<xsl:variable name="sumMontoBaseLinea" select="sum(./cac:TaxTotal/cac:TaxSubtotal/cbc:TaxableAmount)"/>	

			<!-- 32 - 33. Monto base por Código de tributo por línea-->
			<xsl:variable name="sumMontoBase1000LineaSub" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxableAmount)"/>		
			<xsl:variable name="sumMontoBase1016LineaSub" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:TaxableAmount)"/>
			<xsl:variable name="sumMontoBase2000LineaSub" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxableAmount)"/>
			<xsl:variable name="sumMontoBase9995LineaSub" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9995']/cbc:TaxableAmount)"/>
			<xsl:variable name="sumMontoBase9996LineaSub" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9996']/cbc:TaxableAmount)"/>
			<xsl:variable name="sumMontoBase9997LineaSub" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9997']/cbc:TaxableAmount)"/>
			<xsl:variable name="sumMontoBase9998LineaSub" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9998']/cbc:TaxableAmount)"/>
			<xsl:variable name="sumMontoBase9999LineaSub" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxableAmount)"/>
			
			<!-- 33. Monto total de tributos del ítem -->
			<xsl:variable name="montoTributoLinea9996" select="./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9996']/cbc:TaxAmount" />			
						
			<!-- 32 - 33. Código de tributo por línea -->					
			<xsl:variable name="countCodigoTributoLinea1000" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])" />
			<xsl:variable name="countCodigoTributoLinea1016" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1016'])" />
			<xsl:variable name="countCodigoTributoLinea2000" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000'])" />
			<xsl:variable name="countCodigoTributoLinea7152" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='7152'])" />
			<xsl:variable name="countCodigoTributoLinea9995" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9995'])" />
			<xsl:variable name="countCodigoTributoLinea9996" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9996'])" />
			<xsl:variable name="countCodigoTributoLinea9997" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9997'])" />
			<xsl:variable name="countCodigoTributoLinea9998" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9998'])" />
			<xsl:variable name="countCodigoTributoLinea9999" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9999'])" />

			<!-- 34. Valor referencial unitario por ítem en operaciones gratuitas (no onerosas) -->
			<xsl:variable name="valorReferencialUnitarioItemOperacionesGratuitas">	
				<xsl:choose>
	    			<xsl:when test="count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceAmount) &gt; 0">
	    				<xsl:value-of select="sum(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceAmount)"/></xsl:when>
	    			<xsl:otherwise>0</xsl:otherwise>
	  			</xsl:choose>       
			</xsl:variable>	
			
			<!-- 32. Valor unitario por ítem -->
			<xsl:variable name="valorUnitarioItem">	
				<xsl:choose>
	    			<xsl:when test="count(./cac:Price/cbc:PriceAmount) &gt; 0">
	    				<xsl:value-of select="sum(./cac:Price/cbc:PriceAmount)"/></xsl:when>
	    			<xsl:otherwise>0</xsl:otherwise>
	  			</xsl:choose>       
			</xsl:variable>		
						
			<!-- 22. Número de orden del Ítem -->
			<xsl:choose>
				<xsl:when test="not($numeroLinea)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2137'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position())" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:if
						test='not(fn:matches($numeroLinea,"^[0-9]{1,3}?$")) or $numeroLinea &lt;= 0'>
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2137'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position())" />
						</xsl:call-template>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:if test="count(key('by-invoiceLine-id', number($numeroLinea))) > 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2752'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position())" />
				</xsl:call-template>
			</xsl:if>			
					
			<xsl:if test="$cantidadUnidadesLinea">
				<!-- 23. Unidad de medida por ítem -->		
				<xsl:call-template name="existValidateElementNotExist">
					<xsl:with-param name="errorCodeNotExist" select="'2138'"/>
					<xsl:with-param name="node" select="$unidadMedidaLinea"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $unidadMedidaLinea)"/>
				</xsl:call-template>
			
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4258'" />
					<xsl:with-param name="node" select="$cantidadUnidadesLinea_UnitCodeListID" />
					<xsl:with-param name="regexp" select="'^(UN/ECE rec 20)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $cantidadUnidadesLinea_UnitCodeListID)"/>
				</xsl:call-template>
	
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4259'" />
					<xsl:with-param name="node" select="$cantidadUnidadesLinea_UnitCodeListAgencyName" />
					<xsl:with-param name="regexp" select="'^(United Nations Economic Commission for Europe)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $cantidadUnidadesLinea_UnitCodeListAgencyName)"/>
				</xsl:call-template>			
			
				<!-- 24. Cantidad de unidades por item -->	
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2139'" />
					<xsl:with-param name="node" select="$cantidadUnidadesLinea" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $cantidadUnidadesLinea)"/>
				</xsl:call-template>
			</xsl:if>
														
			<xsl:for-each select="./cac:Item">		
				<!-- 29. Descripción detallada del servicio prestado, bien vendido o cedido en uso, indicando las características. -->																							
				<xsl:for-each select="./cbc:Description">					
					<xsl:variable name="descripcionDetalladaServicioPrestado" select="."/>				
					<!-- 29. Descripción detallada del servicio prestado, bien vendido o cedido en uso, indicando las características. -->		
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4084'" />
						<xsl:with-param name="node" select="$descripcionDetalladaServicioPrestado" />
<!-- 						<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,499}$'"/> -->
						<xsl:with-param name="regexp" select="'^(?!\s*$)[\s\S].{2,499}'"/>
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $descripcionDetalladaServicioPrestado)"/>
					</xsl:call-template>						
				</xsl:for-each>	
																		
				<xsl:for-each select="./cac:SellersItemIdentification">
					<!-- 25. Código de producto -->
					<xsl:variable name="codigoProducto" select="./cbc:ID"/>
				
					<!-- 25. Código de producto -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4234'" />
						<xsl:with-param name="node" select="$codigoProducto" />
						<xsl:with-param name="regexp" select="'^((?!\s*$)[\s\S]{1,30})$'"/>
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $codigoProducto)"/>
					</xsl:call-template>
				</xsl:for-each>
				
				<xsl:for-each select="./cac:CommodityClassification">
					<!-- 26. Codigo producto de SUNAT -->
					<xsl:variable name="codigoProductoSUNAT" select="./cbc:ItemClassificationCode"/>
					<xsl:variable name="codigoProductoSUNAT_ListID" select="./cbc:ItemClassificationCode/@listID"/>
					<xsl:variable name="codigoProductoSUNAT_ListAgencyName" select="./cbc:ItemClassificationCode/@listAgencyName"/>
					<xsl:variable name="codigoProductoSUNAT_ListName" select="./cbc:ItemClassificationCode/@listName"/>
					
					<!-- 26. Codigo producto de SUNAT -->
					<xsl:if test="($codigoTipoNotaCredito='11')">
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3001'"/>
							<xsl:with-param name="node" select="$codigoProductoSUNAT" />
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $codigoProductoSUNAT)"/>
						</xsl:call-template>					
					</xsl:if>
				
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4254'" />
						<xsl:with-param name="node" select="$codigoProductoSUNAT_ListID" />
						<xsl:with-param name="regexp" select="'^(UNSPSC)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: 2) ', position(), ' ', $codigoProductoSUNAT_ListID)"/>
					</xsl:call-template>			
			
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4251'" />
						<xsl:with-param name="node" select="$codigoProductoSUNAT_ListAgencyName" />
						<xsl:with-param name="regexp" select="'^(GS1 US)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: 7) ', position(), ' ', $codigoProductoSUNAT_ListAgencyName)"/>
					</xsl:call-template>	
			
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4252'" />
						<xsl:with-param name="node" select="$codigoProductoSUNAT_ListName" />
						<xsl:with-param name="regexp" select="'^(Item Classification)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: 7) ', position(), ' ', $codigoProductoSUNAT_ListName)"/>
					</xsl:call-template>	
				</xsl:for-each>
						
				<xsl:for-each select="./cac:StandardItemIdentification">
					<!-- 27. Código de producto GS1-->
					<xsl:variable name="codigoProductoGS1" select="./cbc:ID"/>
					<!-- 27. Tipo de estructura GTIN -->
					<xsl:variable name="codigoProductoGS1_schemeID" select="./cbc:ID/@schemeID"/>	
					
					<!-- 27. Código de producto GS1-->
					<xsl:if test="($codigoProductoGS1)">
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3199'"/>
							<xsl:with-param name="node" select="$codigoProductoGS1_schemeID" />
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $codigoProductoGS1_schemeID)"/>
						</xsl:call-template>				
					</xsl:if>

					<xsl:variable name="lengthCodigoProductoGS1"><xsl:choose>
						<xsl:when test="count($codigoProductoGS1) &gt; 0">
							<xsl:value-of select="string-length(string($codigoProductoGS1))"/>
						</xsl:when><xsl:otherwise>0</xsl:otherwise>
			    	</xsl:choose></xsl:variable>
			    				
					<xsl:if test="($codigoProductoGS1_schemeID = 'GTIN-8') and not($lengthCodigoProductoGS1 = 8)">										
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3201'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea 1: ', position(), ' ', $codigoProductoGS1_schemeID, ' ', $lengthCodigoProductoGS1)"/>
						</xsl:call-template>
					</xsl:if>
				
					<xsl:if test="($codigoProductoGS1_schemeID = 'GTIN-13' and not($lengthCodigoProductoGS1 = 13))">										
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3201'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea 2: ', position(), ' ', $codigoProductoGS1_schemeID, ' ', $lengthCodigoProductoGS1)"/>
						</xsl:call-template>
					</xsl:if>
					
					<xsl:if test="($codigoProductoGS1_schemeID = 'GTIN-14' and not($lengthCodigoProductoGS1 = 14))">										
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3201'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea 3: ', position(), ' ', $codigoProductoGS1_schemeID, ' ', $lengthCodigoProductoGS1)"/>
						</xsl:call-template>
					</xsl:if>

					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'3200'" />
						<xsl:with-param name="node" select="$codigoProductoGS1_schemeID" />
						<xsl:with-param name="regexp" select="'^(GTIN-8|GTIN-13|GTIN-14|GS1-128|DataBar GS1|DataMatrix GS1)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $codigoProductoGS1_schemeID)"/>
					</xsl:call-template>											
				</xsl:for-each>
			</xsl:for-each>		
			
			<xsl:variable name="sumValorUnitarioItem" select="sum(./cac:Price/cbc:PriceAmount)"/>			
			<xsl:for-each select="./cac:Price">
				<!-- 29. Valor unitario por ítem -->		
				<xsl:variable name="valorUnitarioItem" select="./cbc:PriceAmount"/>	
				<!-- 29. Moneda de Valor unitario por ítem -->		
				<xsl:variable name="valorUnitarioItem_currencyID" select="./cbc:PriceAmount/@currencyID"/>	
				
				<!-- 29. Valor unitario por ítem -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2369'" />
					<xsl:with-param name="node" select="$valorUnitarioItem" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $valorUnitarioItem)"/>
				</xsl:call-template>		
				
				<xsl:if test="($montoTributoLinea9996 &gt; 0) and ($valorUnitarioItem &gt; 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2640'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $montoTributoLinea9996, ' ',$valorUnitarioItem)" />
					</xsl:call-template>
				</xsl:if>			
				
				<xsl:if test="($valorUnitarioItem_currencyID) and ($tipoMoneda!=$valorUnitarioItem_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 4) ', position(), ' ', $tipoMoneda, ' ',$valorUnitarioItem_currencyID)" />
					</xsl:call-template>
				</xsl:if>			
			</xsl:for-each>													
						
			<xsl:for-each select="./cac:PricingReference">
				<xsl:for-each select="./cac:AlternativeConditionPrice">
					<!-- 30. Precio de venta unitario por item -->		
					<!-- 30. Valor referencial unitario por ítem en operaciones no onerosas -->	
					<xsl:variable name="precioVentaUnitarioItem" select="./cbc:PriceAmount"/>
					<!-- 30. Moneda Precio de venta unitario por item -->		
					<xsl:variable name="precioVentaUnitarioItem_currencyID" select="./cbc:PriceAmount/@currencyID"/>	
					<!-- 30. Código de precio -->		
					<xsl:variable name="codigoTipoPrecioItem" select="./cbc:PriceTypeCode"/>
					<xsl:variable name="codigoTipoPrecioItem_ListName" select="./cbc:PriceTypeCode/@listName"/>
					<xsl:variable name="codigoTipoPrecioItem_ListAgencyName" select="./cbc:PriceTypeCode/@listAgencyName"/>
					<xsl:variable name="codigoTipoPrecioItem_ListURI" select="./cbc:PriceTypeCode/@listURI"/>
	
					<!-- 30. Moneda debe ser la misma en todo el documento -->	
					<xsl:if test="($precioVentaUnitarioItem_currencyID) and not($tipoMoneda = $precioVentaUnitarioItem_currencyID)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2071'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: 5) ', position(), ' ', $tipoMoneda, ' ',$precioVentaUnitarioItem_currencyID)" />
						</xsl:call-template>
					</xsl:if>
																		
					<!-- 30. Precio de venta unitario por item -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2367'" />
						<xsl:with-param name="node" select="$precioVentaUnitarioItem" />
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $precioVentaUnitarioItem)"/>
					</xsl:call-template>
					
					<!-- 30. Valor referencial unitario por ítem en operaciones no onerosas -->
					<xsl:if test="not($sumMontoBase9996LineaSub &gt; 0) and ($codigoTipoPrecioItem = '02') and ($precioVentaUnitarioItem &gt; 0)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3224'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $sumMontoBase9996LineaSub, ' ',$codigoTipoPrecioItem, ' ',$precioVentaUnitarioItem)" />
						</xsl:call-template>
					</xsl:if>
	
					<!-- 30. Código de precio -->		
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2410'" />
						<xsl:with-param name="node" select="$codigoTipoPrecioItem"/>
						<xsl:with-param name="regexp" select="'^(01|02|03)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $codigoTipoPrecioItem)"/>
					</xsl:call-template>
						
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4252'" />
						<xsl:with-param name="node" select="$codigoTipoPrecioItem_ListName" />
						<xsl:with-param name="regexp" select="'^(Tipo de Precio)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: 8) ', position(), ' ', $codigoTipoPrecioItem_ListName)"/>
					</xsl:call-template>			
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4251'" />
						<xsl:with-param name="node" select="$codigoTipoPrecioItem_ListAgencyName" />
						<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: 8) ', position(), ' ', $codigoTipoPrecioItem_ListAgencyName)"/>
					</xsl:call-template>	
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4253'" />
						<xsl:with-param name="node" select="$codigoTipoPrecioItem_ListURI" />
						<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo16)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: 6) ', position(), ' ', $codigoTipoPrecioItem_ListURI)"/>
					</xsl:call-template>							
				</xsl:for-each>					
			</xsl:for-each>
															
			<!-- 30. Código de precio -->
			<xsl:variable name="countCodigoPrecio01" select="count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='01'])"/>
			<xsl:variable name="countCodigoPrecio02" select="count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='02'])"/>	
			<xsl:variable name="countCodigoPrecio03" select="count(./cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode[text()='03'])"/>	
			<xsl:if test="($countCodigoPrecio01 &gt; 1) or ($countCodigoPrecio02 &gt; 1) or ($countCodigoPrecio03 &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2409'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoPrecio01, ' ', $countCodigoPrecio02, ' ', $countCodigoPrecio03)" />
				</xsl:call-template>
			</xsl:if>
										
			<!-- /cac:TaxTotal -->																										
			<!-- 31. Monto total de impuestos por linea -->
			<xsl:variable name="countTaxTotal" select="count(./cac:TaxTotal)"/>
			<xsl:if test="($countTaxTotal = 0)">
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
			
			<!-- 32 - 33. Código de tributo por linea -->
			<xsl:if
				test="($countCodigoTributoLinea1000 &gt; 1) or ($countCodigoTributoLinea1016 &gt; 1) or ($countCodigoTributoLinea2000 &gt; 1) 
					or ($countCodigoTributoLinea9995 &gt; 1) or ($countCodigoTributoLinea9996 &gt; 1) or ($countCodigoTributoLinea9997 &gt; 1) 
					or ($countCodigoTributoLinea9998 &gt; 1) or ($countCodigoTributoLinea9999 &gt; 1) or ($countCodigoTributoLinea7152 &gt; 1) ">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3067'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoTributoLinea1000, ' ', $countCodigoTributoLinea2000, ' ', $countCodigoTributoLinea9999)"/>
				</xsl:call-template>
			</xsl:if>	
			
			<xsl:variable name="countCodigoTributoLineaFor" select="$countCodigoTributoLinea1000 + $countCodigoTributoLinea1016 + $countCodigoTributoLinea2000 + $countCodigoTributoLinea9995 + $countCodigoTributoLinea9996 + $countCodigoTributoLinea9997 + $countCodigoTributoLinea9998 + $countCodigoTributoLinea9999" />
			<xsl:variable name="sumaMontoBaseFor" select="sum(./cac:TaxTotal/cac:TaxSubtotal/cbc:TaxableAmount)" />
			<xsl:if test="($countCodigoTributoLineaFor &gt; 1)">
				<xsl:if test="($sumaMontoBaseFor &gt; 0) and not((($countCodigoTributoLinea1000 = 1) and (($countCodigoTributoLinea2000 = 1) or ($countCodigoTributoLinea9999 = 1)))
 					or (($countCodigoTributoLinea1016 = 1) and ($countCodigoTributoLinea9999 = 1)) 
 					or (($countCodigoTributoLinea9995 = 1) and ($countCodigoTributoLinea9999 = 1)) 
 					or (($countCodigoTributoLinea9996 = 1) and (($countCodigoTributoLinea2000 = 1) or ($countCodigoTributoLinea9999 = 1))) 
 					or (($countCodigoTributoLinea9997 = 1) and (($countCodigoTributoLinea2000 = 1) or ($countCodigoTributoLinea9999 = 1))) 
 					or (($countCodigoTributoLinea9998 = 1) and (($countCodigoTributoLinea2000 = 1) or ($countCodigoTributoLinea9999 = 1))))">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3223'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $sumaMontoBaseFor, ' ', $countCodigoTributoLineaFor)"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:if>	
													
			<xsl:if  test="($countCodigoTributoLinea1000=0) and ($countCodigoTributoLinea1016=0) and 
					($countCodigoTributoLinea9995=0) and ($countCodigoTributoLinea9996=0) and 
					($countCodigoTributoLinea9997=0) and ($countCodigoTributoLinea9998=0)">					
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3105'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $sumMontoBase1000LineaSub, ' ', $sumMontoBase1016LineaSub, ' ', $sumMontoBase9995LineaSub, ' ', $sumMontoBase9996LineaSub, ' ', $sumMontoBase9997LineaSub, ' ', $sumMontoBase9998LineaSub)"/>
				</xsl:call-template>
			</xsl:if>												
																																																						
			<!-- Afectacion al IGV por item - Sistema de ISC por item -->
			<xsl:for-each select="./cac:TaxTotal">			
				<!-- 31. Monto total de impuestos del ítem -->
				<!-- 31. Monto total de impuestos por linea -->
				<xsl:variable name="montoTotalImpuestosLinea" select="./cbc:TaxAmount"/>
				<!-- 31. Moneda Monto de IGV de la línea -->			
				<xsl:variable name="montoTotalImpuestosLinea_currencyID" select="./cbc:TaxAmount/@currencyID"/>
				
				<xsl:variable name="montoTributoLinea_1000" select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxAmount)"/>	
	    		<xsl:variable name="montoTributoLinea_1016" select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1016']/cbc:TaxAmount)"/>
	    		<xsl:variable name="montoTributoLinea_2000" select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxAmount)"/>	
	    		<xsl:variable name="montoTributoLinea_7152" select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='7152']/cbc:TaxAmount)"/>
	    		<xsl:variable name="montoTributoLinea_9999" select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxAmount)"/>

				<xsl:variable name="countTributoLinea_1000" select="count(./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])"/>	
				<xsl:variable name="countTributoLinea_1016" select="count(./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1016'])"/>	
				<xsl:variable name="countTributoLinea_9995" select="count(./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9995'])"/>	
				<xsl:variable name="countTributoLinea_9996" select="count(./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9996'])"/>	
				<xsl:variable name="countTributoLinea_2000" select="count(./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000'])"/>	
				<xsl:variable name="countTributoLinea_7152" select="count(./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='7152'])"/>	
				<xsl:variable name="countTributoLinea_9999" select="count(./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9999'])"/>	
								
				<!-- 31. Moneda debe ser la misma en todo el documento -->	
				<xsl:if test="($montoTotalImpuestosLinea_currencyID) and not($tipoMoneda = $montoTotalImpuestosLinea_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 6) ', position(), ' ', $tipoMoneda, ' ',$montoTotalImpuestosLinea_currencyID)" />
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
						
					<xsl:if test="(($countTributoLinea_1000 &gt; 0) or ($countTributoLinea_1016 &gt; 0) or 
						($countTributoLinea_2000 &gt; 0) or ($countTributoLinea_7152 &gt; 0) or ($countTributoLinea_9999 &gt; 0))">																						
						<xsl:if test="($tipoDocumentoDocumentoModificaGlobal = '01') ">			
						    <xsl:variable name="sumaMontoTributoLinea" select="$montoTributoLinea_1000 + $montoTributoLinea_1016 + $montoTributoLinea_2000 +$montoTributoLinea_7152 + $montoTributoLinea_9999"/>
						    <xsl:variable name="dif_MontoTotalImpuestosLinea" select="$montoTotalImpuestosLinea - $sumaMontoTributoLinea" />
							<xsl:if test="($dif_MontoTotalImpuestosLinea &lt; -1) or ($dif_MontoTotalImpuestosLinea &gt; 1)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3292'" />
									<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $dif_MontoTotalImpuestosLinea,'-', $montoTotalImpuestosLinea,'-',$sumaMontoTributoLinea,'-',$montoTributoLinea_1000,'-',$montoTributoLinea_1016,'-',$montoTributoLinea_2000,'-',$montoTributoLinea_7152, '-', $montoTributoLinea_9999)"/>
								</xsl:call-template>						
							</xsl:if>	
						</xsl:if>	
						
						<xsl:if test="not($tipoDocumentoDocumentoModificaGlobal = '01') ">			
						    <xsl:variable name="sumaMontoTributoLinea" select="$montoTributoLinea_1000 + $montoTributoLinea_1016 + $montoTributoLinea_2000 +$montoTributoLinea_7152 + $montoTributoLinea_9999"/>
						    <xsl:variable name="dif_MontoTotalImpuestosLinea" select="$montoTotalImpuestosLinea - $sumaMontoTributoLinea" />
							<xsl:if test="($dif_MontoTotalImpuestosLinea &lt; -1) or ($dif_MontoTotalImpuestosLinea &gt; 1)">					
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4293'" />
									<xsl:with-param name="warningMessage"
									select="concat('Error en la linea: ', position(), ' ', $dif_MontoTotalImpuestosLinea,'-', $montoTotalImpuestosLinea,'-',$sumaMontoTributoLinea,'-',$montoTributoLinea_1000,'-',$montoTributoLinea_1016,'-',$montoTributoLinea_2000,'-',$montoTributoLinea_7152, '-', $montoTributoLinea_9999)"/>
								</xsl:call-template>					
							</xsl:if>	
						</xsl:if>	
					</xsl:if>											
				</xsl:if>

				
				<!-- 32. Afectación al IGV por la línea -->	
				<!-- 32. Afectación IVAP por la línea -->	
				<!-- 33. Afectación del ISC por la línea -->	
				<!-- 33. Afectacion Otros Tributos -->				
				<xsl:for-each select="./cac:TaxSubtotal">	
					<!-- 32. - 33 Monto Base -->		
					<xsl:variable name="montoBase" select="./cbc:TaxableAmount"/>
					<!-- 32 - 33. Moneda TaxableAmount -->		
					<xsl:variable name="montoBase_currencyID" select="./cbc:TaxableAmount/@currencyID"/>	
					<!-- 35. Monto de IGV de la línea -->	
					<!-- 36. Monto de Otros Tributos -->	
					<xsl:variable name="montoIGVLinea" select="./cbc:TaxAmount"/>	
					<!-- 35. Moneda Monto de IGV de la línea -->		
					<xsl:variable name="montoIGVLinea_currencyID" select="./cbc:TaxAmount/@currencyID"/>
					<!-- 38. Impuesto al consumo de bolsas de plástico por ítem -->
					<xsl:variable name="cantidadBolsaPlasticoLinea" select="./cbc:BaseUnitMeasure"/>
					<xsl:variable name="cantidadBolsaPlasticoLinea_unitCode" select="./cbc:BaseUnitMeasure/@unitCode"/>
										
					<xsl:if test="($montoIGVLinea_currencyID) and not($tipoMoneda = $montoIGVLinea_currencyID)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2071'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: 7) ', position(), ' ', $tipoMoneda, ' ',$montoIGVLinea_currencyID)" />
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
					<xsl:if test="($montoBase_currencyID) and not($tipoMoneda = $montoBase_currencyID)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2071'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: 8) ', position(), ' ', $tipoMoneda, ' ',$montoBase_currencyID)" />
						</xsl:call-template>
					</xsl:if>	
					
					<xsl:variable name="countAfectacionIGVLinea" select="./cac:TaxCategory/cbc:TaxExemptionReasonCode"/>
					<xsl:for-each select="./cac:TaxCategory">
						<!-- 32. Tasa del IGV -->		
						<!-- 33. Tasa del tributo -->
						<xsl:variable name="tasaIGV" select="./cbc:Percent "/>	
						<!-- 35. Afectación al IGV por la línea -->		
						<xsl:variable name="afectacionIGVLinea" select="./cbc:TaxExemptionReasonCode"/>	
						<xsl:variable name="afectacionIGVLinea_ListAgencyName" select="./cbc:TaxExemptionReasonCode/@listAgencyName"/>
						<xsl:variable name="afectacionIGVLinea_ListName" select="./cbc:TaxExemptionReasonCode/@listName"/>
						<xsl:variable name="afectacionIGVLinea_ListURI" select="./cbc:TaxExemptionReasonCode/@listURI"/>
						<!-- 34. Tipo de sistema de ISC -->	
						<xsl:variable name="tipoSistemaISC" select="./cbc:TierRange"/>	
						<!-- 38. Impuesto al consumo de bolsas de plástico por ítem -->	
						<xsl:variable name="montoUnitarioLinea" select="./cbc:PerUnitAmount"/>	
											
						<!-- 35. Tasa del IGV -->	
						<!-- 36. Tasa del tributo -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'3102'" />
							<xsl:with-param name="node" select="$tasaIGV" />
							<xsl:with-param name="regexp" select="'^[0-9]{1,3}(\.[0-9]{1,5})?$'" />
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $tasaIGV)"/>
						</xsl:call-template>							
						
						<xsl:if test="($montoBase &gt; 0) and ($codigoTipoNotaCredito = '11') and not($afectacionIGVLinea = '40')">	
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'2642'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ' , $montoBase, ' ', $codigoTipoNotaCredito, ' ', $afectacionIGVLinea)"/>
								</xsl:call-template>
						</xsl:if>	
							
						<xsl:if test="($codigoTipoNotaCredito = '12') and not($afectacionIGVLinea = '17')">	
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'2644'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $codigoTipoNotaCredito, ' ', $afectacionIGVLinea)"/>
								</xsl:call-template>
						</xsl:if>	
							
						<xsl:if test="not($codigoTipoNotaCredito = '12') and ($afectacionIGVLinea = '17')">	
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3230'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $codigoTipoNotaCredito, ' ', $afectacionIGVLinea)"/>
								</xsl:call-template>
						</xsl:if>													
												
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4251'" />
							<xsl:with-param name="node" select="$afectacionIGVLinea_ListAgencyName" />
							<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 9) ', position(), ' ', $afectacionIGVLinea_ListAgencyName)"/>
						</xsl:call-template>	
										
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4252'" />
							<xsl:with-param name="node" select="$afectacionIGVLinea_ListName" />
							<xsl:with-param name="regexp" select="'^(Afectacion del IGV)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 9) ', position(), ' ', $afectacionIGVLinea_ListName)"/>
						</xsl:call-template>			
						
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4253'" />
							<xsl:with-param name="node" select="$afectacionIGVLinea_ListURI" />
							<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo07)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 7) ', position(), ' ', $afectacionIGVLinea_ListURI)"/>
						</xsl:call-template>					
				
						<xsl:for-each select="./cac:TaxScheme">						
							<!-- 32. Código de tributo por línea - Catálogo No. 05 -->	
							<!-- 33. Código de tributo por linea - Catálogo No. 05 -->	
							<xsl:variable name="codigoTributoLinea" select="./cbc:ID"/>								
							<xsl:variable name="codigoTributoLinea_schemeName" select="./cbc:ID/@schemeName" />
							<xsl:variable name="codigoTributoLinea_schemeAgencyName" select="./cbc:ID/@schemeAgencyName" />
							<xsl:variable name="codigoTributoLinea_schemeURI" select="./cbc:ID/@schemeURI" />
							<!-- 32. Nombre de tributo por línea - Catálogo No. 05 -->		
							<!-- 33. Nombre de tributo por línea - Catálogo No. 05 -->
							<xsl:variable name="nombreTributoLinea" select="./cbc:Name"/>	
							<!-- 32. Código internacional tributo por línea - Catálogo No. 05 -->	
							<!-- 33. Código internacional tributo por línea - Catálogo No. 05 -->		
							<xsl:variable name="codigoInternacionalTributoLinea" select="./cbc:TaxTypeCode"/>
							
							<xsl:if test="(not($codigoTributoLinea = '7152'))">	
								<xsl:call-template name="existValidateElementNotExist">
									<xsl:with-param name="errorCodeNotExist" select="'2992'"/>
									<xsl:with-param name="node" select="$tasaIGV"/>
									<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', position(), ' ', $tasaIGV)"/>
								</xsl:call-template>	
							</xsl:if>							
							
							<!-- 32. Afectación al IGV por la línea - Catálogo No. 07 -->	
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'2037'"/>
								<xsl:with-param name="node" select="$codigoTributoLinea"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea)"/>
							</xsl:call-template>	
																						
							<!-- 32. Código de tributo por línea - Catálogo No. 05  -->					
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'2036'" />
								<xsl:with-param name="node" select="$codigoTributoLinea"/>
								<xsl:with-param name="regexp" select="'^(1000|1016|2000|7152|9995|9996|9997|9998|9999)$'" />
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea)"/>
							</xsl:call-template>	
							
							<!-- 33. Tasa del IGV o  Tasa del IVAP -->
							<xsl:if test="not($codigoTributoLinea='7152')">
								<xsl:call-template name="existValidateElementNotExist">
									<xsl:with-param name="errorCodeNotExist" select="'2992'"/>
									<xsl:with-param name="node" select="$tasaIGV" />
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', position(), ' ', $tasaIGV)"/>
								</xsl:call-template>				
							</xsl:if>							
							
							<xsl:if test="($codigoTributoLinea='2000') and ($montoBase &gt; 0)">	
								<xsl:call-template name="existValidateElementNotExist">
									<xsl:with-param name="errorCodeNotExist" select="'2373'"/>
									<xsl:with-param name="node" select="$tipoSistemaISC" />
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', position(), ' ', $tipoSistemaISC)"/>
								</xsl:call-template>	
								
								<xsl:call-template name="regexpValidateElementIfExist">
									<xsl:with-param name="errorCodeValidate" select="'2199'" />
									<xsl:with-param name="node" select="$tipoSistemaISC"/>
									<xsl:with-param name="regexp" select="'^(01|02|03)$'" />
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', position(), ' ', $tipoSistemaISC)"/>
								</xsl:call-template>									
							</xsl:if>
							
							<xsl:if test="not($codigoTributoLinea = '2000')">
								<xsl:call-template name="existValidateElementExist">
									<xsl:with-param name="errorCodeNotExist" select="'3210'"/>
									<xsl:with-param name="node" select="$tipoSistemaISC" />
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', position(), ' ', $tipoSistemaISC)"/>
								</xsl:call-template>	
							</xsl:if>							

<!-- 							<xsl:if test="(($codigoTributoLinea = '2000') or ($codigoTributoLinea = '9999')) and ($codigoTipoNotaCredito = '11') ">	 -->
<!-- 								<xsl:call-template name="rejectCall"> -->
<!-- 									<xsl:with-param name="errorCode" select="'3100'" /> -->
<!-- 									<xsl:with-param name="errorMessage" -->
<!-- 										select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ', $codigoTipoNotaCredito, ' ', $montoBase)"/> -->
<!-- 								</xsl:call-template> -->
<!-- 							</xsl:if>						 -->
							
							<!-- 33. Importe del tributo de la línea -->
							<xsl:if test="($codigoTributoLinea='2000') and ($montoBase &gt; 0)">	
								<xsl:variable name="montoTributo" select="($montoBase * $tasaIGV) * 0.01"/>
						    	<xsl:variable name="dif_MontoIGVLinea" select="$montoIGVLinea - $montoTributo" />
								<xsl:if test="($dif_MontoIGVLinea &lt; -1) or ($dif_MontoIGVLinea &gt; 1)">
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'3108'" />
										<xsl:with-param name="errorMessage"
											select="concat('Error en la linea: ', position(), ' ', $dif_MontoIGVLinea, ' ', $montoIGVLinea, ' ', $montoTributo, ' ', $montoBase,' ', $tasaIGV)"/>
									</xsl:call-template>
								</xsl:if>
							</xsl:if>

							<!-- 33. Tasa del tributo -->
							<xsl:if test="($codigoTributoLinea = '2000') and ($montoBase &gt; 0) and ($tasaIGV=0)">	
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3104'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ', $montoBase, ' ', $tasaIGV)"/>
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
									<xsl:variable name="dif_MontoUnitarioLinea" select="$montoIGVLinea - $calcularImpuestosBolsas" />
										<xsl:if test="($dif_MontoUnitarioLinea &lt; -0.01) or ($dif_MontoUnitarioLinea &gt; 0.01)">									
										<xsl:call-template name="addWarning"> 
											<xsl:with-param name="warningCode" select="'4318'" />
											<xsl:with-param name="warningMessage"
												select="concat('Error en la linea: ', position(), ' ', $dif_MontoUnitarioLinea, ' ', $cantidadBolsaPlasticoLinea,' ', $montoUnitarioLinea,' ', $calcularImpuestosBolsas,' ', $montoIGVLinea)"/>
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
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: 5) ', $codigoTributoLinea_schemeName)"/>
							</xsl:call-template>		
							
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4256'" />
								<xsl:with-param name="node" select="$codigoTributoLinea_schemeAgencyName" />
								<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', $codigoTributoLinea_schemeAgencyName)"/>
							</xsl:call-template>
						
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4257'" />
								<xsl:with-param name="node" select="$codigoTributoLinea_schemeURI" />
								<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo05)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: 4) ', $codigoTributoLinea_schemeURI)"/>
							</xsl:call-template>	
							
							<!-- 32. Afectación al IGV por la línea -->		
							<xsl:if test="($codigoTributoLinea = '9999') and ($montoBase &gt; 0) ">																				
								<!-- 36. Monto de Otros Tributos -->		
								<xsl:variable name="montoTributo" select="($montoBase * $tasaIGV) * 0.01"/>
							    <xsl:variable name="dif_Monto_Otros_Tributos" select="$montoIGVLinea - $montoTributo" />
								<xsl:if test="($dif_Monto_Otros_Tributos &lt; -1) or ($dif_Monto_Otros_Tributos &gt; 1)">
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'3109'" />
										<xsl:with-param name="errorMessage"
											select="concat('Error en la linea: ', position(), ' ', $dif_Monto_Otros_Tributos, ' ', $montoIGVLinea, ' ', $montoTributo, ' ', $montoBase,' ', $tasaIGV)"/>
									</xsl:call-template>
								</xsl:if>																											
							</xsl:if>							
													
							<xsl:if test="not(($codigoTributoLinea='2000') or ($codigoTributoLinea='9999')) and ($montoBase &gt; 0) ">	
								<xsl:call-template name="existValidateElementNotExist">
									<xsl:with-param name="errorCodeNotExist" select="'2371'"/>
									<xsl:with-param name="node" select="$afectacionIGVLinea" />
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea)"/>
								</xsl:call-template>	
							</xsl:if>
							
							<xsl:if test="(($codigoTributoLinea='2000') or ($codigoTributoLinea='9999')) and ($afectacionIGVLinea)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3050'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $codigoTributoLinea, ' ',$afectacionIGVLinea)" />
								</xsl:call-template>
							</xsl:if>																					
							
							<xsl:if test="not(($codigoTributoLinea='2000') or ($codigoTributoLinea='9999')) and ($montoBase &gt; 0) ">	
								<xsl:call-template name="regexpValidateElementIfExist">
									<xsl:with-param name="errorCodeValidate" select="'2040'"/>
									<xsl:with-param name="node" select="$afectacionIGVLinea"/>
									<xsl:with-param name="regexp" select="'^(10|11|12|13|14|15|16|17|20|21|30|31|32|33|34|35|36|37|40)$'" />
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea, ' ',$codigoTributoLinea, ' ',$montoBase)"/>
								</xsl:call-template>
								
								<xsl:if test="($codigoTributoLinea = '1000')">	
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'2040'"/>
										<xsl:with-param name="node" select="$afectacionIGVLinea"/>
										<xsl:with-param name="regexp" select="'^(10)$'" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea, ' ',$codigoTributoLinea, ' ',$montoBase)"/>
									</xsl:call-template>
								</xsl:if>		
								
								<xsl:if test="($codigoTributoLinea = '9996')">	
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'2040'"/>
										<xsl:with-param name="node" select="$afectacionIGVLinea"/>
										<xsl:with-param name="regexp" select="'^(11|12|13|14|15|16|21|31|32|33|34|35|36|37|40)$'" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea, ' ',$codigoTributoLinea, ' ',$montoBase)"/>
									</xsl:call-template>
								</xsl:if>	

								<xsl:if test="($codigoTributoLinea = '1016')">	
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'2040'"/>
										<xsl:with-param name="node" select="$afectacionIGVLinea"/>
										<xsl:with-param name="regexp" select="'^(17)$'" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea, ' ',$codigoTributoLinea, ' ',$montoBase)"/>
									</xsl:call-template>
								</xsl:if>										

								<xsl:if test="($codigoTributoLinea = '9997')">	
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'2040'"/>
										<xsl:with-param name="node" select="$afectacionIGVLinea"/>
										<xsl:with-param name="regexp" select="'^(20)$'" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea, ' ',$codigoTributoLinea, ' ',$montoBase)"/>
									</xsl:call-template>
								</xsl:if>		

								<xsl:if test="($codigoTributoLinea = '9998')">	
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'2040'"/>
										<xsl:with-param name="node" select="$afectacionIGVLinea"/>
										<xsl:with-param name="regexp" select="'^(30)$'" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea, ' ',$codigoTributoLinea, ' ',$montoBase)"/>
									</xsl:call-template>
								</xsl:if>		

								<xsl:if test="($codigoTributoLinea = '9995')">	
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'2040'"/>
										<xsl:with-param name="node" select="$afectacionIGVLinea"/>
										<xsl:with-param name="regexp" select="'^(40)$'" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', position(), ' ', $afectacionIGVLinea, ' ',$codigoTributoLinea, ' ',$montoBase)"/>
									</xsl:call-template>
								</xsl:if>									
							</xsl:if>							
							
							<!-- 32. Monto de IGV/IVAP de la línea -->
							<xsl:if test="(($codigoTributoLinea = '9995') or ($codigoTributoLinea = '9997') or ($codigoTributoLinea = '9998')) and not($montoIGVLinea = 0)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3110'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: 1) ', position(), ' ', $montoIGVLinea, ' ', $codigoTributoLinea)"/>
								</xsl:call-template>
							</xsl:if>
							
							<xsl:if test="($codigoTributoLinea = '9996') and ($montoBase &gt; 0.06) and ($montoIGVLinea = 0)">
								<xsl:if test="($afectacionIGVLinea = '11') or ($afectacionIGVLinea = '12') or ($afectacionIGVLinea = '13')
 									or ($afectacionIGVLinea = '14') or ($afectacionIGVLinea = '15') or ($afectacionIGVLinea = '16') or ($afectacionIGVLinea = '17')"> 
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'3111'" />
										<xsl:with-param name="errorMessage"
											select="concat('Error en la linea: 1) ', position(), ' ', $afectacionIGVLinea, ' ', $montoIGVLinea, ' ', $montoBase, ' ', $codigoTributoLinea)"/>
									</xsl:call-template>
								</xsl:if>
							</xsl:if>							

							<xsl:if test="($codigoTributoLinea = '9996') and ($montoBase &gt; 0) and not($montoIGVLinea = 0)">
								<xsl:if test="($afectacionIGVLinea = '31') or ($afectacionIGVLinea = '32') or ($afectacionIGVLinea = '33')
 									or ($afectacionIGVLinea = '34') or ($afectacionIGVLinea = '35') or ($afectacionIGVLinea = '36')   
 									or ($afectacionIGVLinea = '37') or ($afectacionIGVLinea = '40') or ($afectacionIGVLinea = '21')"> 
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'3110'" />
										<xsl:with-param name="errorMessage"
											select="concat('Error en la linea: 2) ', position(), ' ', $afectacionIGVLinea, ' ', $montoIGVLinea, ' ', $montoBase, ' ', $codigoTributoLinea)"/>
									</xsl:call-template>
								</xsl:if>
							</xsl:if>							

							<xsl:if test="(($codigoTributoLinea = '1000') or ($codigoTributoLinea = '1016')) and ($montoBase &gt; 0.06) and ($montoIGVLinea = 0)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3111'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: 2) ', position(), ' ', $montoIGVLinea, ' ', $montoBase, ' ', $codigoTributoLinea)"/>
								</xsl:call-template>
							</xsl:if>						
							
							<xsl:if test="not (($tipoDocumentoDocumentoModificaGlobal = '30') or ($tipoDocumentoDocumentoModificaGlobal = '42'))">
								<xsl:if test="(($afectacionIGVLinea = '10') or($afectacionIGVLinea = '11') or ($afectacionIGVLinea = '12') or ($afectacionIGVLinea = '13') 
	 									or ($afectacionIGVLinea = '14') or ($afectacionIGVLinea = '15') or ($afectacionIGVLinea = '16') or ($afectacionIGVLinea = '17'))">	 
									<xsl:variable name="montoTributo" select="($montoBase * $tasaIGV) * 0.01"/>
							    	<xsl:variable name="dif_MontoIGVLinea" select="$montoIGVLinea - $montoTributo" />
									<xsl:if test="($dif_MontoIGVLinea &lt; -1) or ($dif_MontoIGVLinea &gt; 1)">
										<xsl:call-template name="rejectCall">
											<xsl:with-param name="errorCode" select="'3103'" />
											<xsl:with-param name="errorMessage"
												select="concat('Error en la linea: ', position(), ' ', $tipoDocumentoDocumentoModificaGlobal, ' ', $dif_MontoIGVLinea, ' ', $montoIGVLinea, ' ', $montoTributo, ' ', $montoBase,' ', $tasaIGV)"/>
										</xsl:call-template>
									</xsl:if>
								</xsl:if>
							</xsl:if>				
																	
							<!-- 32. Tasa del IGV -->
							<xsl:if test="($codigoTributoLinea = '9996') and ($montoBase &gt; 0) and ($tasaIGV = 0)">
								<xsl:if test="($afectacionIGVLinea = '11') or ($afectacionIGVLinea = '12') or ($afectacionIGVLinea = '13')
 									or ($afectacionIGVLinea = '14') or ($afectacionIGVLinea = '15') or ($afectacionIGVLinea = '16') or ($afectacionIGVLinea = '17')"> 
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'2993'" />
										<xsl:with-param name="errorMessage"
											select="concat('Error en la linea: ', position(), ' ',$codigoTributoLinea, ' ', $montoBase, ' ', $tasaIGV, ' ', $afectacionIGVLinea)"/>
									</xsl:call-template>
								</xsl:if>
							</xsl:if>							
							
							<xsl:if test="(($codigoTributoLinea = '1000') or ($codigoTributoLinea = '1016')) and ($montoBase &gt; 0) and ($tasaIGV = 0)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'2993'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ',$codigoTributoLinea, ' ', $montoBase, ' ', $tasaIGV)"/>
								</xsl:call-template>
							</xsl:if>								
																								
							<!-- 32. Nombre de tributo por línea - Catálogo No. 05 -->		
							<!-- 33. Nombre de tributo -->	
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'2996'"/>
								<xsl:with-param name="node" select="$nombreTributoLinea"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $nombreTributoLinea)"/>
							</xsl:call-template>	
							
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'3051'" />
								<xsl:with-param name="node" select="$nombreTributoLinea"/>
								<xsl:with-param name="regexp" select="'^(IGV|IVAP|ISC|ICBPER|EXP|GRA|EXO|INA|OTROS)$'" />
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', position(), ' ', $nombreTributoLinea)"/>
							</xsl:call-template>	
							
							<xsl:if test="(($codigoTributoLinea = '1000') and not($codigoInternacionalTributoLinea = 'VAT')) or
 									(($codigoTributoLinea = '1016') and not($codigoInternacionalTributoLinea = 'VAT')) or  
 									(($codigoTributoLinea = '2000') and not($codigoInternacionalTributoLinea = 'EXC')) or 
 									(($codigoTributoLinea = '9995') and not($codigoInternacionalTributoLinea = 'FRE')) or 
 									(($codigoTributoLinea = '9996') and not($codigoInternacionalTributoLinea = 'FRE')) or 
 									(($codigoTributoLinea = '9997') and not($codigoInternacionalTributoLinea = 'VAT')) or 
									(($codigoTributoLinea = '9998') and not($codigoInternacionalTributoLinea = 'FRE')) or 
 									(($codigoTributoLinea = '9999') and not($codigoInternacionalTributoLinea = 'OTH')) or
 									(($codigoTributoLinea = '7152') and not($codigoInternacionalTributoLinea = 'OTH'))"> 
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'2377'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $codigoInternacionalTributoLinea)"/>
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
			    <xsl:variable name="baseISCxLinea" select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID = '2000']/cbc:TaxableAmount)"/>
			    <xsl:variable name="baseISCxLineaCalculado" select="$valorVentaLinea + $tributoISCxLinea"/>
			    
			    <xsl:if test="($tipoDocumentoDocumentoModificaGlobal = '01') and ($baseISCxLinea &gt; 0)">
					<xsl:variable name="dif_BaseIGVIVAPxLinea" select="$baseISCxLineaCalculado - $montoBaseIGVxLinea" />
					<xsl:if test="($dif_BaseIGVIVAPxLinea &lt; -1) or ($dif_BaseIGVIVAPxLinea &gt; 1)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3272'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea 1) ', position(), '-', $dif_BaseIGVIVAPxLinea, ' ', $montoBaseIGVxLinea, '-', $baseISCxLineaCalculado, '-', $valorVentaLinea, '-', $tributoISCxLinea, '-', $baseISCxLinea)"/>
						</xsl:call-template>						
					</xsl:if>
<!-- 							<xsl:call-template name="addWarning"> -->
<!-- 								<xsl:with-param name="warningCode" select="'00001'" /> -->
<!-- 								<xsl:with-param name="warningMessage" -->
<!-- 									select="concat('Error en la linea 1) ', position(), '-', $dif_BaseIGVIVAPxLinea, ' ', $montoBaseIGVxLinea, '-', $baseISCxLineaCalculado, '-', $valorVentaLinea, '-', $tributoISCxLinea, '-', $baseISCxLinea)"/> -->
<!-- 							</xsl:call-template>					 -->
				</xsl:if>	
				
			    <xsl:if test="not($tipoDocumentoDocumentoModificaGlobal = '01') and ($baseISCxLinea &gt; 0)">
					<xsl:variable name="dif_BaseIGVIVAPxLinea" select="$baseISCxLineaCalculado - $montoBaseIGVxLinea" />
					<xsl:if test="($dif_BaseIGVIVAPxLinea &lt; -1) or ($dif_BaseIGVIVAPxLinea &gt; 1)">
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="'4294'" />
							<xsl:with-param name="warningMessage"
								select="concat('Error en la linea 1) ', position(), '-', $dif_BaseIGVIVAPxLinea, ' ', $montoBaseIGVxLinea, '-', $baseISCxLineaCalculado, '-', $valorVentaLinea, '-', $tributoISCxLinea, '-', $baseISCxLinea)"/>
						</xsl:call-template>					
					</xsl:if>
				</xsl:if>	

				<xsl:if test="($tipoDocumentoDocumentoModificaGlobal = '01') and ($baseISCxLinea = 0)">
			    	<xsl:if test="(($countTributoLinea_1000 &gt; 0) or ($countTributoLinea_1016 &gt; 0))">							
			    		<xsl:variable name="sumBaseIGV_IVAPLinea" select="$montoBase1000 + $montoBase1016"/>
						<xsl:variable name="dif_BaseISCLinea" select="$sumBaseIGV_IVAPLinea - $valorVentaLinea" />						
						<xsl:if test="($dif_BaseISCLinea &lt; -0.001) or ($dif_BaseISCLinea &gt; 0.001)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3272'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea 2) ', position(), '-', $dif_BaseISCLinea, '-', $sumBaseIGV_IVAPLinea, '-', $valorVentaLinea)"/>
							</xsl:call-template>						
						</xsl:if>
<!-- 							<xsl:call-template name="addWarning"> -->
<!-- 								<xsl:with-param name="warningCode" select="'00002'" /> -->
<!-- 								<xsl:with-param name="warningMessage" -->
<!-- 									select="concat('Error en la linea 2) ', position(), '-', $dif_BaseISCLinea, '-', $sumBaseIGV_IVAPLinea, '-', $valorVentaLinea, '-',$montoBase1000, '-',$montoBase1016)"/> -->
<!-- 							</xsl:call-template>							 -->						
					</xsl:if>
				</xsl:if>	
								
				<xsl:if test="not($tipoDocumentoDocumentoModificaGlobal = '01') and ($baseISCxLinea = 0)">
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
									
			<!-- 34. Valor de venta por línea -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2370'"/>
				<xsl:with-param name="node" select="$valorVentaLinea"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $valorVentaLinea)"/>
			</xsl:call-template>	
					
			<xsl:if test="($tipoDocumentoDocumentoModificaGlobal = '01') and ($sumMontoBase9996LineaSub &gt; 0) ">	
				<xsl:variable name="totalImporteCalculado" select="number($valorReferencialUnitarioItemOperacionesGratuitas) * number($cantidadUnidadesLinea)"/>	
				<xsl:variable name="dif_TotalImporte" select="$valorVentaLinea - $totalImporteCalculado" />			
				<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3271'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 1) ', $dif_TotalImporte, ' ', $valorVentaLinea, ' ', $totalImporteCalculado, ' ',$valorReferencialUnitarioItemOperacionesGratuitas, ' ',$cantidadUnidadesLinea)"/>
					</xsl:call-template>
				</xsl:if>	
			</xsl:if>			
			
			<xsl:if test="($tipoDocumentoDocumentoModificaGlobal = '01') and not($sumMontoBase9996LineaSub &gt; 0) ">	
				<xsl:variable name="totalImporteCalculado" select="number($valorUnitarioItem) * number($cantidadUnidadesLinea)"/>	
				<xsl:variable name="dif_TotalImporte" select="$valorVentaLinea - $totalImporteCalculado" />			
				<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3271'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 2) ', $dif_TotalImporte, ' ', $valorVentaLinea, ' ', $totalImporteCalculado, ' ',$valorUnitarioItem, ' ',$cantidadUnidadesLinea)"/>
					</xsl:call-template>
				</xsl:if>	
			</xsl:if>	
			
			<xsl:if test="not($tipoDocumentoDocumentoModificaGlobal = '01') and ($sumMontoBase9996LineaSub &gt; 0) ">	
				<xsl:variable name="totalImporteCalculado" select="number($valorReferencialUnitarioItemOperacionesGratuitas) * number($cantidadUnidadesLinea)"/>	
				<xsl:variable name="dif_TotalImporte" select="$valorVentaLinea - $totalImporteCalculado" />			
				<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4288'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: 1) ', $dif_TotalImporte, ' ', $valorVentaLinea, ' ', $totalImporteCalculado, ' ',$valorReferencialUnitarioItemOperacionesGratuitas, ' ',$cantidadUnidadesLinea)"/>
					</xsl:call-template>					
				</xsl:if>	
			</xsl:if>			
			
			<xsl:if test="not($tipoDocumentoDocumentoModificaGlobal = '01') and not($sumMontoBase9996LineaSub &gt; 0) ">	
				<xsl:variable name="totalImporteCalculado" select="number($valorUnitarioItem) * number($cantidadUnidadesLinea)"/>	
				<xsl:variable name="dif_TotalImporte" select="$valorVentaLinea - $totalImporteCalculado" />			
				<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4288'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: 2) ', $dif_TotalImporte, ' ', $valorVentaLinea, ' ', $totalImporteCalculado, ' ',$valorUnitarioItem, ' ',$cantidadUnidadesLinea)"/>
					</xsl:call-template>					
				</xsl:if>	
			</xsl:if>								
								
		    <xsl:variable name="ValorVentaUnitarioxItem" select="./cac:Price/cbc:PriceAmount"/>
		    <xsl:variable name="ImpuestosItem" select="./cac:TaxTotal/cbc:TaxAmount"/>
		    <xsl:variable name="DsctosNoAfectanBI" select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode = '01']/cbc:Amount)"/>
		    <xsl:variable name="DsctosAfectanBI" select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode = '00']/cbc:Amount)"/>
		    <xsl:variable name="CargosNoAfectanBI" select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode = '48']/cbc:Amount)"/>
		    <xsl:variable name="CargosAfectanBI" select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode = '47']/cbc:Amount)"/>
		    <xsl:variable name="PrecioUnitarioxItem" select="sum(./cac:PricingReference/cac:AlternativeConditionPrice[cbc:PriceTypeCode = '01']/cbc:PriceAmount)"/>
		    <xsl:variable name="PrecioReferencialUnitarioxItem" select="sum(./cac:PricingReference/cac:AlternativeConditionPrice[cbc:PriceTypeCode = '02']/cbc:PriceAmount)"/>
		    <xsl:variable name="PrecioUnitarioCalculado" select="($valorVentaLinea + $ImpuestosItem - $DsctosNoAfectanBI + $CargosNoAfectanBI) div ( $cantidadUnidadesLinea)"/>
		    <xsl:variable name="ValorVentaReferencialxItemCalculado" select="($PrecioReferencialUnitarioxItem * $cantidadUnidadesLinea) - $DsctosAfectanBI + $CargosAfectanBI"/>
    		<xsl:variable name="ValorVentaxItemCalculado" select="($ValorVentaUnitarioxItem * $cantidadUnidadesLinea) - $DsctosAfectanBI + $CargosAfectanBI"/>
			
			<!-- 34. Moneda debe ser la misma en todo el documento -->		
			<xsl:if test="($valorVentaLinea_currencyID) and not($tipoMoneda = $valorVentaLinea_currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: 9) ', position(), ' ', $tipoMoneda, ' ',$valorVentaLinea_currencyID)" />
				</xsl:call-template>
			</xsl:if>				
			
			<xsl:variable name="codigoSUNAT" select="./cac:Item/cac:CommodityClassification/cbc:ItemClassificationCode"/>
			<xsl:variable name="countCodigoConcepto7001For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7001'])"/>
			<xsl:variable name="countCodigoConcepto7002For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7002'])"/>
			<xsl:variable name="countCodigoConcepto7003For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7003'])"/>
			<xsl:variable name="countCodigoConcepto7004For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7004'])"/>
			<xsl:variable name="countCodigoConcepto7005For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7005'])"/>
			<xsl:variable name="countCodigoConcepto7006For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7006'])"/>
			<xsl:variable name="countCodigoConcepto7007For" select="count(./cac:Item/cac:AdditionalItemProperty/cbc:NameCode[text() = '7007'])"/>						
			<xsl:variable name="countCodigoConcepto7002_03" select="count(./cac:Item/cac:AdditionalItemProperty[cbc:Value[text() = '3']]/cbc:NameCode[text() = '7002'])"/>	
			
			<xsl:for-each select="./cac:Item">			

				<xsl:for-each select="./cac:AdditionalItemProperty">
					<!-- 47 - 52. Nombre del concepto -->
					<xsl:variable name="nombreConcepto" select="./cbc:Name"/>
					<!-- 47 - 52. Código del concepto -->
			    	<xsl:variable name="codigoConcepto" select="./cbc:NameCode"/>
					<xsl:variable name="codigoConcepto_listName" select="./cbc:NameCode/@listName"/>
			    	<xsl:variable name="codigoConcepto_listAgencyName" select="./cbc:NameCode/@listAgencyName"/>
			    	<xsl:variable name="codigoConcepto_listURI" select="./cbc:NameCode/@listURI"/>
			    	<!-- 47 - 52. Valor del concepto -->
			    	<xsl:variable name="codigoConcepto_Value" select="./cbc:Value"/>	
			    	
					<!-- 47 -52. Nombre del concepto -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'4235'"/>
						<xsl:with-param name="node" select="$nombreConcepto" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $nombreConcepto)"/>
					</xsl:call-template>
					
					<!-- 47 - 52. Código del concepto -->				
					<xsl:if test="$codigoSUNAT = '84121901'">					
						<xsl:if test="($countCodigoConcepto7001For = 0)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3150'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoSUNAT, ' ', $countCodigoConcepto7001For)"/>
							</xsl:call-template>	
						</xsl:if>
						
						<xsl:if test="($countCodigoConcepto7002_03 &gt; 0) and ($countCodigoConcepto7003For=0)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3151'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoSUNAT, ' ', $countCodigoConcepto7002For, ' ', $countCodigoConcepto7003For)"/>
							</xsl:call-template>	
						</xsl:if>
																		
						<xsl:if test="($countCodigoConcepto7004For = 0)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3152'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoSUNAT, ' ', $countCodigoConcepto7004For)"/>
							</xsl:call-template>	
						</xsl:if>	
						
						<xsl:if test="($countCodigoConcepto7005For = 0)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3153'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoSUNAT, ' ', $countCodigoConcepto7005For)"/>
							</xsl:call-template>	
						</xsl:if>		

						<xsl:if test="($countCodigoConcepto7002_03 &gt; 0) and ($countCodigoConcepto7006For=0)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3154'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoSUNAT, ' ', $countCodigoConcepto7002For, ' ', $countCodigoConcepto7006For)"/>
							</xsl:call-template>	
						</xsl:if>
						
						<xsl:if test="($countCodigoConcepto7002_03 &gt; 0) and ($countCodigoConcepto7007For=0)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'3155'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ', $codigoSUNAT, ' ', $countCodigoConcepto7002For, ' ', $countCodigoConcepto7007For)"/>
							</xsl:call-template>	
						</xsl:if>																																						      				      
				    </xsl:if>
					
					<!-- 47 - 52. Código del concepto -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4252'" />
						<xsl:with-param name="node" select="$codigoConcepto_listName" />
						<xsl:with-param name="regexp" select="'^(Propiedad del item)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: 10) ', position(), ' ', $codigoConcepto_listName)"/>
					</xsl:call-template>			
		
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4251'" />
						<xsl:with-param name="node" select="$codigoConcepto_listAgencyName" />
						<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: 10) ', position(), ' ', $codigoConcepto_listAgencyName)"/>
					</xsl:call-template>	
		
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4253'" />
						<xsl:with-param name="node" select="$codigoConcepto_listURI" />
						<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo55)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: 8) ', position(), ' ', $codigoConcepto_listURI)"/>
					</xsl:call-template>	
					
					<xsl:if test="($codigoConcepto = '7001') or ($codigoConcepto = '7002') or ($codigoConcepto = '7003') or
						($codigoConcepto = '7004') or ($codigoConcepto = '7005') or ($codigoConcepto = '7006') or 
						($codigoConcepto = '7007') or ($codigoConcepto = '7008') or ($codigoConcepto = '7009') or 
						($codigoConcepto = '7010') or ($codigoConcepto = '7011') or ($codigoConcepto='7012')">					
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3064'"/>
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $codigoConcepto, ' ', $codigoConcepto_Value)"/>
						</xsl:call-template>
					</xsl:if>
					
					<xsl:if test="($codigoConcepto = '7013') or ($codigoConcepto = '7015') or ($codigoConcepto = '7016') ">					
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3064'"/>
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $codigoConcepto, ' ', $codigoConcepto_Value)"/>
						</xsl:call-template>
					</xsl:if>
					
					<xsl:if test="$codigoConcepto = '7001'">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<xsl:with-param name="regexp" select="'^(0|1|2)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea 1): ', position(), ' ', $codigoConcepto, ' ', $codigoConcepto_Value)"/>
						</xsl:call-template>					
			       </xsl:if>

				   <xsl:if test="$codigoConcepto = '7002'">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<xsl:with-param name="regexp" select="'^(0|1|2|3)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea 2): ', position(), ' ', $codigoConcepto, ' ', $codigoConcepto_Value)"/>
						</xsl:call-template>					
			       </xsl:if>			 
			      
				   <xsl:if test="$codigoConcepto = '7003'">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,49}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea 3): ', position(), ' ', $codigoConcepto, ' ', $codigoConcepto_Value)"/>
						</xsl:call-template>					
			       </xsl:if>	
			      
				   <xsl:if test="$codigoConcepto = '7004'">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,49}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea 4): ', position(), ' ', $codigoConcepto, ' ', $codigoConcepto_Value)"/>
						</xsl:call-template>					
			       </xsl:if>			 
			      
				   <xsl:if test="$codigoConcepto = '7005'">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'" />
							<xsl:with-param name="node" select="$codigoConcepto_Value" />
							<xsl:with-param name="regexp" select="'^[0-9]{4}-[0-9]{2}-[0-9]{2}?$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea 5): ', position(), ' ', $codigoConcepto, ' ', $codigoConcepto_Value)"/>
						</xsl:call-template>					
			       </xsl:if>	
			    	
				    <!-- UBIGEO -->		      
					<xsl:if test="$codigoConcepto = '7007'">
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4280'" />
								<xsl:with-param name="node" select="$codigoConcepto_Value" />
								<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,199}$'"/>
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea 6): ', position(), ' ', $codigoConcepto, ' ', $codigoConcepto_Value)"/>
							</xsl:call-template>					
				   	</xsl:if>	
				   	
			    	<!-- 57. Monto del crédito otorgado (capital) -->
					<xsl:if test="($codigoConcepto='7012')">					
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'"/>
							<xsl:with-param name="node" select="$codigoConcepto_Value"/>
							<xsl:with-param name="regexp" select="'^[0-9]{1,15}(\.[0-9]{1,2})?$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $codigoConcepto, ' ',$codigoConcepto_Value)"/>
						</xsl:call-template>	
					</xsl:if>						   	
				   	
			    	<!-- 58. Número de póliza -->
					<xsl:if test="($codigoConcepto='7013')">					
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'"/>
							<xsl:with-param name="node" select="$codigoConcepto_Value"/>
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{1,50}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $codigoConcepto, ' ',$codigoConcepto_Value)"/>
						</xsl:call-template>	
					</xsl:if>		
					
			    	<!-- 59. Tipo de seguro -->
					<xsl:if test="($codigoConcepto='7015')">					
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'"/>
							<xsl:with-param name="node" select="$codigoConcepto_Value"/>
							<xsl:with-param name="regexp" select="'^(0|1|2)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $codigoConcepto, ' ',$codigoConcepto_Value)"/>
						</xsl:call-template>	
					</xsl:if>		
					
					<!-- 60. Suma asegurada / alcance de cobertura o monto  -->
					<xsl:if test="($codigoConcepto='7016')">					
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4280'"/>
							<xsl:with-param name="node" select="$codigoConcepto_Value"/>
							<xsl:with-param name="regexp" select="'^[0-9]{1,15}(\.[0-9]{1,2})?$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $codigoConcepto, ' ',$codigoConcepto_Value)"/>
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
			    		<!-- 61. Fecha de inicio de vigencia de cobertura -->					
						<xsl:variable name="fechaInicio" select="./cbc:StartDate"/>	
						<!-- 63. Fecha de término de vigencia de cobertura -->
						<xsl:variable name="fechaFin" select="./cbc:EndDate"/>	
						
						<xsl:if test="($codigoConcepto='7014')">	
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
									select="concat('Error en la linea: ', position(), ' ', $codigoConcepto, ' ', $fechaInicio)"/>
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
									select="concat('Error en la linea: ', position(), ' ', $codigoConcepto, ' ', $fechaFin)"/>
							</xsl:call-template>							
						</xsl:if>															       
			      	</xsl:for-each>	
				</xsl:for-each>	
			</xsl:for-each>																			
		</xsl:for-each>													 	
    			 
		<xsl:for-each select="cac:LegalMonetaryTotal">
			<!-- 43. Total otros Cargos (Que no afectan la base) -->
<!-- 			<xsl:variable name="totalOtrosCargos" select="./cbc:ChargeTotalAmount"/> -->
			<xsl:variable name="totalOtrosCargos">	
				<xsl:choose>
    				<xsl:when test="count(./cbc:ChargeTotalAmount) &gt; 0">
    					<xsl:value-of select="sum(./cbc:ChargeTotalAmount)"/></xsl:when>
    				<xsl:otherwise>0</xsl:otherwise>
  				</xsl:choose>       
			</xsl:variable>			
			<xsl:variable name="totalOtrosCargos_currencyID" select="./cbc:ChargeTotalAmount/@currencyID"/>		
			<!-- 44. Importe total -->
			<xsl:variable name="importeTotal" select="./cbc:PayableAmount"/>								
			<xsl:variable name="importeTotal_currencyID" select="./cbc:PayableAmount/@currencyID"/>		
			<!-- 45. Monto para Redondeo del Importe Total -->
<!-- 			<xsl:variable name="montoRedondeoImporteTotal" select="./cbc:PayableRoundingAmount"/> -->
			<xsl:variable name="montoRedondeoImporteTotal">	
				<xsl:choose>
    				<xsl:when test="count(./cbc:PayableRoundingAmount) &gt; 0">
    					<xsl:value-of select="sum(./cbc:PayableRoundingAmount)"/></xsl:when>
    				<xsl:otherwise>0</xsl:otherwise>
  				</xsl:choose>       
			</xsl:variable>
			<xsl:variable name="montoRedondeoImporteTotal_currencyID" select="./cbc:PayableRoundingAmount/@currencyID"/>

			<!-- 43. Total otros Cargos (Que no afectan la base) -->
			<xsl:if test="$totalOtrosCargos">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2064'"/>
					<xsl:with-param name="node" select="$totalOtrosCargos"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $totalOtrosCargos)"/>
				</xsl:call-template>
	
				<!-- Moneda debe ser la misma en todo el documento -->	
				<xsl:if test="($totalOtrosCargos_currencyID) and not($tipoMoneda=$totalOtrosCargos_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 10) ', position(), ' ', $tipoMoneda, ' ',$totalOtrosCargos_currencyID)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>		
			
			<!-- 44. Importe total -->
			<xsl:if test="$importeTotal">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2062'"/>
					<xsl:with-param name="node" select="$importeTotal"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $importeTotal)"/>
				</xsl:call-template>

				<xsl:if test="($tipoDocumentoDocumentoModificaGlobal = '01')">			
					<xsl:variable name="totalImporte_1" select="$sumTotalValor1000Global + $sumTotalValor9998Global + $sumTotalValor9997Global + $sumTotalValor9995Global + $sumTotalValor1016Global" />		
					<xsl:variable name="totalImporte_2" select="$sumMontoTributo1000Global + $sumMontoTributo2000Global + $sumMontoTributo1016Global + $sumMontoTributo9999Global + $sumMontoTributo7152Global + $totalOtrosCargos + $montoRedondeoImporteTotal" />		
					<xsl:variable name="totalImporteCalculado" select="format-number($totalImporte_1 + $totalImporte_2,'#.##')" />
					<xsl:variable name="dif_TotalImporte" select="$importeTotal - number($totalImporteCalculado)" />
					<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3280'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $importeTotal, ' ', $totalImporteCalculado, ' ', $totalImporte_2, ' ', $totalImporte_1)"/>
						</xsl:call-template>				
					</xsl:if>			
				</xsl:if>				

				<xsl:if test="not($tipoDocumentoDocumentoModificaGlobal = '01')">			
					<xsl:variable name="totalImporte_1" select="$sumTotalValor1000Global + $sumTotalValor9998Global + $sumTotalValor9997Global + $sumTotalValor9995Global + $sumTotalValor1016Global" />		
					<xsl:variable name="totalImporte_2" select="$sumMontoTributo1000Global + $sumMontoTributo2000Global + $sumMontoTributo1016Global + $sumMontoTributo9999Global + $sumMontoTributo7152Global + $totalOtrosCargos + $montoRedondeoImporteTotal" />		
					<xsl:variable name="totalImporteCalculado" select="format-number($totalImporte_1 + $totalImporte_2,'#.##')" />
					<xsl:variable name="dif_TotalImporte" select="$importeTotal - number($totalImporteCalculado)" />
					<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="'4312'" />
							<xsl:with-param name="warningMessage"
								select="concat('Error en la linea: ', $dif_TotalImporte, ' ', $importeTotal, ' ', $totalImporteCalculado, ' ', $totalImporte_2, ' ', $totalImporte_1)"/>
						</xsl:call-template>									
					</xsl:if>			
				</xsl:if>	
				
				<xsl:if test="($codigoTipoNotaCredito = '13')">
					<xsl:if test="not($importeTotal = 0)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3315'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $codigoTipoNotaCredito , ' ',$importeTotal)" />
						</xsl:call-template>
					</xsl:if>				
				</xsl:if>
				
				<!-- Moneda debe ser la misma en todo el documento -->	
				<xsl:if test="($importeTotal_currencyID) and not($tipoMoneda=$importeTotal_currencyID)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: 11) ', position(), ' ', $tipoMoneda, ' ',$importeTotal_currencyID)" />
					</xsl:call-template>
				</xsl:if>								
			</xsl:if>	
			
			<!-- 45. Monto para Redondeo del Importe Total -->
			<xsl:if test="($tipoDocumentoDocumentoModificaGlobal = '01') and 
					($montoRedondeoImporteTotal)">
				<xsl:if test="($montoRedondeoImporteTotal &lt; -1) or ($montoRedondeoImporteTotal &gt; 1)">	
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3303'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $montoRedondeoImporteTotal)" />
					</xsl:call-template>				
<!-- 					<xsl:call-template name="addWarning"> -->
<!-- 						<xsl:with-param name="warningCode" select="'4314'" /> -->
<!-- 						<xsl:with-param name="warningMessage" -->
<!-- 							select="concat('Error en la linea: ', position(), ' ', $montoRedondeoImporteTotal)" /> -->
<!-- 					</xsl:call-template>				 -->
				</xsl:if>			
			</xsl:if>
			
			<xsl:if test="not($tipoDocumentoDocumentoModificaGlobal = '01') and 
					($montoRedondeoImporteTotal)">
				<xsl:if test="($montoRedondeoImporteTotal &lt; -1) or ($montoRedondeoImporteTotal &gt; 1)">				
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4314'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), ' ', $montoRedondeoImporteTotal)" />
					</xsl:call-template>				
				</xsl:if>			
			</xsl:if>
			
			<xsl:if test="($montoRedondeoImporteTotal_currencyID) and not($tipoMoneda = $montoRedondeoImporteTotal_currencyID)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2071'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: 12) ', position(), ' ', $tipoMoneda, ' ',$montoRedondeoImporteTotal_currencyID)" />
				</xsl:call-template>					
			</xsl:if>						
		</xsl:for-each>	

		<xsl:for-each select="cbc:Note">	
			<!-- 56. Código de leyenda -->
			<xsl:variable name="codigoLeyenda" select="./@languageLocaleID" />		
			<!-- 56. Descripción de la leyenda -->
			<xsl:variable name="descripcionLeyenda" select="." />
     						
			<!-- 52. Descripción de la leyenda -->		
			<xsl:call-template name="existAndRegexpValidateElement">
			     <xsl:with-param name="errorCodeNotExist" select="'3006'"/>
			     <xsl:with-param name="errorCodeValidate" select="'3006'"/>
			     <xsl:with-param name="node" select="$descripcionLeyenda"/>
			     <xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,199}$'"/>
			     <xsl:with-param name="descripcion"
			     	select="concat('Error en la linea: ', position(), ' ', $descripcionLeyenda)"/>
		   </xsl:call-template>		  
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'3027'" />
				<xsl:with-param name="node" select="$codigoLeyenda" />
				<xsl:with-param name="regexp" select="'^(1000|1002|2000|2001|2002|2003|2004|2005|2006|2007|2008|2009|2010|2011)$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $codigoLeyenda)"/>
			</xsl:call-template>		    
		</xsl:for-each>		
			
		<xsl:variable name="countFormaPago" select="count(cac:PaymentTerms/cbc:ID[text()='FormaPago'])" />
		<xsl:variable name="countCredito" select="count(cac:PaymentTerms[cbc:ID[text()='FormaPago']]/cbc:PaymentMeansID[text()='Credito'])" />
	|	
		<xsl:variable name="countCuotaString">	
			<xsl:choose>
				<xsl:when test="($countCredito &gt; 0)">					
					<xsl:for-each select="cac:PaymentTerms">
						<xsl:variable name="codigoBienServicio" select="./cbc:PaymentMeansID"/>
						<xsl:variable name="countCodigoBienServicio" select="count(substring($codigoBienServicio,1,5)='Cuota')" />
				 		<xsl:if test="(substring($codigoBienServicio,1,5)='Cuota')"> 
							<xsl:value-of select="sum($countCodigoBienServicio)"/>
						</xsl:if>	
					</xsl:for-each>
				</xsl:when>	
			</xsl:choose>       
		</xsl:variable>	
		
		<xsl:variable name="countCuota" select="string-length($countCuotaString)" />		
		<xsl:variable name="sumaCredito" select="sum(cac:PaymentTerms[cbc:ID[text()='FormaPago']][cbc:PaymentMeansID[text()='Credito']]/cbc:Amount)" />					
		<xsl:variable name="sumaCreditoCuota" select="sum(cac:PaymentTerms[cbc:ID[text()='FormaPago']]/cbc:Amount)" />	
<!-- 		<xsl:variable name="sumaCuota" select="$sumaCreditoCuota - $sumaCredito" />	 -->
		<xsl:variable name="sumaCuotaResta" select="$sumaCreditoCuota - $sumaCredito" />		
		<xsl:variable name="sumaCuota" select="format-number($sumaCuotaResta,'#.##')" />
				
		<xsl:if test="($fasefechaEmision = 0) or ($fasefechaEmision = 2) or ($fasefechaEmision = 3)">
			<xsl:if test="($codigoTipoNotaCredito = '13')">
				<xsl:if test="($countFormaPago = 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3257'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error countFormaPago: ', $countFormaPago)"/>
					</xsl:call-template>
				</xsl:if>		
			</xsl:if>
			
			<xsl:if test="($tipoDocumentoIdentidadReceptorComprobante = '6')">	
				<xsl:if test="($countCredito &gt; 0)">
					<xsl:if test="($countCuota = 0)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3249'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error ', $countCredito, ' - ', $countCuota)"/>
						</xsl:call-template>
					</xsl:if>							
				</xsl:if>	
			</xsl:if>
			
			<xsl:if test="($countCuota &gt; 0)">
				<xsl:if test="($countCredito = 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3252'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error ', $countCredito, ' - ', $countCuota)"/>
					</xsl:call-template>
				</xsl:if>							
			</xsl:if>
		</xsl:if>				

		<!-- Información Adicional  - Detracciones -->
		<xsl:for-each select="cac:PaymentTerms">
			<!-- 63. Monto total incluido la percepción -->
			<!-- 175. Indicador-->
			<!-- 176. Monto del pago único o de las cuotas -->
			<xsl:variable name="indicadorPaymentTerms" select="./cbc:ID" />		
			<!-- 88. Código del Bien o Servicio Sujeto a Detracción -->
			<!-- 175. Forma de pago -->
			<!-- 177. Identificador de la cuota -->
			<xsl:variable name="codigoBienServicio" select="./cbc:PaymentMeansID"/>
			<xsl:variable name="codigoBienServicio_schemeName" select="./cbc:PaymentMeansID/@schemeName"/>
			<xsl:variable name="codigoBienServicio_schemeAgencyName" select="./cbc:PaymentMeansID/@schemeAgencyName"/>
			<xsl:variable name="codigoBienServicio_schemeURI" select="./cbc:PaymentMeansID/@schemeURI"/>
			<!-- 90. Monto y Porcentaje de la detracción -->
			<!-- 90. Monto de detraccion -->
			<!-- 175. Monto neto pendiente de pago -->
			<!-- 177. Monto del pago único o de las cuotas -->
			<xsl:variable name="montoDetraccion" select="./cbc:Amount"/>
			<xsl:variable name="montoDetraccion_currency" select="./cbc:Amount/@currencyID"/>
			<!-- 177. Fecha del pago único o de las cuotas -->
			<xsl:variable name="fechaPagoUnicoCuotas" select="./cbc:PaymentDueDate"/>
					
			<!-- 174. Forma de pago -->		
			<xsl:if test="($fasefechaEmision = 0) or ($fasefechaEmision = 2) or ($fasefechaEmision = 3)">
				<!-- 174. Forma de pago -->
				<xsl:if test="($indicadorPaymentTerms = 'FormaPago')">	
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'3245'"/>
						<xsl:with-param name="node" select="$codigoBienServicio"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $indicadorPaymentTerms, ' ', $codigoBienServicio)" />
					</xsl:call-template>	
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'3246'"/>
						<xsl:with-param name="node" select="$codigoBienServicio"/>
						<xsl:with-param name="regexp" select="'^(Credito|Cuota[0-9]{3})$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $indicadorPaymentTerms, ' ', $codigoBienServicio)"/>
					</xsl:call-template>	
						
					<xsl:if test="count(key('by-PaymentTerms-PaymentMeansID', number($codigoBienServicio))) &gt; 1">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3248'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $codigoBienServicio)" />
						</xsl:call-template>
					</xsl:if>																	
					
					<xsl:if test="($codigoBienServicio = 'Credito')">	
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'3250'" />
							<xsl:with-param name="node" select="$montoDetraccion" />
							<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $montoDetraccion)"/>
						</xsl:call-template>	
						
						<xsl:if test="($tipoDocumentoIdentidadReceptorComprobante = '6')">
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'3251'"/>
								<xsl:with-param name="node" select="$montoDetraccion"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', $codigoBienServicio, ' ', $montoDetraccion)"/>
							</xsl:call-template>
							
							<xsl:if test="not(number($sumaCredito) = number($sumaCuota))">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'3319'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', position(), ' ', $sumaCredito, ' ', $sumaCuota)" />
								</xsl:call-template>													
							</xsl:if>									
						</xsl:if>									
					</xsl:if>	
														
					<xsl:if test="($montoDetraccion_currency) and not($tipoMoneda = $montoDetraccion_currency)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2071'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: 13) ', position(), ' ', $tipoMoneda, ' ',$montoDetraccion_currency)" />
						</xsl:call-template>					
					</xsl:if>
					
					<xsl:if test="(substring($codigoBienServicio,1,5)='Cuota')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'3253'" />
							<xsl:with-param name="node" select="$montoDetraccion" />
							<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'" />
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $montoDetraccion)"/>
						</xsl:call-template>	
						
						<xsl:if test="($tipoDocumentoIdentidadReceptorComprobante = '6')">	
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'3254'"/>
								<xsl:with-param name="node" select="$montoDetraccion"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', $codigoBienServicio, ' ', $montoDetraccion)"/>
							</xsl:call-template>		
						
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'3256'"/>
								<xsl:with-param name="node" select="$fechaPagoUnicoCuotas"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', $codigoBienServicio, ' ', $fechaPagoUnicoCuotas)"/>
							</xsl:call-template>																				
						</xsl:if>
						
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'3255'" />
							<xsl:with-param name="node" select="$fechaPagoUnicoCuotas" />
							<xsl:with-param name="regexp" select="'^[0-9]{1,4}-[0-9]{1,2}-[0-9]{1,2}$'" />
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $fechaPagoUnicoCuotas)"/>
						</xsl:call-template>							
						
<!-- 						<xsl:variable name="c1" select="xs:date($cbcIssueDate)-xs:date($fechaPagoUnicoCuotas)" />					 -->
<!-- 						<xsl:variable name="c2" select="fn:days-from-duration(xs:duration($c1))" /> -->
<!-- 						<xsl:if test="($c2 &gt; -1)"> -->
<!-- 							<xsl:call-template name="rejectCall"> -->
<!-- 								<xsl:with-param name="errorCode" select="'3321'" /> -->
<!-- 								<xsl:with-param name="errorMessage" -->
<!-- 									select="concat('Error ', $fechaPagoUnicoCuotas, ') ', $cbcIssueDate, ' - ', $c1, ' - ', $c2)" /> -->
<!-- 							</xsl:call-template>											 -->
<!-- 						</xsl:if>									 -->
					</xsl:if>
				</xsl:if>
			</xsl:if>																	
		</xsl:for-each>			
			
	</xsl:template>
</xsl:stylesheet>
