# Handle arguments


#if ($($args.Count) gt 1)
#{
#    $Command = $arg[0]
#    Write-Host "Operation is $Command"
#}

$BaseInventoryDir = "data/inventory"
#java arguments
$MAIN_CLASS="com.cobra.forensics.app.Main"
$CLASSPATH="forensics-1.0.jar"
$VMARGS="-Djava.util.logging.config.file=logging.forensics.properties"

$JAVA_HOME_DIR=$env:JAVA_HOME

function displayUsage()
{
    Write-Host "forensics -inv <source dir>"
    Write-Host "forensics -comp <dir1> <dir2>"
    Write-Host "forensics -reg"

}

function exportRegistry(){
    param (
        [parameter(Mandatory = $true, HelpMessage = "Missing base output dir.")][string]$OutputDir
    )
    $FullPath = "$BaseInventoryDir/$OutputDir"
    New-Item -Path . -Name $FullPath -ItemType "directory" -Force
    Write-Host "Exporting registry entries to $FullPath  ..."

    exportHive "Registry::HKEY_CURRENT_USER" $FullPath "HKCU"
    exportHive "Registry::HKEY_USERS" $FullPath "HKU"
    exportHive "Registry::HKEY_CLASSES_ROOT" $FullPath "HKCR"
    exportHive "Registry::HKEY_CURRENT_CONFIG" $FullPath "HKCC"

    exportHive "HKLM:HARDWARE" $FullPath "HKLMHW"
    exportHive "HKLM:SYSTEM" $FullPath "HKLMSYS"
    exportHive "HKLM:SAM" $FullPath "HKCSAM"
    exportHive "HKLM:SECURITY" $FullPath "HKLMSEC"
    exportHive "HKLM:SOFTWARE" $FullPath "HKLMSW"

    Write-Host "Done exporting registry entries"
}

function exportHive (){
    param (
        [parameter(Mandatory = $true, HelpMessage = "Enter the path to start from, for example HKLM:SOFTWARE\Microsoft\Policies")][string]$Path,
        [parameter(Mandatory = $true)][string]$OutputBaseDir,
        [parameter(Mandatory = $true)][string]$Type
    )

    $OutputDir = "$OutputBaseDir/$Type"
    New-Item -Path $OutputBaseDir -Name $Type -ItemType "directory"

    $OutputFile="$OutputDir/output.csv"
    $ErrorFile="$OutputDir/error.txt"

    Write-Host "Exporting registry for $Path to $OutputFile"
    $keys = Get-ChildItem -Path $Path -Recurse -ErrorAction SilentlyContinue
    Write-Host "Retrieved all keys"

    foreach ($key in $keys) {
        if ($key -like "*Classes*"){
            Write-Output "Skipping $key" >> $ErrorFile
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

function buildInventory(){
    param (
        [parameter(Mandatory = $true, HelpMessage = "Enter the source directory.")][string]$SourceDir
    )

    Write-Host "Creating inventories..."
    if($null -ne  $JAVA_HOME_DIR)
    {
        $JAVA="$JAVA_HOME_DIR\bin\java"
        $JAVA_CMD =  "$JAVA $VMARGS -cp $CLASSPATH $MAIN_CLASS inv scripts"
        Write-Host $JAVA_CMD
     #   $JAVA_CMD
        Start-Process -FilePath $JAVA -ArgumentList "$VMARGS -cp $CLASSPATH $MAIN_CLASS inv scripts"
    }

    Write-Host "Done Creating inventories"
}

function compareDirs(){
    param (
        [parameter(Mandatory = $true, HelpMessage = "Enter the first directory.")][string]$FirstDir,
        [parameter(Mandatory = $true, HelpMessage = "Enter the second directory.")][string]$SecondDir
    )

    Write-Host "Comparing dirctories $FirstDir and $SecondDir..."

    Write-Host "Done omparing dirctories"
}


$Count = $args.Count
Write-Host $Count




if ( $Count -ge 1 )
{
    $BaseDirName = Get-Date -Format "yyyy_MMM_dd_hhmmttss"
    $Command = $args[0]
    if ($Command -eq "-reg")
    {
        exportRegistry $BaseDirName
    }
    elseif ($Command -eq "-inv")
    {
        if ($Count -eq 2)
        {
            buildInventory $args[1]
        }
        else {
            Write-Host "Invalid number of arguments for inventory command"
            displayUsage
        }
    }
    elseif ($Command -eq "-comp")
    {
        if ($Count -eq 3)
        {
            compareDirs $args[1] $args[2]
        }
        else {
            Write-Host "Invalid number of arguments for inventory command"
            displayUsage
        }
    }
    else
    {
        Write-Host "Invalid command $Command."
        displayUsage
    }

}
else
{
    displayUsage
}

