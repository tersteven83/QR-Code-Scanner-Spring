package com.pcop.qrcode_scanner.Journal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface JournalRepository extends JpaRepository<Journal, Long> {
    Journal findByOperationDate(LocalDateTime operationDate);
    Iterable<Journal> findAllByOperateurId(Long Id_operateur);

    Iterable<Journal> findAllByOperationDate(LocalDateTime operationDate);
}
