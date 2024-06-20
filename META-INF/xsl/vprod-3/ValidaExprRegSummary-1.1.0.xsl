<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" 
	xmlns:gemfunc="http://www.sunat.gob.pe/gem/functions"
	xmlns="urn:sunat:names:specification:ubl:peru:schema:xsd:SummaryDocuments-1"
	xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
	xmlns:ext="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2"
	xmlns:sac="urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1"
	xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
	xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"	
	xmlns:dp="http://www.datapower.com/extensions"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:date="http://exslt.org/dates-and-times"
	extension-element-prefixes="dp" exclude-result-prefixes="dp" >
	
<!-- INICIO: AXTEROID -->
  <xsl:include href="../util/validate_utils.xsl" dp:ignore-multiple="yes" />
<!-- FIN: AXTEROID -->	
	
	<!-- key Numero de lineas duplicados fin -->
	<xsl:key name="by-invoiceLine-id" 
		match="*[local-name()='SummaryDocuments']/sac:SummaryDocumentsLine" use="cbc:LineID" />
	
	<xsl:key name="by-SummaryDocumentsLineIDCompro"
		match="*[local-name()='SummaryDocuments']/sac:SummaryDocumentsLine" use="concat(cbc:DocumentTypeCode,cbc:ID,cac:Status/cbc:ConditionCode)" />

	<xsl:key name="by-SummaryDocumentsLineID"
		match="*[local-name()='SummaryDocuments']/sac:SummaryDocumentsLine" use="concat(cbc:DocumentTypeCode,cbc:ID)" />

		
	<xsl:param name="nombreArchivoEnviado" />
		
	<xsl:template match="/*">
		<!-- Ini validacion del nombre del archivo vs el nombre del cbc:ID -->		
		<xsl:variable name="rucFilename" select="substring($nombreArchivoEnviado,1,11)" />
		<xsl:variable name="idFilename" select="substring($nombreArchivoEnviado, 13, string-length($nombreArchivoEnviado) - 16)" />
		<xsl:variable name="fechaEnvioFile" select="substring($nombreArchivoEnviado, 16, 8)" />			
		<!-- Fin validacion del nombre del archivo vs el nombre del cbc:ID -->
		
		<!-- Variables -->
		<!-- Datos del resumen diario -->
		<!-- 1. Versión del UBL -->
		<xsl:variable name="cbcUBLVersionID" select="cbc:UBLVersionID" />
		<!-- 2. Versión de la estructura del documento -->
		<xsl:variable name="cbcCustomizationID" select="cbc:CustomizationID" />
		<!-- 3. Identificador del resumen -->
		<xsl:variable name="cbcID" select="cbc:ID" />		
		<!-- 4. Fecha de generación del resumen -->
		<xsl:variable name="cbcIssueDate" select="cbc:IssueDate" />	
		<!-- 5. Fecha de emisión de los documentos -->
		<xsl:variable name="cbcReferenceDate" select="cbc:ReferenceDate" />	
		<xsl:variable name="currentdate" select="fn:current-date()"></xsl:variable>	
		<!-- 7. Emisor -->
		<xsl:variable name="emisor" select="./cac:AccountingSupplierParty"/>		
		<!-- 7.1 Número de RUC -->
		<xsl:variable name="emisorNumeroDocumento" select="$emisor/cbc:CustomerAssignedAccountID"/>			
		<!-- 7.1 Tipo documento -->
		<xsl:variable name="emisorTipoDocumento" select="$emisor/cbc:AdditionalAccountID"/>			
		<!-- 7.2. Apellidos y nombres o denominación o razón social -->
		<xsl:variable name="emisorRazonSocial" select="$emisor/cac:Party/cac:PartyLegalEntity/cbc:RegistrationName"/>		

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

		<!-- 2. Version de la Estructura del Documento -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2072'" />
			<xsl:with-param name="errorCodeValidate" select="'2072'" />
			<xsl:with-param name="node" select="$cbcCustomizationID" />
			<xsl:with-param name="regexp" select="'^(1.1)$'" />
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $cbcCustomizationID)"/>
		</xsl:call-template>			
			
		<!-- 3. El ID debe coincidir con el nombre del archivo -->        
        <xsl:if test="$idFilename != $cbcID">
            <xsl:call-template name="rejectCall"> 
            	<xsl:with-param name="errorCode" select="'2220'" /> 
            	<xsl:with-param name="errorMessage" 
            		select="concat('id del xml diferente al id del nombre de archivo ', $cbcID, ' - ', $idFilename)" /> 
            </xsl:call-template>
        </xsl:if>	
		
		<!-- 4. Fecha de generación del resumen -->        
        <xsl:if test="$fechaEnvioFile != translate($cbcIssueDate,'-','')">
            <xsl:call-template name="rejectCall"> 
            	<xsl:with-param name="errorCode" select="'2346'" /> 
            	<xsl:with-param name="errorMessage" 
            		select="concat('fecha emision del xml diferente a la fecha de emision del nombre del archivo ', translate($cbcIssueDate,'-',''), ' diff ', $fechaEnvioFile)" /> 
            </xsl:call-template>
        </xsl:if>
        
		<!-- 4. Fecha de generación del resumen -->
		<xsl:variable name="t1" select="xs:date($currentdate)-xs:date($cbcIssueDate)" />
		<xsl:variable name="t2" select="fn:days-from-duration(xs:duration($t1))" />			
		<xsl:if
			test="($t2 &lt; 0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2236'" />
				<xsl:with-param name="errorMessage" 
					select="concat('Error en la linea: ', $cbcIssueDate, ' - ', $currentdate, ' - ', $t1, ' - ', $t2)" />
			</xsl:call-template>
		</xsl:if>
		
		<!-- 5. Fecha de emisión de los documentos -->
		<xsl:variable name="t3" select="xs:date($cbcIssueDate)-xs:date($cbcReferenceDate)" />
		<xsl:variable name="t4" select="fn:days-from-duration(xs:duration($t3))" />				
		<xsl:if
			test="($t4 &lt; 0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2671'" />
				<xsl:with-param name="errorMessage" 
					select="concat('Error en la linea: ', $cbcReferenceDate, ' - ', $cbcIssueDate)" />
			</xsl:call-template>
		</xsl:if>
		
		<!-- 7.1 Numero de documento de identidad - Mandatorio -->
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'1034'"/>
			<xsl:with-param name="node" select="$emisorNumeroDocumento"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $emisorNumeroDocumento)"/>
		</xsl:call-template>
		
		<xsl:if test="$rucFilename != $emisorNumeroDocumento">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'1034'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', $rucFilename, ' ', $emisorNumeroDocumento)" />
			</xsl:call-template>
		</xsl:if>

		<!-- 7.1 Tipo documento -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2219'"/>
			<xsl:with-param name="errorCodeValidate" select="'2218'"/>
			<xsl:with-param name="node" select="$emisorTipoDocumento"/>
			<xsl:with-param name="regexp" select="'^[6]{1}$'"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $emisorTipoDocumento)"/>
		</xsl:call-template>
		
		 <!-- 7.2  Apellidos y nombres o denominacion o razon social Emisor --> 
    	<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2229'"/>
			<xsl:with-param name="errorCodeValidate" select="'2228'"/>
			<xsl:with-param name="node" select="$emisorRazonSocial"/>
			<xsl:with-param name="regexp" select="'^[^\s].{1,99}$'"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $emisorRazonSocial)"/>
		</xsl:call-template>

		<!-- Linea de documento -->
		<xsl:for-each select="./sac:SummaryDocumentsLine">
			<!-- 8. Número de fila  -->
			<xsl:variable name="numeroLinea" select="./cbc:LineID"/>	
			<!-- 9. Boleta de venta  -->
			<!-- 9.1 Serie y número de correlativo del documento -->
			<xsl:variable name="serieNumeroDocumento" select="./cbc:ID"/>
			<!-- 9.2 Tipo de Comprobante -->
			<xsl:variable name="tipoComprobante" select="./cbc:DocumentTypeCode"/>
			<!-- 10. Adquirente o usuario -->
			<xsl:variable name="adquirenteUsuario" select="./cac:AccountingCustomerParty"/>
			<!-- 10.1 Número de documento de Identidad -->
			<xsl:variable name="numeroDocumentoIdentidad" select="./cac:AccountingCustomerParty/cbc:CustomerAssignedAccountID"/>
			<!-- 10.2 Tipo de documento de Identidad -->
			<xsl:variable name="tipoDocumentoIdentidad" select="./cac:AccountingCustomerParty/cbc:AdditionalAccountID"/>
			<!-- 11. Comprobante de referencia -->
			<xsl:variable name="comprobanteReferencia" select="./cac:BillingReference"/>
			<!-- 11.1 Serie y número de documento de la boleta de venta que modifica -->
			<xsl:variable name="serieNumeroDocumentoModifica" select="./cac:BillingReference/cac:InvoiceDocumentReference/cbc:ID"/>
			<!-- 11.2 Tipo de documento que modifica -->
			<xsl:variable name="tipoDocumentoModifica" select="./cac:BillingReference/cac:InvoiceDocumentReference/cbc:DocumentTypeCode"/>
			<!-- 12. Informacion de percepcion -->
			<xsl:variable name="informacionPercepcion" select="./sac:SUNATPerceptionSummaryDocumentReference"/>			
			<!-- 12.1 Regimen de percepción -->
			<xsl:variable name="regimenPercepcion" select="./sac:SUNATPerceptionSummaryDocumentReference/sac:SUNATPerceptionSystemCode"/>
			<!-- 12.2 Tasa de la percepción -->
			<xsl:variable name="tasaPercepcion" select="./sac:SUNATPerceptionSummaryDocumentReference/sac:SUNATPerceptionPercent"/>
			<!-- 12.3 Monto de la percepción -->
			<xsl:variable name="montoPercepcion" select="./sac:SUNATPerceptionSummaryDocumentReference/cbc:TotalInvoiceAmount"/>
			<!-- 12.3 Moneda del Monto de la percepción -->
			<xsl:variable name="monedaMontoPercepcion" select="./sac:SUNATPerceptionSummaryDocumentReference/cbc:TotalInvoiceAmount/@currencyID"/>
			<!-- 12.4 Monto total a cobrar incluida la percepción -->
			<xsl:variable name="montoTotalCobrarIncluidaPercepcion" select="./sac:SUNATPerceptionSummaryDocumentReference/sac:SUNATTotalCashed"/>
			<!-- 12.4 Moneda del Monto total a cobrar incluida la percepción -->
			<xsl:variable name="monedaMontoTotalCobrarIncluidaPercepcion" select="./sac:SUNATPerceptionSummaryDocumentReference/sac:SUNATTotalCashed/@currencyID"/>
			<!-- 12.5 Base imponible percepción -->
			<xsl:variable name="baseImponiblePercepcion" select="./sac:SUNATPerceptionSummaryDocumentReference/cbc:TaxableAmount"/>						
			<!-- 13. Código de operación del ítem -->
			<xsl:variable name="codigoOperacionItem" select="./cac:Status/cbc:ConditionCode"/>
			<!-- 14. Importe total de la venta -->
			<xsl:variable name="importeTotalVenta" select="./sac:TotalAmount"/>			
			<!-- 14. Moneda del Importe total de la venta -->
			<xsl:variable name="monedaImporteTotalVenta" select="./sac:TotalAmount/@currencyID"/>

			<xsl:variable name="numeroComprobanteLinea" select="substring($serieNumeroDocumento, 6, string-length($serieNumeroDocumento))" />

			<!-- 9. Número de fila  -->		
						
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2238'"/>
				<xsl:with-param name="node" select="$numeroLinea"/>
				<xsl:with-param name="regexp" select="'^[0-9].{0,4}$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position())"/>
			</xsl:call-template>	
										
			<xsl:if test="$numeroLinea &lt; 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2239'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position())" />
				</xsl:call-template>
			</xsl:if>		
			
			<xsl:if test="count(key('by-invoiceLine-id', $numeroLinea)) > 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2752'" />
					<xsl:with-param name="errorMessage"
						select="concat('El numero de item esta duplicado: ', position())" />
				</xsl:call-template>
			</xsl:if>
			
			<!-- 9.1 Serie y número de correlativo del documento -->
			<xsl:call-template name="existValidateElementIfExist">
				<xsl:with-param name="errorCodeNotExist" select="'2512'"/>
				<xsl:with-param name="node" select="$serieNumeroDocumento"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea 1: ', position(), ' ', $serieNumeroDocumento)"/>	
			</xsl:call-template>
						
			<xsl:if test="$tipoComprobante='03' or $tipoComprobante='07' or $tipoComprobante='08'">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2513'"/>
					<xsl:with-param name="node" select="$serieNumeroDocumento"/>
