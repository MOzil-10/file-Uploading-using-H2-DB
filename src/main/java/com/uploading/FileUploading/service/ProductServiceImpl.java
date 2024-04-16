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

    /*
    MultiPartFile is like a container provided by Java to manage uploaded files.
    Start by sanitizing the file to ensure that it does not bring anything that can be harmful to the app.
    The method throws two exceptions, checks if the file is valid and also checks the size of the file.
    Created attachment variable of type Product which is the actual file that we are going to save
     */
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
        }  catch (Exception e) {
            throw new RuntimeException("Could not save file: " + fileName, e);
        }
    }

    /*
    Method to save multiple files.
    Takes an array of MultipartFile named files.
    Make use of Arrays.asList to convert the array to a List<MultipartFile>.
    Calls the saveAttachment method to save the files.
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

    /*
    This method retrieves all the Product entities from the database (files)
    returns a list of Product objects representing all files stored in the database
     */
    public List<Product> getAllFiles() {
        return  productRepository.findAll();
    }
}
