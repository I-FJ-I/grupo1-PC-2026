# Boletín 4

## Resumen de commits

| # | Mensaje del commit | Qué se hizo | Autor | Enlace |
|---|--------------------|-------------|-------|--------|
| 1 | `ci(actions): añade primer pipeline de integracion continua` | Creación del fichero `.github/workflows/ci.yml` con un job `build` que configura JDK 21 con caché de Maven y ejecuta `mvn --batch-mode verify`. | Jiahui | 857bf50ecf12dd7696d1302c6033dca93105c013 |
| 2 | `test(service): provoca fallo intencionado en test unitario` | Modificación de una aserción en `TaskServiceTest` para forzar un fallo en la suite de pruebas y verificar la detección en el pipeline de CI. | Jiahui | 588b04562d97514491dc73e77927a0b8ecab653c |
| 3 | `test(service): corrige test unitario para restaurar el CI` | Restauración de la aserción original en `TaskServiceTest` comprobando que el check del Pull Request vuelve a estado verde. | Jiahui | d924b58873d1b4340180e3ce098af4da382aebcd |
| 4 | `ci(actions): divide el pipeline en jobs lint, test y package` | Reestructuración de `.github/workflows/ci.yml` en tres jobs secuenciales (`lint`, `test` y `package`) encadenados mediante `needs`, integrando `dorny/test-reporter` y `actions/upload-artifact`. | Jiahui | fd8c8809c695bca1635dd46a52cf0a9dac23b976 |
| 5 | `style(app): introduce fallo de formato saltando pre-commit hook` | Alteración del formato en `TaskApiApplication.java` realizando el commit con `--no-verify` para comprobar que el job `lint` falla y bloquea los jobs posteriores. | Jiahui | 4e0c52113566479f68669e928919fef6fbc84ead |
| 6 | `style(app): corrige formato de codigo con Spotless` | Formateo del código fuente mediante `mvn spotless:apply` para superar el job `lint` y completar la ejecución del pipeline con generación de informes JUnit y artefacto JAR. | Jiahui | 3221d5f5727fd0b9fc1331f8388d03b0b5fe3661 |
| 7 | `ci(actions): romper un test para compruebar que GitHub impide fusionarlo` | Modificación de un test para forzar su fallo y comprobar empíricamente que la regla de protección de rama impide el mergeo del Pull Request. | Fran | d031306a7bf18a6e7ca6c7a52a64e86704eeaf2c |
| 8 | `feat(actions): añadir workflow de IA para revisión de código y badge` | Creación del fichero `.github/workflows/ai-review.yml` invocando el modelo `beam:cloud` de Ollama e inclusión del badge de estado en el `README.md`. | Fran | 3abed67f3a543887c604804687e9a309b364899b |
| 9 | `fix(actions): no se mostraba correctamente el badge en el README` | Eliminación de la extensión `.git` en las URLs del badge dentro del `README.md` para que la imagen SVG renderice correctamente. | Fran | cab803b904a8bfab068904bdb4064532c814d68c |


## Detalle por commit

### 1. `ci(actions): añade primer pipeline de integracion continua`

