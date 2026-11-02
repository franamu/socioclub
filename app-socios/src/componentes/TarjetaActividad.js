import React from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { formatearPesos } from '../utils/cuota';
import '../estilos/TarjetaActividad.css';

/**
 * Tarjeta de una actividad en el catálogo.
 * Recibe TODO por props (componente "tonto": no tiene estado ni pide datos).
 * - Renderizado condicional: etiqueta "Sin cupo" y botón deshabilitado si cupoDisponible === 0.
 * - Clase dinámica: "tarjeta--sin-cupo" pinta la tarjeta en gris.
 */
function TarjetaActividad({ actividad, onVerDetalle }) {
  const sinCupo = actividad.cupoDisponible === 0;

  // La clase CSS depende del estado de la actividad
  const clases = sinCupo ? 'tarjeta tarjeta--sin-cupo' : 'tarjeta';

  return (
    <article className={clases} data-testid={`tarjeta-${actividad.id}`}>
      <span className="tarjeta__disciplina">{actividad.disciplina}</span>
      <h3 className="tarjeta__nombre">{actividad.nombre}</h3>

      <ul className="tarjeta__datos">
        <li>{actividad.dias} · {actividad.horario} hs</li>
        <li>{actividad.edadMinima} a {actividad.edadMaxima} años</li>
        <li>Prof. {actividad.profesor}</li>
      </ul>

      <p className="tarjeta__cuota">{formatearPesos(actividad.cuotaMensual)} / mes</p>

      {sinCupo ? (
        <span className="etiqueta etiqueta--error">Sin cupo</span>
      ) : (
        <span className="etiqueta">{actividad.cupoDisponible} lugares disponibles</span>
      )}

      {onVerDetalle ? (
        <button
          type="button"
          className="boton boton--primario"
          disabled={sinCupo}
          onClick={() => onVerDetalle(actividad.id)}
        >
          Ver detalle
        </button>
      ) : (
        <Link
          to={`/actividades/${actividad.id}`}
          className={sinCupo ? 'boton boton--primario boton--deshabilitado' : 'boton boton--primario'}
          aria-disabled={sinCupo}
          onClick={(e) => sinCupo && e.preventDefault()}
        >
          Ver detalle
        </Link>
      )}
    </article>
  );
}

// Validación de props: si falta algo o viene con otro tipo, React avisa en consola
TarjetaActividad.propTypes = {
  actividad: PropTypes.shape({
    id: PropTypes.number.isRequired,
    nombre: PropTypes.string.isRequired,
    disciplina: PropTypes.string.isRequired,
    dias: PropTypes.string.isRequired,
    horario: PropTypes.string.isRequired,
    edadMinima: PropTypes.number.isRequired,
    edadMaxima: PropTypes.number.isRequired,
    cupoMaximo: PropTypes.number,
    cupoDisponible: PropTypes.number.isRequired,
    cuotaMensual: PropTypes.number.isRequired,
    profesor: PropTypes.string.isRequired
  }).isRequired,
  onVerDetalle: PropTypes.func
};

export default TarjetaActividad;
