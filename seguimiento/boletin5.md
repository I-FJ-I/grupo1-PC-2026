# Boletín 5: Entrega Continua (CD)

## Resumen de commits

| # | Mensaje del commit | Qué se hizo | Autor | Enlace |
|---|--------------------|-------------|-------|--------|
| 1 | `feat(release): añadido el pipeline de publicación` | Creación de `.github/workflows/release.yml` para automatizar el empaquetado y subida de la imagen a GitHub Packages usando `docker/metadata-action`. | Fran | 57aa9c086d45ef248909a10bc7eaa65d7d0fcbf4 |
| 2 | `docs(boletin5): parte B documentada` | Documentación de la publicación de la versión `v1.0.0`: etiqueta anotada, tags generados por el workflow y Release en GitHub. | Jesús | fddef13f2a720342d44440b6f7c59205ef0157d8 |

## Detalle por commit

### 1. `feat(release): añadido el pipeline de publicación`

- **Qué se hizo:** Se implementó un flujo de GitHub Actions que se dispara al integrar código en `main` o al publicar etiquetas de versión. El workflow construye la imagen Docker del proyecto y la publica en GitHub Container Registry (`ghcr.io`) autenticándose con el token nativo de GitHub.
- **Autor:** Fran
- **Relación con el boletín:** [Parte A (Pipeline de publicación)](#parte-a---pipeline-de-publicación).
- **Uso de IA:** No.

### 2. `docs(boletin5): parte B documentada`

- **Qué se hizo:** Se documentó la publicación de la primera versión de la imagen. Se creó la etiqueta anotada `v1.0.0`, se comprobaron los tags generados por el workflow y se publicó la Release en GitHub a partir de esa etiqueta.
- **Autor:** Jesús
- **Relación con el boletín:** [Parte B (Publicar una versión de verdad)](#parte-b---publicar-una-versión-de-verdad).
- **Uso de IA:** No.

## Descripción narrativa de lo hecho en la sesión y relación con los commits

### Parte A - Pipeline de publicación

Para automatizar la Entrega Continua de la aplicación, se implementó un nuevo flujo de trabajo en `.github/workflows/release.yml`. Su función es empaquetar la aplicación en una imagen de contenedor y publicarla automáticamente en **GitHub Container Registry (GHCR)**, de forma que quede vinculada al repositorio en la sección *Packages*.

La pieza fundamental de este workflow es la acción `docker/metadata-action`, que asigna dinámicamente las etiquetas (tags) de la imagen Docker dependiendo del evento que haya disparado la ejecución:

1. **Al fusionar un PR a `main`:** El evento `push` sobre la rama principal activa las reglas `type=sha` y `type=raw,value=latest,enable={{is_default_branch}}`. Esto genera una imagen etiquetada con un identificador único del commit (`sha-<hash>`) y actualiza el tag `latest`, apuntando siempre a la versión más reciente del código integrado.
2. **Al publicar una etiqueta de versión (`vX.Y.Z`):** Si se sube un tag de Git, el workflow extrae la versión y genera etiquetas semánticas (`type=semver`). Por ejemplo, para la versión `v1.2.3`, GHCR publicará etiquetas para `1.2.3`, `1.2` y `1`, además del `sha-<hash>` invariable. El tag `latest` se ignora aquí al no tratarse de la rama por defecto.

La autenticación para subir la imagen a `ghcr.io` se resuelve de forma nativa utilizando `secrets.GITHUB_TOKEN`, sin necesidad de gestionar o configurar credenciales de plataformas externas.

![Imagen en Packages](capturas/FRAN-b5-A.png)

### Parte B - Publicar una versión de verdad

En Git existen las etiquetas, que normalmente son utilizadas para marcar versiones de la aplicación (por ejemplo, la de lanzamiento). Pueden ser tanto ligeras, que son un simple puntero hacia un commit en concreto, como anotadas, que son objetos propios que se guardan en la base de datos de Git y que contienen la siguiente información:

- Puntero a un commit en concreto.
- Nombre del autor y correo.
- Fecha y hora de la creación.
- Mensaje descriptivo.
- Firma GPG (GNU Privacy Guard): autentica tu identidad y garantiza que el código no fue modificado por nadie más. Opcional.

Para crear una etiqueta se ejecuta el comando ``git tag -a v1.0.0 -m "Primera versión publicable de la API de tareas"``. Para subirla al repositorio remoto se usa ``git push origin v1.0.0``. Una vez ejecutados los comandos, la etiqueta ya es visible en el repositorio remoto.

![Etiqueta publicada](./capturas/JESUS-B5-B(1).png)

Al subir esta etiqueta, se lanzó el workflow ``Release`` configurado en el apartado anterior.

![Ejecución del workflow Release](./capturas/JESUS-B5-B(2).png)

Si miramos la imagen publicada, vemos que tiene adjuntadas las siguientes etiquetas a partir de las siguientes reglas:

- ``1.0.0``: Creada a partir de la regla semántica ``type=semver,pattern={{version}}``. Indica la versión exacta de la imagen.
- ``1.0``: Creada a partir de ``type=semver,pattern={{major}}.{{minor}}``, indica la última versión ``1.0.x`` publicada. Si se publicase una nueva imagen ``v1.0.1``, la etiqueta ``1.0`` se repuntaría a esa nueva imagen.
- ``1``: Creada a partir de ``type=semver,pattern={{major}}``, indica la última versión ``1.x`` publicada. Misma idea que la anterior, solo que se reapuntaría cuando el segundo dígito cambia (por ejemplo, ``v1.1.0``).
- ``sha-f9cc8a6``: Creada a partir de  `type=sha,prefix=sha-`, permite tener una trazabilidad desde la imagen hasta el commit exacto.

![Tags de la imagen](./capturas/JESUS-B5-B(3).png)

Por último, se crea la Release a partir de esta imagen, describiendo los cambios realizados a partir de la opción ``Generate release notes``. El resultado se ve en el siguiente [enlace](https://github.com/I-FJ-I/grupo1-PC-2026/releases/tag/v1.0.0).

![Release publicada](./capturas/JESUS-B5-B(4).png)

### Parte C - Verificar la imagen publicada

Una vez publicada la imagen, se verifica su funcionamiento descargándola de GitHub Container Registry (GHCR) y ejecutándola junto a un PostgreSQL real. El procedimiento es el siguiente:

1. Se crea una red personalizada en Docker. A diferencia de la red por defecto, una red definida por el usuario incorpora un servidor DNS interno, de modo que los contenedores se resuelven entre sí por su nombre en lugar de por una IP.
2. Se crea y arranca en segundo plano la imagen ``postgres:16`` en un contenedor. Se le pasa tanto el nombre de la base de datos como las credenciales a través de variables de entorno.
3. Se descarga la imagen publicada en el apartado anterior en su versión ``1.0.0``.
4. Se arranca en segundo plano la imagen descargada. De igual manera, se pasan los datos necesarios a través de variables de entorno. Además, se expone el puerto 8080 del contenedor a través del puerto 8080 del sistema físico.

```cmd
docker network create tareas-net

docker run -d --name db --network tareas-net -e POSTGRES_DB=tareas -e POSTGRES_USER=app -e POSTGRES_PASSWORD=secret_local_dev postgres:16

docker pull ghcr.io/i-fj-i/grupo1-pc-2026:1.0.0

docker run -d --name api --network tareas-net -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/tareas -e SPRING_DATASOURCE_USERNAME=app -e SPRING_DATASOURCE_PASSWORD=secret_local_dev ghcr.io/i-fj-i/grupo1-pc-2026:1.0.0
```

La descarga de la imagen devuelve el valor Digest que identifica a la imagen publicada, lo que confirma que se ejecuta la imagen del registro y no una construcción local:

```cmd
docker pull ghcr.io/i-fj-i/grupo1-pc-2026:1.0.0
1.0.0: Pulling from i-fj-i/grupo1-pc-2026
35e448951dfd: Already exists
d4d50721561e: Pull complete
4c7ba16bd344: Already exists
35dbe55e7395: Pull complete
4f4fb700ef54: Pull complete
a44e35628532: Pull complete
2a88c3415831: Download complete
3e126faff855: Download complete
0df34dd37994: Already exists
082f3711ec02: Already exists
Digest: sha256:b84d53bb1131f9795ad77f776ca1009bfab8b48de280a5bea8427e98cf6db1ca
Status: Downloaded newer image for ghcr.io/i-fj-i/grupo1-pc-2026:1.0.0
```

Si los comandos se ejecutan con éxito, en Docker Desktop aparecen los dos contenedores en ejecución. Que estén en ejecución no garantiza que la aplicación funcione: eso se comprueba con el endpoint de salud.

![Contenedores arrancados](./capturas/JESUS-B5-C(1).png)

Para verificar el funcionamiento, basta con ejecutar el comando ``curl http://localhost:8080/actuator/health``. En caso de éxito, debe devolver ``UP``.

![Resultado del comando curl](./capturas/JESUS-B5-C(2).png)

Una vez comprobado su funcionamiento, se limpia lo creado con ``docker rm -f db api && docker network rm tareas-net``.
