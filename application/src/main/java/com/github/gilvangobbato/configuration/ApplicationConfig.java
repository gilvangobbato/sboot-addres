package com.github.gilvangobbato.configuration;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.github.gilvangobbato.adapter.output.AddressRepository;
import com.github.gilvangobbato.port.input.IAddressUseCase;
import com.github.gilvangobbato.port.output.AddressPort;
import com.github.gilvangobbato.usecase.AddressUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public AddressPort addressRepository(final DynamoDB dynamoDB) {
        return new AddressRepository(dynamoDB);
    }

    @Bean
    public IAddressUseCase addressUseCase(final AddressPort addressPort) {
        return new AddressUseCase(addressPort);
    }

    @Bean
    AmazonDynamoDB amazonDynamoDB(final AwsProperties env) {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(env.endpoint(), env.region()))
                .build();
    }

    @Bean
    DynamoDB dynamoDB(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDB(amazonDynamoDB);
    }

}
