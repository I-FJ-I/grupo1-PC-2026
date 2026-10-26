# Boletín 1



## Resumen de commits

| # | Mensaje del commit | Qué se hizo |
|---|--------------------|-------------|
| 1 | `chore: proyecto Maven inicial de la API de tareas` | Inicializa el repositorio con el esqueleto Maven y la API REST de tareas |
| 2 | `chore(editor): añadir .editorconfig` | Añade configuración de estilo para los editores |
| 3 | `feat(tasks): añadir subtareas y campos tiempo estimado y tiempo dedicado` | Implementa subtareas y los campos de tiempo, con sus reglas y tests |
| 4 | `chore(build): añade Spotless para formateo automático` | Configura el plugin Spotless en el `pom.xml` |
| 5 | `chore(hooks): añade hook de pre-commit con Spotless` | Crea `.githooks/pre-commit` para formatear antes de cada commit |
| 6 | `refactor(dto): formeatear indentación` | Comprueba que el hook reformatea automáticamente el código |
| 7 | `feat(service): tareas con prioridad crítica o alta requieren fecha límite` | Rama A: exige fecha límite a CRITICAL y HIGH |
| 8 | `feat(service): tareas críticas requieren fecha límite futura` | Rama B: exige fecha límite futura a CRITICAL |
| 9 | `merge: resolver conflicto en regla de fecha límite` | Resuelve a mano el conflicto combinando ambas reglas |

## Detalle por commit

### 1. `chore: proyecto Maven inicial de la API de tareas`
- **Qué se hizo:** Se inicializa el repositorio Git y se sube el proyecto Maven generado por IA: la API REST de gestión de tareas con Spring Boot 3, Java 21, JPA/Hibernate, H2, validaciones, manejo de errores centralizado y tests unitarios.
- **Relación con el boletín:** apartado B y D (creación de la aplicación y primer commit).
- **Uso de IA:** IA utilizada para generar el proyecto completo a partir del prompt indicado en el apartado B, más adelante en este boletín.

### 2. `chore(editor): añadir .editorconfig`
- **Qué se hizo:** Se añade un archivo `.editorconfig` para unificar la indentación y el estilo en todos los editores del equipo.
- **Relación con el boletín:** apartado D (control de versiones).
- **Uso de IA:** No se ha utilizado IA.

### 3. `feat(tasks): añadir subtareas y campos tiempo estimado y tiempo dedicado`
- **Qué se hizo:** En la rama `feat/subtasks` se implementa la relación padre-hijo entre tareas, la regla de que una tarea no puede finalizar si tiene subtareas bloqueadas, y los campos tiempo estimado/dedicado calculados a partir de las subtareas. Se añaden 9 tests unitarios nuevos.
- **Relación con el boletín:** expansión de la aplicación con subtareas y las reglas de negocio solicitadas.
- **Uso de IA:** IA utilizada para generar la ampliación a partir del prompt indicado en el apartado D, más adelante en este boletín.

### 4. `chore(build): añade Spotless para formateo automático`
- **Qué se hizo:** Se añade al `pom.xml` el plugin `spotless-maven-plugin` con Google Java Format para unificar el estilo del código.
- **Relación con el boletín:** apartado E (formateo automático en cada commit).
- **Uso de IA:** No se ha utilizado IA.

### 5. `chore(hooks): añade hook de pre-commit con Spotless`
- **Qué se hizo:** Se crea `.githooks/pre-commit` (carpeta versionada) y se configura `core.hooksPath` para que Git ejecute `mvn spotless:apply` antes de cada commit.
- **Relación con el boletín:** apartado E (hook de pre-commit y por qué se versiona en `.githooks`).
- **Uso de IA:** No se ha utilizado IA.

### 6. `refactor(dto): formeatear indentación`
- **Qué se hizo:** Se desordena la indentación de un DTO a propósito y se hace commit: el hook ejecuta Spotless y deja el archivo re-formateado, comprobando que el hook funciona.
- **Relación con el boletín:** apartado E (verificación del hook).
- **Uso de IA:** No se ha utilizado IA.

### 7. `feat(service): tareas con prioridad crítica o alta requieren fecha límite`
- **Qué se hizo:** Rama A (`feat/require-due-date-high`). Modifica `validateCriticalRequiresDueDate` para exigir fecha límite también a las tareas de prioridad HIGH.
- **Relación con el boletín:** apartado F (rama que se mergea sin conflicto).
- **Uso de IA:** IA utilizada para generar el conflicto en las dos ramas ya creadas (ver apartado F).

