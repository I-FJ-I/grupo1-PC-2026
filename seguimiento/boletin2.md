# Boletín 2

## Resumen de commits

| # | Mensaje del commit | Qué se hizo | Autor | Enlace |
|---|--------------------|-------------|-------|--------|
| 1 | `docs(repo): añade README, plantillas, codeowners y guía de contribución` | Creación del README principal, plantillas en `.github/` para estandarizar PRs e Issues, asignación de Code Owners y redacción del CONTRIBUTING.md. | Jiahui | 8545ae044bdc4304c3a04260fae51147e8321718 |
| 2 | `feat(tasks): añade endpoint de estadísticas` | Creación del endpoint GET /api/tasks/stats con su DTO, lógica de cálculo en el servicio, consultas en el repositorio y tests con Mockito. | Jiahui | d66c570138d390f659f797f871a2b144b7ef2e0e |
| 3 | `refactor(tasks): simplifica los imports en el repositorio` | Refactorización de los imports en el repositorio, aplicando la sugerencia recibida en la revisión de código. | Jiahui | 376ce746d69d1837952b3a148962bf2c7c2b8086 |
| 4 | `feat(tasks): añadir filtros de búsqueda` | Ampliación de `GET /api/tasks` con filtros combinables (`status`, `priority`, `dueDate`, `title`) mediante `Specification`, manejo del error 400 y tests unitarios. | Jesús | 3a06b56adfa09e4a560c32c51ce490fb5fb2897c |
| 5 | `feat(tasks): añadir paginación al listado de tareas` | Adaptación del listado `GET /api/tasks` para usar `Pageable` y devolver `Page<Task>` con `@PageableDefault(size = 20)`. | Fran | 0a771d5a709cd0be8b212705f2f1d74506e7394f |
| 6 | `feat(tasks): añadir test para probar la paginación al listado de tareas` | Añadido un test en el servicio para validar la paginación, tras la petición de cambios de la revisión. | Fran | 030fb159a16cdaea255d0283d66aa709dee0b38b |
| 7 | `feat(tasks): añadir endpoint PATCH para actualizar el estado de una tarea` | Implementación del endpoint `PATCH /api/tasks/{id}/status` para actualizar solo el estado de una tarea, con su DTO `TaskStatusUpdate` y tests unitarios. | Gloria | c568d406856095f61d8dcc5e72a1d8a455923828 |

## Detalle por commit

### 1. `docs(repo): añade README, plantillas, codeowners y guía de contribución`

- **Qué se hizo:** Se redactó el `README.md` con las instrucciones de uso y hooks. Además, se añadieron los archivos `.github/PULL_REQUEST_TEMPLATE.md`, `.github/ISSUE_TEMPLATE/bug.md`, `.github/ISSUE_TEMPLATE/enhancement.md` y `.github/CODEOWNERS` para automatizar las revisiones, junto con el archivo `CONTRIBUTING.md` para definir el GitHub Flow.
- **Autor:** Jiahui
- **Relación con el boletín:** Parte A (Publicar el repositorio) y Parte B (Gobernanza del repositorio).
- **Uso de IA:** Gemini. Prompts utilizados: "revisa que el README sea completo y genera las plantillas de la parte B"

### 2. `feat(tasks): añade endpoint de estadísticas`

- **Qué se hizo:** Se implementó una nueva funcionalidad no trivial desde una rama de feature (`feat/estadisticas-tareas`). Se incluyó un nuevo DTO (`TaskStats`), queries para contar tareas por estado en `TaskRepository`, la lógica matemática en `TaskService` y un test unitario mockeando el repositorio para validar el cálculo.
- **Autor:** Jiahui
- **Relación con el boletín:** Parte D (Trabajo por Pull Requests) - Primer ciclo de PR implementando una nueva funcionalidad.
- **Uso de IA:** Gemini. Se le pidió ideas para una mejora no trivial que no generara conflictos con los trabajos de los compañeros.

