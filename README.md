# ⚽ SportPulse — Plataforma de Microservicios de Fútbol

Plataforma de análisis de fútbol en tiempo real, construida con arquitectura de microservicios en Java 17 + Spring Boot 3.2.5, consumiendo datos de [API-Football (RapidAPI)](https://rapidapi.com/api-sports/api/api-football).

## 🤝 Equipo

- [rodrigonewgm](https://github.com/rodrigonewgm)
- [José_29](https://github.com/JoseGabriel391)
-  [DamianVillalba](https://github.com/DamianVillalba)

---

## 📁 Estructura del repositorio

```
sportpulse/
├── ms-gateway/
├── ms-auth/
├── ms-leagues/
├── ms-teams/
├── ms-fixtures/
├── ms-standings/
├── ms-notifications/
├── ms-dashboard/
├── docker-compose.yml
├── .env.example
├── .gitignore
└── README.md
```

---

## 🏗️ Microservicios

| Microservicio       | Puerto | Responsabilidad                              |
|---------------------|--------|----------------------------------------------|
| `ms-gateway`        | 8080   | Punto de entrada, enrutamiento y rate limiting |
| `ms-auth`           | 8081   | Registro, login y emisión de tokens JWT       |
| `ms-leagues`        | 8082   | Ligas, países y temporadas                   |
| `ms-teams`          | 8083   | Equipos, escudos e información general       |
| `ms-fixtures`       | 8085   | Partidos, calendarios y resultados           |
| `ms-standings`      | 8086   | Clasificaciones por liga y temporada         |
| `ms-notifications`  | 8088   | Suscripciones y alertas de eventos           |
| `ms-dashboard`      | 8089   | Resumen ejecutivo agregado                   |

---

## ⚙️ Stack tecnológico

- **Lenguaje:** Java 17
- **Framework:** Spring Boot 3.2.x
- **Seguridad:** Spring Security + JWT (JJWT 0.12.x)
- **Base de datos:** PostgreSQL 15 (ms-auth y ms-notifications)
- **Comunicación inter-servicios:** OpenFeign
- **Documentación:** Swagger UI / OpenAPI 3.0
- **Testing:** JUnit 5 + Mockito
- **Mappers:** MapStruct
- **Contenerización:** Docker + Docker Compose

---

## 🚀 Cómo levantar el proyecto

### Requisitos previos

- Docker y Docker Compose instalados
- Cuenta en [RapidAPI](https://rapidapi.com) con acceso a API-Football

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd sportpulse
```

### 2. Configurar variables de entorno

```bash
cp .env.example .env
# Editar .env y completar RAPIDAPI_KEY y JWT_SECRET
```

### 3. Levantar todos los servicios

```bash
docker compose up --build
```

Para levantar en background:

```bash
docker compose up --build -d
```

### 4. Verificar que todo está corriendo

```bash
docker compose ps
```

El gateway estará disponible en: `http://localhost:8080`

