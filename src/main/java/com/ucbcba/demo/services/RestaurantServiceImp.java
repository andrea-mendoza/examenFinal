package com.ucbcba.demo.services;

import com.ucbcba.demo.Entities.City;
import com.ucbcba.demo.Entities.Restaurant;
import com.ucbcba.demo.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RestaurantServiceImp implements RestaurantService {

    private RestaurantRepository restaurantRepository;

    @Autowired
    @Qualifier(value = "restaurantRepository")
    public void setRestaurantRepository(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Iterable<Restaurant> listAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant getRestaurant(Integer id) {
        return restaurantRepository.findOne(id);
    }

    @Override
    public void deleteRestaurant(Integer id) {
        restaurantRepository.delete(id);
    }

    @Override
    public Iterable<Restaurant> getRestaurantLikeNameCityCategory(String name, Integer cityid, Integer categoryid) {
        return restaurantRepository.getRestaurantLikeNameCityCategory(name,cityid,categoryid);
    }

    @Override
    public Iterable<Restaurant> getRestaurantLikeNameCity(String name, Integer cityid) {
        return restaurantRepository.getRestaurantLikeNameCity(name,cityid);
    }

    @Override
    public Iterable<Restaurant> getRestaurantLikeNameCategory(String name, Integer categoryid) {
        return restaurantRepository.getRestaurantLikeNameCategory(name,categoryid);
    }

    @Override
    public Iterable<Restaurant> getRestaurantLikeCityCategory(Integer cityid, Integer categoryid) {
        return restaurantRepository.getRestaurantLikeCityCategory(cityid,categoryid);
    }

    @Override
    public Iterable<Restaurant> getRestaurantByCity(Integer cityid) {
        return restaurantRepository.getRestaurantByCity(cityid);
    }

    @Override
    public Iterable<Restaurant> getRestaurantByCategory(Integer categoryid) {
        return restaurantRepository.getRestaurantByCategory(categoryid);
    }

    @Override
    public Iterable<Restaurant> getRestaurantByName(String name) {
        return restaurantRepository.getRestaurantByName(name);
    }

    @Override
    public Iterable<Restaurant> getRestaurantByScore(Float score) {
        return restaurantRepository.getRestaurantByScore(score);
    }

    @Override
    public Iterable<Restaurant> getRestaurantLikeCityScore(Integer cityid, Float score) {
        return restaurantRepository.getRestaurantLikeCityScore(cityid,score);
    }

    @Override
    public Iterable<Restaurant> getRestaurantLikeNameScore(String name, Float score) {
        return restaurantRepository.getRestaurantLikeNameScore(name,score);
    }

    @Override
    public Iterable<Restaurant> getRestaurantLikeCategoryScore(Integer categoryid, Float score) {
        return restaurantRepository.getRestaurantLikeCategoryScore(categoryid,score);
    }

    @Override
    public Iterable<Restaurant> getRestaurantLikeNameCityCategoryScore(String name, Integer cityid, Integer categoryid, Float score) {
        return restaurantRepository.getRestaurantLikeNameCityCategoryScore(name,cityid, categoryid,score);
    }

    @Override
    public Iterable<Restaurant> getRestaurantLikeNameCityScore(String name, Integer cityid, Float score) {
        return restaurantRepository.getRestaurantLikeNameCityScore(name,cityid,score);
    }

    @Override
    public Iterable<Restaurant> getRestaurantLikeNameCategoryScore(String name, Integer categoryid, Float score) {
        return restaurantRepository.getRestaurantLikeNameCategoryScore(name,categoryid,score);
    }

    @Override
    public Iterable<Restaurant> getRestaurantLikeCityCategoryScore(Integer cityid, Integer categoryid, Float score) {
        return restaurantRepository.getRestaurantLikeCityCategoryScore(cityid,categoryid,score);
    }

    @Override
    public Iterable<Restaurant> listGeneralRanking(){
        return restaurantRepository.findTop5ByOrderByScoreDesc();
    }

}
