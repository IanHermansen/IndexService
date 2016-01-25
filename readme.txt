los archivos a leer se cambian en el archivo de configuracion "config.txt"

para crear la base de datos de paginas se ejecuta el archivo "MainDB.java", y para crear el indice se ejecuta "MianIndice.java"

1.-Ejecucion Global:

-Debe ponerse en funcionamiento el servidor de mongo.
-Luego la base de datos se levanta ejecutando los archivos "MainDB.java" y "MainIndice.java" en el IndexService. Dentro de este proyecto hay un archivo de configuracion que permite cambiar el nombre de el archivo xml, por defecto hay un archivo xml pequeño llamado "ar.xml", ademas aqui se puede cambiar el nombre del archivo que tendran las stopwords.

-Despues en el Cache se ejecuta el archivo "Servidor.java", donde habra un archivo config para cambiar el tamaño del cache

-Luego de ejecuta el archivo "MainServidor.java", que esta en el Index

-Por ultimo se ejecuta el proyecto del front. Se abrira el navegador y se podra realizar una busqueda.