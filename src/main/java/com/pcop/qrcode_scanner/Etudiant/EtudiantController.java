package com.pcop.qrcode_scanner.Etudiant;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.zxing.WriterException;
import com.pcop.qrcode_scanner.*;
import com.pcop.qrcode_scanner.ExceptionHandler.ResourceAlreadyExistsException;
import com.pcop.qrcode_scanner.ExceptionHandler.ResourceNotFoundException;
import com.pcop.qrcode_scanner.ExceptionHandler.ResourceNotUpdatedException;
import com.pcop.qrcode_scanner.Gender.GenderConverter;
import com.pcop.qrcode_scanner.Journal.Journal;
import com.pcop.qrcode_scanner.Journal.JournalService;
import com.pcop.qrcode_scanner.ProfilePicture.ProfilePicture;
import com.pcop.qrcode_scanner.ProfilePicture.ProfilePictureService;
import com.pcop.qrcode_scanner.QrCode.QrCode;
import com.pcop.qrcode_scanner.QrCode.QrCodeService;
import com.pcop.qrcode_scanner.Role.RoleService;
import com.pcop.qrcode_scanner.Storage.FileInfo;
import com.pcop.qrcode_scanner.Storage.FileStorageService;
import com.pcop.qrcode_scanner.User.UserMapper;
import com.pcop.qrcode_scanner.User.UserPrincipal;
import com.pcop.qrcode_scanner.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.security.Principal;
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

    @Autowired
    ProfilePictureService profilePictureService;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    JournalService journalService;

    @Autowired
    RoleService roleService;
    @Autowired
    private UserService userService;


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
    public Etudiant create(@RequestParam("image") MultipartFile image,
                           @RequestParam("etudiant") String etudiantJson) throws IOException, WriterException {
        // Create ObjectMapper and register JavaTimeModule
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Convert JSON string to Etudiant object
        Etudiant etudiant = objectMapper.readValue(etudiantJson, Etudiant.class);

        //Verify if the etudiant exist in the database
        Optional<Etudiant> etudiantOptional = etudiantService.findByMatriculeOrCinOrEmail(etudiant.getMatricule(),
                etudiant.getCin(), etudiant.getEmail());
        if (etudiantOptional.isPresent()) {
            throw new ResourceAlreadyExistsException("Etudiant already exists in the database");
        }

        saveProfilePicture(image, etudiant);

//        create a QR Code for the student
        QrCode qrCode = new QrCode(UUID.randomUUID().toString(),
                LocalDateTime.now().plusMonths(10),
                LocalDateTime.now(), true);
        qrCodeService.save(qrCode);

//        save the student to the DB
        etudiant.setQrCode(qrCode);
        etudiant = etudiantService.save(etudiant);

//        log the operation to the database
        String operation = String.format("Création de l'étudiant %s %s, cin: %s, IM: %s.", etudiant.getNom(),
                etudiant.getPrenom(), etudiant.getCin(), etudiant.getMatricule());
//        get the user who performed the action by his name
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Journal journal = new Journal(operation, UserMapper.principalToUser(principal, userService), LocalDateTime.now());

        journalService.save(journal);
        return etudiant;
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Etudiant> update(@PathVariable Long id, @RequestBody Etudiant updatedEtudiant) {
//        Verify if the etudiant exists
        Etudiant etudiant = etudiantService.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Etudiant not found for id: " + id)
        );
//        Verify if the etudiant is updated
        boolean isUpdated = GenericUpdater.updateIfChanged(etudiant, updatedEtudiant);
        if (isUpdated) {
//            log the operation to the database
            String operation = String.format("Mise à jour de l'étudiant %s %s, cin: %s, IM: %s.", etudiant.getNom(),
                    etudiant.getPrenom(), etudiant.getCin(), etudiant.getMatricule());
            UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Journal journal = new Journal(operation, UserMapper.principalToUser(principal, userService), LocalDateTime.now());
            journalService.save(journal);

            etudiantService.save(etudiant);
            return ResponseEntity.ok().body(etudiant);
        } else {
            throw new ResourceNotUpdatedException("Etudiant not updated");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}/pdp")
    public ResponseEntity<Etudiant> updateProfilePicture(@PathVariable Long id, @RequestParam("image")MultipartFile image) {
//        Verify if the etudiant exists
        Etudiant etudiant = etudiantService.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Etudiant not found for id: " + id)
        );
//        empty the profile picture of the student and save the new one to it
        etudiant.setProfilePicture(null);
        saveProfilePicture(image, etudiant);

        etudiant = etudiantService.save(etudiant);

//        log the operation to the database
        String operation = String.format("Mise à jour de la photo de profil de l'étudiant %s %s, cin: %s, IM: %s.", etudiant.getNom(),
                etudiant.getPrenom(), etudiant.getCin(), etudiant.getMatricule());
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Journal journal = new Journal(operation, UserMapper.principalToUser(principal, userService), LocalDateTime.now());
        journalService.save(journal);

        return ResponseEntity.ok().body(etudiant);
    }

    private void saveProfilePicture(@RequestParam("image") MultipartFile image, Etudiant etudiant) {
        try {
            FileInfo fileInfo = fileStorageService.save(image);
            ProfilePicture profilePicture = new ProfilePicture(
                    fileInfo.getName(),
                    fileInfo.getPath(),
                    fileInfo.getType(),
                    LocalDateTime.now()
            );
            profilePictureService.save(profilePicture);
            etudiant.setProfilePicture(profilePicture);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving the image: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
//        Verify if the etudiant exists
        Etudiant etudiant = etudiantService.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Etudiant not found for id: " + id)
        );
//        delete the profile picture
        ProfilePicture etudiantPdp = etudiant.getProfilePicture();
        if (etudiantPdp!= null) fileStorageService.delete(etudiantPdp.getName());

//        log the operation to the database
        String operation = String.format("Suppression de l'étudiant %s %s, cin: %s, IM: %s.", etudiant.getNom(),
                etudiant.getPrenom(), etudiant.getCin(), etudiant.getMatricule());
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Journal journal = new Journal(operation, UserMapper.principalToUser(principal, userService), LocalDateTime.now());
        journalService.save(journal);

        etudiantService.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{id}/pdp")
    public ResponseEntity<Etudiant> deleteProfilePicture(@PathVariable Long id) {
        Etudiant etudiant = etudiantService.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Etudiant not found for id: " + id)
        );
        ProfilePicture etudiantPdp = etudiant.getProfilePicture();
        if (etudiantPdp!= null) {
            fileStorageService.delete(etudiantPdp.getName());
        }
        etudiant.setProfilePicture(null);
        etudiant = etudiantService.save(etudiant);

        // log the operation to the database
        String operation = String.format("Suppression de la photo de profil de l'étudiant %s %s, cin: %s, IM: %s.", etudiant.getNom(),
                etudiant.getPrenom(), etudiant.getCin(), etudiant.getMatricule());
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Journal journal = new Journal(operation, UserMapper.principalToUser(principal, userService), LocalDateTime.now());
        journalService.save(journal);

        return ResponseEntity.ok().body(etudiant);
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
