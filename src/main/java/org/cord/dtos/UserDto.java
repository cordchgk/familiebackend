package org.cord.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    private final int id;
    private final String email;
    private final String password;
    private final String firstname;
    private final String surname;
    private final String address;
    private final String verificationhash;
    private final boolean accounstatus;
    private final String sessioncookie;
    private final String apitoken;
}
