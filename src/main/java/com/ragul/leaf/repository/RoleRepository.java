package com.ragul.leaf.repository;

import com.ragul.leaf.model.Role;
import com.ragul.leaf.model.RoleName;
import com.ragul.leaf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
