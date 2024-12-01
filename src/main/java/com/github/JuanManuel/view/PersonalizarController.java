package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.*;
import com.github.JuanManuel.model.entity.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

public class PersonalizarController extends Controller implements Initializable {
    @FXML
    public Button exitButton;
    @FXML
    public Button cartButton;
    @FXML
    public ChoiceBox typeChoice;
    @FXML
    public ComboBox flPRChoice;
    @FXML
    public ComboBox flSecunChoice3;
    @FXML
    public ComboBox flSecunChoice1;
    @FXML
    public ComboBox flSecunChoice2;
    @FXML
    public ComboBox sizeChoice;
    @FXML
    public Button buyButton;
    @FXML
    public ChoiceBox colorChoice;
    @FXML
    public ImageView previewImage;

    private static String tipo;
    private static Image iconoCentro = new Image(PersonalizarController.class.getResource("/com/github/JuanManuel/view/images/iconoCentro.png").toExternalForm());
    private static Image iconoRamo = new Image(PersonalizarController.class.getResource("/com/github/JuanManuel/view/images/iconoRamo.png").toExternalForm());

    private static Ramo ramo = new Ramo();
    private static Centro centro = new Centro();
    private static int firstController;
    private static int secunController;
    private static String frase;
    private static Double precioTotal = 20.5;

    @Override
    public void onOpen(Object input) throws Exception {
        secunController = 0;
        frase = "";
    }


    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Obtener las listas de flores
        List<Flor> florPrin = florDAO.build().findByType(true);
        List<Flor> floresSecun = florDAO.build().findByType(false);

        // Configurar el ChoiceBox de tipo
        typeChoice.setItems(observableArrayList("Centro", "Ramo"));
        typeChoice.setValue("Centro");
        actualizarTipo(null);
        typeChoice.setOnAction(this::actualizarTipo);
        sizeChoice.setItems(observableArrayList("Pequeño (20cm)", "Mediano (30cm)", "Grande (40cm)"));
        sizeChoice.setValue("Pequeño (20cm)");
        sizeChoice.setOnAction(this::actualizarValores);
        colorChoice.setItems(FXCollections.observableArrayList(
                "Rojo", "Verde", "Azul", "Amarillo", "Negro", "Blanco", "Naranja", "Morado",
                "Rosa", "Gris", "Cian", "Turquesa", "Marrón", "Violeta", "Fucsia", "Oliva",
                "Oro", "Plata", "Beige", "Índigo", "Transparente"
        ));
        colorChoice.setValue("Transparente");
        colorChoice.setOnAction(this::actualizarValores);


