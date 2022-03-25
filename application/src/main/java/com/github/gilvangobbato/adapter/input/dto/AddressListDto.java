package com.github.gilvangobbato.adapter.input.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AddressListDto {

    @JsonProperty("addresses")
    private List<AddressDto> addresses;

    @JsonProperty("pagination")
    private PaginationDto pagination;
}
