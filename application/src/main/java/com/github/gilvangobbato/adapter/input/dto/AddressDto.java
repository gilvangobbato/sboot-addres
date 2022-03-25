package com.github.gilvangobbato.adapter.input.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AddressDto {

    @JsonProperty("cep")
    private String cep;
    @JsonProperty("place")
    private String place;
    @JsonProperty("complement")
    private String complement;
    @JsonProperty("district")
    private String district;
    @JsonProperty("city")
    private String city;
    @JsonProperty("ibge_city")
    private String ibgeCity;
    @JsonProperty("gia")
    private String gia;
    @JsonProperty("ddd")
    private String ddd;
    @JsonProperty("siafi")
    private String siafi;
    @JsonProperty("uf")
    private String uf;
    @JsonProperty("country")
    private String country;
    @JsonProperty("ibge_country")
    private String ibgeCountry;

}
