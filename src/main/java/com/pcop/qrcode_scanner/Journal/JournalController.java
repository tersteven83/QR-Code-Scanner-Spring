package com.pcop.qrcode_scanner.Journal;

import com.pcop.qrcode_scanner.ExceptionHandler.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class JournalController {

    @Autowired
    private JournalService journalService;

    @GetMapping
    public Iterable<Journal> getAllJournals() {
        return journalService.findAll();
    }

    @GetMapping("/{id}")
    public Journal getJournalById(@PathVariable Long id) {
        return journalService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Journal not found for id: " + id));
    }

    @GetMapping("/{operationDate}")
    public Iterable<Journal> getJournalsByOperationDate(@PathVariable LocalDateTime operationDate) {
        return journalService.findAllByOperationDate(operationDate);
    }

}
