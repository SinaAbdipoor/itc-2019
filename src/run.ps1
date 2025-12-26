$inputDirectory = "C:\Users\Administrator\Desktop\Sina\ITC2019\Dataset\Original\All\Half1"
$files = Get-ChildItem $inputDirectory
$maxSeconds = 43200

Write-Output "Processing files in $inputDirectory ..."

foreach ($file in $files) {
    Write-Output "Running instance: $($file.Name)"
    java Main "$($inputDirectory)\$($file.Name)" $maxSeconds
}
