package jonelo.jacksum.cli;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.ui.ExitStatus;
import jonelo.sugar.util.ExitException;

/**
 *
 * @author jonelo
 */
public class JacksumHelp {

    /**
     * print Jacksum's program version
     *
     * @return version number
     */
    public static void printVersion() {
        System.out.println(JacksumAPI.NAME + " " + JacksumAPI.VERSION);
    }

    /**
     * print the GPL information and an OSI certified note to stdout
     */
    public static void printGPL() {
        System.out.println("\n " + JacksumAPI.NAME + " v" + JacksumAPI.VERSION + ", Copyright (C) 2011-2014, EVA TECHSOFT. Chandan Paunoori\n");
        System.out.println(" " + JacksumAPI.NAME + " comes with ABSOLUTELY NO WARRANTY; for details see 'license.txt'.");
        System.out.println(" This is free software, and you are welcome to redistribute it under certain");
        System.out.println(" conditions; see 'license.txt' for details.");
        System.out.println(" This software is EvaTechSoft Certified Open Source Software.");
        System.out.println(" Go to http://www.evetechsoft.com to get the latest version.\n");
    }

    /**
     * print GPL info and a short help
     */
    public static void printHelpShort() throws ExitException {
        printGPL();
        System.out.println(" For more information please type:");
        System.out.println(" java -jar jacksum.jar -h en");
        System.out.println("\n Fuer weitere Informationen bitte eingeben:");
        System.out.println(" java -jar jacksum.jar -h de\n");
        throw new ExitException(null, ExitStatus.OK);
    }

    /**
     * print the documentation
     *
     * @param filename the flat file containing the documentation
     */
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

                    // put lines to buffer
                    if (line.length() == 0) {
                        // put out old buffer
                        if (found && sb.length() > 0) {
                            System.out.println(sb.toString());
                        }
                        // new chance
                        found = false;
                        // new buffer
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

                } // end-while
            } // end-if
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
        } // end-try
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
