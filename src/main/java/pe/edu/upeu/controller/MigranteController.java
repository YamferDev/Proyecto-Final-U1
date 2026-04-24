package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import pe.edu.upeu.model.Migrante;
import pe.edu.upeu.service.MigranteServiceImp;
import pe.edu.upeu.service.MigranteServiceInter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MigranteController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtPais;
    @FXML private ComboBox<String> cbVisa;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cbEstatus;
    @FXML private Button btnGuardar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private TableView<Migrante> tvMigrantes;

    private MigranteServiceInter service = new MigranteServiceImp();
    
    private ObservableList<Migrante> migrantesList = FXCollections.observableArrayList();

    private int indiceSeleccionado = -1;

    @FXML
    public void initialize() {
        cbEstatus.setItems(FXCollections.observableArrayList(
                "Regular", "Irregular", "Solicitante de asilo"));
        cbVisa.setItems(FXCollections.observableArrayList(
                "Turista", "Estudiante", "Trabajo", "Residente", "Otro"));

        aplicarFormatoMayuscula(txtNombre);
        aplicarFormatoMayuscula(txtPais);
        
        configurarFormatoFecha();

        btnGuardar.disableProperty().bind(
                txtNombre.textProperty().isEmpty()
                        .or(txtPais.textProperty().isEmpty())
                        .or(cbVisa.valueProperty().isNull())
                        .or(dpFecha.valueProperty().isNull())
                        .or(cbEstatus.valueProperty().isNull()));

        btnLimpiar.disableProperty().bind(
                txtNombre.textProperty().isEmpty()
                        .and(txtPais.textProperty().isEmpty())
                        .and(cbVisa.valueProperty().isNull())
                        .and(dpFecha.valueProperty().isNull())
                        .and(cbEstatus.valueProperty().isNull()));

        btnEliminar.disableProperty().bind(
                tvMigrantes.getSelectionModel().selectedItemProperty().isNull());

        TableColumn<Migrante, String> colNombre = new TableColumn<>("Nombre Completo");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));

        TableColumn<Migrante, String> colPais = new TableColumn<>("País");
        colPais.setCellValueFactory(new PropertyValueFactory<>("paisOrigen"));

        TableColumn<Migrante, String> colVisa = new TableColumn<>("Visa");
        colVisa.setCellValueFactory(new PropertyValueFactory<>("tipoVisa"));

        TableColumn<Migrante, LocalDate> colFecha = new TableColumn<>("Fecha Ingreso");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaIngreso"));

        TableColumn<Migrante, String> colEstatus = new TableColumn<>("Estatus");
        colEstatus.setCellValueFactory(new PropertyValueFactory<>("statusMigratorio"));

        tvMigrantes.getColumns().addAll(colNombre, colPais, colVisa, colFecha, colEstatus);

        actualizarTabla();

        tvMigrantes.getSelectionModel().selectedIndexProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && newSelection.intValue() >= 0) {
                indiceSeleccionado = newSelection.intValue();
                Migrante m = tvMigrantes.getItems().get(indiceSeleccionado);
                txtNombre.setText(m.getNombreCompleto());
                txtPais.setText(m.getPaisOrigen());
                cbVisa.setValue(m.getTipoVisa());
                dpFecha.setValue(m.getFechaIngreso());
                cbEstatus.setValue(m.getStatusMigratorio());
            }
        });
    }

    @FXML
    void guardar(ActionEvent event) {
        Migrante m = new Migrante(
                txtNombre.getText(),
                txtPais.getText(),
                cbVisa.getValue(),
                dpFecha.getValue(),
                cbEstatus.getValue());

        if (indiceSeleccionado == -1) {
            service.agregarMigrante(m);
        } else {
            service.actualizarMigrante(m, indiceSeleccionado);
            indiceSeleccionado = -1;
        }

        limpiar(null);
        actualizarTabla();
    }

    @FXML
    void eliminar(ActionEvent event) {
        if (indiceSeleccionado != -1) {
            service.eliminarMigrante(indiceSeleccionado);
            indiceSeleccionado = -1;
            limpiar(null);
            actualizarTabla();
        }
    }

    @FXML
    void limpiar(ActionEvent event) {
        txtNombre.clear();
        txtPais.clear();
        cbVisa.setValue(null);
        dpFecha.setValue(null);
        cbEstatus.setValue(null);
        indiceSeleccionado = -1;
        tvMigrantes.getSelectionModel().clearSelection();
    }

    private void actualizarTabla() {
        migrantesList.clear();
        migrantesList.addAll(service.listarMigrantes());
        tvMigrantes.setItems(migrantesList);
    }
    
    private void configurarFormatoFecha() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        dpFecha.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return formato.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, formato);
                    } catch (DateTimeParseException e) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        });
        
        dpFecha.setPromptText("Ej. 12/04/2028");
    }

    private void aplicarFormatoMayuscula(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                String[] palabras = newValue.split(" ");
                StringBuilder textoFormateado = new StringBuilder();
                for (int i = 0; i < palabras.length; i++) {
                    String palabra = palabras[i];
                    if (palabra.length() > 0) {
                        textoFormateado.append(Character.toUpperCase(palabra.charAt(0)));
                        if (palabra.length() > 1) {
                            textoFormateado.append(palabra.substring(1).toLowerCase());
                        }
                    }
                    if (i < palabras.length - 1 || newValue.endsWith(" ")) {
                        textoFormateado.append(" ");
                    }
                }
                String resultado = textoFormateado.toString();
                if (!newValue.equals(resultado)) {
                    javafx.application.Platform.runLater(() -> {
                        int posicionCursor = textField.getCaretPosition();
                        textField.setText(resultado);
                        textField.positionCaret(posicionCursor);
                    });
                }
            }
        });
    }
}
