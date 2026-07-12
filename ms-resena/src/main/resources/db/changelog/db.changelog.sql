-- liquibase formatted sql

-- changeset developer:1
CREATE TABLE resenas (
    id_resena BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_producto BIGINT NOT NULL,
    id_cliente BIGINT NOT NULL,
    calificacion INT NOT NULL CHECK (calificacion >= 1 AND calificacion <= 5),
    comentario VARCHAR(500),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- changeset developer:2
-- id_producto hace referencia a ms-producto (1 al 10), id_cliente a cliente-service (1 al 3)
INSERT INTO resenas (id_producto, id_cliente, calificacion, comentario) VALUES
(1, 1, 5, 'Excelente teclado, muy buena respuesta de las teclas.'),
(3, 2, 4, 'Buen mouse, aunque un poco pesado para mi gusto.'),
(5, 3, 5, 'Los auriculares tienen un sonido increible, muy recomendados.');