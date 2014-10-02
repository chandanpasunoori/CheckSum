package jonelo.jacksum.algorithm;

public class Sum24 extends Sum8 {

    public Sum24() {
        super();
        value = 0;
    }

    public long getValue() {
        return value % 0x1000000; 
    }

    public byte[] getByteArray() {
        long val = getValue();
        return new byte[]{(byte) ((val >> 16) & 0xff),
            (byte) ((val >> 8) & 0xff),
            (byte) (val & 0xff)};
    }

}
