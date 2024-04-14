package com.uploading.FileUploading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.uploading.FileUploading")
public class FileUploadingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileUploadingApplication.class, args);
	}

}
