package com.silenceonthewire.company.type.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.silenceonthewire.company.type.api.TypeService;

/**
 * The module that binds the CompanyService so that it can be served.
 */
public class TypeModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(TypeService.class, TypeServiceImpl.class);
    }
}
