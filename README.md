# Java  SpringBoot ZendeskUploader Tool

## Build and run on localhost
maven build with goals : `clean package`: generates target/zduploader-0.0.1-SNAPSHOT.war

Right click on ZDUploaderApplication.java in eclipse - Run as Java Application

## How to execute
- `java -jar zduploader-0.0.1-SNAPSHOT.war C:\Users\IBM_ADMIN\Documents\temp\zdUploaderConfiguration.json` 

## Additional Information
### How to execute the REST API
- `http://localhost:12001/upload?configFile=C:\Users\IBM_ADMIN\Documents\temp\zdUploaderConfiguration.json` 

### Swagger Integration

Run `http://localhost:12001/swagger-ui.html#!/zd-uploader-application/uploadToZendeskUsingGET` 

Swagger.json file: `http://localhost:12001/v2/api-docs?group=springboot`

**Steps to integrate swagger with springboot:** http://stackoverflow.com/a/35907962/5076414
 
Reference document for swagger integration: http://heidloff.net/article/usage-of-swagger-2-0-in-spring-boot-applications-to-document-apis/
