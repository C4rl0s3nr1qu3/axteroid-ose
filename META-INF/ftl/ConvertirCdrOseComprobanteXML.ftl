<?xml version="1.0" encoding="UTF-8" standalone="yes"?><ApplicationResponse xmlns="urn:oasis:names:specification:ubl:schema:xsd:ApplicationResponse-2" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns:ext="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2" xmlns:ns5="urn:oasis:names:specification:ubl:schema:xsd:Invoice-2" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
    <ext:UBLExtensions>
	</ext:UBLExtensions>
    <cbc:UBLVersionID>2.1</cbc:UBLVersionID>
    <cbc:CustomizationID>1.0</cbc:CustomizationID>
    <cbc:ID>${autorización_Comprobante}</cbc:ID>
    <cbc:IssueDate>${fecha_recepcion_comprobante}</cbc:IssueDate>
    <cbc:IssueTime>${hora_recepcion_comprobante}</cbc:IssueTime>
    <cbc:ResponseDate>${fecha_comprobacion_comprobante}</cbc:ResponseDate>
    <cbc:ResponseTime>${hora_comprobacion_comprobante}</cbc:ResponseTime>
    <cac:SenderParty>
        <cac:PartyLegalEntity>
            <cbc:CompanyID schemeAgencyName="PE:SUNAT" schemeID="6" schemeURI="urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo6">${ruc_emisor_pse}</cbc:CompanyID>
        </cac:PartyLegalEntity>
    </cac:SenderParty>
    <cac:ReceiverParty>
        <cac:PartyLegalEntity>
            <cbc:CompanyID schemeAgencyName="PE:SUNAT" schemeID="6" schemeURI="urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo6">${ruc_ose}</cbc:CompanyID>
        </cac:PartyLegalEntity>
    </cac:ReceiverParty>
    <cac:DocumentResponse>
        <cac:Response>
            <cbc:ResponseCode listAgencyName="PE:SUNAT">${o}</cbc:ResponseCode>
            <cbc:Description>${o}</cbc:Description>
            <#list listaStatus as status>
            <cac:Status>
                <cbc:StatusReasonCode listURI="urn:pe:gob:sunat:cpe:see:gem:codigos:codigoretorno">${status.codigo}</cbc:StatusReasonCode>
                <cbc:StatusReason>${status.codigo}</cbc:StatusReason>
            </cac:Status>
            </#list>
        </cac:Response>
        <cac:DocumentReference>
            <cbc:ID>${o}</cbc:ID>
            <cbc:IssueDate>${o}</cbc:IssueDate>
            <cbc:IssueTime>${o}</cbc:IssueTime>
            <cbc:DocumentTypeCode>${o}</cbc:DocumentTypeCode>
            <cac:Attachment>
                <cac:ExternalReference>
                    <cbc:DocumentHash>${o}</cbc:DocumentHash>
                </cac:ExternalReference>
            </cac:Attachment>
        </cac:DocumentReference>
        <cac:IssuerParty>
            <cac:PartyLegalEntity>
                <cbc:CompanyID schemeID="${o}">${o}</cbc:CompanyID>
            </cac:PartyLegalEntity>
        </cac:IssuerParty>
        <cac:RecipientParty>
            <cac:PartyLegalEntity>
                <cbc:CompanyID schemeID="${o}">${o}</cbc:CompanyID>
            </cac:PartyLegalEntity>
        </cac:RecipientParty>
    </cac:DocumentResponse>
</ApplicationResponse>