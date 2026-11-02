import React from 'react';
import ReactDOM from 'react-dom/client';
import './estilos/index.css';
import App from './App';

// Punto de entrada: React toma el <div id="root"> de public/index.html
// y dibuja ahí el árbol de componentes (DOM virtual -> DOM real).
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
