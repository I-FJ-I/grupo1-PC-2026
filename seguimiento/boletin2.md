# Boletín 2

## Resumen de commits

| # | Mensaje del commit | Qué se hizo | Autor | Enlace |
|---|--------------------|-------------|-------|--------|
| 1 | `docs(repo): añade README, plantillas, codeowners y guía de contribución` | Creación del README principal, plantillas en `.github/` para estandarizar PRs e Issues, asignación de Code Owners y redacción del CONTRIBUTING.md. | Jiahui | <<Enlace>> |

## Detalle por commit

### 1. `docs(repo): añade README, plantillas, codeowners y guía de contribución`

- **Qué se hizo:** Se redactó el `README.md` con las instrucciones de uso y hooks. Además, se añadieron los archivos `.github/PULL_REQUEST_TEMPLATE.md`, `.github/ISSUE_TEMPLATE/bug.md`, `.github/ISSUE_TEMPLATE/enhancement.md` y `.github/CODEOWNERS` para automatizar las revisiones, junto con el archivo `CONTRIBUTING.md` para definir el GitHub Flow.
- **Autor:** Jiahui
- **Relación con el boletín:** Parte A (Publicar el repositorio) y Parte B (Gobernanza del repositorio).
- **Uso de IA:** Gemini. Prompts utilizados: "revisa que el README sea completo y genera las plantillas de la parte B"

## Descripción narrativa de lo hecho en la sesión y relación con los commits

### Parte A y B — Publicación y Gobernanza del repositorio

Iniciamos el trabajo documentando el proyecto con un `README.md` completo que explica cómo compilar y arrancar la API, además de detallar cómo activar los hooks de Git en local. Para dejar el repositorio preparado para la colaboración del equipo antes de abrir el primer Pull Request, configuramos varias herramientas de gobernanza en GitHub. Añadimos plantillas para asegurar que todos los PRs e Issues (bugs y nuevas funcionalidades) sigan una estructura común. Creamos un archivo `CODEOWNERS` para forzar que los cambios en el código sean revisados por los miembros del equipo y documentamos las normas de trabajo y la estrategia de fusión en `CONTRIBUTING.md`. Todo esto se consolidó en un primer commit para sentar las bases del repositorio.
