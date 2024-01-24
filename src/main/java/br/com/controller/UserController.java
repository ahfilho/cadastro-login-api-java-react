package br.com.controller;


import br.com.auth.JWTTokenHelper;
import br.com.dto.UserDto;
import br.com.entity.User;
import br.com.service.UserService;
import io.swagger.annotations.Api;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/new/user", produces = {"application/json"})
@Api(value = "open-api")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    JWTTokenHelper jwtTokenHelper;


    @Operation(summary = "Cadastra o usuário com usuário e senha.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Falha no serviço."),

    })
    @PostMapping()
    public ResponseEntity<?> save(@RequestBody @Validated UserDto userDto, BindingResult result) {
        try {
            boolean existingCpf = userService.findByCpf(userDto.getCpf());
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

            // Crie o usuário a partir do DTO
            ModelMapper modelMapper = new ModelMapper();
            var user = modelMapper.map(userDto, User.class);

            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
            // Salve o usuário no banco de dados
            userService.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso! \n");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar usuário. Erro na validação dos dados." + result.getAllErrors());
        }
    }

    @GetMapping("/todos")
    public List<User> list() {
        return userService.listAll();
    }

    @DeleteMapping("/{id}")
    public HttpStatus ramDelete(@PathVariable Long id) throws Exception {
        this.userService.delete(id);
        return HttpStatus.OK;
    }
    @PutMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody @Validated UserDto userDto, BindingResult result) {
       try{
            // Crie o usuário a partir do DTO
            ModelMapper modelMapper = new ModelMapper();
            var user = modelMapper.map(userDto, User.class);

//            String encryptedPassword = passwordEncoder.encode(user.getPassword());
//            user.setPassword(encryptedPassword);
            // Salve o usuário no banco de dados
            userService.dataUpdate(user);

            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso! \n");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar usuário. Erro na validação dos dados." + result.getAllErrors());
        }
    }

}
