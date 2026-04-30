package com.api.NubeSandovalAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DirectorioDTO {

    private Long id;
    private String nombre;
    private Long padreId;                // NUEVO
    private LocalDateTime fechaCreacion; // NUEVO
    private LocalDateTime lastModified;  // NUEVO
    private int totalArchivos;           // NUEVO
    private int totalSubdirectorios;     // NUEVO
}
