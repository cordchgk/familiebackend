package org.cord.rest.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cord.Entities.GroupEntity;
import org.cord.Entities.UserEntity;
import org.cord.daos.GroupDao;
import org.cord.daos.UserDao;
import system.converter.JsonToObject;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/group")
@RequestScoped
public class GroupEndpoint {


    @Inject
    GroupDao groupDao;

    @Inject
    UserDao userDao;


    @OPTIONS
    @Path("{path : .*}")
    public Response options() {

        return Response.ok("")

                       .build();
    }


    @POST
    @Path("/getGroupById")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGroupById(
            @Context HttpHeaders httpHeaders,
            String group) {

        GroupEntity g = (GroupEntity) JsonToObject.getObjectFromJson(
                group,
                GroupEntity.class);
        g = (GroupEntity) this.groupDao.getById(
                g,
                GroupEntity.class,
                this.groupDao.getEntityManager());

        UserEntity user = this.userDao.getByToken(httpHeaders.getHeaderString("apitoken"));

        if(this.groupDao.isUserMember(
                user,
                g)) {

            return Response.ok()
                           .entity(g)
                           .build();
        }
        else {
            return Response.status(403)
                           .build();
        }
    }


    @POST
    @Path("/getUsersByGroupId")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGroupUsersById(
            @Context HttpHeaders httpHeaders,
            String group) {

        GroupEntity toReturn = (GroupEntity) JsonToObject.getObjectFromJson(
                group,
                GroupEntity.class);

        UserEntity user = this.userDao.getByToken(httpHeaders.getHeaderString("apitoken"));

        if(this.groupDao.isUserMember(
                user,
                toReturn)) {

            List<UserEntity> groupUsers = this.groupDao.getGroupUsers(toReturn);

            return Response.ok()
                           .entity(groupUsers)
                           .build();
        }
        else {
            return Response.status(403)
                           .build();
        }

    }


    @GET
    @Path("/getAllUsersGroupByToken")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGroupsByUserToken(
            @Context HttpHeaders httpHeaders) {

        UserEntity user = new UserEntity();
        user.setApitoken(httpHeaders.getHeaderString("apitoken"));
        List<GroupEntity> groups = this.groupDao.getUserGroupsByToken(user);

        return Response.ok(groups)
                       .build();
    }


    @POST
    @Path("/addUserToGroupById")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUserToGroupById(
            @Context HttpHeaders httpHeaders,
            String userAndGroup) {



        JsonNode node = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            node = mapper.readTree(userAndGroup);

        } catch(JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        UserEntity toAdd = new UserEntity();
        toAdd.setId(node.get("userId")
                        .asInt());

        toAdd = (UserEntity) this.userDao.getById(
                toAdd,
                UserEntity.class,
                this.userDao.getEntityManager());
        if(toAdd != null) {
            GroupEntity group = new GroupEntity();
            group.setId(node.get("groupId")
                            .asInt());
            this.groupDao.addUserToGroup(
                    toAdd,
                    group);
            return Response.ok()
                           .build();
        }else{
            return Response.status(409).build();
        }


    }


}
