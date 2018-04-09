package com.silenceonthewire.company.type.impl;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

public class TypeEventTag {

    public static final AggregateEventTag<TypeEvent> INSTANCE = AggregateEventTag.of(TypeEvent.class);
}
