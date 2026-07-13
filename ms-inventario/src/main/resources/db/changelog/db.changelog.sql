--liquibase formatted sql

--changeset equipo:1
CREATE TABLE inventario (
    id_inventario BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    stock_actual INT NOT NULL,
    stock_minimo INT NOT NULL,
    stock_maximo INT NOT NULL,
    fecha_actualizacion DATETIME NOT NULL
);

--changeset equipo:2
-- id_producto hace referencia a los productos sembrados en ms-producto (1 al 10)
-- el producto 2 queda a propósito bajo su stock mínimo, para poder probar /bajo-stock
INSERT INTO inventario (id_producto, stock_actual, stock_minimo, stock_maximo, fecha_actualizacion) VALUES
(1, 25, 5, 50, NOW()),
(2, 3, 5, 40, NOW()),
(3, 60, 10, 80, NOW()),
(4, 15, 5, 60, NOW()),
(5, 8, 10, 45, NOW());

--changeset equipo:3
-- Marca de control para que el DataLoader (datafaker) sepa si ya generó sus datos,
-- sin depender del conteo de filas (que Liquibase ya deja en > 0)
CREATE TABLE seed_control (
    id INT PRIMARY KEY,
    datafaker_seeded BOOLEAN NOT NULL DEFAULT FALSE
);
INSERT INTO seed_control (id, datafaker_seeded) VALUES (1, FALSE);

--changeset equipo:4
-- Si la tabla ya tiene más filas que las 5 sembradas por Liquibase, es porque el DataLoader
-- ya había generado datos en una corrida anterior (antes de existir esta marca): no debe repetirse.
UPDATE seed_control
SET datafaker_seeded = TRUE
WHERE (SELECT COUNT(*) FROM inventario) > 5;