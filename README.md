
## Prerequisite
* Oracle JDK 1.8+
* MySQL 5.7
* Apache RocketMQ 4.6.0
* docker && docker-compose


## Start basic service as DB , Redis 

```
docker-compose -f db/docker-compose.yml up -d
```

## Start Apache RocketMQ 

```

cd rocketmq-all-4.6.0-bin-release
nohup sh bin/mqnamesrv &
nohup sh bin/mqbroker -n localhost:9876 &

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

POST /api/v1/auth/login
{
  "password": "12345678",
  "username": "superAdmin"
}
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


### Framework or service used by this starter project
* File Service, [FastDFS Cluster Installation](https://github.com/ypzhuang/fastdfs)
* MyBatis Plus
* Spring Security with JWT for Web Console Starter(Vue based Web Admin Console)
* AppID/AppSecurity for third part applications(apps)
* Swagger for Online APIs groups

### How to create new APIs
1. Add new entity in the db/model/starter.mwb using MySQL Workbench 8.0
1. File/Export/Forward Engineer SQL  Create Script.., select Output SQL Script File to replace src/main/resources/schema.sql
1. run ./gradlew bootRun to  Start Starter Service
1. Run com.hptiger.starter.generator.MysqlGenerator, input the newly created entity name to create mapper/service/controller source java files
1. Modified @RequestMapping in the generated controller source file, /app/v1/* for third part, /api/v1/* for  Web Console Starter
1. (Optional)If you have defined a column with 6 chars length for a dictionary, you can create a enum in com.hptiger.starter.entity.enums package, then
change the property of Entity from String to this Enum class.  The dictionary can be input in the excel file db/model/init_sql.xlsx, then copy the sql
to the file: src/main/resources/init.sql






