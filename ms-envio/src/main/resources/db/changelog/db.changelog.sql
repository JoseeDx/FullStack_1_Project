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