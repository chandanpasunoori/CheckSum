package jonelo.jacksum.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;
import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;
import jonelo.jacksum.algorithm.Edonkey;
import jonelo.jacksum.algorithm.MD;
import jonelo.jacksum.algorithm.MDgnu;
import jonelo.jacksum.algorithm.None;
import jonelo.jacksum.algorithm.Read;
import jonelo.sugar.util.EncodingException;
import jonelo.sugar.util.ExitException;
import jonelo.sugar.util.GeneralString;

public class CheckFile {

    public final static String COMMENTDEFAULT = JacksumAPI.NAME + ": Comment:";

    private String CSEP = "\t"; 
    private String checkFile = null;
    private MetaInfo metaInfo = null;
    private AbstractChecksum checksum = null;
    private boolean _l = false;
    private Verbose verbose = null;
    private Summary summary = null;
    private long removed = 0;
    private long modified = 0;
    private String workingDir = null;

    public CheckFile(String checkFile) {
        this.checkFile = checkFile;
    }

    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public void setVerbose(Verbose verbose) {
        this.verbose = verbose;
    }

    public Verbose getVerbose() {
        return verbose;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
        summary.setCheck(true);
    }

    public Summary getSummary() {
        return summary;
    }

    public void setList(boolean list) {
        this._l = list;
    }

    public boolean isList() {
        return _l;
    }

    public long getModified() {
        return modified;
    }

