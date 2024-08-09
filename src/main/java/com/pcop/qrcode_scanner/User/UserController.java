package com.pcop.qrcode_scanner.User;


import com.pcop.qrcode_scanner.ExceptionHandler.ResourceAlreadyExistsException;
import com.pcop.qrcode_scanner.ExceptionHandler.ResourceNotFoundException;
import com.pcop.qrcode_scanner.ExceptionHandler.ResourceNotUpdatedException;
import com.pcop.qrcode_scanner.GenericUpdater;
import com.pcop.qrcode_scanner.Role.Role;
import com.pcop.qrcode_scanner.Role.RoleName;
import com.pcop.qrcode_scanner.Role.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

//    get all users

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public Iterable<User> getAllUsers() {
        return userService.findAll();
    }

//    get user by id
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + id));
        return ResponseEntity.ok(UserMapper.entityToDTO(user));
    }

    //    get user by username

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found for username: " + username));
        return ResponseEntity.ok(UserMapper.entityToDTO(user));
    }

    //    delete user by id
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }

//    create user
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        // verify if the user exists in the database
        User userExist = userService.findByUsername(user.getUsername()).orElse(null);
        if (userExist!= null) {
            throw new ResourceAlreadyExistsException("User already exists for username: " + user.getUsername());
        }

        List<Role> userRole = new ArrayList<>();
        for (Role roleName : user.getRoles()) {
            Role role = roleService.findByName(roleName.getName()).orElseThrow(
                    () -> new ResourceNotFoundException("Role not found for name: " + roleName.getName()));
            userRole.add(role);
        }
        user.setRoles(userRole);
        //        encrypt the password in bcrypt
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User userSaved = userService.save(user);
        return ResponseEntity.ok(UserMapper.entityToDTO(userSaved));
    }

//    update user

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + id));
//        verify if the user is updated
        boolean isUpdated = GenericUpdater.updateIfChanged(user, updatedUser);
        if (isUpdated) {
            User userSaved = userService.save(user);
            return ResponseEntity.ok(UserMapper.entityToDTO(userSaved));
        } else {
            throw new ResourceNotUpdatedException("User not updated for id: " + id);
        }
    }

}
