package com.pcop.qrcode_scanner.Journal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    public Journal save(Journal journal) {
        return journalRepository.save(journal);
    }

    public Iterable<Journal> findAllByOperationDate(LocalDateTime operationDate) {
        return journalRepository.findAllByOperationDate(operationDate);
    }

    public Iterable<Journal> findByUserId(Long userId) {
        return journalRepository.findAllByUserId(userId);
    }

    public void deleteById(Long id) {
        journalRepository.deleteById(id);
    }

    public Iterable<Journal> findAll() {
        return journalRepository.findAll();
    }

    public Optional<Journal> findById(Long id) {
        return journalRepository.findById(id);
    }

    public void update(Journal journal) {
        journalRepository.save(journal);
    }

}
