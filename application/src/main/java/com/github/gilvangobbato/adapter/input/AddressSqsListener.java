package com.github.gilvangobbato.adapter.input;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import reactor.core.publisher.Mono;

@Slf4j
public class AddressSqsListener {

    @SqsListener(value = {"viaCepInsert"}, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void viaCepInsertListener(String message) {
        Mono.just(message)
                .doOnNext(log::info)
                .block();
    }

}