        initializaComboBoc(flPRChoice, florPrin);
        initializaComboBoc(flSecunChoice1, floresSecun);
        initializaComboBoc(flSecunChoice2, floresSecun);
        initializaComboBoc(flSecunChoice3, floresSecun);

    }

    private void actualizarValores(Event event) {
        try {

            Flor flPR = (Flor) flPRChoice.getValue();
            List<Flor> floresSecun = new ArrayList<>();
            Flor fSecun_1 = (Flor) flSecunChoice1.getValue();
            Flor fSecun_2 = (Flor) flSecunChoice2.getValue();
            Flor fSecun_3 = (Flor) flSecunChoice3.getValue();
            if (fSecun_1.equals(fSecun_2) || fSecun_1.equals(fSecun_3) || fSecun_3.equals(fSecun_2)) {
                Alerta.showAlert("ERROR", "Flores repetidas", "Las flores de relleno no pueden ser iguales, tienen que ser 3 distintas");
                return;
            } else {
                floresSecun.add(fSecun_1);
                floresSecun.add(fSecun_2);
                floresSecun.add(fSecun_3);
            }
            String size = "Pequeño";
            if (sizeChoice.getValue() != null) {
                size = sizeChoice.getValue().toString();
            }
            String color = colorChoice.getValue().toString();
            int numFlores = 3;
            if (tipo == "Ramo") {
                numFlores = Integer.parseInt(size.substring(0, 2).trim());
            }
            ramo = new Ramo(searchID(), "ramo personalizado " + firstController + " " + Session.getInstance().getCurrentUser().getName(), precioTotal, 10, "Ramo personalizado de " + Session.getInstance().getCurrentUser().getName(), new File("src/main/resources/com/github/JuanManuel/view/images/iconoCentro.png"), flPR, numFlores, color, floresSecun);
            centro = new Centro(searchID(), "centro personalizado " + firstController + " " + Session.getInstance().getCurrentUser().getName(), precioTotal, 10, "Centro personalizado  de " + Session.getInstance().getCurrentUser().getName(), new File("src/main/resources/com/github/JuanManuel/view/images/iconoCentro.png"), flPR, size, frase, floresSecun);
            System.out.println(ramo);
            System.out.println(centro);
        } catch (Exception e) {
            System.out.println("Y si el error son los amigos que hacemos en el camino?");
            throw new RuntimeException(e);
        }
    }

    public void initializaComboBoc(ComboBox c, List<Flor> flores) {
        c.setItems(observableArrayList(flores));
        c.setCellFactory(lv -> new ListCell<Flor>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Flor flor, boolean empty) {
                super.updateItem(flor, empty);

                if (empty || flor == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(flor.getNombre());
                    imageView.setImage(new Image(new ByteArrayInputStream(flor.getImg())));
                    imageView.setFitHeight(20); // Ajusta el tamaño de la imagen
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                }
            }
        });

        // Configurar el renderizado del elemento seleccionado
        c.setButtonCell((ListCell) c.getCellFactory().call(null));
        // Seleccionar un valor inicial
        if (!flores.isEmpty()) {
            c.setValue(flores.get(secunController));
            secunController++;
        }
        c.setOnAction(this::actualizarValores);
    }

    public void buy(ActionEvent actionEvent) {
        Flor fSecun_1 = (Flor) flSecunChoice1.getValue();
        Flor fSecun_2 = (Flor) flSecunChoice2.getValue();
        Flor fSecun_3 = (Flor) flSecunChoice3.getValue();
        if (fSecun_1.equals(fSecun_2) || fSecun_1.equals(fSecun_3) || fSecun_3.equals(fSecun_2)) {
            Alerta.showAlert("ERROR", "Flores repetidas", "Las flores de relleno no pueden ser iguales, tienen que ser 3 distintas");
            return;
        }
        System.out.println(inputFrase());
        actualizarValores(null);

        switch (tipo) {
            case "Centro":
                centroDAO.build().save(centro);
                Session.getInstance().addDetalle(centro, 1);
                break;
            case "Ramo":
                ramoDAO.build().save(ramo);
                Session.getInstance().addDetalle(ramo, 1);
            break;
            default:
                break;
        }
        firstController++;
        goToCart(actionEvent);
    }

    private void actualizarTipo(Event event) {
        tipo = typeChoice.getValue().toString();
        switch (tipo) {
            case "Centro":
                previewImage.setImage(iconoCentro);
                sizeChoice.setValue("Pequeño (20cm)");
                sizeChoice.setItems(observableArrayList("Pequeño (20cm)", "Mediano (30cm)", "Grande (40cm)"));
                sizeChoice.setValue("Pequeño (20cm)");
                break;
            case "Ramo":
                sizeChoice.setValue("3 Flores");
                sizeChoice.setItems(observableArrayList("3 Flores", "5 Flores", "8 Flores", "10 Flores", "15 Flores"));
                previewImage.setImage(iconoRamo);
                sizeChoice.setValue("3 Flores");
                break;
            default:
                break;
        }
    }

    public void goToHome(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.HOME, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void goToCart(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.CARRITO, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public String inputFrase() {
        TextInputDialog tid = new TextInputDialog();
        tid.setTitle("Frase dedicatoria");
        tid.setHeaderText("Inserta una frase");
        tid.setContentText("Esta frase se pondrá en una cinta decorativa.\nPuedes dejarla vacía si no quieres poner una frase.\n");
        Optional<String> opt = tid.showAndWait();
        String result = opt.get();
        return result;
    }

    private int searchID() {
        int result = 1;
        List<Producto> productos = productoDAO.build().findAll();
        for (Producto p:productos) {
            if (p.getIdProducto() == result) {
                result++;
            } else {
                break;
            }
        }
        System.out.println("Nueva id: " + result);
        return result;
    }
}
