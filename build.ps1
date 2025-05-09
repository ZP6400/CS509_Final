Write-Output "Cleaning previous builds"
mvn clean

Write-Output "Compiling"
mvn compile

Write-Output "Running Checkstyle"
mvn checkstyle:check

Write-Output "Running SpotBugs"
mvn spotbugs:check

Write-Output "Generating Doxygen documentation"
doxygen Doxyfile

Write-Output "Running Tests"
mvn test

Write-Output "Build complete!"