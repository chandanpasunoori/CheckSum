package jonelo.jacksum.adapt.gnu.crypto.hash;











































public interface IMessageDigest extends Cloneable {

   
    
    
    
    String name();

    int hashSize();

    int blockSize();

    void update(byte b);

    void update(byte[] in, int offset, int length);

    byte[] digest();

    void reset();

    boolean selfTest();

    Object clone();
}
