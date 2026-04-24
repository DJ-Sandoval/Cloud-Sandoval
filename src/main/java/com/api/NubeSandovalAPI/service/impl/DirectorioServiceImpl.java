package com.api.NubeSandovalAPI.service.impl;

import com.api.NubeSandovalAPI.dto.DirectorioDTO;
import com.api.NubeSandovalAPI.entities.Directorio;
import com.api.NubeSandovalAPI.repository.DirectorioRepository;
import com.api.NubeSandovalAPI.service.interfaces.DirectorioService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class DirectorioServiceImpl implements DirectorioService {

    private final DirectorioRepository directorioRepository;

    public DirectorioServiceImpl(DirectorioRepository directorioRepository) {
        this.directorioRepository = directorioRepository;
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
}


