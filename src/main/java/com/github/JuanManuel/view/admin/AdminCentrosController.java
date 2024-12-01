package com.github.JuanManuel.view.admin;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.centroDAO;
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

public class AdminCentrosController extends Controller implements Initializable {
    private static File img;
    private static Centro centro;
    private static int secunController;
    private static List<Flor> floresPrimarias = florDAO.build().findByType(true);
    private static List<Flor> floresSecundarias = florDAO.build().findByType(false);
    private static final File imgNull = new File("src/main/resources/com/github/JuanManuel/view/images/noPicture.jpg");
    @FXML
    public TableView<Centro> centrosTable;
    @FXML
    public TableColumn<Centro, Integer> idColumn;
    @FXML
    public TableColumn<Centro, byte[]> imgColumn;
    @FXML
    public TableColumn<Centro, String> nameColumn;
    @FXML
    public TableColumn<Centro, Double> priceColumn;
    @FXML
    public TableColumn<Centro, Integer> stockColumn;
    @FXML
    public TableColumn<Centro, Long> descriptionColumn;
    @FXML
    public TableColumn<Centro, String> sizeColumn;
    @FXML
    public TableColumn<Centro, String> phraseColumn;
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
    public ComboBox sizeChoice;
    @FXML
    public TextField phraseField;
    @FXML
    public Button insertButton;
    @FXML
    public Button updateButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button findButton;
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
    public ImageView uploadButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        phraseColumn.setCellValueFactory(new PropertyValueFactory<>("frase"));
        setupSpinners();
        imgColumn.setCellValueFactory(new PropertyValueFactory<>("img"));
        imgColumn.setCellFactory(col -> new TableCell<Centro, byte[]>() {
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
        sizeChoice.setItems(observableArrayList("Pequeño (20cm)", "Mediano (30cm)", "Grande (40cm)"));
        sizeChoice.setValue("Pequeño (20cm)");

        initializaComboBoc(flPRChoice, floresPrimarias);
        initializaComboBoc(flSecunChoice1, floresSecundarias);
        initializaComboBoc(flSecunChoice2, floresSecundarias);
        initializaComboBoc(flSecunChoice3, floresSecundarias);

        centrosTable.setOnMouseClicked(event -> handleTableSelection(event));
        mostrarProductos(centroDAO.build().findAll());

    }

    public void setCentro(Centro p) {
        try {
            centro = p;
            idField.setText(String.valueOf(p.getIdCentro()));
            nameField.setText(p.getNombre());
            priceField.getValueFactory().setValue(p.getPrecio());
            stockField.getValueFactory().setValue(p.getStock());
            descriptionField.setText(p.getDescripcion());
            sizeChoice.setValue(p.getSize());
            phraseField.setText(p.getFrase());
            flPRChoice.setValue(p.getFlorPr());
            flSecunChoice1.setValue(p.getFloresSecun().get(0));
            flSecunChoice2.setValue(p.getFloresSecun().get(1));
            flSecunChoice3.setValue(p.getFloresSecun().get(2));
            sizeChoice.setValue(p.getSize());
            if (p.getDescripcion().contains("prehecho")) {
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
            String tamanio = sizeChoice.getValue().toString();
            String frase = phraseField.getText().toString();
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

            if (centro == null) {
                centro = new Centro();
            }
            centro.setIdCentro(id);
            centro.setIdProducto(id);
            centro.setNombre(nombre);
            centro.setPrecio(price);
            centro.setStock(stock);
            centro.setDescripcion(description);
            centro.setFlorPr(pr);
            centro.setSize(tamanio);
            centro.setFrase(frase);
            centro.setFloresSecun(floresSecun);
            centro.setTipo("centro");
            if(predeterminatedCheck.isSelected()) {
                if (!description.contains("prehecho")) {
                    centro.setDescripcion(description + " prehecho");
                }
            } else {
                if (!description.contains("personalizado")) {
                    centro.setDescripcion(description + " personalizado");
                }
            }
            result = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    public void insert(ActionEvent actionEvent) {
        try {
            if (!validateFields()) {
                return;
            }
            centroDAO.build().insertCentro(centro);
            Alerta.showAlert("INFORMATION", "Producto insertado", "El producto ha sido INSERTADO en la base de datos exitosamente");
            mostrarProductos(centroDAO.build().findAll());
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO insertado", "Error al INSERTADO el producto.");
            e.printStackTrace();
        }
    }

    public void update(ActionEvent actionEvent) {
        try {
            if (!validateFields()) {
                return;
            }
            centroDAO.build().updateCentro(centro);
            Alerta.showAlert("INFORMATION", "Producto actualizado", "El producto ha sido ACTUALIZADO en la base de datos exitosamente");
            mostrarProductos(centroDAO.build().findAll());
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO actualizado", "Error al ACTUALIZADO el producto.");
            e.printStackTrace();
        }
    }

    public void delete(ActionEvent actionEvent) {
        try {
            if (!validateFields()) {
                return;
            }
            centroDAO.build().delete(centro);
            Alerta.showAlert("INFORMATION", "Producto eliminado", "El producto ha sido ELIMINADO en la base de datos exitosamente");
            mostrarProductos(centroDAO.build().findAll());
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Producto NO eliminado", "Error al ELIMINADO el producto.");
            e.printStackTrace();
        }
    }

    public void find(ActionEvent actionEvent) {
        try {
            List<Centro> findLS = new ArrayList<>();
            switch (selectAlert()) {
                case 1:
                    int id = Integer.parseInt(idField.getText().trim());
                    Centro item = centroDAO.build().findByPK(new Centro(id));
                    if (item != null) {
                        findLS.add(item);
                    }
                    break;
                case 2:
                    String name = nameField.getText().trim();
                    findLS = centroDAO.build().findByNames(name);
                    break;
                case 3:
                    findLS = centroDAO.build().findByType(predeterminatedCheck.isSelected());
                    break;
                case 4:
                    findLS = centroDAO.build().findAll();
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

                if (centro == null) {
                    centro = new Centro();
                }

                byte[] imageBytes = java.nio.file.Files.readAllBytes(selectedFile.toPath());
                centro.setImg(imageBytes);
            } else {
                Alerta.showAlert("INFORMATION", "No se seleccionó ninguna imagen", "Por favor selecciona una imagen.");
            }
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Error al cargar imagen", "No se pudo cargar la imagen seleccionada.");
        }
    }

    private void setupSpinners() {
        priceField.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, Double.MAX_VALUE, 0.00, 0.10));
        priceField.getValueFactory().setConverter(new DoubleStringConverter());
        stockField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        stockField.getValueFactory().setConverter(new IntegerStringConverter());
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
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

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

    private void handleTableSelection(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Centro selected = centrosTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                setCentro(selected);
            } else {
                Alerta.showAlert("ERROR", "Ningún producto seleccionado", "Haz clic en un producto para seleccionarlo.");
            }
        }
    }

    public void mostrarProductos(List<Centro> array){
        ObservableList<Centro> observableList = FXCollections.observableArrayList(array);
        centrosTable.setItems(observableList);
        centrosTable.refresh();
    }

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

        phraseField.setText("");
        sizeChoice.setValue("Pequeño (20cm)");


        img = null;
        centro = new Centro();
    }

}

