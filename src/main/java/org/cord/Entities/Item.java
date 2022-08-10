package org.cord.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "Items")
@Table(name = "items")
@Getter
@Setter
public class Item implements Serializable {


    @Id
    @Column(name = "itemid")
    private int itemid;


    @Column(name = "seller_id")

    private int seller_id;

    @Column(name = "name")
    private String name;


    @ManyToOne
    @PrimaryKeyJoinColumn(name = "seller_id")
    @JsonBackReference
    private User user;


}