- **Qué se hizo:** Se creó el directorio `.github/workflows` con el archivo `ci.yml` definiendo el flujo inicial de Integración Continua. El workflow se dispara en eventos `push` sobre `main` y en cualquier `pull_request`, cancelando ejecuciones redundantes mediante `concurrency`, restringiendo permisos a lectura (`contents: read`) y ejecutando un job `build` sobre `ubuntu-latest` con Temurin JDK 21 y caché de dependencias de Maven.
- **Autor:** Jiahui
- **Relación con el boletín:** [Parte A (Primer pipeline de CI)](#parte-a--primer-pipeline-de-ci).
- **Uso de IA:** No

### 2. `test(service): provoca fallo intencionado en test unitario`

- **Qué se hizo:** Se modificó intencionadamente un test unitario en `TaskServiceTest.java` para que su aserción fallase. Al actualizar el Pull Request contra `main`, se verificó que GitHub Actions ejecutó el job `build` y marcó el estado del check en rojo.
- **Autor:** Jiahui
- **Relación con el boletín:** [Parte B (Demostrar que el CI atrapa errores)](#parte-b--demostrar-que-el-ci-atrapa-errores).
- **Uso de IA:** No.

### 3. `test(service): corrige test unitario para restaurar el CI`

- **Qué se hizo:** Se revirtió el fallo introducido en `TaskServiceTest.java` y se realizó un nuevo `push` a la misma rama del Pull Request, comprobando que el pipeline se disparó automáticamente y el check de estado volvió a pasar a verde.
- **Autor:** Jiahui
- **Relación con el boletín:** [Parte B (Demostrar que el CI atrapa errores)](#parte-b--demostrar-que-el-ci-atrapa-errores).
- **Uso de IA:** No.

### 4. `ci(actions): divide el pipeline en jobs lint, test y package`

- **Qué se hizo:** Se refactorizó `.github/workflows/ci.yml` para separar las responsabilidades en tres jobs encadenados con la directiva `needs`: `lint` (verifica el estilo con `mvn -B spotless:check`), `test` (ejecuta `mvn -B verify` y publica los resultados XML de Surefire con `dorny/test-reporter@v3`) y `package` (construye el JAR omitiendo los tests y lo sube como artefacto con una retención de 7 días mediante `actions/upload-artifact@v4`). Se ampliaron además los permisos del token (`actions: read`, `checks: write`).
- **Autor:** Jiahui
- **Relación con el boletín:** [Parte C (Separar responsabilidades en jobs)](#parte-c--separar-responsabilidades-en-jobs).
- **Uso de IA:** No.

### 5. `style(app): introduce fallo de formato saltando pre-commit hook`

- **Qué se hizo:** Se desordenó el código de `TaskApiApplication.java` y se empleó `git commit --no-verify` para omitir el hook local de pre-commit. Tras subir el cambio, se comprobó que el job `lint` falló en GitHub Actions y que los jobs dependientes (`test` y `package`) no llegaron a iniciarse.
- **Autor:** Jiahui
- **Relación con el boletín:** [Parte C (Separar responsabilidades en jobs)](#parte-c--separar-responsabilidades-en-jobs).
- **Uso de IA:** No.

### 6. `style(app): corrige formato de codigo con Spotless`

- **Qué se hizo:** Se aplicó el formato correcto con Spotless y se subió un nuevo commit a la rama, logrando que los tres jobs (`lint`, `test` y `package`) finalizaran con éxito, publicando el informe de pruebas JUnit en el PR y almacenando el artefacto `app-jar`.
- **Autor:** Jiahui
- **Relación con el boletín:** [Parte C (Separar responsabilidades en jobs)](#parte-c--separar-responsabilidades-en-jobs).
- **Uso de IA:** No.

### 7. `ci(actions): romper un test para compruebar que GitHub impide fusionarlo`

- **Qué se hizo:** Se introdujo intencionadamente un fallo en un test unitario creando un PR hacia `main`. El objetivo fue validar que la configuración "Require status checks to pass before merging" bloquea activamente la fusión de código si el job `test` falla.
- **Autor:** Fran
- **Relación con el boletín:** [Parte D (Convertir el CI en una barrera)](#parte-d--convertir-el-ci-en-una-barrera).
- **Uso de IA:** No.

### 8. `feat(actions): añadir workflow de IA para revisión de código y badge`

- **Qué se hizo:** Se implementó un nuevo workflow que reacciona a los PR extrayendo el diff de los cambios y enviándolo mediante un script en Python a la API de Ollama Cloud (`beam:cloud`). El resultado se publica automáticamente como un comentario en el PR y se añadió el badge en el README.
- **Autor:** Fran
- **Relación con el boletín:** [Parte E (Ampliaciones interesantes - Gestión de secretos)](#parte-e--ampliaciones-interesantes-code-review-con-ia).
- **Uso de IA:** Sí (soporte para adaptar el script de Python a la API de Ollama Cloud).

### 9. `fix(actions): no se mostraba correctamente el badge en el README`

- **Qué se hizo:** Se corrigió un error de sintaxis en el Markdown del `README.md` donde el badge no se mostraba por incluir `.git` al final de la URL del repositorio.
- **Autor:** Fran
- **Relación con el boletín:** [Parte E (Ampliaciones interesantes - Gestión de secretos)](#parte-e--ampliaciones-interesantes-code-review-con-ia).
- **Uso de IA:** No.

## Descripción narrativa de lo hecho en la sesión y relación con los commits

### Parte A — Primer pipeline de CI

Con el objetivo de automatizar la validación de cada cambio integrado en el repositorio, se define un primer pipeline de Integración Continua utilizando **GitHub Actions**. Para ello, se crea el fichero `.github/workflows/ci.yml`.
Una vez subido el archivo a la rama y abierto el Pull Request contra `main`, se verifica que el workflow se lanza automáticamente y el check `build` finaliza en verde:
![check build en verde](capturas/JH-b4-A(1).png)

#### Sentido del campo `concurrency` en `ci.yml`

El bloque `concurrency` agrupa las ejecuciones del workflow utilizando el identificador `ci-${{ github.ref }}` (que representa la rama o referencia de Git que disparó el evento) y activa la opción `cancel-in-progress: true`.

Su propósito es **evitar el desperdicio de recursos y tiempo de cómputo en los runners**: si se realizan varios `git push` seguidos a una misma rama en poco tiempo mientras una ejecución anterior del pipeline para esa misma rama aún sigue en curso, GitHub Actions **cancela automáticamente la ejecución anterior (que ha quedado obsoleta)** y mantiene en ejecución únicamente la correspondiente al último commit enviado.

#### Cuándo se dispara el workflow

Según lo definido en la cláusula `on:`, el workflow se dispara automáticamente en dos situaciones:

1. **`push` a la rama `main`:** Cada vez que se suben o fusionan commits directamente sobre la rama principal `main`.
2. **`pull_request` (sobre cualquier rama):** Cada vez que se abre un Pull Request (`opened`), se reabre (`reopened`) o se actualiza con nuevos commits (`synchronize`) en el repositorio, permitiendo validar los cambios en las ramas de trabajo antes de integrarlos.

### Parte B — Demostrar que el CI atrapa errores

Para comprobar empíricamente que el pipeline actúa como barrera ante regresiones, se modifica a propósito una aserción dentro de `src/test/java/com/example/taskapi/service/TaskServiceTest.java` para que falle durante la ejecución de JUnit.

Al hacer `push` a la rama del Pull Request, el workflow `CI` se dispara automáticamente. Al llegar al paso `mvn --batch-mode verify`, Surefire ejecuta la suite de pruebas, detecta el fallo en el test y devuelve un código de salida de error, provocando que el pipeline se ponga en **ROJO** y el check del Pull Request falle:

![check build en rojo](capturas/JH-b4-B(1).png)
![check build rojo detalles](capturas/JH-b4-B(2).png)

A continuación, se restaura la aserción original en `TaskServiceTest.java`, se realiza un nuevo commit y se hace `push` a la misma rama. GitHub Actions detecta la actualización del Pull Request, vuelve a lanzar el workflow y, tras superar todos los tests satisfactoriamente, el check de estado pasa de nuevo a **VERDE**:

![check build en verde de nuevo](capturas/JH-b4-B(3).png)

### Parte C — Separar responsabilidades en jobs

Ejecutar todas las comprobaciones dentro de un único job monolítico dificulta identificar rápidamente en qué etapa ha fallado el pipeline (formato, pruebas o empaquetado). Por ello,se reestructura `.github/workflows/ci.yml` dividiéndolo en tres jobs independientes (`lint`, `test` y `package`) encadenados secuencialmente mediante `needs`.

#### Demostración de fallo de formato en `lint`

Para comprobar cómo el encadenamiento con `needs` detiene el pipeline ante un error temprano, se desordena el formato de la clase `TaskApiApplication.java` y se realiza un commit saltándose el hook local de pre-commit mediante el flag `--no-verify`:

```powershell
git commit --no-verify -m "style(app): introduce fallo de formato saltando pre-commit hook"
git push

```

Al ejecutarse el workflow en GitHub Actions, el job `lint` falla al ejecutar `mvn -B spotless:check`. Como `test` depende de `lint` (`needs: lint`) y `package` depende de `test` (`needs: test`), **los jobs `test` y `package` ni siquiera llegan a ejecutarse**, quedando omitidos:

![diagrama fallo de lint](capturas/JH-b4-C(1).png)

#### Por qué es útil que `lint` vaya primero y por qué el informe lleva `if: always()`

- **Por qué `lint` va primero:** La comprobación estática de formato (`spotless:check`) es una operación muy rápida y ligera que no requiere compilar todo el proyecto ni levantar el contexto de Spring Boot o bases de datos en memoria para los tests. Colocar `lint` en primer lugar aplica el principio *Fail-Fast*: permite detectar incumplimientos de estilo en pocos segundos y abortar el pipeline de inmediato, ahorrando tiempo de feedback y minutos de cómputo en los runners que de otro modo se gastarían ejecutando `test` y `package` sobre código mal formateado.
- **Por qué el paso `Publicar informe de tests` lleva `if: always()`:** En GitHub Actions, un `step` solo se ejecuta por defecto si todos los pasos previos del mismo job han terminado con éxito (`if: success()`). Si algún test falla durante el paso `mvn -B verify`, dicho paso devuelve error y, sin `if: always()`, el paso de `dorny/test-reporter` se omitiría. Precisamente cuando los tests fallan es cuando resulta más necesario publicar el informe JUnit para inspeccionar qué prueba ha fallado y por qué; con `if: always()`, nos aseguramos de que el reporte se genere y publique tanto si los tests pasan como si fallan.

#### Corrección del formato, informe de tests y artefacto JAR generado

Tras corregir el formato aplicando Spotless (`mvn spotless:apply`) y hacer `push`, el pipeline ejecuta secuencialmente con éxito los tres jobs (`lint`, `test` y `package`), publicando el informe de pruebas JUnit y subiendo el artefacto `app-jar`:

![captura todos los jobs en verde](capturas/JH-b4-C(4).png)
![captura summary todo en verde](capturas/JH-b4-C(2).png)
![captura artifact app-jar generado](capturas/JH-b4-C(3).png)

#### Por qué `package` debe volver a construir el proyecto y cómo compartir archivos entre jobs

1. **¿Por qué `package` vuelve a construir el proyecto si `test` ya ejecutó `mvn verify`?**
Porque en GitHub Actions **cada `job` se ejecuta en una máquina virtual (*runner*) independiente y limpia**. Cuando el job `test` finaliza, su máquina virtual y su sistema de archivos local (incluida la carpeta `target/` con las clases compiladas generadas durante `mvn verify`) se destruyen. Al iniciarse el job `package` en un nuevo runner, parte únicamente del código fuente descargado con `actions/checkout@v4`, por lo que no dispone del directorio `target/` del job anterior y debe ejecutar `mvn -B package -DskipTests` para compilar y empaquetar el archivo `.jar`.

2. **¿Cómo podrían compartirse los archivos generados entre ambos jobs?**
Se pueden compartir entre distintos runners mediante dos mecanismos de GitHub Actions:

- **Con artefactos (`actions/upload-artifact` y `actions/download-artifact`):** Subiendo los archivos compilados o el `.jar` de `target/` al final de un job con `actions/upload-artifact@v4` y descargándolos al inicio del job siguiente mediante `actions/download-artifact@v4`.
- **Con caché (`actions/cache`):** Almacenando en caché el directorio `target/` asociado al SHA del commit (`${{ github.sha }}`) en el primer job y restaurándolo en los jobs posteriores del mismo workflow.

### Parte D — Convertir el CI en una barrera

Para garantizar que el código roto nunca llegue a la rama principal, se configuraron reglas de protección sobre la rama `main` desde **Settings → Branches**. Se activó la opción *"Require status checks to pass before merging"* exigiendo obligatoriamente que los jobs `lint`, `test` y `package` se completen con éxito.

Para verificar esta barrera empíricamente, se abrió un Pull Request con un test roto intencionadamente. Al ejecutarse el workflow, el job `test` falló. Como resultado, GitHub deshabilitó el botón de "Merge pull request", demostrando que el CI funciona como un muro de contención efectivo frente a regresiones.

![Comprobación PR bloqueado 1](capturas/FRAN-b4-D(1).png)
![Comprobación PR bloqueado 2](capturas/FRAN-b4-D(2).png)

### Parte E — Ampliaciones interesantes (Code Review con IA)

Se automatizó la revisión de código incorporando un asistente de inteligencia artificial. Para ello, se creó el workflow `ai-review.yml`, que extrae el `diff` de cada Pull Request y lo envía a un modelo de lenguaje en la nube (`beam:cloud` de Ollama).

Para mantener la seguridad, el token de la API no se incluyó en el código fuente, sino que se almacenó de forma cifrada en **Settings → Secrets and variables → Actions** bajo la variable `OLLAMA_API_TOKEN`. El script de Python inyecta este secreto como variable de entorno durante la ejecución y, si el proceso tiene éxito, publica el resumen mediante GitHub CLI (`gh pr comment`). Si falla la API o la autenticación, publica el mensaje de rescate *"La IA ha fallado"*.

Finalmente, se integró un badge en el `README.md` para monitorizar visualmente el estado de esta acción. Tras corregir un pequeño error en la URL (eliminar `.git`), el badge quedó operativo en la cabecera del proyecto.

![Comprobación Code Review 1](capturas/FRAN-b4-E(1).png)
![Comprobación Code Review 2](capturas/FRAN-b4-E(2).png)