package com.github.gilvangobbato.adapter.output;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.github.gilvangobbato.domain.Address;
import com.github.gilvangobbato.port.output.AddressPort;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class AddressRepository implements AddressPort {

    private final AmazonDynamoDBAsync amazonDynamoDBAsync;

    @Override
    public List<Address> findAll(int page, int limit) {
        return null;
    }

    @Override
    public Address findByCep(String cep) {
        return null;
    }

    @Override
    public Long insert(Address address) {
//        amazonDynamoDBAsync.update
        return 1L;
    }

    @Override
    public Long update(Address address) {
        return null;
    }
}
