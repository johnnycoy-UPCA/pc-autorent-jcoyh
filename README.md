# 🚗 AutoRent — Sistema de Alquiler de Autos

Sistema empresarial de gestión de reservas, alquileres y control operativo de vehículos, desarrollado aplicando de forma estricta los principios de la **Arquitectura Hexagonal (Ports & Adapters)** y **Clean Code**. El diseño garantiza que el núcleo del negocio permanezca 100% agnóstico e independiente de frameworks tecnológicos, librerías de persistencia o interfaces web externas.

## 👥 Integrantes y Datos Generales
* **Nombre del Proyecto:** pc-autorent-jcoyh
* **Curso:** Programación Web Back-End
* **Docente:** Ing. Iván Devia Acosta
* **Desarrollador:** Johnny Coy Herrera

## 🛠️ Tecnologías Utilizadas
* **Lenguaje:** Java 17
* **Framework Principal:** Spring Boot 4.1.1
* **Persistencia:** Spring Data JPA / Hibernate
* **Base de Datos:** H2 Database
* **Frontend de Pruebas:** HTML5, CSS3 y JavaScript

---

## 🏛️ Estructura de la Arquitectura
El proyecto implementa la arquitectura hexagonal, aislando por completo las clases del core de cualquier anotación de Spring Boot. Toda la inyección de dependencias y la transaccionalidad relacional se configuran de manera centralizada en la frontera de infraestructura (`BeanConfiguration.java`)

```text
co.edu.clase.pc_autorent_jcoyh
├── domain/                         <-- NÚCLEO PURO (0% Acoplado a Frameworks)
    │       └── repository/         <-- Interfaces nativas de Spring Data JpaRepository
    └── config/
        └── BeanConfiguration.java  <-- Muro Perimetral: Configura Beans y @Transactional
```

---

## 💼 Reglas de Negocio Implementadas
1. **Disponibilidad Estricta:** Solo se pueden alquilar vehículos en estado `DISPONIBLE`.
2. **Cálculo Automático Obligatorio:** El `valorTotal` del contrato se liquida de forma automática multiplicando la cantidad de días por el precio por día del carro. El cliente no puede forzar este monto desde la red.
3. **Flujo de Reservas:** Los contratos cuyas fechas de inicio sean futuras nacen en estado `PENDIENTE` (Reserva), permitiendo que el vehículo siga disponible en el mostrador para uso inmediato en el presente. Al invocar `activarReserva`, el contrato pasa a `ACTIVO` y el auto se bloquea a `ALQUILADO`.
4. **Muro de Cruce de Fechas:** No se permite la creación ni la activación de reservas si las fechas solicitadas se solapan con algún contrato activo o pendiente previo en el inventario H2.
5. **Cierre Flexible y Control de Odómetro:** Al finalizar una renta, se actualiza el millaje del vehículo. El sistema valida estrictamente que el odómetro final no sea menor al inicial y reajusta las tarifas automáticamente si el cliente devolvió el carro en una fecha diferente a la pactada.
6. **Reto Adicional (Sección 32):** Se implementó de forma completa el caso de uso `cancelarAlquiler` que permite anular contratos pendientes antes de su inicio, liberando los compromisos sobre el inventario.

---

## ⚡ Endpoints Mínimos de la API REST

### Vehículos (Autos)
* `POST /api/autos` - Registrar un nuevo vehículo (Nace disponible).
* `PUT /api/autos/mantenimiento` - Ingresar o liberar un vehículo del taller mecánico.
* `PUT /api/autos/{id}/desactivar` - Eliminación lógica (Dar de baja) a un auto del catálogo.
* `GET /api/autos` - Listar todos los vehículos.
* `GET /api/autos/{id}` - Consultar un vehículo por su ID.

### Clientes
* `POST /api/clientes` - Registrar un cliente (Valida correos y documentos únicos).
* `PUT /api/clientes/{id}/desactivar` - Dar de baja lógica a un cliente (Inactivación).
* `GET /api/clientes` - Listar el registro completo de clientes.

