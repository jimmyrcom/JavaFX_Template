package com.company.views.d3;

import com.company.SharedState;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Created by fold on 8/6/16.
 */
public class D3 extends StackPane {
    public D3() {
        super();
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        // file:/home/fold/JavaFX_Template/build/resources/main/html/d3/d3_example.html
        String url = getClass().getResource("/html/d3/d3_example.html").toExternalForm();
        webEngine.load(url);
        this.getChildren().add(browser);
        SharedState.statusBar.setText("D3 is so cool...");
    }
}
