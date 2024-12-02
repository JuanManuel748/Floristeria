package com.github.JuanManuel.view;

public enum Scenes {
    ROOT("view/layout.fxml"),//PAGINA PRINCIPAL
    //MAINPAGE("view/mainPage.fxml"), //PAGINA PRINCIPAL
    LOGIN("view/login.fxml"), // PAGINA LOGIN
    REGISTER("view/register.fxml"), // PAGINA SIGN IN

    // PAGINAS USUARIO
    HOME("view/home.fxml"), // PAGINA HOME
    CENTROS("view/centros.fxml"), // PAGINA CON LOS CENTROS
    RAMOS("view/ramos.fxml"), // PAGINA CON LOS RAMOS
    PRODUCTOS("view/productos.fxml"), // PAGINA CON LOS PRODUCTOS
    PERSONALIZAR("view/personalizar.fxml"), // PAGINA PARA PERSONALIZAR UN CENTRO/RAMO
    CARRITO("view/shoppingCart.fxml"), // PAGINA CON UNA VISTA PREVIA DE LOS PRODUCTOS QUE SE VAN A COMPRAR
    DETAILS("view/details.fxml"), // PAGINA CON LOS DETALLES DEL PRODUCTO SELECCIONADO EN EL CARRITO

    // PAGINAS ADMIN
    ADMINHOME("view/admin/adminHome.fxml"), // PAGINA CON LAS DISTINTAS OPCIONES
    ADMINUSERS("view/admin/adminUsers.fxml"), // PAGINA DONDE SE PUEDEN EDITAR LOS USUARIOS
    ADMINPEDIDOS("view/admin/adminPedidos.fxml"), // PAGINA DONDE SE PUEDEN EDITAR LOS PEDIDOS
    ADMINDETAILS("view/admin/adminDetails.fxml"), // PAGINA DONDE SE PUEDEN EDITAR LOS DETALLES DE LOS PEDIDOS
    ADMINPRODUCTOS("view/admin/adminProductos.fxml"), // PAGINA DONDE SE PUEDEN EDITAR LOS PRODUCTOS
    ADMINRAMOS("view/admin/adminRamos.fxml"), // PAGINA DONDE SE PUEDEN EDITAR LOS RAMOS
    ADMINCENTROS("view/admin/adminCentros.fxml"), // PAGINA DONDE SE PUEDEN EDITAR LOS CENTROS
    ADMINFLORES("view/admin/adminFlores.fxml"), // PAGINA DONDE SE PUEDEN EDITAR LAS FLORES

    WELCOME("view/Welcome.fxml"); //ELEGIR ROL


    private final String url;

    Scenes(String url) {
        this.url = url;
    }

    public String getURL() {
        return url;
    }
}
