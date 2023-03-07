package org.spsochnev.example.task;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExampleService {

    @Autowired
    private ExampleObjectRepository exampleObjectRepository;

    public Map<String, Integer> modify(Integer id, Integer add) {
        Map<String, Integer> res = exampleObjectRepository.incrementCurrentValue(id, add);
        if (res.isEmpty()) throw new EntityNotFoundException();
        return res;
    }
}
