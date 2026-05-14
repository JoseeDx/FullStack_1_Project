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