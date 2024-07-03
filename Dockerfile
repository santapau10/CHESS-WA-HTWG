FROM sbtscala/scala-sbt:eclipse-temurin-17.0.4_1.7.1_3.2.0

RUN apt-get update && apt-get install -y libxrender1 libxtst6 libxi6

WORKDIR /app
ADD . /app

ENV JAVA_OPTS="-Djava.awt.headless=true"

ENTRYPOINT sbt run
