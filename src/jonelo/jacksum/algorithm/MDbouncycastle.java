package jonelo.jacksum.algorithm;

import java.security.NoSuchAlgorithmException;
import jonelo.jacksum.adapt.org.bouncycastle.crypto.Digest;
import jonelo.jacksum.adapt.org.bouncycastle.crypto.digests.GOST3411Digest;
import jonelo.jacksum.adapt.org.bouncycastle.crypto.digests.RIPEMD256Digest;
import jonelo.jacksum.adapt.org.bouncycastle.crypto.digests.RIPEMD320Digest;

public class MDbouncycastle extends AbstractChecksum {

    private Digest md = null;
    private boolean virgin = true;
    private byte[] digest = null;

    public MDbouncycastle(String arg) throws NoSuchAlgorithmException {
        
        length = 0;
        filename = null;
        separator = " ";
        encoding = HEX;
        virgin = true;
        if (arg.equalsIgnoreCase("gost")) {
            md = new GOST3411Digest();
        } else if (arg.equalsIgnoreCase("ripemd256")) {
            md = new RIPEMD256Digest();
        } else if (arg.equalsIgnoreCase("ripemd320")) {
            md = new RIPEMD320Digest();
        } else {
            throw new NoSuchAlgorithmException(arg + " is an unknown algorithm.");
        }
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
            digest = new byte[md.getDigestSize()];
            md.doFinal(digest, 0);
            
            virgin = false;
        }
        
        byte[] save = new byte[digest.length];
        System.arraycopy(digest, 0, save, 0, digest.length);
        return save;
    }

}
