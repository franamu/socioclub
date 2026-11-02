# Plan de trabajo — SocioClub

Nuestro grupo expone en la **semana 2**, así que el hito a cumplir es:

> Todo lo de la semana 1 + Parte B completa (socios paginados con búsqueda, inscripciones con
> RN1 a RN5) + Parte D iniciada (app React con rutas y componentes funcionando con datos
> locales, usando props, estado y contexto). Avance aprox. 70 %.

## Estado al momento de la exposición (semana 2)

| Parte | Requisito del enunciado                                                | Estado |
|-------|------------------------------------------------------------------------|--------|
| A     | 3 páginas, HTML5 semántico, meta viewport, imágenes con alt            | ✅ |
| A     | CSS externo con selectores de tipo, clase, ID y atributo               | ✅ |
| A     | Responsivo: media queries (tarjetas en 1 columna en celular)           | ✅ |
| A     | Catálogo generado con JS desde arreglo de objetos `Actividad` con método `esAptaParaEdad` | ✅ |
| A     | Filtro por disciplina y edad con `addEventListener` y manipulación del DOM | ✅ |
| A     | Formulario validado (obligatorios, email, DNI RN7), errores junto al campo, sin `alert()` | ✅ |
| A     | Objetos del navegador: `history.back()`, `location.search`, `navigator.language`, `screen` | ✅ |
| B     | Proyecto Maven con Spring Web, Thymeleaf, Data JPA, MySQL Driver        | ✅ |
| B     | Capas `model` / `repository` / `service` / `controller`                | ✅ |
| B     | Entidades Socio, Actividad, Inscripcion con `@OneToMany` / `@ManyToOne` | ✅ |
| B     | Métodos derivados (`findByApellidoContainingIgnoreCase`, `findByDisciplina`, …) y `@Query` | ✅ |
| B     | Paginación de socios con `Pageable`                                    | ✅ |
| B     | RN1 a RN5 en `InscripcionService`, mensajes en la vista                | ✅ |
| B     | Vistas con `th:each`, `th:text`, `th:if`, `th:object`/`th:field`, fragmentos | ✅ |
| B     | MySQL configurado + datos iniciales (8 actividades, 20 socios, 2 morosos, Yoga casi llena) | ✅ |
| D     | CRA, 4 rutas con `BrowserRouter`                                        | ✅ |
| D     | Componentes Encabezado, Pie, TarjetaActividad, ListaActividades, DetalleActividad, MisActividades, ResumenCuota | ✅ |
| D     | Props con PropTypes (TarjetaActividad, ResumenCuota)                   | ✅ |
| D     | `useState` / `useEffect`                                               | ✅ |
| D     | Socio logueado en `createContext` / `useContext`                       | ✅ |
| D     | `map` con `key`, renderizado condicional ("Sin cupo", botón deshabilitado, sin actividades) | ✅ |
| D     | CSS externo + clase dinámica (`tarjeta--sin-cupo`)                     | ✅ |
| D     | Comunicación centralizada en `src/servicios/api.js`                    | 🟡 Con datos locales; Axios en semana 3 |
| E     | Jest sobre `calcularCuotaMensual` y `esDniValido`                      | ✅ Adelantado (13 pruebas) |
| C     | API REST, Postman, CORS                                                | ⏳ Semana 3 |
| D     | Conexión real con Axios                                                | ⏳ Semana 3 |
| E     | React Testing Library, prueba de sistema                               | ⏳ Semana 3 / final |
| F     | Informe, bitácora de IA                                                | ⏳ Final |

## Lo que falta y quién lo hace

### Semana 3 — objetivo: 90 %

