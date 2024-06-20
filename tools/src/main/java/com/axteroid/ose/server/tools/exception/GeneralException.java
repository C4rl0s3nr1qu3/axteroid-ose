package com.axteroid.ose.server.tools.exception;

/**
 * User: RAC
 * Date: 20/03/12
 */
public class GeneralException extends RuntimeException {

    public GeneralException() {
    }

    public GeneralException(String code, String message) {
         super(message);
        this.code = code;
    }

    public GeneralException(String code,Object[] parametros) {
        this.code = code;
        this.parametros= parametros;
    }

    protected String code;
    protected Object[] parametros;

    public Object[] getParametros() {
        return parametros;
    }

    public void setParametros(Object[] parametros) {
        this.parametros = parametros;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
