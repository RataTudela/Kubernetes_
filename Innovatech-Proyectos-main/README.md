# Microservicio de Gestión de Proyectos - Innovatech

## Descripción

Este microservicio forma parte del sistema de gestión de proyectos de Innovatech. Su objetivo es administrar proyectos, tareas y asignaciones de usuarios dentro de la plataforma.

El sistema fue desarrollado utilizando Spring Boot y PostgreSQL siguiendo una arquitectura basada en microservicios.

---

# Tecnologías Utilizadas

* Java 21
* Spring Boot 3.5.0
* Spring Data JPA
* PostgreSQL
* Maven
* Lombok
* JUnit 5
* Mockito

---

# Estructura del Proyecto

```text
src
 ├── main
 │   ├── java
 │   │   └── com.example.proyectos
 │   │       ├── controller
 │   │       ├── model
 │   │       ├── repository
 │   │       ├── service
 │   │       └── ProyectosApplication.java
 │   └── resources
 │       └── application.properties
 │
 └── test
     └── java
         └── com.example.proyectos
```

---

# Configuración de Base de Datos

En el archivo `application.properties` configurar:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/proyectos_db
spring.datasource.username=postgres
spring.datasource.password=tu_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

# Ejecución del Proyecto

## Clonar repositorio

```bash
git clone https://github.com/tu-repositorio/Innovatech-Proyectos.git
```

## Entrar al proyecto

```bash
cd Innovatech-Proyectos
```

## Ejecutar aplicación

```bash
mvn spring-boot:run
```

El microservicio quedará disponible en:

```text
http://localhost:8080
```

---

# Entidades

## Proyecto

Representa un proyecto dentro del sistema.

### Atributos

* idProyecto
* nombre
* descripcion
* estado
* prioridad
* fecha_inicio
* fecha_fin

---

## Tarea

Representa tareas asociadas a un proyecto.

### Atributos

* idTarea
* nombre
* descripcion
* estado
* proyecto

---

## Asignacion

Representa la asignación de una tarea a un usuario.

### Atributos

* idAsignacion
* tarea
* idUsuario
* fechaAsignacion
* horasAproximadas

---

# Endpoints Disponibles

## ProyectoController

Base URL:

```text
/api/proyectos/proyecto
```

| Método | Endpoint     | Descripción             |
| ------ | ------------ | ----------------------- |
| GET    | /            | Listar proyectos        |
| GET    | /{id}        | Obtener proyecto por ID |
| POST   | /            | Crear proyecto          |
| PUT    | /{id}        | Actualizar proyecto     |
| PATCH  | /{id}/estado | Actualizar estado       |
| DELETE | /{id}        | Eliminar proyecto       |

---

## TareaController

Base URL:

```text
/api/proyectos
```

| Método | Endpoint              | Descripción                 |
| ------ | --------------------- | --------------------------- |
| GET    | /tareas               | Listar tareas               |
| GET    | /tareas/{id}          | Obtener tarea por ID        |
| GET    | /{proyecto_id}/tareas | Obtener tareas por proyecto |
| POST   | /tareas               | Crear tarea                 |
| PUT    | /tareas/{id}          | Actualizar tarea            |
| PATCH  | /tareas/{id}/estado   | Actualizar estado           |
| DELETE | /tareas/{id}          | Eliminar tarea              |

---

## AsignacionController

Base URL:

```text
/api/proyectos
```

| Método | Endpoint                            | Descripción                                |
| ------ | ----------------------------------- | ------------------------------------------ |
| GET    | /asignacion                         | Listar asignaciones                        |
| GET    | /asignacion/{id}                    | Obtener asignación por ID                  |
| GET    | /usuarios/{id_usuario}/asignaciones | Obtener asignaciones por usuario           |
| GET    | /tareas/{id_tarea}/asignaciones     | Obtener asignaciones por tarea             |
| GET    | /asignacion/horas/{horas}           | Obtener asignaciones por horas aproximadas |
| POST   | /asignacion                         | Crear asignación                           |
| PUT    | /asignacion/{id}                    | Actualizar asignación                      |
| DELETE | /asignacion/{id}                    | Eliminar asignación                        |

---

# Ejemplos JSON

## Crear Proyecto

```json
{
  "nombre": "Sistema ERP",
  "descripcion": "Proyecto de gestión empresarial",
  "estado": "Activo",
  "prioridad": "Alta",
  "fecha_inicio": "2026-05-01",
  "fecha_fin": "2026-06-30"
}
```

---

## Crear Tarea

```json
{
  "nombre": "Diseñar Base de Datos",
  "descripcion": "Modelo relacional",
  "estado": "Pendiente",
  "proyecto": {
    "idProyecto": 1
  }
}
```

---

## Crear Asignación

```json
{
  "tarea": {
    "idTarea": 1
  },
  "idUsuario": 5,
  "fechaAsignacion": "2026-05-01",
  "horasAproximadas": 8
}
```

---

# Testing

El proyecto incluye pruebas unitarias para los servicios:

* TestProyectoService
* TestTareaService
* TestAsignacionService

Para ejecutar los tests:

```bash
mvn test
```

---

# Estado del Proyecto

Actualmente el microservicio permite:

* CRUD completo de proyectos
* CRUD completo de tareas
* CRUD completo de asignaciones
* Filtrado por proyecto
* Filtrado por usuario
* Filtrado por horas aproximadas
* Actualización de estados
* Persistencia con PostgreSQL

---

# Autor

Desarrollado para el proyecto Innovatech.
