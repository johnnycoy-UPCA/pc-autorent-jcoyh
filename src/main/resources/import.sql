-- ===============
-- Tabla: autos
-- ===============
INSERT INTO autos (placa, marca, modelo, anio, precio_por_dia, deposito, kilometraje, estado) 
VALUES ('JCH-777', 'Mazda', 'CX-30', 2026, 180000.0, 500000.0, 12500, 'DISPONIBLE');

INSERT INTO autos (placa, marca, modelo, anio, precio_por_dia, deposito, kilometraje, estado) 
VALUES ('ABC-123', 'Toyota', 'Corolla', 2025, 150000.0, 400000.0, 32000, 'DISPONIBLE');

INSERT INTO autos (placa, marca, modelo, anio, precio_por_dia, deposito, kilometraje, estado) 
VALUES ('XYZ-987', 'Chevrolet', 'Onix', 2024, 120000.0, 300000.0, 45100, 'MANTENIMIENTO');

INSERT INTO autos (placa, marca, modelo, anio, precio_por_dia, deposito, kilometraje, estado) 
VALUES ('XYZ-123', 'Toyota', 'Corolla', 2025, 150000.0, 500000.0, 10200.0, 'ALQUILADO');

INSERT INTO autos (placa, marca, modelo, anio, precio_por_dia, deposito, kilometraje, estado) 
VALUES ('ABC-789', 'Mazda', 'CX-5', 2024, 180000.0, 600000.0, 25000.0, 'DISPONIBLE');

INSERT INTO autos (placa, marca, modelo, anio, precio_por_dia, deposito, kilometraje, estado) 
VALUES ('MNO-456', 'Kia', 'Sportage', 2023, 160000.0, 550000.0, 42000.0, 'DISPONIBLE');


-- ================
-- Tabla: clientes
-- ================
INSERT INTO clientes (nombre, tipo_documento, documento, celular, email, estado)
VALUES ('Carlos Pérez', 'CC', 1020304050, '3001234567', 'carlos@email.com', 'ACTIVO');

INSERT INTO clientes (nombre, tipo_documento, documento, celular, email, estado)
VALUES ('Ana Martínez', 'CC', 987654321, '3159876543', 'ana@email.com', 'ACTIVO');

INSERT INTO clientes (nombre, tipo_documento, documento, celular, email, estado)
VALUES ('John Doe', 'PA', 555444333, '3104445555', 'john@email.com', 'ACTIVO');

INSERT INTO clientes (nombre, tipo_documento, documento, celular, email, estado)
VALUES ('Juana de Arco', 'CC', 123456789, '3000000001', 'juanadarco@email.com', 'INACTIVO');


-- ====================
-- Tabla: alquileres
-- ====================
INSERT INTO alquileres (cliente_id, auto_id, fecha_reserva, fecha_inicio, fecha_fin, cantidad_dias, valor_total, kilometraje_inicial, kilometraje_final, estado)
VALUES (2, 2, '2026-09-01', '2026-09-01', '2026-09-05', 4, 600000.0, 31500, 32000, 'FINALIZADO');

INSERT INTO alquileres (cliente_id, auto_id, fecha_reserva, fecha_inicio, fecha_fin, cantidad_dias, valor_total, kilometraje_inicial, kilometraje_final, estado)
VALUES (1, 1, '2026-10-01', '2026-10-10', '2026-10-25', 5, 900000.0, 12500, 0, 'ACTIVO');

INSERT INTO alquileres (cliente_id, auto_id, fecha_reserva, fecha_inicio, fecha_fin, cantidad_dias, valor_total, kilometraje_inicial, kilometraje_final, estado)
VALUES (2, 2, '2026-10-01', '2026-10-25', '2026-10-29', 4, 600000.0, NULL, NULL, 'PENDIENTE');

INSERT INTO alquileres (cliente_id, auto_id, fecha_reserva, fecha_inicio, fecha_fin, cantidad_dias, valor_total, kilometraje_inicial, kilometraje_final, estado) 
VALUES (1, 4, '2026-10-20', '2026-10-23', '2026-10-28', 5, 750000.0, 10200.0, NULL, 'ACTIVO');

INSERT INTO alquileres (cliente_id, auto_id, fecha_reserva, fecha_inicio, fecha_fin, cantidad_dias, valor_total, kilometraje_inicial, kilometraje_final, estado) 
VALUES (2, 5, '2026-10-21', '2026-10-26', '2026-10-30', 4, 720000.0, NULL, NULL, 'PENDIENTE');

INSERT INTO alquileres (cliente_id, auto_id, fecha_reserva, fecha_inicio, fecha_fin, cantidad_dias, valor_total, kilometraje_inicial, kilometraje_final, estado) 
VALUES (1, 6, '2026-10-22', '2026-11-15', '2026-11-18', 3, 480000.0, NULL, NULL, 'PENDIENTE');
