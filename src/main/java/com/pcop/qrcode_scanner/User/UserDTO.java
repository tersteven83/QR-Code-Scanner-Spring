package com.pcop.qrcode_scanner.User;

import com.pcop.qrcode_scanner.Role.Role;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

public class UserDTO implements Serializable {

    @NotBlank
    private String username;
    @NotBlank
    private boolean enabled;
    private String password;
    @NotBlank
    private List<Role> roles;

    @NotBlank
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(@NotBlank boolean enabled) {
        this.enabled = enabled;
    }

    public @NotBlank List<Role> getRoles() {
        return roles;
    }

    public void setRoles(@NotBlank List<Role> roles) {
        this.roles = roles;
    }

    public @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
