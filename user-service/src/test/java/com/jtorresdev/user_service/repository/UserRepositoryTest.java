package com.jtorresdev.user_service.repository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

import com.jtorresdev.user_service.entity.UserEntity;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Transactional
    @Test
    void testGuardarUsuario(){
        //given -> dado
        UserEntity user = new UserEntity();
        user.setName("pedro");
        user.setEmail("pedro@test.com");


        //when ->accion o comportamiento  a testear

        UserEntity userSave= userRepository.save(user);

       // Optional<UserEntity> userResponse= userRepository.findById(1);
        Optional<UserEntity> userResponse = userRepository.findById(userSave.getId());
        //then -> verifica la salida
        assertTrue(userResponse.isPresent());
        assertEquals("pedro",userResponse.get().getName());

    }

}
