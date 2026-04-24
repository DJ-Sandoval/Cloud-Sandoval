package com.api.NubeSandovalAPI.repository;

import com.api.NubeSandovalAPI.dto.ExplorerResponseDTO;
import com.api.NubeSandovalAPI.entities.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArchivoRepository extends JpaRepository<Archivo, Long> {

    Optional<Archivo> findByHash(String hash);

    @Query("""
        SELECT DISTINCT a FROM Archivo a
        LEFT JOIN ArchivoEtiqueta ae ON ae.archivo.id = a.id
        LEFT JOIN Etiqueta e ON e.id = ae.etiqueta.id
        WHERE LOWER(a.nombreOriginal) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
    """)
    List<Archivo> buscar(@Param("q") String query);
    Flux<ExplorerResponseDTO> explorar(Long directorioId);
    List<Archivo> findByDirectorioId(Long directorioId);
}
