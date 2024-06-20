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
	<xsl:key name="by-document-despatch-reference" match="*[local-name()='Invoice']/cac:DespatchDocumentReference" use="concat(cbc:DocumentTypeCode,' ', cbc:ID)" />	
	<xsl:key name="by-document-additional-reference" match="*[local-name()='Invoice']/cac:AdditionalDocumentReference" use="concat(cbc:DocumentTypeCode,' ', cbc:ID)" />
	<xsl:key name="by-invoiceLine-id" match="*[local-name()='Invoice']/cac:InvoiceLine" use="number(cbc:ID)" />
<!-- 	<xsl:key name="by-subInvoiceLine-id" match="*[local-name()='Invoice']/cac:InvoiceLine/cac:SubInvoiceLine" use="number(cbc:ID)" /> -->
	<xsl:key name="by-subInvoiceLine-id" match="*[local-name()='Invoice']/cac:InvoiceLine/cac:SubInvoiceLine" use="concat(../cbc:ID,'-', cbc:ID)"/>
	
	<!-- Parameter -->
	<xsl:param name="nombreArchivoEnviado" />

	<xsl:template match="/*">
		<!-- Nombre del archivo -->	
		<xsl:variable name="tipoComprobante" select="substring($nombreArchivoEnviado, 13, 2)" />
		<xsl:variable name="numeroSerie" select="substring($nombreArchivoEnviado, 16, 4)" />
		<xsl:variable name="numeroComprobante" select="substring($nombreArchivoEnviado, 21, string-length($nombreArchivoEnviado) - 24)" />		
		<xsl:variable name="numeroRuc" select="substring($nombreArchivoEnviado, 1, 11)" />		
		<!-- Fin Nombre del archivo -->
		
		<!-- 1. Fecha de emisión -->
		<xsl:variable name="cbcIssueDate" select="cbc:IssueDate" />	
		<xsl:variable name="currentdate" select="fn:current-date()"></xsl:variable>			
		<!-- Variables -->
		<!-- 3. Versión del UBL -->
		<xsl:variable name="cbcUBLVersionID" select="cbc:UBLVersionID" />
		<!-- 4. Versión de la estructura del documento -->
		<xsl:variable name="cbcCustomizationID" select="cbc:CustomizationID" />
		<xsl:variable name="cbcCustomizationID_SchemeAgencyName" select="cbc:CustomizationID/@schemeAgencyName" />
		<!-- 5. Numeración, conformada por serie y número correlativo -->
		<xsl:variable name="cbcID" select="cbc:ID" />		
		<!-- 7. Tipo de documento autorizado -->
		<xsl:variable name="tipoDocumento" select="cbc:InvoiceTypeCode"/>		
		<!-- 15. Código de leyenda -->
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
		<xsl:variable name="countCodigoLeyenda2010" select="count(cbc:Note[@languageLocaleID='2010'])" />
		<xsl:variable name="countCodigoLeyenda2011" select="count(cbc:Note[@languageLocaleID='2011'])" />
		<!-- 18 suma.Total valor de venta del partícipe  -->
		<xsl:variable name="sumTotalValorVenta" select="sum(./cac:InvoiceLine/cbc:LineExtensionAmount)"/>	
		<!-- 23. Fondo de Inclusión Social Energético (FISE) del partícipe  -->	
		<!-- 23. Otros Cargos/Descuentos del partícipe  -->
		<xsl:variable name="sumMontoDescuento" select="sum(./cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode[text()='02' or text()='03']]/cbc:Amount)"/>
		<xsl:variable name="sumMontoCargo" select="sum(./cac:InvoiceLine/cac:AllowanceCharge[cbc:AllowanceChargeReasonCode[text()='45' or text()='49' or text()='50' or text()='52']]/cbc:Amount)"/>				
		<!-- 38 suma.Sumatoria IGV - ISC del partícipe  -->
		<xsl:variable name="sumSumatoriaIGVPartícipe" select="sum(./cac:InvoiceLine/cac:TaxTotal[cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000']]/cbc:TaxAmount)"/>
		<xsl:variable name="sumSumatoriaIISCPartícipe" select="sum(./cac:InvoiceLine/cac:TaxTotal[cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000']]/cbc:TaxAmount)"/>		
		<!-- 39. Contar Código de tributo -->					
		<xsl:variable name="countCodigoTributo1000" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])" />
		<xsl:variable name="countCodigoTributo2000" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000'])" />
		<xsl:variable name="countCodigoTributo9997" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9997'])" />		
		<!-- 40. Contar Importe Total - Impuestos -->					
		<xsl:variable name="countImporteTotalImpuestos" select="count(./cac:TaxTotal)" />		
		<!-- 43. Importe total de DAE  -->
		<xsl:variable name="sumImporteTotalDAE" select="sum(./cac:LegalMonetaryTotal/cbc:PayableAmount)"/>												
		<xsl:variable name="monedaImporteTotalDAE" select="cac:LegalMonetaryTotal/cbc:PayableAmount/@currencyID"/>		

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
						<xsl:if test="($t2 &gt; 7)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2108'" />
								<xsl:with-param name="errorMessage" 
									select="concat('Error en la linea 1) ', $t4, ' ', $t6, ' ', $t8, ' ', $t10, ' : ', $s1C, ' - ', $cbcIssueDate, ' - ', $currentdate, ' - ', $t1, ' - ', $t2)" />
							</xsl:call-template>
						</xsl:if>					
				</xsl:if>
			</xsl:when>			
			<xsl:otherwise>			
					<xsl:if test="($t2 &gt; 7)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2108'" />
							<xsl:with-param name="errorMessage" 
								select="concat('Error en la linea 2) ', $t4, ' ', $t6, ' ', $t8, ' ', $t10, ' - ', $cbcIssueDate, ' - ', $currentdate, ' - ', $t1, ' - ', $t2)" />
						</xsl:call-template>
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
		
		<!-- 3. Version del UBL -->
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
		
		<!-- 4. Version de la Estructura del Documento -->
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'2073'"/>
			<xsl:with-param name="node" select="$cbcCustomizationID"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $cbcCustomizationID)"/>
		</xsl:call-template>
		
		<xsl:call-template name="regexpValidateElementIfExist">
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
				select="concat('Error en la linea: 1) ', $cbcCustomizationID_SchemeAgencyName)"/>
		</xsl:call-template>
			
		<!-- 5. Numeración, conformada por serie y número correlativo -->
		<xsl:if test="not($numeroSerie = substring($cbcID, 1, 4))">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'1035'" />
				<xsl:with-param name="errorMessage"
					select="concat('numero de serie del xml diferente al numero de serie del archivo ', substring($cbcID, 1, 4), ' diff ', $numeroSerie)" />
			</xsl:call-template>
		</xsl:if>

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
			<xsl:with-param name="regexp" select="'^[F][A-Z0-9]{3}-[0-9]{1,8}$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cbcID)"/>
		</xsl:call-template>
					
		<!-- 7. Tipo de documento -->				
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'1004'" />
			<xsl:with-param name="node" select="$tipoDocumento" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoDocumento)"/>
		</xsl:call-template>
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'1003'" />
			<xsl:with-param name="node" select="$tipoDocumento"/>
			<xsl:with-param name="regexp" select="'^(34)$'"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoDocumento)"/>
		</xsl:call-template>		
		
		<!-- Datos del ciente o receptor -->				
		<xsl:variable name="countEmisorPartyIdentification" select="count(cac:AccountingSupplierParty/cac:Party/cac:PartyIdentification)"/>
		<xsl:if test="$countEmisorPartyIdentification &gt; 1">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3089'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $countEmisorPartyIdentification)"/>
			</xsl:call-template>
		</xsl:if>

		<!-- Datos del Emisor -->																
		<xsl:for-each select="cac:AccountingSupplierParty">							
			<xsl:for-each select="./cac:Party">	
				<xsl:for-each select="./cac:PartyIdentification">
					<!-- 9. Número de RUC -->
					<xsl:variable name="emisorNumeroDocumento" select="./cbc:ID"/>	
					<!-- 9. Tipo de documento de identidad del emisor -->
					<xsl:variable name="emisorTipoDocumento" select="./cbc:ID/@schemeID"/>	
					
					<!-- 9. Número de RUC -->
					<xsl:if test="not($numeroRuc = $emisorNumeroDocumento)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'1034'" />
							<xsl:with-param name="errorMessage"
								select="concat('ruc del xml diferente al nombre del archivo ', $emisorNumeroDocumento, ' diff ', $numeroRuc)" />
						</xsl:call-template>
					</xsl:if>						
					
					<!-- 9. Tipo de documento de identidad del emisor -->							
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'1008'" />
						<xsl:with-param name="node" select="$emisorTipoDocumento" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $emisorTipoDocumento)"/>
					</xsl:call-template>
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'1007'" />
						<xsl:with-param name="node" select="$emisorTipoDocumento"/>
						<xsl:with-param name="regexp" select="'^(6)$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $emisorTipoDocumento)"/>
					</xsl:call-template>																					
				</xsl:for-each>
				
				<xsl:for-each select="./cac:PartyLegalEntity">
					<!-- 10. Apellidos y nombres, denominación o razón social -->
					<xsl:variable name="emisorRazonSocial" select="./cbc:RegistrationName"/>
					
					<!-- 10. Apellidos y nombres o denominacion o razon social Emisor -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'1037'" />
						<xsl:with-param name="node" select="$emisorRazonSocial" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $emisorRazonSocial)"/>
					</xsl:call-template>
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4338'" />
						<xsl:with-param name="node" select="$emisorRazonSocial"/>
						<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,1499}$'"/>
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $emisorRazonSocial)"/>
					</xsl:call-template>						
				</xsl:for-each>									
			</xsl:for-each>
		</xsl:for-each>		
		
		<!-- Datos del cliente o receptor -->			
		<xsl:variable name="countClientePartyIdentification" select="count(cac:AccountingCustomerParty/cac:Party/cac:PartyIdentification)"/>
		<xsl:if test="$countClientePartyIdentification &gt; 1">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3090'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $countClientePartyIdentification)"/>
			</xsl:call-template>
		</xsl:if>										
				
		<!-- Datos del  Establecimiento afiliado (receptor) -->
		<xsl:for-each select="cac:AccountingCustomerParty">						
			<xsl:for-each select="./cac:Party">			
				<xsl:for-each select="./cac:PartyIdentification">	
					<!-- 11. Número de documento de identidad del adquirente o usuario -->
					<xsl:variable name="clienteNumeroDocumento" select="./cbc:ID"/>	
					<!-- 11. Tipo de documento de identidad del adquirente o usuario -->
					<xsl:variable name="clienteTipoDocumento" select="./cbc:ID/@schemeID"/>
					
					<!-- 11. Número de documento de identidad del adquirente o usuario -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'2014'"/>
						<xsl:with-param name="node" select="$clienteNumeroDocumento"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $clienteNumeroDocumento)"/>
					</xsl:call-template>										
				
					<xsl:if test="$clienteTipoDocumento = '6'">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'2017'" />
							<xsl:with-param name="node" select="$clienteNumeroDocumento" />
							<xsl:with-param name="regexp" select="'^[\d]{11}$'"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $clienteNumeroDocumento)"/>
						</xsl:call-template>		
					</xsl:if>				
				
					<xsl:if test="$clienteTipoDocumento = '1'">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'2801'" />
							<xsl:with-param name="node" select="$clienteNumeroDocumento" />
							<xsl:with-param name="regexp" select="'^[\d]{8}$'"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $clienteNumeroDocumento)"/>
						</xsl:call-template>		
					</xsl:if>					
				
					<xsl:if test="($clienteTipoDocumento='4') or ($clienteTipoDocumento='7') or ($clienteTipoDocumento='0') or 
  							($clienteTipoDocumento='A') or ($clienteTipoDocumento='B') or ($clienteTipoDocumento='C') or  
  							($clienteTipoDocumento='D') or ($clienteTipoDocumento='E')">  
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'2802'" />
							<xsl:with-param name="node" select="$clienteNumeroDocumento" />
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,14}$'"/>
<!--							<xsl:with-param name="isError" select="false()"/> -->
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $clienteNumeroDocumento)"/>
						</xsl:call-template>		
					</xsl:if>				

					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'2015'"/>
						<xsl:with-param name="node" select="$clienteTipoDocumento"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $clienteTipoDocumento)"/>
					</xsl:call-template>
												
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2800'" />
						<xsl:with-param name="node" select="$clienteTipoDocumento" />
						<xsl:with-param name="regexp" select="'^(0|1|4|6|7|A|B|C|D|E|F|G)$'" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $clienteTipoDocumento)"/>
					</xsl:call-template>				
				</xsl:for-each>		
				
				<xsl:for-each select="./cac:PartyLegalEntity">
					<!-- 12. Apellidos y nombres, denominación o razón social del adquirente o usuario -->
					<xsl:variable name="clienteRazonSocial" select="./cbc:RegistrationName"/>		
					
					<!-- 12. Apellidos y nombres, denominación o razón social del adquirente o usuario -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'2021'"/>
						<xsl:with-param name="node" select="$clienteRazonSocial"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $clienteRazonSocial)"/>
					</xsl:call-template>
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2022'" />
						<xsl:with-param name="node" select="$clienteRazonSocial" />
						<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,1499}$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $clienteRazonSocial)"/>
					</xsl:call-template>
				</xsl:for-each>																							
			</xsl:for-each>
		</xsl:for-each>	
						
		<!-- Documentos de referencia -->
		<xsl:for-each select="cac:DespatchDocumentReference">		
			<!-- 13. Número de documento -->			
			<xsl:variable name="numeroGuiaRemisionRelacionada" select="./cbc:ID"/>	
			<!-- 13. Tipo de guía relacionado -->
			<xsl:variable name="tipoGuiaRemisionRelacionada" select="./cbc:DocumentTypeCode"/>			
			<xsl:variable name="tipoGuiaRemisionRelacionada_listAgencyName" select="./cbc:DocumentTypeCode/@listAgencyName"/>	
			<xsl:variable name="tipoGuiaRemisionRelacionada_listName" select="./cbc:DocumentTypeCode/@listName"/>	
			<xsl:variable name="tipoGuiaRemisionRelacionada_listURI" select="./cbc:DocumentTypeCode/@listURI"/>	
			
			<xsl:if test="string($numeroGuiaRemisionRelacionada)">	
				<!-- 13. Número de la guía de remisión relacionada -->		
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4006'"/>
					<xsl:with-param name="node" select="$numeroGuiaRemisionRelacionada"/>
