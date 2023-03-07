package org.spsochnev.example.task;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExampleServiceTests {

    @Mock
    private ExampleObjectRepository exampleObjectRepository;

    @InjectMocks
    private ExampleService exampleService;

    @Test
    @DisplayName("Current value is incremented successfully")
    public void testModifySuccess() {
        Map<String, Integer> map = new HashMap<>();
        map.put("current", 1);
        when(exampleObjectRepository.incrementCurrentValue(anyInt(), anyInt())).thenReturn(map);

        Map<String, Integer> res = exampleService.modify(1, 1);

        assertTrue(res.containsKey("current"));
        assertEquals(1, res.get("current"));
    }

    @Test
    @DisplayName("Current value is not found in DB")
    public void testModifyFail() {
        when(exampleObjectRepository.incrementCurrentValue(anyInt(), anyInt())).thenReturn(new HashMap<>());

        assertThrows(EntityNotFoundException.class, () -> exampleService.modify(1, 1));

        verify(exampleObjectRepository, times(1)).incrementCurrentValue(1, 1);
    }
}
