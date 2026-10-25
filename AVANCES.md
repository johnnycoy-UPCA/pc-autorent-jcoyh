# 📑 Bitácora de Avances - Proyecto AutoRent (`pc-autorent-jcoyh`)

Documentación cronológica de la implementación del microservicio de Alquiler de Autos bajo los lineamientos de la Arquitectura Hexagonal.

---

## 📅 Octubre 7, 2026

### 🛠️ Fase 0: Inicialización y Configuración del Entorno
* **Inicialización del Lienzo:** Creación del proyecto base en Spring Initializr utilizando Maven, Java 17 y definiendo el artefacto único `pc-autorent-jcoyh`.
* **Configuración del pom.xml:** Inyección de las dependencias core del framework: `spring-boot-starter-web` (Capa REST), `spring-boot-starter-data-jpa` (Persistencia), `h2` (Motor de base de datos en memoria) y `spring-boot-starter-test`.
* **Verificación del Motor de Arranque:** Creación y validación de la clase principal de encendido `PcAutorentJcoyhApplication.java` dentro del paquete raíz `co.edu.clase.pc_autorent_jcoyh`.
* **Superación del Obstáculo de Permisos (Administrador):** Identificación del error de comandos globales (`mvn`) por restricciones del sistema. Solución mediante la adopción del Maven Wrapper local (`./mvnw`) para ejecutar la compilación de forma autónoma. Falló igualmente.

### 🧱 Fase 1: Capa de Dominio Puro (El Corazón del Negocio)
* **Aislamiento Tecnológico:** Creación del paquete `domain` completamente desacoplado de Spring Boot, JPA o Hibernate (clases Java puras sin anotaciones decorativas).
* **Definición de Excepciones del Dominio:** Implementación de `AutoNoDisponibleException` y `AlquilerNoValidoException` bajo `domain.exception` para la gestión limpia de errores de negocio.
* **Modelado de Entidades Funcionales:** 
  * `Auto.java`: Implementación del estado (`DISPONIBLE`, `ALQUILADO`, `MANTENIMIENTO`) y las reglas de cambio de estado controladas (`alquilar()`, `liberar()`).
  * `Cliente.java`: Modelado del POJO básico con datos del usuario.
  * `Alquiler.java`: Inyección de las reglas matemáticas obligatorias (`calcularYConfigurarAlquiler()`) garantizando que el `valorTotal` sea computado por el sistema y no por el cliente. Control de doble finalización (`finalizar()`).

### 🛡️ Adaptación de Entorno con Restricciones Estrictas
* **Bypass de Directivas del Sistema:** Se abandona el uso de comandos globales y de ciclo de vida Maven basados en scripts de consola para mitigar bloqueos de Windows.
* **Adopción de Compilación en Memoria:** Configuración del arranque nativo mediante el *Spring Boot Dashboard* de VS Code, permitiendo compilar y ejecutar el microservicio directamente a través de hilos de Java controlados por el IDE.
* **Aislamiento de Red:** Configuración del puerto de escucha `8081` dentro de `src/main/resources/application.properties` para evitar restricciones de puertos (`8080`).

### 🚀 Validación de Arranque Exitoso
* **Despliegue del Motor:** Ejecución en frío exitosa desde el Spring Boot Dashboard en el puerto alterno `8081` sin fallas de colisión de red o restricciones de políticas del sistema corporativo.
* **Planificación de Persistencia In-Memory:** Almacenamiento temporal de las propiedades de base de datos H2 y JPA suministradas por la cátedra, postergando su inserción para salvaguardar el aislamiento del núcleo en las fases iniciales de la Arquitectura Hexagonal.

### 📌 CIERRE DE LA FASE 1: Capa de Dominio Puro
* **Estatus de la Capa:** COMPLETA Y VALIDADA EN ENTORNO LOCAL.

### 🔄 Control de Cambios de Último Minuto (Reglas Funcionales)
* **Refactorización en Entidad Auto:** 
  * Inyección del campo `deposito` (double) para registrar el depósito de seguridad inicial requerido.
  * Inyección del campo `kilometraje` (int) para centralizar de forma histórica los kilómetros totales recorridos por el vehículo.
