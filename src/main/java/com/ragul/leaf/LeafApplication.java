package com.ragul.leaf;

import com.ragul.leaf.model.Role;
import com.ragul.leaf.model.RoleName;
import com.ragul.leaf.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EntityScan(basePackageClasses = {
        LeafApplication.class,
        Jsr310JpaConverters.class
})
public class LeafApplication implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(LeafApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findAll().isEmpty()) {
            Role guest = new Role(RoleName.ROLE_GUEST);
            Role user = new Role(RoleName.ROLE_USER);
            Role admin = new Role(RoleName.ROLE_ADMIN);
            List<Role> roles = Arrays.asList(guest,user,admin);
            roleRepository.saveAll(roles);
        }
    }
}
