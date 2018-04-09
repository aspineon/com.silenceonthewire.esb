package com.silenceonthewire.company.impl;

import akka.Done;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class CompanyEventProcessor extends ReadSideProcessor<CompanyEvent> {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CompanyEventProcessor.class);

    private final CassandraSession session;
    private final CassandraReadSide readSide;

    private PreparedStatement writeCompany;
    private PreparedStatement updateCompany;
    private PreparedStatement deleteCompany;

    @Inject
    public CompanyEventProcessor(final CassandraSession session, final CassandraReadSide readSide){

        this.session = session;
        this.readSide = readSide;
    }

    @Override
    public PSequence<AggregateEventTag<CompanyEvent>> aggregateTags(){

        LOGGER.info(" aggregateTags method ... ");
        return TreePVector.singleton(CompanyEventTag.INSTANCE);
    }

    @Override
    public ReadSideHandler<CompanyEvent> buildHandler() {
        LOGGER.info(" buildHandler method ... ");
        return readSide.<CompanyEvent>builder("company_offset")
                .setGlobalPrepare(this::createTable)
                .setPrepare(evtTag -> prepareWriteCompany()
                        .thenCombine(prepareDeleteCompany(), (d1, d2) -> Done.getInstance())
                        .thenCombine(prepareUpdateCompany(), (d1, d2) -> Done.getInstance())
                )
                .setEventHandler(CompanyEvent.CompanyCreated.class, this::processPostAdded)
                .setEventHandler(CompanyEvent.CompanyUpdated.class, this::processPostUpdated)
                .setEventHandler(CompanyEvent.CompanyDeleted.class, this::processPostDeleted)
                .build();
    }

    private CompletionStage<Done> createTable(){

        return session.executeCreateTable(
                "CREATE TABLE IF NOT EXISTS company (id TEXT PRIMARY KEY, typeId TEXT, name TEXT, phone TEXT, email TEXT, " +
                        "taxNumber TEXT, street TEXT, city TEXT, state TEXT, country TEXT, postalCode TEXT, " +
                        "createdAt TEXT, updatedAt TEXT)"
        );
    }

    private CompletionStage<Done> prepareWriteCompany() {
        return session.prepare(
                "INSERT INTO company (id, typeId, name, phone, email, taxNumber, street, city, state, country, " +
                        "postalCode, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        ).thenApply(ps -> {
            setWriteType(ps);
            return Done.getInstance();
        });
    }

    private void setWriteType(PreparedStatement statement) {
        this.writeCompany = statement;
    }

    private CompletionStage<List<BoundStatement>> processPostAdded(CompanyEvent.CompanyCreated event){

        BoundStatement bindUpdateCompany = updateCompany.bind();
        bindUpdateCompany.setString("typeId", event.company.typeId);
        bindUpdateCompany.setString("name", event.company.name);
        bindUpdateCompany.setString("phone", event.company.phone);
        bindUpdateCompany.setString("email", event.company.email);
        bindUpdateCompany.setString("taxNumber", event.company.taxNumber);
        bindUpdateCompany.setString("street", event.company.street);
        bindUpdateCompany.setString("city", event.company.city);
        bindUpdateCompany.setString("state", event.company.state);
        bindUpdateCompany.setString("country", event.company.country);
        bindUpdateCompany.setString("postalCode", event.company.postalCode);
        bindUpdateCompany.setString("createdAt", event.company.createdAt);
        bindUpdateCompany.setString("updatedAt", event.company.updatedAt);
        bindUpdateCompany.setString("id", event.company.id);
        return CassandraReadSide.completedStatements(Arrays.asList(bindUpdateCompany));
    }

    private CompletionStage<Done> prepareUpdateCompany() {
        return session.prepare(
                "UPDATE company SET typeId = ?, name = ?, phone = ?, email = ?, taxNumber = ?, street = ?, " +
                        "city = ?, state = ?, country = ?, postalCode = ?, createdAt = ?, updatedAt = ? WHERE id = ?"
        ).thenApply(ps -> {
            setUpdateType(ps);
            return Done.getInstance();
        });
    }

    private void setUpdateType(PreparedStatement statement) {
        this.updateCompany = statement;
    }

    private CompletionStage<List<BoundStatement>> processPostUpdated(CompanyEvent.CompanyUpdated event){

        BoundStatement bindWriteCompany = writeCompany.bind();
        bindWriteCompany.setString("id", event.company.id);
        bindWriteCompany.setString("typeId", event.company.typeId);
        bindWriteCompany.setString("name", event.company.name);
        bindWriteCompany.setString("phone", event.company.phone);
        bindWriteCompany.setString("email", event.company.email);
        bindWriteCompany.setString("taxNumber", event.company.taxNumber);
        bindWriteCompany.setString("street", event.company.street);
        bindWriteCompany.setString("city", event.company.city);
        bindWriteCompany.setString("state", event.company.state);
        bindWriteCompany.setString("country", event.company.country);
        bindWriteCompany.setString("postalCode", event.company.postalCode);
        bindWriteCompany.setString("createdAt", event.company.createdAt);
        bindWriteCompany.setString("updatedAt", event.company.updatedAt);
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteCompany));
    }

    private CompletionStage<Done> prepareDeleteCompany() {
        return session.prepare(
                "DELETE FROM company WHERE id=?"
        ).thenApply(ps -> {
            setDeleteCompany(ps);
            return Done.getInstance();
        });
    }

    private void setDeleteCompany(PreparedStatement deleteCompany) {
        this.deleteCompany = deleteCompany;
    }

    private CompletionStage<List<BoundStatement>> processPostDeleted(CompanyEvent.CompanyDeleted event) {
        BoundStatement bindWriteType = deleteCompany.bind();
        bindWriteType.setString("id", event.company.id);
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteType));
    }
}
