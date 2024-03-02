package floud.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(FloudApplication.class, args);
	}

}
