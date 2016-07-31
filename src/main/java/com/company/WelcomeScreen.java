package com.company;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeScreen extends GridPane implements Initializable {

    WelcomeScreen(){
        super();

        setPadding(new Insets(5, 10, 10, 10));
        this.setHgap(10);
        this.setVgap(10);
        TextField search = new TextField("");
        search.setPromptText("Search");
        GridPane.setMargin(search, new Insets(5, 0, 0, 0));
        add(search,0,0);

        TreeView samplesTreeView = new TreeView<>(new TreeItem<Object>());
        samplesTreeView.setShowRoot(false);
        samplesTreeView.setMinWidth(200);
        samplesTreeView.setMaxWidth(200);
        GridPane.setVgrow(samplesTreeView, Priority.ALWAYS);
        add(samplesTreeView, 0, 1);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);
        GridPane.setHgrow(tabPane, Priority.ALWAYS);
        GridPane.setVgrow(tabPane, Priority.ALWAYS);
        add(tabPane, 1, 0, 1, 2);

        Tab main = new Tab("Main");
        Tab docs = new Tab("Docs");

        tabPane.getTabs().setAll(main,docs);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
