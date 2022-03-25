package com.github.gilvangobbato.adapter.output;

import com.github.gilvangobbato.domain.Address;
import com.github.gilvangobbato.port.output.AddressPort;

import java.util.List;

public class AddressRepository implements AddressPort {

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
        System.out.println("Inserindo");
        return 1L;
    }

    @Override
    public Long update(Address address) {
        return null;
    }
}
