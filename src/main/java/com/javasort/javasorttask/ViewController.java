package com.javasort.javasorttask;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ViewController {
    @FXML
    private Button shuffleButton;
    @FXML
    private HBox hBox;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Slider speedSlider;
    @FXML
    private Button sortButton;
    private List<Integer> list = new ArrayList<>();
    private List<Integer> buffer = new ArrayList<>();
    private final int COUNT = 64;

    public void initialize() {
        fillList();

        drawRectangles();

        comboBox.setItems(FXCollections.observableArrayList(
                "BubbleSort",
                "MergeSort",
                "InsertionSort",
                "SelectionSort",
                "CocktailSort"
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
                    swap(i, i - 1);
                    Platform.runLater(() -> highlight(finalI, finalI - 1));
                    try {Thread.sleep((int) speedSlider.getValue());} catch (InterruptedException ignored) {}
                }
            }
        }
    }

    public void selectionSort() {
        for (int left = 0; left < list.size(); left++) {
            int minInd = left;
            for (int i = left; i < list.size(); i++) {
                if (list.get(i) < list.get(minInd)) {
                    minInd = i;
                }
            }
            int finalLeft = left;
            int finalMinInd = minInd;
            Platform.runLater(() -> highlight(finalLeft, finalMinInd));
            swap(left, minInd);
            try {Thread.sleep((int) speedSlider.getValue());} catch (InterruptedException ignored) {}
        }
    }

    public void insertionSort() {
        for (int left = 0; left < list.size(); left++) {
            int value = list.get(left);
            int i = left - 1;
            for (; i >= 0; i--) {
                int finalI = i;
                int finalLeft = left;
                if (value < list.get(i)) {
                    list.set(i + 1, list.get(i));
                } else {
                    break;
                }
                try {Thread.sleep((int) speedSlider.getValue());} catch (InterruptedException ignored) {}
                Platform.runLater(() -> highlight(finalLeft, finalI));
            }
            list.set(i + 1, value);
        }
    }

    public void mergeSort(List<Integer> list) {
        if (list.size() < 2) {
            return;
        }
        int mid = list.size()/2;
        List<Integer> left = new ArrayList<>(list.subList(0, mid));
        List<Integer> right = new ArrayList<>(list.subList(mid, list.size()));

        for (int i = 0; i < this.list.size(); i++) {
            try {Thread.sleep((int) speedSlider.getValue());} catch (InterruptedException ignored) {}
            int finalI1 = i;
            if(left.contains(this.list.get(i))) {
                Platform.runLater(() -> highlightPermanentlyToGreen(finalI1));
            } else if(right.contains(this.list.get(i))) {
                Platform.runLater(() -> highlightPermanentlyToOrange(finalI1));
            }
        }
        Platform.runLater(this::refresh);
        mergeSort(left);
        mergeSort(right);
        merge(left, right, list);
        buffer.removeAll(list);
        buffer.addAll(list);
        for (int i = 0; i < buffer.size(); i++) {
            this.list.set(i, buffer.get(i));
        }
        try {Thread.sleep((int) speedSlider.getValue());} catch (InterruptedException ignored) {}
        Platform.runLater(this::refresh);
    }

    public void cocktailSort() {
        boolean swapped = true;
        int start = 0;
        int end = list.size();

        while (swapped)
        {
            int finalStart = start;
            int finalEnd = end - 1;
            swapped = false;
            for (int i = start; i < end - 1; ++i) {
                if (list.get(i) > list.get(i + 1)) {
                    int temp = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, temp);
                    swapped = true;
                }
                Platform.runLater(() -> highlight(finalStart, finalEnd));
                try {Thread.sleep((int) speedSlider.getValue()/2);} catch (InterruptedException ignored) {}
            }
            if (!swapped) {
                break;
            }
            swapped = false;
            end = end - 1;
            for (int i = end - 1; i >= start; i--) {
                if (list.get(i) > list.get(i + 1)) {
                    int temp = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, temp);
                    swapped = true;
                }
                Platform.runLater(() -> highlight(finalStart, finalEnd));
                try {Thread.sleep((int) speedSlider.getValue()/2);} catch (InterruptedException ignored) {}
            }
            start = start + 1;
        }
    }

    private void merge(List<Integer> left, List<Integer> right, List<Integer> list) {
        int leftIndex = 0;
        int rightIndex = 0;
        int listIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex) < right.get(rightIndex)) {
                list.set(listIndex++, left.get(leftIndex++));
            } else {
                list.set(listIndex++, right.get(rightIndex++));
            }
        }
        while (leftIndex < left.size()) {
            list.set(listIndex++, left.get(leftIndex++));
        }
        while (rightIndex < right.size()) {
            list.set(listIndex++, right.get(rightIndex++));
        }
    }

    private void sort() {
        new Thread(() -> {
            if(comboBox.getValue().equals("BubbleSort"))
                bubbleSort();
            else if(comboBox.getValue().equals("MergeSort"))
                mergeSort(list);
            else if(comboBox.getValue().equals("InsertionSort"))
                insertionSort();
            else if(comboBox.getValue().equals("SelectionSort"))
                selectionSort();
            else if(comboBox.getValue().equals("CocktailSort"))
                cocktailSort();

            for (int i = 0; i < list.size(); i++) {
                int finalI = i;
                Platform.runLater(() -> highlightPermanentlyToGreen(finalI));
                try {Thread.sleep(10);} catch (InterruptedException ignored) {}
            }

            Platform.runLater(this::activateSortButton);
        }).start();
        sortButton.setDisable(true);
    }

    private void activateSortButton() {
        sortButton.setDisable(false);
    }

    private void highlight(int i1, int i2) {
        hBox.getChildren().clear();
        drawRectangles();
        ((Rectangle) hBox.getChildren().get(i1)).fillProperty().set(Color.GREEN);
        ((Rectangle) hBox.getChildren().get(i2)).fillProperty().set(Color.ORANGE);
    }

    private void highlightPermanentlyToGreen(int i1) {
        ((Rectangle) hBox.getChildren().get(i1)).fillProperty().set(Color.GREEN);
    }

    private void highlightPermanentlyToOrange(int i1) {
        ((Rectangle) hBox.getChildren().get(i1)).fillProperty().set(Color.ORANGE);
    }

    private void swap(int i1, int i2) {
        int buffer = list.get(i2);
        list.set(i2, list.get(i1));
        list.set(i1, buffer);
    }

    private void drawRectangles() {
        for (int i = 0; i < list.size(); i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setX(i * 500 / COUNT);
            rectangle.setWidth(500 / COUNT);
            rectangle.setHeight(list.get(i) * 300 / COUNT);
            rectangle.fillProperty().set(Color.BLUEVIOLET);
            rectangle.setStroke(Color.BLACK);
            hBox.getChildren().add(rectangle);
        }
    }

    private void refresh() {
        hBox.getChildren().clear();
        drawRectangles();
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