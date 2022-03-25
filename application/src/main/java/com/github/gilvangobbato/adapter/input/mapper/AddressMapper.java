package com.github.gilvangobbato.adapter.input.mapper;

import com.github.gilvangobbato.adapter.input.dto.AddressDto;
import com.github.gilvangobbato.domain.Address;

public class AddressMapper {

    public static Address toEntity(AddressDto addressDto){
        return Address.builder()
                .cep(addressDto.getCep())
                .city(addressDto.getCity())
                .complement(addressDto.getComplement())
                .country(addressDto.getCountry())
                .ddd(addressDto.getDdd())
                .district(addressDto.getDistrict())
                .gia(addressDto.getGia())
                .ibgeCity(addressDto.getIbgeCity())
                .ibgeCountry(addressDto.getIbgeCountry())
                .place(addressDto.getPlace())
                .uf(addressDto.getUf())
                .siafi(addressDto.getSiafi())
                .build();
    }

    public static AddressDto toDto(Address address){
        return AddressDto.builder()
                .cep(address.getCep())
                .city(address.getCity())
                .complement(address.getComplement())
                .country(address.getCountry())
                .ddd(address.getDdd())
                .district(address.getDistrict())
                .gia(address.getGia())
                .ibgeCity(address.getIbgeCity())
                .ibgeCountry(address.getIbgeCountry())
                .place(address.getPlace())
                .uf(address.getUf())
                .siafi(address.getSiafi())
                .build();
    }

}
