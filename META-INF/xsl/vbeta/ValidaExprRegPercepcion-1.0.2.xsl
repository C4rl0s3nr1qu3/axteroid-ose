<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" 
	xmlns="urn:sunat:names:specification:ubl:peru:schema:xsd:Perception-1"
	xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
	xmlns:ext="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2"
	xmlns:sac="urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1"
	xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
	xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
	xmlns:dp="http://www.datapower.com/extensions"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:date="http://exslt.org/dates-and-times"
	extension-element-prefixes="dp" exclude-result-prefixes="dp" >

	<xsl:include href="../util/validate_utils.xsl" dp:ignore-multiple="yes" />

	<xsl:key name="by-document-SUNATPerception-reference"
		match="*[local-name()='Perception']/sac:SUNATPerceptionDocumentReference" use="concat(cbc:ID,' ', cac:Payment/cbc:ID)" />

	<!-- Parameter -->
	<xsl:param name="nombreArchivoEnviado" />

	<xsl:template match="/*">
		<!-- Ini validacion del nombre del archivo vs el nombre del cbc:ID -->		
		<xsl:variable name="rucFilename" select="substring($nombreArchivoEnviado,1,11)" />
		<xsl:variable name="idFilename" select="substring($nombreArchivoEnviado, 13, string-length($nombreArchivoEnviado) - 16)" />
		<xsl:variable name="fechaEnvioFile" select="substring($nombreArchivoEnviado, 16, 8)" />					
		<xsl:variable name="numeroRuc" select="substring($nombreArchivoEnviado, 1, 11)" />	
		<!-- Variables -->
		<!-- 1. Versión del UBL -->
		<xsl:variable name="cbcUBLVersionID" select="cbc:UBLVersionID" />
		<!-- 2. Versión de la estructura del documento -->
		<xsl:variable name="cbcCustomizationID" select="cbc:CustomizationID" />
		<!-- 4. Numeración, conformada por serie y número correlativo -->
		<xsl:variable name="cbcID" select="cbc:ID" />
		<!-- 5. Fecha de emisión -->
		<xsl:variable name="cbcIssueDate" select="cbc:IssueDate" />		
		<xsl:variable name="currentdate" select="fn:current-date()"></xsl:variable>
		<!-- 7. Indicador de emisión excepcional -->
		<xsl:variable name="indicadorEmisiónExcepcional" select="sac:ExceptionalIndicator" />					
		<!-- Datos del Emisor Electrónico -->		
		<xsl:variable name="emisor" select="cac:AgentParty"/>		
		<!-- 8. Tipo de documento de Identidad del emisor -->
		<xsl:variable name="emisorTipoDocumento" select="$emisor/cac:PartyIdentification/cbc:ID/@schemeID"/>		
		<!-- 7. Número de documento de identidad del emisor -->
		<xsl:variable name="emisorNumeroDocumento" select="$emisor/cac:PartyIdentification/cbc:ID"/>		
		<!-- 9. Nombre comercial del emisor -->
		<xsl:variable name="emisorRazonSocial" select="$emisor/cac:PartyName/cbc:Name"/>

		<!-- Domicilio fiscal del Emisor Electrónico -->	
		<!-- 10. Ubigeo -->
		<xsl:variable name="cacAgentPartyPostalAddressID" select="$emisor/cac:PostalAddress/cbc:ID" />
		<!-- 11. Dirección completa y detallada -->
		<xsl:variable name="cacAgentPartyPostalAddressStreetName" select="$emisor/cac:PostalAddress/cbc:StreetName" />
		<!-- 12. Urbanización -->
		<xsl:variable name="cacAgentPartyPostalAddressCitySubdivisionName" select="$emisor/cac:PostalAddress/cbc:CitySubdivisionName" />
		<!-- 13. Provincia -->
		<xsl:variable name="cacAgentPartyPostalAddressCityName" select="$emisor/cac:PostalAddress/cbc:CityName" />
		<!-- 14. Departamento -->
		<xsl:variable name="cacAgentPartyPostalAddressCountrySubentity" select="$emisor/cac:PostalAddress/cbc:CountrySubentity" />
		<!-- 15. Distrito -->
		<xsl:variable name="cacAgentPartyPostalAddressDistrict" select="$emisor/cac:PostalAddress/cbc:District" />
		<!-- 16. Código del país de la dirección -->
		<xsl:variable name="cacAgentPartyLegalEntityName" select="$emisor/cac:PartyLegalEntity/cbc:RegistrationName" />
		<!-- 17. Apellidos y nombres, denominación o razón social -->
		<xsl:variable name="cacAgentPartyCountryCode" select="$emisor/cac:PostalAddress/cac:Country/cbc:IdentificationCode" />

		<!-- Información del Cliente -->
		<xsl:variable name="cacReceiverParty" select="cac:ReceiverParty" />
		<!-- 18. Número de documento de identidad del Cliente -->
<!-- 		<xsl:variable name="cacReceiverPartyIdentificationID" select="$cacReceiverParty/cac:PartyIdentification/cbc:ID" /> -->
		<xsl:variable name="numeroDocumentoIdentidadCliente" select="$cacReceiverParty/cac:PartyIdentification/cbc:ID" />
		<!-- 19. Tipo de documento de Identidad del Cliente -->
<!-- 		<xsl:variable name="cacReceiverPartyIdentificationSchemeID" select="$cacReceiverParty/cac:PartyIdentification/cbc:ID/@schemeID" /> -->
		<xsl:variable name="tipoDocumentoIdentidadCliente" select="$cacReceiverParty/cac:PartyIdentification/cbc:ID/@schemeID" />
		<!-- 20. Nombre comercial del Cliente -->
