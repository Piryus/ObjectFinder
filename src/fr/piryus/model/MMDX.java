package fr.piryus.model;

import com.google.common.io.BaseEncoding;

import java.util.Arrays;

public class MMDX extends Chunk {

    public MMDX(byte[] bytes) {
        // Call default constructor
        super();
        identifier = "MMDX"; // Captain obvious

        // Define the MMDX check
        identifierCheck = BaseEncoding.base16().lowerCase().decode("58444D4D".toLowerCase()); // 58 44 4D 4D = XDMM

        // Let the fun part begin aka. parsing the file
        int offset = 0;

        // Identify the offset at which  the MMDX chunk starts
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