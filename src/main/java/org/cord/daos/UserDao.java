package org.cord.daos;

import lombok.Getter;
import lombok.Setter;
import org.cord.Entities.UserEntity;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;


@Getter
@Setter
@RequestScoped
public class UserDao extends EMCreator implements DAO, Serializable {


    UserEntity user;


    @PostConstruct
    public void init() {

        this.createManager();
    }


    @PreDestroy
    public void destroy() {

        this.close();

    }


    public boolean createNewUser(UserEntity user) throws SQLException {

        try {
            this.entityManager.getTransaction()
                              .begin();
            entityManager.persist(user);
            entityManager.getTransaction()
                         .commit();

        } catch(Exception e) {
            throw new SQLException();
        }

        UserEntity toReturn = this.getByCredentials(user);

        return !toReturn.notExists();
    }


    public UserEntity getByToken(String token) {

        Query query = this.entityManager.createNativeQuery(
                "SELECT * FROM users u WHERE u.apitoken = ?",
                UserEntity.class);
        query.setParameter(
                1,
                token);

        UserEntity toReturn = new UserEntity();
        toReturn.setId(0);

        try {
            toReturn = (UserEntity) query.getSingleResult();
        } catch(NoResultException ignored) {

        }

        return toReturn;
    }


    public List<UserEntity> getAllUsers() {

        Query query = this.entityManager.createNativeQuery(
                "SELECT * FROM users",
                UserEntity.class);
        List<UserEntity> toReturn = query.getResultList();

        return toReturn;

    }





    public UserEntity getByCredentials(UserEntity user) {

        this.createManager();
        Query query = entityManager.createNativeQuery(
                "SELECT * FROM users WHERE email = ? AND password = ?",
                UserEntity.class);
        query.setParameter(
                1,
                user.getEmail());
        query.setParameter(
                2,
                user.getPassword());

        UserEntity toReturn = new UserEntity();

        try {
            toReturn = (UserEntity) query.getSingleResult();
        } catch(NoResultException ignored) {

        }

        return toReturn;
    }


    public UserEntity updateToken(UserEntity user1) {

        this.createManager();
        this.user = user1;

        String generatedString = this.randomizer();

        this.user.setApitoken(generatedString);

        updateUniqueUserToken(generatedString);

        this.user = this.entityManager.find(
                UserEntity.class,
                this.user.getId());

        return this.user;
    }


    private void updateUniqueUserToken(
            String generatedString) {

        try {
            this.user.setApitoken(generatedString);

            this.entityManager.getTransaction()
                              .begin();
            this.entityManager.merge(this.user);
            this.entityManager.getTransaction()
                              .commit();

        } catch(Exception e) {
            if(e instanceof javax.persistence.RollbackException) {

                String gString = this.randomizer();

                this.user.setApitoken(generatedString);

                updateUniqueUserToken(gString);
            }
            else {
                e.printStackTrace();
            }

        }

    }


    public UserEntity updateUser(UserEntity user) {

        UserEntity toUpdate = this.entityManager.find(
                UserEntity.class,
                user.getId());

        this.entityManager.getTransaction()
                          .begin();
        if(user.getFirstname() != null && !user.getFirstname()
                                               .equals(toUpdate.getFirstname())) {
            toUpdate.setFirstname(user.getFirstname());
        }
        if(user.getSurname() != null && !user.getSurname()
                                             .equals(toUpdate.getSurname())) {
            toUpdate.setSurname(user.getSurname());
        }
        if(user.getAddress() != null && !user.getAddress()
                                             .equals(toUpdate.getAddress())) {
            toUpdate.setAddress(user.getAddress());
        }

        this.entityManager.getTransaction()
                          .commit();

        UserEntity toReturn = this.entityManager.find(
                UserEntity.class,
                toUpdate.getId());

        return toReturn;
    }


    public void deleteUser(UserEntity user) {

    }


    @Override
    public Object deleteById(Object obj) {

        this.user = (UserEntity) obj;

        UserEntity toRemove = this.entityManager.find(
                UserEntity.class,
                user.getId());

        this.entityManager.getTransaction()
                          .begin();
        this.entityManager.remove(toRemove);
        this.entityManager.getTransaction()
                          .commit();

        this.close();
        return null;
    }


    private String randomizer() {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 128;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for(int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();

    }


}
