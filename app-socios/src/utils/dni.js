/**
 * RN7 - DNI válido: 7 u 8 dígitos numéricos, sin puntos.
 * Función pura, probada con Jest.
 *
 * @param {string} dni
 * @returns {boolean}
 */
export function esDniValido(dni) {
  if (typeof dni !== 'string') {
    return false;
  }
  return /^\d{7,8}$/.test(dni.trim());
}
