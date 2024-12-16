# Usamos una imagen base previamente subida
FROM larrazjp/play-framework-app

# Exponemos el puerto en el que corre la app de Play Framework (por defecto 9000)
EXPOSE 9000

# Comando para iniciar la aplicación, si se necesita alguna configuración adicional
CMD ["bin/play-scala-seed", "-Dplay.http.secret.key=wOZy52FIaz2nWf2sguS5Nkw7KXmwDmbY0HACyHYz0wFWCW26odgAoBHMyqaveFzC", "-Dhttp.port=9000"]
