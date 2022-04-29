package com.github.gilvangobbato.adapter.input;

import com.github.gilvangobbato.adapter.input.dto.ViaCepMessage;
import com.github.gilvangobbato.adapter.output.mapper.ViaCepMapper;
import com.github.gilvangobbato.port.input.IAddressUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class AddressSqsListener {

    private final static String CLASS_NAME = "[AddressSqsListener]";
    private final static String SUCCESS = "[SUCCESS]";
    private final static String ERROR = "[ERROR]";

    private final IAddressUseCase addressUseCase;

    @SqsListener(value = {"viaCepInsert"}, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void viaCepInsertListener(final ViaCepMessage message) {
        Mono.just(message)
                .doOnNext(this::logStartMessage)
                .map(ViaCepMessage::getPayload)
                .map(ViaCepMapper::toEntity)
                .flatMap(it -> Mono.defer(() -> addressUseCase.insert(it)))
                .doOnNext(it -> this.logSuccess())
                .doOnError(it -> log.error(CLASS_NAME.concat(ERROR), it))
                .block();
    }

    private void logSuccess() {
        log.info(CLASS_NAME.concat(SUCCESS));
    }

    private void logStartMessage(ViaCepMessage message) {
        log.info(CLASS_NAME
                .concat(" - ")
                .concat(message.getPayload().toString()));
    }

}
