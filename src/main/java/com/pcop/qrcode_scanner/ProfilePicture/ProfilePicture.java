package com.pcop.qrcode_scanner.ProfilePicture;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ProfilePicture {

    @Id
    @SequenceGenerator(
            name = "profile_picture_id_seq",
            sequenceName = "profile_picture_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "profile_picture_id_seq"
    )
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String name;

    private LocalDateTime updatedAt;

    public ProfilePicture() {}

    public ProfilePicture(String name, String path, String type, LocalDateTime updatedAt) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
