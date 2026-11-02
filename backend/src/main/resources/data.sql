-- =====================================================================
-- Datos iniciales del Club Los Algarrobos (sección 3.3 del enunciado)
-- Se ejecuta automáticamente al levantar Spring Boot (spring.sql.init.mode=always).
-- INSERT IGNORE: si la fila ya existe (mismo id) no la vuelve a insertar,
-- así se puede reiniciar la app sin duplicar datos.
-- =====================================================================

-- ---------- Actividades ----------
INSERT IGNORE INTO actividades (id, nombre, disciplina, dias, horario, edad_minima, edad_maxima, cupo_maximo, cuota_mensual, profesor) VALUES
(1, 'Fútbol infantil',   'Fútbol',    'Lun y Mié',      '18:00',  6, 12, 40, 18000.00, 'Diego Ferreyra'),
(2, 'Vóley mixto',       'Vóley',     'Mar y Jue',      '19:00', 13, 60, 24, 20000.00, 'Laura Ceballos'),
(3, 'Básquet juvenil',   'Básquet',   'Lun y Vie',      '19:30', 12, 17, 20, 19000.00, 'Martín Oviedo'),
(4, 'Natación',          'Natación',  'Mar y Jue',      '17:00',  5, 70, 30, 26000.00, 'Sofía Bustos'),
(5, 'Patín artístico',   'Patín',     'Mié y Vie',      '17:30',  5, 15, 25, 21000.00, 'Carla Peralta'),
(6, 'Ajedrez',           'Ajedrez',   'Sáb',            '10:00',  8, 99, 16,  9000.00, 'Raúl Giménez'),
(7, 'Funcional adultos', 'Fitness',   'Lun, Mié y Vie', '20:30', 18, 65, 30, 22000.00, 'Paula Luna'),
(8, 'Yoga',              'Bienestar', 'Mar y Jue',      '09:00', 16, 99, 15, 17000.00, 'Inés Arguello');

-- ---------- Socios de prueba (20, edades variadas, 2 MOROSOS) ----------
-- Edades calculadas a noviembre de 2026.
INSERT IGNORE INTO socios (id, dni, nombre, apellido, fecha_nacimiento, email, telefono, estado_cuota) VALUES
( 1, '30111222', 'Juan',      'Gómez',     '1985-03-12', 'juan.gomez@mail.com',       '351-4001001', 'AL_DIA'),  -- 41 años, 3 actividades (RN5)
( 2, '34222333', 'María',     'Pérez',     '1990-07-25', 'maria.perez@mail.com',      '351-4001002', 'AL_DIA'),  -- 36
( 3, '55333444', 'Lucas',     'Rodríguez', '2016-05-10', 'flia.rodriguez@mail.com',   '351-4001003', 'AL_DIA'),  -- 10, fútbol infantil
( 4, '52444555', 'Valentina', 'Fernández', '2012-09-03', 'flia.fernandez@mail.com',   '351-4001004', 'AL_DIA'),  -- 14, fuera de rango para fútbol infantil (RN3)
( 5, '26555666', 'Carlos',    'López',     '1978-11-30', 'carlos.lopez@mail.com',     '351-4001005', 'MOROSO'),  -- 47, MOROSO (RN4)
( 6, '38666777', 'Ana',       'Martínez',  '1995-02-14', 'ana.martinez@mail.com',     '351-4001006', 'AL_DIA'),  -- 31
( 7, '58777888', 'Pedro',     'Sánchez',   '2019-08-21', 'flia.sanchez@mail.com',     '351-4001007', 'AL_DIA'),  -- 7
( 8, '43888999', 'Laura',     'Romero',    '2001-12-05', 'laura.romero@mail.com',     '351-4001008', 'AL_DIA'),  -- 24
( 9, '20999111', 'Diego',     'Torres',    '1969-04-18', 'diego.torres@mail.com',     '351-4001009', 'AL_DIA'),  -- 57
(10, '50111333', 'Sofía',     'Díaz',      '2011-01-27', 'flia.diaz@mail.com',        '351-4001010', 'AL_DIA'),  -- 15
(11, '33222444', 'Martín',    'Álvarez',   '1988-06-09', 'martin.alvarez@mail.com',   '351-4001011', 'MOROSO'),  -- 38, MOROSO (RN4)
(12, '41333555', 'Julieta',   'Castro',    '1999-10-15', 'julieta.castro@mail.com',   '351-4001012', 'AL_DIA'),  -- 27
(13, '29444666', 'Facundo',   'Ruiz',      '1983-08-02', 'facundo.ruiz@mail.com',     '351-4001013', 'AL_DIA'),  -- 43
(14, '45555777', 'Camila',    'Molina',    '2004-03-19', 'camila.molina@mail.com',    '351-4001014', 'AL_DIA'),  -- 22
(15, '12666888', 'Roberto',   'Suárez',    '1958-12-01', 'roberto.suarez@mail.com',   '351-4001015', 'AL_DIA'),  -- 67, fuera de rango para funcional (RN3)
(16, '36777999', 'Florencia', 'Ortiz',     '1992-05-30', 'florencia.ortiz@mail.com',  '351-4001016', 'AL_DIA'),  -- 34
(17, '24888111', 'Nicolás',   'Vega',      '1975-09-08', 'nicolas.vega@mail.com',     '351-4001017', 'AL_DIA'),  -- 51
(18, '47999222', 'Mariana',   'Acosta',    '2006-02-11', 'mariana.acosta@mail.com',   '351-4001018', 'AL_DIA'),  -- 20, sin actividades
(19, '39111444', 'Gabriel',   'Núñez',     '1997-04-22', 'gabriel.nunez@mail.com',    '351-4001019', 'AL_DIA'),  -- 29, no está en Yoga (toma el último cupo)
(20, '44222555', 'Lucía',     'Herrera',   '2003-08-14', 'lucia.herrera@mail.com',    '351-4001020', 'AL_DIA');  -- 23, no está en Yoga (RN1)

