# Boletín 6

## Resumen de commits

| # | Mensaje del commit | Qué se hizo | Autor | Enlace |
|---|--------------------|-------------|-------|--------|
| 1 | `build(deploy): declarada la collection community.docker` | Creación de `deploy/requirements.yml` para fijar la versión `3.7.0` de la collection que aporta los módulos de Docker. | Jiahui | 08132bd00f4df251a3814b00ba75f82d38639342 |
| 2 | `feat(deploy): añadida la flota de servidores simulada` | Creación de `deploy/servidores/docker-compose.yml` con los tres contenedores con SSH que hacen de máquinas destino: `app1`, `app2` y `db1`. | Jiahui | 1bbae375c233d1ce4b2fe67462e7631a27f08bd1 |
| 3 | `feat(deploy): añadido el inventario por roles` | Creación de `deploy/inventory.ini`, que agrupa las máquinas en `app` y `db` y define cómo conectarse a ellas. | Jiahui | df21bbe8ecdf93bc5aa3c21ef52666a4a80af442 |
| 4 | `feat(deploy): añadido el playbook de despliegue` | Creación de `deploy/deploy.yml`, con los plays que preparan Docker y despliegan la base de datos y la API. | Jiahui | bf7ca390f3e95e8aa27252bf254a52ce054ed245 |
| 5 | `feat(deploy): añadidas las credenciales cifradas con Vault` | Creación de `deploy/group_vars/servidores/vault.yml`, cifrado con Ansible Vault, con las credenciales de GHCR y de la base de datos. | Jiahui | b7f311195b1661b6a3c608087ad1552b372dd935 |
| 6 | `fix(deploy): cambios en los ficheros de ansible` | Correcciones menores en los ficheros para eliminar contenido subido erroneamente. | Fran | `2e0a9e9` |
| 7 | `feat(deploy): añadir endpoint para consultar la versión de la api` | Creación del endpoint `/api/version` inyectando automáticamente la versión desde el `pom.xml` de Maven para verificar despliegues precisos (`1.0.0` y `1.1.0`). | Fran | `3a50865` |
| 8 | `feat(deploy): nuevo job deploy en release.yml` | Ampliación de `.github/workflows/release.yml`: el job `publish` expone la versión publicada y se añade el job `deploy`, que lanza el playbook desde el runner `self-hosted`. | Jesús | `8af1b38dcc86b05f47c4de34ffd110974d87d7be` |
| 9 | `fix(pom): actualización a la nueva versión` | Actualización de la versión del `pom.xml` a `1.3.0` para que `/api/version` coincida con la del tag desplegado. | Jesús | `d20978c9ceb6915b477a242e64cba8b902cb0893` |

## Detalle por commit

### 1. `build(deploy): declarada la collection community.docker`

