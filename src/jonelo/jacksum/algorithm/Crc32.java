package jonelo.jacksum.algorithm;

import java.util.zip.CRC32;

public class Crc32 extends AbstractChecksum {

    private CRC32 crc32 = null;

    public Crc32() {
        super();
        crc32 = new CRC32();
    }

    public void reset() {
        crc32.reset();
        length = 0;
    }

    public void update(byte[] buffer, int offset, int len) {
        crc32.update(buffer, offset, len);
        length += len;
    }

    public void update(int b) {
        crc32.update(b);
        length++;
    }

    public void update(byte b) {
        update((int) (b & 0xFF));
    }

    public long getValue() {
        return crc32.getValue();
    }

    public byte[] getByteArray() {
        long val = crc32.getValue();
        return new byte[]{(byte) ((val >> 24) & 0xff),
            (byte) ((val >> 16) & 0xff),
            (byte) ((val >> 8) & 0xff),
            (byte) (val & 0xff)};
    }

}
