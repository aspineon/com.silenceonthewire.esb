package com.silenceonthewire.company.type.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.util.Optional;

public class TypeEntity extends PersistentEntity<TypeCommand, TypeEvent, TypeState> {

    @Override
    public Behavior initialBehavior(Optional<TypeState> snapshotState) {
        BehaviorBuilder b = newBehaviorBuilder(
                snapshotState.orElse(TypeState.EMPTY));

        b.setCommandHandler(
                TypeCommand.CreateType.class, (cmd, ctx) -> ctx.thenPersist(
                        new TypeEvent.TypeCreated(entityId(), cmd.type), evt -> ctx.reply(Done.getInstance())
                )
        );

        b.setEventHandler(TypeEvent.TypeCreated.class, evt -> new TypeState(Optional.of(evt.type), false));

        b.setCommandHandler(
                TypeCommand.UpdateType.class, (cmd, ctx) -> ctx.thenPersist(
                        new TypeEvent.TypeUpdated(entityId(), cmd.type), evt -> ctx.reply(Done.getInstance())
                )
        );

        b.setEventHandler(TypeEvent.TypeDeleted.class, evt -> new TypeState(Optional.of(evt.type), false));

        b.setCommandHandler(
                TypeCommand.DeleteType.class, (cmd, ctx) -> ctx.thenPersist(
                        new TypeEvent.TypeDeleted(entityId(), cmd.type), evt -> ctx.reply(Done.getInstance())
                )
        );

        b.setEventHandler(TypeEvent.TypeDeleted.class, evt -> new TypeState(Optional.of(evt.type), false));

        b.setReadOnlyCommandHandler(TypeCommand.TypeCurrentState.class, (cmd, ctx) ->
                ctx.reply(state().type)
        );

        return b.build();
    }
}
