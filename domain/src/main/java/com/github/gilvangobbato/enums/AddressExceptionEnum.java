package com.github.gilvangobbato.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AddressExceptionEnum {
    E0001("E0001", "Unexpected exception"),
    E0002("E0002", "Not found"),
    E0003("E0003", "Duplicated");

    private final String code;
    private final String description;
}
