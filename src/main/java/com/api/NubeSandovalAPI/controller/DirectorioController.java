package com.api.NubeSandovalAPI.controller;

import com.api.NubeSandovalAPI.dto.DirectorioDTO;
import com.api.NubeSandovalAPI.entities.Directorio;
import com.api.NubeSandovalAPI.service.interfaces.DirectorioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directorios")
@CrossOrigin("*")
public class DirectorioController {

    private final DirectorioService directorioService;

    public DirectorioController(DirectorioService directorioService) {
        this.directorioService = directorioService;
    }

    @PostMapping
    public ResponseEntity<Directorio> crear(
            @RequestParam String nombre,
            @RequestParam(required = false) Long padreId) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(directorioService.crear(nombre, padreId));
    }

    @GetMapping
    public ResponseEntity<List<Directorio>> listar(
            @RequestParam(required = false) Long padreId) {

        return ResponseEntity.ok(directorioService.listarPorPadre(padreId));
    }

    @GetMapping("/arbol/{id}")
    public ResponseEntity<List<Directorio>> arbol(@PathVariable Long id) {
        return ResponseEntity.ok(directorioService.obtenerArbol(id));
    }

    @GetMapping("/breadcrumbs/{id}")
    public ResponseEntity<List<DirectorioDTO>> breadcrumbs(@PathVariable Long id) {
        return ResponseEntity.ok(directorioService.obtenerBreadcrumb(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DirectorioDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(directorioService.obtenerDirectorio(id));
    }

    @PutMapping("/{id}/rename")
    public ResponseEntity<DirectorioDTO> renombrar(
            @PathVariable Long id,
            @RequestParam String nuevoNombre) {
        return ResponseEntity.ok(directorioService.renombrarDirectorio(id, nuevoNombre));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean forzar) {
        directorioService.eliminarDirectorio(id, forzar);
        return ResponseEntity.noContent().build();
    }
}
