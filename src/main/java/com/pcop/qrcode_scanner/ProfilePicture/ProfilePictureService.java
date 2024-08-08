package com.pcop.qrcode_scanner.ProfilePicture;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfilePictureService {

    @Autowired
    private ProfilePictureRepository profilePictureRepository;

    public ProfilePicture save(ProfilePicture profilePicture) {
        return profilePictureRepository.save(profilePicture);
    }

    public ProfilePicture findByName(String name) {
        return profilePictureRepository.findByName(name);
    }

}
