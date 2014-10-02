package jonelo.jacksum.algorithm;

public class Adler32 extends AbstractChecksum {

    private java.util.zip.Adler32 adler32 = null;

    public Adler32() {
        super();
        adler32 = new java.util.zip.Adler32();
    }

    public void reset() {
        adler32.reset();
        length = 0;
    }

    public void update(byte[] buffer, int offset, int len) {
        adler32.update(buffer, offset, len);
        length += len;
    }

    public void update(int b) {
        adler32.update(b);
        length++;
    }

    public long getValue() {
        return adler32.getValue();
    }

    public byte[] getByteArray() {
        long val = getValue();
        return new byte[]{(byte) ((val >> 24) & 0xff),
            (byte) ((val >> 16) & 0xff),
            (byte) ((val >> 8) & 0xff),
            (byte) (val & 0xff)};
    }

}
