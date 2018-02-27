package fr.piryus.model;

import javafx.concurrent.Task;

import java.io.File;
import java.util.ArrayList;

public class FileList extends Task {

    private ArrayList<File> fileList = new ArrayList<>();
    private String rootPath;

    public FileList(String path) {
        rootPath = path;
    }

    public void listADTInPath(String path) {
        File root = new File(path);
        if (root.isDirectory()) {
            for (File file : root.listFiles()) {
                if (file.isDirectory()) {
                    listADTInPath(file.getAbsolutePath());
                } else {
                    if (file.getName().endsWith("obj0.adt")) {
                        fileList.add(file);
                    }
                }
            }
        } else {
            if (root.getName().endsWith("obj0.adt")) {
                fileList.add(root);
            }
        }
    }

    public ArrayList<File> getFileList() {
        return fileList;
    }

    public int getFileListSize() {
        return fileList.size();
    }

    @Override
    protected ArrayList<File> call() throws Exception {
        listADTInPath(rootPath);
        return null;
    }
}
