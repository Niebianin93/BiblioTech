BiblioTech

Prosta aplikacja do zarzadzania biblioteka.

Technologie
- Java 17
- Maven
- MySQL

Jak uruchomic
1. Upewnij sie ze masz wlaczonego MySQL
2. W pliku src/main/resources/application.properties zmien haslo do bazy danych:
   spring.datasource.password=twoje_haslo_mysql
3. W terminalu w folderze projektu wpisz:
   mvn spring-boot:run

4. Link do aplikacji - http://localhost:8080

UWAGA

Jak nie skonfigurujesz emaila w application.properties, to emaila nie beda dzialac, ale aplikacja uruchomi sie bez problemu
