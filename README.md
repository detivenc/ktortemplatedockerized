<p align="center">
  <a href="https://skillicons.dev">
    <img src="https://skillicons.dev/icons?i=githubactions,kubernetes,docker,kotlin,postgres,ktor&perline=7" />
  </a>
</p>

<h1 align="center">Ktor Template Dockerized</h1>

<p align="center">
  <strong>A professional, ready-to-use Ktor application template with Docker, Kubernetes, and AI integration.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/github/license/DeTiveNC/SpringTemplateDockerized" alt="License" />
  <img src="https://img.shields.io/github/contributors/DeTiveNC/SpringTemplateDockerized?color=dark-green" alt="Contributors" />
  <img src="https://img.shields.io/github/stars/DeTiveNC/SpringTemplateDockerized?style=social" alt="Stars" />
  <img src="https://img.shields.io/github/issues/DeTiveNC/SpringTemplateDockerized" alt="Issues" />
</p>

---

## 🚀 Overview

This template provides a robust foundation for building modern, scalable backends using **Ktor**. It is designed with "production-ready" in mind, featuring containerization, database integration, JWT security, and experimental AI agent support via **Koog**.

The project uses **Amper** — JetBrains' simplified build tool — for a cleaner configuration experience compared to traditional Gradle setups.

### ✨ Key Features

- ⚡ **Ktor 3.1.1**: High-performance, asynchronous web framework for Kotlin.
- 📦 **Docker & Compose**: Seamless containerization and local development setup.
- 🎡 **Kubernetes**: Production-grade manifests for deployment, including resource limits and health probes.
- 🗄️ **Exposed 0.59.0**: Type-safe SQL framework for Kotlin.
- 🔐 **JWT Authentication**: Secure API endpoints out-of-the-box.
- 🤖 **AI Integration**: Built-in support for AI agents using the Koog library.
- 🛠️ **Amper Build System**: YAML-based configuration for dependencies and builds.

---

## 📁 Project Structure

```text
├── .github/workflows   # CI/CD pipelines
├── gradle/             # Version catalogs (libs.versions.toml)
├── k8s/                # Kubernetes manifests
├── src/
│   ├── Application.kt  # App entry point
│   └── plugins/        # Ktor feature configurations
│       ├── AI.kt       # AI Agent setup
│       ├── Databases.kt# Exposed & PostgreSQL setup
│       ├── Routing.kt  # API endpoints
│       ├── Security.kt # JWT & Auth
│       └── ...
├── module.yaml         # Amper build configuration
├── Dockerfile          # Multi-stage optimized build
└── docker-compose.yaml # Local development stack
```

---

## 🛠️ Getting Started

### Prerequisites

- **JDK 21+**
- **Docker & Docker Compose**
- **Amper** (Included via wrapper `./amper`)

### Installation & Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/DeTiveNC/SpringTemplateDockerized.git
   cd SpringTemplateDockerized
   ```

2. **Run locally (Development):**
   ```bash
   ./amper run
   ```

3. **Run with Docker Compose:**
   ```bash
   docker-compose up --build
   ```

---

## 🔒 Security & Configuration

The application is configured via `resources/application.yaml` and environment variables.

| Variable      | Description                      | Default                                     |
|---------------|----------------------------------|---------------------------------------------|
| `DB_URL`      | JDBC connection URL              | `jdbc:postgresql://localhost:5432/postgres` |
| `DB_USERNAME` | Database user                    | `postgres`                                  |
| `DB_PASSWORD` | Database password                | `postgres`                                  |
| `JWT_SECRET`  | Secret key for JWT signing       | `change-this-secret-in-production`          |

> [!IMPORTANT]
> Always change the `JWT_SECRET` and database credentials before deploying to a production environment.

---

## 🔌 API Endpoints

- **Health:** `GET /health` -> Returns `{"status": "UP"}`
- **Auth:** `POST /auth/login` -> Returns a JWT token.
- **Secure:** `GET /secure` -> (Requires JWT) Greets the authenticated user.
- **AI Chat:** `POST /ai/chat` -> (Requires JWT) Interface with the AI agent.

---

## 🚢 Deployment

### Kubernetes

1. Update values in `k8s/config-system.yaml`.
2. Apply the configuration:
   ```bash
   kubectl apply -f k8s/
   ```

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

Distributed under the **MIT License**. See `LICENSE` for more information.

---

<p align="center">
  Made with ❤️ by <a href="https://github.com/detivenc">Nicolas Cao</a>
</p>
