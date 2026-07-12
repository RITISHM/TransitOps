$tempZip = Join-Path $env:TEMP "apache-maven.zip"
$destDir = "c:\Users\divya_y6vjlfl\OneDrive\Desktop\TransitOps\server\maven-temp"
Write-Host "Downloading Maven to $tempZip ..."
Invoke-WebRequest -Uri "https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip" -OutFile $tempZip
Write-Host "Extracting Maven to $destDir ..."
Expand-Archive -Path $tempZip -DestinationPath $destDir -Force
Write-Host "Maven set up successfully."
