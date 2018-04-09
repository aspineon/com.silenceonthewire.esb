package com.silenceonthewire.company.type.impl;

import akka.Done;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import com.silenceonthewire.company.type.impl.TypeEvent.TypeDeleted;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static com.silenceonthewire.company.type.impl.TypeEvent.*;

public class TypeEventProcessor extends ReadSideProcessor<TypeEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TypeEventProcessor.class);

    private final CassandraSession session;
    private final CassandraReadSide readSide;

    private PreparedStatement writeType;
    private PreparedStatement updateType;
    private PreparedStatement deleteType;

    @Inject
    public TypeEventProcessor(final CassandraSession session, final CassandraReadSide readSide) {
        this.session = session;
        this.readSide = readSide;
    }

    @Override
    public PSequence<AggregateEventTag<TypeEvent>> aggregateTags() {
        LOGGER.info(" aggregateTags method ... ");
        return TreePVector.singleton(TypeEventTag.INSTANCE);
    }

    @Override
    public ReadSideHandler<TypeEvent> buildHandler() {
        LOGGER.info(" buildHandler method ... ");
        return readSide.<TypeEvent>builder("company_type_offset")
                .setGlobalPrepare(this::createTable)
                .setPrepare(evtTag -> prepareWriteType()
                        .thenCombine(prepareDeleteType(), (d1, d2) -> Done.getInstance())
                        .thenCombine(prepareUpdateType(), (d1, d2) -> Done.getInstance())
                )
                .setEventHandler(TypeCreated.class, this::processPostAdded)
                .setEventHandler(TypeUpdated.class, this::processPostUpdated)
                .setEventHandler(TypeDeleted.class, this::processPostDeleted)
                .build();
    }

    private CompletionStage<Done> createTable() {
        return session.executeCreateTable(
                "CREATE TABLE IF NOT EXISTS type ( " +
                        "id TEXT, name TEXT, description TEXT, timestamp text, PRIMARY KEY(id))"
        );
    }

    private CompletionStage<Done> prepareWriteType() {
        return session.prepare(
                "INSERT INTO type (id, name, description, timestamp) VALUES (?, ?, ?, ?)"
        ).thenApply(ps -> {
            setWriteType(ps);
            return Done.getInstance();
        });
    }

    private void setWriteType(PreparedStatement statement) {
        this.writeType = statement;
    }

    private CompletionStage<List<BoundStatement>> processPostAdded(TypeCreated event) {
        BoundStatement bindWriteType = writeType.bind();
        bindWriteType.setString("id", event.type.id);
        bindWriteType.setString("name", event.type.name);
        bindWriteType.setString("description", event.type.description);
        bindWriteType.setString("timestamp", event.type.timestamp);
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteType));
    }

    private CompletionStage<Done> prepareUpdateType() {
        return session.prepare(
                "UPDATE type SET name = ?, description = ?, timestamp = ? WHERE id = ?"
        ).thenApply(ps -> {
            setWriteType(ps);
            return Done.getInstance();
        });
    }

    private void setUpdateType(PreparedStatement statement) {
        this.updateType = statement;
    }

    private CompletionStage<List<BoundStatement>> processPostUpdated(TypeUpdated event) {
        BoundStatement bindUpdateType = updateType.bind();
        bindUpdateType.setString("name", event.type.name);
        bindUpdateType.setString("description", event.type.description);
        bindUpdateType.setString("timestamp", event.type.timestamp);
        bindUpdateType.setString("id", event.type.id);
        return CassandraReadSide.completedStatements(Arrays.asList(bindUpdateType));
    }

    private CompletionStage<Done> prepareDeleteType() {
        return session.prepare(
                "DELETE FROM type WHERE id=?"
        ).thenApply(ps -> {
            setDeleteType(ps);
            return Done.getInstance();
        });
    }

    private void setDeleteType(PreparedStatement deleteType) {
        this.deleteType = deleteType;
    }

    private CompletionStage<List<BoundStatement>> processPostDeleted(TypeDeleted event) {
        BoundStatement bindWriteType = deleteType.bind();
        bindWriteType.setString("id", event.type.id);
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteType));
    }
}
