package br.com.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.util.Date;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI acustomOpenAi() {
        return new OpenAPI()
                .components(new Components()).info(new Info().title("Api de login Web.").description("Documentação da api de login. Para acessar os dados: "));
    }
}

