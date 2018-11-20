package io.github.cepr0.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import static java.util.Arrays.asList;

@SpringBootApplication
public class Application {

	private final AppService appService;

	public Application(AppService appService) {
		this.appService = appService;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener
	public void onReady(ApplicationReadyEvent e) {

		Student s1 = appService.createStudent("s1");
		Student s2 = appService.createStudent("s2");
		Student s3 = appService.createStudent("s3");
		Student s4 = appService.createStudent("s4");

		Group g1 = appService.createGroup("g1", asList(s1.getId(), s2.getId()));

		appService.updateGroup(g1.getId(), "g1_", asList(s3.getId(), s4.getId()));
	}
}
