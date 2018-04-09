package com.silenceonthewire.exceptions.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Fault {

    @JsonProperty("message")
    public String errorMessage;

    public Fault(String errorMessage){

        this.errorMessage = errorMessage;
    }
}
