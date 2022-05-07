package com.github.gilvangobbato.configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
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
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClientBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Configuration
public class ApplicationConfig {

    @Bean
    public ObjectMapper objectMapper(){
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));

        return JsonMapper.builder()
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .addModule(timeModule)
                .build();
    }

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
    public QueueMessagingTemplate queueMessageTemplate(final AmazonSQSAsync amazonSQSAsync,
                                                       final ResourceIdResolver resourceIdResolver,
                                                       final ObjectMapper mapper) {

        final MappingJackson2MessageConverter
                converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(mapper);
        converter.setSerializedPayloadClass(String.class);

        return new QueueMessagingTemplate(amazonSQSAsync, resourceIdResolver, converter);
    }

    @Bean
    public AmazonSQSAsync amazonSQS(final AwsProperties properties, final AwsBasicCredentials awsBasicCredentials) {
        return AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withEndpointConfiguration(new AwsClientBuilder
                        .EndpointConfiguration(properties.sqs().endpoint(), properties.region()))
                .build();
    }

    @Bean
    public AddressSqsListener addressSqsListener(final IAddressUseCase addressUseCase) {
        return new AddressSqsListener(addressUseCase);
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


    @Bean
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory() {
        final var factory = new SimpleMessageListenerContainerFactory();
        factory.setWaitTimeOut(5);

        return factory;
    }

    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory(final ObjectMapper mapper, final AmazonSQSAsync amazonSQSAsync) {
        final MappingJackson2MessageConverter
                converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(mapper);
        converter.setStrictContentTypeMatch(false);

        final var queueHandlerFactory = new QueueMessageHandlerFactory();
        queueHandlerFactory.setAmazonSqs(amazonSQSAsync);
        queueHandlerFactory.setArgumentResolvers(Collections.singletonList(
                new PayloadMethodArgumentResolver(converter)
        ));
        return queueHandlerFactory;
    }

}
