package repositories.users;

import com.google.common.collect.ImmutableMap;
import defaultData.DefaultCompanies;
import models.users.Company;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class UpdateCompanyTest extends WithApplication {

    private DefaultCompanies defaultCompanies = new DefaultCompanies();

    private Long notExistsId = 3L;
    private Long existsId =1L;

    private String newName = "Example3";
    private String existsName = "Example2";

    private String newEmail = "company3@example.com";
    private String existsEmail = "company2@example.com";

    private String newPhone = "2";
    private String existsPhone = "1";

    private String newTaxNumber = "3";
    private String existsTaxNumber = "2";

    private String address = "Street 1";
    private String city = "City";
    private String state = "State";
    private String country = "country";
    private String postalCode = "1";

    Database database;

    @Before
    public void setUp() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {

            database = Databases.inMemory(
                    "test",
                    ImmutableMap.of(
                            "MODE", "MYSQL"
                    ),
                    ImmutableMap.of(
                            "logStatements", true
                    )
            );
            Evolutions.applyEvolutions(database);

            defaultCompanies.createCompanies();
        });
    }

    @After
    public void tearDown() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {

            defaultCompanies.deleteCompanies();

            Evolutions.cleanupEvolutions(database);
            database.shutdown();
        });
    }

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void successUpdateCompany(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            Company company = createCompanyModel(existsId, newName, newEmail, newPhone, newTaxNumber);

            final CompanyRepository companyRepository = app.injector().instanceOf(CompanyRepository.class);
            final CompletionStage<Optional<Company>> stage = companyRepository.updateCompany(company);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentCompany -> {

                    return currentCompany.isPresent();
                });
            });
        });
    }

    @Test
    public void updateCompanyWithNotExistingId(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            Company company = createCompanyModel(notExistsId, newName, newEmail, newPhone, newTaxNumber);

            final CompanyRepository companyRepository = app.injector().instanceOf(CompanyRepository.class);
            final CompletionStage<Optional<Company>> stage = companyRepository.updateCompany(company);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentCompany -> {

                    return !currentCompany.isPresent();
                });
            });
        });
    }

    @Test
    public void updateCompanyWithExistingName(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            Company company = createCompanyModel(existsId, existsName, newEmail, newPhone, newTaxNumber);

            final CompanyRepository companyRepository = app.injector().instanceOf(CompanyRepository.class);
            final CompletionStage<Optional<Company>> stage = companyRepository.updateCompany(company);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return !currentUser.isPresent();
                });
            });
        });
    }

    @Test
    public void updateCompanyWithExistingEmail(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            Company company = createCompanyModel(existsId, newName, existsEmail, newPhone, newTaxNumber);

            final CompanyRepository companyRepository = app.injector().instanceOf(CompanyRepository.class);
            final CompletionStage<Optional<Company>> stage = companyRepository.updateCompany(company);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return !currentUser.isPresent();
                });
            });
        });
    }

    @Test
    public void updateCompanyWithExistsPhone(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            Company company = createCompanyModel(existsId, newName, newEmail, existsPhone, newTaxNumber);

            final CompanyRepository companyRepository = app.injector().instanceOf(CompanyRepository.class);
            final CompletionStage<Optional<Company>> stage = companyRepository.updateCompany(company);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return !currentUser.isPresent();
                });
            });
        });
    }

    @Test
    public void updateCompanyWithExistingTaxNumber(){

        running(fakeApplication(inMemoryDatabase("test")), () -> {

            Company company = createCompanyModel(existsId, newName, newEmail, newPhone, existsTaxNumber);

            final CompanyRepository companyRepository = app.injector().instanceOf(CompanyRepository.class);
            final CompletionStage<Optional<Company>> stage = companyRepository.updateCompany(company);

            await().atMost(1, TimeUnit.SECONDS).until(() -> {

                assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(currentUser -> {

                    return !currentUser.isPresent();
                });
            });
        });
    }

    private Company createCompanyModel(Long id, String name, String email, String phone, String taxNumber){

        Company company = new Company();
        company.id = id;
        company.name = name;
        company.email = email;
        company.phone = phone;
        company.taxNumber = taxNumber;
        company.address = this.address;
        company.city = this.city;
        company.state = this.state;
        company.country = this.country;
        company.postalCode = this.postalCode;
        company.createdAt = new Date();
        company.updatedAt = new Date();
        return company;
    }
}
