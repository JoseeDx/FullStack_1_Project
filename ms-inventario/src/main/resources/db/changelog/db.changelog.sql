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