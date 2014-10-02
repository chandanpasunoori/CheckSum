package jonelo.jacksum.algorithm;

import jonelo.jacksum.util.Service;

public class SumBSD extends AbstractChecksum {

    public SumBSD() {
        super();
        separator = " ";
    }

    public void update(byte b) {
        value = (value >> 1) + ((value & 1) << 15);
        value += b & 0xFF;
        value &= 0xffff;
        length++;
    }

    public void update(int b) {
        update((byte) (b & 0xFF));
    }

    public String toString() {
        long kb = (length + 1023) / 1024;
        return ((getEncoding().length() == 0) ? Service.decformat(getValue(), "00000") : getFormattedValue())
                + separator + Service.right(kb, 5) + separator
                + (isTimestampWanted() ? getTimestampFormatted() + separator : "")
                + getFilename();
    }

    public byte[] getByteArray() {
        long val = getValue();
        return new byte[]{(byte) ((val >> 8) & 0xff),
            (byte) (val & 0xff)};
    }

}
