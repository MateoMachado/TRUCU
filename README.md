# TRUCU
### Arquitectura del sistema 

El sistema TrUCU tiene una arquitectura de cliente-servidor, donde el servidor es emulado con una máquina virtual que posee la base de datos del sistema, y el cliente es un programa encargado de realizar las transacciones efectuadas por los usuarios. 

En resumen, la arquitectura del sistema TrUCU es: 
##### 1. Servidor en máquina virtual:
- Creada con Oracle VM VirtualBox 
- Con sistema operativo Ubuntu 20.04 
- Con SQL Server 2019 

##### 2. Programa Backend: 
- Proyecto Maven 3.8.3 con Java 11 
- Realizado en Apache NetBeans IDE 11.3 
- Utiliza SpringBoot Framework 2.5.6 
- Utiliza Microsoft JDBC Driver v9.2.1 para SQL Server 

##### 3. Programa Frontend web: 
- Utiliza Angular 12.1.4 
- Utiliza TypeScript 4.3.4 
- Realizado con Node.js 14.17.4 
- Pagina hosteada en Google con la plataforma Firebase 

## ¿Como ejecutar la aplicación TrUCU? 
La aplicación esta optimizada para que sea muy fácil de ejecutar, el procedimiento es el siguiente: 
1. Configurar el servidor con la base de datos. 
2. Desde la raíz del proyecto, hacer doble click en el archivo startUCU.sh 

El archivo anterior abre una terminal donde se ejecuta el programa de backend, el cual intentará establecer una conexión con la base de datos. Si la conexión es exitosa, automáticamente se abrirá una pestaña en el navegador web con el programa Frontend. ¡Y listo! La aplicación ya está disponible para utilizar. 

###### Notas 
- La ejecución del programa esta optimizada para que no sea necesario descargar ninguna dependencia extra, ni plugins. A menos que se desee compilar el programa o generar un nuevo archivo ejecutable, lo cual no es necesario. 

- El uso de SpringBoot Framework es puramente para establecer una comunicación entre el backend y el frontend, y utilizar herramientas como la inyección de dependencias. No se utiliza ningún ORM ni modulo que facilite la gestión e interacción con la base de datos. 

- El programa de backend cuenta con un Logger que informa todo las transacciones que esta ejecutando, con el objetivo de entender su funcionamiento y visualizar las consultas a la base ejecutadas. Los logs pueden visualizarse en la terminal, y también en el archivo generado en la raíz del proyecto, llamado log.txt 

## Servidor con base de datos 
A continuación, se explica cómo crear y configurar la base de datos dentro de la máquina virtual Linux: 

Una vez que se tenga la máquina virtual creada con Linux2, seguir los pasos de la siguiente guía en la sección “[Install SQL Server](https://docs.microsoft.com/en-us/sql/linux/quickstart-install-connect-ubuntu?view=sql-server-ver15#install)”: 

Una vez que el servicio de SQL este corriendo en la máquina virtual, se debe fijar la IP hacia la cual nos conectamos dentro de la máquina virtual.  

Para esto, ir a las configuraciones de red dentro del remoto e ir a las configuraciones de la red “Ethernet (enp0s8)”. Luego dentro de IPV4, configurar el método IPV4 como manual y fijar la dirección IP del equipo a 192.168.56.103 con netmask 255.255.255.0 y guardar los cambios. Por último, se debe apagar y prender la conexión para actualizar la IP.  

Finalmente, en las configuraciones de la máquina virtual, dentro de Virtual Box se debe tener un adaptador solo-anfitrión con un modo promiscuo: permitir todo y un adaptador NAT con un reenvío del puerto 1433 usando el protocolo TCP hacia la IP de nuestro equipo.  

Una vez configurado todo lo anterior, conectar hacia la instancia de SQL Server utilizando el SQL Server Management Studio y con las siguientes credenciales:  
- IP: 192.168.56.103  
- Authentication: SQL Server Authentication  
- User: SA  
- Password: (la contraseña elegida al configurar SQL Server)  

Una vez conectado, ejecutar el script de creación de la base y está lista para usarse. 
