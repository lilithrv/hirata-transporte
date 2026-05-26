/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  leslie
 * Created: 20 may 2026
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
    direccion_origen VARCHAR(200) NOT NULL,
    direccion_termino VARCHAR(200) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_vehiculo) REFERENCES vehiculos(id_vehiculo)ON DELETE CASCADE,
    FOREIGN KEY (id_conductor) REFERENCES usuarios(id_usuario)
);

CREATE TABLE mantenimiento (
    id_mantenimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_vehiculo INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- cuando se crea la alerta o programa la mantención
    fecha_completado DATETIME, -- cuando se cambia el estado
    tipo_mantenimiento ENUM('Preventivo', 'Correctivo') NOT NULL DEFAULT 'Preventivo',
    origen ENUM('Sistema', 'Manual') NOT NULL DEFAULT 'Sistema',
    descripcion TEXT,
    kilometraje INT,
    estado ENUM('Programado', 'Completado', 'Cancelado') NOT NULL DEFAULT 'Programado',
    id_usuario_mantenimiento INT, -- quien lleva a cabo el mantenimiento
    FOREIGN KEY (id_vehiculo) REFERENCES vehiculos(id_vehiculo)ON DELETE CASCADE,
    FOREIGN KEY (id_usuario_mantenimiento) REFERENCES usuarios(id_usuario)
);

-- ROLES DISPONIBLES EN AMBAS ETAPAS
INSERT INTO roles (nombre) VALUES 
    ('Administrador de Flota'),
    ('Administrador de Mantenimiento'),
    ('Conductor'),
    ('Técnico de Mantenimiento'),
    ('Técnico de IT'),
    ('Administrador de Mantenimiento Equipos'),
    ('Administrador de Inventario');;
    
-- ────────────────────────────────────────────────────────────
-- USUARIOS (5 admin flota, 5 admin mant, 90 conductores)
-- pass: 1234
-- ────────────────────────────────────────────────────────────

