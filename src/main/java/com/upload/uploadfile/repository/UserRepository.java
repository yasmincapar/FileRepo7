package com.upload.uploadfile.repository;

import com.upload.uploadfile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.username = ?1 AND u.password = ?2")
    User findByUsernameAndPassword(String username, String password);
    //



}
