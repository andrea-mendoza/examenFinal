package com.ucbcba.demo.services;

import com.ucbcba.demo.Entities.City;
import com.ucbcba.demo.Entities.Restaurant;

import java.util.List;

public interface RestaurantService {
    Iterable<Restaurant> listAllRestaurants();

    void saveRestaurant(Restaurant restaurant);

    Restaurant getRestaurant(Integer id);

    void deleteRestaurant(Integer id);

    Iterable<Restaurant> getRestaurantLikeName(String name, Integer id);

    Iterable<Restaurant> getByCity(City cityId);
}
