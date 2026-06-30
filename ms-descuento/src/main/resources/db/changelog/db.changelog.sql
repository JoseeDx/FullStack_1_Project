-- liquibase formatted sql

-- changeset jose:1
CREATE TABLE descuentos (
    id_descuento INT AUTO_INCREMENT PRIMARY KEY,
    codigo_cupon VARCHAR(50) NOT NULL UNIQUE,
    porcentaje DOUBLE NOT NULL,
    fecha_expiracion DATETIME NOT NULL,
    activo BOOLEAN NOT NULL
);

-- changeset jose:2
-- Insertamos un par de datos semilla para poder hacer pruebas en Postman más adelante
INSERT INTO descuentos (codigo_cupon, porcentaje, fecha_expiracion, activo) 
VALUES ('INVIERNO20', 20.0, '2026-12-31 23:59:59', true);

INSERT INTO descuentos (codigo_cupon, porcentaje, fecha_expiracion, activo) 
VALUES ('VENCIDO50', 50.0, '2023-01-01 00:00:00', false);