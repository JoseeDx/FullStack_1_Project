package com.tienda.ms_facturacion.repository;

import com.tienda.ms_facturacion.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    @Query("SELECT f FROM Factura f WHERE f.id_pedido = :id_pedido")
    List<Factura> findByIdPedido(Long id_pedido);

    @Query("SELECT f FROM Factura f WHERE f.rut_cliente = :rut_cliente")
    List<Factura> findByRutCliente(String rut_cliente);

    @Query("SELECT f FROM Factura f WHERE f.estado_factura = :estado_factura")
    List<Factura> findByEstadoFactura(String estado_factura);
}