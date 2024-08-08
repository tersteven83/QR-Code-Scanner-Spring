package com.pcop.qrcode_scanner.Etudiant;


import com.google.zxing.WriterException;
import com.pcop.qrcode_scanner.*;
import com.pcop.qrcode_scanner.ExceptionHandler.ResourceAlreadyExistsException;
import com.pcop.qrcode_scanner.ExceptionHandler.ResourceNotFoundException;
import com.pcop.qrcode_scanner.ExceptionHandler.ResourceNotUpdatedException;
import com.pcop.qrcode_scanner.Gender.GenderConverter;
import com.pcop.qrcode_scanner.QrCode.QrCode;
import com.pcop.qrcode_scanner.QrCode.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/etudiants")
public class EtudiantController {

    @Autowired
    EtudiantService etudiantService;

    @Autowired
    QrCodeService qrCodeService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public Iterable<Etudiant> getAllEtudiants() {
        return etudiantService.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Etudiant>> getEtudiantById(@PathVariable Long id) {
        Optional<Etudiant> etudiant = etudiantService.findById(id);
        if (!etudiant.isPresent()) {
            throw new ResourceNotFoundException("Etudiant not found for id: " + id);
        }
        return ResponseEntity.ok().body(etudiant);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/cin/{cin}")
    public ResponseEntity<Optional<Etudiant>> getEtudiantByCin(@PathVariable String cin) {
        Optional<Etudiant> etudiant = etudiantService.findByCin(cin);
        if (!etudiant.isPresent()) {
            throw new ResourceNotFoundException("Etudiant not found for cin: " + cin);
        }
        return ResponseEntity.ok(etudiant);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/sexe/{sexe}")
    public Iterable<Etudiant> getAllEtudiantsBySexe(@PathVariable Character sexe) {
//        Change the char parameter to upperCas
        String sexeToUpCase = sexe.toString().toUpperCase();
        return etudiantService.findAllBySexe(new GenderConverter().convertToEntityAttribute(sexeToUpCase.charAt(0)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Etudiant create(@RequestBody Etudiant etudiant) throws IOException, WriterException {
//        Verify if the etudiant exist in the database
        Optional<Etudiant> etudiantOptional = etudiantService.findByMatriculeOrCinOrEmail(etudiant.getMatricule(),
                etudiant.getCin(), etudiant.getEmail());
        if (etudiantOptional.isPresent()) {
            throw new ResourceAlreadyExistsException("Etudiant already exists in the database");
        }

//        create a QR Code for the student
        QrCode qrCode = new QrCode(UUID.randomUUID().toString(),
                LocalDateTime.now().plusMonths(10),
                LocalDateTime.now(), true);
        qrCodeService.save(qrCode);

//        save the student to the DB
        etudiant.setQrCode(qrCode);
        return etudiantService.save(etudiant);
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping
    public ResponseEntity<Etudiant> update(@RequestBody Etudiant updatedEtudiant) {
//        Verify if the etudiant exists
        Etudiant etudiant = etudiantService.findById(updatedEtudiant.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Etudiant not found for id: " + updatedEtudiant.getId())
        );
//        Verify if the etudiant is updated
        boolean isUpdated = GenericUpdater.updateIfChanged(etudiant, updatedEtudiant);
        if (isUpdated) {
            etudiantService.save(etudiant);
            return ResponseEntity.ok().body(etudiant);
        } else {
            throw new ResourceNotUpdatedException("Etudiant not updated");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
//        Verify if the etudiant exists
        etudiantService.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Etudiant not found for id: " + id)
        );
        etudiantService.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/matricule/{im}")
    public ResponseEntity<Optional<Etudiant>> getEtudiantByMatricule(@PathVariable String im){
        Optional<Etudiant> etudiant = etudiantService.findByMatricule(im);
        if (etudiant.isEmpty()) {
            throw new ResourceNotFoundException("Etudiant not found for matricule: " + im);
        }
        return ResponseEntity.ok(etudiant);
    }

    @GetMapping("/qcode/{data}")
    public ResponseEntity<Optional<Etudiant>> getEtudiantByQrCodeData(@PathVariable String data) {
        Optional<Etudiant> etudiant = etudiantService.findByQrCodeData(data);
        if (etudiant.isEmpty()) {
            throw new ResourceNotFoundException("Etudiant not found for that QR Code");
        }
        return ResponseEntity.ok().body(etudiant);
    }

}
