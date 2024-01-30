# Launch powershell as adming then run the following command
# Set-ExecutionPolicy RemoteSigned
# to get authorization for running powershell scripts, then open a new powershell window.
Write-Host "Hello World!"

# Some useful commands
#Get-Help * shows list of available Powershell cmdlets
#Get-Help * | more
#help Set*
#help Get-Net*

#### grep ###
#help Select*
#Select-String similar to grep
#alias is sls
#use ^c to search for a string at the start of a line
#.e.g cat file.txt | sls ^d

### services ###
#help *service* for service
#Get-Service with alias gsv
show running services
#gsv | sls Running returns nothing becuase Get-Service returns objects not text
#use Get-Member (alias gm)
#gsv | gm - list all Get-Service.Methods() and Get-Service.Properties available for Get-Service
#can use Where-Object use with property to filter out objects for that property
#alias is where
#gsv | where Status -eq Running
#or 
#Get-Service | Where-Object Status -eq Running

Write-Host "List all services"
gsv

### Show running processes and export to csv
Get-Process | Export-Csv procs.csv

State,LocalPort,RemotePort
,OwningProcess,RemoteAddress
ProcessName


,
AppliedSetting

Get-CimInstance Win32_StartupCommand
 @Test
    public void executeGetTCP() throws CobraException {
        ForensicDataProcessor processor = new ForensicDataProcessor("Name",
                "Get-CimInstance Win32_StartupCommand | Select-Object Name,Command,Location,User");
        processor.execute();
    }
