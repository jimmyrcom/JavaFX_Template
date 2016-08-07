package com.company.views.main;


import com.company.SharedState;
import com.company.util.Util;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.commonmark.html.HtmlRenderer;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class Main extends StackPane {
    public Main() {
        super();

        Parser parser = Parser.builder().build();
        Node document = null;
        InputStream stream = getClass().getResourceAsStream("/md/Main.md");
        document = parser.parse(Util.streamToString(stream));
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String markdown = renderer.render(document);

        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        // file:/home/fold/JavaFX_Template/build/resources/main/html/d3/d3_example.html
        webEngine.loadContent("<style>body { margin: 30px; }</style>" + markdown);
        this.getChildren().add(browser);
    }
}
