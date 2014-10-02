package jonelo.jacksum.algorithm;

import jonelo.jacksum.adapt.gnu.crypto.hash.HashFactory;
import jonelo.jacksum.adapt.gnu.crypto.hash.IMessageDigest;
import java.security.NoSuchAlgorithmException;

public class MDgnu extends AbstractChecksum {

    private IMessageDigest md = null;
    private boolean virgin = true;
    private byte[] digest = null;

    public MDgnu(String arg) throws NoSuchAlgorithmException {
        
        length = 0;
        filename = null;
        separator = " ";
        encoding = HEX;
        md = HashFactory.getInstance(arg);
        if (md == null) {
            throw new NoSuchAlgorithmException(arg + " is an unknown algorithm.");
        }
        virgin = true;
    }

    public void reset() {
        md.reset();
        length = 0;
        virgin = true;
    }

    public void update(byte[] buffer, int offset, int len) {
        md.update(buffer, offset, len);
        length += len;
    }

    public void update(byte b) {
        md.update(b);
        length++;
    }

    public void update(int b) {
        update((byte) (b & 0xFF));
    }

    public String toString() {
        return getFormattedValue() + separator
                + (isTimestampWanted() ? getTimestampFormatted() + separator : "")
                + getFilename();
    }

    public byte[] getByteArray() {
        if (virgin) {
            digest = md.digest();
            virgin = false;
        }
        
        byte[] save = new byte[digest.length];
        System.arraycopy(digest, 0, save, 0, digest.length);
        return save;
    }

}
