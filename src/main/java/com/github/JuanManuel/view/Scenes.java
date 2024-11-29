package com.github.JuanManuel.view;

public enum Scenes {
    /*
    Pantallas:
        -WELCOME
        -LOG IN
        -REGISTER/SIGN IN
        -HOME
    */
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

    // PAGINAS ADMIN

    WELCOME("view/Welcome.fxml"); //ELEGIR ROL


    private final String url;

    Scenes(String url) {
        this.url = url;
    }

    public String getURL() {
        return url;
    }
}
