package com.cobra.forensics.audit;

public class TCPAuditor extends GenericWindowsAuditor {
    public static final String TYPE = "TCP";
    private static final String KEY = "Name";
    private static final String CMD = "Get-NetTCPConnection | Select-Object State,LocalPort,RemotePort,OwningProcess,RemoteAddress";

    public TCPAuditor() {
        super(TYPE, KEY, CMD);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}

