<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:regexp="http://exslt.org/regular-expressions"
	xmlns="urn:oasis:names:specification:ubl:schema:xsd:DespatchAdvice-2"
	xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
	xmlns:ext="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2"
	xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
	xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
	xmlns:dp="http://www.datapower.com/extensions" 
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:date="http://exslt.org/dates-and-times"
	extension-element-prefixes="dp" exclude-result-prefixes="dp">

	<xsl:include href="../util/validate_utils.xsl" dp:ignore-multiple="yes" />

	<!-- key Numero de lineas duplicados fin -->
	<xsl:key name="by-despatchLine-id" match="*[local-name()='DespatchAdvice']/cac:DespatchLine" use="number(cbc:ID)" />

	<!-- Parameter -->
	<xsl:param name="nombreArchivoEnviado" />

	<xsl:template match="/*">
		<!-- Ini validacion del nombre del archivo vs el nombre del cbc:ID -->
		<xsl:variable name="numeroRuc" select="substring($nombreArchivoEnviado, 1, 11)" />
		<xsl:variable name="numeroSerie" select="substring($nombreArchivoEnviado, 16, 4)" />
		<xsl:variable name="numeroComprobante" select="substring($nombreArchivoEnviado, 21, string-length($nombreArchivoEnviado) - 24)" />	
		<!-- Fin validacion del nombre del archivo vs el nombre del cbc:ID -->

		<!-- Variables -->
		<!-- Datos de guía de remisión -->

		<!-- 1. Versión del UBL -->
		<xsl:variable name="cbcUBLVersionID" select="cbc:UBLVersionID" />
		<!-- 2. Versión de la estructura del documento -->
		<xsl:variable name="cbcCustomizationID" select="cbc:CustomizationID" />
		<!-- 3. Numeración, conformada por serie y número correlativo -->
		<xsl:variable name="cbcID" select="cbc:ID" />
		<!-- 4. Fecha de emisión -->
		<xsl:variable name="cbcIssueDate" select="cbc:IssueDate" />	
		<xsl:variable name="currentdate" select="fn:current-date()"></xsl:variable>	
		<!-- 6. Tipo de documento (Guia) -->
		<xsl:variable name="tipoDocumentoGuia" select="cbc:DespatchAdviceTypeCode" />
		<!-- II. Guía de Remisión de referencia (dada de baja por cambio de destinatario) -->
		<xsl:variable name="cacOrderReference" select="cac:OrderReference" />

		<!-- III. Numero de DAM (obligatorio cuando el motivo de traslado es importacion) -->
		<!-- IV. Documento Relacionado (Numeración de manifiesto de carga) -->
		<!-- V. Documento Relacionado (Número de Orden de entrega, Número de SCOP, 
			numeración de detracción u OTROS) -->
		<!-- -->
		<xsl:variable name="cacAdditionalDocumentReference" select="cac:AdditionalDocumentReference" />

		<!-- VII. Datos del Remitente -->
		<!-- 18. Numero de documento de identidad del remitente -->
		<xsl:variable name="numeroDocumentoIdentidadRemitente" select="cac:DespatchSupplierParty/cbc:CustomerAssignedAccountID" />
		<!-- 19. Tipo de documento de identidad del remitente -->
		<xsl:variable name="tipoDocumentoIdentidadRemitente" select="cac:DespatchSupplierParty/cbc:CustomerAssignedAccountID/@schemeID" />
		<!-- 20. Apellidos y nombres, denominacion o razon social del remitente -->
		<xsl:variable name="RazonSocialRemitente" select="cac:DespatchSupplierParty/cac:Party/cac:PartyLegalEntity/cbc:RegistrationName" />

		<!-- VIII. Datos del Destinatario -->
		<!-- 21. Numero de documento de identidad del Destinatario -->
		<xsl:variable name="numeroDocumentoIdentidadDestinatario" select="cac:DeliveryCustomerParty/cbc:CustomerAssignedAccountID" />
		<!-- 22. Tipo de documento de identidad del Destinatario -->
		<xsl:variable name="tipoDocumentoIdentidadDestinatario" select="cac:DeliveryCustomerParty/cbc:CustomerAssignedAccountID/@schemeID" />
		<!-- 23. Apellidos y nombres, denominacion o razon social del Destinatario -->
		<xsl:variable name="RazonSocialDestinatario" select="cac:DeliveryCustomerParty/cac:Party/cac:PartyLegalEntity/cbc:RegistrationName" />

		<!-- IX. Datos del Proveedor (cuando se ingrese) -->
		<!-- 24. Numero de documento de identidad del Proveedor -->
		<xsl:variable name="numeroDocumentoIdentidadProveedor" select="cac:SellerSupplierParty/cbc:CustomerAssignedAccountID" />
		<!-- 25. Tipo de documento de identidad del Proveedor -->
		<xsl:variable name="tipoDocumentoIdentidadProveedor" select="cac:SellerSupplierParty/cbc:CustomerAssignedAccountID/@schemeID" />
		<!-- 26. Apellidos y nombres, denominacion o razon social del Proveedor -->
		<xsl:variable name="RazonSocialProveedor" select="cac:SellerSupplierParty/cac:Party/cac:PartyLegalEntity/cbc:RegistrationName" />

		<!-- X. Datos del envío -->
		<!-- 27. Motivo del traslado -->
		<xsl:variable name="motivoTraslado" select="cac:Shipment/cbc:HandlingCode" />
		<!-- 28. Descripción de motivo de traslado -->
		<xsl:variable name="descripcionMotivoTraslado" select="cac:Shipment/cbc:Information" />
		<!-- 29. Indicador de Transbordo Programado -->
		<xsl:variable name="indicadorTransbordoProgramado" select="cac:Shipment/cbc:SplitConsignmentIndicator" />
		<!-- 30. Peso bruto total de los guía -->
		<xsl:variable name="pesoBrutoTotalGuia" select="cac:Shipment/cbc:GrossWeightMeasure" />
		<!-- 31. Unidad de medida del peso bruto -->
		<xsl:variable name="unidadMedidaPesoBruto" select="cac:Shipment/cbc:GrossWeightMeasure/@unitCode" />
		<!-- 32. Numero de Bulltos o Pallets -->
		<xsl:variable name="numeroBulltosPallets" select="cac:Shipment/cbc:TotalTransportHandlingUnitQuantity" />
		<!-- 33. Modalidad de Traslado -->
		<xsl:variable name="modalidadTraslado" select="cac:Shipment/cac:ShipmentStage/cbc:TransportModeCode" />
		<!-- 34. Fecha Inicio de traslado -->
		<xsl:variable name="fechaInicioTraslado" select="cac:Shipment/cac:ShipmentStage/cac:TransitPeriod/cbc:StartDate" />
		<!-- 36. Numero de RUC transportista -->
		<xsl:variable name="numeroRUCTransportista" select="cac:Shipment/cac:ShipmentStage/cac:CarrierParty/cac:PartyIdentification/cbc:ID" />
		<!-- 39. Numero de placa del vehiculo -->
		<xsl:variable name="numeroPlacaVehiculo" select="cac:Shipment/cac:ShipmentStage/cac:TransportMeans/cac:RoadTransport/cbc:LicensePlateID" />
		<!-- 41. Numero de documento de identidad del conductor -->
		<xsl:variable name="numeroDocumentoIdentidadConductor" select="cac:Shipment/cac:ShipmentStage/cac:DriverPerson/cbc:ID" />

		<!-- XV. Direccion punto de llegada -->
		<!-- -->
		<xsl:variable name="cacShipmentDeliveryAddress" select="cac:Shipment/cac:Delivery/cac:DeliveryAddress" />

		<!-- XVII. Direccion del punto de partida -->
		<!-- -->
		<xsl:variable name="cacShipmentOriginAddress" select="cac:Shipment/cac:OriginAddress" />

		<!-- XIX. BIENES A TRANSPORTAR -->
		<!-- -->
		<xsl:variable name="cacDespatchLine" select="cac:DespatchLine" />

		<!-- Datos de guía de remisión -->

		<!-- 1. Versión del UBL -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2111'" />
			<xsl:with-param name="errorCodeValidate" select="'2110'" />
			<xsl:with-param name="node" select="$cbcUBLVersionID" />
			<xsl:with-param name="regexp" select="'^(2.1)$'" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $cbcUBLVersionID)" />
		</xsl:call-template>

		<!-- 2. Versión de la estructura del documento -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2113'" />
			<xsl:with-param name="errorCodeValidate" select="'2112'" />
			<xsl:with-param name="node" select="$cbcCustomizationID" />
			<xsl:with-param name="regexp" select="'^(1.0)$'" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $cbcCustomizationID)" />
		</xsl:call-template>

		<!-- 3. Numeración, conformada por serie y número correlativo -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'1001'" />
			<xsl:with-param name="errorCodeValidate" select="'1001'" />
			<xsl:with-param name="node" select="$cbcID" />
			<xsl:with-param name="regexp" select="'^[T][A-Z0-9]{3}-[0-9]{1,8}?$'" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $cbcID)" />
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
							select="concat('Error en la linea 2) ', $t4, ' ', $t6, ' ', $t8, ' ', $t10, ' : ', $s1C, ' - ', $cbcIssueDate, ' - ', $currentdate, ' - ', $t1, ' - ', $t2)" />
						</xsl:call-template>
				</xsl:if>								
			</xsl:otherwise>
		</xsl:choose>			
					
		<!-- 6. Tipo de documento (Guia) -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'1050'" />
			<xsl:with-param name="errorCodeValidate" select="'1051'" />
			<xsl:with-param name="node" select="$tipoDocumentoGuia" />
			<xsl:with-param name="regexp" select="'^(09)$'" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $tipoDocumentoGuia)" />
		</xsl:call-template>

		<!-- 7. Observaciones (Texto) -->
		<xsl:for-each select="cbc:Note">
			<!-- 7. Observaciones (Texto) -->
			<xsl:variable name="observaciones" select="." />
			<xsl:call-template name="existValidateElementIfExistNULL">
				<xsl:with-param name="errorCodeNotExist" select="'4186'" />
				<xsl:with-param name="node" select="$observaciones" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea 1)  ', $observaciones)" />
			</xsl:call-template>
	
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4186'" />
				<xsl:with-param name="node" select="$observaciones" />