### 3. `refactor(tasks): simplifica los imports en el repositorio`

- **Qué se hizo:** Se actualizaron los imports en `TaskRepository.java` para utilizar nombres simples de clases en lugar de las rutas absolutas de los paquetes, mejorando la legibilidad.
- **Autor:** Jiahui
- **Relación con el boletín:** Parte D (Trabajo por Pull Requests) - Iteración sobre la rama de feature para corregir el código tras el *Code Review* de mi compañero.
- **Uso de IA:** No

### 4. `feat(tasks): añadir filtros de búsqueda`

- **Qué se hizo:** Desde la rama `feat/filtrado-busqueda-tareas` se amplía `GET /api/tasks` con los filtros combinables `status`, `priority`, `dueDate` y `title`, usando `Specification` de Spring Data. Se maneja el error 400 ante parámetros inválidos y se añaden tests de repositorio (`@DataJpaTest`) y de servicio (Mockito).
- **Autor:** Jesús
- **Relación con el boletín:** Parte D (Trabajo por Pull Requests) - Segundo ciclo de PR, conmigo como desarrollador y Jiahui como revisora.
- **Uso de IA:** OpenCode con el modelo DeepSeek V4 Flash a partir del Issue #1.

### 5. `feat(tasks): añadir paginación al listado de tareas`

- **Qué se hizo:** Se modifica `TaskController` y `TaskService` para recibir un `Pageable` de Spring Data y devolver `Page<Task>` en lugar de `List<Task>`, fijando un tamaño de página por defecto de 20 con `@PageableDefault(size = 20)`.
- **Autor:** Fran
- **Relación con el boletín:** Parte D (Trabajo por Pull Requests) - Tercer ciclo de PR implementando una nueva funcionalidad.

### 6. `feat(tasks): añadir test para probar la paginación al listado de tareas`

- **Qué se hizo:** Se añade un test en `TaskServiceTest` para comprobar que el servicio procesa correctamente la paginación, después de que la revisión solicitara los tests que faltaban.
- **Autor:** Fran
- **Relación con el boletín:** Parte D (Trabajo por Pull Requests) - Iteración del tercer ciclo para completar el código tras el *Code Review*.

### 7. `feat(tasks): añadir endpoint PATCH para actualizar el estado de una tarea`

- **Qué se hizo:** Se implementa el endpoint `PATCH /api/tasks/{id}/status` para actualizar únicamente el estado de una tarea sin enviar la entidad completa. Se crea el DTO `TaskStatusUpdate` con el campo `status` obligatorio, se añade el método `updateTaskStatus` en el servicio (que lanza `ResourceNotFoundException` si la tarea no existe) y el endpoint correspondiente en el controlador. Se incluyen tests unitarios que validan tanto la actualización correcta como el caso de tarea inexistente.
- **Autor:** Gloria
- **Relación con el boletín:** Parte D (Trabajo por Pull Requests) - Contribución al repositorio mediante PR desde fork (Parte F del boletín).

## Descripción narrativa de lo hecho en la sesión y relación con los commits

### Parte A — Publicar el repositorio

En primer lugar se crea en GitHub un repositorio **nuevo**, **público** y **vacío** (sin README, para no generar conflictos al conectar el proyecto local). El creador del repositorio da de alta como colaboradores al resto de integrantes para que puedan hacer push. Una vez creado, se conecta el repositorio local con el remoto y se sube la rama principal:

```cmd
git remote add origin https://github.com/I-FJ-I/grupo1-PC-2026.git
git branch -M main
git push -u origin main
```

A continuación se completa el `README.md`, que es el punto de entrada para cualquiera que llegue al repositorio y quiera ponerlo en marcha desde cero. En él se documentan:

- Los **requisitos previos**: Java 21 (JDK 21), Maven (3.8 o superior) y Git.
- La **configuración del entorno y los hooks**, que en este proyecto son **obligatorios** para que Spotless formatee el código antes de cada commit:

