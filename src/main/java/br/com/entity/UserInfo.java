package br.com.entity;

import lombok.Data;

@Data
public class UserInfo {


    private String userName;
    private String password;
    private Object roles;
}
