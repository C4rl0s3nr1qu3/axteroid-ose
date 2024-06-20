package com.axteroid.ose.server.ubl20.exception;

import java.util.List;

/**
 * User: RAC
 * Date: 15/03/12
 */
public interface ValidationErrors {
    int size();
    void addError(ValidationError valErr);
    List<ValidationError> getErrors();

}
