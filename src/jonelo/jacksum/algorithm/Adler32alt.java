






package jonelo.jacksum.algorithm;

public class Adler32alt extends AbstractChecksum {

    private static final long BASE = 65521L; 

    public Adler32alt() {
        super();
        reset();
    }

    public void reset() {
        value = 1L;
        length = 0;
    }

    public void update(byte[] buffer, int offset, int len) {
        long s1 = value & 0xffff;
        long s2 = (value >> 16) & 0xffff;

        for (int n = offset; n < len + offset; n++) {
            s1 = (s1 + (buffer[n] & 0xff)) % BASE;
            s2 = (s2 + s1) % BASE;
        }

        value = (s2 << 16) | s1;
        length += len;
    }

    public void update(byte b) {
        update(new byte[]{b}, 0, 1);
    }

    public void update(int b) {
        update((byte) (b & 0xFF));
    }

    public byte[] getByteArray() {
        long val = getValue();
        return new byte[]{(byte) ((val >> 24) & 0xff),
            (byte) ((val >> 16) & 0xff),
            (byte) ((val >> 8) & 0xff),
            (byte) (val & 0xff)};
    }

}
