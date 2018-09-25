package com.ragul.leaf.controller;


import com.ragul.leaf.exception.AppException;
import com.ragul.leaf.model.Role;
import com.ragul.leaf.model.RoleName;
import com.ragul.leaf.model.User;
import com.ragul.leaf.payload.*;
import com.ragul.leaf.repository.RoleRepository;
import com.ragul.leaf.repository.UserRepository;
import com.ragul.leaf.security.CurrentUser;
import com.ragul.leaf.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public Set<UserProfile> getAll() {
        List<User> users = userRepository.findAll();
        Set<UserProfile> userProfiles = new HashSet<>();

        for (User user : users) {
            UserProfile userProfile = new UserProfile();
            userProfile.setId(user.getId());
            userProfile.setName(user.getName());
            userProfile.setUsername(user.getUsername());
            userProfile.setRole(user.getRoles());
            userProfile.setJoinedAt(user.getCreatedAt());
            userProfiles.add(userProfile);
        }
        return userProfiles;


    }

    @GetMapping("/user/role")
    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse getUserRoleByUsername(@RequestParam(value = "username") String username) {
        User user = userRepository.findFirstByUsername(username);
        RoleResponse roleResponse = new RoleResponse(user.getRoles());
        return roleResponse;
    }

    @PostMapping("/user/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeUserRole(@RequestParam(value = "username") String username, @RequestParam(value = "role") RoleName roleName) {
        System.out.println(roleName);
        if (!userRepository.existsByUsername(username)) {
            return new ResponseEntity(new ApiResponse(false, "Username is not found"),
                    HttpStatus.BAD_REQUEST);

        }

        User user = userRepository.findFirstByUsername(username);

        if (user.getRoles().contains(roleName)) {
            return new ResponseEntity(new ApiResponse(false, "This user already have thia role"),
                    HttpStatus.BAD_REQUEST);
        }
        if (roleName != RoleName.ROLE_GUEST && roleName != RoleName.ROLE_ADMIN && roleName != RoleName.ROLE_USER) {
            return new ResponseEntity(new ApiResponse(false, "Role name is invalid"),
                    HttpStatus.BAD_REQUEST);
        }

        Set<Role> roles = user.getRoles();
        Role userRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new AppException("User Role not set."));
        roles.add(userRole);
        user.setRoles(roles);


        userRepository.save(user);
        return new ResponseEntity(new ApiResponse(true, "This user privilege updated"),
                HttpStatus.OK);

    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('GUEST')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        return userSummary;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }


}
