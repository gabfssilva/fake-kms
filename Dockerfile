FROM adoptopenjdk/openjdk11-openj9:jre

RUN mkdir /opt/shareclasses
RUN mkdir /opt/app

COPY target/scala-2.12/fake-kms-fat.jar /opt/app

RUN /bin/sh -c 'timeout 5s java -Xshareclasses:cacheDir=/opt/shareclasses -Xscmx20M -jar /opt/app/fake-kms-fat.jar --run_type=short; exit 0'

FROM adoptopenjdk/openjdk11-openj9:alpine-jre

ENV APP_HOSTNAME=0.0.0.0
ENV APP_PORT=9000

COPY --from=0 /opt/shareclasses /opt/shareclasses
COPY --from=0 /opt/app /opt/app

CMD ["java", "-Xquickstart", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:+IdleTuningGcOnIdle", "-Xtune:virtualized", "-Xshareclasses:cacheDir=/opt/shareclasses", "-jar", "/opt/app/fake-kms-fat.jar"]

EXPOSE $APP_PORT