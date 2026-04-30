package com.api.NubeSandovalAPI.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "archivos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Archivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreOriginal;

    private String nombreFisico; // UUID

    private String ruta;

    private Long size;

    private String mimeType;

    private String hash;

    private LocalDateTime fechaSubida;
    private LocalDateTime lastModified;

    @ManyToOne
    @JoinColumn(name = "directorio_id")
    private Directorio directorio;

    @OneToMany(mappedBy = "archivo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArchivoEtiqueta> etiquetas = new ArrayList<>();
}
