package com.api.NubeSandovalAPI.service.interfaces;

import com.api.NubeSandovalAPI.dto.DirectorioDTO;
import com.api.NubeSandovalAPI.entities.Directorio;

import java.util.List;

public interface DirectorioService {
    Directorio crear(String nombre, Long padreId);

    List<Directorio> listarPorPadre(Long padreId);

    List<Directorio> obtenerArbol(Long rootId);
    List<DirectorioDTO> obtenerBreadcrumb(Long directorioId);
}
