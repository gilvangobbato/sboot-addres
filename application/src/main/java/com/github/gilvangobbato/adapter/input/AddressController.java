package com.github.gilvangobbato.adapter.input;

import com.github.gilvangobbato.adapter.input.dto.AddressDto;
import com.github.gilvangobbato.adapter.input.dto.AddressListDto;
import com.github.gilvangobbato.adapter.input.mapper.AddressMapper;
import com.github.gilvangobbato.domain.Address;
import com.github.gilvangobbato.port.input.IAddressUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
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
    public ResponseEntity<AddressDto> insert(@RequestBody AddressDto address) {
        useCase.insert(AddressMapper.toEntity(address));
        return ResponseEntity.ok(address);
    }

    @RequestMapping(value = "/v1/address/all",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public ResponseEntity<AddressListDto> findAll(Integer offset, Integer limit) {

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/v1/address/{cep}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public ResponseEntity<AddressDto> findByCep(@PathVariable("cep") String cep) {
        Address address = useCase.findByCep(cep);

        if(address == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(AddressMapper.toDto(address));
    }

}
