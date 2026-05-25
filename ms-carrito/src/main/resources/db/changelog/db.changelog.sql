--liquibase formatted sql

--changeset equipo:1
CREATE TABLE carrito_item (
    id_carrito INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_producto INT NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    fecha_agregado DATETIME NOT NULL,
    cantidad INT NOT NULL
);

--changeset equipo:2
INSERT INTO carrito_item (id_cliente, id_producto, precio_unitario, fecha_agregado, cantidad) VALUES
(1, 1, 29990, '2025-01-10 10:00:00', 2),
(1, 2, 49990, '2025-01-10 10:05:00', 1),
(2, 3, 19990, '2025-01-11 14:00:00', 3),
(2, 1, 29990, '2025-01-11 14:10:00', 1),
(3, 2, 49990, '2025-01-12 09:00:00', 2),
(3, 3, 19990, '2025-01-12 09:15:00', 4),
(4, 1, 29990, '2025-01-13 11:00:00', 1),
(4, 4, 39990, '2025-01-13 11:20:00', 2),
(5, 2, 49990, '2025-01-14 16:00:00', 1),
(5, 5, 59990, '2025-01-14 16:30:00', 3);