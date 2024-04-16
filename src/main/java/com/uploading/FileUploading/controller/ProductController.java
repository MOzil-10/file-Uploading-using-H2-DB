package com.uploading.FileUploading.controller;

import com.uploading.FileUploading.model.Product;
import com.uploading.FileUploading.service.ProductService;
import com.uploading.FileUploading.service.ResponseClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Handles HTTP POST requests to upload a single file.
     * @param file The file to be uploaded.
     * @return ResponseEntity with the response for the uploaded file.
     * @throws Exception If an error occurs during file processing.
     */
    @PostMapping("/single/file")
    public ResponseEntity<ResponseClass> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        Product attachment = productService.saveAttachment(file);
        ResponseClass response = new ResponseClass(attachment.getFileName(),
                attachment.getFileType(),
                attachment.getData().length);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Handles HTTP POST requests to upload multiple files.
     * @param files The array of files to be uploaded.
     * @return ResponseEntity with the response for the uploaded files.
     * @throws Exception If an error occurs during file processing.
     */
    @PostMapping("/multiple/file")
    public ResponseEntity<List<ResponseClass>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) throws Exception {
        List<ResponseClass> responseList = new ArrayList<>();

        for (MultipartFile file : files) {
            Product attachment = productService.saveAttachment(file);
            ResponseClass response = new ResponseClass(attachment.getFileName(),
                    attachment.getFileType(),
                    attachment.getData().length);
            responseList.add(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(responseList);
    }

    /**
     * Retrieves all uploaded files.
     * @return ResponseEntity with the list of uploaded files.
     */
    @GetMapping("/all")
    public ResponseEntity<List<ResponseClass>> getAllFiles() {
        List<Product> products = productService.getAllFiles();
        List<ResponseClass> responseClasses = products.stream().map(product -> new ResponseClass(
                        product.getFileName(),
                        product.getFileType(),
                        product.getData().length))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(responseClasses);
    }
}