INSERT INTO usuarios (nombre, email, password, id_rol) VALUES
('Carlos Mendoza', 'carlos.mendoza@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 1),
('Roberto Soto', 'roberto.soto@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 2),
('Miguel Fuentes', 'miguel.fuentes@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Jorge Castillo', 'jorge.castillo@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Enrique Vásquez', 'enrique.vasquez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 1),
('Pedro Vera', 'pedro.vera@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 1),
('Andrés Araya', 'andres.araya@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 1),
('Fernando Ramírez', 'fernando.ramirez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 1),
('Gabriel Torres', 'gabriel.torres@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 2),
('Ramón Bustos', 'ramon.bustos@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 2),
('Luis Díaz', 'luis.diaz@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 2),
('Manuel Valenzuela', 'manuel.valenzuela@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 2),
('Cristian Lagos', 'cristian.lagos@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Arturo Díaz', 'arturo.diaz@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Ernesto Riquelme', 'ernesto.riquelme@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Javier Farías', 'javier.farias@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Cristian Ojeda', 'cristian.ojeda@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Ramón Vera', 'ramon.vera@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Salvador González', 'salvador.gonzalez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Renato Vargas', 'renato.vargas@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Ignacio Bustos', 'ignacio.bustos@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Gonzalo Vera', 'gonzalo.vera@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Eduardo Valenzuela', 'eduardo.valenzuela@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Renato Cáceres', 'renato.caceres@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('José Torres', 'jose.torres@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Iván Flores', 'ivan.flores@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Héctor Tapia', 'hector.tapia@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Arturo Reyes', 'arturo.reyes@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Salvador Soto', 'salvador.soto@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Cristóbal Villanueva', 'cristobal.villanueva@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Rubén Castro', 'ruben.castro@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Maximiliano Ibáñez', 'maximiliano.ibanez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Manuel Palma', 'manuel.palma@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Rodrigo Pinto', 'rodrigo.pinto@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Guillermo Ríos', 'guillermo.rios@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Javier Martínez', 'javier.martinez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Luis Espinoza', 'luis.espinoza@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Álvaro Palma', 'alvaro.palma@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Manuel Espinoza', 'manuel.espinoza@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Gerardo Flores', 'gerardo.flores@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Iván Vera', 'ivan.vera@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Víctor Pinto', 'victor.pinto@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Sergio Zamora', 'sergio.zamora@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Héctor Figueroa', 'hector.figueroa@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Esteban Fernández', 'esteban.fernandez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Ignacio Sepúlveda', 'ignacio.sepulveda@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Arturo Castillo', 'arturo.castillo@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Rubén Bravo', 'ruben.bravo@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Sergio Cornejo', 'sergio.cornejo@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Iván Fernández', 'ivan.fernandez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Maximiliano Araya', 'maximiliano.araya@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Gabriel Urrutia', 'gabriel.urrutia@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Rodrigo Silva', 'rodrigo.silva@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Cristian Pérez', 'cristian.perez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Salvador Pizarro', 'salvador.pizarro@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Nicolás Fernández', 'nicolas.fernandez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Francisco Valenzuela', 'francisco.valenzuela@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Bruno Pizarro', 'bruno.pizarro@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Rodrigo Chávez', 'rodrigo.chavez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Nicolás Villanueva', 'nicolas.villanueva@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Eduardo Reyes', 'eduardo.reyes@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Fernando Bravo', 'fernando.bravo@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Matías Reyes', 'matias.reyes@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Matías Bustos', 'matias.bustos@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Leonardo Leiva', 'leonardo.leiva@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Raúl Araya', 'raul.araya@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Fernando Pereira', 'fernando.pereira@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Hugo Torres', 'hugo.torres@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Renato Contreras', 'renato.contreras@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Gerardo Vásquez', 'gerardo.vasquez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Eduardo Vargas', 'eduardo.vargas@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Emilio Bustos', 'emilio.bustos@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Arturo Martínez', 'arturo.martinez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Iván Ibáñez', 'ivan.ibanez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Arturo Cornejo', 'arturo.cornejo@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Mario Carrasco', 'mario.carrasco@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Ernesto Muñoz', 'ernesto.munoz@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Gabriel Vásquez', 'gabriel.vasquez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Gabriel Fernández', 'gabriel.fernandez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Álvaro Cáceres', 'alvaro.caceres@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Ricardo Palma', 'ricardo.palma@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Mauricio Vargas', 'mauricio.vargas@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Víctor González', 'victor.gonzalez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Cristóbal Reyes', 'cristobal.reyes@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Oscar Álvarez', 'oscar.alvarez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Oscar Ramírez', 'oscar.ramirez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Gerardo Garrido', 'gerardo.garrido@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Rodrigo Lagos', 'rodrigo.lagos@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Arturo Riquelme', 'arturo.riquelme@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Eduardo Zamora', 'eduardo.zamora@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Rubén Molina', 'ruben.molina@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Bruno González', 'bruno.gonzalez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Arturo Urrutia', 'arturo.urrutia@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Hugo Rojas', 'hugo.rojas@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Ricardo Pinto', 'ricardo.pinto@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Marco Moya', 'marco.moya@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Andrés Silva', 'andres.silva@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Andrés Morales', 'andres.morales@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Manuel Montoya', 'manuel.montoya@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3),
('Cesar Martínez', 'cesar.martinez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 3);

-- ────────────────────────────────────────────────────────────
-- VEHÍCULOS (120 en total: 78 con conductor, 40 sin conductor)
-- ────────────────────────────────────────────────────────────
INSERT INTO vehiculos (id_conductor, patente, marca, modelo, anio, kilometraje_inicial) VALUES
(3, 'BJKP45', 'MERCEDES BENZ', 'ACTROS 3348', 2019, 98000),
(89, 'ZYXF42', 'MERCEDES BENZ', 'ACTROS 3348', 2020, 180955),
(18, 'FBKY72', 'MAN', 'TGS 26.440', 2020, 246569),
(84, 'DHZR55', 'IVECO', 'S-WAY 570', 2022, 183505),
(95, 'MXDZ12', 'MERCEDES BENZ', 'ACTROS 3348', 2020, 23144),
(31, 'SGDX25', 'MERCEDES BENZ', 'ATEGO 1726', 2021, 165984),
(57, 'SPJG55', 'KENWORTH', 'T680', 2021, 31491),
(98, 'DSLY99', 'SCANIA', 'R 450', 2021, 79629),
(22, 'CGHY88', 'DAF', 'XF 530', 2021, 172833),
(76, 'LPHZ85', 'MAN', 'TGX 18.500', 2021, 84707),
(62, 'YCXH88', 'FREIGHTLINER', 'M2 106', 2020, 224198),
(20, 'RTFS77', 'VOLVO', 'FH 500', 2020, 81394),
(86, 'CLHB76', 'SCANIA', 'R 500', 2020, 247328),
(87, 'PLBC72', 'SCANIA', 'R 500', 2020, 52357),
(37, 'RDDH90', 'IVECO', 'S-WAY 570', 2022, 19444),
(96, 'SJPX97', 'MAN', 'TGX 18.500', 2021, 65070),
(32, 'ZWXB73', 'SCANIA', 'R 450', 2021, 236651),
(23, 'HVDX82', 'MAN', 'TGX 26.460', 2022, 220301),
(59, 'WXGS14', 'DAF', 'XF 530', 2021, 101660),
(35, 'JMSX10', 'MERCEDES BENZ', 'ATEGO 1726', 2021, 212722),
(66, 'FZHD15', 'VOLVO', 'FH 460', 2021, 239910),
(33, 'KPGT09', 'MAN', 'TGX 26.460', 2022, 168914),
(42, 'JCSJ38', 'IVECO', 'S-WAY 570', 2022, 190055),
(75, 'SJTP16', 'SCANIA', 'G 410', 2019, 142939),
(36, 'RHPB24', 'FREIGHTLINER', 'M2 106', 2020, 206118),
(85, 'LLGL12', 'KENWORTH', 'T800', 2020, 169032),
(93, 'MSJW14', 'FREIGHTLINER', 'M2 106', 2020, 32228),
(78, 'DBZR74', 'SCANIA', 'R 450', 2021, 221435),
(24, 'TKKD68', 'FREIGHTLINER', 'CASCADIA 126', 2021, 160908),
(54, 'GWTL11', 'VOLVO', 'FH 460', 2021, 214058),
(61, 'SZRZ19', 'DAF', 'XF 530', 2021, 44954),
(65, 'YVMG14', 'IVECO', 'STRALIS 480', 2019, 121867),
(19, 'HCHH35', 'IVECO', 'STRALIS 480', 2019, 137034),
(25, 'KDGK34', 'VOLVO', 'FH 500', 2020, 49539),
(39, 'KVYB19', 'MERCEDES BENZ', 'ACTROS 3348', 2020, 64220),
(71, 'YHKX85', 'MAN', 'TGS 26.440', 2020, 135037),
(91, 'MHMK67', 'IVECO', 'S-WAY 570', 2022, 219680),
(43, 'YZPY10', 'VOLVO', 'FM 380', 2019, 230333),
(51, 'DLBK15', 'MERCEDES BENZ', 'ATEGO 1726', 2021, 178492),
(49, 'HZLW18', 'SCANIA', 'S 500', 2022, 209551),
(77, 'SVSV91', 'MERCEDES BENZ', 'ACTROS 3348', 2020, 101740),
(47, 'GLDZ42', 'MERCEDES BENZ', 'ACTROS 2651', 2019, 172879),
(26, 'VFHP95', 'MAN', 'TGX 18.500', 2021, 53984),
(88, 'CTRC78', 'MAN', 'TGS 26.440', 2020, 113980),
(55, 'WPBP60', 'KENWORTH', 'T680', 2021, 221164),
(30, 'XWDZ68', 'FREIGHTLINER', 'CASCADIA 126', 2021, 99451),
(92, 'TZHW54', 'VOLVO', 'FH 500', 2020, 124160),
(45, 'JGYH17', 'MAN', 'TGX 26.460', 2022, 226461),
(63, 'JMJM14', 'KENWORTH', 'T800', 2020, 169994),
(94, 'PMFG18', 'SCANIA', 'S 500', 2022, 83310),
(15, 'SMWC13', 'SCANIA', 'R 450', 2021, 191039),
(68, 'LPLV17', 'SCANIA', 'G 410', 2019, 184053),
(100, 'GTHV12', 'SCANIA', 'R 500', 2020, 90379),
(16, 'HRVR13', 'VOLVO', 'FMX 440', 2022, 66723),
(53, 'FBPH18', 'SCANIA', 'G 410', 2019, 69633),
(58, 'SVCB24', 'SCANIA', 'G 410', 2019, 192823),
(17, 'GXDC15', 'SCANIA', 'S 500', 2022, 58329),
(13, 'DPHZ17', 'FREIGHTLINER', 'M2 106', 2020, 24053),
(72, 'VKYH54', 'KENWORTH', 'T680', 2021, 166271),
(74, 'RVZF26', 'KENWORTH', 'T800', 2020, 172798),
(27, 'PDKT40', 'VOLVO', 'FM 380', 2019, 72879),
(48, 'TLTY19', 'SCANIA', 'G 410', 2019, 195324),
(50, 'LPSY80', 'FREIGHTLINER', 'M2 106', 2020, 220739),
(14, 'RWYL14', 'VOLVO', 'FM 380', 2019, 217782),
(56, 'LRXT10', 'MAN', 'TGS 26.440', 2020, 81367),
(21, 'ZSHB74', 'MERCEDES BENZ', 'ATEGO 1726', 2021, 70011),
(41, 'FRSM15', 'FREIGHTLINER', 'CASCADIA 126', 2021, 125927),
(44, 'SXJT16', 'MAN', 'TGX 26.460', 2022, 97374),
(28, 'CGDV10', 'IVECO', 'S-WAY 570', 2022, 19619),
(70, 'KFJV10', 'FREIGHTLINER', 'M2 106', 2020, 237899),
(79, 'YLTD96', 'VOLVO', 'FMX 440', 2022, 80007),
(69, 'HBKG81', 'SCANIA', 'R 450', 2021, 196065),
(60, 'MTMH89', 'VOLVO', 'FH 460', 2021, 179793),
(64, 'KYMX82', 'SCANIA', 'R 450', 2021, 129743),
(52, 'WHHR17', 'MAN', 'TGX 26.460', 2022, 99096),
(38, 'JPPL13', 'VOLVO', 'FH 500', 2020, 127059),
(82, 'GVBV16', 'SCANIA', 'R 450', 2021, 159893),
(40, 'MBMR88', 'MAN', 'TGS 26.440', 2020, 183982),
(67, 'BDXZ14', 'DAF', 'XF 530', 2021, 36138),
(NULL, 'SXXX15', 'SCANIA', 'S 500', 2022, 173383),
(NULL, 'XBCR13', 'FREIGHTLINER', 'CASCADIA 126', 2021, 198407),
(NULL, 'VTCF20', 'MERCEDES BENZ', 'ATEGO 1726', 2021, 123763),
(NULL, 'HKDP57', 'SCANIA', 'S 500', 2022, 218704),
(NULL, 'RKSM52', 'VOLVO', 'FM 380', 2019, 55893),
(NULL, 'GBWY12', 'FREIGHTLINER', 'CASCADIA 126', 2021, 76578),
(NULL, 'ZHZT14', 'MERCEDES BENZ', 'ATEGO 1726', 2021, 125796),
(NULL, 'DKJH10', 'VOLVO', 'FMX 440', 2022, 110845),
(NULL, 'BXVZ17', 'FREIGHTLINER', 'M2 106', 2020, 113758),
(NULL, 'RJSR67', 'SCANIA', 'G 410', 2019, 213355),
(NULL, 'RYSH45', 'FREIGHTLINER', 'M2 106', 2020, 188482),
(NULL, 'JKFV15', 'VOLVO', 'FH 500', 2020, 33627),
(NULL, 'WGWM15', 'IVECO', 'STRALIS 480', 2019, 141542),
(NULL, 'DZXT99', 'VOLVO', 'FH 460', 2021, 132770),
(NULL, 'WBPL66', 'IVECO', 'S-WAY 570', 2022, 105885),
(NULL, 'DGBX19', 'KENWORTH', 'T800', 2020, 109883),
(NULL, 'SLTS10', 'SCANIA', 'R 450', 2021, 179778),
(NULL, 'DGPK18', 'MERCEDES-BENZ', 'ACTROS 3348', 2020, 89787),
(NULL, 'XSWR92', 'MAN', 'TGX 26.460', 2022, 163967),
(NULL, 'LGJZJ5', 'MAN', 'TGX 18.500', 2021, 66955),
(NULL, 'FDXL19', 'MERCEDES-BENZ', 'ACTROS 3348', 2020, 114369),
(NULL, 'GDPC13', 'MERCEDES-BENZ', 'ACTROS 2651', 2019, 23258),
(NULL, 'JDYB81', 'KENWORTH', 'T800', 2020, 66610),
(NULL, 'WDXD13', 'VOLVO', 'FH 500', 2020, 151614),
(NULL, 'FBVM74', 'KENWORTH', 'T800', 2020, 94874),
(NULL, 'LGRT96', 'MAN', 'TGX 18.500', 2021, 144542),
(NULL, 'CMKT49', 'SCANIA', 'G 410', 2019, 130073),
(NULL, 'JDDR57', 'MERCEDES-BENZ', 'ATEGO 1726', 2021, 245737),
(NULL, 'LRCT18', 'MAN', 'TGS 26.440', 2020, 166602),
(NULL, 'XKWK15', 'VOLVO', 'FMX 440', 2022, 122740),
(NULL, 'KDYC11', 'MAN', 'TGX 26.460', 2022, 91926),
(NULL, 'WPRP11', 'MAN', 'TGX 26.460', 2022, 35313),
(NULL, 'XTZR19', 'MAN', 'TGX 26.460', 2022, 102693),
(NULL, 'SGDX37', 'IVECO', 'STRALIS 480', 2019, 65768),
(NULL, 'XZWR10', 'KENWORTH', 'T800', 2020, 69286),
(NULL, 'BHVY12', 'KENWORTH', 'T680', 2021, 182446),
(NULL, 'CVZC16', 'VOLVO', 'FH 500', 2020, 158457),
(NULL, 'DHJY10', 'MAN', 'TGX 26.460', 2022, 128920),
(NULL, 'SHSM42', 'VOLVO', 'FH 500', 2020, 51264),
(NULL, 'LHSM10', 'IVECO', 'STRALIS 480', 2019, 39836);


-- ────────────────────────────────────────────────────────────
-- KILOMETRAJE (para los 20 vehículos en mantenimiento)
-- ────────────────────────────────────────────────────────────
INSERT INTO kilometraje (id_conductor, id_vehiculo, kilometros, fecha_registro, direccion_origen, direccion_termino) VALUES
(3, 1, 99000, DATE_SUB(NOW(), INTERVAL 20 DAY), 'Av. Lonquén 10500, San Bernardo', 'Ruta 5 Sur Km 400, Los Ángeles'),
(37, 16, 183847, DATE_SUB(NOW(), INTERVAL 80 DAY), 'Ruta 57 Km 10, Colina', 'Av. Argentina 1600, Valparaíso'),
(37, 16, 187218, DATE_SUB(NOW(), INTERVAL 92 DAY), 'Av. Vicuña Mackenna 7200, La Florida', 'Av. Argentina 1600, Valparaíso'),
(37, 16, 191984, DATE_SUB(NOW(), INTERVAL 69 DAY), 'Ruta 5 Norte Km 15, Lampa', 'Av. Circunvalación 3400, Chillán'),
(37, 16, 196980, DATE_SUB(NOW(), INTERVAL 40 DAY), 'Av. Lonquén 10500, San Bernardo', 'Ruta 5 Sur Km 400, Los Ángeles'),
(37, 16, 199730, DATE_SUB(NOW(), INTERVAL 25 DAY), 'Av. General Velásquez 8900, Maipú', 'Av. OHiggins 850, Rancagua'),
(14, 65, 47459, DATE_SUB(NOW(), INTERVAL 105 DAY), 'Av. El Salto 2300, Huechuraba', 'Parque Industrial Lampa, Lampa'),
(14, 65, 49450, DATE_SUB(NOW(), INTERVAL 80 DAY), 'Ruta 5 Norte Km 15, Lampa', 'Puerto de Valparaíso, Valparaíso'),
(14, 65, 53846, DATE_SUB(NOW(), INTERVAL 69 DAY), 'Ruta 68 Km 12, Pudahuel', 'Parque Industrial Curicó, Curicó'),
(14, 65, 60264, DATE_SUB(NOW(), INTERVAL 38 DAY), 'Camino Melipilla Km 5, Padre Hurtado', 'Zona Industrial Graneros, Graneros'),
(14, 65, 66498, DATE_SUB(NOW(), INTERVAL 16 DAY), 'Av. El Salto 2300, Huechuraba', 'Av. Los Carrera 550, Osorno'),
(13, 59, 72601, DATE_SUB(NOW(), INTERVAL 65 DAY), 'Av. Lonquén 10500, San Bernardo', 'Av. El Belloto 2800, Quilpué'),
(13, 59, 75953, DATE_SUB(NOW(), INTERVAL 92 DAY), 'Parque Industrial Enea, Pudahuel', 'Terminal Logístico Concepción, Concepción'),
(13, 59, 82546, DATE_SUB(NOW(), INTERVAL 30 DAY), 'Puerto Terrestre Los Andes, Los Andes', 'Av. Los Carrera 550, Osorno'),
(13, 59, 89726, DATE_SUB(NOW(), INTERVAL 36 DAY), 'Av. Américo Vespucio 1234, Pudahuel', 'Puerto de San Antonio, San Antonio'),
(13, 59, 93464, DATE_SUB(NOW(), INTERVAL 19 DAY), 'Av. Vicuña Mackenna 7200, La Florida', 'Ruta 5 Sur Km 400, Los Ángeles'),
(62, 12, 56135, DATE_SUB(NOW(), INTERVAL 60 DAY), 'Av. El Salto 2300, Huechuraba', 'Ruta 5 Sur Km 680, Temuco'),
(62, 12, 62821, DATE_SUB(NOW(), INTERVAL 40 DAY), 'Av. General Velásquez 8900, Maipú', 'Ruta 5 Sur Km 680, Temuco'),
(62, 12, 68666, DATE_SUB(NOW(), INTERVAL 48 DAY), 'Av. La Serena 1200, Cerrillos', 'Puerto de Valparaíso, Valparaíso'),
(62, 12, 73882, DATE_SUB(NOW(), INTERVAL 40 DAY), 'Camino Lo Echevers 1500, Quilicura', 'Ruta 5 Sur Km 400, Los Ángeles'),
(62, 12, 77934, DATE_SUB(NOW(), INTERVAL 20 DAY), 'Zona Industrial Noviciado, Pudahuel', 'Centro de Distribución Nos, San Bernardo'),
(20, 13, 226105, DATE_SUB(NOW(), INTERVAL 60 DAY), 'Av. Lonquén 10500, San Bernardo', 'Av. Argentina 1600, Valparaíso'),
(20, 13, 233027, DATE_SUB(NOW(), INTERVAL 92 DAY), 'Acceso Sur Bodega Central, Lo Espejo', 'Centro de Distribución Nos, San Bernardo'),
(20, 13, 238148, DATE_SUB(NOW(), INTERVAL 69 DAY), 'Av. Lonquén 10500, San Bernardo', 'Terminal Pesquero San Antonio, San Antonio'),
(20, 13, 245832, DATE_SUB(NOW(), INTERVAL 26 DAY), 'Camino Melipilla Km 5, Padre Hurtado', 'Puerto de Valparaíso, Valparaíso'),
(20, 13, 248240, DATE_SUB(NOW(), INTERVAL 19 DAY), 'Zona Industrial Noviciado, Pudahuel', 'Zona Industrial La Cruz, La Cruz'),
(26, 44, 19017, DATE_SUB(NOW(), INTERVAL 50 DAY), 'Av. Presidente Eduardo Frei Montalva 6000, Renca', 'Parque Industrial Curicó, Curicó'),
(26, 44, 22247, DATE_SUB(NOW(), INTERVAL 56 DAY), 'Av. Lonquén 10500, San Bernardo', 'Parque Industrial Curicó, Curicó'),
(26, 44, 26434, DATE_SUB(NOW(), INTERVAL 39 DAY), 'Av. Américo Vespucio 1234, Pudahuel', 'Zona Industrial La Cruz, La Cruz'),
(26, 44, 34053, DATE_SUB(NOW(), INTERVAL 46 DAY), 'Camino Lo Echevers 1500, Quilicura', 'Ruta 5 Norte Km 85, Los Andes'),
(26, 44, 38667, DATE_SUB(NOW(), INTERVAL 17 DAY), 'Terminal de Carga Lo Boza, Pudahuel', 'Parque Industrial Lampa, Lampa'),
(35, 21, 53385, DATE_SUB(NOW(), INTERVAL 96 DAY), 'Av. General Velásquez 8900, Maipú', 'Av. Circunvalación 3400, Chillán'),
(35, 21, 59601, DATE_SUB(NOW(), INTERVAL 69 DAY), 'Zona Industrial Noviciado, Pudahuel', 'Zona Industrial Coronel, Coronel'),
(35, 21, 66913, DATE_SUB(NOW(), INTERVAL 46 DAY), 'Ruta 5 Sur Km 25, San Bernardo', 'Zona Industrial Graneros, Graneros'),
(35, 21, 71731, DATE_SUB(NOW(), INTERVAL 10 DAY), 'Av. Vicuña Mackenna 7200, La Florida', 'Puerto de San Antonio, San Antonio'),
(76, 11, 32697, DATE_SUB(NOW(), INTERVAL 69 DAY), 'Zona Industrial Noviciado, Pudahuel', 'Zona Industrial Coronel, Coronel'),
(76, 11, 38489, DATE_SUB(NOW(), INTERVAL 24 DAY), 'Av. La Serena 1200, Cerrillos', 'Parque Industrial Curicó, Curicó'),
(76, 11, 46098, DATE_SUB(NOW(), INTERVAL 20 DAY), 'Ruta 78 Km 8, Maipú', 'Av. Circunvalación 3400, Chillán'),
(23, 19, 212317, DATE_SUB(NOW(), INTERVAL 48 DAY), 'Camino Melipilla Km 5, Padre Hurtado', 'Ruta 5 Sur Km 400, Los Ángeles'),
(23, 19, 219775, DATE_SUB(NOW(), INTERVAL 28 DAY), 'Ruta 78 Km 8, Maipú', 'Zona Industrial Graneros, Graneros'),
(23, 19, 222474, DATE_SUB(NOW(), INTERVAL 18 DAY), 'Av. Presidente Eduardo Frei Montalva 6000, Renca', 'Puerto de San Antonio, San Antonio'),
(91, 38, 142334, DATE_SUB(NOW(), INTERVAL 96 DAY), 'Zona Industrial Noviciado, Pudahuel', 'Ruta 68 Km 95, Casablanca'),
(91, 38, 149038, DATE_SUB(NOW(), INTERVAL 60 DAY), 'Av. Vicuña Mackenna 7200, La Florida', 'Ruta 5 Norte Km 85, Los Andes'),
(91, 38, 154140, DATE_SUB(NOW(), INTERVAL 24 DAY), 'Acceso Sur Bodega Central, Lo Espejo', 'Av. Argentina 1600, Valparaíso'),
(91, 38, 160812, DATE_SUB(NOW(), INTERVAL 19 DAY), 'Av. Lonquén 10500, San Bernardo', 'Av. El Belloto 2800, Quilpué'),
(64, 75, 166341, DATE_SUB(NOW(), INTERVAL 96 DAY), 'Ruta 68 Km 12, Pudahuel', 'Ruta 5 Sur Km 140, Rancagua'),
(64, 75, 170861, DATE_SUB(NOW(), INTERVAL 57 DAY), 'Av. General Velásquez 8900, Maipú', 'Ruta 68 Km 95, Casablanca'),
(64, 75, 173100, DATE_SUB(NOW(), INTERVAL 44 DAY), 'Av. Portales 3400, San Joaquín', 'Av. El Belloto 2800, Quilpué'),
(64, 75, 179140, DATE_SUB(NOW(), INTERVAL 11 DAY), 'Av. Portales 3400, San Joaquín', 'Av. El Belloto 2800, Quilpué'),
(94, 51, 76396, DATE_SUB(NOW(), INTERVAL 55 DAY), 'Av. Portales 3400, San Joaquín', 'Zona Industrial Graneros, Graneros'),
(94, 51, 80709, DATE_SUB(NOW(), INTERVAL 48 DAY), 'Terminal de Carga Lo Boza, Pudahuel', 'Ruta 68 Km 95, Casablanca'),
(94, 51, 83622, DATE_SUB(NOW(), INTERVAL 33 DAY), 'Ruta 78 Km 8, Maipú', 'Parque Industrial Temuco, Temuco'),
(94, 51, 88708, DATE_SUB(NOW(), INTERVAL 48 DAY), 'Terminal de Carga Lo Boza, Pudahuel', 'Terminal de Carga Quilpué, Quilpué'),
(94, 51, 95203, DATE_SUB(NOW(), INTERVAL 15 DAY), 'Camino Melipilla Km 5, Padre Hurtado', 'Ruta 5 Sur Km 400, Los Ángeles'),
(51, 40, 138536, DATE_SUB(NOW(), INTERVAL 55 DAY), 'Av. Vicuña Mackenna 7200, La Florida', 'Av. OHiggins 850, Rancagua'),
(51, 40, 142736, DATE_SUB(NOW(), INTERVAL 52 DAY), 'Parque Industrial Enea, Pudahuel', 'Av. Los Carrera 550, Osorno'),
(51, 40, 147402, DATE_SUB(NOW(), INTERVAL 57 DAY), 'Av. Lonquén 10500, San Bernardo', 'Ruta 5 Sur Km 680, Temuco'),
(51, 40, 154280, DATE_SUB(NOW(), INTERVAL 28 DAY), 'Av. Vicuña Mackenna 7200, La Florida', 'Av. OHiggins 850, Rancagua'),
(51, 40, 160553, DATE_SUB(NOW(), INTERVAL 14 DAY), 'Camino Melipilla Km 5, Padre Hurtado', 'Parque Industrial Curicó, Curicó'),
(74, 61, 213839, DATE_SUB(NOW(), INTERVAL 66 DAY), 'Av. Vicuña Mackenna 7200, La Florida', 'Ruta 5 Norte Km 85, Los Andes'),
(74, 61, 220828, DATE_SUB(NOW(), INTERVAL 24 DAY), 'Ruta 57 Km 10, Colina', 'Terminal de Carga Quilpué, Quilpué'),
(74, 61, 225292, DATE_SUB(NOW(), INTERVAL 10 DAY), 'Camino Melipilla Km 5, Padre Hurtado', 'Parque Industrial Curicó, Curicó'),
(17, 58, 141432, DATE_SUB(NOW(), INTERVAL 42 DAY), 'Av. El Salto 2300, Huechuraba', 'Av. OHiggins 850, Rancagua'),
(17, 58, 145355, DATE_SUB(NOW(), INTERVAL 26 DAY), 'Terminal de Carga Lo Boza, Pudahuel', 'Terminal Logístico Concepción, Concepción'),
(17, 58, 151276, DATE_SUB(NOW(), INTERVAL 11 DAY), 'Av. Vicuña Mackenna 7200, La Florida', 'Terminal Logístico Concepción, Concepción'),
(98, 9, 161151, DATE_SUB(NOW(), INTERVAL 45 DAY), 'Av. Portales 3400, San Joaquín', 'Ruta 5 Sur Km 140, Rancagua'),
(98, 9, 166016, DATE_SUB(NOW(), INTERVAL 42 DAY), 'Ruta 78 Km 8, Maipú', 'Av. Argentina 1600, Valparaíso'),
(98, 9, 172517, DATE_SUB(NOW(), INTERVAL 19 DAY), 'Av. Portales 3400, San Joaquín', 'Av. Concepción 2100, Talca'),
(68, 53, 44722, DATE_SUB(NOW(), INTERVAL 96 DAY), 'Av. Portales 3400, San Joaquín', 'Terminal Logístico Concepción, Concepción'),
(68, 53, 48530, DATE_SUB(NOW(), INTERVAL 66 DAY), 'Terminal de Carga Lo Boza, Pudahuel', 'Terminal de Carga Quilpué, Quilpué'),
(68, 53, 53459, DATE_SUB(NOW(), INTERVAL 30 DAY), 'Av. Presidente Eduardo Frei Montalva 6000, Renca', 'Centro de Distribución Nos, San Bernardo'),
(68, 53, 56092, DATE_SUB(NOW(), INTERVAL 18 DAY), 'Ruta 68 Km 12, Pudahuel', 'Ruta 68 Km 95, Casablanca'),
(30, 47, 220521, DATE_SUB(NOW(), INTERVAL 65 DAY), 'Ruta 5 Sur Km 25, San Bernardo', 'Av. OHiggins 850, Rancagua'),
(30, 47, 228272, DATE_SUB(NOW(), INTERVAL 60 DAY), 'Av. Lonquén 10500, San Bernardo', 'Av. Argentina 1600, Valparaíso'),
(30, 47, 233977, DATE_SUB(NOW(), INTERVAL 42 DAY), 'Ruta 57 Km 10, Colina', 'Av. OHiggins 850, Rancagua'),
(30, 47, 237295, DATE_SUB(NOW(), INTERVAL 48 DAY), 'Camino Melipilla Km 5, Padre Hurtado', 'Av. Padre Hurtado 1800, San Fernando'),
(30, 47, 242193, DATE_SUB(NOW(), INTERVAL 11 DAY), 'Av. La Serena 1200, Cerrillos', 'Terminal de Carga Quilpué, Quilpué'),
(22, 10, 182553, DATE_SUB(NOW(), INTERVAL 40 DAY), 'Av. Vicuña Mackenna 7200, La Florida', 'Zona Industrial Graneros, Graneros'),
(22, 10, 189907, DATE_SUB(NOW(), INTERVAL 60 DAY), 'Av. El Salto 2300, Huechuraba', 'Ruta 5 Norte Km 85, Los Andes'),
(22, 10, 191720, DATE_SUB(NOW(), INTERVAL 38 DAY), 'Acceso Sur Bodega Central, Lo Espejo', 'Parque Industrial Temuco, Temuco'),
(22, 10, 194356, DATE_SUB(NOW(), INTERVAL 25 DAY), 'Av. Portales 3400, San Joaquín', 'Centro de Distribución Nos, San Bernardo'),
(58, 57, 86034, DATE_SUB(NOW(), INTERVAL 69 DAY), 'Ruta 5 Norte Km 15, Lampa', 'Terminal Logístico Concepción, Concepción'),
(58, 57, 89894, DATE_SUB(NOW(), INTERVAL 34 DAY), 'Ruta 5 Sur Km 25, San Bernardo', 'Zona Industrial Graneros, Graneros'),
(58, 57, 91785, DATE_SUB(NOW(), INTERVAL 17 DAY), 'Ruta 57 Km 10, Colina', 'Ruta 68 Km 95, Casablanca');

-- ────────────────────────────────────────────────────────────
-- MANTENIMIENTO (estado Programado)
-- ────────────────────────────────────────────────────────────
INSERT INTO mantenimiento (id_vehiculo, estado, tipo_mantenimiento, origen, descripcion, kilometraje, id_usuario_mantenimiento) VALUES
(16, 'Programado', 'Preventivo', 'Sistema', '', 200427,  null),
(65, 'Programado', 'Correctivo', 'Manual', '', 67715, null),
(59, 'Programado', 'Preventivo', 'Sistema', '', 95123, null),
(13, 'Programado', 'Correctivo', 'Manual', '', 249549, null),
(44, 'Programado', 'Correctivo', 'Manual', '', 39253, null),
(11, 'Programado', 'Preventivo', 'Sistema', '', 47894, null),
(75, 'Programado', 'Preventivo', 'Sistema', '', 180614, null),
(61, 'Programado', 'Preventivo', 'Sistema', '', 226787, null ),
(58, 'Programado', 'Preventivo', 'Sistema', '', 152091, null),
(47, 'Programado', 'Correctivo', 'Manual', '', 242860, null),
(10, 'Programado', 'Preventivo', 'Sistema', '', 195380, null),
(57, 'Programado', 'Correctivo', 'Manual', '', 93308, null);

INSERT INTO mantenimiento (id_vehiculo, estado, tipo_mantenimiento, origen, descripcion, kilometraje, id_usuario_mantenimiento, fecha_creacion, fecha_completado) VALUES
(12, 'Completado', 'Preventivo', 'Sistema', 'Cambio de aceite y filtros', 78756, 2, '2026-04-01 07:00:00', '2026-04-05 15:00:32'),
(21, 'Completado', 'Correctivo', 'Manual', 'Cambio de alternador', 73125, 2, '2026-04-01 08:00:00', '2026-04-05 15:03:32'),
(19, 'Completado', 'Preventivo', 'Sistema', 'Revisión de frenos y suspensión', 224260, 9, '2026-04-09 07:00:00', '2026-04-10 11:20:32'),
(38, 'Completado', 'Preventivo', 'Sistema', 'Cambio de correa de distribución', 161463, 12, '2026-04-09 06:00:00', '2026-04-12 15:00:32'),
(51, 'Cancelado', 'Correctivo', 'Manual', 'Mantención mal programada', 96395, 11, '2026-04-01 07:00:00', '2026-04-05 15:00:32'),
(40, 'Completado', 'Correctivo', 'Manual', 'Reparación de sistema hidráulico', 162504, 12, '2026-04-15 17:00:00', '2026-04-16 15:00:32'),
(9, 'Cancelado', 'Preventivo', 'Sistema', 'Se posterga para 1000km más', 173414, 11, '2026-03-31 07:00:00', '2026-04-02 10:10:32'),
(53, 'Completado', 'Correctivo', 'Manual', 'Cambio de alternador', 56802, 12, '2026-04-01 07:00:00', '2026-04-03 15:00:32');


-- ────────────────────────────────────────────────────────────
-- ETAPA 2
-- ────────────────────────────────────────────────────────────


-- Tipos de equipo
CREATE TABLE tipos_equipo (
    id_tipo_equipo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO tipos_equipo (nombre) VALUES
    ('Notebook'),
    ('PC Escritorio'),
    ('Impresora'),
    ('Celular');

-- Tipos de pieza de hardware
CREATE TABLE tipos_pieza (
    id_tipo_pieza INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO tipos_pieza (nombre) VALUES
    ('Memoria RAM'),
    ('Disco Duro HDD'),
    ('Disco Duro SSD'),
    ('CPU'),
    ('GPU'),
    ('Fuente de Poder'),
    ('Tarjeta Madre'),
    ('Ventilador'),
    ('Batería'),
    ('Pantalla'),
    ('Tóner'),
    ('Otro');

-- Tipos de software
CREATE TABLE tipos_software (
    id_tipo_software INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO tipos_software (nombre) VALUES
    ('Sistema Operativo'),
    ('Ofimática'),
    ('Antivirus'),
    ('Navegador'),
    ('Comunicaciones'),
    ('Utilidades'),
    ('Otro');

-- ============================================================
-- RF-06 / RF-08: EQUIPOS DE OFICINA
-- ============================================================

CREATE TABLE equipos_oficina (
    id_equipo INT AUTO_INCREMENT PRIMARY KEY,
    id_tipo_equipo INT NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    numero_serie VARCHAR(100) NOT NULL UNIQUE,
    estado ENUM('Activo', 'Inactivo', 'En Mantenimiento') NOT NULL DEFAULT 'Activo',
    id_responsable INT,
    fecha_adquisicion DATE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_tipo_equipo) REFERENCES tipos_equipo(id_tipo_equipo),
    FOREIGN KEY (id_responsable) REFERENCES usuarios(id_usuario)
);

-- ============================================================
-- RF-09: INVENTARIO DE PIEZAS (stock disponible en bodega)
-- ============================================================

CREATE TABLE piezas (
    id_pieza INT AUTO_INCREMENT PRIMARY KEY,
    id_tipo_pieza INT NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(100),
    descripcion TEXT,
    stock_actual INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 1,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_tipo_pieza) REFERENCES tipos_pieza(id_tipo_pieza)
);

-- ============================================================
-- RF-09: COMPONENTES INSTALADOS EN CADA EQUIPO
-- ============================================================

CREATE TABLE equipo_componentes (
    id_componente INT AUTO_INCREMENT PRIMARY KEY,
    id_equipo INT NOT NULL,
    id_pieza INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    fecha_instalacion DATE,
    FOREIGN KEY (id_equipo) REFERENCES equipos_oficina(id_equipo) ON DELETE CASCADE,
    FOREIGN KEY (id_pieza)  REFERENCES piezas(id_pieza)
);

-- ============================================================
-- RF-06 / RF-08: MANTENIMIENTO DE EQUIPOS
-- Estado: Correctivo o Preventivo
-- ============================================================

CREATE TABLE mantenimiento_equipos (
    id_mantenimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_equipo INT NOT NULL,
    tipo_mantenimiento ENUM('Preventivo', 'Correctivo') NOT NULL DEFAULT 'Preventivo',
    estado ENUM('Programado', 'En Proceso', 'Completado', 'Cancelado') NOT NULL DEFAULT 'Programado',
    descripcion TEXT,
    id_tecnico INT,
    fecha_inicio DATETIME,
    fecha_completado DATETIME,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_equipo) REFERENCES equipos_oficina(id_equipo) ON DELETE CASCADE,
    FOREIGN KEY (id_tecnico) REFERENCES usuarios(id_usuario)
);

-- ============================================================
-- RF-09: PIEZAS USADAS EN CADA MANTENIMIENTO
-- ============================================================

CREATE TABLE mantenimiento_piezas (
    id_mant_pieza INT AUTO_INCREMENT PRIMARY KEY,
    id_mantenimiento INT NOT NULL,
    id_pieza INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    tipo_uso ENUM('Reemplazo', 'Reparación', 'Adición', 'Retiro', 'Otro') NOT NULL DEFAULT 'Reemplazo',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_mantenimiento) REFERENCES mantenimiento_equipos(id_mantenimiento) ON DELETE CASCADE,
    FOREIGN KEY (id_pieza) REFERENCES piezas(id_pieza)
);

-- ============================================================
-- RF-07: CATÁLOGO DE SOFTWARE
-- ============================================================

CREATE TABLE software (
    id_software INT AUTO_INCREMENT PRIMARY KEY,
    id_tipo_software INT NOT NULL,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    fabricante VARCHAR(100),
    descripcion TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_tipo_software) REFERENCES tipos_software(id_tipo_software)
);

-- ============================================================
-- RF-07 / RF-08: SOFTWARE INSTALADO POR EQUIPO
-- Historial de versiones y actualizaciones
-- ============================================================

CREATE TABLE software_equipo (
    id_sw_equipo INT AUTO_INCREMENT PRIMARY KEY,
    id_equipo INT NOT NULL,
    id_software INT NOT NULL,
    version VARCHAR(50) NOT NULL,
    estado ENUM('Instalado', 'Actualizado', 'Desinstalado') NOT NULL DEFAULT 'Instalado',
    id_tecnico  INT,
    fecha_accion DATETIME NOT NULL,
    notas TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_equipo) REFERENCES equipos_oficina(id_equipo) ON DELETE CASCADE,
    FOREIGN KEY (id_software) REFERENCES software(id_software),
    FOREIGN KEY (id_tecnico) REFERENCES usuarios(id_usuario)
);

-- ============================================================
-- CHECKLISTS ESPECÍFICOS POR TIPO DE EQUIPO 
-- ============================================================

-- Checklist para Notebooks y PCs de Escritorio
CREATE TABLE detalle_mant_computador (
    id_detalle_pc INT AUTO_INCREMENT PRIMARY KEY,
    id_mantenimiento INT NOT NULL UNIQUE,
    desarmado_inicial BOOLEAN DEFAULT FALSE,
    limpieza_fisica BOOLEAN DEFAULT FALSE,
    check_ram BOOLEAN DEFAULT FALSE,
    check_almacenamiento BOOLEAN DEFAULT FALSE,
    armado_cierre BOOLEAN DEFAULT FALSE,
    actualizacion_so BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_mantenimiento) REFERENCES mantenimiento_equipos(id_mantenimiento) ON DELETE CASCADE
);

-- Checklist para Impresoras 
CREATE TABLE detalle_mant_impresora (
    id_detalle_imp INT AUTO_INCREMENT PRIMARY KEY,
    id_mantenimiento INT NOT NULL UNIQUE,
    limpieza_rodillos BOOLEAN DEFAULT FALSE,
    revision_toner BOOLEAN DEFAULT FALSE,
    calibracion_cabezales BOOLEAN DEFAULT FALSE,
    actualizacion_firmware BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_mantenimiento) REFERENCES mantenimiento_equipos(id_mantenimiento) ON DELETE CASCADE
);

-- Checklist para Celulares
CREATE TABLE detalle_mant_celular (
    id_detalle_cel INT AUTO_INCREMENT PRIMARY KEY,
    id_mantenimiento INT NOT NULL UNIQUE,
    revision_pantalla_tactil BOOLEAN DEFAULT FALSE,
    test_rendimiento_bateria BOOLEAN DEFAULT FALSE,
    limpieza_puertos_carga BOOLEAN DEFAULT FALSE,
    actualizacion_android BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_mantenimiento) REFERENCES mantenimiento_equipos(id_mantenimiento) ON DELETE CASCADE
);

CREATE TABLE detalle_mant_notebook (
    id_detalle_nb INT AUTO_INCREMENT PRIMARY KEY,
    id_mantenimiento INT NOT NULL UNIQUE,
    desarme_inicial BOOLEAN DEFAULT FALSE,
    limpieza_fisica BOOLEAN DEFAULT FALSE,
    check_ram BOOLEAN DEFAULT FALSE,
    check_almacenamiento BOOLEAN DEFAULT FALSE,
    cambio_pasta BOOLEAN DEFAULT FALSE,
    armado_cierre BOOLEAN DEFAULT FALSE,
    sustitucion_piezas BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_mantenimiento) REFERENCES mantenimiento_equipos(id_mantenimiento) ON DELETE CASCADE
);

INSERT INTO usuarios (nombre, email, password, id_rol) VALUES
    ('Carlos Mendoza', 'c.mendoza@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 4),
    ('Ana Rodríguez', 'a.rodriguez@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 4),
    ('Diego Soto', 'd.soto@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 5),
    ('Valentina Torres', 'v.torres@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 5),
    ('Roberto Fuentes', 'r.fuentes@hirata.cl', '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 6),
    ('Javiera Campos',   'j.campos@hirata.cl',     '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 6),
    ('Matías Herrera',   'm.herrera@hirata.cl',    '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 7),
    ('Daniela Núñez',    'd.nunez@hirata.cl',      '$2a$10$scGxNiZOnINrxXydL2.0x.zfC.4S1.NN1mIM.d24kw0U58NwX9k1S', 7);

SET @tec_mant1 = (SELECT id_usuario FROM usuarios WHERE email = 'c.mendoza@hirata.cl');
SET @tec_mant2 = (SELECT id_usuario FROM usuarios WHERE email = 'a.rodriguez@hirata.cl');
SET @tec_it1   = (SELECT id_usuario FROM usuarios WHERE email = 'd.soto@hirata.cl');
SET @tec_it2   = (SELECT id_usuario FROM usuarios WHERE email = 'v.torres@hirata.cl');
SET @adm_inv   = (SELECT id_usuario FROM usuarios WHERE email = 'r.fuentes@hirata.cl');


-- Notebooks (10)
INSERT INTO equipos_oficina (id_tipo_equipo, marca, modelo, numero_serie, estado, id_responsable, fecha_adquisicion) VALUES
(1, 'Dell', 'Latitude 5540', 'NB-DELL-001', 'Activo', @tec_it1, '2023-03-10'),
(1, 'Dell', 'Latitude 5540', 'NB-DELL-002', 'Activo', @tec_it1, '2023-03-10'),
(1, 'HP', 'ProBook 450 G10', 'NB-HP-001', 'Activo', @tec_it2, '2023-05-15'),
(1, 'HP', 'ProBook 450 G10', 'NB-HP-002', 'Activo',  @tec_it2, '2023-05-15'),
(1, 'HP',  'EliteBook 840 G10', 'NB-HP-003', 'En Mantenimiento', @tec_it2, '2022-11-20'),
(1, 'Lenovo', 'ThinkPad T14 Gen 3', 'NB-LEN-001', 'Activo', @tec_mant1, '2023-07-01'),
(1, 'Lenovo', 'ThinkPad T14 Gen 3', 'NB-LEN-002', 'Activo', @tec_mant1, '2023-07-01'),
(1, 'Asus', 'ExpertBook B5 B5402', 'NB-ASUS-001', 'Activo', @adm_inv, '2023-09-12'),
(1, 'Acer', 'TravelMate P4 TMP414', 'NB-ACER-001', 'Inactivo', @adm_inv, '2021-06-30'),
(1, 'MSI', 'Modern 15 B13M', 'NB-MSI-001', 'Activo', @tec_it1,  '2024-01-08');

-- PC Escritorio (12)
INSERT INTO equipos_oficina (id_tipo_equipo, marca, modelo, numero_serie, estado, id_responsable, fecha_adquisicion) VALUES
(2, 'Dell', 'OptiPlex 7010', 'PC-DELL-001', 'Activo', @tec_it1, '2022-08-14'),
(2, 'Dell', 'OptiPlex 7010', 'PC-DELL-002', 'Activo', @tec_it1,' 2022-08-14'),
(2, 'Dell', 'OptiPlex 7010', 'PC-DELL-003', 'En Mantenimiento', @tec_it1, '2022-08-14'),
(2, 'HP', 'EliteDesk 800 G9', 'PC-HP-001', 'Activo', @tec_it2, '2023-01-20'),
(2, 'HP', 'EliteDesk 800 G9', 'PC-HP-002', 'Activo', @tec_it2, '2023-01-20'),
(2, 'HP', 'ProDesk 400 G9', 'PC-HP-003', 'Activo', @tec_mant2, '2023-04-05'),
(2, 'HP', 'ProDesk 400 G9', 'PC-HP-004', 'Inactivo', @tec_mant2, '2021-03-18'),
(2, 'Lenovo', 'ThinkCentre M70s Gen 4', 'PC-LEN-001','Activo', @adm_inv, '2023-06-22'),
(2, 'Lenovo', 'ThinkCentre M70s Gen 4', 'PC-LEN-002', 'Activo', @adm_inv, '2023-06-22'),
(2, 'Asus', 'ExpertCenter D5 D500TC', 'PC-ASUS-001', 'Activo', @tec_it1, '2023-10-15'),
(2, 'Acer', 'Veriton M6680G','PC-ACER-001', 'Activo', @tec_mant1, '2022-12-01'),
(2, 'Acer', 'Veriton X4690G','PC-ACER-002', 'En Mantenimiento', @tec_mant1, '2022-12-01');

-- Impresoras (5)
INSERT INTO equipos_oficina (id_tipo_equipo, marca, modelo, numero_serie, estado, id_responsable, fecha_adquisicion) VALUES
(3, 'HP', 'LaserJet Pro M404dn', 'IMP-HP-001', 'Activo', @tec_mant2, '2022-05-10'),
(3, 'HP', 'LaserJet Pro M404dn', 'IMP-HP-002', 'En Mantenimiento', @tec_mant2, '2022-05-10'),
(3, 'Canon', 'i-SENSYS LBP633Cdw', 'IMP-CAN-001', 'Activo', @tec_it2, '2023-02-28'),
(3, 'Epson', 'WorkForce Pro WF-C5790', 'IMP-EPS-001', 'Activo',  @tec_it1, '2023-08-07'),
(3, 'Brother', 'HL-L8360CDW', 'IMP-BRO-001', 'Inactivo', @adm_inv, '2021-11-15');

-- Celulares (5)
INSERT INTO equipos_oficina (id_tipo_equipo, marca, modelo, numero_serie, estado, id_responsable, fecha_adquisicion) VALUES
(4, 'Samsung', 'Galaxy A54 5G', 'CEL-SAM-001', 'Activo', @tec_mant1, '2023-04-20'),
(4, 'Samsung', 'Galaxy A54 5G', 'CEL-SAM-002', 'Activo', @tec_mant2, '2023-04-20'),
(4, 'Motorola','Moto G84', 'CEL-MOT-001', 'Activo', @tec_it1,   '2023-07-14'),
(4, 'Motorola','Moto G84', 'CEL-MOT-002', 'En Mantenimiento', @tec_it2, '2023-07-14'),
(4, 'Xiaomi',  'Redmi Note 12', 'CEL-XIA-001', 'Activo', @adm_inv,'2024-02-01');

-- ============================================================
-- PIEZAS — 100 unidades en inventario
-- ============================================================

-- Memoria RAM — 14 piezas
INSERT INTO piezas (id_tipo_pieza, marca, modelo, descripcion, stock_actual, stock_minimo) VALUES
(1, 'Kingston', 'KVR32N22S8/8', 'DDR4 8GB 3200MHz SODIMM',12, 3),
(1, 'Kingston', 'KVR32N22D8/16','DDR4 16GB 3200MHz DIMM', 8,  2),
(1, 'Corsair', 'CMK8GX4M1A2666C16','DDR4 8GB 2666MHz DIMM',  6,  2),
(1, 'Corsair', 'CMK32GX4M2E3200C16','DDR4 32GB 3600MHz DIMM', 3,  1),
(1, 'Samsung', 'M471A1K43EB1-CWE', 'DDR4 8GB 3200MHz SODIMM OEM', 9,  3),
(1, 'Samsung', 'M425R1GB4BB0-CQK', 'DDR5 8GB 4800MHz SODIMM', 4,  2),
(1, 'G.Skill', 'F4-3200C16S-16GIS','DDR4 16GB 3200MHz DIMM', 5,  2),
(1, 'G.Skill', 'F5-6000J3040G32GX2','DDR5 32GB 6000MHz DIMM Kit', 2,  1),
(1, 'Crucial', 'CT8G4SFS832A','DDR4 8GB 3200MHz SODIMM', 10, 3),
(1, 'Crucial','CT4G4SFS8266', 'DDR4 4GB 2666MHz SODIMM', 7,  2),
(1, 'HyperX', 'HX432S20IB2/16','DDR4 16GB 3200MHz SODIMM', 5,  2),
(1, 'Patriot', 'PSD48G320081S','DDR4 8GB 3200MHz SODIMM', 8,  2),
(1, 'TeamGroup', 'TED416G3200C22-S01','DDR4 16GB 3200MHz SODIMM', 4,  1),
(1, 'Adata', 'AD4S32008G22-SGN', 'DDR4 8GB 3200MHz SODIMM', 6,  2);

-- Disco Duro HDD — 9 piezas
INSERT INTO piezas (id_tipo_pieza, marca, modelo, descripcion, stock_actual, stock_minimo) VALUES
(2, 'Seagate','ST1000DM010', 'Barracuda 1TB 7200rpm SATA', 7,  2),
(2, 'Seagate', 'ST2000DM008', 'Barracuda 2TB 7200rpm SATA', 5,  2),
(2, 'WD', 'WD10EZEX','Blue 1TB 7200rpm SATA', 6,  2),
(2, 'WD', 'WD20EZAZ','Blue 2TB 5400rpm SATA', 4,  1),
(2, 'WD', 'WD40EFRX','Red 4TB NAS 5400rpm', 2,  1),
(2, 'Toshiba', 'HDWD110UZSVA','P300 1TB 7200rpm SATA', 5,  2),
(2, 'Toshiba', 'HDWD120UZSVA','P300 2TB 7200rpm SATA', 3,  1),
(2, 'HGST', 'HUS726020ALE614','Ultrastar 2TB 7200rpm', 2,  1),
(2, 'Seagate', 'ST2000VX015','SkyHawk 2TB Vigilancia', 3,  1);

-- SSD — 9 piezas
INSERT INTO piezas (id_tipo_pieza, marca, modelo, descripcion, stock_actual, stock_minimo) VALUES
(3, 'Samsung', 'MZ-77E500B/AM','870 EVO 500GB SATA 2.5"', 8,  2),
(3, 'Samsung', 'MZ-77E1T0B/AM', '870 EVO 1TB SATA 2.5"', 5,  2),
(3, 'WD','WDS500G2B0A', 'Blue 500GB SATA 2.5"',7,  2),
(3, 'WD', 'WDS100T2B0A','Blue 1TB SATA 2.5"', 4,  2),
(3, 'Kingston','SA400S37/480G', 'A400 480GB SATA 2.5"',6,  2),
(3, 'Kingston','SNV2S/1000G','NV2 1TB M.2 NVMe', 5,  2),
(3, 'Crucial','CT500MX500SSD1','MX500 500GB SATA 2.5"', 6,  2),
(3, 'Crucial','CT1000MX500SSD1','MX500 1TB SATA 2.5"', 3,  1),
(3, 'SanDisk','SDSSDA-500G-G26','Ultra 500GB SATA 2.5"', 4,  1);

-- CPU — 9 piezas
INSERT INTO piezas (id_tipo_pieza, marca, modelo, descripcion, stock_actual, stock_minimo) VALUES
(4, 'Intel', 'BX8071512400','Core i5-12400 LGA1700 2.5GHz', 4,  1),
(4, 'Intel', 'BX8071512700', 'Core i7-12700 LGA1700 2.1GHz', 3,  1),
(4, 'Intel','BX8071512900', 'Core i9-12900 LGA1700 2.4GHz', 2,  1),
(4, 'Intel', 'BX8071513600K', 'Core i5-13600K LGA1700 3.5GHz', 3,  1),
(4, 'AMD', '100-100000927BOX','Ryzen 5 5600 AM4 3.5GHz', 4,  1),
(4, 'AMD', '100-100000063WOF','Ryzen 7 5800X AM4 3.8GHz', 2,  1),
(4, 'AMD', '100-100000061WOF','Ryzen 9 5900X AM4 3.7GHz',  2,  1),
(4, 'Intel','BX8071512100', 'Core i3-12100 LGA1700 3.3GHz',  5,  2),
(4, 'AMD','100-100001015BOX', 'Ryzen 5 7600 AM5 3.8GHz',  2,  1);

-- GPU — 8 piezas

INSERT INTO piezas (id_tipo_pieza, marca, modelo, descripcion, stock_actual, stock_minimo) VALUES
(5, 'NVIDIA', 'RTX 3060 12GB', 'GeForce RTX 3060 12GB GDDR6', 3,  1),
(5, 'NVIDIA', 'RTX 3070 8GB', 'GeForce RTX 3070 8GB GDDR6',  2,  1),
(5, 'AMD', 'RX 6600 8GB', 'Radeon RX 6600 8GB GDDR6',  3,  1),
(5, 'AMD', 'RX 6700 10GB', 'Radeon RX 6700 10GB GDDR6', 2,  1),
(5, 'NVIDIA','GTX 1660 Super 6GB','GeForce GTX 1660 SUPER 6GB', 4,  1),
(5, 'NVIDIA','RTX 4060 8GB', 'GeForce RTX 4060 8GB GDDR6',  2,  1),
(5, 'AMD', 'RX 7600 8GB', 'Radeon RX 7600 8GB GDDR6',  2,  1),
(5, 'NVIDIA', 'RTX 3050 8GB', 'GeForce RTX 3050 8GB GDDR6', 3,  1);

-- Fuente de Poder — 8 piezas
INSERT INTO piezas (id_tipo_pieza, marca, modelo, descripcion, stock_actual, stock_minimo) VALUES
(6, 'Corsair', 'CP-9020197-NA', 'RM750x 750W 80+ Gold Modular', 3,  1),
(6, 'EVGA', '220-G3-0650-Y1', 'SuperNOVA 650W G3 80+ Gold', 3,  1),
(6, 'Seasonic', 'SSR-850FX', 'Focus GX-850 850W 80+ Gold', 2,  1),
(6, 'Cooler Master','MPE-6001-ACAAB','MWE 600W 80+ Bronze', 4,  2),
(6, 'be quiet!', 'BN282', 'Straight Power 11 750W', 2,  1),
(6, 'Thermaltake','W0373RU', 'Smart 500W 80+ White', 5,  2),
(6, 'Antec', 'NEO ECO 650W','NeoECO 650W 80+ Bronze', 3,  1),
(6, 'FSP',  'FSP550-50AACBL', 'Hydro 550W 80+ Bronze', 3,  1);

-- Tarjeta Madre — 8 piezas
INSERT INTO piezas (id_tipo_pieza, marca, modelo, descripcion, stock_actual, stock_minimo) VALUES
(7, 'ASUS', 'ROG STRIX B550-F', 'B550 ATX AM4 DDR4', 2,  1),
(7, 'MSI', 'MAG B550 TOMAHAWK', 'B550 ATX AM4 DDR4', 2,  1),
(7, 'Gigabyte', 'B660M DS3H', 'B660 mATX LGA1700 DDR4', 3,  1),
(7, 'ASRock', 'B460M Pro4', 'B460 mATX LGA1200 DDR4', 2,  1),
(7, 'ASUS', 'PRIME Z690-P', 'Z690 ATX LGA1700 DDR4/5', 2,  1),
(7, 'MSI', 'PRO B660M-A DDR4', 'B660 mATX LGA1700 DDR4', 3,  1),
(7, 'Gigabyte', 'Z690 UD DDR4', 'Z690 ATX LGA1700 DDR4', 2,  1),
(7, 'ASRock', 'B550M Pro4', 'B550 mATX AM4 DDR4', 2,  1);

-- Ventilador — 8 piezas
INSERT INTO piezas (id_tipo_pieza, marca, modelo, descripcion, stock_actual, stock_minimo) VALUES
(8, 'Noctua', 'NF-A12x25 PWM', 'Ventilador 120mm 2000rpm',5,  2),
(8, 'be quiet!', 'BL067', 'Silent Wings 3 120mm PWM', 5,  2),
(8, 'Corsair', 'CO-9050072-WW','LL120 RGB 120mm', 4,  2),
(8, 'Arctic', 'ACFAN00119A','P12 PWM 120mm 200-1800rpm', 8,  3),
(8, 'Cooler Master','MFX-B2DN-18NPK','SickleFlow 120 ARGB', 6,  2),
(8, 'Noctua', 'NH-D15','Cooler CPU dual torre', 3,  1),
(8, 'be quiet!', 'BK022', 'Dark Rock 4 Cooler CPU', 3,  1),
(8, 'DeepCool',  'DP-MCH4-GMX400', 'GAMMAXX 400 Cooler CPU', 4,  2);

-- Batería — 9 piezas
INSERT INTO piezas (id_tipo_pieza, marca, modelo, descripcion, stock_actual, stock_minimo) VALUES
(9, 'Dell', 'PGFX4', 'Batería 68Wh Latitude 5540', 4,  2),
(9, 'HP',  'L78555-005', 'Batería 56Wh ProBook 450 G10', 4,  2),
(9, 'Lenovo', '02DL014', 'Batería 57Wh ThinkPad T14 Gen3', 4,  2),
(9, 'Asus', '0B200-03980100', 'Batería 50Wh ExpertBook B5', 3,  1),
(9, 'Acer', 'AP18C7M','Batería 48Wh TravelMate P4', 3,  1),
(9, 'Samsung', 'AA-PBUN4TB', 'Batería 60Wh Galaxy Book', 3,  1),
(9, 'MSI', 'BTY-M6H', 'Batería 52.4Wh Modern 15', 3,  1),
(9, 'Generic', 'BTY-S14', 'Batería universal 48Wh 11.1V', 2,  1),
(9, 'Toshiba', 'PA5157U-1BRS', 'Batería 45Wh Tecra A50', 2,  1);

-- Pantalla — 9 piezas
INSERT INTO piezas (id_tipo_pieza, marca, modelo, descripcion, stock_actual, stock_minimo) VALUES
(10, 'Samsung','S27F350FHL', 'Monitor 27" FHD IPS 60Hz', 3,  1),
(10, 'LG', '24MK430H-B', 'Monitor 24" FHD IPS 75Hz', 4,  1),
(10, 'Dell', 'P2722H','Monitor 27" FHD IPS 60Hz', 3,  1),
(10, 'ASUS','VA24EHE', 'Monitor 24" FHD VA 75Hz', 4,  1),
(10, 'Acer', 'R271', 'Monitor 27" FHD IPS 75Hz', 2,  1),
(10, 'BenQ', 'GW2480', 'Monitor 24" FHD IPS 60Hz', 3,  1),
(10, 'ViewSonic','VA2732-H', 'Monitor 27" FHD VA 75Hz', 2,  1),
(10, 'HP', '24mh FHD', 'Monitor 24" FHD IPS 75Hz', 3,  1),
(10, 'Philips','275E1S','Monitor 27" QHD IPS 75Hz', 2,  1);

-- Tóner — 9 piezas
INSERT INTO piezas (id_tipo_pieza, marca, modelo, descripcion, stock_actual, stock_minimo) VALUES
(11, 'HP', 'CE285A', 'Tóner 85A Negro HP LaserJet', 6,  2),
(11, 'HP','CF226A','Tóner 26A Negro HP LaserJet', 5,  2),
(11, 'Canon','3514C002', 'Tóner 052 Negro i-SENSYS', 4,  2),
(11, 'Canon','3009C002', 'Tóner 057 Negro i-SENSYS', 4,  2),
(11, 'Epson','C13S110079','Tóner S110079 Negro WF-C5790', 3,  1),
(11, 'Brother', 'TN-3480', 'Tóner TN-3480 Negro HL-L8360CDW', 4,  2),
(11, 'Samsung', 'MLT-D111S', 'Tóner D111S Negro Xpress', 3,  1),
(11, 'Lexmark', '50F4H00','Tóner 504H Negro MS310', 2,  1),
(11, 'Xerox', '106R03623','Tóner Phaser 3330 Negro', 2,  1);

-- ============================================================
-- CATÁLOGO DE SOFTWARE
-- ============================================================

INSERT INTO software (id_tipo_software, nombre, fabricante, descripcion) VALUES

-- Sistema Operativo (tipo 1)
(1, 'Windows 11 Pro', 'Microsoft',  'Sistema operativo escritorio/laptop'),
(1, 'Windows 10 Pro', 'Microsoft',  'Sistema operativo escritorio (LTS)'),
(1, 'Android 13', 'Google',     'Sistema operativo móvil'),
(1, 'Android 14', 'Google',     'Sistema operativo móvil'),

-- Ofimática (tipo 2)
(2, 'Microsoft Office 365', 'Microsoft', 'Suite ofimática en la nube'),
(2, 'LibreOffice 7.6', 'TDF',  'Suite ofimática open source'),
(2, 'Google Workspace', 'Google', 'Suite ofimática colaborativa'),

-- Antivirus (tipo 3)
(3, 'Norton 360',  'NortonLifeLock','Antivirus + VPN + Firewall'),
(3, 'Kaspersky Endpoint Sec.', 'Kaspersky', 'Seguridad endpoint empresarial'),
(3, 'Windows Defender', 'Microsoft','Antivirus integrado Windows'),

-- Navegador (tipo 4)
(4, 'Google Chrome', 'Google',  'Navegador web'),
(4, 'Mozilla Firefox', 'Mozilla', 'Navegador web open source'),
(4, 'Microsoft Edge','Microsoft','Navegador web integrado'),

-- Comunicaciones (tipo 5)
(5, 'Microsoft Teams', 'Microsoft', 'Plataforma colaboración y videoconf.'),
(5, 'Zoom','Zoom', 'Videoconferencia empresarial'),
(5, 'Slack', 'Salesforce', 'Mensajería y colaboración'),

-- Utilidades (tipo 6)
(6, '7-Zip','7-Zip', 'Compresor/descompresor archivos'),
(6, 'Adobe Acrobat Reader','Adobe', 'Visor de documentos PDF'),
(6, 'CCleaner','Piriform', 'Limpieza y optimización del sistema'),

-- Otro: Drivers e impresoras (tipo 7)
(7, 'Driver HP LaserJet', 'HP', 'Driver universal impresoras HP'),
(7, 'Firmware Canon i-SENSYS', 'Canon', 'Firmware para impresoras Canon'),
(7, 'HP Printer Software','HP', 'Software gestión impresoras HP');

-- ============================================================
-- SOFTWARE POR EQUIPO
-- Capturar IDs de software
-- ============================================================

SET @sw_win11     = (SELECT id_software FROM software WHERE nombre = 'Windows 11 Pro');
SET @sw_win10     = (SELECT id_software FROM software WHERE nombre = 'Windows 10 Pro');
SET @sw_android14 = (SELECT id_software FROM software WHERE nombre = 'Android 14');
SET @sw_office    = (SELECT id_software FROM software WHERE nombre = 'Microsoft Office 365');
SET @sw_libreoff  = (SELECT id_software FROM software WHERE nombre = 'LibreOffice 7.6');
SET @sw_norton    = (SELECT id_software FROM software WHERE nombre = 'Norton 360');
SET @sw_chrome    = (SELECT id_software FROM software WHERE nombre = 'Google Chrome');
SET @sw_teams     = (SELECT id_software FROM software WHERE nombre = 'Microsoft Teams');
SET @sw_zoom      = (SELECT id_software FROM software WHERE nombre = 'Zoom');
SET @sw_driver_hp = (SELECT id_software FROM software WHERE nombre = 'Driver HP LaserJet');
SET @sw_fw_canon  = (SELECT id_software FROM software WHERE nombre = 'Firmware Canon i-SENSYS');
SET @sw_hp_print  = (SELECT id_software FROM software WHERE nombre = 'HP Printer Software');
SET @sw_adobe     = (SELECT id_software FROM software WHERE nombre = 'Adobe Acrobat Reader');

-- Capturar IDs de equipos por tipo
SET @nb01 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'NB-DELL-001');
SET @nb02 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'NB-DELL-002');
SET @nb03 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'NB-HP-001');
SET @nb04 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'NB-HP-002');
SET @nb05 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'NB-HP-003');
SET @nb06 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'NB-LEN-001');
SET @nb07 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'NB-LEN-002');
SET @nb08 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'NB-ASUS-001');
SET @nb09 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'NB-ACER-001');
SET @nb10 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'NB-MSI-001');

-- PC Escritorio → Windows 10 + LibreOffice + Chrome
SET @pc01 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'PC-DELL-001');
SET @pc02 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'PC-DELL-002');
SET @pc03 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'PC-DELL-003');
SET @pc04 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'PC-HP-001');
SET @pc05 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'PC-HP-002');
SET @pc06 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'PC-HP-003');
SET @pc07 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'PC-HP-004');
SET @pc08 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'PC-LEN-001');
SET @pc09 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'PC-LEN-002');
SET @pc10 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'PC-ASUS-001');
SET @pc11 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'PC-ACER-001');
SET @pc12 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'PC-ACER-002');

