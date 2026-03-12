$url = "https://repo1.maven.org/maven2/com/formdev/flatlaf/3.2/flatlaf-3.2.jar"
$outPath = "lib/flatlaf-3.2.jar"

if (!(Test-Path "lib")) {
    New-Item -ItemType Directory -Path "lib"
}

Write-Host "Downloading FlatLaf..."
Invoke-WebRequest -Uri $url -OutFile $outPath
Write-Host "XONG! Thu vien da san sang."