<!-- 					<xsl:with-param name="regexp" select="'^[B]{1}[A-Z0-9]{1,3}-[0-9]{1,8}$'"/> -->
					<xsl:with-param name="regexp" select="'^[B]{1}[A-Z0-9]{1,3}-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $serieNumeroDocumento)"/>
				</xsl:call-template>
				
				<xsl:if test="number($numeroComprobanteLinea)=0">			
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2513'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ',  $serieNumeroDocumento, ' ', $numeroComprobanteLinea)" />
					</xsl:call-template>
				</xsl:if>	
			</xsl:if>
			
			<!-- 9.2 Tipo de Comprobante -->
			<xsl:call-template name="existValidateElementIfExist">
				<xsl:with-param name="errorCodeNotExist" select="'2242'"/>
				<xsl:with-param name="node" select="$tipoComprobante"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $tipoComprobante)"/>	
			</xsl:call-template>
			
			<xsl:if
				test="not($tipoComprobante='03' or $tipoComprobante='07' or $tipoComprobante='08')">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2241'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ',  $tipoComprobante)" />
				</xsl:call-template>
			</xsl:if>
			
			<xsl:variable name="countIDCompro" select="count(key('by-SummaryDocumentsLineIDCompro', concat($tipoComprobante, $serieNumeroDocumento, $codigoOperacionItem)))"/>
			<xsl:if test="($countIDCompro &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3094'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea ', position(), ' ', $countIDCompro,' ', $tipoComprobante, ' ', $serieNumeroDocumento, ' ', $codigoOperacionItem)" />
				</xsl:call-template>					
			</xsl:if>
			
			<xsl:variable name="countID" select="count(key('by-SummaryDocumentsLineID', concat($tipoComprobante, $serieNumeroDocumento)))"/>
			<xsl:if test="($countID &gt; 1)">
				<xsl:variable name="countIDComproAdic" select="count(key('by-SummaryDocumentsLineIDCompro', concat($tipoComprobante, $serieNumeroDocumento, '1')))"/>
				<xsl:variable name="countIDComproModi" select="count(key('by-SummaryDocumentsLineIDCompro', concat($tipoComprobante, $serieNumeroDocumento, '2')))"/>
				<xsl:variable name="countIDComproAnul_1" select="count(key('by-SummaryDocumentsLineIDCompro', concat($tipoComprobante, $serieNumeroDocumento, '3')))"/>
				<xsl:variable name="countIDComproAnul_2" select="count(key('by-SummaryDocumentsLineIDCompro', concat($tipoComprobante, $serieNumeroDocumento, '4')))"/>
				<xsl:if test="($countIDComproAdic &gt; 0) and ($countIDComproModi &gt; 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3095'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea ', position(), ' ', $countID,' ', $countIDComproAdic, ' ', $countIDComproModi)" />
					</xsl:call-template>		
				</xsl:if>	
				
				<xsl:if test="(($countIDComproAnul_1 &gt; 0) or ($countIDComproAnul_2 &gt; 0)) and ($countIDComproModi &gt; 0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'3096'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea ', position(), ' ', $countID,' ', $countIDComproAnul_1, ' ', $countIDComproAnul_2, ' ', $countIDComproModi)" />
					</xsl:call-template>		
				</xsl:if>	
									
			</xsl:if>
			
			<!-- 14. Importe total de la venta -->
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2251'"/>
				<xsl:with-param name="errorCodeValidate" select="'2251'"/>
				<xsl:with-param name="node" select="$importeTotalVenta"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $importeTotalVenta)"/>
			</xsl:call-template>
			
			<xsl:if test="$importeTotalVenta &lt; 0">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2251'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $importeTotalVenta)"/>
				</xsl:call-template>
			</xsl:if>

			<!-- 10. Adquirente o usuario -->				
			<xsl:if test="$importeTotalVenta &gt; 700">				
				<xsl:call-template name="existValidateElementIfExist">
					<xsl:with-param name="errorCodeNotExist" select="'2514'"/>
					<xsl:with-param name="node" select="$adquirenteUsuario"/>
					<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $adquirenteUsuario)"/>	
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if test="$adquirenteUsuario">	
				<xsl:call-template name="existValidateElementIfNoExistCount">
					<xsl:with-param name="errorCodeNotExist" select="'2014'"/>
					<xsl:with-param name="node" select="$numeroDocumentoIdentidad"/>
					<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $numeroDocumentoIdentidad)"/>	
				</xsl:call-template>								
				
				<!-- 10.1 Número de documento de Identidad -->
				<xsl:if test="(($tipoDocumentoIdentidad = '4') or ($tipoDocumentoIdentidad = '7') or 
					($tipoDocumentoIdentidad = '0') or ($tipoDocumentoIdentidad = 'A') or 
					($tipoDocumentoIdentidad = 'B') or ($tipoDocumentoIdentidad = 'C') or 
					($tipoDocumentoIdentidad = 'D') or ($tipoDocumentoIdentidad = 'E') or 
					($tipoDocumentoIdentidad = 'F') or ($tipoDocumentoIdentidad = 'G'))">							
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4208'"/>
						<xsl:with-param name="node" select="$numeroDocumentoIdentidad"/>
	<!-- 					<xsl:with-param name="regexp" select="'^[\d\w\-]{1,15}$'"/> -->
						<xsl:with-param name="regexp" select="'^[\d\w]{1,15}$'"/>
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ',position(), ' ', $numeroDocumentoIdentidad, ' ', $tipoDocumentoIdentidad)"/>
					</xsl:call-template>	
				</xsl:if>				
				
				<xsl:call-template name="existValidateElementIfNoExistCount">
					<xsl:with-param name="errorCodeNotExist" select="'2015'"/>
					<xsl:with-param name="node" select="$tipoDocumentoIdentidad"/>
					<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: a ', position(), ' ', $tipoDocumentoIdentidad)"/>	
				</xsl:call-template>
				
				<!-- 10.2 Tipo de documento de Identidad del adquirente o usuario -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2016'"/>
					<xsl:with-param name="node" select="$tipoDocumentoIdentidad"/>
					<xsl:with-param name="regexp" select="'^(0|1|4|6|7|A|B|C|D|E|F|G|-)$'" />
					<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $tipoDocumentoIdentidad)"/>	
				</xsl:call-template>
				
				<xsl:variable name="stringTipoDI" select="fn:string-length($tipoDocumentoIdentidad)" />
				<xsl:if test="($stringTipoDI=0)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2015'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: b ', position(), ' ',$tipoDocumentoIdentidad, ' ',$stringTipoDI)" />
					</xsl:call-template>
				</xsl:if>	

				<!-- 10.1 Número de documento de Identidad -->
				<xsl:if test="($tipoDocumentoIdentidad='6')">
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2017'"/>
						<xsl:with-param name="node" select="$numeroDocumentoIdentidad"/>
						<xsl:with-param name="regexp" select="'^[\d]{11}$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $numeroDocumentoIdentidad)"/>
					</xsl:call-template>				
				</xsl:if>
				
				<xsl:if test="($tipoDocumentoIdentidad='1')">
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'4207'"/>
						<xsl:with-param name="node" select="$numeroDocumentoIdentidad"/>
						<xsl:with-param name="regexp" select="'^[\d]{8}$'"/>
						<xsl:with-param name="isError" select="false()"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $numeroDocumentoIdentidad)"/>
					</xsl:call-template>				
				</xsl:if>									
			</xsl:if>
			
			<xsl:if test="(($tipoComprobante='07') or ($tipoComprobante='08')) and not($codigoOperacionItem='3')">
				<!-- 11.1 Serie y número de documento de la boleta de venta que modifica -->
				<xsl:call-template name="existValidateElementIfExist">
					<xsl:with-param name="errorCodeNotExist" select="'2524'"/>
					<xsl:with-param name="node" select="$serieNumeroDocumentoModifica"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ',$tipoComprobante, ' ', $serieNumeroDocumentoModifica)"/>	
				</xsl:call-template>			
			</xsl:if>			
			
			<!-- 11. Comprobante de referencia -->
			<xsl:if test="($comprobanteReferencia)">
				<xsl:if test="not(($tipoComprobante='07') or ($tipoComprobante='08'))">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2582'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea 2: ', position(), ' ',$tipoComprobante)" />
					</xsl:call-template>
				</xsl:if>			
			
				<xsl:if test="($tipoComprobante = '07') or ($tipoComprobante = '08')">
					<!-- 11.2. Tipo de documento que modifica -->
					<xsl:if test="not($codigoOperacionItem = '3')">
						<xsl:call-template name="existValidateElementIfExist">
							<xsl:with-param name="errorCodeNotExist" select="'2583'"/>
							<xsl:with-param name="node" select="$tipoDocumentoModifica"/>
							<xsl:with-param name="descripcion" 
								select="concat('Error en la linea 3: ', position(), ' ', $tipoDocumentoModifica, ' ', $codigoOperacionItem)"/>	
						</xsl:call-template>
						
						<xsl:if test="not(($tipoDocumentoModifica = '03') or ($tipoDocumentoModifica = '12') or 
								($tipoDocumentoModifica = '16') or ($tipoDocumentoModifica = '55'))">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2513'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: ', position(), ' ',$tipoComprobante, ' - ', $tipoDocumentoModifica)" />
							</xsl:call-template>
						</xsl:if>					
					</xsl:if>								
				</xsl:if>
																		
				<!-- 11.1 Serie y número de documento de la boleta de venta que modifica -->	
				<xsl:if test="((($tipoDocumentoModifica='12') or ($tipoDocumentoModifica='16') or ($tipoDocumentoModifica='55')) 
						and not($codigoOperacionItem='3'))">
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2920'"/>
						<xsl:with-param name="node" select="$serieNumeroDocumentoModifica"/>
						<xsl:with-param name="regexp" select="'^[a-zA-Z0-9-]{1,20}-[a-zA-Z0-9-]{1,20}$'"/>
