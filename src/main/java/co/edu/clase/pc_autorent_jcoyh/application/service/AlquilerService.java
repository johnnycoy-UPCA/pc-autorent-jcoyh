package co.edu.clase.pc_autorent_jcoyh.application.service;

import co.edu.clase.pc_autorent_jcoyh.application.port.input.CancelarAlquilerUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.input.CrearAlquilerUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.input.FinalizarAlquilerUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.input.IniciarReservaUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.output.AlquilerRepositoryPort;
import co.edu.clase.pc_autorent_jcoyh.application.port.output.AutoRepositoryPort;
import co.edu.clase.pc_autorent_jcoyh.application.port.output.ClienteRepositoryPort;
import co.edu.clase.pc_autorent_jcoyh.domain.exception.AlquilerException;
import co.edu.clase.pc_autorent_jcoyh.domain.exception.AutoException;
import co.edu.clase.pc_autorent_jcoyh.domain.exception.NoEncontradoException;
import co.edu.clase.pc_autorent_jcoyh.domain.exception.MensajesError;
import co.edu.clase.pc_autorent_jcoyh.domain.model.Alquiler;
import co.edu.clase.pc_autorent_jcoyh.domain.model.Auto;
import co.edu.clase.pc_autorent_jcoyh.domain.model.Cliente;
import co.edu.clase.pc_autorent_jcoyh.domain.model.EstadosNegocio;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
 
