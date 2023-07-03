package com.birzeit.ordermanagementapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends User {

    @Column(columnDefinition = "TINYTEXT")
    private String firstName;

    @Column(columnDefinition = "TINYTEXT")
    private String lastName;

    private LocalDate bornAt;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

}