### 8. `feat(service): tareas críticas requieren fecha límite futura`
- **Qué se hizo:** Rama B (`feat/due-date-must-be-future`). Modifica la misma línea del mismo método para exigir a las tareas CRITICAL una fecha límite futura.
- **Relación con el boletín:** apartado F (rama cuyo merge produce el conflicto).
- **Uso de IA:** IA utilizada para generar el conflicto en las dos ramas ya creadas (ver apartado F).

### 9. `merge: resolver conflicto en regla de fecha límite`
- **Qué se hizo:** Al mergear la rama B, Git detecta un conflicto en la misma línea. Se edita a mano quitando los marcadores `<<<<<<<  =======  >>>>>>>` y se deja una versión que combina ambas reglas: HIGH exige fecha límite y CRITICAL exige fecha futura. Se cierra con `git add` y `git commit`.
- **Relación con el boletín:** apartado F (provocar y resolver el conflicto).
- **Uso de IA:** No se ha utilizado IA.

## Descripción narrativa de lo hecho en la sesión y relación con los commits

### Parte A - Instalación y verificación del entorno

Se comprueba que el equipo tiene Git, Maven y JDK 21 instalados con los siguientes comandos y salidas: 

```bash
# En los comentarios se indica la salida obtenida

git --version 
# git version 2.55.0.windows.5

java -version 
#java 21.0.8 2025-07-15 LTS
#Java(TM) SE Runtime Environment (build 21.0.8+12-LTS-250)
#Java HotSpot(TM) 64-Bit Server VM (build 21.0.8+12-LTS-250, mixed mode, sharing) 

mvn -version
#Apache Maven 3.9.11 (3e54c93a704957b63ee3494413a2b544fd3d825b)
#Maven home: C:\ProgramData\chocolatey\lib\maven\apache-maven-3.9.11
#Java version: 21.0.8, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-21
#Default locale: es_ES, platform encoding: UTF-8
#OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"

```

Además, se configura la identidad con la que aparecerán los commits y se establece una política de pull segura:

```cmd
git config --global user.name "Tu Nombre"
git config --global user.email "tu@email.com"
git config --global pull.ff only      # evita merges automáticos sorpresa
git config --global init.defaultBranch main #establece que la rama predeterminada de todos los repositorios locales se llame main
```

### Parte B — Generar la aplicación con ayuda de IA

El modelo de IA utilizado ha sido DeepSeek V4.1 Flash, de OpenCode. El prompt empleado ha sido el siguiente: 

```
Genera un proyecto Maven con Spring Boot 3 y Java 21: una API REST de gestión de tareas (entidad Task con id, título, descripción, estado, prioridad y fecha límite). Usa Spring Data JPA con Hibernate para la persistencia y H2 en memoria como base de datos. Incluye endpoints CRUD, capa de servicio, repositorio JPA, validaciones con Bean Validation, manejo de errores con @RestControllerAdvice y tests unitarios con JUnit 5. 

Requisitos mínimos de la aplicación para considerarse “no trivial”:

- Al menos una entidad de dominio con varios campos y validaciones.
- Persistencia mediante Spring Data JPA/Hibernate.
- Operaciones CRUD completas (crear, leer, actualizar, borrar).
- Separación en capas (controlador / servicio / repositorio).
- Manejo de errores centralizado: un recurso inexistente devuelve 404, no una traza de excepción.
- Al menos 5 tests que comprueben reglas de negocio reales (p. ej. “no se puede crear una tarea con fecha límite pasada”), no simples assertNotNull.
- No debe incluir tests de integración que levanten el contexto de la aplicación y/o una base de datos. Solo debe incluir tests unitarios.
- El proyecto debe generar mediante mvn package un JAR ejecutable (fat JAR) que incluya todas las dependencias y pueda iniciarse mediante java -jar target/<nombre>.j

```

La estructura del proyecto generado es la siguiente:

