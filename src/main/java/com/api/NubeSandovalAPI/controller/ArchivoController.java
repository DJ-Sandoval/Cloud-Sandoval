package com.api.NubeSandovalAPI.controller;

import com.api.NubeSandovalAPI.dto.ArchivoResponseDTO;
import com.api.NubeSandovalAPI.dto.ExplorerResponseDTO;
import com.api.NubeSandovalAPI.service.interfaces.ArchivoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/archivos")
public class ArchivoController {

    private final ArchivoService archivoService;

    public ArchivoController(ArchivoService archivoService) {
        this.archivoService = archivoService;
    }

    @PostMapping
    public ResponseEntity<ArchivoResponseDTO> subir(
            @RequestParam MultipartFile file,
            @RequestParam(required = false) List<String> etiquetas,
            @RequestParam(required = false) Long directorioId) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(archivoService.subirArchivo(file, etiquetas, directorioId));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ArchivoResponseDTO>> buscar(
            @RequestParam String q) {

        return ResponseEntity.ok(archivoService.buscar(q));
    }

    @GetMapping("/{id}/thumbnail")
    public ResponseEntity<byte[]> thumbnail(@PathVariable Long id) {

        byte[] imagen = archivoService.obtenerThumbnail(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imagen);
    }

    @GetMapping("/explorer")
    public ResponseEntity<ExplorerResponseDTO> explorer(
            @RequestParam(required = false) Long directorioId) {

        return ResponseEntity.ok(archivoService.explorar(directorioId));
    }
}