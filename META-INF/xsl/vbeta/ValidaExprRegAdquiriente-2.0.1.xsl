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
	<xsl:key name="by-invoiceLine-id" match="*[local-name()='Invoice']/cac:InvoiceLine" use="number(cbc:ID)" />
<!-- 	<xsl:key name="by-subInvoiceLine-id" match="*[local-name()='Invoice']/cac:InvoiceLine/cac:SubInvoiceLine" use="number(cbc:ID)" /> -->
<!-- 	<xsl:key name="by-subInvoiceLine-id-2" match="*[local-name()='Invoice']/cac:InvoiceLine" use="concat(cbc:ID,'-', ./cac:SubInvoiceLine/cbc:ID)" /> -->
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
		<!-- 8. Periodo de abono: Fecha desde -->
		<xsl:variable name="periodoAbonoFechaDesde" select="cac:InvoicePeriod/cbc:StartDate"/>						
		<!-- 9. Periodo de abono: Fecha hasta -->
		<xsl:variable name="periodoAbonoFechaHasta" select="cac:InvoicePeriod/cbc:EndDate"/>	
		<!-- 10. Tipo de canal facturado (fisico/virtual) -->
		<xsl:variable name="tipoCanalFacturado" select="cbc:InvoiceTypeCode/@listID"/>	
		<!-- Datos del documento -->
		<!-- 22. Importe total  -->
		<!-- 'Importe Total' de Entidades financieras  - Resumen de comisiones y gastos de emisores de las tarjetas de bancos locales -->
		<xsl:variable name="sumImporteTotal_1" select="sum(./cac:InvoiceLine[cac:Item/cac:SellersItemIdentification/cbc:ID/@schemeID='1']/cac:ItemPriceExtension/cbc:Amount)"/>
		<!-- 'Importe Total' de Entidades financieras  - Resumen de comisiones y gastos de emisores de las tarjetas de bancos foraneos  -->
		<xsl:variable name="sumImporteTotal_2" select="sum(./cac:InvoiceLine[cac:Item/cac:SellersItemIdentification/cbc:ID/@schemeID='2']/cac:ItemPriceExtension/cbc:Amount)"/>
		<!-- 'Importe Total' de Adquirente en los sistemas de pago - Resumen de comisiones y gastos -->
		<xsl:variable name="sumImporteTotal_ADQ" select="sum(./cac:InvoiceLine[cac:Item/cac:SellersItemIdentification/cbc:ID='2']/cac:ItemPriceExtension/cbc:Amount)"/> 		
		<!-- 37. Moneda Importe total procesado en el periodo  -->
		<xsl:variable name="sumImporteTotalProcesadoPeriodo" select="sum(./cac:LegalMonetaryTotal/cbc:LineExtensionAmount)"/>												
		<xsl:variable name="monedaImporteTotalProcesadoPeriodo" select="cac:LegalMonetaryTotal/cbc:LineExtensionAmount/@currencyID"/>	

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
			<xsl:with-param name="regexp" select="'^(30|42)$'"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoDocumento)"/>
		</xsl:call-template>		
		
		<!-- 8. Periodo de abono: Fecha desde -->		
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'2472'"/>
			<xsl:with-param name="node" select="$periodoAbonoFechaDesde"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $periodoAbonoFechaDesde)"/>
		</xsl:call-template>			

		<!-- 9. Periodo de abono: Fecha hasta -->		
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'2473'"/>
			<xsl:with-param name="node" select="$periodoAbonoFechaHasta"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $periodoAbonoFechaHasta)"/>
		</xsl:call-template>

		<!-- 10. Tipo de canal facturado (fisico/virtual) -->		
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'2474'" />
			<xsl:with-param name="node" select="$tipoCanalFacturado" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoCanalFacturado)"/>
		</xsl:call-template>
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2475'" />
			<xsl:with-param name="node" select="$tipoCanalFacturado"/>
			<xsl:with-param name="regexp" select="'^(01|02)$'"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoCanalFacturado)"/>
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
					<!-- 11. Número de RUC -->
					<xsl:variable name="emisorNumeroDocumento" select="./cbc:ID"/>	
					<!-- 11. Tipo de documento de identidad del emisor -->
					<xsl:variable name="emisorTipoDocumento" select="./cbc:ID/@schemeID"/>	
					
					<!-- 11. Número de RUC -->
					<xsl:if test="not($numeroRuc = $emisorNumeroDocumento)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'1034'" />
							<xsl:with-param name="errorMessage"
								select="concat('ruc del xml diferente al nombre del archivo ', $emisorNumeroDocumento, ' diff ', $numeroRuc)" />
						</xsl:call-template>
					</xsl:if>						
					
					<!-- 11. Tipo de documento de identidad del emisor -->							
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
					<!-- 13. Número de documento de identidad del adquirente o usuario -->
					<xsl:variable name="clienteNumeroDocumento" select="./cbc:ID"/>	
					<!-- 13. Tipo de documento de identidad del adquirente o usuario -->
					<xsl:variable name="clienteTipoDocumento" select="./cbc:ID/@schemeID"/>
					
					<!-- 13. Número de documento de identidad del adquirente o usuario -->
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
							<xsl:with-param name="errorCodeValidate" select="'4207'" />
							<xsl:with-param name="node" select="$clienteNumeroDocumento" />
							<xsl:with-param name="regexp" select="'^[\d]{8}$'"/>
							<xsl:with-param name="isError" select="false()"/> 
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', position(), ' ', $clienteNumeroDocumento)"/>
						</xsl:call-template>		
					</xsl:if>					
				
					<xsl:if test="($clienteTipoDocumento='4') or ($clienteTipoDocumento='7') or ($clienteTipoDocumento='0') or 
  							($clienteTipoDocumento='A') or ($clienteTipoDocumento='B') or ($clienteTipoDocumento='C') or  
  							($clienteTipoDocumento='D') or ($clienteTipoDocumento='E')">  
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4208'" />
							<xsl:with-param name="node" select="$clienteNumeroDocumento" />
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,14}$'"/>
							<xsl:with-param name="isError" select="false()"/> 
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
							select="concat('Error en la linea: ',  position(), ' ', $clienteTipoDocumento)"/>
					</xsl:call-template>				
				</xsl:for-each>		
				
				<xsl:for-each select="./cac:PartyLegalEntity">
					<!-- 14. Apellidos y nombres, denominación o razón social del adquirente o usuario -->
					<xsl:variable name="clienteRazonSocial" select="./cbc:RegistrationName"/>		
					
					<!-- 14. Apellidos y nombres, denominación o razón social del adquirente o usuario -->
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
				
				<xsl:for-each select="./cac:PartyName">
					<!-- 15. Nombre Comercial -->
					<xsl:variable name="clienteNombreComercial" select="./cbc:Name"/>	
									
					<!-- 15. Nombre Comercial -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4099'"/>
						<xsl:with-param name="node" select="$clienteNombreComercial"/>
						<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,1499}$'"/>
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $clienteNombreComercial)"/>
					</xsl:call-template>								
				</xsl:for-each>		
																						
			</xsl:for-each>
		</xsl:for-each>	
				
		<!-- Entidades financieras  - Resumen de comisiones y gastos de emisores de las tarjetas -->
		<!-- Adquirente en los sistemas de pago - Resumen de comisiones y gastos -->
		<xsl:for-each select="cac:InvoiceLine">
			<!-- 17. Número de orden del Ítem -->
			<xsl:variable name="numeroItem" select="./cbc:ID"/>	
			<!-- 20. Total comisiones -->
			<!-- 31. Monto de la Comisión -->
			<xsl:variable name="totalComisiones" select="./cbc:LineExtensionAmount"/>
			<!-- 32. Tipo de moneda de la comisión -->		
			<xsl:variable name="totalComisiones_currencyID" select="./cbc:LineExtensionAmount/@currencyID"/>	
			<!-- Datos de la Linea -->
			<!-- 18. Indicador de tipo de comisión -->
			<!-- 33. Indicador de tipo de comisión -->					
			<xsl:variable name="indicadorTipoComisionLinea" select="./cac:Item/cac:SellersItemIdentification/cbc:ID" />										
			<!-- 19. Indicador de institución financiera -->
			<!-- 33. Indicador de tipo de comisión -->					
			<xsl:variable name="indicadorInstituciónFinancieraLinea" select="./cac:Item/cac:SellersItemIdentification/cbc:ID/@schemeID" />							
			<!-- 21. Contar Código de tributo por línea -->					
			<xsl:variable name="countCodigoTributoLinea1000" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])" />
			<!-- 21. Suma Monto de IGV por línea -->
			<xsl:variable name="sumMontoIGV1000Linea" select="sum(./cac:SubInvoiceLine/cac:TaxTotal/cac:TaxSubtotal[cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxAmount)"/>										
			<!-- 22. Contar Importe total por línea -->					
			<xsl:variable name="countImporteTotalLinea" select="count(./cac:ItemPriceExtension)" />
			<!-- 22. Suma Total comisiones por línea -->
			<xsl:variable name="sumTotalComisionesLinea" select="sum(./cbc:LineExtensionAmount)"/>										
			<!-- 21. Suma Total IGV por línea -->
			<xsl:variable name="sumTotalIGVLinea" select="sum(./cac:TaxTotal/cac:TaxSubTotal/cbc:TaxAmount)"/>	
			<xsl:variable name="sumTotalTaxLinea" select="sum(./cac:TaxTotal/cbc:TaxAmount)"/>										
			<!-- 23. Contar Número de orden por línea -->					
			<xsl:variable name="countNumeroOrdenLinea" select="count(./cac:SubInvoiceLine)" />
			<!-- 35. Contar Monto de cargo/descuento LInea -->					
			<xsl:variable name="countTotalIGVLinea" select="count(./cac:TaxTotal)" />			
			<!-- 21. Suma Monto de IGV por Línea -->
			<xsl:variable name="sumTotalIGVAbonoLinea" select="sum(./cac:AllowanceCharge[cbc:ID[text() = 'Abono']]/cbc:Amount)"/>													
			<xsl:variable name="sumTotalIGVCargoLinea" select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode[text() = '00' or text() = '01']]/cbc:Amount)"/>										
			<xsl:variable name="sumTotalIGVDescuentoLinea" select="sum(./cac:AllowanceCharge[cbc:AllowanceChargeReasonCode[text() = '47' or text() = '48']]/cbc:Amount)"/>	
			<!-- 18. Indicador de tipo de comisión -->
			<!-- 33. Indicador de tipo de comisión -->
			<!-- <xsl:variable name="indicadorTipoComisionLinea" select="./cbc:ID"/> -->
			<xsl:variable name="indicadorTipoComisionLinea" select="./cac:Item/cac:SellersItemIdentification/cbc:ID"/>
			
			<!-- 17. Número de orden del Ítem -->
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
			
			<!-- Item -->							
			<xsl:for-each select="./cac:Item">	    			
				<xsl:for-each select="./cac:SellersItemIdentification">
					<!-- 18. Indicador de tipo de comisión -->
					<!-- 33. Indicador de tipo de comisión -->
					<!-- <xsl:variable name="indicadorTipoComisionLinea" select="./cbc:ID"/> -->
					<!-- 19. Indicador de institución financiera -->
					<xsl:variable name="indicadorInstituciónFinanciera" select="./cbc:ID/@schemeID"/>
					
					<!-- 18. Indicador de tipo de comisión -->
					<!-- 33. Indicador de tipo de comisión -->
					<xsl:call-template name="existValidateElementNotExist">
						<xsl:with-param name="errorCodeNotExist" select="'2476'"/>
						<xsl:with-param name="node" select="$indicadorTipoComisionLinea"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $numeroItem, ' ', $indicadorTipoComisionLinea)"/>
					</xsl:call-template>
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2477'" />
						<xsl:with-param name="node" select="$indicadorTipoComisionLinea" />
						<xsl:with-param name="regexp" select="'^(1|2)$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $numeroItem, ' ', $indicadorTipoComisionLinea)"/>
					</xsl:call-template>					
					
					<xsl:if test="($indicadorTipoComisionLinea = '1')">
						<!-- 19. Indicador de institución financiera -->
						<xsl:call-template name="existValidateElementNotExist">
							<xsl:with-param name="errorCodeNotExist" select="'2478'"/>
							<xsl:with-param name="node" select="$indicadorInstituciónFinanciera"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $numeroItem, ' ', $indicadorInstituciónFinanciera)"/>
						</xsl:call-template>
						
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'2479'" />
							<xsl:with-param name="node" select="$indicadorInstituciónFinanciera" />
							<xsl:with-param name="regexp" select="'^(1|2)$'"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $numeroItem, ' ', $indicadorInstituciónFinanciera)"/>
						</xsl:call-template>						
					</xsl:if>
				</xsl:for-each>	
			</xsl:for-each>
								
			<!-- 20. Total comisiones -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2370'"/>
				<xsl:with-param name="node" select="$totalComisiones"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $numeroItem, ' ', $totalComisiones)"/>
			</xsl:call-template>				
																
			<xsl:if test="($indicadorTipoComisionLinea = '1')">					
				<xsl:variable name="sumTotalComisionesItem" select="sum(./cac:SubInvoiceLine/cbc:LineExtensionAmount)"/>	
			   	<xsl:variable name="dif_TotalComisiones" select="$totalComisiones - $sumTotalComisionesItem" />
				<xsl:if test="($dif_TotalComisiones &lt; -1) or ($dif_TotalComisiones &gt; 1)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4354'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $dif_TotalComisiones, ' ', $totalComisiones, ' ', $sumTotalComisionesItem)"/>
					</xsl:call-template>
				</xsl:if>				
			</xsl:if>
							
			<xsl:if test="($totalComisiones_currencyID)">				
				<xsl:if test="not($totalComisiones_currencyID = $monedaImporteTotalProcesadoPeriodo)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2337'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $totalComisiones_currencyID, ' ', $monedaImporteTotalProcesadoPeriodo)"/>
					</xsl:call-template>
				</xsl:if>			
			</xsl:if>	

			<xsl:if test="($countCodigoTributoLinea1000 &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3067'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $numeroItem, ' ', $countCodigoTributoLinea1000)"/>
				</xsl:call-template>
			</xsl:if>
				
			<xsl:if test="($indicadorTipoComisionLinea = '1')">		
				<xsl:if test="($countCodigoTributoLinea1000 = 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2042'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $countCodigoTributoLinea1000)"/>
					</xsl:call-template>
				</xsl:if>				
			</xsl:if>
			
			<!-- 34. Otros Abonos Otros Cargos/descuentos -->			
			<xsl:if test="($indicadorTipoComisionLinea = '2')">			
				<xsl:for-each select="./cac:AllowanceCharge">
					<!-- 34. Indicador de cargo/descuento -->	
					<xsl:variable name="indicadorCargoDescuento" select="./cbc:ChargeIndicator"/>
					<!-- 34. Código de cargo/descuento -->
					<xsl:variable name="codigoCargoDescuento" select="./cbc:AllowanceChargeReasonCode"/>
					<xsl:variable name="codigoCargoDescuento_listAgencyName" select="./cbc:AllowanceChargeReasonCode/@listAgencyName"/>
					<xsl:variable name="codigoCargoDescuento_listName" select="./cbc:AllowanceChargeReasonCode/@listName"/>
					<xsl:variable name="codigoCargoDescuento_listURI" select="./cbc:AllowanceChargeReasonCode/@listURI"/>
					<!-- 34. Monto de cargo/descuento -->
					<xsl:variable name="montoCargoDescuento" select="./cbc:Amount"/>
					<xsl:variable name="montoCargoDescuento_currencyID" select="./cbc:Amount/@currencyID"/>		
					
					<!-- AAA -->
					<!-- 34. Indicador de cargo/descuento -->
					<xsl:choose>
						<xsl:when test="not($indicadorCargoDescuento = 'true')">
							<xsl:call-template name="regexpValidateElementIfExistTrue">
								<xsl:with-param name="errorCodeValidate" select="'3114'"/>
								<xsl:with-param name="node" select="$codigoCargoDescuento"/>
								<xsl:with-param name="regexp" select="'^(47|48)$'" />
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', $numeroItem, ' ', $codigoCargoDescuento, ' ', $indicadorCargoDescuento)"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="not($indicadorCargoDescuento = 'false')">
							<xsl:call-template name="regexpValidateElementIfExistTrue">
								<xsl:with-param name="errorCodeValidate" select="'3114'"/>
								<xsl:with-param name="node" select="$codigoCargoDescuento"/>
								<xsl:with-param name="regexp" select="'^(00|01)$'" />
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', $numeroItem, ' ', $codigoCargoDescuento, ' ', $indicadorCargoDescuento)"/>
							</xsl:call-template>
						</xsl:when>						
					</xsl:choose>					
										
					<!-- 34. Código de cargo/descuento -->	
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
						<xsl:with-param name="regexp" select="'^(00|01|47|48)$'" />
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

					<!-- 34. Monto de cargo/descuento -->
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2955'"/>
						<xsl:with-param name="node" select="$montoCargoDescuento"/>
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $numeroItem, ' ', $montoCargoDescuento)"/>
					</xsl:call-template>	

					<xsl:if test="($montoCargoDescuento_currencyID)">				
						<xsl:if test="not($montoCargoDescuento_currencyID = $monedaImporteTotalProcesadoPeriodo)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2337'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', $numeroItem, ' ', $montoCargoDescuento_currencyID, ' ', $monedaImporteTotalProcesadoPeriodo)"/>
							</xsl:call-template>
						</xsl:if>			
					</xsl:if>						
				</xsl:for-each>				
			</xsl:if>
						
			<xsl:if test="($indicadorTipoComisionLinea = '2')">		
				<!-- 35. Total IGV -->			
				<xsl:if test="($countTotalIGVLinea = 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3195'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $countTotalIGVLinea)"/>
					</xsl:call-template>
				</xsl:if>	
						
				<xsl:if test="($countTotalIGVLinea &gt; 1)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3026'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $countTotalIGVLinea)"/>
					</xsl:call-template>
				</xsl:if>				
			</xsl:if>			
						
			<xsl:for-each select="./cac:TaxTotal">
				<!-- 21. Total IGV -->	
				<!-- 35. Total IGV -->
				<xsl:variable name="totalIGV" select="./cbc:TaxAmount"/>
				<xsl:variable name="totalIGV_currencyID" select="./cbc:TaxAmount/@currencyID"/>
				
				<!-- 21. Total IGV -->	
				<!-- 35. Total IGV -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'3021'"/>
					<xsl:with-param name="node" select="$totalIGV"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $numeroItem, ' ', $totalIGV)"/>
				</xsl:call-template>	
				
				<xsl:if test="($indicadorTipoComisionLinea = '2')">		
					<xsl:if test="($totalIGV_currencyID)">				
						<xsl:if test="not($totalIGV_currencyID = $monedaImporteTotalProcesadoPeriodo)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2337'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', $numeroItem, ' ', $totalIGV_currencyID, ' ', $monedaImporteTotalProcesadoPeriodo)"/>
							</xsl:call-template>
						</xsl:if>			
					</xsl:if>
				</xsl:if>
													
				<xsl:for-each select="./cac:TaxSubtotal">		
					<!-- 35. Base imponible de IGV -->	
					<xsl:variable name="baseImponibleISubTotalGV" select="./cbc:TaxableAmount"/>
					<xsl:variable name="baseImponibleISubTotalGV_currencyID" select="./cbc:TaxableAmount/@currencyID"/>
					<!-- 21. Sub Total IGV -->
					<!-- 35. Sub Total IGV -->	
					<xsl:variable name="subTotalIGV" select="./cbc:TaxAmount"/>
					<xsl:variable name="subTotalIGV_currencyID" select="./cbc:TaxAmount/@currencyID"/>
					
					<xsl:if test="($indicadorTipoComisionLinea = '2')">
						<!-- 35. Base imponible de IGV -->	
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'3031'"/>
							<xsl:with-param name="node" select="$baseImponibleISubTotalGV"/>
							<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $numeroItem, ' ', $baseImponibleISubTotalGV)"/>
						</xsl:call-template>	
		
						<xsl:if test="($baseImponibleISubTotalGV_currencyID)">				
							<xsl:if test="not($baseImponibleISubTotalGV_currencyID = $monedaImporteTotalProcesadoPeriodo)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'2337'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', $numeroItem, ' ', $baseImponibleISubTotalGV_currencyID, ' ', $monedaImporteTotalProcesadoPeriodo)"/>
								</xsl:call-template>
							</xsl:if>			
						</xsl:if>					
					</xsl:if>
					
					<!-- 21. Total IGV -->	
					<!-- 35. Sub Total IGV -->					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2033'"/>
						<xsl:with-param name="node" select="$subTotalIGV"/>
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $numeroItem, ' ', $subTotalIGV)"/>
					</xsl:call-template>	
					
					<xsl:if test="($indicadorTipoComisionLinea = '1')">
					   	<xsl:variable name="dif_subTotalIGV" select="$subTotalIGV - $sumMontoIGV1000Linea" />
						<xsl:if test="($dif_subTotalIGV &lt; -1) or ($dif_subTotalIGV &gt; 1)">
							<xsl:call-template name="addWarning">
								<xsl:with-param name="warningCode" select="'4360'" />
								<xsl:with-param name="warningMessage"
									select="concat('Error en la linea: ', $numeroItem, ' ', $dif_subTotalIGV, ' ', $subTotalIGV, ' ', $sumMontoIGV1000Linea)"/>
							</xsl:call-template>
						</xsl:if>									
					</xsl:if>
					
					<xsl:if test="($indicadorTipoComisionLinea = '2')">
						<xsl:variable name="calculo_subTotalIGV" select="$baseImponibleISubTotalGV * 0.18" />
					   	<xsl:variable name="dif_subTotalIGV" select="$subTotalIGV - $calculo_subTotalIGV" />
						<xsl:if test="($dif_subTotalIGV &lt; -1) or ($dif_subTotalIGV &gt; 1)">
							<xsl:call-template name="addWarning">
								<xsl:with-param name="warningCode" select="'4360'" />
								<xsl:with-param name="warningMessage"
									select="concat('Error en la linea: ', $numeroItem, ' ', $dif_subTotalIGV, ' ', $subTotalIGV, ' ', $calculo_subTotalIGV, ' ', $baseImponibleISubTotalGV)"/>
							</xsl:call-template>
						</xsl:if>										
					</xsl:if>					
					
					<xsl:if test="($subTotalIGV_currencyID)">				
						<xsl:if test="not($subTotalIGV_currencyID = $monedaImporteTotalProcesadoPeriodo)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2337'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', $numeroItem, ' ', $subTotalIGV_currencyID, ' ', $monedaImporteTotalProcesadoPeriodo)"/>
							</xsl:call-template>
						</xsl:if>			
					</xsl:if>		
										
					<xsl:for-each select="./cac:TaxCategory">
						<xsl:for-each select="./cac:TaxScheme">
							<!-- 21. Código de tributo IGV -->		
							<xsl:variable name="codigoTributoLinea" select="./cbc:ID"/>	
							<xsl:variable name="codigoTributoLinea_schemeName" select="./cbc:ID/@schemeName"/>
							<xsl:variable name="codigoTributoLinea_schemeAgencyName" select="./cbc:ID/@schemeAgencyName"/>
							<xsl:variable name="codigoTributoLinea_schemeURI" select="./cbc:ID/@schemeURI"/>								
							
							<!-- 21. Código de tributo IGV -->	
							<xsl:call-template name="existValidateElementNotExist">
								<xsl:with-param name="errorCodeNotExist" select="'2037'"/>
								<xsl:with-param name="node" select="$codigoTributoLinea" />
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', $numeroItem, ' ', $codigoTributoLinea)"/>
							</xsl:call-template>	
							
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'2036'"/>
								<xsl:with-param name="node" select="$codigoTributoLinea"/>
								<xsl:with-param name="regexp" select="'^(1000)$'" />
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', $numeroItem, ' ', $codigoTributoLinea)"/>
							</xsl:call-template>														
																					
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4255'" />
								<xsl:with-param name="node" select="$codigoTributoLinea_schemeName" />
								<xsl:with-param name="regexp" select="'^(Codigo de tributos)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: 1) ', $codigoTributoLinea_schemeName)"/>
							</xsl:call-template>		
						
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4256'" />
								<xsl:with-param name="node" select="$codigoTributoLinea_schemeAgencyName" />
								<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: 1) ', $codigoTributoLinea_schemeAgencyName)"/>
							</xsl:call-template>
					
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'4257'" />
								<xsl:with-param name="node" select="$codigoTributoLinea_schemeURI" />
								<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo05)$'" />
								<xsl:with-param name="isError" select="false()"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: 1) ', $codigoTributoLinea_schemeURI)"/>
							</xsl:call-template>								
						</xsl:for-each>		
					</xsl:for-each>	
				</xsl:for-each>	
			</xsl:for-each>	
			
			<xsl:if test="($countImporteTotalLinea = 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2480'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $numeroItem, ' ', $countImporteTotalLinea)"/>
				</xsl:call-template>
			</xsl:if>
						
			<xsl:for-each select="./cac:ItemPriceExtension">
				<!-- 22. Importe total -->	
				<xsl:variable name="importeTotal" select="./cbc:Amount"/>
				<xsl:variable name="importeTotal_currencyID" select="./cbc:Amount/@currencyID"/>			
			
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2481'"/>
					<xsl:with-param name="node" select="$importeTotal"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', $numeroItem, ' ', $importeTotal)"/>
				</xsl:call-template>				

				<!-- ////  -->
				<!-- 
				<xsl:if test="($indicadorTipoComisionLinea = '1')">	
					<xsl:variable name="totalImporteCalculado" select="$sumTotalComisionesLinea + $sumTotalTaxLinea" />							
				   	<xsl:variable name="dif_TotalImporte_1" select="$importeTotal - $totalImporteCalculado" />
					<xsl:variable name="dif_TotalImporte">	
						<xsl:value-of select='format-number($dif_TotalImporte_1, "#.##")' />       
					</xsl:variable>	
					<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="'4348'" />
							<xsl:with-param name="warningMessage"
								select="concat('Error en la linea: (1) ', $numeroItem, ') ', $dif_TotalImporte, ' | ', $importeTotal, ' | ', $totalImporteCalculado, ' | ', $sumTotalComisionesLinea, ' | ', $sumTotalTaxLinea)"/>
						</xsl:call-template>
					</xsl:if>				
				</xsl:if>				
			
				<xsl:if test="($indicadorTipoComisionLinea = '2')">
					<xsl:variable name="totalImporteCalculado" select="($sumTotalComisionesLinea + $sumTotalTaxLinea + $sumTotalIGVCargoLinea)- $sumTotalIGVDescuentoLinea" />
				   	<xsl:variable name="dif_TotalImporte_1" select="$importeTotal - $totalImporteCalculado" />
					<xsl:variable name="dif_TotalImporte">	
						<xsl:value-of select='format-number($dif_TotalImporte_1, "#.##")' />       
					</xsl:variable>		
					<xsl:if test="($dif_TotalImporte &lt; -1) or ($dif_TotalImporte &gt; 1)">
						<xsl:call-template name="addWarning">
							<xsl:with-param name="warningCode" select="'4348'" />
							<xsl:with-param name="warningMessage"
								select="concat('Error en la linea: (2) ', $numeroItem, ') ', $dif_TotalImporte, ' | ', $importeTotal, ' | ', $totalImporteCalculado, ' | ', $sumTotalComisionesLinea, ' | ', $sumTotalTaxLinea, ' | ', $sumTotalIGVCargoLinea, ' | ', $sumTotalIGVDescuentoLinea)"/>
						</xsl:call-template>
					</xsl:if>				
				</xsl:if>
				-->
				
				<xsl:if test="($importeTotal_currencyID)">				
					<xsl:if test="not($importeTotal_currencyID = $monedaImporteTotalProcesadoPeriodo)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2337'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', $numeroItem, ' ', $importeTotal_currencyID, ' ', $monedaImporteTotalProcesadoPeriodo)"/>
						</xsl:call-template>
					</xsl:if>			
				</xsl:if>							
			</xsl:for-each>		
									
			<xsl:if test="($indicadorTipoComisionLinea = '1')">
				<xsl:if test="($indicadorInstituciónFinancieraLinea = '1') and not($countNumeroOrdenLinea &gt; 0)">	
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4364'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', $numeroItem, ' ', $indicadorInstituciónFinancieraLinea, ' ', $countNumeroOrdenLinea)"/>
					</xsl:call-template>						
				</xsl:if>	
												
				<!-- Entidades financieras  - Detalle por banco emisor -->				
				<xsl:for-each select="./cac:SubInvoiceLine">	
					<!-- 23. Número de orden  -->		
					<xsl:variable name="numeroOrden" select="./cbc:ID"/>	
					<!-- 26. Comisión del banco emisor -->
					<xsl:variable name="comisionBancoEmisor" select="./cbc:LineExtensionAmount"/>	
					<!-- 27. Tipo de moneda de la comisión -->	
					<xsl:variable name="comisionBancoEmisor_currencyID" select="./cbc:LineExtensionAmount/@currencyID"/>
					<!-- Datos de la Linea -->
					<!-- 26. Suma Total comisiones por línea Sub-->
					<xsl:variable name="sumTotalComisionesLineaSub" select="sum(./cbc:LineExtensionAmount)"/>										
					<!-- 27. Suma Total IGV por línea Sub -->
					<xsl:variable name="sumTotalIGVLineaSub" select="sum(./cac:TaxTotal/cac:TaxSubTotal/cbc:TaxAmount)"/>										
					
					<!-- 28. Contar Código de tributo por línea Sub-->					
					<xsl:variable name="countCodigoTributoLinea1000Sub" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])" />
					<!-- 29. Contar Importe total por línea Sub -->					
					<xsl:variable name="countImporteTotalLineaSub" select="count(./cac:ItemPriceExtension)" />	
							
					<!-- 23. Número de orden -->
					<xsl:choose>
						<xsl:when test="not($numeroOrden)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2023'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', $numeroItem, ' ', $numeroOrden)" />
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:if
								test='not(fn:matches($numeroOrden,"^[0-9]{1,3}?$")) or $numeroOrden &lt;= 0'>
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'2023'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', $numeroItem, ' ', $numeroOrden)" />
								</xsl:call-template>
							</xsl:if>
						</xsl:otherwise>
					</xsl:choose>	
	
					<xsl:if test="count(key('by-subInvoiceLine-id', concat($numeroItem, '-',$numeroOrden))) &gt; 1">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2752'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', $numeroItem, ' ', $numeroOrden, ' ',count(key('by-subInvoiceLine-id', concat($numeroItem, '-',$numeroOrden))))" />
						</xsl:call-template>
					</xsl:if>
								
					<xsl:variable name="countRucBancoEmisorTarjeta" select="count(./cac:OriginatorParty/cac:PartyIdentification/cbc:ID)"/>
					<xsl:if test="(($indicadorInstituciónFinancieraLinea = '1') and ($countRucBancoEmisorTarjeta = 0))">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2484'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea : ', $numeroItem, ' ', $indicadorInstituciónFinancieraLinea, ' ', $countRucBancoEmisorTarjeta)"/>
						</xsl:call-template>
					</xsl:if>								
								
					<xsl:for-each select="./cac:OriginatorParty">	
						<xsl:for-each select="./cac:PartyIdentification">	
							<!-- 24. RUC Banco emisor de la tarjeta -->
							<xsl:variable name="rucBancoEmisorTarjeta" select="./cbc:ID" />
							<xsl:variable name="tipoDocumentoBancoEmisorTarjeta" select="./cbc:ID/@schemeID" />	
							
							<!-- 24. RUC Banco emisor de la tarjeta -->
							<xsl:if test="($indicadorInstituciónFinancieraLinea = '1')">	
								<xsl:call-template name="existValidateElementNotExist">
									<xsl:with-param name="errorCodeNotExist" select="'2484'"/>
									<xsl:with-param name="node" select="$rucBancoEmisorTarjeta"/>			
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', $numeroItem, ' ',  $rucBancoEmisorTarjeta)"/>
								</xsl:call-template>	
								
								<xsl:call-template name="existValidateElementNotExist">
									<xsl:with-param name="errorCodeNotExist" select="'2516'"/>
									<xsl:with-param name="node" select="$tipoDocumentoBancoEmisorTarjeta"/>			
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', $numeroItem, ' ',  $tipoDocumentoBancoEmisorTarjeta)"/>
								</xsl:call-template>	
										
								<xsl:call-template name="regexpValidateElementIfExist">
									<xsl:with-param name="errorCodeValidate" select="'2485'"/>
									<xsl:with-param name="node" select="$tipoDocumentoBancoEmisorTarjeta"/>
									<xsl:with-param name="regexp" select="'^(6)$'"/>
									<xsl:with-param name="descripcion" 
										select="concat('Error en la linea: ', $numeroItem, ' ', $tipoDocumentoBancoEmisorTarjeta)"/>
								</xsl:call-template>																	
							</xsl:if>																			
						</xsl:for-each>																			
					</xsl:for-each>		
					
					<xsl:for-each select="./cac:Item">	    			
						<!-- 25. Nombre Banco emisor de la tarjeta -->
						<xsl:variable name="nombreBancoEmisorTarjeta" select="./cbc:Description"/>
						
						<!-- 25. Nombre Banco emisor de la tarjeta -->		
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'4350'"/>
							<xsl:with-param name="node" select="$nombreBancoEmisorTarjeta"/>
							<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,1499}$'"/>
							<xsl:with-param name="isError" select="false()"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $numeroItem, ' ', $nombreBancoEmisorTarjeta)"/>
						</xsl:call-template>														
					</xsl:for-each>	
						
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2486'"/>
						<xsl:with-param name="node" select="$comisionBancoEmisor"/>
						<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $numeroItem, ' ', $comisionBancoEmisor)"/>
					</xsl:call-template>				
																				
					<xsl:if test="($comisionBancoEmisor_currencyID)">				
						<xsl:if test="not($comisionBancoEmisor_currencyID = $monedaImporteTotalProcesadoPeriodo)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2337'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', $numeroItem, ' ', $comisionBancoEmisor_currencyID, ' ', $monedaImporteTotalProcesadoPeriodo)"/>
							</xsl:call-template>
						</xsl:if>			
					</xsl:if>	
									
					<xsl:if test="($countCodigoTributoLinea1000Sub &gt; 1)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'3067'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', $numeroItem, ' ', $countCodigoTributoLinea1000Sub)"/>
						</xsl:call-template>
					</xsl:if>				
											
					<xsl:for-each select="./cac:TaxTotal">
						<!-- 28. Monto de IGV -->	
						<xsl:variable name="montoIGV" select="./cbc:TaxAmount"/>
						<xsl:variable name="montoIGV_currencyID" select="./cbc:TaxAmount/@currencyID"/>
						
						<!-- 28. Monto IGV -->	
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'2497'"/>
							<xsl:with-param name="node" select="$montoIGV"/>
							<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $numeroItem, ' ', $montoIGV)"/>
						</xsl:call-template>	
						
						<xsl:if test="($montoIGV_currencyID)">				
							<xsl:if test="not($montoIGV_currencyID = $monedaImporteTotalProcesadoPeriodo)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'2337'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', $numeroItem, ' ', $montoIGV_currencyID, ' ', $monedaImporteTotalProcesadoPeriodo)"/>
								</xsl:call-template>
							</xsl:if>			
						</xsl:if>				
																
						<xsl:for-each select="./cac:TaxSubtotal">		
							<!-- 28. Base imponible de IGV -->	
							<xsl:variable name="baseImponibleIGV" select="./cbc:TaxableAmount"/>
							<xsl:variable name="baseImponibleIGV_currencyID" select="./cbc:TaxableAmount/@currencyID"/>
							<!-- 28. Sub Total Monto de IGV -->
							<xsl:variable name="subTotalMontoIGV" select="./cbc:TaxAmount"/>
							<xsl:variable name="subTotalMontoIGV_currencyID" select="./cbc:TaxAmount/@currencyID"/>
							
							<!-- 28. Base imponible de IGV -->
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'2590'"/>
								<xsl:with-param name="node" select="$baseImponibleIGV"/>
								<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', $numeroItem, ' ', $baseImponibleIGV)"/>
							</xsl:call-template>	
							
							<xsl:if test="($baseImponibleIGV_currencyID)">				
								<xsl:if test="not($baseImponibleIGV_currencyID = $monedaImporteTotalProcesadoPeriodo)">
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'2337'" />
										<xsl:with-param name="errorMessage"
											select="concat('Error en la linea: ', $numeroItem, ' ', $baseImponibleIGV_currencyID, ' ', $monedaImporteTotalProcesadoPeriodo)"/>
									</xsl:call-template>
								</xsl:if>			
							</xsl:if>		
										
							<!-- 28. Sub Total Monto de IGV -->						
							<xsl:call-template name="regexpValidateElementIfExist">
								<xsl:with-param name="errorCodeValidate" select="'2033'"/>
								<xsl:with-param name="node" select="$subTotalMontoIGV"/>
								<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
								<xsl:with-param name="descripcion" 
									select="concat('Error en la linea: ', $numeroItem, ' ', $subTotalMontoIGV)"/>
							</xsl:call-template>	
							
							<!-- ////  -->
							<!-- 
							<xsl:variable name="calculo_subTotalMontoIGV" select="$baseImponibleIGV * 0.18" />
						   	<xsl:variable name="dif_subTotalMontoIGV" select="$subTotalMontoIGV - $calculo_subTotalMontoIGV" />
							<xsl:if test="($dif_subTotalMontoIGV &lt; -1) or ($dif_subTotalMontoIGV &gt; 1)">
								<xsl:call-template name="addWarning">
									<xsl:with-param name="warningCode" select="'4365'" />
									<xsl:with-param name="warningMessage"
										select="concat('Error en la linea: ', $numeroItem, ') ', $dif_subTotalMontoIGV, ' ', $subTotalMontoIGV, ' ', $calculo_subTotalMontoIGV, ' ', $baseImponibleIGV)"/>
								</xsl:call-template>
							</xsl:if>									
							-->
							
							<xsl:if test="($subTotalMontoIGV_currencyID)">				
								<xsl:if test="not($subTotalMontoIGV_currencyID = $monedaImporteTotalProcesadoPeriodo)">
									<xsl:call-template name="rejectCall">
										<xsl:with-param name="errorCode" select="'2337'" />
										<xsl:with-param name="errorMessage"
											select="concat('Error en la linea: ', $numeroItem, ' ', $subTotalMontoIGV_currencyID, ' ', $monedaImporteTotalProcesadoPeriodo)"/>
									</xsl:call-template>
								</xsl:if>			
							</xsl:if>		
												
							<xsl:for-each select="./cac:TaxCategory">
								<xsl:for-each select="./cac:TaxScheme">
									<!-- 28. Código de tributo IGV Sub -->		
									<xsl:variable name="codigoTributoLineaSub" select="./cbc:ID"/>	
									<xsl:variable name="codigoTributoLineaSub_schemeName" select="./cbc:ID/@schemeName"/>
									<xsl:variable name="codigoTributoLineaSub_schemeAgencyName" select="./cbc:ID/@schemeAgencyName"/>
									<xsl:variable name="codigoTributoLineaSub_schemeURI" select="./cbc:ID/@schemeURI"/>								
									
									<!-- 28. Código de tributo IGV Sub -->	
									<xsl:call-template name="existValidateElementNotExist">
										<xsl:with-param name="errorCodeNotExist" select="'2037'"/>
										<xsl:with-param name="node" select="$codigoTributoLineaSub" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', $numeroItem, ' ', $codigoTributoLineaSub)"/>
									</xsl:call-template>	
									
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'2036'"/>
										<xsl:with-param name="node" select="$codigoTributoLineaSub"/>
										<xsl:with-param name="regexp" select="'^(1000)$'" />
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: ', $numeroItem, ' ', $codigoTributoLineaSub)"/>
									</xsl:call-template>														
																							
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'4255'" />
										<xsl:with-param name="node" select="$codigoTributoLineaSub_schemeName" />
										<xsl:with-param name="regexp" select="'^(Codigo de tributos)$'" />
										<xsl:with-param name="isError" select="false()"/>
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: 1) ', $codigoTributoLineaSub_schemeName)"/>
									</xsl:call-template>		
								
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'4256'" />
										<xsl:with-param name="node" select="$codigoTributoLineaSub_schemeAgencyName" />
										<xsl:with-param name="regexp" select="'^(PE:SUNAT)$'" />
										<xsl:with-param name="isError" select="false()"/>
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: 1) ', $codigoTributoLineaSub_schemeAgencyName)"/>
									</xsl:call-template>
							
									<xsl:call-template name="regexpValidateElementIfExist">
										<xsl:with-param name="errorCodeValidate" select="'4257'" />
										<xsl:with-param name="node" select="$codigoTributoLineaSub_schemeURI" />
										<xsl:with-param name="regexp" select="'^(urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo05)$'" />
										<xsl:with-param name="isError" select="false()"/>
										<xsl:with-param name="descripcion" 
											select="concat('Error en la linea: 1) ', $codigoTributoLineaSub_schemeURI)"/>
									</xsl:call-template>								
								</xsl:for-each>		
							</xsl:for-each>	
						</xsl:for-each>	
					</xsl:for-each>							
	
					<xsl:if test="($countImporteTotalLineaSub = 0)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2482'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', $numeroItem, ' ', $countImporteTotalLineaSub)"/>
						</xsl:call-template>
					</xsl:if>
								
					<xsl:for-each select="./cac:ItemPriceExtension">
						<!-- 29. Importe total Entidad Emisor -->	
						<xsl:variable name="importeTotalEntidadEmisor" select="./cbc:Amount"/>
						<xsl:variable name="importeTotalEntidadEmisor_currencyID" select="./cbc:Amount/@currencyID"/>			
					
						<!-- 29. Importe total Entidad Emisor -->
						<xsl:call-template name="regexpValidateElementIfExist">
							<xsl:with-param name="errorCodeValidate" select="'2483'"/>
							<xsl:with-param name="node" select="$importeTotalEntidadEmisor"/>
							<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea: ', $numeroItem, ' ', $importeTotalEntidadEmisor)"/>
						</xsl:call-template>				
					
						<!-- ////  -->
						<!-- 
					   	<xsl:variable name="dif_importeTotalEntidadEmisor" select="$importeTotalEntidadEmisor - ($sumTotalComisionesLineaSub + $sumTotalIGVLineaSub)" />
						<xsl:if test="($dif_importeTotalEntidadEmisor &lt; -1) or ($dif_importeTotalEntidadEmisor &gt; 1)">
							<xsl:call-template name="addWarning">
								<xsl:with-param name="warningCode" select="'4349'" />
								<xsl:with-param name="warningMessage"
									select="concat('Error en la linea: ', $numeroItem, ' ', $dif_importeTotalEntidadEmisor, ' ', $importeTotalEntidadEmisor, ' ', $sumTotalComisionesLineaSub, ' ', $sumTotalIGVLineaSub)"/>
							</xsl:call-template>
						</xsl:if>				
						-->
						<xsl:if test="($importeTotalEntidadEmisor_currencyID)">				
							<xsl:if test="not($importeTotalEntidadEmisor_currencyID = $monedaImporteTotalProcesadoPeriodo)">
								<xsl:call-template name="rejectCall">
									<xsl:with-param name="errorCode" select="'2337'" />
									<xsl:with-param name="errorMessage"
										select="concat('Error en la linea: ', $numeroItem, ' ', $importeTotalEntidadEmisor_currencyID, ' ', $monedaImporteTotalProcesadoPeriodo)"/>
								</xsl:call-template>
							</xsl:if>			
						</xsl:if>							
					</xsl:for-each>																							
				</xsl:for-each>	
			</xsl:if>																
		</xsl:for-each>		
		
		<xsl:for-each select="cac:LegalMonetaryTotal">
			<!-- 37. Importe total procesado en el periodo -->
			<xsl:variable name="importeTotalProcesadoPeriodo" select="./cbc:LineExtensionAmount"/>
			<xsl:variable name="importeTotalProcesadoPeriodo_currencyID" select="./cbc:LineExtensionAmount/@currencyID"/>		
			<!-- 38. Monto abonado en el periodo facturado -->
			<xsl:variable name="montoAbonadoPeriodoFacturado" select="./cbc:PayableAmount"/>		
			<xsl:variable name="montoAbonadoPeriodoFacturado_currencyID" select="./cbc:PayableAmount/@currencyID"/>	
			
			<!-- 37. Importe total procesado en el periodo -->		
			<xsl:call-template name="existValidateElementNotExist">
				<xsl:with-param name="errorCodeNotExist" select="'2487'"/>
				<xsl:with-param name="node" select="$importeTotalProcesadoPeriodo"/>			
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ',  $importeTotalProcesadoPeriodo)"/>
			</xsl:call-template>	
					
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2488'"/>
				<xsl:with-param name="node" select="$importeTotalProcesadoPeriodo"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $importeTotalProcesadoPeriodo)"/>
			</xsl:call-template>		
			
