/**
 * DATOS LOCALES (semana 2).
 * Mientras la API REST no está lista, la app trabaja con estos arreglos.
 * Tienen la misma forma que el JSON que va a devolver el backend, así en la
 * semana 3 solo hay que reemplazar src/servicios/api.js por llamadas con Axios
 * sin tocar los componentes.
 *
 * Los datos coinciden con data.sql del backend (sección 3.3 del enunciado).
 */

export const actividadesLocales = [
  { id: 1, nombre: 'Fútbol infantil',   disciplina: 'Fútbol',    dias: 'Lun y Mié',      horario: '18:00', edadMinima: 6,  edadMaxima: 12, cupoMaximo: 40, cupoDisponible: 38, cuotaMensual: 18000, profesor: 'Diego Ferreyra' },
  { id: 2, nombre: 'Vóley mixto',       disciplina: 'Vóley',     dias: 'Mar y Jue',      horario: '19:00', edadMinima: 13, edadMaxima: 60, cupoMaximo: 24, cupoDisponible: 21, cuotaMensual: 20000, profesor: 'Laura Ceballos' },
  { id: 3, nombre: 'Básquet juvenil',   disciplina: 'Básquet',   dias: 'Lun y Vie',      horario: '19:30', edadMinima: 12, edadMaxima: 17, cupoMaximo: 20, cupoDisponible: 18, cuotaMensual: 19000, profesor: 'Martín Oviedo' },
  { id: 4, nombre: 'Natación',          disciplina: 'Natación',  dias: 'Mar y Jue',      horario: '17:00', edadMinima: 5,  edadMaxima: 70, cupoMaximo: 30, cupoDisponible: 28, cuotaMensual: 26000, profesor: 'Sofía Bustos' },
  { id: 5, nombre: 'Patín artístico',   disciplina: 'Patín',     dias: 'Mié y Vie',      horario: '17:30', edadMinima: 5,  edadMaxima: 15, cupoMaximo: 25, cupoDisponible: 24, cuotaMensual: 21000, profesor: 'Carla Peralta' },
  { id: 6, nombre: 'Ajedrez',           disciplina: 'Ajedrez',   dias: 'Sáb',            horario: '10:00', edadMinima: 8,  edadMaxima: 99, cupoMaximo: 16, cupoDisponible: 14, cuotaMensual: 9000,  profesor: 'Raúl Giménez' },
  { id: 7, nombre: 'Funcional adultos', disciplina: 'Fitness',   dias: 'Lun, Mié y Vie', horario: '20:30', edadMinima: 18, edadMaxima: 65, cupoMaximo: 30, cupoDisponible: 28, cuotaMensual: 22000, profesor: 'Paula Luna' },
  // Yoga queda SIN CUPO así se muestra la etiqueta "Sin cupo" y el botón deshabilitado
  { id: 8, nombre: 'Yoga',              disciplina: 'Bienestar', dias: 'Mar y Jue',      horario: '09:00', edadMinima: 16, edadMaxima: 99, cupoMaximo: 15, cupoDisponible: 0,  cuotaMensual: 17000, profesor: 'Inés Arguello' }
];

export const sociosLocales = [
  { id: 1,  dni: '30111222', nombre: 'Juan',      apellido: 'Gómez',     fechaNacimiento: '1985-03-12', estadoCuota: 'AL_DIA' },
  { id: 2,  dni: '34222333', nombre: 'María',     apellido: 'Pérez',     fechaNacimiento: '1990-07-25', estadoCuota: 'AL_DIA' },
  { id: 3,  dni: '55333444', nombre: 'Lucas',     apellido: 'Rodríguez', fechaNacimiento: '2016-05-10', estadoCuota: 'AL_DIA' },
  { id: 5,  dni: '26555666', nombre: 'Carlos',    apellido: 'López',     fechaNacimiento: '1978-11-30', estadoCuota: 'MOROSO' },
  { id: 14, dni: '45555777', nombre: 'Camila',    apellido: 'Molina',    fechaNacimiento: '2004-03-19', estadoCuota: 'AL_DIA' },
  { id: 18, dni: '47999222', nombre: 'Mariana',   apellido: 'Acosta',    fechaNacimiento: '2006-02-11', estadoCuota: 'AL_DIA' }
];

/**
 * Inscripciones activas, con la actividad "embebida" (igual que las devolverá
 * GET /api/socios/{id}/inscripciones).
 */
export const inscripcionesLocales = [
  { id: 1,  socioId: 1, estado: 'ACTIVA', fechaInscripcion: '2026-03-02', actividad: actividadesLocales[7] }, // Juan - Yoga
  { id: 15, socioId: 1, estado: 'ACTIVA', fechaInscripcion: '2026-03-11', actividad: actividadesLocales[1] }, // Juan - Vóley
  { id: 16, socioId: 1, estado: 'ACTIVA', fechaInscripcion: '2026-03-11', actividad: actividadesLocales[6] }, // Juan - Funcional
  { id: 2,  socioId: 2, estado: 'ACTIVA', fechaInscripcion: '2026-03-02', actividad: actividadesLocales[7] }, // María - Yoga
  { id: 24, socioId: 2, estado: 'ACTIVA', fechaInscripcion: '2026-03-17', actividad: actividadesLocales[3] }, // María - Natación
  { id: 17, socioId: 3, estado: 'ACTIVA', fechaInscripcion: '2026-03-12', actividad: actividadesLocales[0] }, // Lucas - Fútbol
  { id: 18, socioId: 3, estado: 'ACTIVA', fechaInscripcion: '2026-03-12', actividad: actividadesLocales[3] }, // Lucas - Natación
  { id: 3,  socioId: 5, estado: 'ACTIVA', fechaInscripcion: '2026-03-03', actividad: actividadesLocales[7] }, // Carlos - Yoga
  { id: 10, socioId: 14, estado: 'ACTIVA', fechaInscripcion: '2026-03-06', actividad: actividadesLocales[7] } // Camila - Yoga
  // Mariana Acosta (id 18) no tiene actividades: muestra el mensaje "sin actividades"
];