<!-- 						<xsl:with-param name="regexp" select="'^(?!0+-)^[a-zA-Z0-9-]{1,20}-(?!0+$)([0-9]{1,20})$'"/> -->
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $serieNumeroDocumentoModifica)"/>
					</xsl:call-template>
				</xsl:if>	
					
				<xsl:if test="(($tipoDocumentoModifica='03') and not($codigoOperacionItem='3'))">
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2920'"/>
						<xsl:with-param name="node" select="$serieNumeroDocumentoModifica"/>
<!-- 						<xsl:with-param name="regexp" select="'^([B][A-Z0-9]{3})-(?!0+$)([0-9]{1,8})$|^[0-9]{1,4}-[0-9]{1,8}$'"/> -->
						<xsl:with-param name="regexp" select="'^([B][A-Z0-9]{3})-(?!0+$)([0-9]{1,8})$|^(EB01)-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $serieNumeroDocumentoModifica)"/>
					</xsl:call-template>
				</xsl:if>	
			</xsl:if>
							
			<!-- 12. Informacion de percepcion -->
			<xsl:if test="($informacionPercepcion)">
				<xsl:if test="not($tipoComprobante='03') or ($codigoOperacionItem='2')">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2986'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea ', position(), ' ', $tipoComprobante, ' ', $codigoOperacionItem)" />
					</xsl:call-template>
				</xsl:if>
				
				<!-- 12.1 Regimen de percepción -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2517'"/>
					<xsl:with-param name="node" select="$regimenPercepcion"/>
					<xsl:with-param name="regexp" select="'^(01|02|03)$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $regimenPercepcion)"/>
				</xsl:call-template>
			
				<!-- 12.2 Tasa de la percepción -->	
				<xsl:if test="string($tasaPercepcion)">						
					<xsl:if test="not((($regimenPercepcion='01') and ($tasaPercepcion=2)) or 
						(($regimenPercepcion='02') and ($tasaPercepcion=1)) or (($regimenPercepcion='03') and ($tasaPercepcion=0.5)))">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2891'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea ', position(), ' ', $tasaPercepcion)" />
						</xsl:call-template>
					</xsl:if>
				</xsl:if>
			
				<!-- 12.3 Monto de la percepción -->		
				<xsl:if test="string($montoPercepcion)">				
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2893'"/>
						<xsl:with-param name="node" select="$montoPercepcion"/>
						<xsl:with-param name="regexp" select="'^[\-\+\d]{1}[\d]{0,11}(\.[\d]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $montoPercepcion)"/>
					</xsl:call-template>	
					
					<xsl:if test="$montoPercepcion &lt;= 0">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2893'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ',  $montoPercepcion)" />
						</xsl:call-template>
					</xsl:if>		
					
					<xsl:if test="($monedaImporteTotalVenta='PEN')">
						<xsl:variable name="calculoPago" select="(($baseImponiblePercepcion * $tasaPercepcion)*0.01)"/>
						<xsl:variable name="difRecibidoCobrado" select="$calculoPago - $montoPercepcion" />
						<xsl:if test="($difRecibidoCobrado &lt; -1) or ($difRecibidoCobrado &gt; 1)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2608'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: a ', position(), ' ', $difRecibidoCobrado, ' ', $montoPercepcion, ' ', $calculoPago, ' ',$baseImponiblePercepcion, ' ',$tasaPercepcion)" />
							</xsl:call-template>
						</xsl:if>				
					</xsl:if>
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2685'"/>
						<xsl:with-param name="node" select="$monedaMontoPercepcion"/>
						<xsl:with-param name="regexp" select="'^(PEN)$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $monedaMontoPercepcion)"/>
					</xsl:call-template>					
				</xsl:if>
							
				<!-- 12.5 Base imponible percepción -->	
				<xsl:if test="string($baseImponiblePercepcion)">		
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2897'"/>
						<xsl:with-param name="node" select="$baseImponiblePercepcion"/>
						<xsl:with-param name="regexp" select="'^[\-\+\d]{1}[\d]{0,11}(\.[\d]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $baseImponiblePercepcion)"/>
					</xsl:call-template>	
					
					<xsl:if test="$baseImponiblePercepcion &lt;= 0">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2897'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ',  $baseImponiblePercepcion)" />
						</xsl:call-template>
					</xsl:if>						
				</xsl:if>	
									
				<!-- 12.4 Monto total a cobrar incluida la percepción -->	
				<xsl:if test="string($montoTotalCobrarIncluidaPercepcion)">	
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2895'"/>
						<xsl:with-param name="node" select="$montoTotalCobrarIncluidaPercepcion"/>
						<xsl:with-param name="regexp" select="'^[\-\+\d]{1}[\d]{0,11}(\.[\d]{1,2})?$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $montoTotalCobrarIncluidaPercepcion)"/>
					</xsl:call-template>	
					
					<xsl:if test="$montoTotalCobrarIncluidaPercepcion &lt;= 0">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2895'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea: ', position(), ' ',  $montoTotalCobrarIncluidaPercepcion)" />
						</xsl:call-template>
					</xsl:if>	

					<xsl:if test="($monedaImporteTotalVenta='PEN')">
						<xsl:variable name="difMontoTotalPerc" select="($importeTotalVenta + $montoPercepcion)- $montoTotalCobrarIncluidaPercepcion" />	
						<xsl:if test="($difMontoTotalPerc &lt; -1) or ($difMontoTotalPerc &gt; 1)">
							<xsl:call-template name="rejectCall">
								<xsl:with-param name="errorCode" select="'2608'" />
								<xsl:with-param name="errorMessage"
									select="concat('Error en la linea: c ', position(), ' ', $difMontoTotalPerc, ' ',  $importeTotalVenta, ' ', $montoPercepcion, ' ',$montoTotalCobrarIncluidaPercepcion)" />
							</xsl:call-template>
						</xsl:if>
					</xsl:if>
					
					<xsl:call-template name="regexpValidateElementIfExist">
						<xsl:with-param name="errorCodeValidate" select="'2690'"/>
						<xsl:with-param name="node" select="$monedaMontoTotalCobrarIncluidaPercepcion"/>
						<xsl:with-param name="regexp" select="'^(PEN)$'"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', position(), ' ', $monedaMontoTotalCobrarIncluidaPercepcion)"/>
					</xsl:call-template>						
				</xsl:if>			
			</xsl:if>
								
			<!-- 13. Código de operación del ítem -->
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2522'"/>
				<xsl:with-param name="errorCodeValidate" select="'2896'"/>
				<xsl:with-param name="node" select="$codigoOperacionItem"/>
				<xsl:with-param name="regexp" select="'^(1|2|3|4)$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $codigoOperacionItem)"/>				
			</xsl:call-template>
			
			<!-- 14. Importe total de la venta -->			
			<xsl:variable name="sumTVVOGravadas">	
				<xsl:choose>
    				<xsl:when test="count(./sac:BillingPayment[cbc:InstructionID='01']/cbc:PaidAmount)>0">
    					<xsl:value-of select="sum(./sac:BillingPayment[cbc:InstructionID='01']/cbc:PaidAmount)"/></xsl:when>
    					<xsl:otherwise>0</xsl:otherwise>
  				</xsl:choose>       
			</xsl:variable>			
			<xsl:variable name="sumTVVOInafectas" select="sum(./sac:BillingPayment[cbc:InstructionID='03']/cbc:PaidAmount)"/>		
			<xsl:variable name="sumTVVOExoneradas" select="sum(./sac:BillingPayment[cbc:InstructionID='02']/cbc:PaidAmount)"/>	
			<xsl:variable name="sumTVVOExportacion">	
				<xsl:choose>
    				<xsl:when test="count(./sac:BillingPayment[cbc:InstructionID='04']/cbc:PaidAmount)>0">
    					<xsl:value-of select="sum(./sac:BillingPayment[cbc:InstructionID='04']/cbc:PaidAmount)"/></xsl:when>
    					<xsl:otherwise>0</xsl:otherwise>
  				</xsl:choose>       
			</xsl:variable>	
			<xsl:variable name="sumTVVOGratuitas">	
				<xsl:choose>
    				<xsl:when test="count(./sac:BillingPayment[cbc:InstructionID='05']/cbc:PaidAmount)>0">
    					<xsl:value-of select="sum(./sac:BillingPayment[cbc:InstructionID='05']/cbc:PaidAmount)"/></xsl:when>
    					<xsl:otherwise>0</xsl:otherwise>
  				</xsl:choose>       
			</xsl:variable>	
			<xsl:variable name="sumIGV" select="sum(./cac:TaxTotal[cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='1000']/cbc:TaxAmount)"/>		
			<xsl:variable name="sumatoriaISC" select="sum(./cac:TaxTotal[cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='2000']/cbc:TaxAmount)"/>		
			<xsl:variable name="sumatoriaOtrosTributos" select="sum(./cac:TaxTotal[cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='9999']/cbc:TaxAmount)"/>	
			<xsl:variable name="sumImpuestoBolsaPlast" select="sum(./cac:TaxTotal[cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID='7152']/cbc:TaxAmount)"/>			
			<xsl:variable name="sumatoriaOtrosCargos" select="sum(./cac:AllowanceCharge/cbc:Amount)"/>		
			<xsl:variable name="sumImporteTVV" select="$sumTVVOGravadas + $sumTVVOInafectas + $sumTVVOExoneradas + $sumTVVOExportacion" />					
			<xsl:variable name="sumImpuestosTVV" select="$sumIGV + $sumatoriaISC + $sumatoriaOtrosTributos + $sumatoriaOtrosCargos + $sumImpuestoBolsaPlast" />
			<xsl:variable name="sumImporteTotal" select="$sumImporteTVV + $sumImpuestosTVV" />
			<xsl:variable name="difImporteTotalVenta" select="$importeTotalVenta - $sumImporteTotal" />
			<xsl:if test="($difImporteTotalVenta &lt; -5) or ($difImporteTotalVenta &gt; 5)">
				<xsl:call-template name="addWarning">
					<xsl:with-param name="warningCode" select="'4027'" />
					<xsl:with-param name="warningMessage"
						select="concat('Error en la linea: ', position(), '-', $difImporteTotalVenta,'-', $importeTotalVenta,'-', $sumImporteTotal,'-', $sumImporteTVV,'-', $sumImpuestosTVV)"/>
				</xsl:call-template>
			</xsl:if>						
						
			<!-- 15. Operaciones gravadas -->	
			<!-- 16. Operaciones exoneradas -->
			<!-- 17. Operaciones inafectas -->
			<!-- 18. Operaciones Gratuitas -->											
			<xsl:for-each select="./sac:BillingPayment">		
				<!-- 15.1 Total valor de venta -->
				<xsl:variable name="totalValorVenta" select="./cbc:PaidAmount"/>
				<!-- 15.2 Códigos de tipo de valor de venta -->
				<xsl:variable name="codigosTipoValorVenta" select="./cbc:InstructionID"/>
				<!-- 15.1 Moneda del Total valor de venta -->
				<xsl:variable name="monedaTotalValorVenta" select="./cbc:PaidAmount/@currencyID"/>
				
				<xsl:if test="$monedaImporteTotalVenta != $monedaTotalValorVenta">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $monedaImporteTotalVenta, ' ',  $monedaTotalValorVenta)" />
					</xsl:call-template>
				</xsl:if>
				
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2255'"/>
					<xsl:with-param name="errorCodeValidate" select="'2254'"/>
					<xsl:with-param name="node" select="$totalValorVenta"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $totalValorVenta)"/>
				</xsl:call-template>	
									
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2257'"/>
					<xsl:with-param name="errorCodeValidate" select="'2256'"/>
					<xsl:with-param name="node" select="$codigosTipoValorVenta"/>
					<xsl:with-param name="regexp" select="'^(01|02|03|04|05)$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codigosTipoValorVenta)"/>
				</xsl:call-template>													
			</xsl:for-each>

			<xsl:variable name="countCodigosTipoValorVenta01" select="count(./sac:BillingPayment/cbc:InstructionID[text()=01])"/>		
			<xsl:variable name="countCodigosTipoValorVenta02" select="count(./sac:BillingPayment/cbc:InstructionID[text()=02])"/>			
			<xsl:variable name="countCodigosTipoValorVenta03" select="count(./sac:BillingPayment/cbc:InstructionID[text()=03])"/>		
			<xsl:variable name="countCodigosTipoValorVenta04" select="count(./sac:BillingPayment/cbc:InstructionID[text()=04])"/>						
			<xsl:variable name="countCodigosTipoValorVenta05" select="count(./sac:BillingPayment/cbc:InstructionID[text()=05])"/>							
			<xsl:if test="($countCodigosTipoValorVenta01 &gt; 1) or ($countCodigosTipoValorVenta02 &gt; 1) or 
				($countCodigosTipoValorVenta03 &gt; 1) or ($countCodigosTipoValorVenta04 &gt; 1) or 
				($countCodigosTipoValorVenta05 &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2357'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigosTipoValorVenta01)" />
				</xsl:call-template>
 			</xsl:if>
 			
 			<!-- 19. Sumatoria otros cargos del item -->
			<xsl:for-each select="./cac:AllowanceCharge">		
				<!-- 19.1 Indicador de cargo -->				
				<xsl:variable name="indicadorCargo" select="./cbc:ChargeIndicator"/>
				<!-- 19.2 Importe total -->
				<xsl:variable name="importeTotal" select="./cbc:Amount"/>
				
				<xsl:if test="not($indicadorCargo='true')">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2263'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $indicadorCargo)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2261'"/>
					<xsl:with-param name="node" select="$importeTotal"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $importeTotal)"/>
				</xsl:call-template>	
					
				<xsl:if test="$importeTotal &lt;= 0">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'2266'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), ' ',  $importeTotal)" />
					</xsl:call-template>
				</xsl:if>	
			</xsl:for-each>

			<xsl:variable name="countIndicadorCargo" select="count(./cac:AllowanceCharge/cbc:ChargeIndicator[text()='true'])"/>			
			<xsl:if test="($countIndicadorCargo &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2411'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countIndicadorCargo)" />
				</xsl:call-template>
 			</xsl:if>				
			
			<!-- 20. IGV -->
			<!-- 21. ISC -->	
			<!-- 21. OTROS TRIBUTOS -->	
			<xsl:for-each select="./cac:TaxTotal">	
				<!-- 20.1 Total IGV -->
				<xsl:variable name="totalIGV" select="./cbc:TaxAmount"/>
				<!-- 20.1 Moneda del Total IGV -->
				<xsl:variable name="monedaTotalIGV" select="./cbc:TaxAmount/@currencyID"/>
				<!-- 20.1 Sub Total IGV -->
				<xsl:variable name="subTotalIGV" select="./cac:TaxSubtotal/cbc:TaxAmount"/>
				<!-- 20.1 Moneda del Sub Total IGV -->
				<xsl:variable name="monedaSubTotalIGV" select="./cac:TaxSubtotal/cbc:TaxAmount/@currencyID"/>
				<!-- 20.2 Código de tributo -->
				<xsl:variable name="codigoTributo" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID"/>
				<!-- 20.3 Nombre de tributo -->
				<xsl:variable name="nombreTributo" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:Name"/>
				<!-- 20.4 Código internacional de tributo -->
				<xsl:variable name="codigoInternacionalTributo" select="./cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:TaxTypeCode"/>
								
				<!-- 22. Moneda -->	
				<xsl:if test="($monedaImporteTotalVenta != $monedaTotalIGV) or ($monedaImporteTotalVenta != $monedaSubTotalIGV)">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2071'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ',  $monedaTotalIGV, ' ', $monedaSubTotalIGV)" />
					</xsl:call-template>
				</xsl:if>					
						
				<!-- 28. Total IGV -->
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2048'" />
					<xsl:with-param name="errorCodeValidate" select="'2048'"/>
					<xsl:with-param name="node" select="$totalIGV"/>
					<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,2})?$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $totalIGV)"/>
				</xsl:call-template>
				
				<xsl:variable name="sumIGV_Calc" select="(($sumTVVOGravadas + $sumatoriaISC) * 0.18)" />
				<xsl:variable name="difSumIGV" select="$sumIGV - $sumIGV_Calc" />
				<xsl:if test="($difSumIGV &lt; -5) or ($difSumIGV &gt; 5)">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4019'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), '-', $difSumIGV,'-', $sumIGV,'-', $sumIGV_Calc,'-', $sumTVVOGravadas,'-', $sumatoriaISC)"/>
					</xsl:call-template>
				</xsl:if>	
					
				<xsl:call-template name="existValidateElementIfExist">
					<xsl:with-param name="errorCodeNotExist" select="'2344'"/>
					<xsl:with-param name="node" select="$subTotalIGV"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $subTotalIGV)"/>
				</xsl:call-template>
				
				<xsl:if test="(number($totalIGV) != number($subTotalIGV))">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2344'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ',  $totalIGV, ' ',  $subTotalIGV)" />
					</xsl:call-template>
				</xsl:if>	
				
				<!-- 28. Código de tributo -->
				<xsl:call-template name="existValidateElementIfExist">
					<xsl:with-param name="errorCodeNotExist" select="'2269'"/>
					<xsl:with-param name="node" select="$codigoTributo"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codigoTributo)"/>
				</xsl:call-template>
																										
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2268'" />
					<xsl:with-param name="node" select="$codigoTributo" />
					<xsl:with-param name="regexp" select="'^(1000|1016|2000|7152|9995|9996|9997|9998|9999)$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $codigoTributo)"/>
				</xsl:call-template>
				
				<!-- 28. Nombre de tributo -->
				<xsl:call-template name="existValidateElementIfExist">
					<xsl:with-param name="errorCodeNotExist" select="'2271'"/>
					<xsl:with-param name="node" select="$nombreTributo"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $nombreTributo)"/>
				</xsl:call-template>		
				
				<xsl:if test="($codigoTributo = '1000') and ($nombreTributo!='IGV')">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2276'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ',  $nombreTributo)" />
					</xsl:call-template>
				</xsl:if>
						
				<xsl:if test="($codigoTributo = '2000') and ($nombreTributo!='ISC')">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2275'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ',  $nombreTributo)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:for-each>
		    
			<xsl:variable name="countCodigoTributo1000" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1000'])" />
			<xsl:variable name="countCodigoTributo1016" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='1016'])" />
			<xsl:variable name="countCodigoTributo2000" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='2000'])" />
			<xsl:variable name="countCodigoTributo9999" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID[text()='9999'])" />			
			<xsl:variable name="countNombreTributoISC" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:Name[text()='ISC'])" />		
			<xsl:variable name="countNombreTributoIGV" select="count(./cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:TaxTypeCode[text()='VAT'])" />
			<xsl:if 
				test="($countCodigoTributo1000 &gt; 1) or ($countCodigoTributo2000 &gt; 1) or ($countCodigoTributo9999 &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2355'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(),' ', $countCodigoTributo1000, ' ', $countCodigoTributo2000, ' ', $countCodigoTributo9999)" />
				</xsl:call-template>
 			</xsl:if>
 			
 			<xsl:if
				test="(($countCodigoTributo1000 = 0) and ($countCodigoTributo1016 = 0))">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2278'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', position(), ' ', $countCodigoTributo1000, ' ', $countCodigoTributo1016)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>
				
	</xsl:template>
	
</xsl:stylesheet>