public class AlquilerService implements CrearAlquilerUseCase, 
                                        FinalizarAlquilerUseCase, 
                                        CancelarAlquilerUseCase, 
                                        IniciarReservaUseCase {

    private final AlquilerRepositoryPort alquilerRepositoryPort;
    private final AutoRepositoryPort autoRepositoryPort;
    private final ClienteRepositoryPort clienteRepositoryPort;

    public AlquilerService(AlquilerRepositoryPort alquilerRepositoryPort, 
                        AutoRepositoryPort autoRepositoryPort,
                        ClienteRepositoryPort clienteRepositoryPort) {

        this.alquilerRepositoryPort = alquilerRepositoryPort;
        this.autoRepositoryPort = autoRepositoryPort;
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    private boolean seCruzaConPeriodo(Alquiler alquilerExistente, 
                                    LocalDate nuevaFechaInicio, 
                                    int nuevosDias) {
                                        
        if (EstadosNegocio.ALQUILER_CANCELADO.equalsIgnoreCase(alquilerExistente.getEstado()) || 
            EstadosNegocio.ALQUILER_FINALIZADO.equalsIgnoreCase(alquilerExistente.getEstado())) {
            return false;
        }

        LocalDate nuevoFin = nuevaFechaInicio.plusDays(nuevosDias);
        
        boolean seCruzaConFechas = !nuevaFechaInicio.isAfter(alquilerExistente.getFechaFin()) && 
                                !nuevoFin.isBefore(alquilerExistente.getFechaInicio());

        boolean bloqueoPorEstado = EstadosNegocio.ALQUILER_ACTIVO.equalsIgnoreCase(alquilerExistente.getEstado()) || 
                                EstadosNegocio.ALQUILER_PENDIENTE.equalsIgnoreCase(alquilerExistente.getEstado());

        return seCruzaConFechas && bloqueoPorEstado;
    }

    private Auto validarClienteYAuto(Long clienteId, 
                                    Long autoId, 
                                    Long alquilerId, 
                                    LocalDate fechaInicio, 
                                    int cantidadDias) {

        Cliente cliente = clienteRepositoryPort.buscarPorId(clienteId)
                            .orElseThrow(() -> new NoEncontradoException(
                                    String.format(MensajesError.CLIENTE_NO_REGISTRADO, clienteId)
                                )
                            );

        if (EstadosNegocio.CLIENTE_INACTIVO.equalsIgnoreCase(cliente.getEstado())) {
            throw new AlquilerException(
                    String.format(MensajesError.CLIENTE_INACTIVO, cliente.getDocumento())
            );
        }

        Auto auto = autoRepositoryPort.buscarPorId(autoId)
                        .orElseThrow(() -> new NoEncontradoException(
                                String.format(MensajesError.AUTO_NO_REGISTRADO, autoId)
                            )
                        );

        if (!EstadosNegocio.AUTO_DISPONIBLE.equals(auto.getEstado())) {
            throw new AlquilerException(
                    String.format(MensajesError.AUTO_NO_DISPONIBLE, auto.getPlaca(), auto.getEstado())
            );
        }

        boolean seCruzaConAlquilerVigente = alquilerRepositoryPort
                                                .listarTodos()
                                                .stream()
                                                .filter(alq -> alq.getAutoId().equals(auto.getId()))
                                                .filter(alq -> alquilerId == null || !alq.getId().equals(alquilerId))
                                                .anyMatch(alq -> seCruzaConPeriodo(alq, fechaInicio, cantidadDias));

        if (seCruzaConAlquilerVigente) {
            throw new AlquilerException(
                String.format(MensajesError.ALQUILER_FECHAS_CRUZADAS, auto.getPlaca())
            );
        }

        return auto;
    }

    private void calcularYConfigurarAlquiler(Alquiler alquilerNuevo, 
                                            double precioPorDia, 
                                            LocalDate fechaInicioDigitada) {

        if (alquilerNuevo.getCantidadDias() <= 0) {
            throw new AlquilerException(MensajesError.ALQUILER_CANTIDAD_DIAS_INVALIDA);
        }
        if (fechaInicioDigitada == null) {
            throw new AlquilerException(MensajesError.ALQUILER_FECHA_INICIO_OBLIGATORIA);
        }
        if (fechaInicioDigitada.isBefore(LocalDate.now())) {
            throw new AlquilerException(
                String.format(MensajesError.ALQUILER_FECHA_PASADA, fechaInicioDigitada)
            );
        }

        alquilerNuevo.setValorTotal(precioPorDia * alquilerNuevo.getCantidadDias());
        alquilerNuevo.setFechaInicio(fechaInicioDigitada);
        alquilerNuevo.setFechaFin(fechaInicioDigitada.plusDays(alquilerNuevo.getCantidadDias()));

        if (!fechaInicioDigitada.isAfter(LocalDate.now())) {
            alquilerNuevo.setEstado(EstadosNegocio.ALQUILER_ACTIVO);
        } else {
            alquilerNuevo.setKilometrajeInicial(null);
        }
    }

    private void alquilarAuto(Auto auto) {
        if (!EstadosNegocio.AUTO_DISPONIBLE.equals(auto.getEstado())) {
            throw new AutoException(
                String.format(MensajesError.AUTO_NO_DISPONIBLE, auto.getPlaca(), auto.getEstado())
            );
        }

        auto.setEstado(EstadosNegocio.AUTO_ALQUILADO);
    }

    private void procesarDevolucion(Alquiler alquiler, 
                                LocalDate fechaEntregaReal, 
                                Double kilometrajeFinalReal, 
                                double precioPorDia) {

        if (!EstadosNegocio.ALQUILER_ACTIVO.equals(alquiler.getEstado())) {
            throw new AlquilerException(MensajesError.ALQUILER_DEVOLUCION_ESTADO_INVALIDO);
        }
        if (EstadosNegocio.ALQUILER_FINALIZADO.equals(alquiler.getEstado())) {
            throw new AlquilerException(MensajesError.ALQUILER_FINALIZAR_DOBLE);
        }
        if (alquiler.getKilometrajeInicial() != null && kilometrajeFinalReal < alquiler.getKilometrajeInicial()) {
            throw new AlquilerException(
                String.format(MensajesError.AUTO_KM_MENOR, kilometrajeFinalReal, alquiler.getKilometrajeInicial())
            );
        }

        alquiler.setFechaFin(fechaEntregaReal);
        
        long diasCalculados = ChronoUnit.DAYS.between(alquiler.getFechaInicio(), alquiler.getFechaFin());
        alquiler.setCantidadDias((diasCalculados <= 0) ? 1 : (int) diasCalculados);
        
        alquiler.setValorTotal(precioPorDia * alquiler.getCantidadDias());
        alquiler.setKilometrajeFinal(kilometrajeFinalReal);
        alquiler.setEstado(EstadosNegocio.ALQUILER_FINALIZADO);
    }

    @Override
    public Alquiler crearAlquiler(Alquiler alquiler) {

        Auto auto = validarClienteYAuto(alquiler.getClienteId(), 
                                        alquiler.getAutoId(), 
                                        null, 
                                        alquiler.getFechaInicio(), 
                                        alquiler.getCantidadDias());

        Alquiler nuevoAlquiler = new Alquiler(
                                    null, 
                                    alquiler.getClienteId(), 
                                    alquiler.getAutoId(), 
                                    LocalDate.now(), 
                                    alquiler.getFechaInicio(), 
                                    null, 
                                    alquiler.getCantidadDias(), 
                                    0.0, 
                                    auto.getKilometraje(), 
                                    null, 
                                    EstadosNegocio.ALQUILER_PENDIENTE
                                );
        
        this.calcularYConfigurarAlquiler(nuevoAlquiler, auto.getPrecioPorDia(), 
                                                alquiler.getFechaInicio());

        if (EstadosNegocio.ALQUILER_ACTIVO.equals(nuevoAlquiler.getEstado())) {
            alquilarAuto(auto);
            autoRepositoryPort.guardar(auto);
        }

        return alquilerRepositoryPort.guardar(nuevoAlquiler);
    }

    @Override
    public Alquiler finalizarAlquiler(Long alquilerId, 
                                    Double kilometrajeFinal, 
                                    LocalDate fechaEntregaReal) {

        Alquiler alquiler = alquilerRepositoryPort.buscarPorId(alquilerId)
                                .orElseThrow(() -> new NoEncontradoException(
                                        String.format(MensajesError.ALQUILER_NO_ENCONTRADO, alquilerId)
                                    )
                                );

        Auto auto = autoRepositoryPort.buscarPorId(alquiler.getAutoId())
                        .orElseThrow(() -> new NoEncontradoException(
                                String.format(MensajesError.AUTO_NO_REGISTRADO, alquiler.getAutoId())
                            )
                        );

        LocalDate fechaFinalizacion = (fechaEntregaReal != null) 
                                    ? fechaEntregaReal 
                                    : LocalDate.now();

        this.procesarDevolucion(alquiler,
                                fechaFinalizacion, 
                                kilometrajeFinal, 
                                auto.getPrecioPorDia());

        if (kilometrajeFinal == null) {
            throw new AutoException(MensajesError.AUTO_NO_KILOMETRAJE);
        }
        if (kilometrajeFinal < auto.getKilometraje()) {
            throw new AutoException(
                String.format(MensajesError.AUTO_KM_MENOR, kilometrajeFinal, auto.getKilometraje())
            );
        }

        auto.setKilometraje(kilometrajeFinal);
        auto.setEstado(EstadosNegocio.AUTO_DISPONIBLE);


        autoRepositoryPort.guardar(auto);
        return alquilerRepositoryPort.guardar(alquiler);
    }

    @Override
    public Alquiler iniciarReserva(Long alquilerId) {
        Alquiler alquiler = alquilerRepositoryPort.buscarPorId(alquilerId)
                                .orElseThrow(() -> new NoEncontradoException(
                                        String.format(MensajesError.ALQUILER_NO_ENCONTRADO, alquilerId)
                                    )
                                );

        Auto auto = validarClienteYAuto(alquiler.getClienteId(), 
                                        alquiler.getAutoId(), 
                                        alquiler.getId(), 
                                        alquiler.getFechaInicio(), 
                                        alquiler.getCantidadDias());
        
        if (!EstadosNegocio.ALQUILER_PENDIENTE.equals(alquiler.getEstado())) {
            throw new AlquilerException(MensajesError.ALQUILER_INICIO_RESERVA_INVALIDO);
        }
        alquiler.setKilometrajeInicial(auto.getKilometraje());
        alquiler.setEstado(EstadosNegocio.ALQUILER_ACTIVO);

        this.alquilarAuto(auto);

        autoRepositoryPort.guardar(auto);
        return alquilerRepositoryPort.guardar(alquiler);
    }

    @Override
    public Alquiler cancelarAlquiler(Long alquilerId) {
        Alquiler alquiler = alquilerRepositoryPort.buscarPorId(alquilerId)
                                .orElseThrow(() -> new NoEncontradoException(
                                        String.format(MensajesError.ALQUILER_NO_ENCONTRADO, alquilerId)
                                    )
                                );

        Auto auto = autoRepositoryPort.buscarPorId(alquiler.getAutoId())
                        .orElseThrow(() -> new NoEncontradoException(
                                String.format(MensajesError.AUTO_NO_REGISTRADO, alquiler.getAutoId())
                            )
                        );

        if (!EstadosNegocio.ALQUILER_ACTIVO.equals(alquiler.getEstado()) && 
            !EstadosNegocio.ALQUILER_PENDIENTE.equals(alquiler.getEstado())) {
            throw new AlquilerException(
                String.format(MensajesError.ALQUILER_CANCELACION_INVALIDA, alquiler.getEstado())
            );
        }

        alquiler.setEstado(EstadosNegocio.ALQUILER_CANCELADO);

        auto.setEstado(EstadosNegocio.AUTO_DISPONIBLE);

        autoRepositoryPort.guardar(auto);
        return alquilerRepositoryPort.guardar(alquiler);
    }
}
