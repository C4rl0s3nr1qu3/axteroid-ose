package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

import com.axteroid.ose.server.tools.util.StringUtil;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToSerieNumeroTypeConverter implements CustomConverter {

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        String valueAsString = ((String) source);
        if (StringUtils.isBlank(valueAsString)) return null;

        String[] token = StringUtils.split(valueAsString, '-');

        if (token.length == 2) {
            return token[0]+"-" + StringUtil.rpad(token[1], 8, '0');
        }else{
            return valueAsString;
        }


    }
}
