package jonelo.jacksum.algorithm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.util.Service;
import jonelo.sugar.util.EncodingException;
import jonelo.sugar.util.GeneralString;

public class CombinedChecksum extends AbstractChecksum {

    private Vector algorithms;

    public CombinedChecksum() {
        init();
    }

    public CombinedChecksum(String[] algos, boolean alternate) throws NoSuchAlgorithmException {
        init();
        setAlgorithms(algos, alternate);
    }

    private void init() {
        algorithms = new Vector();
        length = 0;
        filename = null;
        separator = " ";
        encoding = HEX;
    }

    public void addAlgorithm(String algorithm, boolean alternate) throws NoSuchAlgorithmException {
        AbstractChecksum checksum = JacksumAPI.getChecksumInstance(algorithm, alternate);
        checksum.setName(algorithm);
        algorithms.add(checksum);
    }

    public void setAlgorithms(String[] algos, boolean alternate) throws NoSuchAlgorithmException {
        for (int i = 0; i < algos.length; i++) {
            addAlgorithm(algos[i], alternate);
        }
    }

    public void reset() {
        
        for (int i = 0; i < algorithms.size(); i++) {
            ((AbstractChecksum) algorithms.elementAt(i)).reset();
        }
        length = 0;
    }

    public void update(int b) {
        for (int i = 0; i < algorithms.size(); i++) {
            ((AbstractChecksum) algorithms.elementAt(i)).update(i);
        }
        length++;
    }

    public void update(byte b) {
        for (int i = 0; i < algorithms.size(); i++) {
            ((AbstractChecksum) algorithms.elementAt(i)).update(b);
        }
        length++;
    }

    public void update(byte[] bytes, int offset, int length) {
        for (int i = 0; i < algorithms.size(); i++) {
            ((AbstractChecksum) algorithms.elementAt(i)).update(bytes, offset, length);
        }
        this.length += length;
    }

    public void update(byte[] bytes) {
        for (int i = 0; i < algorithms.size(); i++) {
            ((AbstractChecksum) algorithms.elementAt(i)).update(bytes);
        }
        this.length += bytes.length;
    }

    public byte[] getByteArray() {
        Vector v = new Vector();
        int size = 0;
        for (int i = 0; i < algorithms.size(); i++) {
            byte[] tmp = ((AbstractChecksum) algorithms.elementAt(i)).getByteArray();
            v.add(tmp);
            size += tmp.length;
        }
        byte[] ret = new byte[size];
        int offset = 0;
        for (int i = 0; i < v.size(); i++) {
            byte[] src = (byte[]) v.elementAt(i);
            System.arraycopy(src, 0, ret, offset, src.length);
            offset += src.length;
        }
        return ret;
    }

    public void firstFormat(StringBuffer formatBuf) {

        
        GeneralString.replaceAllStrings(formatBuf, "#FINGERPRINT", "#CHECKSUM");

        
        setEncoding(encoding);

        StringBuffer buf = new StringBuffer();
        String format = formatBuf.toString();

        if (format.indexOf("#CHECKSUM{i}") > -1 || format.indexOf("#ALGONAME{i}") > -1) {

            for (int i = 0; i < algorithms.size(); i++) {
                StringBuffer line = new StringBuffer(format);
                GeneralString.replaceAllStrings(line, "#CHECKSUM{i}",
                        ((AbstractChecksum) algorithms.elementAt(i)).getFormattedValue());
                GeneralString.replaceAllStrings(line, "#ALGONAME{i}",
                        ((AbstractChecksum) algorithms.elementAt(i)).getName());
                buf.append(line);
                if (algorithms.size() > 1) {
                    buf.append("\n");
                }
            }
        } else {
            buf.append(format);
        }

        
        if (buf.toString().indexOf("#CHECKSUM{") > -1) {
            
            for (int i = 0; i < algorithms.size(); i++) {
                GeneralString.replaceAllStrings(buf, "#CHECKSUM{" + i + "}",
                        ((AbstractChecksum) algorithms.elementAt(i)).getFormattedValue());
            }
        }

        if (buf.toString().indexOf("#ALGONAME{") > -1) {
            
            for (int i = 0; i < algorithms.size(); i++) {
                GeneralString.replaceAllStrings(buf, "#ALGONAME{" + i + "}",
                        ((AbstractChecksum) algorithms.elementAt(i)).getName());
            }
        }
        formatBuf.setLength(0);
        formatBuf.append(buf.toString());
    }

    public void setEncoding(String encoding) throws EncodingException {
        for (int i = 0; i < algorithms.size(); i++) {
            ((AbstractChecksum) algorithms.elementAt(i)).setEncoding(encoding);
        }
        this.encoding = ((AbstractChecksum) algorithms.elementAt(0)).getEncoding();
    }

}