### Alquileres / Reservas (Implementación con DTOs)
* `POST /api/alquileres` - Crear un contrato en estado `PENDIENTE` o `ACTIVO` según la fecha de inicio.
* `PUT /api/alquileres/{id}/iniciar` - Check-in físico. Activa una reserva pendiente y captura el odómetro actual del carro.
* `PUT /api/alquileres/{id}/finalizar` - Cierre flexible del contrato. Ejecuta la reliquidación de tiempos y libera el auto.
* `PUT /api/alquileres/{id}/cancelar` - Cancelación total de un contrato antes de que inicie.

---

## 📟 DOCUMENTACIÓN DE LA API REST (ENDPOINTS)

Todos los endpoints de la API manejan datos en formato JSON tanto para las solicitudes (Payloads) como para las respuestas. Los tipos de datos para los odómetros han sido homogeneizados utilizando objetos de precisión decimal `Double` para evitar datos sucios en el motor H2.

---

### 🚗 1. MÓDULO DE VEHÍCULOS (AUTOS)

#### 🔹 Registrar Vehículo
* **Ruta:** `POST /api/autos`
* **Descripción:** Registra un nuevo automóvil en el inventario. Validación estricta por el campo placa. Todo vehículo nuevo se inicializa automáticamente en estado `DISPONIBLE`.
* **Ejemplo de Solicitud (Body JSON):**
```json
{
  "placa": "JCH-777",
  "marca": "Mazda",
  "modelo": "CX-30",
  "anio": 2026,
  "precioPorDia": 180000.0,
  "deposito": 500000.0,
  "kilometraje": 12500.0
}
```
* **Ejemplo de Respuesta Exitosa (201 Created):**
```json
{
  "id": 1,
  "placa": "JCH-777",
  "marca": "Mazda",
  "modelo": "CX-30",
  "anio": 2026,
  "precioPorDia": 180000.0,
  "deposito": 500000.0,
  "kilometraje": 12500.0,
  "estado": "DISPONIBLE"
}
```

#### 🔹 Consultar todos los Vehículos
* **Ruta:** `GET /api/autos`
* **Descripción:** Recupera la lista completa de vehículos almacenados en la base de datos relacional.
* **Ejemplo de Respuesta (200 OK):**
```json
[
  {
    "id": 1,
    "placa": "JCH-777",
    "estado": "DISPONIBLE",
    "kilometraje": 12500.0
  }
]
```

#### 🔹 Control del Taller Mecánico (Mantenimiento)
* **Ruta:** `PUT /api/autos/mantenimiento`
* **Descripción:** Envía o retira un auto del taller de mantenimiento. El inventario valida de forma autónoma que el vehículo no cuente con contratos activos.
* **Ejemplo de Solicitud (Body JSON - ActualizarMantenimientoDto):**
```json
{
  "placa": "JCH-777",
  "enMantenimiento": true,
  "kilometraje": 12500.0
}
```
* **Ejemplo de Respuesta (200 OK):**
```json
{
  "id": 1,
  "placa": "JCH-777",
  "estado": "MANTENIMIENTO",
  "kilometraje": 12500.0
}
```

#### 🔹 Dar de Baja un Vehículo
* **Ruta:** `PUT /api/autos/{id}/desactivar`
* **Descripción:** Inactiva permanentemente un vehículo del catálogo por desuso o desincorporación de flota.
* **Ejemplo de Respuesta (200 OK):**
```json
{
  "id": 1,
  "placa": "JCH-777",
  "estado": "INACTIVO"
}
```

---

### 👥 2. MÓDULO DE CLIENTES

