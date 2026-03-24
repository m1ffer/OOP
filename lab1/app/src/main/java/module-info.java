module org.example.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.graphics;

    opens org.example.app to javafx.fxml;
    exports org.example.app;
    exports org.example.controllers;
    opens org.example.controllers to javafx.fxml;
}