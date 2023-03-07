package org.spsochnev.example.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ExampleController {

    @Autowired
    private ExampleService exampleService;

    @PostMapping(value = "/modify",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Integer> modify(@RequestBody ModifyRequestDTO requestDTO) {
        Integer id = requestDTO.id();
        Integer add = requestDTO.add();
        return exampleService.modify(id, add);
    }
}
