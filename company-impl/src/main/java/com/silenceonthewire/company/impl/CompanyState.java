package com.silenceonthewire.company.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.silenceonthewire.company.api.Company;

import java.util.Optional;

public class CompanyState implements CompressedJsonable {

    public static final CompanyState EMPTY = new CompanyState(Optional.empty(), false);

    public Optional<Company> company;
    public boolean currentState;

    @JsonCreator
    public CompanyState(Optional<Company> company, boolean currentState){

        this.company = company;
        this.currentState = currentState;
    }
}
