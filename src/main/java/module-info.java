module com.github.JuanManuel {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.github.JuanManuel to javafx.fxml;
    exports com.github.JuanManuel;
}
