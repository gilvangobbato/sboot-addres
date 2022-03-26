package com.github.gilvangobbato.configuration;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.github.gilvangobbato.adapter.output.AddressRepository;
import com.github.gilvangobbato.port.input.IAddressUseCase;
import com.github.gilvangobbato.port.output.AddressPort;
import com.github.gilvangobbato.usecase.AddressUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public AddressPort addressRepository(final AmazonDynamoDBAsync amazonDynamoDBAsync){
        return new AddressRepository(amazonDynamoDBAsync);
    }

    @Bean
    public IAddressUseCase addressUseCase(final AddressPort addressPort){
        return new AddressUseCase(addressPort);
    }

    @Bean
    AmazonDynamoDBAsync amazonDynamoDBAsync(final AwsProperties env) {
        return AmazonDynamoDBAsyncClientBuilder
                .standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(env.endpoint(), env.region()))
                .build();
    }

}
