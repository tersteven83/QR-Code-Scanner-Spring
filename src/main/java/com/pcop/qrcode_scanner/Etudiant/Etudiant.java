package com.pcop.qrcode_scanner.Etudiant;


import com.pcop.qrcode_scanner.Gender.Gender;
import com.pcop.qrcode_scanner.Gender.GenderConverter;
import com.pcop.qrcode_scanner.Journal.Journal;
import com.pcop.qrcode_scanner.ProfilePicture.ProfilePicture;
import com.pcop.qrcode_scanner.QrCode.QrCode;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(
        name = "etudiant",
        uniqueConstraints = {
                @UniqueConstraint(name = "etudiant_email_unique", columnNames = "email"),
                @UniqueConstraint(name = "etudiant_matricule_unique", columnNames = "matricule"),
                @UniqueConstraint(name = "etudiant_cin_unique", columnNames = "cin")
        }
)
public class Etudiant {

    @Id
    @SequenceGenerator(
            name = "etudiant_id_seq",
            sequenceName = "etudiant_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "etudiant_id_seq"
    )
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private Long id;

    @Column(
            name = "nom",
            nullable = false,
            length = 255
    )
    private String nom;

    @Column(
            name = "prenom",
            nullable = false,
            length = 255
    )
    private String prenom;

    @Column(
            name = "dob",
            nullable = false
    )
    private LocalDate dob;

    @Column(
            name = "cin",
            length = 100
    )
    private String cin;

    @Column(
            name = "cin_date"
    )
    private LocalDate cin_date;

    @Column(
            name = "email",
            nullable = false,
            length = 255
    )
    private String email;

    @Column(
            name = "tel",
            nullable = false,
            length = 100
    )
    private String tel;

    @Column(
            name = "matricule",
            nullable = false,
            length = 100
    )
    private String matricule;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "parcours")
    private String parcours;

    @Column(name = "annee_univ")
    private String anneeUniv;

    @Column(
            name = "sexe",
            nullable = false
    )
//    @Enumerated(EnumType.ORDINAL)
    @Convert(converter = GenderConverter.class)
    private Gender sexe;

    @OneToOne
    @JoinColumn(name = "id_qcode")
    private QrCode qrCode;

    @OneToOne
    @JoinColumn(name = "id_pdp")
    private ProfilePicture profilePicture;

    public Etudiant(){}

    public Etudiant(String nom, String prenom, LocalDate dob, String cin, LocalDate cin_date, String email, String tel, String adresse, String parcours, String anneeUniv, Gender sexe, String matricule) {
        this.nom = nom;
        this.prenom = prenom;
        this.dob = dob;
        this.cin = cin;
        this.cin_date = cin_date;
        this.email = email;
        this.tel = tel;
        this.adresse = adresse;
        this.parcours = parcours;
        this.anneeUniv = anneeUniv;
        this.sexe = sexe;
        this.matricule = matricule;
    }

    @Override
    public String toString() {
        return "Etudiant{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dob=" + dob +
                ", cin='" + cin + '\'' +
                ", cin_date=" + cin_date +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", matricule='" + matricule + '\'' +
                ", adresse='" + adresse + '\'' +
                ", parcours='" + parcours + '\'' +
                ", anneeUniv='" + anneeUniv + '\'' +
                ", sexe=" + sexe +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public LocalDate getCin_date() {
        return cin_date;
    }

    public void setCin_date(LocalDate cin_date) {
        this.cin_date = cin_date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getParcours() {
        return parcours;
    }

    public void setParcours(String parcours) {
        this.parcours = parcours;
    }

    public String getAnneeUniv() {
        return anneeUniv;
    }

    public void setAnneeUniv(String anneeUniv) {
        this.anneeUniv = anneeUniv;
    }

    public Gender getSexe() {
        return sexe;
    }

    public void setSexe(Gender sexe) {
        this.sexe = sexe;
    }

    public QrCode getQrCode() {
        return qrCode;
    }

    public void setQrCode(QrCode qrCode) {
        this.qrCode = qrCode;
    }

    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
    }
}
