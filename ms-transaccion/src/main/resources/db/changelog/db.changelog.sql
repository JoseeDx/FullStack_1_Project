--liquibase formatted sql

--changeset equipo:1
CREATE TABLE transaccion (
    id_transaccion INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_cliente INT NOT NULL,
    metodo_pago VARCHAR(100) NOT NULL,
    monto_pago DOUBLE NOT NULL,
    estado_pago VARCHAR(50) NOT NULL,
    fecha_transaccion DATETIME NOT NULL
);

--changeset equipo:2
INSERT INTO transaccion (id_pedido, id_cliente, metodo_pago, monto_pago, estado_pago, fecha_transaccion) VALUES
(1, 1, 'Tarjeta de crédito', 59990, 'COMPLETADO', '2025-01-10 10:30:00'),
(2, 2, 'Transferencia bancaria', 139980, 'COMPLETADO', '2025-01-11 14:15:00'),
(3, 1, 'Tarjeta de débito', 49990, 'PENDIENTE', '2025-01-12 09:00:00');

--changeset equipo:3
-- Marca de control para que el DataLoader (datafaker) sepa si ya generó sus datos,
-- sin depender del conteo de filas (que Liquibase ya deja en > 0)
CREATE TABLE seed_control (
    id INT PRIMARY KEY,
    datafaker_seeded BOOLEAN NOT NULL DEFAULT FALSE
);
INSERT INTO seed_control (id, datafaker_seeded) VALUES (1, FALSE);

--changeset equipo:4
-- Si la tabla ya tiene más filas que las 3 sembradas por Liquibase, es porque el DataLoader
-- ya había generado datos en una corrida anterior (antes de existir esta marca): no debe repetirse.
UPDATE seed_control
SET datafaker_seeded = TRUE
WHERE (SELECT COUNT(*) FROM transaccion) > 3;