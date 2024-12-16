# Usamos una imagen base de Java 19
FROM openjdk:19-jdk-slim

# Seteamos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo zip generado con sbt dist
# Asegúrate de reemplazar <app>.zip por el nombre de tu archivo
COPY target/universal/play-scala-seed-1.0-SNAPSHOT.zip /app/

# Descomprimimos el archivo de distribución
RUN apt-get update && apt-get install -y unzip && \
    unzip play-scala-seed-1.0-SNAPSHOT.zip && \
    mv play-scala-seed-1.0-SNAPSHOT/* . && \
    rm -rf play-scala-seed-1.0-SNAPSHOT.zip play-scala-seed-1.0-SNAPSHOT

# Exponemos el puerto en el que corre la app de Play Framework (por defecto 9000)
EXPOSE 9000

# Definimos el comando de inicio de la app
CMD ["bin/play-scala-seed", "-Dplay.http.secret.key=wOZy52FIaz2nWf2sguS5Nkw7KXmwDmbY0HACyHYz0wFWCW26odgAoBHMyqaveFzC", "-Dhttp.port=9000"]
