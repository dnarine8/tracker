package com.cobra.forensics.audit;

public class ServiceAuditor extends GenericWindowsAuditor {
    public static final String TYPE = "SERVICE";
    private static final String KEY = "Name";
    private static final String CMD = "Get-Service | Select-Object Name,DisplayName,Status,ServiceType,StartType";

    public ServiceAuditor() {
        super(TYPE, KEY, CMD);
    }
    @Override
    public boolean supportInventory(){
        return true;
    }
}

