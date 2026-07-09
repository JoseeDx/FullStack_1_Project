# Ecosistema de Microservicios — Tienda de Periféricos

El sistema implementa una arquitectura de microservicios para una tienda online de periféricos, con comunicación síncrona entre servicios, persistencia distribuida y enrutamiento centralizado mediante API Gateway.

## 👥 Integrantes

* Catalina Correa
* Jose Barrientos

## 🏗️ Arquitectura del Sistema

El sistema utiliza un **API Gateway** centralizado (Spring Cloud Gateway) que orquesta las peticiones hacia 10 microservicios especializados, garantizando separación de responsabilidades, cohesión por dominio y alta mantenibilidad. Cada microservicio implementa el patrón **CSR (Controller–Service–Repository)**, persistencia con JPA + Hibernate, documentación con Swagger/OpenAPI y HATEOAS mediante un segundo controlador (`ControllerV2` + `ModelAssembler`).

### Microservicios Desarrollados

| Microservicio | Responsable | Dominio | Puerto |
|---|---|---|---|
| **api-gateway** | Equipo | Punto de entrada único | 9090 |
| **cliente-service** | Jose | Gestión de usuarios y roles | 9091 |
| **ms-producto** | Catalina | Catálogo de productos y categorías | 8081 |
| **ms-carrito** | Catalina | Carrito de compras temporal | 8084 |
| **ms-inventario** | Catalina | Control de stock por producto | 8083 |
| **ms-pedido** | Jose | Consolidación de órdenes de compra | 9094 |
| **ms-transaccion** | Catalina | Procesamiento de pagos | 8085 |
| **ms-facturacion** | Catalina | Emisión de facturas (IVA, validación de RUT) | 8086 |
| **ms-resena** | Jose | Valoraciones de productos (1-5 estrellas) | 8087 |
| **ms-envio** | Jose | Gestión logística y estados de despacho | 8088 |
| **ms-descuento** | Jose | Cupones y reglas de descuento | 8082 |

## 🌐 Rutas principales del Gateway

Todas las peticiones externas pasan por `http://localhost:9090` y se enrutan según el siguiente prefijo:

| Ruta | Microservicio destino |
|---|---|
| `/api/v1/clientes/**` | cliente-service |
| `/api/v1/productos/**`, `/api/v1/categorias/**` | ms-producto |
| `/api/v1/carrito/**` | ms-carrito |
| `/api/v1/inventario/**`, `/inventario/v2/**` | ms-inventario |
| `/api/v1/pedidos/**` | ms-pedido |
| `/api/v1/transacciones/**` | ms-transaccion |
| `/api/v1/facturas/**`, `/facturas/v2/**` | ms-facturacion |
| `/api/v1/resenas/**`, `/resenas/v2/**` | ms-resena |
| `/api/v1/envios/**` | ms-envio |
| `/api/v1/descuentos/**` | ms-descuento |

## 📄 Documentación Swagger / OpenAPI

Cada microservicio expone su propia UI de Swagger. En ejecución local (fuera de Docker), acceder directamente al puerto del servicio:

| Microservicio | Swagger UI (local) |
|---|---|
| cliente-service | http://localhost:9091/swagger-ui.html |
| ms-producto | http://localhost:8081/swagger-ui.html |
| ms-carrito | http://localhost:8084/swagger-ui.html |
| ms-inventario | http://localhost:8083/swagger-ui.html |
| ms-pedido | http://localhost:9094/swagger-ui.html |
| ms-transaccion | http://localhost:8085/swagger-ui.html |
| ms-facturacion | http://localhost:8086/swagger-ui.html |
| ms-resena | http://localhost:8087/swagger-ui.html |
| ms-envio | http://localhost:8088/swagger-ui.html |
| ms-descuento | http://localhost:8082/swagger-ui.html |

## ⚙️ Reglas de Oro Implementadas

1. **Persistencia segura**: mapeo explícito de columnas (`@Column(name="...")`) para asegurar consistencia entre el modelo Java y la base de datos MySQL.
2. **Aislamiento de capas**: uso estricto de **DTOs** con métodos `toModel()` y `fromModel()`. Ninguna entidad JPA se expone directamente al cliente.
3. **SSOT con Liquibase**: versionado de base de datos automatizado, única fuente de verdad para la creación de tablas.
4. **Gateway centralizado**: todas las comunicaciones externas se validan y enrutan a través del API Gateway.
5. **Manejo centralizado de errores**: `GlobalExceptionHandler` (`@RestControllerAdvice`) en cada microservicio para respuestas HTTP consistentes.

## 🛠️ Tecnologías Utilizadas

* **Java 21**
* **Spring Boot** (Web, Data JPA, Validation)
* **Spring Cloud Gateway** (webflux)
* **MySQL & Liquibase**
* **OpenFeign** (comunicación síncrona entre microservicios)
* **Springdoc OpenAPI 2.8.9** (documentación de APIs)
* **JUnit 5 + Mockito** (pruebas unitarias)
* **Docker & Docker Compose** (contenerización)
* **Java Faker** (carga de datos de prueba vía DataLoader)

## 🚀 Cómo levantar el proyecto

### Ejecución local con Docker (recomendada)

1. Asegurar que MySQL esté disponible (Laragon/XAMPP) o usar el contenedor de MySQL si está definido en el compose.
2. En la raíz del proyecto:
   ```bash
   docker-compose up -d --build
   ```
3. El sistema estará disponible a través del `api-gateway` en `http://localhost:9090`.

### Ejecución local sin Docker (desde el IDE)

1. Levantar MySQL localmente.
2. Ajustar `application.properties`/`application.yml` de cada microservicio para usar `localhost` en vez de `host.docker.internal`.
3. Ejecutar cada microservicio de forma individual desde IntelliJ/VS Code, respetando los puertos indicados en la tabla anterior.
4. Levantar `api-gateway` al final, una vez que los demás servicios estén activos.

## 🧪 Pruebas Unitarias

Los microservicios cuentan con pruebas unitarias (JUnit 5 + Mockito) sobre la capa de servicio y controladores, cubriendo reglas de negocio, casos de éxito y casos de error. Para ejecutarlas:

```bash
cd <nombre-del-microservicio>
./mvnw test
```

---

### 💡 Nota del Desarrollador

Este proyecto sigue buenas prácticas de arquitectura de software, incluyendo manejo centralizado de excepciones, separación de responsabilidades bajo el patrón CSR y documentación formal de API con Swagger/OpenAPI.
