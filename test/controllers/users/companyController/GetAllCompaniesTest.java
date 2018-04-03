package controllers.users.companyController;

import com.google.common.collect.ImmutableMap;
import controllers.users.routes;
import defaultData.DefaultCompanies;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static play.mvc.Http.HttpVerbs.GET;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

public class GetAllCompaniesTest extends WithApplication {

    private DefaultCompanies defaultCompanies = new DefaultCompanies();

    Database database;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Before
    public void setUp(){

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
    }

    @After
    public void tearDown(){

        defaultCompanies.deleteCompanies();

        Evolutions.cleanupEvolutions(database);
        database.shutdown();
    }

    @Test
    public void getCompaniesTest(){

        Result result = route(app, fakeRequest().method(Helpers.GET).uri(controllers.users.routes.CompanyController.all().url()));

        result.body().dataStream();
    }

}
