package org.cord.REST.endpoints;


import org.junit.jupiter.api.Test;


import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.support.discovery.SelectorResolver;

import javax.json.Json;

import java.util.HashMap;

import static io.restassured.RestAssured.given;




import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
class UserEndpointTest {

    @Test
    void init() {
    }

    @Test
    void getUserByApiToken() {


        given().header("api-token", "aslykiyltvsxodfrxmqxumeomfzxhzhsuopbqinyjkrhfntdrurbvneqsgceiejoqsfsyulnahiaifgfsiuaafbkzsfwmnusruidbihurxuyfswkhlpqeweseabxeznr")
                .when()
                .get("/rest/user/get")
                .then()
                .statusCode(200);


        given().header("api-token", "123")
                .when()
                .get("/rest/user/get")
                .then()
                .statusCode(401);
    }

    @Test
    void login() {
        String json = "{\"email\":\"cord.ch@hotmail.de\",\"password\":\"Blasbara123?!\"}";
        given()
                .header("Content-Type","application/json")
                .body(json)
                .when()
                .post("/rest/user/login")
                .then()
                .statusCode(200);


        given()
                .header("Content-Type","application/json")
                .body("")
                .when()
                .post("/rest/user/login")
                .then()
                .statusCode(401);

    }

    @Test
    void create() {
    }

    @Test
    void createUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getAll() {

    }

    @Test
    void updateUser() {
    }
}