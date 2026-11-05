# SocioClub — Club Social y Deportivo Los Algarrobos

Trabajo Práctico Integrador · Taller de Construcción de Software · Universidad Siglo 21

Sistema web de gestión de socios, actividades e inscripciones con tres caras que comparten
una sola base MySQL y un solo backend Spring Boot:

| Carpeta           | Qué es                                   | Tecnología                                   | Estado (semana 2) |
|-------------------|------------------------------------------|----------------------------------------------|-------------------|
| `/sitio-publico`  | Sitio público del club (Parte A)         | HTML5 · CSS3 · JavaScript                    | ✅ Completo        |
| `/backend`        | Sistema de secretaría (Parte B) + API (C)| Spring Boot · Thymeleaf · JPA · MySQL        | ✅ Parte B completa · Parte C pendiente |
| `/app-socios`     | App "Mi Club" para socios (Parte D)      | React · React Router                         | 🟡 Rutas y componentes con datos locales |
| `/base-datos`     | Script de creación y datos iniciales     | SQL                                          | ✅                 |
| `/docs`           | Plan de trabajo, capturas, informe       | Markdown / PDF                               | 🟡 Plan de trabajo |

## Integrantes

| Nombre | Parte a cargo |
|--------|---------------|
|  | Parte A — Sitio público |
| Francisco Amuchástegui | Parte B — Backend / Thymeleaf (expone esta parte) |
|  | Parte B — Reglas de negocio / JPA |
|  | Parte D — App React |
|  | Parte C/E — API REST y pruebas |

---

## 1. Requisitos

- **Java 17 a 23** y **Maven 3.8+** (`java -version`, `mvn -version`).
  Spring Boot 3.3 no soporta JDK 24 o superior. Si `mvn -version` muestra un JDK más nuevo
  (por ejemplo el `openjdk` de Homebrew), apuntá Maven a un JDK 17/18/21 con `JAVA_HOME`:

  ```bash
  export JAVA_HOME=$(/usr/libexec/java_home -v 18)   # macOS; en Windows: set JAVA_HOME=C:\...\jdk-17
  ```
- **MySQL 8** corriendo en `localhost:3306` (instalado localmente o en Docker, ver sección 2)
- **Node.js 18 o superior** y npm (`node -v`)
- Un navegador moderno (Chrome, Firefox, Edge)

## 2. Crear la base de datos

Opción 0 — MySQL en Docker (si no tenés MySQL instalado o el local no arranca):

```bash
docker run -d --name socioclub-mysql -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 -v socioclub-mysql-data:/var/lib/mysql mysql:8.4
```

Queda con usuario `root` / clave `root`, que es lo que espera el backend. Las próximas veces
alcanza con `docker start socioclub-mysql`.

Opción A — dejar que el backend la cree (más simple):

- El `application.properties` usa `createDatabaseIfNotExist=true` y `ddl-auto=update`, así que
  al levantar el backend se crea el esquema `socioclub`, las tres tablas y se cargan los datos
  iniciales desde `backend/src/main/resources/data.sql`.

Opción B — con el script SQL:

```bash
mysql -u root -p < base-datos/socioclub.sql
```

Los datos iniciales se cargan con `INSERT IGNORE`: si la fila ya existe no se vuelve a insertar,
así que reiniciar el backend **no** deshace las inscripciones que se hayan cargado. Para volver al
estado inicial (Yoga con 1 lugar libre) borrá la base y dejá que el backend la cree de nuevo:

```bash
mysql -u root -proot -e "DROP DATABASE socioclub;"
```

(con Docker: `docker exec socioclub-mysql mysql -uroot -proot -e "DROP DATABASE socioclub;"`).

En ambos casos, si tu usuario/clave de MySQL no es `root`/`root`, editá
`backend/src/main/resources/application.properties`:

```properties
spring.datasource.username=root
spring.datasource.password=root
```

## 3. Levantar el backend (secretaría)

```bash
cd backend
mvn spring-boot:run
```

Abrir <http://localhost:8080>. Pantallas disponibles:

| URL                                   | Qué hace                                                  |
|---------------------------------------|-----------------------------------------------------------|
| `/`                                   | Panel con ocupación de cada actividad                     |
| `/actividades`                        | Listado con cupo disponible, editar, eliminar             |
| `/actividades/nueva`                  | Alta de actividad                                         |
| `/actividades/{id}/inscriptos`        | Inscriptos activos de una actividad y baja (RF7, RF9)     |
| `/socios`                             | Listado paginado (10 por página) con búsqueda por apellido|
| `/socios/nuevo`                       | Alta de socio                                             |
| `/socios/{id}`                        | Ficha del socio con sus inscripciones                     |
| `/inscripciones/nueva`                | Inscribir socio a actividad aplicando RN1 a RN5           |

Para correr las pruebas unitarias del servicio de inscripciones (RN1 a RN5 con Mockito):

```bash
cd backend
mvn test
```

## 4. Levantar el sitio público

No necesita servidor: abrir `sitio-publico/index.html` en el navegador (doble clic) o, mejor,
con la extensión *Live Server* de VS Code para que `history.back()` y los parámetros de URL
funcionen igual que en producción. Sin VS Code, cualquier servidor estático sirve:

