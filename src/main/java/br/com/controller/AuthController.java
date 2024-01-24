package br.com.controller;

import br.com.auth.JWTTokenHelper;
import br.com.entity.AuthenticationRequest;
import br.com.entity.LoginResponse;
import br.com.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping(value = "/api/auth", produces = {"application/json"})
@CrossOrigin
@Tag(name="/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JWTTokenHelper jwtTokenHelper;

    public AuthController(AuthenticationManager authenticationManager, JWTTokenHelper jwtTokenHelper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenHelper = jwtTokenHelper;
    }


    @Operation(summary = "Autentica o login",method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Autentica o usuário com sucesso"),
            @ApiResponse(responseCode = "401",description = "Unauthorized"),

    })
    @PostMapping("/api/auth/login" )
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse res)
            throws InvalidKeySpecException, NoSuchAlgorithmException {

        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUserName(), authenticationRequest.getPassword()));

        User user = (User) authentication.getPrincipal();
        String jwtToken = jwtTokenHelper.generateToken(user.getUsername());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginResponse response = new LoginResponse();
        response.setToken(jwtToken);

        return ResponseEntity.ok(response);
    }


}