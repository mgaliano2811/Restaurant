module sev.adams {
    requires javafx.controls;
    requires javafx.fxml;

    opens sev.adams to javafx.fxml;
    exports sev.adams;
}
