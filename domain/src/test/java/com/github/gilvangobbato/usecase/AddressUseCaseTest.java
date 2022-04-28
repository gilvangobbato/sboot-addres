package com.github.gilvangobbato.usecase;

import com.github.gilvangobbato.domain.Address;
import com.github.gilvangobbato.port.input.IAddressUseCase;
import com.github.gilvangobbato.port.output.AddressPort;
import com.github.gilvangobbato.port.output.IAddressSqsSender;
import com.github.gilvangobbato.port.output.ViaCepPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class AddressUseCaseTest {

    private AddressPort addressPort;
    private ViaCepPort viaCepPort;
    private IAddressSqsSender sqsSender;

    private IAddressUseCase useCase;


    @BeforeEach
    public void setUp() {
        this.addressPort = Mockito.mock(AddressPort.class);
        this.viaCepPort = Mockito.mock(ViaCepPort.class);
        this.sqsSender = Mockito.mock(IAddressSqsSender.class);

        this.useCase = new AddressUseCase(addressPort, viaCepPort, sqsSender);
    }

    @Test
    void shouldInsertSuccess() {
        Address address = this.buildAddress();

        when(addressPort.insert(any(Address.class))).thenReturn(CompletableFuture.runAsync(() -> {
        }));

        StepVerifier.create(this.useCase.insert(address))
                .expectComplete()
                .verify();
    }

    @Test
    void shouldReturnAddressFromDynamoDB() {
        when(addressPort.findByCep(any())).thenReturn(CompletableFuture.completedFuture(this.buildAddress()));

        StepVerifier.create(this.useCase.findByCep("95720000"))
                .assertNext(it -> {
                    assertNotNull(it);
                    assertEquals("RS", it.getUf());
                }).verifyComplete();

    }

    @Test
    void shouldReturnAddressFromViaCep() {
        final var address = this.buildAddress();

        when(addressPort.findByCep(any())).thenReturn(new CompletableFuture<Address>().completeAsync(() -> null));
        when(viaCepPort.getByCep(any())).thenReturn(Mono.just(address));
        when(sqsSender.send(any())).thenReturn(Mono.just(address));
        String cep = "95720000";
        StepVerifier.create(this.useCase.findByCep(cep))
                .assertNext(it -> {
                    assertNotNull(it);
                    assertEquals("RS", it.getUf());
                    Mockito.verify(viaCepPort).getByCep(eq(cep));
                    Mockito.verify(sqsSender).send(eq(it));
                }).verifyComplete();
    }

    private Address buildAddress() {
        return Address.builder()
                .cep("95720000")
                .ibgeCity("4858744")
                .uf("RS")
                .city("Garibaldi")
                .ddd("54")
                .createdAt(LocalDateTime.now())
                .build();
    }
}