<!--   	<xsl:call-template name="findElementInCatalog"> -->
<!-- 		<xsl:with-param name="errorCodeValidate" select="'3088'"/> -->
<!-- 		<xsl:with-param name="idCatalogo" select="$importeTotalProcesadoPeriodo_currencyID"/> -->
<!-- 		<xsl:with-param name="catalogo" select="'02'"/> -->
<!-- 		<xsl:with-param name="descripcion"  -->
<!-- 					select="concat('Error en la linea: ', position(), ' ', $importeTotalProcesadoPeriodo_currencyID)"/> -->
<!-- 	</xsl:call-template>								 -->
		
			<!-- 38. Monto abonado en el periodo facturado -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2052'"/>
				<xsl:with-param name="node" select="$montoAbonadoPeriodoFacturado"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $montoAbonadoPeriodoFacturado)"/>
			</xsl:call-template>			

		   	<xsl:variable name="dif_montoAbonadoPeriodoFacturado" select="$montoAbonadoPeriodoFacturado - ($sumImporteTotalProcesadoPeriodo - $sumImporteTotal_1 - $sumImporteTotal_2 - $sumImporteTotal_ADQ)" />
			<xsl:if test="($dif_montoAbonadoPeriodoFacturado &lt; -1) or ($dif_montoAbonadoPeriodoFacturado &gt; 1)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4363'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', position(), ' ', $dif_montoAbonadoPeriodoFacturado, ' ', $montoAbonadoPeriodoFacturado, ' ', $sumImporteTotalProcesadoPeriodo, ' ', $sumImporteTotal_1, ' ', $sumImporteTotal_2, ' ', $sumImporteTotal_ADQ)"/>
				</xsl:call-template>
			</xsl:if>	

			<xsl:if test="($montoAbonadoPeriodoFacturado_currencyID)">				
				<xsl:if test="not($montoAbonadoPeriodoFacturado_currencyID = $monedaImporteTotalProcesadoPeriodo)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2337'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $montoAbonadoPeriodoFacturado_currencyID, ' ', $monedaImporteTotalProcesadoPeriodo)"/>
					</xsl:call-template>
				</xsl:if>			
			</xsl:if>			
		</xsl:for-each>																																		
	</xsl:template>	
</xsl:stylesheet>
