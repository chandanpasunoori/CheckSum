package jonelo.jacksum.ui;

public class Verbose {

    private boolean warnings;
    private boolean details;
    private boolean summary;

    public Verbose() {
        reset();
    }

    public void reset() {
        warnings = true;
        details = true;
        summary = false;
    }

    public void setWarnings(boolean warnings) {
        this.warnings = warnings;
    }

    public boolean getWarnings() {
        return warnings;
    }

    public void setDetails(boolean details) {
        this.details = details;
    }

    public boolean getDetails() {
        return details;
    }

    public void setSummary(boolean summary) {
        this.summary = summary;
    }

    public boolean getSummary() {
        return summary;
    }

}
