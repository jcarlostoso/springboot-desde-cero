package com.jtorresdev.user_service.service;

import com.jtorresdev.user_service.entity.UserEntity;
import com.jtorresdev.user_service.feignclients.BikeFeignClient;
import com.jtorresdev.user_service.feignclients.CarFeignClient;
import com.jtorresdev.user_service.model.Bike;
import com.jtorresdev.user_service.model.Car;
import com.jtorresdev.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {
//    @Mock
//    private UserRepository userRepository;
    @MockBean
    private UserRepository userRepository;

//    @InjectMocks
//    private UserService userService;
    @Autowired
    private UserService userService;

    @MockBean
    RestTemplate restTemplate;

    @MockBean
    CarFeignClient carFeignClient;

    @MockBean
    BikeFeignClient bikeFeignClient;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll(){
        // Se crean datos de prueba simulados
        UserEntity mockUser1= new UserEntity();
        mockUser1.setName("Juan");
        mockUser1.setEmail("juan@email.com");
        UserEntity mockUser2= new UserEntity();
        mockUser2.setName("Maria");
        mockUser2.setEmail("maria@email.com");

        List<UserEntity> mockUsers = new ArrayList<>();
        mockUsers.add(mockUser1);
        mockUsers.add(mockUser2);
        // Se configura el comportamiento del repositorio mockeado para devolver los usuarios simulados
        when(userRepository.findAll()).thenReturn(mockUsers);

        // Se llama al método que queremos probar
        List<UserEntity> users = userService.getAll();

        // Se verifica que la lista obtenida no sea nula
        assertNotNull(users);

        // Se verifica que la lista contenga exactamente 2 usuarios
        assertEquals(2, users.size());

        // Se comprueba que los datos sean correctos
        assertEquals("Juan", users.get(0).getName());
        assertEquals("Maria", users.get(1).getName());

        // Se verifica que `findAll()` fue llamado exactamente una vez
        verify(userRepository, times(1)).findAll();

    }
    @Test
    void testGetUserById() {

        // Definimos un usuario de prueba

        UserEntity mockUser = new UserEntity();
        mockUser.setId(1);  // Asignamos un ID manualmente para simular la base de datos
        mockUser.setName("Juan");
        mockUser.setEmail("juan@email.com");

        // Simulamos que el repositorio devuelve el usuario cuando se le pide por ID
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        // Llamamos al método a probar
        UserEntity result = userService.getUserbyId(1);


        // Verificamos que el usuario obtenido no es nulo
        assertNotNull(result);

        // Validamos que los datos coincidan con lo esperado
        assertEquals("Juan", result.getName());
        assertEquals("juan@email.com", result.getEmail());

        // Verificamos que `findById(1)` se haya ejecutado una sola vez
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testSaveUser() {
        // Definimos un usuario de prueba
        UserEntity mockUser = new UserEntity();
        mockUser.setId(1);  // Asignamos un ID manualmente para simular la base de datos
        mockUser.setName("Juan");
        mockUser.setEmail("juan@email.com");

        // Simulamos que el repositorio guarda el registro
            when(userRepository.save(mockUser)).thenReturn(mockUser);
        // Llamamos al método a probar
        UserEntity result = userService.save(mockUser);
        // Verificamos que el usuario guardado no es nulo
        assertNotNull(result);
        // Comprobamos que los datos guardados son correctos
        assertEquals("Juan", result.getName());
        assertEquals("juan@email.com", result.getEmail());
        assertTrue(result.getId() > 0);  // Confirmamos que tiene un ID asignado



    }
    @Test
    void testGetCars(){

        int userId =1;
        List<Car> mockCars = List.of(new Car("Toyota", "Corolla", userId), new Car("Honda", "Civic", userId));
        when(restTemplate.getForObject("http://localhost:8002/car/byuser/" + userId, List.class)).thenReturn(mockCars);
        List<Car> cars= userService.getCars(userId);
        assertNotNull(cars);
        assertEquals(2,cars.size());
        verify(restTemplate, times(1)).getForObject("http://localhost:8002/car/byuser/" + userId, List.class);

}
    @Test
    void testGetBikes() {

        /*
        * Explicación rápida
        * 1.Crea datos de prueba: Se define un  y una lista de bicicletas simuladas.
        * 2.Mockea la respuesta del servicio externo con  para evitar llamadas reales.
        * 3.Ejecuta el método userService.getBike(userId), el cual debería obtener la lista de bicicletas.
        * 4.Valida el resultado con assertNotNull  y assertEquals para confirmar que los datos obtenidos son correctos.
        * 5.Verifica con verify() que  restTemplate fue invocado exactamente una vez, asegurando que el método funciona como se espera
        * */


        // Se define un userId de prueba
        int userId = 1;

        // Se crea una lista simulada de bicicletas con datos ficticios
        List<Bike> mockBikes = List.of(new Bike("Toyota", "Corolla", userId), new Bike("Honda", "Civic", userId));

        // Se simula la llamada al servicio externo utilizando Mockito
        // Cuando se llame a esta URL con RestTemplate, se devolverá la lista `mockBikes` en lugar de hacer una petición real
        when(restTemplate.getForObject("http://localhost:8003/bike/byuser/" + userId, List.class)).thenReturn(mockBikes);

        // Se llama al método `getBike(userId)` de la clase `UserService`
        List<Bike> bikes = userService.getBike(userId);

        // Verificación de que la lista de bicicletas obtenida no es nula
        assertNotNull(bikes);

        // Verificación de que se obtuvieron exactamente 2 bicicletas en la respuesta
        assertEquals(2, bikes.size());

        // Se verifica que la llamada a RestTemplate se haya realizado exactamente una vez
        verify(restTemplate, times(1)).getForObject("http://localhost:8003/bike/byuser/" + userId, List.class);
    }

    @Test
    void testSaveCar(){
        /*Definimos los datos de prueba*/
        //Simula el auto a guardar
        int userId = 1;
        Car mockCar = new Car();
        mockCar.setUserId(userId);
        mockCar.setBrand("Toyota");
        mockCar.setModel("Corolla");

        // Simulación del coche guardado
        Car savedCar = new Car("Toyota", "Corolla", userId);

        // Simulamos la respuesta del Feign Client
        when(carFeignClient.save(mockCar)).thenReturn(savedCar);

        // Ejecutamos el método que queremos probar
        Car resultCar = userService.saveCar(userId, mockCar);
        // Validamos que el resultado no sea nulo
        assertNotNull(resultCar);

        // Validamos que los datos sean correctos
        assertEquals("Toyota", resultCar.getBrand());
        assertEquals("Corolla", resultCar.getModel());
        assertEquals(userId, resultCar.getUserId());

        // Verificamos que la llamada al Feign Client ocurrió exactamente una vez
        verify(carFeignClient, times(1)).save(mockCar);


    }

    @Test
    void testSaveBike(){
        /*Definimos los datos de prueba*/
        //Simula la moto/bike a guardar
        int userId = 1;
        Bike mockBikeRequest = new Bike("Yamaha","2020",userId);

        //simulado del moto a guardar
        Bike mockBikeResponse = new Bike("Yamaha","2020",userId);

        // Simulamos la respuesta del Feign Client
        when(bikeFeignClient.save(mockBikeRequest)).thenReturn(mockBikeResponse);

        // Ejecutamos el método que queremos probar en este caso del service
        Bike bikeResult= userService.saveBike(userId,mockBikeRequest);

        //hacemos las validaciones
        assertNotNull(bikeResult);
        assertEquals("Yamaha",bikeResult.getBrand());
        assertEquals("2020",bikeResult.getModel());

        // Verificamos que la llamada al Feign Client ocurrió exactamente una vez
        verify(bikeFeignClient,times(1)).save(mockBikeRequest);

    }

    @Test
    void testGetUserAndVehicles(){
        //simular los datos

        int userId = 1;

        // Simular usuario existente
        UserEntity mockUser = new UserEntity();
        mockUser.setId(userId);
        mockUser.setName("Juan");
        mockUser.setEmail("juan@email.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Simular coches asociados al usuario
        List<Car> mockCars = List.of(
                new Car("Toyota", "Corolla", userId),
                new Car("Honda", "Civic", userId)
        );
        when(carFeignClient.getCars(userId)).thenReturn(mockCars);//simula la respuesta de Feing

        // Simular motos asociadas al usuario
        List<Bike> mockBikes = List.of(
                new Bike("Yamaha", "R1", userId),
                new Bike("Suzuki", "GSX-R", userId));
        when(bikeFeignClient.getBikes(userId)).thenReturn(mockBikes);//simula la respuesta de Feing

        // Ejecutar el método a probar
        Map<String, Object> result = userService.getUserAndVehicles(userId);

        // Validar que el resultado no es nulo
        assertNotNull(result);

        // Validar que el usuario está presente en el resultado
        assertTrue(result.containsKey("User"));
        assertEquals(mockUser, result.get("User"));

        // Validar que los coches están en el resultado
        assertTrue(result.containsKey("Cars"));
        assertEquals(mockCars, result.get("Cars"));

        // Validar que las motos están en el resultado
        assertTrue(result.containsKey("Bikes"));
        assertEquals(mockBikes, result.get("Bikes"));

        // Verificar que cada servicio se llamó una sola vez
        verify(userRepository, times(1)).findById(userId);
        verify(carFeignClient, times(1)).getCars(userId);
        verify(bikeFeignClient, times(1)).getBikes(userId);

    }
    @Test
    void testGetUserAndVehicles_UserNotFound() {
        int userId = 99;

        // Simular que el usuario no existe
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Ejecutar el método
        Map<String, Object> result = userService.getUserAndVehicles(userId);
        System.out.println(result);
        // Validar que el resultado contiene el mensaje de error
        assertTrue(result.containsKey("Mensaje"));
        assertEquals("no existe el usuario", result.get("Mensaje"));

        // Verificar que `findById()` se llamó una vez, pero no se llamaron los otros servicios
        verify(userRepository, times(1)).findById(userId);
        verify(carFeignClient, never()).getCars(anyInt());
        verify(bikeFeignClient, never()).getBikes(anyInt());
    }
    @Test
    void testGetUserAndVehicles_UserWithoutCarsAndBikes() {
        int userId = 2;

        // Simular usuario existente
        UserEntity mockUser = new UserEntity();
        mockUser.setId(userId);
        mockUser.setName("Carlos");
        mockUser.setEmail("carlos@email.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Simular que no tiene coches
        when(carFeignClient.getCars(userId)).thenReturn(List.of());

        // Simular que no tiene motos
        when(bikeFeignClient.getBikes(userId)).thenReturn(List.of());

        // Ejecutar el método
        Map<String, Object> result = userService.getUserAndVehicles(userId);

        // Validar que el resultado no es nulo
        assertNotNull(result);

        // Verificar que el usuario está presente en el resultado
        assertTrue(result.containsKey("User"));
        assertEquals(mockUser, result.get("User"));

        // Verificar que el mensaje de "sin coches" aparece en el resultado
        assertTrue(result.containsKey("Cars"));
        assertEquals("Usuario sin coches", result.get("Cars"));

        // Verificar que el mensaje de "sin motos" aparece en el resultado
        assertTrue(result.containsKey("Bikes"));
        assertEquals("Usuario sin Motos", result.get("Bikes"));

        // Verificar que `findById()`, `getCars()` y `getBikes()` fueron llamados una vez cada uno
        verify(userRepository, times(1)).findById(userId);
        verify(carFeignClient, times(1)).getCars(userId);
        verify(bikeFeignClient, times(1)).getBikes(userId);
    }



}
