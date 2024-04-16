package com.uploading.FileUploading.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseClass {

    private String fileName;
    private String fileType;
    private long fileSize;
}