```cmd
git config core.hooksPath .githooks
```

- **Cómo construir** el proyecto y ejecutar los tests, generando el JAR ejecutable:

```cmd
mvn clean package
# genera target/task-api-1.0.0.jar
```

- **Cómo arrancar** la aplicación, que usa una base de datos H2 en memoria y no necesita servicios externos:

```cmd
java -jar target/task-api-1.0.0.jar
# API disponible en http://localhost:8080/api/tasks
```

- La lista de **endpoints** (`GET`, `POST`, `PUT`, `PATCH` y `DELETE`) y una sección de colaboración que remite a `CONTRIBUTING.md`.

### Parte B — Gobernanza del repositorio

Antes de abrir el primer PR se deja el repositorio preparado para que colaborar sea fácil. Se añaden las siguientes piezas de gobernanza:

**Plantilla de Pull Request** (`.github/PULL_REQUEST_TEMPLATE.md`), que se carga sola al abrir cada PR y fuerza a indicar qué hace, cómo se ha probado y las notas para quien revisa:

```
## Qué hace este PR

Closes #

## Cómo lo he probado

- [ ] Tests unitarios nuevos o actualizados
- [ ] Probado manualmente con curl / navegador

## Notas para quien revise

<!-- decisiones discutibles, alternativas descartadas, dudas -->
```

**Plantillas de Issue** (`.github/ISSUE_TEMPLATE/`), una para *enhancement* y otra para *bug*. Las plantillas llevan un bloque YAML de cabecera con el nombre, la descripción, el prefijo del título y la etiqueta por defecto, de forma que GitHub rellena parte del Issue automáticamente:

```markdown
---
name: Nueva funcionalidad
about: Sugiere una mejora o un nuevo endpoint para el proyecto
title: '[FEAT] '
labels: enhancement
assignees: ''
---
```

**Propietarios del código** (`.github/CODEOWNERS`), que hacen que GitHub solicite automáticamente la revisión a las personas indicadas cuando un PR toca los archivos que controlan. En este caso todo el repositorio lo revisa el equipo:

```
# Toda la aplicación la revisa el equipo por defecto
*           @JHLumu @I-FJ-I @whoisasu
```

**Estrategia de fusión**: se escoge **Squash and merge**, de manera que cada PR entra en `main` como un único commit con significado y el historial queda lineal. Esta decisión queda documentada en el `CONTRIBUTING.md`.

**Guía de contribución** (`CONTRIBUTING.md`), que reúne todo lo que necesita saber alguien que quiera contribuir: la configuración del entorno y los hooks, el flujo de trabajo basado en GitHub Flow (rama descriptiva → PR → revisión de un Code Owner) y la política de fusión:

```markdown
## Flujo de trabajo (GitHub Flow)

1. Asegúrate de que `main` está en verde (compila y pasa los tests).
2. Crea una rama descriptiva a partir de `main` (ej: `feat/nuevo-endpoint`, `fix/error-404`).
3. Realiza tus cambios asegurándote de seguir los Conventional Commits.
4. Sube la rama y abre un **Pull Request**.
5. Rellena la plantilla del PR y enlaza el Issue correspondiente.
6. Espera la revisión de al menos un Code Owner.
```

Con todo esto listo se hace el commit y se sube:

```cmd
git add README.md CONTRIBUTING.md .github
git commit -m "docs(repo): añade README, plantillas, codeowners y guía de contribución"
git push
```

Al llegar a GitHub se comprueba que reconoce las plantillas (aparecen al crear un Issue o un PR) y los propietarios de código (se solicitan sus revisiones solas).

### Parte C — Proteger la rama main

En **Settings → Rules → Rulesets** se crea una regla llamada `Main` que se aplica a la rama por defecto del repositorio (`~DEFAULT_BRANCH`, es decir, `main`). La regla queda activa y con las siguientes condiciones:

