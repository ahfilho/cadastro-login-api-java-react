package br.com;

import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Sisweb", version = "1", description = "API de login e cadastro de usuário"))
public class WebSystemApplication {

public static void main(String[] args) {
    SpringApplication.run(WebSystemApplication.class, args);
}


}
