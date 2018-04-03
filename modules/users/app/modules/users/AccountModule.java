package modules.users;

import actors.users.CompanyActor;
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;

public class AccountModule extends AbstractModule implements AkkaGuiceSupport {

    @Override
    protected void configure() {

        bindActor(CompanyActor.class, "account-actor");
    }
}
