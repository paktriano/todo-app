package io.github.mat3e.controller;

import io.github.mat3e.TaskConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.sql.DataSource;


@RestController
@RequestMapping("/info")
class InfoController {
//    @Value("${spring.datasource.url}")
//    private String url;


    private TaskConfigurationProperties myProp;
    private DataSourceProperties dataSource;

    InfoController(final TaskConfigurationProperties myProp, final DataSourceProperties dataSource) {
        this.myProp = myProp;
        this.dataSource = dataSource;
    }

//    @GetMapping("/info/url")
    @Secured("ROLE_ADMIN")
    @GetMapping("/url")
    String url(){

        return dataSource.getUrl();
    }

//    @GetMapping("/info/prop")
    @RolesAllowed({"ROLE_USER"})
    @GetMapping("/prop")
    boolean myProp(){
       return myProp.getTemplate().isAllowMultipleTasks();
    }
}
