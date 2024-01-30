package br.com.seeding;

import br.com.entity.User;
import br.com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigSeeding {

    @Autowired
    private UserRepository userRepository;

    public ConfigSeeding(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public CommandLineRunner seedDatabase() {

        //SE POSSÍVEL, INSERIR ALGUM USUÁRIO NO FORMULARIO DE LOGIN.
        //DESSA FORMA O TOKEN É ENVIADO PARA O FRONT.


        return args -> {
            if (userRepository.count() == 0) {
                User user = new User();
                user.setFirstName("Joana");
                user.setLastName("Silva");
                user.setUserName("Maria");
                user.setEmail("maria@dgmail.com");
                user.setPassword("456123");
                user.setCpf("123.456.789-09");
                user.setProfile("usuario");


                userRepository.save(user);


                User user2 = new User();
                user2.setFirstName("João");
                user2.setLastName("Santos");
                user2.setUserName("Manoel");
                user2.setEmail("joao@gmail.com");
                user2.setPassword("123456");
                user2.setCpf("987.654.321-09");
                user2.setProfile("admin");


                userRepository.save(user2);
            }
        };

    }
}
