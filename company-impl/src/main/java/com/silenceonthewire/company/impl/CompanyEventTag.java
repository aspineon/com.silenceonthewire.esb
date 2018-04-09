package com.silenceonthewire.company.impl;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

public class CompanyEventTag {

    public static final AggregateEventTag<CompanyEvent> INSTANCE = AggregateEventTag.of(CompanyEvent.class);
}
