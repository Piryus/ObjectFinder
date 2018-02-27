package fr.piryus.model;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.BaseEncoding;

import java.nio.ByteOrder;
import java.util.Arrays;

public class MDDF extends Chunk{

    // Parsed data
    private Multimap<Integer, Coordinates> m2Position = ArrayListMultimap.create();
    private Multimap<Integer, Integer> m2Scale = ArrayListMultimap.create();

    public MDDF(byte bytes[]) {
        // Call super default constructor
        super();
        identifier = "MDDF"; // Captain obvious

        // Define the MDDF check
        identifierCheck = BaseEncoding.base16().lowerCase().decode("4644444D".toLowerCase()); // 46 44 44 4D = FDDM

        // Let the fun part begin aka. parsing the file
        int offset = 0;

        // Identify the offset at which the MMID chunk starts
        while(!Arrays.equals(identifierCheck, Arrays.copyOfRange(bytes, offset, offset+4)))
            offset++;

        // Identify the size of the chunk
        offset+=identifierCheck.length;
        size = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, offset, offset+4)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();

        // Copy chunk data into a byte array
        offset+=4;
        data = Arrays.copyOfRange(bytes, offset, offset + size);

        // Get each m2's position and scale
        for(int i = 0; i < getSize(); i+=36) {
            byte mmidEntryData[] = Arrays.copyOfRange(data, i, i+4);
            byte posDataX[] = Arrays.copyOfRange(data, i+8, i+12);
            byte posDataY[] = Arrays.copyOfRange(data, i+12, i+16);
            byte posDataZ[] = Arrays.copyOfRange(data, i+16, i+20);
            byte scaleData[] = Arrays.copyOfRange(data, i+32, i+34);
            int mmidEntry = java.nio.ByteBuffer.wrap(mmidEntryData).order(ByteOrder.LITTLE_ENDIAN).getInt();
            float posX = java.nio.ByteBuffer.wrap(posDataX).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            float posY = java.nio.ByteBuffer.wrap(posDataY).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            float posZ = java.nio.ByteBuffer.wrap(posDataZ).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            Coordinates position = new Coordinates(posX, posY, posZ);
            int scale = java.nio.ByteBuffer.wrap(scaleData).order(ByteOrder.LITTLE_ENDIAN).getShort();

            // Put m2's position and scale into a Multimap
            m2Position.put(mmidEntry, position);
            m2Scale.put(mmidEntry, scale);
        }
    }

    public Multimap<Integer, Integer> getM2Scale() {
        return m2Scale;
    }

    public Multimap<Integer, Coordinates> getM2Position() {
        return m2Position;
    }
}