```cmd

C:.
├───src
│   ├───main
│   │   ├───java
│   │   │   └───com
│   │   │       └───example
│   │   │           └───taskmanager
│   │   │               ├───config # Configuración del proyecto
│   │   │               ├───controller # Endpoints de la aplicación
│   │   │               ├───domain # Modelo de dominio
│   │   │               ├───dto # Clases DTO utilizadas para la petición y respuestas HTTP
│   │   │               ├───exception # Definición de excepciones y un handler global
│   │   │               ├───repository # Acceso y persistencia de los datos
│   │   │               └───service # Lógica de negocio definida a través de interfaces
│   │   │                   └───impl # Implementaciones concretas de las interfaces
│   │   └───resources
│   └───test
│       └───java
│           └───com
│               └───example
│                   └───taskmanager # Tests para la capa de dominio y servicio
│                       ├───domain 
│                       └───service 
│                           └───impl
└───target
    ├───classes # Clases .java compiladas a .class. Estructura igual que src/main/java
    │   └───com
    │       └───example
    │           └───taskmanager 
    │               ├───config
    │               ├───controller
    │               ├───domain
    │               ├───dto
    │               ├───exception
    │               ├───repository
    │               └───service
    │                   └───impl
    ├───generated-sources # Carpeta donde herramientas externas generan código automático durante la compilación
    │   └───annotations
    ├───generated-test-sources # Carpeta donde herramientas externas generan código automático durante la compilación
    │   └───test-annotations
    ├───maven-archiver # Archivos internos de Maven para empaquetar el proyecto
    ├───maven-status # Registros internos de Maven
    │   └───maven-compiler-plugin
    │       ├───compile
    │       │   └───default-compile
    │       └───testCompile
    │           └───default-testCompile
    ├───surefire-reports # Resultados de las pruebas unitarias
    └───test-classes # Igual a classes, pero para src/test/java
        └───com
            └───example
                └───taskmanager
                    ├───domain
                    └───service
                        └───impl

```

### Parte C — Construir y probar en local

Para construir el proyecto, se ejecuta ``mvn clean package``. Este comando elimina y vuelve a generar la carpeta ``target``, validando la estructura del proyecto, ejecutando los tests definidos y empaquetando todo el proyecto y dependencias en un único archivo ejecutable (``.jar``).

```cmd
#Ejemplo de ejecución exitosa
C:\MISUM\PracticasContinuas\boletin1\proyecto>mvn clean package
[INFO] Scanning for projects...
[INFO]
[INFO] ----------------------< com.example:task-manager >----------------------
[INFO] Building task-manager 1.0.0
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- clean:3.2.0:clean (default-clean) @ task-manager ---
[INFO] Deleting C:\MISUM\PracticasContinuas\boletin1\proyecto\target
[INFO]
[INFO] --- resources:3.3.1:resources (default-resources) @ task-manager ---
[INFO] Copying 1 resource from src\main\resources to target\classes
[INFO]
[INFO] --- compiler:3.8.1:compile (default-compile) @ task-manager ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 16 source files to C:\MISUM\PracticasContinuas\boletin1\proyecto\target\classes
[INFO]
[INFO] --- resources:3.3.1:testResources (default-testResources) @ task-manager ---
[INFO] skip non existing resourceDirectory C:\MISUM\PracticasContinuas\boletin1\proyecto\src\test\resources
[INFO]
[INFO] --- compiler:3.8.1:testCompile (default-testCompile) @ task-manager ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 2 source files to C:\MISUM\PracticasContinuas\boletin1\proyecto\target\test-classes
[INFO]
[INFO] --- surefire:2.22.2:test (default-test) @ task-manager ---
[INFO]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.taskmanager.domain.TaskTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.169 s - in com.example.taskmanager.domain.TaskTest
[INFO] Running com.example.taskmanager.service.impl.TaskServiceImplTest
Mockito is currently self-attaching to enable the inline-mock-maker. This will no longer work in future releases of the JDK. Please add Mockito as an agent to your build what is described in Mockito's documentation: https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#0.3
WARNING: A Java agent has been loaded dynamically (C:\Users\jesus\.m2\repository\net\bytebuddy\byte-buddy-agent\1.15.11\byte-buddy-agent-1.15.11.jar)
WARNING: If a serviceability tool is in use, please run with -XX:+EnableDynamicAgentLoading to hide this warning
WARNING: If a serviceability tool is not in use, please run with -Djdk.instrument.traceUsage for more information
WARNING: Dynamic loading of agents will be disallowed by default in a future release
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.526 s - in com.example.taskmanager.service.impl.TaskServiceImplTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 21, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO]
[INFO] --- jar:3.1.0:jar (default-jar) @ task-manager ---
[INFO] Building jar: C:\MISUM\PracticasContinuas\boletin1\proyecto\target\task-manager.jar
[INFO]
[INFO] --- spring-boot:3.4.4:repackage (default) @ task-manager ---
[INFO] Replacing main artifact C:\MISUM\PracticasContinuas\boletin1\proyecto\target\task-manager.jar with repackaged archive, adding nested dependencies in BOOT-INF/.
[INFO] The original artifact has been renamed to C:\MISUM\PracticasContinuas\boletin1\proyecto\target\task-manager.jar.original
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  10.643 s
[INFO] Finished at: 2026-09-27T21:30:29+02:00
[INFO] ------------------------------------------------------------------------

```

