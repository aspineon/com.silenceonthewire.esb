package com.silenceonthewire.company.type.impl;

import akka.Done;
import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import com.silenceonthewire.company.type.api.Type;
import com.silenceonthewire.company.type.api.TypeService;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class TypeServiceImpl implements TypeService {

    private final PersistentEntityRegistry persistentEntityRegistry;
    private final CassandraSession session;

    @Inject
    public TypeServiceImpl(final PersistentEntityRegistry registry, ReadSide readSide, CassandraSession session) {
        this.persistentEntityRegistry = registry;
        this.session = session;

        persistentEntityRegistry.register(TypeEntity.class);
        readSide.register(TypeEventProcessor.class);
    }

    @Override
    public ServiceCall<NotUsed, Optional<Type>> read(String id) {

        return request -> {
            CompletionStage<Optional<Type>> typeFuture =
                    session.selectOne("SELECT * FROM type WHERE id = ?", id).thenApply(
                            currentRow -> currentRow.map(
                                    row -> new Type(
                                            row.getString("id"), row.getString("name"),
                                            row.getString("description"), row.getString("timestamp")
                                    )
                            )
                    );
            return typeFuture;
        };
    }

    @Override
    public ServiceCall<NotUsed, List<Type>> readAll() {

        return request -> {
            CompletionStage<List<Type>> typeFuture = session.selectAll("select * from type").thenApply(
                    rows -> rows.stream().map(
                            row -> new Type(
                                    row.getString("id"), row.getString("name"),
                                    row.getString("description"), row.getString("timestamp")
                            )
                    ).collect(Collectors.toList())
            );
            return typeFuture;
        };
    }

    @Override
    public ServiceCall<Type, Done> create() {

        return type -> {

            PersistentEntityRef<TypeCommand> ref = typeEntityRef(type);
            return ref.ask(new TypeCommand.CreateType(type));
        };
    }

    @Override
    public ServiceCall<Type, Done> update() {

        return type -> {

            PersistentEntityRef<TypeCommand> ref = typeEntityRef(type);
            return ref.ask(new TypeCommand.UpdateType(type));
        };
    }

    @Override
    public ServiceCall<NotUsed, Done> delete(String id) {

        return request -> {
            CompletionStage<Optional<Type>> typeFuture =
                    session.selectOne("SELECT * FROM type WHERE id = ?", id).thenApply(
                            currentRow -> currentRow.map(
                                    row -> new Type(
                                            row.getString("id"), row.getString("name"),
                                            row.getString("description"), row.getString("timestamp")
                                    )
                            )
                    );
            try {
                Type type = typeFuture.toCompletableFuture().get().get();
                PersistentEntityRef<TypeCommand> ref = typeEntityRef(type);
                return ref.ask(new TypeCommand.DeleteType(type));
            } catch (InterruptedException e) {
                e.printStackTrace();
                return CompletableFuture.completedFuture(Done.getInstance());
            } catch (ExecutionException e) {
                e.printStackTrace();
                return CompletableFuture.completedFuture(Done.getInstance());
            } catch (Exception e){
                e.printStackTrace();
                return CompletableFuture.completedFuture(Done.getInstance());
            }
        };
    }

    private PersistentEntityRef<TypeCommand> typeEntityRef(Type type) {
        return persistentEntityRegistry.refFor(TypeEntity.class, type.id);
    }
}