-- Impresoras → Driver HP + Firmware Canon + HP Printer Software
SET @imp01 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'IMP-HP-001');
SET @imp02 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'IMP-HP-002');
SET @imp03 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'IMP-CAN-001');
SET @imp04 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'IMP-EPS-001');
SET @imp05 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'IMP-BRO-001');

-- Celulares → Android 14 + Teams + Zoom
SET @cel01 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'CEL-SAM-001');
SET @cel02 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'CEL-SAM-002');
SET @cel03 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'CEL-MOT-001');
SET @cel04 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'CEL-MOT-002');
SET @cel05 = (SELECT id_equipo FROM equipos_oficina WHERE numero_serie = 'CEL-XIA-001');

-- === Software en Notebooks: Windows 11 + Office 365 + Norton 360 ===
INSERT INTO software_equipo (id_equipo, id_software, version, estado, id_tecnico, fecha_accion) VALUES
(@nb01, @sw_win11,  '22H2',  'Actualizado', @tec_it1, '2023-03-12'),
(@nb01, @sw_office, '16.0',  'Instalado',   @tec_it1, '2023-03-12'),
(@nb01, @sw_norton, '22.24', 'Instalado',   @tec_it1, '2023-03-12'),
(@nb02, @sw_win11,  '22H2',  'Actualizado', @tec_it1, '2023-03-12'),
(@nb02, @sw_office, '16.0',  'Instalado',   @tec_it1, '2023-03-12'),
(@nb03, @sw_win11,  '23H2',  'Actualizado', @tec_it2, '2023-05-17'),
(@nb03, @sw_office, '16.0',  'Instalado',   @tec_it2, '2023-05-17'),
(@nb03, @sw_norton, '22.24', 'Instalado',   @tec_it2, '2023-05-17'),
(@nb04, @sw_win11,  '23H2',  'Actualizado', @tec_it2, '2023-05-17'),
(@nb04, @sw_office, '16.0',  'Instalado',   @tec_it2, '2023-05-17'),
(@nb04, @sw_norton, '22.24', 'Instalado',   @tec_it2, '2023-05-17'),
(@nb05, @sw_office, '16.0',  'Instalado',   @tec_it2, '2022-11-22'),
(@nb05, @sw_norton, '22.20', 'Instalado',   @tec_it2, '2022-11-22'),
(@nb06, @sw_win11,  '23H2',  'Actualizado', @tec_it1, '2023-07-03'),
(@nb06, @sw_office, '16.0',  'Instalado',   @tec_it1, '2023-07-03'),
(@nb06, @sw_norton, '22.24', 'Instalado',   @tec_it1, '2023-07-03'),
(@nb07, @sw_win11,  '23H2',  'Actualizado', @tec_it1, '2023-07-03'),
(@nb07, @sw_office, '16.0',  'Instalado',   @tec_it1, '2023-07-03'),
(@nb07, @sw_norton, '22.24', 'Instalado',   @tec_it1, '2023-07-03'),
(@nb08, @sw_win11,  '23H2',  'Actualizado', @tec_it2, '2023-09-14'),
(@nb08, @sw_norton, '22.24', 'Instalado',   @tec_it2, '2023-09-14'),
(@nb09, @sw_win11,  '21H2',  'Instalado',   @tec_it1, '2021-07-02'),
(@nb09, @sw_office, '15.0',  'Instalado',   @tec_it1, '2021-07-02'),
(@nb09, @sw_norton, '22.10', 'Instalado',   @tec_it1, '2021-07-02'),
(@nb10, @sw_win11,  '23H2',  'Actualizado', @tec_it2, '2024-01-10'),
(@nb10, @sw_norton, '22.24', 'Instalado',   @tec_it2, '2024-01-10');

