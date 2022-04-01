package com.github.gilvangobbato.port.output;

import com.github.gilvangobbato.domain.Address;

import java.util.List;

public interface AddressPort {
    List<Address> findAll(int page, int limit);

    Address findByCep(String cep);

    Long insert(Address address);

    Long update(Address address);
}
