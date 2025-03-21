package com.example.vse_site.Repository;


import com.example.vse_site.Entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByUsername(String username);
    Optional<MyUser> findByEmail(String email);
    @Query("SELECT u FROM MyUser u WHERE u.verificationCode = ?1")
    MyUser findByVerificationCode(String code);

}
