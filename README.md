# 📚 API de Tienda

## 📌 Resumen del Proyecto

**TiendaAPI** es una API REST construida con **Spring Boot 3.5.5** y **Java 21**, diseñada para gestionar las operaciones básicas de una tienda: clientes, productos y ventas. La aplicación se conecta a una base de datos MySQL, permitiendo realizar operaciones **CRUD** (Crear, Leer, Actualizar, Borrar) y otras consultas especializadas.

---

## 🛠️ Tecnologías y Dependencias

Este proyecto utiliza las siguientes tecnologías y librerías:

* **Spring Boot 3.5.5**: Framework principal para el desarrollo de la API.
* **Java 21**: Versión del lenguaje de programación.
* **Spring Data JPA**: Para la persistencia de datos y la interacción con la base de datos.
* **Spring Validation**: Para la validación de los datos en las peticiones (`DTOs`).
* **MySQL Connector/J**: Driver para la conexión con la base de datos MySQL.
* **Lombok**: Para reducir el código repetitivo (`getters`, `setters`, `constructors`).
* **Springdoc OpenAPI UI**: Para generar documentación de la API de forma automática.
* **Maven**: Herramienta de gestión y construcción del proyecto.

---

## ⚙️ Prerrequisitos y Configuración

Antes de ejecutar el proyecto, asegúrate de tener instalados los siguientes componentes:

* **Java 21** o superior.
* **Maven 3.x**.
* **MySQL Server** o acceso a una instancia de MySQL.

### Configuración de la base de datos

1.  Crea una base de datos en tu servidor MySQL.
2.  Copia y pega el siguiente contenido en el archivo `src/main/resources/application.properties` y reemplaza los valores de `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` con los tuyos.

    ```properties
    spring.application.name=TiendaAPI

    spring.jpa.hibernate.ddl-auto=update

    spring.datasource.url=${DB_URL}
    spring.datasource.username=${DB_USERNAME}
    spring.datasource.password=${DB_PASSWORD}

    spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

    server.port=8080
    ```

> ⚠️ **Nota:** El valor `spring.jpa.hibernate.ddl-auto=update` configurará Hibernate para que cree y actualice automáticamente las tablas en la base de datos basándose en tus clases `Entity`.

---

## 🚀 Cómo Iniciar la API

Puedes ejecutar la aplicación de dos maneras:

1.  **Desde la línea de comandos con Maven:**
    ```bash
    mvn spring-boot:run
    ```

2.  **Generando un archivo JAR ejecutable:**
    ```bash
    # Empaqueta la aplicación en un archivo .jar
    mvn clean package

    # Ejecuta el archivo .jar
    java -jar target/TiendaAPI-0.0.1-SNAPSHOT.jar
    ```

La API se iniciará en `http://localhost:8080`.

---

## 🗺️ Endpoints de la API

La API está organizada en tres secciones principales: `clientes`, `productos` y `ventas`. A continuación se detallan los endpoints disponibles.

### 👥 Clientes

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `POST` | `/clientes` | Crea un nuevo cliente. |
| `GET` | `/clientes` | Obtiene una lista de todos los clientes activos. |
| `GET` | `/clientes/all` | Obtiene una lista con todos los clientes, activos o inactivos, de la base de datos. |
| `GET` | `/clientes/{id_cliente}` | Obtiene un cliente por su ID. |
| `PUT` | `/clientes/{id_cliente}` | Edita la información de un cliente. |
| `DELETE` | `/clientes/{id_cliente}` | "Elimina" (cancela) un cliente por su ID. |

### 📦 Productos

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `POST` | `/productos` | Crea un nuevo producto. |
| `GET` | `/productos` | Obtiene una lista de productos activos. |
| `GET` | `/productos/all` | Obtiene una lista con todos los productos, tanto activos como inactivos, de la base de datos. |
| `GET` | `/productos/{codigo_producto}` | Obtiene un producto por su código. |
| `GET` | `/productos/falta_stock` | Obtiene los productos con stock bajo (stock < 5). |
| `PUT` | `/productos/{codigo_producto}` | Edita la información de un producto. |
| `DELETE` | `/productos/{codigo_producto}` | "Elimina" (cancela) un producto por su código. |

### 🛒 Ventas

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `POST` | `/ventas` | Crea una nueva venta. |
| `GET` | `/ventas` | Lista todas las ventas. Se puede filtrar por `estado` (`COMPLETADA` o `CANCELADA`). |
| `GET` | `/ventas/{codigo_venta}` | Obtiene una venta por su código. |
| `DELETE` | `/ventas/{codigo_venta}` | "Elimina" (cancela) una venta. |
| `GET` | `/ventas/{codigo_venta}/productos` | Obtiene los productos de una venta específica. |
| `GET` | `/ventas/diario/{fecha_venta}` | Obtiene el total de ventas y la cantidad de ventas para un día específico (formato `YYYY-MM-DD`). |
| `GET` | `/ventas/mayor_venta` | Obtiene los detalles de la venta con el monto total más alto. |

---

## 📄 Documentación y Colección de Postman

### Documentación de la API (Swagger UI)

Una vez que la aplicación esté en ejecución, puedes acceder a la documentación interactiva de la API en la siguiente URL:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### 🚀 Colección de Postman

Puedes importar todos los endpoints de la API a Postman para realizar pruebas de manera sencilla.

Descarga la colección en formato JSON desde este enlace:
[Colección de Postman](https://github.com/Joanjo17/tienda_api/blob/main/TiendaAPI.postman_collection.json)