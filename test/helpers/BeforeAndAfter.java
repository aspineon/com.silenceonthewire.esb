package helpers;

import com.google.common.collect.ImmutableMap;
import defaultData.DefaultUsers;
import org.junit.After;
import org.junit.Before;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class BeforeAndAfter {

    private final DefaultUsers defaultUsers = new DefaultUsers();

    Database database;

    @Before
    public void setUp() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {

            database = Databases.inMemory(
                    "mydatabase",
                    ImmutableMap.of(
                            "MODE", "MYSQL"
                    ),
                    ImmutableMap.of(
                            "logStatements", true
                    )
            );
            Evolutions.applyEvolutions(this.database);

            defaultUsers.createUsers();
        });
    }

    @After
    public void tearDown() throws Exception {
        running(fakeApplication(inMemoryDatabase("test")), () -> {

            this.defaultUsers.deleteUsers();

            Evolutions.cleanupEvolutions(this.database);
            this.database.shutdown();
        });
    }
}
