package jonelo.jacksum.algorithm;

public class Sum8 extends AbstractChecksum {

    public Sum8() {
        super();
        value = 0;
    }

    public void reset() {
        value = 0;
        length = 0;
    }

    public void update(byte b) {
        value += b & 0xFF;
        length++;
    }

    public void update(int b) {
        value += b & 0xFF;
        length++;
    }

    public long getValue() {
        return value % 256;
    }

    public byte[] getByteArray() {
        return new byte[]{(byte) (getValue() & 0xff)};
    }

}
