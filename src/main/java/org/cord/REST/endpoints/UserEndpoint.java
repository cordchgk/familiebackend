package org.cord.REST.endpoints;


import com.google.gson.Gson;
import org.cord.Entities.User;
import org.cord.REST.TestBean;
import org.cord.daos.UserDao;
import system.converter.ToJSON;
import system.converter.HashConverter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Path("/user")
public class UserEndpoint {


    UserDao userDao;


    @Inject
    TestBean testBean;


    @PostConstruct
    public void init() {
        this.userDao = new UserDao();


    }

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByApiToken(@Context HttpHeaders httpHeaders) {



        String api = httpHeaders.getHeaderString("api-token");


        if (api == null) {
            return null;
        } else {

            User user = this.userDao.getByToken(api);
            if (user.notExists()){
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }else{
                return Response.ok(user).header("api-token",api).build();
            }



        }

    }


    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(String userJson, @Context HttpHeaders httpHeaders) {

        testBean.add();

        User user = this.getUserFromJson(userJson);
        if (user == null){
            return Response.status(401).build();
        }

        try {
            user.setPassword(HashConverter.sha384(user.getPassword()));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


        user = this.userDao.getByCredentials(user);


        if (user.notExists()) {
            return Response.status(401).build();
        } else {
            user = this.userDao.updateToken(user);

            return Response.ok(user).header("apitoken", user.getApitoken()).build();

        }


    }


    @GET
    @Path("create")

    public void create(String user) {
        this.userDao.create();
    }

    @POST
    @Path("createUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String newUser) {

        User user = this.getUserFromJson(newUser);
        try {
            user.setPassword(HashConverter.sha384(user.getPassword()));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        if (user.getEmail() == null | user.getPassword() == null | user.getFirstname() == null
                | user.getSurname() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            this.userDao.createNewUser(user);
            this.userDao.updateToken(user);


            return Response.ok(user).header("apitoken", user.getApitoken()).build();
        }


    }


    @POST
    @Path("deleteUser")
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteUser(String user) {

        User userToRemove = this.getUserFromJson(user);

        this.userDao.deleteUser(userToRemove);
    }


    @GET
    @Path(("getAll"))
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAll() {


        return this.userDao.getAllUsers();
    }


    @POST
    @Path(("updateUser"))
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(String user) {

        User toReturn = this.userDao.updateUser(this.getUserFromJson(user));

        return Response.ok(toReturn).header("apitoken", toReturn.getApitoken()).build();
    }


    private User getUserFromJson(String json) {
        return new Gson().fromJson(json, User.class);
    }


}
