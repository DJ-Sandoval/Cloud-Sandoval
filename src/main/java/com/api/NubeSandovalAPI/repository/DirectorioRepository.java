package com.api.NubeSandovalAPI.repository;


import com.api.NubeSandovalAPI.entities.Directorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DirectorioRepository extends JpaRepository<Directorio, Long> {

    List<Directorio> findByPadreId(Long padreId);
    @Query("SELECT d FROM Directorio d WHERE d.padre IS NULL")
    List<Directorio> findRaices(); // NUEVO
    @Query("SELECT d FROM Directorio d WHERE d.padre.id = :padreId " +
            "AND LOWER(d.nombre) = LOWER(:nombre)")
    Optional<Directorio> findByPadreIdAndNombreIgnoreCase(
            @Param("padreId") Long padreId,
            @Param("nombre") String nombre); // NUEVO

    @Query("SELECT d FROM Directorio d WHERE d.padre IS NULL " +
            "AND LOWER(d.nombre) = LOWER(:nombre)")
    Optional<Directorio> findByPadreIsNullAndNombreIgnoreCase(
            @Param("nombre") String nombre); // NUEVO

    @Query("SELECT COUNT(d) FROM Directorio d WHERE d.padre.id = :padreId")
    Long countByPadreId(@Param("padreId") Long padreId); // NUEVO
    // 🌳 CTE: obtener árbol completo desde un nodo
    @Query(value = """
        WITH RECURSIVE arbol AS (
            SELECT * FROM directorios WHERE id = :rootId
            UNION ALL
            SELECT d.* FROM directorios d
            INNER JOIN arbol a ON d.id_directorio_padre = a.id
        )
        SELECT * FROM arbol
    """, nativeQuery = true)
    List<Directorio> obtenerArbol(@Param("rootId") Long rootId);

    @Query(value = """
        WITH RECURSIVE ruta AS (
            SELECT * FROM directorios WHERE id = :id
            UNION ALL
            SELECT d.* FROM directorios d
            INNER JOIN ruta r ON d.id = r.id_directorio_padre
        )
        SELECT * FROM ruta
    """, nativeQuery = true)
    List<Directorio> obtenerRutaHastaRoot(@Param("id") Long id);
}