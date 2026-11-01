# Boletín 1

## Resumen de commits

| Mensaje del commit                                                            | Qué se hizo                                                                                           | Autor  | Enlace    |
| ----------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------- | ------ | --------- |
| `chore: proyecto Maven inicial de la API de tareas`                           | Inicialización del repositorio y archivos base de configuración.                                      | Jiahui | `1547711` |
| `chore(editor): añade .editorconfig`                                          | Añadir el archivo `.editorconfig` para estandarizar la indentación.                                   | Jiahui | `d1b2aa0` |
| `feat(tasks): añadir endpoint para filtrar tareas por estado`                 | Implementación de una función de filtrado por `TaskStatus` en el repositorio, servicio y controlador. | Jiahui | `9496178` |
| `chore(build): añade Spotless para formateo automático`                       | Configuración del plugin Spotless en `pom.xml`.                                                       | Jiahui | `87bef72` |
| `chore(hooks): añade hook de pre-commit con Spotless`                         | Creación y configuración del hook para forzar el formateo antes del commit.                           | Jiahui | `e08d1f2` |
| `chore(build): cambiar java.version a 17`                                     | Cambio en `pom.xml` para bajar la versión de Java a la 17.                                            | Jiahui | `01e9ed7` |
| `chore(build): cambiar java.version a 22`                                     | Cambio  en `pom.xml` para subir la versión de Java a la 22.                                           | Jiahui | `6079693` |
| `"chore(build): resolver conflicto en pom.xml al fusionar versiones de Java"` | Resolución manual de conflicto en la fusión, restaurando Java 21.                                     | Jiahui | `3ecbff0` |

---

## Detalle por commit

**1. `chore: proyecto Maven inicial de la API de tareas`**

* **Qué se hizo:** Se inicializó el repositorio Git en local y se crearon los archivos `.gitignore` (para ignorar la carpeta `target/`, archivos compilados, `.idea/` y `.env`) y `.gitattributes` (para normalizar los saltos de línea LF y designar los `.jar` como binarios). ^4681ce
* **Autor:** Jiahui
* **Relación con el boletín:** Parte D (Control de versiones con Git).
* **Uso de IA:** No se ha usado.

**2. `chore(editor): añade .editorconfig`**

* **Qué se hizo:** Se añadió el archivo `.editorconfig` en la raíz del proyecto para fijar el juego de caracteres a UTF-8, saltos de línea LF y la indentación de 4 espacios (2 para YAML/JSON).
* **Autor:** Jiahui
* **Relación con el boletín:** Parte D (Control de versiones con Git).
* **Uso de IA:** No se ha usado.

**3. `feat(tasks): añadir endpoint para filtrar tareas por estado`**

* **Qué se hizo:** Se modificaron los archivos `TaskRepository.java`, `TaskService.java` y `TaskController.java` para permitir buscar tareas pasando su estado (enum `TaskStatus`) por la URL. Se probó usando `curl.exe` desde PowerShell.
* **Autor:** Jiahui
* **Relación con el boletín:** Parte D (Crea una rama, haz un cambio pequeño pero no trivial y haz commit en ella).
* **Uso de IA:** Gemini. Prompt utilizado: *"Añade una función para filtrar por estados"*

**4. `chore(build): añade Spotless para formateo automático`**

* **Qué se hizo:** Se añadió el plugin Spotless al `pom.xml` para automatizar el formateo del código bajo el estilo de Google.
* **Autor:** Jiahui
* **Relación con el boletín:** Parte E (Formateo automático en cada commit).
* **Uso de IA:** No se ha usado.

**5. `chore(hooks): añade hook de pre-commit con Spotless`** ^2edb8b

* **Qué se hizo:** Se configuró un hook de Git en `.githooks/pre-commit` para obligar al formateo en cada commit. Se solucionó un error de ejecución en Windows provocado por los saltos de línea CRLF en el archivo del hook.
* **Autor:** Jiahui
* **Relación con el boletín:** Parte E (Formateo automático en cada commit).
* **Uso de IA:** Gemini. Prompt implícito al solicitar ayuda con el error: *"error: cannot spawn .githooks/pre-commit: No such file or directory"* y adaptar el script bash para encontrar el `pom.xml` en las subcarpetas.

**6. `chore(build): cambiar java.version a 17`**

* **Qué se hizo:** En una nueva rama (`rama-java-17`), se modificó el archivo `pom.xml` cambiando la propiedad `<java.version>` de `21` a `17`.
* **Autor:** Jiahui
* **Relación con el boletín:** Parte F (Provocar y resolver un conflicto).
* **Uso de IA:** No se ha usado.

