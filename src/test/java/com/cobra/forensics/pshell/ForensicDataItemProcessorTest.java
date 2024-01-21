package com.cobra.forensics.pshell;

import com.cobra.forensics.util.CobraException;
import org.junit.Test;

public class ForensicDataItemProcessorTest {

    @Test
    public void create() throws CobraException {
        ForensicDataProcessor processor = new ForensicDataProcessor("test","Name","Get-Process");
    }

    @Test
    public void executeGetService() throws CobraException {
        ForensicDataProcessor processor = new ForensicDataProcessor("service","Name",
                "Get-Service | Select-Object Name,DisplayName,Status,ServiceType,StartType");
        processor.execute();

    }

    @Test
    public void executeGetProcess() throws CobraException {
        ForensicDataProcessor processor = new ForensicDataProcessor("process","Name",
                "Get-Process | Select-Object Name,Path,FileVersion,ProductVersion,Company,Product");
        processor.execute();
    }


    @Test
    public void executeGetTCP() throws CobraException {
        ForensicDataProcessor processor = new ForensicDataProcessor("tcp","Name",
                "Get-NetTCPConnection | Select-Object State,LocalPort,RemotePort,OwningProcess,RemoteAddress");
        processor.execute();
    }

    @Test
    public void executeGetStartup() throws CobraException {
        ForensicDataProcessor processor = new ForensicDataProcessor("startup","Name",
                "Get-CimInstance Win32_StartupCommand | Select-Object Name,Command,Location,User");
        processor.execute();
    }
}