- **Qué se hizo:** Se declaró en `deploy/requirements.yml` la dependencia de `community.docker` en su versión `3.7.0`, de forma que cualquier miembro del grupo o el pipeline instale exactamente la misma versión.
- **Autor:** Jiahui
- **Relación con el boletín:** [Parte A (Instalar Ansible y sus dependencias)](#parte-a---instalar-ansible-y-sus-dependencias).
- **Uso de IA:** N/A.

### 2. `feat(deploy): añadida la flota de servidores simulada`

- **Qué se hizo:** Se definió la flota de tres contenedores Linux con SSH, cada uno accesible desde un puerto distinto del anfitrión, y se comprobó el acceso manual a cada máquina.
- **Autor:** Jiahui
- **Relación con el boletín:** [Parte B (Preparar la flota de servidores destino)](#parte-b---preparar-la-flota-de-servidores-destino).
- **Uso de IA:** N/A.

### 3. `feat(deploy): añadido el inventario por roles`

- **Qué se hizo:** Se creó el inventario con los grupos `app` y `db`, reunidos en el grupo `servidores`, y se verificó la conexión de Ansible con toda la flota mediante el módulo `ping`.
- **Autor:** Jiahui
- **Relación con el boletín:** [Parte C (El inventario)](#parte-c---el-inventario).
- **Uso de IA:** N/A.

### 4. `feat(deploy): añadido el playbook de despliegue`

- **Qué se hizo:** Se creó el playbook multi-host, adaptando la imagen a la publicada por el grupo en el Boletín 5, y se validó su sintaxis y su lista de tareas.
- **Autor:** Jiahui
- **Relación con el boletín:** [Parte D (El playbook de despliegue)](#parte-d---el-playbook-de-despliegue).
- **Uso de IA:** N/A.

### 5. `feat(deploy): añadidas las credenciales cifradas con Vault`

- **Qué se hizo:** Se generó un token de GitHub de solo lectura de paquetes y se guardaron las credenciales del despliegue en un fichero cifrado con Ansible Vault, comprobando que el fichero versionado es ilegible.
- **Autor:** Jiahui
- **Relación con el boletín:** [Parte E (Proteger las credenciales con Ansible Vault)](#parte-e---proteger-las-credenciales-con-ansible-vault).
- **Uso de IA:** N/A.

### 6. `fix(deploy): cambios en los ficheros de ansible`

- **Qué se hizo:** Se realizaron correcciones menores en las tareas de despliegue para eliminar contenido subido erroneamente.
- **Autor:** Fran
- **Relación con el boletín:** [Parte F (Verificación final de despliegues y control de versiones)](#parte-f---verificación-final-de-despliegues-y-control-de-versiones).
- **Uso de IA:** No.

### 7. `feat(deploy): añadir endpoint para consultar la versión de la api`

- **Qué se hizo:** Se implementó el controlador REST `/api/version` combinado con el filtrado de recursos de Maven (`@project.version@`), permitiendo comprobar inequívocamente la versión activa de la aplicación tras cada despliegue.
- **Autor:** Fran
- **Relación con el boletín:** [Parte F (Verificación final de despliegues y control de versiones)](#parte-f---verificación-final-de-despliegues-y-control-de-versiones).
- **Uso de IA:** No.

### 8. `feat(deploy): nuevo job deploy en release.yml`

- **Qué se hizo:** Se añadió a `publish` la salida `version` y se creó el job `deploy`, que solo se ejecuta si la imagen se ha publicado correctamente y únicamente con tags `vX.Y.Z`. Se ejecuta en el runner ``self-hosted``, con aprobación manual del entorno `produccion`, y lanzando `ansible-playbook` con la versión publicada.
- **Autor:** Jesús
- **Relación con el boletín:** [Parte G.3 (Ampliar el release.yml)](#g3---ampliar-el-releaseyml).
- **Uso de IA:** N/A.

### 9. `fix(pom): actualización a la nueva versión`

- **Qué se hizo:** Se actualizó la versión del `pom.xml` a `1.3.0`, ya que `/api/version` lee la versión de este fichero y devolvía `1.1.0` tras desplegar la imagen `1.2.0`. Con ello, la versión que informa la aplicación coincide con la del tag publicado.
- **Autor:** Jesús
- **Relación con el boletín:** [Parte G.4 (Pruebas de extremo a extremo)](#g4---pruebas-de-extremo-a-extremo).
- **Uso de IA:** N/A.

## Descripción narrativa de lo hecho en la sesión y relación con los commits

### Parte A - Instalar Ansible y sus dependencias

Se utiliza Ansible con `core 2.16.19`, instalado con `pipx`. No se instala con `apt`, ya que la indicación del boletín corresponde a Ubuntu 24.04 y en Ubuntu 26.04 se obtendría otra versión.

La dependencia se declara en `deploy/requirements.yml` para que la instalación sea reproducible.

La collection se instala a partir de ese fichero y se comprueba la versión disponible:

```bash
ansible-galaxy collection install -r deploy/requirements.yml
ansible-galaxy collection list community.docker
```

En la salida aparecen dos versiones. La `3.8.0` viene incluida con el paquete de Ansible instalado mediante `pipx`, mientras que la `3.7.0` es la instalada a partir del fichero de requisitos. Ansible busca primero en `~/.ansible/collections`, por lo que se utiliza la `3.7.0`.

![Versiones de la collection community.docker](./capturas/JH-b6-A(1).png)

### Parte B - Preparar la flota de servidores destino

En lugar de contratar servidores en la nube, se utilizan tres contenedores Linux con SSH que hacen de máquinas destino: dos para la aplicación (`app1` y `app2`) y uno para la base de datos (`db1`). Se definen en `deploy/servidores/docker-compose.yml`, que no tiene relación con el `docker-compose.yml` de la aplicación del Boletín 3. Los contenedores se ejecutan con `privileged: true` porque cada uno correrá su propio Docker en su interior.

La flota se levanta y se comprueba su estado con los siguientes comandos:

```bash
docker compose -f deploy/servidores/docker-compose.yml up -d
docker compose -f deploy/servidores/docker-compose.yml ps
```

Los tres contenedores quedan en ejecución y con sus puertos publicados en el anfitrión.

![Flota de servidores levantada](./capturas/JH-b6-B(1).png)

Antes de usar Ansible, se comprueba el acceso manual a cada máquina con `ssh root@localhost -p <puerto>` y se ejecuta `hostname` en cada una. Cada conexión devuelve un nombre distinto, lo que confirma que cada puerto llega a una máquina diferente.

Se observa además que las tres máquinas presentan la misma huella ED25519. Esto se debe a que todas parten de la misma imagen, que trae las claves de host ya generadas, por lo que SSH detecta que la clave ya es conocida por otro nombre. En un entorno real, cada servidor tendría su propia clave.

![Acceso por SSH a las tres máquinas](./capturas/JH-b6-B(2).png)

### Parte C - El inventario

El inventario `deploy/inventory.ini` indica a Ansible qué máquinas existen, cómo llegar a ellas y qué rol tiene cada una. Sus secciones cumplen las siguientes funciones:

- ``[app]`` y ``[db]``: agrupan las máquinas por rol. Todas apuntan a `localhost` y se distinguen por el puerto.
- ``[servidores:children]``: crea un grupo que contiene a `app` y `db`, para poder dirigirse a toda la flota a la vez.
- ``[servidores:vars]``: define las variables comunes, como el intérprete de Python, la contraseña y las opciones de SSH.

La contraseña `root` queda en claro en el inventario. Se acepta porque son máquinas de laboratorio en el propio ordenador y sin datos reales. En servidores reales se utilizarían claves SSH.

Para que Ansible pueda enviar la contraseña por SSH se necesita `sshpass`, que ya estaba instalado en el sistema. Se comprueba la estructura de grupos y, después, la conexión con toda la flota:

```bash
ansible-inventory -i deploy/inventory.ini --graph
ansible -i deploy/inventory.ini servidores -m ping
```

El módulo `ping` de Ansible no es un ping de red: entra por SSH y ejecuta un módulo de Python en cada máquina. Las tres máquinas responden `pong`, por lo que Ansible ya controla la flota.

![Grupos del inventario y ping a la flota](./capturas/JH-b6-C(1).png)

### Parte D - El playbook de despliegue

El playbook `deploy/deploy.yml` se copia del boletín y se adapta la imagen a la publicada por el grupo en el Boletín 5. El nombre se escribe en minúsculas porque GHCR lo exige así:

```yaml
imagen: "ghcr.io/i-fj-i/grupo1-pc-2026:{{ version_app }}"
```

El playbook se divide en tres plays, ordenados de forma que la API encuentre disponibles Docker y la base de datos:

| Play | Máquinas | Qué hace |
|------|----------|----------|
| `Preparar Docker en toda la flota` | `servidores` | Instala Docker y el SDK de Python y arranca `dockerd` a mano, ya que los contenedores no tienen systemd. |
| `Desplegar la base de datos` | `db` | Arranca PostgreSQL con sus datos en un volumen. |
| `Desplegar la API de tareas` | `app` | Inicia sesión en GHCR, descarga la imagen, arranca la API y espera a que esté sana. |

Los contenedores internos utilizan `network_mode: host`, de modo que comparten la red de la máquina que los aloja. Así, la API puede resolver el nombre `db1` y su puerto queda expuesto directamente en la máquina. El host de la base de datos no se escribe a mano, sino que se obtiene con `groups['db'][0]`, el primer host del grupo `db`.

La versión a desplegar se indica con la variable `version_app`. Se utiliza siempre un tag concreto y nunca `latest`, ya que con este último no se podría saber qué versión está corriendo, reproducir un despliegue pasado ni volver atrás.

Como el playbook necesita las credenciales de la Parte E, en este punto no se ejecuta, sino que se valida su sintaxis y se consulta la lista de tareas:

```bash
ansible-playbook -i deploy/inventory.ini deploy/deploy.yml --syntax-check
ansible-playbook -i deploy/inventory.ini deploy/deploy.yml --list-tasks
```

La sintaxis es correcta y aparecen los tres plays con sus tareas. Estas comprobaciones no se conectan a las máquinas, por lo que no garantizan que el despliegue funcione.

![Validación del playbook](./capturas/JH-b6-D(1).png)

### Parte E - Proteger las credenciales con Ansible Vault

Las credenciales nunca se escriben en el playbook ni se pasan por `--extra-vars`, ya que quedarían en el historial de la shell y en la tabla de procesos. En su lugar, se guardan cifradas con Ansible Vault.

En primer lugar, se genera en GitHub un token clásico con el permiso `read:packages`. Ansible únicamente descarga la imagen, por lo que no necesita más permisos.

A continuación, se crea el fichero cifrado. Al crearlo se define una contraseña maestra, que será necesaria para descifrarlo:

```bash
mkdir -p deploy/group_vars/servidores
EDITOR="vim -n -i NONE" ansible-vault create deploy/group_vars/servidores/vault.yml
```

Las opciones de `vim` evitan que el contenido en claro quede en un fichero de intercambio o en el historial del editor. Ansible carga automáticamente la carpeta `group_vars/` que está junto al inventario, y `servidores` coincide con el nombre del grupo, por lo que las tres máquinas reciben estas variables.

Por último, se comprueba con `cat` que el fichero versionado es ilegible. Su contenido empieza por la cabecera `$ANSIBLE_VAULT;1.1;AES256`, seguida únicamente del texto cifrado. Para modificarlo más adelante se utiliza `ansible-vault edit`.

![Fichero cifrado con Ansible Vault](./capturas/JH-b6-E(1).png)

### Parte F - Verificación final de despliegues y control de versiones

Para comprobar el correcto funcionamiento del despliegue y del control de versiones, en primer lugar se lanza el playbook indicando la versión `1.0.0` mediante la variable `version_app`:

```bash
ansible-playbook -i deploy/inventory.ini deploy/deploy.yml \
  --ask-vault-pass --extra-vars "version_app=1.0.0"

```

El playbook completa su ejecución correctamente para la versión inicial.

![Ejecución inicial](./capturas/FRAN-b6-F(1).png)

Observamos como ambas réplicas comunican con la misma base de datos que, actualmente, está vacía.

![Respuesta api](./capturas/FRAN-b6-F(2).png)

Al ejecutar de nuevo el playbook podemos apreciar como ninguna de las máquinas ha cambiado.

![Idempotencia](./capturas/FRAN-b6-F(3).png)

Tras desarrollar un nuevo endpoint para la nueva versión, configurar la inyección de la versión desde el `pom.xml` y generar la nueva etiqueta (`v1.1.0`), subimos la nueva versión.

A continuación, se ejecuta de nuevo el playbook actualizando la variable a la nueva versión (`1.1.0`):

```bash
ansible-playbook -i deploy/inventory.ini deploy/deploy.yml \
  --ask-vault-pass --extra-vars "version_app=1.1.0"

```

En la primera ejecución del playbook falla el *smoke test* pero las máquinas se modifican correctamente.

![Smoke test fallido](./capturas/FRAN-b6-F(4).png)

Tras una segunda ejecución del playbook observamos como el *smoke test* finaliza correctamente y nos confirma que la API de la versión 1.1.0 está sana y respondiendo.

![Smoke test fallido](./capturas/FRAN-b6-F(5).png)

Se consulta el nuevo endpoint (`/api/version`) directamente con `curl` a las direcciones IP de los contenedores `app1` y `app2`. En ambos casos, devuelven correctamente el JSON con la versión `1.1.0` activa, validando así que el despliegue se ha realizado con éxito y precisión.

![Consulta de version 1](./capturas/FRAN-b6-F(6).png)

Finalmente volvemos a ejecutar el playbook para volver a la versión 1.0.0 y volvemos a probar el endpoint implementado en la versión 1.1.0 para demostrar que no se encuentra disponible en la versión que estamos utilizando actualmente.

![Rollback](./capturas/FRAN-b6-F(7).png)

![Consulta de version 2](./capturas/FRAN-b6-F(8).png)

### Parte G — Disparar el despliegue desde GitHub Actions (cierre CD)

#### G.1 — Registrar tu ordenador como runner

Para automatizar el despliegue de forma que cada publicación de una nueva versión dispare Ansible, se necesita un entorno de ejecución (*runner*) que tenga acceso por SSH a la flota de servidores destino. Al estar utilizando máquinas virtuales alojadas en el propio ordenador físico, se registra este mismo equipo como un runner autoalojado (*self-hosted runner*) en GitHub.

Se accede a **Settings → Actions → Runners → New self-hosted runner** y se selecciona la arquitectura correspondiente (macOS ARM64 en mi caso). A continuación, se descargan y configuran los binarios del runner en una terminal:

```bash
mkdir actions-runner && cd actions-runner
curl -o actions-runner-osx-arm64-2.338.2.tar.gz -L https://github.com/actions/runner/releases/download/...
tar xzf ./actions-runner-osx-arm64-2.338.2.tar.gz
./config.sh --url https://github.com/I-FJ-I/grupo1-PC-2026 --token <TOKEN>

```

Se aceptan los valores por defecto durante la configuración y se arranca el proceso con `./run.sh`, dejándolo a la escucha de nuevos trabajos.

![Configuración del runner](./capturas/FRAN-b6-G(1).png)

Tras arrancar el script, se verifica en la interfaz web de GitHub que el runner `MacBook-Pro-de-Fran` aparece correctamente registrado, con las etiquetas `self-hosted`, `macOS` y `ARM64`, y en estado **Idle** (ocioso), listo para recibir peticiones.

![Estado del runner](./capturas/FRAN-b6-G(2).png)

#### G.2 — Entorno protegido y secreto del Vault

Para evitar que los despliegues a los servidores se ejecuten de forma automática sin supervisión, se configura un entorno protegido que requiera aprobación manual.

En **Settings → Environments → New environment**, se crea un entorno llamado `produccion`. Dentro de su configuración, se activa la regla **Required reviewers** y se añaden los usuarios correspondientes para que autoricen el flujo de trabajo antes de que pase al runner.

Finalmente, en la sección **Environment secrets** de esa misma pantalla, se añade un nuevo secreto denominado `VAULT_PASS`. Este secreto contiene la contraseña maestra necesaria para que el *playbook* de Ansible pueda descifrar el archivo `vault.yml` durante el despliegue automático, garantizando así la seguridad de las credenciales inyectadas sin exponerlas en el código.

![Nuevo entorno](./capturas/FRAN-b6-G(3).png)


#### G.3 - Ampliar el ``release.yml``

Una vez se ha preparado el entorno de ejecución y se ha protegido mediante aprobación manual, se amplía el workflow ``release.yml`` para que, cada vez que se publique una nueva versión, esta se despliegue automáticamente en la flota con Ansible.

Para ello, se añade el nuevo job ``deploy`` al workflow, justo después de ``publish``:

```yaml
name: Release

on:
  push:
    branches: [ main ]
    tags: [ "v*.*.*" ]

permissions:
  contents: read
  packages: write

jobs:
  publish:
    runs-on: ubuntu-latest
    timeout-minutes: 20
    outputs:
      version: ${{ steps.meta.outputs.version }}   # NUEVO: 1.0.0 cuando el tag es v1.0.0
    steps:
      - uses: actions/checkout@v4

      - name: Login en GHCR
        uses: docker/login-action@v3
        with:
          registry: ghcr.io
          username: ${{ github.actor }}
          password: ${{ secrets.GITHUB_TOKEN }}

      - name: Metadatos (tags)
        id: meta
        uses: docker/metadata-action@v5
        with:
          images: ghcr.io/${{ github.repository }}
          tags: |
            type=sha,prefix=sha-
            type=semver,pattern={{version}}
            type=semver,pattern={{major}}.{{minor}}
            type=semver,pattern={{major}}
            type=raw,value=latest,enable={{is_default_branch}}

      - name: Construir y publicar
        uses: docker/build-push-action@v6
        with:
          context: .
          push: true
          tags: ${{ steps.meta.outputs.tags }}
          labels: ${{ steps.meta.outputs.labels }}

  # NUEVO: todo lo que sigue es lo que añades en esta parte
  deploy:
    needs: publish                                  # solo si la imagen se publicó bien
    # solo al publicar una versión (tag vX.Y.Z), no en cada push a main
    if: startsWith(github.ref, 'refs/tags/v')
    runs-on: self-hosted                            # tu ordenador
    environment: produccion                         # se detiene aquí hasta que un reviewer lo apruebe
    timeout-minutes: 15
    steps:
      - uses: actions/checkout@v4

      - name: Instalar la collection de Ansible
        run: ansible-galaxy collection install -r deploy/requirements.yml

      - name: Desplegar con Ansible
        env:
          VAULT_PASS: ${{ secrets.VAULT_PASS }}
          VERSION: ${{ needs.publish.outputs.version }}
        run: |
          VAULT_FILE="$(mktemp)"
          trap 'rm -f "$VAULT_FILE"' EXIT
          printf '%s' "$VAULT_PASS" > "$VAULT_FILE"
          ansible-playbook -i deploy/inventory.ini deploy/deploy.yml \
            --vault-password-file "$VAULT_FILE" \
            --extra-vars "version_app=$VERSION"

```

Este nuevo job se ejecuta únicamente si la imagen se ha publicado correctamente (``needs: publish``). En el caso de que se haya publicado con éxito, solo se lanza el despliegue cuando se publica una nueva versión con el formato ``vX.Y.Z``, y no en cada push a la rama ``main``, como ocurre en el job de ``publish`` o en el workflow ``pages.yml``. Para que este job se ejecute en el equipo registrado como *self-hosted runner* se utiliza ``runs-on: self-hosted``. De esta forma, ``deploy`` puede conectarse por SSH a ``app1`` y ``app2``.

Para que este job se ejecute en el entorno protegido configurado en el apartado anterior, se especifica ``environment: produccion``. De esta forma, se tiene acceso al secreto ``VAULT_PASS`` y el job queda pausado hasta que sea aprobado manualmente. Para mantener una trazabilidad desde la aplicación en ejecución hasta el tag de Git que la produjo, la versión desplegada es la misma que el tag de la imagen (``version_app=$VERSION``). Para que ``deploy`` reciba la nueva versión publicada, es necesario añadir en el job ``publish`` la sección ``outputs``.

Por último, el job se divide en los siguientes pasos:

1. Instala la collection de Ansible.
2. Crea un fichero temporal y guarda su ruta en una variable.
3. Programa con ``trap`` el borrado del fichero temporal cuando el script falle/finalice.
4. Guarda en el fichero temporal la contraseña de Vault. 
5. Ejecuta el playbook sobre los servidores de la flota especificando el fichero que contiene la contraseña Vault e indicando a través de variables de entorno la versión a desplegar.

#### G.4 - Pruebas de extremo a extremo

Antes de empezar, es necesario tener lanzada la flota completa y el runner. Una vez ambos estén en ejecución, se publica una versión nueva ``v1.2.0``.

```bash
git tag -a v1.2.0 -m "Adición de despliegue automático de nuevas versiones"
git push origin v1.2.0
```

![Nueva versión publicada](./capturas/JESUS-B6-G(4).png)

Al publicarse esta nueva versión, se lanza automáticamente el job ``deploy``. En la pestaña de despliegues del entorno ``produccion`` se observa el historial de la versión ``v1.2.0``. El despliegue quedó asociado al merge realizado para subir el ``release.yml`` actualizado. 

![Release lanzado al publicarse la nueva versión](./capturas/JESUS-B6-G(5).png)


Los dos primeros intentos fallaron debido a un problema en la flota (``db1`` no se lanzaba con éxito). Tras corregir el error, el tercer intento se completó correctamente. El tercer intento, aunque finalizó con éxito, no solicitó aprobación manual del job ``deploy``. Esto es, debido a que no se configuró la opción ``Required reviewers`` en el momento de la ejecución. Una vez se ha configurado, se lanzó de nuevo el workflow ``release`` completo, requiriendo ahora sí de una aprobación manual.

![Deploy pendiente de confirmar](./capturas/JESUS-B6-G(6).png)

![Aprobación manual del deploy](./capturas/JESUS-B6-G(7).png)

Una vez se ha aprobado, el job ``deploy`` finaliza con éxito.

![Aprobación manual del deploy](./capturas/JESUS-B6-G(8).png)

Para verificar que la nueva versión está funcionando, se abre una nueva terminal y se realizan peticiones a las dos máquinas destino ``app1`` y ``app2``. Se consultan los nuevos endpoints creados en el apartado F:

![Curl con version anticuada](./capturas/JESUS-B6-G(9).png)

Al comprobar el despliegue de la versión ``v1.2.0``, el endpoint ``/api/version`` devolvió ``1.1.0``. Esto se debe a que el endpoint no lee la versión del tag de Git ni de la imagen, sino del ``pom.xml``, que Maven inyecta en ``application.properties`` al compilar mediante ``@project.version@``. Como el ``pom.xml`` no se actualizó antes de crear el tag, la imagen se publicó etiquetada como ``1.2.0``, pero la aplicación que contiene seguía declarando la versión 1.1.0.

Para corregirlo se actualiza la versión del ``pom.xml`` a ``1.3.0`` y se publica una nueva versión con el tag ``v1.3.0``, de modo que la versión de la imagen y la que informa la aplicación coincidan.

![PR con el fix del pom.xml](./capturas/JESUS-B6-G(10).png)

![Release ejecutado con éxito](./capturas/JESUS-B6-G(11).png)

Una vez se ha publicado la nueva versión con éxito y se ha desplegado en la flota completa, se verifica si devuelve la versión correcta.

![Curl con version correcta](./capturas/JESUS-B6-G(12).png)

Ahora ``/api/version`` devuelve ``1.3.0`` en ambas réplicas, la misma versión que el tag publicado. Esto confirma que el flujo completo funciona de extremo a extremo, desde el ``git push`` del tag hasta la aplicación corriendo en ``app1`` y ``app2``.

Para terminar, se muestra la arquitectura completa que se ha montado en este boletín. En el diagrama se indica dónde se ejecuta cada pieza y cómo se conectan entre sí. Además, cada flecha lleva un número que marca el orden de los pasos desde que se publica el tag ``vX.Y.Z`` hasta que la aplicación queda corriendo en las dos réplicas, y el protocolo que utiliza.

![Arquitectura completa del despliegue](./capturas/JESUS-B6-G(13).png)

Los pasos que se numeran en el diagrama son los siguientes:

1. Se publica el tag ``vX.Y.Z`` en el repositorio con ``git push`` (HTTPS). La conexión la inicia el desarrollador.
2. GitHub detecta el tag y lanza el workflow ``release``.
3. El job ``publish``, que se ejecuta en un runner de GitHub, construye la imagen y la sube a GHCR (HTTPS) con la versión del tag.
4. El job ``deploy`` queda pausado en el entorno ``produccion`` hasta que se aprueba manualmente.
5. El runner autoalojado consulta GitHub (HTTPS) y es él quien inicia la conexión, por lo que no es necesario abrir ningún puerto. Una vez aprobado el despliegue, recibe el trabajo junto con el secreto ``VAULT_PASS``.
6. El runner ejecuta Ansible, que actúa como controlador desde el propio ordenador.
7. Ansible inicia una conexión SSH con ``app1``, ``app2`` y ``db1`` a través de los puertos ``2223``, ``2224`` y ``2225``. Estos tres contenedores los ejecuta el Docker del anfitrión.
8. Dentro de ``app1`` y ``app2``, su Docker interno descarga la imagen de GHCR (HTTPS) y arranca la API.
9. La API de cada réplica se conecta con PostgreSQL, que corre en ``db1`` (TCP).
10. Por último, se comprueba el despliegue con ``curl`` (HTTP) a los puertos ``8081`` y ``8082`` del anfitrión, que redirigen al puerto ``8080`` de cada API.
