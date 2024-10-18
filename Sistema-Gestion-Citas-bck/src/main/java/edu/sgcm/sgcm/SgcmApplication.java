package edu.sgcm.sgcm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class SgcmApplication {

	public static void main(String[] args) {
		SpringApplication.run(SgcmApplication.class, args);
	}

}
