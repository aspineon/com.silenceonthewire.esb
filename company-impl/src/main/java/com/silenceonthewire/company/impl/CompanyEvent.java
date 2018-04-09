package com.silenceonthewire.company.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import com.silenceonthewire.company.api.Company;
import lombok.Builder;
import lombok.Value;

public interface CompanyEvent extends Jsonable, AggregateEvent<CompanyEvent> {

    @Override
    default AggregateEventTagger<CompanyEvent> aggregateTag(){

        return CompanyEventTag.INSTANCE;
    }

    @Value
    @Builder
    @JsonDeserialize
    final class CompanyCreated implements CompanyEvent, CompressedJsonable {

        public String entityId;
        public Company company;

        @JsonCreator
        public CompanyCreated(String entityId, Company company){

            this.entityId = entityId;
            this.company = company;
        }
    }

    @Value
    @Builder
    @JsonDeserialize
    final class CompanyUpdated implements CompanyEvent, CompressedJsonable {

        public String entityId;
        public Company company;

        @JsonCreator
        public CompanyUpdated(String entityId, Company company){

            this.entityId = entityId;
            this.company = company;
        }
    }

    @Value
    @Builder
    @JsonDeserialize
    final class CompanyDeleted implements CompanyEvent, CompressedJsonable {

        public String entityId;
        public Company company;

        @JsonCreator
        public CompanyDeleted(String entityId, Company company){

            this.entityId = entityId;
            this.company = company;
        }
    }
}
