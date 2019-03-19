FROM adoptopenjdk/openjdk11-openj9:jre

ENV APP_HOSTNAME=0.0.0.0
ENV APP_PORT=9000

RUN mkdir /opt/shareclasses
RUN mkdir /opt/app

COPY target/scala-2.12/fake-kms-fat.jar /opt/app

RUN /bin/sh -c 'timeout 5s java -Xshareclasses:cacheDir=/opt/shareclasses -Xscmx20M -jar /opt/app/fake-kms-fat.jar --run_type=short; exit 0'

CMD ["java", "-Xquickstart", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:+IdleTuningGcOnIdle", "-Xtune:virtualized", "-Xshareclasses:cacheDir=/opt/shareclasses", "-jar", "/opt/app/fake-kms-fat.jar"]

EXPOSE $APP_PORT