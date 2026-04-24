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
import com.api.NubeSandovalAPI.utils.FileStorageUtil;
import com.api.NubeSandovalAPI.utils.HashUtil;
import com.api.NubeSandovalAPI.utils.ThumbnailUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
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

    public ArchivoServiceImpl(
            ArchivoRepository archivoRepository,
            ArchivoEtiquetaRepository archivoEtiquetaRepository,
            EtiquetaService etiquetaService,
            DirectorioRepository directorioRepository,
            HashUtil hashUtil,
            FileStorageUtil storageUtil,
            ThumbnailUtil thumbnailUtil
    ) {

        this.archivoRepository = archivoRepository;
        this.archivoEtiquetaRepository = archivoEtiquetaRepository;
        this.etiquetaService = etiquetaService;
        this.hashUtil = hashUtil;
        this.storageUtil = storageUtil;
        this.directorioRepository = directorioRepository;
        this.thumbnailUtil = thumbnailUtil;
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

    private ArchivoResponseDTO mapToDTO(Archivo archivo) {
        ArchivoResponseDTO dto = new ArchivoResponseDTO();
        dto.setId(archivo.getId());
        dto.setNombreOriginal(archivo.getNombreOriginal());
        dto.setMimeType(archivo.getMimeType());
        dto.setSize(archivo.getSize());
        dto.setRuta(archivo.getRuta());
        return dto;
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
    public Flux<ExplorerResponseDTO> explorar(Long directorioId) {

        return Flux.defer(() -> {

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

            return Flux.just(response);

        }).subscribeOn(Schedulers.boundedElastic()); // 🔥 CLAVE
    }
}