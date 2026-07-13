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

-- changeset developer:3
-- Marca de control para que el DataLoader (datafaker) sepa si ya generó sus datos,
-- sin depender del conteo de filas (que Liquibase ya deja en > 0)
CREATE TABLE seed_control (
    id INT PRIMARY KEY,
    datafaker_seeded BOOLEAN NOT NULL DEFAULT FALSE
);
INSERT INTO seed_control (id, datafaker_seeded) VALUES (1, FALSE);

-- changeset developer:4
-- Si la tabla ya tiene más filas que las 3 sembradas por Liquibase, es porque el DataLoader
-- ya había generado datos en una corrida anterior (antes de existir esta marca): no debe repetirse.
UPDATE seed_control
SET datafaker_seeded = TRUE
WHERE (SELECT COUNT(*) FROM resenas) > 3;