package com.api.NubeSandovalAPI.service.interfaces;

import com.api.NubeSandovalAPI.dto.DirectorioDTO;
import com.api.NubeSandovalAPI.entities.Directorio;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DirectorioService {
    Directorio crear(String nombre, Long padreId);

    List<Directorio> listarPorPadre(Long padreId);

    List<Directorio> obtenerArbol(Long rootId);
    List<DirectorioDTO> obtenerBreadcrumb(Long directorioId);
    DirectorioDTO renombrarDirectorio(Long directorioId, String nuevoNombre);
    void eliminarDirectorio(Long directorioId, boolean forzar);
    DirectorioDTO obtenerDirectorio(Long directorioId);
    List<Directorio> listarRaices();



}