-- === Software en PC Escritorio: Windows 10 + LibreOffice + Chrome ===
INSERT INTO software_equipo (id_equipo, id_software, version, estado, id_tecnico, fecha_accion) VALUES
(@pc01, @sw_win10,   '22H2',  'Actualizado', @tec_it1, '2022-08-16'),
(@pc01, @sw_libreoff,'7.6.0', 'Instalado',   @tec_it1, '2022-08-16'),
(@pc01, @sw_chrome,  '119.0', 'Actualizado', @tec_it1, '2022-08-16'),
(@pc02, @sw_win10,   '22H2',  'Actualizado', @tec_it1, '2022-08-16'),
(@pc02, @sw_chrome,  '119.0', 'Actualizado', @tec_it1, '2022-08-16'),
(@pc03, @sw_win10,   '21H2',  'Instalado',   @tec_it1, '2022-08-16'),
(@pc03, @sw_libreoff,'7.5.0', 'Instalado',   @tec_it1, '2022-08-16'),
(@pc03, @sw_chrome,  '115.0', 'Instalado',   @tec_it1, '2022-08-16'),
(@pc04, @sw_win10,   '22H2',  'Actualizado', @tec_it2, '2023-01-22'),
(@pc04, @sw_libreoff,'7.6.0', 'Instalado',   @tec_it2, '2023-01-22'),
(@pc04, @sw_chrome,  '120.0', 'Actualizado', @tec_it2, '2023-01-22'),
(@pc05, @sw_win10,   '22H2',  'Actualizado', @tec_it2, '2023-01-22'),
(@pc05, @sw_libreoff,'7.6.0', 'Instalado',   @tec_it2, '2023-01-22'),
(@pc06, @sw_win10,   '22H2',  'Actualizado', @tec_it1, '2023-04-07'),
(@pc06, @sw_libreoff,'7.6.0', 'Instalado',   @tec_it1, '2023-04-07'),
(@pc06, @sw_chrome,  '120.0', 'Actualizado', @tec_it1, '2023-04-07'),
(@pc07, @sw_win10,   '21H1',  'Instalado',   @tec_it1, '2021-03-20'),
(@pc07, @sw_libreoff,'7.0.0', 'Instalado',   @tec_it1, '2021-03-20'),
(@pc07, @sw_chrome,  '89.0',  'Instalado',   @tec_it1, '2021-03-20'),
(@pc08, @sw_win10,   '22H2',  'Actualizado', @tec_it2, '2023-06-24'),
(@pc08, @sw_libreoff,'7.6.0', 'Instalado',   @tec_it2, '2023-06-24'),
(@pc08, @sw_chrome,  '120.0', 'Actualizado', @tec_it2, '2023-06-24'),
(@pc09, @sw_win10,   '22H2',  'Actualizado', @tec_it2, '2023-06-24'),
(@pc09, @sw_libreoff,'7.6.0', 'Instalado',   @tec_it2, '2023-06-24'),
(@pc09, @sw_chrome,  '120.0', 'Actualizado', @tec_it2, '2023-06-24'),
(@pc10, @sw_win10,   '22H2',  'Actualizado', @tec_it1, '2023-10-17'),
(@pc10, @sw_libreoff,'7.6.0', 'Instalado',   @tec_it1, '2023-10-17'),
(@pc10, @sw_chrome,  '119.0', 'Actualizado', @tec_it1, '2023-10-17'),
(@pc11, @sw_win10,   '22H2',  'Actualizado', @tec_it2, '2022-12-03'),
(@pc11, @sw_libreoff,'7.6.0', 'Instalado',   @tec_it2, '2022-12-03'),
(@pc11, @sw_chrome,  '108.0', 'Instalado',   @tec_it2, '2022-12-03'),
(@pc12, @sw_libreoff,'7.4.0', 'Instalado',   @tec_it2, '2022-12-03'),
(@pc12, @sw_chrome,  '108.0', 'Instalado',   @tec_it2, '2022-12-03');

