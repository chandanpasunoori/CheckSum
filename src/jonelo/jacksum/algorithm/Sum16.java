package jonelo.jacksum.algorithm;

public class Sum16 extends Sum8 {

    public Sum16() {
        super();
        value = 0;
    }

    public long getValue() {
        return value % 0x10000; 
    }

    public byte[] getByteArray() {
        long val = getValue();
        return new byte[]{(byte) ((val >> 8) & 0xff),
            (byte) (val & 0xff)};
    }

}
