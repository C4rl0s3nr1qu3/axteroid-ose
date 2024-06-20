package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import java.util.ArrayList;
import java.util.List;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CountrySubentityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CustomerReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DescriptionType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DistrictType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentTypeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.InformationType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NoteType;
import org.apache.commons.lang.StringUtils;
import org.dozer.ConfigurableCustomConverter;

import com.axteroid.ose.server.tools.constantes.Constantes;

import un.unece.uncefact.data.specification.unqualifieddatatypesschemamodule._2.TextType;

public class StringToTextTypeConverter implements ConfigurableCustomConverter {


    private String parameter;

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        String valueAsString = ((String) source);
        if(StringUtils.isBlank(valueAsString)) return null;
        TextType nameType = null;

        if ("DistrictType".equals(parameter)) {
            nameType = new DistrictType();
        } else if ("CountrySubentityType".equals(parameter)) {
            nameType = new CountrySubentityType();
        } else if ("DescriptionType".equals(parameter)) {
            List<TextType> result = new ArrayList<TextType>();

            if(StringUtils.isNotBlank(valueAsString)){
                String[] bloques = valueAsString.split("(?<=\\G.{" + Constantes.MAX_BLOCK_DESCRIPCION+ "})");
                for (int i = 0; i< bloques.length; i++) {
                    TextType item = new DescriptionType();
                    item.setValue(bloques[i]);
                    result.add(item);
                }
            }
            return result;
        } else if ("TextType".equals(parameter)) {
            nameType = new TextType();
        } else if ("NoteType".equals(parameter)) {
        	nameType = new NoteType();
        } else if("InformationType".equals(parameter)) {
        	nameType = new InformationType();
        } else if("DocumentTypeType".equals(parameter)){
        	nameType = new DocumentTypeType();
        } else if("CustomerReferenceType".equals(parameter)){
        	nameType = new CustomerReferenceType();
        }        
        nameType.setValue(valueAsString);
        return nameType;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public static void main(String[] args) {
        /*      String str = "abcdefgh";
        List<String> result = new ArrayList<String>();
        boolean continuar = true;
        *//**
         * 0,25    25*0,25*1
         * 25,50   25*1,25*2
         * 50,75   25*2,25*3
         *//*
        int max = str.length() / MAX_DESCRIPCION;
        for (int i = 0; i <= max; i++) {
            result.add(str.substring(MAX_DESCRIPCION*i,(i+1)*MAX_DESCRIPCION));
        }
        System.out.println(result);*/
        String s = "1234567890ab";
        String[] bloques = s.split("(?<=\\G.{7})");

        for (int i = 0; i < bloques.length; i++) {
            System.out.println(bloques[i]);
        }

    }
}
