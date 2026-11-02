import React, { useState, useEffect } from 'react';
import ListaActividades from './ListaActividades';
import { obtenerActividades, mensajeDeError } from '../servicios/api';
import '../estilos/Actividades.css';

/**
 * Ruta "/actividades": catálogo con filtro por disciplina.
 *
 * - useState guarda las actividades, la disciplina elegida, el estado de carga y el error.
 * - useEffect carga las actividades cuando el componente aparece ([] como dependencias:
 *   se ejecuta una sola vez). Si en vez de [] pusiéramos [disciplina], se volvería a
 *   pedir a la API con cada cambio de filtro; acá filtramos en memoria.
 */
function Actividades() {
  const [actividades, setActividades] = useState([]);
  const [disciplina, setDisciplina] = useState('');
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    let activo = true; // evita actualizar el estado si el componente se desmontó antes de la respuesta

    obtenerActividades()
      .then((datos) => {
        if (activo) setActividades(datos);
      })
      .catch((e) => {
        if (activo) setError(mensajeDeError(e));
      })
      .finally(() => {
        if (activo) setCargando(false);
      });

    return () => {
      activo = false;
    };
  }, []);

  // Disciplinas únicas para el combo, derivadas del estado (no hace falta otro estado)
  const disciplinas = [...new Set(actividades.map((a) => a.disciplina))].sort();

  const filtradas = disciplina ? actividades.filter((a) => a.disciplina === disciplina) : actividades;

  return (
    <section className="actividades">
      <h1>Actividades</h1>

      <div className="actividades__filtro">
        <label htmlFor="disciplina">Disciplina</label>
        <select id="disciplina" value={disciplina} onChange={(e) => setDisciplina(e.target.value)}>
          <option value="">Todas</option>
          {disciplinas.map((d) => (
            <option key={d} value={d}>
              {d}
            </option>
          ))}
        </select>
        <span className="actividades__contador">
          {filtradas.length} de {actividades.length}
        </span>
      </div>

      {cargando && <p className="cargando">Cargando actividades...</p>}
      {error && <p className="alerta alerta--error">{error}</p>}

      {!cargando && !error && <ListaActividades actividades={filtradas} />}
    </section>
  );
}

export default Actividades;
