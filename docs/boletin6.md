# Boletín 6

## Resumen de commits

| # | Mensaje del commit | Qué se hizo | Autor | Enlace |
|---|--------------------|-------------|-------|--------|
| 1 | `build(deploy): declarada la collection community.docker` | Creación de `deploy/requirements.yml` para fijar la versión `3.7.0` de la collection que aporta los módulos de Docker. | Jiahui | 08132bd00f4df251a3814b00ba75f82d38639342 |
| 2 | `feat(deploy): añadida la flota de servidores simulada` | Creación de `deploy/servidores/docker-compose.yml` con los tres contenedores con SSH que hacen de máquinas destino: `app1`, `app2` y `db1`. | Jiahui | 1bbae375c233d1ce4b2fe67462e7631a27f08bd1 |
| 3 | `feat(deploy): añadido el inventario por roles` | Creación de `deploy/inventory.ini`, que agrupa las máquinas en `app` y `db` y define cómo conectarse a ellas. | Jiahui | df21bbe8ecdf93bc5aa3c21ef52666a4a80af442 |
| 4 | `feat(deploy): añadido el playbook de despliegue` | Creación de `deploy/deploy.yml`, con los plays que preparan Docker y despliegan la base de datos y la API. | Jiahui | bf7ca390f3e95e8aa27252bf254a52ce054ed245 |
| 5 | `feat(deploy): añadidas las credenciales cifradas con Vault` | Creación de `deploy/group_vars/servidores/vault.yml`, cifrado con Ansible Vault, con las credenciales de GHCR y de la base de datos. | Jiahui | b7f311195b1661b6a3c608087ad1552b372dd935 |

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