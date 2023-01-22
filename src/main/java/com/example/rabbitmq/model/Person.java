package com.example.rabbitmq.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("position")
    private String position;
}
