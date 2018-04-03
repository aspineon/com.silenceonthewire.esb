import com.google.inject.AbstractModule;
import java.time.Clock;

import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import org.avaje.datasource.DataSourceConfig;
import com.typesafe.config.Config;
import play.Environment;
import services.ApplicationTimer;
import services.AtomicCounter;
import services.Counter;

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 *
 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
public class Module extends AbstractModule {

    private final Environment environment;
    private final Config config;

    public Module(
            Environment environment,
            Config config
    ) {
        this.environment = environment;
        this.config = config;
    }

    @Override
    public void configure() {
        // Use the system clock as the default implementation of Clock
        bind(Clock.class).toInstance(Clock.systemDefaultZone());
        // Ask Guice to create an instance of ApplicationTimer when the
        // application starts.
        bind(ApplicationTimer.class).asEagerSingleton();
        // Set AtomicCounter as the implementation for Counter.
        bind(Counter.class).to(AtomicCounter.class);

        ServerConfig ebeanConfig = new ServerConfig();
        DataSourceConfig db = new DataSourceConfig();
        db.setDriver(config.getString("db.default.driver"));
        db.setUsername(config.getString("db.default.username"));
        db.setPassword(config.getString("db.default.password"));
        db.setUrl(config.getString("db.default.url"));

        ebeanConfig.setDefaultServer(true);
        ebeanConfig.setDataSourceConfig(db);
        EbeanServerFactory.create(ebeanConfig);
    }

}
