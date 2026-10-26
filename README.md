# 🚗 AutoRent — Sistema de Alquiler de Autos (Enterprise Version)

Sistema empresarial de gestión de reservas, alquileres y control operativo de la flota vehicular de la compañía, desarrollado aplicando de forma estricta los principios de la **Arquitectura Hexagonal (Ports & Adapters)**, **Clean Code** y el diseño de **Modelo de Dominio Anémico**. El Core del negocio permanece 100% agnóstico e inmune a acoplamientos con frameworks de red, anotaciones de persistencia relacional o interfaces externas.

## 👥 Integrantes y Datos Generales
* **Nombre del Proyecto:** pc-autorent-jcoyh
* **Curso:** Programación Web Back-End
* **Docente:** Ing. Iván Devia Acosta
* **Desarrollador:** Johnny Coy Herrera

## 🛠️ Tecnologías y Módulos Utilizados
* **Lenguaje:** Java 17 (JDK Puro).
* **Framework Principal:** Spring Boot 4.1.2 (Restringido estrictamente a la periferia de infraestructura).
* **Persistencia:** Spring Data JPA / Hibernate Core.
* **Motor Relacional:** H2 Database Engine (In-Memory).
* **Frontend de Control Operativo:** HTML5, CSS3 Avanzado (Con estados visuales UX reactivos) y JavaScript Moderno (Async/Await & Event-Listeners).

---

## 🏛️ Estructura de la Arquitectura (Hexágono Puro)
El proyecto implementa un aislamiento perimetral total. Las clases situadas en el core (`domain/model`) son POJOs planos inmutables y anémicos. Toda la inyección de dependencias y el control de transacciones de la base de datos se centralizan en los muros de la infraestructura (`BeanConfiguration.java`).

```text
co.edu.clase.pc_autorent_jcoyh
├── domain/                         <-- NÚCLEO ANÉMICO PURO (0% anotaciones de Frameworks)
│   ├── model/                      <-- POJOs pasivos de datos (Auto, Cliente, Alquiler)
│   └── exception/                  <-- Control semántico de errores del negocio
├── application/                    <-- CAPA INTERMEDIA DE ORQUESTACIÓN
│   ├── port/                       
│   │   ├── input/                  <-- Casos de Uso (Fronteras de Entrada)
│   │   └── output/                 <-- Contratos de Persistencia (Puertos de Salida)
│   └── service/                    <-- Motores lógicos. Centralizan validaciones y ecuaciones
└── infrastructure/                 <-- PERIFERIA TÉCNICA (Frameworks y Herramientas)
    ├── adapter/
    │   ├── input/rest/             <-- Controladores HTTP y DTOs de amortiguación de red
    │   └── output/persistence/     <-- Entidades físicas, Mappers y Repositorios JPA
    └── config/                     <-- BeanConfiguration: Muro perimetral de inyección
```

---

## 💼 Reglas de Negocio y Aduanas Lógicas Implementadas

### 👥 Módulo de Clientes
1. **Validación Perimetral de Documento (POST):** Durante la creación, el payload captura la identificación como una cadena de texto a través de `CrearClienteDto`, permitiendo que el Servicio evalúe la expresión regular estricta `^[0-9]+$`. Esto detiene tramas corruptas conteniendo letras o guiones (ej. `"179-348625"`) en la frontera, transformando fallos de red en respuestas controladas `400 Bad Request`.
2. **Consistencia de Correo RFC 5322:** Los emails son validados bajo el patrón internacional RFC 5322, verificando la estructura `usuario@dominio.extension` e impidiendo puntos o arrobas consecutivas.
3. **Idempotencia Relacional de Cuentas:** Todo correo ingresado en mostrador o actualización es procesado mediante el método `.toLowerCase()`, garantizando que las cuentas se almacenen exclusivamente en minúsculas en H2 para evitar duplicidades semánticas.
4. **Máquina de Estados Eficiente (Reactivación Automática):** Al consumir el caso de uso `actualizarCliente`, si el cliente consultado se encontraba en estado `INACTIVO`, el sistema actualiza sus datos de contacto y altera de forma transparente su propiedad regresándolo al estado `ACTIVO`.

### 🚗 Módulo de Vehículos (Autos)
1. **Restricción de Placa Única Relacional:** El esquema físico de la tabla H2 define la columna placa con la propiedad `unique = true`, forzando la unicidad absoluta en el motor de base de datos independientemente del ID autoincremental de la fila.
2. **Aduana Estructural Vehicular:** Se implementa el patrón de expresiones regulares nativas `^[A-Z]{3}-[0-9]{3}$`, forzando a que toda placa nueva posea la máscara estricta de 3 letras mayúsculas, un guión divisor obligatorio y 3 números.
3. **Cota Cronológica Temporal:** El año de registro del automóvil debe constar de 4 dígitos exactos y ser estrictamente superior al año 2000.
4. **Proporcionalidad Financiera en Inventario:** El depósito de garantía exigido en mostrador no puede ser inferior, bajo ninguna circunstancia, al precio de alquiler por día estipulado para el vehículo.

