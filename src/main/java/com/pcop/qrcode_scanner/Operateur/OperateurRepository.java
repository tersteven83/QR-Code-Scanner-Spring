package com.pcop.qrcode_scanner.Operateur;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OperateurRepository extends JpaRepository<Operateur, Long> {
    Operateur findByUtilisateur(String utilisateur);
    Operateur findByRefreshToken(String refreshToken);
}
