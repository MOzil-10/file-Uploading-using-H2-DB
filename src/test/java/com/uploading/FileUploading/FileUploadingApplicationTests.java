package com.uploading.FileUploading;

import com.uploading.FileUploading.model.Product;
import com.uploading.FileUploading.repository.ProductRepository;
import com.uploading.FileUploading.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileUploadingApplicationTests {

	private final ProductRepository repository;
	private final ProductServiceImpl service;

	@Autowired
	public FileUploadingApplicationTests(ProductRepository repository, ProductServiceImpl service){
		this.repository = repository;
		this.service = service;
	}

	@Test
	public void testSaveAttachment()throws Exception{
		MockMultipartFile mockFile = new MockMultipartFile(
				"file","test.txt","text/plain","Hello World".getBytes()
		);
		Product product = service.saveAttachment(mockFile);
		assertNotNull(product.getId());
		assertEquals("test.txt", product.getFileName());
		assertEquals("text/plain", product.getFileType());

	}

	@Test
	public void testSaveFiles()throws Exception{
		MockMultipartFile mockFile1 = new MockMultipartFile(
				"file", "text.txt", "text/plain", "Hello Ozil".getBytes()
		);

		MockMultipartFile mockFile2 = new MockMultipartFile(
				"file", "text.txt", "text/plain", "Goodbye Ozil".getBytes()
		);
		service.saveFiles(new MockMultipartFile[]{mockFile1,mockFile2});
		List<Product> products = service.getAllFiles();
		System.out.println("Saved Files");
		for (Product product: products){
			System.out.println(product.getFileName());
		}
		assertEquals(2, products.size());
		assertEquals("test1.pdf", products.get(0).getFileName());
		assertEquals("test2.txt", products.get(1).getFileName());
	}
	@BeforeEach
	public void setUp() {
		repository.deleteAll();
	}

	@Test
	public void testSaveAttachmentInvalidName() {
		MockMultipartFile mockFile = new MockMultipartFile(
				"file", "../test.txt", "text/plain", "Hello, world!".getBytes());
		assertThrows(Exception.class, () -> service.saveAttachment(mockFile));
	}

	@Test
	public void testSaveAttachmentTooLarge() {
		byte[] bytes = new byte[1024 * 1024 * 10];
		MockMultipartFile mockFile = new MockMultipartFile(
				"file", "test.txt", "text/plain", bytes);
		assertThrows(Exception.class, () -> service.saveAttachment(mockFile));
	}
}