#### 🔹 Registrar Cliente
* **Ruta:** `POST /api/clientes`
* **Descripción:** Crea un registro de cliente. El sistema valida de forma estricta que el documento de identificación sea único.
* **Ejemplo de Solicitud (Body JSON):**
```json
{
  "nombre": "Carlos Pérez",
  "tipoDocumento": "CC",
  "documento": 1020304050,
  "celular": "3001234567",
  "email": "carlos@email.com"
}
```
* **Ejemplo de Respuesta (201 Created):**
```json
{
  "id": 1,
  "nombre": "Carlos Pérez",
  "tipoDocumento": "CC",
  "documento": 1020304050,
  "celular": "3001234567",
  "email": "carlos@email.com",
  "estado": "ACTIVO"
}
```

---

### 📑 3. MÓDULO DE ALQUILERES Y RESERVAS

#### 🔹 Apertura de Reservas / Alquileres
* **Ruta:** `POST /api/alquileres`
* **Descripción:** Registra un nuevo contrato. El sistema calcula automáticamente el `valorTotal` (precioPorDia × cantidadDias). Si la fecha de inicio es presente, el estado cambia a `ACTIVO` y el vehículo se bloquea; si es a futuro, se guarda en estado `PENDIENTE` y los campos de kilometraje nacen en `NULL`.
* **Ejemplo de Solicitud (Body JSON - CrearAlquilerDto):**
```json
{
  "clienteId": 1,
  "autoId": 1,
  "cantidadDias": 5,
  "fechaInicio": "2026-10-23"
}
```
* **Ejemplo de Respuesta Exitosa (201 Created):**
```json
{
  "id": 1,
  "clienteId": 1,
  "autoId": 1,
  "fechaReserva": "2026-10-21",
  "fechaInicio": "2026-10-23",
  "fechaFin": "2026-10-28",
  "cantidadDias": 5,
  "valorTotal": 900000.0,
  "kilometrajeInicial": 12500.0,
  "kilometrajeFinal": null,
  "estado": "ACTIVO"
}
```

#### 🔹 Entregar Vehículo (Iniciar Reserva Pendiente)
* **Ruta:** `PUT /api/alquileres/{id}/iniciar`
* **Descripción:** Realiza el check-in físico de una reserva que estaba a futuro (`PENDIENTE`). En este instante, captura de forma segura el kilometraje actual del auto y bloquea su disponibilidad.
* **Ejemplo de Respuesta (200 OK):**
```json
{
  "id": 2,
  "estado": "ACTIVO",
  "kilometrajeInicial": 12500.0
}
```

#### 🔹 Finalizar Alquiler (Check-in de Retorno)
* **Ruta:** `PUT /api/alquileres/{id}/finalizar`
* **Descripción:** Cierra el contrato de alquiler, actualiza el odómetro acumulado del auto físico y libera el vehículo regresándolo a estado `DISPONIBLE`.
* **Ejemplo de Solicitud (Body JSON - FinalizarAlquilerDto):**
```json
{
  "kilometrajeFinal": 12850.0,
  "fechaFin": "2026-10-28"
}
```
* **Ejemplo de Respuesta (200 OK):**
```json
{
  "id": 1,
  "estado": "FINALIZADO",
  "kilometrajeInicial": 12500.0,
  "kilometrajeFinal": 12850.0
}
```

#### 🔹 Cancelar Alquiler / Reserva
* **Ruta:** `PUT /api/alquileres/{id}/cancelar`
* **Descripción:** Cancela un contrato. El dominio valida de forma autónoma que el alquiler se encuentre en estado válido (`PENDIENTE` o `ACTIVO`).
* **Ejemplo de Respuesta (200 OK):**
```json
{
  "id": 1,
  "estado": "CANCELADO"
}
```

---

## 📝 Ejemplos de Solicitudes y Respuestas HTTP (Ciclo Completo)

A continuación se detalla la secuencia cronológica de transacciones en la red para un flujo operativo de inicio a fin en la plataforma:

