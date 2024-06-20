package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import org.dozer.CustomConverter;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.io.File;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class FileToDataHandlerTypeConverter implements CustomConverter {

    public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {

        File desFile = (File) sourceFieldValue;
        if(desFile==null) return null;
        DataSource dataSource = new FileDataSource(desFile);
        DataHandler dataHandler = new DataHandler(dataSource);
        return dataHandler;
    }

}
