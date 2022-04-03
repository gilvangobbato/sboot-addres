package com.github.gilvangobbato.usecase;

import com.github.gilvangobbato.domain.Address;
import com.github.gilvangobbato.port.input.IAddressUseCase;
import com.github.gilvangobbato.port.output.AddressPort;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class AddressUseCase implements IAddressUseCase {

    private final AddressPort addressPort;

    @Override
    public Mono<Boolean> insert(Address address) {
        addressPort.insert(address).join();
        return Mono.just(Boolean.TRUE);
    }

    @Override
    public void update(Address address) {

    }

    @Override
    public Mono<Address> findByCep(String cep) {
        return Mono.fromFuture(addressPort.findByCep(cep));
    }

    @Override
    public Flux<Address> getAddressList(int offset, int limit) {
        return Flux.from(addressPort.findAll(offset, limit).items());
    }
}
