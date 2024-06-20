package com.axteroid.ose.server.ubl20.exception;

import com.axteroid.ose.server.tools.exception.GeneralException;

/**
 * User: RAC
 * Date: 20/03/12
 */
public class InvalidRequestException extends GeneralException {

    public InvalidRequestException() {
        setCode("7026");
    }
}
