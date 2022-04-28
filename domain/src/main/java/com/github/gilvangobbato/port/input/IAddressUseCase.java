package com.github.gilvangobbato.port.input;

import com.github.gilvangobbato.domain.Address;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAddressUseCase {

    Mono<Boolean> insert(Address address);

    Mono<Boolean> update(Address address);

    Mono<Address> findByCep(String cep);

    Flux<Address> getAddressList(int offset, int limit);

}
