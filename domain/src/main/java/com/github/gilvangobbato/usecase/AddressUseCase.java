package com.github.gilvangobbato.usecase;

import com.github.gilvangobbato.domain.Address;
import com.github.gilvangobbato.port.input.IAddressUseCase;
import com.github.gilvangobbato.port.output.AddressPort;
import com.github.gilvangobbato.port.output.IAddressSqsSender;
import com.github.gilvangobbato.port.output.ViaCepPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
public class AddressUseCase implements IAddressUseCase {

    private final AddressPort addressPort;
    private final ViaCepPort viaCepPort;
    private final IAddressSqsSender sqsSender;

    @Override
    public Mono<Boolean> insert(Address address) {
        return Mono.fromFuture(addressPort.insert(address))
                .map(it -> Boolean.TRUE)
                .onErrorReturn(Boolean.FALSE);
    }

    @Override
    public Mono<Boolean> update(Address address) {
        return addressPort.update(address);
    }

    @Override
    public Mono<Address> findByCep(String cep) {
        return Mono.fromFuture(addressPort.findByCep(cep))
//                .doOnNext(it -> log.info(it.toString()))
                .switchIfEmpty(
                        Mono.defer(() -> viaCepPort.getByCep(cep)
                                .flatMap(sqsSender::send)
                        )
                );
    }

    @Override
    public Flux<Address> getAddressList(int offset, int limit) {
        return Flux.from(addressPort.findAll(offset, limit).items());
    }
}
