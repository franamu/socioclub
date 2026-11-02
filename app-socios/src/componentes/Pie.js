import React from 'react';
import '../estilos/Pie.css';

function Pie() {
  const anio = new Date().getFullYear();
  return (
    <footer className="pie">
      <p>© {anio} Club Social y Deportivo Los Algarrobos · Córdoba</p>
      <p className="pie__chico">Taller de Construcción de Software · Universidad Siglo 21</p>
    </footer>
  );
}

export default Pie;
