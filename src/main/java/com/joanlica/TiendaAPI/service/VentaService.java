package com.joanlica.TiendaAPI.service;

import com.joanlica.TiendaAPI.repository.IVentaRepository;
import org.springframework.stereotype.Service;

@Service
public class VentaService implements IVentaService{

    private final IVentaRepository ventaRepository;

    public VentaService(IVentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

}