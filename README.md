<p align="center">
  <a href="https://skillicons.dev">
    <img src="https://skillicons.dev/icons?i=githubactions,kubernetes,docker,kotlin,postgres,ktor&perline=7" />
  </a>
</p>

<h1 align="center">Ktor Template Dockerized</h1>

<p align="center">
  <strong>Plantilla profesional de Ktor lista para producción con Docker, Kubernetes e integración de IA.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/github/license/DeTiveNC/ktortemplatedockerized" alt="Licencia" />
  <img src="https://img.shields.io/github/contributors/DeTiveNC/ktortemplatedockerized?color=dark-green" alt="Colaboradores" />
  <img src="https://img.shields.io/github/stars/DeTiveNC/ktortemplatedockerized?style=social" alt="Estrellas" />
  <img src="https://img.shields.io/github/issues/DeTiveNC/ktortemplatedockerized" alt="Issues" />
</p>

---

## 🚀 Descripción General

Esta plantilla proporciona una base sólida y escalable para construir backends modernos utilizando **Ktor**. Está diseñada con un enfoque "listo para producción", incluyendo contenerización, integración con bases de datos, seguridad JWT y soporte experimental para agentes de IA mediante **Koog**.

El proyecto utiliza **Amper**, la herramienta de construcción simplificada de JetBrains, para una experiencia de configuración más limpia en comparación con los setups tradicionales de Gradle.

### ✨ Características Principales

- ⚡ **Ktor 3.4.0**: Framework web asíncrono de alto rendimiento para Kotlin.
- 📦 **Docker & Compose**: Contenerización fluida y entorno de desarrollo local listo.
- 🎡 **Helm**: Gestión de despliegues simplificada mediante charts de Helm para Kubernetes.
- 🗄️ **Exposed 1.1.1**: Framework SQL de tipado seguro para Kotlin.
- 🔐 **Seguridad JWT**: Endpoints protegidos mediante autenticación JWT.
- 🤖 **Integración de IA**: Soporte integrado para agentes de IA utilizando la librería Koog.
- 🛠️ **Sistema de Construcción Amper**: Configuración basada en YAML para dependencias y builds.

---

## 📁 Estructura del Proyecto

```text
├── .github/workflows   # Pipelines de CI/CD
├── gradle/             # Catálogo de versiones (libs.versions.toml)
├── ktortemplatedockerized-chart/ # Chart de Helm para despliegue
├── src/
│   ├── Application.kt  # Punto de entrada de la aplicación
│   └── plugins/        # Configuraciones de características de Ktor
│       ├── AI.kt       # Configuración de Agentes de IA
│       ├── Databases.kt# Configuración de Exposed & PostgreSQL
│       ├── Routing.kt  # Definición de endpoints de la API
│       ├── Security.kt # Configuración de JWT & Autenticación
│       └── ...
├── module.yaml         # Configuración de construcción de Amper
├── Dockerfile          # Build optimizado multi-etapa
└── docker-compose.yaml # Stack de desarrollo local
```

---

## 🛠️ Comenzando

### Requisitos Previos

- **JDK 21+**
- **Docker & Docker Compose**
- **Amper** (Incluido mediante el wrapper `./amper`)

### Instalación y Ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/DeTiveNC/ktortemplatedockerized.git
   cd ktortemplatedockerized
   ```

2. **Ejecutar localmente (Desarrollo):**
   ```bash
   ./amper run
   ```

3. **Ejecutar con Docker Compose:**
   ```bash
   docker-compose up --build
   ```

---

## 🔒 Seguridad y Configuración

La aplicación se configura a través de `resources/application.yaml` y variables de entorno.

| Variable      | Descripción                      | Valor por Defecto                           |
|---------------|----------------------------------|---------------------------------------------|
| `DB_URL`      | URL de conexión JDBC             | `jdbc:postgresql://localhost:5432/postgres` |
| `DB_USERNAME` | Usuario de la base de datos      | `postgres`                                  |
| `DB_PASSWORD` | Contraseña de la base de datos   | `postgres`                                  |
| `JWT_SECRET`  | Clave secreta para firma JWT     | `change-this-secret-in-production`          |

> [!IMPORTANT]
> Cambia siempre el `JWT_SECRET` y las credenciales de la base de datos antes de desplegar en un entorno de producción.

---

## 🔌 Endpoints de la API

- **Health:** `GET /health` -> Retorna `{"status": "UP"}`
- **Auth:** `POST /auth/login` -> Retorna un token JWT.
- **Secure:** `GET /secure` -> (Requiere JWT) Saluda al usuario autenticado.
- **AI Chat:** `POST /ai/chat` -> (Requiere JWT) Interfaz con el agente de IA.

---

## 🚢 Despliegue
1. Construye la imagen de Docker:
   ```bash
   docker build -t ktor-app:latest .
   ```

2. Instala el Chart de Helm:
   ```bash
   helm install ktortemplatedockerized ./ktortemplatedockerized-chart
   ```

3. (Opcional) Actualiza el Chart:
   ```bash
   helm upgrade ktortemplatedockerized ./ktortemplatedockerized-chart
   ```

---

## 🤝 Contribuir

¡Las contribuciones son bienvenidas! Siéntete libre de enviar un Pull Request.

1. Haz un Fork del proyecto.
2. Crea tu rama de características (`git checkout -b feature/NuevaCaracteristica`).
3. Realiza tus cambios (`git commit -m 'Añadir NuevaCaracteristica'`).
4. Sube los cambios a tu rama (`git push origin feature/NuevaCaracteristica`).
5. Abre un Pull Request.

---

## 📄 Licencia

Distribuido bajo la **Licencia MIT**. Consulta `LICENSE` para más información.

---

<p align="center">
  Hecho con ❤️ por <a href="https://github.com/detivenc">Nicolas Cao</a>
</p>
