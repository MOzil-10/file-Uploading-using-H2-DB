package com.uploading.FileUploading.controller;

import com.uploading.FileUploading.model.Product;
import com.uploading.FileUploading.service.ProductService;
import com.uploading.FileUploading.service.ResponseClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
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

    /*
      This method handles HTTP POST requests to the endpoint /single/base
      Defined attachment variable and initialized it to null value
      Defined downloadUrl variable and initialized it to an empty string
      Calls the saveAttachment from the service(productService) to save the file
      downloadUrl creates url to download the file
       */
    @PostMapping("/single/file")
    public ResponseClass uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        Product attachment = null;
        String downloadURl = "";

        attachment = productService.saveAttachment(file);
        downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(attachment.getId())
                .toUriString();

        return new ResponseClass(attachment.getFileName(),
                downloadURl,
                file.getContentType(),
                file.getSize());
    }

    /*
    This method handles HTTP POST requests to the endpoint /multiple/base
    For each file, call the saveAttachment method of productService to save the file to the database.
     */
    @PostMapping("/multiple/file")
    public List<ResponseClass> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) throws Exception {
        List<ResponseClass> responseList = new ArrayList<>();

        for (MultipartFile file : files) {
            Product attachment = productService.saveAttachment(file);
            String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(attachment.getId())
                    .toUriString();
            ResponseClass response = new ResponseClass(attachment.getFileName(),
                    downloadUrl,
                    file.getContentType(),
                    file.getSize());
            responseList.add(response);
        }
        return responseList;
    }

    //for retrieving  all the  files uploaded
    @GetMapping("/all")
    public ResponseEntity<List<ResponseClass>> getAllFiles() {
        List<Product> products = productService.getAllFiles();
        List<ResponseClass> responseClasses = products.stream().map(product -> {
            String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(product.getId())
                    .toUriString();
            return new ResponseClass(product.getFileName(),
                    downloadURL,
                    product.getFileType(),
                    product.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.ok().body(responseClasses);
    }

}
