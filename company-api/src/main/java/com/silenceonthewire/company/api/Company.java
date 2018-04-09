package com.silenceonthewire.company.api;

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
public class Company {

    @NonNull
    public String id;
    @NonNull
    public String typeId;
    @NonNull
    public String name;
    @NonNull
    public String phone;
    @NonNull
    public String email;
    @NonNull
    public String taxNumber;
    @NonNull
    public String street;
    @NonNull
    public String city;
    @NonNull
    public String state;
    @NonNull
    public String country;
    @NonNull
    public String postalCode;
    @NonNull
    public String createdAt;
    @NonNull
    public String updatedAt;

    @JsonCreator
    public Company(
            String id, String typeId, String name, String phone, String email, String taxNumber, String street,
            String city, String state, String country, String postalCode, String createdAt, String updatedAt
    ){

        this.id = Preconditions.checkNotNull(id, "id");
        this.typeId = Preconditions.checkNotNull(typeId, "typeId");
        this.name = Preconditions.checkNotNull(name, "name");
        this.phone = Preconditions.checkNotNull(phone, "phone");
        this.email = Preconditions.checkNotNull(email, "email");
        this.taxNumber = Preconditions.checkNotNull(taxNumber, "taxNumber");
        this.street = Preconditions.checkNotNull(street, "street");
        this.city = Preconditions.checkNotNull(city, "city");
        this.state = Preconditions.checkNotNull(state, "state");
        this.country = Preconditions.checkNotNull(country, "country");
        this.postalCode = Preconditions.checkNotNull(postalCode, "postalCode");
        this.createdAt = Preconditions.checkNotNull(createdAt, "createdAt");
        this.updatedAt = Preconditions.checkNotNull(updatedAt, "updatedAt");
    }
}
