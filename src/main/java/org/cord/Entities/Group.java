package org.cord.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Getter
@Setter
@Entity(name = "Group")
@Table(name = "gruppe")
public class Group implements Serializable {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "gid", nullable = false)
    private int id;

    @Basic
    @Column(name = "name", nullable = true, length = -1)
    private String name;

    @Basic
    @Column(name = "userid", nullable = true)
    private Integer userid;

    @ManyToMany(mappedBy = "gruppe")
    @JsonIgnore
    private Set<User> users;


    @Override
    public boolean equals(Object o) {

        return true;
    }


    @Override
    public int hashCode() {

        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (userid != null ? userid.hashCode() : 0);
        return result;
    }


}
