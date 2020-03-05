FROM openjdk:8-jdk-alpine

EXPOSE 8082

RUN set -ex \
  && apk update \
  && apk upgrade

RUN mkdir /spring-shop-customers

COPY /targets/*.jar /spring-shop-customers/
WORKDIR /spring-shop-customers

CMD ["java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=dev", "accounts.jar"]
