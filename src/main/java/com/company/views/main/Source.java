package com.company.views.main;

import com.company.util.Util;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

import java.io.InputStream;

/**
 * Created by fold on 8/7/16.
 */
public class Source extends StackPane{

    public Source() {
        super();
        TextArea txt = new TextArea();
        InputStream stream = getClass().getResourceAsStream("/md/Main.md");
        txt.setText(Util.streamToString(stream));
        this.getChildren().add(txt);
    }
}
