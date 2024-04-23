package ru.kubsu.geocoder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kubsu.geocoder.model.Test;
import ru.kubsu.geocoder.repository.TestRepository;

/**
 * @author Bogdan Lesin
 */
@Service
public class TestService {

    private final TestRepository repository;

    @Autowired
    public TestService(final TestRepository repository) {
        this.repository = repository;
    }
    public Test build(final Integer id, final String name) {
        final Test test = new Test();
        test.setId(id);
        test.setName(name);
        return test;
    }

    public void save(final String name) {
        final Test test = new Test();
        test.setName(name);

        repository.save(test);
    }

    public Test load(final String name) {
        return repository.findByName(name)
                .orElse(null);
    }
}
