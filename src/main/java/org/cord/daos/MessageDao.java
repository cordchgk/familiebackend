package org.cord.daos;

import org.cord.Entities.MessageEntity;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;


@RequestScoped
public class MessageDao extends EMCreator implements DAO {


    public MessageDao() {

        this.createManager();
    }


    @PreDestroy
    public void destroy() {

        this.close();
    }





    public void sendMessage(MessageEntity messageEntity){
        this.entityManager.getTransaction()
                          .begin();
        entityManager.persist(messageEntity);
        entityManager.getTransaction()
                     .commit();
    }


    @Override
    public Object deleteById(Object obj) {

        return null;
    }


}
