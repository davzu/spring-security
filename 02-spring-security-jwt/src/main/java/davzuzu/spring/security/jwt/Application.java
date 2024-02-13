package davzuzu.spring.security.jwt;

import davzuzu.spring.security.jwt.model.ERole;
import davzuzu.spring.security.jwt.model.RoleEntity;
import davzuzu.spring.security.jwt.model.UserEntity;
import davzuzu.spring.security.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;

	@Bean
	CommandLineRunner init() {
		return args -> {
			UserEntity userEntity = UserEntity.builder()
					.email("david@mail.com")
					.username("david")
					.password(passwordEncoder.encode("1234"))
					.roles(Set.of(RoleEntity.builder()
											.name(ERole.ADMIN)
											.build()))
					.build();

			userRepository.save(createUserEntity("david","david@mail.com","1234",ERole.ADMIN));
			userRepository.save(createUserEntity("carlos","carlos@mail.com","1234",ERole.USER));
			userRepository.save(createUserEntity("rebeca","rebeca@mail.com","1234",ERole.INVITED));
		};
	}

	private UserEntity createUserEntity(String username, String email, String password, ERole role) {
		return UserEntity
				.builder()
				.username(username)
				.email(email)
				.password(passwordEncoder.encode(password))
				.roles(Set.of(RoleEntity.builder()
						.name(role)
						.build()))
				.build();
	}

}
