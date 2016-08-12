package com.company;

import com.company.views.d3.D3;
import com.company.views.d3.Erlang;
import com.company.views.main.*;
import com.company.views.spreadsheet.SpreadSheet;
import com.company.welcome.MenuData;
import javafx.application.HostServices;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;

import java.util.*;

/**
 * Created by fold on 7/29/16.
 */
public final class SharedState {
    public final static String APP_NAME = "My JavaFX Application";
    public final static String APP_NAME_FILE = "my_app";
    public static Stage stage = new Stage();
    public static HostServices hostServices;
    public static StatusBar statusBar;
    public static List<TreeItem<MenuData>> menuItems = new ArrayList<TreeItem<MenuData>>();
    public static TreeView menu = new TreeView<>(null);

    public static void makeMenu(){
        MenuData root = new MenuData("Root", new LinkedHashMap<String, Class>());
        menuItems.add(new TreeItem<MenuData>(root));

        MenuData main = new MenuData("Main", new LinkedHashMap<String, Class>(){{
            put("Main", com.company.views.main.Main.class);
            put("Source", com.company.views.main.Source.class);
            put("Canvas", com.company.views.main.MyCanvas.class);
        }});
        menuItems.add(new TreeItem<MenuData>(main));

        MenuData child = new MenuData("D3.js", new LinkedHashMap<String, Class>(){{
            put("Web View", D3.class);
            put("Erlang", Erlang.class);
        }});
        main.children.add(new TreeItem<MenuData>(child));

        MenuData child2 = new MenuData("Spreadsheet", new LinkedHashMap<String, Class>(){{
            put("SpreadSheet", SpreadSheet.class);
        }});
        main.children.add(new TreeItem<MenuData>(child2));

    }
}
