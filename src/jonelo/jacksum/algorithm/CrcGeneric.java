package jonelo.jacksum.algorithm;

import java.security.NoSuchAlgorithmException;
import jonelo.sugar.util.GeneralString;
import jonelo.jacksum.util.Service;

public class CrcGeneric extends AbstractChecksum {

    private int width;         
    private long poly;         
    private long initialValue; 
    private boolean refIn;     
    private boolean refOut;    
    private long xorOut;       
    private long[] table;      
    private long topBit;       
    private long maskAllBits;  
    private long maskHelp;     

    public CrcGeneric(int width, long poly,
            long initialValue, boolean refIn,
            boolean refOut, long xorOut) throws NoSuchAlgorithmException {
        super();
        this.width = width;
        this.poly = poly;
        this.initialValue = initialValue;
        this.refIn = refIn;
        this.refOut = refOut;
        this.xorOut = xorOut;
        init();
    }

    public CrcGeneric(String props) throws NoSuchAlgorithmException {
        String[] array = GeneralString.split(props, ","); 
        if (array.length != 6) {
            throw new NoSuchAlgorithmException("Can't create the algorithm, 6 parameters are expected");
        }
        try {
            width = Integer.parseInt(array[0]);
            poly = Long.parseLong(array[1], 16);
            initialValue = new java.math.BigInteger(array[2], 16).longValue(); 
            refIn = array[3].equalsIgnoreCase("true");
            refOut = array[4].equalsIgnoreCase("true");
            xorOut = new java.math.BigInteger(array[5], 16).longValue();
        } catch (NumberFormatException e) {
            throw new NoSuchAlgorithmException("Unknown algorithm: invalid parameters. " + e.toString());
        }
        init();
    }

    private void init() throws NoSuchAlgorithmException {
        topBit = 1L << (width - 1);       
        maskAllBits = ~0L >>> (64 - width); 
        maskHelp = maskAllBits >>> 8;     
        check();
        fillTable();
        reset();
    }

    private void check() throws NoSuchAlgorithmException {
        if (width < 8 || width > 64) {
            throw new NoSuchAlgorithmException("Error: width has to be in range [8..64].");
        }

        if (poly != (poly & maskAllBits)) {
            throw new NoSuchAlgorithmException("Error: invalid polynomial for the " + width + " bit CRC.");
        }

        if (initialValue != (initialValue & maskAllBits)) {
            throw new NoSuchAlgorithmException("Error: invalid init value for the " + width + " bit CRC.");
        }

        if (xorOut != (xorOut & maskAllBits)) {
            throw new NoSuchAlgorithmException("Error: invalid xorOut value for the " + width + " bit CRC.");
        }
    }

    public void reset() {
        length = 0;
        
        value = initialValue;

        if (refIn) {
            
            value = reflect(value, width);
        }
    }

    public String getString() {
        StringBuffer sb = new StringBuffer();
        int nibbles = width / 4 + ((width % 4 > 0) ? 1 : 0);
        sb.append(width);
        sb.append(",");
        sb.append(Service.hexformat(poly, nibbles).toUpperCase());
        sb.append(",");
        sb.append(Service.hexformat(initialValue, nibbles).toUpperCase());
        sb.append(",");
        sb.append(refIn ? "true" : "false");
        sb.append(",");
        sb.append(refOut ? "true" : "false");
        sb.append(",");
        sb.append(Service.hexformat(xorOut, nibbles).toUpperCase());
        return sb.toString();
    }

    public String getName() {
        if (name == null) {
            return getString();
        } else {
            return name;
        }
    }

    public void setInitialValue(long initialValue) {
        this.initialValue = initialValue;
    }

    public long getInitialValue() {
        return initialValue;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setPoly(long poly) {
        this.poly = poly;
    }

    public long getPoly() {
        return this.poly;
    }

    public void setRefIn(boolean refIn) {
        this.refIn = refIn;
    }

    public boolean getRefIn() {
        return this.refIn;
    }

    public void setRefOut(boolean refOut) {
        this.refOut = refOut;
    }

    public boolean getRefOut() {
        return this.refOut;
    }

    public void setXorOut(long xorOut) {
        this.xorOut = xorOut;
    }

    public long getXorOut() {
        return xorOut;
    }

    private static long reflect(long value, int bits) {
        long temp = 0L;
        for (int i = 0; i < bits; i++) {
            temp <<= 1L;
            temp |= (value & 1L);
            value >>>= 1L;
        }
        return (value << bits) | temp;
    }

    private void fillTable() {
        long remainder;
        boolean mybit;
        table = new long[256];

        
        for (int dividend = 0; dividend < 256; dividend++) {

            
            remainder = (long) dividend;

            if (refIn) { 
                remainder = reflect(remainder, 8);
            }

            remainder <<= (width - 8);

            for (int bit = 0; bit < 8; bit++) {
                
                mybit = ((remainder & topBit) != 0);
                remainder <<= 1;
                if (mybit) {
                    remainder ^= poly;
                }
            }

            if (refIn) { 
                remainder = reflect(remainder, width);
            }
            
            table[dividend] = (remainder & maskAllBits);
        }
    }

    public void update(byte b) {
        
        int index;
        if (refIn) {
            
            index = ((int) (value ^ b) & 0xff);

            
            value >>>= 8;

            
            value &= maskHelp;

        } else {
            
            index = ((int) ((value >>> (width - 8)) ^ b) & 0xff);

            
            value <<= 8;
        }

        
        value ^= (table[index]);
        length++;
    }

    public void update(int b) {
        update((byte) (b & 0xFF));
    }

    public long getValue() {
        return getFinal();
    }

    private long getFinal() {
        long remainder = value; 

        
        if (refIn != refOut) {
            remainder = reflect(remainder, width);
        }
        
        return (remainder ^ xorOut) & maskAllBits;
    }

    public byte[] getByteArray() {
        long finalvalue = getFinal();
        byte array[] = new byte[width / 8 + ((width % 8 > 0) ? 1 : 0)];

        for (int i = array.length - 1; i > -1; i--) {
            array[i] = (byte) (finalvalue & 0xFF);
            finalvalue >>>= 8;
        }
        return array;
    }

}
