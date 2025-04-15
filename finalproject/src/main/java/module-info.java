module sev.adams {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens sev.adams to javafx.fxml;
    exports sev.adams;
}
