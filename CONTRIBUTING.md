# Guía de Contribución

¡Gracias por interesarte en contribuir a la Task API!

## Configuración del entorno

1. Clona el repositorio.
2. Activa los hooks obligatorios para el formateo de código con Spotless:
   `git config core.hooksPath .githooks`

## Flujo de trabajo (GitHub Flow)

1. Asegúrate de que `main` está en verde (compila y pasa los tests).
2. Crea una rama descriptiva a partir de `main` (ej: `feat/nuevo-endpoint`, `fix/error-404`).
3. Realiza tus cambios asegurándote de seguir los Conventional Commits.
4. Sube la rama y abre un **Pull Request**.
5. Rellena la plantilla del PR y enlaza el Issue correspondiente.
6. Espera la revisión de al menos un Code Owner.

## Estrategia de fusión

En este proyecto utilizamos la estrategia **Squash and merge**. De esta manera, mantenemos un historial de commits en `main` completamente lineal y limpio, agrupando todos los commits intermedios del PR en uno solo que aporta un cambio con significado.
