/**
 * Capa de servicio de la app: es el ÚNICO lugar que sabe de dónde salen los datos.
 *
 * SEMANA 2: devuelve los arreglos locales de src/datos/datosLocales.js,
 * envueltos en Promesas para que los componentes ya usen async/await
 * exactamente igual que cuando haya API.
 *
 * SEMANA 3: cada función se reemplaza por una llamada con Axios, por ejemplo:
 *   export const obtenerActividades = (disciplina) =>
 *     axios.get(`${API}/actividades`, { params: { disciplina } }).then(r => r.data);
 * Los componentes no cambian.
 */
import { actividadesLocales, sociosLocales, inscripcionesLocales } from '../datos/datosLocales';

// Copias mutables para que inscribirse / darse de baja "funcionen" en memoria con los datos locales
let actividades = actividadesLocales.map((a) => ({ ...a }));
let inscripciones = inscripcionesLocales.map((i) => ({ ...i }));
let proximoId = 100;

// Simula la latencia de red para que se vea el estado "Cargando..."
const demorar = (valor, ms = 300) => new Promise((resolver) => setTimeout(() => resolver(valor), ms));

// Simula un error de la API (más adelante será la respuesta 409 del backend)
const rechazar = (mensaje, regla) => Promise.reject({ response: { status: 409, data: { error: mensaje, regla } } });

const calcularEdad = (fechaIso) => {
  const nacimiento = new Date(fechaIso);
  const hoy = new Date();
  let edad = hoy.getFullYear() - nacimiento.getFullYear();
  const m = hoy.getMonth() - nacimiento.getMonth();
  if (m < 0 || (m === 0 && hoy.getDate() < nacimiento.getDate())) edad--;
  return edad;
};

/** GET /api/socios/dni/{dni} */
export function buscarSocioPorDni(dni) {
  const socio = sociosLocales.find((s) => s.dni === dni);
  if (!socio) {
    return Promise.reject({ response: { status: 404, data: { error: 'No existe un socio con ese DNI' } } });
  }
  return demorar({ ...socio, edad: calcularEdad(socio.fechaNacimiento) });
}

/** GET /api/actividades?disciplina=... */
export function obtenerActividades(disciplina) {
  const lista = disciplina ? actividades.filter((a) => a.disciplina === disciplina) : actividades;
  return demorar(lista.map((a) => ({ ...a })));
}

/** GET /api/actividades/{id} */
export function obtenerActividad(id) {
  const actividad = actividades.find((a) => a.id === Number(id));
  if (!actividad) {
    return Promise.reject({ response: { status: 404, data: { error: 'No existe la actividad' } } });
  }
  return demorar({ ...actividad });
}

/** GET /api/socios/{id}/inscripciones */
export function obtenerInscripcionesDeSocio(socioId) {
  const lista = inscripciones.filter((i) => i.socioId === socioId && i.estado === 'ACTIVA');
  return demorar(lista.map((i) => ({ ...i })));
}

/**
 * POST /api/inscripciones  { socioId, actividadId }
 * Las reglas RN1 a RN5 viven en el backend; acá se simulan solo para que la
 * app con datos locales muestre los mensajes de error en pantalla.
 */
export function inscribir(socio, actividadId) {
  const actividad = actividades.find((a) => a.id === Number(actividadId));
  if (!actividad) return Promise.reject({ response: { status: 404, data: { error: 'No existe la actividad' } } });

  const activasDelSocio = inscripciones.filter((i) => i.socioId === socio.id && i.estado === 'ACTIVA');

  if (socio.estadoCuota === 'MOROSO') return rechazar('Debe regularizar su cuota en secretaría', 'RN4');
  if (activasDelSocio.some((i) => i.actividad.id === actividad.id)) return rechazar('El socio ya está inscripto en esta actividad', 'RN2');
  const edad = calcularEdad(socio.fechaNacimiento);
  if (edad < actividad.edadMinima || edad > actividad.edadMaxima) {
    return rechazar(`La edad del socio (${edad} años) está fuera del rango permitido: ${actividad.edadMinima} a ${actividad.edadMaxima} años`, 'RN3');
  }
  if (activasDelSocio.length >= 3) return rechazar('Alcanzó el máximo de 3 actividades', 'RN5');
  if (actividad.cupoDisponible <= 0) return rechazar('La actividad no tiene cupo disponible', 'RN1');

  actividad.cupoDisponible -= 1;
  const nueva = { id: proximoId++, socioId: socio.id, estado: 'ACTIVA', fechaInscripcion: new Date().toISOString().slice(0, 10), actividad };
  inscripciones.push(nueva);
  return demorar({ ...nueva });
}

/** PUT /api/inscripciones/{id}/baja */
export function darDeBaja(inscripcionId) {
  const inscripcion = inscripciones.find((i) => i.id === inscripcionId);
  if (!inscripcion) return Promise.reject({ response: { status: 404, data: { error: 'No existe la inscripción' } } });
  inscripcion.estado = 'BAJA';
  const actividad = actividades.find((a) => a.id === inscripcion.actividad.id);
  if (actividad) actividad.cupoDisponible += 1;
  return demorar({ ...inscripcion });
}

/**
 * Extrae el mensaje de error de la respuesta (misma forma que el 409 del Anexo A:
 * { error: "...", regla: "RN1" }). Los componentes muestran este texto al socio.
 */
export function mensajeDeError(error) {
  if (error && error.response && error.response.data && error.response.data.error) {
    return error.response.data.error;
  }
  return 'Ocurrió un error inesperado. Intentá de nuevo.';
}
