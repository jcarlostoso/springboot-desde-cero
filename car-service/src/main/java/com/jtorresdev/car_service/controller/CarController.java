package com.jtorresdev.car_service.controller;

import com.jtorresdev.car_service.entity.CarEntity;
import com.jtorresdev.car_service.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {

    @Autowired
    CarService carService;


    @GetMapping
    public ResponseEntity<List<CarEntity>> getAll(){
        List<CarEntity> cars=carService.getAll();
        if(cars.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cars);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CarEntity> getById(@PathVariable("id") int id){
        CarEntity car = carService.getCarById(id);
        if(car == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(car);
    }
    @PostMapping
    public ResponseEntity<CarEntity> create(@RequestBody CarEntity car){
        CarEntity carNew = carService.save(car);
        return ResponseEntity.ok(carNew);
    }
    @GetMapping("/byuser/{userId}")
    public ResponseEntity<List<CarEntity>>getByUserId(@PathVariable("userId") int userId){
        List<CarEntity> cars=carService.byUserId(userId);
        return ResponseEntity.ok(cars);
    }
}
