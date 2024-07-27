package com.pcop.qrcode_scanner;

import com.pcop.qrcode_scanner.Etudiant.Etudiant;
import com.pcop.qrcode_scanner.Etudiant.EtudiantRepository;
import com.pcop.qrcode_scanner.Gender.Gender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class QrcodeScannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(QrcodeScannerApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(EtudiantRepository repository) {
		return args -> {
            Etudiant etudiant = new Etudiant("Steven", "Ter", LocalDate.now(),
					"1234556", LocalDate.now(),
					"tersteven@gmail.com", "0342796766", "Isada", "L3", "2023-2024", Gender.MALE, "2518");
			Etudiant etudiant1 = new Etudiant("Stella", "Marris", LocalDate.now(),
					"102013034056", LocalDate.now(),
                    "marrissa@gmail.com", "0349807698", "Mandroseza", "L3", "2023-2024", Gender.FEMALE, "2517");

            repository.save(etudiant1);
            repository.save(etudiant);
			System.out.println("Saved etudiant with ID: " + etudiant.getId());
			System.out.println(repository.findByMatricule("2518"));
        };
	}

}
