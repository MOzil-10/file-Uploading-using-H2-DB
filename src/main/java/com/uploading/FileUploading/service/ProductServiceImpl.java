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
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    /**
     * Saves a single file as a Product entity.
     * @param file The file to be saved.
     * @return The saved Product entity.
     * @throws IOException If an I/O error occurs during file processing.
     * @throws MaxUploadSizeExceededException If the file size exceeds the maximum allowed size.
     */
    public Product saveAttachment(MultipartFile file) throws IOException, MaxUploadSizeExceededException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (fileName.contains("...")) {
                throw new Exception("File contains invalid path sequence: " + fileName);
            }
            if (file.getBytes().length > (1024 * 1024)) {
                throw new MaxUploadSizeExceededException(file.getSize());
            }

            Product attachment = new Product(fileName, file.getContentType(), file.getBytes());
            return productRepository.save(attachment);
        }  catch (Exception e) {
            throw new RuntimeException("Could not save file: " + fileName, e);
        }
    }

    /**
     * Saves multiple files as Product entities.
     * @param files The array of files to be saved.
     */
    public void saveFiles(MultipartFile[] files) {

        Arrays.asList(files).forEach(file -> {
            try {
                saveAttachment(file);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Retrieves all Product entities from the database.
     * @return A list of Product entities representing all files stored in the database.
     */
    public List<Product> getAllFiles() {
        return  productRepository.findAll();
    }
}
