package com.jtorresdev.bike_service.controller;

import com.jtorresdev.bike_service.entity.BikeEntity;
import com.jtorresdev.bike_service.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bike")
public class BikeController {

    @Autowired
    BikeService bikeService;


    @GetMapping
    public ResponseEntity<List<BikeEntity>> getAll(){
        List<BikeEntity> bike= bikeService.getAll();
        if(bike.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bike);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BikeEntity> getById(@PathVariable("id") int id){
        BikeEntity bike = bikeService.getCarById(id);
        if(bike == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bike);
    }
    @PostMapping
    public ResponseEntity<BikeEntity> create(@RequestBody BikeEntity bike){
        BikeEntity bikeNew = bikeService.save(bike);
        return ResponseEntity.ok(bikeNew);
    }
    @GetMapping("/byuser/{userId}")
    public ResponseEntity<List<BikeEntity>>getByUserId(@PathVariable("userId") int userId){
        List<BikeEntity> bikes= bikeService.byUserId(userId);
        return ResponseEntity.ok(bikes);
    }
}
