package org.cord.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Getter
@Setter
@Entity(name = "User")
public class User implements Serializable {


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


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Item> items;


    public User(
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


        User user = (User) o;
        return this.id == user.id;
    }


    @Override
    public int hashCode() {

        return getClass().hashCode();
    }




}