package repositories.users;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Transaction;
import models.users.Company;
import play.Logger;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class CompanyRepository {

    private final DatabaseExecutionContext executionContext;
    private final EbeanServer ebeanServer;


    @Inject
    public CompanyRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<List<Company>> getAll(){

        return supplyAsync(() -> {

            return ebeanServer.find(Company.class).findList();
        }, executionContext);
    }

    public CompletionStage<Optional<Company>> getById(Long id){

        return supplyAsync(() -> {

            return ebeanServer.find(Company.class).setId(id).findOneOrEmpty();
        },executionContext);
    }

    public CompletionStage<Optional<Company>> getByName(String name){

        return supplyAsync(() -> {

            return ebeanServer.find(Company.class).where().eq("name", name).findOneOrEmpty();
        }, executionContext);
    }

    public CompletionStage<Optional<Company>> getByPhone(String phone){

        return supplyAsync(() -> {

            return ebeanServer.find(Company.class).where().eq("phone", phone).findOneOrEmpty();
        }, executionContext);
    }

    public CompletionStage<Optional<Company>> getByTaxNumber(String taxNumber){

        return supplyAsync(() -> {

            return ebeanServer.find(Company.class).where().eq("taxNumber", taxNumber).findOneOrEmpty();
        }, executionContext);
    }

    public CompletionStage<Optional<Company>> getByEmail(String email){

        return supplyAsync(() -> {

            return ebeanServer.find(Company.class).where().eq("email", email).findOneOrEmpty();
        }, executionContext);
    }

    public CompletionStage<Optional<Company>> addCompany(Company company){

        return supplyAsync(() -> {

            try {

                company.save();
                return ebeanServer.find(Company.class).setId(company.id).findOneOrEmpty();
            } catch (Exception e) {

                Logger.error(e.getMessage(), e);
                return Optional.empty();
            }
        }, executionContext);
    }

    public CompletionStage<Optional<Company>> updateCompany(Company company){

        return supplyAsync(() -> {

            Transaction transaction = ebeanServer.beginTransaction();
            Optional<Company> optionalCompany = Optional.empty();
            try {

                company.update();
                transaction.commit();
                optionalCompany = ebeanServer.find(Company.class).setId(company.id).findOneOrEmpty();
            } catch (Exception e) {

                Logger.error(e.getMessage(), e);
            } finally {

                transaction.end();
                return optionalCompany;
            }
        }, executionContext);
    }

    public CompletionStage<Optional<Company>> deleteCompany(Long id){

        return supplyAsync(() -> {

            Optional<Company> optionalCompany = ebeanServer.find(Company.class).setId(id).findOneOrEmpty();
            if(optionalCompany.isPresent()){

                ebeanServer.delete(optionalCompany.get());
            }

            return optionalCompany;
        }, executionContext);
    }
}
