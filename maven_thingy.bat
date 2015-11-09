call mvn package
java -cp target/PSP-1.0.jar application.Main levels/levels.dat 400
java -cp target/PSP-1.0.jar application.Main levels/levels2.dat 218
java -cp target/PSP-1.0.jar application.Main levels/levels3.dat 203
call mvn clean
pause