--liquibase formatted sql

--changeset jose:1
CREATE TABLE cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE
);

--changeset jose:2
INSERT INTO cliente (nombre, correo) VALUES 
('Juan Perez', 'juan.perez@example.com'),
('Maria Lopez', 'maria.lopez@example.com'),
('Carlos Diaz', 'carlos.diaz@example.com');

--changeset jose:3
CREATE TABLE roles (
    id_rol BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL
);

--changeset jose:4
INSERT INTO roles (nombre_rol) VALUES 
('ADMIN'),
('USER'),
('GUEST');

--changeset jose:5

ALTER TABLE cliente ADD COLUMN id_rol BIGINT;

--changeset jose:6

ALTER TABLE cliente 
ADD CONSTRAINT fk_cliente_rol 
FOREIGN KEY (id_rol) REFERENCES roles(id_rol);

--changeset jose:7

UPDATE cliente SET id_rol = 2 WHERE id_rol IS NULL; 