package com.company;
/* BSD license
Copyright (c) 2015 Jimmy Ruska (jimmyruska@gmail.com)
All rights reserved.

Redistribution and use in source and binary forms are permitted
provided that the above copyright notice and this paragraph are
duplicated in all such forms and that any documentation,
advertising materials, and other materials related to such
distribution and use acknowledge that the software was developed
by the <organization>. The name of the
<organization> may not be used to endorse or promote products derived
from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DefaultContentCtrl implements Initializable {

    @FXML
    BorderPane content;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Button nextScene = new Button("Next Scene");
        try {
            //Parent root = FXMLLoader.load(getClass().getResource("/fxml/ContentBody2.fxml"));
            WelcomeScreen root = new WelcomeScreen();
            nextScene.setOnMouseClicked(x -> {
                SharedState.statusBar.setText("Welcome");
                content.setCenter(root);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //((StatusBar)content.getParent().getScene().lookup("#statusBar")).setText("Changed");
        //SharedState.statusBar.setText("Changed");
        content.setCenter(nextScene);
    }

    public DefaultContentCtrl(){
        //

    }


}