Una vez ejecutado con éxito, se lanza la aplicación con ``java -jar target/task-manager.jar``. En caso de error, la terminal devolvería el código de error.

```cmd
# Salida esperada

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.4.4)

2026-09-27T21:32:28.224+02:00  INFO 20868 --- [task-manager] [           main] c.e.taskmanager.TaskManagerApplication   : Starting TaskManagerApplication using Java 21.0.8 with PID 20868 (C:\MISUM\PracticasContinuas\boletin1\proyecto\target\task-manager.jar started by jesús in C:\MISUM\PracticasContinuas\boletin1\proyecto)
[...]
2026-09-27T21:32:34.281+02:00  INFO 20868 --- [task-manager] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2026-09-27T21:32:34.309+02:00  INFO 20868 --- [task-manager] [           main] c.e.taskmanager.TaskManagerApplication   : Started TaskManagerApplication in 6.725 seconds (process running for 7.446) 

```

Lanzada la aplicación, queda por probar los endpoints de la aplicación para garantizar su correcto funcionamiento. Se abre una nueva terminal (cmd) y se utiliza ``curl.exe`` para realizar las peticiones HTTP. Como en la terminal de Windows las comillas del JSON son problemáticas, se escribe el cuerpo de la petición en un archivo ``task.json`` y se referencia con ``-d @task.json``:

```cmd
:: Archivo JSON con el cuerpo de la petición
echo {"title":"Tarea de prueba","priority":"MEDIUM","dueDate":"2026-12-31"} > task.json

:: 1. Crear tarea (POST)
curl.exe -X POST http://localhost:8080/api/tasks -H "Content-Type: application/json" -d @task.json

:: 2. Listar tareas (GET)
curl.exe http://localhost:8080/api/tasks

:: 3. Obtener por id (GET)
curl.exe http://localhost:8080/api/tasks/1

:: 4. Actualizar (PUT) 
echo {"title":"Tarea editada","priority":"HIGH","dueDate":"2026-12-31"} > task.json
curl.exe -X PUT http://localhost:8080/api/tasks/1 -H "Content-Type: application/json" -d @task.json

:: 5. Cambiar estado a DONE (PATCH)
echo {"status":"DONE"} > task.json
curl.exe -X PATCH http://localhost:8080/api/tasks/1/status -H "Content-Type: application/json" -d @task.json

:: 6. Tarea inexistente (GET -> 404 con JSON, no traza)
curl.exe http://localhost:8080/api/tasks/99999
```

### Parte D — Control de versiones con Git

Para inicializar el repositorio git, como no se dispone de un repositorio remoto existente, se crea uno nuevo con ``git init``. Además, se crea un archivo ``.gitignore`` en el que se especifica la carpeta ``target/``, todo archivo ``.class``, la carpeta ``.idea`` completa y el archivo ``.env``.


```cmd
git init
(echo target/ & echo *.class & echo .idea/ & echo .env) > .gitignore

```

Adicionalmente se ejecuta el siguiente comando para normalizar los finales de línea según el sistema operativo utilizado (en este caso Windows 11). Además, trata los archivos ``.jar`` como binarios.

```cmd
(echo * text=auto eol=lf & echo *.jar binary) > .gitattributes
```

Una vez se ha configurado git a través de estos archivos, se realiza el primer commit del repositorio. Como se ha creado desde cero, ninguno de los archivos es seguido por git, por lo que se deben añadir con ``git add``. Primero, se añaden los archivos ``.gitignore`` y ``.gitattributes`` para aplicar sus reglas al resto de archivos y commits futuros. 

