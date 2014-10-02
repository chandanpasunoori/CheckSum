package jonelo.jacksum.algorithm;

public class Crc32Mpeg2 extends Cksum {

    public void reset() {
        
        value = 0xFFFFFFFF;
        length = 0;
    }

    
    
    public long getValue() {
        return (value & 0xFFFFFFFFL);
    }

}