-- ---------- Inscripciones ----------
-- Yoga (id 8, cupo 15) queda con 14 activas: 1 lugar libre para demostrar RN1
-- (Gabriel Núñez toma el último lugar y Lucía Herrera es rechazada).
-- Juan Gómez (id 1) tiene 3 activas para demostrar RN5.
INSERT IGNORE INTO inscripciones (id, socio_id, actividad_id, fecha_inscripcion, estado) VALUES
-- Yoga: 14 activas
( 1,  1, 8, '2026-03-02', 'ACTIVA'),
( 2,  2, 8, '2026-03-02', 'ACTIVA'),
( 3,  5, 8, '2026-03-03', 'ACTIVA'),
( 4,  6, 8, '2026-03-03', 'ACTIVA'),
( 5,  8, 8, '2026-03-04', 'ACTIVA'),
( 6,  9, 8, '2026-03-04', 'ACTIVA'),
( 7, 11, 8, '2026-03-05', 'ACTIVA'),
( 8, 12, 8, '2026-03-05', 'ACTIVA'),
( 9, 13, 8, '2026-03-06', 'ACTIVA'),
(10, 14, 8, '2026-03-06', 'ACTIVA'),
(11, 15, 8, '2026-03-09', 'ACTIVA'),
(12, 16, 8, '2026-03-09', 'ACTIVA'),
(13, 17, 8, '2026-03-10', 'ACTIVA'),
(14, 18, 8, '2026-03-10', 'ACTIVA'),
-- Juan Gómez: vóley y funcional (ya tiene yoga => 3 activas)
(15,  1, 2, '2026-03-11', 'ACTIVA'),
(16,  1, 7, '2026-03-11', 'ACTIVA'),
-- Chicos
(17,  3, 1, '2026-03-12', 'ACTIVA'),   -- Lucas (10) fútbol infantil
(18,  3, 4, '2026-03-12', 'ACTIVA'),   -- Lucas natación
(19,  7, 1, '2026-03-13', 'ACTIVA'),   -- Pedro (7) fútbol infantil
(20, 10, 5, '2026-03-13', 'ACTIVA'),   -- Sofía (15) patín
(21, 10, 3, '2026-03-16', 'ACTIVA'),   -- Sofía básquet juvenil
(22,  4, 3, '2026-03-16', 'ACTIVA'),   -- Valentina (14) básquet juvenil
(23,  4, 2, '2026-03-17', 'ACTIVA'),   -- Valentina vóley
-- Adultos
(24,  2, 4, '2026-03-17', 'ACTIVA'),   -- María natación
(25,  6, 7, '2026-03-18', 'ACTIVA'),   -- Ana funcional
(26,  9, 6, '2026-03-18', 'ACTIVA'),   -- Diego ajedrez
(27, 13, 6, '2026-03-19', 'ACTIVA'),   -- Facundo ajedrez
(28, 12, 2, '2026-03-19', 'ACTIVA'),   -- Julieta vóley
-- Una baja lógica de ejemplo
(29,  8, 7, '2026-03-20', 'BAJA');     -- Laura se dio de baja de funcional
