package org.cord.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;


@Getter
@Setter
@Entity
@Table(name = "messages", schema = "public", catalog = "postgres")
public class MessageEntity {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "mid", nullable = false)
    private int id;

    @Basic
    @Column(name = "sender_id", nullable = true)
    private int senderId;

    @Basic
    @Column(name = "receiver_id", nullable = true)
    private int receiverId;

    @Basic
    @Column(name = "timedate", nullable = true)
    private Timestamp timedate;

    @Basic
    @Column(name = "message", nullable = true, length = -1)
    private String message;




    @Override
    public boolean equals(Object o) {

        if(this
                == o) {
            return true;
        }
        if(o
                == null
                || getClass()
                != o.getClass()) {
            return false;
        }

        MessageEntity that = (MessageEntity) o;

        if(id
                != that.id) {
            return false;
        }
        if(!Objects.equals(
                senderId,
                that.senderId)) {
            return false;
        }
        if(!Objects.equals(
                receiverId,
                that.receiverId)) {
            return false;
        }
        if(!Objects.equals(
                timedate,
                that.timedate)) {
            return false;
        }
        if(!Objects.equals(
                message,
                that.message)) {
            return false;
        }

        return true;
    }





}
