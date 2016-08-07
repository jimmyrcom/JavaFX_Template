package com.company;



import com.company.welcome.WelcomeScreen;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.StatusBar;

import java.net.URL;
import java.util.ResourceBundle;

public class MainCtrl implements Initializable {
    @FXML
    StatusBar statusBar;
    @FXML
    BorderPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SharedState.statusBar = statusBar;
        root.setCenter(new WelcomeScreen());
    }


}
