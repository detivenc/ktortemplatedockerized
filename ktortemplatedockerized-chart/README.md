# Ktor Template Dockerized Chart

Este es un Helm chart para desplegar la aplicación **Ktor Template Dockerized** en un clúster de Kubernetes.

## Requisitos Previos

- [Helm](https://helm.sh/docs/intro/install/) v3.0+
- Clúster de Kubernetes configurado
- Imagen de Docker de la aplicación construida (e.g., `ktor-app:latest`)

## Instalación

Para instalar el chart con el nombre de release `my-release`:

```bash
helm install my-release ./ktortemplatedockerized-chart
```

## Configuración

Los siguientes valores son los más importantes en `values.yaml`:

| Parámetro | Descripción | Defecto |
|-----------|-------------|---------|
| `image.repository` | Repositorio de la imagen | `ktor-app` |
| `image.tag` | Tag de la imagen | `latest` |
| `service.port` | Puerto del servicio | `8080` |
| `env.DB_URL` | URL de conexión a la base de datos | `jdbc:postgresql://ktor-db:5432/postgres` |
| `env.JWT_SECRET` | Secreto para JWT | `change-this-secret-in-production` |

## Desinstalación

Para desinstalar/borrar el despliegue de `my-release`:

```bash
helm uninstall my-release
```
