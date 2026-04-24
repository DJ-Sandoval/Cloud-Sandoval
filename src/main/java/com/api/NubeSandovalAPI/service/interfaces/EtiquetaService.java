package com.api.NubeSandovalAPI.service.interfaces;

import com.api.NubeSandovalAPI.entities.Etiqueta;

import java.util.List;

public interface EtiquetaService {
    List<Etiqueta> obtenerOCrearEtiquetas(List<String> nombres);
}
