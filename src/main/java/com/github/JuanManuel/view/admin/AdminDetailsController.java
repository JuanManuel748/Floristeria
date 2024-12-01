package com.github.JuanManuel.view.admin;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.pedidoDAO;
import com.github.JuanManuel.model.DAO.productoDAO;
import com.github.JuanManuel.model.entity.*;
import com.github.JuanManuel.view.Alerta;
import com.github.JuanManuel.view.Controller;
import com.github.JuanManuel.view.Scenes;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.IntegerStringConverter;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

public class AdminDetailsController extends Controller implements Initializable{
    @FXML
    public TableView<Detalles> productsTable;
    @FXML
    public TableColumn<Detalles, Integer> idColumn;
    @FXML
    public TableColumn<Detalles, byte[]> imgColumn;
    @FXML
    public TableColumn<Detalles, String> nameColumn;
    @FXML
    public TableColumn<Detalles, Double> priceColumn;
    @FXML
    public TableColumn<Detalles, Integer> numColumn;
    @FXML
    public ComboBox productChoice;
    @FXML
    public Spinner quantitySpinner;
    @FXML
    public Button insertButton;
    @FXML
    public Button updateButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button findButton;
    @FXML
    public ImageView previewImage;


    private static Pedido ped;
    private static List<Producto> productoList = productoDAO.build().findAll();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPro().getIdProducto()).asObject());
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPro().getNombre()));
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPro().getPrecio()).asObject());
        numColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCantidad()).asObject());

        imgColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getPro().getImg())
        );
        imgColumn.setCellFactory(col -> new TableCell<Detalles, byte[]>() {
            @Override
            protected void updateItem(byte[] item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        Image image = new Image(new ByteArrayInputStream(item));
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(50);
                        imageView.setFitHeight(50);
                        imageView.setPreserveRatio(true);
                        setGraphic(imageView);
                    } catch (Exception e) {

                        setGraphic(null);
                    }
                }
            }
        });


        productsTable.setOnMouseClicked(event -> handleTableSelection(event));
        initializaComboBoc(productChoice, productoList);
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        quantitySpinner.getValueFactory().setConverter(new IntegerStringConverter());
        mostrarDetalles(ped);
    }

    public void setDetalle(Detalles selected) {
        productChoice.setValue(selected.getPro());
        quantitySpinner.getValueFactory().setValue(selected.getCantidad());
    }

    public void clearFields() {
        productChoice.setValue(productoList.get(0));
        quantitySpinner.getValueFactory().setValue(0);
    }

    public void initializaComboBoc(ComboBox c, List<Producto> array) {
        c.setItems(observableArrayList(array));
        c.setCellFactory(lv -> new ListCell<Producto>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Producto pr, boolean empty) {
                super.updateItem(pr, empty);

                if (empty || pr == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(pr.getNombre());
                    byte[] imgData = pr.getImg();
                    if (imgData != null) {
                        Image image = new Image(new ByteArrayInputStream(imgData));
                        imageView.setImage(image);
                    } else {
                        imageView.setImage(null);
                    }
                    imageView.setFitHeight(20);
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                }
            }
        });

        c.setButtonCell((ListCell) c.getCellFactory().call(null));
        if (!array.isEmpty()) {
            c.setValue(array.get(0));
        }
    }



    public void mostrarDetalles(Pedido pedido) {
        List<Detalles> detallesList = pedido.getDetalles();

        productsTable.getItems().setAll(detallesList);
        productsTable.refresh();
    }

    public boolean validateFields() {
        boolean result = false;
        try {
            Producto pro = (Producto) productChoice.getValue();
            if (pro == null) {
                Alerta.showAlert("ERROR", "Producto no encontrado", "El producto no existe en la base de datos");
                return result;
            }
            int cantidad = (int) quantitySpinner.getValue();
            if (cantidad < 0) {
                Alerta.showAlert("ERROR", "Cantidad inváida", "No puedes poner una cantidad menor que 0");
                return result;
            }
            result = true;
        } catch (Exception e) {

        }
        return result;

    }


    public void insert(ActionEvent actionEvent) {
        clearFields();
    }

    public void update(ActionEvent actionEvent) {
        clearFields();
    }

    public void delete(ActionEvent actionEvent) {
        clearFields();
    }

    public void find(ActionEvent actionEvent) {
        clearFields();
    }

    public static void setPedido(Pedido p) {
        ped = p;
    }

    @Override
    public void onOpen(Object input) throws Exception {

    }

    public void goToHome(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.ADMINHOME, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void logout(ActionEvent actionEvent) {
        try {
            User u = Session.getInstance().getCurrentUser();
            Session.getInstance().logOut(u);
            App.currentController.changeScene(Scenes.LOGIN, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClose(Object output) {

    }

    private void handleTableSelection(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Detalles selected = productsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                setDetalle(selected);
            } else {
                Alerta.showAlert("ERROR", "Ningún producto seleccionado", "Haz clic en un producto para seleccionarlo.");
            }
        }
    }

}
