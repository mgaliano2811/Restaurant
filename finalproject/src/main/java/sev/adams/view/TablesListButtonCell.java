package sev.adams.view;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;

// A cell that acts like a button, so we can react to it being pressed
//  The info for an individual cell is two values seperated by a colon
    //  tableId:tableCapacity (stored in this.item)
public class TablesListButtonCell extends ListCell<String> {
    // The button for this cell
    private Button cellButton;
    
    // The Table information of the table this cell represents is stored in its item
    //  It is formatted as a key value list in a string, like so:
    //  "TableID:(tableID),Capacity:(capacity),CustomerGroup:(customerGroup)"
    //  Though customerGroup may not be assigned
    public TablesListButtonCell() {
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
    //  The info for an individual cell is two values seperated by a colon
    //  So tableData is: "tableId:tableCapacity"
    @Override
    protected void updateItem(String tableData, boolean empty) {
        super.updateItem(tableData, empty);

        if (empty || tableData == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(tableData);
            cellButton.setText("Table Info");
            setGraphic(cellButton);
        }
    }
}
