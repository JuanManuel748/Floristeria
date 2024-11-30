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
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));

        imgColumn.setCellValueFactory(new PropertyValueFactory<>("img"));
        imgColumn.setCellFactory(col -> new TableCell<Flor, byte[]>() {
            @Override
            protected void updateItem(byte[] item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    try {
                        Image image = new Image(new ByteArrayInputStream(item));
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(50);
                        imageView.setFitHeight(50);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null); // Manejo de imágenes corruptas
                    }
                }
            }
        });

        prColumn.setCellValueFactory(cellData -> cellData.getValue().adminProperty());
        prColumn.setCellFactory(tc -> new CheckBoxTableCell<>());

        florTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Flor selected = florTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    setFlor(selected);
                }
            }
        });

        priceField.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, Double.MAX_VALUE, 0.00, 0.10));
        priceField.getValueFactory().setConverter(new DoubleStringConverter());
        stockField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        stockField.getValueFactory().setConverter(new IntegerStringConverter());

        List<Flor> florList = florDAO.build().findAll();
        mostrarProductos(florList);
    }

    public void setFlor(Flor p) {
        idField.setText(String.valueOf(p.getIdProducto()));
        nameField.setText(p.getNombre());
        priceField.getValueFactory().setValue(p.getPrecio());
        stockField.getValueFactory().setValue(p.getStock());
        descriptionField.setText(p.getDescripcion());
        colorField.setText(p.getColor());
        prCheck.setSelected(p.getTipoFlor());

        byte[] byteImage = p.getImg();
        Image defaultImage = new Image(imgNull.toURI().toString());
        if (byteImage != null) {
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteImage);
                Image image = new Image(byteArrayInputStream);
                uploadButton.setImage(image);
            } catch (Exception e) {
                uploadButton.setImage(defaultImage);
            }
        } else {
            uploadButton.setImage(defaultImage);
        }

        validateFields();
    }



    /**
     * Convierte un archivo de imagen en un array de bytes.
     */
    private byte[] imgToByteArray(File file) {
        try {
            return java.nio.file.Files.readAllBytes(file.toPath());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void mostrarProductos(List<Flor> array) {
        ObservableList<Flor>productoObservableList = FXCollections.observableArrayList(array);
        florTable.setItems(productoObservableList);
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
        img = imgNull;
        flor = null;
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
                if (flor == null) {
                    flor = new Flor();
                }
                flor.setImg(img);

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
            String color = colorField.getText().trim();
            Boolean isPR = prCheck.isSelected();
            if (nombre.equals("")) {
                if (!confirm("Nombre vacio", "No has puesto un nombre al producto")) {
                    return result;
                }
            }
            if (precio <= 0) {
                if (!confirm("Precio menor que 0", "No puedes poner un precio en 0 o en negativo")) {
                    return result;
                } else {
                    return result;
                }
            }
            if (stock == 0) {
                if (!confirm("Stock en 0", "El stock esta en 0")) {
                    return result;
                }
            }
            if (stock < 0) {
                Alerta.showAlert("ERROR", "Stock negativo", "El stock no puede ser menor que 0");
                return result;
            }
            if(description.equals("")) {
                if (!confirm("Descripcion vacia", "No has puesto ninguna descripcion")) {
                    return result;
                }
            }
            if(color.equals("")) {
                if (!confirm("Color vacio", "No has puesto ningun color")) {
                    return result;
                }
            }
            flor = new Flor(id, nombre, precio, stock, description, color, isPR);
            result = true;
        } catch (NumberFormatException nfe) {
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

            // Convierte la imagen seleccionada a un array de bytes
            if (img != null && img.exists()) {
                flor.setImg(imgToByteArray(img));  // Asigna la imagen como byte[]
            }

            florDAO.build().insertFlor(flor);
            Alerta.showAlert("INFORMATION", "Flor insertada", "La flor ha sido insertada en la base de datos exitosamente");

            List<Flor> allProducts = florDAO.build().findAll();
            mostrarProductos(allProducts);

            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Flor NO insertada", "La flor NO ha sido insertada en la base de datos exitosamente.\nRevisa que la id no este en uso");
            throw new RuntimeException(e);
        }
    }

    public void update(ActionEvent actionEvent) {
        try {
            if(!validateFields()) {
                return;
            }

            if (img != null && img.exists()) {
                flor.setImg(imgToByteArray(img));
            }

            florDAO.build().updateFlor(flor);
            Alerta.showAlert("INFORMATION", "Flor actualizada", "La flor ha sido actualizada en la base de datos exitosamente");

            List<Flor> allProducts = florDAO.build().findAll();
            mostrarProductos(allProducts);

            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO actualizada", "La flor NO ha sido actualizada en la base de datos exitosamente.\nRevisa que la id no este en uso");
            throw new RuntimeException(e);
        }
    }

    public void delete(ActionEvent actionEvent) {
        try {
            if(flor == null) {
                return;
            }
            florDAO.build().delete(flor);
            Alerta.showAlert("INFORMATION", "Flor eliminada", "La flor ha sido eliminada en la base de datos exitosamente");

            List<Flor> allProducts = florDAO.build().findAll();
            mostrarProductos(allProducts);

            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO eliminada", "La flor NO ha sido eliminada en la base de datos exitosamente.\nRevisa que la id no este en uso");
            throw new RuntimeException(e);
        }
    }

    public void find(ActionEvent actionEvent) {
        try {
            List<Flor> florLS = new ArrayList<>();

            switch (selectAlert()) {
                case 1: // ID
                    florLS.add(florDAO.build().findByPK(new Flor(Integer.parseInt(idField.getText().trim()))));
                    break;
                case 2: // NOMBRE
                    florLS = florDAO.build().findByName(nameField.getText().trim());
                    break;
                case 3: // TIPO
                    florLS = florDAO.build().findByType(prCheck.isSelected());
                    break;
                case 4: // ALL
                    florLS = florDAO.build().findAll();
                    break;
                default:
                    return;
            }
            mostrarProductos(florLS);

            clearFields();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


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


}
