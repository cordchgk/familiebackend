package org.cord.rest.endpoints;


import org.cord.Entities.Group;
import org.cord.Entities.User;
import org.cord.daos.DAO;
import org.cord.daos.GroupDao;
import org.cord.daos.UserDao;
import system.converter.JsonToObject;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Path("/group")
@RequestScoped
public class GroupEndpoint {


    GroupDao groupDao;

    @OPTIONS
    @Path("{path : .*}")
    public Response options() {

        return Response.ok("")

                       .build();
    }
    @PostConstruct
    public void init() {

        this.groupDao = new GroupDao();

    }


    @GET
    @Path("/getAllUsersGroupByToken")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGroupsByUserToken(
            @Context HttpHeaders httpHeaders) {

        User user = new User();
        user.setApitoken(httpHeaders.getHeaderString("apitoken"));
        List<Group> groups = this.groupDao.getUserGroupsByToken(user);
        return Response.ok(groups)
                       .build();
    }


}
