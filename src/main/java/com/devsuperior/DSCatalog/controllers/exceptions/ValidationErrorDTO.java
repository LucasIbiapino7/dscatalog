package com.devsuperior.DSCatalog.controllers.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorDTO extends CustomErrorDTO{

    private List<FieldMessage> errors = new ArrayList<>();

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addError(String fieldName, String message){
        errors.add(new FieldMessage(fieldName, message));
    }
}
