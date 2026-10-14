$ErrorActionPreference = 'Stop'
$dest = Join-Path $PSScriptRoot 'lib\mysql-connector-j-8.0.33.jar'
$url = 'https://repo.maven.apache.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar'
Invoke-WebRequest -Uri $url -OutFile $dest
Write-Host "Driver salvo em $dest"
