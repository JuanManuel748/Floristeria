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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.IntegerStringConverter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    private static final File imgNull = new File("src/main/resources/com/github/JuanManuel/view/images/noPicture.jpg");


    private static Pedido ped;
    private static Detalles det;
    private static List<Producto> productoList = productoDAO.build().findAll();

    /**
     * Inicializa los componentes del controlador.
     * Se configura la tabla de productos, el ComboBox de productos, y el Spinner de cantidad.
     * Además, se carga la información del pedido actual y se muestra en la tabla.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ped = Session.getInstance().getPedido();
            det = new Detalles(ped);
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Configura los campos del formulario con los detalles del producto seleccionado.
     * Se actualiza la imagen de vista previa y los valores del producto y la cantidad.
     *
     * @param selected El detalle del producto seleccionado a mostrar.
     */
    public void setDetalle(Detalles selected) {
        det = selected;
        productChoice.setValue(selected.getPro());
        quantitySpinner.getValueFactory().setValue(selected.getCantidad());
        byte[] byteImage = selected.getPro().getImg();
        Image defaultImage = new Image(imgNull.toURI().toString());
        if (byteImage != null) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteImage);
            Image image = new Image(byteArrayInputStream);
            previewImage.setImage(image);
        } else {
            previewImage.setImage(defaultImage);
        }
    }

    /**
     * Limpia los campos del formulario, restableciendo el ComboBox y el Spinner.
     */
    public void clearFields() {
        productChoice.setValue(productoList.get(0));
        quantitySpinner.getValueFactory().setValue(1);
    }

    /**
     * Inicializa el ComboBox de productos con una lista de productos.
     * También se configura el renderizado para mostrar el nombre y la imagen del producto.
     *
     * @param c El ComboBox a inicializar.
     * @param array La lista de productos para cargar en el ComboBox.
     */
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

        c.setOnAction(this::setProducto);
    }

    /**
     * Actualiza la vista previa de la imagen cuando se selecciona un producto en el ComboBox.
     *
     * @param event El evento de acción que dispara la actualización de la imagen.
     */
    private void setProducto(Event event) {
        Producto select = (Producto) productChoice.getValue();
        byte[] byteImage = select.getImg();
        Image defaultImage = new Image(imgNull.toURI().toString());
        if (byteImage != null) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteImage);
            Image image = new Image(byteArrayInputStream);
            previewImage.setImage(image);
        } else {
            previewImage.setImage(defaultImage);
        }
    }

    /**
     * Muestra los detalles de un pedido en la tabla de productos.
     *
     * @param pedido El pedido cuyos detalles se deben mostrar.
     */
    public void mostrarDetalles(Pedido pedido) {
        List<Detalles> detallesList = pedido.getDetalles();
        productsTable.getItems().setAll(detallesList);
        productsTable.refresh();
    }

    /**
     * Valida los campos del formulario antes de realizar una operación.
     * Verifica que se haya seleccionado un producto y que la cantidad sea válida.
     *
     * @return true si los campos son válidos, false si no lo son.
     */
    public boolean validateFields() {
        det = new Detalles(ped);
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
            det.setPro(pro);
            det.setCantidad(cantidad);
            det.setSubtotal(pro.getPrecio()*cantidad);
            result = true;
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Error en la validación", "La validación no se ha podido completar");
            e.printStackTrace();
        }
        return result;

    }

    /**
     * Inserta un nuevo detalle en el pedido, verificando si el producto ya existe.
     * Si el producto ya está en el pedido, muestra una alerta informando del error.
     *
     * @param actionEvent El evento que dispara la acción de inserción.
     */
    public void insert(ActionEvent actionEvent) {
        List<Detalles> detailsList = ped.getDetalles();
        try {

            // Validar los campos antes de continuar
            if (!validateFields()) {
                return;
            }

            // Crear una nueva instancia de Detalles para evitar problemas de referencia
            Detalles nuevoDetalle = new Detalles(ped, det.getPro(), det.getCantidad());


            // Comprobar si el producto ya existe en la lista
            boolean encontrado = false;
            for (Detalles detalleExistente : detailsList) {
                Producto productoExistente = detalleExistente.getPro();
                Producto productoSeleccionado = nuevoDetalle.getPro();

                if (productoExistente.equals(productoSeleccionado)) {
                    encontrado = true;
                    break;
                }
            }

            // Si no se encontró el producto, se añade el nuevo detalle a la lista
            if (!encontrado) {
                detailsList.add(nuevoDetalle);
            } else {
                Alerta.showAlert("INFORMATION", "Producto ya en el pedido",
                        "El producto seleccionado ya estaba en el pedido, prueba a actualizarlo.");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            ped.setDetalles(detailsList);
            pedidoDAO.build().save(ped);
            Pedido temp = ped;
            ped = pedidoDAO.build().findByPK(temp);
            mostrarDetalles(ped);
            clearFields();
        }
    }

    /**
     * Actualiza un detalle en el pedido, cambiando la cantidad y subtotal del producto.
     * Si el producto no se encuentra en el pedido, muestra una alerta.
     *
     * @param actionEvent El evento que dispara la acción de actualización.
     */
    public void update(ActionEvent actionEvent) {

        List<Detalles> detailsList = ped.getDetalles();
        try {
            if(!validateFields()) {
                return;
            }

            Boolean find = false;
            for(Detalles detail: detailsList) {
                if (detail.getPro().equals(det.getPro())) {
                    detail.setCantidad(det.getCantidad());
                    detail.setSubtotal(det.getSubtotal());
                    find= true;
                    break;
                }
            }
            if (!find) {
                Alerta.showAlert("ERROR", "Producto no encontrado en el pedido", "El producto seleccionado no esta en el pedido, prueba a insertarlo");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            ped.setDetalles(detailsList);
            pedidoDAO.build().save(ped);
            Pedido temp = ped;
            ped = pedidoDAO.build().findByPK(temp);
            mostrarDetalles(ped);
            clearFields();
        }
    }

    /**
     * Elimina un producto del pedido.
     * Primero valida si el producto está en el pedido, y si es así, lo elimina. Si no, muestra una alerta.
     * Luego, actualiza la lista de detalles en el pedido y guarda los cambios.
     *
     * @param actionEvent El evento que dispara la acción de eliminación.
     */
    public void delete(ActionEvent actionEvent) {
        List<Detalles> detailsList = ped.getDetalles();
        try {
            // Validar los campos antes de continuar
            if (!validateFields()) {
                return;
            }

            boolean find = false;
            // Buscar si el producto existe en los detalles del pedido
            for (Detalles detail : detailsList) {
                if (detail.getPro().equals(det.getPro())) {
                    find = true;
                    break;
                }
            }

            // Si el producto no está en la lista, mostrar un mensaje de error
            if (!find) {
                Alerta.showAlert("ERROR", "Producto no encontrado en el pedido", "El producto seleccionado no está en el pedido, prueba a insertarlo");
            } else {
                // Eliminar el producto de la lista de detalles
                detailsList.remove(det);
                Alerta.showAlert("INFORMATION", "Producto eliminado del pedido", "El producto ha sido eliminado del pedido");
            }

        } catch (Exception e) {
            Alerta.showAlert("ERROR", "ERROR EN DELETE", "Ha habido un error al intentar borrar el producto");
            throw new RuntimeException(e);
        } finally {
            // Guardar los cambios en la base de datos y actualizar la vista
            ped.setDetalles(detailsList);
            pedidoDAO.build().save(ped);
            Pedido temp = ped;
            ped = pedidoDAO.build().findByPK(temp);
            mostrarDetalles(ped);
            clearFields();
        }
    }

    /**
     * Método que se ejecuta cuando se abre la vista o el controlador.
     * En este caso no tiene ninguna implementación específica.
     *
     * @param input Parámetro de entrada, si se proporciona, pero no se utiliza.
     * @throws Exception Si ocurre un error al abrir la vista.
     */
    @Override
    public void onOpen(Object input) throws Exception {
        // Este método está vacío, no realiza ninguna acción específica
    }

    /**
     * Redirige a la pantalla de inicio de administración de pedidos.
     *
     * @param actionEvent El evento que dispara la acción de cambio de escena.
     */
    public void goToHome(ActionEvent actionEvent) {
        try {
            // Cambiar a la escena de administración de pedidos
            App.currentController.changeScene(Scenes.ADMINPEDIDOS, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cierra la sesión del usuario actual y redirige a la pantalla de login.
     *
     * @param actionEvent El evento que dispara la acción de cierre de sesión.
     */
    public void logout(ActionEvent actionEvent) {
        try {
            User u = Session.getInstance().getCurrentUser();
            Session.getInstance().logOut(u);
            // Cambiar a la escena de login
            App.currentController.changeScene(Scenes.LOGIN, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método que se ejecuta cuando se cierra la vista o el controlador.
     * En este caso no tiene ninguna implementación específica.
     *
     * @param output Parámetro de salida, si se proporciona, pero no se utiliza.
     */
    @Override
    public void onClose(Object output) {
        // Este método está vacío, no realiza ninguna acción específica
    }

    /**
     * Maneja la selección de una fila en la tabla de productos.
     * Si se hace doble clic en un producto, se muestra su detalle en los campos correspondientes.
     *
     * @param event El evento de clic en la tabla.
     */
    private void handleTableSelection(MouseEvent event) {
        if (event.getClickCount() == 2) {
            // Obtener el detalle del producto seleccionado
            Detalles selected = productsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // Si un producto fue seleccionado, mostrar sus detalles
                setDetalle(selected);
            } else {
                // Si no hay producto seleccionado, mostrar una alerta
                Alerta.showAlert("ERROR", "Ningún producto seleccionado", "Haz clic en un producto para seleccionarlo.");
            }
        }
    }

}