<!-- 		<xsl:variable name="cacReceiverPartyNameName" select="$cacReceiverParty/cac:PartyName/cbc:Name" /> -->
		<xsl:variable name="nombreComercialCliente" select="$cacReceiverParty/cac:PartyName/cbc:Name" />
		
		<!-- Domicilio fiscal del Cliente -->
		<!-- 21. Ubigeo -->
		<xsl:variable name="cacReceiverPartyPostalAddressID" select="$cacReceiverParty/cac:PostalAddress/cbc:ID" />
		<!-- 22. Dirección completa y detallada -->
		<xsl:variable name="cacReceiverPartyPostalAddressStreetName" select="$cacReceiverParty/cac:PostalAddress/cbc:StreetName" />
		<!-- 23. Urbanización -->
		<xsl:variable name="cacReceiverPartyPostalAddressCitySubdivisionName" select="$cacReceiverParty/cac:PostalAddress/cbc:CitySubdivisionName" />
		<!-- 24. Provincia -->
		<xsl:variable name="cacReceiverPartyPostalAddressCityName" select="$cacReceiverParty/cac:PostalAddress/cbc:CityName" />
		<!-- 25. Departamento -->
		<xsl:variable name="cacReceiverPartyPostalAddressCountrySubentity" select="$cacReceiverParty/cac:PostalAddress/cbc:CountrySubentity" />
		<!-- 26. Distrito -->
		<xsl:variable name="cacReceiverPartyPostalAddressDistrict" select="$cacReceiverParty/cac:PostalAddress/cbc:District" />
		<!-- 27. Código del país de la dirección -->
		<xsl:variable name="cacReceiverPartyLegalEntityName" select="$cacReceiverParty/cac:PartyLegalEntity/cbc:RegistrationName" />
		<!-- 28. Apellidos y nombres, denominación o razón social -->
		<xsl:variable name="cacReceiverPartyCountryCode" select="$cacReceiverParty/cac:PostalAddress/cac:Country/cbc:IdentificationCode" />
		
		<!-- Datos de la percepción del CPE -->
		<!-- 29. Régimen de percepción -->
		<xsl:variable name="regimenPercepcion" select="sac:SUNATPerceptionSystemCode" />
		<!-- 30. Tasa de percepción -->
		<xsl:variable name="tasaPercepcion" select="sac:SUNATPerceptionPercent" />
		<!-- 31. Observaciones -->
		<xsl:variable name="cbcNote" select="cbc:Note" />
		<!-- 32. Importe total Percibido -->
		<xsl:variable name="importeTotalPercibido" select="cbc:TotalInvoiceAmount" />
		<!-- 33. Moneda del Importe total Percibido -->
		<xsl:variable name="monedaImporteTotalPercibido" select="cbc:TotalInvoiceAmount/@currencyID" />
		<xsl:variable name="sumaImportePercibido" select="sum(sac:SUNATPerceptionDocumentReference[cbc:ID/@schemeID!='07' and cbc:ID/@schemeID!='40']/sac:SUNATPerceptionInformation/sac:SUNATPerceptionAmount)" />		
		<!-- 34. Importe total Cobrado -->
		<xsl:variable name="importeTotalCobrado" select="sac:SUNATTotalCashed" />
		<xsl:variable name="sumaImporteTotalACobrar" select="sum(sac:SUNATPerceptionDocumentReference[cbc:ID/@schemeID!='07' and cbc:ID/@schemeID!='40']/sac:SUNATPerceptionInformation/sac:SUNATNetTotalCashed)" />				
		<!-- 35. Moneda del Importe total Cobrado -->
		<xsl:variable name="monedaImporteTotalCobrado" select="sac:SUNATTotalCashed/@currencyID" />
		<!-- 36. Monto de redondeo del importe total pagado -->
		<xsl:variable name="montoRedondeoImporteTotalPagado" select="cbc:PayableRoundingAmount" />
		<xsl:variable name="montoRedondeoImporteTotalPagado_currency" select="cbc:PayableRoundingAmount/@currencyID" />
		
		<!-- Validaciones -->
		<!-- 1. Version del UBL -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2111'" />
			<xsl:with-param name="errorCodeValidate" select="'2110'" />
			<xsl:with-param name="node" select="$cbcUBLVersionID" />
			<xsl:with-param name="regexp" select="'^(2.0)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cbcUBLVersionID)"/>
		</xsl:call-template>

		<!-- 2. Version de la Estructura del Documento -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2113'" />
			<xsl:with-param name="errorCodeValidate" select="'2112'" />
			<xsl:with-param name="node" select="$cbcCustomizationID" />
			<xsl:with-param name="regexp" select="'^(1.0)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cbcCustomizationID)"/>
		</xsl:call-template>

		<!-- 4. Numeración, conformada por serie y número correlativo -->
		<xsl:if
			test="substring-before($nombreArchivoEnviado,'.') != concat($rucFilename,'-40-',$cbcID)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'1049'" />
				<xsl:with-param name="errorMessage"
					select="concat('Validation Filename error name: ', $nombreArchivoEnviado,'; cbc:ID: ', $cbcID)" />
			</xsl:call-template>
		</xsl:if>
							
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'1001'"/>
			<xsl:with-param name="node" select="$cbcID"/>
			<xsl:with-param name="regexp" select="'^[P][A-Z0-9]{3}-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cbcID)"/>
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
								<xsl:with-param name="errorCode" select="'2600'" />
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
							<xsl:with-param name="errorCode" select="'2600'" />
							<xsl:with-param name="errorMessage" 
								select="concat('Error en la linea 2) ', $t4, ' ', $t6, ' ', $t8, ' ', $t10, ' : ', $s1C, ' - ', $cbcIssueDate, ' - ', $currentdate, ' - ', $t1, ' - ', $t2)" />
						</xsl:call-template>
					</xsl:if>				
				</xsl:if>														
			</xsl:otherwise>
		</xsl:choose>			

		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'3322'" />
			<xsl:with-param name="node" select="$indicadorEmisiónExcepcional" />
			<xsl:with-param name="regexp" select="'^(01)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $indicadorEmisiónExcepcional)"/>
		</xsl:call-template>
				
		<!-- 7. Numero del Documento del emisor - Nro RUC -->				
		<xsl:if test="not($numeroRuc = $emisorNumeroDocumento)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'1034'" />
				<xsl:with-param name="errorMessage"
					select="concat('ruc del xml diferente al nombre del archivo ', $emisorNumeroDocumento, ' diff ', $numeroRuc)" />
			</xsl:call-template>
		</xsl:if>	
									
		<!-- 8. Tipo de documento de Identidad, por default 6-RUC - Mandatorio -->
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'2678'"/>
			<xsl:with-param name="node" select="$emisorTipoDocumento"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', position(), ' ', $emisorTipoDocumento)"/>
		</xsl:call-template>		
		
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2678'" />
			<xsl:with-param name="errorCodeValidate" select="'2511'" />
			<xsl:with-param name="node" select="$emisorTipoDocumento" />
			<xsl:with-param name="regexp" select="'^(6)$'" />
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $emisorTipoDocumento)"/>
		</xsl:call-template>

		<!-- 9. Nombre comercial - Opcional -->
		<xsl:call-template name="existValidateElementIfExistNULL">
			<xsl:with-param name="errorCodeNotExist" select="'2901'"/>
			<xsl:with-param name="node" select="$emisorRazonSocial"/>
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $emisorRazonSocial)"/>
		</xsl:call-template>
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2901'" />
			<xsl:with-param name="node" select="$emisorRazonSocial" />
			<xsl:with-param name="regexp" select="'^(.{0,1500})$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $emisorRazonSocial)"/>
		</xsl:call-template>

		<!-- 10. Codigo Ubigeo - Opcional -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2917'" />
			<xsl:with-param name="node" select="$cacAgentPartyPostalAddressID" />
			<xsl:with-param name="regexp" select="'(?!^(00))^\d{2}(?!(00))\d{4}$(?&lt;!(00)$)'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacAgentPartyPostalAddressID)"/>
		</xsl:call-template>

		<!-- 11. Direccion completa y detallada - Opcional -->
		<xsl:call-template name="existValidateElementIfExistNULL">
			<xsl:with-param name="errorCodeNotExist" select="'2916'"/>
			<xsl:with-param name="node" select="$cacAgentPartyPostalAddressStreetName"/>
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $cacAgentPartyPostalAddressStreetName)"/>
		</xsl:call-template>
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2916'" />		
			<xsl:with-param name="node" select="$cacAgentPartyPostalAddressStreetName" />
			<xsl:with-param name="regexp" select="'^(.{0,100})$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $cacAgentPartyPostalAddressStreetName)"/>
		</xsl:call-template>

		<!-- 12. Urbanizacion - Opcional -->
		<xsl:call-template name="existValidateElementIfExistNULL">
			<xsl:with-param name="errorCodeNotExist" select="'2902'"/>
			<xsl:with-param name="node" select="$cacAgentPartyPostalAddressCitySubdivisionName"/>
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $cacAgentPartyPostalAddressCitySubdivisionName)"/>
		</xsl:call-template>
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2902'" />
			<xsl:with-param name="node" select="$cacAgentPartyPostalAddressCitySubdivisionName" />
			<xsl:with-param name="regexp" select="'^(.{0,30})$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacAgentPartyPostalAddressCitySubdivisionName)"/>
		</xsl:call-template>

		<!-- 13. Provincia - Opcional -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2903'" />
			<xsl:with-param name="node" select="$cacAgentPartyPostalAddressCityName" />
			<xsl:with-param name="regexp" select="'^(.{0,30})$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacAgentPartyPostalAddressCityName)"/>
		</xsl:call-template>

		<!-- 14. Departamento - Opcional -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2904'" />
			<xsl:with-param name="node" select="$cacAgentPartyPostalAddressCountrySubentity" />
			<xsl:with-param name="regexp" select="'^(.{0,30})$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacAgentPartyPostalAddressCountrySubentity)"/>
		</xsl:call-template>

		<!-- 15. Distrito - Opcional -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2905'" />
			<xsl:with-param name="node" select="$cacAgentPartyPostalAddressDistrict" />
			<xsl:with-param name="regexp" select="'^(.{0,30})$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacAgentPartyPostalAddressDistrict)"/>
		</xsl:call-template>

		<!-- 16. Codigo del pais de la direccion - Opcional, debe ser PE -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2548'" />
			<xsl:with-param name="node" select="$cacAgentPartyCountryCode" />
			<xsl:with-param name="regexp" select="'^(PE)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacAgentPartyCountryCode)"/>
		</xsl:call-template>

		<!-- 17. Apellidos y nombres, denominacion o razon social - Mandatorio -->
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'1037'"/>
			<xsl:with-param name="node" select="$cacAgentPartyLegalEntityName"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', position(), ' ', $cacAgentPartyLegalEntityName)"/>
		</xsl:call-template>
									
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'1037'" />
			<xsl:with-param name="errorCodeValidate" select="'1038'" />
			<xsl:with-param name="node" select="$cacAgentPartyLegalEntityName" />
			<xsl:with-param name="regexp" select="'^(.{1,1500})$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacAgentPartyLegalEntityName)"/>
		</xsl:call-template>

		<!-- Datos del Cliente -->
		<!-- 18. Numero de documento de identidad - Mandatorio -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2679'" />
			<xsl:with-param name="errorCodeValidate" select="'2680'" />
			<xsl:with-param name="node" select="$numeroDocumentoIdentidadCliente" />
			<xsl:with-param name="regexp" select="'^[a-zA-Z0-9]{1,15}$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $numeroDocumentoIdentidadCliente)"/>
		</xsl:call-template>

		<!-- 18.  -->
		<xsl:if test="$emisorNumeroDocumento = $numeroDocumentoIdentidadCliente">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2604'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ',  $emisorNumeroDocumento, '  ',$numeroDocumentoIdentidadCliente)"/>
			</xsl:call-template>
		</xsl:if>	

		<!-- 19. Tipo de documento de Identidad, 0,1,4,6,7,A - Mandatorio -->
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'2516'"/>
			<xsl:with-param name="node" select="$tipoDocumentoIdentidadCliente"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', position(), ' ', $tipoDocumentoIdentidadCliente)"/>
		</xsl:call-template>			
		
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2516'" />
			<xsl:with-param name="errorCodeValidate" select="'2511'" />
			<xsl:with-param name="node" select="$tipoDocumentoIdentidadCliente" />
