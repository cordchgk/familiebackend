package org.cord.daos;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Field;

public interface DAO<T> {


    default T getById(
            Object obj,
            Class c,EntityManager entityManagerI) {

        int id;

        try {

            Field field = obj.getClass()
                             .getDeclaredField("id");
            field.setAccessible(true);
            id = (int) field.get(obj);

        } catch(NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Object toReturn = entityManagerI.find(
                c,
                id);

        return (T) toReturn;

    }

    T deleteById(Object obj);


}
