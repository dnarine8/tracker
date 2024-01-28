# Launch powershell as admin then run the following command
# Set-ExecutionPolicy RemoteSigned
# to get authorization for running powershell scripts, then open a new powershell window.

$path = $args[0]
$output = $args[1]


function exportRegistry (){
   param (
        [parameter(Mandatory = $true, HelpMessage = "Enter the path to start from, for example HKLM:SOFTWARE\Microsoft\Policies")][string]$Path,
        [parameter(Mandatory = $true)][string]$OutputFile,
        [parameter(Mandatory = $true)][string]$ErrorFile
    )

   Write-Host "Exporting registry for $Path"
   $keys = Get-ChildItem -Path $Path -Recurse -ErrorAction SilentlyContinue
   Write-Host "Retrieved all keys"

    foreach ($key in $keys) {
        if ($key -like "*Classes*"){
            Write-Output "Skipping $key" >> error.txt
        }   
        else {
            foreach ($property in $key) {  
                foreach ($name in $key.Property) {
                    try {   
                         $Value = Get-ItemPropertyValue -Path $key.PSPath -Name $name
                         Write-Output "$key\$name, $Value" >> $OutputFile
                    }
                    catch {
                        Write-Output ("Error processing $key $name") >> $ErrorFile 
                    }
                }
            }
        }
    }
    Write-Host "Done exporting registry entries for $Path"
}


$timestamp = Get-Date -Format "yyyy_MMM_dd_hhmmttss"
Write-Host $time
exportRegistry "Registry::HKEY_CURRENT_USER" "user.csv" "user_error.txt"
exportRegistry "Registry::HKEY_USERS" "users.csv" "users_error.txt"

exportRegistry "Registry::HKEY_CLASSES_ROOT" "root.csv" "root_error.txt"
exportRegistry "Registry::HKEY_CURRENT_CONFIG" "config.csv" "config_error.txt"

exportRegistry "HKLM:HARDWARE" "hardware.csv" "hardware_error.txt"
exportRegistry "HKLM:SYSTEM" "system.csv" "system_error.txt"
exportRegistry "HKLM:SAM" "sam.csv" "sam_error.txt"
exportRegistry "HKLM:SECURITY" "security.csv" "security_error.txt"
exportRegistry "HKLM:SOFTWARE" "software.csv" "software_error.txt"

$timestamp = Get-Date -Format "yyyy_MMM_dd_hhmmttss"
Write-Host $time
