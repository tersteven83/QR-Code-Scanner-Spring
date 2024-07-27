package com.pcop.qrcode_scanner.Etudiant;


import com.pcop.qrcode_scanner.*;
import com.pcop.qrcode_scanner.ExceptionHandler.ResourceAlreadyExistsException;
import com.pcop.qrcode_scanner.ExceptionHandler.ResourceNotFoundException;
import com.pcop.qrcode_scanner.ExceptionHandler.ResourceNotUpdatedException;
import com.pcop.qrcode_scanner.Gender.GenderConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/etudiants")
public class EtudiantController {

    @Autowired
    EtudiantService etudiantService;

    @GetMapping
    public Iterable<Etudiant> getAllEtudiants() {
        return etudiantService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Etudiant>> getEtudiantById(@PathVariable Long id) {
        Optional<Etudiant> etudiant = etudiantService.findById(id);
        if (!etudiant.isPresent()) {
            throw new ResourceNotFoundException("Etudiant not found for id: " + id);
        }
        return ResponseEntity.ok().body(etudiant);
    }

    @GetMapping("/cin/{cin}")
    public ResponseEntity<Optional<Etudiant>> getEtudiantByCin(@PathVariable String cin) {
        Optional<Etudiant> etudiant = etudiantService.findByCin(cin);
        if (!etudiant.isPresent()) {
            throw new ResourceNotFoundException("Etudiant not found for cin: " + cin);
        }
        return ResponseEntity.ok(etudiant);
    }

    @GetMapping("/sexe/{sexe}")
    public Iterable<Etudiant> getAllEtudiantsBySexe(@PathVariable Character sexe) {
//        Change the char parameter to upperCas
        String sexeToUpCase = sexe.toString().toUpperCase();
        return etudiantService.findAllBySexe(new GenderConverter().convertToEntityAttribute(sexeToUpCase.charAt(0)));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Etudiant create(@RequestBody Etudiant etudiant) {
//        Verify if the etudiant exist in the database
        Optional<Etudiant> etudiantOptional = etudiantService.findByMatriculeOrCinOrEmail(etudiant.getMatricule(),
                etudiant.getCin(), etudiant.getEmail());
        if (etudiantOptional.isPresent()) {
            throw new ResourceAlreadyExistsException("Etudiant already exists in the database");
        }
        return etudiantService.save(etudiant);
    }


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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
//        Verify if the etudiant exists
        etudiantService.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Etudiant not found for id: " + id)
        );
        etudiantService.deleteById(id);
    }

    @GetMapping("/matricule/{im}")
    public ResponseEntity<Optional<Etudiant>> getEtudiantByMatricule(@PathVariable String im){
        Optional<Etudiant> etudiant = etudiantService.findByMatricule(im);
        if (etudiant.isEmpty()) {
            throw new ResourceNotFoundException("Etudiant not found for matricule: " + im);
        }
        return ResponseEntity.ok(etudiant);
    }

}
