package io.github.mat3e;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
//import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
//import org.springframework.validation.Validator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import javax.validation.Validator;

@EnableAsync
@SpringBootApplication
//public class TodoAppApplication implements RepositoryRestConfigurer {
public class TodoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoAppApplication.class, args);
	}

	@Bean
	Validator validator(){
		return new LocalValidatorFactoryBean();
	}

//	@Override
//	public void configureValidatingRepositoryEventListener(final ValidatingRepositoryEventListener validatingListener) {
//		validatingListener.addValidator("beforeCreate", validator());
//		validatingListener.addValidator("beforeSave", validator());
//	}
}