<!-- 				<xsl:with-param name="regexp" select="'^(.{0,250})$'" /> -->
				<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,249}$'"/>
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea 2) ', $observaciones)" />
			</xsl:call-template>
		</xsl:for-each>
		
		<!-- II. Guía de Remisión de referencia (dada de baja por cambio de destinatario) -->
		<xsl:if test="$cacOrderReference">
			<xsl:if test="count($cacOrderReference)>1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2753'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', count($cacOrderReference))" />
				</xsl:call-template>
			</xsl:if>

			<xsl:for-each select="$cacOrderReference">
				<!-- 8. Serie y Numero de documento -->
				<xsl:variable name="serieNumeroDocumentoGuia" select="./cbc:ID" />
				<!-- 9. Código del tipo de documento -->
				<xsl:variable name="codigoTipoDocumentoGuia" select="./cbc:OrderTypeCode" />
				<!-- 10. Tipo de documento (Descripción) -->
				<xsl:variable name="tipoDocumentoDescripcionGuia"
					select="./cbc:OrderTypeCode/@name" />

				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'1055'" />
					<xsl:with-param name="node" select="$serieNumeroDocumentoGuia" />
					<xsl:with-param name="regexp"
						select="'^[T][A-Z0-9]{3}-[0-9]{1,8}$|(EG01)-[0-9]{1,8}$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', $serieNumeroDocumentoGuia)" />
				</xsl:call-template>

				<xsl:call-template name="existAndRegexpValidateElement">
					<xsl:with-param name="errorCodeNotExist" select="'1056'" />
					<xsl:with-param name="errorCodeValidate" select="'2755'" />
					<xsl:with-param name="node" select="$codigoTipoDocumentoGuia" />
					<xsl:with-param name="regexp" select="'^(09)$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', $codigoTipoDocumentoGuia)" />
				</xsl:call-template>

				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'4187'" />
					<xsl:with-param name="node" select="$tipoDocumentoDescripcionGuia" />
					<xsl:with-param name="regexp" select="'^[\w\d].{1,50}$'" />
					<xsl:with-param name="isError" select="false()" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', $tipoDocumentoDescripcionGuia)" />
				</xsl:call-template>
			</xsl:for-each>
		</xsl:if>

		<!-- III. Numero de DAM (obligatorio cuando el motivo de traslado es importacion) -->
		<!-- IV. Documento Relacionado (Numeración de manifiesto de carga) -->
		<!-- V. Documento Relacionado (Número de Orden de entrega, Número de SCOP, 
			numeración de detracción u OTROS) -->
		<xsl:variable name="codigoTipoDocumento" select="$cacOrderReference/cbc:OrderTypeCode" />
		<xsl:for-each select="$cacAdditionalDocumentReference">
			<!-- 11. Numero de documento -->
			<!-- 13. Numero de documento -->
			<!-- 15. Numero de documento -->
			<xsl:variable name="numeroDocumentoRelacionado" select="./cbc:ID" />
			<!-- 12. Código del tipo de documento relacionado -->
			<xsl:variable name="codigoTipoDocumentoRelacionado"
				select="./cbc:DocumentTypeCode" />

			<!-- 11. Numero de documento -->
			<!-- codigoTipoDocumentoRelacionado -->
			<xsl:if test="$codigoTipoDocumentoRelacionado='01'">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2769'" />
					<xsl:with-param name="node" select="$numeroDocumentoRelacionado" />
					<xsl:with-param name="regexp"
						select="'^[0-9]{4}-[0-9]{2}-[0-9]{3}-[0-9]{6}$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', $codigoTipoDocumentoRelacionado, ' ', $numeroDocumentoRelacionado)" />
				</xsl:call-template>

				<xsl:if
					test="($motivoTraslado) and not($motivoTraslado='08' or $motivoTraslado='09')">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4191'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', ' ', $codigoTipoDocumentoRelacionado, ' ', $motivoTraslado)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>

			<!-- 12. Código del tipo de documento relacionado -->
			<xsl:call-template name="existValidateElementIfExist">
				<xsl:with-param name="errorCodeNotExist" select="'1058'" />
				<xsl:with-param name="node"
					select="$codigoTipoDocumentoRelacionado" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $codigoTipoDocumentoRelacionado)" />
			</xsl:call-template>

			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2755'" />
				<xsl:with-param name="node"
					select="$codigoTipoDocumentoRelacionado" />
				<xsl:with-param name="regexp" select="'^(01|02|03|04|05|06)$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $codigoTipoDocumentoRelacionado)" />
			</xsl:call-template>

			<!-- 13. Numero de documento -->
			<!-- codigoTipoDocumentoRelacionado -->
			<xsl:if test="$codigoTipoDocumentoRelacionado='04'">
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'1057'" />
					<xsl:with-param name="node" select="$numeroDocumentoRelacionado" />
					<xsl:with-param name="regexp"
						select="'^[0-9]{3}-[0-9]{4}-[0-9]{4}$'" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', $codigoTipoDocumentoRelacionado, ' ', $numeroDocumentoRelacionado)" />
				</xsl:call-template>

				<xsl:if
					test="($motivoTraslado) and not($motivoTraslado='08' or $motivoTraslado='09')">
					<xsl:call-template name="addWarning">
						<xsl:with-param name="warningCode" select="'4192'" />
						<xsl:with-param name="warningMessage"
							select="concat('Error en la linea: ', ' ', $codigoTipoDocumentoRelacionado, ' ', $motivoTraslado)" />
					</xsl:call-template>
				</xsl:if>
			</xsl:if>

			<!-- 15. Numero de documento -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2756'" />
				<xsl:with-param name="node" select="$numeroDocumentoRelacionado" />
				<xsl:with-param name="regexp" select="'^.{1,20}$'" />
				<!-- <xsl:with-param name="regexp" select="'^[\w].{1,20}$'" /> -->
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $numeroDocumentoRelacionado)" />
			</xsl:call-template>
		</xsl:for-each>

		<!-- VII. Datos del Remitente -->
		<!-- 18. Numero de documento de identidad del remitente -->
		<xsl:if test="$numeroRuc != $numeroDocumentoIdentidadRemitente">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'1034'" />
				<xsl:with-param name="errorMessage"
					select="concat('ruc del xml diferente al nombre del archivo ', $numeroRuc, ' diff ', $numeroDocumentoIdentidadRemitente)" />
			</xsl:call-template>
		</xsl:if>

		<!-- 19. Tipo de documento de identidad del remitente -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2678'" />
			<xsl:with-param name="errorCodeValidate" select="'2511'" />
			<xsl:with-param name="node"
				select="$tipoDocumentoIdentidadRemitente" />
			<xsl:with-param name="regexp" select="'^(6)$'" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $tipoDocumentoIdentidadRemitente)" />
		</xsl:call-template>

		<!-- 20. Apellidos y nombres, denominacion o razon social del remitente -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'1037'" />
			<xsl:with-param name="errorCodeValidate" select="'1038'" />
			<xsl:with-param name="node" select="$RazonSocialRemitente" />
			<xsl:with-param name="regexp" select="'^(.{1,100})$'" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $RazonSocialRemitente)" />
		</xsl:call-template>

		<!-- VIII. Datos del Destinatario -->
		<!-- 21. Numero de documento de identidad del Destinatario -->
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'2757'" />
			<xsl:with-param name="node"
				select="$numeroDocumentoIdentidadDestinatario" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $numeroDocumentoIdentidadDestinatario)" />
		</xsl:call-template>

		<xsl:if
			test="($tipoDocumentoIdentidadDestinatario = '0') or ($tipoDocumentoIdentidadDestinatario = 'A')">
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2758'" />
				<xsl:with-param name="node"
					select="$numeroDocumentoIdentidadDestinatario" />
				<xsl:with-param name="regexp" select="'^[a-zA-Z0-9]{1,15}$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $numeroDocumentoIdentidadDestinatario)" />
			</xsl:call-template>
		</xsl:if>

		<xsl:if test="$tipoDocumentoIdentidadDestinatario = '1'">
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4207'" />
				<xsl:with-param name="node"
					select="$numeroDocumentoIdentidadDestinatario" />
				<xsl:with-param name="regexp" select="'^[0-9]{8}$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $numeroDocumentoIdentidadDestinatario)" />
			</xsl:call-template>
		</xsl:if>

		<xsl:if
			test="($tipoDocumentoIdentidadDestinatario = '4') or ($tipoDocumentoIdentidadDestinatario = '7')">
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4208'" />
				<xsl:with-param name="node"
					select="$numeroDocumentoIdentidadDestinatario" />
				<xsl:with-param name="regexp" select="'^[a-zA-Z0-9]{1,12}$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $numeroDocumentoIdentidadDestinatario)" />
			</xsl:call-template>
		</xsl:if>

		<xsl:if test="$tipoDocumentoIdentidadDestinatario = '6'">
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2017'" />
				<xsl:with-param name="node"
					select="$numeroDocumentoIdentidadDestinatario" />
				<xsl:with-param name="regexp" select="'^[0-9]{11}$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $numeroDocumentoIdentidadDestinatario)" />
			</xsl:call-template>
		</xsl:if>

		<xsl:if
			test="$motivoTraslado[text() = '18' or text() = '04' or text() = '4' or text() = '02' or text() = '2']">
			<xsl:if
				test="$numeroDocumentoIdentidadDestinatario != $numeroDocumentoIdentidadRemitente">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2554'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $numeroDocumentoIdentidadDestinatario, ' ', $numeroDocumentoIdentidadRemitente)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:if>

		<xsl:if
			test="$motivoTraslado[text() = '01' or text() = '1' or text() = '19' or text() = '09' or text() = '9']">
			<xsl:if
				test="$numeroDocumentoIdentidadDestinatario = $numeroDocumentoIdentidadRemitente">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2555'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $numeroDocumentoIdentidadDestinatario, ' ', $numeroDocumentoIdentidadRemitente)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:if>

		<!-- 22. Tipo de documento de identidad del Destinatario -->
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'2759'" />
			<xsl:with-param name="node" select="$tipoDocumentoIdentidadDestinatario" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $tipoDocumentoIdentidadDestinatario)" />
		</xsl:call-template>
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2760'" />
			<xsl:with-param name="node" select="$tipoDocumentoIdentidadDestinatario" />
			<xsl:with-param name="regexp" select="'^(0|1|4|6|7|A|B|C|D|E|F|G)$'" />
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $tipoDocumentoIdentidadDestinatario)"/>
		</xsl:call-template>		

		<!-- 23. Apellidos y nombres, denominacion o razon social del Destinatario -->
		<xsl:call-template name="existAndRegexpValidateElement">
			<xsl:with-param name="errorCodeNotExist" select="'2761'" />
			<xsl:with-param name="errorCodeValidate" select="'2762'" />
			<xsl:with-param name="node" select="$RazonSocialDestinatario" />
			<xsl:with-param name="regexp" select="'^(.{1,100})$'" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $RazonSocialDestinatario)" />
		</xsl:call-template>
		
		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'2762'" />
			<xsl:with-param name="node" select="$RazonSocialDestinatario" />
			<xsl:with-param name="regexp" select="'^[^\n\t\r\f]{1,}$'"/>
			<xsl:with-param name="descripcion" 
				select="concat('Error en la linea: ', $RazonSocialDestinatario)" />
		</xsl:call-template>			

		<!-- IX. Datos del Proveedor (cuando se ingrese) -->
		<xsl:if test="cac:SellerSupplierParty">
			<!-- 24. Numero de documento de identidad del Proveedor -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2764'" />
				<xsl:with-param name="node"
					select="$numeroDocumentoIdentidadProveedor" />
				<xsl:with-param name="regexp" select="'^[1-2][0-9]{10}$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $numeroDocumentoIdentidadProveedor)" />
			</xsl:call-template>

			<xsl:if
				test="$numeroDocumentoIdentidadProveedor = $numeroDocumentoIdentidadRemitente or $numeroDocumentoIdentidadProveedor = $numeroDocumentoIdentidadDestinatario">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'4053'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $numeroDocumentoIdentidadProveedor, ' ', $numeroDocumentoIdentidadRemitente, ' ', $numeroDocumentoIdentidadDestinatario)" />
				</xsl:call-template>
			</xsl:if>

			<!-- 25. Tipo de documento de identidad del Proveedor -->
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2765'" />
				<xsl:with-param name="errorCodeValidate" select="'2766'" />
				<xsl:with-param name="node" select="$tipoDocumentoIdentidadProveedor" />
				<xsl:with-param name="regexp" select="'^(6)$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $tipoDocumentoIdentidadProveedor)" />
			</xsl:call-template>

			<!-- 26. Apellidos y nombres, denominacion o razon social del Proveedor -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4189'" />
				<xsl:with-param name="node" select="$RazonSocialProveedor" />
				<xsl:with-param name="regexp" select="'^(.{1,100})$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $RazonSocialProveedor)" />
			</xsl:call-template>
		</xsl:if>

		<!-- X. Datos del envío -->
		<!-- 27. Motivo del traslado -->
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'1062'" />
			<xsl:with-param name="node" select="$motivoTraslado" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $motivoTraslado)" />
		</xsl:call-template>
		
		<!-- 27. Motivo del traslado -->
		<xsl:if test="$motivoTraslado[text() = '09']">
			<xsl:if
				test="count($cacAdditionalDocumentReference/cbc:DocumentTypeCode[text()='01']) = 0">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2767'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $motivoTraslado)" />
				</xsl:call-template>
			</xsl:if>
		</xsl:if>

		<xsl:if test="$motivoTraslado[text() = '08']">
			<xsl:if
				test="(count($cacAdditionalDocumentReference/cbc:DocumentTypeCode[text()='04']) = 0) and
					(count($cacAdditionalDocumentReference/cbc:DocumentTypeCode[text()='01']) = 0)">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2768'" />
					<xsl:with-param name="errorMessage"
						select="concat('Error en la linea: ', $motivoTraslado)" />
				</xsl:call-template>
			</xsl:if>

			<!-- 32. Numero de Bulltos o Pallets -->
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2771'" />
				<xsl:with-param name="errorCodeValidate" select="'2772'" />
				<xsl:with-param name="node" select="$numeroBulltosPallets" />
				<xsl:with-param name="regexp" select="'^([0-9]{1,12})$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $numeroBulltosPallets)" />
			</xsl:call-template>
		</xsl:if>

		<!-- 28. Descripción de motivo de traslado -->
		<xsl:if test="$motivoTraslado[text() = '13']">
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'4055'" />
				<xsl:with-param name="errorCodeValidate" select="'4190'" />
				<xsl:with-param name="node" select="$descripcionMotivoTraslado" />
				<xsl:with-param name="regexp" select="'^(.{1,100})$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $descripcionMotivoTraslado)" />
			</xsl:call-template>
		</xsl:if>

		<!-- 30. Peso bruto total de los guía -->
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'2880'" />
			<xsl:with-param name="node" select="$pesoBrutoTotalGuia" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $pesoBrutoTotalGuia)" />
		</xsl:call-template>

		<xsl:call-template name="regexpValidateElementIfExist">
			<xsl:with-param name="errorCodeValidate" select="'4155'" />
			<xsl:with-param name="node" select="$pesoBrutoTotalGuia" />
			<xsl:with-param name="regexp"
				select="'(?!(^0+(\.0+)?$))(^\d{1,12}(\.\d{1,3})?$)'" />
			<xsl:with-param name="isError" select="false()" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $pesoBrutoTotalGuia)" />
		</xsl:call-template>

		<!-- 31. Unidad de medida del peso bruto -->
		<xsl:if test="$pesoBrutoTotalGuia">
			<xsl:call-template name="existValidateElementIfExist">
				<xsl:with-param name="errorCodeNotExist" select="'2881'" />
				<xsl:with-param name="node" select="$unidadMedidaPesoBruto" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $unidadMedidaPesoBruto)" />
			</xsl:call-template>

			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'4154'" />
				<xsl:with-param name="node" select="$unidadMedidaPesoBruto" />
				<xsl:with-param name="regexp" select="'^(KGM)$'" />
				<xsl:with-param name="isError" select="false()" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $unidadMedidaPesoBruto)" />
			</xsl:call-template>
		</xsl:if>

		<!-- 32. Numero de Bulltos o Pallets -->
		<xsl:if test="($motivoTraslado[text()!='08']) and ($numeroBulltosPallets)">
			<xsl:call-template name="addWarning">
				<xsl:with-param name="warningCode" select="'4195'" />
				<xsl:with-param name="warningMessage"
					select="concat('Error en la linea: ', $motivoTraslado, ' ', $numeroBulltosPallets)" />
			</xsl:call-template>
		</xsl:if>

		<!-- 33. Modalidad de Traslado -->
		<xsl:call-template name="existValidateElementIfNotExist">
			<xsl:with-param name="errorCodeNotExist" select="'1065'" />
			<xsl:with-param name="node" select="$modalidadTraslado" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $modalidadTraslado)" />
		</xsl:call-template>

		<xsl:if test="($modalidadTraslado[text() = '01'])">			
