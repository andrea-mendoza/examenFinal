package com.ucbcba.demo.repository;

import com.ucbcba.demo.Entities.LevelRestaurant;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface LevelRestaurantRepository extends CrudRepository<LevelRestaurant,Integer> {
}