* **Refactorización en Entidad Alquiler e Interacción de Datos:**
  * Inyección de las propiedades `kilometrajeInicial` y `kilometrajeFinal`.
  * Rediseño de `calcularYConfigurarAlquiler()` para capturar y congelar automáticamente el kilometraje de salida del auto.
  * Rediseño de `finalizar()` implementando validaciones de consistencia lógica (el kilometraje final nunca puede ser inferior al inicial).
  * **Sincronización Automática:** Configuración de la regla de negocio cruzada donde la devolución del alquiler actualiza directamente el odómetro total del objeto `Auto` y ejecuta su correspondiente método `.liberar()`.


---

## 📅 Octubre 8, 2026

### 📌 APERTURA DE LA FASE 2: Capa de Aplicación (Puertos y Servicios)
* **Estatus de la Capa:** EN DESARROLLO E IMPLEMENTACIÓN DE CONTRATOS.
* **Aislamiento Core Java:** Construcción de la capa libre de anotaciones del framework Spring (`@Service`, `@Autowired`), manteniendo las interfaces como puros contratos arquitectónicos.

### 🧱 Implementación Funcional de la Fase 2
* **Diseño de Puertos de Entrada (Casos de Uso):**
  * `RegistrarAutoUseCase`: Firma encargada de la inserción de vehículos en el sistema.
  * `CrearAlquilerUseCase`: Firma que encapsula la intención de iniciar una renta.
  * `FinalizarAlquilerUseCase`: Firma que captura el evento de recepción del automóvil.
* **Diseño de Puertos de Salida (Persistencia Abstracta):**
  * `AutoRepositoryPort` y `AlquilerRepositoryPort`: Abstracciones completas que aíslan la lógica del negocio de los mecanismos tecnológicos de bases de datos.
* **Despliegue de Servicios Coordinadores:**
  * `AutoService`: Implementa la creación de autos forzando por diseño de aplicación el estado inicial `DISPONIBLE`.
  * `AlquilerService`: Orquestador fundamental. Recupera las entidades de los puertos abstractos, invoca los métodos de negocio del dominio (`auto.alquilar()`, `alquiler.finalizar()`) y delega la actualización de los estados en la persistencia.

### 📌 APERTURA DE LA FASE 2: Capa de Aplicación (Puertos y Servicios)
* **Estatus de la Capa:** EN DESARROLLO E IMPLEMENTACIÓN DE CONTRATOS.
* **Aislamiento Core Java:** Construcción de la capa libre de anotaciones del framework Spring (`@Service`, `@Autowired`), manteniendo las interfaces como puros contratos arquitectónicos.

### 🧱 Implementación Funcional de la Fase 2
* **Diseño de Puertos de Entrada (Casos de Uso):**
  * `RegistrarAutoUseCase`: Firma encargada de la inserción de vehículos en el sistema.
  * `CrearAlquilerUseCase`: Firma que encapsula la intención de iniciar una renta.
  * `FinalizarAlquilerUseCase`: Firma que captura el evento de recepción del automóvil.
* **Diseño de Puertos de Salida (Persistencia Abstracta):**
  * `AutoRepositoryPort` y `AlquilerRepositoryPort`: Abstracciones completas que aíslan la lógica del negocio de los mecanismos tecnológicos de bases de datos.
* **Despliegue de Servicios Coordinadores:**
  * `AutoService`: Implementa la creación de autos forzando por diseño de aplicación el estado inicial `DISPONIBLE`.
  * `AlquilerService`: Orquestador fundamental. Recupera las entidades de los puertos abstractos, invoca los métodos de negocio del dominio (`auto.alquilar()`, `alquiler.finalizar()`) y delega la actualización de los estados en la persistencia.

### 🔄 Refinamiento Crítico de Excepciones en Aplicación
* **Eliminación Total de IDs Técnicos:** Se modificó la excepción en el método de búsqueda inicial de `AlquilerService.java`. En caso de no existir el registro en el puerto, se suprime el ID numérico (`autoId`) reemplazándolo por el mensaje semántico de negocio: `"El vehículo solicitado no se encuentra registrado en el sistema."`.
* **Seguridad NullPointer:** Se garantiza la consistencia del flujo evitando llamadas al método `.getPlaca()` sobre referencias nulas durante fallas de búsqueda.

### 📌 APERTURA DE LA FASE 3: Infraestructura de Adaptadores Web (Controllers)
* **Estatus de la Capa:** EN DESARROLLO - ADAPTADORES DE ENTRADA CONFIGURADOS.
* **Mapeo de Rutas REST:** Implementación de componentes anotados con `@RestController` de Spring Boot para exponer la interfaz pública del sistema.

