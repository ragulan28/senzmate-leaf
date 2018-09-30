package com.ragul.leaf.controller;

import com.ragul.leaf.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class RoleController {

    @Autowired
    RoleRepository roleRepository;



}
