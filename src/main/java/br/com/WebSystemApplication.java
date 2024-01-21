package br.com;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info=@Info(title="SisWeb",version = "1.0",description = "Api com login e cadastro"))
public class WebSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebSystemApplication.class, args);
	}

}
