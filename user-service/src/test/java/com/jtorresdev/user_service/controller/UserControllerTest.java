package com.jtorresdev.user_service.controller;

import com.jtorresdev.user_service.entity.UserEntity;
import com.jtorresdev.user_service.service.UserService;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private UserController userController;

    @Test
    void testGetAll() {

        // Se crean datos simulados
        UserEntity mockUser1 = new UserEntity(1, "Juan", "juan@email.com");
        UserEntity mockUser2 = new UserEntity(2, "Maria", "maria@email.com");
        List<UserEntity> mockUsers = List.of(mockUser1, mockUser2);

        // Se simula el comportamiento de UserService
        when(userService.getAll()).thenReturn(mockUsers);

        ResponseEntity<List<UserEntity>> response = userController.getAll();
        ResponseEntity<List<UserEntity>> result= ResponseEntity.ok(mockUsers);

        // Validaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Juan", response.getBody().get(0).getName());
        assertEquals("Maria", response.getBody().get(1).getName());

     /*user vacio*/
        // Simulamos que la lista está vacía
        when(userService.getAll()).thenReturn(Collections.emptyList());
        // Llamamos al controlador
        ResponseEntity<List<UserEntity>> responseNull = userController.getAll();

        // Validamos que se devuelve un estado 204 No Content
        assertEquals(HttpStatus.NO_CONTENT, responseNull.getStatusCode());
        assertNull(responseNull.getBody());  // No debería haber contenido en la respuesta

}
    @Test
    void getById(){

        // Se crean datos simulados
        int idUser=1;
        int idUserF=99;
        UserEntity mockUser1 = new UserEntity(1, "Juan", "juan@email.com");
        // Se simula el comportamiento de UserService
        when(userService.getUserbyId(idUser)).thenReturn(mockUser1);

        ResponseEntity<UserEntity> response = userController.getById(idUser);

        // Validaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(idUser,response.getBody().getId());


        /*user vacio*/
        when(userService.getUserbyId(idUserF)).thenReturn(null);
        ResponseEntity<UserEntity> responseNull=userController.getById(idUserF);
        assertEquals(HttpStatus.NO_CONTENT,responseNull.getStatusCode());
        assertNull(responseNull.getBody());

    }
    @Test
    void testCreateUser(){
        // Se crean datos simulados

        UserEntity mockRequestUser= new UserEntity(1, "Juan", "juan@email.com");
        // Se simula el comportamiento de UserService
        when(userService.save(Mockito.any(UserEntity.class))).thenReturn(mockRequestUser);

        ResponseEntity<UserEntity>userResponse=userController.create(mockRequestUser);


        // Validaciones
        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertNotNull(userResponse.getBody());
    }


}
