INSERT INTO 
	`persona_db`.`persona`(`cc`,`nombre`,`apellido`,`genero`,`edad`) 
VALUES
	(123456789,'Pepe','Perez','M',30),
	(987654321,'Pepito','Perez','M',null),
	(321654987,'Pepa','Juarez','F',30),
	(147258369,'Pepita','Juarez','F',10),
	(963852741,'Fede','Perez','M',18);

INSERT INTO `persona_db`.`profesion` (`id`, `nom`, `des`)
VALUES
    (1, 'Ingeniero', 'Especialista en ingeniería civil'),
    (2, 'Doctor', 'Especialista en medicina general'),
    (3, 'Profesor', 'Docente de secundaria'),
    (4, 'Abogado', 'Especialista en derecho penal'),
    (5, 'Estudiante', 'Actualmente cursando estudios secundarios');

-- Inserción en la tabla estudios
INSERT INTO `persona_db`.`estudios` (`id_prof`, `cc_per`, `fecha`, `univer`)
VALUES
    (1, 123456789, '2015-06-15', 'Universidad Nacional'),
    (2, 987654321, '2018-09-12', 'Universidad de Los Andes'),
    (3, 321654987, '2020-01-20', 'Pontificia Universidad Javeriana'),
    (4, 147258369, '2023-02-14', 'Universidad Externado'),
    (5, 963852741, '2022-03-10', 'Universidad del Rosario');

-- Inserción en la tabla telefono
INSERT INTO `persona_db`.`telefono` (`num`, `oper`, `duenio`)
VALUES
    ('3001234567', 'Claro', 123456789),
    ('3109876543', 'Movistar', 123456789),
    ('3204567890', 'Tigo', 987654321),
    ('3111122334', 'Claro', 321654987),
    ('3009988776', 'Movistar', 963852741);