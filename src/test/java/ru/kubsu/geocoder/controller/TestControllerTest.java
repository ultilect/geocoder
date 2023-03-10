package ru.kubsu.geocoder.controller;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kubsu.geocoder.dto.RestApiError;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestControllerTest {
    @LocalServerPort
    private Integer port;
    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    @BeforeAll
    static void beforeAll() {

    }
    @AfterAll
    static void afterAll() {
    }

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void integrationTest() {
        ResponseEntity<ru.kubsu.geocoder.model.Test> response =
                testRestTemplate.getForEntity("http://localhost:"+
                        this.port+"/tests/check/5?name=newname",ru.kubsu.geocoder.model.Test.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        final ru.kubsu.geocoder.model.Test body = response.getBody();
        if(body == null) {
            fail("Body is null");
        }
        assertEquals(5, body.getId());
        assertEquals("newname", body.getName());
        //assertEquals(null, body.getDone());
        assertNull(body.getDone());
        //assertEquals(null, body.getMark());
        assertNull(body.getMark());
    }

    @Test
    void integrationTestWhenNameIsNull() { //Negative Test
       ResponseEntity<HashMap<String, String>> response = testRestTemplate
               .exchange("http://localhost:" + this.port + "/tests/check/1", HttpMethod.GET, null,
                       new ParameterizedTypeReference<HashMap<String, String>>() {});
       assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

       final HashMap<String, String> body = response.getBody();
       System.out.println(body);
       if(body == null) {
           fail("Body is null");
       }
       assertEquals("/tests/check/1", body.get("path"));
       body.remove("path");

       assertEquals("Bad Request", body.get("error"));
       body.remove("error");

       assertEquals("400", body.get("status"));
       body.remove("status");

       body.remove("timestamp");
       //assertEquals(true, body.isEmpty());
       assertTrue(body.isEmpty());
    }

    @Test
    void integrationTestWhenIdIsString() { //Negative Test
       ResponseEntity<RestApiError> response = testRestTemplate
               .exchange("http://localhost:" + this.port + "/tests/check/str?name=test",
                       HttpMethod.GET, null, RestApiError.class);
       assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

       final RestApiError body = response.getBody();
       System.out.println(body);
       if(body == null) {
           fail("Body is null");
       }

       assertEquals("/tests/check/str", body.getPath());
       assertEquals("Bad Request", body.getError());
       assertEquals(400, body.getStatus());

    }
}
