FROM ypzhuang/openjdk-alpine as builder

WORKDIR /root
COPY gradle.properties /root/.gradle/
COPY . /root
RUN ./gradlew --init-script ./init.gradle build -x test


FROM ypzhuang/openjre-alpine
LABEL maintainer="yinping.zhuang@hp.com"

COPY  --from=builder /root/build/libs/starter-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
