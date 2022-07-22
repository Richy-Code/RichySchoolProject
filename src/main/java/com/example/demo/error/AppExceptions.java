package com.example.demo.error;

import com.example.demo.binding_entities.ErrorMessage;

public abstract class AppExceptions extends Exception{
    private ErrorMessage errorMessage;

    public AppExceptions(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
