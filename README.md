
## Prerequisite
* Oracle JDK 1.8+
* Gradle 4.0+
* MySQL 5.7
* docker


```
./gradlew bootRun --args='--spring.profiles.active=dev'
```

```
./gradlew build --info
docker run --name basic_s -e "SPRING_PROFILES_ACTIVE=dev" -e "spring_datasource_url=jdbc:mysql://10.1.70.99:3306/yhj?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false" -p 9080:8080  --rm  basic


docker run --name fjsj -e "SPRING_PROFILES_ACTIVE=dev" -e "spring_datasource_url=jdbc:mysql://172.16.177.88:3306/fjsj?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false" -e "spring_datasource_password=Mg6WZkHHdGDLpH0QwKS" -d  -p 9999:8080 registry.cn-shanghai.aliyuncs.com/fangju/fjsj_service



```
```
CREATE DATABASE fjsj CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

```

Run
```
docker run --name mysql -v `pwd`:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=12345678 -e MYSQL_DATABASE=yhj -d -p 3306:3306 mysql:5.7
./gradlew bootRun
```

## API Document
```
http://127.0.0.1:8080/swagger-ui.html
```


## Test
```
./gradlew test && ./gradlew integrationTest

open build/reports/tests/test/index.html build/reports/tests/integrationTest/index.html
```
### Unit Test 
```
./gradlew test
open build/reports/tests/test/index.html
```
###Integeration Test
```
./gradlew integrationTest
open build/reports/tests/integrationTest/index.html
```