### 1. Registrar un Cliente (`POST /api/clientes`)
**Solicitud (JSON):**
```json
{
  "nombre": "Carlos Pérez",
  "documento": "123456789",
  "celular": "3001234567",
  "email": "carlos@email.com"
}
```
**Respuesta (`201 Created`):**
```json
{
  "id": 1,
  "nombre": "Carlos Pérez",
  "documento": "123456789",
  "celular": "3001234567",
  "email": "carlos@email.com",
  "estado": "ACTIVO"
}
```

### 2. Registrar un Vehículo (`POST /api/autos`)
**Solicitud (JSON):**
```json
{
  "placa": "XYZ-123",
  "marca": "Toyota",
  "modelo": "Corolla",
  "anio": 2025,
  "precioPorDia": 150000.0,
  "deposito": 500000.0,
  "kilometraje": 10000.0
}
```
**Respuesta (`201 Created`):**
```json
{
  "id": 1,
  "placa": "XYZ-123",
  "marca": "Toyota",
  "modelo": "Corolla",
  "anio": 2025,
  "precioPorDia": 150000.0,
  "deposito": 500000.0,
  "kilometraje": 10000.0,
  "estado": "DISPONIBLE"
}
```

### 3. Ingresar el Vehículo a Taller (`PUT /api/autos/mantenimiento`)
**Solicitud (JSON):**
```json
{
  "placa": "XYZ-123",
  "enMantenimiento": true,
  "kilometraje": 10000.0
}
```
**Respuesta (`200 OK`):**
```json
{
  "id": 1,
  "placa": "XYZ-123",
  "marca": "Toyota",
  "modelo": "Corolla",
  "anio": 2025,
  "precioPorDia": 150000.0,
  "deposito": 500000.0,
  "kilometraje": 10000.0,
  "estado": "MANTENIMIENTO"
}
```

### 4. Salida de Taller Mecánico (`PUT /api/autos/mantenimiento`)
**Solicitud (JSON):**
```json
{
  "placa": "XYZ-123",
  "enMantenimiento": false,
  "kilometraje": 10050.0
}
```
**Respuesta (`200 OK`):**
```json
{
  "id": 1,
  "placa": "XYZ-123",
  "marca": "Toyota",
  "modelo": "Corolla",
  "anio": 2025,
  "precioPorDia": 150000.0,
  "deposito": 500000.0,
  "kilometraje": 10050.0,
  "estado": "DISPONIBLE"
}
```

### 5. Reservar un Alquiler a Futuro (`POST /api/alquileres`)
**Solicitud (JSON mapeado con `CrearAlquilerDto`):**
```json
{
  "clienteId": 1,
  "autoId": 1,
  "cantidadDias": 4,
  "fechaInicio": "2026-11-01"
}
```
**Respuesta (`201 Created`):**
```json
{
  "id": 1,
  "clienteId": 1,
  "autoId": 1,
  "fechaReserva": "2026-10-21",
  "fechaInicio": "2026-11-01",
  "fechaFin": "2026-11-05",
  "cantidadDias": 4,
  "valorTotal": 600000.0,
  "kilometrajeInicial": null,
  "kilometrajeFinal": null,
  "estado": "PENDIENTE"
}
```

### 6. Iniciar el Alquiler (Check-in Físico — `PUT /api/alquileres/1/iniciar`)
**Solicitud (URL Path, sin cuerpo):**
```text
PUT http://localhost:8081/api/alquileres/1/iniciar
```
**Respuesta (`200 OK`):**
```json
{
  "id": 1,
  "clienteId": 1,
  "autoId": 1,
  "fechaReserva": "2026-10-21",
  "fechaInicio": "2026-11-01",
  "fechaFin": "2026-11-05",
  "cantidadDias": 4,
  "valorTotal": 600000.0,
  "kilometrajeInicial": 10050.0,
  "kilometrajeFinal": null,
  "estado": "ACTIVO"
}
```

