package co.edu.clase.pc_autorent_jcoyh.domain.exception;

public final class MensajesError {
    private MensajesError() {}

    public static final String AUTO_KM_MENOR = "El kilometraje ingresado (%.2f) no puede ser menor que el kilometraje registrado (%.2f) del vehículo.";
    public static final String AUTO_NO_DISPONIBLE = "El auto con placa %s no está DISPONIBLE. Su estado es %s";
    public static final String AUTO_MANTENIMIENTO_DUPLICADO = "El vehículo con placa %s ya se encuentra en MANTENIMIENTO.";
    public static final String AUTO_MANTENIMIENTO_ESTADO_INVALIDO = "El vehículo con placa %s no puede ingresar a mantenimiento porque su estado actual es %s.";
    public static final String AUTO_BAJA_CONTRATO_ACTIVO = "El vehículo con placa %s no puede ser dado de baja porque se encuentra bajo un contrato de alquiler ACTIVO vigente.";
    public static final String AUTO_MANTENIMIENTO_SALIDA_INVALIDA = "El vehículo con placa %s no se encuentra actualmente en estado de MANTENIMIENTO.";
    public static final String AUTO_PLACA_REPETIDA = "El vehículo con placa %s ya se encuentra registrado y ACTIVO en el sistema.";
    public static final String AUTO_PLACA_NO_REGISTRADO = "El vehículo con placa %s no se encuentra registrado en el sistema.";
    public static final String AUTO_NO_REGISTRADO = "El vehículo con ID %s no se encuentra registrado en el sistema.";
    public static final String AUTO_NO_KILOMETRAJE = "El registro del kilometraje es obligatorio.";

    public static final String CLIENTE_CAMPO_OBLIGATORIO = "Debe diligenciar los campos obligatorios y no pueden estar vacíos.";
    public static final String CLIENTE_DOCUMENTO_OBLIGATORIO = "Debe ingresar un documento válido.";
    public static final String CLIENTE_EMAIL_INVALIDO = "El correo electrónico '%s' no tiene un formato válido.";
    public static final String CLIENTE_DOCUMENTO_REPETIDO = "Ya existe un cliente registrado con el documento %d en el sistema.";
    public static final String CLIENTE_INACTIVO = "El cliente con documento %d se encuentra INACTIVO.";
    public static final String CLIENTE_NO_REGISTRADO = "El cliente con ID %d no se encuentra registrado en el sistema.";
    public static final String CLIENTE_ALQUILER_ACTIVO = "El cliente %s no puede ser inactivado debido a que posee un contrato de alquiler ACTIVO vigente.";

    public static final String ALQUILER_CANTIDAD_DIAS_INVALIDA = "La cantidad de días del contrato debe ser mayor que cero.";
    public static final String ALQUILER_FECHA_INICIO_OBLIGATORIA = "La fecha de inicio del alquiler es obligatoria.";
    public static final String ALQUILER_FECHA_PASADA = "La fecha de inicio (%s) no puede ser anterior a la fecha de hoy.";
    public static final String ALQUILER_INICIO_RESERVA_INVALIDO = "Solo se pueden iniciar contratos de alquiler que se encuentren en estado PENDIENTE.";
    public static final String ALQUILER_DEVOLUCION_ESTADO_INVALIDO = "Solo se pueden finalizar alquileres que se encuentren en estado ACTIVO.";
    public static final String ALQUILER_CANCELACION_INVALIDA = "Solo se pueden cancelar contratos en estado ACTIVO o PENDIENTE. El contrato se encuentra %s";
    public static final String ALQUILER_FINALIZAR_DOBLE = "No se puede finalizar dos veces el mismo alquiler.";
    public static final String ALQUILER_FECHAS_CRUZADAS = "El vehículo con placa %s cuenta con un alquiler ACTIVO en la fecha ingresada.";
    public static final String ALQUILER_NO_ENCONTRADO = "Contrato de alquiler no encontrado (ID: %s).";
    public static final String ALQUILER_CAMPOS_OBLIGATORIOS = "Todos los campos (clienteId, autoId, cantidadDias) son obligatorios.";

}