### 📑 Módulo de Alquileres y Reservas
1. **Muro de Cruce de Fechas:** El sistema inspecciona los registros relacionales en H2 impidiendo transacciones o aperturas de contratos si las fechas solicitadas se cruzan cronológicamente con alguna reserva pendiente o activa previa sobre el mismo automóvil.
2. **Cierre Contractual Flexible y Rollover:** Al procesar un retorno en mostrador, el sistema recalcula los costos si las fechas reales difieren de las pactadas. Además, mediante la interfaz web se implementa un interruptor de *Rollover* para mitigar desfases matemáticos en caso de que el odómetro físico del carro haya completado su ciclo volviendo a cero.
3. **Disponibilidad Estricta:** Solo se pueden alquilar vehículos en estado `DISPONIBLE`.
4. **Flujo de Reservas:** Los contratos cuyas fechas de inicio sean futuras nacen en estado `PENDIENTE` (Reserva), permitiendo que el vehículo siga disponible en el mostrador para uso inmediato en el presente. Al invocar `activarReserva`, el contrato pasa a `ACTIVO` y el auto se bloquea a `ALQUILADO`.

---

## ⚡ Endpoints Estratégicos de la API REST

### Módulo Clientes
* `POST /api/clientes` - Alta de clientes blindada (Valida unicidad de identificación y formato de cadenas).
* `PUT /api/clientes/{id}/actualizar` - Modificación flexible de datos de contacto (Celular / Email) con reactivación automática de estados.
* `PUT /api/clientes/{id}/desactivar` - Inactivación definitiva (Baja lógica) validando la ausencia de rentas en curso.
* `GET /api/clientes` - Listar el registro completo de clientes.
* `GET /api/clientes/{id}` - Consultar un cliente por su ID.

### Módulo Vehículos (Autos)
* `POST /api/autos` - Inserción en catálogo en estado base `DISPONIBLE` (Aplica Regex de placa y cotas de años).
* `PUT /api/autos/{id}/tarifas` - Ajuste transaccional de precios diarios y depósitos (Valida que el depósito sea $\ge$ precio por día).
* `PUT /api/autos/{id}/desactivar` - Desincorporación de flota (Baja lógica).
* `GET /api/autos` - Listar todos los vehículos.
* `GET /api/autos/{id}` - Consultar un vehículo por su ID.

### Módulo Alquileres
* `POST /api/alquileres` - Mapeo de contratos celebrados. Si la fecha de inicio es futura nace como `PENDIENTE` (Reserva); si es presente nace `ACTIVO` bloqueando el vehículo en H2 a estado `ALQUILADO`.
* `PUT /api/alquileres/{id}/iniciar` - Check-in en mostrador de una reserva previa, capturando el millaje real del auto.
* `PUT /api/alquileres/{id}/finalizar` - Check-out contractual flexible con reliquidación temporal de tiempos financieros.
* `PUT /api/alquileres/{id}/cancelar` - Anulación total de contratos pendientes liberando los compromisos sobre el inventario.
* `GET /api/alquileres` - Recupera el listado histórico completo de contratos celebrados (Finalizados, Activos y Pendientes) almacenados en H2.
* `GET /api/alquileres/{id}` - Consulta la información detallada de un contrato de alquiler específico mediante su ID.

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

#### 🔹 Consultar todos los Alquileres
* **Ruta:** `GET /api/alquileres`
* **Descripción:** Recupera la lista completa de órdenes contractuales del sistema AutoRent para la sincronización de la grilla del Frontend.
* **Ejemplo de Respuesta (200 OK):**
```json
[
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
  },
  {
    "id": 6,
    "clienteId": 1,
    "autoId": 6,
    "fechaReserva": "2026-10-22",
    "fechaInicio": "2026-11-15",
    "fechaFin": "2026-11-18",
    "cantidadDias": 3,
    "valorTotal": 480000,
    "kilometrajeInicial": null,
    "kilometrajeFinal": null,
    "estado": "PENDIENTE"
  }
]
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
<img width="3016" height="5998" alt="pc-autorent-jcoyh ENTREGABLE Diagrama" src="https://github.com/user-attachments/assets/940f79a4-6b6f-4c21-beed-ccc090ffdbdd" />