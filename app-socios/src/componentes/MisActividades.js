import React, { useState, useEffect } from 'react';
import { Link, Navigate } from 'react-router-dom';
import { useSocio } from '../contexto/SocioContext';
import ResumenCuota from './ResumenCuota';
import { obtenerInscripcionesDeSocio, darDeBaja, mensajeDeError } from '../servicios/api';
import { formatearPesos } from '../utils/cuota';
import '../estilos/MisActividades.css';

/**
 * Ruta "/mis-actividades": inscripciones activas del socio, baja y cuota estimada.
 * El socio viene del contexto. Si no hay socio, redirige al ingreso.
 */
function MisActividades() {
  const { socio } = useSocio();

  const [inscripciones, setInscripciones] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');
  const [mensaje, setMensaje] = useState('');

  // Dependencia [socio]: si cambia el socio (otro ingreso), se vuelven a cargar sus inscripciones
  useEffect(() => {
    if (!socio) return;
    setCargando(true);
    obtenerInscripcionesDeSocio(socio.id)
      .then(setInscripciones)
      .catch((e) => setError(mensajeDeError(e)))
      .finally(() => setCargando(false));
  }, [socio]);

  if (!socio) {
    return <Navigate to="/" replace />;
  }

  const manejarBaja = async (inscripcion) => {
    const confirmado = window.confirm(`¿Darte de baja de ${inscripcion.actividad.nombre}?`);
    if (!confirmado) return;

    setError('');
    setMensaje('');
    try {
      await darDeBaja(inscripcion.id);
      // Actualizamos el estado quitando la inscripción: React vuelve a dibujar la lista
      setInscripciones(inscripciones.filter((i) => i.id !== inscripcion.id));
      setMensaje(`Te diste de baja de ${inscripcion.actividad.nombre}.`);
    } catch (e) {
      setError(mensajeDeError(e));
    }
  };

  const actividadesActivas = inscripciones.map((i) => i.actividad);

  return (
    <section className="mis-actividades">
      <h1>Mis actividades</h1>
      <p className="mis-actividades__socio">
        {socio.nombre} {socio.apellido} · DNI {socio.dni} ·{' '}
        <span className={socio.estadoCuota === 'MOROSO' ? 'etiqueta etiqueta--error' : 'etiqueta etiqueta--ok'}>
          {socio.estadoCuota === 'MOROSO' ? 'Cuota pendiente' : 'Cuota al día'}
        </span>
      </p>

      {socio.estadoCuota === 'MOROSO' && (
        <p className="alerta alerta--info">
          Tenés la cuota pendiente: podés consultar y darte de baja, pero para anotarte a nuevas
          actividades primero regularizá tu situación en secretaría.
        </p>
      )}

      {mensaje && <p className="alerta alerta--exito">{mensaje}</p>}
      {error && <p className="alerta alerta--error">{error}</p>}

      {cargando ? (
        <p className="cargando">Cargando tus actividades...</p>
      ) : (
        <div className="mis-actividades__contenido">
          <div>
            {inscripciones.length === 0 ? (
              <div className="mis-actividades__vacio">
                <p>Todavía no estás anotado/a en ninguna actividad.</p>
                <Link to="/actividades" className="boton boton--primario">
                  Ver actividades
                </Link>
              </div>
            ) : (
              <ul className="mis-actividades__lista">
                {inscripciones.map((inscripcion) => (
                  <li key={inscripcion.id} className="inscripcion">
                    <div>
                      <h3>{inscripcion.actividad.nombre}</h3>
                      <p>
                        {inscripcion.actividad.dias} · {inscripcion.actividad.horario} hs · Prof.{' '}
                        {inscripcion.actividad.profesor}
                      </p>
                      <p className="inscripcion__cuota">{formatearPesos(inscripcion.actividad.cuotaMensual)} / mes</p>
                    </div>
                    <button
                      type="button"
                      className="boton boton--peligro"
                      onClick={() => manejarBaja(inscripcion)}
                    >
                      Darme de baja
                    </button>
                  </li>
                ))}
              </ul>
            )}

            {inscripciones.length > 0 && inscripciones.length < 3 && (
              <Link to="/actividades" className="boton">
                + Anotarme a otra actividad
              </Link>
            )}
          </div>

          <ResumenCuota actividades={actividadesActivas} />
        </div>
      )}
    </section>
  );
}

export default MisActividades;
