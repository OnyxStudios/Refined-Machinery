package abused_master.refinedmachinery.utils;

public class OreGenEntry {

    private int size, count, maxHeight;
    private boolean generate;

    public OreGenEntry(int size, int count, int maxHeight, boolean generate) {
        this.size = size;
        this.count = count;
        this.maxHeight = maxHeight;
        this.generate = generate;
    }

    public boolean doesGenerate() {
        return generate;
    }

    public int getSize() {
        return size;
    }

    public int getCount() {
        return count;
    }

    public int getMaxHeight() {
        return maxHeight;
    }
}
