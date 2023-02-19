FROM openjdk:17
EXPOSE 8080
ADD target/money-manager-container.jar money-manager-container.jar
ENTRYPOINT ["java","-jar","/money-manager-container.jar"]