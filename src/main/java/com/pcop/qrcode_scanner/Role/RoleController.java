package com.pcop.qrcode_scanner.Role;

import com.pcop.qrcode_scanner.ExceptionHandler.ResourceAlreadyExistsException;
import com.pcop.qrcode_scanner.ExceptionHandler.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

//    get all roles

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public Iterable<Role> getAllRoles() {
        return roleService.findAll();
    }

//    get role by id
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable Long id) {
        return roleService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found for id: " + id));
    }

//    delete role
    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping("/{id}")
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteById(id);
    }

//    create role
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public Role createRole(@RequestBody Role role) {
//        verify if the role already exists
        if (roleService.findById(role.getId()).isPresent()) {
            throw new ResourceAlreadyExistsException("Role already exists with id: " + role.getId());
        }
        return roleService.save(role);
    }

//    update role
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public Role updateRole(@PathVariable Long id, @RequestBody Role roleDetails) {
        Role role = roleService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found for id: " + id));
        role.setName(roleDetails.getName());
        return roleService.save(role);
    }


}
