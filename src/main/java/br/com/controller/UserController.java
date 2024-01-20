package br.com.controller;


import br.com.dto.UserDto;
import br.com.entity.User;
import br.com.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/new/user")
@CrossOrigin
public class UserController {

    private final UserService userService;


    @PostMapping()
    public ResponseEntity<String> save(@RequestBody @Validated UserDto userDto, BindingResult result) {
        try {
            boolean existingCpf = userService.findByCpf(userDto.getCpf());

            if (existingCpf) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        String.format("Não foi possível cadastrar o cliente: " + userDto.getUserName() + ", pois, o CPF:" + userDto.getCpf() + " já existe na base de dados."));
            }
            boolean existingEmail = userService.findByCpf(userDto.getCpf());
//
//            if (existingEmail) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//                        String.format("Não foi possível cadastrar o cliente: " + userDto.getNome() + ", pois, o E-mail:" + userDto.getEmail() + " já existe na base de dados."));
//            }

            ModelMapper modelMapper = new ModelMapper();
            var user = new User();
            user = modelMapper.map(userDto, User.class);
            userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso! \n");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar usuário. Erro na validação dos dados." + result.getAllErrors());
        }

    }

}
