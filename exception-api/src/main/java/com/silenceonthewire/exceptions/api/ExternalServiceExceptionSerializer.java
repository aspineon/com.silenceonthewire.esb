package com.silenceonthewire.exceptions.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightbend.lagom.javadsl.api.deser.ExceptionSerializer;
import com.lightbend.lagom.javadsl.api.deser.RawExceptionMessage;
import com.lightbend.lagom.javadsl.api.transport.MessageProtocol;

import java.io.IOException;
import java.util.Collection;

public class ExternalServiceExceptionSerializer implements ExceptionSerializer {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final ExternalServiceExceptionSerializer INSTANCE = new ExternalServiceExceptionSerializer();

    @Override
    public RawExceptionMessage serialize(Throwable exception, Collection<MessageProtocol> accept) {
        return null;
    }

    @Override
    public Throwable deserialize(RawExceptionMessage message) {
        return ExceptionFactory.getInstance(mapExceptionToFault(message));
    }

    Fault mapExceptionToFault(RawExceptionMessage message) {

        Fault fault;

        try {
            String errorJson = message.messageAsText();
            fault = OBJECT_MAPPER.readValue(errorJson, Fault.class);
        } catch (IOException ex) {

            fault = new Fault("No payload available.");
        }
        return fault;
    }
}
