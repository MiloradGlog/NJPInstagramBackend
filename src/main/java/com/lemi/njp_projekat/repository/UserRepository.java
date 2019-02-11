package com.lemi.njp_projekat.repository;

import com.lemi.njp_projekat.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmailAndPassword(String email, String password);
    User findByUsername(String username);

}
