package com.upload.uploadfile.util;

import com.upload.uploadfile.model.Role;
import com.upload.uploadfile.model.User;
import com.upload.uploadfile.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitializerRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(InitializerRunner.class);

    @Autowired
    UserRepository repository;

    @Override
    public void run(String... args) throws Exception {
        repository.deleteAll();
        repository.save(User.builder().username("james").password("12345").role(Role.USER).build());
        repository.save(User.builder().username("lucas").password("12345").role(Role.USER).build());
        repository.save(User.builder().username("issy").password("12345").role(Role.USER).build());
        repository.save(User.builder().username("yasmin").password("1234").role(Role.ADMIN).build());
        repository.findAll().forEach(user -> logger.info("{}", user));

    }
}
