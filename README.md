
## Prerequisite
* Oracle JDK 1.8+
* MySQL 5.7
* docker && docker-compose


## Start basic service as DB , Redis 

```
docker-compose -f db/docker-compose.yml up -d
```

## Start Starter Service
* start with default  dev profile
```
./gradlew bootRun
```

* or start with prod profile
```
./gradlew bootRun --args='--spring.profiles.active=prod'
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

## API Document
```
http://127.0.0.1:8080/swagger-ui.html
```


## Docker Image Build
### Build Docker mage:
```
docker build -t service-starter .

```

### Start Docker Container of the Image
```
 docker run  --name starter \
 -e "SPRING_PROFILES_ACTIVE=dev" \
 -e "spring_datasource_url=jdbc:mysql://IP:3306/starter?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false" \
 -p 9080:8080  --rm  service-starter
  
 *** change IP to your own IP ***
 
open http://localhost:9080

```





