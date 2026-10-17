# Boletín 3

## Resumen de commits

| # | Mensaje del commit | Qué se hizo | Autor | Enlace |
|---|--------------------|-------------|-------|--------|
| 1 | `feat(actuator): añade endpoint de salud con Spring Boot Actuator` | Inclusión de la dependencia `spring-boot-starter-actuator` y configuración en `application.properties` para exponer `/actuator/health` e `info`. | Jiahui | 6518552838d5ee145c1476d7aae3fbd26750078c |
| 2 | `build(docker): añade Dockerfile multi-stage y .dockerignore` | Creación del `Dockerfile` en dos etapas (compilación con Maven/JDK 21 y ejecución con JRE 21 sin root) y exclusión de archivos innecesarios en `.dockerignore`. | Jiahui | ea01da813c49d7b4360390fa1416d25fa073ad4c |
| 3 | `feat(docker): migra de H2 a PostgreSQL y añade docker-compose` | Sustitución del driver de H2 por PostgreSQL, limpieza de propiedades en `application.properties` y creación del fichero `docker-compose.yml` para levantar la API junto con PostgreSQL. | Jiahui | 7e8b7f8ef27f476aeeb89671d8467230894de0d8 |
| 4 | `refactor(docker): externaliza la configuración a .env y añade plantilla .env.example` | Parametrización del `docker-compose.yml` mediante variables de entorno, creación de la plantilla `.env.example`, y aseguramiento de la exclusión de `.env` en `.gitignore` y `.dockerignore`. | Jiahui | c9ca4c2ddb1ec969884df877747acbf398c88a43 |

## Detalle por commit

### 1. `feat(actuator): añade endpoint de salud con Spring Boot Actuator`

