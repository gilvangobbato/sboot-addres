package com.github.gilvangobbato.port.input;

import com.github.gilvangobbato.domain.Address;
import reactor.core.publisher.Mono;

public interface IAddressUseCase {

    Mono<Boolean> insert(Address address);

    void update(Address address);

    Mono<Address> findByCep(String cep);

}
