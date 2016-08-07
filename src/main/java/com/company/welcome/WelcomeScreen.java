package com.company.welcome;

//        Copyright (c) 2016, Jimmy Ruska
//        All rights reserved.
//
//        Redistribution and use in source and binary forms, with or without
//        modification, are permitted provided that the following conditions are met:
//
//        * Redistributions of source code must retain the above copyright notice, this
//        list of conditions and the following disclaimer.
//
//        * Redistributions in binary form must reproduce the above copyright notice,
//        this list of conditions and the following disclaimer in the documentation
//        and/or other materials provided with the distribution.
//
//        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
//        AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//        IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//        DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
//        FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//        DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//        SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
//        CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
//        OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
//        OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.



import com.company.SharedState;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class WelcomeScreen extends GridPane implements Initializable {

    TabPane tabPane = new TabPane();

    public WelcomeScreen(){
        super();
        SharedState.makeMenu();

        setPadding(new Insets(5, 10, 10, 10));
        this.setHgap(10);
        this.setVgap(10);
        TextField search = new TextField("");
        search.setPromptText("Search");
        GridPane.setMargin(search, new Insets(5, 0, 0, 0));
        add(search,0,0);
        search.textProperty().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable o) {
                if (search.getText().length() == 0){
                    buildMenu(tabPane);
                    return;
                }
                filterMenu(search.getText().toString(), tabPane);
            }
        });

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);
        GridPane.setHgrow(tabPane, Priority.ALWAYS);
        GridPane.setVgrow(tabPane, Priority.ALWAYS);
        add(tabPane, 1, 0, 1, 2);

        buildMenu(tabPane);

        // select first tab by default
        TreeItem<MenuData> first = (TreeItem<MenuData>)SharedState.menu.getRoot().getChildren().get(0);
        for (Map.Entry<String, Class> firstTab: first.getValue().tabs.entrySet()){
            try {
                Node node = (Node) firstTab.getValue().newInstance();
                Tab tab = new Tab(firstTab.getKey());
                GridPane.setVgrow(node, Priority.ALWAYS);
                node.maxHeight(Integer.MAX_VALUE);

                tab.setContent(node);
                tabPane.getTabs().add(tab);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        SharedState.menu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            TreeItem<MenuData> item = (TreeItem<MenuData>) newValue;
            if (item != null && tabPane.getTabs() != null) tabPane.getTabs().clear();
            if (item == null) return;
            for (Map.Entry<String, Class> entry : item.getValue().tabs.entrySet()) {
                try {
                    Node node = (Node) entry.getValue().newInstance();
                    Tab tab = new Tab(entry.getKey());
                    GridPane.setVgrow(node, Priority.ALWAYS);
                    node.maxHeight(Integer.MAX_VALUE);

                    tab.setContent(node);
                    tabPane.getTabs().add(tab);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        add(SharedState.menu, 0, 1);

        SharedState.menu.setShowRoot(false);
        SharedState.menu.setMinWidth(300);
        SharedState.menu.setMaxWidth(300);
        GridPane.setVgrow(SharedState.menu, Priority.ALWAYS);

        //browser.getEngine().executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}");
    }

    public void buildMenu(TabPane tabPane){
        TreeItem<MenuData> root = SharedState.menuItems.get(0);
        root.getChildren().clear();
        addElements(0, SharedState.menuItems.subList(1, SharedState.menuItems.size()), root);

        SharedState.menu.setRoot(root);
        root.setExpanded(true);

        /*
        Event.fireEvent(root.getChildren().get(0), new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY
                , 1, true, true, true, true, true, true, true, true, true, true, null));
                */
    }

    public void filterMenu(String search, TabPane tabPane){
        TreeItem<MenuData> root = SharedState.menuItems.get(0);
        List<TreeItem<MenuData>> list = new ArrayList<>();
        filterElements(SharedState.menuItems.subList(1, SharedState.menuItems.size()), root, list);
        list.sort((x,y) -> {
            String x1 = x.getValue().name.toLowerCase();
            String y1 = y.getValue().name.toLowerCase();
            // exact match
            if (x1.equals(search)) return -1;
            if (y1.equals(search)) return 1;
            // contains
            if (x1.contains(search) && !y1.contains(search)) return -1;
            if (!x1.contains(search) && y1.contains(search)) return 1;
            // Return the one with at least some characters matching
            Set<Character> searchSet = search.chars().mapToObj(e->(char)e).collect(Collectors.toSet());
            Set<Character> x1Set = x1.chars().mapToObj(e->(char)e).collect(Collectors.toSet());
            Set<Character> y1Set = y1.chars().mapToObj(e->(char)e).collect(Collectors.toSet());
            Set<Character> x1Intersect = new HashSet<Character>(x1Set);
            Set<Character> y1Intersect = new HashSet<Character>(y1Set);
            x1Intersect.retainAll(searchSet);
            y1Intersect.retainAll(searchSet);
            if (x1Intersect.size() > 0 && y1Intersect.size() == 0) return -1;
            if (y1Intersect.size() > 0 && x1Intersect.size() == 0) return 1;
            if (((float)x1Intersect.size() / y1Intersect.size()) > 1.9 ) return -1;
            if (((float)y1Intersect.size() / x1Intersect.size()) > 1.9 ) return 1;
            // Otherwise use levenshtein
            return ((Integer) StringUtils.getLevenshteinDistance(search, x1))
                            .compareTo(StringUtils.getLevenshteinDistance(search, y1));
                }
        );
        root.getChildren().clear();
        for (TreeItem<MenuData> x : list){
            x.getChildren().clear();
            root.getChildren().add(x);
        }
    }

    public void filterElements(List<TreeItem<MenuData>> children, TreeItem<MenuData> root, List<TreeItem<MenuData>> list){
        for(TreeItem<MenuData> child : children) {
            list.add(child);
            filterElements(child.getValue().children, child, list);
        }
        root.setExpanded(true);
    }


    public void addElements(int level, List<TreeItem<MenuData>> children, TreeItem<MenuData> root){
        for(TreeItem<MenuData> child : children) {
            root.getChildren().add(child);
            if (level == 0) topLevelText(child);
            addElements(level+1, child.getValue().children, child);
        }
        root.setExpanded(true);
    }

    public void topLevelText(TreeItem<MenuData> x){
        Node icon = new ImageView(new Image(getClass().getResourceAsStream("/image/1470528934_cafe_coffee_16.png")));
        x.setGraphic(icon);
        Text t = x.getValue();
        t.setFont(Font.font ("Verdana", FontWeight.BOLD, 15));
        t.setFontSmoothingType(FontSmoothingType.LCD);
        t.setFill(Color.PURPLE);

    }

    public void expandTabs(TreeItem<MenuData> item){
        EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
        };

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
