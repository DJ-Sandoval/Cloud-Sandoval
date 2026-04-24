package com.api.NubeSandovalAPI.repository;

import com.api.NubeSandovalAPI.entities.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {

    Optional<Etiqueta> findByNombreIgnoreCase(String nombre);

    List<Etiqueta> findByNombreIn(List<String> nombres);
}
