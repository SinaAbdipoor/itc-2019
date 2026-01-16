$inputDirectory = "C:\Users\Administrator\Documents\GitHub\itc-2019\src\Half1"
$files = Get-ChildItem $inputDirectory
$maxSeconds = 14400

Write-Output "Processing files in $inputDirectory ..."

foreach ($file in $files) {
    Write-Output "Running instance: $($file.Name)"
    java Main "$($inputDirectory)\$($file.Name)" $maxSeconds
}
