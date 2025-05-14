# Fundraising Manager for SII

## 🧾 Overview
The project is a backend REST API developed for the recruitment task by SII Polska. The goal is to create a system for managing collection boxes used during charity fundraising events. The system helps charity organizations track collection boxes, manage their assignments, handle donations in multiple currencies, and generate financial reports per event.

## 🎯 Features
- Register and manage **collection boxes** with unique identifiers
- Assign boxes to **fundraising events** (only if empty)
- Add money to boxes in multiple currencies (right now only: EUR, GBP, PLN, USD)
- Transfer money from boxes to event's accounts with **currency conversion** (based on exchange API: *https://open.er-api.com/v6/latest*)
- Automatically empty collection boxes upon unregistration
- Generate **financial reports** showing the total collected balance per event in its designated currency.

## 🧩 Technical Stack
- **Java 21**
- **Spring Boot**
- **Maven**
- **H2 Database** (for development and testing)

## 🧬 Project Structure - Key Directories
- **`controllers/`**: REST controllers that handle HTTP requests and define API endpoints
- **`models/`**: Holds the entity classes representing the application's data structures
- **`repositories/`**: Includes interfaces for data access, typically extending Spring Data JPA repositories
- **`servicies/`**: Contains service classes that implement the business logic of the application

## 🏗️ Building the Application
1. Clone the repository:
```bash
git clone https://github.com/s28166/fundraisingManagerSII.git
```
2. Navigate to the main catalog of the project
3. Build the project using Maven:
```bash
mvn clean install
```
## 🏃 Running the Application
The project can be run by using Maven or by executing the generated JAR file
### Option 1: Maven
```bash
mvn spring-boot:run
```
### Option 2: JAR file
```bash
java -jar target/fundraisingManagerSII-0.0.1-SNAPSHOT.jar
```
The application will start on `http://localhost:8080/`

## 🔚 REST API Endpoint
The API provides the following endpoints:
### Collections Boxes
- Register a new box
  - URL: `POST /collections/register`
- List all boxes
  - URL: `GET /collections/list`
- Unregister a box
  - URL: `DELERE /collections/{id}/unregister`
- Assign a box to an event
  - URL: `PATCH /collections/{boxId}/assign_to/{eventId}`
- Add money to a box
  - URL: `PATCH /collections/{id}/add`
  - Sample Request Body:
  ```json
  {
    "currency": "one_of_PLN_EUR_USD_GBP",
    "amount": 100
  }
  ```
- Transfer money from a box to an event
  - URL: `/collections/{id}/transfer`

### Fundraising Events
- Create a new event
  - URL: `POST /events/create`
  - Sample Request Body:
  ```json
  {
    "name": "example_name",
    "currency": "one_of_PLN_EUR_USD_GBP"
  }
  ```
- Show events report
  - URL: `GET /events/report`
