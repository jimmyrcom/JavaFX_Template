package com.company.welcome;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.text.Text;

import java.util.*;

public class MenuData extends Text {
    public String name = "Undefined";
    public Map<String,Class> tabs = new TreeMap<>();
    public List<TreeItem<MenuData>> children = new ArrayList<>();

    public MenuData(String name, Map<String,Class> tabs){
        this.name = name;
        this.tabs = tabs;
        this.setText(name);
    }

}