```cmd
#Salida esperada
Changes to be committed:
  (use "git restore --staged <file>..." to unstage)
        new file:   .gitattributes
        new file:   .gitignore

Untracked files:
  (use "git add <file>..." to include in what will be committed)
        README.md
        pom.xml
        src/
```

 ``.gitignore`` se ejecuta correctamente, ya que la carpeta ``target/`` no aparece. Con esto comprobado, se ejecuta ``git add -A`` para incluir el resto de archivos, y ``git commit -m "chore: proyecto Maven inicial de la API de tareas" `` con el que se guarda el estado del proyecto hasta este momento.


Después de este commit, se añade un nuevo archivo ``.editorconfig`` para que todos los editores usen la misma indentación y se incluye en un nuevo commit.

```bash
git add .editorconfig
git commit -m "chore(editor): añadir .editorconfig"
```

A continuación, se añade una nueva característica al proyecto: subtareas. Para ello, se crea una nueva rama ``git switch -c feat/subtasks`` y se utiliza el siguiente prompt:

```txt
# Prompt utilizado
Se quiere expandir el proyecto con las siguientes características:
- Las tareas pueden tener subtareas. Esto añade un nuevo caso: Si una tarea A tiene una subtarea en estado bloqueante, la tarea A no puede finalizar hasta que dicha subtarea deje de estar bloqueante.
- Añade los campos tiempo estimado y tiempo dedicado a las tareas. Como existen tareas y subtareas, el tiempo estimado y el tiempo dedicado de la tarea se define a partir de las subtareas.

Incluye al menos 5 tests que comprueben estas nuevas características (SOLO deben ser tests unitarios). Mantén la arquitectura y las tecnologías ya presentes en el proyecto.
```

Una vez generado el código, se añaden los cambios al staging area con ``git add -A`` y se realiza el commit.

```cmd
git commit -m "feat(tasks): añadir subtareas y campos tiempo estimado y tiempo dedicado"
```

Para integrar estos cambios en la rama principal, se realiza un merge de ``feat/subtasks`` a ``main``:

```cmd
git switch main
Switched to branch 'main'

git merge feat/subtasks
#Updating b02151b..58683d0
#Fast-forward
 .../java/com/example/taskmanager/domain/Task.java  | 106 ++++++++++++++++-
 .../com/example/taskmanager/dto/TaskRequest.java   |  13 ++-
 .../com/example/taskmanager/dto/TaskResponse.java  |  21 +++-
 .../taskmanager/service/impl/TaskServiceImpl.java  |  11 ++
 .../com/example/taskmanager/domain/TaskTest.java   | 126 +++++++++++++++++++++
 .../service/impl/TaskServiceImplTest.java          |  10 +-
 6 files changed, 274 insertions(+), 13 deletions(-)

git log --oneline --graph --all
* 58683d0 (HEAD -> main, feat/subtasks) feat(tasks): añadir subtareas y campos tiempo estimado y tiempo dedicado
* b02151b chore(editor): añadir .editorconfig
* 8ba1eb1 chore: proyecto Maven inicial de la API de tareas

```

### Parte E — Formateo automático en cada commit

Se añade al `pom.xml` el plugin Spotless para que el proyecto tenga un único estilo y se pueda comprobar automáticamente:

```xml
<plugin>
  <groupId>com.diffplug.spotless</groupId>
  <artifactId>spotless-maven-plugin</artifactId>
  <version>2.43.0</version>
  <configuration>
    <java>
      <googleJavaFormat/>
      <removeUnusedImports/>
      <trimTrailingWhitespace/>
      <endWithNewline/>
    </java>
  </configuration>
</plugin>
```
Para comprobar que el estilo sea el correcto, se ejecuta ``mvn spotless:check``. En caso de que falle, la build falla y se indica el número de archivos que no cumplen con el estilo. 

