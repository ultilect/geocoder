package ru.kubsu.geocoder.controller;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kubsu.geocoder.dto.RestApiError;
import ru.kubsu.geocoder.model.Mark;
import ru.kubsu.geocoder.repository.TestRepository;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestControllerTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    private TestRepository testRepository;
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

    @Test
    void integrationTestSaveTestModel() { // Positive Test
        ResponseEntity<Void> response = testRestTemplate
                .exchange("http://localhost:" + this.port + "/tests/saveTest?name=test",
                HttpMethod.GET, null, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void integrationTestSaveTestModelWhenNameIsNull() { // Negative Test
       ResponseEntity<Void> response = testRestTemplate
               .exchange("http://localhost:" + this.port + "/tests/saveTest",
               HttpMethod.GET, null, Void.class);

       assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void integrationTestSaveTestModelWhenExists() { // Negative Test
        ru.kubsu.geocoder.model.Test test =  new ru.kubsu.geocoder.model.Test();
        String nameTest = "newtest";
        test.setName(nameTest);
        testRepository.save(test);

        ResponseEntity<Void> response = testRestTemplate
                .exchange("http://localhost:" + this.port + "/tests/saveTest?name="+nameTest,
                        HttpMethod.GET, null, Void.class);
        //Bad error, need try-catch :/
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    @Test
    void integrationTestLoadTestModel() { // Positive Test
       ru.kubsu.geocoder.model.Test test =  new ru.kubsu.geocoder.model.Test();
       String nameTest = "newtest111";
       test.setName(nameTest);
       test.setDone(true);
       test.setMark(Mark.B);
       testRepository.save(test);

       ResponseEntity<ru.kubsu.geocoder.model.Test> response = testRestTemplate
               .exchange("http://localhost:"+this.port+"/tests/load/"+nameTest,
                       HttpMethod.GET, null, ru.kubsu.geocoder.model.Test.class);

       assertEquals(HttpStatus.OK, response.getStatusCode());

       final ru.kubsu.geocoder.model.Test body = response.getBody();
       if(body == null) {
           fail("Body is null");
       }

       assertEquals(nameTest, body.getName());
       assertTrue(body.getDone());
       assertEquals(Mark.B, body.getMark());
    }

    @Test
    void integrationTestLoadTestModelWhenNoRecord() {
        ResponseEntity<ru.kubsu.geocoder.model.Test> response = testRestTemplate
                .exchange("http://localhost:"+this.port+"/tests/load/teestt",
                        HttpMethod.GET, null, ru.kubsu.geocoder.model.Test.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        if(response.getBody() != null) {
            fail("No record. Body must be null");
        }
    }

    @Test
    void integrationTestLoadTestModelBadURL() {
        ResponseEntity<ru.kubsu.geocoder.model.Test> response = testRestTemplate
                .exchange("http://localhost:"+this.port+"/tests/load",
                        HttpMethod.GET, null, ru.kubsu.geocoder.model.Test.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