-- === Software en Impresoras: Driver HP + Firmware Canon + HP Printer SW ===
INSERT INTO software_equipo (id_equipo, id_software, version, estado, id_tecnico, fecha_accion) VALUES
(@imp01, @sw_driver_hp, '61.0',  'Actualizado', @tec_mant2, '2022-05-12'),
(@imp01, @sw_adobe,     '23.0',  'Instalado',   @tec_mant2, '2022-05-12'),
(@imp01, @sw_hp_print,  '40.0',  'Instalado',   @tec_mant2, '2022-05-12'),
(@imp02, @sw_driver_hp, '61.0',  'Instalado',   @tec_mant2, '2022-05-12'),
(@imp02, @sw_hp_print,  '40.0',  'Instalado',   @tec_mant2, '2022-05-12'),
(@imp03, @sw_fw_canon,  '3.09',  'Actualizado', @tec_it2,   '2023-03-01'),
(@imp03, @sw_adobe,     '23.0',  'Instalado',   @tec_it2,   '2023-03-01'),
(@imp03, @sw_hp_print,  '40.0',  'Instalado',   @tec_it2,   '2023-03-01'),
(@imp04, @sw_driver_hp, '61.0',  'Instalado',   @tec_it1,   '2023-08-09'),
(@imp04, @sw_adobe,     '23.0',  'Instalado',   @tec_it1,   '2023-08-09'),
(@imp04, @sw_hp_print,  '40.0',  'Instalado',   @tec_it1,   '2023-08-09'),
(@imp05, @sw_fw_canon,  '2.05',  'Instalado',   @tec_mant2, '2021-11-17'),
(@imp05, @sw_adobe,     '21.0',  'Instalado',   @tec_mant2, '2021-11-17'),
(@imp05, @sw_hp_print,  '38.0',  'Instalado',   @tec_mant2, '2021-11-17');

