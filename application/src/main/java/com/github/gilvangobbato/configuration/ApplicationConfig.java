package com.github.gilvangobbato.configuration;

import com.github.gilvangobbato.adapter.output.AddressRepository;
import com.github.gilvangobbato.domain.Address;
import com.github.gilvangobbato.port.input.IAddressUseCase;
import com.github.gilvangobbato.port.output.AddressPort;
import com.github.gilvangobbato.usecase.AddressUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClientBuilder;

import java.net.URI;

@Configuration
public class ApplicationConfig {

    @Bean
    public IAddressUseCase addressUseCase(final AddressPort addressPort) {
        return new AddressUseCase(addressPort);
    }

    @Bean
    public AwsBasicCredentials awsBasicCredentials(final AwsProperties properties) {
        return AwsBasicCredentials.create(properties.accessKey(), properties.secretKey());
    }

    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient(final AwsBasicCredentials awsBasicCredentials,
                                                   final AwsProperties properties) {
        DynamoDbAsyncClientBuilder clientBuilder = DynamoDbAsyncClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials));
        if (!properties.endpoint().isEmpty()) {
            clientBuilder.endpointOverride(URI.create(properties.endpoint()));
        }
        return clientBuilder.build();
    }

    @Bean
    public DynamoDbEnhancedAsyncClient getDynamoDbEnhancedAsyncClient(final DynamoDbAsyncClient asyncClient) {
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(asyncClient)
                .build();
    }

    @Bean
    public AddressPort addressRepository(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
        return new AddressRepository(
                dynamoDbEnhancedAsyncClient.table(Address.class.getSimpleName(), TableSchema.fromBean(Address.class))
        );
    }
}
