package com.library.library_management;

import com.library.library_management.entity.User;
import com.library.library_management.enums.Role;
import com.library.library_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LibraryManagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementApplication.class, args);
	}

	@Bean
	CommandLineRunner createAdmin(UserRepository repository, PasswordEncoder encoder){
		return args -> {
			if (!repository.existsByUsername(Role.ADMIN.toString())) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword(encoder.encode("admin123"));
				admin.setRole(Role.ADMIN);
				repository.save(admin);
				System.out.println("Default admin user created.");
			}
		};
	}

}