### 🧱 Implementación Funcional de la Fase 3
* **Despliegue de Endpoints en AutoController:**
  * `POST /api/autos`: Diseñado para recibir las solicitudes de inserción de vehículos mapeando directamente hacia el puerto `RegistrarAutoUseCase`.
* **Despliegue de Endpoints en AlquilerController:**
  * `POST /api/alquileres`: Captura las solicitudes de apertura de contratos extrayendo de forma limpia los parámetros requeridos por `CrearAlquilerUseCase`.
  * `PUT /api/alquileres/{id}/finalizar`: Ruta dedicada a procesar las devoluciones de autos mediante capturas de payload dinámicas para inyectar el kilometraje final hacia el caso de uso.

### 🧩 Alineación de Inyección de Dependencias
* **Validación de Requisitos del Profesor:** Se verificó la consistencia del archivo de ensamblaje con respecto a la sección 10 del PDF de la cátedra (`infrastructure/config/BeanConfiguration.java`).
* **Blindaje de la Capa de Aplicación:** Se confirma el rechazo al uso de anotaciones intrusivas del framework (`@Service`) en el núcleo del sistema, delegando el ciclo de vida de los componentes al esquema de Beans explícitos para asegurar la testabilidad y el desacoplamiento exigidos.

### 📌 CIERRE DE LA FASE 3: Infraestructura de Adaptadores Web y Configuración
* **Estatus de la Capa:** COMPLETA Y CONECTADA MEDIANTE ENSAMBLAJE DE BEANS.

### 🧱 Elementos Técnicos Incorporados
* **Despliegue del Cableado Central:** Creación de `infrastructure.config.BeanConfiguration.java` utilizando las anotaciones tecnológicas `@Configuration` y `@Bean`.
* **Bypass de Inyección Spring:** Se inyectaron explícitamente los orquestadores pivos `AutoService` y `AlquilerService` en el contenedor virtual de Spring Boot, suministrándoles sus dependencias requeridas sin alterar su pureza de código Java Estándar.
* **Persistencia Volátil Simulada:** Implementación de adaptadores de salida anónimos en memoria (`ConcurrentHashMap`) dentro del archivo de configuración, asegurando el desacoplamiento total y permitiendo que los endpoints REST interactúen de forma funcional antes de incorporar el motor físico de H2 y JPA.


=====================================================================

# 📑 Bitácora de Avances - Proyecto AutoRent (`pc-autorent-jcoyh`)

Documentación cronológica de la implementación del microservicio de Alquiler de Autos bajo los lineamientos de la Arquitectura Hexagonal.

---

## 📅 Octubre 7, 2026

### 🛠️ Fase 0: Inicialización y Configuración del Entorno
* **Inicialización del Lienzo:** Creación del proyecto base en Spring Initializr utilizando Maven, Java 17 y definiendo el artefacto único `pc-autorent-jcoyh`.
* **Configuración del pom.xml:** Inyección de las dependencias core del framework: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `h2` y `spring-boot-starter-test`.
* **Verificación del Motor de Arranque:** Creación y validación de la clase principal de encendido `PcAutorentJcoyhApplication.java` dentro del paquete raíz `co.edu.clase.pc_autorent_jcoyh`.
* **Aislamiento de Red:** Configuración del puerto de escucha `8081` dentro de `src/main/resources/application.properties` para evitar colisiones de puertos con el desarrollo paralelo de NetBeans (`8080`).

### 🧱 CIERRE DE LA FASE 1: Capa de Dominio Puro
* **Estatus de la Capa:** COMPLETA Y VALIDADA EN ENTORNO LOCAL.
* **Aislamiento Tecnológico:** Creación del paquete `domain` completamente desacoplado de Spring Boot, JPA o Hibernate (clases Java puras sin anotaciones decorativas).
* **Definición de Excepciones del Dominio:** Implementación de `AutoNoDisponibleException` y `AlquilerNoValidoException` bajo `domain.exception` para la gestión limpia de errores de negocio.
* **Modelado de Entidades Funcionales y Control de Cambios:** 
  * `Auto.java`: Implementación de los estados mínimos (`DISPONIBLE`, `ALQUILADO`, `MANTENIMIENTO`) junto con los nuevos atributos de negocio requeridos: `deposito` (seguridad inicial obligatoria) y `kilometraje` (historial de odómetro acumulado).
  * `Cliente.java`: Modelado del POJO básico con datos del usuario.
  * `Alquiler.java`: Inyección de las propiedades `kilometrajeInicial` y `kilometrajeFinal`. Diseño de la regla cruzada donde la devolución del vehículo calcula consistencias lógicas, actualiza el kilometraje histórico del auto y ejecuta de forma atómica su método `.liberar()`.

