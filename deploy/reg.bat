echo off
set DIR=%1
set OUTPUT=data\inventory\%DIR%
set HKLMDIR=%OUTPUT%\HKLM\output.csv
set HKCUDIR=%OUTPUT%\HKCU\output.csv
set HKUDIR=%OUTPUT%\HKU\output.csv
set HKCRDIR=%OUTPUT%\HKCR\output.csv
set HKCCDIR=%OUTPUT%\HKCC\output.csv

echo "Retrieving HKLM registry entries..."
powershell.exe ../scripts/reg.ps1 HKLM %HKLMDIR%
echo "Done"

echo "Retrieving HKCU registry entries..."
powershell.exe ../scripts/reg.ps1 HKCU %HKCUDIR%
echo "Done"

echo "Retrieving HKU registry entries..."
powershell.exe ../scripts/reg.ps1 Registry::HKEY_USERS %HKUDIR%
echo "Done"

echo "Retrieving HKCR registry entries..."
powershell.exe ../scripts/reg.ps1 Registry::HKEY_CLASSES_ROOT %HKCRDIR%
echo "Done"

echo "Retrieving HKCC registry entries..."
powershell.exe ../scripts/reg.ps1 Registry::HKEY_CURRENT_CONFIG %HKCCDIR%
echo "Done"

:EOF
