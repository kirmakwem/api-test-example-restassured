FROM maven:3.6.3-ibmjava-8

WORKDIR /tests

COPY . .

CMD mvn clean test