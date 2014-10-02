package jonelo.jacksum.cli;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.ui.ExitStatus;
import jonelo.sugar.util.ExitException;

public class JacksumHelp {

    public static void printVersion() {
        System.out.println(JacksumAPI.NAME + " " + JacksumAPI.VERSION);
    }

    public static void printGPL() {
    }

    public static void printHelpShort() throws ExitException {
        printGPL();
        System.out.println(" For more information please type:");
        System.out.println(" java -jar jacksum.jar -h en");
        throw new ExitException(null, ExitStatus.OK);
    }

    public static void printHelpLong(String filename, String search) throws FileNotFoundException, IOException {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            is = Jacksum.class.getResourceAsStream(filename);
            if (is == null) {
                throw new FileNotFoundException(filename);
            }
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line;
            if (search == null) {
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } else {
                StringBuffer sb = new StringBuffer();
                boolean found = false;
                while ((line = br.readLine()) != null) {

                    if (line.length() == 0) {

                        if (found && sb.length() > 0) {
                            System.out.println(sb.toString());
                        }

                        found = false;

                        sb = new StringBuffer();
                    } else {
                        sb.append(line);
                        sb.append('\n');
                        if (!found
                                && ((line.length() > 18 && line.substring(0, 18).trim().toLowerCase().startsWith(search))
                                || (line.toLowerCase().startsWith(search)))) {
                            found = true;
                        }
                    }

                }
            }
        } finally {
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    public static void help(String code, String search) throws ExitException {
        String filename = "/help/jacksum/help_" + code + ".txt";
        int exitcode = ExitStatus.OK;
        try {
            printHelpLong(filename, search);
        } catch (FileNotFoundException fnfe) {
            System.err.println("Helpfile " + filename + " not found.");
            exitcode = ExitStatus.PARAMETER;
        } catch (IOException ioe) {
            System.err.println("Problem while reading helpfile " + filename);
            exitcode = ExitStatus.PARAMETER;
        }
        throw new ExitException(null, exitcode);
    }
}
