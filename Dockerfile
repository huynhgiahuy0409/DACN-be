FROM maven:3.9.4

WORKDIR /
COPY . .
RUN mvn clean install
CMD mvn spring-boot:run