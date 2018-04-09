package com.silenceonthewire.company.impl;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import com.silenceonthewire.company.api.Company;
import com.silenceonthewire.company.api.CompanyService;
import play.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class CompanyServiceImpl implements CompanyService {

    private final PersistentEntityRegistry persistentEntityRegistry;
    private final CassandraSession session;

    @Inject
    public CompanyServiceImpl(final PersistentEntityRegistry registry, ReadSide readSide, CassandraSession session) {
        this.persistentEntityRegistry = registry;
        this.session = session;

        persistentEntityRegistry.register(CompanyEntity.class);
        readSide.register(CompanyEventProcessor.class);
    }

    @Override
    public ServiceCall<NotUsed, Optional<Company>> read(String id) {

        return request -> {

            CompletionStage<Optional<Company>> companyFuture = session.selectOne(
                    "SELECT * FROM company WHERE id = ?", id
            ).thenApply(
                    currentRow -> currentRow.map(
                            row -> new Company(
                                    row.getString("id"), row.getString("typeId"),
                                    row.getString("name"), row.getString("phone"),
                                    row.getString("email"), row.getString("taxNumber"),
                                    row.getString("street"), row.getString("city"),
                                    row.getString("state"), row.getString("country"),
                                    row.getString("postalCode"), row.getString("createdAt"),
                                    row.getString("updatedAt")
                            )
                    )
            );

            return companyFuture;
        };
    }

    @Override
    public ServiceCall<NotUsed, List<Company>> readAll() {

        return request -> {

            CompletionStage<List<Company>> companiesFuture = session.selectAll("SELECT * FROM company").thenApply(
                    rows -> rows.stream().map(
                            row -> new Company(
                                    row.getString("id"), row.getString("typeId"),
                                    row.getString("name"), row.getString("phone"),
                                    row.getString("email"), row.getString("taxNumber"),
                                    row.getString("street"), row.getString("city"),
                                    row.getString("state"), row.getString("country"),
                                    row.getString("postalCode"), row.getString("createdAt"),
                                    row.getString("updatedAt")
                            )
                    ).collect(Collectors.toList())
            );
            return companiesFuture;
        };
    }

    @Override
    public ServiceCall<Company, Done> create() {

        return company -> {

            PersistentEntityRef<CompanyCommand> ref = typeEntityRef(company);
            return ref.ask(new CompanyCommand.CreateCompany(company));
        };
    }

    @Override
    public ServiceCall<Company, Done> update() {

        return company -> {

            PersistentEntityRef<CompanyCommand> ref = typeEntityRef(company);
            return ref.ask(new CompanyCommand.UpdateCompany(company));
        };
    }

    @Override
    public ServiceCall<NotUsed, Done> delete(String id) {

        return request -> {
            CompletionStage<Optional<Company>> companyFuture =
                    session.selectOne("SELECT * FROM company WHERE id = ?", id).thenApply(
                            currentRow -> currentRow.map(
                                    row -> new Company(
                                            row.getString("id"), row.getString("typeId"),
                                            row.getString("name"), row.getString("phone"),
                                            row.getString("email"), row.getString("taxNumber"),
                                            row.getString("street"), row.getString("city"),
                                            row.getString("state"), row.getString("country"),
                                            row.getString("postalCode"), row.getString("createdAt"),
                                            row.getString("updatedAt")
                                    )
                            )
                    );
            try {
                Company company = companyFuture.toCompletableFuture().get().get();
                PersistentEntityRef<CompanyCommand> ref = typeEntityRef(company);
                return ref.ask(new CompanyCommand.DeleteCompany(company));

            } catch (InterruptedException e) {
                e.printStackTrace();
                return CompletableFuture.completedFuture(Done.getInstance());
            } catch (ExecutionException e) {
                e.printStackTrace();
                return CompletableFuture.completedFuture(Done.getInstance());
            } catch (Exception e) {
                e.printStackTrace();
                return CompletableFuture.completedFuture(Done.getInstance());
            }
        };
    }

    private PersistentEntityRef<CompanyCommand> typeEntityRef(Company company) {
        return persistentEntityRegistry.refFor(CompanyEntity.class, company.id);
    }
}