```cmd
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  14.683 s
[INFO] Finished at: 2026-09-28T22:15:59+02:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal com.diffplug.spotless:spotless-maven-plugin:2.43.0:check (default-cli) on project task-manager: The following files had format violations:
[ERROR]     src\main\java\com\example\taskmanager\config\ApplicationConfig.java
[ERROR]         @@ -1,19 +1,18 @@
[ERROR]          package·com.example.taskmanager.config;
[ERROR]
[ERROR]         +import·java.time.Clock;
[ERROR]          import·org.springframework.context.annotation.Bean;
[ERROR]          import·org.springframework.context.annotation.Configuration;
[ERROR]
[ERROR]         -import·java.time.Clock;
[ERROR]         -
[ERROR]          /**
[ERROR]         -·*·Beans·de·infraestructura.·Se·expone·{@link·Clock}·como·bean·para·que·la·capa
[ERROR]         -·*·de·servicio·dependa·de·una·abstraccion·del·tiempo·y·sea·testeable.
[ERROR]         +·*·Beans·de·infraestructura.·Se·expone·{@link·Clock}·como·bean·para·que·la·capa·de·servicio·dependa
[ERROR]         +·*·de·una·abstraccion·del·tiempo·y·sea·testeable.
[ERROR]          ·*/
[ERROR]          @Configuration
[ERROR]          public·class·ApplicationConfig·{
[ERROR]
[ERROR]         -····@Bean
[ERROR]         -····public·Clock·clock()·{
[ERROR]         -········return·Clock.systemDefaultZone();
[ERROR]         -····}
[ERROR]         +··@Bean
[ERROR]         +··public·Clock·clock()·{
[ERROR]         +····return·Clock.systemDefaultZone();
[ERROR]         +··}
[ERROR]          }
[ERROR]     src\main\java\com\example\taskmanager\controller\TaskController.java
[ERROR]         @@ -7,6 +7,8 @@
[ERROR]          import·com.example.taskmanager.dto.TaskStatusUpdateRequest;
[ERROR]          import·com.example.taskmanager.service.TaskService;
[ERROR]          import·jakarta.validation.Valid;
[ERROR]         +import·java.net.URI;
[ERROR]         +import·java.util.List;
[ERROR]          import·org.springframework.http.HttpStatus;
[ERROR]          import·org.springframework.http.ResponseEntity;
[ERROR]          import·org.springframework.web.bind.annotation.DeleteMapping;
[ERROR]         @@ -22,58 +24,54 @@
[ERROR]          import·org.springframework.web.bind.annotation.RestController;
[ERROR]          import·org.springframework.web.servlet.support.ServletUriComponentsBuilder;
[ERROR]
[ERROR]         -import·java.net.URI;
[ERROR]         -import·java.util.List;
[ERROR]         -
[ERROR]         -/**
[ERROR]         -·*·API·REST·de·gestion·de·tareas.
[ERROR]         -·*/
[ERROR]         +/**·API·REST·de·gestion·de·tareas.·*/
[ERROR]          @RestController
[ERROR]     ... (86 more lines that didn't fit)
[ERROR] Violations also present in 16 other files.
[ERROR] Run 'mvn spotless:apply' to fix these violations.
[ERROR] -> [Help 1]
[ERROR]

```

Para arreglarlo, se ejecuta ``mvn spotless:apply``:

```cmd

INFO] ----------------------< com.example:task-manager >----------------------
[INFO] Building task-manager 1.0.0
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- spotless:2.43.0:apply (default-cli) @ task-manager ---
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\main\java\com\example\taskmanager\config\ApplicationConfig.java
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\main\java\com\example\taskmanager\controller\TaskController.java
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\main\java\com\example\taskmanager\domain\Task.java
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\main\java\com\example\taskmanager\domain\TaskPriority.java
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\main\java\com\example\taskmanager\domain\TaskStatus.java
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\main\java\com\example\taskmanager\dto\TaskRequest.java
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\main\java\com\example\taskmanager\dto\TaskResponse.java
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\main\java\com\example\taskmanager\dto\TaskStatusUpdateRequest.java
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\main\java\com\example\taskmanager\exception\BusinessRuleException.java
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\main\java\com\example\taskmanager\exception\ErrorResponse.java
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\main\java\com\example\taskmanager\exception\GlobalExceptionHandler.java
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\main\java\com\example\taskmanager\exception\TaskNotFoundException.java
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\main\java\com\example\taskmanager\repository\TaskRepository.java
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\main\java\com\example\taskmanager\service\impl\TaskServiceImpl.java
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\main\java\com\example\taskmanager\service\TaskService.java
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\main\java\com\example\taskmanager\TaskManagerApplication.java
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\test\java\com\example\taskmanager\domain\TaskTest.java
[INFO] Writing clean file: C:\Users\jesus\OneDrive\Belgeler\misum\proyecto\src\test\java\com\example\taskmanager\service\impl\TaskServiceImplTest.java
[INFO] Spotless.Java is keeping 18 files clean - 18 were changed to be clean, 0 were already clean, 0 were skipped because caching determined they were already clean
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.784 s
[INFO] Finished at: 2026-09-28T22:19:23+02:00
[INFO] ------------------------------------------------------------------------

```

