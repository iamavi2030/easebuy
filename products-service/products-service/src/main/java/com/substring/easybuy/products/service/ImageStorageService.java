package com.substring.easybuy.products.service;

import java.util.List;

public interface ImageStorageService {

    String uploadImage(String fileName, byte[] imageData);

    List<String> uploadImages(List<String> fileNames, List<byte[]> imageDataList);

    void deleteImage(String imageUrl);

    void deleteImages(List<String> imageUrls);

    byte[] downloadImage(String imageUrl);

    boolean exists(String imageUrl);
}
