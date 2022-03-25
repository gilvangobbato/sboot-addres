package com.github.gilvangobbato.port.input;

import com.github.gilvangobbato.domain.Address;

public interface IAddressUseCase {

    void insert(Address address);

    void update(Address address);


}
