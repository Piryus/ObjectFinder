package fr.piryus.view;

import javafx.collections.FXCollections;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import fr.piryus.model.FileList;
import fr.piryus.model.Report;
import fr.piryus.model.Search;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import static java.util.Arrays.asList;

public class SearchController {

    private Search search;
    private FileList filesInFolder;

    @FXML
    private TextField pathField;
    @FXML
    private TextArea objectsField;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button searchButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Button browseButton;
    @FXML
    private ChoiceBox coordsChoiceBox;

    public SearchController() {
    }


    private boolean checkModelsSuffix() {
        boolean isOk = true;
        for (String object : asList(objectsField.getText().split(","))) {
            if (!object.toUpperCase().contains(".M2") && !object.toUpperCase().contains(".WMO"))
                isOk = false;
        }
        return isOk;
    }

    @FXML
    private void handleSearch(ActionEvent e) {
        if (new File(pathField.getText()).isDirectory() && !objectsField.getText().isEmpty() && checkModelsSuffix()) {

            // Reset the progress bar and label
            progressBar.setProgress(0);
            statusLabel.setText("");

            // Block the buttons and the editable fields
            blockActions(true);

            // Update the progress bar and progress text
            progressBar.setProgress(-1.0f);
            statusLabel.setText("Listing ADTs...");

            // List the ADTs
            filesInFolder = new FileList(pathField.getText());
            new Thread(filesInFolder).start();
            filesInFolder.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                    event -> {
                        // Execute the search on the created list
                        search = new Search(filesInFolder, asList(objectsField.getText().split(",")));

                        // Display the progress bar
                        progressBar.progressProperty().unbind();
                        progressBar.progressProperty().bind(search.progressProperty());

                        // Display the file being checked into
                        statusLabel.textProperty().unbind();
                        statusLabel.textProperty().bind(search.messageProperty());

                        // Display the number of ADTs checked when search is finished and pop an alert
                        search.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                                event1 -> {
                                    // Update statusLabel with the number of files checked
                                    statusLabel.textProperty().unbind();
                                    statusLabel.setText("Scanned " + filesInFolder.getFileListSize() + " ADTs.");

                                    // Pop an alert asking whether to open the report
                                    Alert searchAlert = new Alert(Alert.AlertType.CONFIRMATION);
                                    searchAlert.setTitle("Results");
                                    searchAlert.setHeaderText(null);
                                    searchAlert.setContentText("Open the search report ?");
                                    Optional<ButtonType> result = searchAlert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        // Create the report if the user clicks on "OK"
                                        Report report = new Report(search, new ArrayList<>(asList(objectsField.getText().split(","))), coordsChoiceBox.getValue().toString());

                                        // Auto reset the search
                                        handleCancel(null);
                                    }
                                });

                        // Start the search
                        new Thread(search).start();

                    });

        } else if (!(new File(pathField.getText()).isDirectory())) {
            // If the directory's path is incorrect, pop an alert
            Alert directoryAlert = new Alert(Alert.AlertType.ERROR);
            directoryAlert.setTitle("Error 169");
            directoryAlert.setHeaderText("Error 169 : Incorrect path.");
            directoryAlert.setContentText("Please check the selected path validity and try again. If this error persists, please contact the developer.");
            directoryAlert.showAndWait();

        } else if (objectsField.getText().isEmpty()) {
            // If the user did not specify an object to search, pop an alert
            Alert emptyObjectFieldAlert = new Alert(Alert.AlertType.ERROR);
            emptyObjectFieldAlert.setTitle("Error 1179");
            emptyObjectFieldAlert.setHeaderText("Error 1179 : No objects.");
            emptyObjectFieldAlert.setContentText("You didn't specify any object to look for. If this error persists, please contact the developer.");
            emptyObjectFieldAlert.showAndWait();
        } else if (!checkModelsSuffix()) {
            // If the user did not specify an object's extension
            Alert extensionObjectAlert = new Alert(Alert.AlertType.ERROR);
            extensionObjectAlert.setTitle("Error 451");
            extensionObjectAlert.setHeaderText("Error 451 : Missing extension.");
            extensionObjectAlert.setContentText("You didn't specify every objects' extensions. Please specify '.m2' or '.wmo' for each object. If this error persists, please contact the developer.");
            extensionObjectAlert.showAndWait();
        }
    }

    // Click on cancel button
    @FXML
    private void handleCancel(ActionEvent e) {
        if (filesInFolder != null && filesInFolder.isRunning())
            filesInFolder.cancel(true);
        if (search != null && search.isRunning())
            search.cancel(true);

        // Reset the progress bar and label
        progressBar.progressProperty().unbind();
        statusLabel.textProperty().unbind();
        progressBar.setProgress(0);
        statusLabel.setText("");

        // Unblock the buttons and the editable fields
        blockActions(false);

    }

    @FXML
    private void handleAbout(ActionEvent e) {
        Alert alertAbout = new Alert(Alert.AlertType.INFORMATION);
        alertAbout.getDialogPane().setPrefWidth(530);
        alertAbout.setResizable(true);
        alertAbout.setTitle("About ObjectFinder");
        alertAbout.setHeaderText("Copyright (c) 2018 Piryus");
        alertAbout.setContentText("Please feel free to contact me at : contact@piryus.fr for any questions.\nSpecial thanks to Mjollna for her advice and suggestions.\n\n" +
                "World of Warcraft and Warcraft are trademarks or registered trademarks of Blizzard Entertainment, Inc., in the U.S. and/or other countries.\n" +
                "ObjectFinder is not affiliated with Blizzard Entertainment.\n" +
                "\n" +
                "ObjectFinder is licensed under the MIT License\n" +
                "\n" +
                "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
                "of this software and associated documentation files (the \"Software\"), to deal\n" +
                "in the Software without restriction, including without limitation the rights\n" +
                "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
                "copies of the Software, and to permit persons to whom the Software is\n" +
                "furnished to do so, subject to the following conditions:\n" +
                "\n" +
                "The above copyright notice and this permission notice shall be included in all\n" +
                "copies or substantial portions of the Software.\n" +
                "\n" +
                "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
                "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
                "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
                "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
                "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n" +
                "SOFTWARE.\n");

        alertAbout.showAndWait();
    }

    // Block or unblock the buttons and editable fields
    private void blockActions(boolean bool) {
        searchButton.setDisable(bool);
        browseButton.setDisable(bool);
        pathField.setEditable(!bool);
        pathField.setMouseTransparent(bool);
        pathField.setFocusTraversable(!bool);
        objectsField.setEditable(!bool);
        objectsField.setMouseTransparent(bool);
        objectsField.setFocusTraversable(!bool);
        cancelButton.setDisable(!bool);
    }

    @FXML
    private void handleBrowse(ActionEvent e) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Pick a folder");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File choosenOne = directoryChooser.showDialog(new Stage());
        if (choosenOne != null)
            pathField.setText(choosenOne.getAbsolutePath());
    }

    @FXML
    private void initialize() {
        pathField.setPromptText("Enter the path to the folder containing the ADTs, subfolders will be included.");
        objectsField.setPromptText("stormwind.wmo,test.m2,nd_dalaran.wmo,bloodelf_inn.wmo,humanmalescale.m2");
        cancelButton.setDisable(true);
        statusLabel.setText("");

        // Add choices to the coordinate system choice box
        coordsChoiceBox.setItems(FXCollections.observableArrayList("Classic", "!tele-ready", "worldport-ready"));
        coordsChoiceBox.getSelectionModel().selectFirst();
    }
}
