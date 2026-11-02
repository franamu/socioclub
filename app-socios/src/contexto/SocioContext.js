import React, { createContext, useContext, useState } from 'react';
import PropTypes from 'prop-types';

/**
 * Contexto del socio que ingresó.
 *
 * ¿Por qué un contexto y no props? El socio logueado lo necesitan el Encabezado,
 * el DetalleActividad (para inscribirse) y MisActividades. Pasarlo por props
 * obligaría a que App -> Routes -> cada página lo reciban y reenvíen aunque
 * no lo usen ("prop drilling"). Con createContext/useContext cualquier
 * componente del árbol lo lee directamente.
 */
const SocioContext = createContext(null);

export function SocioProvider({ children }) {
  // Estado compartido: null = nadie ingresó todavía
  const [socio, setSocio] = useState(null);

  const ingresar = (datosSocio) => setSocio(datosSocio);
  const salir = () => setSocio(null);

  const valor = { socio, ingresar, salir };

  return <SocioContext.Provider value={valor}>{children}</SocioContext.Provider>;
}

SocioProvider.propTypes = {
  children: PropTypes.node.isRequired
};

/** Hook propio para no repetir useContext(SocioContext) en cada componente. */
export function useSocio() {
  const contexto = useContext(SocioContext);
  if (contexto === null) {
    throw new Error('useSocio debe usarse dentro de un <SocioProvider>');
  }
  return contexto;
}

export default SocioContext;
