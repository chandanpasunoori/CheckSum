package jonelo.jacksum.algorithm;

import java.io.*;

public class None extends AbstractChecksum {

    public None() {
        super();
        encoding = HEX;
    }

    public void reset() {
        length = 0;
    }

    public String toString() {
        return length + separator
                + (isTimestampWanted() ? getTimestampFormatted() + separator : "")
                + getFilename();
    }

    public String getFormattedValue() {
        return "";
    }

    public long readFile(String filename, boolean reset) throws IOException {
        this.filename = filename;
        if (isTimestampWanted()) {
            setTimestamp(filename);
        }

        File f = new File(filename);
        length = f.length();
        return length;
    }

}
