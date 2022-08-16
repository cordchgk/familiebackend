package system;


import io.quarkus.runtime.Startup;
import org.cord.daos.EMFactoryCreator;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;


@Singleton
@Startup
public class Lifecycle {


    @PostConstruct
    public void init() {

        EMFactoryCreator.getInstance();

    }


}
