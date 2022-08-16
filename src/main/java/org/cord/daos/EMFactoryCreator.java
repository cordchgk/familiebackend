package org.cord.daos;


import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Serializable;


@Getter
@Setter
@ApplicationScoped
public class EMFactoryCreator implements Serializable {


    private static final EMFactoryCreator instance = new EMFactoryCreator();


    public static EMFactoryCreator getInstance() {

        return instance;
    }


    EntityManagerFactory entityManagerFactory;


    private EMFactoryCreator() {

        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }


    public EntityManager createEntityManager() {

        return this.entityManagerFactory.createEntityManager();
    }


}
