# Biblioteca API — Sistema de Gestión de Biblioteca

API REST construida con **Spring Boot 3.3** y **MongoDB Atlas** para el curso de Diseño de Software (4° Semestre).

## Estructura del proyecto

```
biblioteca-api/
└── src/main/java/com/biblioteca/
    ├── BibliotecaApiApplication.java
    ├── controller/
    │   ├── LibroController.java
    │   ├── UsuarioController.java
    │   ├── EjemplarController.java
    │   └── PrestamoController.java
    ├── dto/
    │   ├── LibroRequest.java / LibroResponse.java
    │   ├── UsuarioRequest.java / UsuarioResponse.java
    │   ├── EjemplarRequest.java / EjemplarResponse.java
    │   └── PrestamoRequest.java / PrestamoResponse.java
    ├── exception/
    │   └── GlobalExceptionHandler.java
    ├── model/
    │   ├── Libro.java
    │   ├── Usuario.java  (+ TipoUsuario enum)
    │   ├── Ejemplar.java (+ EstadoEjemplar enum)
    │   └── Prestamo.java (+ EstadoPrestamo enum)
    ├── repository/
    │   ├── LibroRepository.java
    │   ├── UsuarioRepository.java
    │   ├── EjemplarRepository.java
    │   └── PrestamoRepository.java
    └── service/
        ├── LibroService.java / impl/LibroServiceImpl.java
        ├── UsuarioService.java / impl/UsuarioServiceImpl.java
        ├── EjemplarService.java / impl/EjemplarServiceImpl.java
        └── PrestamoService.java / impl/PrestamoServiceImpl.java
```

## Configuración

Edita `src/main/resources/application.properties` y reemplaza la URI de MongoDB con tu cadena de conexión real de MongoDB Atlas:

```properties
spring.data.mongodb.uri=mongodb+srv://<usuario>:<password>@<cluster>.mongodb.net/biblioteca_db?retryWrites=true&w=majority
spring.data.mongodb.database=biblioteca_db
server.port=8080
```

## Ejecutar

```bash
mvn spring-boot:run
```

---

## Endpoints — Guía de Postman

URL base: `http://localhost:8080`

---

### 📚 Libros — `/api/libros`

#### Crear libro
```
POST /api/libros
Content-Type: application/json

{
  "isbn": "978-0-13-468599-1",
  "titulo": "Clean Code",
  "autor": "Robert C. Martin",
  "anioPublicacion": 2008,
  "categoria": "Programación"
}
```
Respuesta: `201 Created`

#### Listar todos los libros
```
GET /api/libros
```
Respuesta: `200 OK` — array de libros

#### Consultar libro por ID
```
GET /api/libros/{id}
```
Respuesta: `200 OK`

#### Actualizar libro
```
PUT /api/libros/{id}
Content-Type: application/json

{
  "isbn": "978-0-13-468599-1",
  "titulo": "Clean Code (Edición actualizada)",
  "autor": "Robert C. Martin",
  "anioPublicacion": 2008,
  "categoria": "Ingeniería de Software"
}
```
Respuesta: `200 OK`

#### Eliminar libro
```
DELETE /api/libros/{id}
```
Respuesta: `204 No Content`

---

### 👤 Usuarios — `/api/usuarios`

#### Crear estudiante
```
POST /api/usuarios
Content-Type: application/json

{
  "nombre": "Ana García",
  "correo": "ana.garcia@universidad.edu",
  "tipoUsuario": "ESTUDIANTE",
  "codigoEstudiante": "EST-2024-001",
  "programa": "Ingeniería de Software"
}
```
Respuesta: `201 Created`

#### Crear profesor
```
POST /api/usuarios
Content-Type: application/json

{
  "nombre": "Dr. Carlos Pérez",
  "correo": "carlos.perez@universidad.edu",
  "tipoUsuario": "PROFESOR",
  "codigoProfesor": "PROF-001",
  "facultad": "Ingeniería"
}
```

#### Crear bibliotecario
```
POST /api/usuarios
Content-Type: application/json

{
  "nombre": "María López",
  "correo": "maria.lopez@biblioteca.edu",
  "tipoUsuario": "BIBLIOTECARIO",
  "codigoEmpleado": "EMP-001",
  "turno": "MAÑANA"
}
```

