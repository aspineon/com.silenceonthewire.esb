package repositories.users;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Transaction;
import models.users.EmailAndPassword;
import models.users.PhoneAndPassword;
import models.users.User;
import play.Logger;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class UserRepository {

    private final DatabaseExecutionContext executionContext;
    private final EbeanServer ebeanServer;


    @Inject
    public UserRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<List<User>> getAll(){

        return supplyAsync(() -> {

            return ebeanServer.find(User.class).findList();
        }, executionContext);
    }

    public CompletionStage<Optional<User>> addUser(User user){

        return supplyAsync(()-> {

            try {
                ebeanServer.insert(user);
                return ebeanServer.find(User.class).setId(user.id).findOneOrEmpty();
            } catch (Exception e){

                Logger.error(e.getMessage(), e);
                return Optional.empty();
            }
        }, executionContext);
    }

    public CompletionStage<Optional<User>> updateUser(User user){

        return supplyAsync(() -> {

            Transaction txn = ebeanServer.beginTransaction();
            Optional<User> optionalUser = Optional.empty();
            try {

                ebeanServer.update(user);
                txn.commit();
                optionalUser = ebeanServer.find(User.class).setId(user.id).findOneOrEmpty();
            } finally {

                txn.end();
                return optionalUser;
            }
        }, executionContext);
    }

    public CompletionStage<Optional<User>> deleteUser(Long id){

        return supplyAsync(() -> {
            Optional<User> userOptional = ebeanServer.find(User.class).setId(id).findOneOrEmpty();
            if(userOptional.isPresent()) {
                ebeanServer.delete(userOptional.get());
                return userOptional;
            }
            return Optional.empty();
        }, executionContext);
    }

    public CompletionStage<Optional<User>> getById(Long id){

        return supplyAsync(() -> {

            return ebeanServer.find(User.class).setId(id).findOneOrEmpty();
        }, executionContext);
    }

    public CompletionStage<Optional<User>> getByEmail(String email){

        return supplyAsync(() -> {

            return ebeanServer.find(User.class).where().eq("email", email.toLowerCase()).findOneOrEmpty();
        }, executionContext);
    }

    public CompletionStage<Optional<User>> getByPhone(String phone){

        return supplyAsync(() -> {

            return ebeanServer.find(User.class).where().eq("phone", phone).findOneOrEmpty();
        }, executionContext);
    }

    public CompletionStage<Optional<User>> getByEmailAndPassword(EmailAndPassword emailAndPassword){

        return supplyAsync(() -> {

            return ebeanServer.find(User.class).where().eq("email", emailAndPassword.email.toLowerCase())
                    .eq("password", User.getSha512(emailAndPassword.password)).findOneOrEmpty();
        }, executionContext);
    }

    public CompletionStage<Optional<User>> getByPhoneAndPassword(PhoneAndPassword phoneAndPassword){

        return supplyAsync(() -> {

            return ebeanServer.find(User.class).where().eq("phone", phoneAndPassword.phone)
                    .eq("password", User.getSha512(phoneAndPassword.password)).findOneOrEmpty();
        });
    }
}
