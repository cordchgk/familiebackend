package org.cord.rest.endpoints;


import org.cord.Entities.UserEntity;
import org.cord.daos.DAO;
import org.cord.daos.GroupDao;
import org.cord.daos.UserDao;
import system.converter.HashConverter;
import system.converter.JsonToObject;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;


@Path("/user")
@RequestScoped
public class UserEndpoint {


    @Inject
    UserDao userDao;


    DAO DAO;


    @PostConstruct
    public void init() {

        this.DAO = new GroupDao();
    }


    @POST
    @Path("createUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String newUser) {

        UserEntity user = (UserEntity) JsonToObject.getObjectFromJson(
                newUser,
                UserEntity.class);

        if(user.getEmail()
                == null
                | user.getPassword()
                == null
                | user.getFirstname()
                == null
                | user.getSurname()
                == null
                | user.getPassword()
                == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .build();
        }

        try {
            user.setPassword(HashConverter.sha384(user.getPassword()));
        } catch(UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        try {
            this.userDao.createNewUser(user);
        } catch(SQLException e) {
            return Response.status(409)
                           .build();
        }
        this.userDao.updateToken(user);

        user = this.userDao.getByCredentials(user);
        return Response.ok(user)
                       .header(
                               "apitoken",
                               user.getApitoken())
                       .build();

    }


    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(
            String userJson,
            @Context HttpHeaders httpHeaders) {

        UserEntity user = (UserEntity) JsonToObject.getObjectFromJson(
                userJson,
                UserEntity.class);
        if(user
                == null) {
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
                           .header(
                                   "Set-Cookie",
                                   "apitoken="
                                           + user.getApitoken()
                                           + "; HttpOnly; SameSite=None")
                           .header(
                                   "apitoken",
                                   user.getApitoken())
                           .entity(user)
                           .build();

        }

    }


    @GET
    @Path("/logout")
    public Response logout(
            @Context HttpHeaders httpHeaders) {

        String api = httpHeaders.getHeaderString("apitoken");

        UserEntity user = new UserEntity();

        user = this.userDao.getByToken(api);
        user.setApitoken("");
        if(!user.notExists()) {

            this.userDao.updateUser(user);
            user = new UserEntity();
            return Response.ok()
                           .entity(user)
                           .build();
        }
        else {
            return Response.status(401)
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

        UserEntity user = (UserEntity) JsonToObject.getObjectFromJson(
                userJson,
                UserEntity.class);
        user = (UserEntity) this.DAO.getById(
                user,
                UserEntity.class,
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

        String api = httpHeaders.getHeaderString("apitoken");

        if(api
                == null
                || api.equals("")) {
            return Response.status(401)
                           .build();
        }
        else {

            UserEntity user = this.userDao.getByToken(api);
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

        UserEntity toReturn = this.userDao.updateUser((UserEntity) JsonToObject.getObjectFromJson(
                user,
                UserEntity.class));

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

        UserEntity userToRemove = (UserEntity) JsonToObject.getObjectFromJson(
                user,
                UserEntity.class);

        this.userDao.deleteUser(userToRemove);

        return Response.ok()
                       .build();
    }


}