---

## 📅 Octubre 8, 2026

### 📌 CIERRE DE LA FASE 2: Capa de Aplicación (Puertos y Servicios)
* **Estatus de la Capa:** COMPLETA Y VALIDADA EN ENTORNO LOCAL.
* **Diseño de Puertos de Entrada (Casos de Uso):** Creación de las firmas arquitectónicas core: `RegistrarAutoUseCase`, `CrearAlquilerUseCase` y `FinalizarAlquilerUseCase`.
* **Diseño de Puertos de Salida (Persistencia Abstracta):** Creación de `AutoRepositoryPort` y `AlquilerRepositoryPort` para independizar el negocio de los mecanismos tecnológicos de almacenamiento.
* **Despliegue de Servicios Coordinadores:** Implementación de `AutoService` y `AlquilerService`. Refinamiento humano de excepciones sustituyendo los IDs técnicos de base de datos (`autoId`) por mensajes semánticos y la propiedad de identidad natural de la placa.

### 📌 CIERRE DE LA FASE 3: Adaptadores Web e Infraestructura de Configuración
* **Estatus de la Capa:** COMPLETA Y LEVANTADA CON ÉXITO EN EL PUERTO 8081.
* **Despliegue de Adaptadores Web REST:** Creación de `AutoController` (`POST /api/autos`) y `AlquilerController` (`POST /api/alquileres`, `PUT /api/alquileres/{id}/finalizar`) utilizando componentes anotados con `@RestController` de Spring Boot.
* **Despliegue del Cableado Central (Beans):** Creación de `infrastructure.config.BeanConfiguration.java` utilizando `@Configuration` y `@Bean`. Inyección explícita de los servicios puros en el contenedor virtual de Spring Boot en conjunto con una persistencia volátil simulada (`ConcurrentHashMap`), permitiendo interactuar con los endpoints REST de forma funcional y en caliente.

==================================================

### 🧪 Fase 3.5: Validación Funcional en Caliente
* **Despliegue de Suite Integradora:** Creación del archivo script nativo `pruebas.http` para la simulación interactiva de tráficos HTTP directos en la interfaz del editor.
* **Resultados de Batería de Pruebas:**
  * **Registro Automotor:** Verificación exitosa del endpoint `POST /api/autos`. Asignación del identificador inicial secuencial `1` e inyección por defecto del estado funcional `DISPONIBLE`.
  * **Contratación Volátil:** Verificación exitosa del endpoint `POST /api/alquileres`. Se comprobó el cómputo automático del valor acumulado total (\$900.000) y el aislamiento del odómetro de salida.
  * **Cierre de Ciclo Operativo:** Verificación exitosa del endpoint `PUT /api/alquileres/{id}/finalizar`, comprobando la mutación coherente de los estados del negocio en la memoria intermedia de los Beans configurados.

### 🔄 Ajuste en Mensajes de Validación de Catálogo
* **Identificación de Duplicados por ID:** Modificación en `AutoService.java` para interceptar registros con placas repetidas y notificar explícitamente al usuario el ID técnico bajo el cual está guardado el vehículo existente en el sistema.
* **Soporte de Búsqueda Volátil:** Inyección del método de filtrado por streaming en `BeanConfiguration.java` para resolver las consultas del puerto abstracta sobre estructuras de mapas.

### 📌 APERTURA DE LA FASE 4: Infraestructura de Persistencia Real (JPA y H2)
* **Estatus de la Capa:** EN DESARROLLO - MOTOR RELACIONAL ACTIVADO.
* **Activación de Persistencia In-Memory:** Inyección de propiedades de base de datos relacional para levantar el motor H2 (`jdbc:h2:mem:autorentdb`) en el entorno de VS Code.
* **Resolución de Advertencias:** Inclusión de la directiva `spring.jpa.open-in-view=false` para optimizar el rendimiento de consultas y limpiar los logs de la consola.

