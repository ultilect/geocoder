package ru.kubsu.geocoder.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kubsu.geocoder.client.NominatimClient;
import ru.kubsu.geocoder.dto.NominatimPlace;
import ru.kubsu.geocoder.model.Address;
import ru.kubsu.geocoder.repository.AddressRepository;
import ru.kubsu.geocoder.repository.TestRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GeocoderControllerTest {
    @LocalServerPort
    private Integer port;

    @MockBean
    private NominatimClient nominatimClient;
    @Autowired
    private TestRepository testRepository;

    @Autowired
    private AddressRepository addressRepository;
    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    @BeforeEach
    void setUp() {
       addressRepository.deleteAll();
    }
    @Test
    void search() {
        final Address testPlace = buildTestAddress();
        //TODO: исправить buildTestPlace() на реальные данные
        when(nominatimClient.search(anyString())).thenReturn(Optional.of(buildTestPlace()));

        ResponseEntity<Address> response =
                testRestTemplate.getForEntity("http://localhost:"+
                        this.port+"/geocoder/search?q=кубгу",Address.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final Address body = response.getBody();
        assertEquals(testPlace, body);
    }

    @Test
    void searchWhenNominatimNotResponse() {
        when(nominatimClient.search(anyString())).thenReturn(Optional.empty());

        ResponseEntity<NominatimPlace> response =
                testRestTemplate.getForEntity("http://localhost:"+
                        this.port+"/geocoder/search?q=кубгу",NominatimPlace.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
   }

   private static NominatimPlace buildTestPlace() {
        return new NominatimPlace();
   }
    private static Address buildTestAddress() {
        return Address.of(buildTestPlace());
    }
}
