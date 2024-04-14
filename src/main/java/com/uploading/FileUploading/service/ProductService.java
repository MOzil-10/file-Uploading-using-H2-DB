package com.uploading.FileUploading.service;

import com.uploading.FileUploading.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    Product saveAttachment(MultipartFile file) throws IOException;
    void saveFiles(MultipartFile[] files) throws IOException;
    List<Product> getAllFiles();
}
