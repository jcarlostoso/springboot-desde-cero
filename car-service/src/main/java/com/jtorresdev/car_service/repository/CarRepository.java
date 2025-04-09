package com.jtorresdev.car_service.repository;

import com.jtorresdev.car_service.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<CarEntity,Integer> {
    List<CarEntity>findByUserId(int userId);
}
