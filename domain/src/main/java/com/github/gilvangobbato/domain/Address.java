package com.github.gilvangobbato.domain;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class Address {
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
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("cep")
    public String getCep() {
        return cep;
    }

    public String getPlace() {
        return place;
    }

    public String getComplement() {
        return complement;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }

    public String getIbgeCity() {
        return ibgeCity;
    }

    public String getGia() {
        return gia;
    }

    public String getDdd() {
        return ddd;
    }

    public String getSiafi() {
        return siafi;
    }

    public String getUf() {
        return uf;
    }

    public String getCountry() {
        return country;
    }

    public String getIbgeCountry() {
        return ibgeCountry;
    }

}
