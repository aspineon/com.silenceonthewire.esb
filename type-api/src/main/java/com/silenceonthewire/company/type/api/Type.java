package com.silenceonthewire.company.type.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Wither;

@Immutable
@JsonDeserialize
@Value
@Builder
@Wither
public class Type {

    @NonNull
    public String id;
    @NonNull
    public String name;
    @NonNull
    public String description;
    @NonNull
    public String timestamp;

    @JsonCreator
    public Type(String id, String name, String description, String timestamp) {

        this.id = Preconditions.checkNotNull(id, "id");
        this.name = Preconditions.checkNotNull(name, "name");
        this.description = Preconditions.checkNotNull(description, "description");
        this.timestamp = Preconditions.checkNotNull(timestamp, "timestamp");
    }
}
