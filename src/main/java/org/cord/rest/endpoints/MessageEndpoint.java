package org.cord.rest.endpoints;


import io.quarkus.security.User;
import org.cord.Entities.MessageEntity;
import org.cord.Entities.UserEntity;
import org.cord.daos.MessageDao;
import org.cord.daos.UserDao;
import system.converter.JsonToObject;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;

@RequestScoped
@Path(value = "/messages")
public class MessageEndpoint {


    @Inject
    MessageDao messageDao;


    @Inject
    UserDao userDao;


    @POST
    @Path("/sendMessage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendMessage(
            @Context HttpHeaders httpHeaders,
            String message) {

        MessageEntity messageEntity = (MessageEntity) JsonToObject.getObjectFromJson(
                message,
                MessageEntity.class);



        UserEntity sender = this.userDao.getByToken(httpHeaders.getHeaderString("apitoken"));
        messageEntity.setTimedate(new Timestamp(System.currentTimeMillis()));
        messageEntity.setSenderId(sender.getId());
        this.messageDao.sendMessage(messageEntity);

        return Response.ok()
                       .build();
    }


    @OPTIONS
    @Path("{path : .*}")
    public Response options() {

        return Response.ok("")

                       .build();
    }


}
