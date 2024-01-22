package com.cobra.forensics.audit;

public class StartupAuditor extends GenericWindowsAuditor {
    public static final String TYPE = "STARTUP";
    private static final String KEY = "Name";
    private static final String CMD = "Get-CimInstance Win32_StartupCommand | Select-Object Name,Command,Location,User";

    public StartupAuditor() {
        super(TYPE, KEY, CMD);
    }

}
