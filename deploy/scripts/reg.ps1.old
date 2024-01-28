# Launch powershell as adming then run the following command
# Set-ExecutionPolicy RemoteSigned
# to get authorization for running powershell scripts, then open a new powershell window.

$path = $args[0]
$output = $args[1]

function hello(){
Write-Host "Hello World!"
Write-Host "path = $path"
Write-Host "output = $outFile"

}

function exportRegistry {
    param (
        [parameter(Mandatory = $true, HelpMessage = "Enter the path to start from, for example HKLM:SOFTWARE\Microsoft\Policies")][string]$Path,
        [parameter(Mandatory = $true)][string]$Outfile
    )

    Write-Host "Hello World!  $Path $Outfile"

    #Test if $Path is accessible
#    if (Test-Path $path -ErrorAction stop) {
#        Write-Host ("Path {0} is valid, continuing..." -f $Path) -ForegroundColor Green
#    }
#    else {
#        Write-Warning ("Could not access path {0}, check syntax and permissions. Exiting..." -f $path)
#        return
#    }

        #Check file extension, if it's not .csv or .xlsx exit
    if (-not ($Outfile.EndsWith('.csv') -or $Outfile.EndsWith('.xlsx'))) {
        Write-Warning ("The specified {0} output file should use the .csv or .xlsx extension, exiting..." -f $Outfile)
        return
    }


    #Set $keys variable
    Write-Host ("Retrieving keys from {0}" -f $Path) -ForegroundColor Green
#    $keys = Get-ChildItem -Path $path -Recurse -ErrorAction SilentlyContinue
    $keys = Get-ChildItem -Path "HKCU:\" -Recurse -ErrorAction SilentlyContinue

    Write-Host $keys

    $total = foreach ($key in $keys) {
        foreach ($property in $key) {
            Write-Host ("Processing {0}" -f $property) -ForegroundColor Green
            foreach ($name in $key.Property) {
                try {   
                     [PSCustomObject]@{
                        Name     = $property.Name + '\'+ $($name)
                       # Property = "$($name)"
                        Value    = Get-ItemPropertyValue -Path $key.PSPath -Name $name
                        Type     = $key.GetValueKind($name)
                    }
                     #Write-Host $object
                
                }
                catch {
                    Write-Warning ("Error processing {0} in {1}" -f $property, $key.name)
                }
          }
        }
    }

    #write to file
     try {
            New-Item -Path $Outfile -ItemType File -Force:$true -Confirm:$false -ErrorAction Stop | Out-Null
            $total | Sort-Object Name, Property | Export-Csv -Path $Outfile -Encoding UTF8 -Delimiter ',' -NoTypeInformation
            Write-Host ("`nExported results to {0}" -f $Outfile) -ForegroundColor Green
        }
        catch {
            Write-Warning ("`nCould not export results to {0}, check path and permissions" -f $Outfile)
            return
        }
}

hello
exportRegistry $path $output

#exportRegistry "HKLM:SOFTWARE\Microsoft\Policies" junk.csv 


#exportRegistry "HKLM" hkcm.csv 
#exportRegistry "HKCU" hkcu.csv 
#exportRegistry "Registry::HKEY_USERS" hku.csv 
#exportRegistry "Registry::HKEY_CURRENT_CONFIG" hkcc.csv 
#exportRegistry "Registry::HKEY_CLASSES_ROOT" hkcr.csv 

