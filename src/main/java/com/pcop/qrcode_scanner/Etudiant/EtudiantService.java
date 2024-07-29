package com.pcop.qrcode_scanner.Etudiant;

import com.pcop.qrcode_scanner.Gender.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EtudiantService {

    @Autowired
    EtudiantRepository etudiantRepository;

    public Etudiant save(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }

    public Optional<Etudiant> findByMatricule(String matricule) {
        return etudiantRepository.findByMatricule(matricule);
    }

    public  Optional<Etudiant> findByMatriculeOrCinOrEmail(String matricule, String cin, String email) {
        return etudiantRepository.findByMatriculeOrCinOrEmail(matricule, cin, email);
    }

    public Optional<Etudiant> findByCin(String cin) {
        return etudiantRepository.findByCin(cin);
    }

    public Iterable<Etudiant> findAllBySexe(Gender gender) {
        return etudiantRepository.findAllBySexe(gender);
    }

    public void deleteById(Long id) {
        etudiantRepository.deleteById(id);
    }

    public Iterable<Etudiant> findAll() {
        return etudiantRepository.findAll();
    }

    public Optional<Etudiant> findById(Long id) {
        return etudiantRepository.findById(id);
    }

    public Optional<Etudiant> findByQrCodeData(String data) {
        return etudiantRepository.findByQrCodeData(data);
    }

}
