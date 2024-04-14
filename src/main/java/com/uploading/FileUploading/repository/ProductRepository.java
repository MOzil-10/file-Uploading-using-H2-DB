package com.uploading.FileUploading.repository;


import com.uploading.FileUploading.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {


}
