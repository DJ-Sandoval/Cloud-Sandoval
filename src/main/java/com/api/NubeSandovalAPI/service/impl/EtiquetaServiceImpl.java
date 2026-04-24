package com.api.NubeSandovalAPI.service.impl;

import com.api.NubeSandovalAPI.entities.Etiqueta;
import com.api.NubeSandovalAPI.repository.EtiquetaRepository;
import com.api.NubeSandovalAPI.service.interfaces.EtiquetaService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EtiquetaServiceImpl implements EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;

    public EtiquetaServiceImpl(EtiquetaRepository etiquetaRepository) {
        this.etiquetaRepository = etiquetaRepository;
    }

    @Override
    @Transactional
    public List<Etiqueta> obtenerOCrearEtiquetas(List<String> nombres) {

        if (nombres == null || nombres.isEmpty()) {
            return List.of();
        }

        List<Etiqueta> resultado = new ArrayList<>();

        for (String nombreRaw : nombres) {

            String nombre = normalizar(nombreRaw);

            Etiqueta etiqueta = etiquetaRepository
                    .findByNombreIgnoreCase(nombre)
                    .orElseGet(() -> {
                        Etiqueta nueva = new Etiqueta();
                        nueva.setNombre(nombre);
                        return etiquetaRepository.save(nueva);
                    });

            resultado.add(etiqueta);
        }

        return resultado;
    }

    private String normalizar(String nombre) {
        return nombre.trim().toLowerCase();
    }
}
