package com.api.NubeSandovalAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExplorerResponseDTO {

    private List<DirectorioDTO> directorios;
    private List<ArchivoResponseDTO> archivos;
}
