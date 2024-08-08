package com.pcop.qrcode_scanner.ProfilePicture;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
    ProfilePicture findByName(String name);
}
