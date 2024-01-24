package br.com.controller;


import br.com.auth.JWTTokenHelper;
import br.com.entity.User;
import br.com.entity.AuthenticationRequest;
import br.com.entity.LoginResponse;
import br.com.entity.UserInfo;
//import jakarta.servlet.http.HttpServletResponse;
import br.com.service.UserDetailsServiceImpl;
import br.com.service.UserService;
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
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JWTTokenHelper jwtTokenHelper;

    private final CorsConfigurationSource corsConfigurationSource;
    private final PasswordEncoder passwordEncoder;

//    private final UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    public AuthenticationController(AuthenticationManager authenticationManager, JWTTokenHelper jwtTokenHelper , CorsConfigurationSource corsConfigurationSource, PasswordEncoder passwordEncoder, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenHelper = jwtTokenHelper;
//        this.userDetailsService = userDetailsService;
        this.corsConfigurationSource = corsConfigurationSource;
        this.passwordEncoder = passwordEncoder;
//        this.userService = userService;
    }


    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse res)
            throws InvalidKeySpecException, NoSuchAlgorithmException {

        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUserName(), authenticationRequest.getPassword()));

        var nomeEsenha = new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword());
        var auth = this.authenticationManager.authenticate(nomeEsenha);

        // Obter as informações do usuário a partir do objeto Authentication
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String jwtToken = jwtTokenHelper.generateToken(String.valueOf(nomeEsenha));

        LoginResponse response = new LoginResponse();
        response.setToken(jwtToken);
        ////MORRE AQUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
        return ResponseEntity.ok(response);
    }


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