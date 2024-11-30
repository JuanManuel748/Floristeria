package com.github.JuanManuel.view.admin;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.florDAO;
import com.github.JuanManuel.model.DAO.productoDAO;
import com.github.JuanManuel.model.DAO.ramoDAO;
import com.github.JuanManuel.model.DAO.userDAO;
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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
        secunController = 0;
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("colorEnvol"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("cantidadFlores"));

        imgColumn.setCellValueFactory(new PropertyValueFactory<>("img"));
        imgColumn.setCellFactory(col -> new TableCell<Ramo, byte[]>() {
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
                        setGraphic(null);
                    }
                }
            }
        });


        ramosTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Ramo selected = ramosTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    setRamo(selected);
                }
            }
        });

        priceField.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, Double.MAX_VALUE, 0.00, 0.10));
        priceField.getValueFactory().setConverter(new DoubleStringConverter());
        stockField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        stockField.getValueFactory().setConverter(new IntegerStringConverter());

        quantitySpinner.setItems(observableArrayList("3 Flores", "5 Flores", "8 Flores", "10 Flores", "15 Flores"));
        quantitySpinner.setValue("3 Flores");

        colorField.setItems(FXCollections.observableArrayList(
                "Rojo", "Verde", "Azul", "Amarillo", "Negro", "Blanco", "Naranja", "Morado",
                "Rosa", "Gris", "Cian", "Turquesa", "Marrón", "Violeta", "Fucsia", "Oliva",
                "Oro", "Plata", "Beige", "Índigo", "Transparente"
        ));
        colorField.setValue("Transparente");

        initializaComboBoc(flPRChoice, floresPrimarias);
        initializaComboBoc(flSecunChoice1, floresSecundarias);
        initializaComboBoc(flSecunChoice2, floresSecundarias);
        initializaComboBoc(flSecunChoice3, floresSecundarias);

        List<Ramo> ls = ramoDAO.build().findAll();
        mostrarProductos(ls);
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
    }


    public void setRamo(Ramo p) {
        idField.setText(String.valueOf(p.getIdProducto()));
        nameField.setText(p.getNombre());
        priceField.getValueFactory().setValue(p.getPrecio());
        stockField.getValueFactory().setValue(p.getStock());
        descriptionField.setText(p.getDescripcion());
        /////////////////////
        colorField.setValue(p.getColorEnvol());
        quantitySpinner.setValue(p.getCantidadFlores() + " Flores");

        flPRChoice.setValue(p.getFlorPr());
        flSecunChoice1.setValue(p.getFloresSecun().get(0));
        flSecunChoice2.setValue(p.getFloresSecun().get(1));
        flSecunChoice3.setValue(p.getFloresSecun().get(2));

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

        ramo = p;
    }

    private byte[] imgToByteArray(File file) {
        try {
            return java.nio.file.Files.readAllBytes(file.toPath());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void mostrarProductos(List<Ramo> array) {
        ObservableList<Ramo>productoObservableList = FXCollections.observableArrayList(array);
        ramosTable.setItems(productoObservableList);
        ramosTable.refresh();
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
        predeterminatedCheck.setSelected(false);
        quantitySpinner.setValue("3 Flores");
        colorField.setValue("Transparente");
        flPRChoice.setValue(flPRChoice.getItems().get(0));
        flSecunChoice1.setValue(flSecunChoice1.getItems().get(0));
        flSecunChoice2.setValue(flSecunChoice1.getItems().get(1));
        flSecunChoice3.setValue(flSecunChoice1.getItems().get(2));
        img = imgNull;
        ramo = null;
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
                if (ramo == null) {
                    ramo = new Ramo();
                }
                ramo.setImg(img);

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
            String description = descriptionField.getText();
            Flor flPR = (Flor) flPRChoice.getValue();
            Boolean prehecho = predeterminatedCheck.isSelected();
            List<Flor> floresSecun = new ArrayList<>();
            floresSecun.add((Flor) flSecunChoice1.getValue());
            floresSecun.add((Flor) flSecunChoice2.getValue());
            floresSecun.add((Flor) flSecunChoice3.getValue());
            String color = String.valueOf(colorField.getValue());

            int cantidad = Integer.parseInt(quantitySpinner.getValue().toString().substring(0, 2).trim());

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

            if(uploadButton.getImage() == null) {
                if (!confirm("Imagen vacia", "No has puesto ningun imagen")) {
                    return result;
                } else {
                    uploadImage(null);
                }
            }

            ramo.setIdRamo(id);
            ramo.setIdProducto(id);
            ramo.setNombre(nombre);
            ramo.setPrecio(precio);
            ramo.setStock(stock);
            ramo.setDescripcion(description);
            ramo.setFlorPr(flPR);
            ramo.setCantidadFlores(cantidad);
            ramo.setColorEnvol(color);
            ramo.setFloresSecun(floresSecun);
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

            if (img != null && img.exists()) {
                ramo.setImg(imgToByteArray(img));
            }

            ramoDAO.build().insertRamo(ramo);
            Alerta.showAlert("INFORMATION", "Ramo insertado", "El ramo ha sido insertado en la base de datos exitosamente");

            List<Ramo> allProducts = ramoDAO.build().findAll();
            mostrarProductos(allProducts);

            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Ramo NO insertado", "El ramo NO ha sido insertado en la base de datos exitosamente.\nRevisa que la id no este en uso");
            throw new RuntimeException(e);
        }
    }

    public void update(ActionEvent actionEvent) {
        try {
            if (!validateFields()) {
                return;
            }

            if (img != null && img.exists()) {
                ramo.setImg(imgToByteArray(img)); // Solo se actualiza la imagen si se ha seleccionado una nueva
            } else if (ramo.getImg() == null || ramo.getImg().length == 0) {
                // No hacer nada en caso de que no se seleccione una nueva imagen,
                // y si la imagen no es nula, no modificarla
                // No asignar null a ramo.setImg()
            } // Aquí no es necesario asignar null a la imagen si no se ha actualizado


            ramoDAO.build().updateRamo(ramo);
            Alerta.showAlert("INFORMATION", "Ramo actualizado", "El ramo ha sido actualizado en la base de datos exitosamente");

            List<Ramo> allProducts = ramoDAO.build().findAll();
            mostrarProductos(allProducts);

            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Ramo NO actualizado", "El ramo NO ha sido actualizado en la base de datos exitosamente.\nRevisa que la id no este en uso");
            throw new RuntimeException(e);
        }
    }

    public void delete(ActionEvent actionEvent) {
        try {
            ramoDAO.build().delete(new Ramo(Integer.parseInt(idField.getText().trim())));
            Alerta.showAlert("INFORMATION", "Ramo eliminado", "El ramo ha sido eliminado en la base de datos exitosamente");

            List<Ramo> allProducts = ramoDAO.build().findAll();
            mostrarProductos(allProducts);

            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Ramo NO eliminado", "El ramo NO ha sido eliminado en la base de datos exitosamente.\nRevisa que la id no este en uso");
            throw new RuntimeException(e);
        }
    }

    public void find(ActionEvent actionEvent) {
        try {
            List<Ramo> lista = new ArrayList<>();

            switch (selectAlert()) {
                case 1: // ID
                    lista.add(ramoDAO.build().findByPK(new Ramo(Integer.parseInt(idField.getText().trim()))));
                    break;
                case 2: // NOMBRE
                    lista = ramoDAO.build().findByName(nameField.getText().trim());
                    break;
                case 3: // TIPO
                    lista = ramoDAO.build().findByType(predeterminatedCheck.isSelected());
                    break;
                case 4: // ALL
                    lista = ramoDAO.build().findAll();
                    break;
                default:
                    return;
            }
            mostrarProductos(lista);

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
