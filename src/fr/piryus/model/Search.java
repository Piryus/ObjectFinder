package fr.piryus.model;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.concurrent.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Search extends Task<List<File>> {

    private FileList filesToCheck;
    private List<String> objectsToSearch;
    private List<File> checkedFiles = new ArrayList<>();
    private Multimap<String, ADT> adtResultsList = ArrayListMultimap.create();

    public Search(FileList filesToCheck, List<String> objects) {
        this.filesToCheck = filesToCheck;
        objectsToSearch = objects;
    }

    @Override
    protected List<File> call() throws Exception {

        // Number of files already checked, used by UI
        int filesChecked = 0;

        // Scan every file
        for(File file : filesToCheck.getFileList()) {
            try {
                // Open the bufferedReader on the file
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
                String line;

                // Update task message for the UI
                this.updateMessage("Scanning : " + file.getAbsolutePath());

                // Read the whole file
                while((line = br.readLine()) != null) {
                    for(String object : objectsToSearch) {
                        if(line.toUpperCase().contains("\\"+object.toUpperCase())) {
                            // Check that there is a "\" before the string meaning the exact object filename was found
                            // Parse the file and add it to a list
                            adtResultsList.put(object, new ADT(file));
                        }
                    }
                }
                // File was completely scanned, add it to the list of checked files
                checkedFiles.add(file);
                filesChecked++;
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Update task progress : number of files checked divided by total number of files
            this.updateProgress(filesChecked, filesToCheck.getFileListSize());
        }

        return checkedFiles;
    }

    public Multimap<String, ADT> getAdtResultsList() {
        return adtResultsList;
    }
}
