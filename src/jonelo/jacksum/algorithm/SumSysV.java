package jonelo.jacksum.algorithm;


public class SumSysV extends AbstractChecksum {

    public SumSysV() {
        super();
        separator = " ";
    }

    public void update(int b) {
        value += b & 0xFF;
        length++;
    }

    public void update(byte b) {
        value += b & 0xFF;
        length++;
    }

    public long getValue() {
        long r = (value & 0xffff) + (((value & 0xffffffff) >> 16) & 0xffff);
        value = (r & 0xffff) + (r >> 16);
        return value;
    }

    public String toString() {
        long kb = (length + 511) / 512;
        return getFormattedValue()
                + separator + kb + separator
                + (isTimestampWanted() ? getTimestampFormatted() + separator : "")
                + filename;
    }

    public byte[] getByteArray() {
        long val = getValue();
        return new byte[]{(byte) ((val >> 8) & 0xff),
            (byte) (val & 0xff)};
    }

}