- **Qué se hizo:** Se añadió la dependencia starter de Spring Boot Actuator en el `pom.xml` y se configuró `application.properties` para exponer los endpoints `health` e `info`, restringiendo los detalles de salud a usuarios autorizados.
- **Autor:** Jiahui
- **Relación con el boletín:** [Parte B (Endpoint de salud)](#parte-b--endpoint-de-salud).
- **Uso de IA:** No.

### 2. `build(docker): añade Dockerfile multi-stage y .dockerignore`

- **Qué se hizo:** Se definió un `Dockerfile` multi-stage que separa la fase de compilación (`maven:3.9-eclipse-temurin-21`) de la fase de ejecución (`eclipse-temurin:21-jre`), ejecutando la aplicación con un usuario sin privilegios (`app`) e integrando un `HEALTHCHECK` contra `/actuator/health`. También se añadió el fichero `.dockerignore` para evitar enviar artefactos locales, configuraciones del IDE o secretos al contexto de build.
- **Autor:** Jiahui
- **Relación con el boletín:** [Parte C (Dockerfile multi-stage)](#parte-c--dockerfile-multi-stage).
- **Uso de IA:** Gemini. Prompts utilizados: "cómo medir el tiempo de build con docker en PowerShell".

### 3. `feat(docker): migra de H2 a PostgreSQL y añade docker-compose`

- **Qué se hizo:** Se modificó el `pom.xml` para incluir el driver de PostgreSQL en ejecución y aislar H2 exclusivamente para los tests unitarios, adaptando el `application.properties` para trabajar con Hibernate y PostgreSQL. Asimismo, se creó el fichero `docker-compose.yml` definiendo dos servicios (`db` con imagen oficial de PostgreSQL 16, volumen persistente y `pg_isready` como healthcheck, y `api` construida a partir del `Dockerfile` con dependencia condicional de salud).
- **Autor:** Jiahui
- **Relación con el boletín:** Parte D (De H2 a PostgreSQL con Compose).
- **Uso de IA:** Gemini. Para adaptar el proyecto de H2 a PostgreSQL.

### 4. `refactor(docker): externaliza la configuración a .env y añade plantilla .env.example`

- **Qué se hizo:** Se externalizaron las credenciales y parámetros de la base de datos fuera del `docker-compose.yml` utilizando un fichero local `.env`, complementado con una plantilla versionada `.env.example`. Se protegió el entorno local añadiendo `.env` al `.gitignore` y al `.dockerignore`, y se configuraron sintaxis avanzadas de Docker Compose (`${VAR}`, validación estricta con `:?` y escape de variables `$${VAR}` para los healthchecks).
- **Autor:** Jiahui
- **Relación con el boletín:** Parte E (Sacar la configuración a un fichero `.env`).
- **Uso de IA:** No

## Descripción narrativa de lo hecho en la sesión y relación con los commits

### Parte B — Endpoint de salud

Antes de contenerizar la aplicación, es necesario dotarla de un mecanismo que permita comprobar si el servicio está realmente listo para recibir tráfico (y no simplemente que el proceso haya arrancado). Este endpoint será utilizado posteriormente por el `HEALTHCHECK` del `Dockerfile` (Parte C), por Docker Compose (Parte D) y en los futuros pipelines de integración y despliegue continuo.

Para ello, se añade la dependencia de **Spring Boot Actuator** en el archivo `pom.xml`:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

En `src/main/resources/application.properties` se configura Actuator para exponer por web los endpoints health e info.

```Properties
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=when-authorized
```

Una vez aplicados los cambios, se arranca la aplicación en local y se verifica el funcionamiento del endpoint realizando una petición con curl:

```DOS
curl.exe http://localhost:8080/actuator/health
{"status":"UP"}
```

### Parte C — Dockerfile multi-stage

Para poder empaquetar la aplicación se ha usado un archivo `Dockerfile` en la raíz del proyecto para una construcción **multi-stage** en dos etapas:

1. **Etapa `build` (`maven:3.9-eclipse-temurin-21`):** Se encarga de descargar las dependencias y compilar el código fuente generando el archivo `.jar`. Se copia primero el `pom.xml` y se ejecuta `mvn -B dependency:go-offline` antes de copiar el directorio `src/` para aprovechar la caché de capas de Docker.
2. **Etapa `runtime` (`eclipse-temurin:21-jre`):** Parte de una imagen base mucho más reducida que solo contiene el JRE. En ella se instala `curl` (necesario únicamente para el chequeo de salud), se crea un usuario del sistema sin privilegios de `root` (`app`, con UID 1001), se copia exclusivamente el `.jar` generado en la etapa anterior y se configura el `HEALTHCHECK` apuntando a `/actuator/health`.

Se crea un archivo `.dockerignore` para excluir del contexto de construcción carpetas pesadas, metadatos de Git, configuraciones de entorno (`.env`) y documentación que no deberían ser incluidos en la imagen generada.

A continuación, se construye la imagen y se arranca un contenedor mapeando el puerto 8080:

```cmd
docker build -t tareas-api:0.1 .
docker run --rm -p 8080:8080 tareas-api:0.1
```

Desde otra terminal se comprueba que el endpoint `/api/tasks` responde correctamente y, ejecutando `docker ps`, se verifica que tras superar el periodo de inicio el contenedor alcanza el estado `(healthy)` gracias al `HEALTHCHECK` configurado.

![Check de salud tras el primer build y run con docker](capturas/B3/JIAHUI-b3-C(1).png)

#### Comparación de tamaños entre etapas

Para comprobar el ahorro de espacio del enfoque *multi-stage*, se construye una imagen deteniéndose en la etapa intermedia de compilación (`--target build`) y se compara con la imagen final de ejecución:

```cmd
docker build --target build -t tareas-api:build .
docker images tareas-api
```

![resultado de las dos buils](capturas/B3/JIAHUI-b3-C(2).png)

**Explicación de la diferencia de tamaño:** La imagen `tareas-api:build` es notablemente más pesada, pues incluye el **JDK 21 completo**, la instalación de **Apache Maven**, el código fuente (`src/`), las clases compiladas intermedias en `target/` y también el repositorio local de caché de Maven (`/root/.m2`) con todas las dependencias descargadas por `dependency:go-offline`. Por otro ladoo, la imagen `tareas-api:0.1` parte de una imagen limpia que solo contiene el **JRE 21** (entorno de ejecución sin herramientas de compilación) y solo copia el `.jar` final. Esto reduce en gran medida el peso de la imagen producida.

#### Demostración de la caché de capas

Cada instrucción del `Dockerfile` genera una capa que Docker reutiliza de su caché si la instrucción y los archivos que involucra no han cambiado, pero si una capa cambia, **se invalida esa capa y todas las posteriores**. Para demostrarlo empíricamente en PowerShell (se ha usado `Measure-Command`), se realizan dos pruebas modificando una línea en `TaskApiApplication.java`:

1. **Orden óptimo (`COPY pom.xml .` antes que `COPY src ./src`):**
   Se añade un comentario en `TaskApiApplication.java` y se reconstruye la imagen:

   ```powershell
   Measure-Command { docker build -t tareas-api:0.1 . | Out-Host }
   # TotalSeconds : 11,5103781
   ```

   Como `pom.xml` no ha sufrido cambios, Docker reutiliza de la caché (`CACHED`) la capa `RUN mvn -B dependency:go-offline` y solo vuelve a ejecutar desde `COPY src ./src` en adelante, completando el build en **11,51 segundos**.

2. **Orden invertido (`COPY src ./src` por encima de `COPY pom.xml .`):**
   Se modifica el `Dockerfile` colocando la copia de `src/` antes de la copia del `pom.xml`, se modifica línea de comentario añadida en `TaskApiApplication.java` y se mide de nuevo:

   ```powershell
   Measure-Command { docker build -t tareas-api:0.1 . | Out-Host }
   # TotalSeconds : 42,3788604
   ```

   En este caso, al cambiar un archivo dentro de `src/`, se invalida la primera capa `COPY src ./src` y **todas las capas siguientes**, forzando a Docker a repetir la ejecución de `RUN mvn -B dependency:go-offline` y descargar de nuevo todas las dependencias desde Maven Central. El tiempo de construcción ahora son **42,38 segundos**, mucho más lento que en el primer caso.

Una vez comprobada la diferencia, se restaura el `Dockerfile` a su orden original y se limpia el comentario de prueba en el código fuente.

### Parte D — De H2 a PostgreSQL con Compose

Para que la API tenga un almacenamiento persistente, se integra **PostgreSQL** y se implementa el despliegue con **Docker Compose**.

#### D.1 Cambiar la base de datos

En primer lugar, se actualiza el fichero `pom.xml` para incorporar el driver JDBC de PostgreSQL en tiempo de ejecución (`runtime`), manteniendo H2 acotado estrictamente al ámbito de pruebas (`test`) para no romper los tests unitarios.

Después, se limpian del fichero `src/main/resources/application.properties` todas las propiedades asociadas a H2 y se asegura la directiva de actualización automática del esquema mediante JPA/Hibernate.

#### D.2 docker-compose.yml

Para levantar de manera tanto la base de datos y la aplicación, se define el fichero `docker-compose.yml` en la raíz del proyecto. Durante el arranque, el servicio api utiliza `depends_on` con la condición `service_healthy`. Esto obliga a Docker a esperar a que el comando `pg_isready` del contenedor de PostgreSQL devuelva un estado saludable antes de lanzar el contenedor de la API. Además, el uso de un volumen nombrado (`db-data`) garantiza la persistencia física de la información fuera del ciclo de vida de los contenedores.

#### D.3 Comprobar la persistencia

Para comprobar el comportamiento del almacenamiento y la persistencia, se ejecuta el stack completo en segundo plano:

```DOS
docker compose up --build -d
docker compose ps        
```

![Resultado de docker compose ps](capturas/B3/JIAHUI-b3-D(1).png)

Una vez que la aplicación termina de inicializarse, se inserta una tarea de prueba mediante una petición HTTP POST contra el endpoint de la API.

```cmd
Invoke-RestMethod -Uri http://localhost:8080/api/tasks -Method Post -ContentType "application/json" -Body '{"title": "Comprobar persistencia", "description": "Prueba con Postgres", "status": "PENDING", "priority": "HIGH", "dueDate": "2026-10-20"}'

id          : 1
title       : Comprobar persistencia
description : Prueba con Postgres
status      : PENDING
priority    : HIGH
dueDate     : 2026-10-20
```

Al consultar el listado con `curl.exe http://localhost:8080/api/tasks`, la tarea creada aparece correctamente registrada en la base de datos relacional.

```cmd
curl.exe http://localhost:8080/api/tasks

[{"id":1,"title":"Comprobar persistencia","description":"Prueba con Postgres","status":"PENDING","priority":"HIGH","dueDate":"2026-10-20"}]
```

A continuación se evalúa el impacto de las distintas opciones de parada de Compose:

1. `docker compose down`: Detiene y elimina los contenedores y la red privada, pero preserva el volumen nombrado (db-data). Al volver a levantar el stack (`docker compose up -d`), los datos previamente insertados continúan intactos y la tarea sigue disponible en el listado.

    ```cmd
    PS D:\grupo1-PC-2026> docker compose down
    [+] down 3/3
    ✔ Container grupo1-pc-2026-api-1 Removed                                                  0.4s
    ✔ Container grupo1-pc-2026-db-1  Removed                                                  0.4s
    ✔ Network grupo1-pc-2026_default Removed                                                  0.3s
    PS D:\grupo1-PC-2026> docker compose up -d
    [+] up 3/3
    ✔ Network grupo1-pc-2026_default Created                                                  0.0s
    ✔ Container grupo1-pc-2026-db-1  Healthy                                                  6.0s
    ✔ Container grupo1-pc-2026-api-1 Started                                                  6.1s

    What's next:
        Filter, search, and stream logs from all your Compose services
        in one place with Docker Desktop's Logs view. docker-desktop://dashboard/logs?appId=grupo1-pc-2026
    PS D:\grupo1-PC-2026> curl.exe http://localhost:8080/api/tasks
    [{"id":1,"title":"Comprobar persistencia","description":"Prueba con Postgres","status":"PENDING","priority":"HIGH","dueDate":"2026-10-20"}]
    ```

2. `docker compose down -v`: Detiene los servicios y **elimina también los volúmenes asociados**. Al reiniciar el      entorno de cero, el volumen db-data se recrea vacío, por lo que la consulta del listado devuelve un resultado sin elementos.

    ```cmd
    PS D:\Universidad\Master\PC\Boletin-grupo\grupo1-PC-2026> docker compose down -v
    [+] down 4/4
    ✔ Container grupo1-pc-2026-api-1 Removed                                                  0.3s
    ✔ Container grupo1-pc-2026-db-1  Removed                                                  0.4s
    ✔ Network grupo1-pc-2026_default Removed                                                  0.3s
    ✔ Volume grupo1-pc-2026_db-data  Removed                                                  0.0s
    PS D:\Universidad\Master\PC\Boletin-grupo\grupo1-PC-2026> docker compose up -d
    [+] up 4/4
    ✔ Network grupo1-pc-2026_default Created                                                  0.0s
    ✔ Volume grupo1-pc-2026_db-data  Created                                                  0.0s
    ✔ Container grupo1-pc-2026-db-1  Healthy                                                  5.9s
    ✔ Container grupo1-pc-2026-api-1 Started                                                  6.1s

    What's next:
        Filter, search, and stream logs from all your Compose services
        in one place with Docker Desktop's Logs view. docker-desktop://dashboard/logs?appId=grupo1-pc-2026
    PS D:\Universidad\Master\PC\Boletin-grupo\grupo1-PC-2026> curl.exe http://localhost:8080/api/tasks
    []
    ```

### Parte E — Sacar la configuración a un fichero `.env`

Con el fin de esconder las variables de entorno de desarollo y que no pasen a producción, se separa la configuración del código mediante el uso de variables de entorno gestionadas de forma automática por Docker Compose a través del fichero `.env`.

#### Paso 1 y 2 — Plantilla `.env.example` y fichero local `.env`

Se crea en la raíz del proyecto una plantilla documentada (`.env.example`) que **sí se versiona** en el repositorio.
A partir de ella, se genera el fichero local `.env` que es exclusivo para la máquina de desarrollo y se excluye del control de versiones.

#### Paso 3 y 4 — Protección en .gitignore y .dockerignore

Se añade `.env` al fichero `.gitignore` y al `.dockerignore`, para que no se envíe al demonio de Docker durante la construcción de la imagen.

#### Paso 5 — Uso de variables en docker-compose.yml

Se modifica el fichero `docker-compose.yml` para emplear tres mecanismos distintos provistos por Docker Compose:

- ${VAR}: Sustitución directa por el valor definido en el entorno o en el fichero `.env`.

- ${VAR:?mensaje}: Validación estricta que impide el arranque de Compose y muestra un error descriptivo si la variable no está definida o está vacía.

- \$${VAR}: Escape del símbolo de dólar para evitar que Docker Compose resuelva la variable antes de tiempo, permitiendo que sea evaluada internamente dentro del contenedor (utilizado en el comando `pg_isready` del healthcheck).

#### Paso 6 y 7 — Verificación y arranque final

Antes de desplegar el stack, se comprueba la correcta resolución de las variables ejecutando:

```DOS
docker compose config

name: grupo1-pc-2026
services:
  api:
    build:
      context: D:\Universidad\Master\PC\Boletin-grupo\grupo1-PC-2026
      dockerfile: Dockerfile
    depends_on:
      db:
        condition: service_healthy
        required: true
    environment:
      SPRING_DATASOURCE_PASSWORD: secret_local_dev
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/tareas
      SPRING_DATASOURCE_USERNAME: app
    networks:
      default: null
    ports:
      - mode: ingress
        target: 8080
        published: "8080"
        protocol: tcp
  db:
    environment:
      POSTGRES_DB: tareas
      POSTGRES_PASSWORD: secret_local_dev
      POSTGRES_USER: app
    healthcheck:
      test:
        - CMD-SHELL
        - pg_isready -U app -d tareas
      timeout: 3s
      interval: 5s
      retries: 10
    image: postgres:16
    networks:
      default: null
    ports:
      - mode: ingress
        host_ip: 127.0.0.1
        target: 5432
        published: "5432"
        protocol: tcp
    volumes:
      - type: volume
        source: db-data
        target: /var/lib/postgresql/data
        volume: {}
networks:
  default:
    name: grupo1-pc-2026_default
volumes:
  db-data:
    name: grupo1-pc-2026_db-data
```

Verificando que los patrones ${...} han sido reemplazados con éxito por los valores del `.env`. Por último, se limpia el entorno y se levanta el stack validando que el servicio Actuator responde de manera óptima:

```DOS
docker compose down -v
docker compose up --build -d

curl.exe http://localhost:8080/actuator/health
{"status":"UP"}
```