```bash
python3 -m http.server 5500 --directory sitio-publico
```

y abrir <http://localhost:5500>.

## 5. Levantar la app React "Mi Club"

```bash
cd app-socios
npm install
npm start
```

Abrir <http://localhost:3000>. Si el puerto 3000 está ocupado por otro programa, arrancarla en
otro puerto con `PORT=3001 npm start` (en Windows: `set PORT=3001 && npm start`).

> El socio que ingresó se guarda en un contexto de React en memoria: si se recarga la página o
> se escribe una URL a mano, la app vuelve a la pantalla de ingreso.

En esta etapa (semana 2) la app trabaja con **datos locales**
(`src/datos/datosLocales.js`) a través de `src/servicios/api.js`; en la semana 3 ese archivo
pasa a usar Axios contra `http://localhost:8080/api` sin tocar los componentes.

Pruebas con Jest (una sola corrida, sin modo interactivo):

```bash
CI=true npm test -- --watchAll=false
```

## 6. Usuarios / DNI de prueba

No hay contraseñas: el socio ingresa a la app solo con su DNI (RF8).

| DNI        | Socio               | Edad | Cuota   | Caso que cubre                                       |
|------------|---------------------|------|---------|------------------------------------------------------|
| `30111222` | Juan Gómez          | 41   | AL_DIA  | Tiene 3 actividades → **RN5** y **RN2** (ya está en Yoga) |
| `34222333` | María Pérez         | 36   | AL_DIA  | 2 actividades → bonificación 10 % (RN6)              |
| `55333444` | Lucas Rodríguez     | 10   | AL_DIA  | Fútbol infantil y natación                           |
| `52444555` | Valentina Fernández | 14   | AL_DIA  | Fuera del rango de Fútbol infantil (6 a 12) → **RN3** |
| `26555666` | Carlos López        | 47   | MOROSO  | Cualquier inscripción → **RN4**                      |
| `33222444` | Martín Álvarez      | 38   | MOROSO  | Cualquier inscripción → **RN4**                      |
| `12666888` | Roberto Suárez      | 67   | AL_DIA  | Fuera del rango de Funcional (18 a 65) → **RN3**     |
| `47999222` | Mariana Acosta      | 20   | AL_DIA  | Sin actividades → mensaje "sin actividades"          |
| `39111444` | Gabriel Núñez       | 29   | AL_DIA  | Toma el último lugar de Yoga                         |
| `44222555` | Lucía Herrera       | 23   | AL_DIA  | Con Yoga sin cupo → **RN1**                          |

**Yoga** arranca con 14 inscriptos activos de 15: queda **1 lugar**. Al inscribir a Gabriel Núñez
el cupo queda en 0 y la siguiente inscripción (por ejemplo Lucía Herrera) es rechazada por **RN1**.
Los demás adultos ya están en Yoga, por lo que con ellos aplica RN2.

## 7. Reglas de negocio y dónde están

| Regla | Qué valida                                   | Dónde                                                              |
|-------|----------------------------------------------|--------------------------------------------------------------------|
| RN1   | Cupo máximo                                  | `InscripcionService.inscribir` (backend)                           |
| RN2   | Sin duplicados                               | `InscripcionService.inscribir`                                     |
| RN3   | Edad dentro del rango                        | `InscripcionService.inscribir` + `Actividad.esAptaParaEdad`        |
| RN4   | Socio moroso no se inscribe                  | `InscripcionService.inscribir`                                     |
| RN5   | Máximo 3 actividades                         | `InscripcionService.inscribir` (constante `MAXIMO_ACTIVIDADES_POR_SOCIO`) |
| RN6   | Bonificación 10 % / 15 %                     | `app-socios/src/utils/cuota.js` (probada con Jest)                 |
| RN7   | DNI de 7 u 8 dígitos                         | `sitio-publico/js/script.js` y `app-socios/src/utils/dni.js`       |

Las reglas RN1 a RN5 están **solo en la capa de servicio**: el controlador Thymeleaf y, en la
semana 3, el controlador REST llaman al mismo `InscripcionService`.

## 8. Estructura del backend

```
backend/src/main/java/com/losalgarrobos/socioclub/
├── SocioclubApplication.java
├── model/          Socio, Actividad, Inscripcion, EstadoCuota, EstadoInscripcion   (entidades JPA)
├── repository/     SocioRepository, ActividadRepository, InscripcionRepository     (JpaRepository)
├── service/        SocioService, ActividadService, InscripcionService (RN1-RN5), excepciones
└── controller/     HomeController, ActividadController, SocioController, InscripcionController
backend/src/main/resources/
├── application.properties
├── data.sql                       datos iniciales (sección 3.3)
├── templates/                     vistas Thymeleaf (fragmentos/layout.html = encabezado y pie comunes)
└── static/css/secretaria.css
```

## 9. Plan de trabajo

Ver [`docs/PLAN_DE_TRABAJO.md`](docs/PLAN_DE_TRABAJO.md).
