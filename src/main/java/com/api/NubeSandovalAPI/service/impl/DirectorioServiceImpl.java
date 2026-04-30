package com.api.NubeSandovalAPI.service.impl;

import com.api.NubeSandovalAPI.dto.DirectorioDTO;
import com.api.NubeSandovalAPI.entities.Directorio;
import com.api.NubeSandovalAPI.repository.ArchivoRepository;
import com.api.NubeSandovalAPI.repository.DirectorioRepository;
import com.api.NubeSandovalAPI.service.interfaces.DirectorioService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class DirectorioServiceImpl implements DirectorioService {
    private final ArchivoRepository archivoRepository;
    private final DirectorioRepository directorioRepository;

    public DirectorioServiceImpl(DirectorioRepository directorioRepository, ArchivoRepository archivoRepository) {
        this.directorioRepository = directorioRepository;
        this.archivoRepository = archivoRepository;
    }

    @Override
    public Directorio crear(String nombre, Long padreId) {

        Directorio padre = null;

        if (padreId != null) {
            padre = directorioRepository.findById(padreId)
                    .orElseThrow(() -> new RuntimeException("Directorio padre no existe"));
        }

        Directorio dir = new Directorio();
        dir.setNombre(nombre);
        dir.setPadre(padre);
        dir.setFechaCreacion(LocalDateTime.now());

        return directorioRepository.save(dir);
    }

    @Override
    public List<Directorio> listarPorPadre(Long padreId) {
        return directorioRepository.findByPadreId(padreId);
    }

    @Override
    public List<Directorio> obtenerArbol(Long rootId) {
        return directorioRepository.obtenerArbol(rootId);
    }
    @Override
    public List<DirectorioDTO> obtenerBreadcrumb(Long directorioId) {

        List<Directorio> ruta = directorioRepository.obtenerRutaHastaRoot(directorioId);

        Collections.reverse(ruta);

        return ruta.stream()
                .map(d -> {
                    DirectorioDTO dto = new DirectorioDTO();
                    dto.setId(d.getId());
                    dto.setNombre(d.getNombre());
                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional
    public DirectorioDTO renombrarDirectorio(Long directorioId, String nuevoNombre) {
        Directorio dir = directorioRepository.findById(directorioId)
                .orElseThrow(() -> new RuntimeException("Directorio no encontrado"));

        // Validar nombre duplicado
        if (dir.getPadre() != null) {
            directorioRepository.findByPadreIdAndNombreIgnoreCase(
                            dir.getPadre().getId(), nuevoNombre)
                    .ifPresent(d -> {
                        if (!d.getId().equals(directorioId)) {
                            throw new RuntimeException("Ya existe un directorio con ese nombre aquí");
                        }
                    });
        } else {
            directorioRepository.findByPadreIsNullAndNombreIgnoreCase(nuevoNombre)
                    .ifPresent(d -> {
                        if (!d.getId().equals(directorioId)) {
                            throw new RuntimeException("Ya existe un directorio con ese nombre en la raíz");
                        }
                    });
        }

        dir.setNombre(nuevoNombre);
        dir.setLastModified(LocalDateTime.now());
        Directorio actualizado = directorioRepository.save(dir);
        return convertirADTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminarDirectorio(Long directorioId, boolean forzar) {
        Directorio dir = directorioRepository.findById(directorioId)
                .orElseThrow(() -> new RuntimeException("Directorio no encontrado"));

        Long totalArchivos = archivoRepository.countByDirectorioId(directorioId);
        Long totalSubdirectorios = directorioRepository.countByPadreId(directorioId);

        if (!forzar && (totalArchivos > 0 || totalSubdirectorios > 0)) {
            throw new RuntimeException(
                    "El directorio no está vacío. Contiene " + totalArchivos +
                            " archivos y " + totalSubdirectorios +
                            " subdirectorios. Use forzar=true para eliminar con todo el contenido");
        }

        directorioRepository.delete(dir);
    }

    @Override
    public DirectorioDTO obtenerDirectorio(Long directorioId) {
        Directorio dir = directorioRepository.findById(directorioId)
                .orElseThrow(() -> new RuntimeException("Directorio no encontrado"));
        return convertirADTO(dir);
    }

    @Override
    public List<Directorio> listarRaices() {
        return directorioRepository.findRaices();
    }

    private DirectorioDTO convertirADTO(Directorio dir) {
        DirectorioDTO dto = new DirectorioDTO();
        dto.setId(dir.getId());
        dto.setNombre(dir.getNombre());
        dto.setFechaCreacion(dir.getFechaCreacion());
        dto.setLastModified(dir.getLastModified());

        if (dir.getPadre() != null) {
            dto.setPadreId(dir.getPadre().getId());
        }

        // Conteo de archivos y subdirectorios
        Long totalArchivos = archivoRepository.countByDirectorioId(dir.getId());
        Long totalSubdirectorios = directorioRepository.countByPadreId(dir.getId());

        dto.setTotalArchivos(totalArchivos != null ? totalArchivos.intValue() : 0);
        dto.setTotalSubdirectorios(totalSubdirectorios != null ? totalSubdirectorios.intValue() : 0);

        return dto;
    }
}


