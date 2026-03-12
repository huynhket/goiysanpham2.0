$urls = @(
    "https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-sync/4.11.1/mongodb-driver-sync-4.11.1.jar",
    "https://repo1.maven.org/maven2/org/mongodb/bson/4.11.1/bson-4.11.1.jar",
    "https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-core/4.11.1/mongodb-driver-core-4.11.1.jar",
    "https://repo1.maven.org/maven2/org/json/json/20231013/json-20231013.jar"
)

if (!(Test-Path "lib")) {
    New-Item -ItemType Directory -Path "lib"
}

foreach ($url in $urls) {
    $fileName = Split-Path $url -Leaf
    $outPath = Join-Path "lib" $fileName
    Write-Host "Downloading $fileName..."
    Invoke-WebRequest -Uri $url -OutFile $outPath
}

Write-Host "XONG! Hãy Refresh lại dự án trong Eclipse."
