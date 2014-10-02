package jonelo.jacksum.algorithm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.Checksum;
import jonelo.jacksum.util.Service;
import jonelo.sugar.util.Base32;
import jonelo.sugar.util.Base64;
import jonelo.sugar.util.BubbleBabble;
import jonelo.sugar.util.EncodingException;
import jonelo.sugar.util.GeneralString;

abstract public class AbstractChecksum implements Checksum {

    public final static String BIN = "bin";
    public final static String DEC = "dec";
    public final static String OCT = "oct";
    public final static String HEX = "hex";
    public final static String HEX_UPPERCASE = "hexup";
    public final static String BASE16 = "base16";
    public final static String BASE32 = "base32";
    public final static String BASE64 = "base64";
    public final static String BUBBLEBABBLE = "bubblebabble";
    public final static int BUFFERSIZE = 8192;

    protected long value;
    protected long length;
    protected String separator;
    protected String filename;
    protected String encoding;
    protected int group; 
    protected char groupChar; 

    protected String timestampFormat;
    protected Format timestampFormatter;
    protected long timestamp;
    protected String name;

    public AbstractChecksum() {
        value = 0;
        length = 0;
        separator = "\t";
        filename = null;
        encoding = "";
        timestampFormat = null;
        timestampFormatter = null;
        timestamp = 0;
        group = 0;
        groupChar = ' ';
        name = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    
    public void reset() {
        value = 0;
        length = 0;
    }

    
    public void update(int b) {
        length++;
    }

    public void update(byte b) {
        update((int) (b & 0xFF));
    }

    
    public void update(byte[] bytes, int offset, int length) {
        for (int i = offset; i < length + offset; i++) {
            update(bytes[i]);
        }
    }

    public void update(byte[] bytes) {
        update(bytes, 0, bytes.length);
    }

    public long getValue() {
        return value;
    }

    public long getLength() {
        return length;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getSeparator() {
        return separator;
    }

    public byte[] getByteArray() {
        return new byte[]{(byte) (value & 0xff)};
    }

    public String toString() {
        return getFormattedValue() + separator
                + length + separator
                + (isTimestampWanted() ? getTimestampFormatted() + separator : "")
                + filename;
    }

    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof AbstractChecksum) {
            AbstractChecksum abstractChecksum = (AbstractChecksum) anObject;
            return Arrays.equals(getByteArray(), abstractChecksum.getByteArray());
        }
        return false;
    }

    public int hashCode() {
        
        
        
        
        byte b[] = getByteArray();
        int s = 0;
        for (int i = 0; i < b.length; i++) {
            s = ((s << 8) + b[i]) % 0x7FFFF1; 
        }
        return s;
    }

