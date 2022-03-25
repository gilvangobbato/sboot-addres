package com.github.gilvangobbato.configuration;

import com.github.gilvangobbato.adapter.output.AddressRepository;
import com.github.gilvangobbato.port.input.IAddressUseCase;
import com.github.gilvangobbato.port.output.AddressPort;
import com.github.gilvangobbato.usecase.AddressUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public AddressPort addressRepository(){
        return new AddressRepository();
    }

    @Bean
    public IAddressUseCase addressUseCase(final AddressPort addressPort){
        return new AddressUseCase(addressPort);
    }

}
