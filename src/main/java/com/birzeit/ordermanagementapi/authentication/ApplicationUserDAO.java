package com.birzeit.ordermanagementapi.authentication;

import java.util.Optional;

public interface ApplicationUserDAO {
    Optional<ApplicationUser> selectApplicationUserByUsername(String username);
}