package com.uploading.FileUploading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ComponentScan("com.uploading.FileUploading")
@Configuration
@ComponentScan(basePackages = "com.uploading.FileUploading")
@EntityScan(basePackages = "com.uploading.FileUploading.model")
public class FileUploadingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileUploadingApplication.class, args);
	}

}
