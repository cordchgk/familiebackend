package org.cord.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Getter
@Setter
@Entity(name = "User")
public class UserEntity implements Serializable {


    @Column(name = "userid")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "surname")
    private String surname;

    @Column(name = "address")
    private String address;

    @Column(name = "verificationhash")
    private String verificationhash;

    @Column(name = "accountstatus")
    private boolean accountstatus;

    @Column(name = "sessioncookie")
    private String sessioncookie;

    @Column(name = "apitoken", unique = true)
    @JsonIgnore
    private String apitoken;


    @Column(name = "birthday")
    private Date birthday;


    @ManyToMany
    @JoinTable(name = "memberofgroup", joinColumns = @JoinColumn(name = "uid"), inverseJoinColumns = @JoinColumn(name = "gid"))
    private Set<GroupEntity> gruppe;


    public UserEntity(
            String email,
            String password,
            String firstname,
            String surname) {

        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.surname = surname;
    }


    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.id)
                     .append("\n");
        stringBuilder.append(this.email)
                     .append("\n");
        stringBuilder.append(this.password)
                     .append("\n");
        stringBuilder.append(this.firstname)
                     .append("\n");
        stringBuilder.append(this.surname)
                     .append("\n");
        stringBuilder.append(this.address)
                     .append("\n");
        stringBuilder.append(this.birthday)
                     .append("\n");
        stringBuilder.append(this.verificationhash)
                     .append("\n");
        stringBuilder.append(this.accountstatus)
                     .append("\n");
        stringBuilder.append(this.sessioncookie)
                     .append("\n");
        stringBuilder.append(this.apitoken)
                     .append("\n");

        return stringBuilder.toString();
    }


    public boolean notExists() {

        return this.getId() == 0;
    }


    @Override
    public boolean equals(Object o) {

        UserEntity user = (UserEntity) o;
        return this.id == user.id;
    }


    @Override
    public int hashCode() {

        return getClass().hashCode();
    }


}