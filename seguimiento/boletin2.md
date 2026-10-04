# Boletín 2

## Resumen de commits

| # | Mensaje del commit | Qué se hizo | Autor | Enlace |
|---|--------------------|-------------|-------|--------|
| 1 | `docs(repo): añade README, plantillas, codeowners y guía de contribución` | Creación del README principal, plantillas en `.github/` para estandarizar PRs e Issues, asignación de Code Owners y redacción del CONTRIBUTING.md. | Jiahui | 8545ae044bdc4304c3a04260fae51147e8321718 |
| 2 | `feat(tasks): añade endpoint de estadísticas globales` | Creación del endpoint GET /stats con su DTO, lógica de cálculo en el servicio, consultas en el repositorio y tests con Mockito. | Jiahui | d66c570138d390f659f797f871a2b144b7ef2e0e |
| 3 | `refactor(tasks): simplifica los imports en el repositorio` | Refactorización de los imports en el repositorio, aplicando la sugerencia recibida en la revisión de código. | Jiahui | 376ce746d69d1837952b3a148962bf2c7c2b8086 |

## Detalle por commit

### 1. `docs(repo): añade README, plantillas, codeowners y guía de contribución`

- **Qué se hizo:** Se redactó el `README.md` con las instrucciones de uso y hooks. Además, se añadieron los archivos `.github/PULL_REQUEST_TEMPLATE.md`, `.github/ISSUE_TEMPLATE/bug.md`, `.github/ISSUE_TEMPLATE/enhancement.md` y `.github/CODEOWNERS` para automatizar las revisiones, junto con el archivo `CONTRIBUTING.md` para definir el GitHub Flow.
- **Autor:** Jiahui
- **Relación con el boletín:** Parte A (Publicar el repositorio) y Parte B (Gobernanza del repositorio).
- **Uso de IA:** Gemini. Prompts utilizados: "revisa que el README sea completo y genera las plantillas de la parte B"

### 2. `feat(tasks): añade endpoint de estadísticas globales`

- **Qué se hizo:** Se implementó una nueva funcionalidad no trivial desde una rama de feature (`feat/estadisticas-tareas`). Se incluyó un nuevo DTO (`TaskStats`), queries para contar tareas por estado en `TaskRepository`, la lógica matemática en `TaskService` y un test unitario mockeando el repositorio para validar el cálculo.
- **Autor:** Jiahui
- **Relación con el boletín:** Parte D (Trabajo por Pull Requests) - Primer ciclo de PR implementando una nueva funcionalidad.
- **Uso de IA:** Gemini. Se le pidió ideas para una mejora no trivial que no generara conflictos con los trabajos de los compañeros.

### 3. `refactor(tasks): simplifica los imports en el repositorio`

- **Qué se hizo:** Se actualizaron los imports en `TaskRepository.java` para utilizar nombres simples de clases en lugar de las rutas absolutas de los paquetes, mejorando la legibilidad.
- **Autor:** Jiahui
- **Relación con el boletín:** Parte D (Trabajo por Pull Requests) - Iteración sobre la rama de feature para corregir el código tras el *Code Review* de mi compañero.
- **Uso de IA:** No

## Descripción narrativa de lo hecho en la sesión y relación con los commits

### Parte A y B — Publicación y Gobernanza del repositorio

Iniciamos el trabajo documentando el proyecto con un `README.md` completo que explica cómo compilar y arrancar la API, además de detallar cómo activar los hooks de Git en local. Para dejar el repositorio preparado para la colaboración del equipo antes de abrir el primer Pull Request, configuramos varias herramientas de gobernanza en GitHub. Añadimos plantillas para asegurar que todos los PRs e Issues (bugs y nuevas funcionalidades) sigan una estructura común. Creamos un archivo `CODEOWNERS` para forzar que los cambios en el código sean revisados por los miembros del equipo y documentamos las normas de trabajo y la estrategia de fusión en `CONTRIBUTING.md`. Todo esto se consolidó en un primer commit para sentar las bases del repositorio.

### Parte D — Trabajo por Pull Requests

Para cubrir el flujo de GitHub Flow de esta parte, realizamos dos ciclos completos de Pull Requests (PR) alternando los roles de desarrollador y revisor.

**Ciclo 1: Yo (Jiahui) como desarrollador y @whoisasu como revisor:**

1. **Issue:** Abrí el Issue `#3` utilizando la plantilla de *enhancement*, detallando los criterios de aceptación para un nuevo endpoint de estadísticas globales (`GET /api/tasks/stats`).
2. **Rama:** Creé la rama local `feat/estadisticas-tareas` partiendo de `main`.
3. **Desarrollo y Tests:** Implementé el DTO, las queries en el repositorio, la lógica en el servicio y añadí los tests unitarios con Mockito. Formateé con Spotless mediante el pre-commit hook y subí la rama.
4. **Pull Request:** Abrí el PR usando la plantilla del proyecto e incluí `Closes #2` para enlazar el Issue. @whoisasu fue asignado automáticamente gracias al archivo `CODEOWNERS`.
5. **Revisión de Código:** @whoisasu revisó el PR y solicitó un cambio estilístico de `TaskStatus` y `LocalDate` a imports estáticos/simples en la cabecera del repositorio.
6. **Resolución** Subí un nuevo commit refactorizando los imports.

![captura del issue](seguimiento/capturas/JH-b2-D(1).png)

![captura del pull request](image.png)

