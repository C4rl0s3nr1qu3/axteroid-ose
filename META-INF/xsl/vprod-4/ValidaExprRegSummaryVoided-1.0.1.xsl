<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns="urn:oasis:names:specification:ubl:schema:xsd:Invoice-2"
	xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
	xmlns:ext="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2"
	xmlns:sac="urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1"
	xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
	xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
	xmlns:dp="http://www.datapower.com/extensions" xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:date="http://exslt.org/dates-and-times"
	extension-element-prefixes="dp" exclude-result-prefixes="dp">
	
	<xsl:include href="../util/validate_utils.xsl" dp:ignore-multiple="yes"/>
	
	<!-- key Numero de lineas duplicados fin -->
	<xsl:key name="by-invoiceLine-id"
		match="*[local-name()='VoidedDocuments']/sac:VoidedDocumentsLine" use="cbc:LineID" />

	<xsl:key name="by-invoiceLine-idDocuments"
		match="*[local-name()='VoidedDocuments']/sac:VoidedDocumentsLine" use="concat(cbc:DocumentTypeCode,sac:DocumentSerialID,sac:DocumentNumberID)" />

	<!-- Parameter -->
	<xsl:param name="nombreArchivoEnviado" />

	<xsl:template match="/*">

		<!-- Variables -->
		<xsl:variable name="cbcUBLVersionID" select="./cbc:UBLVersionID" />
		<xsl:variable name="cbcCustomizationID" select="./cbc:CustomizationID" />
		<xsl:variable name="cbcID" select="./cbc:ID" />		
		<xsl:variable name="cbcIssueDate" select="./cbc:IssueDate" />	
		<xsl:variable name="cbcReferenceDate" select="./cbc:ReferenceDate" />
		<xsl:variable name="currentdate" select="fn:current-date()"></xsl:variable>		
		
		<!-- Ini validacion del nombre del archivo vs el nombre del cbc:ID -->	
		<!-- <xsl:variable name="numeroRuc" select="substring($nombreArchivoEnviado, 1, 11)" /> -->
		<xsl:variable name="rucFilename" select="substring($nombreArchivoEnviado, 1, 11)" />
		<xsl:variable name="idFilename" select="substring($nombreArchivoEnviado, 13, string-length($nombreArchivoEnviado) - 16)" />
		<xsl:variable name="fechaEnvioFile" select="substring($nombreArchivoEnviado, 16, 8)" />
		<!-- Fin validacion del nombre del archivo vs el nombre del cbc:ID -->

		<!-- Datos del Emisor Electrónico -->		
		<xsl:variable name="emisor" select="./cac:AccountingSupplierParty"/>		
		<!-- Mandatorio -->
		<xsl:variable name="emisorTipoDocumento" select="$emisor/cbc:AdditionalAccountID"/>		
		<!-- Mandatorio -->
		<xsl:variable name="emisorNumeroDocumento" select="$emisor/cbc:CustomerAssignedAccountID"/>		
		<!-- Opcional -->
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

		<!-- 2.- Version de la Estructura del Documento --> 
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2072'"/>
			<xsl:with-param name="node" select="$cbcCustomizationID"/>
			<xsl:with-param name="regexp" select="'^(1.0)$'"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cbcCustomizationID)"/>
		</xsl:call-template>

		<!-- 3. Identificador de la comunicación -->		
		<xsl:if
			test="substring-before($nombreArchivoEnviado,'.') != concat($rucFilename, '-', $cbcID)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2220'" />
				<xsl:with-param name="errorMessage"
					select="concat('Validation Filename error, name: ', $nombreArchivoEnviado,' = ', rucFilename, '-', $cbcID)" />
			</xsl:call-template>
		</xsl:if>
		
		<!-- 4. La fecha de generación del resumen debe ser igual a la fecha consignada en el nombre del archivo -->
		<xsl:if test="$fechaEnvioFile != translate($cbcIssueDate,'-','')">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2346'" />
				<xsl:with-param name="errorMessage"
					select="concat('fecha emision del xml diferente a la fecha de emision del nombre del archivo ', translate($cbcIssueDate,'-',''), ' diff ', $fechaEnvioFile)" />
			</xsl:call-template>
		</xsl:if>
		
		<xsl:variable name="t1" select="xs:date($currentdate)-xs:date($cbcIssueDate)" />
		<xsl:variable name="t2" select="fn:days-from-duration(xs:duration($t1))" />		
		<xsl:if
			test="($t2 &lt; 0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2301'" />
				<xsl:with-param name="errorMessage" 
					select="concat('Error en la linea: ', $cbcIssueDate, ' - ', $currentdate)" />
			</xsl:call-template>
		</xsl:if>

		<!-- 5. -->
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
		
		<!-- 7.- Tipo de Documento del Emisor - RUC --> 
		<xsl:call-template name="existValidateElementIfNoExistCount">
			<xsl:with-param name="errorCodeNotExist" select="'1034'"/>
			<xsl:with-param name="node" select="$emisorNumeroDocumento"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $emisorNumeroDocumento)"/>
		</xsl:call-template>
		
		<xsl:if
			test="$rucFilename != $emisorNumeroDocumento">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'1034'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $rucFilename, ' - ', $emisorNumeroDocumento)" />
				</xsl:call-template>
		</xsl:if>
		
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2288'"/>
			<xsl:with-param name="errorCodeValidate" select="'2287'"/>
			<xsl:with-param name="node" select="$emisorTipoDocumento"/>
			<xsl:with-param name="regexp" select="'^[6]{1}$'"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $emisorTipoDocumento)"/>
		</xsl:call-template>

		<!-- 8.- Apellidos y nombres o denominacion o razon social Emisor -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2229'"/>
			<xsl:with-param name="errorCodeValidate" select="'2228'"/>
			<xsl:with-param name="node" select="$emisorRazonSocial"/>
