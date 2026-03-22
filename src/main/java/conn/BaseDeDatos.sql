/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Rodolfo Guerrero, Leslie Reyes
 * Created: 14 mar 2026
 */

CREATE DATABASE IF NOT EXISTS transporte_hirata;

USE transporte_hirata;

CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    id_rol INT,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

CREATE TABLE vehiculos (
    id_vehiculo INT AUTO_INCREMENT PRIMARY KEY,
    id_conductor INT,
    patente VARCHAR(10) UNIQUE NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    anio INT NOT NULL,
    kilometraje_inicial INT NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_conductor) REFERENCES usuarios(id_usuario)
);

CREATE TABLE kilometraje (
    id_kilometraje INT AUTO_INCREMENT PRIMARY KEY,
    id_conductor INT NOT NULL,
    id_vehiculo INT NOT NULL,
    kilometros INT NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_vehiculo) REFERENCES vehiculos(id_vehiculo),
    FOREIGN KEY (id_conductor) REFERENCES usuarios(id_usuario)
);

CREATE TABLE mantenimiento (
    id_mantenimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_vehiculo INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- cuando se crea la alerta o programa la mantención
    fecha_completado DATE, -- cuando se cambia el estado
    tipo_mantenimiento ENUM('Preventivo', 'Correctivo') NOT NULL DEFAULT 'Preventivo',
    origen ENUM('Sistema', 'Manual') NOT NULL DEFAULT 'Sistema',
    descripcion TEXT,
    kilometraje INT,
    estado ENUM('Programado', 'Completado', 'Cancelado') NOT NULL DEFAULT 'Programado',
    id_usuario_mantenimiento INT, -- quien lleva a cabo el mantenimiento
    FOREIGN KEY (id_vehiculo) REFERENCES vehiculos(id_vehiculo),
    FOREIGN KEY (id_usuario_mantenimiento) REFERENCES usuarios(id_usuario)
);


-- ROLES DISPONIBLES

INSERT INTO roles (nombre) VALUES 
    ('Administrador de Flota'),
    ('Administrador de Mantenimiento'),
    ('Conductor');

-- INSERT EJEMPLOS

-- pass: 1234
INSERT INTO usuarios (nombre, email, password, id_rol) VALUES 
('Carlos Mendoza',  'carlos.mendoza@hirata.cl',  '$2a$10$7QJ6YkqJv1Yb8V7zWlQh1uO6p9F0yQn3YzYFzZ3JZlJw5z1yKpG6C', 1),
('Roberto Soto',    'roberto.soto@hirata.cl',    '$2a$10$7QJ6YkqJv1Yb8V7zWlQh1uO6p9F0yQn3YzYFzZ3JZlJw5z1yKpG6C', 2),
('Miguel Fuentes',  'miguel.fuentes@hirata.cl',  '$2a$10$7QJ6YkqJv1Yb8V7zWlQh1uO6p9F0yQn3YzYFzZ3JZlJw5z1yKpG6C', 3),
('Jorge Castillo', 'jorge.castillo@hirata.cl', '$2a$10$7QJ6YkqJv1Yb8V7zWlQh1uO6p9F0yQn3YzYFzZ3JZlJw5z1yKpG6C', 3);


INSERT INTO vehiculos (id_conductor, patente, marca, modelo, anio, kilometraje_inicial) VALUES
(3, 'BJKP45', 'Mercedes-Benz', 'Actros 2651', 2019, 98000),
(4, 'FHRM21', 'Volvo', 'FH 460', 2021, 45000);
