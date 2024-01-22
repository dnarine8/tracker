package com.cobra.forensics.audit;

public class ProcessAuditor extends GenericWindowsAuditor {
    public static final String TYPE = "PROCESS";
    private static final String KEY = "Name";
    private static final String CMD = "Get-Process | Select-Object Name,Path,FileVersion,ProductVersion,Company,Product";

    public ProcessAuditor() {
        super(TYPE, KEY, CMD);
    }

}
