FROM openjdk:17
RUN mkdir /code
WORKDIR /code
COPY target/money-manager-container.jar /code
ENTRYPOINT ["java","-jar","money-manager-container.jar"]