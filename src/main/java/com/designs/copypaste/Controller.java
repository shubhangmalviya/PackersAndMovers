package com.designs.copypaste;

import com.designs.copypaste.core.CopyPasteHelper;
import com.designs.copypaste.file_chooser.FolderChooser;
import com.designs.copypaste.file_chooser.ImageFileChooser;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Controller {

    @FXML
    private TextField mFileName;

    @FXML
    private TextField mImgFilePath;

    @FXML
    private TextField mProjectFolderPath;

    private Stage mStage;

    public void setWindowStage(Stage stage) {
        mStage = stage;
    }

    @FXML
    public void onProjectFileBrowseClicked() {
        FolderChooser csvFileChooser = new FolderChooser();
        String text = getValue(mProjectFolderPath);
        File file = csvFileChooser.openFileChooser(mStage, text);
        if (file != null && file.exists()) {
            String filePath = file.getPath();
            mProjectFolderPath.setText(filePath);
        } else {
            System.out.println("No file chosen");
        }
    }

    @FXML
    public void onImageFileBrowseClicked() {
        ImageFileChooser csvFileChooser = new ImageFileChooser();
        String text = getValue(mImgFilePath);
        File file = csvFileChooser.openFileChooser(mStage, text);
        if (file != null && file.exists()) {
            String filePath = file.getPath();
            mImgFilePath.setText(filePath);
        } else {
            System.out.println("No file chosen");
        }
    }

    @FXML
    public void onGenerateFileClicked() {
        if (canGenerate()) {

            String outputFileName = getValue(mFileName);
            String imgFilePath = getValue(mImgFilePath);
            String projectFolderPath = getValue(mProjectFolderPath);

            CopyPasteHelper copyPasteHelper = new CopyPasteHelper();
            try {
                copyPasteHelper.move(outputFileName, imgFilePath, projectFolderPath);
                showAlert("Success", "File generated", "Please check the output folder", Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                showAlertWithStackTrace("Alert", "Something wrong with the input", e.getMessage(), e, Alert.AlertType.ERROR);
            }
        }else {
            showAlert("Alert", "Please provide vaild input", "Check the input form for wrong/incomplete values", Alert.AlertType.ERROR);
        }
    }

    private void showAlertWithStackTrace(String title, String headerText, String message, Exception ex, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();

        alert.showAndWait();
    }


    private void showAlert(String title, String headerText, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);

        alert.showAndWait();
    }

    private String getValue(TextField textField) {
        String text = textField.getText();
        return text != null ? text.trim() : null;
    }

    private boolean canGenerate() {

        String outputFileName = getValue(mFileName);
        String imgFilePath = getValue(mImgFilePath);
        String projectFolderPath = getValue(mProjectFolderPath);

        return validField(outputFileName) && validFolderPath(projectFolderPath) && validFilePath(imgFilePath);
    }

    private boolean validField(String field) {
        return field != null && !field.isEmpty();
    }

    private boolean validFilePath(String filePath) {

        if (!validField(filePath)) {
            return false;
        }

        File file = new File(filePath);

        return file.exists() && !file.isDirectory();
    }

    private boolean validFolderPath(String filePath) {

        if (!validField(filePath)) {
            return false;
        }

        File file = new File(filePath);

        return file.exists() && file.isDirectory();
    }
}
