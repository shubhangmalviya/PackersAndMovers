package com.designs.copypaste.file_chooser;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ImageFileChooser {

    public File openFileChooser(final Stage stage, String filePath) {
        final FileChooser fileChooser = new FileChooser();

        if (filePath != null && !filePath.isEmpty()) {

            File file = new File(filePath);

            if (file.exists()) {
                File parentDirectory = file;
                if (!file.isDirectory()) {
                    parentDirectory = file.getParentFile();
                }
                fileChooser.setInitialDirectory(parentDirectory);
            }
        }
        fileChooser.setTitle("Select the Image file");

        // no image file extension filter is applied to make it easy.

        return fileChooser.showOpenDialog(stage);
    }
}
