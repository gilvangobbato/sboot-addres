package com.github.gilvangobbato.adapter.output;

import com.github.gilvangobbato.domain.Address;
import com.github.gilvangobbato.port.output.AddressPort;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class AddressRepository implements AddressPort {

    private final static String TABLE_NAME = "Address";
    private final DynamoDbAsyncTable<Address> dynamoDbAsyncTable;

    @Override
    public PagePublisher<Address> findAll(int page, int limit) {
        return dynamoDbAsyncTable.scan(
                ScanEnhancedRequest.builder()
                        .segment(page).
                        limit(limit)
                        .build()
        );
    }

    @Override
    public CompletableFuture<Address> findByCep(String cep) {
        return dynamoDbAsyncTable.getItem(Key.builder().partitionValue(cep).build());
    }

    @Override
    public CompletableFuture<Void> insert(Address address) {
//        address.setCreatedAt(LocalDateTime.now());
        PutItemEnhancedRequest<Address> add = PutItemEnhancedRequest
                .builder(Address.class)
                .conditionExpression(Expression.builder()
                        .expression("attribute_not_exists(cep)")
                        .build()
                )
                .item(address)
                .build();
        return dynamoDbAsyncTable.putItem(add);
    }


    @Override
    public CompletableFuture<Address> update(Address address) {
//        address.setUpdatedAt(LocalDateTime.now());
        return dynamoDbAsyncTable.updateItem(address);
    }
}
