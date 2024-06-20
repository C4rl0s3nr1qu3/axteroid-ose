package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ResponseType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DescriptionType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ReferenceIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ResponseCodeType;
import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

import com.axteroid.ose.server.tools.constantes.Constantes;

import java.util.ArrayList;
import java.util.List;

public class StringToResponseTypeConverter implements CustomConverter {
    public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {

        String value = (String) sourceFieldValue;

        if (StringUtils.isBlank(value)) return null;

        String[] values = StringUtils.split(value, "|");
        String id = values[0];
        String code = values[1];
        String description = values[2];

        String[] bloques = description.split("(?<=\\G.{" + Constantes.MAX_BLOCK_DESCRIPCION + "})");
        List<ResponseType> result = new ArrayList<ResponseType>();
        ReferenceIDType referenceIDType = new ReferenceIDType();
        if(StringUtils.isNotBlank(id)){
        referenceIDType.setValue(id);
        }else{
            referenceIDType.setValue("");
        }

        ResponseCodeType responseCodeType = new ResponseCodeType();
        responseCodeType.setValue(code);
        ResponseType responseType = new ResponseType();
        responseType.setReferenceID(referenceIDType);
        responseType.setResponseCode(responseCodeType);
        for (String bloque : bloques) {
            DescriptionType descriptionType = new DescriptionType();
            descriptionType.setValue(bloque);
            responseType.getDescription().add(descriptionType);
        }
        result.add(responseType);
        return result;
    }

}
