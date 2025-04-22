module sev.adams {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens sev.adams to javafx.fxml;
    opens sev.adams.controller to javafx.fxml;
    opens sev.adams.model to javafx.fxml;
    opens sev.adams.view to javafx.fxml;
    opens sev.adams.observer to javafx.fxml;
    exports sev.adams;
    exports sev.adams.controller;
    exports sev.adams.model;
    exports sev.adams.view;
    exports sev.adams.observer;
    
    // Restaurant stuff
    exports restaurant;
}
