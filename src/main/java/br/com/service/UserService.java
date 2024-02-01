package br.com.service;

import br.com.entity.Authority;
import br.com.entity.User;
import br.com.enume.Role;
import br.com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
        user.setEnabled(true);

        List<Authority> authorityList = new ArrayList<>();

        String lowercaseProfile = user.getProfile().toLowerCase();

        if ("admin".equals(lowercaseProfile)) {
            user.setProfile(Role.ROLE_ADMIN.getRole().toLowerCase());
            authorityList.add(createAuthorithy("ADMIN", "Admin role"));
            user.setAuthorities(authorityList);
        } else {
            if ("usuario".equals(lowercaseProfile)) {
            user.setProfile(Role.ROLE_USER.getRole().toLowerCase());
            authorityList.add(createAuthorithy("USER", "User role"));
        } else {
            throw new IllegalArgumentException("Perfil inválido: " + user.getProfile());
        }
            user.setAuthorities(authorityList);
        }

        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userRepository.save(user);
    }


    private Authority createAuthorithy(String roleCode, String roleDescription) {
        Authority authority = new Authority();
        authority.setRoleCode(roleCode);
        authority.setRoleDescription(roleDescription);
        return authority;
    }

    @Override
    public UserDetails loadUserByUsername(String nome) throws UsernameNotFoundException {
        User user = userRepository.findByNome(nome);

        if (user != null) {
            return (UserDetails) user;
        }
        throw new UsernameNotFoundException("Usuário não encontrado: " + nome);

    }


    public List<User> listAll(User authenticatedUser) {
        if (authenticatedUser == null || isAdmin(authenticatedUser)) {
            return userRepository.findAll();
        } else {
            return Collections.singletonList(authenticatedUser);
        }
    }

    public boolean isAdmin(User user) {
        return "admin".equalsIgnoreCase(user.getProfile());
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
