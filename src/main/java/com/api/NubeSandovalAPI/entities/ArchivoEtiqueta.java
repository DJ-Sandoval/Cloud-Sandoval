package com.api.NubeSandovalAPI.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "archivo_etiqueta")
@Getter
@Setter
@NoArgsConstructor

public class ArchivoEtiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Archivo archivo;

    @ManyToOne
    private Etiqueta etiqueta;
}
