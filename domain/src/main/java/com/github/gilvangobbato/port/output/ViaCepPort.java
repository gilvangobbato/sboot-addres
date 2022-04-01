package com.github.gilvangobbato.port.output;

import com.github.gilvangobbato.domain.Address;

public interface ViaCepPort {

    Address getByCep(String cep);

}
