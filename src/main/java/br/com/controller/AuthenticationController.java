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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.persistence.Table;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping(value = "/user", produces = {"application/json"})
@CrossOrigin
@Table(name="api-login")
public class AuthenticationController {
    @Autowired

    private AuthenticationManager authenticationManager;
    @Autowired

    private JWTTokenHelper jwtTokenHelper;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private CorsConfigurationSource corsConfigurationSource;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JWTTokenHelper jwtTokenHelper, UserDetailsServiceImpl userDetailsServiceImpl,
                                    CorsConfigurationSource corsConfigurationSource, PasswordEncoder passwordEncoder, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenHelper = jwtTokenHelper;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.corsConfigurationSource = corsConfigurationSource;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Operation(summary = "Autentica o login",method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Autentica o usuário com sucesso"),
            @ApiResponse(responseCode = "401",description = "Não autenticado."),
            @ApiResponse(responseCode = "401",description = "Falha no serviço."),


    })
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse res)
            throws InvalidKeySpecException, NoSuchAlgorithmException {

        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));


        User user = (User) authentication.getPrincipal();  // apagar se ficar pior
        // Recupera o usuário do banco de dados pelo nome de usuário
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(authenticationRequest.getUsername());

        // Gera o token JWT
        String jwtToken = jwtTokenHelper.generateToken(user.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication); // apagar se ficar pior

        // Cria a resposta
        LoginResponse response = new LoginResponse();
        response.setToken(jwtToken);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retorna informações do user para autenticar",method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Autentica o usuário com sucesso"),
            @ApiResponse(responseCode = "401",description = "Não autenticado."),
            @ApiResponse(responseCode = "500",description = "Falha no serviço."),


    })
    @GetMapping("/auth/userinfo")
    public ResponseEntity<?> getUserInfo(Principal user) {

        User userObj = (User) userDetailsServiceImpl.loadUserByUsername(user.getName());

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userObj.getUsername());
        userInfo.setPassword(userObj.getPassword());
        userInfo.setRoles(userObj.getAuthorities().toArray());

        return ResponseEntity.ok(userInfo);

    }

}
