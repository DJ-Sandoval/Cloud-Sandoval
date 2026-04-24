package com.api.NubeSandovalAPI.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileStorageUtil {

    @Value("${storage.base-path}")
    private String basePath;

    public String guardarArchivo(MultipartFile file, String uuidNombre) {

        try {
            Path path = Paths.get(basePath, uuidNombre);
            Files.copy(file.getInputStream(), path);

            return path.toString();

        } catch (IOException e) {
            throw new RuntimeException("Error guardando archivo");
        }
    }
}