**7. `chore(build): cambiar java.version a 22`**

* **Qué se hizo:** En otra rama distinta creada desde main (`rama-java-22`), se modificó la misma línea del archivo `pom.xml` para establecer la versión de Java a `22`.
* **Autor:** Jiahui
* **Relación con el boletín:** Parte F (Provocar y resolver un conflicto).
* **Uso de IA:** No se ha usado.

**8. `chore(build): resolver conflicto en pom.xml al fusionar versiones de Java`**

* **Qué se hizo:** Al hacer merge de la segunda rama, Git detuvo la fusión por un conflicto en `pom.xml`. Se eliminaron los marcadores `<<<<<<<`, `=======` y `>>>>>>>` manualmente en el editor, dejando el archivo en su estado correcto (versión 21) y se finalizó la fusión.
* **Autor:** Jiahui
* **Relación con el boletín:** Parte F (Provocar y resolver un conflicto).
* **Uso de IA:** No se ha usado.

---

## Descripción narrativa de lo hecho en la sesión y relación con los commits

**Parte A — Instalación y verificación del entorno**
La sesión comenzó verificando que las herramientas necesarias (Git, Java 21 y Maven) estuvieran correctamente instaladas en el sistema Windows 11. Además, se configuró el perfil global de Git (`user.name` y `user.email`), se forzó la política `pull.ff only` para evitar commits de fusión indeseados al descargar cambios, y se fijó `main` como el nombre predeterminado para las ramas iniciales.

**Parte B — Generar la aplicación con ayuda de IA**
Mediante el uso de IA se estructuró el proyecto base: una API REST en Spring Boot enfocada en la gestión de tareas (To-Do). El dominio incluyó la entidad `Task` con enumeradores asociados como `TaskStatus` y `TaskPriority`. Se implementó Spring Data JPA usando H2 como base de datos en memoria y se configuró un controlador de errores global con `@RestControllerAdvice` para devolver errores 400 y 404 limpios, implementando validaciones estrictas (por ejemplo, evitar que la fecha límite `deadline` estuviese en el pasado).

**Parte C — Construir y probar en local**
Se validó el ciclo de vida de Maven ejecutando `mvn clean package` para compilar, pasar los tests unitarios y generar el *fat JAR* en el directorio `target/`. Seguidamente, se ejecutó la aplicación con `java -jar` comprobando que el servidor web embebido arrancaba correctamente en el puerto 8080. Se realizaron pruebas de inserción y consulta a través de PowerShell usando tanto `curl.exe`.

**Parte D — Control de versiones con Git**
Se inicializó el repositorio local. Para asegurar la portabilidad y limpieza del proyecto, se crearon los archivos `.gitignore` y `.gitattributes`, realizando el primer commit (`1547711`). A continuación, se añadió el `.editorconfig` en un segundo commit (`d1b2aa0`).
Para aplicar el flujo de ramas, se creó la rama `feat/filtrar-tareas-por-estado` donde se desarrolló un nuevo endpoint en la API que permitía filtrar tareas. Tras confirmar su funcionamiento, se hizo el commit (`9496178`) y se fusionó en `main`.

**Parte E — Formateo automático en cada commit**
Para mantener un estilo uniforme de código (formato Google), se añadió el plugin `spotless-maven-plugin` al `pom.xml` (`87bef72`). Se automatizó la ejecución de Spotless creando un script en `.githooks/pre-commit`. Se ha adaptado el script para buscar los `pom.xml` en las subcarpetas del repositorio. Finalmente, se registró el hook en Git (`e08d1f2`).

**Parte F — Provocar y resolver un conflicto**
Para comprender cómo actúa Git ante ediciones simultáneas, se crearon dos ramas independientes desde `main`: `rama-java-17` y `rama-java-22`. En la primera, se modificó la etiqueta `<java.version>` del `pom.xml` para indicar la versión 17, haciendo su respectivo commit (`01e9ed7`). En la segunda, se editó exactamente la misma línea pero indicando la versión 22 (`6079693`).
La primera rama se integró sin problemas en `main`. Al intentar fusionar la segunda, Git notificó un conflicto. Se abrió el archivo, se localizaron los marcadores de conflicto inyectados por Git, se eliminaron y se restauró la versión coherente (Java 21). Tras guardar el archivo, el conflicto se resolvió confirmando la acción mediante un `git add` y el commit de fusión final (`3ecbff0`).