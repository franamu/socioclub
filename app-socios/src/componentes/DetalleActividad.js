import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useSocio } from '../contexto/SocioContext';
import { obtenerActividad, inscribir, mensajeDeError } from '../servicios/api';
import { formatearPesos } from '../utils/cuota';
import '../estilos/DetalleActividad.css';

/**
 * Ruta "/actividades/:id": detalle de una actividad y botón "Inscribirme".
 *
 * - useParams lee el :id de la URL.
 * - useEffect con [id] como dependencia: si el id cambia (navegar de una actividad
 *   a otra) se vuelve a pedir el detalle.
 * - El socio sale del contexto; si nadie ingresó, el botón lleva al ingreso.
 */
function DetalleActividad() {
  const { id } = useParams();
  const navegar = useNavigate();
  const { socio } = useSocio();

  const [actividad, setActividad] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');
  const [mensaje, setMensaje] = useState('');
  const [enviando, setEnviando] = useState(false);

  useEffect(() => {
    setCargando(true);
    setError('');
    obtenerActividad(id)
      .then(setActividad)
      .catch((e) => setError(mensajeDeError(e)))
      .finally(() => setCargando(false));
  }, [id]);

  const manejarInscripcion = async () => {
    setError('');
    setMensaje('');
    setEnviando(true);
    try {
      // Recorrido completo (semana 3): clic -> Axios -> controlador REST -> servicio -> repositorio -> MySQL
      await inscribir(socio, actividad.id);
      setMensaje(`¡Listo! Quedaste inscripto/a en ${actividad.nombre}.`);
      // Refrescamos el cupo que se muestra en pantalla
      setActividad({ ...actividad, cupoDisponible: actividad.cupoDisponible - 1 });
    } catch (e) {
      // Mensaje de la regla de negocio (409) visible para el socio
      setError(mensajeDeError(e));
    } finally {
      setEnviando(false);
    }
  };

  if (cargando) return <p className="cargando">Cargando actividad...</p>;
  if (!actividad) return <p className="alerta alerta--error">{error || 'No encontramos la actividad.'}</p>;

  const sinCupo = actividad.cupoDisponible === 0;

  return (
    <section className="detalle">
      <Link to="/actividades" className="detalle__volver">
        ← Volver al catálogo
      </Link>

      <span className="tarjeta__disciplina">{actividad.disciplina}</span>
      <h1>{actividad.nombre}</h1>

      <dl className="detalle__datos">
        <dt>Días y horario</dt>
        <dd>{actividad.dias} · {actividad.horario} hs</dd>
        <dt>Edades</dt>
        <dd>{actividad.edadMinima} a {actividad.edadMaxima} años</dd>
        <dt>Profesor/a</dt>
        <dd>{actividad.profesor}</dd>
        <dt>Cuota mensual</dt>
        <dd>{formatearPesos(actividad.cuotaMensual)}</dd>
        <dt>Cupo</dt>
        <dd>
          {sinCupo ? (
            <span className="etiqueta etiqueta--error">Sin cupo</span>
          ) : (
            `${actividad.cupoDisponible} de ${actividad.cupoMaximo} lugares disponibles`
          )}
        </dd>
      </dl>

      {mensaje && <p className="alerta alerta--exito">{mensaje}</p>}
      {error && <p className="alerta alerta--error">{error}</p>}

      {socio ? (
        <button
          type="button"
          className="boton boton--primario boton--grande"
          onClick={manejarInscripcion}
          disabled={sinCupo || enviando}
        >
          {enviando ? 'Inscribiendo...' : 'Inscribirme'}
        </button>
      ) : (
        <div className="detalle__sin-socio">
          <p>Para inscribirte tenés que ingresar con tu DNI.</p>
          <button type="button" className="boton boton--primario" onClick={() => navegar('/')}>
            Ingresar
          </button>
        </div>
      )}
    </section>
  );
}

export default DetalleActividad;
