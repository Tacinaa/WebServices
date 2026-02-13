package service;

import entity.Restaurant;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.stream.Collectors;

@Named("detailed")
@ApplicationScoped
public class DetailedMenuFormatter implements MenuFormatter {

    @Override
    public String format(Restaurant restaurant) {
        if (restaurant.dishes == null || restaurant.dishes.isEmpty()) {
            return "No dishes available.";
        }
        return restaurant.dishes.stream()
                .map(d -> d.name
                        + " | " + (d.description == null ? "" : d.description)
                        + " | " + d.price + "€"
                        + " | Allergens: " + (d.allergens == null ? "None" : d.allergens))
                .collect(Collectors.joining("\n"));
    }
}