package com.example.rabbitmq.convert;

import com.example.rabbitmq.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class PojoConvert {

    @Test
    void check() throws JsonProcessingException {
        var mapper = new ObjectMapper();
        var pesron = new Person("German", "Plaxunov", "Jun");
        var string = mapper.writeValueAsString(pesron);
        System.out.println(string);
    }

}
