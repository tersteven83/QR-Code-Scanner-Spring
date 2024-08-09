package com.pcop.qrcode_scanner.User;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;


public class UserMapper {

    public static UserPrincipal userToPrincipal(User user) {
        UserPrincipal userPrincipal = new UserPrincipal();
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .toList();

        userPrincipal.setUsername(user.getUsername());
        userPrincipal.setPassword(user.getPassword());
        userPrincipal.setEnabled(user.isEnabled());
        userPrincipal.setAuthorities(authorities);
        return userPrincipal;
    }

    public static User principalToUser(UserPrincipal userPrincipal, UserService userService) {
        return userService.findByUsername(userPrincipal.getUsername()).orElseThrow(
                () -> new RuntimeException("User not found for username: " + userPrincipal.getUsername())
        );
    }

    public static UserDTO entityToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEnabled(user.isEnabled());
        userDTO.setRoles(user.getRoles());
        return userDTO;
    }

}
