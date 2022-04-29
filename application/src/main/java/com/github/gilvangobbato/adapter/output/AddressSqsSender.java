package com.github.gilvangobbato.adapter.output;

import com.github.gilvangobbato.adapter.output.dto.ViaCepAddressDto;
import com.github.gilvangobbato.adapter.output.mapper.ViaCepMapper;
import com.github.gilvangobbato.domain.Address;
import com.github.gilvangobbato.port.output.IAddressSqsSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
public class AddressSqsSender implements IAddressSqsSender {

    private static final String QUEUE_NAME = "viaCepInsert";
    private final QueueMessagingTemplate messagingTemplate;

    @Override
    public Mono<Address> send(Address address) {
        address.setCep(address.getCep().replace("-", ""));
        return Mono.just(address)
                .map(this::buildMessage)
                .doOnNext(message -> messagingTemplate.convertAndSend(QUEUE_NAME, message))
                .thenReturn(address);
    }

    private Message<ViaCepAddressDto> buildMessage(Address address) {
        return MessageBuilder
                .withPayload(ViaCepMapper.toDto(address))
                .build();
    }
}
