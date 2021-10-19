# TRUCU
## Tips
### Generar archivo ejecutable .jar
1. Ejecutar el siguiente comando en la terminal desde la raiz del proyecto
```
mvn clean package
```
2. El archivo `.jar` se generará en la carpeta `TrUCU/target` con el  formato ```{artifactId}-{proyect.version}.jar```
 
 Ejemplo:
 ```TrUCU-1.0.0.jar```
 
 3. Para correr el archivo `.jar` ejecutar el siguiente comando en consola:

``` bash
java -jar TrUCU-1.0.0.jar
```




#### Informacion Tecnica:
- Java 11.0.8
- Maven 3.8.3
- NetBeans
- Springboot 2.5.5
- SQL Server