<!-- 			<xsl:with-param name="regexp" select="'^[01467A]{1}$'" /> -->
			<xsl:with-param name="regexp" select="'^(0|1|4|6|7|A|B|C|D|E|F|G|H)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoDocumentoIdentidadCliente)"/>
		</xsl:call-template>

		<!-- 20. Nombre comercial - Opcional -->		
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'2911'"/>
			<xsl:with-param name="node" select="$nombreComercialCliente"/>
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', position(), ' ', $nombreComercialCliente)"/>	
		</xsl:call-template>
				
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2911'" />
			<xsl:with-param name="node" select="$nombreComercialCliente" />
			<xsl:with-param name="regexp" select="'^(.{1,1500})$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $nombreComercialCliente)"/>
		</xsl:call-template>

		<!-- 21. Codigo Ubigeo - Opcional -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2917'" />
			<xsl:with-param name="node" select="$cacReceiverPartyPostalAddressID" />
			<xsl:with-param name="regexp" select="'(?!^(00))^\d{2}(?!(00))\d{4}$(?&lt;!(00)$)'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacReceiverPartyPostalAddressID)"/>
		</xsl:call-template>

		<!-- 22. Direccion completa y detallada - Opcional -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2919'" />
			<xsl:with-param name="node" select="$cacReceiverPartyPostalAddressStreetName" />
			<xsl:with-param name="regexp" select="'^(.{0,100})$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacReceiverPartyPostalAddressStreetName)"/>
		</xsl:call-template>

		<!-- 23. Urbanizacion - Opcional -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2912'" />
			<xsl:with-param name="node" select="$cacReceiverPartyPostalAddressCitySubdivisionName" />
			<xsl:with-param name="regexp" select="'^(.{0,30})$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacReceiverPartyPostalAddressCitySubdivisionName)"/>
		</xsl:call-template>

		<!-- 24. Provincia - Opcional -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2913'" />
			<xsl:with-param name="node" select="$cacReceiverPartyPostalAddressCityName" />
			<xsl:with-param name="regexp" select="'^(.{0,30})$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacReceiverPartyPostalAddressCityName)"/>
		</xsl:call-template>

		<!-- 25. Departamento - Opcional -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2914'" />
			<xsl:with-param name="node" select="$cacReceiverPartyPostalAddressCountrySubentity" />
			<xsl:with-param name="regexp" select="'^(.{0,30})$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacReceiverPartyPostalAddressCountrySubentity)"/>
		</xsl:call-template>

		<!-- 26. Distrito - Opcional -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2915'" />
			<xsl:with-param name="node" select="$cacReceiverPartyPostalAddressDistrict" />
			<xsl:with-param name="regexp" select="'^(.{0,30})$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacReceiverPartyPostalAddressDistrict)"/>
		</xsl:call-template>

		<!-- 27. Codigo del pais de la direccion - Opcional -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2548'" />
			<xsl:with-param name="node" select="$cacReceiverPartyCountryCode" />
			<xsl:with-param name="regexp" select="'^(PE)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacReceiverPartyCountryCode)"/>
		</xsl:call-template>

		<!-- 28. Apellidos y nombres, denominacion o razon social - Mandatorio -->
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'2134'"/>
			<xsl:with-param name="node" select="$cacReceiverPartyLegalEntityName"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', position(), ' ', $cacReceiverPartyLegalEntityName)"/>
		</xsl:call-template>		
		
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2134'" />
			<xsl:with-param name="errorCodeValidate" select="'2133'" />
			<xsl:with-param name="node"
				select="$cacReceiverPartyLegalEntityName" />
			<xsl:with-param name="regexp" select="'^(.{1,1500})$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacReceiverPartyLegalEntityName)"/>
		</xsl:call-template>
			
		<!-- 29. Régimen de percepción -->
		<xsl:if test="not(($regimenPercepcion='01') or ($regimenPercepcion='02') or ($regimenPercepcion = '03'))">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2602'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea ', position(), ' ', $regimenPercepcion)" />
			</xsl:call-template>
		</xsl:if>
		
		<!-- 30. Tasa de percepción -->
		<xsl:if test="($regimenPercepcion='01') and ($tasaPercepcion!=2)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2603'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea ', position(), $regimenPercepcion, ' ', $tasaPercepcion)" />
			</xsl:call-template>
		</xsl:if>
		
		<xsl:if test="($regimenPercepcion='02') and ($tasaPercepcion!=1)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2603'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea ', position(), $regimenPercepcion, ' ', $tasaPercepcion)" />
			</xsl:call-template>
		</xsl:if>
		
		<xsl:if test="($regimenPercepcion='03') and ($tasaPercepcion!=0.5)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2603'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea ', position(), $regimenPercepcion, ' ', $tasaPercepcion)" />
			</xsl:call-template>
		</xsl:if>
				
		<!-- Ini Datos de Percepcion y otros -->
		<!-- 32. Importe total Percibido, tiene que ser mayor que cero -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2669'" />
			<xsl:with-param name="node" select="$importeTotalPercibido" />
			<xsl:with-param name="regexp" select="'(?!(^0+(\.0+)?$))(^\d{1,12}(\.\d{1,2})?$)'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $importeTotalPercibido)"/>
		</xsl:call-template>

		<!-- 33. Moneda del Importe total Percibido, debe ser PEN -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2685'" />
			<xsl:with-param name="node"
				select="$monedaImporteTotalPercibido" />
			<xsl:with-param name="regexp" select="'^(PEN)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $monedaImporteTotalPercibido)"/>
		</xsl:call-template>

		<!-- 34. Importe total Cobrado, tiene que ser mayor que cero -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2687'" />
			<xsl:with-param name="node" select="$importeTotalCobrado" />
			<xsl:with-param name="regexp" select="'(?!(^0+(\.0+)?$))(^\d{1,12}(\.\d{1,2})?$)'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $importeTotalCobrado)"/>
		</xsl:call-template>

		<!-- 35. Moneda del Importe total Cobrado, debe ser PEN -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2690'" />
			<xsl:with-param name="node" select="$monedaImporteTotalCobrado" />
			<xsl:with-param name="regexp" select="'^(PEN)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $monedaImporteTotalCobrado)"/>
		</xsl:call-template>

		<xsl:if test="($montoRedondeoImporteTotalPagado)">
			<xsl:if test="($montoRedondeoImporteTotalPagado &lt; -1) or ($montoRedondeoImporteTotalPagado &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3303'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $montoRedondeoImporteTotalPagado)"/>
				</xsl:call-template>				
			</xsl:if>	
					
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'3304'" />
				<xsl:with-param name="node" select="$montoRedondeoImporteTotalPagado_currency" />
				<xsl:with-param name="regexp" select="'^(PEN)$'" />
				<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', position(), ' ', $montoRedondeoImporteTotalPagado_currency)"/>
			</xsl:call-template>		
		</xsl:if>
		
		
		<xsl:if test="($indicadorEmisiónExcepcional = '01')">
			<xsl:variable name="countNumeroDocumentoRel" select="count(sac:SUNATPerceptionDocumentReference/cbc:ID)" />
		  	<xsl:if test="($countNumeroDocumentoRel &gt; 1)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'3323'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $indicadorEmisiónExcepcional, ' ', $countNumeroDocumentoRel)"/>
				</xsl:call-template>		  
		  	</xsl:if>
		</xsl:if>
		
		
		
		<xsl:for-each select="sac:SUNATPerceptionDocumentReference">
			<!-- 36. Tipo de documento Relacionado -->
			<xsl:variable name="tipoDocumentoRel" select="./cbc:ID/@schemeID" />
			<!-- 37. Numero de documento Relacionado, conformado por la serie y numero -->
			<xsl:variable name="numeroDocumentoRel" select="./cbc:ID" />
			<!-- 38. Fecha emision documento Relacionado -->
			<xsl:variable name="fechaEmisionDocumentoRel" select="./cbc:IssueDate" />
			<!-- 39. Importe total documento Relacionado -->
			<xsl:variable name="importeTotalDocumentoRel" select="./cbc:TotalInvoiceAmount" />
			<!-- 40. Tipo de moneda documento Relacionado, el tipo de moneda lo valida el squema XSD -->
