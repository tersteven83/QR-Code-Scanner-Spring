package com.pcop.qrcode_scanner;

import com.pcop.qrcode_scanner.Etudiant.Etudiant;
import com.pcop.qrcode_scanner.Etudiant.EtudiantRepository;
import com.pcop.qrcode_scanner.Gender.Gender;
import com.pcop.qrcode_scanner.QrCode.QrCode;
import com.pcop.qrcode_scanner.QrCode.QrCodeRepository;
import com.pcop.qrcode_scanner.Role.Role;
import com.pcop.qrcode_scanner.Role.RoleName;
import com.pcop.qrcode_scanner.Role.RoleRepository;
import com.pcop.qrcode_scanner.User.User;
import com.pcop.qrcode_scanner.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class QrcodeScannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(QrcodeScannerApplication.class, args);
	}


	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository,
										RoleRepository roleRepository,
										EtudiantRepository etudiantRepository,
										QrCodeRepository qrCodeRepository) {
		return args -> {
			roleRepository.save(new Role(RoleName.ADMIN));
			roleRepository.save(new Role(RoleName.USER));
			System.out.println("Saved roles: ADMIN, USER");

            // find roles by name and set them to a user's roles. In a real application, you'd fetch the roles from a database or a service.
			List<Role> roles = Arrays.asList(roleRepository.findByName(RoleName.ADMIN).orElse(null),
					roleRepository.findByName(RoleName.USER).orElse(null));

            // create a new user with the role list
            userRepository.save(new User(true, "$2a$12$4fG5IZffuRSbjsXNEhNrLu2FbyhPKHIlkqqyLi/gEFQ00fotcEkLK", roles, "admin"));
            System.out.println("Saved users: admin");

			// create a qr code
			QrCode qrCode = new QrCode(UUID.randomUUID().toString(),
					LocalDateTime.now().plusMonths(10),
					LocalDateTime.now(), true);
			qrCodeRepository.save(qrCode);

			// create a student for the initialization
			Etudiant etudiant = new Etudiant("Steven", "Ter", LocalDate.now(),
					"1234556", LocalDate.now(),
					"tersteven@gmail.com", "0342796766", "Isada", "L3", "2023-2024", Gender.MALE, "2518");
			etudiant.setQrCode(qrCode);

			etudiantRepository.save(etudiant);
			System.out.println("Saved etudiant with ID: " + etudiant.getId());

        };
	}

}
