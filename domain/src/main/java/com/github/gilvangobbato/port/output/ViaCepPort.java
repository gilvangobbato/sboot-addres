package com.github.gilvangobbato.port.output;

import com.github.gilvangobbato.domain.Address;
import reactor.core.publisher.Mono;

public interface ViaCepPort {

    Mono<Address> getByCep(String cep);

}
