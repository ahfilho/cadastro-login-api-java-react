package br.com.entity;

import lombok.Data;

@Data
public class UserInfo {


    private String userName;
    private String senha;
    private Object roles;
}
