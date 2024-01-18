package br.com.service;

import br.com.entity.User;
import br.com.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {

        List<User> users = userRepository.findAll();

        for (User usr : users) {
            if ("ADMINISTRADOR".equals(usr.getPerfil())) {
                // É um administrador
                System.out.println("Administrador: " + usr.getNome());
            } else {
                // É um usuário comum
                System.out.println("Usuário Comum: " + usr.getNome());
            }
        }
        userRepository.save(user);
    }
    public boolean findByCpf(String cpf) {
        User existingCpf = userRepository.userWithSameCpf(cpf);
        return existingCpf != null;
    }
    public boolean findByEmail(String email) {
        User existingEmail = userRepository.userWithSameCpf(email);
        return existingEmail != null;
    }



}
