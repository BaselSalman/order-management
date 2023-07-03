package com.birzeit.ordermanagementapi.services;

import com.birzeit.ordermanagementapi.dtos.ManagerRequestDTO;
import com.birzeit.ordermanagementapi.dtos.ManagerResponseDTO;
import com.birzeit.ordermanagementapi.entities.Manager;
import com.birzeit.ordermanagementapi.entities.User;
import com.birzeit.ordermanagementapi.entities.UserCredentials;
import com.birzeit.ordermanagementapi.repositories.ManagerRepository;
import com.birzeit.ordermanagementapi.repositories.UserCredentialsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;

    private final UserCredentialsRepository userCredentialsRepository;

    private final PasswordEncoder passwordEncoder;

    public ManagerService(
            ManagerRepository managerRepository,
            PasswordEncoder passwordEncoder,
            UserCredentialsRepository userCredentialsRepository
    ) {
        this.managerRepository = managerRepository;
        this.passwordEncoder = passwordEncoder;
        this.userCredentialsRepository = userCredentialsRepository;
    }

    public ManagerResponseDTO addManager(ManagerRequestDTO dto) {
        Manager manager = new Manager();
        manager.setEmail(dto.email());
        manager.setUserCredentials(UserCredentials
                .builder()
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .user(manager)
                .role("MANAGER")
                .build());
        return dtoMapping(managerRepository.save(manager));
    }

    public List<Manager> getManagers() {
        return managerRepository.findAll();
    }

    public Optional<UserCredentials> getAnyManager() {
        return userCredentialsRepository.findByRole("MANAGER").stream().findAny();
    }

    public static ManagerResponseDTO dtoMapping(Manager manager) {
        return new ManagerResponseDTO(
                manager.getId(),
                manager.getEmail(),
                manager.getUserCredentials().getUsername()
        );
    }

}
