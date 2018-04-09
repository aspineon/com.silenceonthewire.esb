package com.silenceonthewire.company.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;
import com.silenceonthewire.exceptions.api.ExternalServiceExceptionSerializer;

import java.util.List;
import java.util.Optional;

public interface CompanyService extends Service {

    // curl http://localhost:52592/api/company
    ServiceCall<NotUsed, Optional<Company>> read(String id);
    // curl http://localhost:52592/api/company/id
    ServiceCall<NotUsed, List<Company>> readAll();
    // curl -H "Content-Type: application/json" -X POST -d
    // '{"id":"1","typeId":"1", "name":"xyz", "phone": "2222", "email":"email@example.com", "taxNumber": "3435435",
    // "street": "street", "city": "city", "state": "state", "country": "country", "postalCode":"3454",
    // "createdAt": "09/04/2018", "updatedAt": "09/04/2018"}'
    // http://localhost:52592/api/company
    ServiceCall<Company, Done> create();
    // curl -H "Content-Type: application/json" -X PUT -d
    // '{"id":"1","typeId":"1", "name":"xyz", "phone": "2222", "email":"email@example.com", "taxNumber": "3435435",
    // "street": "street", "city": "city", "state": "state", "country": "country", "postalCode":"3454",
    // "createdAt": "09/04/2018", "updatedAt": "09/04/2018"}'
    // http://localhost:52592/api/company
    ServiceCall<Company, Done> update();
    // curl -H "Content-Type: application/json" -X DELETE http://localhost:52592/api/company/1
    ServiceCall<NotUsed, Done> delete(String id);

    @Override
    default Descriptor descriptor(){

        return Service.named("company").withCalls(
                Service.restCall(Method.GET, "/api/company", this::readAll),
                Service.restCall(Method.GET, "/api/company/:id", this::read),
                Service.restCall(Method.POST, "/api/company", this::create),
                Service.restCall(Method.PUT, "/api/company", this::update),
                Service.restCall(Method.DELETE, "/api/company/:id", this::delete)
        ).withExceptionSerializer(ExternalServiceExceptionSerializer.INSTANCE)
                .withAutoAcl(true);
    }
}