#### Listar todos los usuarios
```
GET /api/usuarios
```

#### Listar por tipo
```
GET /api/usuarios?tipo=ESTUDIANTE
GET /api/usuarios?tipo=PROFESOR
GET /api/usuarios?tipo=BIBLIOTECARIO
```

#### Consultar usuario por ID
```
GET /api/usuarios/{id}
```

#### Actualizar usuario
```
PUT /api/usuarios/{id}
Content-Type: application/json

{ ... mismos campos que al crear ... }
```

#### Eliminar usuario
```
DELETE /api/usuarios/{id}
```
Respuesta: `204 No Content`

---

### 📖 Ejemplares — `/api/ejemplares`

> Un ejemplar es una copia física de un libro. Primero debes crear el libro y copiar su ID.

#### Crear ejemplar
```
POST /api/ejemplares
Content-Type: application/json

{
  "codigoEjemplar": "LIB-001-A",
  "libroId": "<id del libro>",
  "estado": "DISPONIBLE",
  "ubicacion": "Estante A-3"
}
```
Estados válidos: `DISPONIBLE`, `PRESTADO`, `EN_REPARACION`, `DADO_DE_BAJA`

Respuesta: `201 Created`

#### Listar todos los ejemplares
```
GET /api/ejemplares
```

#### Listar ejemplares de un libro
```
GET /api/ejemplares?libroId={libroId}
```

#### Consultar ejemplar por ID
```
GET /api/ejemplares/{id}
```

#### Actualizar ejemplar
```
PUT /api/ejemplares/{id}
Content-Type: application/json

{
  "codigoEjemplar": "LIB-001-A",
  "libroId": "<id del libro>",
  "estado": "EN_REPARACION",
  "ubicacion": "Estante B-1"
}
```

#### Eliminar ejemplar
```
DELETE /api/ejemplares/{id}
```

---

### 🔄 Préstamos — `/api/prestamos`

> Flujo completo: Crear usuario → Crear libro → Crear ejemplar → Registrar préstamo → Registrar devolución

#### Registrar préstamo
```
POST /api/prestamos
Content-Type: application/json

{
  "usuarioId": "<id del usuario>",
  "ejemplarId": "<id del ejemplar>",
  "fechaDevolucionEsperada": "2026-06-03"
}
```
- El ejemplar debe estar en estado `DISPONIBLE`
- Si no envías `fechaDevolucionEsperada`, se asignan 15 días automáticamente
- Al crear el préstamo, el ejemplar cambia a estado `PRESTADO`

Respuesta: `201 Created`

#### Registrar devolución
```
PUT /api/prestamos/{id}/devolucion
```
- El préstamo debe estar en estado `ACTIVO`
- Al devolver, el ejemplar vuelve a estado `DISPONIBLE`

Respuesta: `200 OK`

#### Listar todos los préstamos
```
GET /api/prestamos
```

#### Listar préstamos activos
```
GET /api/prestamos/activos
```

#### Listar préstamos de un usuario
```
GET /api/prestamos?usuarioId={usuarioId}
```

#### Consultar préstamo por ID
```
GET /api/prestamos/{id}
```

---

## Flujo de prueba completo en Postman

1. `POST /api/libros` → copia el `id` del libro
2. `POST /api/ejemplares` con el `libroId` → copia el `id` del ejemplar
3. `POST /api/usuarios` → copia el `id` del usuario
4. `POST /api/prestamos` con `usuarioId` y `ejemplarId` → copia el `id` del préstamo
5. `GET /api/prestamos/activos` → verifica que aparece el préstamo
6. `PUT /api/prestamos/{id}/devolucion` → registra la devolución
7. `GET /api/ejemplares/{id}` → verifica que el estado volvió a `DISPONIBLE`

---

## Códigos de respuesta

| Código | Significado |
|--------|-------------|
| 200 OK | Consulta o actualización exitosa |
| 201 Created | Recurso creado correctamente |
| 204 No Content | Eliminación exitosa |
| 400 Bad Request | Datos inválidos o regla de negocio violada |
| 404 Not Found | Recurso no encontrado |
| 500 Internal Server Error | Error inesperado del servidor |
