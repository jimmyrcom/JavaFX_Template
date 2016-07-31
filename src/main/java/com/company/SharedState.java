package com.company;

import javafx.application.HostServices;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;

/**
 * Created by fold on 7/29/16.
 */
public final class SharedState {
    public final static String APP_NAME = "MyApp";
    public final static String APP_NAME_FILE = "my_app";
    public static Stage stage = new Stage();
    public static HostServices hostServices;
    public static StatusBar statusBar;

}
