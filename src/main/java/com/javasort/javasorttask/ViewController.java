package com.javasort.javasorttask;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ViewController {
    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private Button shuffleButton;

    @FXML
    private HBox hBox;

    @FXML
    private ComboBox<String> comboBox;

    private final int COUNT = 30;

    private List<Integer> list = new ArrayList<>();

    @FXML
    private Slider speedSlider;

    @FXML
    private Button sortButton;

    public void initialize() {
        fillList();

        for (int i = 0; i < list.size(); i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setX(i * 500 / COUNT);
            rectangle.setWidth(500 / COUNT);
            rectangle.setHeight(list.get(i) * 300 / COUNT);
            rectangle.fillProperty().set(Color.BLUEVIOLET);
            rectangle.setStroke(Color.BROWN);
            hBox.getChildren().add(rectangle);
        }

        comboBox.setItems(FXCollections.observableArrayList(
                "BubbleSort",
                "ShellSort",
                "InsertionSort",
                "SelectionSort"
        ));
        comboBox.setValue("BubbleSort");

        sortButton.setOnAction(event -> sort());
        shuffleButton.setOnAction(actionEvent -> {
            list.clear();
            fillList();
            refresh();
        });
    }

    private void bubbleSort() {
        boolean needIteration = true;
        while (needIteration) {
            needIteration = false;
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i) < list.get(i - 1)) {
                    needIteration = true;
                    int finalI = i;
                    Platform.runLater(() -> swap(finalI, finalI - 1));
                    try {Thread.sleep((int) speedSlider.getValue());} catch (InterruptedException e) {}
                }
            }
        }
    }

    public void selectionSort() {
        for (int left = 0; left < list.size(); left++) {
            int maxInd = left;
            int minInd = left;
            for (int i = left; i < list.size() - left; i++) {
                if (list.get(i) >= list.get(maxInd)) {
                    maxInd = i;
                }
                if (list.get(i) <= list.get(minInd)) {
                    minInd = i;
                }
            }
            int finalLeft = left;
            int finalMaxInd = maxInd;
            try {Thread.sleep((int) speedSlider.getValue());} catch (InterruptedException e) {}
            Platform.runLater(() -> swap(list.size() - finalLeft - 1, finalMaxInd));
            try {Thread.sleep((int) speedSlider.getValue());} catch (InterruptedException e) {}
            int finalMinInd = minInd;
            Platform.runLater(() -> swap(finalLeft, finalMinInd));
        }
    }
    public void insertionSort() {
        for (int left = 0; left < list.size(); left++) {
            int value = list.get(left);
            int i = left - 1;
            for (; i >= 0; i--) {
                int finalI = i;
                if (value < list.get(i)) {
                    list.set(i + 1, list.get(i));
                    try {Thread.sleep((int) speedSlider.getValue());} catch (InterruptedException e) {}
                    Platform.runLater(() -> swap(finalI + 1, finalI + 1));
                } else {
                    break;
                }
                try {Thread.sleep((int) speedSlider.getValue());} catch (InterruptedException e) {}
                Platform.runLater(() -> swap(finalI, finalI));
            }
            list.set(i + 1, value);
        }
    }

    public void shellSort() {
        int gap = list.size() / 2;
        while (gap >= 1) {
            for (int right = 0; right < list.size(); right++) {
                for (int c = right - gap; c >= 0; c -= gap) {
                    if (list.get(c) > list.get(c + gap)) {
                        int finalC = c;
                        int finalGap = gap;
                        Platform.runLater(() -> swap(finalC, finalC + finalGap));
                    }
                }
            }
            gap = gap / 2;
        }
    }

    private void sort() {
        new Thread(() -> {
            if(comboBox.getValue().equals("BubbleSort"))
                bubbleSort();
            else if(comboBox.getValue().equals("ShellSort"))
                shellSort();
            else if(comboBox.getValue().equals("InsertionSort"))
                insertionSort();
            else if(comboBox.getValue().equals("SelectionSort"))
                selectionSort();
        }).start();
    }

    private void swap(int i1, int i2) {
        int buffer = list.get(i2);
        list.set(i2, list.get(i1));
        list.set(i1, buffer);
        hBox.getChildren().clear();
        for (int i = 0; i < list.size(); i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setX(i * 500 / COUNT);
            rectangle.setWidth(500 / COUNT);
            rectangle.setHeight(list.get(i) * 300 / COUNT);
            rectangle.fillProperty().set(Color.BLUEVIOLET);
            rectangle.setStroke(Color.BROWN);
            hBox.getChildren().add(rectangle);
        }
        ((Rectangle) hBox.getChildren().get(i1)).fillProperty().set(Color.GREEN);
        ((Rectangle) hBox.getChildren().get(i2)).fillProperty().set(Color.ORANGE);
    }

    private void refresh() {
        hBox.getChildren().clear();
        for (int i = 0; i < list.size(); i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setX(i * 500 / COUNT);
            rectangle.setWidth(500 / COUNT);
            rectangle.setHeight(list.get(i) * 300 / COUNT);
            rectangle.fillProperty().set(Color.BLUEVIOLET);
            rectangle.setStroke(Color.BROWN);
            hBox.getChildren().add(rectangle);
        }
    }

    private void fillList() {
        while(list.size() != COUNT) {
            int random = new Random().nextInt(COUNT);
            if(list.contains(random)) {
                continue;
            }
            list.add(random);
        }
    }
}