package fr.piryus.model;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.BaseEncoding;

import java.nio.ByteOrder;
import java.util.Arrays;

public class MODF extends Chunk{

    // Parsed data
    private Multimap<Integer, Coordinates> wmoPosition = ArrayListMultimap.create();
    private Multimap<Integer, Integer> wmoScale = ArrayListMultimap.create();

    public MODF(byte bytes[]) {
        // Call super default constructor
        super();
        identifier = "MODF"; // Captain obvious

        // Define the MODF check
        identifierCheck = BaseEncoding.base16().lowerCase().decode("46444F4D".toLowerCase()); //  46 44 4F 4D = FDOM

        // Let the fun part begin aka. parsing the file
        int offset = 0;

        // Identify the offset at which the MODF chunk starts
        while(!Arrays.equals(identifierCheck, Arrays.copyOfRange(bytes, offset, offset+4)))
            offset++;

        // Identify the size of the chunk
        offset+=identifierCheck.length;
        size = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, offset, offset+4)).order(ByteOrder.LITTLE_ENDIAN).getInt();

        // Copy chunk data into a byte array
        offset+=4;
        data = Arrays.copyOfRange(bytes, offset, offset + size);

        // Get each m2's position and scale
        for(int i = 0; i < getSize(); i+=64) {
            byte mwidEntryData[] = Arrays.copyOfRange(data, i, i+4);
            byte posDataX[] = Arrays.copyOfRange(data, i+8, i+12);
            byte posDataY[] = Arrays.copyOfRange(data, i+12, i+16);
            byte posDataZ[] = Arrays.copyOfRange(data, i+16, i+20);
            byte scaleData[] = Arrays.copyOfRange(data, i+62, i+64);
            int mwidEntry = java.nio.ByteBuffer.wrap(mwidEntryData).order(ByteOrder.LITTLE_ENDIAN).getInt();
            float posX = java.nio.ByteBuffer.wrap(posDataX).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            float posY = java.nio.ByteBuffer.wrap(posDataY).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            float posZ = java.nio.ByteBuffer.wrap(posDataZ).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            Coordinates position = new Coordinates(posX, posY, posZ);
            int scale = java.nio.ByteBuffer.wrap(scaleData).order(ByteOrder.LITTLE_ENDIAN).getShort();

            // Put m2's position and scale into a Multimap
            wmoPosition.put(mwidEntry, position);
            wmoScale.put(mwidEntry, scale);
        }
    }

    public Multimap<Integer, Integer> getWmoScale() {
        return wmoScale;
    }

    public Multimap<Integer, Coordinates> getWmoPosition() {
        return wmoPosition;
    }
}
