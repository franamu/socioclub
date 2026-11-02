# Bitácora de uso de IA (Anexo C)

Se completa a lo largo del cuatrimestre y va como último capítulo del informe.
Mínimo 5 entradas; en al menos 3 tiene que haber un error o ajuste detectado por el grupo.

| N.º | Parte | Qué le pedimos a la IA (resumen del prompt) | Qué nos devolvió | Qué estaba mal / qué cambiamos y por qué |
|-----|-------|----------------------------------------------|------------------|-------------------------------------------|
| 1 | A, B, D | Le pasamos el enunciado completo y le pedimos que armara el proyecto hasta el hito de la semana 2 (sitio público, backend Spring Boot con Thymeleaf y reglas RN1–RN5, app React con datos locales). | La estructura de carpetas `/sitio-publico`, `/backend`, `/app-socios`, el código de las tres partes, `data.sql`, README y plan de trabajo. | La IA no pudo compilar el backend en su entorno (sin acceso a Maven Central), así que lo compilamos y probamos nosotros con `mvn spring-boot:run` contra MySQL. _(Completar: qué falló al levantarlo, si falló algo.)_ |
| 2 | B | _(ej.: le pedimos que explicara línea por línea `InscripcionService.inscribir` y por qué RN4 se chequea antes que RN1)_ | | |
| 3 | D | _(ej.: le preguntamos por qué el socio está en un contexto y no en props)_ | | |
| 4 | | | | |
| 5 | | | | |

## Decisiones tomadas frente a sugerencias de la IA

- **Sin Lombok**: las entidades tienen getters y setters escritos a mano porque Lombok no se vio
  en la materia.
- **Sin `spring-boot-starter-validation`**: las validaciones de formularios están en la capa de
  servicio con `IllegalArgumentException`, para no agregar una herramienta que no se vio.
- **create-react-app y no Vite**; **react-router-dom 6**; **Axios** (semana 3) y no `fetch`.
- **`src/servicios/api.js` ya existe desde la semana 2** devolviendo Promesas con datos locales,
  para que en la semana 3 solo cambie el interior de cada función.
