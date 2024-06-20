<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" 
	xmlns="urn:sunat:names:specification:ubl:peru:schema:xsd:Retention-1"
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
<!-- PRODUCCION  -->
<!--   <xsl:include href="util/validate_utils.xsl" dp:ignore-multiple="yes" /> -->
<!-- BETA  -->
  <xsl:include href="../util/validate_utils.xsl" dp:ignore-multiple="yes" />
<!-- FIN: AXTEROID -->	

	<xsl:key name="by-document-SUNATRetention-reference"
		match="*[local-name()='Retention']/sac:SUNATRetentionDocumentReference" use="concat(cbc:ID,' ', cac:Payment/cbc:ID)" />

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
		<xsl:variable name="cbcReferenceDate" select="cbc:ReferenceDate" />
		<xsl:variable name="currentdate" select="fn:current-date()"></xsl:variable>
		
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

		<!-- Información del Proveedor -->
		<xsl:variable name="cacReceiverParty" select="cac:ReceiverParty" />
		<!-- 18. Número de documento de identidad del proveedor -->
		<xsl:variable name="numeroDocumentoIdentidadProveedor" select="$cacReceiverParty/cac:PartyIdentification/cbc:ID" />
		<!-- 19. Tipo de documento de Identidad del proveedor -->
		<xsl:variable name="tipoDocumentoIdentidadProveedor" select="$cacReceiverParty/cac:PartyIdentification/cbc:ID/@schemeID" />
		<!-- 20. Nombre comercial del proveedor -->
		<xsl:variable name="nombreComercialProveedor" select="$cacReceiverParty/cac:PartyName/cbc:Name" />
		
		<!-- Domicilio fiscal del Proveedor -->
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
		<xsl:variable name="cacReceiverPartyCountryCode" select="$cacReceiverParty/cac:PostalAddress/cac:Country/cbc:IdentificationCode" />
		<!-- 28. Apellidos y nombres, denominación o razón social -->
		<xsl:variable name="cacReceiverPartyLegalEntityName" select="$cacReceiverParty/cac:PartyLegalEntity/cbc:RegistrationName" />
		
		<!-- Datos de la Retención del CRE -->
		<!-- 29. Régimen de Retención -->
		<xsl:variable name="regimenRetencion" select="sac:SUNATRetentionSystemCode" />
		<!-- 30. Tasa de Retención -->
		<xsl:variable name="tasaRetencion" select="sac:SUNATRetentionPercent" />
		<!-- 31. Observaciones -->
		<xsl:variable name="cbcNote" select="cbc:Note" />
		<!-- 32. Importe total Retenido -->
		<xsl:variable name="importeTotalRetenido" select="cbc:TotalInvoiceAmount" />
		<xsl:variable name="sumaImporteRetenido" select="sum(sac:SUNATRetentionDocumentReference[cbc:ID/@schemeID!='07' and cbc:ID/@schemeID!='20']/sac:SUNATRetentionInformation/sac:SUNATRetentionAmount)" />
		<!-- 33. Moneda del Importe total Retenido -->
		<xsl:variable name="monedaImporteTotalRetenido" select="cbc:TotalInvoiceAmount/@currencyID" />
		<!-- 34. Importe total Pagado -->
		<xsl:variable name="importeTotalPagado" select="sac:SUNATTotalPaid" />
		<xsl:variable name="sumaImporteTotalAPagar" select="sum(sac:SUNATRetentionDocumentReference[cbc:ID/@schemeID!='07' and cbc:ID/@schemeID!='20']/sac:SUNATRetentionInformation/sac:SUNATNetTotalPaid)" />		
		<!-- 35. Moneda del Importe total Pagado -->
		<xsl:variable name="monedaImporteTotalPagado" select="sac:SUNATTotalPaid/@currencyID" />
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
			test="substring-before($nombreArchivoEnviado,'.') != concat($rucFilename,'-20-',$cbcID)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'1049'" />
				<xsl:with-param name="errorMessage"
					select="concat('Validation Filename error name: ', $nombreArchivoEnviado,'; cbc:ID: ', $cbcID)" />
			</xsl:call-template>
		</xsl:if>
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'1001'"/>
			<xsl:with-param name="node" select="$cbcID"/>
			<xsl:with-param name="regexp" select="'^[R][A-Z0-9]{3}-[0-9]{1,8}$|^[0-9]{1,4}-[0-9]{1,8}$'"/>
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
		
		<!-- 7. Numero del Documento del emisor - Nro RUC -->
		<xsl:if test="not($numeroRuc = $emisorNumeroDocumento)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'1034'" />
				<xsl:with-param name="errorMessage"
					select="concat('ruc del xml diferente al nombre del archivo ', $emisorNumeroDocumento, ' diff ', $numeroRuc)" />
			</xsl:call-template>
		</xsl:if>	
		
		<!-- 8.  Tipo de documento de Identidad del emisor -->
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'2678'"/>
			<xsl:with-param name="node" select="$emisorTipoDocumento"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', position(), ' ', $emisorTipoDocumento)"/>
		</xsl:call-template>		
				
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2678'"/>
			<xsl:with-param name="errorCodeValidate" select="'2511'"/>
			<xsl:with-param name="node" select="$emisorTipoDocumento"/>
			<xsl:with-param name="regexp" select="'^[6]{1}$'"/>
