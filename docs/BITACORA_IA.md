# Bitácora de uso de IA (Anexo C)

Registro del uso de herramientas de IA durante el desarrollo. Va como último capítulo del informe final.

| N.º | Parte | Qué le pedimos a la IA (resumen del prompt) | Qué nos devolvió | Qué estaba mal / qué cambiamos y por qué |
|-----|-------|----------------------------------------------|------------------|-------------------------------------------|
| 1 | A, B, D | Le pasamos el enunciado completo y le pedimos que armara el proyecto hasta el hito de la semana 2 (sitio público, backend Spring Boot con Thymeleaf y reglas RN1–RN5, app React con datos locales). | La estructura de carpetas `/sitio-publico`, `/backend`, `/app-socios`, el código de las tres partes, `data.sql`, README y plan de trabajo. | La IA no pudo compilar el backend en su entorno, así que lo levantamos nosotros. Al hacerlo aparecieron tres problemas que el código generado no contemplaba: Maven tomaba un JDK más nuevo que el soportado por Spring Boot 3.3 (se resolvió fijando `JAVA_HOME` a un JDK 17-23), el MySQL local no arrancaba (se reemplazó por un contenedor Docker) y el puerto 3000 estaba ocupado (la app React corre con `PORT=3001`). El README se actualizó con esos pasos. |

## Decisiones tomadas frente a sugerencias de la IA

- **Sin Lombok**: las entidades tienen getters y setters escritos a mano porque Lombok no se vio
  en la materia.
- **Sin `spring-boot-starter-validation`**: las validaciones de formularios están en la capa de
  servicio con `IllegalArgumentException`, para no agregar una herramienta que no se vio.
- **create-react-app y no Vite**; **react-router-dom 6**; **Axios** (semana 3) y no `fetch`.
- **`src/servicios/api.js` ya existe desde la semana 2** devolviendo Promesas con datos locales,
  para que en la semana 3 solo cambie el interior de cada función.
