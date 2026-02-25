# Build stage using Alpine (Amper downloads its own JRE)
FROM alpine:3.23 AS build

RUN apk add --no-cache wget curl tar unzip bash coreutils

WORKDIR /workspace/app

# Copy Amper wrapper and make it executable
COPY amper ./
RUN chmod +x amper

# Copy module config and sources
COPY module.yaml .
COPY src src
COPY resources resources

# Build the fat JAR using Amper (downloads Amper dist + JRE on first run)
ENV AMPER_NO_WELCOME_BANNER=1
RUN ./amper package

# Production stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create non-root user
RUN addgroup -S ktor && adduser -S ktor -G ktor

# Copy the fat JAR produced by Amper
COPY --from=build /workspace/app/build /tmp/amper-build
RUN find /tmp/amper-build -name "*-jvm-all.jar" | head -1 | xargs -I{} cp {} /app/ktor-app.jar && \
    rm -rf /tmp/amper-build

# Change ownership
RUN chown ktor:ktor /app/ktor-app.jar

USER ktor:ktor

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

# Entrypoint with JVM container optimizations
ENTRYPOINT ["java",
    "-XX:+UseContainerSupport",
    "-XX:MaxRAMPercentage=80.0",
    "-XX:+UseG1GC",
    "-XX:MaxGCPauseMillis=200",
    "-jar", "/app/ktor-app.jar"]
