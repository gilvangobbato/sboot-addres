package com.github.gilvangobbato.port.output;

import com.github.gilvangobbato.domain.Address;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AddressPort {
    PagePublisher<Address> findAll(int page, int limit);

    CompletableFuture<Address> findByCep(String cep);

    CompletableFuture<Void> insert(Address address);

    CompletableFuture<Address> update(Address address);
}
