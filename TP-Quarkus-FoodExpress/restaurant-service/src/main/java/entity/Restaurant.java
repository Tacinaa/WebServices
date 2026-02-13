package entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Restaurant extends PanacheEntity {

    @NotBlank
    @Size(min = 2, max = 100)
    public String name;

    @NotBlank
    public String cuisine;

    @NotBlank
    public String address;

    @NotBlank
    public String city;

    @Pattern(regexp = "^[0-9+ ]{6,20}$")
    public String phone;

    @DecimalMin("0.0")
    @DecimalMax("5.0")
    public Double rating;

    @NotNull
    public LocalTime openingTime;

    @NotNull
    public LocalTime closingTime;

    public boolean active = true;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Dish> dishes = new ArrayList<>();
}