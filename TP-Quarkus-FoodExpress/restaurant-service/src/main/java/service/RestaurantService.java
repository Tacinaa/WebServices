package service;

import entity.Dish;
import entity.Restaurant;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
public class RestaurantService {

    public List<Restaurant> findAll() {
        return Restaurant.listAll();
    }

    public Optional<Restaurant> findById(Long id) {
        return Restaurant.findByIdOptional(id);
    }

    @Transactional
    public Restaurant create(Restaurant restaurant) {
        restaurant.persist();
        return restaurant;
    }

    @Transactional
    public Restaurant update(Long id, Restaurant updated) {
        Restaurant r = Restaurant.findById(id);
        if (r == null) return null;

        r.name = updated.name;
        r.cuisine = updated.cuisine;
        r.address = updated.address;
        r.city = updated.city;
        r.phone = updated.phone;
        r.rating = updated.rating;
        r.openingTime = updated.openingTime;
        r.closingTime = updated.closingTime;
        r.active = updated.active;

        return r;
    }

    @Transactional
    public boolean delete(Long id) {
        return Restaurant.deleteById(id);
    }

    @Transactional
    public Dish addDish(Long restaurantId, Dish dish) {
        Restaurant r = Restaurant.findById(restaurantId);
        if (r == null) return null;

        dish.restaurant = r;
        dish.persist();
        return dish;
    }

    @Transactional
    public boolean removeDish(Long restaurantId, Long dishId) {
        Dish dish = Dish.findById(dishId);
        if (dish == null || dish.restaurant == null) return false;
        if (!dish.restaurant.id.equals(restaurantId)) return false;

        return Dish.deleteById(dishId);
    }

    public boolean dishExists(Long restaurantId, Long dishId) {
        Dish dish = Dish.findById(dishId);
        return dish != null
                && dish.restaurant != null
                && dish.restaurant.id.equals(restaurantId);
    }

    public List<Restaurant> searchByCuisine(String cuisine) {
        if (cuisine == null || cuisine.isBlank()) return List.of();
        return Restaurant.list("cuisine", cuisine);
    }

    @Inject
    @Named("simple")
    MenuFormatter menuFormatter;

    public String formatMenu(Long restaurantId) {
        Restaurant r = Restaurant.findById(restaurantId);
        if (r == null) return "Restaurant not found";
        return menuFormatter.format(r);
    }
}