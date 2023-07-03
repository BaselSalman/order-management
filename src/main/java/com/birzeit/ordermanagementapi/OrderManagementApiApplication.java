package com.birzeit.ordermanagementapi;

import com.birzeit.ordermanagementapi.dtos.ManagerRequestDTO;
import com.birzeit.ordermanagementapi.services.ManagerService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class OrderManagementApiApplication {

	@Autowired
	private ManagerService managerService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PostConstruct
	public void init() {
		if(managerService.getAnyManager().isEmpty()) {
			managerService.addManager(new ManagerRequestDTO("basel.salman.1221@gmail.com",
					"bsalman", "123123456"));
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderManagementApiApplication.class, args);
	}

}
