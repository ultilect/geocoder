package ru.kubsu.geocoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kubsu.geocoder.model.Test;
import ru.kubsu.geocoder.service.TestService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Bogdan Lesin
 */
@RestController
@RequestMapping("tests")
public class TestController {

    private final TestService service;
    @Autowired
    public TestController(final TestService service) {
        this.service = service;
    }

    @GetMapping(value = "/check/{id}", produces = APPLICATION_JSON_VALUE)
    public Test test(final @PathVariable("id") Integer id,
                     final @RequestParam("name") String name) {
        return service.build(id, name);
    }

    @GetMapping(value = "/saveTest", produces = APPLICATION_JSON_VALUE)
    public void save(final @RequestParam("name") String name) {
        service.save(name);
    }

    @GetMapping(value = "/load/{name}", produces = APPLICATION_JSON_VALUE)
    public Test load(final @PathVariable("name") String name) {
        return service.load(name);
    }
}
