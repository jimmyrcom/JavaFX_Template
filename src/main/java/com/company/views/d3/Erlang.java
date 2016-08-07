package com.company.views.d3;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Created by fold on 8/6/16.
 */
public class Erlang extends StackPane {
    public Erlang() {
        super();
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load("http://erlang.org");
        this.getChildren().add(browser);
    }
}
