package com.company.welcome;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuData extends Text {
    public String name = "Undefined";
    public HashMap<String,Class> tabs = new HashMap<>();
    public List<TreeItem<MenuData>> children = new ArrayList<>();

    public MenuData(String name, HashMap<String,Class> tabs){
        this.name = name;
        this.tabs = tabs;
        this.setText(name);
    }

}