| Ajuste | Valor |
|---|---|
| Require a pull request before merging | Sí |
| Require at least 1 approval | Sí |
| Require review from Code Owners | Sí |
| Require conversation resolution before merging | Sí |
| Dismiss stale pull request approvals when new commits are pushed | Sí |
| Do not allow bypassing the above settings | Sí (sin actores de excepción) |
| Allow force pushes | No |
| Allow deletions | No |

Con estas reglas se consigue que **nadie pueda escribir directamente en `main`**: todo cambio tiene que pasar por un PR, revisado por al menos una persona y por un propietario del código, con las conversaciones resueltas. Además, si se sube un commit nuevo a un PR ya aprobado, la aprobación anterior se descarta automáticamente, y ni siquiera quien administra el repositorio puede saltarse la regla. Por último, se prohíben los *force push* y el borrado de `main`, para que el historial de la rama principal no se pueda reescribir ni eliminar por accidente.

Para comprobar que la protección funciona de verdad, se intenta hacer un push directo a `main`:

```cmd
git switch main
echo "prueba" >> README.md
git commit -am "chore: intento de push directo"
git push
```

El servidor rechaza el push porque la rama está protegida por la regla:

```
remote: error: GH013: Repository rule violations found for refs/heads/main.
remote:
remote: - Changes must be made through a pull request.
remote:
! [remote rejected] main -> main (push declined due to repository rule violations)
error: failed to push some refs to 'https://github.com/I-FJ-I/grupo1-PC-2026.git'
```

Puesto que el commit no ha llegado al remoto, se deshace en local para dejar la rama limpia:

```cmd
git reset --hard origin/main
```

### Parte D — Trabajo por Pull Requests

Se realizan tres ciclos completos de Pull Request (PR) alternando los roles de desarrollador y revisor. En el primer ciclo Jiahui desarrolla el endpoint de estadísticas y Jesús lo revisa; en el segundo los papeles se intercambian y Jesús desarrolla el filtrado y la búsqueda de tareas mientras Jiahui revisa; y en el tercero Fran desarrolla la paginación del listado.

**Ciclo 1: Jiahui como desarrolladora y Jesús como revisor:**

1. **Issue:** Se abre el Issue `#3` con la plantilla de *enhancement*, detallando los criterios de aceptación para un nuevo endpoint de estadísticas globales (`GET /api/tasks/stats`).
2. **Rama:** Se crea la rama local `feat/estadisticas-tareas` partiendo de `main`.
3. **Desarrollo y Tests:** Se implementa el DTO, las queries en el repositorio, la lógica en el servicio y los tests unitarios con Mockito. Se formatea con Spotless mediante el pre-commit hook y se sube la rama.
4. **Pull Request:** Se abre el PR usando la plantilla del proyecto e incluyendo `Closes #2` para enlazar el Issue. Jesús se asigna automáticamente al especificarse en `CODEOWNERS`.
5. **Revisión de Código:** Jesús revisa el PR y solicita un cambio estilístico de `TaskStatus` y `LocalDate` a imports simples en la cabecera del repositorio.
6. **Resolución:** Se sube un nuevo commit refactorizando los imports, con lo que el PR queda listo para fusionar.

![captura del issue](capturas/JH-b2-D(1).png)

![captura del pull request](capturas/JH-n2-D(2).png)

**Ciclo 2: Jesús como desarrollador y Jiahui como revisor:**

