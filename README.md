# Ecosistema de Microservicios: EJEMPLO-GATEWAY

Este repositorio contiene una arquitectura de microservicios robusta, escalable y modular, desarrollada bajo el estándar de **Spring Boot** para un sistema de e-commerce moderno. El proyecto implementa comunicación síncrona entre servicios, persistencia distribuida y un enrutamiento centralizado.

## 🏗️ Arquitectura del Sistema
El sistema utiliza un **API Gateway** centralizado que orquesta las peticiones hacia múltiples microservicios especializados, garantizando una separación clara de responsabilidades y alta mantenibilidad.

### Microservicios Desarrollados:
* **api-gateway**: Punto de entrada único (Puerto 9090).
* **cliente-service**: Gestión integral de usuarios.
* **ms-producto**: Catálogo y gestión de inventario.
* **ms-carrito**: Lógica de compras temporales.
* **ms-pedido**: Consolidación de órdenes de compra.
* **ms-transaccion**: Procesamiento de pagos con máquina de estados.
* **ms-resena**: Sistema de valoraciones (1-5 estrellas).
* **ms-envio**: Gestión logística y estados de despacho.
* **ms-descuento**: Aplicación de cupones y reglas matemáticas.

## ⚙️ Reglas de Oro Implementadas
Para garantizar la integridad y robustez, este proyecto sigue estrictos estándares de desarrollo:

1.  **Persistencia Segura**: Mapeo explícito de columnas (`@Column(name="...")`) para asegurar consistencia entre el modelo Java y la base de datos MySQL.
2.  **Aislamiento de Capas**: Uso estricto de **DTOs** con métodos `toModel()` y `fromModel()`. Ninguna entidad JPA es expuesta directamente al frontend.
3.  **SSOT con Liquibase**: Versionado de base de datos automatizado, actuando como la única fuente de verdad para la creación y migración de tablas.
4.  **Gateway Centralizado**: Todas las comunicaciones externas son validadas y enrutadas a través del API Gateway, manteniendo la seguridad y organización del sistema.

## 🛠️ Tecnologías Utilizadas
* **Java 17+**
* **Spring Boot (Spring Web, Data JPA, Validation)**
* **MySQL & Liquibase**
* **OpenFeign** (Comunicación síncrona entre microservicios)
* **Springdoc OpenAPI (Swagger)** (Documentación profesional de APIs)
* **Docker & Docker Compose** (Contenerización del ecosistema)

## 🚀 Cómo Levantar el Proyecto
1.  **Base de Datos**: Asegurar que MySQL esté en ejecución en Laragon/XAMPP.
2.  **Orquestación**: En la raíz del proyecto, ejecutar el comando:
    ```bash
    docker-compose up -d --build
    ```
3.  **Acceso**: El sistema estará disponible a través del `api-gateway` en el puerto **9090**.

---

### 💡 Nota del Desarrollador
Este proyecto sigue las mejores prácticas de arquitectura de software, incluyendo el manejo centralizado de excepciones (`GlobalExceptionHandler`) para garantizar respuestas profesionales y evitar errores genéricos de Spring Boot. 

> *"Arquitectura diseñada para la escalabilidad, validada por estándares de persistencia y enrutamiento inteligente."*
