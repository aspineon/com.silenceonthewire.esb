package com.silenceonthewire.company.type.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;
import com.silenceonthewire.exceptions.api.ExternalServiceExceptionSerializer;

import java.util.List;
import java.util.Optional;

public interface TypeService extends Service {

    // curl http://localhost:59603//api/type/id
    ServiceCall<NotUsed, Optional<Type>> read(String id);
    // curl http://localhost:59603//api/type
    ServiceCall<NotUsed, List<Type>> readAll();
    // curl -H "Content-Type: application/json" -X POST -d '{"id":"1","name":"xyz", "description": "hello", "timestamp": "timestamp"}' http://localhost:59603/api/type
    ServiceCall<Type, Done> create();
    // curl -H "Content-Type: application/json" -X PUT -d '{"id":"1","name":"xyz", "description": "hello", "timestamp": "timestamp"}' http://localhost:59603/api/type
    ServiceCall<Type, Done> update();
    // curl -H "Content-Type: application/json" -X DELETE http://localhost:59603/api/type/1
    ServiceCall<NotUsed, Done> delete(String id);

    @Override
    default Descriptor descriptor(){

        return Service.named("type").withCalls(
                Service.restCall(Method.GET, "/api/type", this::readAll),
                Service.restCall(Method.GET, "/api/type/:id", this::read),
                Service.restCall(Method.POST, "/api/type", this::create),
                Service.restCall(Method.PUT, "/api/type", this::update),
                Service.restCall(Method.DELETE, "/api/type/:id", this::delete)
        ).withExceptionSerializer(ExternalServiceExceptionSerializer.INSTANCE)
                .withAutoAcl(true);
    }
}
