package com.jtorresdev.user_service.feignclients;

import com.jtorresdev.user_service.model.Bike;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
/*
//Como se implementa eureka ya no se usa la url, con el "name" debe funcionar
@FeignClient(name = "bike-server", url = "http://localhost:8003/bike")
*/
@FeignClient(name = "bike-server")
public interface BikeFeignClient {
    @PostMapping()
    Bike save(@RequestBody Bike bike);

    @GetMapping("/byuser/{userId}")
    List<Bike> getBikes(@PathVariable("userId") int userId);
}
