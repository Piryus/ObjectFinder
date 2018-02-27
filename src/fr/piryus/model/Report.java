package fr.piryus.model;

import com.google.common.collect.Multimap;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Report {

    public Report(Search search, ArrayList<String> objects, String coordSystem) {
        try {
            // Create a temp txt file
            File tempReport = File.createTempFile("ObjectFinder_Report", ".txt");
            // Fill it with the report
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempReport));
            // Header
            bw.write("###################################");
            bw.newLine();
            bw.write("#       ObjectFinder Report       #");
            bw.newLine();
            bw.write("###################################");
            bw.newLine();
            bw.newLine();

            // Get search results
            final Multimap<String, ADT> results = search.getAdtResultsList();

            // For each object searched
            for (String object : objects) {

                // Dirty way to get the number of results
                int resultNumber = 0;
                int adtNumber = 0;
                for (ADT adt : results.values()) {
                    if (adt.getModel(object) != null) {
                        resultNumber += adt.getModel(object).getScaleList().size();
                        adtNumber++;
                    }
                }

                // Print object header
                bw.write("#" + object + " : " + resultNumber + " result(s) in " + adtNumber + " ADT(s).");
                bw.newLine();

                for (ADT adt : results.get(object)) {

                    // Remove _obj0.adt from the file name (and print it)
                    int i = adt.getFileName().lastIndexOf('.');
                    bw.write("  " + adt.getFileName().substring(0, i - 5)); // "obj_0" size = 5

                    // Print scale and position
                    ArrayList<Float> scaleList = new ArrayList<>(adt.getModel(object).getScaleList());
                    ArrayList<Coordinates> posList = new ArrayList<>(adt.getModel(object).getPositionList());
                    for (int j = 0; j < posList.size(); j++) {
                        bw.newLine();
                        bw.write("      Scale: " + scaleList.get(j) + "%");
                        bw.newLine();
                        bw.write("      Position: " + posList.get(j).toString(coordSystem));
                        bw.newLine();
                        bw.write("      ----------");
                    }

                    bw.newLine();
                }

                // If nothing was found :(
                if (results.get(object).isEmpty()) {
                    bw.write("  No result found.");
                    bw.newLine();
                }

                bw.newLine();
            }
            bw.close();

            // Open the temp report, don't know if it's the best way
            Desktop.getDesktop().edit(tempReport);

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
