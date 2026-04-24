package com.api.NubeSandovalAPI.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "directorios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Directorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_directorio_padre")
    private Directorio padre;

    private LocalDateTime fechaCreacion;


}
