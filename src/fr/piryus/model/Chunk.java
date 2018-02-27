package fr.piryus.model;

public class Chunk {

    protected String identifier;
    protected int size;
    protected byte data[];
    protected byte identifierCheck[];

    public Chunk() {}

    public byte[] getData() {
        return data;
    }

    public int getSize() {
        return size;
    }
}
