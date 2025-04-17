package sev.adams.view;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;

// A cell that acts like a button, so we can react to it being pressed
//  The info for an individual cell is two values seperated by a colon
    //  tableId:tableCapacity (stored in this.item)
public class TablesListButtonCell extends ListCell<String> {
    // The button for this cell
    Button cellButton;
    
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
            String tableName = "Table " + tableData.split(":")[0];
            String tableCapacity = "Capacity " + tableData.split(":")[1];

            setText(tableName + " | " + tableCapacity);
            cellButton.setText("Table Info");
            setGraphic(cellButton);
        }
    }
}