1. **Issue:** Se abre el Issue `#1` con la plantilla de *enhancement*, detallando los filtros combinables que debe aceptar `GET /api/tasks` (`status`, `priority`, `dueDate` y `title`) y sus criterios de aceptación.
2. **Rama:** Se crea la rama local `feat/filtrado-busqueda-tareas` partiendo de `main`.
3. **Desarrollo y Tests:** Se implementan los filtros con `Specification` de Spring Data, se añade el manejo del error 400 para parámetros inválidos y los tests correspondientes. Se formatea con Spotless mediante el pre-commit hook y se sube la rama.
4. **Pull Request:** Se abre el PR #4 usando la plantilla del proyecto e incluyendo `Closes #1` para enlazar el Issue. Jiahui y Fran quedan como revisores.
5. **Revisión de Código:** Tras la primera aprobación, como la rama se creó antes de fusionarse el PR de estadísticas, se actualiza con `git rebase origin/main` y `git push --force-with-lease`. Esto descarta las aprobaciones previas y Jiahui vuelve a revisar la versión final.
6. **Merge:** Con el PR aprobado se fusiona a `main` con la estrategia elegida y después se borra la rama.

**Ciclo 3: Fran como desarrollador y Jesús y Jiahui como revisores:**

1. **Issue:** Se abre el Issue `#5` con la plantilla de *enhancement*, detallando los criterios de aceptación para añadir paginación al listado de tareas (`GET /api/tasks`).
2. **Rama:** Se crea la rama local `feat/paginacion` partiendo de `main`.
3. **Desarrollo y Tests:** Se modifica el controlador y el servicio para recibir un `Pageable` y devolver `Page<Task>`, con un tamaño de página por defecto de 20, y se sube la rama.
4. **Pull Request:** Se abre el PR #6 usando la plantilla del proyecto e incluyendo `Closes #5`. Jiahui y Jesús quedan como revisores.
5. **Revisión de Código:** Jiahui solicita cambios porque faltan los tests de la paginación. Se añade el test correspondiente y se sube, descartando las aprobaciones previas.
6. **Merge:** Con el PR aprobado se fusiona a `main` y se borra la rama.

![captura del pull request de paginación](capturas/FJ-b2-D(1).png)

### Parte E — Provocar un conflicto en un PR

En este caso no hizo falta crear dos ramas artificiales: el conflicto salió solo con los dos primeros ciclos de la Parte D. La rama de Jiahui (`feat/estadisticas-tareas`) y la de Jesús (`feat/filtrado-busqueda-tareas`) partían del mismo `main` y tocaban las mismas zonas de código. Como el PR de Jiahui se fusionó primero, `main` avanzó y la segunda rama se quedó desactualizada.

| Archivo | Rama de Jiahui | Rama de Jesús |
|---|---|---|
| `TaskController` | Añade el endpoint `GET /stats` | Convierte `getAllTasks` en `getTasks` con los filtros |
| `TaskService` | Añade `getTaskStatistics` | Sustituye `getAllTasks` por `searchTasks` |
| `TaskRepository` | Añade `countByStatus` y `countByDueDateBeforeAndStatusNot` | Hereda de `JpaSpecificationExecutor` |
| `TaskServiceTest` | Añade el test de estadísticas | Añade dos tests de búsqueda |

GitHub avisa de que el PR #4 no se puede fusionar por conflictos. Para arreglarlo en local no se hace un merge, sino un **rebase** de la rama sobre `main`, de forma que los cambios se reaplican encima de los de Jiahui:

```cmd
git switch feat/filtrado-busqueda-tareas
git fetch origin
git rebase origin/main
```

El rebase reaplica los commits y se detiene al encontrar los conflictos:

```
Auto-merging src/main/java/com/example/taskapi/controller/TaskController.java
CONFLICT (content): Merge conflict in src/main/java/com/example/taskapi/repository/TaskRepository.java
CONFLICT (content): Merge conflict in src/main/java/com/example/taskapi/service/TaskService.java
CONFLICT (content): Merge conflict in src/test/java/com/example/taskapi/service/TaskServiceTest.java
error: could not apply ... feat(tasks): añadir filtros de búsqueda
```

