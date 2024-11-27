-- MySQL Workbench Synchronization
-- Generated: 2024-11-27 21:34
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: junpe

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `floReyesDB` DEFAULT CHARACTER SET utf8;

CREATE TABLE IF NOT EXISTS `floReyesDB`.`Usuario` (
  `telefono` VARCHAR(9) NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `contraseña` LONGTEXT NOT NULL,
  PRIMARY KEY (`telefono`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `floReyesDB`.`Pedido` (
  `idPedido` INT(11) NOT NULL,
  `fechaPedido` VARCHAR(45) NOT NULL,
  `fechaEntrega` VARCHAR(45) NOT NULL,
  `total` DECIMAL(10,2) NOT NULL,
  `estado` ENUM('PAGADO', 'PENDIENTE') NOT NULL DEFAULT 'PENDIENTE',
  `telefonoUsuario` VARCHAR(9) NOT NULL,
  PRIMARY KEY (`idPedido`),
  INDEX `fk_Pedido_Usuario_idx` (`telefonoUsuario` ASC),
  CONSTRAINT `fk_Pedido_Usuario`
    FOREIGN KEY (`telefonoUsuario`)
    REFERENCES `floReyesDB`.`Usuario` (`telefono`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `floReyesDB`.`Producto` (
  `idProducto` INT(11) NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `precio` DECIMAL(10,2) NOT NULL,
  `stock` INT(11) NOT NULL,
  `tipo` ENUM('flor', 'complemento', 'centro', 'ramo') NOT NULL,
  `description` VARCHAR(200) NOT NULL,
  `imagen` LONGBLOB NULL DEFAULT NULL,
  PRIMARY KEY (`idProducto`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `floReyesDB`.`Flor` (
  `idFlor` INT(11) NOT NULL,
  `color` VARCHAR(45) NOT NULL,
  `tipo` TINYINT(4) NOT NULL,
  PRIMARY KEY (`idFlor`),
  CONSTRAINT `fk_Flor_Producto`
    FOREIGN KEY (`idFlor`)
    REFERENCES `floReyesDB`.`Producto` (`idProducto`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `floReyesDB`.`Centro` (
  `idCentro` INT(11) NOT NULL,
  `florPrincipal` INT(11) NOT NULL,
  `tamaño` VARCHAR(45) NOT NULL,
  `frase` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`idCentro`),
  INDEX `fk_Centro_Producto_idx` (`idCentro` ASC),
  INDEX `fk_Centro_Flor_idx` (`florPrincipal` ASC),
  CONSTRAINT `fk_Centro_Producto`
    FOREIGN KEY (`idCentro`)
    REFERENCES `floReyesDB`.`Producto` (`idProducto`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Centro_Flor`
    FOREIGN KEY (`florPrincipal`)
    REFERENCES `floReyesDB`.`Flor` (`idFlor`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `floReyesDB`.`Ramo` (
  `idRamo` INT(11) NOT NULL,
  `florPrincipal` INT(11) NOT NULL,
  `cantidadFlores` INT(11) NOT NULL,
  `colorEnvoltorio` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idRamo`),
  INDEX `fk_Ramo_Producto_idx` (`idRamo` ASC),
  INDEX `fk_Ramo_Flor_idx` (`florPrincipal` ASC),
  CONSTRAINT `fk_Ramo_Producto`
    FOREIGN KEY (`idRamo`)
    REFERENCES `floReyesDB`.`Producto` (`idProducto`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Ramo_Flor`
    FOREIGN KEY (`florPrincipal`)
    REFERENCES `floReyesDB`.`Flor` (`idFlor`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `floReyesDB`.`RamoFlores` (
  `Flor_idFlor` INT(11) NOT NULL,
  `Ramo_idRamo` INT(11) NOT NULL,
  PRIMARY KEY (`Flor_idFlor`, `Ramo_idRamo`),
  INDEX `fk_RamoFlores_Ramo_idx` (`Ramo_idRamo` ASC),
  INDEX `fk_RamoFlores_Flor_idx` (`Flor_idFlor` ASC),
  CONSTRAINT `fk_RamoFlores_Flor`
    FOREIGN KEY (`Flor_idFlor`)
    REFERENCES `floReyesDB`.`Flor` (`idFlor`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_RamoFlores_Ramo`
    FOREIGN KEY (`Ramo_idRamo`)
    REFERENCES `floReyesDB`.`Ramo` (`idRamo`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `floReyesDB`.`CentroFlores` (
  `Flor_idFlor` INT(11) NOT NULL,
  `Centro_idCentro` INT(11) NOT NULL,
  PRIMARY KEY (`Flor_idFlor`, `Centro_idCentro`),
  INDEX `fk_CentroFlores_Centro_idx` (`Centro_idCentro` ASC),
  INDEX `fk_CentroFlores_Flor_idx` (`Flor_idFlor` ASC),
  CONSTRAINT `fk_CentroFlores_Flor`
    FOREIGN KEY (`Flor_idFlor`)
    REFERENCES `floReyesDB`.`Flor` (`idFlor`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_CentroFlores_Centro`
    FOREIGN KEY (`Centro_idCentro`)
    REFERENCES `floReyesDB`.`Centro` (`idCentro`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `floReyesDB`.`Pedido_Producto` (
  `Pedido_idPedido` INT(11) NOT NULL,
  `Producto_idProducto` INT(11) NOT NULL,
  `cantidad` INT(11) NOT NULL,
  `subtotal` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`Pedido_idPedido`, `Producto_idProducto`),
  INDEX `fk_PedidoProducto_Producto_idx` (`Producto_idProducto` ASC),
  INDEX `fk_PedidoProducto_Pedido_idx` (`Pedido_idPedido` ASC),
  CONSTRAINT `fk_PedidoProducto_Pedido`
    FOREIGN KEY (`Pedido_idPedido`)
    REFERENCES `floReyesDB`.`Pedido` (`idPedido`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_PedidoProducto_Producto`
    FOREIGN KEY (`Producto_idProducto`)
    REFERENCES `floReyesDB`.`Producto` (`idProducto`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
