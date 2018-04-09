package com.silenceonthewire.company.type.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.silenceonthewire.company.type.api.Type;

import java.util.Optional;

public class TypeState implements CompressedJsonable {

    public static final TypeState EMPTY = new TypeState(Optional.empty(), false);

    public Optional<Type> type;

    public boolean currentState;

    @JsonCreator
    public TypeState(Optional<Type> type, boolean currentState) {

        this.type = type;
        this.currentState = currentState;
    }
}
