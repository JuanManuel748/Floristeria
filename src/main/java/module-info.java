module com.github.JuanManuel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires com.h2database;

    opens com.github.JuanManuel.view to javafx.fxml;
    exports com.github.JuanManuel.view;
    exports com.github.JuanManuel;
    opens com.github.JuanManuel to javafx.fxml;
    exports com.github.JuanManuel.model.entity;
}
