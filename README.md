# Event Registration System

## Description

Simple event registration application with admin event creation and public participant registration.

## Quick start

```bash
git clone https://github.com/rogerpurtsak/eventregistrationsystem.git
cd eventregistrationsystem
```

Start the backend:

```powershell
cd backend
.\gradlew.bat bootRun
```

Open a second terminal and start the frontend:

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173` in your browser.

## Technologies

- Java 21
- Spring Boot 3.4.1
- Spring Data JPA
- H2 Database
- React
- Vite

## Requirements

- Java 21
- Node.js 20.19+ (or 22.12+)
- npm

## Project structure

```
event-registration-system/
  backend/    Spring Boot REST API
  frontend/   React frontend
```

## Backend setup

```bash
cd backend
./gradlew bootRun
```

Windows (PowerShell):

```powershell
cd backend
.\gradlew.bat bootRun
```

Backend runs on: `http://localhost:8080`

## Frontend setup

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on: `http://localhost:5173`

## Admin credentials

Configured in `backend/src/main/resources/application.properties`.

Default credentials:

```
email: admin@example.com
password: admin123
```

## H2 database

H2 console: `http://localhost:8080/h2-console`

```
JDBC URL:  jdbc:h2:file:./data/eventdb
Username:  sa
Password:  (empty)
```

## API endpoints

### GET /api/events

Returns all events with registration counts.

No token required.

Response:
```json
[
  {
    "id": 1,
    "title": "Jooksuvõistlus",
    "eventTime": "2026-08-01T10:00:00",
    "maxParticipants": 100,
    "registeredCount": 3,
    "availableSpots": 97
  }
]
```

---

### POST /api/auth/login

Admin login. Returns a token.

No token required.

Request:
```json
{
  "email": "admin@example.com",
  "password": "admin123"
}
```

Response:
```json
{
  "token": "3f2a1b4c-..."
}
```

---

### POST /api/admin/events

Creates a new event. **Requires token.**

Header:
```
Authorization: Bearer <token>
```

Request:
```json
{
  "title": "Jooksuvõistlus",
  "eventTime": "2026-08-01T10:00:00",
  "maxParticipants": 100
}
```

Response:
```json
{
  "id": 1,
  "title": "Jooksuvõistlus",
  "eventTime": "2026-08-01T10:00:00",
  "maxParticipants": 100,
  "registeredCount": 0,
  "availableSpots": 100
}
```

---

### POST /api/events/{eventId}/registrations

Registers a participant for an event.

No token required.

Request:
```json
{
  "firstName": "Jaan",
  "lastName": "Tamm",
  "personalCode": "38001011234"
}
```

Response: `201 Created` (no body)

Errors:
- `404` — event not found
- `409` — event is full or personal code already registered
