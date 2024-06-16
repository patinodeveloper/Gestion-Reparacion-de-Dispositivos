-- Creación de la base de datos
CREATE DATABASE sis_gestion_rep;
USE sis_gestion_rep;

-- Tabla usuarios
CREATE TABLE usuarios(
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    contrasena VARCHAR(255)
);

-- Tabla clientes
CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(255)
);

-- Tabla tipos de dispositivos
CREATE TABLE tipos_dispositivos (
    id_tipo_dispositivo INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(100) UNIQUE
);

-- Tabla dispositivos
CREATE TABLE dispositivos (
    id_dispositivo INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_tipo_dispositivo INT NOT NULL,
    marca VARCHAR(100),
    modelo VARCHAR(100),
    numero_serie VARCHAR(50),
    color VARCHAR(30),
    problema VARCHAR(255),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente),
    FOREIGN KEY (id_tipo_dispositivo) REFERENCES tipos_dispositivos(id_tipo_dispositivo)
);

-- Tabla reparaciones
CREATE TABLE reparaciones (
    id_reparacion INT AUTO_INCREMENT PRIMARY KEY,
    id_dispositivo INT NOT NULL,
    servicio VARCHAR(255) NOT NULL,
    costo DECIMAL(10, 2) NOT NULL,
    fecha_recepcion DATE NOT NULL,
    fecha_entrega DATE,
    estado ENUM('Pendiente', 'Completado', 'Entregado') DEFAULT 'Pendiente',
    estado_pago ENUM('No Pagado', 'Pago Parcial', 'Pagado') DEFAULT 'No Pagado',
    FOREIGN KEY (id_dispositivo) REFERENCES dispositivos(id_dispositivo)
);

-- Tabla pagos
CREATE TABLE pagos (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_reparacion INT NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    fecha_pago DATE NOT NULL,
    metodo_pago ENUM('Efectivo', 'Depósito', 'Transferencia') NOT NULL,
    FOREIGN KEY (id_reparacion) REFERENCES reparaciones(id_reparacion)
);