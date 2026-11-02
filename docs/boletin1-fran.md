# Boletín 1 - Fran

## Resumen de commits

| # | Mensaje del commit | Qué se hizo | Autor | Enlace |
|---|--------------------|-------------|-------|--------|
| 1 | `chore: proyecto Maven inicial de la API de tareas` | Creación inicial del proyecto base con Spring Boot, Maven, `.gitignore` y `.gitattributes`. | FJ | [a85c15f](#) |
| 2 | `chore(editor): añade .editorconfig` | Se añadió el archivo de configuración para unificar el estilo de indentación en diferentes editores. | FJ | [531a614](#) |
| 3 | `chore(build): añade Spotless para formateo automático` | Se configuró el plugin Spotless en el `pom.xml` y se añadió un hook de pre-commit. | FJ | [2e1a7d0](#) |
| 4 | `chore(comentario): añade el comentario de la rama 1` | Se modificó una línea de texto en una rama alternativa (`feat/rama-1`) para preparar un conflicto de fusión. | FJ | [f39b0b7](#) |
| 5 | `chore(comentario): añade el comentario de la rama 2` | Se modificó la misma línea de texto de forma distinta en otra rama paralela (`feat/rama-2`). | FJ | [14a631e](#) |
| 6 | `chore(conflicto): solucionado el conflicto de la rama 1 y 2` | Se resolvió manualmente el conflicto generado al fusionar `rama-2` sobre `main` (que ya tenía los cambios de `rama-1`). | FJ | [5246cde](#) |

## Detalle por commit

### 1. `chore: proyecto Maven inicial de la API de tareas`
- **Qué se hizo:** Se inicializó el repositorio Git local y se añadieron los archivos base generados del proyecto Maven (API REST de tareas) junto con los ficheros `.gitignore` y `.gitattributes` para asegurar compatibilidad de finales de línea entre sistemas operativos.
- **Autor:** FJ
- **Relación con el boletín:** Parte D (Control de versiones con Git).
- **Uso de IA:** Se utilizó IA para generar todo el código inicial (API REST de gestión de tareas con Spring Boot 3, Java 21, JPA y validaciones) según lo solicitado en la Parte B.

### 2. `chore(editor): añade .editorconfig`
- **Qué se hizo:** Se agregó el archivo `.editorconfig` en la raíz del proyecto para fijar el estilo de código (charset, finales de línea en `lf` e indentación con espacios) sin importar el IDE que se use.
- **Autor:** FJ
- **Relación con el boletín:** Parte D (Configuraciones base de repositorio).
- **Uso de IA:** No se ha usado (código proporcionado en las instrucciones del boletín).

### 3. `chore(build): añade Spotless para formateo automático`
- **Qué se hizo:** Inclusión del plugin `spotless-maven-plugin` en el archivo `pom.xml` y creación de un script `.githooks/pre-commit` para obligar a que todo el código esté formateado antes de permitir un commit.
- **Autor:** FJ
- **Relación con el boletín:** Parte E (Formateo automático en cada commit).
- **Uso de IA:** No se ha usado.

### 4. `chore(comentario): añade el comentario de la rama 1`
- **Qué se hizo:** Se creó una nueva rama (`feat/rama-1`) y se introdujo un cambio específico en un archivo de texto como preparación para la prueba de conflictos.
- **Autor:** FJ
- **Relación con el boletín:** Parte F (Provocar y resolver un conflicto).
- **Uso de IA:** No se ha usado.

### 5. `chore(comentario): añade el comentario de la rama 2`
- **Qué se hizo:** Partiendo de nuevo desde `main`, se creó otra rama (`feat/rama-2`) y se alteró exactamente la misma línea modificada en la rama 1.
- **Autor:** FJ
- **Relación con el boletín:** Parte F (Provocar y resolver un conflicto).
- **Uso de IA:** No se ha usado.

### 6. `chore(conflicto): solucionado el conflicto de la rama 1 y 2`
- **Qué se hizo:** Se fusionó la rama 1 en `main` sin problemas. Al intentar fusionar la rama 2, Git detectó una colisión. Se abrieron los marcadores `<<<<<<<`, `=======` y `>>>>>>>`, se eligió la versión final y se hizo este commit de *merge* para resolverlo.
- **Autor:** FJ
- **Relación con el boletín:** Parte F (Provocar y resolver un conflicto).
- **Uso de IA:** No se ha usado.

---

## Descripción narrativa de lo hecho en la sesión y relación con los commits

### Parte A — Instalación y verificación del entorno
Se verificó la correcta instalación de Git, Java (JDK 21) y Maven mediante los comandos de versión por terminal. A continuación, se procedió a configurar la identidad global de Git estableciendo nombre y correo, y aplicando la directiva `pull.ff only` para evitar *merge commits* automáticos indeseados al sincronizar repositorios, además de establecer `main` como nombre por defecto de la rama principal.

### Parte B — Generar la aplicación con ayuda de IA
Se utilizó un LLM para generar completamente el código de una API REST orientada a la gestión de tareas (To-Do). El resultado incluyó un proyecto Maven, una entidad `Task` con persistencia H2 (en memoria), un patrón de diseño en capas (Controlador, Servicio y Repositorio), manejo centralizado de excepciones vía `@RestControllerAdvice` (devolviendo `404` en lugar de *stacktraces*) y pruebas unitarias estrictas con JUnit 5 y Mockito.

### Parte C — Construir y probar en local
Se descargó y empaquetó el código generado utilizando `mvn clean package`, produciendo un *JAR* ejecutable. Posteriormente, se probó la ejecución levantando el servidor en local mediante `java -jar` y se comprobó su funcionamiento enviando peticiones mediante `curl` a los endpoints implementados.

### Parte D — Control de versiones con Git
El directorio del proyecto fue inicializado como repositorio de Git. En el **Commit 1**, se añadieron los archivos del framework junto con las reglas explícitas del `.gitignore` y el `.gitattributes`. Inmediatamente después, en el **Commit 2**, se implementó el fichero `.editorconfig` para estandarizar el entorno de desarrollo del equipo.

### Parte E — Formateo automático en cada commit
Para asegurar la homogeneidad del código, se integró el plugin Spotless en el ciclo de vida de Maven. Además, en el **Commit 3** se vinculó un *hook* de `pre-commit` ubicándolo en `.githooks` y configurando Git para ejecutarlo automáticamente. Esto garantizó que cada fichero Java confirmase el estándar de estilo de Google antes de integrarse al historial.

### Parte F — Provocar y resolver un conflicto
Para practicar la gestión de colisiones, se crearon dos ramas alternativas a partir de la rama principal (`feat/rama-1` y `feat/rama-2`). En cada rama se registró un commit (el **Commit 4** y el **Commit 5**) sobrescribiendo la misma línea de un archivo. Al integrarlas secuencialmente, la primera fusión actuó mediante *fast-forward*, pero la segunda provocó el conflicto esperado. Se procedió a editar los archivos afectados para eliminar los delimitadores inyectados por Git y se selló la operación creando el **Commit 6** de *merge*.
