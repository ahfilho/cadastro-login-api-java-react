package br.com.dto;

import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String cpf;
    private String profile;
}
