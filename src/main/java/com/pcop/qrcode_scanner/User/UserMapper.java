package com.pcop.qrcode_scanner.User;

import org.springframework.security.core.authority.SimpleGrantedAuthority

import java.util.List;


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

}
