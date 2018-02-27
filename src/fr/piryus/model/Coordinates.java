package fr.piryus.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;

public class Coordinates {

    private float x, y, z;
    private String mapID;
    private String mapName;

    public Coordinates(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    @Override
    public String toString() {
        return "X: "+getX()+" Y: "+getY()+" Z: "+getZ();
    }

    public String toString(String system) {

        mapID = "(MapId not found, please update your MapIdList)";
        // Get Map Id from list
        try {
            File jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            BufferedReader br = new BufferedReader(new FileReader(jarFile.getParent()+"/MapIdList.txt"));
            String line = null;
            while((line = br.readLine()) != null) {
                if(line.substring(line.indexOf("\t")+1,line.length()).toUpperCase().equals(mapName.toUpperCase())) {
                    this.mapID = line.substring(0,line.indexOf("\t"));
                }
            }
        } catch (java.io.IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        String returnedString = "An error occured while getting your coordinate system choice.";
        switch (system) {
            case "!tele-ready":
                returnedString = "!tele " + (getZ() - 17066) * (-1) + " " + (getX() - 17066) * (-1) + " " + getY() + " " + mapID;
                break;
            case "worldport-ready":
                returnedString = "/console worldport " + mapID + " " + (getZ() - 17066) * (-1) + " " + (getX() - 17066) * (-1) + " " + getY();
                break;
            case "Classic":
                returnedString = "X: " + (getZ() - 17066) * (-1) + " Y: " + (getX() - 17066) * (-1) + " Z: " + getY();
                break;
        }
            return returnedString;
    }
}
