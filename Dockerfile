FROM gradle:6.0.1-jdk8 as builder

WORKDIR /home/gradle/
COPY . /home/gradle/
RUN gradle --init-script ./init.gradle build -x test

FROM ypzhuang/openjre-alpine
LABEL maintainer="zhuangyinping@gmail.com"


COPY  --from=builder /home/gradle/build/libs/*.jar /app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