| # | Tarea | Parte | Responsable | Entregable |
|---|-------|-------|-------------|------------|
| 1 | `ActividadRestController`, `SocioRestController`, `InscripcionRestController` bajo `/api` reutilizando los servicios existentes | C | _(nombre)_ | Endpoints del Anexo A |
| 2 | `@RestControllerAdvice` que traduzca `ReglaDeNegocioException` → **409** `{ "error", "regla" }` y `RecursoNoEncontradoException` → **404** | C | _(nombre)_ | JSON de error |
| 3 | Resolver el ciclo Socio ↔ Inscripcion en JSON (`@JsonIgnore` en las listas `inscripciones` o DTOs) y documentar la decisión | C | _(nombre)_ | Respuesta JSON sin ciclo |
| 4 | Configurar CORS para `http://localhost:3000` (`WebMvcConfigurer` en `config/`) | C | _(nombre)_ | Clase `ConfiguracionCors` |
| 5 | Colección Postman con todos los endpoints + 5 casos de error (RN1–RN5) | C/E | _(nombre)_ | `docs/SocioClub.postman_collection.json` |
| 6 | Reemplazar el cuerpo de `src/servicios/api.js` por Axios (`npm install axios`) | D | _(nombre)_ | Ingreso, inscripción y baja reales |
| 7 | Quitar la simulación de reglas de `api.js`: el 409 lo devuelve el backend | D | _(nombre)_ | — |
| 8 | Pruebas de componente con React Testing Library sobre `TarjetaActividad` (nombre visible; cupo 0 → "Sin cupo" y botón deshabilitado) | E | _(nombre)_ | `TarjetaActividad.test.js` |

### Entrega final — objetivo: 100 %

| # | Tarea | Parte | Responsable |
|---|-------|-------|-------------|
| 9  | Prueba de sistema: tabla con 6 casos punta a punta (1 exitoso + uno por RN1–RN5) | E | _(nombre)_ |
| 10 | Informe técnico PDF (8–12 páginas): solución, diagrama de arquitectura, modelo de datos, tabla de endpoints, capturas, resultados de pruebas, conclusiones | F | todos |
| 11 | Bitácora de IA (Anexo C): mínimo 5 entradas, 3 con correcciones detectadas por el grupo | F | todos |
| 12 | Incorporar devoluciones del docente de la exposición | — | todos |
| 13 | Repositorio GitHub con commits de todos los integrantes; README actualizado | F | _(nombre)_ |

## Riesgos que ya identificamos

- **Ciclo JSON Socio ↔ Inscripcion** (Anexo B): las listas `inscripciones` de Socio y Actividad
  van a serializar en bucle. Plan: `@JsonIgnore` sobre esas listas (lo más simple de explicar)
  o DTOs si queremos controlar exactamente qué sale.
- **LazyInitializationException** en la API: `Inscripcion.socio` y `.actividad` son `LAZY`. En
  Thymeleaf funciona porque `open-in-view=true`; en REST conviene usar las `@Query` con
  `JOIN FETCH` que ya están en `InscripcionRepository`.
- **Cambio en vivo que puede pedir el docente**: "máximo 4 actividades" → una sola constante
  `MAXIMO_ACTIVIDADES_POR_SOCIO` en `InscripcionService`; "agregar un dato a la tarjeta" →
  `TarjetaActividad.js` + su `propTypes`.

## Guion de la demo (7 minutos)

1. **Sitio público** (1 min): filtro por edad 10 → quedan 4 tarjetas; formulario con DNI con
   puntos → error junto al campo; botón Volver (`history.back()`).
2. **Secretaría** (4 min):
   - `/actividades`: cupos; editar cupo de Yoga y volver.
   - `/socios?apellido=gom`: búsqueda + paginación (20 socios, 10 por página).
   - `/inscripciones/nueva`: Valentina Fernández → Fútbol infantil ⇒ **RN3**;
     Carlos López → Ajedrez ⇒ **RN4**; Juan Gómez → Ajedrez ⇒ **RN5**; Juan Gómez → Yoga ⇒ **RN2**;
     Gabriel Núñez → Yoga (entra, queda cupo 0); Lucía Herrera → Yoga ⇒ **RN1**.
   - `/actividades/8/inscriptos`: ver los 15 inscriptos y dar de baja uno (baja lógica).
3. **App React** (2 min): ingresar con `30111222`; Mis actividades con cuota 15 % de
   descuento; catálogo con Yoga en gris "Sin cupo"; detalle de Ajedrez → Inscribirme ⇒ RN5;
   darse de baja de Yoga → la cuota se recalcula sola (estado → re-render).
