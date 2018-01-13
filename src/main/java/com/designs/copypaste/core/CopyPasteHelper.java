package com.designs.copypaste.core;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Collections;

public class CopyPasteHelper {

    public void move(String outputFileName, String imgFilePath, String projectFolderPath) throws IOException {

        File projectFolder = new File(projectFolderPath);
        File generatedDirectory = new File(projectFolder, outputFileName);
        File imgFile = new File(imgFilePath);
        File imgFolder = imgFile.getParentFile();

        String simpleFile = getSimpleFileName(imgFilePath);
        String extension = getExtension(imgFilePath);

        File file1x = get1xImage(imgFolder, simpleFile, extension);
        File file2x = get2xImage(imgFolder, simpleFile, extension);
        File file3x = get3xImage(imgFolder, simpleFile, extension);

        if (!generatedDirectory.exists()) {
            Files.createDirectory(generatedDirectory.toPath());
        }

        moveFile(file1x, generatedDirectory , outputFileName + "." + extension);
        moveFile(file2x, generatedDirectory , outputFileName + "@2x." + extension);
        moveFile(file3x, generatedDirectory , outputFileName + "@3x." + extension);
        generateIndexFile(generatedDirectory, outputFileName, extension);
    }

    private void generateIndexFile(File folderPath, String outputFileName, String extension) throws IOException {

        String format = "export const %s = require(\"./%s.%s\");";

        String content = String.format(format, outputFileName, outputFileName, extension);

        File file = new File(folderPath, "index.js");
        Files.write(file.toPath(), Collections.singleton(content), Charset.forName("UTF-8"));
    }

    private String getExtension(String imgFilePath) {
        int extensionIndex = imgFilePath.lastIndexOf(".");
        if (extensionIndex != -1) {
            return imgFilePath.substring(extensionIndex + 1);
        }
        return null;
    }

    private String getSimpleFileName(String imgFilePath) throws IOException {
        int indexOfSlash = imgFilePath.lastIndexOf("/");

        if (!imgFilePath.contains("@")) {

            int indexOfAt = imgFilePath.lastIndexOf("@");

            String fileName;

            if (indexOfSlash == -1) {
                indexOfSlash = 0;
            }

            if (indexOfAt > indexOfSlash) {
                fileName = imgFilePath.substring(indexOfSlash + 1, indexOfAt);
            }else {
                int indexOfDot = imgFilePath.indexOf(".");
                 fileName = imgFilePath.substring(indexOfSlash + 1, indexOfDot);
            }

            return fileName;
        }else {
            int index = imgFilePath.indexOf("@");
            return imgFilePath.substring(indexOfSlash, index);
        }
    }

    private File get1xImage(File imgFolder, String simpleImgName, String extension) {
        return new File(imgFolder,simpleImgName + "." + extension);
    }

    private File get2xImage(File imgFolder, String simpleImgName, String extension) {
        return new File(imgFolder,simpleImgName + "@2x." + extension);
    }

    private File get3xImage(File imgFolder, String simpleImgName, String extension) {
        return new File(imgFolder,simpleImgName + "@3x." + extension);
    }

    private void moveFile(File source, File directory, String outputFile) throws IOException {
        Files.move(source.toPath(), directory.toPath().resolve(outputFile), StandardCopyOption.REPLACE_EXISTING);
    }

}
