# FROM  mcr.microsoft.com/openjdk/jdk:21-ubuntu AS build
# COPy..

FROM openjdk:21-slim
COPY target/*.jar app.jar
ENTRYPOINT [ "java","-jar","/app.jar" ]

