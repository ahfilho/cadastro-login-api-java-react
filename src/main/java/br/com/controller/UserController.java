package br.com.controller;


import br.com.dto.UserDto;
import br.com.entity.User;
import br.com.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/new/user")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping()
    public ResponseEntity<String> save(@RequestBody @Validated UserDto userDto, BindingResult result) {
        try {
            boolean existingCpf = userService.findByCpf(userDto.getCpf());
            boolean existingEmail = userService.findByCpf(userDto.getCpf());

            if (existingCpf || existingCpf) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        String.format("Não foi possível cadastrar o cliente: " + userDto.getUserName() + ", pois, o CPF e/ou E-mail:" + userDto.getCpf() + " já existem na base de dados."));
            }

            // Certifique-se de que a senha está presente no userDto
            String rawPassword = userDto.getPassword();

            if (rawPassword == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A senha não pode ser nula.");
            }

            // Crie o usuário a partir do DTO
            ModelMapper modelMapper = new ModelMapper();
            var user = modelMapper.map(userDto, User.class);

            // Criptografe e configure a senha no objeto User
            String encryptedPassword = passwordEncoder.encode(rawPassword);
            user.setPassword(encryptedPassword);

            userService.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso! \n");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar usuário. Erro na validação dos dados." + result.getAllErrors());
        }
    }
    @GetMapping("/todos")
    public List<User> list(){
        return userService.listAll();
    }
    @DeleteMapping("/{id}")
    public HttpStatus ramDelete(@PathVariable Long id) throws Exception {
        this.userService.delete(id);
        return HttpStatus.OK;
    }




}
