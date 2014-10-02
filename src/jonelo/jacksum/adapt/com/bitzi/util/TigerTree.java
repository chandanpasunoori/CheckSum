









package jonelo.jacksum.adapt.com.bitzi.util;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Vector;
import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

public class TigerTree extends MessageDigest {

    private static final int BLOCKSIZE = 1024;
    private static final int HASHSIZE = 24;

    private final byte[] buffer;

    private int bufferOffset;

    private long byteCount;

    private AbstractChecksum tiger;

    private Vector nodes;

    public TigerTree(String name) throws NoSuchAlgorithmException {
        super(name);
        buffer = new byte[BLOCKSIZE];
        bufferOffset = 0;
        byteCount = 0;
        tiger = JacksumAPI.getChecksumInstance(name);
        nodes = new Vector();
    }

    protected int engineGetDigestLength() {
        return HASHSIZE;
    }

    protected void engineUpdate(byte in) {
        byteCount += 1;
        buffer[bufferOffset++] = in;
        if (bufferOffset == BLOCKSIZE) {
            blockUpdate();
            bufferOffset = 0;
        }
    }

    protected void engineUpdate(byte[] in, int offset, int length) {
        byteCount += length;

        int remaining;
        while (length >= (remaining = BLOCKSIZE - bufferOffset)) {
            System.arraycopy(in, offset, buffer, bufferOffset, remaining);
            bufferOffset += remaining;
            blockUpdate();
            length -= remaining;
            offset += remaining;
            bufferOffset = 0;
        }

        System.arraycopy(in, offset, buffer, bufferOffset, length);
        bufferOffset += length;
    }

    protected byte[] engineDigest() {
        byte[] hash = new byte[HASHSIZE];
        try {
            engineDigest(hash, 0, HASHSIZE);
        } catch (DigestException e) {
            return null;
        }
        return hash;
    }

    protected int engineDigest(byte[] buf, int offset, int len)
            throws DigestException {
        if (len < HASHSIZE) {
            throw new DigestException();
        }

        
        blockUpdate();

        
        while (nodes.size() > 1) {
            Vector newNodes = new Vector();
            Enumeration iter = nodes.elements();
            while (iter.hasMoreElements()) {
                byte[] left = (byte[]) iter.nextElement();
                if (iter.hasMoreElements()) {
                    byte[] right = (byte[]) iter.nextElement();
                    tiger.reset();
                    tiger.update((byte) 1); 
                    tiger.update(left);
                    tiger.update(right);
                    newNodes.addElement((Object) tiger.getByteArray());
                } else {
                    newNodes.addElement((Object) left);
                }
            }
            nodes = newNodes;
        }
        System.arraycopy(nodes.elementAt(0), 0, buf, offset, HASHSIZE);
        engineReset();
        return HASHSIZE;
    }

    protected void engineReset() {
        bufferOffset = 0;
        byteCount = 0;
        nodes = new Vector();
        tiger.reset();
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    protected void blockUpdate() {
        tiger.reset();
        tiger.update((byte) 0); 
        tiger.update(buffer, 0, bufferOffset);
        if ((bufferOffset == 0) && (nodes.size() > 0)) 
        {
            return; 
        }
        nodes.addElement((Object) tiger.getByteArray());
    }

}
