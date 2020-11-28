package io.github.mat3e.controller;

import io.github.mat3e.model.Task;
import io.github.mat3e.model.TaskRepository;
import org.hibernate.internal.build.AllowPrintStacktrace;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import javax.naming.directory.NoSuchAttributeException;
import javax.persistence.SecondaryTable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.OptionalInt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.mockito.ArgumentMatchers.anyInt;

//@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TaskRepository repo;

    @Test
    void httpGet_returnsAllTasks(){
        // given
        int initial = repo.findAll().size();
        repo.save(new Task("foo", LocalDateTime.now()));
        repo.save(new Task("bar", LocalDateTime.now()));


        // when
        Task[] result = restTemplate.getForObject("http://localhost:"+ port + "/tasks", Task[].class);

        // then
        assertThat(result).hasSize(initial + 2);

    }

    @Test
    void httpGet_returnsSingleTaskIfExist(){
        // given
        int initial = repo.findAll().size();
        var result = repo.findById(initial - 1);

        // then
        assertThat(result.get()).isInstanceOf(Task.class);
    }

    @Test
    void httpGet_returnsSingleTaskIfNotExist(){
        // given
        int initial = repo.findAll().size();
//        var result = repo.findById(initial + 1);

        // when
        var result = repo.findById(initial+1);

        // then
        assertThat(result).isEqualTo(Optional.empty());
    }

}