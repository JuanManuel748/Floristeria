package com.github.JuanManuel.view.admin;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.florDAO;
import com.github.JuanManuel.model.DAO.ramoDAO;
import com.github.JuanManuel.model.entity.*;
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

import static javafx.collections.FXCollections.observableArrayList;

public class AdminRamosController extends Controller implements Initializable {
    private static File img;
    private static Ramo ramo;
    private static int secunController;
    private static List<Flor> floresPrimarias = florDAO.build().findByType(true);
    private static List<Flor> floresSecundarias = florDAO.build().findByType(false);

    private static final File imgNull = new File("src/main/resources/com/github/JuanManuel/view/images/noPicture.jpg");
    @FXML
    public TableView<Ramo> ramosTable;
    @FXML
    public TableColumn<Ramo, Integer> idColumn;
    @FXML
    public TableColumn<Ramo, byte[]> imgColumn;
    @FXML
    public TableColumn<Ramo, String> nameColumn;
    @FXML
    public TableColumn<Ramo, Double> priceColumn;
    @FXML
    public TableColumn<Ramo, Integer> stockColumn;
    @FXML
    public TableColumn<Ramo, Long> descriptionColumn;
    @FXML
    public TableColumn<Ramo, String> colorColumn;
    @FXML
    public TableColumn<Ramo, Integer> nfloresColumn;
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
    public ComboBox colorField;
    @FXML
    public CheckBox predeterminatedCheck;
    @FXML
    public ComboBox flPRChoice;
    @FXML
    public ComboBox flSecunChoice1;
    @FXML
    public ComboBox flSecunChoice2;
    @FXML
    public ComboBox flSecunChoice3;
    @FXML
    public ComboBox quantitySpinner;

