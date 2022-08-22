package org.cord.daos;

import lombok.Getter;
import lombok.Setter;
import org.cord.Entities.User;
import system.converter.HashConverter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;


@Getter
@Setter
@RequestScoped
public class UserDao extends EMCreator implements DAO, Serializable {


    User user;


    @PostConstruct
    public void init() {

        this.createManager();
    }


    @PreDestroy
    public void destroy() {

        this.close();

    }


    public boolean createNewUser(User user) {

        this.entityManager.getTransaction()
                          .begin();
        entityManager.persist(user);
        entityManager.getTransaction()
                     .commit();

        User toReturn = this.getByCredentials(user);


        return !toReturn.notExists();
    }


    public User getByToken(String token) {

        Query query = this.entityManager.createNativeQuery(
                "SELECT * FROM users u WHERE u.apitoken = ?",
                User.class);
        query.setParameter(
                1,
                token);

        User toReturn = new User();
        toReturn.setId(0);

        try {
            toReturn = (User) query.getSingleResult();
        } catch(NoResultException ignored) {

        }

        return toReturn;
    }


    public List<User> getAllUsers() {

        Query query = this.entityManager.createNativeQuery(
                "SELECT * FROM users",
                User.class);
        List<User> toReturn = query.getResultList();



        return toReturn;

    }


    public User getByCredentials(User user) {

        this.createManager();
        Query query = entityManager.createNativeQuery(
                "SELECT * FROM users WHERE email = ? AND password = ?",
                User.class);
        query.setParameter(
                1,
                user.getEmail());
        query.setParameter(
                2,
                user.getPassword());

        User toReturn = new User();

        try {
            toReturn = (User) query.getSingleResult();
        } catch(NoResultException ignored) {

        }


        return toReturn;
    }


    public User updateToken(User user1) {

        this.createManager();
        this.user = user1;

        String generatedString = this.randomizer();

        this.user.setApitoken(generatedString);

        updateUniqueUserToken(generatedString);

        this.user = this.entityManager.find(
                User.class,
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


    public User updateUser(User user) {

        User toUpdate = this.entityManager.find(
                User.class,
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

        User toReturn = this.entityManager.find(
                User.class,
                toUpdate.getId());



        return toReturn;
    }


    public void deleteUser(User user) {

    }


    @Override
    public Object deleteById(Object obj) {

        this.user = (User) obj;

        User toRemove = this.entityManager.find(
                User.class,
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