<!-- 			<xsl:variable name="monedaImporteTotalDocumentoRel" select="./cbc:TotalInvoiceAmount/@currencyID" /> -->
			<xsl:variable name="tipoMonedaImporteTotalDocumentoRel" select="./cbc:TotalInvoiceAmount/@currencyID" />
			<!-- 41. Fecha de cobro -->
			<xsl:variable name="fechaCobro" select="./cac:Payment/cbc:PaidDate" />
			<!-- 42. Numero de cobro -->
			<xsl:variable name="numeroCobro" select="./cac:Payment/cbc:ID" />
			<!-- 43 Importe de cobro sin percepción -->
<!-- 			<xsl:variable name="importeCobro" select="./cac:Payment/cbc:PaidAmount" /> -->
			<xsl:variable name="importeCobroSinPercepcion" select="./cac:Payment/cbc:PaidAmount" />			
			<!-- 44. Moneda de cobro, debe ser la misma que la del documento relacionado -->
<!-- 			<xsl:variable name="monedaImporteCobro" select="./cac:Payment/cbc:PaidAmount/@currencyID" /> -->
			<xsl:variable name="monedaCobro" select="./cac:Payment/cbc:PaidAmount/@currencyID" />
			<!-- 45. Importe percibido -->
			<xsl:variable name="importePercibido" select="./sac:SUNATPerceptionInformation/sac:SUNATPerceptionAmount" />
			<!-- 46. Moneda de importe percibido -->
			<xsl:variable name="monedaImportePercibido" select="./sac:SUNATPerceptionInformation/sac:SUNATPerceptionAmount/@currencyID" />
			<!-- 47. Fecha de percepción -->
			<xsl:variable name="fechaPercepcion" select="./sac:SUNATPerceptionInformation/sac:SUNATPerceptionDate" />
			<!-- 48. Importe total a cobrar (neto) -->
			<xsl:variable name="importeTotalACobrar" select="./sac:SUNATPerceptionInformation/sac:SUNATNetTotalCashed" />
			<!-- 49. Moneda del monto neto Cobrado -->
