-- Eliminar tablas si existen
DROP TABLE IF EXISTS "ramoflores";
DROP TABLE IF EXISTS "ramo";
DROP TABLE IF EXISTS "pedido_producto";
DROP TABLE IF EXISTS "centroflores";
DROP TABLE IF EXISTS "centro";
DROP TABLE IF EXISTS "flor";
DROP TABLE IF EXISTS "pedido";
DROP TABLE IF EXISTS "producto";
DROP TABLE IF EXISTS "usuario";

-- Crear tablas
CREATE TABLE "centro" (
  "idCentro" INT NOT NULL,
  "florPrincipal" INT NOT NULL,
  "tamaño" VARCHAR(45) NOT NULL,
  "frase" VARCHAR(100),
  PRIMARY KEY ("idCentro")
);

CREATE TABLE "centroflores" (
  "Flor_idFlor" INT NOT NULL,
  "Centro_idCentro" INT NOT NULL,
  PRIMARY KEY ("Flor_idFlor", "Centro_idCentro")
);

CREATE TABLE "flor" (
  "idFlor" INT NOT NULL,
  "color" VARCHAR(45) NOT NULL,
  "tipo" SMALLINT NOT NULL,
  PRIMARY KEY ("idFlor")
);

CREATE TABLE "pedido" (
  "idPedido" INT NOT NULL,
  "fechaPedido" VARCHAR(45) NOT NULL,
  "fechaEntrega" VARCHAR(45) NOT NULL,
  "total" DECIMAL(10, 2) NOT NULL,
  "estado" VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
  "telefonoUsuario" VARCHAR(9) NOT NULL,
  PRIMARY KEY ("idPedido")
);

CREATE TABLE "pedido_producto" (
  "Pedido_idPedido" INT NOT NULL,
  "Producto_idProducto" INT NOT NULL,
  "cantidad" INT NOT NULL,
  "subtotal" DECIMAL(10, 2) NOT NULL,
  PRIMARY KEY ("Pedido_idPedido", "Producto_idProducto")
);

CREATE TABLE "producto" (
  "idProducto" INT NOT NULL,
  "nombre" VARCHAR(45) NOT NULL,
  "precio" DECIMAL(10, 2) NOT NULL,
  "stock" INT NOT NULL,
  "tipo" VARCHAR(200) NOT NULL DEFAULT 'complemento',
  "description" VARCHAR(200) NOT NULL,
  "imagen" BLOB,
  PRIMARY KEY ("idProducto")
);

CREATE TABLE "ramo" (
  "idRamo" INT NOT NULL,
  "florPrincipal" INT NOT NULL,
  "cantidadFlores" INT NOT NULL,
  "colorEnvoltorio" VARCHAR(45),
  PRIMARY KEY ("idRamo")
);

CREATE TABLE "ramoflores" (
  "Flor_idFlor" INT NOT NULL,
  "Ramo_idRamo" INT NOT NULL,
  PRIMARY KEY ("Flor_idFlor", "Ramo_idRamo")
);

CREATE TABLE "usuario" (
  "telefono" VARCHAR(9) NOT NULL,
  "nombre" VARCHAR(100) NOT NULL,
  "contraseña" TEXT NOT NULL,
  "isAdmin" SMALLINT NOT NULL DEFAULT 0,
  PRIMARY KEY ("telefono")
);

-- Relaciones (claves foráneas)
ALTER TABLE "centroflores"
  ADD FOREIGN KEY ("Centro_idCentro") REFERENCES "centro" ("idCentro") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "centroflores"
  ADD FOREIGN KEY ("Flor_idFlor") REFERENCES "flor" ("idFlor") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "pedido"
  ADD FOREIGN KEY ("telefonoUsuario") REFERENCES "usuario" ("telefono") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "pedido_producto"
  ADD FOREIGN KEY ("Pedido_idPedido") REFERENCES "pedido" ("idPedido") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "pedido_producto"
  ADD FOREIGN KEY ("Producto_idProducto") REFERENCES "producto" ("idProducto") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "ramo"
  ADD FOREIGN KEY ("florPrincipal") REFERENCES "flor" ("idFlor") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "ramoflores"
  ADD FOREIGN KEY ("Flor_idFlor") REFERENCES "flor" ("idFlor") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "ramoflores"
  ADD FOREIGN KEY ("Ramo_idRamo") REFERENCES "ramo" ("idRamo") ON DELETE CASCADE ON UPDATE CASCADE;
