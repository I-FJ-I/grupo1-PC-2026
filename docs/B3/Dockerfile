# Dependencias: Java 17 y Maven 3.10.0
FROM maven:3.10.0-eclipse-temurin-17

# Metadatos del contenedor
LABEL org.opencontainers.image.title="logbook-tests"
LABEL org.opencontainers.image.description="Entorno de tests para zalando/logbook"
LABEL org.opencontainers.image.source="https://github.com/zalando/logbook"

# El código fuente NO se copia: se monta en /project en tiempo de ejecución.
WORKDIR /project

CMD ["mvn", "-B", "test"]
