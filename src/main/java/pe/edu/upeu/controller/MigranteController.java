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

/**
 * CONTROLADOR
 * Esta clase es el "Cerebro" de nuestra interfaz.
 * Conecta los botones y campos de texto (Vista) con los datos (Modelo y Repositorio).
 */
public class MigranteController {

    // --- ELEMENTOS DE LA INTERFAZ GRÁFICA (@FXML) ---
    // Estas variables están conectadas directamente al archivo FXML
    @FXML private TextField txtNombre;
    @FXML private TextField txtPais;
    @FXML private ComboBox<String> cbVisa;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cbEstatus;
    @FXML private Button btnGuardar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private TableView<Migrante> tvMigrantes;

    // --- NUESTRA BASE DE DATOS Y LISTA DE TABLA ---
    // Usamos el Service para comunicarnos con el Repositorio (Buenas prácticas)
    private MigranteServiceInter service = new MigranteServiceImp();
    
    // Una lista "Observable" especial de JavaFX que avisa a la tabla cuando hay cambios
    private ObservableList<Migrante> migrantesList = FXCollections.observableArrayList();

    // Variable para saber qué fila de la tabla hemos hecho clic (-1 significa que no hay nada seleccionado)
    private int indiceSeleccionado = -1;

    /**
     * MÉTODO INITIALIZE
     * Se ejecuta automáticamente cuando se abre la ventana.
     * Aquí configuramos cómo se ven y comportan los elementos al inicio.
     */
    @FXML
    public void initialize() {
        // 1. Llenar los ComboBox con las opciones pedidas en el ejercicio
        cbEstatus.setItems(FXCollections.observableArrayList(
                "Regular", "Irregular", "Solicitante de asilo"));
        cbVisa.setItems(FXCollections.observableArrayList(
                "Turista", "Estudiante", "Trabajo", "Residente", "Otro"));

        // 2. Aplicar un filtro para que los nombres y países se pongan en Mayúscula automáticamente
        aplicarFormatoMayuscula(txtNombre);
        aplicarFormatoMayuscula(txtPais);
        
        // 3. Configurar la fecha para que se pueda escribir a mano en formato dd/MM/yyyy
        configurarFormatoFecha();

        // 4. Reglas de Validación de Botones:
        // Desactivar el botón Guardar si falta llenar algún campo
        btnGuardar.disableProperty().bind(
                txtNombre.textProperty().isEmpty()
                        .or(txtPais.textProperty().isEmpty())
                        .or(cbVisa.valueProperty().isNull())
                        .or(dpFecha.valueProperty().isNull())
                        .or(cbEstatus.valueProperty().isNull()));

        // Desactivar el botón Limpiar si todos los campos ya están vacíos
        btnLimpiar.disableProperty().bind(
                txtNombre.textProperty().isEmpty()
                        .and(txtPais.textProperty().isEmpty())
                        .and(cbVisa.valueProperty().isNull())
                        .and(dpFecha.valueProperty().isNull())
                        .and(cbEstatus.valueProperty().isNull()));

        // Desactivar el botón Eliminar si no hemos seleccionado a nadie en la tabla
        btnEliminar.disableProperty().bind(
                tvMigrantes.getSelectionModel().selectedItemProperty().isNull());

        // 5. Configurar las Columnas de la Tabla para que lean los atributos de la clase Migrante
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

        // Añadimos las columnas a la tabla
        tvMigrantes.getColumns().addAll(colNombre, colPais, colVisa, colFecha, colEstatus);

        // 6. Cargar los datos a la tabla (al principio estará vacía)
        actualizarTabla();

        // 7. Evento: ¿Qué pasa cuando hacemos clic en una fila de la tabla?
        tvMigrantes.getSelectionModel().selectedIndexProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && newSelection.intValue() >= 0) {
                // Guardamos el índice (posición) seleccionado
                indiceSeleccionado = newSelection.intValue();
                // Obtenemos el migrante de esa fila
                Migrante m = tvMigrantes.getItems().get(indiceSeleccionado);
                // Ponemos sus datos en los campos de texto para poder editarlos
                txtNombre.setText(m.getNombreCompleto());
                txtPais.setText(m.getPaisOrigen());
                cbVisa.setValue(m.getTipoVisa());
                dpFecha.setValue(m.getFechaIngreso());
                cbEstatus.setValue(m.getStatusMigratorio());
            }
        });
    }

    /**
     * MÉTODO GUARDAR
     * Se ejecuta cuando presionamos el botón "Guardar".
     */
    @FXML
    void guardar(ActionEvent event) {
        // 1. Creamos un nuevo objeto Migrante con lo que está escrito en el formulario
        Migrante m = new Migrante(
                txtNombre.getText(),
                txtPais.getText(),
                cbVisa.getValue(),
                dpFecha.getValue(),
                cbEstatus.getValue());

        // 2. Revisamos si estamos creando uno nuevo o actualizando uno existente
        if (indiceSeleccionado == -1) {
            // Si el índice es -1, significa que no seleccionamos nada en la tabla, es alguien NUEVO.
            service.agregarMigrante(m);
        } else {
            // Si el índice es diferente de -1, es porque hicimos clic en la tabla, vamos a ACTUALIZAR.
            service.actualizarMigrante(m, indiceSeleccionado);
            indiceSeleccionado = -1; // Reseteamos el índice
        }

        // 3. Limpiamos las cajas de texto y refrescamos la tabla para que aparezca
        limpiar(null);
        actualizarTabla();
    }

    /**
     * MÉTODO ELIMINAR
     * Se ejecuta cuando presionamos el botón "Eliminar".
     */
    @FXML
    void eliminar(ActionEvent event) {
        // Si hay una fila seleccionada...
        if (indiceSeleccionado != -1) {
            // Lo borramos mandando su posición
            service.eliminarMigrante(indiceSeleccionado);
            indiceSeleccionado = -1; // Reseteamos
            limpiar(null); // Vaciamos los campos
            actualizarTabla(); // Actualizamos la tabla para que desaparezca
        }
    }

    /**
     * MÉTODO LIMPIAR
     * Vacía todos los campos del formulario.
     */
    @FXML
    void limpiar(ActionEvent event) {
        txtNombre.clear();
        txtPais.clear();
        cbVisa.setValue(null);
        dpFecha.setValue(null);
        cbEstatus.setValue(null);
        indiceSeleccionado = -1;
        tvMigrantes.getSelectionModel().clearSelection(); // Deseleccionamos la tabla
    }

    /**
     * MÉTODO ACTUALIZAR TABLA
     * Pide los datos al repositorio y los pinta en la tabla visual.
     */
    private void actualizarTabla() {
        migrantesList.clear(); // Borramos lo visual
        migrantesList.addAll(service.listarMigrantes()); // Traemos lo real del repositorio
        tvMigrantes.setItems(migrantesList); // Se lo pasamos a la tabla
    }
    
    /**
     * CONFIGURAR FORMATO DE FECHA
     * Permite que el DatePicker acepte texto escrito a mano en formato dd/MM/yyyy
     */
    private void configurarFormatoFecha() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        dpFecha.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                // Convierte de Fecha a Texto para mostrarlo
                if (date != null) {
                    return formato.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                // Convierte el Texto escrito a mano a una Fecha interna
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, formato);
                    } catch (DateTimeParseException e) {
                        return null; // Si escribe mal la fecha, no hace nada
                    }
                } else {
                    return null;
                }
            }
        });
        
        // Poner un texto de fondo como ayuda visual
        dpFecha.setPromptText("Ej. 12/04/2028");
    }

    /**
     * MÉTODO EXTRA: FORMATO MAYÚSCULA
     * Pone la primera letra de cada palabra en mayúscula mientras escribes.
     */
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
