/**
 * RN6 - Bonificación.
 * La cuota mensual estimada es la suma de las cuotas de las actividades activas,
 * con 10 % de descuento si tiene 2 actividades y 15 % si tiene 3.
 *
 * Función pura: mismo arreglo de entrada => mismo resultado, sin efectos secundarios.
 * Por eso se puede probar con Jest sin montar ningún componente.
 *
 * @param {Array<{cuotaMensual: number}>} actividades - actividades activas del socio
 * @returns {number} cuota mensual estimada, redondeada a entero
 */
export function calcularCuotaMensual(actividades) {
  if (!Array.isArray(actividades) || actividades.length === 0) {
    return 0;
  }

  const subtotal = actividades.reduce((acum, actividad) => acum + Number(actividad.cuotaMensual || 0), 0);
  const descuento = porcentajeDescuento(actividades.length);

  return Math.round(subtotal * (1 - descuento / 100));
}

/** Porcentaje de descuento según cantidad de actividades (0, 10 o 15). */
export function porcentajeDescuento(cantidadActividades) {
  if (cantidadActividades >= 3) return 15;
  if (cantidadActividades === 2) return 10;
  return 0;
}

/** Formatea un número como pesos argentinos sin decimales: 17000 => "$ 17.000" */
export function formatearPesos(monto) {
  return '$ ' + Number(monto).toLocaleString('es-AR', { maximumFractionDigits: 0 });
}
