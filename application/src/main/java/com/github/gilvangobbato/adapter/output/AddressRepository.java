package com.github.gilvangobbato.adapter.output;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.github.gilvangobbato.domain.Address;
import com.github.gilvangobbato.port.output.AddressPort;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class AddressRepository implements AddressPort {

    private final static String ADDRESS = "Address";
    private final DynamoDB dynamoDB;

    @Override
    public List<Address> findAll(int page, int limit) {
        return null;
    }

    @Override
    public Address findByCep(String cep) {

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("cep = :v_cep").withValueMap(new ValueMap().withString(":v_cep", cep));

        try{
            ItemCollection<QueryOutcome> outcome = this.getTable().query(querySpec);
            Item item = outcome.iterator().next();
            return Address.builder()
                    .cep(item.getString("cpf"))
                    .ibgeCity(item.getString("ibge_city"))
                    .city(item.getString("city"))
                    .build();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Long insert(Address address) {

        final Map<String, Object> infoMap = new HashMap<>();

        infoMap.put("city", address.getCity());
        infoMap.put("complement", address.getComplement());
        infoMap.put("country", address.getCountry());
        infoMap.put("ddd", address.getDdd());
        infoMap.put("district", address.getDistrict());
        infoMap.put("gia", address.getGia());
        infoMap.put("ibge_city", address.getIbgeCity());
        infoMap.put("place", address.getPlace());
        infoMap.put("ibge_country", address.getIbgeCountry());
        infoMap.put("siafi", address.getSiafi());
        infoMap.put("uf", address.getUf());
        infoMap.put("insert_date", LocalDateTime.now().toString());

        try {
            PutItemOutcome outcome = this.getTable().putItem(
                    Item.fromMap(infoMap).withPrimaryKey("cep", address.getCep()),
                    "attribute_not_exists(cep)",
                    null,
                    null);
            System.out.println(outcome.getPutItemResult());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 1L;
    }

    private Table getTable() {
        return dynamoDB.getTable(ADDRESS);
    }

    @Override
    public Long update(Address address) {
        return null;
    }
}
