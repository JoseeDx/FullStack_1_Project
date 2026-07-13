--liquibase formatted sql

--changeset jose:1
CREATE TABLE pedidos (
    id_pedido BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_pedido DATETIME NOT NULL
);

--changeset jose:2

INSERT INTO pedidos (fecha_pedido) VALUES
(NOW()),
('2026-05-14 10:00:00'),
('2026-05-14 11:30:00');

--changeset jose:3
-- Marca de control para que el DataLoader (datafaker) sepa si ya generó sus datos,
-- sin depender del conteo de filas (que Liquibase ya deja en > 0)
CREATE TABLE seed_control (
    id INT PRIMARY KEY,
    datafaker_seeded BOOLEAN NOT NULL DEFAULT FALSE
);
INSERT INTO seed_control (id, datafaker_seeded) VALUES (1, FALSE);

--changeset jose:4
-- Si la tabla ya tiene más filas que las 3 sembradas por Liquibase, es porque el DataLoader
-- ya había generado datos en una corrida anterior (antes de existir esta marca): no debe repetirse.
UPDATE seed_control
SET datafaker_seeded = TRUE
WHERE (SELECT COUNT(*) FROM pedidos) > 3;