-- === Software en Celulares: Android 14 + Teams + Zoom ===
INSERT INTO software_equipo (id_equipo, id_software, version, estado, id_tecnico, fecha_accion) VALUES
(@cel01, @sw_android14, '14.0',  'Actualizado', @tec_mant1, '2023-04-22'),

(@cel01, @sw_zoom, '5.16',  'Instalado',   @tec_mant1, '2023-04-22'),
(@cel02, @sw_android14, '14.0',  'Actualizado', @tec_mant2, '2023-04-22'),
(@cel02, @sw_zoom, '5.16', 'Instalado', @tec_mant2, '2023-04-22'),
(@cel03, @sw_android14, '14.0', 'Actualizado', @tec_it1, '2023-07-16'),
(@cel03, @sw_teams, '5.17', 'Instalado', @tec_it1, '2023-07-16'),
(@cel03, @sw_zoom, '5.16', 'Instalado',  @tec_it1,  '2023-07-16'),
(@cel04, @sw_android14, '14.0', 'Instalado', @tec_it2, '2023-07-16'),
(@cel04, @sw_teams, '5.15',  'Instalado',  @tec_it2,  '2023-07-16'),
(@cel04, @sw_zoom, '5.14',  'Instalado', @tec_it2,  '2023-07-16'),
(@cel05, @sw_android14, '14.0', 'Actualizado', @adm_inv, '2024-02-03'),
(@cel05, @sw_teams, '5.17', 'Instalado', @adm_inv, '2024-02-03');
