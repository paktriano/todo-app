package io.github.mat3e.controller;

import io.github.mat3e.logic.TaskService;
import io.github.mat3e.model.Task;
import io.github.mat3e.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;


import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)

@ActiveProfiles("integration")
@AutoConfigureMockMvc
class TaskControllerLiteIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

//    @Autowired
    @MockBean
    private TaskRepository repo;

    @MockBean
    private TaskService service;

    @Test
    void httpGet_returnsGivenTask() {
        // given
        String description = "foo";
        when(repo.findById(anyInt()))
                .thenReturn(Optional.of(new Task(description, LocalDateTime.now())));
//        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();

        // when + then
        try {
            mockMvc.perform(get("/tasks/123"))
                    .andDo(print())
    //                .andExpect(status().is2xxSuccessful());
                    .andExpect(content().string(containsString(description)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
