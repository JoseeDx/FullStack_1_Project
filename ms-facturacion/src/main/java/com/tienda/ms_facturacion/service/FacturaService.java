package com.tienda.ms_facturacion.service;


import com.tienda.ms_facturacion.client.PedidoClient;
import com.tienda.ms_facturacion.client.PedidoResponse;
import com.tienda.ms_facturacion.exception.BadRequestException;
import com.tienda.ms_facturacion.exception.ResourceNotFoundException;
import com.tienda.ms_facturacion.model.Factura;
import com.tienda.ms_facturacion.repository.FacturaRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class FacturaService {

    private static Logger log = LoggerFactory.getLogger(FacturaService.class);

    private FacturaRepository facturaRepository;
    private PedidoClient pedidoClient;

    public FacturaService(FacturaRepository facturaRepository, PedidoClient pedidoClient) {
        this.facturaRepository = facturaRepository;
        this.pedidoClient = pedidoClient;
    }

    public List<Factura> listar() {
        log.info("Listando todas las facturas");
        List<Factura> facturas = facturaRepository.findAll();
        log.info("Total facturas encontradas: {}", facturas.size());
        return facturas;
    }

    public Factura obtenerPorId(Long id) {
        log.info("Buscando factura por id={}", id);
        Factura factura = facturaRepository.findById(id).orElse(null);
        if (factura == null) {
            log.warn("Factura no encontrada id={}", id);
            throw new ResourceNotFoundException("Factura no existe id: " + id);
        }
        log.info("Factura encontrada id={}", id);
        return factura;
    }

    public List<Factura> obtenerPorIdPedido(Long id_pedido) {
        log.info("Buscando facturas por id pedido={}", id_pedido);
        List<Factura> facturas = facturaRepository.findByIdPedido(id_pedido);
        log.info("Facturas encontradas para pedido={}: {}", id_pedido, facturas.size());
        return facturas;
    }

    public List<Factura> obtenerPorRut(String rut_cliente) {
        log.info("Buscando facturas por rut={}", rut_cliente);
        if (!validarRut(rut_cliente))
            throw new BadRequestException("RUT inválido: " + rut_cliente);
        List<Factura> facturas = facturaRepository.findByRutCliente(rut_cliente);
        log.info("Facturas encontradas para rut={}: {}", rut_cliente, facturas.size());
        return facturas;
    }

    public List<Factura> obtenerPorEstado(String estado_factura) {
        log.info("Buscando facturas por estado={}", estado_factura);
        List<Factura> facturas = facturaRepository.findByEstadoFactura(estado_factura);
        log.info("Facturas encontradas por estado={}: {}", estado_factura, facturas.size());
        return facturas;
    }

    public Factura guardar(Factura factura) {
        log.info("Iniciando guardar factura idPedido={}, rut={}, subtotal={}",
                factura.getId_pedido(), factura.getRut_cliente(), factura.getSubtotal());

        if (!validarRut(factura.getRut_cliente()))
            throw new BadRequestException("RUT inválido: " + factura.getRut_cliente());
        if (factura.getSubtotal() <= 0)
            throw new BadRequestException("El subtotal debe ser mayor a 0");

        try {
            PedidoResponse pedido = pedidoClient.obtenerPedido(factura.getId_pedido());
            log.info("Pedido validado: {}", pedido.getId_pedido());

            factura.setIva(calcularIVA(factura.getSubtotal()));
            factura.setTotal(calcularTotal(factura.getSubtotal(), factura.getIva()));
            factura.setFecha_factura(LocalDateTime.now());
            factura.setEstado_factura("EMITIDA");

            Factura guardada = facturaRepository.save(factura);
            log.info("Factura guardada exitosamente id={}", guardada.getId_factura());
            return guardada;
        } catch (Exception e) {
            log.error("Error al validar pedido o guardar factura: {}", e.getMessage(), e);
            throw new BadRequestException("No se pudo validar el pedido, intente más tarde");
        }
    }

    public Factura actualizar(Long id, Factura factura) {
        log.info("Iniciando actualizar factura id={}", id);

        Factura existente = facturaRepository.findById(id).orElse(null);
        if (existente == null) {
            log.warn("Factura no encontrada para actualizar id={}", id);
            throw new ResourceNotFoundException("Factura no existe id: " + id);
        }

        if (!validarRut(factura.getRut_cliente()))
            throw new BadRequestException("RUT inválido: " + factura.getRut_cliente());
        if (factura.getSubtotal() <= 0)
            throw new BadRequestException("El subtotal debe ser mayor a 0");

        try {
            existente.setId_pedido(factura.getId_pedido());
            existente.setRut_cliente(factura.getRut_cliente());
            existente.setSubtotal(factura.getSubtotal());
            existente.setIva(calcularIVA(factura.getSubtotal()));
            existente.setTotal(calcularTotal(factura.getSubtotal(), existente.getIva()));
            existente.setEstado_factura(factura.getEstado_factura());
            existente.setFecha_factura(LocalDateTime.now());

            Factura actualizada = facturaRepository.save(existente);
            log.info("Factura actualizada exitosamente id={}", actualizada.getId_factura());
            return actualizada;
        } catch (Exception e) {
            log.error("Error crítico al actualizar factura id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public void eliminar(Long id) {
        log.info("Iniciando eliminación de factura id={}", id);

        if (!facturaRepository.existsById(id)) {
            log.warn("Factura no existe para eliminar id={}", id);
            throw new ResourceNotFoundException("Factura no existe id: " + id);
        }

        try {
            facturaRepository.deleteById(id);
            log.info("Factura eliminada exitosamente id={}", id);
        } catch (Exception e) {
            log.error("Error crítico al eliminar factura id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // Métodos de cálculo
    public int calcularIVA(int subtotal) {
        if (subtotal < 0)
            throw new IllegalArgumentException("El subtotal no puede ser negativo");
        return subtotal * 19 / 100;
    }

    public int calcularTotal(int subtotal, int iva) {
        if (subtotal < 0 || iva < 0)
            throw new IllegalArgumentException("Los valores no pueden ser negativos");
        return subtotal + iva;
    }

    public boolean validarRut(String rut) {
        if (rut == null || rut.isBlank()) return false;
        rut = rut.replace(".", "").replace("-", "").toUpperCase();
        if (rut.length() < 2) return false;
        String cuerpo = rut.substring(0, rut.length() - 1);
        char dvIngresado = rut.charAt(rut.length() - 1);
        try {
            int suma = 0;
            int multiplo = 2;
            for (int i = cuerpo.length() - 1; i >= 0; i--) {
                suma += Character.getNumericValue(cuerpo.charAt(i)) * multiplo;
                multiplo = multiplo == 7 ? 2 : multiplo + 1;
            }
            int dvEsperado = 11 - (suma % 11);
            char dv = dvEsperado == 11 ? '0' : dvEsperado == 10 ? 'K' : (char) ('0' + dvEsperado);
            return dv == dvIngresado;
        } catch (Exception e) {
            return false;
        }
    }
}