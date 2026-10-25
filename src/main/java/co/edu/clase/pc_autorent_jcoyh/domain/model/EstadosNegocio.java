package co.edu.clase.pc_autorent_jcoyh.domain.model;

public final class EstadosNegocio {

    private EstadosNegocio() {}

    public static final String AUTO_DISPONIBLE = "DISPONIBLE";
    public static final String AUTO_ALQUILADO = "ALQUILADO";
    public static final String AUTO_MANTENIMIENTO = "MANTENIMIENTO";
    public static final String AUTO_INACTIVO = "INACTIVO";

    public static final String CLIENTE_ACTIVO = "ACTIVO";
    public static final String CLIENTE_INACTIVO = "INACTIVO";

    public static final String ALQUILER_PENDIENTE = "PENDIENTE";
    public static final String ALQUILER_ACTIVO = "ACTIVO";
    public static final String ALQUILER_FINALIZADO = "FINALIZADO";
    public static final String ALQUILER_CANCELADO = "CANCELADO";
}
