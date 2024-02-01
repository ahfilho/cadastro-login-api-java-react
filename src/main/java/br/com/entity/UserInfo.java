package br.com.entity;

import lombok.Data;

@Data
public class UserInfo {

    private String firstName;
    private String lastName;
    private String password;
    private Object roles;
}
