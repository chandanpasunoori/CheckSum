package jonelo.sugar.util;

public class ExitException extends Exception {

    private int exitcode;

    public ExitException(String s) {
        super(s);
        exitcode = 0;
    }

    public ExitException(String s, int exitcode) {
        super(s);
        this.exitcode = exitcode;
    }

    public int getExitCode() {
        return exitcode;
    }

}
