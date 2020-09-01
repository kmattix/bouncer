package milkomeda.bouncer.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class BouncerConfig {
    private String cmdPrefix;
    private boolean autoRoleEnabled;

    public BouncerConfig(String cmdPrefix, boolean autoRoleEnabled) {
        this.cmdPrefix = cmdPrefix;
        this.autoRoleEnabled = autoRoleEnabled;
    }

    public BouncerConfig() throws IOException {
        this.cmdPrefix = "!";
        this.autoRoleEnabled = false;
        writeConfig(new File("bouncer.cfg"));
    }

    public void writeConfig(File cfgFile) throws IOException {
        FileWriter cfgFW = new FileWriter(cfgFile);
        cfgFW.write("cmdPrefix= " + cmdPrefix +
                "\nautoRoleEnabled= " + autoRoleEnabled);
        cfgFW.close();
    }

    public static BouncerConfig readConfig(File cfgFile) throws FileNotFoundException {
        String cfgCmdPrefix = "";
        boolean cfgAutoRoleEnabled = false;
        Scanner fileScan = new Scanner(cfgFile);
        Scanner lineScan;
        while(fileScan.hasNextLine()){
            lineScan = new Scanner(fileScan.nextLine());
            lineScan.useDelimiter(" ");
            lineScan.next();
            cfgCmdPrefix = lineScan.next();
            lineScan = new Scanner(fileScan.nextLine());
            lineScan.next();
            cfgAutoRoleEnabled = lineScan.nextBoolean();
        }
        return new BouncerConfig(cfgCmdPrefix, cfgAutoRoleEnabled);
    }

    public String getCmdPrefix() {
        return cmdPrefix;
    }

    public void setCmdPrefix(String cmdPrefix) {
        this.cmdPrefix = cmdPrefix;
    }

    public boolean isAutoRoleEnabled() {
        return autoRoleEnabled;
    }

    public void setAutoRoleEnabled(boolean autoRoleEnabled) {
        this.autoRoleEnabled = autoRoleEnabled;
    }
}
