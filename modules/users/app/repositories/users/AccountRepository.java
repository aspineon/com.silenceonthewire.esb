package repositories.users;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Transaction;
import models.users.Account;
import models.users.User;
import play.Logger;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class AccountRepository {

    private final DatabaseExecutionContext executionContext;
    private final EbeanServer ebeanServer;


    @Inject
    public AccountRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<Optional<Account>> getById(Long id){

        return supplyAsync(() -> {

            return ebeanServer.find(Account.class).setId(id).findOneOrEmpty();
        }, executionContext);
    }

    public CompletionStage<Optional<Account>> getByUser(Long id){

        return supplyAsync(() -> {

            Optional<User> optionalUser = ebeanServer.find(User.class).setId(id).findOneOrEmpty();
            if(optionalUser.isPresent() && optionalUser != null) {
                return ebeanServer.find(Account.class).where().eq("user", optionalUser.get()).findOneOrEmpty();
            }
            return Optional.empty();
        }, executionContext);
    }

    public CompletionStage<Optional<Account>> getBySsn(String ssn){

        return supplyAsync(() -> {

            return ebeanServer.find(Account.class).where().eq("ssn", ssn).findOneOrEmpty();
        }, executionContext);
    }

    public CompletionStage<Optional<Account>> getByTaxNumber(String taxNumber){

        return supplyAsync(() -> {

            return ebeanServer.find(Account.class).where().eq("taxNumber", taxNumber).findOneOrEmpty();
        }, executionContext);
    }

    public CompletionStage<Optional<Account>> create(Account account){

        return supplyAsync(() -> {

            try {
                account.save();
                return ebeanServer.find(Account.class).setId(account.id).findOneOrEmpty();
            } catch (Exception e){

                Logger.error(e.getMessage(), e);
                return Optional.empty();
            }
        }, executionContext);
    }

    public CompletionStage<Optional<Account>> update(Account account){

        return supplyAsync(() -> {

            Transaction transaction = ebeanServer.beginTransaction();
            Optional<Account> optionalAccount = Optional.empty();

            try {

                if(account.user == null){

                    transaction.end();
                    return optionalAccount;
                }
                account.update();
                transaction.commit();
                optionalAccount = Optional.of(account);
                transaction.end();
            } catch (Exception e){

                Logger.error(e.getMessage(), e);
                transaction.end();
                return Optional.empty();
            } finally {

                return optionalAccount;
            }
        }, executionContext);
    }
}
