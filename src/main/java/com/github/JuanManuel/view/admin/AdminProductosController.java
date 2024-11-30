package com.github.JuanManuel.view.admin;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.productoDAO;
import com.github.JuanManuel.model.DAO.userDAO;
import com.github.JuanManuel.model.entity.Producto;
import com.github.JuanManuel.model.entity.Session;
import com.github.JuanManuel.model.entity.User;
import com.github.JuanManuel.view.Alerta;
import com.github.JuanManuel.view.Controller;
import com.github.JuanManuel.view.Scenes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminProductosController extends Controller implements Initializable {
    private static File img;
    private static Producto producto;
    private static final File imgNull = new File("src/main/resources/com/github/JuanManuel/view/images/noPicture.jpg");
    @FXML
    public TableView<Producto> productsTable;
    @FXML
    public TableColumn<Producto, Integer> idColumn;
    @FXML
    public TableColumn<Producto, byte[]> imgColumn;
    @FXML
    public TableColumn<Producto, String> nameColumn;
    @FXML
    public TableColumn<Producto, Double> priceColumn;
    @FXML
    public TableColumn<Producto, Integer> stockColumn;
    @FXML
    public TableColumn<Producto, Long> descriptionColumn;
    @FXML
    public TableColumn<Producto, Integer> quantityColumn;
    @FXML
    public Button insertButton;
    @FXML
    public Button updateButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button findButton;
    @FXML
    public ImageView uploadButton;
    @FXML
    public TextField idField;
    @FXML
    public TextField nameField;
    @FXML
    public Spinner priceField;
    @FXML
    public Spinner stockField;
    @FXML
    public TextArea descriptionField;

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
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("cantidadVendida"));
        quantityColumn.setVisible(false);

        imgColumn.setCellValueFactory(new PropertyValueFactory<>("img"));
        imgColumn.setCellFactory(col -> new TableCell<Producto, byte[]>() {
            @Override
            protected void updateItem(byte[] item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    Image image = new Image(new ByteArrayInputStream(item));
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);
                    setGraphic(imageView);
                }
            }
        });

        productsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Producto selected = productsTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    setProducto(selected);
                }
            }
        });

        priceField.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, Double.MAX_VALUE, 0.00, 0.10));
        priceField.getValueFactory().setConverter(new DoubleStringConverter());
        stockField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        stockField.getValueFactory().setConverter(new IntegerStringConverter());




        List<Producto> productoList = productoDAO.build().findByType("complemento");
        mostrarProductos(productoList);
    }

    public void setProducto(Producto p) {
        idField.setText(String.valueOf(p.getIdProducto()));
        nameField.setText(p.getNombre());
        priceField.getValueFactory().setValue(p.getPrecio());
        stockField.getValueFactory().setValue(p.getStock());
        descriptionField.setText(p.getDescripcion());
        byte[] byteImage = p.getImg();
        Image defaultImage = new Image(imgNull.toURI().toString());
        if (byteImage != null) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteImage);
            Image image = new Image(byteArrayInputStream);
            uploadButton.setImage(image);
        } else {
            uploadButton.setImage(defaultImage);
        }
        validateFields();
    }

    public void mostrarProductos(List<Producto> array) {
        ObservableList<Producto>productoObservableList = FXCollections.observableArrayList(array);
        productsTable.setItems(productoObservableList);
        productsTable.refresh();
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        descriptionField.setText("");
        if (priceField.getValueFactory() != null) {
            priceField.getValueFactory().setValue(0.0);
        }
        if (stockField.getValueFactory() != null) {
            stockField.getValueFactory().setValue(0);
        }
        try {
            Image defaultImage = new Image(imgNull.toURI().toString());
            uploadButton.setImage(defaultImage);
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Error al cargar imagen predeterminada", "No se pudo cargar la imagen predeterminada.");
        }

        img = null;
        producto = null;
    }

    public void uploadImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        img = fileChooser.showOpenDialog(App.stage);

        if (img != null) {
            try {
                if (producto == null) {
                    producto = new Producto();
                }
                producto.setImg(img);

                Image image = new Image(img.toURI().toString());
                uploadButton.setImage(image);
            } catch (Exception e) {
                Alerta.showAlert("ERROR", "Error al cargar la imagen", "No se pudo cargar la imagen seleccionada.");
            }
        } else {
            Alerta.showAlert("ERROR", "Imagen no seleccionada", "No se seleccionó ninguna imagen.");
        }
    }


    public boolean confirm(String header, String content) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("CONFIRMATION");
        alerta.setHeaderText(header);
        alerta.setContentText(content + ".\n¿Estas seguro?");
        Optional<ButtonType> result = alerta.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validateFields() {
        boolean result = false;
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String nombre = nameField.getText();
            Double precio = (Double) priceField.getValue();
            int stock = (int) stockField.getValue();
            String tipo = "complemento";
            String description = descriptionField.getText();
            if (nombre.equals("")) {
                if (!confirm("Nombre vacío", "No has puesto un nombre al producto")) {
                    return result;
                }
            }
            if (precio <= 0) {
                Alerta.showAlert("ERROR", "Precio inválido", "El precio no puede ser menor o igual a 0.");
                return result;
            }
            if (stock < 0) {
                Alerta.showAlert("ERROR", "Stock negativo", "El stock no puede ser menor que 0.");
                return result;
            }
            if (description.equals("")) {
                if (!confirm("Descripción vacía", "No has puesto ninguna descripción.")) {
                    return result;
                }
            }
            producto.setIdProducto(id);
            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setStock(stock);
            producto.setTipo(tipo);
            producto.setDescripcion(description);

            result = true;
        } catch (NumberFormatException nfe) {
            Alerta.showAlert("ERROR", "Error de formato", "Los campos ID, precio o stock tienen formato incorrecto.");
            nfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void insert(ActionEvent actionEvent) {
        try {
            if(!validateFields()) {
                return;
            }
            productoDAO.build().insertProducto(producto);
            Alerta.showAlert("INFORMATION", "Producto insertado", "El producto ha sido insertado en la base de datos exitosamente");

            List<Producto> allProducts = productoDAO.build().findByType("complemento");
            mostrarProductos(allProducts);

            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO insertado", "El producto NO ha sido insertado en la base de datos exitosamente.\nRevisa que la id del producto no este en uso");
            throw new RuntimeException(e);
        }
    }

    public void update(ActionEvent actionEvent) {
        try {
            if(!validateFields()) {
                return;
            }
            productoDAO.build().save(producto);
            Alerta.showAlert("INFORMATION", "Producto actualizado", "El producto ha sido actualizado en la base de datos exitosamente");

            List<Producto> allProducts = productoDAO.build().findByType("complemento");
            mostrarProductos(allProducts);

            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO actualizado", "El producto NO ha sido actualizado en la base de datos");
            throw new RuntimeException(e);
        }
    }

    public void delete(ActionEvent actionEvent) {
        try {
            if (producto == null) {
                Alerta.showAlert("ERROR", "Ningún producto seleccionado", "Por favor, selecciona un producto para eliminar.");
                return;
            }
            productoDAO.build().delete(producto);
            Alerta.showAlert("INFORMATION", "Producto eliminado", "El producto ha sido eliminado de la base de datos exitosamente");

            List<Producto> allProducts = productoDAO.build().findByType("complemento");
            mostrarProductos(allProducts);

            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO eliminado", "El producto NO ha sido eliminado de la base de datos");
            throw new RuntimeException(e);
        }
    }

    // TERMINAR
    public void find(ActionEvent actionEvent) {

        quantityColumn.setVisible(false);
        try {
            List<Producto> proList = new ArrayList<>();

            switch (selectAlert()) {
                case 1: // ID
                    proList.add(productoDAO.build().findByPK(producto));
                    break;
                case 2: // NOMBRE
                    proList = productoDAO.build().findByComplName(nameField.getText(), "complemento", "complemento");
                    break;
                case 3: // GROUP BY

                    quantityColumn.setVisible(true);
                    proList = productoDAO.build().findGroupBy(insertQuantity(), "complemento");
                    break;
                case 4: // ALL
                    proList = productoDAO.build().findByType("complemento");
                    break;
                default:
                    return;
            }
            mostrarProductos(proList);

            clearFields();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int insertQuantity() {
        int result = 0;

        Alert al = new Alert(Alert.AlertType.INFORMATION);
        al.setTitle("Ingresar cantidad");
        al.setHeaderText("Por favor, ingresa la cantidad que deseas usar en el group by");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Cantidad");

        al.getDialogPane().setContent(quantityField);

        Optional<ButtonType> resultOptional = al.showAndWait();

        if (resultOptional.isPresent() && resultOptional.get() == ButtonType.OK) {
            try {
                result = Integer.parseInt(quantityField.getText().trim());
            } catch (NumberFormatException e) {
                Alerta.showAlert("ERROR", "Entrada no válida", "Por favor, ingresa una cantidad numérica válida.");
                result = 0;
            }
        }

        return result;
    }

    private int selectAlert() {
        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setTitle("Elegir campo");
        al.setHeaderText("Elige el campo por el que quieres buscar");
        ButtonType phone = new ButtonType("ID");
        ButtonType name = new ButtonType("Nombre");
        ButtonType admin = new ButtonType("GROUPBY");
        ButtonType all = new ButtonType("All");
        ButtonType cancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        al.getButtonTypes().setAll(phone, name, admin, all, cancel);

        Optional<ButtonType> result = al.showAndWait();
        if (result.get() == phone) {
            return 1;
        } else if (result.get() == name) {
            return 2;
        } else if (result.get() == admin) {
            return 3;
        } else if (result.get() == all) {
            return 4;
        } else {
            return 0;
        }

    }


}
