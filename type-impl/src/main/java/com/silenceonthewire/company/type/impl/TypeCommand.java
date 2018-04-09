package com.silenceonthewire.company.type.impl;

import akka.Done;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import com.silenceonthewire.company.type.api.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

public interface TypeCommand extends Jsonable {

    @Value
    @Builder
    @JsonDeserialize
    final class CreateType implements TypeCommand, PersistentEntity.ReplyType<Done> {
        public Type type;

        public CreateType(Type type) {

            this.type = type;
        }
    }

    @Value
    @Builder
    @JsonDeserialize
    final class UpdateType implements TypeCommand, PersistentEntity.ReplyType<Done> {
        public Type type;

        public UpdateType(Type type) {

            this.type = type;
        }
    }

    @Value
    @Builder
    @JsonDeserialize
    final class DeleteType implements TypeCommand, PersistentEntity.ReplyType<Done> {
        public Type type;

        public DeleteType(Type type) {

            this.type = type;
        }
    }

    @JsonDeserialize
    final class TypeCurrentState implements TypeCommand, PersistentEntity.ReplyType<Optional<Type>> {}
}
