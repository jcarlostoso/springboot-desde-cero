package com.jtorresdev.car_service.service;

import com.jtorresdev.car_service.entity.CarEntity;
import com.jtorresdev.car_service.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    @Autowired
    CarRepository carRepository;

    public List<CarEntity> getAll(){
        return carRepository.findAll();
    }

    public CarEntity getCarById(int id){
        return carRepository.findById(id).orElse(null);
    }

    public CarEntity save(CarEntity user){
        CarEntity carNew= carRepository.save(user);
        return carNew;
    }

    public List<CarEntity> byUserId(int userId){
        return carRepository.findByUserId(userId);
    }
}
