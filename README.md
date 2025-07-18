# Rowing Coaching Backend

This is the backend component of a full-stack rowing coaching platform designed to support team-based workout management and live performance tracking for Concept2 ergometer users.

## Project Overview

The Rowing Coaching application aims to connect athletes and coaches through real-time feedback, centralized logbook tracking, and roster management. The backend handles core services such as:

- Coach and athlete user management
- Team creation and joining via unique codes
- Secure authentication (JWT-based)
- PostgreSQL data persistence
- Workout tracking and historical access
- Future support for Concept2 Logbook API integration

## Tech Stack

- **Language:** Java
- **Framework:** Spring Boot
- **Database:** PostgreSQL
- **Security:** Spring Security with JWT
- **Build Tool:** Maven

## Modules In Progress

- User authentication and role-based authorization
- Team management (creation, joining, listing)
- Workout log ingestion and access
- RESTful API integration for mobile frontend

## Project Status

This backend is under active development. Core services like authentication and basic team management are functional, and the architecture is designed to scale with future integrations such as live BLE data ingestion.

---

