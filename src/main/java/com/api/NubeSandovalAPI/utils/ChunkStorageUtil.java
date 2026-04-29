package com.api.NubeSandovalAPI.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

@Component
public class ChunkStorageUtil {

    @Value("${storage.base-path}")
    private String basePath;

    public Path getChunkDir(String uploadId) {
        return Paths.get(basePath, "tmp", uploadId);
    }

    public void saveChunk(String uploadId, int chunkIndex, MultipartFile file) {

        try {
            Path dir = getChunkDir(uploadId);
            Files.createDirectories(dir);

            Path chunkPath = dir.resolve("chunk_" + chunkIndex);

            Files.copy(file.getInputStream(), chunkPath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException("Error guardando chunk", e);
        }
    }

    public Path mergeChunks(String uploadId, int totalChunks, String finalFileName) {

        try {
            Path dir = getChunkDir(uploadId);
            Path finalPath = Paths.get(basePath, finalFileName);

            try (OutputStream os = Files.newOutputStream(finalPath)) {

                for (int i = 0; i < totalChunks; i++) {
                    Path chunk = dir.resolve("chunk_" + i);
                    Files.copy(chunk, os);
                }
            }

            return finalPath;

        } catch (IOException e) {
            throw new RuntimeException("Error uniendo chunks", e);
        }
    }

    public void deleteChunks(String uploadId) {
        try {
            Path dir = getChunkDir(uploadId);
            if (Files.exists(dir)) {
                Files.walk(dir)
                        .sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try { Files.delete(path); } catch (IOException ignored) {}
                        });
            }
        } catch (IOException e) {
            throw new RuntimeException("Error limpiando chunks");
        }
    }
}
