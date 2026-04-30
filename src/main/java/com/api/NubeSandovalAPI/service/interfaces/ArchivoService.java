package com.api.NubeSandovalAPI.service.interfaces;

import com.api.NubeSandovalAPI.dto.ArchivoResponseDTO;
import com.api.NubeSandovalAPI.dto.ExplorerResponseDTO;
import org.springframework.core.io.Resource;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArchivoService {
    ArchivoResponseDTO subirArchivo(
            MultipartFile file,
            List<String> etiquetas,
            Long directorioId);


    List<ArchivoResponseDTO> buscar(String query);
    byte[] obtenerThumbnail(Long archivoId);
    ExplorerResponseDTO explorar(Long directorioId);
    String iniciarUpload();

    void subirChunk(String uploadId, int chunkIndex, MultipartFile file);

    ArchivoResponseDTO finalizarUpload(
            String uploadId,
            String nombreOriginal,
            int totalChunks,
            List<String> etiquetas,
            Long directorioId);

    ArchivoResponseDTO renombrarArchivo(Long archivoId, String nuevoNombre) throws IOException;
    ArchivoResponseDTO moverArchivo(Long archivoId, Long nuevoDirectorioId);
    void eliminarArchivo(Long archivoId) throws IOException;
    Resource descargarArchivo(Long archivoId) throws IOException;
    ArchivoResponseDTO obtenerArchivo(Long archivoId);
    List<ArchivoResponseDTO> listarArchivosRaiz();
}


