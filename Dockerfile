FROM ypzhuang/openjdk-alpine as builder

WORKDIR /root
COPY . /root
RUN ./gradlew --init-script ./init.gradle build -x test


FROM ypzhuang/openjre-alpine
LABEL maintainer="zhuangyinping@gmail.com"

COPY  --from=builder /root/build/libs/starter-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
