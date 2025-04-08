package com.jtorresdev.bike_service.service;

import com.jtorresdev.bike_service.entity.BikeEntity;

import com.jtorresdev.bike_service.repository.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeService {
    @Autowired
    BikeRepository bikeRepository;

    public List<BikeEntity> getAll(){
        return bikeRepository.findAll();
    }

    public BikeEntity getCarById(int id){
        return bikeRepository.findById(id).orElse(null);
    }

    public BikeEntity save(BikeEntity user){
        BikeEntity carNew= bikeRepository.save(user);
        return carNew;
    }
    public  List<BikeEntity> byUserId(int userId){
        return bikeRepository.findByUserId(userId);
    }
}
