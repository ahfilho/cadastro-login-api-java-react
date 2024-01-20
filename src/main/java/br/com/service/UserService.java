package br.com.service;

import br.com.entity.User;
import br.com.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService  implements UserDetailsService {

    final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {

        List<User> users = userRepository.findAll();

        for (User usr : users) {
            if ("ADMINISTRADOR".equals(usr.getPerfil())) {
                // É um administrador
                System.out.println("Administrador: " + usr.getUsername());
            } else {
                // É um usuário comum
                System.out.println("Usuário Comum: " + usr.getUsername());
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


    @Override
    public UserDetails loadUserByUsername(String nome) throws UsernameNotFoundException {
        User user = userRepository.findByNome(nome);

        if (user != null) {
            return (UserDetails) user;
        }
        throw new UsernameNotFoundException("Usuário não encontrado: " + nome);

    }
}
