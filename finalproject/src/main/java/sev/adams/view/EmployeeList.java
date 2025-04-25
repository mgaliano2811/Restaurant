package sev.adams.view;

import java.util.ArrayList;

import javafx.scene.control.ListView;

public class EmployeeList extends ListView<String>{

    // Create a new render with all of the given employee strings, simple
    public void renderEmployees(ArrayList<String> allEmployeeStrings) {
        this.getItems().clear();

        for (String employeeString : allEmployeeStrings) {
            this.getItems().add(employeeString);
        }
    }
    
}
