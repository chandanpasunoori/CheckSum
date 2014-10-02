/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benchmark;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import jonelo.sugar.util.ExitException;
import org.apache.commons.lang3.time.StopWatch;

/**
 *
 * @author Chandan
 */
public class BenchMark {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            StopWatch watch = new StopWatch();

            String filesDir = "Files";
            File dir = new File(filesDir);
            String[] parameters;

            //crc8
            System.out.println("###Checking with CRC8 Algoritm");
            watch.start();
            new jonelo.jacksum.cli.Jacksum(new String[]{"-a", "crc8", dir.getAbsolutePath()});
            System.out.println("Checking with CRC8 Algoritm has completed in " + watch.getNanoTime() + " nano seconds");
            watch.reset();
            System.out.println("====================================================================");
            System.out.println();
            //crc16
            System.out.println("###Checking with CRC16 Algoritm");
            watch.start();
            new jonelo.jacksum.cli.Jacksum(new String[]{"-a", "crc16", dir.getAbsolutePath()});
            System.out.println("Checking with CRC16 Algoritm has completed in " + watch.getNanoTime() + " nano seconds");
            watch.reset();
            System.out.println("====================================================================");
            System.out.println();
            //crc32
            System.out.println("###Checking with CRC32 Algoritm");
            watch.start();
            new jonelo.jacksum.cli.Jacksum(new String[]{"-a", "crc32", dir.getAbsolutePath()});
            System.out.println("Checking with CRC32 Algoritm has completed in " + watch.getNanoTime() + " nano seconds");
            watch.reset();
            System.out.println("====================================================================");
            System.out.println();

            //System.out.println(watch.getNanoTime());
        } catch (ExitException ex) {
            Logger.getLogger(BenchMark.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
