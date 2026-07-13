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

--changeset equipo:2
-- id_pedido hace referencia a los pedidos sembrados en ms-pedido (1, 2, 3)
INSERT INTO factura (id_pedido, rut_cliente, subtotal, iva, total, estado_factura, fecha_factura) VALUES
(1, '11111111-1', 59990, 11398, 71388, 'EMITIDA', '2026-05-14 10:20:00'),
(2, '22222222-2', 69990, 13298, 83288, 'PAGADA', '2026-05-14 12:00:00'),
(3, '12345678-5', 189990, 36098, 226088, 'ANULADA', '2026-05-15 09:30:00');

--changeset equipo:3
-- Marca de control para que el DataLoader (datafaker) sepa si ya generó sus datos,
-- sin depender del conteo de filas (que Liquibase ya deja en > 0)
CREATE TABLE seed_control (
    id INT PRIMARY KEY,
    datafaker_seeded BOOLEAN NOT NULL DEFAULT FALSE
);
INSERT INTO seed_control (id, datafaker_seeded) VALUES (1, FALSE);

--changeset equipo:4
-- Si la tabla ya tiene más filas que las 3 sembradas por Liquibase, es porque el DataLoader
-- ya había generado datos en una corrida anterior (antes de existir esta marca): no debe repetirse.
UPDATE seed_control
SET datafaker_seeded = TRUE
WHERE (SELECT COUNT(*) FROM factura) > 3;