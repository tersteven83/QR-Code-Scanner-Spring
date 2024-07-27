package com.pcop.qrcode_scanner.Operateur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OperateurService {

    @Autowired
    private OperateurRepository operateurRepository;

    public Operateur save(Operateur operateur) {
        return operateurRepository.save(operateur);
    }

    public Operateur findByUtilisateur(String utilisateur) {
        return operateurRepository.findByUtilisateur(utilisateur);
    }

    public Operateur findByRefreshToken(String refreshToken) {
        return operateurRepository.findByRefreshToken(refreshToken);
    }

    public void deleteById(Long id) {
        operateurRepository.deleteById(id);
    }

    public Iterable<Operateur> findAll() {
        return operateurRepository.findAll();
    }

    public Optional<Operateur> findById(Long id) {
        return operateurRepository.findById(id);
    }

    public Operateur update(Operateur operateur) {
        return operateurRepository.save(operateur);
    }

}
