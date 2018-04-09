package com.silenceonthewire.company.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.util.Optional;

public class CompanyEntity extends PersistentEntity<CompanyCommand, CompanyEvent, CompanyState> {

    @Override
    public Behavior initialBehavior(Optional<CompanyState> snapshotState) {
        BehaviorBuilder b = newBehaviorBuilder(
                snapshotState.orElse(CompanyState.EMPTY));

        b.setCommandHandler(
                CompanyCommand.CreateCompany.class, (cmd, ctx) -> ctx.thenPersist(
                        new CompanyEvent.CompanyCreated(entityId(), cmd.company), evt -> ctx.reply(Done.getInstance())
                )
        );

        b.setEventHandler(CompanyEvent.CompanyCreated.class, evt -> new CompanyState(Optional.of(evt.company), false));

        b.setCommandHandler(
                CompanyCommand.UpdateCompany.class, (cmd, ctx) -> ctx.thenPersist(
                        new CompanyEvent.CompanyUpdated(entityId(), cmd.company), evt -> ctx.reply(Done.getInstance())
                )
        );

        b.setEventHandler(CompanyEvent.CompanyDeleted.class, evt -> new CompanyState(Optional.of(evt.company), false));

        b.setCommandHandler(
                CompanyCommand.DeleteCompany.class, (cmd, ctx) -> ctx.thenPersist(
                        new CompanyEvent.CompanyDeleted(entityId(), cmd.company), evt -> ctx.reply(Done.getInstance())
                )
        );

        b.setEventHandler(CompanyEvent.CompanyDeleted.class, evt -> new CompanyState(Optional.of(evt.company), false));

        b.setReadOnlyCommandHandler(CompanyCommand.CompanyCurrentState.class, (cmd, ctx) ->
                ctx.reply(state().company)
        );

        return b.build();
    }
}
