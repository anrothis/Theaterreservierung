# Theaterreservierung
Simple JavaFX and Spring Boot application, to coordinate the reservations for cultural association members

## Introduction

Ein Tool zur Unterstützung bei der Verwaltung der Mitglieder eines Kulturvereins.
Der Fokus liegt auf der Organisation der Theaterveranstltungen. 
Die Applikation soll dabei helfen schnell und effizient Reservierungsanfragen der Mitglieder zu erfassen und die Daten
zur weiteren Verarbeitung in unterschiedlichen Formaten bereit zu sellen.


## Index

### Technologie Stack

Zum Aufbau der Benutzeroberfläche wird JavaFX in Kombination von *.fxml* Datein verwendet. Als Persistenzlayer kommt eine
H2 Datenbankstuktur zum Einstatz. Das von Spring Boot bereitgestellt JPARepository fungiert dabei als Data Handler.

### Getting Started

### Clone
  ```java
  git clone https://github.com/anrothis/Theaterreservierung

  cd .\Theaterreservierung\
  ```
  
### Running
  ```
  mvn javafx:run
  ```


