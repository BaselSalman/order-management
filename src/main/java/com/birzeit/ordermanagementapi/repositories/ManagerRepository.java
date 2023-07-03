package com.birzeit.ordermanagementapi.repositories;

import com.birzeit.ordermanagementapi.entities.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {
}
