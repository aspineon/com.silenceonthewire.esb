package com.silenceonthewire.company.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.silenceonthewire.company.api.CompanyService;

public class CompanyModule extends AbstractModule implements ServiceGuiceSupport {

    @Override
    protected void configure() {
        bindService(CompanyService.class, CompanyServiceImpl.class);
    }
}
