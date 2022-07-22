package com.example.demo.error;

import com.example.demo.binding_entities.ErrorMessage;

public class NoPassRecordFoundException extends AppExceptions{

    public NoPassRecordFoundException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
