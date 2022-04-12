package com.github.gilvangobbato.adapter.output;

import com.github.gilvangobbato.adapter.output.dto.ViaCepAddressDto;
import com.github.gilvangobbato.adapter.output.mapper.ViaCepMapper;
import com.github.gilvangobbato.domain.Address;
import com.github.gilvangobbato.port.output.ViaCepPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ViaCepRepository implements ViaCepPort {

    private final RestTemplate restTemplate;

    @Override
    public Mono<Address> getByCep(String cep) {
        return Mono.just(cep)
                .flatMap(it->Mono.just(restTemplate.getForEntity("https://viacep.com.br/ws/".concat(cep).concat("/json/"), ViaCepAddressDto.class)))
                .map(response->{
                    if(response.getStatusCode().is2xxSuccessful()){
                        ViaCepAddressDto dto = response.getBody();
                        return (dto == null || dto.isErro()) ? null : dto;
                    }
                    return null;
                })
                .map(ViaCepMapper::toEntity);
    }
}