<!-- 			<xsl:variable name="monedaImporteTotalACobrar" select="./sac:SUNATPerceptionInformation/sac:SUNATNetTotalCashed/@currencyID" /> -->
			<xsl:variable name="monedaMontoNetoCobrado" select="./sac:SUNATPerceptionInformation/sac:SUNATNetTotalCashed/@currencyID" />
			<!-- 50. La moneda de referencia para el Tipo de Cambio -->
			<xsl:variable name="monedaReferenciaTipoCambio" select="./sac:SUNATPerceptionInformation/cac:ExchangeRate/cbc:SourceCurrencyCode" />
			<!-- 51 La moneda objetivo para la Tasa de Cambio -->
<!-- 			<xsl:variable name="monedaPENTipoCambio" select="./sac:SUNATPerceptionInformation/cac:ExchangeRate/cbc:TargetCurrencyCode" /> -->
			<xsl:variable name="monedaObjetivoTasaCambio" select="./sac:SUNATPerceptionInformation/cac:ExchangeRate/cbc:TargetCurrencyCode" />
			<!-- 52. El factor aplicado a la moneda de origen para calcular la moneda de destino (Tipo de cambio) -->
			<xsl:variable name="importeTipoCambio" select="./sac:SUNATPerceptionInformation/cac:ExchangeRate/cbc:CalculationRate" />			
			<!-- 53. Fecha de cambio -->
			<xsl:variable name="fechaTipoCambio" select="./sac:SUNATPerceptionInformation/cac:ExchangeRate/cbc:Date" />
									
			<!-- 36. Tipo de documento Relacionado -->
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2691'" />
				<xsl:with-param name="errorCodeValidate" select="'2692'" />
				<xsl:with-param name="node" select="$tipoDocumentoRel" />
				<xsl:with-param name="regexp" select="'^(01|03|12|07|08|40)$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea ', position(), ' ', $tipoDocumentoRel)" />
			</xsl:call-template>
			
			<xsl:if test="($indicadorEmisiónExcepcional = '01')">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'3324'" />
					<xsl:with-param name="node" select="$tipoDocumentoRel" />
					<xsl:with-param name="regexp" select="'^(01)$'" />
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $indicadorEmisiónExcepcional, ' ', $tipoDocumentoRel)"/>
				</xsl:call-template>			
			</xsl:if>
			
			<!-- 37. Numero de documento Relacionado, conformado por la serie y numero -->
			<xsl:choose>
				<xsl:when test="$tipoDocumentoRel = '12'">
					<xsl:call-template name="existAndRegexpValidateElement">
						<xsl:with-param name="errorCodeNotExist" select="'2693'" />
						<xsl:with-param name="errorCodeValidate" select="'2694'" />
						<xsl:with-param name="node" select="$numeroDocumentoRel" />
						<xsl:with-param name="regexp" select="'^-[a-zA-Z0-9-]{1,20}(-[0-9]{1,20})$'" />
