package com.api.NubeSandovalAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArchivoResponseDTO {
    private Long id;
    private String nombreOriginal;
    private String mimeType;
    private Long size;
    private String ruta;
}


