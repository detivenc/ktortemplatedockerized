<p align="center">
  <a href="https://skillicons.dev">
    <img src="https://skillicons.dev/icons?i=git,githubactions,kubernetes,docker,kotlin,postgres&perline=6" />
  </a>
</p>

<h1 align="center">Ktor, Docker, and Kubernetes Template</h1>

<p align="center">
  A ready-to-use, containerized Ktor application template for rapid development and deployment.
  <br/>
  <br/>
  <a href="https://github.com/DeTiveNC/SpringTemplateDockerized/issues">Report Bug</a>
  ·
  <a href="https://github.com/DeTiveNC/SpringTemplateDockerized/issues">Request Feature</a>
</p>


  ![Contributors](https://img.shields.io/github/contributors/DeTiveNC/SpringTemplateDockerized?color=dark-green)
  ![Forks](https://img.shields.io/github/forks/DeTiveNC/SpringTemplateDockerized?style=social)
  ![Stargazers](https://img.shields.io/github/stars/DeTiveNC/SpringTemplateDockerized?style=social)
  ![Issues](https://img.shields.io/github/issues/DeTiveNC/SpringTemplateDockerized)
  ![License](https://img.shields.io/github/license/DeTiveNC/SpringTemplateDockerized)
  [![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/DeTiveNC/SpringTemplateDockerized)

## About The Project

This project provides a solid foundation for building and deploying **Ktor** applications. It is pre-configured with Docker for containerization, PostgreSQL as the database, and Kubernetes for orchestration. The build system uses **Amper** — JetBrains' simpler build tool — instead of Gradle.

### Features

*   **Ktor:** Lightweight, async Kotlin-first web framework by JetBrains.
*   **Amper:** Simple YAML-based build tool, replacing Gradle.
*   **Exposed:** Kotlin SQL library for database access (replacing JPA).
*   **Docker:** Containerize your application for consistent environments and easy scaling.
*   **PostgreSQL:** A powerful, open-source object-relational database system.
*   **Kubernetes:** Pre-configured manifests with resource limits, health probes and security contexts.

### Built With

*   [Ktor](https://ktor.io/)
*   [Kotlin](https://kotlinlang.org/)
*   [Amper](https://github.com/JetBrains/amper)
*   [Exposed](https://github.com/JetBrains/Exposed)
*   [Docker](https://www.docker.com/)
*   [PostgreSQL](https://www.postgresql.org/)
*   [Kubernetes](https://kubernetes.io/)

## Project Structure

```
├── amper              # Amper wrapper script (Unix)
├── amper.bat          # Amper wrapper script (Windows)
├── module.yaml        # Amper build config (replaces build.gradle.kts)
├── src/
│   ├── Application.kt          # Ktor entry point
│   └── plugins/
│       ├── Routing.kt          # HTTP routes (/health, /)
│       ├── Databases.kt        # Database connection (Exposed + PostgreSQL)
│       └── Serialization.kt    # JSON content negotiation
├── resources/
│   ├── application.yaml        # Ktor server config
│   └── logback.xml             # Logging config
├── Dockerfile                  # Multi-stage build using Amper
├── docker-compose.yaml         # Local dev with PostgreSQL
└── k8s/
    ├── ktor_app.yaml           # Deployment + Service for the app
    ├── postgresql.yaml         # Deployment + Service + PVC for PostgreSQL
    └── config-system.yaml      # ConfigMap + Secret
```

## Getting Started

Follow these steps to get a local copy of the project up and running.

### Prerequisites

*   Docker
*   JDK 21 or later (only needed for local development without Docker)

### Installation

1.  **Clone the repo**
    ```sh
    git clone https://github.com/DeTiveNC/SpringTemplateDockerized.git
    ```
2.  **Build the project**
    Use the Amper wrapper to build the project.
    ```sh
    ./amper build
    ```
3.  **Run locally**
    ```sh
    ./amper run
    ```
4.  **Run with Docker Compose**
    This will start the Ktor application and the PostgreSQL database.
    ```sh
    docker-compose up
    ```

## REST API Endpoints

*   **Health Check:**
    `GET /health`
    ```json
    {"status": "UP"}
    ```

*   **Root:**
    `GET /`
    ```
    Ktor application is running!
    ```

## Environment Variables

| Variable       | Description                        | Default                                   |
|----------------|------------------------------------|-------------------------------------------|
| `DB_URL`       | JDBC URL for the PostgreSQL database | `jdbc:postgresql://localhost:5432/postgres` |
| `DB_USERNAME`  | Database username                  | `postgres`                                |
| `DB_PASSWORD`  | Database password                  | `postgres`                                |

## Deployment

1.  **Build the Docker image**
    ```sh
    docker compose build
    ```
2.  **Push the Docker image**
    ```sh
    docker push <your-username>/ktor-app:<tag-name>
    ```

## Kubernetes

The `k8s/` directory contains Kubernetes manifests to deploy the application.

1.  Fill in the placeholder values in `k8s/config-system.yaml`
2.  Apply the manifests:
    ```sh
    kubectl apply -f k8s/config-system.yaml
    kubectl apply -f k8s/postgresql.yaml
    kubectl apply -f k8s/ktor_app.yaml
    ```

## Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

## License

Distributed under the MIT License. See `LICENSE` for more information.

## Authors

*   **Nicolas Cao** - *Comp Eng Student* - [Nicolas Cao](https://github.com/detivenc)
