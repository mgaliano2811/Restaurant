package sev.adams.view;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;

public class CustomersListButtonCell extends ListCell<String> {
    // The button for this cell
    Button cellButton;
    
    public CustomersListButtonCell() {
        cellButton = new Button(this.getText());
        // Set up what the button does when its pressed
        cellButton.setOnAction(event -> 
            {
            String item = this.getItem();
            System.out.println(item);
            }
        );
    }


    // How we handle the info for this cell
    //  The info for an individual cell is 
    @Override
    protected void updateItem(String customerData, boolean empty) {
        super.updateItem(customerData, empty);

        if (empty || customerData == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(customerData);
            cellButton.setText("Customer Group Info");
            setGraphic(cellButton);
        }
    }
}
