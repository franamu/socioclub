import React from 'react';
import PropTypes from 'prop-types';
import { calcularCuotaMensual, porcentajeDescuento, formatearPesos } from '../utils/cuota';
import '../estilos/ResumenCuota.css';

/**
 * Muestra la cuota mensual estimada del socio (RF10 / RN6).
 * El cálculo NO está acá: lo hace la función pura calcularCuotaMensual de src/utils,
 * que se prueba con Jest. El componente solo presenta el resultado.
 */
function ResumenCuota({ actividades }) {
  const cantidad = actividades.length;
  const subtotal = actividades.reduce((acum, a) => acum + a.cuotaMensual, 0);
  const descuento = porcentajeDescuento(cantidad);
  const total = calcularCuotaMensual(actividades);

  return (
    <aside className="resumen-cuota">
      <h3>Tu cuota mensual estimada</h3>

      <dl className="resumen-cuota__detalle">
        <dt>Actividades activas</dt>
        <dd>{cantidad} de 3</dd>

        <dt>Subtotal</dt>
        <dd>{formatearPesos(subtotal)}</dd>

        <dt>Bonificación</dt>
        <dd>{descuento > 0 ? `${descuento} %` : 'Sin bonificación'}</dd>
      </dl>

      <p className="resumen-cuota__total">{formatearPesos(total)}</p>

      {cantidad === 1 && (
        <p className="resumen-cuota__ayuda">Sumá una segunda actividad y obtené 10 % de descuento.</p>
      )}
      {cantidad === 2 && (
        <p className="resumen-cuota__ayuda">Con una tercera actividad el descuento sube a 15 %.</p>
      )}
    </aside>
  );
}

ResumenCuota.propTypes = {
  actividades: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number.isRequired,
      nombre: PropTypes.string.isRequired,
      cuotaMensual: PropTypes.number.isRequired
    })
  ).isRequired
};

export default ResumenCuota;
