FROM sbtscala/scala-sbt:eclipse-temurin-17.0.4_1.7.1_3.2.0

RUN apt-get update && apt-get install -y libxrender1 libxtst6 libxi6 x11-apps

WORKDIR /app
ADD . /app

ENV DISPLAY=:0
ENV HEADLESS=true

ENTRYPOINT ["sbt", "run"]
