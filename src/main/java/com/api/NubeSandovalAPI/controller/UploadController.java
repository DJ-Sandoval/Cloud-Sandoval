package com.api.NubeSandovalAPI.controller;

import com.api.NubeSandovalAPI.dto.ArchivoResponseDTO;
import com.api.NubeSandovalAPI.dto.InitUploadResponse;
import com.api.NubeSandovalAPI.service.interfaces.ArchivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final ArchivoService archivoService;

    // 1. INIT
    @PostMapping("/init")
    public ResponseEntity<InitUploadResponse> init() {

        String uploadId = archivoService.iniciarUpload();

        InitUploadResponse res = new InitUploadResponse();
        res.setUploadId(uploadId);

        return ResponseEntity.ok(res);
    }

    // 2. CHUNK
    @PostMapping("/chunk")
    public ResponseEntity<Void> subirChunk(
            @RequestParam String uploadId,
            @RequestParam int chunkIndex,
            @RequestParam MultipartFile file) {

        archivoService.subirChunk(uploadId, chunkIndex, file);

        return ResponseEntity.ok().build();
    }

    // 3. COMPLETE
    @PostMapping("/complete")
    public ResponseEntity<ArchivoResponseDTO> complete(
            @RequestParam String uploadId,
            @RequestParam String nombreOriginal,
            @RequestParam int totalChunks,
            @RequestParam(required = false) List<String> etiquetas,
            @RequestParam(required = false) Long directorioId) {

        return ResponseEntity.ok(
                archivoService.finalizarUpload(
                        uploadId, nombreOriginal, totalChunks, etiquetas, directorioId)
        );
    }
}
