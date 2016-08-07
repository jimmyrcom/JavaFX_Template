package com.company;
// Template by JimmyRuska@gmail.com based on FXSampler
// https://bitbucket.org/controlsfx/controlsfx/src/
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

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import com.company.util.SQL;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Make an FXML loader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
        // Set the controller for the FXML
        loader.setController(new MainCtrl());
        // Turn FXML into class
        Parent root = loader.load();
        // hostingServices is used to launch the default OS browser
        SharedState.hostServices = getHostServices();

        // Save primary stage to sharedstate

        // Make FXML Max screen size
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        // Set window title
        primaryStage.setTitle(SharedState.APP_NAME);
        // Set window icon
        primaryStage.getIcons().add(new Image("/image/1470528934_cafe_coffee_32.png"));
        // Set Scene
        primaryStage.setScene(new Scene(root));
        // when you close the window make the java app stop
        primaryStage.setOnCloseRequest(x -> {
                Platform.exit();
                System.exit(0);
        });
        SharedState.stage = primaryStage;
        // Show window
        primaryStage.show();
    }

    public static void main(String[] args) {
        SQL.create_db();
        // Start FXML when java starts
        launch(args);
        SharedState.makeMenu();
    }


    public static void clojureInterop(){
        IFn require = Clojure.var("clojure.core", "require");
        require.invoke(Clojure.read("com.company.core"));
        IFn foo = Clojure.var("com.company.core", "foo");
        foo.invoke();
    }
}
