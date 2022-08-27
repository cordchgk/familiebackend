package org.cord.rest.endpoints;


import io.quarkus.test.junit.QuarkusTest;
import org.cord.Entities.UserEntity;
import org.cord.daos.DAO;
import org.cord.daos.UserDao;
import org.junit.jupiter.api.Test;
import system.converter.HashConverter;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;


@QuarkusTest
class UserEndpointTest {


    @Test
    void getUserByApiToken() {

        String token = this.getToken();

        given().header(
                       "api-token",
                       token)
               .when()
               .get("/rest/user/getByToken")
               .then()
               .statusCode(200);

        given().header(
                       "api-token",
                       "123")
               .when()
               .get("/rest/user/getByToken")
               .then()
               .statusCode(401);
    }


    @Test
    void login() {

        String json = "{\"email\":\"cord.ch@hotmail.de\",\"password\":\"Blasbara123?!\"}";
        given().header(
                       "Content-Type",
                       "application/json")
               .body(json)
               .when()
               .post("/rest/user/login")
               .then()
               .statusCode(200);

        given().header(
                       "Content-Type",
                       "application/json")
               .body("")
               .when()
               .post("/rest/user/login")
               .then()
               .statusCode(401);

    }


    @Test
    void getAll() {

    }


    @Test
    void updateUser() {

    }


    @Test
    void getById() {

        String token = this.getToken();
        given().body("{\"id\":\"1\"}")
               .header(
                       "api-token",
                       token)
               .header(
                       "Content-Type",
                       "application/json")
               .when()
               .post("/rest/user/getById")
               .then()
               .statusCode(200);

        given().body("{\"id\":\"1\"}")
               .header(
                       "api-token",
                       "123")
               .header(
                       "Content-Type",
                       "application/json")
               .when()
               .post("/rest/user/getById")
               .then()
               .statusCode(403);
    }


    @Test
    void deleteUser() {

    }

    private void createDummy() {

        UserDao userDao = new UserDao();

        UserEntity user;

        try {
            user = new UserEntity("cord.ch@hotmail.de", HashConverter.sha384("Blasbara123?!"), "Cord", "Göken");
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try {
            userDao.createNewUser(user);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }

    }


    private String getToken() {

        UserDao userDao = new UserDao();
        DAO dao = userDao;
        userDao.createManager();

        UserEntity user = new UserEntity();
        user.setId(1);
        user = (UserEntity) dao.getById(
                user,
                UserEntity.class,
                userDao.getEntityManager());
        return user.getApitoken();
    }





}