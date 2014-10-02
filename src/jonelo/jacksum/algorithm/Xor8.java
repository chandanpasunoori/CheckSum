package jonelo.jacksum.algorithm;

public class Xor8 extends AbstractChecksum {

    public Xor8() {
        super();
        value = 0;
    }

    public void update(byte b) {
        value ^= b & 0xFF;
        length++;
    }

    public void update(int b) {
        update((byte) (b & 0xFF));
    }

}
