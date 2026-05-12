package com.example.edustream_saga_orchestrator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class EdustreamSagaOrchestratorApplication implements CommandLineRunner {

	@Value("${server.port}")
	private String port;

	public static void main(String[] args) {
		SpringApplication.run(EdustreamSagaOrchestratorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("EduStream Saga Orchestrator Started");
		log.info("Saga Orchestrator is running at http://localhost:{}", port);
	}

}