### 🔧 Verificación de Advertencias de Configuración H2
* **Diagnóstico de Mapeo en el IDE:** Identificación de avisos preventivos (`is an unknown property`) en la suite de VS Code asociados a las directivas de la consola web de H2.
* **Validación de Contexto:** Se determina la naturaleza no bloqueante del aviso, generada por la ausencia temporal de entidades JPA físicas mapeadas en el motor relacional. Se procede con el arranque seguro en frío.

### 🔧 Optimización y Depuración de Logs de Persistencia
* **Remoción de Dialecto Redundante:** Se eliminó la propiedad `spring.jpa.database-platform` del archivo `application.properties` tras interceptar la advertencia `HHH90000025`. Se delega la resolución del dialecto relacional al motor de autodetección nativo de Hibernate, garantizando logs de consola 100% limpios de advertencias.

### 🧱 Despliegue de Componentes Relacionales (JPA)
* **Modelado Físico Automotor:** Creación de `AutoEntity.java` bajo el paquete `persistence.entity` utilizando anotaciones nativas de persistencia (`@Entity`, `@Table`).
* **Estructuración de Columnas Únicas:** Configuración del atributo `placa` indexado como llave única de negocio (`unique = true`) a nivel relacional en el motor H2.
* **Mapeo Integrado de Atributos:** Inserción de los campos físicos de persistencia `deposito` (double) y `kilometraje` (int) en perfecta sincronía con los cambios de odómetro e ingresos validados previamente en caliente.

### 🧱 Conexión de Componentes Físicos de Persistencia
* **Despliegue de Interfaces de Datos:** Creación de `AutoJpaRepository.java` extendiendo las consultas nativas de Spring Data JPA.
* **Implementación del Mapeador Técnico:** Creación de `AutoMapper.java` encargado del aislamiento bidireccional entre la entidad relacional y el POJO de negocio.
* **Puente Arquitectónico:** Despliegue de `AutoPersistenceAdapter.java` para consolidar el cumplimiento del puerto de salida abstracta delegando la persistencia real en el motor relacional.

### 🔌 Migración y Transición de Persistencia Core
* **Migración Arquitectónica de Auto:** Desconexión completa del repositorio volátil simulado (`ConcurrentHashMap`) dentro de `BeanConfiguration.java`. 
* **Cableado Relacional:** Activación del enlace físico inyectando el componente `AutoPersistenceAdapter` hacia el caso de uso `RegistrarAutoUseCase`. Los datos automotores ahora fluyen con persistencia atómica hacia el motor relacional indexado en H2.

### 🧱 Despliegue de Componentes Relacionales de Contratos
* **Modelado Físico de Renta:** Creación de `AlquilerEntity.java` bajo el paquete `persistence.entity` utilizando anotaciones de JPA.
* **Mapeo Avanzado de Odómetros:** Inclusión de las columnas relacionales `kilometraje_inicial` y `kilometraje_final` a nivel de base de datos H2 para consolidar el histórico de recorridos y la auditoría dinámica del sistema.

### 🧱 Despliegue de Traductores y Repositorios de Alquiler
* **Repositorio de Persistencia:** Creación de `AlquilerJpaRepository.java` extendiendo las capacidades CRUD de Spring Data JPA sobre el motor H2.
* **Mapeador de Dominio Renta:** Creación de `AlquilerMapper.java` garantizando el aislamiento bidireccional entre la tabla física de contratos relacionales y las mutaciones lógicas de odómetros controladas por el núcleo del negocio.

---

## 📅 Octubre 9, 2026

### 📌 CIERRE DE LA FASE 4: Infraestructura de Persistencia Real (JPA y H2)
* **Estatus de la Capa:** 100% COMPLETA, CONECTADA Y OPERATIVA.
* **Consolidación del Adaptador Contractual:** Despliegue de `AlquilerPersistenceAdapter.java` eliminando el último rastro de simulación volátil en memoria.
* **Ensamblaje Arquitectónico Global:** Refactorización final de `BeanConfiguration.java`. El microservicio opera de manera integral bajo la inyección real del motor H2, sincronizando transacciones complejas de odómetros y estados de negocio de extremo a extremo.

