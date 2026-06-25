--liquibase formatted sql

--changeset equipo:1
CREATE TABLE factura (
    id_factura BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pedido BIGINT NOT NULL,
    rut_cliente VARCHAR(12) NOT NULL,
    subtotal INT NOT NULL,
    iva INT NOT NULL,
    total INT NOT NULL,
    estado_factura VARCHAR(50) NOT NULL,
    fecha_factura DATETIME NOT NULL
);