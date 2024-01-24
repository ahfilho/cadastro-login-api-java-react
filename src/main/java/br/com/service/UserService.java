package br.com.service;

import br.com.entity.User;
import br.com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveNewUser(User user) {

        List<User> users = userRepository.findAll();

        for (User usr : users) {
            if ("ADMINISTRADOR".equals(usr.getProfile())) {
                // É um administrador
                System.out.println("Administrador: " + usr.getUsername());
            } else {
                // É um usuário comum
                System.out.println("Usuário Comum: " + usr.getUsername());
            }
        }
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String nome) throws UsernameNotFoundException {
        User user = userRepository.findByNome(nome);

        if (user != null) {
            return (UserDetails) user;
        }
        throw new UsernameNotFoundException("Usuário não encontrado: " + nome);

    }

    public List<User> listAll() {
        return userRepository.findAll();
    }

    public void deleteUserById(Long id) throws Exception {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if ("admin".equalsIgnoreCase(user.getProfile())) {
                userRepository.delete(user);
            } else {
                throw new Exception("Usuário não autorizado para excluir.");
            }
        } else {
            throw new Exception("Usuário não encontrado para excluir.");
        }
    }

    public void resetPasswordFromUser(User user) {
        try {
            User existingUser = userRepository.userWithSameCpf(user.getCpf());

            if (existingUser != null) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(existingUser);
            } else {
                throw new RuntimeException("Usuário não encontrado pelo CPF: " + user.getCpf());
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar a senha: " + e.getMessage(), e);
        }
    }

    public boolean findUserByCpf(String cpf) {
        User existingCpf = userRepository.userWithSameCpf(cpf);
        return existingCpf != null;
    }

    public boolean findByEmail(String email) {
        User existingEmail = userRepository.userWithSameEmail(email);
        return existingEmail != null;
    }

}