<!-- 						<xsl:with-param name="regexp" select="'^([a-zA-Z0-9]{1,20}(-[0-9]{1,20})|[-]{1})$'" /> -->
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea ', position(), ' ', $numeroDocumentoRel)" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="existAndRegexpValidateElement">
						<xsl:with-param name="errorCodeNotExist" select="'2693'" />
						<xsl:with-param name="errorCodeValidate" select="'2694'" />
						<xsl:with-param name="node" select="$numeroDocumentoRel" />
						<xsl:with-param name="regexp" select="'^(E001|EB01|((F|P|B)[A-Z0-9]{3})|([0-9]{4}))-(?!0+$)([0-9]{1,8})$'"/>
<!-- 						<xsl:with-param name="regexp" select="'^(E001|EB01|((F|B|P)[A-Z0-9]{3})|((?!(^0{4}))\d{4}))-(?!0+$)([0-9]{1,8})$'" /> -->
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea ', position(), ' ', $numeroDocumentoRel)" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:if test="$tipoDocumentoRel = '03'">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'3328'"/>
					<xsl:with-param name="node" select="$numeroDocumentoRel"/>
 						<xsl:with-param name="regexp" select="'^[\d]{1}$'"/> 
<!--					<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,29}$'"/>-->
					<xsl:with-param name="descripcion" 
						select="concat('Error en la linea: ', position(), ' ', $numeroDocumentoRel)"/>   							
				</xsl:call-template>			
			</xsl:if>
			
		
			
			<!-- 39. Importe total documento Relacionado -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2696'" />
				<xsl:with-param name="node" select="$importeTotalDocumentoRel" />
				<xsl:with-param name="regexp" select="'(?!(^0+(\.0+)?$))(^\d{1,12}(\.\d{1,2})?$)'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea ', position(), ' ', $importeTotalDocumentoRel)" />
			</xsl:call-template>

			<!--Datos del Pago (3) -->
			<xsl:if test="($tipoDocumentoRel!='07') and not($fechaCobro)">
				<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2702'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoDocumentoRel)" />
				</xsl:call-template>				
			</xsl:if>
			
			<xsl:if test="($fechaCobro)">						
				<!-- 41. Fecha de cobro -->
				<xsl:if test="(substring($fechaCobro, 1, 7)!=substring($cbcIssueDate, 1, 7))">	
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2659'" />
						<xsl:with-param name="errorMessage" 
							select="concat('Error en la linea: ', $fechaCobro, ' - ', $cbcIssueDate)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:if test="(substring($cbcIssueDate, 1, 7)=substring($fechaEmisionDocumentoRel, 1, 7))">	
					<xsl:variable name="t1" select="xs:date($fechaCobro)-xs:date($fechaEmisionDocumentoRel)" />
					<xsl:variable name="t2" select="fn:days-from-duration(xs:duration($t1))" />			
					<xsl:if	test="($t2 &lt; 0)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2612'" />
							<xsl:with-param name="errorMessage" 
								select="concat('Error en la linea: a) ', $fechaCobro, ' - ', $fechaEmisionDocumentoRel, ' - ', $t1, ' - ', $t2)" />
						</xsl:call-template>
					</xsl:if>						
					
					<xsl:variable name="t3" select="xs:date($fechaCobro)-xs:date($cbcIssueDate)" />
					<xsl:variable name="t4" select="fn:days-from-duration(xs:duration($t3))" />			
					<xsl:if	test="($t4 &gt; 0)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2612'" />
							<xsl:with-param name="errorMessage" 
								select="concat('Error en la linea: b) ', $fechaCobro, ' - ', $cbcIssueDate, ' - ', $t3, ' - ', $t4)" />
						</xsl:call-template>
					</xsl:if>						
				</xsl:if>		
						
				<xsl:if test="(substring($cbcIssueDate, 1, 7)!=substring($fechaEmisionDocumentoRel, 1, 7))">	
					<xsl:if test="(substring($fechaCobro, 1, 7)!=substring($cbcIssueDate, 1, 7))">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2612'" />
							<xsl:with-param name="errorMessage" 
								select="concat('Error en la linea: c) ', $fechaCobro, ' - ', $cbcIssueDate)" />
						</xsl:call-template>
					</xsl:if>						
					
					<xsl:variable name="t5" select="xs:date($fechaCobro)-xs:date($cbcIssueDate)" />
					<xsl:variable name="t6" select="fn:days-from-duration(xs:duration($t5))" />			
					<xsl:if	test="($t6 &gt; 0)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2612'" />
							<xsl:with-param name="errorMessage" 
								select="concat('Error en la linea: d) ', $fechaCobro, ' - ', $cbcIssueDate, ' - ', $t5, ' - ', $t6)" />
						</xsl:call-template>
					</xsl:if>					
				</xsl:if>		
			</xsl:if>
			
			<xsl:if test="($tipoDocumentoRel!='07')">
				<!-- 42. Numero de cobro -->
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2697'" />
					<xsl:with-param name="errorCodeValidate" select="'2698'" />
					<xsl:with-param name="node" select="$numeroCobro" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,9}$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea ', position(), ' ', $numeroCobro)" />
				</xsl:call-template>
				
				<xsl:if
					test="count(key('by-document-SUNATPerception-reference', concat($numeroDocumentoRel, ' ',$numeroCobro))) > 1">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2626'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea ', position(), ' ', $numeroDocumentoRel,' ', $numeroCobro)" />
					</xsl:call-template>
				</xsl:if>

				<!-- 43 Importe del cobro -->
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2699'" />
					<xsl:with-param name="errorCodeValidate" select="'2700'" />
					<xsl:with-param name="node" select="$importeCobroSinPercepcion" />
					<xsl:with-param name="regexp" select="'(?!(^0+(\.0+)?$))(^\d{1,12}(\.\d{1,2})?$)'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea ', position(), ' ', $importeCobroSinPercepcion)" />
				</xsl:call-template>

				<!-- 44. Moneda de cobro, debe ser la misma que la del documento relacionado -->
				<xsl:if test="$tipoMonedaImporteTotalDocumentoRel != $monedaCobro">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2607'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea ', position(), $tipoMonedaImporteTotalDocumentoRel, ' - ', $monedaCobro)" />
					</xsl:call-template>
				</xsl:if>			
			</xsl:if>
			
			<!-- 52. El factor aplicado a la moneda de origen para calcular la moneda de destino (Tipo de cambio) -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2716'" />
				<xsl:with-param name="node" select="$importeTipoCambio" />
				<xsl:with-param name="regexp" select="'(?!(^0+(\.0+)?$))(^\d{1,4}(\.\d{1,6})?$)'" />
				<xsl:with-param name="descripcion"
				 	select="concat('Error en la linea ', position(), ' ', $importeTipoCambio)" />
			</xsl:call-template>
				
			<!-- Ini Datos de la Percepcion -->
			<!-- 45. Importe percibido -->
			<xsl:if test="($importePercibido)">					
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2705'" />
					<xsl:with-param name="node" select="$importePercibido" />
					<xsl:with-param name="regexp" select="'(?!(^0+(\.0+)?$))(^\d{1,12}(\.\d{1,2})?$)'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea ', position(), ' ', $importePercibido)" />
				</xsl:call-template>
				
				<xsl:if test="($tipoMonedaImporteTotalDocumentoRel='PEN')">						
					<xsl:variable name="montoPagoConsignado" select="($importeCobroSinPercepcion * $tasaPercepcion) div 100" />
					<xsl:variable name="diffMontoPagado" select="$montoPagoConsignado - $importePercibido" />
					<xsl:if test="($diffMontoPagado &lt; -1) or ($diffMontoPagado &gt; 1)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2608'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea 45a ', position(), ' ', $importeCobroSinPercepcion, ' ', $tasaPercepcion, ' ', $montoPagoConsignado,' ', $importePercibido, ' ', $diffMontoPagado)" />								
						</xsl:call-template>
					</xsl:if>
				</xsl:if>	
				
				<xsl:if test="($tipoMonedaImporteTotalDocumentoRel!='PEN')">						
					<xsl:variable name="montoPagoConsignado" select="(($importeCobroSinPercepcion * $tasaPercepcion) div 100) * $importeTipoCambio" />
					<xsl:variable name="diffMontoPagado" select="$montoPagoConsignado - $importePercibido" />
					<xsl:if test="($diffMontoPagado &lt; -1) or ($diffMontoPagado &gt; 1)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2608'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea 45b ', position(), ' ', $importeCobroSinPercepcion, ' ', $tasaPercepcion, ' ', $importeTipoCambio, ' ', $montoPagoConsignado,' ', $importePercibido,' ', $diffMontoPagado)" />								
						</xsl:call-template>						
					</xsl:if>	
				</xsl:if>						
			</xsl:if>
			
			<!-- 46. Moneda de importe percibido -->
			<xsl:if test="($monedaImportePercibido)">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2707'" />
					<xsl:with-param name="node" select="$monedaImportePercibido" />
					<xsl:with-param name="regexp" select="'^(PEN)$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea ', position(), ' ', $monedaImportePercibido)" />
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if test="($importeTotalACobrar)">	
				<!-- 48. Monto total a cobrar -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2711'" />
					<xsl:with-param name="node" select="$importeTotalACobrar" />
					<xsl:with-param name="regexp" select="'(?!(^0+(\.0+)?$))(^\d{1,12}(\.\d{1,2})?$)'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea ', position(), ' ', $importeTotalACobrar)" />
				</xsl:call-template>
				
				<xsl:if test="($tipoMonedaImporteTotalDocumentoRel='PEN')">	
					<xsl:variable name="montoXPagar" select="$importeCobroSinPercepcion + $importePercibido" />					
					<xsl:variable name="diffMontoPagado" select="$importeTotalACobrar - $montoXPagar" />
					<xsl:if test="($diffMontoPagado &lt; -1) or ($diffMontoPagado &gt; 1)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2608'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea 48a ', position(), ' ', $importeCobroSinPercepcion,' ', $importePercibido, ' ', $montoXPagar, ' ', $importeTotalACobrar, ' ', $diffMontoPagado)" />								
						</xsl:call-template>
					</xsl:if>
				</xsl:if>	
				
				<xsl:if test="($tipoMonedaImporteTotalDocumentoRel!='PEN')">						
					<xsl:variable name="montoPagoConsignado" select="$importeCobroSinPercepcion * $importeTipoCambio" />
					<xsl:variable name="montoXPagar" select="$montoPagoConsignado + $importePercibido" />
					<xsl:variable name="diffMontoPagado" select="$importeTotalACobrar - $montoXPagar" />
					<xsl:if test="($diffMontoPagado &lt; -1) or ($diffMontoPagado &gt; 1)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2608'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea 48b ', position(), ' ', $importeCobroSinPercepcion, ' ', $importeTipoCambio, ' ', $montoPagoConsignado,' ', $importePercibido, ' ', $montoXPagar, ' ', $importeTotalACobrar, ' ', $diffMontoPagado)" />								
						</xsl:call-template>
					</xsl:if>
				</xsl:if>					
			</xsl:if>
			
			<xsl:if test="($monedaMontoNetoCobrado)">
				<!-- 49. Moneda del monto total a cobrar -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2713'" />
					<xsl:with-param name="node" select="$monedaMontoNetoCobrado" />
					<xsl:with-param name="regexp" select="'^(PEN)$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea ', position(), ' ', $monedaMontoNetoCobrado)" />
				</xsl:call-template>
			</xsl:if>

			<!-- Ini Tipo de cambio -->
			<xsl:if test="($tipoDocumentoRel!='07')">
				<xsl:if test="($tipoMonedaImporteTotalDocumentoRel!='PEN')">
					<!-- 50. La moneda de referencia para el Tipo de Cambio -->
					<xsl:call-template name="existValidateElementIfExist">
						<xsl:with-param name="errorCodeNotExist" select="'2719'" />
						<xsl:with-param name="node" select="$monedaReferenciaTipoCambio" />
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea ', position(), ' ', $monedaReferenciaTipoCambio)" />
					</xsl:call-template>
					
					<!-- 52. El factor aplicado a la moneda de origen para calcular la moneda de destino (Tipo de cambio) -->
					<xsl:call-template name="existValidateElementIfExist">
						<xsl:with-param name="errorCodeNotExist" select="'2721'"/>
						<xsl:with-param name="node" select="$importeTipoCambio"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $importeTipoCambio)"/>
					</xsl:call-template>	
					
					<!-- 53. Fecha de cambio -->
					<xsl:call-template name="existValidateElementIfExist">
						<xsl:with-param name="errorCodeNotExist" select="'2722'"/>
						<xsl:with-param name="node" select="$fechaTipoCambio"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $fechaTipoCambio)"/>
					</xsl:call-template>	
				</xsl:if>

				<xsl:if
					test="$monedaReferenciaTipoCambio != $tipoMonedaImporteTotalDocumentoRel">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2749'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea ', position(), $monedaReferenciaTipoCambio, ' - ', $tipoMonedaImporteTotalDocumentoRel)" />
					</xsl:call-template>
				</xsl:if>

				<!-- 51 La moneda objetivo para la Tasa de Cambio, debe ser PEN -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2715'" />
					<xsl:with-param name="node" select="$monedaObjetivoTasaCambio" />
					<xsl:with-param name="regexp" select="'^(PEN)$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea ', position(), ' ', $monedaObjetivoTasaCambio)" />
				</xsl:call-template>

			</xsl:if>
		</xsl:for-each>	
		
		<!-- 32. Importe total Percibido, tiene que ser mayor que cero -->	
		<xsl:variable name="dif_Monto_TP" select="$importeTotalPercibido - $sumaImportePercibido" />
		<xsl:if test="($dif_Monto_TP &lt; -0.001) or ($dif_Monto_TP &gt; 0.001)">									
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2667'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $importeTotalPercibido, ' ', $sumaImportePercibido)"/>
			</xsl:call-template>
		</xsl:if>
		
		<!-- 34. Importe total Cobrado, tiene que ser mayor que cero -->
		<xsl:variable name="dif_Monto_TC" select="$importeTotalCobrado - $sumaImporteTotalACobrar" />
		<xsl:if test="($dif_Monto_TC &lt; -0.001) or ($dif_Monto_TC &gt; 0.001)">				
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2668'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $importeTotalCobrado, ' ', $sumaImporteTotalACobrar)"/>
			</xsl:call-template>
		</xsl:if>		
	</xsl:template>
</xsl:stylesheet>