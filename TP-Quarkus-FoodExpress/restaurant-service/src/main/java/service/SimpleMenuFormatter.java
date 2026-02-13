package service;

import entity.Restaurant;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.stream.Collectors;

@Named("simple")
@ApplicationScoped
public class SimpleMenuFormatter implements MenuFormatter {

    @Override
    public String format(Restaurant restaurant) {
        if (restaurant.dishes == null || restaurant.dishes.isEmpty()) {
            return "No dishes available.";
        }
        return restaurant.dishes.stream()
                .map(d -> d.name + " - " + d.price + "€")
                .collect(Collectors.joining("\n"));
    }
}