<!-- 			<xsl:if test="(not(string($numeroPlacaVehiculo)) and string($numeroDocumentoIdentidadConductor)) or -->
<!-- 				(string($numeroPlacaVehiculo) and not(string($numeroDocumentoIdentidadConductor)))"> -->
<!-- 				<xsl:call-template name="rejectCall"> -->
<!-- 					<xsl:with-param name="errorCode" select="'2774'" /> -->
<!-- 					<xsl:with-param name="errorMessage" -->
<!-- 						select="concat('Error en la linea: ', $modalidadTraslado, ' ', $numeroPlacaVehiculo, ' ', $numeroDocumentoIdentidadConductor)" /> -->
<!-- 				</xsl:call-template> -->
<!-- 			</xsl:if> -->

			<xsl:call-template name="existValidateElementIfNotExist">
				<xsl:with-param name="errorCodeNotExist" select="'1066'" />
				<xsl:with-param name="node" select="$numeroRUCTransportista" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $modalidadTraslado, ' ', $numeroRUCTransportista)" />
			</xsl:call-template>
		</xsl:if>

		<xsl:if test="$modalidadTraslado[text() = '02']">
			<xsl:call-template name="existValidateElementIfNotExist">
				<xsl:with-param name="errorCodeNotExist" select="'1067'" />
				<xsl:with-param name="node" select="$numeroPlacaVehiculo" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $modalidadTraslado, ' ', $numeroPlacaVehiculo)" />
			</xsl:call-template>

			<xsl:call-template name="existValidateElementIfNotExist">
				<xsl:with-param name="errorCodeNotExist" select="'1068'" />
				<xsl:with-param name="node" select="$numeroDocumentoIdentidadConductor" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $modalidadTraslado, ' ', $numeroDocumentoIdentidadConductor)" />
			</xsl:call-template>

			<xsl:call-template name="existValidateElementExist">
				<xsl:with-param name="errorCodeNotExist" select="'4159'" />
				<xsl:with-param name="node" select="$numeroRUCTransportista" />
				<xsl:with-param name="isError" select="false()"/>
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $modalidadTraslado, ' ', $numeroRUCTransportista)" />
			</xsl:call-template>
		</xsl:if>

		<!-- 34. Fecha Inicio de traslado -->
		<xsl:call-template name="existValidateElementIfExist">
			<xsl:with-param name="errorCodeNotExist" select="'1069'" />
			<xsl:with-param name="node" select="$fechaInicioTraslado" />
			<xsl:with-param name="descripcion"
				select="concat('Error en la linea: ', $fechaInicioTraslado)" />
		</xsl:call-template>

		<!-- XV. Direccion punto de llegada -->
		<xsl:variable name="countUbigeoLlegada"
			select="count($cacShipmentDeliveryAddress/cbc:ID)" />
		<xsl:if test="($countUbigeoLlegada=0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2775'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: 43a ', $countUbigeoLlegada)" />
			</xsl:call-template>
		</xsl:if>

		<xsl:for-each select="$cacShipmentDeliveryAddress">
			<!-- 43. Ubigeo de llegada -->
			<xsl:variable name="ubigeoLlegada" select="./cbc:ID" />
			<!-- 44. Direccion completa y detallada de llegada -->
			<xsl:variable name="direccionCompletaDetalladaLlegada"
				select="./cbc:StreetName" />

			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2775'" />
				<xsl:with-param name="errorCodeValidate" select="'2776'" />
				<xsl:with-param name="node" select="$ubigeoLlegada" />
				<xsl:with-param name="regexp" select="'^[0-9]{6}$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 43b ', $ubigeoLlegada)" />
			</xsl:call-template>

			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2777'" />
				<xsl:with-param name="errorCodeValidate" select="'2778'" />
				<xsl:with-param name="node" select="$direccionCompletaDetalladaLlegada" />
				<xsl:with-param name="regexp" select="'^(.{0,100})$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 44 ', $direccionCompletaDetalladaLlegada)" />
			</xsl:call-template>
		</xsl:for-each>

		<!-- XVII. Direccion del punto de partida -->
		<xsl:variable name="countUbigeoPartida" select="count($cacShipmentOriginAddress/cbc:ID)" />
		<xsl:if test="($countUbigeoPartida=0)">
			<xsl:call-template name="rejectCall">
				<xsl:with-param name="errorCode" select="'2775'" />
				<xsl:with-param name="errorMessage"
					select="concat('Error en la linea: 46a ', $countUbigeoPartida)" />
			</xsl:call-template>
		</xsl:if>

		<xsl:for-each select="$cacShipmentOriginAddress">
			<!-- 46. Ubigeo de llegada -->
			<xsl:variable name="ubigeoPartida" select="./cbc:ID" />
			<!-- 47. Direccion completa y detallada de llegada -->
			<xsl:variable name="direccionCompletaDetalladaPartida"
				select="./cbc:StreetName" />

			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2775'" />
				<xsl:with-param name="errorCodeValidate" select="'2776'" />
				<xsl:with-param name="node" select="$ubigeoPartida" />
				<xsl:with-param name="regexp" select="'^[0-9]{6}$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 46b ', $ubigeoPartida)" />
			</xsl:call-template>
							
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2777'" />
				<xsl:with-param name="errorCodeValidate" select="'2778'" />
				<xsl:with-param name="node" select="$direccionCompletaDetalladaPartida" />
				<xsl:with-param name="regexp" select="'^(.{0,100})$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: 47a ', $direccionCompletaDetalladaPartida)" />
			</xsl:call-template>
			
