-- liquibase formatted sql

-- changeset Jose:1
CREATE TABLE envios (
    id_envio INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha_despacho DATETIME
);

-- changeset Jose:2
INSERT INTO envios (id_pedido, direccion, ciudad, estado, fecha_despacho) 
VALUES (1, 'Av. Siempre Viva 742', 'Springfield', 'EN_RUTA', '2026-06-20 10:00:00');

INSERT INTO envios (id_pedido, direccion, ciudad, estado, fecha_despacho)
VALUES (2, 'Calle Falsa 123', 'Santiago', 'PREPARANDO', NULL);

-- changeset Jose:3
-- Marca de control para que el DataLoader (datafaker) sepa si ya generó sus datos,
-- sin depender del conteo de filas (que Liquibase ya deja en > 0)
CREATE TABLE seed_control (
    id INT PRIMARY KEY,
    datafaker_seeded BOOLEAN NOT NULL DEFAULT FALSE
);
INSERT INTO seed_control (id, datafaker_seeded) VALUES (1, FALSE);

-- changeset Jose:4
-- Si la tabla ya tiene más filas que las 2 sembradas por Liquibase, es porque el DataLoader
-- ya había generado datos en una corrida anterior (antes de existir esta marca): no debe repetirse.
UPDATE seed_control
SET datafaker_seeded = TRUE
WHERE (SELECT COUNT(*) FROM envios) > 2;