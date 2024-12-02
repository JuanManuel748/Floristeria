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
    public Spinner<Double> priceField;
    @FXML
    public Spinner<Integer> stockField;
    @FXML
    public TextArea descriptionField;

    /**
     * Método llamado automáticamente al cargar la vista.
     * Configura las columnas de la tabla, los spinners y carga los productos iniciales.
     */
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

        productsTable.setOnMouseClicked(event -> handleTableSelection(event));
        setupSpinners();

        List<Producto> productoList = productoDAO.build().findByType("complemento");
        mostrarProductos(productoList);
    }

    /**
     * Configura los spinners de precio y stock con valores predeterminados.
     */
    private void setupSpinners() {
        priceField.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, Double.MAX_VALUE, 0.00, 0.10));
        priceField.getValueFactory().setConverter(new DoubleStringConverter());
        stockField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        stockField.getValueFactory().setConverter(new IntegerStringConverter());
    }

    /**
     * Maneja la selección de filas en la tabla al hacer doble clic.
     * @param event Evento del mouse.
     */
    private void handleTableSelection(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Producto selected = productsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                setProducto(selected);
            } else {
                Alerta.showAlert("ERROR", "Ningún producto seleccionado", "Haz clic en un producto para seleccionarlo.");
            }
        }
    }

    /**
     * Establece los datos de un producto en los campos de la interfaz.
     * @param p Producto a mostrar.
     */
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
        producto = p;
    }

    /**
     * Muestra una lista de productos en la tabla.
     * @param array Lista de productos.
     */
    public void mostrarProductos(List<Producto> array) {
        ObservableList<Producto> productoObservableList = FXCollections.observableArrayList(array);
        productsTable.setItems(productoObservableList);
        productsTable.refresh();
    }

    /**
     * Limpia los campos de entrada de la interfaz, restableciendo valores por defecto.
     */
    private void clearFields() {
        idField.setText(String.valueOf(Producto.searchID()));
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

    /**
     * Inserta un nuevo producto en la base de datos.
     * @param actionEvent Evento generado por el botón de insertar.
     */
    public void insert(ActionEvent actionEvent) {
        try {
            if (!validateFields()) {
                return;
            }
            productoDAO.build().insertProducto(producto);
            Alerta.showAlert("INFORMATION", "Producto insertado", "El producto ha sido insertado en la base de datos exitosamente");
            mostrarProductos(productoDAO.build().findByType("complemento"));
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO insertado", "Error al insertar el producto.");
        }
    }

    /**
     * Actualiza un producto existente en la base de datos.
     * @param actionEvent Evento generado por el botón de actualizar.
     */
    public void update(ActionEvent actionEvent) {

        try {
            if (!validateFields()) {
                return;
            }
            productoDAO.build().save(producto);
            Alerta.showAlert("INFORMATION", "Producto actualizado", "El producto ha sido actualizado exitosamente");
            mostrarProductos(productoDAO.build().findByType("complemento"));
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO actualizado", "Error al actualizar el producto.");
        }
    }

    /**
     * Elimina el producto seleccionado de la base de datos.
     * @param actionEvent Evento generado por el botón de eliminar.
     */
    public void delete(ActionEvent actionEvent) {
        try {
            if (producto == null) {
                Alerta.showAlert("ERROR", "Ningún producto seleccionado", "Selecciona un producto para eliminar.");
                return;
            }
            productoDAO.build().delete(producto);
            Alerta.showAlert("INFORMATION", "Producto eliminado", "El producto ha sido eliminado.");
            mostrarProductos(productoDAO.build().findByType("complemento"));
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO eliminado", "Error al eliminar el producto.");
        }
    }

    /**
     * Busca productos según criterios especificados por el usuario.
     * @param actionEvent Evento generado por el botón de buscar.
     */
    public void find(ActionEvent actionEvent) {
        try {
            List<Producto> proList = new ArrayList<>();
            switch (selectAlert()) {
                case 1:
                    int id = Integer.parseInt(idField.getText().trim());
                    Producto producto = productoDAO.build().findByPK(new Producto(id));
                    if (producto != null) {
                        proList.add(producto);
                    }
                    break;
                case 2:
                    String name = nameField.getText().trim();
                    proList = productoDAO.build().findByComplName(name, "complemento", "complemento");
                    break;
                case 3:
                    quantityColumn.setVisible(true);
                    int quantity = insertQuantity();
                    proList = productoDAO.build().findGroupBy(quantity, "complemento");
                    break;
                case 4:
                    proList = productoDAO.build().findByType("complemento");
                    break;
                default:
                    return;
            }
            mostrarProductos(proList);
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Error de búsqueda", "Error al buscar productos.");
        }
    }

    /**
     * Valida los campos de entrada antes de realizar operaciones de base de datos.
     * @return true si los campos son válidos; false de lo contrario.
     */
    private boolean validateFields() {
        try {
            if (nameField.getText().trim().isEmpty()) {
                Alerta.showAlert("ERROR", "Nombre inválido", "El campo de nombre no puede estar vacío.");
                return false;
            }
            if ((Double) priceField.getValue() <= 0) {
                Alerta.showAlert("ERROR", "Precio inválido", "El precio debe ser mayor a 0.");
                return false;
            }
            if ((Integer) stockField.getValue() < 0) {
                Alerta.showAlert("ERROR", "Stock inválido", "El stock no puede ser negativo.");
                return false;
            }
            if (descriptionField.getText().trim().isEmpty()) {
                Alerta.showAlert("ERROR", "Descripción inválida", "El campo de descripción no puede estar vacío.");
                return false;
            }
            producto = producto == null ? new Producto() : producto;
            producto.setIdProducto(idField.getText().isEmpty() ? 0 : Integer.parseInt(idField.getText()));
            producto.setNombre(nameField.getText().trim());
            producto.setPrecio((Double) priceField.getValue());
            producto.setStock((Integer) stockField.getValue());
            producto.setDescripcion(descriptionField.getText().trim());
            producto.setTipo("complemento");
            return true;
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Validación fallida", "Error al validar los campos.");
            return false;
        }
    }

    /**
     * Método ejecutado al abrir la vista o inicializarla.
     * Puede recibir datos de entrada para configurar la vista.
     *
     * @param input un objeto con los datos de entrada para inicializar la vista.
     * @throws Exception si ocurre algún error durante la inicialización.
     */
    @Override
    public void onOpen(Object input) throws Exception {

    }

    /**
     * Método ejecutado al cerrar la vista.
     * Puede manejar la salida o datos generados antes de cerrar.
     *
     * @param output un objeto con los datos de salida o resultado de la vista.
     */
    @Override
    public void onClose(Object output) {

    }

    /**
     * Muestra un cuadro de diálogo para que el usuario introduzca una cantidad.
     * @return La cantidad introducida por el usuario.
     */
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

    /**
     * Muestra una alerta para seleccionar un criterio de búsqueda.
     * @return El número correspondiente a la opción seleccionada (1: ID, 2: Nombre, 3: Agrupar por cantidad, 4: Todos).
     */
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

    /**
     * Maneja la acción de cerrar sesión y regresar a la pantalla de inicio de sesión.
     * @param action Evento generado por el botón de logout.
     */
    public void logout(ActionEvent action) {
        try {
            User u = Session.getInstance().getCurrentUser();
            Session.getInstance().logOut(u);
            App.currentController.changeScene(Scenes.LOGIN, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Navega a la pantalla principal del administrador.
     * @param action Evento generado por el botón de ir a inicio.
     */
    public void goToHome(ActionEvent action) {
        try {
            App.currentController.changeScene(Scenes.ADMINHOME, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Permite al usuario cargar una imagen para el producto.
     * @param mouseEvent Evento generado al hacer clic en el botón de cargar imagen.
     */
    public void uploadImage(MouseEvent mouseEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar imagen");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.jpeg", "*.png", "*.bmp", "*.gif")
            );

            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                img = selectedFile;
                Image image = new Image(selectedFile.toURI().toString());
                uploadButton.setImage(image);

                if (producto == null) {
                    producto = new Producto();
                }

                byte[] imageBytes = java.nio.file.Files.readAllBytes(selectedFile.toPath());
                producto.setImg(imageBytes);
            } else {
                Alerta.showAlert("INFORMATION", "No se seleccionó ninguna imagen", "Por favor selecciona una imagen.");
            }
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Error al cargar imagen", "No se pudo cargar la imagen seleccionada.");
        }
    }

}
