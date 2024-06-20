package com.axteroid.ose.server.ubl20.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * User: RAC
 * Date: 15/03/12
 */
public class ParamsValidationException extends RuntimeException {
    private ValidationErrors validationErrors;

    public ParamsValidationException(ValidationErrors validationErrors) {
        super("Parámetros inválidos");
        this.validationErrors = validationErrors;
    }

    public ValidationErrors getValidationErrors() {
        return validationErrors;
    }

    public Object[] getParameter(String code) {
        List<ValidationError> errors = validationErrors.getErrors();
        for (ValidationError error : errors) {
            if (error.getCode().equals(code)) {
                return error.getArguments();
            }
        }
        return null;
    }

    public List<String> getErrorCodes() {
        List<ValidationError> errors = validationErrors.getErrors();
        List<String> result = new ArrayList<String>();

        for (ValidationError error : errors) {
            result.add(error.getCode());
        }
        return result;
    }


}

