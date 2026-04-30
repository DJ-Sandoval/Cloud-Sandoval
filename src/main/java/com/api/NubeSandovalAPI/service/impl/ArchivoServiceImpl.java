package com.api.NubeSandovalAPI.service.impl;

import com.api.NubeSandovalAPI.dto.ArchivoResponseDTO;
import com.api.NubeSandovalAPI.dto.DirectorioDTO;
import com.api.NubeSandovalAPI.dto.ExplorerResponseDTO;
import com.api.NubeSandovalAPI.entities.Archivo;
import com.api.NubeSandovalAPI.entities.ArchivoEtiqueta;
import com.api.NubeSandovalAPI.entities.Directorio;
import com.api.NubeSandovalAPI.entities.Etiqueta;
import com.api.NubeSandovalAPI.repository.ArchivoEtiquetaRepository;
import com.api.NubeSandovalAPI.repository.ArchivoRepository;
import com.api.NubeSandovalAPI.repository.DirectorioRepository;
import com.api.NubeSandovalAPI.service.exception.ArchivoDuplicadoException;
import com.api.NubeSandovalAPI.service.interfaces.ArchivoService;
import com.api.NubeSandovalAPI.service.interfaces.EtiquetaService;
import com.api.NubeSandovalAPI.utils.ChunkStorageUtil;
import com.api.NubeSandovalAPI.utils.FileStorageUtil;
import com.api.NubeSandovalAPI.utils.HashUtil;
import com.api.NubeSandovalAPI.utils.ThumbnailUtil;
import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ArchivoServiceImpl implements ArchivoService {

    private final ArchivoRepository archivoRepository;
    private final ArchivoEtiquetaRepository archivoEtiquetaRepository;
    private final EtiquetaService etiquetaService;
    private final DirectorioRepository directorioRepository;
    private final HashUtil hashUtil;
    private final FileStorageUtil storageUtil;
    private final ThumbnailUtil thumbnailUtil;
    private final ChunkStorageUtil chunkStorageUtil;

    public ArchivoServiceImpl(
            ArchivoRepository archivoRepository,
            ArchivoEtiquetaRepository archivoEtiquetaRepository,
            EtiquetaService etiquetaService,
            DirectorioRepository directorioRepository,
            HashUtil hashUtil,
            FileStorageUtil storageUtil,
            ThumbnailUtil thumbnailUtil,
            ChunkStorageUtil chunkStorageUtil
    ) {

        this.archivoRepository = archivoRepository;
        this.archivoEtiquetaRepository = archivoEtiquetaRepository;
        this.etiquetaService = etiquetaService;
        this.hashUtil = hashUtil;
        this.storageUtil = storageUtil;
        this.directorioRepository = directorioRepository;
        this.thumbnailUtil = thumbnailUtil;
        this.chunkStorageUtil = chunkStorageUtil;
    }

    @Transactional
    @Override
    public ArchivoResponseDTO subirArchivo(
            MultipartFile file,
            List<String> etiquetas,
            Long directorioId) {

        try {
            // 0. VALIDACIONES BÁSICAS
            if (file.isEmpty()) {
                throw new RuntimeException("El archivo está vacío");
            }

            // 1. HASH
            String hash = hashUtil.calcularSHA256(file.getInputStream());

            archivoRepository.findByHash(hash).ifPresent(a -> {
                throw new ArchivoDuplicadoException("Archivo duplicado detectado");
            });

            // 2. DIRECTORIO (🔥 NUEVO)
            Directorio directorio = null;

            if (directorioId != null) {
                directorio = directorioRepository.findById(directorioId)
                        .orElseThrow(() -> new RuntimeException("Directorio no existe"));
            }

            // 3. NOMBRE Y EXTENSIÓN (SEGURA)
            String original = file.getOriginalFilename();

            String extension = "";
            if (original != null && original.contains(".")) {
                extension = original.substring(original.lastIndexOf("."));
            }

            String uuid = UUID.randomUUID().toString();
            String nombreFisico = uuid + extension;

            // 4. GUARDAR ARCHIVO EN DISCO
            String ruta = storageUtil.guardarArchivo(file, nombreFisico);

            // 5. ENTITY ARCHIVO
            Archivo archivo = new Archivo();
            archivo.setNombreOriginal(original);
            archivo.setNombreFisico(nombreFisico);
            archivo.setRuta(ruta);
            archivo.setMimeType(file.getContentType());
            archivo.setSize(file.getSize());
            archivo.setHash(hash);
            archivo.setFechaSubida(LocalDateTime.now());
            archivo.setDirectorio(directorio); // 🔥 AQUÍ

            archivoRepository.save(archivo);

            // 6. ETIQUETAS
            List<Etiqueta> etiquetasDB = etiquetaService.obtenerOCrearEtiquetas(etiquetas);

            if (!etiquetasDB.isEmpty()) {
                List<ArchivoEtiqueta> relaciones = etiquetasDB.stream()
                        .map(et -> {
                            ArchivoEtiqueta ae = new ArchivoEtiqueta();
                            ae.setArchivo(archivo);
                            ae.setEtiqueta(et);
                            return ae;
                        })
                        .toList();

                archivoEtiquetaRepository.saveAll(relaciones);
            }

            return mapToDTO(archivo);

        } catch (IOException e) {
            throw new RuntimeException("Error procesando archivo", e);
        }
    }

    @Override
    public List<ArchivoResponseDTO> buscar(String query) {
        return archivoRepository.buscar(query)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }



    @Override
    public byte[] obtenerThumbnail(Long archivoId) {

        Archivo archivo = archivoRepository.findById(archivoId)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));

        Path ruta = Paths.get(archivo.getRuta());

        if (!Files.exists(ruta)) {
            throw new RuntimeException("Archivo físico no existe");
        }

        // Validar MIME
        if (!archivo.getMimeType().startsWith("image")) {
            throw new RuntimeException("No se puede generar thumbnail para este tipo");
        }

        return thumbnailUtil.generarThumbnail(ruta, 200, 200);
    }

    @Override
    public String iniciarUpload() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void subirChunk(String uploadId, int chunkIndex, MultipartFile file) {
        chunkStorageUtil.saveChunk(uploadId, chunkIndex, file);
    }

    @Override
    @Transactional
    public ArchivoResponseDTO finalizarUpload(
            String uploadId,
            String nombreOriginal,
            int totalChunks,
            List<String> etiquetas,
            Long directorioId) {

        // 1. EXTENSION
        String extension = "";
        if (nombreOriginal.contains(".")) {
            extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        }

        String uuid = UUID.randomUUID().toString();
        String nombreFisico = uuid + extension;

        // 2. MERGE
        Path finalPath = chunkStorageUtil.mergeChunks(uploadId, totalChunks, nombreFisico);

        // 3. HASH
        String hash;
        try (InputStream is = Files.newInputStream(finalPath)) {
            hash = hashUtil.calcularSHA256(is);
        } catch (IOException e) {
            throw new RuntimeException("Error hash");
        }

        archivoRepository.findByHash(hash).ifPresent(a -> {
            chunkStorageUtil.deleteChunks(uploadId);
            throw new ArchivoDuplicadoException("Archivo duplicado");
        });

        // 4. DIRECTORIO
        Directorio directorio = null;
        if (directorioId != null) {
            directorio = directorioRepository.findById(directorioId)
                    .orElseThrow(() -> new RuntimeException("Directorio no existe"));
        }

        // 5. ENTITY
        Archivo archivo = new Archivo();
        archivo.setNombreOriginal(nombreOriginal);
        archivo.setNombreFisico(nombreFisico);
        archivo.setRuta(finalPath.toString());
        archivo.setMimeType("application/octet-stream");
        archivo.setSize(finalPath.toFile().length());
        archivo.setHash(hash);
        archivo.setFechaSubida(LocalDateTime.now());
        archivo.setDirectorio(directorio);

        archivoRepository.save(archivo);

        // 6. ETIQUETAS
        List<Etiqueta> etiquetasDB = etiquetaService.obtenerOCrearEtiquetas(etiquetas);

        archivoEtiquetaRepository.saveAll(
                etiquetasDB.stream().map(et -> {
                    ArchivoEtiqueta ae = new ArchivoEtiqueta();
                    ae.setArchivo(archivo);
                    ae.setEtiqueta(et);
                    return ae;
                }).toList()
        );

        // 7. LIMPIAR
        chunkStorageUtil.deleteChunks(uploadId);

        return mapToDTO(archivo);
    }

    @Override
    public ExplorerResponseDTO explorar(Long directorioId) {

        List<Directorio> directorios = directorioRepository.findByPadreId(directorioId);

        List<Archivo> archivos = archivoRepository.findByDirectorioId(directorioId);

        List<DirectorioDTO> dirDTO = directorios.stream()
                .map(d -> {
                    DirectorioDTO dto = new DirectorioDTO();
                    dto.setId(d.getId());
                    dto.setNombre(d.getNombre());
                    return dto;
                })
                .toList();

        List<ArchivoResponseDTO> archivosDTO = archivos.stream()
                .map(this::mapToDTO)
                .toList();

        ExplorerResponseDTO response = new ExplorerResponseDTO();
        response.setDirectorios(dirDTO);
        response.setArchivos(archivosDTO);

        return response;
    }

    // ========== NUEVOS MÉTODOS ==========

    @Override
    @Transactional
    public ArchivoResponseDTO renombrarArchivo(Long archivoId, String nuevoNombre) throws IOException {
        Archivo archivo = archivoRepository.findById(archivoId)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));

        // Validar que no exista otro archivo con el mismo nombre en el mismo directorio
        if (archivo.getDirectorio() != null) {
            archivoRepository.findByDirectorioIdAndNombreOriginalIgnoreCase(
                            archivo.getDirectorio().getId(), nuevoNombre)
                    .ifPresent(a -> {
                        throw new RuntimeException("Ya existe un archivo con ese nombre en este directorio");
                    });
        } else {
            archivoRepository.findByDirectorioIsNullAndNombreOriginalIgnoreCase(nuevoNombre)
                    .ifPresent(a -> {
                        throw new RuntimeException("Ya existe un archivo con ese nombre en la raíz");
                    });
        }

        // Renombrar archivo físico
        Path oldPath = Paths.get(archivo.getRuta());
        Path newPath = oldPath.resolveSibling(nuevoNombre);
        Files.move(oldPath, newPath);

        // Actualizar entidad
        archivo.setNombreOriginal(nuevoNombre);
        archivo.setRuta(newPath.toString());
        archivo.setLastModified(LocalDateTime.now());

        Archivo actualizado = archivoRepository.save(archivo);
        return mapToDTO(actualizado);
    }

    @Override
    @Transactional
    public ArchivoResponseDTO moverArchivo(Long archivoId, Long nuevoDirectorioId) {
        Archivo archivo = archivoRepository.findById(archivoId)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));

        Directorio nuevoDirectorio = null;
        if (nuevoDirectorioId != null) {
            nuevoDirectorio = directorioRepository.findById(nuevoDirectorioId)
                    .orElseThrow(() -> new RuntimeException("Directorio destino no encontrado"));
        }

        archivo.setDirectorio(nuevoDirectorio);
        archivo.setLastModified(LocalDateTime.now());

        Archivo actualizado = archivoRepository.save(archivo);
        return mapToDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminarArchivo(Long archivoId) throws IOException {
        Archivo archivo = archivoRepository.findById(archivoId)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));

        // Eliminar archivo físico
        Path filePath = Paths.get(archivo.getRuta());
        Files.deleteIfExists(filePath);

        // Eliminar de BD (etiquetas se eliminan en cascada)
        archivoRepository.delete(archivo);
    }

    @Override
    public Resource descargarArchivo(Long archivoId) throws IOException {
        Archivo archivo = archivoRepository.findById(archivoId)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));

        Path filePath = Paths.get(archivo.getRuta());
        Resource resource = new org.springframework.core.io.UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("No se puede leer el archivo");
        }
    }

    @Override
    public ArchivoResponseDTO obtenerArchivo(Long archivoId) {
        Archivo archivo = archivoRepository.findById(archivoId)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));
        return mapToDTO(archivo);
    }

    @Override
    public List<ArchivoResponseDTO> listarArchivosRaiz() {
        return archivoRepository.findByDirectorioIsNull()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // Actualiza el método mapToDTO existente
    private ArchivoResponseDTO mapToDTO(Archivo archivo) {
        ArchivoResponseDTO dto = new ArchivoResponseDTO();
        dto.setId(archivo.getId());
        dto.setNombreOriginal(archivo.getNombreOriginal());
        dto.setMimeType(archivo.getMimeType());
        dto.setSize(archivo.getSize());
        dto.setRuta(archivo.getRuta());
        dto.setFechaSubida(archivo.getFechaSubida());
        dto.setLastModified(archivo.getLastModified());

        if (archivo.getDirectorio() != null) {
            dto.setDirectorioId(archivo.getDirectorio().getId());
            dto.setDirectorioNombre(archivo.getDirectorio().getNombre());
        }

        return dto;
    }
}