<!-- 					<xsl:with-param name="regexp" select="'^[T][A-Z0-9]{3}-[0-9]{1,8}$|^[0-9]{4}-[0-9]{1,8}$|^[EG]{2}[0-9]{2}-[0-9]{1,8}$|^[G]{1}[0-9]{3}-[0-9]{1,8}$'"/> -->
					<xsl:with-param name="regexp" select="'^[T][A-Z0-9]{3}-[0-9]{1,8}$|^[0-9]{4}-[0-9]{1,8}$|^[EG0][1-4]{1}-[0-9]{1,8}$|^[EG07] {4}-[0-9]{1,8}$|^[G][0-9]{3}-[0-9]{1,8}$|^[V][A-Z0-9]{3}-[0-9]{1,8}$'"/>
					<xsl:with-param name="isError" select="false()"/> 
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $numeroGuiaRemisionRelacionada)"/>
				</xsl:call-template>			
				
				<!-- 13. Tipo de la guía de remisión relacionada -->						
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
					<xsl:with-param name="node" select="$tipoGuiaRemisionRelacionada_listAgencyName" />
					<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: 6) ', position(), ' ', $tipoGuiaRemisionRelacionada_listAgencyName)"/>
				</xsl:call-template>			
		
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4252'" />
					<xsl:with-param name="node" select="$tipoGuiaRemisionRelacionada_listName" />
					<xsl:with-param name="regexp" select="'^(Tipo de Documento)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: 6) ', position(), ' ', $tipoGuiaRemisionRelacionada_listName)"/>
				</xsl:call-template>	
		
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4253'" />
					<xsl:with-param name="node" select="$tipoGuiaRemisionRelacionada_listURI" />
					<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo01)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: 2) ', position(), ' ', $tipoGuiaRemisionRelacionada_listURI)"/>
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
		
		<!-- 14. Tipo y número de otro documento relacionado -->
		<xsl:for-each select="cac:AdditionalDocumentReference">		
			<xsl:variable name="numeroOtroDocumentoRelacionada" select="./cbc:ID"/>
			<xsl:variable name="tipoOtroDocumentoRelacionada" select="./cbc:DocumentTypeCode"/>	
			<xsl:variable name="tipoOtroDocumentoRelacionada_listAgencyName" select="./cbc:DocumentTypeCode/@listAgencyName"/>	
			<xsl:variable name="tipoOtroDocumentoRelacionada_listName" select="./cbc:DocumentTypeCode/@listName"/>	
			<xsl:variable name="tipoOtroDocumentoRelacionada_listURI" select="./cbc:DocumentTypeCode/@listURI"/>		
			
			<xsl:if test="$numeroOtroDocumentoRelacionada">
				<!-- 14. Número de otro documento relacionado -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4010'"/>
					<xsl:with-param name="node" select="$numeroOtroDocumentoRelacionada"/>
					<xsl:with-param name="regexp" select="'^[\w].{1,30}$'"/>
					<xsl:with-param name="isError" select="false()"/> 
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ',$numeroOtroDocumentoRelacionada)"/>
				</xsl:call-template>	
			
				<xsl:call-template name="regexpValidateElementIfExistTrue">
					<xsl:with-param name="errorCodeValidate" select="'4010'"/>
					<xsl:with-param name="node" select="$numeroOtroDocumentoRelacionada"/>
					<xsl:with-param name="regexp" select="'[\s]'"/>
					<xsl:with-param name="isError" select="false()"/> 
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ',$numeroOtroDocumentoRelacionada)"/>
				</xsl:call-template>
				
				<!-- 14. Tipo de otro documento relacionado -->	
				<xsl:if test="not(($tipoOtroDocumentoRelacionada = '05') or ($tipoOtroDocumentoRelacionada = '99'))"> 
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4009'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoOtroDocumentoRelacionada)" />
					</xsl:call-template>					
				</xsl:if>					
				
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4251'" />
					<xsl:with-param name="node" select="$tipoOtroDocumentoRelacionada_listAgencyName" />
					<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: 7) ', position(), ' ', $tipoOtroDocumentoRelacionada_listAgencyName)"/>
				</xsl:call-template>			
		
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4252'" />
					<xsl:with-param name="node" select="$tipoOtroDocumentoRelacionada_listName" />
					<xsl:with-param name="regexp" select="'^(Tipo de Documento)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: 7) ', position(), ' ', $tipoOtroDocumentoRelacionada_listName)"/>
				</xsl:call-template>	
		
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4253'" />
					<xsl:with-param name="node" select="$tipoOtroDocumentoRelacionada_listURI" />
					<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo12)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: 3) ', position(), ' ', $tipoOtroDocumentoRelacionada_listURI)"/>
				</xsl:call-template>								
			</xsl:if>
		
			<xsl:if
				test="count(key('by-document-additional-reference', concat($tipoOtroDocumentoRelacionada,' ',$numeroOtroDocumentoRelacionada))) > 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2365'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $tipoOtroDocumentoRelacionada,' ',$numeroOtroDocumentoRelacionada)"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>		
		
		<!-- 15. Leyendas -->
		<xsl:if test="($countCodigoLeyenda1000 &gt; 1) or ($countCodigoLeyenda1002 &gt; 1) or ($countCodigoLeyenda2000 &gt; 1) or ($countCodigoLeyenda2001 &gt; 1) or 
			($countCodigoLeyenda2002 &gt; 1) or ($countCodigoLeyenda2003 &gt; 1) or ($countCodigoLeyenda2004 &gt; 1) or ($countCodigoLeyenda2005 &gt; 1) or
			($countCodigoLeyenda2006 &gt; 1) or ($countCodigoLeyenda2007 &gt; 1) or ($countCodigoLeyenda2008 &gt; 1) or ($countCodigoLeyenda2009 &gt; 1) or
			($countCodigoLeyenda2010 &gt; 1) or ($countCodigoLeyenda2011 &gt; 1)">
			<xsl:call-template name="addWarning">
				<xsl:with-param name="warningCode" select="'4362'" />
				<xsl:with-param name="warningMessage"
					select="concat('Error en la linea: ', $countCodigoLeyenda1000)"/>
			</xsl:call-template>			
		</xsl:if>		
				
		<xsl:for-each select="cbc:Note">	
			<!-- 15. Código de leyenda -->
			<xsl:variable name="codigoLeyenda" select="./@languageLocaleID" />		
			<!-- 15. Descripción de la leyenda -->
			<xsl:variable name="descripcionLeyenda" select="." />
			
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'3027'" />
				<xsl:with-param name="node" select="$codigoLeyenda" />
				<xsl:with-param name="regexp" select="'^(1000|1002|2000|2001|2002|2003|2004|2005|2006|2007|2008|2009|2010|2011)$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $codigoLeyenda)"/>
			</xsl:call-template>
						
			<!-- 15. Descripción de la leyenda -->		
			<xsl:if test="($descripcionLeyenda)">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'3006'"/>
					<xsl:with-param name="node" select="$descripcionLeyenda"/>
					 <xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,499}$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $descripcionLeyenda)"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>			
														
		<!-- Datos de cada partícipe -->
		<xsl:for-each select="cac:InvoiceLine">
			<!-- 16. Número de orden -->
			<xsl:variable name="numeroItem" select="./cbc:ID"/>	
			<!-- 19. Total valor de venta del partícipe -->
			<xsl:variable name="totalValorVentaParticipe" select="./cbc:LineExtensionAmount"/>	
			<xsl:variable name="totalValorVentaParticipe_currencyID" select="./cbc:LineExtensionAmount/@currencyID"/>

			<!-- 20. Contar Total impuestos del partícipe Linea -->					
			<xsl:variable name="countTotalImpuestosParticipeLinea" select="count(./cac:TaxTotal)" />		
			<!-- 21. Contar Código de tributo por línea -->					
			<xsl:variable name="countCodigoTributoLinea1000" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])" />
			<xsl:variable name="countCodigoTributoLinea2000" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000'])" />
			<xsl:variable name="countCodigoTributoLinea9997" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9997'])" />				
			<!-- 21. Suma Sumatoria ISC del partícipe -->
			<xsl:variable name="sumSumatoriaISCParticipeLinea" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxAmount)"/>	
			<!-- 22. Suma Sumatoria ISC del partícipe -->
			<xsl:variable name="sumSumatoriaIGVParticipeLinea" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxAmount)"/>									
			<!-- 23. Suma Otros Cargos/Descuentos del partícipe -->
			<xsl:variable name="sumMontoDescuentoLinea02" select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='02']/cbc:Amount)"/>	
			<xsl:variable name="sumMontoCargoLinea49" select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode='49']/cbc:Amount)"/>	
			<!-- 23. Suma Fondo de Inclusión Social Energético (FISE) del partícipe -->
			<xsl:variable name="sumFISEParticipeLinea" select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode[text() = '45']]/cbc:Amount)"/>										
			<!-- 23. Suma Otros Cargos/Descuentos del partícipe -->
			<xsl:variable name="sumOtrosCargosParticipeLinea" select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode[text() = '50']]/cbc:Amount)"/>	
			<xsl:variable name="sumOtrosDescuentosParticipeLinea" select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode[text() = '03']]/cbc:Amount)"/>	
			<!-- 24. Contar Importe total por línea -->					
			<xsl:variable name="countImporteTotalLinea" select="count(./cac:ItemPriceExtension)" />					
			<!-- 31. Suma Valor de venta  - detalle del partícipe -->
			<xsl:variable name="sumValorVentaDetalleParticipeLinea" select="sum(./cac:SubInvoiceLine[cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000']]/cbc:LineExtensionAmount)"/>	
			<!-- 33. Suma Monto de ISC por ítem - detalle del partícipe -->
			<xsl:variable name="sumMontoISCDetalleParticipeLinea" select="sum(./cac:SubInvoiceLine/cac:TaxTotal[cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000']]/cbc:TaxAmount)"/>	

			<!-- 16. Número de orden del Ítem -->
			<xsl:choose>
				<xsl:when test="not($numeroItem)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2023'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $numeroItem)" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:if
						test='not(fn:matches($numeroItem,"^[0-9]{1,3}?$")) or $numeroItem &lt;= 0'>
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2023'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', $numeroItem)" />
						</xsl:call-template>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:if test="count(key('by-invoiceLine-id', number($numeroItem))) &gt; 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2752'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $numeroItem)" />
				</xsl:call-template>
			</xsl:if>
			
			<!-- Datos de cada partícipe -->
			<!-- 17. Número de documento de identidad -->				
			<xsl:variable name="countParticipePartyIdentification" select="count(./cac:OriginatorParty/cac:PartyIdentification)"/>
			<xsl:if test="$countParticipePartyIdentification &gt; 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2490'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $numeroItem, ' ', $countParticipePartyIdentification)"/>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:for-each select="./cac:OriginatorParty">	
				<xsl:for-each select="./cac:PartyIdentification">	
					<!-- 17. Número de documento de identidad -->
					<xsl:variable name="rucParticipe" select="./cbc:ID" />
					<xsl:variable name="tipoDocumentoParticipe" select="./cbc:ID/@schemeID" />	
					
					<!-- 17. Número de documento de identidad -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'2491'"/>
						<xsl:with-param name="node" select="$rucParticipe"/>			
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $numeroItem, ' ',  $rucParticipe)"/>
					</xsl:call-template>	
					
					<xsl:if test="$tipoDocumentoParticipe='6'">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'2489'" />
							<xsl:with-param name="node" select="$rucParticipe" />
							<xsl:with-param name="regexp" select="'^[\d]{11}$'"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $numeroItem, ' ', $rucParticipe)"/>
						</xsl:call-template>		
					</xsl:if>					
					
					<xsl:if test="($tipoDocumentoParticipe='4') or ($tipoDocumentoParticipe='7') or
							($tipoDocumentoParticipe='0') or ($tipoDocumentoParticipe='A') or 
							($tipoDocumentoParticipe='B') or ($tipoDocumentoParticipe='C') or 
							($tipoDocumentoParticipe='D') or ($tipoDocumentoParticipe='E')">
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'2489'" />
							<xsl:with-param name="node" select="$rucParticipe" />
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,14}$'"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $numeroItem, ' ', $rucParticipe)"/>
						</xsl:call-template>		
					</xsl:if>		
						
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'2516'"/>
						<xsl:with-param name="node" select="$tipoDocumentoParticipe"/>			
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $numeroItem, ' ',  $tipoDocumentoParticipe)"/>
					</xsl:call-template>	
							
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2016'"/>
						<xsl:with-param name="node" select="$tipoDocumentoParticipe"/>
						<xsl:with-param name="regexp" select="'^(0|1|4|6|7|A|B|C|D|E)$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $numeroItem, ' ', $tipoDocumentoParticipe)"/>
					</xsl:call-template>																																			
				</xsl:for-each>																			
			</xsl:for-each>		
											
			<!-- 18. Apellidos y nombres, denominación o razón social del partícipe -->							
			<xsl:for-each select="./cac:Item">	    			
				<!-- 18. Apellidos y nombres, denominación o razón social del partícipe -->
				<xsl:variable name="nombreParticipe" select="./cbc:Description"/>
				
				<!-- 18. Apellidos y nombres, denominación o razón social del partícipe -->		
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4350'"/>
					<xsl:with-param name="node" select="$nombreParticipe"/>
					<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{2,1499}$'"/>
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea a): ', $numeroItem, ' ', $nombreParticipe)"/>
				</xsl:call-template>	
						
				<xsl:call-template name="regexpValidateElementIfExistTrue">
					<xsl:with-param name="errorCodeValidate" select="'4350'"/>
					<xsl:with-param name="node" select="$nombreParticipe"/>
					<xsl:with-param name="regexp" select="'[\t]'"/>
					<xsl:with-param name="isError" select="false()"/> 
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea b): ', $numeroItem, ' ', $nombreParticipe)"/>
				</xsl:call-template>											
			</xsl:for-each>	
			
								
			<!-- 19. Total valor de venta del partícipe -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2370'"/>
				<xsl:with-param name="node" select="$totalValorVentaParticipe"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $numeroItem, ' ', $totalValorVentaParticipe)"/>
			</xsl:call-template>			
																										
			<xsl:variable name="sumTotalValorVentaParticipe" select="($sumValorVentaDetalleParticipeLinea + $sumMontoCargoLinea49) - $sumMontoDescuentoLinea02"/>				
		   	<xsl:variable name="dif_TotalValorVentaParticipe" select="$totalValorVentaParticipe - $sumTotalValorVentaParticipe" />
			<xsl:if test="($dif_TotalValorVentaParticipe &lt; -1) or ($dif_TotalValorVentaParticipe &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4354'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', $numeroItem, ' ', $dif_TotalValorVentaParticipe, ' ', $totalValorVentaParticipe, ' ', $sumTotalValorVentaParticipe, ' ', $sumValorVentaDetalleParticipeLinea, ' ', $sumMontoCargoLinea49, ' ', $sumMontoDescuentoLinea02)"/>
				</xsl:call-template>
			</xsl:if>				
																					
			<xsl:if test="($totalValorVentaParticipe_currencyID)">				
				<xsl:if test="not($totalValorVentaParticipe_currencyID = $monedaImporteTotalDAE)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2337'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $totalValorVentaParticipe_currencyID, ' ', $monedaImporteTotalDAE)"/>
					</xsl:call-template>
				</xsl:if>			
			</xsl:if>	
													
			<!-- 20. Total impuestos del partícipe -->				
			<xsl:if test="($countTotalImpuestosParticipeLinea = 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3195'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $numeroItem, ' ', $countTotalImpuestosParticipeLinea)"/>
				</xsl:call-template>
			</xsl:if>	
					
			<xsl:if test="($countTotalImpuestosParticipeLinea &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3026'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $numeroItem, ' ', $countTotalImpuestosParticipeLinea)"/>
				</xsl:call-template>
			</xsl:if>	
			
			<xsl:if 
				test="($countCodigoTributoLinea1000 = 0) and ($countCodigoTributoLinea9997 = 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3105'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $numeroItem, ' ', $countCodigoTributoLinea1000, ' ', $countCodigoTributoLinea9997)"/>
				</xsl:call-template>
			</xsl:if>	
			
			<xsl:if 
				test="($countCodigoTributoLinea1000 &gt; 1) or ($countCodigoTributoLinea2000 &gt; 1) or ($countCodigoTributoLinea9997 &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3067'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $numeroItem, ' ', $countCodigoTributoLinea1000, ' ', $countCodigoTributoLinea2000, ' ', $countCodigoTributoLinea9997)"/>
				</xsl:call-template>
			</xsl:if>												
						
			<xsl:for-each select="./cac:TaxTotal">
				<!-- 20. Total impuestos del partícipe -->	
				<xsl:variable name="totalImpuestosParticipe" select="./cbc:TaxAmount"/>
				<xsl:variable name="totalImpuestosParticipe_currencyID" select="./cbc:TaxAmount/@currencyID"/>
				
				<!-- 21. Monto Sumatoria ISC del partícipe linea -->	
				<xsl:variable name="montoSumatoriaISCParticipeLinea" select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxAmount)"/>	
				<!-- 22. Monto Sumatoria IGV del partícipe linea -->
				<xsl:variable name="montoSumatoriaIGVParticipeLinea" select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxAmount)"/>	
						
				<!-- 20. Total impuestos del partícipe -->	
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'3021'"/>
					<xsl:with-param name="node" select="$totalImpuestosParticipe"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $numeroItem, ' ', $totalImpuestosParticipe)"/>
				</xsl:call-template>	
				
				<xsl:if test="($totalImpuestosParticipe_currencyID) and not($totalImpuestosParticipe_currencyID = $monedaImporteTotalDAE)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2337'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $totalImpuestosParticipe_currencyID, ' ', $monedaImporteTotalDAE)"/>
					</xsl:call-template>		
				</xsl:if>

				<xsl:variable name="sumaSumatoriaISCIGVParticipeLinea" select="$montoSumatoriaISCParticipeLinea + $montoSumatoriaIGVParticipeLinea"/>
			    <xsl:variable name="dif_TotalImpuestosParticipeLinea" select="$totalImpuestosParticipe - $sumaSumatoriaISCIGVParticipeLinea" />
				<xsl:if test="($dif_TotalImpuestosParticipeLinea &lt; -1) or ($dif_TotalImpuestosParticipeLinea &gt; 1)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4293'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $dif_TotalImpuestosParticipeLinea,'-', $totalImpuestosParticipe,'-',$sumaSumatoriaISCIGVParticipeLinea, '-', $montoSumatoriaIGVParticipeLinea,'-', $montoSumatoriaISCParticipeLinea)"/>
					</xsl:call-template>
				</xsl:if>	
																											
				<xsl:for-each select="./cac:TaxSubtotal">		
					<!-- 21. Sumatoria ISC del partícipe -->
					<!-- 22. Sumatoria IGV del partícipe -->	
					<xsl:variable name="sumatoriaISCIGVParticipe" select="./cbc:TaxAmount"/>
					<xsl:variable name="sumatoriaISCIGVParticipe_currencyID" select="./cbc:TaxAmount/@currencyID"/>
					
					<!-- 21. Sumatoria ISC del partícipe -->
					<!-- 22. Sumatoria IGV del partícipe -->				
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2033'"/>
						<xsl:with-param name="node" select="$sumatoriaISCIGVParticipe"/>
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $numeroItem, ' ', $sumatoriaISCIGVParticipe)"/>
					</xsl:call-template>	
					
					<xsl:if test="($sumatoriaISCIGVParticipe_currencyID) and not($sumatoriaISCIGVParticipe_currencyID = $monedaImporteTotalDAE)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2337'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', $numeroItem, ' ', $sumatoriaISCIGVParticipe_currencyID, ' ', $monedaImporteTotalDAE)"/>
						</xsl:call-template>		
					</xsl:if>		
										
					<xsl:for-each select="./cac:TaxCategory">
						<xsl:for-each select="./cac:TaxScheme">
							<!-- 21. Código de tributo ISC -->
							<!-- 22. Código de tributo IGV -->		
							<xsl:variable name="codigoTributoLinea" select="./cbc:ID"/>	
							<xsl:variable name="codigoTributoLinea_schemeName" select="./cbc:ID/@schemeName"/>
							<xsl:variable name="codigoTributoLinea_schemeAgencyName" select="./cbc:ID/@schemeAgencyName"/>
							<xsl:variable name="codigoTributoLinea_schemeURI" select="./cbc:ID/@schemeURI"/>								
							
							<!-- 21. Código de tributo ISC -->
							<!-- 22. Código de tributo IGV -->	
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'2037'"/>
								<xsl:with-param name="node" select="$codigoTributoLinea" />
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', $numeroItem, ' ', $codigoTributoLinea)"/>
							</xsl:call-template>	
							
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'2036'"/>
								<xsl:with-param name="node" select="$codigoTributoLinea"/>
								<xsl:with-param name="regexp" select="'^(1000|2000|9997)$'" />
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', $numeroItem, ' ', $codigoTributoLinea)"/>
							</xsl:call-template>		
							
							<xsl:if test="($codigoTributoLinea = 1000)">
							    <xsl:variable name="sumaMontoTributoLinea" select="($sumValorVentaDetalleParticipeLinea + $sumSumatoriaISCParticipeLinea + $sumMontoCargoLinea49) - $sumMontoDescuentoLinea02"/>
							    <xsl:variable name="calcularMontoTributoLinea" select="($sumaMontoTributoLinea * 0.18)"/>
							    <xsl:variable name="dif_SumatoriaISCIGVParticipe" select="$sumatoriaISCIGVParticipe - $calcularMontoTributoLinea" />
								<xsl:if test="($dif_SumatoriaISCIGVParticipe &lt; -1) or ($dif_SumatoriaISCIGVParticipe &gt; 1)">
									<xsl:call-template name="addWarning">
										<xsl:with-param name="warningCode" select="'4360'" />
										<xsl:with-param name="warningMessage"
											select="concat('Error en la linea 1) ', position(), ' ', $dif_SumatoriaISCIGVParticipe,'-', $sumatoriaISCIGVParticipe,'-', $calcularMontoTributoLinea,'-', $sumaMontoTributoLinea, '-', $sumValorVentaDetalleParticipeLinea,'-', $sumSumatoriaISCParticipeLinea,'-', $sumMontoCargoLinea49,'-', $sumMontoDescuentoLinea02)"/>
									</xsl:call-template>
								</xsl:if>																																										
							</xsl:if>
							
							<!-- ////  -->
							<!-- 
							<xsl:if test="($codigoTributoLinea = 2000)">
								<xsl:variable name="dif_SumatoriaISCIGVParticipe" select="$sumatoriaISCIGVParticipe - $sumMontoISCDetalleParticipeLinea" />
								<xsl:if test="($dif_SumatoriaISCIGVParticipe &lt; -1) or ($dif_SumatoriaISCIGVParticipe &gt; 1)">
									<xsl:call-template name="addWarning">
										<xsl:with-param name="warningCode" select="'4359'" />
										<xsl:with-param name="warningMessage"
											select="concat('Error en la linea: ', position(), ' ', $dif_SumatoriaISCIGVParticipe,'-', $sumatoriaISCIGVParticipe,'-', $sumMontoISCDetalleParticipeLinea)"/>
									</xsl:call-template>
								</xsl:if>																			
							</xsl:if>
							-->
							
							<xsl:if test="($codigoTributoLinea = 9997)">
								<xsl:if test="not($sumatoriaISCIGVParticipe = 0)">										
									<xsl:call-template name="addWarning">
										<xsl:with-param name="warningCode" select="'4360'" />
										<xsl:with-param name="warningMessage"
											select="concat('Error en la linea 2) ', $numeroItem, ' ', $codigoTributoLinea, ' ', $sumatoriaISCIGVParticipe)" />
									</xsl:call-template>
								</xsl:if>																				
							</xsl:if>
																					
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4255'" />
								<xsl:with-param name="node" select="$codigoTributoLinea_schemeName" />
								<xsl:with-param name="regexp" select="'^(Codigo de tributos)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: 1) ', $numeroItem, ' ', $codigoTributoLinea_schemeName)"/>
							</xsl:call-template>		
						
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4256'" />
								<xsl:with-param name="node" select="$codigoTributoLinea_schemeAgencyName" />
								<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: 1) ', $numeroItem, ' ', $codigoTributoLinea_schemeAgencyName)"/>
							</xsl:call-template>
					
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4257'" />
								<xsl:with-param name="node" select="$codigoTributoLinea_schemeURI" />
								<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo05)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: 1) ', $numeroItem, ' ', $codigoTributoLinea_schemeURI)"/>
							</xsl:call-template>																													
						</xsl:for-each>		
					</xsl:for-each>	
				</xsl:for-each>	
			</xsl:for-each>	
						
			<!-- 23. Fondo de Inclusión Social Energético (FISE) del partícipe -->	
			<!-- 23. Otros Cargos/Descuentos del partícipe -->				
			<xsl:for-each select="./cac:AllowanceCharge">
				<!-- 23. Indicador de cargo/descuento -->	
				<xsl:variable name="indicadorCargoDescuento" select="./cbc:ChargeIndicator"/>
				<!-- 23. Código de cargo/descuento -->
				<xsl:variable name="codigoCargoDescuento" select="./cbc:AllowanceChargeReasonCode"/>
				<xsl:variable name="codigoCargoDescuento_listAgencyName" select="./cbc:AllowanceChargeReasonCode/@listAgencyName"/>
				<xsl:variable name="codigoCargoDescuento_listName" select="./cbc:AllowanceChargeReasonCode/@listName"/>
				<xsl:variable name="codigoCargoDescuento_listURI" select="./cbc:AllowanceChargeReasonCode/@listURI"/>
				<!-- 23. Factor de cargo/descuento -->
				<xsl:variable name="factorCargoDescuento" select="./cbc:MultiplierFactorNumeric"/>
				<!-- 23. Monto de cargo/descuento -->
				<xsl:variable name="montoCargoDescuento" select="./cbc:Amount"/>
				<xsl:variable name="montoCargoDescuento_currencyID" select="./cbc:Amount/@currencyID"/>		
				<!-- 23. Monto base del cargo/descuento -->
				<xsl:variable name="montoBaseCargoDescuento" select="./cbc:BaseAmount"/>
				<xsl:variable name="montoBaseCargoDescuento_currencyID" select="./cbc:BaseAmount/@currencyID"/>					

				<!-- 23. Indicador de cargo/descuento -->
				<xsl:choose>
					<xsl:when test="not($indicadorCargoDescuento = 'true')">
						<xsl:call-template name="regexpValidateElementIfExistTrue">
							<xsl:with-param name="errorCodeValidate" select="'3114'"/>
							<xsl:with-param name="node" select="$codigoCargoDescuento"/>
							<xsl:with-param name="regexp" select="'^(45|49|50|52)$'" />
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $numeroItem, ' ', $indicadorCargoDescuento, ' ', $codigoCargoDescuento)"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="not($indicadorCargoDescuento = 'false')">
						<xsl:call-template name="regexpValidateElementIfExistTrue">
							<xsl:with-param name="errorCodeValidate" select="'3114'"/>
							<xsl:with-param name="node" select="$codigoCargoDescuento"/>
							<xsl:with-param name="regexp" select="'^(02|03)$'" />
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $numeroItem, ' ', $indicadorCargoDescuento, ' ', $codigoCargoDescuento)"/>
						</xsl:call-template>
					</xsl:when>						
				</xsl:choose>					
									
				<!-- 23. Código de cargo/descuento -->	
				<xsl:if test="($indicadorCargoDescuento)">	
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'3073'"/>
						<xsl:with-param name="node" select="$codigoCargoDescuento" />
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $numeroItem, ' ', $codigoCargoDescuento)"/>
					</xsl:call-template>					
				</xsl:if>
						
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4268'"/>
					<xsl:with-param name="node" select="$codigoCargoDescuento"/>
					<xsl:with-param name="regexp" select="'^(02|03|45|49|50|52)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $numeroItem, ' ', $codigoCargoDescuento)"/>
				</xsl:call-template>					
						
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4251'" />
					<xsl:with-param name="node" select="$codigoCargoDescuento_listAgencyName" />
					<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: 1) ', $numeroItem, ' ', $codigoCargoDescuento_listAgencyName)"/>
				</xsl:call-template>	
				
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4252'" />
					<xsl:with-param name="node" select="$codigoCargoDescuento_listName" />
					<xsl:with-param name="regexp" select="'^(Cargo/descuento)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: 1) ', $numeroItem, ' ', $codigoCargoDescuento_listName)"/>
				</xsl:call-template>	

				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4253'" />
					<xsl:with-param name="node" select="$codigoCargoDescuento_listURI" />
					<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo53)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: 1) ', $numeroItem, ' ', $codigoCargoDescuento_listURI)"/>
				</xsl:call-template>			

				<!-- 23. Factor de cargo/descuento -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'3052'"/>
					<xsl:with-param name="node" select="$factorCargoDescuento"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,3}(\.[0-9]{1,5})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $numeroItem, ' ', $factorCargoDescuento)"/>
				</xsl:call-template>	

				<!-- 23. Monto de cargo/descuento -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2955'"/>
					<xsl:with-param name="node" select="$montoCargoDescuento"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $numeroItem, ' ', $montoCargoDescuento)"/>
				</xsl:call-template>	
				
				<xsl:if test="($codigoCargoDescuento) and ($factorCargoDescuento &gt; 0)">	
				    <xsl:variable name="calculoMontoCargoDescuento" select="$montoBaseCargoDescuento * $factorCargoDescuento"/>
				    <xsl:variable name="dif_MontoCargoDescuento" select="$montoCargoDescuento - $calculoMontoCargoDescuento" />
					<xsl:if test="($dif_MontoCargoDescuento &lt; -1) or ($dif_MontoCargoDescuento &gt; 1)">
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="'4289'" />
							<xsl:with-param name="warningMessage"
								select="concat('Error en la linea: ', $numeroItem, ' ', $dif_MontoCargoDescuento, ' ', $montoCargoDescuento, ' ', $calculoMontoCargoDescuento, ' ', $montoBaseCargoDescuento, ' ', $factorCargoDescuento, ' ', $codigoCargoDescuento)" />
						</xsl:call-template>
					</xsl:if>																																													
				</xsl:if>				
				
				<xsl:if test="($montoCargoDescuento_currencyID) and not($montoCargoDescuento_currencyID = $monedaImporteTotalDAE)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2337'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $montoCargoDescuento_currencyID, ' ', $monedaImporteTotalDAE)"/>
					</xsl:call-template>		
				</xsl:if>			
				
				<!-- 23. Monto base del cargo/descuento -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'3053'"/>
					<xsl:with-param name="node" select="$montoBaseCargoDescuento"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $numeroItem, ' ', $montoBaseCargoDescuento)"/>
				</xsl:call-template>	

				<xsl:if test="($montoBaseCargoDescuento_currencyID) and not($montoBaseCargoDescuento_currencyID = $monedaImporteTotalDAE)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2337'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $montoBaseCargoDescuento_currencyID, ' ', $monedaImporteTotalDAE)"/>
					</xsl:call-template>		
				</xsl:if>								
			</xsl:for-each>				
						
			<xsl:if test="($countImporteTotalLinea = 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2480'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $numeroItem, ' ', $countImporteTotalLinea)"/>
				</xsl:call-template>
			</xsl:if>
						
			<xsl:for-each select="./cac:ItemPriceExtension">
				<!-- 24. Importe total del partícipe -->	
				<xsl:variable name="importeTotal" select="./cbc:Amount"/>
				<xsl:variable name="importeTotal_currencyID" select="./cbc:Amount/@currencyID"/>			
						
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2481'"/>
					<xsl:with-param name="node" select="$importeTotal"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $numeroItem, ' ', $importeTotal)"/>
				</xsl:call-template>				
			
				<xsl:variable name="calculoImporteTotal" select="($totalValorVentaParticipe + $sumSumatoriaISCParticipeLinea + $sumSumatoriaIGVParticipeLinea + $sumFISEParticipeLinea + $sumOtrosCargosParticipeLinea) - $sumOtrosDescuentosParticipeLinea" />									   					   	
			   	<xsl:variable name="dif_importeTotal" select="$importeTotal - $calculoImporteTotal" />
				<xsl:if test="($dif_importeTotal &lt; -1) or ($dif_importeTotal &gt; 1)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4348'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', $numeroItem, '-', $dif_importeTotal, '-', $importeTotal, '-', $calculoImporteTotal, '-', $totalValorVentaParticipe, '-', $sumSumatoriaISCParticipeLinea, '-', $sumSumatoriaIGVParticipeLinea, '-', $sumFISEParticipeLinea, '-', $sumOtrosCargosParticipeLinea, '-', $sumOtrosDescuentosParticipeLinea)"/>
					</xsl:call-template>
				</xsl:if>												
						
				<xsl:if test="($importeTotal_currencyID) and not($importeTotal_currencyID = $monedaImporteTotalDAE)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2337'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $importeTotal_currencyID, ' ', $monedaImporteTotalDAE)"/>
					</xsl:call-template>		
				</xsl:if>							
			</xsl:for-each>		
							
			<!-- 25. Leyendas a nivel del partícipe -->
			<xsl:variable name="countCodigoLeyenda1000Linea" select="count(./cbc:Note[@languageLocaleID='1000'])" />
			<xsl:variable name="countCodigoLeyenda1002Linea" select="count(./cbc:Note[@languageLocaleID='1002'])" />
			<xsl:variable name="countCodigoLeyenda2000Linea" select="count(./cbc:Note[@languageLocaleID='2000'])" />
			<xsl:variable name="countCodigoLeyenda2001Linea" select="count(./cbc:Note[@languageLocaleID='2001'])" />
			<xsl:variable name="countCodigoLeyenda2002Linea" select="count(./cbc:Note[@languageLocaleID='2002'])" />
			<xsl:variable name="countCodigoLeyenda2003Linea" select="count(./cbc:Note[@languageLocaleID='2003'])" />
			<xsl:variable name="countCodigoLeyenda2004Linea" select="count(./cbc:Note[@languageLocaleID='2004'])" />
			<xsl:variable name="countCodigoLeyenda2005Linea" select="count(./cbc:Note[@languageLocaleID='2005'])" />
			<xsl:variable name="countCodigoLeyenda2006Linea" select="count(./cbc:Note[@languageLocaleID='2006'])" />
			<xsl:variable name="countCodigoLeyenda2007Linea" select="count(./cbc:Note[@languageLocaleID='2007'])" />
			<xsl:variable name="countCodigoLeyenda2008Linea" select="count(./cbc:Note[@languageLocaleID='2008'])" />
			<xsl:variable name="countCodigoLeyenda2009Linea" select="count(./cbc:Note[@languageLocaleID='2009'])" />							
			<xsl:variable name="countCodigoLeyenda2010Linea" select="count(./cbc:Note[@languageLocaleID='2010'])" />
			<xsl:variable name="countCodigoLeyenda2011Linea" select="count(./cbc:Note[@languageLocaleID='2011'])" />								
			<xsl:if test="($countCodigoLeyenda1000Linea &gt; 1) or ($countCodigoLeyenda1002Linea &gt; 1) or ($countCodigoLeyenda2000Linea &gt; 1) or ($countCodigoLeyenda2001Linea &gt; 1) or 
 				($countCodigoLeyenda2002Linea &gt; 1) or ($countCodigoLeyenda2003Linea &gt; 1) or ($countCodigoLeyenda2004Linea &gt; 1) or ($countCodigoLeyenda2005Linea &gt; 1) or 
 				($countCodigoLeyenda2006Linea &gt; 1) or ($countCodigoLeyenda2007Linea &gt; 1) or ($countCodigoLeyenda2008Linea &gt; 1) or ($countCodigoLeyenda2009Linea &gt; 1) or 
 				($countCodigoLeyenda2010Linea &gt; 1) or ($countCodigoLeyenda2011Linea &gt; 1)"> 
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4362'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', $numeroItem, $countCodigoLeyenda1000Linea)"/>
				</xsl:call-template>			
			</xsl:if>		
					
			<xsl:for-each select="./cbc:Note">	
				<!-- 25. Código de leyenda -->
				<xsl:variable name="codigoLeyendaLinea" select="./@languageLocaleID" />		
				<!-- 25. Descripción de la leyenda -->
				<xsl:variable name="descripcionLeyendaLinea" select="." />
				
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'3027'" />
					<xsl:with-param name="node" select="$codigoLeyendaLinea" />
					<xsl:with-param name="regexp" select="'^(1000|1002|2000|2001|2002|2003|2004|2005|2006|2007|2008|2009|2010|2011)$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $numeroItem, ' ', $codigoLeyendaLinea)"/>
				</xsl:call-template>
							
				<!-- 25. Descripción de la leyenda -->		
				<xsl:if test="($descripcionLeyendaLinea)">
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'3006'"/>
						<xsl:with-param name="node" select="$descripcionLeyendaLinea"/>
						 <xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,499}$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $numeroItem, ' ', $descripcionLeyendaLinea)"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:for-each>								
																			
			<!-- Datos de cada participe -  Detalle del partícipe -->				
			<xsl:for-each select="./cac:SubInvoiceLine">	
				<!-- 26. Número de orden del ítem - detalle del partícipe  -->		
				<xsl:variable name="numeroOrden" select="./cbc:ID"/>	
				<!-- 27. Unidad de medida por ítem -->		
				<xsl:variable name="unidadMedidaItem" select="./cbc:InvoicedQuantity/@unitCode"/>	
				<xsl:variable name="unidadMedidaItem_ListID" select="./cbc:InvoicedQuantity/@unitCodeListID"/>	
				<xsl:variable name="unidadMedidaItem_ListAgencyName" select="./cbc:InvoicedQuantity/@unitCodeListAgencyName"/>	
				<!-- 28. Cantidad de unidades por ítem -->	
				<xsl:variable name="cantidadUnidadesItem" select="./cbc:InvoicedQuantity"/>						
				<!-- 31. Valor de venta por ítem - detalle del partícipe -->
				<xsl:variable name="valorVentaParticipeItem" select="./cbc:LineExtensionAmount"/>	
				<xsl:variable name="valorVentaParticipeItem_currencyID" select="./cbc:LineExtensionAmount/@currencyID"/>				
				<!-- 30. Suma Valor unitario por ítem-->
				<xsl:variable name="sumValorUnitarioItem" select="sum(./cac:Price/cbc:PriceAmount)"/>
				<!-- 26. Suma Descuentos que afecten la base imponible-->
				<xsl:variable name="sumDescuentosAfectenBaseImponibleItem" select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode[text()='00' or text()='07']]/cbc:BaseAmount)"/>				
				<!-- 26. Suma Cargos que afecten la base imponible-->
				<xsl:variable name="sumCargosAfectenBaseImponibleItem" select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode[text()='47' or text()='54']]/cbc:BaseAmount)"/>				
				<!-- 32. Contar Total Impuestos por ítem -->					
				<xsl:variable name="countTotalImpuestosParticipeItem" select="count(./cac:TaxTotal)" />	
				<!-- 33. Monto base del IGV -->
				<xsl:variable name="sumMontoBaseIGV" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000']]/cbc:TaxableAmount)"/>								
				<!-- 33. Suma Monto del tributo del ítem-->			
				<xsl:variable name="sumMontoTributoItem1000" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000']]/cbc:TaxAmount)"/>			
				<xsl:variable name="sumMontoTributoItem2000" select="sum(./cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000']]/cbc:TaxAmount)"/>																	
				<!-- 28. Contar Código de tributo por ítem-->					
				<xsl:variable name="countCodigoTributoItem1000" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])" />
				<xsl:variable name="countCodigoTributoItem2000" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000'])" />
				<xsl:variable name="countCodigoTributoItem9997" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9997'])" />
				<!-- 36. Contar Importe total por Item -->					
				<xsl:variable name="countImporteTotalItem" select="count(./cac:ItemPriceExtension)" />	
				<!-- 26. Suma Valor de venta por ítem-->
				<xsl:variable name="sumValorVentaItem" select="sum(./cbc:LineExtensionAmount)"/>									
				<!-- 27. Suma Monto del tributo del ítem -->
				<xsl:variable name="sumMontoTributoItem" select="sum(./cac:TaxTotal/cac:TaxSubTotal/cbc:TaxAmount)"/>														
				
				<!-- 26. Número de orden del ítem - detalle del partícipe  -->	
				<xsl:choose>
					<xsl:when test="not($numeroOrden)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2492'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', $numeroItem, ' ', $numeroOrden)" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if
							test='not(fn:matches($numeroOrden,"^[0-9]{1,3}?$")) or $numeroOrden &lt;= 0'>
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2492'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', $numeroItem, ' ', $numeroOrden)" />
							</xsl:call-template>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>	

