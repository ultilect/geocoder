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
import ru.kubsu.geocoder.dto.RestApiError;
import ru.kubsu.geocoder.model.Address;
import ru.kubsu.geocoder.repository.AddressRepository;
import ru.kubsu.geocoder.repository.TestRepository;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
        final String query = "кубгу";
        final Address testPlace = buildTestAddress(query);
        when(nominatimClient.search(anyString())).thenReturn(Optional.of(buildTestPlace()));

        ResponseEntity<Address> response =
                testRestTemplate.getForEntity("http://localhost:"+
                        this.port+"/geocoder/search?q=" + query,Address.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final Address body = response.getBody();
        if(body == null) {
            fail("Body is null");
        }
        assertEquals(testPlace.getQuery(),body.getQuery());
        assertEquals(testPlace.getAddress(),body.getAddress());
        assertEquals(testPlace.getLongitude(),body.getLongitude());
        assertEquals(testPlace.getLatitude(),body.getLatitude());
    }

    @Test
    void searchCheckCache() {
        final String query = "кубгу";
        when(nominatimClient.search(anyString())).thenReturn(Optional.of(buildTestPlace()));

        ResponseEntity<Address> response1 =
                testRestTemplate.getForEntity("http://localhost:"+
                        this.port+"/geocoder/search?q=" + query,Address.class);
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        Address body1 = response1.getBody();
        if(body1 == null) {
            fail("Body is null");
        }

        AtomicReference<Integer> amountRecords = new AtomicReference<>(0);
        addressRepository.findAll()
                .forEach(place -> amountRecords .getAndSet(amountRecords .get() + 1));
        assertEquals(1, amountRecords.get());

        ResponseEntity<Address> response2 =
                testRestTemplate.getForEntity("http://localhost:"+
                        this.port+"/geocoder/search?q=" + query,Address.class);
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        Address body2 = response2.getBody();
        if(body2 == null) {
            fail("Body is null");
        }
        assertEquals(body1, body2);

        AtomicReference<Integer> amountRecords2 = new AtomicReference<>(0);
        addressRepository.findAll()
                .forEach(place -> amountRecords2 .getAndSet(amountRecords2 .get() + 1));
        assertEquals(1, amountRecords2.get());
    }

    @Test
    void searchWhenNominatimNotResponse() {
        final String query = "кубгу";
        final Address testPlace = buildTestAddress(query);
        when(nominatimClient.search(anyString())).thenReturn(Optional.empty());

        ResponseEntity<NominatimPlace> response =
                testRestTemplate.getForEntity("http://localhost:"+
                        this.port+"/geocoder/search?q=" + query,NominatimPlace.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
   }

   @Test
   void reverse() {
       final String query = null;
       final Address testPlace = buildTestAddress(query);
       when(nominatimClient.reverse(anyString(), anyString())).thenReturn(Optional.of(buildTestPlace()));

       ResponseEntity<Address> response =
               testRestTemplate.getForEntity("http://localhost:" +
                       this.port + "/geocoder/reverse?lat=10.5&lon=11", Address.class);

       assertEquals(HttpStatus.OK, response.getStatusCode());
       final Address body = response.getBody();

       if(body == null) {
           fail("Body is null");
       }

       assertEquals(testPlace.getLatitude(), body.getLatitude());
       assertEquals(testPlace.getLongitude(), body.getLongitude());
       assertEquals(testPlace.getAddress(), body.getAddress());
       assertNull(body.getQuery());
   }

   @Test
   void reverseCheckCache() {
       when(nominatimClient.reverse(anyString(), anyString())).thenReturn(Optional.of(buildTestPlace()));
       ResponseEntity<Address> response1 =
               testRestTemplate.getForEntity("http://localhost:" +
                       this.port + "/geocoder/reverse?lat=10.5&lon=11", Address.class);
       assertEquals(HttpStatus.OK, response1.getStatusCode());

       final Address body1 = response1.getBody();
       if(body1 == null) {
           fail("Body is null");
       }

       AtomicReference<Integer> amountRecords = new AtomicReference<>(0);
       addressRepository.findAll()
               .forEach(place -> amountRecords .getAndSet(amountRecords .get() + 1));
       assertEquals(1, amountRecords.get());

       ResponseEntity<Address> response2 =
               testRestTemplate.getForEntity("http://localhost:" +
                       this.port + "/geocoder/reverse?lat=10.5&lon=11", Address.class);
       assertEquals(HttpStatus.OK, response2.getStatusCode());

       final Address body2 = response2.getBody();
       if(body2 == null) {
           fail("Body is null");
       }
       assertEquals(body1, body2);

       AtomicReference<Integer> amountRecords2 = new AtomicReference<>(0);
       addressRepository.findAll()
               .forEach(place -> amountRecords2 .getAndSet(amountRecords2 .get() + 1));
       assertEquals(1, amountRecords2.get());
   }

   @Test
   void reverseWhenLonIsNull() {
        ResponseEntity<RestApiError> response = testRestTemplate.getForEntity("http://localhost:" +
                this.port + "/geocoder/reverse?lat=a", RestApiError.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        RestApiError body = response.getBody();
        if(body == null) {
            fail("Body is null");
        }
        assertEquals("/geocoder/reverse", body.path());
        assertEquals("Bad Request", body.error());
        assertEquals(400, body.status());
   }

   @Test
   void reverseWhenBadQueryParams() {
       final String query = null;
       when(nominatimClient.reverse(anyString(), anyString())).thenReturn(Optional.empty());
       ResponseEntity<RestApiError> response =
               testRestTemplate.getForEntity("http://localhost:" +
                       this.port + "/geocoder/reverse?lat=a&lon=b", RestApiError.class);
       assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
       final RestApiError body = response.getBody();
       assertNotNull(body);
       assertEquals(400, body.status());
       assertEquals("/geocoder/reverse", body.path());
       assertEquals("Bad Request", body.error());
   }

   private static NominatimPlace buildTestPlace() {
        return new NominatimPlace();
   }
    private static Address buildTestAddress(final String query) {
        return Address.of(buildTestPlace(), query);
    }
}
