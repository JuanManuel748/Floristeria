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

    private void setupSpinners() {
        priceField.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, Double.MAX_VALUE, 0.00, 0.10));
        priceField.getValueFactory().setConverter(new DoubleStringConverter());
        stockField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        stockField.getValueFactory().setConverter(new IntegerStringConverter());
    }

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

    public void mostrarProductos(List<Flor> array) {
        ObservableList<Flor> ObservableList = FXCollections.observableArrayList(array);
        florTable.setItems(ObservableList);
        florTable.refresh();
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
        colorField.setText("");
        prCheck.setSelected(false);
        img = null;
        flor = null;
    }

    public void insert(ActionEvent actionEvent) {
        try {
            if (!validateFields()) {
                return;
            }
            florDAO.build().save(flor);
            Alerta.showAlert("INFORMATION", "Producto insertado", "El producto ha sido insertado en la base de datos exitosamente");
            mostrarProductos(florDAO.build().findAll());
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO insertado", "Error al insertar el producto.");
            e.printStackTrace();
        }
    }

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

    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

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
