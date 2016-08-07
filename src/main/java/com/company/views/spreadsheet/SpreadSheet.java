package com.company.views.spreadsheet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

/**
 * Created by fold on 8/7/16.
 */
public class SpreadSheet extends StackPane {
    public SpreadSheet() {
        int rowCount = 15;
        int columnCount = 15;
        GridBase grid = new GridBase(rowCount, columnCount);

        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        for (int row = 0; row < grid.getRowCount(); ++row) {
            final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
            for (int column = 0; column < grid.getColumnCount(); ++column) {
                list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1, Integer.toString(row*column)));
            }
            rows.add(list);
        }
        grid.setRows(rows);

        SpreadsheetView spv = new SpreadsheetView(grid);
        getChildren().add(spv);
    }
}
