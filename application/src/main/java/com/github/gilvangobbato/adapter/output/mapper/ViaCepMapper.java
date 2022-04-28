package com.github.gilvangobbato.adapter.output.mapper;

import com.github.gilvangobbato.adapter.output.dto.ViaCepAddressDto;
import com.github.gilvangobbato.domain.Address;

public class ViaCepMapper {

    public static Address toEntity(ViaCepAddressDto dto) {
        if (dto == null) return null;

        return Address.builder()
                .gia(dto.getGia())
                .ddd(dto.getDdd())
                .siafi(dto.getSiafi())
                .ibgeCountry("")
                .country("Brasil")
                .complement(dto.getComplemento())
                .uf(dto.getUf())
                .district(dto.getBairro())
                .place(dto.getLogradouro())
                .city(dto.getLocalidade())
                .ibgeCity(dto.getIbge())
                .cep(dto.getCep())
                .build();
    }

    public static ViaCepAddressDto toDto(Address address){
        return ViaCepAddressDto.builder()
                .gia(address.getGia())
                .ddd(address.getDdd())
                .siafi(address.getSiafi())
                .complemento(address.getComplement())
                .uf(address.getUf())
                .bairro(address.getDistrict())
                .logradouro(address.getPlace())
                .localidade(address.getCity())
                .ibge(address.getIbgeCity())
                .cep(address.getCep())
                .build();
    }

}
