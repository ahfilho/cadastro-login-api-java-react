package br.com.service;


import br.com.entity.User;
import br.com.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public boolean findByCpf(String cpf) {
        User existingCLient = userRepository.clientWithSameCpf(cpf);
        return existingCLient != null;
    }
}
