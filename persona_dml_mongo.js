db = db.getSiblingDB("persona_db");

db.persona.insertMany([
  {
      "_id": NumberInt(123456789),
      "nombre": "Pepe",
      "apellido": "Perez",
      "genero": "M",
      "edad": NumberInt(30),
      "_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
  },
  {
      "_id": NumberInt(987654321),
      "nombre": "Pepito",
      "apellido": "Perez",
      "genero": "M",
      "_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
  },
  {
      "_id": NumberInt(321654987),
      "nombre": "Pepa",
      "apellido": "Juarez",
      "genero": "F",
      "edad": NumberInt(30),
      "_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
  },
  {
      "_id": NumberInt(147258369),
      "nombre": "Pepita",
      "apellido": "Juarez",
      "genero": "F",
      "edad": NumberInt(10),
      "_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
  },
  {
      "_id": NumberInt(963852741),
      "nombre": "Fede",
      "apellido": "Perez",
      "genero": "M",
      "edad": NumberInt(18),
      "_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
  }
], { ordered: false })

db.profesion.insertMany([
  {
      "_id": NumberInt(1),
      "nom": "Ingeniero de Sistemas",
      "des": "Profesional en desarrollo y mantenimiento de sistemas de software."
  },
  {
      "_id": NumberInt(2),
      "nom": "Analista de Datos",
      "des": "Especialista en la interpretación y análisis de datos."
  },
  {
      "_id": NumberInt(3),
      "nom": "Diseñador Gráfico",
      "des": "Profesional en creación de material visual."
  }
])

db.estudios.insertMany([
  {
      "id_prof": NumberInt(1),
      "cc_per": NumberInt(123456789),
      "fecha": ISODate("2015-06-15"),
      "univer": "Universidad Nacional"
  },
  {
      "id_prof": NumberInt(2),
      "cc_per": NumberInt(987654321),
      "fecha": ISODate("2018-11-20"),
      "univer": "Universidad de los Andes"
  },
  {
      "id_prof": NumberInt(3),
      "cc_per": NumberInt(321654987),
      "fecha": ISODate("2020-07-10"),
      "univer": "Pontificia Universidad Javeriana"
  }
])

db.telefono.insertMany([
  {
      "num": "3001234567",
      "oper": "Claro",
      "duenio": NumberInt(123456789)
  },
  {
      "num": "3107654321",
      "oper": "Movistar",
      "duenio": NumberInt(987654321)
  },
  {
      "num": "3123456789",
      "oper": "Tigo",
      "duenio": NumberInt(321654987)
  },
  {
      "num": "3209876543",
      "oper": "Claro",
      "duenio": NumberInt(147258369)
  },
  {
      "num": "3151230987",
      "oper": "Movistar",
      "duenio": NumberInt(963852741)
  }
])