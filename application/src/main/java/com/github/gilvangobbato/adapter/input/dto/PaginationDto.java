package com.github.gilvangobbato.adapter.input.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PaginationDto {

    @JsonProperty("page")
    private Integer page;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("lastPage")
    private Boolean lastPage;

}
