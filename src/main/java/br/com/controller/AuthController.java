package br.com.controller;

import br.com.dto.LoginDto;
import br.com.dto.UserDto;
import br.com.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

//    private final AuthenticationManager authenticationManager;
//
//    public AuthController(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }


    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginDto authenticationRequest) {
        try {
            String nome = "dinho";
            String senha = String.valueOf(123456);
            if (authenticationRequest.getNome().equals(nome) && authenticationRequest.getSenha().equals(senha)) {
                return ResponseEntity.status(HttpStatus.OK).body("Login bem-sucedido!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas.");
    }

}