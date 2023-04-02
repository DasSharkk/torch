$InitPwd = $Pwd

mkdir ~\AppData\Local\torch
Set-Location ~\AppData\Local\torch
git clone https://github.com/mooziii/torch install
Set-Location install
./gradlew installDist
Copy-Item build\install\torch\* .. -Recurse
Set-Location ..
Remove-Item install -Recurse -Force
Remove-Item bin/torch -Force
Set-Location bin

$FinalPath = [Environment]::GetEnvironmentVariable("PATH", "User") + ";" + $Pwd
[Environment]::SetEnvironmentVariable( "Path", $FinalPath, "User" )

Set-Location $InitPwd
$env:Path = [System.Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path","User")