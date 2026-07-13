--liquibase formatted sql

--changeset equipo:1
CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(100) NOT NULL
);

--changeset equipo:2
CREATE TABLE producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre_producto VARCHAR(150) NOT NULL,
    descripcion_producto VARCHAR(500),
    precio_producto DOUBLE NOT NULL,
    id_categoria INT NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

--changeset equipo:3
INSERT INTO categoria (nombre_categoria) VALUES
('Teclados'),
('Mouses'),
('Auriculares'),
('Monitores');

--changeset equipo:4
INSERT INTO producto (nombre_producto, descripcion_producto, precio_producto, id_categoria) VALUES
('Teclado Mecánico HyperX Alloy', 'Teclado mecánico con switches Red', 59990, 1),
('Teclado Logitech G Pro X', 'Teclado compacto para gaming', 89990, 1),
('Mouse Logitech G502', 'Mouse gaming con sensor HERO 25K', 49990, 2),
('Mouse Razer DeathAdder V3', 'Mouse ergonómico ultra liviano', 69990, 2),
('Auriculares HyperX Cloud II', 'Auriculares con sonido 7.1 virtual', 79990, 3),
('Auriculares SteelSeries Arctis 7', 'Auriculares inalámbricos gaming', 99990, 3),
('Monitor LG 24 144Hz', 'Monitor Full HD 144Hz para gaming', 189990, 4),
('Monitor Samsung Odyssey G5', 'Monitor curvo QHD 165Hz', 299990, 4),
('Teclado Razer BlackWidow V3', 'Teclado mecánico con retroiluminación RGB', 74990, 1),
('Mouse SteelSeries Rival 600', 'Mouse gaming con doble sensor', 59990, 2);

--changeset equipo:5
ALTER TABLE categoria ADD COLUMN activo BOOLEAN NOT NULL DEFAULT TRUE;

--changeset equipo:6
ALTER TABLE producto ADD COLUMN activo BOOLEAN NOT NULL DEFAULT TRUE;

--changeset equipo:7
-- Marca de control para que el DataLoader (datafaker) sepa si ya generó sus datos,
-- sin depender del conteo de filas (que Liquibase ya deja en > 0)
CREATE TABLE seed_control (
    id INT PRIMARY KEY,
    datafaker_seeded BOOLEAN NOT NULL DEFAULT FALSE
);
INSERT INTO seed_control (id, datafaker_seeded) VALUES (1, FALSE);

--changeset equipo:8
-- Si la tabla ya tiene más filas que las 10 sembradas por Liquibase, es porque el DataLoader
-- ya había generado datos en una corrida anterior (antes de existir esta marca): no debe repetirse.
UPDATE seed_control
SET datafaker_seeded = TRUE
WHERE (SELECT COUNT(*) FROM producto) > 10;