import { calcularCuotaMensual, porcentajeDescuento } from './cuota';

// Pruebas unitarias con Jest sobre la función pura calcularCuotaMensual (RN6)
describe('calcularCuotaMensual (RN6)', () => {
  const yoga = { nombre: 'Yoga', cuotaMensual: 17000 };
  const voley = { nombre: 'Vóley mixto', cuotaMensual: 20000 };
  const funcional = { nombre: 'Funcional adultos', cuotaMensual: 22000 };

  test('sin actividades la cuota es 0', () => {
    expect(calcularCuotaMensual([])).toBe(0);
  });

  test('con 1 actividad no hay descuento', () => {
    expect(calcularCuotaMensual([yoga])).toBe(17000);
  });

  test('con 2 actividades aplica 10 % de descuento', () => {
    // 17000 + 20000 = 37000 -> 33300
    expect(calcularCuotaMensual([yoga, voley])).toBe(33300);
  });

  test('con 3 actividades aplica 15 % de descuento', () => {
    // 17000 + 20000 + 22000 = 59000 -> 50150
    expect(calcularCuotaMensual([yoga, voley, funcional])).toBe(50150);
  });

  test('si recibe algo que no es un arreglo devuelve 0', () => {
    expect(calcularCuotaMensual(null)).toBe(0);
    expect(calcularCuotaMensual(undefined)).toBe(0);
  });

  test('porcentajeDescuento devuelve 0, 10 o 15', () => {
    expect(porcentajeDescuento(0)).toBe(0);
    expect(porcentajeDescuento(1)).toBe(0);
    expect(porcentajeDescuento(2)).toBe(10);
    expect(porcentajeDescuento(3)).toBe(15);
  });
});
