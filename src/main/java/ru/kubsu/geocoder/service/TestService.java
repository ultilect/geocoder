package ru.kubsu.geocoder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kubsu.geocoder.model.Test;
import ru.kubsu.geocoder.repository.TestRepository;

@Service
public class TestService {

    private TestRepository repository;

    @Autowired
    public TestService(TestRepository repository) {
        this.repository = repository;
    }
    public Test build(Integer id, String name) {
        Test test = new Test();
        test.setId(id);
        test.setName(name);
        return test;
    }

    public void save(String name) {
        Test test = new Test();
        test.setName(name);

        repository.save(test);
    }

    public Test load(String name) {
        return repository.findByName(name)
                .orElse(null);
    }
}
