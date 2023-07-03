package com.birzeit.ordermanagementapi.controllers;

import com.birzeit.ordermanagementapi.dtos.CustomerRequestDTO;
import com.birzeit.ordermanagementapi.dtos.CustomerResponseDTO;
import com.birzeit.ordermanagementapi.dtos.ManagerRequestDTO;
import com.birzeit.ordermanagementapi.dtos.ManagerResponseDTO;
import com.birzeit.ordermanagementapi.entities.Manager;
import com.birzeit.ordermanagementapi.services.ManagerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/managers")
@SecurityRequirement(name = "Bearer Authentication")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('manager:write')")
    public ResponseEntity<?> addManager(@Valid @RequestBody ManagerRequestDTO dto) throws URISyntaxException {
        ManagerResponseDTO manager = managerService.addManager(dto);
        return ResponseEntity.created(new URI("/managers/" + manager.id())).body(manager);
    }
}
