package com.api.NubeSandovalAPI.service.interfaces;

import com.api.NubeSandovalAPI.dto.ArchivoResponseDTO;
import com.api.NubeSandovalAPI.dto.ExplorerResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArchivoService {
    ArchivoResponseDTO subirArchivo(
            MultipartFile file,
            List<String> etiquetas,
            Long directorioId);


    List<ArchivoResponseDTO> buscar(String query);
    byte[] obtenerThumbnail(Long archivoId);
    ExplorerResponseDTO explorar(Long directorioId);
}


