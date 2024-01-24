package br.com.controller;


import br.com.auth.JWTTokenHelper;
import br.com.entity.User;
import br.com.entity.AuthenticationRequest;
import br.com.entity.LoginResponse;
import br.com.entity.UserInfo;
//import jakarta.servlet.http.HttpServletResponse;
import br.com.service.UserDetailsServiceImpl;
import br.com.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
@Tag(name = "SisWeb", description = "Api de login web")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JWTTokenHelper jwtTokenHelper;

    private final CorsConfigurationSource corsConfigurationSource;
    private final PasswordEncoder passwordEncoder;

//    private final UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    public AuthenticationController(AuthenticationManager authenticationManager, JWTTokenHelper jwtTokenHelper, CorsConfigurationSource corsConfigurationSource, PasswordEncoder passwordEncoder, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenHelper = jwtTokenHelper;
        this.corsConfigurationSource = corsConfigurationSource;
        this.passwordEncoder = passwordEncoder;
    }


    @Operation(description = "Realiza o login do usuãrio no sistema e retorna um token, junto do status code 200, após confirmar os dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "As informações estão corretas, o usuario foi autenticado e o token foi gerado."),
            @ApiResponse(responseCode = "201", description = "Usuário autenticado com sucesso, dados validados."),
            @ApiResponse(responseCode = "401", description = "Unauthorized, bloqueado"),
            @ApiResponse(responseCode = "500", description = "Falha no serviço."),

    })
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse res)
            throws InvalidKeySpecException, NoSuchAlgorithmException {

        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUserName(), authenticationRequest.getPassword()));

        var nomeEsenha = new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword());
        var auth = this.authenticationManager.authenticate(nomeEsenha);

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String jwtToken = jwtTokenHelper.generateToken(String.valueOf(nomeEsenha));

        LoginResponse response = new LoginResponse();
        response.setToken(jwtToken);
        return ResponseEntity.ok(response);
    }


    @Operation(description = "Fornece Informações do usuário após validar o token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Falha no serviço."),

    })
    @GetMapping("/auth/userinfo")
    public ResponseEntity<?> getUserInfo(Principal user) {

        User userObj = (User) userDetailsServiceImpl.loadUserByUsername(user.getName());

        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userObj.getUsername());
        userInfo.setPassword(userObj.getPassword());
        userInfo.setRoles(userObj.getProfile());

        return ResponseEntity.ok(userInfo);

    }

}