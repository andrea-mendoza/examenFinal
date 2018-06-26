package com.ucbcba.demo.services;

import com.ucbcba.demo.Entities.LevelRestaurant;
import com.ucbcba.demo.repository.CategoryRepository;
import com.ucbcba.demo.repository.LevelRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.logging.Level;

@Service
public class LevelRestaurantServiceImp implements LevelRestaurantService{

    private LevelRestaurantRepository levelRestaurantRepository;
    @Autowired
    @Qualifier(value="levelRestaurantRepository")
    public  void setLevelRestaurantRepository(LevelRestaurantRepository levelRestaurantRepository){
        this.levelRestaurantRepository=levelRestaurantRepository;
    }

    @Override
    public Iterable<LevelRestaurant> listAllLevels() {
        return levelRestaurantRepository.findAll();
    }

    @Override
    public void saveLevel(LevelRestaurant level) {
        levelRestaurantRepository.save(level);
    }

    @Override
    public LevelRestaurant getLevelRestaurant(Integer id) {
        return levelRestaurantRepository.findOne(id);
    }

    @Override
    public void deleteLevel(Integer id) {
        levelRestaurantRepository.delete(id);
    }
}
