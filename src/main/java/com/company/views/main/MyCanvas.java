package com.company.views.main;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class MyCanvas extends StackPane {
    public MyCanvas() {
        Rectangle rect = new Rectangle(100, 40, 100, 100);
        rect.setArcHeight(50);
        rect.setArcWidth(50);
        rect.setFill(Color.BLACK);

        TranslateTransition tt = new TranslateTransition(Duration.millis(2000), rect);
        tt.setByX(200f);
        tt.setCycleCount(Animation.INDEFINITE);
        tt.setAutoReverse(true);

        tt.play();

        this.getChildren().add(rect);
    }
}