Para que estos dos comandos se ejecuten antes de cada commit de manera automática, se utilizan los hooks. Un hook es un script que Git ejecuta automáticamente cuando ocurre un evento (por ejemplo, un commit o push).


```cmd
# Crea el hook (versionado, para que tus compañeros lo tengan igual)
mkdir .githooks

echo #!/bin/sh > ".githooks\pre-commit"
echo echo ^"Formatting with Spotless...^" >> ".githooks\pre-commit"
echo mvn -q spotless:apply ^|^| exit 1 >> ".githooks\pre-commit"
echo git add -u >> ".githooks\pre-commit"

# Indica a Git que use esa carpeta de hooks
git config core.hooksPath .githooks
```

Una vez configurado, se hace un commit tanto del ``pom.xml`` modificado como del nuevo archivo ``.githooks/pre-commit``:

```cmd
git add pom.xml 
git commit -m "chore(build): añade Spotless para formateo automático"

git add .githooks/pre-commit
git commit -m "chore(hooks): añade hook de pre-commit con Spotless"
```

Para comprobar que verdaderamente se aplica este hook, se desordena la indentación de un fichero Java (``TaskRequest.java``) y se hace el siguiente commit:

```java
package com.example.taskmanager.dto;

import com.example.taskmanager.domain.TaskPriority;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/** Payload de entrada para crear o actualizar una tarea. */
public record TaskRequest(
@NotBlank(message = "El titulo es obligatorio")
@Size(min = 3, max = 120, message = "El titulo debe tener entre 3 y 120 caracteres")
String title,
@Size(max = 2000, message = "La descripcion no puede superar los 2000 caracteres")
String description,
@NotNull(message = "La prioridad es obligatoria") TaskPriority priority,
LocalDate dueDate,
@Min(value = 0, message = "El tiempo estimado no puede ser negativo") Integer estimatedTime,
@Min(value = 0, message = "El tiempo dedicado no puede ser negativo") Integer dedicatedTime,
Long parentId) {}
```

```cmd
git add src/main/java/com/example/taskmanager/dto/TaskRequest.java
git commit -m "refactor(dto): formeatear indentación"
Formatting with Spotless...
[main 9158090] refactor(dto): formeatear indentación
```
Si se vuelve a acceder a ``TaskRequest``:

```java
package com.example.taskmanager.dto;

import com.example.taskmanager.domain.TaskPriority;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/** Payload de entrada para crear o actualizar una tarea. */
public record TaskRequest(
    @NotBlank(message = "El titulo es obligatorio")
        @Size(min = 3, max = 120, message = "El titulo debe tener entre 3 y 120 caracteres")
        String title,
    @Size(max = 2000, message = "La descripcion no puede superar los 2000 caracteres")
        String description,
    @NotNull(message = "La prioridad es obligatoria") TaskPriority priority,
    LocalDate dueDate,
    @Min(value = 0, message = "El tiempo estimado no puede ser negativo") Integer estimatedTime,
    @Min(value = 0, message = "El tiempo dedicado no puede ser negativo") Integer dedicatedTime,
    Long parentId) {}

```

> Es **muy importante** tener en cuenta que **.git/hooks/ no se versiona**. Por ello, se guarda en ``.githooks``, que sí viaja con el repositorio y así todos los compañeros lo tienen. No obstante, la configuración ``core.hooksPath`` es local: **cada persona que haga un ``git clone`` debe ejecutar ``git config core.hooksPath .githooks`` una vez** para que Git use esa carpeta. Además, aunque el repositorio tenga el hook, es posible saltarse todos los hooks con ``git commit --no-verify``, por lo que un hook local nunca sustituye a la comprobación en CI.
> 

### Parte F — Provocar y resolver un conflicto

Para finalizar, al realizar un merge hacia main pueden ocurrir conflictos (líneas de un archivo que ambas ramas modifican). Se crean dos ramas (ambas desde main) que modifican el método ``validateCriticalRequiresDueDate`` de ``TaskServiceImpl`` y suben un commit:

#### Rama A - feat/require-due-date-high

```java
 private void validateCriticalRequiresDueDate(TaskPriority priority, LocalDate dueDate) {
    if ((priority == TaskPriority.CRITICAL || priority == TaskPriority.HIGH) && dueDate == null) {
      throw new BusinessRuleException("Una tarea de prioridad CRITICAL o HIGH requiere fecha limite");
    }
  }
```

