package com.company.util;
// Template by JimmyRuska@gmail.com based on FXSampler
// https://bitbucket.org/controlsfx/controlsfx/src/
/* BSD license
Copyright (c) 2015 Jimmy Ruska (jimmyruska@gmail.com)
All rights reserved.

Redistribution and use in source and binary forms are permitted
provided that the above copyright notice and this paragraph are
duplicated in all such forms and that any documentation,
advertising materials, and other materials related to such
distribution and use acknowledge that the software was developed
by the <organization>. The name of the
<organization> may not be used to endorse or promote products derived
from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */


import com.company.SharedState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

public class Util {

    public static void setScene(String FXML, Stage stage) {
        try {
            Parent root = FXMLLoader.load(SharedState.class.getResource(FXML));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void webEngineCallback(WebEngine webEngine, BooleanSupplier lambda) {
        ChangeListener cb = new ChangeListener<Worker.State>() {
            public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    if (lambda.getAsBoolean()) webEngineRemoveListener(webEngine, this);
                }
            }
        };
        webEngine.getLoadWorker().stateProperty().addListener(cb);
    }
    public static void webEngineRemoveListener(WebEngine webEngine, ChangeListener listener){
        webEngine.getLoadWorker().stateProperty().removeListener(listener);
    }

    public static Optional<Object> actionAndCallback(Supplier<Optional<Object>> action, Function<Optional<Object>, Optional<Object>> reaction){
        //Optional<Object> sharedState = Optional.empty();
        //Function<Optional<Object>, Optional<Object>> compose = x -> { return x; };
        //Runnable task = () -> action.get();
        //ExecutorService exec = Executors.newSingleThreadExecutor();
        //exec.submit(task);
        Optional<Object> response = action.get();
        if (response.isPresent()){
            return reaction.apply(response);
        }
        else return Optional.empty();
    }

    public static String readResource(String resource){
        InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resource);
        return streamToString(stream);
    }

    public static String streamToString(InputStream inputStream){
        try {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("UTF-8");
        }
        catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public interface Void {
        void action();
    }
    public static void tooltip(String txt, Control node){
        final Tooltip tooltip = new Tooltip();
        tooltip.setText(txt);
        node.setTooltip(tooltip);
    }
    public static void clickSite(String url, Node node){
        node.setOnMouseClicked((event) -> {
            SharedState.hostServices.showDocument(url);
        });
    }

    public static void onClick(Node node, Void lambda){
        node.setOnMouseClicked((event) -> {
            lambda.action();
        });
    }

    public static void choose(String query, String type, String ext, Supplier<String> fun){
        choose(query, type, ext, fun.get());
    }
    public static void choose(String query, String type, String ext, String write){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save exported file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("*."+ext, "*."+ext);
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(type+"_" +query + "."+ext);
        File file = fileChooser.showSaveDialog(null);
        if (write==null || file == null) return;
        try {
            Files.write(file.toPath(), Arrays.asList(new String[] {write}), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }

    public static void setInterval(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(delay);
                    runnable.run();
                }
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }

    public void slider_bind(Slider slider, Text text, Integer start){
        IntegerProperty sliderValue = new SimpleIntegerProperty(start);
        slider.valueProperty().bindBidirectional(sliderValue);
        text.textProperty().bind(sliderValue.asString());
    }
    public void slider_bind_double(Slider slider, Text text, Double start){
        DoubleProperty sliderValue = new SimpleDoubleProperty(start);
        slider.valueProperty().bindBidirectional(sliderValue);
        text.textProperty().bind(Bindings.createStringBinding(() -> {
            String str = sliderValue.asString().get();
            if (str.length() > 5) str = str.substring(0,5);
            return str;
        }, sliderValue));
    }

    public static String userDir(){
        String home = System.getenv("HOME");
        if (home !=null) return home;
        String home2 = System.getenv("home");
        if (home2 !=null) return home2;
        String appdata = System.getenv("APPDATA");
        if (appdata != null) return appdata;
        String winHome = System.getProperty("user.home");
        if (winHome != null) return winHome;
        return "";
    }

    public Object JSONToJava(String json, Class clss){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, clss);
        } catch (IOException e) {
            e.printStackTrace();
            return new Object();
        }
    }

    public String javaToJSON(Object obj)  {
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            return ow.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /*
    public static ActorRef createThread(Class className, String threadName){
        return system.actorOf(Props.create(className), threadName);
    }
    */

}
