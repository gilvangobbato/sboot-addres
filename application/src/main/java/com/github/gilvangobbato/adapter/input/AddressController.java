package com.github.gilvangobbato.adapter.input;

import com.github.gilvangobbato.adapter.input.dto.AddressDto;
import com.github.gilvangobbato.adapter.input.dto.AddressListDto;
import com.github.gilvangobbato.adapter.input.mapper.AddressMapper;
import com.github.gilvangobbato.port.input.IAddressUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
public class AddressController {

    private final IAddressUseCase useCase;

    @GetMapping(value = "/v1/address")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Working " + LocalDateTime.now().toString());
    }

    @RequestMapping(value = "/v1/address",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    public Mono<ResponseEntity<AddressDto>> insert(@RequestBody AddressDto address) {

        return Mono.just(address)
                .flatMap(it -> Mono.defer(() -> useCase.insert(AddressMapper.toEntity(it))))
                .map(it -> {
                    if (it) {
                        return ResponseEntity.ok(address);

                    } else {
                        return ResponseEntity.badRequest().build();
                    }
                });
    }

    @RequestMapping(value = "/v1/address",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.PUT)
    public Mono<ResponseEntity<AddressDto>> update(@RequestBody AddressDto address) {

        return Mono.just(address)
                .flatMap(it -> Mono.defer(() -> useCase.update(AddressMapper.toEntity(it))))
                .map(it -> {
                    if (it)
                        return ResponseEntity.ok(address);
                    else
                        return ResponseEntity.badRequest().build();
                });
    }

    @RequestMapping(value = "/v1/address/all",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public Mono<ResponseEntity<AddressListDto>> findAll(
            @RequestParam(defaultValue = "0", value = "offset") Integer offset,
            @RequestParam(defaultValue = "10", value = "limit") Integer limit) {
        return Flux.from(useCase.getAddressList(offset, limit))
                .map(AddressMapper::toDto)
                .collectList()
                .map(AddressMapper::toDtoList)
                .map(ResponseEntity::ok);
    }

    @RequestMapping(value = "/v1/address/{cep}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public Mono<ResponseEntity<AddressDto>> findByCep(@PathVariable("cep") String cep) {

        return Mono.just(cep)
                .flatMap(it -> Mono.defer(() -> useCase.findByCep(it)))
                .map(it -> ResponseEntity.ok(AddressMapper.toDto(it)));
    }

}
