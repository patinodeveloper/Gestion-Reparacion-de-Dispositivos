-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.11.2-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para sis_gestion_rep
CREATE DATABASE IF NOT EXISTS `sis_gestion_rep` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci */;
USE `sis_gestion_rep`;

-- Volcando estructura para procedimiento sis_gestion_rep.actualizar_estado_pago
DELIMITER //
CREATE PROCEDURE `actualizar_estado_pago`(IN id_reparacion INT)
BEGIN
    DECLARE total_pagado DECIMAL(10, 2);

    -- Calcular el total pagado para la reparación específica
    SELECT SUM(monto)
    INTO total_pagado
    FROM pagos
    WHERE id_reparacion = id_reparacion;

    -- Actualizar el estado de pago basado en el total pagado
    UPDATE reparaciones
    SET estado_pago = CASE
        WHEN total_pagado IS NULL OR total_pagado = 0 THEN 'No Pagado'
        WHEN total_pagado < costo THEN 'Pago Parcial'
        ELSE 'Pagado'
    END
    WHERE id_reparacion = id_reparacion;
END//
DELIMITER ;

-- Volcando estructura para tabla sis_gestion_rep.clientes
CREATE TABLE IF NOT EXISTS `clientes` (
  `id_cliente` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_cliente`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla sis_gestion_rep.clientes: ~3 rows (aproximadamente)
DELETE FROM `clientes`;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` (`id_cliente`, `nombre`, `telefono`, `direccion`) VALUES
	(1, 'Daniela Yamilet', '8979790832', 'Aviación, Ébano, SLP'),
	(2, 'Cliente Frecuente', '8451001010', 'Boulevard Principal, Zona Centro'),
	(3, 'Antonio Patoño', '4811018619', 'Av. Manuel C. Larraga #69, Col. Antonio J Bermudez');
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;

-- Volcando estructura para tabla sis_gestion_rep.dispositivos
CREATE TABLE IF NOT EXISTS `dispositivos` (
  `id_dispositivo` int(11) NOT NULL AUTO_INCREMENT,
  `id_cliente` int(11) NOT NULL,
  `id_tipo_dispositivo` int(11) NOT NULL,
  `marca` varchar(100) DEFAULT NULL,
  `modelo` varchar(100) DEFAULT NULL,
  `numero_serie` varchar(50) DEFAULT NULL,
  `color` varchar(30) DEFAULT NULL,
  `problema` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_dispositivo`),
  KEY `id_cliente` (`id_cliente`),
  KEY `id_tipo_dispositivo` (`id_tipo_dispositivo`),
  CONSTRAINT `dispositivos_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`),
  CONSTRAINT `dispositivos_ibfk_2` FOREIGN KEY (`id_tipo_dispositivo`) REFERENCES `tipos_dispositivos` (`id_tipo_dispositivo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla sis_gestion_rep.dispositivos: ~3 rows (aproximadamente)
DELETE FROM `dispositivos`;
/*!40000 ALTER TABLE `dispositivos` DISABLE KEYS */;
INSERT INTO `dispositivos` (`id_dispositivo`, `id_cliente`, `id_tipo_dispositivo`, `marca`, `modelo`, `numero_serie`, `color`, `problema`) VALUES
	(1, 1, 1, 'Samsung', 'Galaxy S21', 'SN123456', 'Negro', 'Pantalla rota'),
	(2, 2, 2, 'Apple', 'iPad Pro', 'SN654321', 'Blanco', 'Problema con la batería'),
	(3, 3, 3, 'HP', 'Pavilion', 'SN789012', 'Gris', 'No enciende');
/*!40000 ALTER TABLE `dispositivos` ENABLE KEYS */;

-- Volcando estructura para tabla sis_gestion_rep.pagos
CREATE TABLE IF NOT EXISTS `pagos` (
  `id_pago` int(11) NOT NULL AUTO_INCREMENT,
  `id_reparacion` int(11) NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `fecha_pago` date NOT NULL,
  `metodo_pago` enum('Efectivo','Depósito','Transferencia') NOT NULL,
  PRIMARY KEY (`id_pago`),
  KEY `id_reparacion` (`id_reparacion`),
  CONSTRAINT `pagos_ibfk_1` FOREIGN KEY (`id_reparacion`) REFERENCES `reparaciones` (`id_reparacion`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla sis_gestion_rep.pagos: ~3 rows (aproximadamente)
DELETE FROM `pagos`;
/*!40000 ALTER TABLE `pagos` DISABLE KEYS */;
INSERT INTO `pagos` (`id_pago`, `id_reparacion`, `monto`, `fecha_pago`, `metodo_pago`) VALUES
	(1, 1, 100.00, '2024-06-05', 'Efectivo'),
	(2, 1, 50.00, '2024-06-10', 'Transferencia'),
	(3, 2, 100.00, '2024-06-10', 'Efectivo');
/*!40000 ALTER TABLE `pagos` ENABLE KEYS */;

-- Volcando estructura para tabla sis_gestion_rep.reparaciones
CREATE TABLE IF NOT EXISTS `reparaciones` (
  `id_reparacion` int(11) NOT NULL AUTO_INCREMENT,
  `id_dispositivo` int(11) NOT NULL,
  `servicio` varchar(255) NOT NULL,
  `costo` decimal(10,2) NOT NULL,
  `fecha_recepcion` date NOT NULL,
  `fecha_entrega` date DEFAULT NULL,
  `estado` enum('Pendiente','Completado','Entregado') DEFAULT 'Pendiente',
  `estado_pago` enum('No Pagado','Pago Parcial','Pagado') DEFAULT 'No Pagado',
  PRIMARY KEY (`id_reparacion`),
  KEY `id_dispositivo` (`id_dispositivo`),
  CONSTRAINT `reparaciones_ibfk_1` FOREIGN KEY (`id_dispositivo`) REFERENCES `dispositivos` (`id_dispositivo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla sis_gestion_rep.reparaciones: ~3 rows (aproximadamente)
DELETE FROM `reparaciones`;
/*!40000 ALTER TABLE `reparaciones` DISABLE KEYS */;
INSERT INTO `reparaciones` (`id_reparacion`, `id_dispositivo`, `servicio`, `costo`, `fecha_recepcion`, `fecha_entrega`, `estado`, `estado_pago`) VALUES
	(1, 1, 'Reemplazo de pantalla', 150.00, '2024-06-01', '2024-06-10', 'Completado', 'Pago Parcial'),
	(2, 2, 'Reemplazo de batería', 200.00, '2024-06-02', '2024-06-11', 'Completado', 'No Pagado'),
	(3, 3, 'Reparación de placa madre', 300.00, '2024-06-03', '2024-06-12', 'Pendiente', 'No Pagado');
/*!40000 ALTER TABLE `reparaciones` ENABLE KEYS */;

-- Volcando estructura para tabla sis_gestion_rep.tipos_dispositivos
CREATE TABLE IF NOT EXISTS `tipos_dispositivos` (
  `id_tipo_dispositivo` int(11) NOT NULL AUTO_INCREMENT,
  `tipo` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_tipo_dispositivo`),
  UNIQUE KEY `tipo` (`tipo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla sis_gestion_rep.tipos_dispositivos: ~3 rows (aproximadamente)
DELETE FROM `tipos_dispositivos`;
/*!40000 ALTER TABLE `tipos_dispositivos` DISABLE KEYS */;
INSERT INTO `tipos_dispositivos` (`id_tipo_dispositivo`, `tipo`) VALUES
	(1, 'Celular'),
	(3, 'Laptop'),
	(2, 'Tablet');
/*!40000 ALTER TABLE `tipos_dispositivos` ENABLE KEYS */;

-- Volcando estructura para tabla sis_gestion_rep.usuarios
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `contrasena` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla sis_gestion_rep.usuarios: ~1 rows (aproximadamente)
DELETE FROM `usuarios`;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` (`id_usuario`, `nombre`, `email`, `contrasena`) VALUES
	(1, 'Antonio Patiño', 'patino10@gmail.com', 'root');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;

-- Volcando estructura para vista sis_gestion_rep.vista_estado_pago
-- Creando tabla temporal para superar errores de dependencia de VIEW
CREATE TABLE `vista_estado_pago` (
	`id_reparacion` INT(11) NOT NULL,
	`id_dispositivo` INT(11) NOT NULL,
	`servicio` VARCHAR(255) NOT NULL COLLATE 'latin1_swedish_ci',
	`costo` DECIMAL(10,2) NOT NULL,
	`fecha_recepcion` DATE NOT NULL,
	`fecha_entrega` DATE NULL,
	`estado` ENUM('Pendiente','Completado','Entregado') NULL COLLATE 'latin1_swedish_ci',
	`total_pagado` DECIMAL(32,2) NULL,
	`estado_pago` VARCHAR(12) NULL COLLATE 'utf8mb4_general_ci'
) ENGINE=MyISAM;

-- Volcando estructura para vista sis_gestion_rep.vista_estado_pago
-- Eliminando tabla temporal y crear estructura final de VIEW
DROP TABLE IF EXISTS `vista_estado_pago`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vista_estado_pago` AS SELECT 
    r.id_reparacion,
    r.id_dispositivo,
    r.servicio,
    r.costo,
    r.fecha_recepcion,
    r.fecha_entrega,
    r.estado,
    COALESCE(SUM(p.monto), 0) AS total_pagado,
    CASE 
        WHEN COALESCE(SUM(p.monto), 0) = 0 THEN 'No Pagado'
        WHEN COALESCE(SUM(p.monto), 0) < r.costo THEN 'Pago Parcial'
        ELSE 'Pagado'
    END AS estado_pago
FROM 
    reparaciones r
LEFT JOIN 
    pagos p 
ON 
    r.id_reparacion = p.id_reparacion
GROUP BY 
    r.id_reparacion, 
    r.id_dispositivo,
    r.servicio,
    r.costo,
    r.fecha_recepcion,
    r.fecha_entrega,
    r.estado ;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
