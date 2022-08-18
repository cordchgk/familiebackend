package org.cord.rest.endpoints;


import org.cord.Entities.User;
import org.cord.daos.DAO;
import org.cord.daos.UserDao;
import system.converter.HashConverter;
import system.converter.JsonToObject;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Path("/user")
@RequestScoped
public class UserEndpoint {


    @Inject
    UserDao userDao;


    DAO DAO;


    @PostConstruct
    public void init() {

        this.DAO = userDao;
    }


    @POST
    @Path("createUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String newUser) {

        User user = (User) JsonToObject.getObjectFromJson(
                newUser,
                User.class);
        try {
            user.setPassword(HashConverter.sha384(user.getPassword()));
        } catch(UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        if(user.getEmail() == null | user.getPassword() == null | user.getFirstname() == null | user.getSurname() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .build();
        }
        else {
            this.userDao.createNewUser(user);
            this.userDao.updateToken(user);

            return Response.ok(user)
                           .header(
                                   "apitoken",
                                   user.getApitoken())
                           .build();
        }

    }


    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(
            String userJson,
            @Context HttpHeaders httpHeaders) {

        System.out.println(userJson);

        User user = (User) JsonToObject.getObjectFromJson(
                userJson,
                User.class);
        if(user == null) {
            return Response.status(401)

                           .build();
        }

        try {
            user.setPassword(HashConverter.sha384(user.getPassword()));
        } catch(UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        this.userDao.createManager();
        user = this.userDao.getByCredentials(user);

        if(user.notExists()) {
            return Response.status(401)

                           .build();
        }
        else {
            user = this.userDao.updateToken(user);

            return Response.status(200)
                           .header("apitoken",
                                   user.getApitoken())
                           .entity(user)
                           .build();

        }

    }


    @OPTIONS
    @Path("{path : .*}")
    public Response options() {

        return Response.ok("")

                       .build();
    }


    @POST
    @Path(("getById"))
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getById(
            @Context HttpHeaders httpHeaders,
            String userJson) {

        User user = (User) JsonToObject.getObjectFromJson(
                userJson,
                User.class);
        user = (User) this.DAO.getById(
                user,
                User.class,
                this.userDao.getEntityManager());

        if(httpHeaders.getHeaderString("api-token")
                      .equals(user.getApitoken())) {
            return Response.ok(user)
                           .build();
        }
        return Response.status(403)
                       .build();

    }


    @GET
    @Path("/getByToken")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByToken(@Context HttpHeaders httpHeaders) {

        String api = httpHeaders.getHeaderString("api-token");

        if(api == null) {
            return null;
        }
        else {

            User user = this.userDao.getByToken(api);
            if(user.notExists()) {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .build();
            }
            else {
                return Response.ok(user)
                               .header(
                                       "api-token",
                                       api)
                               .build();
            }

        }

    }


    @POST
    @Path(("updateUser"))
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(String user) {

        User toReturn = this.userDao.updateUser((User) JsonToObject.getObjectFromJson(
                user,
                User.class));

        return Response.ok(toReturn)
                       .header(
                               "apitoken",
                               toReturn.getApitoken())
                       .build();
    }


    @POST
    @Path("deleteUser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(String user) {

        User userToRemove = (User) JsonToObject.getObjectFromJson(
                user,
                User.class);

        this.userDao.deleteUser(userToRemove);

        return Response.ok()
                       .build();
    }


}
