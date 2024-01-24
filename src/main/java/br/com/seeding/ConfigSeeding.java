package br.com.seeding;
import br.com.entity.User;
import br.com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ConfigSeeding {

    @Autowired
    private UserRepository userRepository;

    public ConfigSeeding(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public CommandLineRunner seedDatabase() {
        return args -> {
            // Verifica se já existem usuários no banco de dados
            if (userRepository.count() == 0) {
                // Cria um usuário de exemplo
                User user = new User();
                user.setUserName("Maria");
                user.setEmail("maria@dgmail.com");
                user.setPassword("456123");
                user.setCpf("123.456.789-09");
                user.setProfile("usuario");

                // Se necessário, adicione authorities ao usuário
                // user.setAuthorities(Arrays.asList(authority1, authority2, ...));

                // Salva o usuário no banco de dados
                userRepository.save(user);


                // Cria e salva o segundo usuário de exemplo
                User user2 = new User();
                user2.setUserName("João");
                user2.setEmail("joao@gmail.com");
                user2.setPassword("123456");
                user2.setCpf("987.654.321-09");
                user2.setProfile("admin");

                // Se necessário, adicione authorities ao usuário
                // user2.setAuthorities(Arrays.asList(authority3, authority4, ...));

                userRepository.save(user2);
            }
        };
    }
}