### 7. Terminar y Liquidar Alquiler (`PUT /api/alquileres/1/finalizar`)
**Solicitud (JSON mapeado con `FinalizarAlquilerDto`):**
```json
{
  "kilometrajeFinal": 10500.0,
  "fechaFin": "2026-11-05"
}
```
**Respuesta (`200 OK`):**
```json
{
  "id": 1,
  "clienteId": 1,
  "autoId": 1,
  "fechaReserva": "2026-10-21",
  "fechaInicio": "2026-11-01",
  "fechaFin": "2026-11-05",
  "cantidadDias": 4,
  "valorTotal": 600000.0,
  "kilometrajeInicial": 10050.0,
  "kilometrajeFinal": 10500.0,
  "estado": "FINALIZADO"
}
```

### 8. Dar de Baja el Auto (`PUT /api/autos/1/desactivar`)
**Solicitud (URL Path):**
```text
PUT http://localhost:8081/api/autos/1/desactivar
```
**Respuesta (`200 OK`):**
```json
{
  "id": 1,
  "placa": "XYZ-123",
  "marca": "Toyota",
  "modelo": "Corolla",
  "anio": 2025,
  "precioPorDia": 150000.0,
  "deposito": 500000.0,
  "kilometraje": 10500.0,
  "estado": "INACTIVO"
}
```

### 9. Dar de Baja el Cliente (`PUT /api/clientes/1/desactivar`)
**Solicitud (URL Path):**
```text
PUT http://localhost:8081/api/clientes/1/desactivar
```
**Respuesta (`200 OK`):**
```json
{
  "id": 1,
  "nombre": "Carlos Pérez",
  "documento": "123456789",
  "celular": "3001234567",
  "email": "carlos@email.com",
  "estado": "INACTIVO"
}
```

### 2. Manejo de Errores de Negocio (`GlobalExceptionHandler` en Acción)
Cuando se intenta finalizar un alquiler mandando un kilometraje menor al registrado inicialmente, el core dispara un `AlquilerException` y el interceptor global responde de la siguiente manera:

**Respuesta de Error (`400 Bad Request`):**
```json
{
  "timestamp": "2026-10-21T09:14:02.125",
  "codigo": 400,
  "error": "Bad Request",
  "mensaje": "El kilometraje ingresado (12000.00) no puede ser menor que el kilometraje registrado (12500.00) del vehículo."
}
```

---

## 🚀 Guía de Compilación y Ejecución desde IDEs

El proyecto utiliza **Apache Maven** como motor de automatización, garantizando portabilidad absoluta en cualquier entorno de desarrollo. Como prerrequisito global, se debe tener configurado el **Java JDK 17** en la máquina y en el IDE de elección.

---

### ☕ Opción 1: Ejecución desde Apache NetBeans

1. **Importar el Proyecto Relacional:**
   * Abrir NetBeans (Versión 12 o superior recomendada).
   * Seleccionar el menú superior **File** ──> **Open Project...**
   * Navegar hasta el directorio raíz del proyecto (`pc-autorent-jcoyh`). El IDE detectará de forma automática el archivo raíz **`pom.xml`** y mostrará el icono del proyecto con una "M" de Maven.
   * Hacer clic en **Open Project**.
2. **Descargar Dependencias (Clean and Build):**
   * Hacer clic derecho sobre la carpeta raíz del proyecto en el panel izquierdo de navegación (*Projects*).
   * Seleccionar la opción **Clean and Build**.
   * NetBeans ejecutará Maven para descargar los componentes de Spring Boot, Spring Data JPA y el motor H2 en memoria.
3. **Lanzar el Microservicio:**
   * Desplegar las siguientes carpetas: `Source Packages` ──> `co.edu.clase.pc_autorent_jcoyh`.
   * Buscar la clase principal que inicializa el ecosistema: **`PcAutorentJcoyhApplication.java`**.
   * Hacer clic derecho sobre este archivo y seleccionar la opción **Run File** (o presionar la combinación **`Shift + F6`**).
4. **Verificación:** Monitorear la ventana inferior **`Output`**. El servidor estará activo cuando se visualice la confirmación del puerto embebido **`8081`**.

