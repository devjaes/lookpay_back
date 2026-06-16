# LookPay — Backend (2022)

> Spring Boot service for a facial-recognition payment simulation, exploring password-less biometric authentication via AWS Rekognition.

## Overview

Exploration (2022) of biometric checkout: a user enrolls a face, then at the point of sale the live capture is matched against the enrolled identity using AWS Rekognition, with a sub-2s authentication target end-to-end.

This repo is the backend half of the system. It exposes the REST API consumed by the kiosk frontend, persists users/clients/companies/cards/payments in PostgreSQL, and orchestrates the face-match decision flow.

## Stack

| Layer | Tech |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.1 |
| Persistence | Spring Data JPA + PostgreSQL |
| Face matching | AWS Rekognition |
| Password hashing | Argon2 |
| Build | Maven (`mvnw`) |

## Repo scope

- This repo is the **backend** only.
- The kiosk frontend (Next.js) lives in a separate private repo.

## Local setup

```bash
# 1. Configure DB credentials and AWS Rekognition access
#    in src/main/resources/application.yaml (or via a .env file —
#    spring-dotenv is wired in)

# 2. Run
./mvnw spring-boot:run        # macOS / Linux
mvnw.cmd spring-boot:run      # Windows
```

## Status

- Earlier project (2022). Exploration of password-less biometric auth.
- Kept public as a record of the architecture and AWS Rekognition integration approach.

## Portfolio

[Project entry on devjaes.dev →](https://devjaes.dev/work/lookpay)