### 📌 PROYECTO CULMINADO Y ENTREGABLE
* **Estatus General:** CERTIFICADO CON PERSISTENCIA H2 EN EL PUERTO 8081.
* **Resultados de Auditoría:** Suite de pruebas en caliente aprobada con manejo controlado de excepciones semánticas de negocio (`RuntimeException`) ante duplicidad de placas y cierres extemporáneos de rentas. Flujo de datos relacionales verificado exitosamente mediante trazas de Hibernate e interacción directa en consola web.

### 📊 Automatización de Poblado y Semillero de Datos (Seeding)
* **Despliegue de Script SQL Core:** Creación del archivo de inicialización nativo `src/main/resources/data.sql` para automatizar la inserción de datos históricos y estados vehiculares.
* **Sincronización de Arranque:** Inyección del parámetro `spring.sql.init.mode=always` para garantizar que la carga de registros semilla ocurra en el ciclo correcto post-creación de tablas JPA. Esto optimiza el entorno para capturas de pantalla inmediatas en la consola web de H2 sin requerir peticiones HTTP previas.

### 👥 Apertura del Módulo e Infraestructura de Clientes
* **Diseño del Dominio Cliente:** Creación de `Cliente.java` estructurando la identidad a través de un campo semántico `tipoDocumento` (String) y un identificador de negocio numérico `documento` (Long).
* **Blindaje de Negocio por Documento Único:** Despliegue de `ClienteService.java` implementando validaciones cruzadas sobre el puerto de salida para interrumpir registros duplicados arrojando excepciones humanizadas `ClienteNoValidoException`.

### 🧱 Cierre de Infraestructura e Integración del Módulo Clientes
* **Modelo Físico de Tabla Clientes:** Despliegue de `ClienteEntity.java` mapeando la columna `documento` como valor numérico `Long` con restricción física única.
* **Mapeo y Puentes REST:** Creación de las capas adaptadoras (`ClienteJpaRepository`, `ClienteMapper`, `ClientePersistenceAdapter` y `ClienteController`), enlazando el endpoint `POST /api/clientes`.
* **Cableado Central de Dependencias:** Inyección del caso de uso `clienteService` en `BeanConfiguration.java`, completando el aislamiento del ciclo relacional del cliente.

### 🧪 Fase 4.5: Verificación Funcional del Módulo Clientes en Caliente
* **Ampliación de Batería Interactiva HTTP:** Mapeo de scripts de control para el alta y validación cruzada de usuarios en el archivo `pruebas.http`.
* **Resultados de las Pruebas de Criterio:**
  * **Persistencia de Identidad:** Verificación exitosa del endpoint `POST /api/clientes` procesando el formato numérico `Long` para el documento de forma atómica.
  * **Freno Eficiente de Duplicados:** Validación exitosa de la excepción del negocio, interrumpiendo el flujo ante entradas de números de cédula repetidos y notificando limpiamente el ID del registro propietario original en la base de datos.

### 🚀 Optimización Final y Consolidación del Motor Relacional
* **Restauración de Ciclo de Vida Nativo:** Se eliminó el script manual `schema.sql` y se configuró exitosamente `spring.jpa.hibernate.ddl-auto=update`. Se delega el modelado DDL de las 3 tablas al motor automático de Hibernate.
* **Poblado Masivo Silencioso:** Activación definitiva del semillero a través de `import.sql`. Se comprobó el correcto almacenamiento físico en H2 de catálogos de vehículos, contratos y usuarios con formato numérico.
* **Estabilización de Entorno:** El microservicio se encuentra 100% finalizado, compilando sin alertas en el puerto `8081` y con la persistencia totalmente enlazada al diseño de la Arquitectura Hexagonal.

### 🎨 Despliegue de Interfaz Gráfica Front-End Integrada
* **Creación de Componente Cliente Visual:** Implementación del archivo `src/main/resources/static/index.html` para la simulación interactiva y validación visual del microservicio.
* **Integración Nativa Asíncrona:** Inyección de scripts estructurados mediante `Fetch API` para conectar el entorno web con los endpoints REST (`/api/autos`, `/api/clientes`, `/api/alquileres`) expuestos en el puerto `8081`. 
* **Auditoría de Errores Visuales:** Se añade un monitor oscuro de trazas JSON que renderiza dinámicamente códigos HTTP Status y mensajes semánticos de excepciones. Esto proporciona un entorno ideal para la sustentación práctica ante el docente.
