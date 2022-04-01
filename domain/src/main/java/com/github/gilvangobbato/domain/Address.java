package com.github.gilvangobbato.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private Long id;
    private String cep;
    private String place;
    private String complement;
    private String district;
    private String city;
    private String ibgeCity;
    private String gia;
    private String ddd;
    private String siafi;
    private String uf;
    private String country;
    private String ibgeCountry;
    private LocalDateTime updateDate;
    private LocalDateTime insertionDate;
}