En `TaskRepository` el conflicto estaba en la propia declaración de la interfaz, porque ambas ramas modificaron esa línea. Su resolución consistió en mantener las nuevas consultas de estadísticas y añadir la extensión de `JpaSpecificationExecutor` para los filtros.

```java
@Repository
<<<<<<< HEAD
public interface TaskRepository extends JpaRepository<Task, Long> {

  long countByStatus(TaskStatus status);

  long countByDueDateBeforeAndStatusNot(LocalDate date, TaskStatus status);
}
=======
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {}
>>>>>>> feat/filtrado-busqueda-tareas (feat(tasks): añadir filtros de búsqueda)
```

```java
@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

  long countByStatus(TaskStatus status);

  long countByDueDateBeforeAndStatusNot(LocalDate date, TaskStatus status);
}
```

En `TaskService` el conflicto era en los imports: Jiahui importó `TaskStats` y Jesús importó `TaskPriority` y `TaskSpecification`. Se mantienen todos los imports.

```java
import com.example.taskapi.model.Task;
<<<<<<< HEAD
=======
import com.example.taskapi.model.TaskPriority;
>>>>>>> feat/filtrado-busqueda-tareas (feat(tasks): añadir filtros de búsqueda)
import com.example.taskapi.model.TaskStatus;
import com.example.taskapi.repository.TaskRepository;
import com.example.taskapi.repository.TaskSpecification;
```

En `TaskServiceTest` ambos añadieron tests al final del archivo. Como son tests distintos para funcionalidades distintas, ambos se mantienen.

```java
  @Test
<<<<<<< HEAD
  void getTaskStatistics_ShouldReturnCorrectCounts() {
    ...
    assertEquals(2L, stats.overdueTasks());
=======
  void searchTasks_ShouldDelegueToRepositoryWithSpecification() {
    ...
  }

  @Test
  void searchTasks_ShouldReturnAll_WhenNoFiltersProvided() {
    ...
    verify(taskRepository, times(1)).findAll(any(Specification.class));
>>>>>>> feat/filtrado-busqueda-tareas (feat(tasks): añadir filtros de búsqueda)
  }
}
```

En `TaskController` no hizo falta resolución manual de los conflictos, git combinó automáticamente los cambios. Una vez resueltos los conflictos, se marcan todos como resueltos y se continúa el rebase:

```cmd
git add src/main/java/com/example/taskapi/repository/TaskRepository.java
git add src/main/java/com/example/taskapi/service/TaskService.java
git add src/test/java/com/example/taskapi/service/TaskServiceTest.java
git rebase --continue
```

Al terminar, la rama ya está construida sobre `main` y hay que subirla. Como el rebase reescribe los commits, el push normal se rechaza y hay que forzarlo con `--force-with-lease` (solo pisa el remoto si nadie ha subido cambios a esa rama desde el último `fetch`):

```cmd
git push --force-with-lease
```

Tras el force-push, los cambios se subieron correctamente y el conflicto desapareció de la PR, que ya podía fusionarse con main. Sin embargo, al reescribir el historial con el rebase, GitHub descartó automáticamente las aprobaciones previas de Fran y Jiahui, ya que los commits habían cambiado. Fue necesario que Jiahui revisara de nuevo el código y volviera a aprobar la PR antes de poder fusionarla.

Las siguientes capturas muestran el estado de la PR durante este proceso: primero el cuerpo de la PR con los revisores asignados, y después el timeline completo donde se aprecia cómo Fran y Jiahui aprobaron inicialmente, Jesús forzó el push descartando esas reviews, y finalmente Jiahui volvió a aprobar con el mensaje "Está perfecto, buen trabajo!".

