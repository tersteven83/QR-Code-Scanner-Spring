package com.pcop.qrcode_scanner.Etudiant;

import com.pcop.qrcode_scanner.Gender.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByMatricule(String matricule);
    Optional<Etudiant> findByCin(String cin);
    List<Etudiant> findAllBySexe(Gender gender);
    Optional<Etudiant> findByMatriculeOrCinOrEmail(String matricule, String cin, String email);
    Optional<Etudiant> findByQrCodeData(String qrCode_data);
}
