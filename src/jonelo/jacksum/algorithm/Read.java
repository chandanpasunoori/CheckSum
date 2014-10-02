package jonelo.jacksum.algorithm;

public class Read extends AbstractChecksum {

    public Read() {
        super();
        encoding = HEX;
    }

    public void reset() {
        length = 0;
    }

    
    public void update(byte[] bytes, int offset, int length) {
        this.length += length;
    }

    public void update(byte[] bytes) {
        this.length += bytes.length;
    }

    public void update(int b) {
        length++;
    }

    public void update(byte b) {
        length++;
    }

    public String toString() {
        return length + separator
                + (isTimestampWanted() ? getTimestampFormatted() + separator : "")
                + getFilename();
    }

    public String getFormattedValue() {
        return "";
    }
}
