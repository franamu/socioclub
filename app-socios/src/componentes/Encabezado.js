import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { useSocio } from '../contexto/SocioContext';
import '../estilos/Encabezado.css';

/**
 * Barra superior. Lee el socio del contexto para mostrar su nombre y
 * cambiar el menú según haya ingresado o no (renderizado condicional).
 */
function Encabezado() {
  const { socio, salir } = useSocio();
  const navegar = useNavigate();

  const manejarSalir = () => {
    salir();
    navegar('/');
  };

  return (
    <header className="encabezado">
      <div className="encabezado__contenido">
        <NavLink to="/" className="encabezado__logo">
          Mi Club <small>· Los Algarrobos</small>
        </NavLink>

        <nav className="encabezado__nav">
          <NavLink to="/actividades">Actividades</NavLink>
          {socio && <NavLink to="/mis-actividades">Mis actividades</NavLink>}
        </nav>

        {socio ? (
          <div className="encabezado__socio">
            <span>Hola, {socio.nombre}</span>
            <button type="button" className="boton boton--chico" onClick={manejarSalir}>
              Salir
            </button>
          </div>
        ) : (
          <NavLink to="/" className="boton boton--chico">
            Ingresar
          </NavLink>
        )}
      </div>
    </header>
  );
}

export default Encabezado;
