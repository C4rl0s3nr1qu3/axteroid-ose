package com.axteroid.ose.server.ubl20.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * User: RAC
 * Date: 15/03/12
 */
public class ValidationErrorsBeanValidationImpl implements ValidationErrors{
    List<ValidationError> validationErrors = new ArrayList<ValidationError>();

    public int size() {
        return validationErrors.size();
    }


    public void addError(ValidationError valErr) {
        validationErrors.add(valErr);
    }


    public List<ValidationError> getErrors() {
        return validationErrors;
    }

}
