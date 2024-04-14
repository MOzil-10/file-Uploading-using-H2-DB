package com.uploading.FileUploading.service;

import com.uploading.FileUploading.model.Product;
import com.uploading.FileUploading.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Product saveAttachment(MultipartFile file) throws IOException, MaxUploadSizeExceededException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("...")) {
                throw new Exception("File contains invalid path sequence: " + fileName);
            }
            if (file.getBytes().length > (1024 * 1024)) {
                throw new MaxUploadSizeExceededException(file.getSize());
            }

            Product attachment = new Product(fileName, file.getContentType(), file.getBytes());
            return productRepository.save(attachment);
        } catch (Exception e) {
            throw new RuntimeException("Could not save file: " + fileName, e);
        }
    }

    public void saveFiles(MultipartFile[] files) {

        Arrays.asList(files).forEach(file -> {
            try {
                saveAttachment(file);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        });
    }

    public List<Product> getAllFiles() {
        return  productRepository.findAll();
    }
}
