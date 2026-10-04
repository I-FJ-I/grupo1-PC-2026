# Task API - Gestión de Tareas

API REST para la gestión de tareas (To-Do) desarrollada con Java 21 y Spring Boo. Este proyecto permite realizar operaciones CRUD sobre tareas, gestionando su estado, prioridad y fecha límite mediante persistencia en una base de datos H2 en memori.

## Requisitos previos

Para poder construir, ejecutar y contribuir a este proyecto, necesitas tener instalados en tu sistema:

- **Java 21** (JDK 21)
- **Maven** (3.8 o superior)
- **Git**

## Configuración del entorno de desarrollo (Hooks)

Este proyecto utiliza el plugin **Spotless** para garantizar un formato de código uniforme siguiendo el estilo de Google.

Es **obligatorio** activar el hook de pre-commit de Git antes de empezar a contribuir. Esto asegura que el código se formatee automáticamente cada vez que hagas un commit. Para activarlo, ejecuta el siguiente comando en la raíz del repositorio:

```bash
git config core.hooksPath .githooks

```

(Nota: El hook ejecutará `mvn spotless:apply` automáticamente antes de cada commit).

## Cómo construir el proyecto

Para compilar el código fuente, ejecutar los tests unitarios y empaquetar la aplicación en un archivo JAR ejecutable ("fat JAR"), utiliza el siguiente comando de Maven en la raíz del proyecto:

```bash
mvn clean package

```

Una vez finalizado, el archivo compilado se generará en la ruta `target/task-api-1.0.0.jar`.

## Cómo arrancar la aplicación

El proyecto utiliza una base de datos en memoria (H2) que se inicializa al vuelo, por lo que no necesitas configurar servicios externos. Para iniciar el servidor web embebido, ejecuta el JAR generado:

```bash
java -jar target/task-api-1.0.0.jar

```

La aplicación arrancará y la API estará disponible en `http://localhost:8080/api/tasks`.

## Endpoints de la API

La aplicación expone los siguientes endpoints para gestionar las tareas:

- `GET /api/tasks`: Recupera la lista completa de tareas.

- `GET /api/tasks/{id}`: Recupera los detalles de una tarea específica por su ID.

- `POST /api/tasks`: Crea una nueva tarea (requiere título, estado, prioridad y una fecha límite en el presente o futuro).

- `PUT /api/tasks/{id}`: Actualiza los datos de una tarea existente.

- `DELETE /api/tasks/{id}`: Elimina una tarea por su ID.

## Colaboración

Si deseas colaborar en este proyecto, por favor revisa el archivo `CONTRIBUTING.md` para conocer el flujo de trabajo basado en GitHub Flow (ramas, Pull Requests y revisiones de código). Todos los Pull Requests deben ser revisados por los propietarios del código definidos en el archivo `CODEOWNERS`.
