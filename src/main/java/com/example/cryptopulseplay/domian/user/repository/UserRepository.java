package com.example.cryptopulseplay.domian.user.repository;

import com.example.cryptopulseplay.domian.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