![captura del PR #4 con el cuerpo y los revisores asignados](capturas/JS-b2-E(4).png)

![captura del timeline de la PR mostrando las aprobaciones, el force-push y la aprobación final](capturas/JS-b2-E(5).png)

#### Conflicto en la PR de Fran (paginación)

Cuando se fusionó la rama de Jesús (`feat/filtrado-busqueda-tareas`), la PR de Fran (`feat/paginacion`) pasó a tener conflictos, ya que ambos habían modificado las mismas zonas de `TaskController`, `TaskService` y `TaskServiceTest`. GitHub marcó la PR #6 como no fusionable hasta resolverlos:

![captura de GitHub mostrando conflictos en PR #6](capturas/FRAN-b2-E(1).jpeg)

Fran resolvió el conflicto en local con un rebase sobre `main`, igual que se explicó antes:

```cmd
git switch feat/paginacion
git rebase main
```

El rebase produjo conflictos en dos commits. Primero en el commit de la paginación (`e3ab398`), donde `TaskController` y `TaskService` tenían la misma línea de `getAllTasks` modificada de dos formas distintas (filtros vs paginación):

```
Auto-fusionando src/main/java/com/example/taskapi/controller/TaskController.java
CONFLICTO (contenido): Conflicto de fusión en .../TaskController.java
Auto-fusionando src/main/java/com/example/taskapi/service/TaskService.java
CONFLICTO (contenido): Conflicto de fusión en .../TaskService.java
error: no se pudo aplicar e3ab398... feat(tasks): añadir paginación al listado de tareas
```

![captura del comando git rebase main con conflictos](capturas/FRAN-b2-E(2).jpeg)

Al abrir los archivos en el editor, los marcadores de conflicto aparecen claramente. En `TaskController` la rama `HEAD` (con los filtros de Jesús) y la rama entrante (con la paginación de Fran) modifican el mismo método `getAllTasks`:

![captura del conflicto en TaskController](capturas/FRAN-b2-E(3).jpeg)

En `TaskService` ocurre lo mismo con los métodos `searchTasks` y `getAllTasks`:

![captura del conflicto en TaskService](capturas/FRAN-b2-E(4).jpeg)

Tras resolver y continuar el rebase, el segundo commit (el de los tests, `ce8e68b`) también tuvo conflicto en `TaskServiceTest`, porque ambos habían añadido tests al final del archivo:

![captura del conflicto en TaskServiceTest](capturas/FRAN-b2-E(5).jpeg)

Fran resolvió ambos, marcó los archivos como resueltos y continuó el rebase dos veces:

```cmd
git add .
git rebase --continue   # primer commit resuelto (paginación)
# ... conflicto en el segundo commit ...
git add .
git rebase --continue   # segundo commit resuelto (tests)
```

![captura de los dos rebase --continue](capturas/FRAN-b2-E(6).jpeg)

Finalmente, como el rebase reescribe el historial, subió los cambios con force-with-lease y GitHub recalculó la PR sin conflictos:

```cmd
git push origin feat/paginacion --force-with-lease
```

![captura del push final de Fran](capturas/FRAN-b2-E(7).jpeg)

### Parte F — PR con fork

Para resolver el issue [#8](https://github.com/samuelavilesc/practicas-continuas-gloriasamu/issues/8) del repositorio de Samuel y Gloria, Jiahui siguió el flujo de fork + Pull Request. El issue pedía implementar el endpoint `GET /api/tasks/overdue` para listar tareas vencidas (con `dueDate` anterior a hoy y estado distinto de `COMPLETED`).

Jiahui hizo fork del repositorio, creó la rama `feat/listar-tareas-vencidas`, implementó el endpoint siguiendo el patrón del proyecto (repositorio, servicio, controlador y test), y abrió el [PR #11](https://github.com/samuelavilesc/practicas-continuas-gloriasamu/pull/11) contra `main` del repositorio original. Gloria revisó y aprobó los cambios, y el PR se fusionó mediante squash and merge.

![captura del issue #8](capturas/JS-b2-F(1).png)

![captura del PR #11 abierto](capturas/JS-b2-F(2).png)

![captura del PR #11 mergeado](capturas/JS-b2-F(3).png)
