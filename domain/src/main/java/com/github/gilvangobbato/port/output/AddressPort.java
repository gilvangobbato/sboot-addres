package com.github.gilvangobbato.port.output;

import com.github.gilvangobbato.domain.Address;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;

import java.util.concurrent.CompletableFuture;

public interface AddressPort {
    PagePublisher<Address> findAll(int page, int limit);

    CompletableFuture<Address> findByCep(String cep);

    CompletableFuture<Void> insert(Address address);

    Mono<Boolean> update(Address address);
}
