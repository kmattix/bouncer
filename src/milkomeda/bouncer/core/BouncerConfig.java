package milkomeda.bouncer.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class BouncerConfig {
    private File cfgFile;
    private String cmdPrefix, autoRoleID;
    private boolean autoRoleEnabled;

    public BouncerConfig(File cfgFile, String cmdPrefix, String autoRoleID, boolean autoRoleEnabled) {
        this.cfgFile = cfgFile;
        this.cmdPrefix = cmdPrefix;
        this.autoRoleID = autoRoleID;
        this.autoRoleEnabled = autoRoleEnabled;
    }

    public BouncerConfig(File cfgFile) throws IOException {
        this.cfgFile = cfgFile;
        this.cmdPrefix = "!";
        this.autoRoleID = null;
        this.autoRoleEnabled = false;
        writeConfig();
    }

    public void writeConfig() throws IOException {
        FileWriter cfgFW = new FileWriter(cfgFile);
        cfgFW.write("cmdPrefix= " + cmdPrefix +
                "\nautoRoleID= " + autoRoleID +
                "\nautoRoleEnabled= " + autoRoleEnabled);
        cfgFW.close();
    }

    public static BouncerConfig readConfig(File cfgFile) throws FileNotFoundException {
        String cfgCmdPrefix = "";
        String autoRoleID = "";
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
            autoRoleID = lineScan.next();
            lineScan = new Scanner(fileScan.nextLine());
            lineScan.next();
            cfgAutoRoleEnabled = lineScan.nextBoolean();
            lineScan.close();
        }
        fileScan.close();
        return new BouncerConfig(cfgFile, cfgCmdPrefix, autoRoleID, cfgAutoRoleEnabled);
    }

    public String getAutoRoleID(){
        return autoRoleID;
    }

    /**
     * @param autoRoleID no case sensitive string for the role, {@code null} if removing auto roll
     */
    public void setAutoRoleID(String autoRoleID){
        this.autoRoleID = autoRoleID;
        this.autoRoleEnabled = autoRoleID != null;
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
}
