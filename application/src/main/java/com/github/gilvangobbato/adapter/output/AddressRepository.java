package com.github.gilvangobbato.adapter.output;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.github.gilvangobbato.domain.Address;
import com.github.gilvangobbato.port.output.AddressPort;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return null;
    }

    @Override
    public Long insert(Address address) {
        Table table = dynamoDB.getTable(ADDRESS);
        final Map<String, AttributeValue> infoMap = new HashMap<>();

        infoMap.put("city", new AttributeValue(address.getCity()));
        infoMap.put("complement", new AttributeValue(address.getComplement()));
        infoMap.put("country", new AttributeValue(address.getCountry()));
        infoMap.put("ddd", new AttributeValue(address.getDdd()));
        infoMap.put("district", new AttributeValue(address.getDistrict()));
        infoMap.put("gia", new AttributeValue(address.getGia()));
        infoMap.put("ibge_city", new AttributeValue(address.getIbgeCity()));
        infoMap.put("place", new AttributeValue(address.getPlace()));
        infoMap.put("ibge_country", new AttributeValue(address.getIbgeCountry()));
        infoMap.put("siafi", new AttributeValue(address.getSiafi()));
        infoMap.put("uf", new AttributeValue(address.getUf()));
        infoMap.put("insert_date", new AttributeValue(LocalDateTime.now().toString()));

        try {
            PutItemOutcome outcome = table.putItem(new Item().withPrimaryKey("cep", new AttributeValue(address.getCep())).withMap("Address", infoMap));
            System.out.println(outcome.getPutItemResult().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 1L;
    }

    @Override
    public Long update(Address address) {
        return null;
    }
}
