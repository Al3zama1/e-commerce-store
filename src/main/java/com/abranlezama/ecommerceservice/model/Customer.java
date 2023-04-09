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
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 30)
    private String firstName;
    @Column(nullable = false, length = 30)
    private String lastName;
    @Column(nullable = false, length = 20)
    private String phone;
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

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(firstName, customer.firstName) &&
                Objects.equals(lastName, customer.lastName) && Objects.equals(phone, customer.phone) &&
                Objects.equals(dateOfBirth, customer.dateOfBirth) && Objects.equals(street, customer.street) &&
                Objects.equals(city, customer.city) && Objects.equals(region, customer.region) &&
                Objects.equals(postalCode, customer.postalCode) && Objects.equals(country, customer.country) &&
                Objects.equals(user, customer.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, phone, dateOfBirth, street, city, region, postalCode, country, user);
    }
}
