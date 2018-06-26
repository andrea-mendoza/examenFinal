package com.ucbcba.demo.services;

import com.ucbcba.demo.Entities.LevelRestaurant;

public interface LevelRestaurantService {


    Iterable<LevelRestaurant> listAllLevels();

    void saveLevel(LevelRestaurant level);

    LevelRestaurant getLevelRestaurant(Integer id);

    void deleteLevel(Integer id);
}
