package com.github.gilvangobbato.usecase;

import com.github.gilvangobbato.domain.Address;
import com.github.gilvangobbato.port.input.IAddressUseCase;
import com.github.gilvangobbato.port.output.AddressPort;
import com.github.gilvangobbato.port.output.ViaCepPort;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@AllArgsConstructor
public class AddressUseCase implements IAddressUseCase {

    private final AddressPort addressPort;
    private final ViaCepPort viaCepPort;

    @Override
    public Mono<Boolean> insert(Address address) {
        addressPort.insert(address).join();
        return Mono.just(Boolean.TRUE);
    }

    @Override
    public Mono<Boolean> update(Address address) {
        return addressPort.update(address);
    }

    @Override
    public Mono<Address> findByCep(String cep) {
        return Mono.from(Mono.fromFuture(addressPort.findByCep(cep)))
                .switchIfEmpty(viaCepPort.getByCep(cep));
    }

    @Override
    public Flux<Address> getAddressList(int offset, int limit) {
        return Flux.from(addressPort.findAll(offset, limit).items());
    }
}
