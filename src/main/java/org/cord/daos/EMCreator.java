package org.cord.daos;

import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

@Getter
@Setter
@RequestScoped
public class EMCreator {


    EMFactoryCreator emFactoryCreator;

    EntityManager entityManager;


    public void createManager() {

        this.emFactoryCreator = EMFactoryCreator.getInstance();
        this.entityManager = this.emFactoryCreator.createEntityManager();

    }


    public void close() {

        this.entityManager.close();

    }


}