---

### 💻 Opción 2: Ejecución desde Visual Studio Code (VS Code)

*Nota: Requiere tener instalados los plugins oficiales "Extension Pack for Java" y "Spring Boot Extension Pack" en el entorno.*

1. **Importar el Proyecto:**
   * Abrir VS Code.
   * Hacer clic en **File** ──> **Open Folder...**
   * Seleccionar la carpeta raíz `pc-autorent-jcoyh`.
2. **Construcción y Descarga de Librerías:**
   * Desplegar la terminal interna integrada con el atajo universal **`Ctrl + ~`** (o `Cmd + ~` en Mac).
   * Ejecutar el comando de compilación segura saltando la verificación de pruebas unitarias obsoletas:
     ```bash
     mvn clean package -Dmaven.test.skip=true
     ```
3. **Lanzar el Microservicio:**
   * En el explorador de archivos, navegar hasta el paquete principal: `src/main/java/co/edu/clase/pc_autorent_jcoyh/`.
   * Abrir la clase **`PcAutorentJcoyhApplication.java`**.
   * Hacer clic en el enlace flotante **`Run`** ubicado inmediatamente encima de la firma del método `public static void main` (o presiona la tecla **`F5`**).
4. **Verificación:** Valida el flujo del arranque en la pestaña inferior *Debug Console* comprobando que el servidor levantó en el puerto **`8081`**.

---

### 💎 Opción 3: Ejecución desde IntelliJ IDEA

1. **Importar el Proyecto:**
   * Abrir IntelliJ IDEA.
   * Seleccionar la opción **Open** e indicar la ruta de la carpeta del proyecto.
   * Si el entorno lo solicita, marcar **Open as Project** y luego **Trust Project**.
2. **Sincronizar el Entorno Maven:**
   * Al cargar la estructura, hacer clic en la alerta flotante inferior que indica **Load Maven Project** (o abrir el panel lateral derecho *Maven* y presionar el botón circular de actualizar). El IDE mapeará el archivo `pom.xml` automáticamente.
3. **Lanzar el Microservicio:**
   * Presionar la combinación **`Double Shift`** (tecla Shift dos veces) para abrir la barra de búsqueda global.
   * Escribir **`PcAutorentJcoyhApplication`** y presionar Enter para abrir el archivo.
   * Hacer clic sobre el icono del triángulo verde (*Play*) situado a la izquierda del método `main`, y seleccionar **Run 'PcAutorentJcoyhApplication.main()'** (o utilizar el atajo **`Shift + F10`**).
4. **Verificación:** Confirmar el despliegue limpio y sin errores analizando la consola de la pestaña inferior *Run*.

---

### 🌐 Herramientas de Verificación Perimetral (Común a todos los IDEs)

Una vez que el servidor web embebido se encuentra en ejecución por medio de cualquiera de los entornos anteriores, se habilitan los accesos en la red local:

*   **Consola Administrativa SQL (H2 Database):** Abrir el navegador e ingresar a [http://localhost:8081/h2-console](http://localhost:8081/h2-console). 
    *   *Configuración obligatoria:* Asegurarse de cambiar el campo **JDBC URL** a `jdbc:h2:mem:autorentdb`, escribir `sa` en **User Name** y dejar la casilla de la contraseña totalmente vacía.
*   **Panel Operativo Frontend de Pruebas:** Si lo desea, puede abrir de manera directa en el navegador Google Chrome el archivo estático ubicado en la ruta del proyecto: `src/main/resources/static/index_v7.html` o ingresando a la ruta [http://localhost:8081/index_v7.html](http://localhost:8081/index_v7.html) para simular las transacciones y peticiones HTTP en tiempo real.

## Diagrama del proyecto
<img width="3016" height="5998" alt="pc-autorent-jcoyh ENTREGABLE Diagrama" src="https://github.com/user-attachments/assets/940f79a4-6b6f-4c21-beed-ccc090ffdbdd" />s