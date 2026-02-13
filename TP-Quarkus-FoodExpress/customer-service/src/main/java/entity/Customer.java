package entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
public class Customer extends PanacheEntity {

    @NotBlank
    @Size(min = 2, max = 50)
    public String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    public String lastName;

    @NotBlank
    @Email
    @Column(unique = true)
    public String email;

    @NotBlank
    @Pattern(regexp = "^[0-9+ ]{6,20}$")
    public String phone;

    @NotBlank
    public String address;

    @NotBlank
    public String city;

    @NotBlank
    @Pattern(regexp = "\\d{5}")
    public String zipCode;

    public LocalDateTime createdAt = LocalDateTime.now();

    public boolean active = true;
}