```cmd
git commit -m "feat(service): tareas con prioridad crítica o alta requieren fecha límite"
Formatting with Spotless...
[feat/require-due-date-high 21859a8] feat(service): tareas con prioridad crítica o alta requieren fecha límite
 1 file changed, 3 insertions(+), 2 deletions(-)
```

#### Rama B - feat/due-date-must-be-future

```java
 private void validateCriticalRequiresDueDate(TaskPriority priority, LocalDate dueDate) {
    if (priority == TaskPriority.CRITICAL && (dueDate == null || !dueDate.isAfter(today()))) {
      throw new BusinessRuleException("Una tarea CRITICAL requiere fecha limite futura");
    }
  }
```

```cmd
git commit -m "feat(service): tareas críticas requieren fecha límite futura"
Formatting with Spotless...
[feat/due-date-must-be-future 75bc3c6] feat(service): tareas críticas requieren fecha límite futura
 1 file changed, 2 insertions(+), 2 deletions(-)
```

Una vez se ha hecho el commit en ambas ramas, se mergea la primera rama a main:

```cmd
git switch main
Switched to branch 'main'

git merge feat/require-due-date-high
Updating 9158090..21859a8
Fast-forward
 .../java/com/example/taskmanager/service/impl/TaskServiceImpl.java   | 5 +++--
 1 file changed, 3 insertions(+), 2 deletions(-)

git log --oneline --graph --all
* 75bc3c6 (feat/due-date-must-be-future) feat(service): tareas críticas requieren fecha límite futura
| * 21859a8 (HEAD -> main, feat/require-due-date-high) feat(service): tareas con prioridad crítica o alta requieren fecha límite
|/
* 9158090 refactor(dto): formeatear indentación
* 951368f chore(hooks): añade hook de pre-commit con Spotless
* 0c839a4 chore(build): añade Spotless para formateo automático
* 58683d0 (feat/subtasks) feat(tasks): añadir subtareas y campos tiempo estimado y tiempo dedicado
* b02151b chore(editor): añadir .editorconfig
* 8ba1eb1 chore: proyecto Maven inicial de la API de tareas

git status
On branch main
nothing to commit, working tree clean

git merge feat/due-date-must-be-future
Auto-merging src/main/java/com/example/taskmanager/service/impl/TaskServiceImpl.java
CONFLICT (content): Merge conflict in src/main/java/com/example/taskmanager/service/impl/TaskServiceImpl.java
Automatic merge failed; fix conflicts and then commit the result.
```

Al haber un conflicto, Git detiene el merge y marca las zonas conflictivas en el propio archivo con los siguientes marcadores:


- `<<<<<<< HEAD` → versión actual de ``main`` (los cambios traídos de ``feat/require-due-date-high``).
- `=======` → separa las dos versiones en conflicto.
- `>>>>>>> feat/due-date-must-be-future` → los cambios entrantes (``feat/due-date-must-be-future``).

```java
  private void validateCriticalRequiresDueDate(TaskPriority priority, LocalDate dueDate) {
<<<<<<< HEAD
    if ((priority == TaskPriority.CRITICAL || priority == TaskPriority.HIGH) && dueDate == null) {
      throw new BusinessRuleException(
          "Una tarea de prioridad CRITICAL o HIGH requiere fecha limite");
=======
    if (priority == TaskPriority.CRITICAL && (dueDate == null || !dueDate.isAfter(today()))) {
      throw new BusinessRuleException("Una tarea CRITICAL requiere fecha limite futura");
>>>>>>> feat/due-date-must-be-future
    }
  }
```

Para poder continuar con el merge, es necesario resolver los conflictos manualmente, quedándose con una versión final:

```java
 private void validateCriticalRequiresDueDate(TaskPriority priority, LocalDate dueDate) {
    if (dueDate == null && (priority == TaskPriority.CRITICAL || priority == TaskPriority.HIGH)) {
      throw new BusinessRuleException("Una tarea de prioridad CRITICAL o HIGH requiere fecha limite");
    }
    if (priority == TaskPriority.CRITICAL && !dueDate.isAfter(today())) {
      throw new BusinessRuleException("Una tarea CRITICAL requiere fecha limite futura");
    }
  }
```

Solucionado el conflicto, se añade a la staging area y se sube un commit con la versión final:

```cmd
git add src/main/java/com/example/taskmanager/service/impl/TaskServiceImpl.java
git commit -m "merge: resolver conflicto en regla de fecha límite"
```


