package sunset.gui.util;

import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.util.prefs.BackingStoreException;

public class WinRegistry {

    /* Windows hives */
    public static final int HKEY_CURRENT_USER = 0x80000001;
    public static final int HKEY_LOCAL_MACHINE = 0x80000002;

    public static String getRegKey(int hive, String keyName, String valueName) throws BackingStoreException {
        if (SystemUtils.IS_OS_WINDOWS) {

            String hiveName;
            if (hive == HKEY_CURRENT_USER)
                hiveName = "HKCU";
            else if (hive == HKEY_LOCAL_MACHINE)
                hiveName = "HKLM";
            else
                return null;

            String path = hiveName + ":\\" + keyName;

            return getRegKey(path, valueName);
        } else {
            return null;
        }
    }

    public static String getRegKey(String path, String valueName) throws BackingStoreException {
        String query = buildRegistryQuery(path, valueName);

        try {
            Process process = Runtime.getRuntime().exec(query);
            process.waitFor();

            return new String(process.getInputStream().readAllBytes());

        } catch (IOException | InterruptedException e) {
            throw new BackingStoreException(e);
        }
    }

    public static String buildRegistryQuery(String path, String valueName) {
        // sanitize strings; single quotes in PowerShell don't allow escaping,
        // so only replace single quotes (escape sequence is "''", two single quotes)
        path = path.replace("\'", "\'\'");
        valueName = valueName.replace("\'", "\'\'");
        /*
         * Powershell code:
         *
         * # only request item if path is valid
         * If (Test-Path '<path>') {
         *      # fetch RegistryKey object associated with path
         *      # then get value (returns empty string if value is not set)
         *      (Get-Item '<path>').GetValue('<valueName>')
         * }
         */
        return "PowerShell -Command {If(Test-Path \'" + path + "\'){(Get-Item \'" + path + "\').GetValue(\'" + valueName + "\')}}";
    }
}
