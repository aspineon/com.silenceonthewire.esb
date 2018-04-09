package com.silenceonthewire.company.type.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import com.silenceonthewire.company.type.api.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

public interface TypeEvent extends Jsonable, AggregateEvent<TypeEvent> {

    @Override
    default AggregateEventTagger<TypeEvent> aggregateTag(){

        return TypeEventTag.INSTANCE;
    }

    @Value
    @Builder
    @JsonDeserialize
    final class TypeCreated implements TypeEvent, CompressedJsonable {
        public String entityId;
        public Type type;

        @JsonCreator
        public TypeCreated(String entityId, Type type) {

            this.entityId = entityId;
            this.type = type;
        }
    }

    @Value
    @Builder
    @JsonDeserialize
    final class TypeUpdated implements TypeEvent, CompressedJsonable {
        public String entityId;
        public Type type;

        @JsonCreator
        public TypeUpdated(String entityId, Type type) {

            this.entityId = entityId;
            this.type = type;
        }
    }

    @Value
    @Builder
    @JsonDeserialize
    final class TypeDeleted implements TypeEvent, CompressedJsonable {
        public String entityId;
        public Type type;

        @JsonCreator
        public TypeDeleted(String entityId, Type type) {

            this.entityId = entityId;
            this.type = type;
        }
    }
}
