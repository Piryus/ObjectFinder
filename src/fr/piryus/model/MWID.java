package fr.piryus.model;

import com.google.common.io.BaseEncoding;

import java.util.ArrayList;
import java.util.Arrays;

public class MWID extends Chunk {

    private ArrayList<Integer> filenameOffset = new ArrayList<>();

    public MWID(byte bytes[]) {
        // Call super default constructor
        super();
        identifier = "MWID"; // Captain obvious

        // Define the MWID check
        identifierCheck = BaseEncoding.base16().lowerCase().decode("4449574D".toLowerCase()); // 44 49 57 4D = DIWM

        // Let the fun part begin aka. parsing the file
        int offset = 0;

        // Identify the offset at which the MWID chunk starts
        while(!Arrays.equals(identifierCheck, Arrays.copyOfRange(bytes, offset, offset+4)))
            offset++;

        // Identify the size of the chunk
        offset+=identifierCheck.length;
        size = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, offset, offset+4)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();

        // Convert the data bytes into a list of filename offset
        offset+=4;
        data = Arrays.copyOfRange(bytes, offset, offset + size);
        for(int i = 0; i < size; i+=4) {
            filenameOffset.add(java.nio.ByteBuffer.wrap(Arrays.copyOfRange(data, i, i+4)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt());
        }

        //System.out.println(filenameOffset);
    }

    public ArrayList<Integer> getFilenameOffset() {
        return filenameOffset;
    }
}