    public String getFormattedValue() {
        if (encoding.equalsIgnoreCase(HEX)) {
            
            return Service.format(getByteArray(), false, group, groupChar);
        } else if (encoding.equalsIgnoreCase(HEX_UPPERCASE)) {
            return Service.format(getByteArray(), true, group, groupChar);
        } else if (encoding.equalsIgnoreCase(BASE16)) {
            return Service.format(getByteArray(), true, 0, groupChar);
        } else if (encoding.equalsIgnoreCase(BASE32)) {
            return Base32.encode(getByteArray());
        } else if (encoding.equalsIgnoreCase(BASE64)) {
            return Base64.encodeBytes(getByteArray(), Base64.DONT_BREAK_LINES);
        } else if (encoding.equalsIgnoreCase(BUBBLEBABBLE)) {
            return BubbleBabble.encode(getByteArray());
        } else if (encoding.equalsIgnoreCase(DEC)) {
            BigInteger big = new BigInteger(1, getByteArray());
            return big.toString();
        } else if (encoding.equalsIgnoreCase(BIN)) {
            return Service.formatAsBits(getByteArray());
        } else if (encoding.equalsIgnoreCase(OCT)) {
            BigInteger big = new BigInteger(1, getByteArray());
            return big.toString(8);
        } else 
        {
            return Long.toString(getValue()); 
        }
    }

    
    public void firstFormat(StringBuffer format) {
        
        GeneralString.replaceAllStrings(format, "#FINGERPRINT", "#CHECKSUM");
    }

    
    public String format(String format) {

        StringBuffer temp = new StringBuffer(format);
        firstFormat(temp);
        GeneralString.replaceAllStrings(temp, "#CHECKSUM{i}", "#CHECKSUM");
        GeneralString.replaceAllStrings(temp, "#ALGONAME{i}", "#ALGONAME");

        GeneralString.replaceAllStrings(temp, "#ALGONAME", getName());
        
        
        GeneralString.replaceAllStrings(temp, "#CHECKSUM", getFormattedValue());
        
        GeneralString.replaceAllStrings(temp, "#FILESIZE", Long.toString(length));
        
        if (temp.toString().indexOf("#FILENAME{") > -1) { 
            File filetemp = new File(filename);
            GeneralString.replaceAllStrings(temp, "#FILENAME{NAME}", filetemp.getName());
            String parent = filetemp.getParent();
            if (parent == null) {
                parent = "";
            } else if (!parent.endsWith(File.separator)
                    && 
                    (!parent.endsWith(":") && System.getProperty("os.name").toLowerCase().startsWith("windows"))) {
                parent += File.separator;
            }
            GeneralString.replaceAllStrings(temp, "#FILENAME{PATH}", parent);
        }
        GeneralString.replaceAllStrings(temp, "#FILENAME", filename);
        
        if (isTimestampWanted()) {
            GeneralString.replaceAllStrings(temp, "#TIMESTAMP", getTimestampFormatted());
        }
        
        GeneralString.replaceAllStrings(temp, "#SEPARATOR", separator);
        GeneralString.replaceAllStrings(temp, "#QUOTE", "\"");
        return temp.toString();
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setEncoding(String encoding) throws EncodingException {
        if (encoding == null) {
            this.encoding = ""; 
        } else if (encoding.equalsIgnoreCase("bb")) { 
            this.encoding = BUBBLEBABBLE;
        } else if ((encoding.length() == 0) || 
                encoding.equalsIgnoreCase(HEX)
                || encoding.equalsIgnoreCase(HEX_UPPERCASE)
                || encoding.equalsIgnoreCase(DEC)
                || encoding.equalsIgnoreCase(BIN)
                || encoding.equalsIgnoreCase(OCT)
                || encoding.equalsIgnoreCase(BASE16)
                || encoding.equalsIgnoreCase(BASE32)
                || encoding.equalsIgnoreCase(BASE64)
                || encoding.equalsIgnoreCase(BUBBLEBABBLE)) {
            this.encoding = encoding;
        } else {
            throw new EncodingException("Encoding is not supported");
        }
    }

    public String getEncoding() {
        return encoding;
    }

    public boolean isGroupWanted() {
        return (group > 0);
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getGroup() {
        return group;
    }

    public void setGroupChar(char groupChar) {
        this.groupChar = groupChar;
    }

    public char getGroupChar() {
        return groupChar;
    }

    public void setGrouping(int group, char groupChar) {
        setGroup(group);
        setGroupChar(groupChar);
    }

    public void setHex(boolean hex) {
        encoding = hex ? HEX : "";
    }

    public void setUpperCase(boolean uppercase) {
        encoding = uppercase ? HEX_UPPERCASE : HEX;
    }

    public String getHexValue() {
        return Service.format(getByteArray(), encoding.equalsIgnoreCase(HEX_UPPERCASE), group, groupChar);
    }

    public void setTimestamp(String filename) {
        File file = new File(filename);
        this.timestamp = file.lastModified();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestampFormat(String timestampFormat) {
        this.timestampFormat = timestampFormat;
    }

    public String getTimestampFormat() {
        return timestampFormat;
    }

    public String getTimestampFormatted() {
        if (timestampFormatter == null) {
            timestampFormatter = new SimpleDateFormat(timestampFormat);
        }
        return timestampFormatter.format(new Date(timestamp));
    }

    public boolean isTimestampWanted() {
        return (timestampFormat != null);
    }

    public long readFile(String filename) throws IOException {
        return readFile(filename, true);
    }

    public long readFile(String filename, boolean reset) throws IOException {
        this.filename = filename;
        if (isTimestampWanted()) {
            setTimestamp(filename);
        }

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        long lengthBackup = 0;

        
        try {
            fis = new FileInputStream(filename);
            bis = new BufferedInputStream(fis);
            if (reset) {
                reset();
            }
            lengthBackup = length;
            int len = 0;
            byte[] buffer = new byte[BUFFERSIZE];
            while ((len = bis.read(buffer)) > -1) {
                update(buffer, 0, len);
            }
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return length - lengthBackup;
    }

}
