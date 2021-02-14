package com.marcin.acaisoft.exeptions;

import java.time.LocalDateTime;

public class MessageNotFoundExeption extends RuntimeException {

    private LocalDateTime localDateTime;

    public MessageNotFoundExeption(String params, String values){
        super("Could not found Message object witch params (" + params + ") = (" + values +").");
        this.localDateTime = LocalDateTime.now();

    }

    public LocalDateTime getExceptionTime(){
        return localDateTime;
    }
}
