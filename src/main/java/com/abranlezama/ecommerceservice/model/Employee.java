package com.abranlezama.ecommerceservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 30)
    private String firstName;
    @Column(nullable = false, length = 30)
    private String lastName;
    @Column(nullable = false, length = 20)
    private String phone;
    private LocalDate startDate;
    private LocalDate leaveDate;
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    @Column(nullable = false, length = 20)
    private String street;
    @Column(nullable = false, length = 20)
    private String city;
    @Column(nullable = false, length = 20)
    private String region;
    @Column(nullable = false, length = 20)
    private String postalCode;
    @Column(nullable = false, length = 20)
    private String country;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(firstName, employee.firstName) &&
                Objects.equals(lastName, employee.lastName) && Objects.equals(phone, employee.phone) &&
                Objects.equals(startDate, employee.startDate) && Objects.equals(leaveDate, employee.leaveDate) &&
                Objects.equals(dateOfBirth, employee.dateOfBirth) && Objects.equals(street, employee.street) &&
                Objects.equals(city, employee.city) && Objects.equals(region, employee.region) &&
                Objects.equals(postalCode, employee.postalCode) && Objects.equals(country, employee.country) &&
                Objects.equals(user, employee.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, phone, startDate, leaveDate, dateOfBirth, street,
                city, region, postalCode, country, user);
    }
}
