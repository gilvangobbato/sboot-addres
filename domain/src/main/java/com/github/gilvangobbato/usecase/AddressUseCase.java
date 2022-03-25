package com.github.gilvangobbato.usecase;

import com.github.gilvangobbato.domain.Address;
import com.github.gilvangobbato.port.input.IAddressUseCase;
import com.github.gilvangobbato.port.output.AddressPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddressUseCase implements IAddressUseCase {

    private final AddressPort addressPort;

    @Override
    public void insert(Address address) {
        addressPort.insert(address);
    }

    @Override
    public void update(Address address) {

    }
}