<!-- 			<xsl:with-param name="regexp" select="'^[^\s].{1,100}'"/> -->
			<xsl:with-param name="regexp" select="'^[^\s].{1,100}$'"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $emisorRazonSocial)"/>
		</xsl:call-template>

		<!-- Documentos de la Baja -->
		<xsl:for-each select="./sac:VoidedDocumentsLine">
			<!-- 9. Número de Item  -->
			<xsl:variable name="numeroLinea" select="./cbc:LineID"/>			
			<!-- 10. Tipo de documento -->
			<xsl:variable name="tipoDocumento" select="./cbc:DocumentTypeCode"/>			
			<!-- 11.  -->
			<xsl:variable name="serieDocumentoDadoBaja" select="./sac:DocumentSerialID"/>
			<!-- 12.  -->
			<xsl:variable name="numeroDocumentoDadoBaja" select="./sac:DocumentNumberID"/>
			<!-- 13.  -->
			<xsl:variable name="motivoBaja" select="./sac:VoidReasonDescription"/>

			<!-- 9. Número de Item  -->
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2307'"/>
				<xsl:with-param name="errorCodeValidate" select="'2305'"/>
				<xsl:with-param name="node" select="$numeroLinea"/>
<!-- 				<xsl:with-param name="regexp" select="'^[0-9]{1,}?$'"/> -->
<!-- 				<xsl:with-param name="regexp" select="'^[0-9].{5}$'"/> -->
				<xsl:with-param name="regexp" select="'^[0-9]{1,5}$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position())"/>
			</xsl:call-template>
			
			<xsl:if test="$numeroLinea &lt; 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2306'" />
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

			<!-- 10. Tipo de Documento -->			
			<xsl:if test="not(string($tipoDocumento))">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2309'" />
					<xsl:with-param name="errorMessage" 
						select="concat('Error en la linea: ', position(), ' ', $tipoDocumento)"/>
				</xsl:call-template>
			</xsl:if>

			<xsl:if
				test="not($tipoDocumento = '01' or $tipoDocumento = '07' or $tipoDocumento = '08' or 
					$tipoDocumento = '30' or $tipoDocumento = '34' or $tipoDocumento = '42')">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2308'" />
					<xsl:with-param name="errorMessage" 
						select="concat('Error en la linea: ', position(), ' ', $tipoDocumento)"/>
				</xsl:call-template>
			</xsl:if>

			<!-- 11. Serie de los documentos --> 	
			<xsl:call-template name="existValidateElementNotExist">
				<xsl:with-param name="errorCodeNotExist" select="'2311'"/>
				<xsl:with-param name="node" select="$serieDocumentoDadoBaja"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $serieDocumentoDadoBaja)"/>
			</xsl:call-template>
			
