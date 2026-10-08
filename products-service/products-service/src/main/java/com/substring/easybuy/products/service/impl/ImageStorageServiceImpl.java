package com.substring.easybuy.products.service.impl;

import com.substring.easybuy.products.service.ImageStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ImageStorageServiceImpl implements ImageStorageService {

    @Value("${image.storage.path:uploads/images}")
    private String storagePath;

    @Value("${image.storage.base-url:http://localhost:8080/images}")
    private String baseUrl;

    private Path getStorageDirectory() throws IOException {
        Path path = Paths.get(storagePath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        return path;
    }

    @Override
    @Transactional
    public String uploadImage(String fileName, byte[] imageData) {
        try {
            String uniqueFileName = UUID.randomUUID() + "_" + fileName;
            Path storageDirectory = getStorageDirectory();
            Path filePath = storageDirectory.resolve(uniqueFileName);
            
            Files.write(filePath, imageData, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
            String imageUrl = baseUrl + "/" + uniqueFileName;
            log.info("Image uploaded successfully: {}", imageUrl);
            return imageUrl;
        } catch (IOException e) {
            log.error("Failed to upload image: {}", fileName, e);
            throw new RuntimeException("Failed to upload image: " + fileName, e);
        }
    }

    @Override
    @Transactional
    public List<String> uploadImages(List<String> fileNames, List<byte[]> imageDataList) {
        if (fileNames == null || imageDataList == null || fileNames.size() != imageDataList.size()) {
            throw new IllegalArgumentException("File names and image data lists must be non-null and of equal size");
        }

        List<String> imageUrls = new ArrayList<>();
        for (int i = 0; i < fileNames.size(); i++) {
            String imageUrl = uploadImage(fileNames.get(i), imageDataList.get(i));
            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }

    @Override
    @Transactional
    public void deleteImage(String imageUrl) {
        try {
            String fileName = extractFileName(imageUrl);
            Path storageDirectory = getStorageDirectory();
            Path filePath = storageDirectory.resolve(fileName);
            
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("Image deleted successfully: {}", imageUrl);
            } else {
                log.warn("Image not found for deletion: {}", imageUrl);
            }
        } catch (IOException e) {
            log.error("Failed to delete image: {}", imageUrl, e);
            throw new RuntimeException("Failed to delete image: " + imageUrl, e);
        }
    }

    @Override
    @Transactional
    public void deleteImages(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }
        for (String imageUrl : imageUrls) {
            deleteImage(imageUrl);
        }
    }

    @Override
    public byte[] downloadImage(String imageUrl) {
        try {
            String fileName = extractFileName(imageUrl);
            Path storageDirectory = getStorageDirectory();
            Path filePath = storageDirectory.resolve(fileName);
            
            if (!Files.exists(filePath)) {
                throw new RuntimeException("Image not found: " + imageUrl);
            }
            
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            log.error("Failed to download image: {}", imageUrl, e);
            throw new RuntimeException("Failed to download image: " + imageUrl, e);
        }
    }

    @Override
    public boolean exists(String imageUrl) {
        try {
            String fileName = extractFileName(imageUrl);
            Path storageDirectory = getStorageDirectory();
            Path filePath = storageDirectory.resolve(fileName);
            return Files.exists(filePath);
        } catch (IOException e) {
            log.error("Failed to check image existence: {}", imageUrl, e);
            return false;
        }
    }

    private String extractFileName(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new IllegalArgumentException("Image URL cannot be null or empty");
        }
        int lastSlashIndex = imageUrl.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            return imageUrl;
        }
        return imageUrl.substring(lastSlashIndex + 1);
    }
}
