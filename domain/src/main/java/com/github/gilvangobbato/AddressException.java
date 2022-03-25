package com.github.gilvangobbato;

import com.github.gilvangobbato.enums.AddressExceptionEnum;
import lombok.Getter;

@Getter
public class AddressException extends RuntimeException{

    private final AddressExceptionEnum reasonEnum;

    public AddressException(String message){
        super(message);
        this.reasonEnum = AddressExceptionEnum.E0001;
    }

    public AddressException(AddressExceptionEnum reasonEnum){
        super(reasonEnum.getDescription());
        this.reasonEnum = reasonEnum;
    }

}
