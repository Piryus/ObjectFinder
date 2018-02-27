package fr.piryus.model;

import java.util.ArrayList;
import java.util.Collection;

public class Model {

    private String type; // M2 or WMO
    private String filename;
    private Collection<Coordinates> positionList = new ArrayList<>();
    private Collection<Float> scaleList = new ArrayList<>();

    public Model(String type, String filename, Collection<Coordinates> pos, Collection<Integer> scaleList, String ADTFileName) {
        this.type = type;
        this.filename = filename.substring(0,filename.length()-1); // Remove last trash char
        positionList = pos;
        for(Coordinates coords : positionList) {
            String sub = ADTFileName.substring(0, ADTFileName.lastIndexOf("_"));
            int end = sub.substring(0, sub.lastIndexOf("_")).lastIndexOf("_");
            coords.setMapName(ADTFileName.substring(0, end));
        }
        // Store scale as percentage
        for(int scale : scaleList) {
            this.scaleList.add((float)scale/1024*100);
        }
    }

    public String getFilename() {
        return filename;
    }

    public Collection<Coordinates> getPositionList() {
        return positionList;
    }

    public Collection<Float> getScaleList() {
        return scaleList;
    }
}