    /**
     * Cambia la escena a la pantalla principal del administrador.
     *
     * @param actionEvent el evento de acción disparado al llamar a este método.
     */
    public void goToHome(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.ADMINHOME, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cierra la sesión del usuario actual y redirige a la pantalla de inicio de sesión.
     *
     * @param actionEvent el evento de acción disparado al llamar a este método.
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
     * Inicializa los componentes de la vista.
     * @param url URL de inicialización.
     * @param resourceBundle Bundle de recursos.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        secunController = 0;
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("colorEnvol"));
        nfloresColumn.setCellValueFactory(new PropertyValueFactory<>("cantidadFlores"));
        setupSpinners();
        imgColumn.setCellValueFactory(new PropertyValueFactory<>("img"));
        imgColumn.setCellFactory(col -> new TableCell<Ramo, byte[]>() {
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
        colorField.setItems(FXCollections.observableArrayList("Rojo", "Verde", "Azul", "Amarillo", "Negro", "Blanco", "Naranja", "Morado","Rosa", "Gris", "Cian", "Turquesa", "Marrón", "Violeta", "Fucsia", "Oliva","Oro", "Plata", "Beige", "Índigo", "Transparente"));
        colorField.setValue("Transparente");
        initializaComboBoc(flPRChoice, floresPrimarias);
        initializaComboBoc(flSecunChoice1, floresSecundarias);
        initializaComboBoc(flSecunChoice2, floresSecundarias);
        initializaComboBoc(flSecunChoice3, floresSecundarias);
        quantitySpinner.setItems(observableArrayList("3 Flores", "5 Flores", "8 Flores", "10 Flores", "15 Flores"));
        quantitySpinner.setValue("3 Flores");

        ramosTable.setOnMouseClicked(event -> handleTableSelection(event));
        mostrarProductos(ramoDAO.build().findAll());
    }

    /**
     * Configura un ComboBox con una lista de flores.
     * @param c ComboBox a inicializar.
     * @param flores Lista de flores a agregar al ComboBox.
     */
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
                    imageView.setFitHeight(30);
                    imageView.setFitWidth(30);
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                }
            }
        });

        c.setButtonCell((ListCell) c.getCellFactory().call(null));
        if (!flores.isEmpty()) {
            c.setValue(flores.get(secunController));
            secunController++;
        }
    }

    /**
     * Valida los campos de entrada del formulario y construye un objeto Ramo.
     * @return true si todos los campos son válidos, false en caso contrario.
     */
    public boolean validateFields() {
        boolean result = false;
        try {
            if (idField.getText().equals("")) {
                Alerta.showAlert("ERROR", "ID vacía", "La ID esta vacía, proporciona una id que no este en uso");
                return result;
            }
            int id = Integer.parseInt(idField.getText());
            String nombre = nameField.getText();
            if(nombre.equals("")) {
                Alerta.showAlert("ERROR", "Nombre vacío", "El nombre esta vacío, proporciona un nombre adecuado");
                return result;
            }
            Double price = Double.parseDouble(priceField.getValue().toString());
            if (price <= 0) {
                Alerta.showAlert("ERROR", "Precio erróneo", "El precio no puede ser 0 o menos");
                return result;
            }
            int stock = Integer.parseInt(stockField.getValue().toString());
            if(stock < 0) {
                Alerta.showAlert("ERROR", "Stock erróneo", "El stock no puede ser menor que 0");
                return result;
            }
            String description = descriptionField.getText();
            if (description.equals("")) {
                Alerta.showAlert("ERROR", "Descripcion vacía", "La descripcion esta vacía, proporciona una descripcion adecuada");
                return result;
            }
            int numFlores = Integer.parseInt(quantitySpinner.getValue().toString().substring(0, 2).trim());
            String color = colorField.getValue().toString();
            Flor pr = (Flor) flPRChoice.getValue();
            List<Flor> floresSecun = new ArrayList<>();
            Flor f1 = (Flor) flSecunChoice1.getValue();
            Flor f2 = (Flor) flSecunChoice2.getValue();
            Flor f3 = (Flor) flSecunChoice3.getValue();
            if (f1.equals(f2) || f1.equals(f3) || f2.equals(f3)) {
                Alerta.showAlert("ERROR", "Flores de relleno repetidas", "las flores de relleno no se pueden repetir, debes seleccionar 3 distintas");
                return result;
            }
            floresSecun.add(f1);
            floresSecun.add(f2);
            floresSecun.add(f3);

            if (ramo == null) {
                ramo = new Ramo();
            }
            ramo.setIdRamo(id);
            ramo.setIdProducto(id);
            ramo.setNombre(nombre);
            ramo.setPrecio(price);
            ramo.setStock(stock);
            ramo.setDescripcion(description);
            ramo.setFlorPr(pr);
            ramo.setColorEnvol(color);
            ramo.setCantidadFlores(numFlores);
            ramo.setFloresSecun(floresSecun);
            ramo.setTipo("ramo");
            if(predeterminatedCheck.isSelected()) {
                if (!description.contains("prehecho")) {
                    ramo.setDescripcion(description + " prehecho");
                }
            } else {
                if (!description.contains("personalizado")) {
                    ramo.setDescripcion(description + " personalizado");
                }
            }
            result = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Configura un ramo en el formulario para ser actualizado o eliminado.
     * @param p Objeto Ramo a configurar en el formulario.
     */
    public void setRamo(Ramo p) {
        try {
            ramo = p;
            idField.setText(String.valueOf(p.getIdRamo()));
            nameField.setText(p.getNombre());
            priceField.getValueFactory().setValue(p.getPrecio());
            stockField.getValueFactory().setValue(p.getStock());
            descriptionField.setText(p.getDescripcion());
            colorField.setValue(p.getColorEnvol());
            quantitySpinner.setValue(p.getCantidadFlores() + " Flores");
            flPRChoice.setValue(p.getFlorPr());
            flSecunChoice1.setValue(p.getFloresSecun().get(0));
            flSecunChoice2.setValue(p.getFloresSecun().get(1));
            flSecunChoice3.setValue(p.getFloresSecun().get(2));
            if(p.getDescripcion().contains("prehecho")) {
                predeterminatedCheck.setSelected(true);
            }
            byte[] byteImage = p.getImg();
            Image defaultImage = new Image(imgNull.toURI().toString());
            if (byteImage != null) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteImage);
                Image image = new Image(byteArrayInputStream);
                uploadButton.setImage(image);
            } else {
                uploadButton.setImage(defaultImage);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Muestra en la tabla todos los productos de tipo Ramo proporcionados en una lista.
     * @param array Lista de ramos a mostrar en la tabla.
     */
    public void mostrarProductos(List<Ramo> array){
        ObservableList<Ramo> observableList = FXCollections.observableArrayList(array);
        ramosTable.setItems(observableList);
        ramosTable.refresh();
    }

    /**
     * Limpia todos los campos del formulario y reinicia la selección.
     */
    public void clearFields() {
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

        quantitySpinner.setValue("3 Flores");
        colorField.setValue("Transparente");

        img = null;
        ramo = new Ramo();
    }

    /**
     * Inserta un nuevo ramo en la base de datos.
     * @param actionEvent Evento que dispara la acción de inserción.
     */
    public void insert(ActionEvent actionEvent) {
        try {
            if (!validateFields()) {
                return;
            }
            ramoDAO.build().insertRamo(ramo);
            Alerta.showAlert("INFORMATION", "Producto insertado", "El producto ha sido insertado en la base de datos exitosamente");
            mostrarProductos(ramoDAO.build().findAll());
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO insertado", "Error al insertar el producto.");
            e.printStackTrace();
        }
    }

    /**
     * Actualiza un ramo existente en la base de datos.
     * @param actionEvent Evento que dispara la acción de actualización.
     */
    public void update(ActionEvent actionEvent) {
        try {
            if (!validateFields()) {
                return;
            }
            ramoDAO.build().updateRamo(ramo);
            Alerta.showAlert("INFORMATION", "Producto insertado", "El producto ha sido insertado en la base de datos exitosamente");
            mostrarProductos(ramoDAO.build().findAll());
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO insertado", "Error al insertar el producto.");
            e.printStackTrace();
        }
    }

    /**
     * Elimina un ramo de la base de datos.
     * @param actionEvent Evento que dispara la acción de eliminación.
     */
    public void delete(ActionEvent actionEvent) {
        try {
            if (!validateFields()) {
                return;
            }
            ramoDAO.build().delete(ramo);
            Alerta.showAlert("INFORMATION", "Producto insertado", "El producto ha sido insertado en la base de datos exitosamente");
            mostrarProductos(ramoDAO.build().findAll());
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO insertado", "Error al insertar el producto.");
            e.printStackTrace();
        }
    }

    /**
     * Busca ramos en la base de datos según los criterios seleccionados.
     * @param actionEvent Evento que dispara la acción de búsqueda.
     */
    public void find(ActionEvent actionEvent) {
        try {
            List<Ramo>findLS = new ArrayList<>();
            switch (selectAlert()) {
                case 1:
                    int id = Integer.parseInt(idField.getText().trim());
                    Ramo item = ramoDAO.build().findByPK(new Ramo(id));
                    if (item != null) {
                        findLS.add(item);
                    }
                    break;
                case 2:
                    String name = nameField.getText().trim();
                    findLS = ramoDAO.build().findByNames(name);
                    break;
                case 3:
                    findLS = ramoDAO.build().findByType(predeterminatedCheck.isSelected());
                    break;
                case 4:
                    findLS = ramoDAO.build().findAll();
                    break;
                default:
                    return;
            }

            mostrarProductos(findLS);
            clearFields();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Muestra un cuadro de diálogo para seleccionar el criterio de búsqueda.
     * @return Entero que indica el criterio de búsqueda seleccionado.
     */
    private int selectAlert() {
        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setTitle("Elegir campo");
        al.setHeaderText("Elige el campo por el que quieres buscar");
        ButtonType phone = new ButtonType("ID");
        ButtonType name = new ButtonType("Nombre");
        ButtonType admin = new ButtonType("Tipo");
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
     * Permite al usuario cargar una imagen desde su sistema de archivos.
     * @param mouseEvent Evento que dispara la carga de la imagen.
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

                if (ramo == null) {
                    ramo = new Ramo();
                }

                byte[] imageBytes = java.nio.file.Files.readAllBytes(selectedFile.toPath());
                ramo.setImg(imageBytes);
            } else {
                Alerta.showAlert("INFORMATION", "No se seleccionó ninguna imagen", "Por favor selecciona una imagen.");
            }
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Error al cargar imagen", "No se pudo cargar la imagen seleccionada.");
        }
    }

    /**
     * Configura los spinners de precio y stock con valores y restricciones predeterminadas.
     */
    private void setupSpinners() {
        priceField.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, Double.MAX_VALUE, 0.00, 0.10));
        priceField.getValueFactory().setConverter(new DoubleStringConverter());
        stockField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        stockField.getValueFactory().setConverter(new IntegerStringConverter());
    }

    /**
     * Maneja la selección de un ramo en la tabla al hacer doble clic.
     * @param event Evento del clic en la tabla.
     */
    private void handleTableSelection(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Ramo selected = ramosTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                setRamo(selected);
            } else {
                Alerta.showAlert("ERROR", "Ningún producto seleccionado", "Haz clic en un producto para seleccionarlo.");
            }
        }
    }

}

