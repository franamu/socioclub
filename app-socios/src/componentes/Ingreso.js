import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSocio } from '../contexto/SocioContext';
import { buscarSocioPorDni, mensajeDeError } from '../servicios/api';
import { esDniValido } from '../utils/dni';
import '../estilos/Ingreso.css';

/**
 * Ruta "/": el socio escribe su DNI y la app lo busca.
 * - useState: valor del input, error y estado de carga (estado LOCAL del componente).
 * - useSocio (contexto): guarda el socio encontrado para el resto de la app.
 */
function Ingreso() {
  const [dni, setDni] = useState('');
  const [error, setError] = useState('');
  const [cargando, setCargando] = useState(false);

  const { socio, ingresar } = useSocio();
  const navegar = useNavigate();

  const manejarEnvio = async (evento) => {
    evento.preventDefault();
    setError('');

    // RN7 validada en el cliente antes de consultar
    if (!esDniValido(dni)) {
      setError('El DNI debe tener 7 u 8 dígitos numéricos, sin puntos');
      return;
    }

    setCargando(true);
    try {
      const encontrado = await buscarSocioPorDni(dni.trim());
      ingresar(encontrado);
      navegar('/mis-actividades');
    } catch (e) {
      setError(mensajeDeError(e));
    } finally {
      setCargando(false);
    }
  };

  return (
    <section className="ingreso">
      <h1>Bienvenido a Mi Club</h1>
      <p>Ingresá con tu DNI para ver tus actividades, tu cuota y anotarte a nuevas actividades.</p>

      {socio && (
        <p className="alerta alerta--info">
          Ya ingresaste como <strong>{socio.nombre} {socio.apellido}</strong>.
        </p>
      )}

      <form onSubmit={manejarEnvio} className="ingreso__form" noValidate>
        <label htmlFor="dni">DNI (sin puntos)</label>
        <input
          id="dni"
          type="text"
          inputMode="numeric"
          placeholder="Ej: 30111222"
          value={dni}
          onChange={(e) => setDni(e.target.value)}
          className={error ? 'campo--error' : ''}
          autoFocus
        />
        {error && <span className="error">{error}</span>}

        <button type="submit" className="boton boton--primario" disabled={cargando}>
          {cargando ? 'Buscando...' : 'Ingresar'}
        </button>
      </form>

      <aside className="ingreso__ayuda">
        <h2>DNI de prueba</h2>
        <ul>
          <li><code>30111222</code> Juan Gómez · 3 actividades (máximo)</li>
          <li><code>34222333</code> María Pérez · 2 actividades</li>
          <li><code>55333444</code> Lucas Rodríguez · 10 años</li>
          <li><code>26555666</code> Carlos López · MOROSO</li>
          <li><code>47999222</code> Mariana Acosta · sin actividades</li>
        </ul>
      </aside>
    </section>
  );
}

export default Ingreso;
