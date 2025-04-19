package com.jtorresdev.user_service.controller;

import com.jtorresdev.user_service.entity.UserEntity;
import com.jtorresdev.user_service.model.Bike;
import com.jtorresdev.user_service.model.Car;
import com.jtorresdev.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping
    public ResponseEntity<List<UserEntity>> getAll(){
        List<UserEntity> users=userService.getAll();
        if(users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getById(@PathVariable("id") int id){
        UserEntity user = userService.getUserbyId(id);
        if(user == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(user);
    }
    @PostMapping
    public ResponseEntity<UserEntity> create(@RequestBody UserEntity user){
        UserEntity userNew = userService.save(user);
        return ResponseEntity.ok(userNew);
    }
    @CircuitBreaker(name = carsCB, fallbackMethod = "fallbackGetCars")
    @GetMapping("/cars/{userId}")
    public ResponseEntity<List<Car>>getCars(@PathVariable("userId") int userId){
        //buscamos al user
        UserEntity user= userService.getUserbyId(userId);

        if (user ==null){
            return ResponseEntity.notFound().build();
        }
        //buscamos los cars del id usuario.
         List<Car>cars =userService.getCars(userId);
        return ResponseEntity.ok(cars);
    }
    @CircuitBreaker(name = bikesCB, fallbackMethod = "fallbackGetBikes")
    @GetMapping("/bikes/{userId}")
    public ResponseEntity<List<Bike>>getBikes(@PathVariable("userId") int userId){
        //buscamos al user
        UserEntity user= userService.getUserbyId(userId);

        if (user ==null){
            return ResponseEntity.notFound().build();
        }
        //buscamos los bikes del id usuario.
        List<Bike>bikes =userService.getBike(userId);
        return ResponseEntity.ok(bikes);
    }

    @CircuitBreaker(name = carsCB, fallbackMethod = "fallbackSaveCars")
    @PostMapping("/savecar/{userId}")
    public ResponseEntity<Car> saveCar(@PathVariable("userId") int userId, @RequestBody Car car) {
        if(userService.getUserbyId(userId) == null)
            return ResponseEntity.notFound().build();
        Car carNew = userService.saveCar(userId, car);
        return ResponseEntity.ok(car);
    }
    @CircuitBreaker(name = bikesCB, fallbackMethod = "fallbackSaveBikes")
    @PostMapping("/savebike/{userId}")
    public ResponseEntity<Bike>saveBike(@PathVariable("userId") int userId, @RequestBody Bike bike){
        if(userService.getUserbyId(userId)==null){
            return ResponseEntity.notFound().build();
        }
        Bike bikeNew= userService.saveBike(userId,bike);
        return  ResponseEntity.ok(bike);
    }
    @CircuitBreaker(name = allCB, fallbackMethod = "fallbackGetAll")
    @GetMapping("/getAll/{userId}")
    public ResponseEntity<Map<String,Object>> getAllVehicles(@PathVariable("userId") int userId){
        Map<String,Object>result=userService.getUserAndVehicles(userId);
        return ResponseEntity.ok(result);

    }

    private ResponseEntity<List<Car>> fallbackGetCars(@PathVariable("userId") int userId, RuntimeException e ){
        return new ResponseEntity("el usuario "+userId+ " tiene los coches en el taller",
        HttpStatus.OK);
    }

    private ResponseEntity<Car> fallbackSaveCars(@PathVariable("userId") int userId, @RequestBody Car car, RuntimeException e ){
        return new ResponseEntity("el usuario "+userId+ " no tiene dinero para carros",
                HttpStatus.OK);
    }

    private ResponseEntity<List<Bike>> fallbackGetBikes(@PathVariable("userId") int userId, RuntimeException e){
        return new ResponseEntity("el usuario "+userId+ " tiene las Motos en el taller",
                HttpStatus.OK);
    }

    private ResponseEntity<Bike> fallbackSaveBikes(@PathVariable("userId") int userId, @RequestBody Bike bike, RuntimeException e){
        return new ResponseEntity("el usuario "+userId+ "  no tiene dinero para motos",
                HttpStatus.OK);
    }

    public ResponseEntity<Map<String,Object>> fallbackGetAll(@PathVariable("userId") int userId, RuntimeException e){
        return new ResponseEntity("el usuario "+userId+ " tiene las Motos y los carros en el taller",
                HttpStatus.OK);
    }

}
