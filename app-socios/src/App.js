import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { SocioProvider } from './contexto/SocioContext';
import Encabezado from './componentes/Encabezado';
import Pie from './componentes/Pie';
import Ingreso from './componentes/Ingreso';
import Actividades from './componentes/Actividades';
import DetalleActividad from './componentes/DetalleActividad';
import MisActividades from './componentes/MisActividades';
import './estilos/App.css';

/**
 * Componente raíz. Define las 4 rutas del enunciado con React Router
 * y envuelve todo en el SocioProvider para que cualquier pantalla
 * pueda saber qué socio ingresó (useContext) sin pasar props.
 */
function App() {
  return (
    <SocioProvider>
      <BrowserRouter>
        <div className="app">
          <Encabezado />
          <main className="contenido">
            <Routes>
              <Route path="/" element={<Ingreso />} />
              <Route path="/actividades" element={<Actividades />} />
              <Route path="/actividades/:id" element={<DetalleActividad />} />
              <Route path="/mis-actividades" element={<MisActividades />} />
              {/* Cualquier otra URL vuelve al ingreso */}
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </main>
          <Pie />
        </div>
      </BrowserRouter>
    </SocioProvider>
  );
}

export default App;
