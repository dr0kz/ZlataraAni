package com.webshop.configuration;

import java.io.*;
import java.nio.file.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;

public class FileUploadUtil {

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(new FileSystemResource("").getFile().getAbsolutePath()+Paths.get(uploadDir));

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }
    public static void deleteFile(String filePath){
        Path path = Paths.get(new FileSystemResource("").getFile().getAbsolutePath()+Paths.get(filePath));
        try {
            Files.delete(path);
        } catch (IOException e) {
            System.out.println("Failed to delete the file with path"+filePath);
        }
    }
}