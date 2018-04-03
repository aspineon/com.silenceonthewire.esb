package modules.users;

import actors.users.AccountActor;
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;

public class AccountModule extends AbstractModule implements AkkaGuiceSupport {

    @Override
    protected void configure() {

        bindActor(AccountActor.class, "account-actor");
    }
}
