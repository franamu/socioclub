import React from 'react';
import PropTypes from 'prop-types';
import { useNavigate } from 'react-router-dom';
import TarjetaActividad from './TarjetaActividad';
import '../estilos/ListaActividades.css';

/**
 * Lista de tarjetas. Recibe el arreglo ya filtrado por props y lo recorre con map.
 * El atributo key le permite a React saber qué tarjeta cambió al re-renderizar.
 * La navegación se decide acá y se pasa por props: la tarjeta no necesita conocer las rutas.
 */
function ListaActividades({ actividades }) {
  const navegar = useNavigate();

  if (actividades.length === 0) {
    return <p className="lista__vacia">No hay actividades para mostrar.</p>;
  }

  return (
    <section className="lista-actividades">
      {actividades.map((actividad) => (
        <TarjetaActividad
          key={actividad.id}
          actividad={actividad}
          onVerDetalle={(id) => navegar(`/actividades/${id}`)}
        />
      ))}
    </section>
  );
}

ListaActividades.propTypes = {
  actividades: PropTypes.arrayOf(PropTypes.object).isRequired
};

export default ListaActividades;
