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

-- changeset jose:3
-- Marca de control para que el DataLoader (datafaker) sepa si ya generó sus datos,
-- sin depender del conteo de filas (que Liquibase ya deja en > 0)
CREATE TABLE seed_control (
    id INT PRIMARY KEY,
    datafaker_seeded BOOLEAN NOT NULL DEFAULT FALSE
);
INSERT INTO seed_control (id, datafaker_seeded) VALUES (1, FALSE);

-- changeset jose:4
-- Si la tabla ya tiene más filas que las 2 sembradas por Liquibase, es porque el DataLoader
-- ya había generado datos en una corrida anterior (antes de existir esta marca): no debe repetirse.
UPDATE seed_control
SET datafaker_seeded = TRUE
WHERE (SELECT COUNT(*) FROM descuentos) > 2;