    public long getRemoved() {
        return removed;
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public void perform() throws FileNotFoundException, IOException, MetaInfoVersionException, ExitException {
        

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            fis = new FileInputStream(checkFile);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            br.mark(1024);

            String thisLine = null;
            int ignoretokens = 2; 
            String filename = null;

            if ((thisLine = br.readLine()) != null) {
                if (thisLine.startsWith(MetaInfo.METAINFO)
                        && !thisLine.startsWith(metaInfo.getCommentchars())) {

                    
                    metaInfo = new MetaInfo(thisLine);

                } else {
                    if (verbose.getWarnings()) {
                        System.err.println("Jacksum: Warning: file does not contain meta information. Please set suitable command line parameters.");
                    }
                    
                    
                    br.reset();
                }
            } else {
                throw new ExitException("File is empty.\nExit.", ExitStatus.CHECKFILE);
            }

            try {
                checksum = JacksumAPI.getChecksumInstance(metaInfo.getAlgorithm(), metaInfo.isAlternate());
            } catch (NoSuchAlgorithmException nsae) {
                throw new ExitException(nsae.getMessage(), ExitStatus.CHECKFILE);
            }

            if (checksum instanceof MD
                    || checksum instanceof MDgnu
                    || checksum instanceof Edonkey) {
                ignoretokens--; 
            }

            if (checksum instanceof None
                    || checksum instanceof Read) {
                ignoretokens--; 
            }

            if (metaInfo.isSeparatorWanted()) {
                CSEP = metaInfo.getSeparator();
                checksum.setSeparator(CSEP);
            } else { 
                CSEP = checksum.getSeparator();
            }

            
            if (metaInfo.isTimestampFormat()) {
                ignoretokens++;
                checksum.setTimestampFormat(metaInfo.getTimestampFormat());
                
                String[] result = GeneralString.split(metaInfo.getTimestampFormat(), CSEP);
                ignoretokens += result.length - 1;
            } else {
                checksum.setTimestampFormat(null);
            }

            if (metaInfo.isGrouping()) {
                checksum.setGroup(metaInfo.getGrouping());
                checksum.setGroupChar(metaInfo.getGroupChar());
            } else {
                checksum.setGroup(0);
            }

            if (metaInfo.isEncoding()) {
                try {
                    checksum.setEncoding(metaInfo.getEncoding());
                } catch (EncodingException e) {
                    if (verbose.getWarnings()) {
                        System.err.println("Jacksum: Warning: " + e.getMessage());
                    }
                }
            }

            
            
            int skipchecksumlen = 0;
            if ((checksum.getEncoding().length() == 0) || 
                    (checksum.getEncoding().equalsIgnoreCase(AbstractChecksum.DEC)) || 
                    (checksum.getEncoding().equalsIgnoreCase(AbstractChecksum.OCT)) || 
                    (checksum instanceof None || checksum instanceof Read) 
                    ) {
                skipchecksumlen = 0;
            } else {
                skipchecksumlen = checksum.getFormattedValue().length();
                ignoretokens--; 
            }

            
            String folder = "";
            boolean lastLineWasEmptyLine = true;
            while ((thisLine = br.readLine()) != null) {
                if ((!thisLine.startsWith(COMMENTDEFAULT)) && 
                        (!thisLine.startsWith(metaInfo.getCommentchars())) 
                        ) {
                    if (thisLine.length() == 0) { 
                        lastLineWasEmptyLine = true;
                        continue;
                    }
                    if (thisLine.startsWith(JacksumAPI.NAME)) {
                        if (verbose.getWarnings()) {
                            System.err.println(JacksumAPI.NAME + ": Warning: Ignoring unknown directive.");
                        }
                    } else {

                        if (lastLineWasEmptyLine
                                && metaInfo.isRecursive()
                                && !metaInfo.isPathInfo()
                                && thisLine.endsWith(":")) { 
                            lastLineWasEmptyLine = false;
                            folder = thisLine.substring(0, thisLine.length() - 1);

                            if (workingDir != null && workingDir.length() > 0) {
                                folder = workingDir + metaInfo.getFilesep() + folder;
                            }

                            if (File.separatorChar != metaInfo.getFilesep()) {
                                folder = folder.replace(metaInfo.getFilesep(), File.separatorChar);
                            }

                            if (!_l) {
                                System.out.println("\n" + folder + ":");
                            }
                            if (folder.length() > 0) {
                                folder += File.separator;
                            }
                        } else { 
                            try {
                                filename = parseFilename(thisLine, ignoretokens, skipchecksumlen);
                                int skip = filename.length();

                                if (metaInfo.isPathInfo() && workingDir != null && workingDir.length() > 0) {
                                    folder = workingDir + metaInfo.getFilesep();
                                    skip += folder.length();
                                }

                                if (File.separatorChar != metaInfo.getFilesep()) {
                                    filename = filename.replace(metaInfo.getFilesep(), File.separatorChar);
                                }

                                if (_l) {
                                    skipOkFiles(folder + filename, thisLine, skip);
                                } else {
                                    System.out.print(whatChanged(folder + filename, thisLine, skip));
                                    System.out.println(filename);
                                }
                            } catch (NoSuchElementException e) {
                                if (verbose.getWarnings()) {
                                    System.err.println(JacksumAPI.NAME + ": Warning: Invalid entry: " + thisLine);
                                }
                            } catch (IOException ioe) {
                                summary.addErrorFile();
                                String detail = null;
                                if (verbose.getDetails()) {
                                    detail = filename + " [" + ioe.getMessage() + "]";
                                } else {
                                    detail = filename;
                                }
                                System.err.println("Jacksum: Error: " + detail);
                            }
                        }
                    }
                } 

            } 

        } finally {
            
            summary.setRemovedFiles(removed);
            summary.setModifiedFiles(modified);
            
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }

    private void skipOkFiles(String filename, String thisLine, int skip) throws IOException {
        boolean out = false;
        if (!(new File(filename).exists())) {
            removed++;
            out = true;
        } else {
            String output = getChecksumOutput(filename);
            if (!output.regionMatches(0, thisLine, 0, output.length() - skip)) {
                out = true;
                modified++;
            }
        }
        if (out) {
            System.out.println(filename);
        }
        summary.addFile();
    }

    private String whatChanged(String filename, String thisLine, int skip) throws IOException {
        if (!(new File(filename).exists())) {
            removed++;
            summary.addFile();
            return ("[REMOVED] ");
        } else {
            String output = getChecksumOutput(filename);
            if (!output.regionMatches(0, thisLine, 0, output.length() - skip)) {
                modified++;
                summary.addFile();
                return ("[FAILED]  ");
            } else {
                summary.addFile();
                return ("[OK]      ");
            }
        }
    }

    private String parseFilename(String thisLine, int ignoretokens, int skipchecksumlen)
            throws NoSuchElementException {
        
        if (skipchecksumlen > 0) {
            thisLine = thisLine.substring(skipchecksumlen + CSEP.length());
        }

        StringBuffer buf = new StringBuffer();
        String[] result = GeneralString.split(thisLine, CSEP);
        
        buf.append(result[ignoretokens]); 
        for (int i = ignoretokens + 1; i < result.length; i++) {
            buf.append(CSEP);
            buf.append(result[i]);
        }
        return buf.toString();
    }

    private String getChecksumOutput(String filename) throws IOException {
        summary.addBytes(checksum.readFile(filename, true));

        File f = new File(filename);
        if (metaInfo.isRecursive() && !metaInfo.isPathInfo()) {
            checksum.setFilename(f.getName());
        } else {
            checksum.setFilename(filename);
        }
        
        return checksum.toString();
    }

}
