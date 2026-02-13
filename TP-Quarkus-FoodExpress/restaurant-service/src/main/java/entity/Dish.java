package entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

@Entity
public class Dish extends PanacheEntity {

    @NotBlank
    public String name;

    public String description;

    @NotNull
    @Positive
    public BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    public DishCategory category;

    public boolean available = true;

    public String allergens;

    @ManyToOne
    @JsonIgnore
    public Restaurant restaurant;
}