package br.com.controller;


import br.com.auth.JWTTokenHelper;
import br.com.dto.UserDto;
import br.com.entity.User;
import br.com.service.UserDetailsServiceImpl;
import br.com.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/new/user", produces = {"application/json"})
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    JWTTokenHelper jwtTokenHelper;


    @Operation(summary = "Cadastra o usuário com nome e senha.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created. Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST. Verifique os dados. Cpf ou E-mail já existem no banco."),
            @ApiResponse(responseCode = "500", description = "Falha no serviço."),


    })
    @PostMapping()
    public ResponseEntity<?> save(@RequestBody @Validated UserDto userDto, BindingResult result) {
        try {
            boolean existingCpf = userService.findUserByCpf(userDto.getCpf());
            boolean existingEmail = userService.findByEmail(userDto.getEmail());

            if (existingCpf) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        String.format("Não foi possível cadastrar o cliente: " + userDto.getUserName() + ", pois, o CPF e/ou E-mail:" + userDto.getCpf() + " já existem na base de dados."));
            }
            if (existingEmail) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        String.format("Não foi possível cadastrar o cliente: " + userDto.getUserName() + ", pois, o E-mail:" + userDto.getEmail() + " já existe na base de dados."));
            }
            if (userDto.getUserName() == null || userDto.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A senha não pode ser nula.");
            }
            ModelMapper modelMapper = new ModelMapper();
            var user = modelMapper.map(userDto, User.class);

            userService.saveNewUser(user);

            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso! \n");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar usuário. Erro na validação dos dados." + result.getAllErrors());
        }
    }

    @Operation(summary = "Lista os usuarios com base no perfil autenticao. Se não estiver autenticado, lista todos os usuários,.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK. Lista todos os cadastrados."),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST. Verifique os dados. Cpf ou E-mail já existe no banco."),
            @ApiResponse(responseCode = "500", description = "Deleta o usuário apenas se o perfil for de ADMIN."),


    })
    @GetMapping("/todos")
    public ResponseEntity<List<User>> listAllUsers(Principal principal) {
        if (principal == null) {
            List<User> allUsers = userService.listAll(null);
            return ResponseEntity.ok(allUsers);
        }
        UserDetailsServiceImpl userDetailsService = null;
        User authenticatedUser = (User) userDetailsService.loadUserByUsername(principal.getName());

        if (userService.isAdmin(authenticatedUser)) {
            List<User> allUsers = userService.listAll(authenticatedUser);
            return ResponseEntity.ok(allUsers);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    @Operation(summary = "Deleta um usário do banco..", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleta um usuário pelo seu ID"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST. Verifique os dados. Cpf ou E-mail já existe no banco."),
            @ApiResponse(responseCode = "500", description = "Deleta o usuário apenas se o perfil for de ADMIN."),
    })
    @DeleteMapping("/{id}")
    public HttpStatus userDelete(@PathVariable Long id) throws Exception {
        this.userService.deleteUserById(id);
        return HttpStatus.OK;
    }

    @Operation(summary = "Reseta a senha de um usuário mediante confirmação de CPF E EMAIL.", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK. Operação realizada com sucesso."),
            @ApiResponse(responseCode = "201", description = "CREATED. REQUEST. Verifique os dados. Cpf ou E-mail já existe no banco."),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST. CPF e/ou E-mail não existem no banco. "),


    })
    @PutMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody @Validated UserDto userDto, BindingResult result) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            var user = modelMapper.map(userDto, User.class);
            userService.resetPasswordFromUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Senha alterada com sucesso! \n");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao alterar os dados. Erro na validação." + result.getAllErrors());
        }
    }

}