<!-- 			<xsl:variable name="s1C" select="substring($serieDocumentoDadoBaja, 1, 1)" /> -->
<!-- 			<xsl:if test="$s1C = 'S' "> -->
<!-- 				<xsl:call-template name="rejectCall"> -->
<!-- 					<xsl:with-param name="errorCode" select="'2581'" /> -->
<!-- 					<xsl:with-param name="errorMessage"  -->
<!-- 						select="concat('Error en la linea: ', position(), ' ', $serieDocumentoDadoBaja)"/> -->
<!-- 				</xsl:call-template> -->
<!-- 			</xsl:if> -->
			
			<xsl:if test="$tipoDocumento = '01' ">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2310'"/>
					<xsl:with-param name="node" select="$serieDocumentoDadoBaja"/>
					<xsl:with-param name="regexp" select="'^[F][A-Z0-9]{3}$|^[0-9]{1,4}$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea 1: ', position(), ' ', $serieDocumentoDadoBaja)"/>
				</xsl:call-template>
			</xsl:if>
					
			<xsl:if test="($tipoDocumento = '30') or ($tipoDocumento = '34') or ($tipoDocumento = '42')">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2310'"/>
					<xsl:with-param name="node" select="$serieDocumentoDadoBaja"/>
					<xsl:with-param name="regexp" select="'^[F][A-Z0-9]{3}$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea 2: ', position(), ' ', $serieDocumentoDadoBaja)"/>
				</xsl:call-template>
			</xsl:if>			
			
			<xsl:if test="($tipoDocumento = '07') or ($tipoDocumento = '08')">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2310'"/>
					<xsl:with-param name="node" select="$serieDocumentoDadoBaja"/>
					<xsl:with-param name="regexp" select="'^[F][A-Z0-9]{3}$|^[0-9]{1,4}$'"/>
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea 3: ', position(), ' ', $serieDocumentoDadoBaja)"/>
				</xsl:call-template>		
			</xsl:if>		
																
			<!--12. Numero correlativo del documento dado de baja -->
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2313'"/>
				<xsl:with-param name="errorCodeValidate" select="'2312'"/>
				<xsl:with-param name="node" select="$numeroDocumentoDadoBaja"/>
				<xsl:with-param name="regexp" select="'^[0-9]{1,8}?$'"/>
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $numeroDocumentoDadoBaja)"/>
			</xsl:call-template>

			<xsl:variable name="countIDDoc" select="count(key('by-invoiceLine-idDocuments', concat($tipoDocumento, $serieDocumentoDadoBaja, $numeroDocumentoDadoBaja)))"/>
			<xsl:if test="($countIDDoc > 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2348'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea ', position(), ' ', $tipoDocumento,' ', $serieDocumentoDadoBaja, ' ', $numeroDocumentoDadoBaja)" />
				</xsl:call-template>					
			</xsl:if>

			<!--13.  --> 
			<xsl:choose>
				<xsl:when test="not(string($motivoBaja))">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2315'" />
						<xsl:with-param name="errorMessage" 
							select="concat('Error en la linea: ',position(), ' ', $motivoBaja)"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="string-length($motivoBaja) &lt; 3">				
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4203'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', position(), ' ', $motivoBaja)"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise></xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>

	</xsl:template>
</xsl:stylesheet>
