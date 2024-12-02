package com.github.JuanManuel.view.admin;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.florDAO;
import com.github.JuanManuel.model.DAO.productoDAO;
import com.github.JuanManuel.model.DAO.userDAO;
import com.github.JuanManuel.model.entity.Flor;
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
import javafx.scene.control.cell.CheckBoxTableCell;
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

public class AdminFloresController extends Controller implements Initializable {
    private static File img;
    private static Flor flor;
    private static final File imgNull = new File("src/main/resources/com/github/JuanManuel/view/images/noPicture.jpg");
    @FXML
    public TableView<Flor> florTable;
    @FXML
    public TableColumn<Flor, Integer> idColumn;
    @FXML
    public TableColumn<Flor, byte[]> imgColumn;
    @FXML
    public TableColumn<Flor, String> nameColumn;
    @FXML
    public TableColumn<Flor, Double> priceColumn;
    @FXML
    public TableColumn<Flor, Integer> stockColumn;
    @FXML
    public TableColumn<Flor, Long> descriptionColumn;
    @FXML
    public TableColumn<Flor, String> colorColumn;
    @FXML
    public TableColumn<Flor, Boolean> prColumn;
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
    @FXML
    public TextField colorField;
    @FXML
    public CheckBox prCheck;

    /**
     * Cambia la vista a la pantalla de inicio de administración.
     *
     * @param actionEvent El evento que activa la acción de cambiar de escena.
     */
    public void goToHome(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.ADMINHOME, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cierra la sesión del usuario actual y redirige a la pantalla de login.
     *
     * @param actionEvent El evento que activa la acción de cierre de sesión.
     */
    public void logout(ActionEvent actionEvent) {
        try {
            User u = Session.getInstance().getCurrentUser();
            Session.getInstance().logOut(u);
            App.currentController.changeScene(Scenes.LOGIN, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inicializa la vista con las configuraciones necesarias, incluyendo
     * la configuración de las columnas de la tabla, los controles de spinner
     * y la carga de los productos de flores desde la base de datos.
     *
     * @param url La URL de la vista FXML.
     * @param resourceBundle El recurso que contiene las configuraciones de idioma.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        prColumn.setCellValueFactory(cellData -> cellData.getValue().adminProperty());
        prColumn.setCellFactory(tc -> new CheckBoxTableCell<>());

        imgColumn.setCellValueFactory(new PropertyValueFactory<>("img"));
        imgColumn.setCellFactory(col -> new TableCell<Flor, byte[]>() {
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

        florTable.setOnMouseClicked(event -> handleTableSelection(event));
        setupSpinners();

        List<Flor> ls = florDAO.build().findAll();
        mostrarProductos(ls);
    }

    /**
     * Configura los spinners para los campos de precio y stock con valores válidos
     * y sus convertidores adecuados.
     */
    private void setupSpinners() {
        priceField.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, Double.MAX_VALUE, 0.00, 0.10));
        priceField.getValueFactory().setConverter(new DoubleStringConverter());
        stockField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        stockField.getValueFactory().setConverter(new IntegerStringConverter());
    }

    /**
     * Maneja la selección de una fila en la tabla de flores. Si se hace doble clic
     * en una fila, se cargan los datos del producto seleccionado en los campos correspondientes.
     *
     * @param event El evento de clic en la tabla.
     */
    private void handleTableSelection(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Flor selected = florTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                setFlor(selected);
            } else {
                Alerta.showAlert("ERROR", "Ningún producto seleccionado", "Haz clic en un producto para seleccionarlo.");
            }
        }
    }

    /**
     * Establece los datos del producto en los campos correspondientes de la vista.
     *
     * @param p El objeto Flor con los datos a cargar.
     */
    public void setFlor(Flor p) {
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
        colorField.setText(p.getColor());
        prCheck.setSelected(p.getTipoFlor());
        flor = p;
    }

    /**
     * Muestra una lista de productos en la tabla.
     *
     * @param array La lista de flores a mostrar.
     */
    public void mostrarProductos(List<Flor> array) {
        ObservableList<Flor> ObservableList = FXCollections.observableArrayList(array);
        florTable.setItems(ObservableList);
        florTable.refresh();
    }

    /**
     * Limpia los campos de entrada en la vista, incluyendo los campos de texto,
     * los spinners y la imagen de la flor.
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
        colorField.setText("");
        prCheck.setSelected(false);
        img = null;
        flor = null;
    }

    /**
     * Inserta un nuevo producto en la base de datos, mostrando una alerta
     * si la inserción es exitosa o si ocurre algún error.
     *
     * @param actionEvent El evento que activa la acción de inserción.
     */
    public void insert(ActionEvent actionEvent) {
        try {
            if (!validateFields()) {
                return;
            }
            florDAO.build().insertFlor(flor);
            Alerta.showAlert("INFORMATION", "Producto insertado", "El producto ha sido insertado en la base de datos exitosamente");
            mostrarProductos(florDAO.build().findAll());
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO insertado", "Error al insertar el producto.");
            e.printStackTrace();
        }
    }

    /**
     * Actualiza los datos de un producto en la base de datos y muestra una alerta
     * si la actualización es exitosa o si ocurre algún error.
     *
     * @param actionEvent El evento que activa la acción de actualización.
     */
    public void update(ActionEvent actionEvent) {

        try {
            if (!validateFields()) {
                return;
            }
            florDAO.build().save(flor);
            Alerta.showAlert("INFORMATION", "Producto actualizado", "El producto ha sido actualizado exitosamente");
            mostrarProductos(florDAO.build().findAll());
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO actualizado", "Error al actualizar el producto.");
        }
    }

    /**
     * Elimina el producto actualmente seleccionado de la base de datos y
     * muestra una alerta con el resultado de la acción.
     *
     * @param actionEvent El evento que activa la acción de eliminación.
     */
    public void delete(ActionEvent actionEvent) {
        try {
            if (flor == null) {
                Alerta.showAlert("ERROR", "Ningún producto seleccionado", "Selecciona un producto para eliminar.");
                return;
            }
            florDAO.build().delete(flor);
            Alerta.showAlert("INFORMATION", "Producto eliminado", "El producto ha sido eliminado.");
            mostrarProductos(florDAO.build().findAll());
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO eliminado", "Error al eliminar el producto.");
        }
    }

    /**
     * Realiza una búsqueda de productos según el criterio seleccionado
     * en un cuadro de diálogo. Los productos encontrados se muestran en la tabla.
     *
     * @param actionEvent El evento que activa la acción de búsqueda.
     */
    public void find(ActionEvent actionEvent) {
        try {
            List<Flor> findList = new ArrayList<>();
            switch (selectAlert()) {
                case 1:
                    int id = Integer.parseInt(idField.getText().trim());
                    Flor item = florDAO.build().findByPK(new Flor(id));
                    if (item != null) {
                        findList.add(item);
                    }
                    break;
                case 2:
                    String name = nameField.getText().trim();
                    findList = florDAO.build().findByName(name);
                    break;
                case 3:
                    findList = florDAO.build().findByType(prCheck.isSelected());
                    break;
                case 4:
                    findList = florDAO.build().findAll();
                    break;
                default:
                    return;
            }
            mostrarProductos(findList);
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Error de búsqueda", "Error al buscar productos.");
        }
    }

    /**
     * Valida los campos de entrada antes de realizar una acción como insertar o actualizar.
     *
     * @return true si todos los campos son válidos, false en caso contrario.
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
            if (flor == null) {
                flor = new Flor();
            }
            flor.setIdProducto(Integer.parseInt(idField.getText().trim()));
            flor.setIdFlor(flor.getIdProducto());
            flor.setNombre(nameField.getText().trim());
            flor.setPrecio((Double) priceField.getValue());
            flor.setStock((Integer) stockField.getValue());
            flor.setDescripcion(descriptionField.getText().trim());
            flor.setTipo("flor");
            flor.setColor(colorField.getText());
            flor.setTipoFlor(prCheck.isSelected());
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Se ejecuta cuando se abre la vista. Este método está vacío por ahora.
     */
    @Override
    public void onOpen(Object input) throws Exception {

    }

    /**
     * Se ejecuta cuando se cierra la vista. Este método está vacío por ahora.
     */
    @Override
    public void onClose(Object output) {

    }

    /**
     * Muestra una alerta con las opciones para seleccionar un criterio de búsqueda.
     *
     * @return El número de la opción seleccionada.
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
     * Permite al usuario cargar una imagen para el centro seleccionado.
     * La imagen seleccionada se asigna al centro y se muestra en la interfaz.
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

                if (flor == null) {
                    flor = new Flor();
                }

                byte[] imageBytes = java.nio.file.Files.readAllBytes(selectedFile.toPath());
                flor.setImg(imageBytes);
            } else {
                Alerta.showAlert("INFORMATION", "No se seleccionó ninguna imagen", "Por favor selecciona una imagen.");
            }
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Error al cargar imagen", "No se pudo cargar la imagen seleccionada.");
        }
    }




}
