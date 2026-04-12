# Equipo05 - SportPulseMS

# ⚽ SportPulse — Plataforma de Microservicios de Fútbol (Equipo05)

[![Java](https://img.shields.io/badge/Java-17-red?logo=java)](https://www.oracle.com/java/)  
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?logo=springboot)](https://spring.io/projects/spring-boot)  
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql)](https://www.postgresql.org/)  
[![Swagger](https://img.shields.io/badge/API-Docs-yellow?logo=swagger)]()  
[![Docker](https://img.shields.io/badge/Docker-Compose-informational?logo=docker)](https://www.docker.com/)

---

## 📌 Descripción
Plataforma backend basada en **arquitectura de microservicios** para el análisis de fútbol en tiempo real.  
Consume datos de una API externa y los distribuye mediante servicios independientes.

## 🧩 Microservicios

- ms-gateway → API Gateway (enrutamiento y rate limiting)
- ms-auth → Autenticación y generación de JWT
- ms-leagues → Información de ligas
- ms-teams → Información de equipos
- ms-fixtures → Partidos y resultados
- ms-standings → Clasificaciones
- ms-notifications → Suscripciones y alertas
- ms-dashboard → Resumen agregado del sistema

## ⚙️ Tecnologías

- Java 17
- Spring Boot
- Spring Security + JWT
- PostgreSQL
- Docker + Docker Compose

## 🤝 Colaboradores

- rodrigonewgm
- José_29
- Damian Villalba

---