<!-- 			<xsl:with-param name="regexp" select="'^(6)$'" /> -->
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $emisorTipoDocumento)"/>
		</xsl:call-template>
		
		<!-- 9. Nombre comercial del emisor -->		
		<xsl:call-template name="existValidateElementIfExistNULL">
			<xsl:with-param name="errorCodeNotExist" select="'2901'"/>
			<xsl:with-param name="node" select="$emisorRazonSocial"/>
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
					select="concat('Error en la linea: ', $emisorRazonSocial)"/>
		</xsl:call-template>
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2901'"/>
			<xsl:with-param name="node" select="$emisorRazonSocial"/>
<!-- 			<xsl:with-param name="regexp" select="'^[^\s].{1,100}$'"/> -->
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

		<!-- Fin Datos del Emisor Electrónico -->
		<!-- Ini Datos del Proveedor -->

		<!-- 18. Numero de documento de identidad - Mandatorio -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2723'" />
			<xsl:with-param name="errorCodeValidate" select="'2724'" />
			<xsl:with-param name="node" select="$numeroDocumentoIdentidadProveedor" />
			<xsl:with-param name="regexp" select="'^[0-9]{11}$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $numeroDocumentoIdentidadProveedor)"/>
		</xsl:call-template>	
		
		<!-- 18.  -->
		<xsl:if test="$emisorNumeroDocumento = $numeroDocumentoIdentidadProveedor">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2620'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ',  $emisorNumeroDocumento, '  ',$numeroDocumentoIdentidadProveedor)"/>
			</xsl:call-template>
		</xsl:if>	

		<!-- 19. Tipo de documento de Identidad, 6=RUC - Mandatorio -->
		<xsl:call-template name="existValidateElementNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'2516'"/>
			<xsl:with-param name="node" select="$tipoDocumentoIdentidadProveedor"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', position(), ' ', $tipoDocumentoIdentidadProveedor)"/>
		</xsl:call-template>				
		
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2516'" />
			<xsl:with-param name="errorCodeValidate" select="'2511'" />
			<xsl:with-param name="node" select="$tipoDocumentoIdentidadProveedor" />
			<xsl:with-param name="regexp" select="'^(6)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoDocumentoIdentidadProveedor)"/>
		</xsl:call-template>

		<!-- 20. Nombre comercial - Opcional -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2906'" />
			<xsl:with-param name="node" select="$nombreComercialProveedor" />
			<xsl:with-param name="regexp" select="'^(.{0,1500})$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $nombreComercialProveedor)"/>
		</xsl:call-template>

		<!-- Domicilio fiscal del Proveedor -->
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
			<xsl:with-param name="errorCodeValidate" select="'2918'" />
			<xsl:with-param name="node" select="$cacReceiverPartyPostalAddressStreetName" />
			<xsl:with-param name="regexp" select="'^(.{0,100})$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacReceiverPartyPostalAddressStreetName)"/>
		</xsl:call-template>

		<!-- 23. Urbanizacion - Opcional -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2907'" />
			<xsl:with-param name="node" select="$cacReceiverPartyPostalAddressCitySubdivisionName" />
			<xsl:with-param name="regexp" select="'^(.{0,30})$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacReceiverPartyPostalAddressCitySubdivisionName)"/>
		</xsl:call-template>

		<!-- 24. Provincia - Opcional -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2908'" />
			<xsl:with-param name="node" select="$cacReceiverPartyPostalAddressCityName" />
			<xsl:with-param name="regexp" select="'^(.{0,30})$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacReceiverPartyPostalAddressCityName)"/>
		</xsl:call-template>

		<!-- 25. Departamento - Opcional -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2909'" />
			<xsl:with-param name="node" select="$cacReceiverPartyPostalAddressCountrySubentity" />
			<xsl:with-param name="regexp" select="'^(.{0,30})$'" />
			<xsl:with-param name="isError" select="false()"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacReceiverPartyPostalAddressCountrySubentity)"/>
		</xsl:call-template>

		<!-- 26. Distrito - Opcional -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2910'" />
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
			<xsl:with-param name="node" select="$cacReceiverPartyLegalEntityName" />
			<xsl:with-param name="regexp" select="'^(.{1,1500})$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $cacReceiverPartyLegalEntityName)"/>
		</xsl:call-template>

		<!-- Fin Datos del Proveedor -->

		<!-- Datos de la Retención del CRE -->
		<!-- 29. Regimen de retencion, debe de 01 -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2618'" />
			<xsl:with-param name="node" select="$regimenRetencion" />
			<xsl:with-param name="regexp" select="'^(01)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $regimenRetencion)"/>
		</xsl:call-template>

		<!-- 30. Tasa de retencion, debe der 3.00 -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2619'" />
			<xsl:with-param name="node" select="$tasaRetencion" />
			<xsl:with-param name="regexp" select="'^(3|3.0|3.00)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tasaRetencion)"/>				
		</xsl:call-template>
		
		<!-- 32. Importe total Retenido, tiene que ser mayor que cero -->						
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2669'" />
			<xsl:with-param name="node" select="$importeTotalRetenido" />
			<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', position(), ' ', $importeTotalRetenido)"/>
		</xsl:call-template>

		<!-- 33. Moneda del Importe total Retenido, debe ser PEN -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2728'" />
			<xsl:with-param name="node" select="$monedaImporteTotalRetenido" />
			<xsl:with-param name="regexp" select="'^(PEN)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $monedaImporteTotalRetenido)"/>
		</xsl:call-template>

		<!-- 34. Importe total Pagado, tiene que ser mayor que cero -->
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2730'" />
			<xsl:with-param name="node" select="$importeTotalPagado" />
			<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', position(), ' ', $importeTotalPagado)"/>
		</xsl:call-template>

		<!-- 35. Moneda del Importe total Pagado, debe ser PEN -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2732'" />
			<xsl:with-param name="errorCodeValidate" select="'2732'" />
			<xsl:with-param name="node" select="$monedaImporteTotalPagado" />
			<xsl:with-param name="regexp" select="'^(PEN)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $monedaImporteTotalPagado)"/>
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
		
		<xsl:for-each select="sac:SUNATRetentionDocumentReference">
			<!-- 36. Tipo de documento Relacionado -->
			<xsl:variable name="tipoDocumentoRel" select="./cbc:ID/@schemeID" />
			<!-- 37. Numero de documento Relacionado, conformado por la serie y numero -->
			<xsl:variable name="numeroDocumentoRel" select="./cbc:ID" />
			<!-- 38. Fecha emision documento Relacionado -->
			<xsl:variable name="fechaEmisionDocumentoRel" select="./cbc:IssueDate" />
			<!-- 39. Importe total documento Relacionado -->
			<xsl:variable name="importeTotalDocumentoRel" select="./cbc:TotalInvoiceAmount" />
			<!-- 40. Tipo de moneda documento Relacionado, el tipo de moneda lo valida el squema XSD -->
			<xsl:variable name="tipoMonedaImporteTotalDocumentoRel" select="./cbc:TotalInvoiceAmount/@currencyID" />
			<!-- 41 Fecha de pago -->
			<xsl:variable name="fechaPago" select="./cac:Payment/cbc:PaidDate" />
			<!-- 42. Numero de pago -->
			<xsl:variable name="numeroPago" select="./cac:Payment/cbc:ID" />
			<!-- 43. Importe de pago sin retención -->
			<xsl:variable name="importePagoSinRetencion" select="./cac:Payment/cbc:PaidAmount" />
			<!-- 44. Moneda de pago -->
			<xsl:variable name="monedaImportePago" select="./cac:Payment/cbc:PaidAmount/@currencyID" />
			<!-- 45. Importe retenido -->
			<xsl:variable name="importeRetenido" select="./sac:SUNATRetentionInformation/sac:SUNATRetentionAmount" />
			<!-- 46. Moneda de importe retenido -->
			<xsl:variable name="monedaImporteRetenido" select="./sac:SUNATRetentionInformation/sac:SUNATRetentionAmount/@currencyID" />
			<!-- 47. Fecha de Retención -->
			<xsl:variable name="fechaRetencion" select="./sac:SUNATRetentionInformation/sac:SUNATRetentionDate" />
			<!-- 48. Importe total a pagar (neto) -->
			<xsl:variable name="importeTotalAPagar" select="./sac:SUNATRetentionInformation/sac:SUNATNetTotalPaid" />		
			<!-- 49. Moneda del monto neto pagado -->
			<xsl:variable name="monedaMontoNetoPagado" select="./sac:SUNATRetentionInformation/sac:SUNATNetTotalPaid/@currencyID" />
			<!-- 50. La moneda de referencia para el Tipo de Cambio -->
			<xsl:variable name="monedaReferenciaTipoCambio" select="./sac:SUNATRetentionInformation/cac:ExchangeRate/cbc:SourceCurrencyCode" />
			<!-- 51. La moneda objetivo para la Tasa de Cambio -->
			<xsl:variable name="monedaObjetivoTasaCambio" select="./sac:SUNATRetentionInformation/cac:ExchangeRate/cbc:TargetCurrencyCode" />
			<!-- 52. El factor aplicado a la moneda de origen para calcular la moneda de destino (Tipo de cambio) -->
			<xsl:variable name="importeTipoCambio" select="./sac:SUNATRetentionInformation/cac:ExchangeRate/cbc:CalculationRate" />
			<!-- 53. Fecha de cambio -->
			<xsl:variable name="fechaTipoCambio" select="./sac:SUNATRetentionInformation/cac:ExchangeRate/cbc:Date" />

			
			<!-- 36. Tipo de documento Relacionado -->
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2691'" />
				<xsl:with-param name="errorCodeValidate" select="'2692'" />
				<xsl:with-param name="node" select="$tipoDocumentoRel" />
				<xsl:with-param name="regexp" select="'^(01|12|07|08|20)$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea ', position(), ' ', $tipoDocumentoRel)" />
			</xsl:call-template>

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
							select="concat('Error en la linea a) ', position(), ' ', $numeroDocumentoRel)" />
					</xsl:call-template>
				</xsl:when>				
				<xsl:otherwise>
					<xsl:call-template name="existAndRegexpValidateElement">
						<xsl:with-param name="errorCodeNotExist" select="'2693'" />
						<xsl:with-param name="errorCodeValidate" select="'2694'" />
						<xsl:with-param name="node" select="$numeroDocumentoRel" />
						<xsl:with-param name="regexp" select="'^(E001|((F|R)[A-Z0-9]{3})|([0-9]{4}))-(?!0+$)([0-9]{1,8})$'"/>
						<xsl:with-param name="descripcion"
							select="concat('Error en la linea b) ', position(), ' ', $numeroDocumentoRel)" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
						
			<!-- 39. Importe total documento Relacionado -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2696'" />
				<xsl:with-param name="node" select="$importeTotalDocumentoRel" />
				<xsl:with-param name="regexp" select="'(?!(^0+(\.0+)?$))(^\d{1,12}(\.\d{1,2})?$)'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea ', position(), ' ', $importeTotalDocumentoRel)" />
			</xsl:call-template>

			<!-- Datos del Pago (3) -->
			<!-- 41 Fecha de pago -->
			<xsl:if test="($tipoDocumentoRel!='07') and not($fechaPago)">
				<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2737'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea: ', position(), ' ', $tipoDocumentoRel)" />
				</xsl:call-template>				
			</xsl:if>
						
			<xsl:if test="($fechaPago)">
				<!-- 41 Fecha de pago -->				
				<xsl:if test="(substring($fechaPago, 1, 7)!=substring($cbcIssueDate, 1, 7))">	
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2661'" />
						<xsl:with-param name="errorMessage" 
							select="concat('Error en la linea: ', $fechaPago, ' - ', $cbcIssueDate)" />
					</xsl:call-template>
				</xsl:if>				

				<xsl:if test="(substring($cbcIssueDate, 1, 7)=substring($fechaEmisionDocumentoRel, 1, 7))">	
					<xsl:variable name="t1" select="xs:date($cbcIssueDate)-xs:date($fechaEmisionDocumentoRel)" />
					<xsl:variable name="t2" select="fn:days-from-duration(xs:duration($t1))" />			
					<xsl:if	test="($t2 &lt; 0)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2625'" />
							<xsl:with-param name="errorMessage" 
								select="concat('Error en la linea: a) ', $fechaPago, ' - ', $fechaEmisionDocumentoRel, ' - ', $t1, ' - ', $t2)" />
						</xsl:call-template>
					</xsl:if>						
					
					<xsl:variable name="t3" select="xs:date($fechaPago)-xs:date($cbcIssueDate)" />
					<xsl:variable name="t4" select="fn:days-from-duration(xs:duration($t3))" />			
					<xsl:if	test="($t4 &gt; 0)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2625'" />
							<xsl:with-param name="errorMessage" 
								select="concat('Error en la linea: b) ', $fechaPago, ' - ', $cbcIssueDate, ' - ', $t3, ' - ', $t4)" />
						</xsl:call-template>
					</xsl:if>						
				</xsl:if>		
						
				<xsl:if test="(substring($cbcIssueDate, 1, 7)!=substring($fechaEmisionDocumentoRel, 1, 7))">	
					<xsl:if test="(substring($fechaPago, 1, 7)!=substring($cbcIssueDate, 1, 7))">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2625'" />
							<xsl:with-param name="errorMessage" 
								select="concat('Error en la linea: c) ', $fechaPago, ' - ', $cbcIssueDate)" />
						</xsl:call-template>
					</xsl:if>						
					
					<xsl:variable name="t5" select="xs:date($fechaPago)-xs:date($cbcIssueDate)" />
					<xsl:variable name="t6" select="fn:days-from-duration(xs:duration($t5))" />			
					<xsl:if	test="($t6 &gt; 0)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2625'" />
							<xsl:with-param name="errorMessage" 
								select="concat('Error en la linea: d) ', $fechaPago, ' - ', $cbcIssueDate, ' - ', $t5, ' - ', $t6)" />
						</xsl:call-template>
					</xsl:if>					
				</xsl:if>				
												
			</xsl:if>
			
			<xsl:if test="($tipoDocumentoRel!='07')">
				<!-- 42. Numero de pago -->
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2733'" />
					<xsl:with-param name="errorCodeValidate" select="'2734'" />
					<xsl:with-param name="node" select="$numeroPago" />
					<xsl:with-param name="regexp" select="'^[0-9]{1,9}$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea ', position(), ' ', $numeroPago)" />
				</xsl:call-template>
				
				<xsl:if
					test="count(key('by-document-SUNATRetention-reference', concat($numeroDocumentoRel, ' ',$numeroPago))) > 1">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2626'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea ', position(), ' ', $numeroDocumentoRel,' ', $numeroPago)" />
					</xsl:call-template>
				</xsl:if>
				
				<!-- 43. Importe de pago sin retención -->
				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'2735'" />
					<xsl:with-param name="errorCodeValidate" select="'2736'" />
					<xsl:with-param name="node" select="$importePagoSinRetencion" />
					<xsl:with-param name="regexp" select="'(?!(^0+(\.0+)?$))(^\d{1,12}(\.\d{1,2})?$)'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea ', position(), ' ', $importePagoSinRetencion)" />
				</xsl:call-template>
				
				<!-- 44. Moneda de pago -->
				<xsl:if test="$tipoMonedaImporteTotalDocumentoRel != $monedaImportePago">
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2622'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea ', position(), $tipoMonedaImporteTotalDocumentoRel,' - ', $monedaImportePago)" />
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
			
			<!-- Ini Datos de la Retencion -->
			<!-- 45. Importe retenido -->
			<xsl:if test="($importeRetenido)">	
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2740'" />
					<xsl:with-param name="node" select="$importeRetenido" />
					<xsl:with-param name="regexp" select="'(?!(^0+(\.0+)?$))(^\d{1,12}(\.\d{1,2})?$)'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea ', position(), ' ', $importeRetenido)" />
				</xsl:call-template>

				<xsl:if test="($tipoMonedaImporteTotalDocumentoRel='PEN')">						
					<xsl:variable name="montoPagoConsignado" select="($importePagoSinRetencion * $tasaRetencion) div 100" />
					<xsl:variable name="diffMontoPagado" select="$montoPagoConsignado - $importeRetenido" />
					<xsl:if test="($diffMontoPagado &lt; -1) or ($diffMontoPagado &gt; 1)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2623'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea 45a ', position(), ' ', $importePagoSinRetencion, ' ', $tasaRetencion, ' ', $montoPagoConsignado,' ', $importeRetenido, ' ', $diffMontoPagado)" />								
						</xsl:call-template>
					</xsl:if>
				</xsl:if>	
				
				<xsl:if test="($tipoMonedaImporteTotalDocumentoRel!='PEN')">						
					<xsl:variable name="montoPagoConsignado" select="(($importePagoSinRetencion * $tasaRetencion) div 100) * $importeTipoCambio" />
					<xsl:variable name="diffMontoPagado" select="$montoPagoConsignado - $importeRetenido" />
					<xsl:if test="($diffMontoPagado &lt; -1) or ($diffMontoPagado &gt; 1)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2623'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea 45b ', position(), ' ', $importePagoSinRetencion, ' ', $tasaRetencion, ' ', $importeTipoCambio, ' ', $montoPagoConsignado,' ', $importeRetenido,' ', $diffMontoPagado)" />								
						</xsl:call-template>
					</xsl:if>
				</xsl:if>						
			</xsl:if>
			
			<!-- 46. Moneda de importe retenido -->
			<xsl:if test="($monedaImporteRetenido)">	
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2742'" />	
					<xsl:with-param name="node" select="$monedaImporteRetenido" />
					<xsl:with-param name="regexp" select="'^(PEN)$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea ', position(), ' ', $monedaImporteRetenido)" />
				</xsl:call-template>
			</xsl:if>
				
			<xsl:if test="($importeTotalAPagar)">	
				<!-- 48. Importe total a pagar (neto) -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2746'" />
					<xsl:with-param name="node" select="$importeTotalAPagar" />
					<xsl:with-param name="regexp" select="'(?!(^0+(\.0+)?$))(^\d{1,12}(\.\d{1,2})?$)'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea ', position(), ' ', $importeTotalAPagar)" />
				</xsl:call-template>
				
				<xsl:if test="($tipoMonedaImporteTotalDocumentoRel='PEN')">						
					<xsl:variable name="montoXPagar" select="$importePagoSinRetencion - $importeRetenido" />
					<xsl:variable name="diffMontoPagado" select="$importeTotalAPagar - $montoXPagar" />
					<xsl:if test="($diffMontoPagado &lt; -1) or ($diffMontoPagado &gt; 1)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2623'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea 48a ', position(), ' ', $importePagoSinRetencion,' ', $importeRetenido, ' ', $montoXPagar, ' ', $importeTotalAPagar, ' ', $diffMontoPagado)" />								
						</xsl:call-template>
					</xsl:if>
				</xsl:if>	
				
				<xsl:if test="($tipoMonedaImporteTotalDocumentoRel!='PEN')">						
					<xsl:variable name="montoPagoConsignado" select="$importePagoSinRetencion * $importeTipoCambio" />
					<xsl:variable name="montoXPagar" select="$montoPagoConsignado - $importeRetenido" />
					<xsl:variable name="diffMontoPagado" select="$importeTotalAPagar - $montoXPagar" />
					<xsl:if test="($diffMontoPagado &lt; -1) or ($diffMontoPagado &gt; 1)">
						<xsl:call-template name="rejectCall">
							<xsl:with-param name="errorCode" select="'2623'" />
							<xsl:with-param name="errorMessage"
								select="concat('Error en la linea 48b ', position(), ' ', $importePagoSinRetencion, ' ', $importeTipoCambio, ' ', $montoPagoConsignado,' ', $importeRetenido,' ', $montoXPagar, ' ', $importeTotalAPagar, ' ', $diffMontoPagado)" />								
						</xsl:call-template>
					</xsl:if>
				</xsl:if>					
			</xsl:if>
			
			<xsl:if test="($monedaMontoNetoPagado)">	
				<!-- 49. Moneda del monto neto pagado -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2748'" />
					<xsl:with-param name="node" select="$monedaMontoNetoPagado" />
					<xsl:with-param name="regexp" select="'^(PEN)$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea ', position(), ' ', $monedaMontoNetoPagado)" />
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if test="($tipoDocumentoRel!='07')">					
				<xsl:if test="($tipoMonedaImporteTotalDocumentoRel!='PEN')">	
				<!-- 50. La moneda de referencia para el Tipo de Cambio -->				
					<xsl:call-template name="existValidateElementIfExist">
						<xsl:with-param name="errorCodeNotExist" select="'2719'"/>
						<xsl:with-param name="node" select="$monedaReferenciaTipoCambio"/>
						<xsl:with-param name="descripcion" 
							select="concat('Error en la linea: ', $monedaReferenciaTipoCambio)"/>
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
				
				<!-- 50. La moneda de referencia para el Tipo de Cambio -->		
				<xsl:if test="($monedaReferenciaTipoCambio!=$tipoMonedaImporteTotalDocumentoRel)">	
					<xsl:call-template name="rejectCall">
						<xsl:with-param name="errorCode" select="'2749'" />
						<xsl:with-param name="errorMessage"
							select="concat('Error en la linea ', position(), $monedaReferenciaTipoCambio, ' ', $tipoMonedaImporteTotalDocumentoRel)" />
					</xsl:call-template>
				</xsl:if>

				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2715'" />
					<xsl:with-param name="node" select="$monedaObjetivoTasaCambio" />
					<xsl:with-param name="regexp" select="'^(PEN)$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea ', position(), ' ', $monedaObjetivoTasaCambio)" />
				</xsl:call-template>
											
			</xsl:if>
		</xsl:for-each>		

		<!-- 32. Importe total Retenido, tiene que ser mayor que cero -->	
		<xsl:variable name="dif_Monto_TR" select="$importeTotalRetenido - $sumaImporteRetenido" />
		<xsl:if test="($dif_Monto_TR &lt; -0.001) or ($dif_Monto_TR &gt; 0.001)">									
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2628'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $dif_Monto_TR, ' ', $importeTotalRetenido, ' ', $sumaImporteRetenido)"/>
			</xsl:call-template>
		</xsl:if>
		
		<!-- 34. Importe total Pagado, tiene que ser mayor que cero -->	
		<xsl:variable name="dif_Monto_TP" select="$importeTotalPagado - $sumaImporteTotalAPagar" />
		<xsl:if test="($dif_Monto_TP &lt; -0.001) or ($dif_Monto_TP &gt; 0.001)">			
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2629'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: ', position(), ' ', $dif_Monto_TP, ' ', $importeTotalPagado, ' ', $sumaImporteTotalAPagar)"/>
			</xsl:call-template>
		</xsl:if>
		
	</xsl:template>
</xsl:stylesheet>