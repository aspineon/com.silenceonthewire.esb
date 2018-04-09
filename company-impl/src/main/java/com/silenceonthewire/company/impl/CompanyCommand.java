package com.silenceonthewire.company.impl;

import akka.Done;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import com.silenceonthewire.company.api.Company;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

public interface CompanyCommand extends Jsonable {

    @Value
    @Builder
    @JsonDeserialize
    final class CreateCompany implements CompanyCommand, PersistentEntity.ReplyType<Done> {
        public Company company;

        public CreateCompany(Company company) {

            this.company = company;
        }
    }

    @Value
    @Builder
    @JsonDeserialize
    final class UpdateCompany implements CompanyCommand, PersistentEntity.ReplyType<Done> {
        public Company company;

        public UpdateCompany(Company company) {

            this.company = company;
        }
    }

    @Value
    @Builder
    @JsonDeserialize
    final class DeleteCompany implements CompanyCommand, PersistentEntity.ReplyType<Done> {
        public Company company;

        public DeleteCompany(Company company) {

            this.company = company;
        }
    }

    @JsonDeserialize
    final class CompanyCurrentState implements CompanyCommand, PersistentEntity.ReplyType<Optional<Company>> {}
}