<!-- 			<xsl:call-template name="regexpValidateElementIfExist"> -->
<!-- 				<xsl:with-param name="errorCodeValidate" select="'2778'"/> -->
<!-- 				<xsl:with-param name="node" select="$direccionCompletaDetalladaPartida"/> -->
<!-- 				<xsl:with-param name="regexp" select="'^\s*(?:\S\s*){0,3}$'"/> -->
<!-- 				<xsl:with-param name="descripcion"  -->
<!-- 					select="concat('Error en la linea: 47b ',$direccionCompletaDetalladaPartida)"/> -->
<!-- 			</xsl:call-template>			 -->
			
		</xsl:for-each>

		<xsl:for-each select="$cacDespatchLine">
			<!-- 49. Numero de orden del item -->
			<xsl:variable name="numeroItem" select="./cbc:ID" />
			<!-- 50. Cantidad del item -->
			<xsl:variable name="cantidadItem" select="./cbc:DeliveredQuantity" />

			<!-- 49. Numero de orden del item -->
			<xsl:call-template name="regexpValidateElementIfExist">
				<xsl:with-param name="errorCodeValidate" select="'2023'" />
				<xsl:with-param name="node" select="$numeroItem" />
				<xsl:with-param name="regexp" select="'^[0-9]{1,3}?$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $numeroItem)" />
			</xsl:call-template>

			<xsl:if test="count(key('by-despatchLine-id', number($numeroItem))) > 1">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'2752'" />
					<xsl:with-param name="errorMessage"
						select="concat('El numero de item esta duplicado: ', $numeroItem)" />
				</xsl:call-template>
			</xsl:if>

			<!-- 50. Cantidad del item -->
			<xsl:call-template name="existAndRegexpValidateElement">
				<xsl:with-param name="errorCodeNotExist" select="'2779'" />
				<xsl:with-param name="errorCodeValidate" select="'2780'" />
				<xsl:with-param name="node" select="$cantidadItem" />
				<xsl:with-param name="regexp" select="'^[0-9]{1,12}(\.[0-9]{1,10})?$'" />
				<xsl:with-param name="descripcion"
					select="concat('Error en la linea: ', $cantidadItem)" />
			</xsl:call-template>

			<xsl:for-each select="./cac:Item">
				<!-- 52. Descripcion detallada del ítem -->
				<xsl:variable name="descripcionDetalladaItem" select="./cbc:Name" />
				<!-- 53. Codigo del item -->
				<xsl:variable name="codigoItem" select="./cac:SellersItemIdentification/cbc:ID" />

				<!-- 52. Descripcion detallada del ítem -->
				<xsl:call-template name="existValidateElementIfExist">
					<xsl:with-param name="errorCodeNotExist" select="'2781'" />
					<xsl:with-param name="node" select="$descripcionDetalladaItem" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', $descripcionDetalladaItem)" />
				</xsl:call-template>

				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2782'" />
					<xsl:with-param name="node" select="$descripcionDetalladaItem" />
<!-- 					<xsl:with-param name="regexp" select="'^[^\n].{0,249}$'" /> -->
					<xsl:with-param name="regexp" select="'^(?!\s*$)[^\s].{0,249}$'"/>
					<xsl:with-param name="isError" select="false()" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', $descripcionDetalladaItem)" />
				</xsl:call-template>

				<!-- 53. Codigo del item -->
				<xsl:call-template name="regexpValidateElementIfExist">
					<xsl:with-param name="errorCodeValidate" select="'2783'" />
					<xsl:with-param name="node" select="$codigoItem" />
					<xsl:with-param name="regexp" select="'^[\w].{0,15}$'" />
					<xsl:with-param name="isError" select="false()" />
					<xsl:with-param name="descripcion"
						select="concat('Error en la linea: ', $codigoItem)" />
				</xsl:call-template>
			</xsl:for-each>
		</xsl:for-each>

		<xsl:copy-of select="." />

	</xsl:template>

</xsl:stylesheet>
