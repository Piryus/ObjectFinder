package fr.piryus.model;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class ADT {

    // Raw data
    private String fileName;
    private byte bytes[];
    private MMDX mmdx;
    private MMID mmid;
    private MDDF mddf;
    private MWMO mwmo;
    private MWID mwid;
    private MODF modf;

    // Parsed M2 data
    private ArrayList<String> m2Filenames = new ArrayList<>();
    private Multimap<Integer, Coordinates> m2Position = ArrayListMultimap.create();
    private Multimap<Integer, Integer> m2Scale = ArrayListMultimap.create();

    // Parsed WMO data
    private ArrayList<String> wmoFilenames = new ArrayList<>();
    private Multimap<Integer, Coordinates> wmoPosition = ArrayListMultimap.create();
    private Multimap<Integer, Integer> wmoScale = ArrayListMultimap.create();

    // Sorted data
    private ArrayList<Model> modelList = new ArrayList<>();

    public ADT(File file) throws IOException {
        // Get the file name
        fileName = file.getName();

        // Copy the whole file into a byte array
        bytes = Files.readAllBytes(file.toPath());

        // ----------------------M2------------------------

        // Parse the MMDX chunk
        mmdx = new MMDX(bytes);

        // Parse the MMID chunk
        mmid = new MMID(bytes);

        // Set the m2 filenames
        parseM2Filenames();

        // Parse the MDDF chunk
        mddf = new MDDF(bytes);
        m2Position = mddf.getM2Position();
        m2Scale = mddf.getM2Scale();

        // Create m2 models with every infos
        for(int i = 0; i < m2Filenames.size(); i++) {
            modelList.add(new Model("M2", m2Filenames.get(i), m2Position.get(i), m2Scale.get(i), fileName));
        }

        // ----------------------WMO-----------------------

        // Parse the MWMO chunk
        mwmo = new MWMO(bytes);

        // Parse the MWID chunk
        mwid = new MWID(bytes);

        // Set the wmo filenames
        parseWMOFilenames();

        // Parse the MODF chunk
        modf = new MODF(bytes);
        wmoPosition = modf.getWmoPosition();
        wmoScale = modf.getWmoScale();

        // Create wmo models with every infos
        for(int i = 0; i < wmoFilenames.size(); i++) {
            modelList.add(new Model("WMO", wmoFilenames.get(i), wmoPosition.get(i), wmoScale.get(i), fileName));
        }
    }

    public String getFileName() {
        return fileName;
    }

    public Model getModel(String object) {
        Model returnedModel = null;
        for(Model model : modelList) {
            int lastSlash = model.getFilename().lastIndexOf('\\');
            if(model.getFilename().substring(lastSlash+1).toUpperCase().equals(object.toUpperCase())) {
                returnedModel = model;
            }
        }
        return returnedModel;
    }

    private void parseM2Filenames() throws UnsupportedEncodingException {
        // Use filename offsets from MMID chunk to split MMDX data into filenames
        for(int i = 0; i < mmid.getFilenameOffset().size(); i++) {

            // Define where to split the string
            int toOff;
            if(i+1 > mmid.getFilenameOffset().size()-1)
                toOff = mmdx.getSize();
            else
                toOff = mmid.getFilenameOffset().get(i+1);

            byte filename[] = Arrays.copyOfRange(mmdx.getData(), mmid.getFilenameOffset().get(i), toOff);
            m2Filenames.add(new String(filename, "UTF-8"));
        }
    }

    private void parseWMOFilenames() throws UnsupportedEncodingException {
        // Use filename offsets from MWID chunk to split MWMO data into filenames
        for(int i = 0; i < mwid.getFilenameOffset().size(); i++) {

            // Define where to split the string
            int toOff;
            if(i+1 > mwid.getFilenameOffset().size()-1)
                toOff = mwmo.getSize();
            else
                toOff = mwid.getFilenameOffset().get(i+1);

            byte filename[] = Arrays.copyOfRange(mwmo.getData(), mwid.getFilenameOffset().get(i), toOff);
            wmoFilenames.add(new String(filename, "UTF-8"));
        }
    }
}
