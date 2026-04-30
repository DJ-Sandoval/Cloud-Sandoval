package com.api.NubeSandovalAPI.controller;

import com.api.NubeSandovalAPI.dto.ArchivoResponseDTO;
import com.api.NubeSandovalAPI.dto.ExplorerResponseDTO;
import com.api.NubeSandovalAPI.service.interfaces.ArchivoService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/archivos")
@CrossOrigin
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

    @PutMapping("/{id}/rename")
    public ResponseEntity<ArchivoResponseDTO> renombrar(
            @PathVariable Long id,
            @RequestParam String nuevoNombre) {
        try {
            return ResponseEntity.ok(archivoService.renombrarArchivo(id, nuevoNombre));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/move")
    public ResponseEntity<ArchivoResponseDTO> mover(
            @PathVariable Long id,
            @RequestParam(required = false) Long directorioId) {
        return ResponseEntity.ok(archivoService.moverArchivo(id, directorioId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            archivoService.eliminarArchivo(id);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> descargar(@PathVariable Long id) {
        try {
            Resource resource = archivoService.descargarArchivo(id);
            ArchivoResponseDTO info = archivoService.obtenerArchivo(id);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(info.getMimeType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + info.getNombreOriginal() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArchivoResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(archivoService.obtenerArchivo(id));
    }

    @GetMapping("/raiz")
    public ResponseEntity<List<ArchivoResponseDTO>> listarRaiz() {
        return ResponseEntity.ok(archivoService.listarArchivosRaiz());
    }
}