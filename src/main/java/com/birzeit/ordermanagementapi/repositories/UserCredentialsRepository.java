package com.birzeit.ordermanagementapi.repositories;

import com.birzeit.ordermanagementapi.entities.Manager;
import com.birzeit.ordermanagementapi.entities.User;
import com.birzeit.ordermanagementapi.entities.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Integer> {
    List<UserCredentials> findByRole(String role);

    UserCredentials findByUsername(String username);
}
