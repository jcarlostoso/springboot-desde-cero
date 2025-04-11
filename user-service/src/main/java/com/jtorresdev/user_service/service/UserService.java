package com.jtorresdev.user_service.service;

import com.jtorresdev.user_service.entity.UserEntity;
import com.jtorresdev.user_service.feignclients.BikeFeignClient;
import com.jtorresdev.user_service.feignclients.CarFeignClient;
import com.jtorresdev.user_service.model.Bike;
import com.jtorresdev.user_service.model.Car;
import com.jtorresdev.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CarFeignClient carFeignClient;

    @Autowired
    BikeFeignClient bikeFeignClient;

    public List<UserEntity> getAll(){
        return userRepository.findAll();
    }

    public UserEntity getUserbyId(int id){
        return userRepository.findById(id).orElse(null);
    }

    public UserEntity save(UserEntity user){
        UserEntity userNew=userRepository.save(user);
        return userNew;
    }

    public List<Car> getCars(int userId){
        /*
        //Como se implementa eureka ya no se usa la url, con el "name" debe funcionar
        y en config/RestTempleteConfig.java agregar notacion  @LoadBalanced
        List<Car> cars = restTemplate.getForObject("http://localhost:8002/car/byuser/"+userId,List.class);
        */
        List<Car> cars = restTemplate.getForObject("http://car-service/car/byuser/"+userId,List.class);
        return cars;
    }

    public List<Bike>getBike(int bikeId){
                /*
        //Como se implementa eureka ya no se usa la url, con el "name" debe funcionar
         List<Bike> bikes = restTemplate.getForObject("http://localhost:8003/bike/byuser/"+bikeId,List.class);
         y en config/RestTempleteConfig.java agregar notacion  @LoadBalanced
        */
        List<Bike> bikes = restTemplate.getForObject("http://bike-service/bike/byuser/"+bikeId,List.class);
        return bikes;
    }

    public Car saveCar(int userId, Car car){
        car.setUserId(userId);
        Car carNew= carFeignClient.save(car);
        return carNew;
    }
    public Bike saveBike(int userId, Bike bike){
        bike.setUserId(userId);
        Bike nikeNew=bikeFeignClient.save(bike);
        return nikeNew;
    }

    public Map<String, Object> getUserAndVehicles(int userId){

        Map<String,Object> result= new HashMap<>();

        UserEntity user = userRepository.findById(userId).orElse(null);

        if(user == null){
            result.put("Mensaje", "no existe el usuario");
            return result;
        }
        result.put("User", user);

        List<Car>cars=carFeignClient.getCars(userId);
        if (cars.isEmpty()){
            result.put("Cars", "Usuario sin coches");
        }else {
            result.put("Cars", cars);
        }

        List<Bike>bikes= bikeFeignClient.getBikes(userId);
        if (bikes.isEmpty()){
            result.put("Bikes","Usuario sin Motos");
        }else {
            result.put("Bikes",bikes);
        }
        return result;

    }

}
