package com.pcop.qrcode_scanner.Operateur;

import com.pcop.qrcode_scanner.Journal.Journal;
import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Operateur {
    @Id
    @SequenceGenerator(
            name = "operateur_id_seq",
            sequenceName = "operateur_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "operateur_id_seq"
    )
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    private String utilisateur;
    private String passwd;
    private String refreshToken;

    @OneToMany(mappedBy = "operateur")
    private Collection<Journal> journals;

    public Operateur() {
    }

    public Operateur(String utilisateur, String passwd, String refreshToken, Collection<Journal> journals) {
        this.utilisateur = utilisateur;
        this.passwd = passwd;
        this.refreshToken = refreshToken;
        this.journals = journals;
    }

    @Override
    public String toString() {
        return "Operateur{" +
                "id=" + id +
                ", utilisateur='" + utilisateur + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", journals=" + journals +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Collection<Journal> getJournals() {
        return journals;
    }

    public void setJournals(Collection<Journal> journals) {
        this.journals = journals;
    }
}