<!-- 				<xsl:if test="count(key('by-subInvoiceLine-id', number($numeroOrden))) &gt; 1"> -->
<!-- 					<xsl:call-template name="rejectCall"> -->
<!-- 						<xsl:with-param name="errorCode" select="'2493'" /> -->
<!-- 						<xsl:with-param name="errorMessage" -->
<!-- 							select="concat('Error en la linea: ', $numeroItem, ' ', $numeroOrden)" /> -->
<!-- 					</xsl:call-template> -->
<!-- 				</xsl:if> -->
							
				<xsl:if test="count(key('by-subInvoiceLine-id', concat($numeroItem, '-',$numeroOrden))) &gt; 1">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2493'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $numeroOrden, ' ',count(key('by-subInvoiceLine-id', concat($numeroItem, '-',$numeroOrden))))" />
					</xsl:call-template>
				</xsl:if>							
							
				<!-- 27. Unidad de medida por ítem -->		
				<xsl:call-template name="existValidateElementNotExist">
					<xsl:with-param name="errorCodeNotExist" select="'2883'"/>
					<xsl:with-param name="node" select="$unidadMedidaItem"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $unidadMedidaItem)"/>
				</xsl:call-template>
				
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4258'" />
					<xsl:with-param name="node" select="$unidadMedidaItem_ListID" />
					<xsl:with-param name="regexp" select="'^(UN/ECE rec 20)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $unidadMedidaItem_ListID)"/>
				</xsl:call-template>			
			
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4259'" />
					<xsl:with-param name="node" select="$unidadMedidaItem_ListAgencyName" />
					<xsl:with-param name="regexp" select="'^(United Nations Economic Commission for Europe)$'" />
					<xsl:with-param name="isError" select="false()"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $unidadMedidaItem_ListAgencyName)"/>
				</xsl:call-template>								
							
				<!-- 28. Cantidad de unidades por item -->	
				<xsl:call-template name="existValidateElementNotExist">
					<xsl:with-param name="errorCodeNotExist" select="'2024'"/>
					<xsl:with-param name="node" select="$cantidadUnidadesItem"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $cantidadUnidadesItem)"/>
				</xsl:call-template>							
							
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2025'" />
					<xsl:with-param name="node" select="$cantidadUnidadesItem" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $cantidadUnidadesItem)"/>
				</xsl:call-template>
			
				<xsl:for-each select="./cac:Item">	    			
					<!-- 29. Descripción del bien  -->
					<xsl:variable name="descripcionBien" select="./cbc:Description"/>
										
					<!-- 29. Descripción del bien  -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'2026'"/>
						<xsl:with-param name="node" select="$descripcionBien"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $descripcionBien)"/>
					</xsl:call-template>
						
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2027'"/>
						<xsl:with-param name="node" select="$descripcionBien"/>
						<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,1499}$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $descripcionBien)"/>
					</xsl:call-template>														
				</xsl:for-each>	
										
				<xsl:for-each select="./cac:Price">
					<!-- 30. Valor unitario por ítem - detalle del partícipe -->	
					<xsl:variable name="valorUnitarioItemDetalleParticipe" select="./cbc:PriceAmount"/>		
					<xsl:variable name="valorUnitarioItemDetalleParticipe_currencyID" select="./cbc:PriceAmount/@currencyID"/>					
					
					<!-- 30. Valor unitario por ítem - detalle del partícipe -->	
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'2068'"/>
						<xsl:with-param name="node" select="$valorUnitarioItemDetalleParticipe"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $valorUnitarioItemDetalleParticipe)"/>
					</xsl:call-template>
										
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2369'"/>
						<xsl:with-param name="node" select="$valorUnitarioItemDetalleParticipe"/>
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $valorUnitarioItemDetalleParticipe)"/>
					</xsl:call-template>	
					
					<xsl:if test="($valorUnitarioItemDetalleParticipe_currencyID)and not($valorUnitarioItemDetalleParticipe_currencyID = $monedaImporteTotalDAE)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2337'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $valorUnitarioItemDetalleParticipe_currencyID, ' ', $monedaImporteTotalDAE)"/>
						</xsl:call-template>						
					</xsl:if>											
			    </xsl:for-each>	
			    
			    <!-- 31. Valor de venta por ítem - detalle del partícipe -->	
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2486'"/>
					<xsl:with-param name="node" select="$valorVentaParticipeItem"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $numeroItem, ' ', $valorVentaParticipeItem)"/>
				</xsl:call-template>				

				<xsl:variable name="calculoValorUnitarioItem" select="($sumValorUnitarioItem * $cantidadUnidadesItem)"/>
				<xsl:variable name="calculoTotalValorUnitarioItem" select="($calculoValorUnitarioItem + $sumCargosAfectenBaseImponibleItem) - $sumDescuentosAfectenBaseImponibleItem"/>								
				<xsl:variable name="dif_ValorVentaItem" select="$valorVentaParticipeItem - $calculoTotalValorUnitarioItem" />
				<xsl:if test="($dif_ValorVentaItem &lt; -1) or ($dif_ValorVentaItem &gt; 1)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4355'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $dif_ValorVentaItem, ' ', $valorVentaParticipeItem, ' ', $calculoTotalValorUnitarioItem, ' ', $calculoValorUnitarioItem, ' ', $sumCargosAfectenBaseImponibleItem, ' ', $sumDescuentosAfectenBaseImponibleItem)"/>
					</xsl:call-template>
				</xsl:if>												
																																
				<xsl:if test="($valorVentaParticipeItem_currencyID)and not($valorVentaParticipeItem_currencyID = $monedaImporteTotalDAE)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2337'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $valorVentaParticipeItem_currencyID, ' ', $monedaImporteTotalDAE)"/>
					</xsl:call-template>						
				</xsl:if>		
								
				<!-- 32. Total Impuestos por ítem - detalle del partícipe -->				
				<xsl:if test="($countTotalImpuestosParticipeItem = 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2494'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $countTotalImpuestosParticipeItem)"/>
					</xsl:call-template>
				</xsl:if>	
						
				<xsl:if test="($countTotalImpuestosParticipeItem &gt; 1)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2495'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $countTotalImpuestosParticipeItem)"/>
					</xsl:call-template>
				</xsl:if>			
				
				<xsl:if test="($countCodigoTributoItem1000 &gt; 1) or ($countCodigoTributoItem2000 &gt; 1) or ($countCodigoTributoItem9997 &gt; 1)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2499'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $countCodigoTributoItem1000, ' ', $countCodigoTributoItem2000, ' ', $countCodigoTributoItem9997)"/>
					</xsl:call-template>
				</xsl:if>								

				<xsl:if test="($countCodigoTributoItem1000 = 0) and ($countCodigoTributoItem9997 = 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2584'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $countCodigoTributoItem1000, ' ', $countCodigoTributoItem9997)"/>
					</xsl:call-template>
				</xsl:if>	
						    			    
				<xsl:for-each select="./cac:TaxTotal">
					<!-- 32. Total Impuestos por ítem - detalle del partícipe -->	
					<xsl:variable name="totalImpuestosParticipeItem" select="./cbc:TaxAmount"/>
					<xsl:variable name="totalImpuestosParticipeItem_currencyID" select="./cbc:TaxAmount/@currencyID"/>
					
					<!-- 32. Total Impuestos por ítem - detalle del partícipe -->	
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2496'"/>
						<xsl:with-param name="node" select="$totalImpuestosParticipeItem"/>
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $numeroItem, ' ', $totalImpuestosParticipeItem)"/>
					</xsl:call-template>	
										
					<xsl:variable name="dif_TotalImpuestosParticipeItem" select="$totalImpuestosParticipeItem - ($sumMontoTributoItem1000 + $sumMontoTributoItem2000)" />
					<xsl:if test="($dif_TotalImpuestosParticipeItem &lt; -1) or ($dif_TotalImpuestosParticipeItem &gt; 1)">
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="'4356'" />
							<xsl:with-param name="warningMessage"
								select="concat('Error en la linea: ', $numeroItem, ' ', $dif_TotalImpuestosParticipeItem, ' ', $totalImpuestosParticipeItem, ' ', $sumMontoTributoItem1000, ' ', $sumMontoTributoItem2000)"/>
						</xsl:call-template>
					</xsl:if>						
										
					<xsl:if test="($totalImpuestosParticipeItem_currencyID) and not($totalImpuestosParticipeItem_currencyID = $monedaImporteTotalDAE)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2337'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', $numeroItem, ' ', $totalImpuestosParticipeItem_currencyID, ' ', $monedaImporteTotalDAE)"/>
						</xsl:call-template>			
					</xsl:if>		
																				
					<xsl:for-each select="./cac:TaxSubtotal">		
						<!-- 33. Monto de ISC por ítem - detalle del partícipe -->	
						<xsl:variable name="montoTributoItem" select="./cbc:TaxAmount"/>
						<xsl:variable name="montoTributoItem_currencyID" select="./cbc:TaxAmount/@currencyID"/>											
						<!-- 34. Monto de IGV por ítem - detalle por partícipe -->
						<xsl:variable name="montoBaseIGVItem" select="./cbc:TaxableAmount"/>
						<xsl:variable name="montoBaseIGVItem_currencyID" select="./cbc:TaxableAmount/@currencyID"/>							
																	
						<!-- 33. Monto de ISC por ítem - detalle del partícipe -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'2497'"/>
							<xsl:with-param name="node" select="$montoTributoItem"/>
							<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $numeroItem, ' ', $montoTributoItem)"/>
						</xsl:call-template>	
						
						<xsl:if test="($montoTributoItem_currencyID) and not($montoTributoItem_currencyID = $monedaImporteTotalDAE)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2337'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', $numeroItem, ' ', $montoTributoItem_currencyID, ' ', $monedaImporteTotalDAE)"/>
							</xsl:call-template>		
						</xsl:if>		
									
						<!-- 34. Monto de IGV por ítem - detalle por partícipe -->						
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'2590'"/>
							<xsl:with-param name="node" select="$montoBaseIGVItem"/>
							<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $numeroItem, ' ', $montoBaseIGVItem)"/>
						</xsl:call-template>									
				
						<xsl:if test="($montoBaseIGVItem_currencyID) and not($montoBaseIGVItem_currencyID = $monedaImporteTotalDAE)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2337'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', $numeroItem, ' ', $montoBaseIGVItem_currencyID, ' ', $monedaImporteTotalDAE)"/>
							</xsl:call-template>			
						</xsl:if>		
											
						<xsl:for-each select="./cac:TaxCategory">
							<!-- 34. Tipo de sistema de ISC -->	
							<xsl:variable name="tipoSistemaISC" select="./cbc:TierRange"/>				
							<!-- 34. Afectación al IGV -->		
							<xsl:variable name="afectacionIGV" select="./cbc:TaxExemptionReasonCode"/>
																							
							<xsl:for-each select="./cac:TaxScheme">
								<!-- 33. Código de tributo por ítem -->
								<!-- 34. Código de tributo por ítem -->		
								<xsl:variable name="codigoTributoItem" select="./cbc:ID"/>	
								<xsl:variable name="codigoTributoItem_schemeName" select="./cbc:ID/@schemeName"/>
								<xsl:variable name="codigoTributoItem_schemeAgencyName" select="./cbc:ID/@schemeAgencyName"/>
								<xsl:variable name="codigoTributoItem_schemeURI" select="./cbc:ID/@schemeURI"/>								
								
								<!-- 33. Código de tributo por ítem -->
								<!-- 34. Código de tributo por ítem -->	
								<xsl:call-template name="existValidateElementNotExist">
									<xsl:with-param name="errorCodeNotExist" select="'2498'"/>
									<xsl:with-param name="node" select="$codigoTributoItem" />
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', $numeroItem, ' ', $codigoTributoItem)"/>
								</xsl:call-template>	
								
								<xsl:call-template name="regexpValidateElementIfExist">
									<xsl:with-param name="errorCodeValidate" select="'2036'"/>
									<xsl:with-param name="node" select="$codigoTributoItem"/>
									<xsl:with-param name="regexp" select="'^(1000|2000|9997)$'" />
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', $numeroItem, ' ', $codigoTributoItem)"/>
								</xsl:call-template>		
								
								<!-- 34.Tipo de sistema de ISC -->
								<xsl:if test="($codigoTributoItem='2000') and ($montoTributoItem &gt; 0) ">	
									<xsl:call-template name="existValidateElementNotExist">
										<xsl:with-param name="errorCodeNotExist" select="'2373'"/>
										<xsl:with-param name="node" select="$tipoSistemaISC" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', $numeroItem, ' ', $codigoTributoItem, ' ', $montoTributoItem, ' ', $tipoSistemaISC)"/>
									</xsl:call-template>	
								
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'2041'"/>
										<xsl:with-param name="node" select="$tipoSistemaISC"/>
										<xsl:with-param name="regexp" select="'^(01|02|03)$'" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', $numeroItem, ' ', $codigoTributoItem, ' ', $montoTributoItem, ' ', $tipoSistemaISC)"/>
									</xsl:call-template>
								</xsl:if>
								
								<xsl:if test="not($codigoTributoItem = '2000')">
									<xsl:call-template name="existValidateElementExist">
										<xsl:with-param name="errorCodeNotExist" select="'3210'"/>
										<xsl:with-param name="node" select="$tipoSistemaISC" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', $numeroItem, ' ', $codigoTributoItem, ' ', $tipoSistemaISC)"/>
									</xsl:call-template>	
								</xsl:if>	
								
								<xsl:if test="($codigoTributoItem = '1000') or ($codigoTributoItem = '9997')">	
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'2040'"/>
										<xsl:with-param name="node" select="$afectacionIGV"/>
										<xsl:with-param name="regexp" select="'^(10|11|12|13|14|15|16|17|20|21|30|31|32|33|34|35|36|37|40)$'" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', position(), ' ', $afectacionIGV, ' ',$codigoTributoItem)"/>
									</xsl:call-template>
								</xsl:if>																
																
								<xsl:if test="($codigoTributoItem = 1000)">	
									<xsl:variable name="calculo_subTotalMontoIGV" select="$montoBaseIGVItem * 0.18" />
								   	<xsl:variable name="dif_subTotalMontoIGV" select="$montoTributoItem - $calculo_subTotalMontoIGV" />
									<xsl:if test="($dif_subTotalMontoIGV &lt; -1) or ($dif_subTotalMontoIGV &gt; 1)">
										<xsl:call-template name="addWarning">
											<xsl:with-param name="warningCode" select="'4365'" />
											<xsl:with-param name="warningMessage"
												select="concat('Error en la linea: ', $numeroItem, ' ', $dif_subTotalMontoIGV, ' ', $montoTributoItem, ' ', $calculo_subTotalMontoIGV, ' ', $montoBaseIGVItem)"/>
										</xsl:call-template>
									</xsl:if>								
								</xsl:if>												
											
								<xsl:if test="($codigoTributoItem = 9997) and not($montoTributoItem = 0)">	
									<xsl:call-template name="addWarning">
										<xsl:with-param name="warningCode" select="'4365'" />
										<xsl:with-param name="warningMessage"
											select="concat('Error en la linea: ', $numeroItem, ' ', $codigoTributoItem, ' ', $montoTributoItem)"/>
									</xsl:call-template>							
								</xsl:if>												
																						
								<xsl:call-template name="regexpValidateElementIfExist">
									<xsl:with-param name="errorCodeValidate" select="'4255'" />
									<xsl:with-param name="node" select="$codigoTributoItem_schemeName" />
									<xsl:with-param name="regexp" select="'^(Codigo de tributos)$'" />
									<xsl:with-param name="isError" select="false()"/>
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', $numeroItem, ' ', $codigoTributoItem_schemeName)"/>
								</xsl:call-template>		
							
								<xsl:call-template name="regexpValidateElementIfExist">
									<xsl:with-param name="errorCodeValidate" select="'4256'" />
									<xsl:with-param name="node" select="$codigoTributoItem_schemeAgencyName" />
									<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
									<xsl:with-param name="isError" select="false()"/>
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', $numeroItem, ' ', $codigoTributoItem_schemeAgencyName)"/>
								</xsl:call-template>
						
								<xsl:call-template name="regexpValidateElementIfExist">
									<xsl:with-param name="errorCodeValidate" select="'4257'" />
									<xsl:with-param name="node" select="$codigoTributoItem_schemeURI" />
									<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo05)$'" />
									<xsl:with-param name="isError" select="false()"/>
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', $numeroItem, ' ', $codigoTributoItem_schemeURI)"/>
								</xsl:call-template>								
							</xsl:for-each>		
						</xsl:for-each>	
					</xsl:for-each>	
				</xsl:for-each>			
								
				<!-- 35. Cargos y Descuentos por ítem - detalle por partícipe -->
				<xsl:for-each select="./cac:AllowanceCharge">
					<!-- 35. Indicador de cargo/descuento -->	
					<xsl:variable name="indicadorCargoDescuentoItem" select="./cbc:ChargeIndicator"/>
					<!-- 35. Código de cargo/descuento -->
					<xsl:variable name="codigoCargoDescuentoItem" select="./cbc:AllowanceChargeReasonCode"/>
					<xsl:variable name="codigoCargoDescuentoItem_listAgencyName" select="./cbc:AllowanceChargeReasonCode/@listAgencyName"/>
					<xsl:variable name="codigoCargoDescuentoItem_listName" select="./cbc:AllowanceChargeReasonCode/@listName"/>
					<xsl:variable name="codigoCargoDescuentoItem_listURI" select="./cbc:AllowanceChargeReasonCode/@listURI"/>
					<!-- 35. Factor de cargo/descuento -->
					<xsl:variable name="factorCargoDescuentoItem" select="./cbc:MultiplierFactorNumeric"/>						
					<!-- 35. Monto de cargo/descuento -->
					<xsl:variable name="montoCargoDescuentoItem" select="./cbc:Amount"/>
					<xsl:variable name="montoCargoDescuentoItem_currencyID" select="./cbc:Amount/@currencyID"/>			
					<!-- 35. Monto base del cargo/descuento -->
					<xsl:variable name="montoBaseCargoDescuentoItem" select="./cbc:BaseAmount"/>
					<xsl:variable name="montoBaseCargoDescuentoItem_currencyID" select="./cbc:BaseAmount/@currencyID"/>					
					
					<!-- 35. Indicador de cargo/descuento -->
					<xsl:choose>
						<xsl:when test="not($indicadorCargoDescuentoItem = 'true')">
							<xsl:call-template name="regexpValidateElementIfExistTrue">
								<xsl:with-param name="errorCodeValidate" select="'2585'"/>
								<xsl:with-param name="node" select="$codigoCargoDescuentoItem"/>
								<xsl:with-param name="regexp" select="'^(47|54)$'" />
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', $numeroItem, ' ', $indicadorCargoDescuentoItem, ' ', $codigoCargoDescuentoItem)"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="not($indicadorCargoDescuentoItem = 'false')">
							<xsl:call-template name="regexpValidateElementIfExistTrue">
								<xsl:with-param name="errorCodeValidate" select="'2585'"/>
								<xsl:with-param name="node" select="$codigoCargoDescuentoItem"/>
								<xsl:with-param name="regexp" select="'^(00|07)$'" />
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', $numeroItem, ' ', $indicadorCargoDescuentoItem, ' ', $codigoCargoDescuentoItem)"/>
							</xsl:call-template>
						</xsl:when>						
					</xsl:choose>					
										
					<!-- 35. Código de cargo/descuento -->	
					<xsl:if test="($indicadorCargoDescuentoItem)">	
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'2586'"/>
							<xsl:with-param name="node" select="$codigoCargoDescuentoItem" />
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $numeroItem, ' ', $codigoCargoDescuentoItem)"/>
						</xsl:call-template>					
					</xsl:if>
							
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4357'"/>
						<xsl:with-param name="node" select="$codigoCargoDescuentoItem"/>
						<xsl:with-param name="regexp" select="'^(00|07|47|54)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $numeroItem, ' ', $codigoCargoDescuentoItem)"/>
					</xsl:call-template>					
							
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4251'" />
						<xsl:with-param name="node" select="$codigoCargoDescuentoItem_listAgencyName" />
						<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: 1) ', $numeroItem, ' ', $codigoCargoDescuentoItem_listAgencyName)"/>
					</xsl:call-template>	
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4252'" />
						<xsl:with-param name="node" select="$codigoCargoDescuentoItem_listName" />
						<xsl:with-param name="regexp" select="'^(Cargo/descuento)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: 1) ', $numeroItem, ' ', $codigoCargoDescuentoItem_listName)"/>
					</xsl:call-template>	

					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4253'" />
						<xsl:with-param name="node" select="$codigoCargoDescuentoItem_listURI" />
						<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo53)$'" />
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: 1) ', $numeroItem, ' ', $codigoCargoDescuentoItem_listURI)"/>
					</xsl:call-template>			

					<!-- 35. Factor de cargo/descuento -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2587'"/>
						<xsl:with-param name="node" select="$factorCargoDescuentoItem"/>
						<xsl:with-param name="regexp" select="'^[0-9]{1,3}(\.[0-9]{1,5})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $numeroItem, ' ', $factorCargoDescuentoItem)"/>
					</xsl:call-template>	

					<!-- 35. Monto de cargo/descuento -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2588'"/>
						<xsl:with-param name="node" select="$montoCargoDescuentoItem"/>
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $numeroItem, ' ', $montoCargoDescuentoItem)"/>
					</xsl:call-template>	
					
					<xsl:if test="($codigoCargoDescuentoItem) and ($factorCargoDescuentoItem &gt; 0)">	
					    <xsl:variable name="calculoMontoCargoDescuentoItem" select="$montoBaseCargoDescuentoItem * $factorCargoDescuentoItem"/>
					    <xsl:variable name="dif_MontoCargoDescuentoItem" select="$montoCargoDescuentoItem - $calculoMontoCargoDescuentoItem" />
						<xsl:if test="($dif_MontoCargoDescuentoItem &lt; -1) or ($dif_MontoCargoDescuentoItem &gt; 1)">
							<xsl:call-template name="addWarning">
								<xsl:with-param name="warningCode" select="'4358'" />
								<xsl:with-param name="warningMessage"
									select="concat('Error en la linea: ', $numeroItem, ' ', $dif_MontoCargoDescuentoItem, ' ', $montoCargoDescuentoItem, ' ', $calculoMontoCargoDescuentoItem, ' ', $montoBaseCargoDescuentoItem, ' ', $factorCargoDescuentoItem, ' ', $codigoCargoDescuentoItem)" />
							</xsl:call-template>
						</xsl:if>																																													
					</xsl:if>						
					
					<xsl:if test="($montoCargoDescuentoItem_currencyID) and not($montoCargoDescuentoItem_currencyID = $monedaImporteTotalDAE)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2337'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', $numeroItem, ' ', $montoCargoDescuentoItem_currencyID, ' ', $monedaImporteTotalDAE)"/>
						</xsl:call-template>	
					</xsl:if>		
									
					<!-- 35. Monto base del cargo/descuento -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2589'"/>
						<xsl:with-param name="node" select="$montoBaseCargoDescuentoItem"/>
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $numeroItem, ' ', $montoBaseCargoDescuentoItem)"/>
					</xsl:call-template>	
	
					<xsl:if test="($montoBaseCargoDescuentoItem_currencyID) and not($montoBaseCargoDescuentoItem_currencyID = $monedaImporteTotalDAE)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2337'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', $numeroItem, ' ', $montoBaseCargoDescuentoItem_currencyID, ' ', $monedaImporteTotalDAE)"/>
						</xsl:call-template>		
					</xsl:if>														
				</xsl:for-each>			

				<xsl:if test="($countImporteTotalItem = 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2482'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $countImporteTotalItem)"/>
					</xsl:call-template>
				</xsl:if>
							
				<xsl:for-each select="./cac:ItemPriceExtension">
					<!-- 29. Importe total Entidad Emisor -->	
					<xsl:variable name="importeTotalItem" select="./cbc:Amount"/>
					<xsl:variable name="importeTotalItem_currencyID" select="./cbc:Amount/@currencyID"/>			
				
					<!-- 29. Importe total Entidad Emisor -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2483'"/>
						<xsl:with-param name="node" select="$importeTotalItem"/>
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $importeTotalItem)"/>
					</xsl:call-template>				
				
				   	<xsl:variable name="dif_importeTotalItem" select="$importeTotalItem - ($sumValorVentaItem + $sumMontoTributoItem1000 + $sumMontoTributoItem2000)" />
					<xsl:if test="($dif_importeTotalItem &lt; -1) or ($dif_importeTotalItem &gt; 1)">
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="'4349'" />
							<xsl:with-param name="warningMessage"
								select="concat('Error en la linea: ', position(), ' ', $dif_importeTotalItem, ' ', $importeTotalItem, ' ', $sumValorVentaItem, ' ', $sumMontoTributoItem1000, ' ', $sumMontoTributoItem2000)"/>
						</xsl:call-template>
					</xsl:if>				
				
					<xsl:if test="($importeTotalItem_currencyID) and not($importeTotalItem_currencyID = $monedaImporteTotalDAE)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2337'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ', $importeTotalItem_currencyID, ' ', $monedaImporteTotalDAE)"/>
						</xsl:call-template>		
					</xsl:if>							
				</xsl:for-each>																							
			</xsl:for-each>		
		</xsl:for-each>		
		
		

		<!-- 40. Importe Total - Impuestos -->				
		<xsl:if test="($countImporteTotalImpuestos = 0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2956'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $countImporteTotalImpuestos)"/>
			</xsl:call-template>
		</xsl:if>	
				
		<xsl:if test="($countImporteTotalImpuestos &gt; 1)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3024'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $countImporteTotalImpuestos)"/>
			</xsl:call-template>
		</xsl:if>			
		
		<xsl:if test="($countCodigoTributo1000 &gt; 1) or ($countCodigoTributo2000 &gt; 1) or ($countCodigoTributo9997 &gt; 1)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'3068'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $countCodigoTributo1000, ' ', $countCodigoTributo2000, ' ', $countCodigoTributo9997)"/>
			</xsl:call-template>
		</xsl:if>								

		<xsl:if test="($countCodigoTributo1000 = 0) and ($countCodigoTributo9997 = 0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2278'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $countCodigoTributo1000, ' ', $countCodigoTributo9997)"/>
			</xsl:call-template>
		</xsl:if>	

		<xsl:for-each select="./cac:TaxTotal">
			<!-- 38. Importe Total - Impuestos -->	
			<xsl:variable name="importeTotalImpuestos" select="./cbc:TaxAmount"/>
			<xsl:variable name="importeTotalImpuestos_currencyID" select="./cbc:TaxAmount/@currencyID"/>			
			<!-- 21. Suma Monto total - IGV -->	
			<xsl:variable name="sumMontoTotalIGV" select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxAmount)"/>	
			<!-- 22. Suma Monto total - ISC -->
			<xsl:variable name="sumMontoTotalISC" select="sum(./cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxAmount)"/>	
					
			<xsl:if test="($importeTotalImpuestos_currencyID) and not($importeTotalImpuestos_currencyID = $monedaImporteTotalDAE)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2337'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $importeTotalImpuestos_currencyID, ' ', $monedaImporteTotalDAE)"/>
				</xsl:call-template>		
			</xsl:if>

			<xsl:variable name="sumaSumatoriaISCIGV" select="$sumMontoTotalIGV + $sumMontoTotalISC"/>
		    <xsl:variable name="dif_TotalImpuestos" select="$importeTotalImpuestos - $sumaSumatoriaISCIGV" />
			<xsl:if test="($dif_TotalImpuestos &lt; -1) or ($dif_TotalImpuestos &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4301'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', $dif_TotalImpuestos,'-', $importeTotalImpuestos,'-',$sumaSumatoriaISCIGV, '-', $sumMontoTotalIGV,'-', $sumMontoTotalISC)"/>
				</xsl:call-template>
			</xsl:if>	
																										
			<xsl:for-each select="./cac:TaxSubtotal">		
				<!-- 38. Monto total - IGV -->
				<!-- 39. Monto total - ISC -->
				<xsl:variable name="montoTotalISCIGV" select="./cbc:TaxAmount"/>
				<xsl:variable name="montoTotalISCIGV_currencyID" select="./cbc:TaxAmount/@currencyID"/>					
				
				<!-- 38. Monto total - IGV -->
				<!-- 39. Monto total - ISC -->				
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2048'"/>
					<xsl:with-param name="node" select="$montoTotalISCIGV"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $montoTotalISCIGV)"/>
				</xsl:call-template>	
				
				<xsl:if test="($montoTotalISCIGV_currencyID) and not($montoTotalISCIGV_currencyID = $monedaImporteTotalDAE)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2337'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $montoTotalISCIGV_currencyID, ' ', $monedaImporteTotalDAE)"/>
					</xsl:call-template>		
				</xsl:if>		
									
				<xsl:for-each select="./cac:TaxCategory">
					<xsl:for-each select="./cac:TaxScheme">
						<!-- 38. Código de tributo -->
						<!-- 39. Código de tributo -->		
						<xsl:variable name="codigoTributo" select="./cbc:ID"/>	
						<xsl:variable name="codigoTributo_schemeName" select="./cbc:ID/@schemeName"/>
						<xsl:variable name="codigoTributo_schemeAgencyName" select="./cbc:ID/@schemeAgencyName"/>
						<xsl:variable name="codigoTributo_schemeURI" select="./cbc:ID/@schemeURI"/>								
						
						<!-- 38. Código de tributo ISC -->
						<!-- 39. Código de tributo IGV -->	
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'3059'"/>
							<xsl:with-param name="node" select="$codigoTributo" />
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $codigoTributo)"/>
						</xsl:call-template>	
						
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'3007'"/>
							<xsl:with-param name="node" select="$codigoTributo"/>
							<xsl:with-param name="regexp" select="'^(1000|2000|9997)$'" />
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $codigoTributo)"/>
						</xsl:call-template>		

						<!-- ////  -->
						<!--
						<xsl:if test="($codigoTributo = 1000)">
						   	<xsl:variable name="dif_MontoTotalISCIGV" select="$montoTotalISCIGV - $sumSumatoriaIGVPartícipe" />
							<xsl:if test="($dif_MontoTotalISCIGV &lt; -1) or ($dif_MontoTotalISCIGV &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4290'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', $dif_MontoTotalISCIGV, ' ', $montoTotalISCIGV, ' ', $sumSumatoriaIGVPartícipe)"/>
								</xsl:call-template>
							</xsl:if>																																									
						</xsl:if>
						-->
						
						<!-- ////  -->
						<!-- 
						<xsl:if test="($codigoTributo = 2000)">
						   	<xsl:variable name="dif_MontoTotalISCIGV" select="$montoTotalISCIGV - $sumSumatoriaIISCPartícipe" />
							<xsl:if test="($dif_MontoTotalISCIGV &lt; -1) or ($dif_MontoTotalISCIGV &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4305'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', $dif_MontoTotalISCIGV, ' ', $montoTotalISCIGV, ' ', $sumSumatoriaIISCPartícipe)"/>
								</xsl:call-template>
							</xsl:if>																																									
						</xsl:if>
								-->
																										
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4255'" />
							<xsl:with-param name="node" select="$codigoTributo_schemeName" />
							<xsl:with-param name="regexp" select="'^(Codigo de tributos)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 1) ', $codigoTributo_schemeName)"/>
						</xsl:call-template>		
					
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4256'" />
							<xsl:with-param name="node" select="$codigoTributo_schemeAgencyName" />
							<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 1) ', $codigoTributo_schemeAgencyName)"/>
						</xsl:call-template>
				
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4257'" />
							<xsl:with-param name="node" select="$codigoTributo_schemeURI" />
							<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo05)$'" />
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: 1) ', $codigoTributo_schemeURI)"/>
						</xsl:call-template>																													
					</xsl:for-each>		
				</xsl:for-each>	
			</xsl:for-each>	
		</xsl:for-each>	
		
		<xsl:variable name="sumImporteTotalImpuestos" select="sum(./cac:TaxTotal/cbc:TaxAmount)"/>				
		<xsl:for-each select="cac:LegalMonetaryTotal">
			<!-- 37. Importe total  - Valor de Venta -->
			<xsl:variable name="importeTotalValorVenta" select="./cbc:LineExtensionAmount"/>
			<xsl:variable name="importeTotalValorVenta_currencyID" select="./cbc:LineExtensionAmount/@currencyID"/>					
			<!-- 41. Importe Total - Descuentos (Que no afectan la base)-->
			<xsl:variable name="importeTotalDescuentos" select="./cbc:AllowanceTotalAmount"/>		
			<xsl:variable name="importeTotalDescuentos_currencyID" select="./cbc:AllowanceTotalAmount/@currencyID"/>	
			<!-- 42. Importe Total - Cargos (Que no afectan la base)-->
			<xsl:variable name="importeTotalCargos" select="./cbc:ChargeTotalAmount"/>		
			<xsl:variable name="importeTotalCargos_currencyID" select="./cbc:ChargeTotalAmount/@currencyID"/>	
			<!-- 43. Importe total de DAE -->
			<xsl:variable name="importeTotalDAE" select="./cbc:PayableAmount"/>		
			<xsl:variable name="importeTotalDAE_currencyID" select="./cbc:PayableAmount/@currencyID"/>	
			
			<!-- 37. Importe total  - Valor de Venta -->
			<xsl:variable name="sumImporteTotalValorVenta" select="sum(./cbc:LineExtensionAmount)"/>				
			<!-- 41. Importe Total - Descuentos (Que no afectan la base)-->
			<xsl:variable name="sumImporteTotalDescuentos" select="sum(./cbc:AllowanceTotalAmount)"/>			
			<!-- 42. Importe Total - Cargos (Que no afectan la base)-->
			<xsl:variable name="sumImporteTotalCargos" select="sum(./cbc:ChargeTotalAmount)"/>		
			<!-- 43. Importe total de DAE -->
			<xsl:variable name="sumImporteTotalDAE" select="sum(./cbc:PayableAmount)"/>		
												
			<!-- 37. Importe total procesado en el periodo -->		
			<xsl:call-template name="existValidateElementNotExist">
				<xsl:with-param name="errorCodeNotExist" select="'2487'"/>
				<xsl:with-param name="node" select="$importeTotalValorVenta"/>			
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ',  $importeTotalValorVenta)"/>
			</xsl:call-template>	
					
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2488'"/>
				<xsl:with-param name="node" select="$importeTotalValorVenta"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $importeTotalValorVenta)"/>
			</xsl:call-template>			

		   	<xsl:variable name="dif_ImporteTotalValorVenta" select="$sumImporteTotalValorVenta - $sumTotalValorVenta" />
			<xsl:if test="($dif_ImporteTotalValorVenta &lt; -1) or ($dif_ImporteTotalValorVenta &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4309'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', position(), ' ', $dif_ImporteTotalValorVenta, ' ', $sumImporteTotalValorVenta, ' ', $sumTotalValorVenta)"/>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if test="($importeTotalValorVenta_currencyID) and not($importeTotalValorVenta_currencyID = $monedaImporteTotalDAE)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2337'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $importeTotalValorVenta_currencyID, ' ', $monedaImporteTotalDAE)"/>
				</xsl:call-template>		
			</xsl:if>	
			
			<!-- 41. Importe Total - Descuentos (Que no afectan la base)-->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2065'"/>
				<xsl:with-param name="node" select="$importeTotalDescuentos"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $importeTotalDescuentos)"/>
			</xsl:call-template>	
			
		   	<xsl:variable name="dif_ImporteTotalDescuentos" select="$sumImporteTotalDescuentos - $sumMontoDescuento" />
			<xsl:if test="($dif_ImporteTotalDescuentos &lt; -1) or ($dif_ImporteTotalDescuentos &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4307'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', position(), ' ', $dif_ImporteTotalDescuentos, ' ', $sumImporteTotalDescuentos, ' ', $sumMontoDescuento)"/>
				</xsl:call-template>
			</xsl:if>						
			
			<xsl:if test="($importeTotalDescuentos_currencyID) and not($importeTotalDescuentos_currencyID = $monedaImporteTotalDAE)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2337'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $importeTotalDescuentos_currencyID, ' ', $monedaImporteTotalDAE)"/>
				</xsl:call-template>		
			</xsl:if>	
						
			<!-- 42. Importe Total - Cargos (Que no afectan la base)-->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2064'"/>
				<xsl:with-param name="node" select="$importeTotalCargos"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $importeTotalCargos)"/>
			</xsl:call-template>			
			
		   	<xsl:variable name="dif_ImporteTotalCargos" select="$sumImporteTotalCargos - $sumMontoCargo" />
			<xsl:if test="($dif_ImporteTotalCargos &lt; -1) or ($dif_ImporteTotalCargos &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4308'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', position(), ' ', $dif_ImporteTotalCargos, ' ', $sumImporteTotalCargos, ' ', $sumMontoCargo)"/>
				</xsl:call-template>
			</xsl:if>			
			
			<xsl:if test="($importeTotalCargos_currencyID) and not($importeTotalCargos_currencyID = $monedaImporteTotalDAE)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2337'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $importeTotalCargos_currencyID, ' ', $monedaImporteTotalDAE)"/>
				</xsl:call-template>		
			</xsl:if>							
				
			<!-- 43. Importe total de DAE -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2062'"/>
				<xsl:with-param name="node" select="$importeTotalDAE"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $importeTotalDAE)"/>
			</xsl:call-template>			

		   	<xsl:variable name="dif_importeTotalDAE" select="$sumImporteTotalDAE - (($sumImporteTotalValorVenta + $sumImporteTotalImpuestos + $sumImporteTotalCargos) - $sumImporteTotalDescuentos)" />
			<xsl:if test="($dif_importeTotalDAE &lt; -1) or ($dif_importeTotalDAE &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4312'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', position(), '-', $dif_importeTotalDAE, '-', $sumImporteTotalDAE, '-', $sumImporteTotalValorVenta, '-', $sumImporteTotalImpuestos, '-', $sumImporteTotalCargos, '-', $sumImporteTotalDescuentos)"/>
				</xsl:call-template>
			</xsl:if>	

			<xsl:if test="($importeTotalDAE_currencyID) and not($importeTotalDAE_currencyID = $monedaImporteTotalDAE)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3088'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $importeTotalDAE_currencyID, ' ', $monedaImporteTotalDAE)"/>
				</xsl:call-template>		
			</xsl:if>			
		</xsl:for-each>																																		
	</xsl:template>	
</xsl:stylesheet>
