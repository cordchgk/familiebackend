package org.cord.daos;

import org.cord.Entities.User;
import system.converter.HashConverter;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;


public class UserDao {

    User user;

    EntityManagerFactory entityManagerFactory;
    EntityManager entityManager;


    private void createManager() {
        this.entityManagerFactory = Persistence.
                createEntityManagerFactory("default");
        this.entityManager = entityManagerFactory.
                createEntityManager();
    }


    public User getByToken(String token) {
        this.createManager();

        Query query = this.entityManager.createNativeQuery("SELECT * FROM users u WHERE u.apitoken = ?", User.class);
        query.setParameter(1, token);

        User toReturn = new User();
        toReturn.setUserid(0);

        try {
            toReturn = (User) query.getSingleResult();
        } catch (NoResultException ignored) {

        }


        this.entityManager.close();
        this.entityManagerFactory.close();
        return toReturn;
    }


    public User getById(User user) {

        this.createManager();

        user = this.entityManager.find(User.class, user.getUserid());

        this.entityManager.close();
        this.entityManagerFactory.close();
        return user;

    }


    public List<User> getAllUsers() {
        this.createManager();
        Query query = this.entityManager.createNativeQuery("SELECT * FROM users", User.class);
        List<User> toReturn = query.getResultList();


        this.entityManager.close();
        this.entityManagerFactory.close();

        return toReturn;

    }

    public User getByCredentials(User user) {
        this.createManager();


        Query query = entityManager.createNativeQuery("SELECT * FROM users WHERE email = ? AND password = ?", User.class);
        query.setParameter(1, user.getEmail());
        query.setParameter(2, user.getPassword());


        User toReturn = new User();

        try {
            toReturn = (User) query.getSingleResult();
        } catch (NoResultException ignored) {

        }


        this.entityManager.close();
        this.entityManagerFactory.close();
        return toReturn;
    }


    public void create() {
        this.createManager();

        User user;

        try {
            user = new User("cord.ch@hotmail.de", HashConverter.sha384("Blasbara123?!"), "Cord", "Göken");
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


        this.entityManager.getTransaction().begin();
        this.entityManager.persist(user);

        this.entityManager.getTransaction().commit();


        this.entityManager.close();
        this.entityManagerFactory.close();

    }


    public boolean createNewUser(User user) {
        this.createManager();

        this.entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();

        User toReturn = this.getByCredentials(user);


        this.entityManager.close();
        this.entityManagerFactory.close();

        return !toReturn.notExists();
    }

    public User updateToken(User user1) {
        this.createManager();
        this.user = user1;


        String generatedString = this.randomizer();


        this.user.setApitoken(generatedString);

        updateUniqueUserToken(this.entityManager, generatedString);


        this.user = this.entityManager.find(User.class, this.user.getUserid());
        this.entityManager.close();
        this.entityManagerFactory.close();
        return this.user;
    }


    private void updateUniqueUserToken(EntityManager entitymanager, String generatedString) {

        try {
            this.user.setApitoken(generatedString);

            entitymanager.getTransaction().begin();
            entitymanager.merge(this.user);
            entitymanager.getTransaction().commit();


        } catch (Exception e) {
            if (e instanceof javax.persistence.RollbackException) {

                String gString = this.randomizer();

                this.user.setApitoken(generatedString);


                updateUniqueUserToken(entitymanager, gString);
            } else {
                e.printStackTrace();
            }


        }


    }

    public void delete() {
        this.createManager();

        User user = this.entityManager.find(User.class, 6);

        this.entityManager.getTransaction().begin();
        this.entityManager.remove(user);
        this.entityManager.getTransaction().commit();

        this.entityManager.close();
        this.entityManagerFactory.close();

    }


    public void deleteUser(User user) {
        this.createManager();


        User toRemove = this.entityManager.find(User.class, user.getUserid());

        this.entityManager.getTransaction().begin();
        this.entityManager.remove(toRemove);
        this.entityManager.getTransaction().commit();


        this.entityManager.close();
        this.entityManagerFactory.close();
    }


    public User updateUser(User user) {
        this.createManager();
        User toUpdate = this.entityManager.find(User.class, user.getUserid());


        this.entityManager.getTransaction().begin();
        if (user.getFirstname() != null && !user.getFirstname().equals(toUpdate.getFirstname())) {
            toUpdate.setFirstname(user.getFirstname());
        }
        if (user.getSurname() != null && !user.getSurname().equals(toUpdate.getSurname())) {
            toUpdate.setSurname(user.getSurname());
        }
        if (user.getAddress() != null && !user.getAddress().equals(toUpdate.getAddress())) {
            toUpdate.setAddress(user.getAddress());
        }
        try {
            if (user.getPassword() != null && !HashConverter.sha384(user.getPassword()).equals(toUpdate.getPassword())) {
                toUpdate.setPassword(HashConverter.sha384(user.getPassword()));
            }
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


        this.entityManager.getTransaction().commit();

        User toReturn = this.entityManager.find(User.class, toUpdate.getUserid());
        this.entityManager.close();
        this.entityManagerFactory.close();
        return toReturn;
    }


    private String randomizer() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 128;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();


    }

}
