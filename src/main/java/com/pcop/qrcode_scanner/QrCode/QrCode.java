package com.pcop.qrcode_scanner.QrCode;

import com.pcop.qrcode_scanner.Etudiant.Etudiant;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class QrCode {
    @Id
    @SequenceGenerator(
            name = "qrcode_id_seq",
            sequenceName = "qrcode_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "qrcode_id_seq"
    )
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String data;

    @Column(nullable = false)
    private LocalDateTime expireDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isValid;

    @OneToOne(optional = false)
    @JoinColumn(
            name = "id_etudiant",
            referencedColumnName = "id"
    )
    private Etudiant qrcodeOwner;

    private String path;

    public QrCode() {
    }

    public QrCode(String data, LocalDateTime expireDate, LocalDateTime createdAt, boolean isValid, Etudiant qrcodeOwner, String path) {
        this.data = data;
        this.expireDate = expireDate;
        this.createdAt = createdAt;
        this.isValid = isValid;
        this.qrcodeOwner = qrcodeOwner;
        this.path = path;
    }

    @Override
    public String toString() {
        return "QrCode{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", expireDate=" + expireDate +
                ", createdAt=" + createdAt +
                ", isValid=" + isValid +
                ", qrcodeOwner=" + qrcodeOwner +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public Etudiant getQrcodeOwner() {
        return qrcodeOwner;
    }

    public void setQrcodeOwner(Etudiant qrcodeOwner) {
        this.qrcodeOwner = qrcodeOwner;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
