package com.github.gilvangobbato.usecase;

import com.github.gilvangobbato.domain.Address;
import com.github.gilvangobbato.port.input.IAddressUseCase;
import com.github.gilvangobbato.port.output.AddressPort;
import com.github.gilvangobbato.port.output.ViaCepPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
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
        return Mono
                .just(cep)
                .doOnNext(log::info)
                .flatMap(it->Mono.fromFuture(addressPort.findByCep(cep)))
                .switchIfEmpty(viaCepPort.getByCep(cep))
                .doOnNext(this::sendToQueue);
    }

    @Override
    public Flux<Address> getAddressList(int offset, int limit) {
        return Flux.from(addressPort.findAll(offset, limit).items());
    }

    @Override
    public void sendToQueue(Address address) {
        //TODO - SendToQueue
        log.info("Sent to queue");
    }
}
