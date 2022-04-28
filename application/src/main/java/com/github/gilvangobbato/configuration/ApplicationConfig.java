package com.github.gilvangobbato.configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.github.gilvangobbato.adapter.input.AddressSqsListener;
import com.github.gilvangobbato.adapter.output.AddressRepository;
import com.github.gilvangobbato.adapter.output.AddressSqsSender;
import com.github.gilvangobbato.adapter.output.ViaCepRepository;
import com.github.gilvangobbato.domain.Address;
import com.github.gilvangobbato.port.input.IAddressUseCase;
import com.github.gilvangobbato.port.output.AddressPort;
import com.github.gilvangobbato.port.output.IAddressSqsSender;
import com.github.gilvangobbato.port.output.ViaCepPort;
import com.github.gilvangobbato.usecase.AddressUseCase;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
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
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .build();
    }

    @Bean
    public IAddressUseCase addressUseCase(
            final AddressPort addressPort,
            final ViaCepPort viaCepPort,
            final IAddressSqsSender sqsSender) {
        return new AddressUseCase(addressPort, viaCepPort, sqsSender);
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
    public QueueMessagingTemplate queueMessageTemplate(final AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

    @Bean
    public AmazonSQSAsync amazonSQS(final AwsProperties properties, final AwsBasicCredentials awsBasicCredentials) {
        return AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withEndpointConfiguration(new AwsClientBuilder
                        .EndpointConfiguration(properties.endpoint(), properties.region()))
                .build();
    }

    @Bean
    public AddressSqsListener addressSqsListener(){
        return new AddressSqsListener();
    }

    @Bean
    public IAddressSqsSender messageSender(
            final QueueMessagingTemplate queueMessageTemplate) {
        return new AddressSqsSender(queueMessageTemplate);
    }

    @Bean
    public AddressPort addressRepository(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
        return new AddressRepository(
                dynamoDbEnhancedAsyncClient.table(Address.class.getSimpleName(), TableSchema.fromBean(Address.class))
        );
    }

    @Bean
    public ViaCepRepository viaCepRepository(final RestTemplate restTemplate) {
        return new ViaCepRepository(restTemplate);
    }
}
