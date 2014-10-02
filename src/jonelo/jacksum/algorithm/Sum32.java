package jonelo.jacksum.algorithm;

public class Sum32 extends Sum8 {

    public Sum32() {
        super();
        value = 0;
    }

    public long getValue() {
        return value % 0x100000000L; 
    }

    public byte[] getByteArray() {
        long val = getValue();
        return new byte[]{(byte) ((val >> 24) & 0xff),
            (byte) ((val >> 16) & 0xff),
            (byte) ((val >> 8) & 0xff),
            (byte) (val & 0xff)};
    }

}
