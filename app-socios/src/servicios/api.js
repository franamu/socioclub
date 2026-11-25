/**
 * Capa de servicio de la app: es el ÚNICO lugar que sabe de dónde salen los datos.
 * Todas las llamadas a la API REST del backend pasan por acá con Axios. Así, si cambia
 * una URL se toca un solo archivo y los componentes no se enteran.
 *
 * Axios devuelve una Promesa que se rechaza sola si la respuesta es 4xx o 5xx: por eso a los
 * componentes les alcanza con try/catch (o .catch) y mensajeDeError.
 */
import axios from 'axios';

// Cliente con la dirección base: cada función solo indica el resto de la ruta
const api = axios.create({ baseURL: 'http://localhost:8080/api' });

// La respuesta de Axios trae también estado y cabeceras; a los componentes solo les sirve el cuerpo (JSON)
const soloDatos = (respuesta) => respuesta.data;

/** GET /api/socios/dni/{dni} */
export function buscarSocioPorDni(dni) {
  return api.get(`/socios/dni/${dni}`).then(soloDatos);
}

/** GET /api/actividades?disciplina=... (si disciplina es undefined, Axios no agrega el parámetro) */
export function obtenerActividades(disciplina) {
  return api.get('/actividades', { params: { disciplina } }).then(soloDatos);
}

/** GET /api/actividades/{id} */
export function obtenerActividad(id) {
  return api.get(`/actividades/${id}`).then(soloDatos);
}

/** GET /api/socios/{id}/inscripciones */
export function obtenerInscripcionesDeSocio(socioId) {
  return api.get(`/socios/${socioId}/inscripciones`).then(soloDatos);
}

/**
 * POST /api/inscripciones  { socioId, actividadId }
 * Las reglas RN1 a RN5 las aplica el backend; si alguna falla responde 409 con el mensaje.
 */
export function inscribir(socio, actividadId) {
  return api.post('/inscripciones', { socioId: socio.id, actividadId: Number(actividadId) }).then(soloDatos);
}

/** PUT /api/inscripciones/{id}/baja */
export function darDeBaja(inscripcionId) {
  return api.put(`/inscripciones/${inscripcionId}/baja`).then(soloDatos);
}

/**
 * Extrae el mensaje de error de la respuesta (misma forma que el 409 del Anexo A:
 * { error: "...", regla: "RN1" }). Los componentes muestran este texto al socio.
 */
export function mensajeDeError(error) {
  // Sin response no hubo respuesta que leer: backend apagado o CORS bloqueado por el navegador
  if (!error || !error.response) {
    return 'No se pudo conectar con el servidor. Verificá que el backend esté levantado.';
  }
  if (error.response.data && error.response.data.error) {
    return error.response.data.error;
  }
  return 'Ocurrió un error inesperado. Intentá de nuevo.';
}
