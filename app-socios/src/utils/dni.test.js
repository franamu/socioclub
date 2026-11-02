import { esDniValido } from './dni';

// Pruebas unitarias con Jest sobre la función pura esDniValido (RN7)
describe('esDniValido (RN7)', () => {
  test('acepta un DNI de 8 dígitos', () => {
    expect(esDniValido('40123456')).toBe(true);
  });

  test('acepta un DNI de 7 dígitos', () => {
    expect(esDniValido('4012345')).toBe(true);
  });

  test('rechaza un DNI con puntos', () => {
    expect(esDniValido('40.123.456')).toBe(false);
  });

  test('rechaza un DNI con letras', () => {
    expect(esDniValido('4012345A')).toBe(false);
  });

  test('rechaza un DNI de 6 dígitos', () => {
    expect(esDniValido('401234')).toBe(false);
  });

  test('rechaza un DNI de 9 dígitos', () => {
    expect(esDniValido('401234567')).toBe(false);
  });

  test('rechaza vacío o valores que no son texto', () => {
    expect(esDniValido('')).toBe(false);
    expect(esDniValido(null)).toBe(false);
    expect(esDniValido(40123456)).toBe(false);
  });
});
