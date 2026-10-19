# Boletín 3

## Resumen de commits

| # | Mensaje del commit | Qué se hizo | Autor | Enlace |
|---|--------------------|-------------|-------|--------|
| 1 | `feat(actuator): añade endpoint de salud con Spring Boot Actuator` | Inclusión de la dependencia `spring-boot-starter-actuator` y configuración en `application.properties` para exponer `/actuator/health` e `info`. | Jiahui | 6518552838d5ee145c1476d7aae3fbd26750078c |
| 2 | `build(docker): añade Dockerfile multi-stage y .dockerignore` | Creación del `Dockerfile` en dos etapas (compilación con Maven/JDK 21 y ejecución con JRE 21 sin root) y exclusión de archivos innecesarios en `.dockerignore`. | Jiahui | ea01da813c49d7b4360390fa1416d25fa073ad4c |
| 3 | `feat(docker): migra de H2 a PostgreSQL y añade docker-compose` | Sustitución del driver de H2 por PostgreSQL, limpieza de propiedades en `application.properties` y creación del fichero `docker-compose.yml` para levantar la API junto con PostgreSQL. | Jiahui | 7e8b7f8ef27f476aeeb89671d8467230894de0d8 |
| 4 | `refactor(docker): externaliza la configuración a .env y añade plantilla .env.example` | Parametrización del `docker-compose.yml` mediante variables de entorno, creación de la plantilla `.env.example`, y aseguramiento de la exclusión de `.env` en `.gitignore` y `.dockerignore`. | Jiahui | c9ca4c2ddb1ec969884df877747acbf398c88a43 |
| 5 | `feat(dev container) configurar dev container y documentar despliegue local` | Añadido fichero `devcontainer.json` para entorno reproducible, actualizado `README.md` con instrucciones de Docker y Dev Container, e incluido `.env` en `.gitignore`. | Fran | 7ae911d39ab24fe9e7235598fd0b514fd2381b19 |


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
- **Relación con el boletín:** [Parte D (De H2 a PostgreSQL con Compose)](#parte-d--de-h2-a-postgresql-con-compose).
- **Uso de IA:** Gemini. Para adaptar el proyecto de H2 a PostgreSQL.

### 4. `refactor(docker): externaliza la configuración a .env y añade plantilla .env.example`

- **Qué se hizo:** Se externalizaron las credenciales y parámetros de la base de datos fuera del `docker-compose.yml` utilizando un fichero local `.env`, complementado con una plantilla versionada `.env.example`. Se protegió el entorno local añadiendo `.env` al `.gitignore` y al `.dockerignore`, y se configuraron sintaxis avanzadas de Docker Compose (`${VAR}`, validación estricta con `:?` y escape de variables `$${VAR}` para los healthchecks).
- **Autor:** Jiahui
- **Relación con el boletín:** [Parte E (Sacar la configuración a un fichero `.env`)](#parte-e--sacar-la-configuración-a-un-fichero-env).
- **Uso de IA:** No

### 5. `feat(dev container): configurar dev container y documentar despliegue local`

- **Qué se hizo:** Se creó el directorio `.devcontainer` con el fichero `devcontainer.json` configurado para Java 21, Maven y soporte de Docker-in-Docker. Se actualizó el `README.md` con los pasos para levantar el entorno de manera local mediante Compose y mediante Dev Container. Se verificó que el fichero local `.env` estuviera excluido en `.gitignore`.
- **Autor:** Fran
- **Relación con el boletín:** [Parte F (Dev container)](#parte-f--dev-container) y [Parte G (Versionar los cambios)](#parte-g--versionar-los-cambios).
- **Uso de IA:** Gemini. Prompts utilizados: "explicación de campos en devcontainer.json".

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

### Parte F — Dev container

Para garantizar que cualquier persona del equipo trabaje con las mismas herramientas (JDK, Maven, extensiones del IDE) sin tener que instalarlas localmente, se ha implementado un entorno de desarrollo como código utilizando Dev Containers.

Se creó el archivo `.devcontainer/devcontainer.json` con la siguiente configuración:

- **`name`**: Nombre identificativo del entorno (`tareas-api`).
- **`image`**: Define la imagen base proporcionada por Microsoft que ya incluye Java 21.
- **`features`**: Se añaden scripts de instalación estandarizados:
  - `java`: Fuerza la instalación de Maven.
  - `docker-in-docker`: **Esta feature es clave**, ya que instala el motor de Docker dentro del propio contenedor de desarrollo. Permite ejecutar comandos como `docker compose up` desde la terminal integrada de VS Code para levantar la base de datos sin salir del entorno aislado.
- **`forwardPorts`**: Mapea los puertos `8080` (API) y `5432` (PostgreSQL) hacia la máquina host para poder interactuar con ellos desde el navegador o clientes de base de datos locales.
- **`postCreateCommand`**: Ejecuta comandos automáticamente tras crear el contenedor:
  1. `test -f .env || cp .env.example .env`: Si no existe `.env`, crea uno a partir de la plantilla para evitar fallos en Compose.
  2. `if [ -d .githooks ]; then git config core.hooksPath .githooks; fi`: Configura la ruta de los hooks de Git si el proyecto los utiliza.
  3. `mvn -B dependency:go-offline`: Descarga en modo *batch* todas las dependencias del `pom.xml` para acelerar las compilaciones futuras.
- **`customizations`**: Fuerza a VS Code a instalar automáticamente el paquete de extensiones de Java y de Docker al abrir el contenedor.

**Verificación:** Se abrió el proyecto en VS Code seleccionando *Dev Containers: Reopen in Container*. Una vez cargado el entorno, se ejecutó `docker compose up --build -d` en la terminal integrada y se validó el acceso correcto desde el navegador host a `http://127.0.0.1:8080/actuator/health`.

### Parte G — Versionar los cambios

Se actualizó el fichero `README.md` de la raíz del proyecto incorporando dos apartados detallados:

1. Cómo levantar el proyecto con Docker (usando Docker Compose y el archivo `.env`).
2. Cómo iniciar y usar el entorno de Dev Container.

Todo el trabajo (Dockerfile, Compose, Dev Container, actualizaciones del POM y propiedades) se englobó en una nueva rama a partir de un Issue en GitHub y se integró a `main` a través de un Pull Request, asegurando de que el fichero `.env` nunca formara parte de los archivos rastreados por Git.

### Parte H - Registry de Docker (no modifica el repositorio)

Docker Hub (o Registry de Docker) es un servicio en línea que permite almacenar, compartir y descargar imágenes de Docker. Para subir una imagen a Docker Hub, es necesario tener creada una cuenta en Docker. Para iniciar sesión, se ejecuta el comando ``docker login``, el cual autentica con las credenciales existentes.

```powershell

>docker login
Authenticating with existing credentials... [Username: wjesussanchez]

Info → To login with a different account, run 'docker logout' followed by 'docker login'


Login Succeeded

```

El siguiente paso es construir la imagen. Aquellas que son subidas por los usuarios/la comunidad siguen el siguiente formato en su nombre: ``<nombre_de_usuario>/<repositorio>:<etiqueta>``. Dado que las imágenes creadas en el apartado C no contienen el nombre de usuario ``wjesussanchez``, es necesario volver a construirlas con un nuevo nombre. Para evitar la duplicidad, simplemente se añade un alias a la imagen ya existente:

```powershell
docker tag tareas-api:0.1 wjesussanchez/tareas-api:0.1
C:\MISUM\PracticasContinuas\grupo1-PC-2026>docker images
                                                                                                    i Info →   U  In Use
IMAGE                          ID             DISK USAGE   CONTENT SIZE   EXTRA
grupo1-pc-2026-api:latest      c5d7860eea27        550MB          160MB    U
postgres:16                    ca0bd484cb98        642MB          166MB    U
tareas-api:0.1                 7876ec49429c        550MB          160MB
tareas-api:build               04f91650e30b       1.18GB          408MB
wjesussanchez/tareas-api:0.1   7876ec49429c        550MB          160MB
```

Como se puede ver en la salida de ``docker images``, tanto ``tareas-api:0.1`` como ``wjesussanchez/tareas-api:0.1`` tienen el mismo identificador. Con la convención de nombres cumplida, la imagen se puede subir a Docker Hub mediante el siguiente comando:

```powershell
docker push wjesussanchez/tareas-api:0.1
d9d511d19a19: Pushed
d0d2974baaa8: Pushed
435bf5c20694: Pushed
44136fa355b3: Pushed
082f3711ec02: Pushed
0df34dd37994: Pushed
35e448951dfd: Pushed
4c7ba16bd344: Pushed
3fff897544cd: Pushed
4f4fb700ef54: Mounted from hazelcast/hazelcast
5acf4b33726a: Pushed
7c29b9cc5371: Pushed
0.1: digest: sha256:7876ec49429c30b73c59f53e0389055a7fffd813807dad07a3e6d4eb7d824d92 size: 856
```

Una vez finalizado el comando, la imagen ya está disponible para todo el mundo. En el siguiente enlace se muestra en Docker Hub: https://hub.docker.com/r/wjesussanchez/tareas-api. 

![Repositorio de Docker Hub](<capturas/B3/JESUS-b3-H(1).png>)


### Parte I - Entorno de pruebas reproducible (no modifica el repositorio)

#### I.1 Repositorio elegido

Dada la gran cantidad de repositorios disponibles en GitHub, se aplicaron varios filtros para acotar la búsqueda:

- **Estrellas:** mínimo 1000.
- **Lenguaje:** Java.
- **Último push:** posterior al 1 de octubre de 2026.
- **Licencia:** MIT.

La búsqueda utilizada fue:

```
stars:>1000 language:Java pushed:>2026-10-01 license:mit
```

El resultado fue de alrededor de 107 repositorios. De entre todos ellos se escogió [zalando/logbook](https://github.com/zalando/logbook), una librería Java extensible que permite registrar de forma completa las peticiones y respuestas HTTP de distintas tecnologías, tanto del lado del cliente como del servidor. El repositorio fue clonado fuera de este repositorio para la realización de esta parte.

```powershell
git clone https://github.com/zalando/logbook.git 
```

#### Análisis de requisitos

Este proyecto solo presenta dos dependencias obligatorias:
- Java 17 (o mayor): Se indica en el [README.md](https://github.com/zalando/logbook/blob/main/README.md#dependencies), en la sección de Dependencias, y se especifica en el [pom.xml](https://github.com/zalando/logbook/blob/main/pom.xml) raíz:

  ```xml
  <java.version>17</java.version>
  <maven.compiler.source>17</maven.compiler.source>
  <maven.compiler.target>17</maven.compiler.target>
  ```

- Maven 3.10.0: utilizado como gestor de dependencias y herramienta de build para todos los módulos del proyecto. Todas sus dependencias se resuelven desde Maven Central (no se especifica ningún repositorio en el [pom.xml](https://github.com/zalando/logbook/blob/main/pom.xml) raíz). Además, para asegurarse de que se usa la misma versión de Maven sin importar la máquina, el proyecto utiliza Maven Wrapper, especificando la versión 3.10.0 en [maven-wrapper.properties](https://github.com/zalando/logbook/blob/main/.mvn/wrapper/maven-wrapper.properties).


  ```properties
  wrapperVersion=3.3.4
  distributionType=bin
  distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.10.0/apache-maven-3.10.0-bin.zip
  wrapperUrl=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.4/maven-wrapper-3.3.4.jar
  ```

De manera adicional, uno de sus módulos (`logbook-ktor`) utiliza Kotlin, más concretamente su versión 2.4.20, declarado en el módulo `logbook-parent/pom.xml`.

El resto de dependencias solo son necesarias si se usan los módulos de integración correspondientes:

- Servlet Container.
- Apache HttpClient 4.x o 5.x.
- JAX-RS 3.x (Jakarta REST).
- Netty 4.x.
- OkHttp 2.x o 3.x.
- Spring 7.0.9.
- Spring Boot 4.1.1.
- Ktor 3.6.0.
- logstash-logback-encoder 5.x.
- Jackson 2.22.3 o Jackson 3.2.3.

Por último, a nivel de testing, las dependencias que se utilizan se declaran en el módulo `logbook-parent/pom.xml`, reutilizado por el resto de módulos:

| Dependencia | Versión | Para qué se usa |
|---|---:|---|
| JUnit Jupiter | 6.1.3 | Framework principal: ciclo de vida, `@Test`, aserciones y tests parametrizados |
| AssertJ | 3.27.7 | Aserciones fluidas y legibles sobre objetos, listas y excepciones |
| Mockito | 5.2.0 | Creación de mocks y spies para aislar dependencias |
| `mockito-junit-jupiter` | 5.2.0 | Integración de Mockito con JUnit 5 (`@Mock`, `@InjectMocks`) |
| Hamcrest | 3.0 | Matchers usados por algunos tests y por las verificaciones de WireMock |
| WireMock | 3.12.1 | Servidor HTTP embebido para simular respuestas sin depender de servicios reales |
| Guava | 33.7.2-jre | Utilidades de apoyo en los tests (colecciones, streams, etc.) |
| JsonPath | 3.0.0 | Extracción y verificación de campos concretos dentro de respuestas JSON |


#### Dockerfile

El proyecto solo requiere como dependencias JDK 17 y Maven. No obstante, su primera ejecución necesita conexión a Maven Central para descargar las dependencias y plugins necesarios. También, dado el uso de WireMock, que levanta un servidor HTTP local, se requiere acceso a la red loopback.

Sabiendo esto, el archivo ``Dockerfile`` incluirá la imagen `maven:3.10.0-eclipse-temurin-17`, que incluye las dos dependencias obligatorias. Por otro lado, se establece ``/project`` como directorio de trabajo, que es donde se montará el código fuente. 

```dockerfile
# Dependencias: Java 17 y Maven 3.10.0
FROM maven:3.10.0-eclipse-temurin-17

# Metadatos del contenedor
LABEL org.opencontainers.image.title="logbook-tests"
LABEL org.opencontainers.image.description="Entorno de tests para zalando/logbook"
LABEL org.opencontainers.image.source="https://github.com/zalando/logbook"

# El código fuente NO se copia: se monta en /project en tiempo de ejecución.
WORKDIR /project

CMD ["mvn", "-B", "test"]
```

Para construir la imagen se ejecuta el siguiente comando:

```powershell
C:\MISUM\PracticasContinuas\grupo1-PC-2026>cd seguimiento/b3

C:\MISUM\PracticasContinuas\grupo1-PC-2026\seguimiento\B3>docker build -t logbook-tests:1.0 .
[+] Building 3.1s (7/7) FINISHED                                                                                                       docker:desktop-linux
 => [internal] load build definition from Dockerfile                                                                                                   0.1s
 => => transferring dockerfile: 778B                                                                                                                   0.0s
 => [internal] load metadata for docker.io/library/maven:3.9-eclipse-temurin-17                                                                        2.4s
 => [auth] library/maven:pull token for registry-1.docker.io                                                                                           0.0s
 => [internal] load .dockerignore                                                                                                                      0.1s
 => => transferring context: 2B                                                                                                                        0.0s
 => [1/2] FROM docker.io/library/maven:3.9-eclipse-temurin-17@sha256:1a352420f7aba21f5ad08df31bab55f74c013fb491f1ae8ab1dd7ff9ed698584                  0.1s
 => => resolve docker.io/library/maven:3.9-eclipse-temurin-17@sha256:1a352420f7aba21f5ad08df31bab55f74c013fb491f1ae8ab1dd7ff9ed698584                  0.0s
 => CACHED [2/2] WORKDIR /project                                                                                                                      0.0s
 => exporting to image                                                                                                                                 0.2s
 => => exporting layers                                                                                                                                0.0s
 => => exporting manifest sha256:4ce2e383a6881e7e1481e258d965cd34f6adf682236ec7cda1dd380dbe592dcc                                                      0.0s
 => => exporting config sha256:24a907d3a99d2592d3a6c78cee8f8628403b4a20e12131cbd39df5839a289398                                                        0.0s
 => => exporting attestation manifest sha256:b5a21fd606539e3410f428b0e8cc39cd604a47910a3a6d9619ddfda152fe429f                                          0.0s
 => => exporting manifest list sha256:e36de2c00a679b65fab0182edc49db9bc413994ae0e46d29c0e65ce00011f9e1                                                 0.0s
 => => naming to docker.io/library/logbook-tests:1.0                                                                                                   0.0s
 => => unpacking to docker.io/library/logbook-tests:1.0                                                                                                0.0s

View build details: docker-desktop://dashboard/build/desktop-linux/desktop-linux/r4duo9kppp9g79sbk1vbwaq1n
```
Una vez construida, se ejecutan los tests montando el código fuente y un volumen para la caché de Maven:

```powershell
docker run --rm `
  -v "C:\MISUM\PracticasContinuas\parte-i\logbook:/project" `
  -v logbook-m2:/root/.m2 `
  logbook-tests:1.0
```
Hay que tener en cuenta lo siguiente:

- La primera opción `-v` enlaza la carpeta local `parte-i\logbook` con `/project` dentro del contenedor, de modo que Maven compila y ejecuta los tests sobre el código real. De esta forma, se separa el entorno de ejecución del código.
- La segunda opción ``-v`` monta el volumen nombrado `logbook-m2` en `/root/.m2`, que es donde Maven guarda las dependencias descargadas. De esta forma, **NO** es necesario descargar en cada ejecución.
- Por último, `--rm` elimina el contenedor al terminar.

La primera ejecución tarda más porque descarga todas las dependencias; las siguientes reutilizan la caché de `logbook-m2` y arrancan mucho más rápido.

Al lanzar el contenedor, se muestra que el reactor de Maven contiene 33 entradas. En el momento de la entrega se han completado 14 de ellas, todas con estado 'SUCCESS' y sin ningún test con fallo o error. Dado el tiempo de la entrega, no es posible incluir el resumen final del reactor
 ya que el proceso sigue en ejecución.
El log parcial se adjunta como evidencia en [test-ejecucion-resultado.txt](B3/test-ejecucion-resultado.txt) y la suite puede reproducirse con el
mismo comando.

