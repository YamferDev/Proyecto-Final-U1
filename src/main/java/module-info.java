module pe.edu.upeu {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;

    requires static lombok;

    opens pe.edu.upeu.controller to javafx.fxml;
    opens pe.edu.upeu.model to javafx.base; // Para PropertyValueFactory de la tabla
    exports pe.edu.upeu;
    exports pe.edu.upeu.model;
}