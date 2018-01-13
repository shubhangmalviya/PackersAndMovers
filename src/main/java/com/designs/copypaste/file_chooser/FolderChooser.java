package com.designs.copypaste.file_chooser;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FolderChooser {

    public File openFileChooser(final Stage stage, String filePath) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        if (filePath != null && !filePath.isEmpty()) {

            File file = new File(filePath);

            if (file.exists()) {
                File parentDirectory = file;
                if (!file.isDirectory()) {
                    parentDirectory = file.getParentFile();
                }
                directoryChooser.setInitialDirectory(parentDirectory);
            }
        }
        directoryChooser.setTitle("Select the Project Folder");

        return directoryChooser.showDialog(stage);
    }

}
