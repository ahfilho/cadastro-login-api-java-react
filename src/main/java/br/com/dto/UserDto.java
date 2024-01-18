package br.com.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    private Long UUID;
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String perfil;
}
