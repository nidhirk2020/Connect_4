module com.example.connect_4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.connect_4 to javafx.fxml;
    exports com.example.connect_4;
}