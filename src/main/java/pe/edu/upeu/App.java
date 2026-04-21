package pe.edu.upeu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Cargamos la vista principal
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_migrantes.fxml"));

        // Configuramos la ventana para que se ajuste a la pantalla
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        // Creamos la escena y le agregamos BootstrapFX para los estilos
        Scene scene = new Scene(loader.load(), bounds.getWidth(), bounds.getHeight() - 100);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        stage.setScene(scene);
        stage.setTitle("Sistema de Padrón de Migrantes");
        stage.show();
    }
}
