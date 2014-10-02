package jonelo.sugar.util;

public final class GeneralProgram {

    public final static void requiresMinimumJavaVersion(final String version) {
        try {
            String ver = System.getProperty("java.vm.version");
            
            if (isJ2SEcompatible() && (ver.compareTo(version) < 0)) {
                System.out.println("ERROR: a newer Java VM is required."
                        + "\nVendor of your Java VM:        " + System.getProperty("java.vm.vendor")
                        + "\nVersion of your Java VM:       " + ver
                        + "\nRequired minimum J2SE version: " + version);

                
                
                System.exit(1);
            }
        } catch (Throwable t) {
            System.out.println("uncaught exception: " + t);
            t.printStackTrace();
        }
    }

    public static boolean isSupportFor(String version) {
        return isJ2SEcompatible()
                ? (System.getProperty("java.version").compareTo(version) >= 0)
                : false;
    }

    public static boolean isJ2SEcompatible() {
        String vendor = System.getProperty("java.vm.vendor");
        if ( 
                vendor.startsWith("Free Software Foundation")
                || 
                vendor.startsWith("Kaffe.org")) {
            return false;
        }
        return true;
    }

}
