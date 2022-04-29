package com.github.gilvangobbato.adapter.input.dto;

import com.github.gilvangobbato.adapter.output.dto.ViaCepAddressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViaCepMessage implements Message<ViaCepAddressDto> {
    private Map<String, Object> headers;
    private ViaCepAddressDto payload;

    @Override
    public ViaCepAddressDto getPayload() {
        return payload;
    }

    @Override
    public MessageHeaders getHeaders() {
        return new MessageHeaders(headers);
    }
}
