import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom'; // agrega comparaciones sobre el DOM: toBeInTheDocument, toBeDisabled, toHaveClass
import TarjetaActividad from './TarjetaActividad';

// Misma forma que el JSON de GET /api/actividades/{id}
const yoga = {
  id: 8,
  nombre: 'Yoga',
  disciplina: 'Bienestar',
  dias: 'Mar y Jue',
  horario: '09:00',
  edadMinima: 16,
  edadMaxima: 99,
  cupoMaximo: 15,
  cupoDisponible: 1,
  cuotaMensual: 17000,
  profesor: 'Inés Arguello'
};

// Función que monta la tarjeta con un onVerDetalle simulado y lo devuelve.
// jest.fn() registra si lo llamaron y con qué argumentos.
function montarTarjeta(cambios = {}) {
  const onVerDetalle = jest.fn();
  render(<TarjetaActividad actividad={{ ...yoga, ...cambios }} onVerDetalle={onVerDetalle} />);
  return onVerDetalle;
}

// La tarjeta no importa api.js ni usa rutas: se prueba sola, sin backend y sin Router
describe('TarjetaActividad', () => {
  test('muestra el nombre, la disciplina y la cuota formateada', () => {
    montarTarjeta();

    expect(screen.getByText('Yoga')).toBeInTheDocument();
    expect(screen.getByText('Bienestar')).toBeInTheDocument();
    expect(screen.getByText('$ 17.000 / mes')).toBeInTheDocument();
  });

  test('sin cupo: muestra "Sin cupo", se pinta en gris y no deja ver el detalle', () => {
    const onVerDetalle = montarTarjeta({ cupoDisponible: 0 });
    const boton = screen.getByRole('button', { name: 'Ver detalle' });

    expect(screen.getByText('Sin cupo')).toBeInTheDocument();
    expect(screen.getByTestId('tarjeta-8')).toHaveClass('tarjeta--sin-cupo');
    expect(boton).toBeDisabled();

    fireEvent.click(boton);
    expect(onVerDetalle).not.toHaveBeenCalled();
  });

  test('con cupo: al hacer clic llama a onVerDetalle con el id de la actividad', () => {
    const onVerDetalle = montarTarjeta();

    // queryByText devuelve null si no encuentra nada (getByText haría fallar el test)
    expect(screen.queryByText('Sin cupo')).not.toBeInTheDocument();
    expect(screen.getByTestId('tarjeta-8')).not.toHaveClass('tarjeta--sin-cupo');

    fireEvent.click(screen.getByRole('button', { name: 'Ver detalle' }));
    expect(onVerDetalle).toHaveBeenCalledTimes(1);
    expect(onVerDetalle).toHaveBeenCalledWith(8);
  });
});
