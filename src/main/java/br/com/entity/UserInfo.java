package br.com.entity;

import lombok.Data;

@Data
public class UserInfo {


    private String username;
    private String password;
    private Object roles;
}
