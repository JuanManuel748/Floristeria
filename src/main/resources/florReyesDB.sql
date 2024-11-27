-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 27-11-2024 a las 10:49:48
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `floreyesdb`
CREATE SCHEMA IF NOT EXISTS `floReyesDB` DEFAULT CHARACTER SET utf8 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `centro`
--

CREATE TABLE `centro` (
  `idCentro` int(11) NOT NULL,
  `florPrincipal` int(11) NOT NULL,
  `tamaño` varchar(45) NOT NULL,
  `frase` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `centroflore`
--

CREATE TABLE `centroflore` (
  `Flor_idFlor` int(11) NOT NULL,
  `Centro_idCentro` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `flor`
--

CREATE TABLE `flor` (
  `idFlor` int(11) NOT NULL,
  `color` varchar(45) NOT NULL,
  `tipo` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Volcado de datos para la tabla `flor`
--


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedido`
--

CREATE TABLE `pedido` (
  `idPedido` int(11) NOT NULL,
  `fechaPedido` varchar(45) NOT NULL,
  `fechaEntrega` varchar(45) NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `estado` enum('PAGADO','PENDIENTE') NOT NULL DEFAULT 'PENDIENTE',
  `telefonoUsuario` varchar(9) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Volcado de datos para la tabla `pedido`
--


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedido_producto`
--

CREATE TABLE `pedido_producto` (
  `Pedido_idPedido` int(11) NOT NULL,
  `Producto_idProducto` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

CREATE TABLE `producto` (
  `idProducto` int(11) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `stock` int(11) NOT NULL,
  `tipo` enum('flor','complemento','centro','ramo') NOT NULL,
  `description` varchar(200) NOT NULL,
  `imagen` longblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Volcado de datos para la tabla `producto`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ramo`
--

CREATE TABLE `ramo` (
  `idRamo` int(11) NOT NULL,
  `florPrincipal` int(11) NOT NULL,
  `cantidadFlores` int(11) NOT NULL,
  `colorEnvoltorio` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ramoflores`
--

CREATE TABLE `ramoflores` (
  `Flor_idFlor` int(11) NOT NULL,
  `Ramo_idRamo` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `telefono` varchar(9) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `contraseña` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--


--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `centro`
--
ALTER TABLE `centro`
  ADD PRIMARY KEY (`idCentro`),
  ADD KEY `idFlor_idx` (`florPrincipal`);

--
-- Indices de la tabla `centroflore`
--
ALTER TABLE `centroflore`
  ADD PRIMARY KEY (`Flor_idFlor`,`Centro_idCentro`),
  ADD KEY `fk_Flor_has_Centro_Centro1_idx` (`Centro_idCentro`),
  ADD KEY `fk_Flor_has_Centro_Flor1_idx` (`Flor_idFlor`);

--
-- Indices de la tabla `flor`
--
ALTER TABLE `flor`
  ADD PRIMARY KEY (`idFlor`);

--
-- Indices de la tabla `pedido`
--
ALTER TABLE `pedido`
  ADD PRIMARY KEY (`idPedido`),
  ADD KEY `idUsuario_idx` (`telefonoUsuario`);

--
-- Indices de la tabla `pedido_producto`
--
ALTER TABLE `pedido_producto`
  ADD PRIMARY KEY (`Pedido_idPedido`,`Producto_idProducto`),
  ADD KEY `fk_Pedido_has_Producto_Producto1_idx` (`Producto_idProducto`),
  ADD KEY `fk_Pedido_has_Producto_Pedido1_idx` (`Pedido_idPedido`);

--
-- Indices de la tabla `producto`
--
ALTER TABLE `producto`
  ADD PRIMARY KEY (`idProducto`);

--
-- Indices de la tabla `ramo`
--
ALTER TABLE `ramo`
  ADD PRIMARY KEY (`idRamo`),
  ADD KEY `idFlor_idx` (`florPrincipal`);

--
-- Indices de la tabla `ramoflores`
--
ALTER TABLE `ramoflores`
  ADD PRIMARY KEY (`Flor_idFlor`,`Ramo_idRamo`),
  ADD KEY `fk_Flor_has_Ramo_Ramo1_idx` (`Ramo_idRamo`),
  ADD KEY `fk_Flor_has_Ramo_Flor1_idx` (`Flor_idFlor`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`telefono`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `centro`
--
ALTER TABLE `centro`
  ADD CONSTRAINT `fk_Centro_Flor` FOREIGN KEY (`florPrincipal`) REFERENCES `flor` (`idFlor`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_Centro_Producto` FOREIGN KEY (`idCentro`) REFERENCES `producto` (`idProducto`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `centroflore`
--
ALTER TABLE `centroflore`
  ADD CONSTRAINT `fk_CentroFlore_Centro` FOREIGN KEY (`Centro_idCentro`) REFERENCES `centro` (`idCentro`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_CentroFlore_Flor` FOREIGN KEY (`Flor_idFlor`) REFERENCES `flor` (`idFlor`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `flor`
--
ALTER TABLE `flor`
  ADD CONSTRAINT `fk_Flor_Producto` FOREIGN KEY (`idFlor`) REFERENCES `producto` (`idProducto`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `pedido`
--
ALTER TABLE `pedido`
  ADD CONSTRAINT `fk_Pedido_Usuario` FOREIGN KEY (`telefonoUsuario`) REFERENCES `usuario` (`telefono`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `pedido_producto`
--
ALTER TABLE `pedido_producto`
  ADD CONSTRAINT `fk_PedidoProducto_Pedido` FOREIGN KEY (`Pedido_idPedido`) REFERENCES `pedido` (`idPedido`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_PedidoProducto_Producto` FOREIGN KEY (`Producto_idProducto`) REFERENCES `producto` (`idProducto`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `ramo`
--
ALTER TABLE `ramo`
  ADD CONSTRAINT `fk_Ramo_Flor` FOREIGN KEY (`florPrincipal`) REFERENCES `flor` (`idFlor`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_Ramo_Producto` FOREIGN KEY (`idRamo`) REFERENCES `producto` (`idProducto`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `ramoflores`
--
ALTER TABLE `ramoflores`
  ADD CONSTRAINT `fk_RamoFlores_Flor` FOREIGN KEY (`Flor_idFlor`) REFERENCES `flor` (`idFlor`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_RamoFlores_Ramo` FOREIGN KEY (`Ramo_idRamo`) REFERENCES `ramo` (`idRamo`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
