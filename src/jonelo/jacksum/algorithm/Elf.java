package jonelo.jacksum.algorithm;

public class Elf extends AbstractChecksum {

    protected long ghash;

    public Elf() {
        super();
        reset();
    }

    public void reset() {
        value = 0;
        length = 0;
    }

    public void update(byte b) {
        value = (value << 4) + (b & 0xFF);
        long ghash = value & 0xF0000000L;
        if (ghash != 0) {
            value ^= (ghash >>> 24);
        }
        value &= ~ghash;
        length++;
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
