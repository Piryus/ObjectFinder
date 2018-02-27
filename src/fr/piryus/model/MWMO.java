package fr.piryus.model;

import com.google.common.io.BaseEncoding;

import java.util.Arrays;

public class MWMO extends Chunk {

    public MWMO(byte[] bytes) {
        // Call default constructor
        super();
        identifier = "MWMO"; // Captain obvious

        // Define the MWMO check
        identifierCheck = BaseEncoding.base16().lowerCase().decode("4F4D574D".toLowerCase()); // 4F 4D 57 4D = OMWM

        // Let the fun part begin aka. parsing the file
        int offset = 0;

        // Identify the offset at which  the MWMO chunk starts
        while (!Arrays.equals(identifierCheck, Arrays.copyOfRange(bytes, offset, offset + 4)))
            offset++;

        // Identify the size of the chunk
        offset += identifierCheck.length;
        size = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(bytes, offset, offset + 4)).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();

        // Convert the data bytes into a string
        offset += 4;
        data = Arrays.copyOfRange(bytes, offset, offset + size);
    }
}