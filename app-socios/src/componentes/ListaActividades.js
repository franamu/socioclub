import React from 'react';
import PropTypes from 'prop-types';
import TarjetaActividad from './TarjetaActividad';
import '../estilos/ListaActividades.css';

/**
 * Lista de tarjetas. Recibe el arreglo ya filtrado por props y lo recorre con map.
 * El atributo key le permite a React saber qué tarjeta cambió al re-renderizar.
 */
function ListaActividades({ actividades }) {
  if (actividades.length === 0) {
    return <p className="lista__vacia">No hay actividades para mostrar.</p>;
  }

  return (
    <section className="lista-actividades">
      {actividades.map((actividad) => (
        <TarjetaActividad key={actividad.id} actividad={actividad} />
      ))}
    </section>
  );
}

ListaActividades.propTypes = {
  actividades: PropTypes.arrayOf(PropTypes.object).isRequired
};

export default ListaActividades;
