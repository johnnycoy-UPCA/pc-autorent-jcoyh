package co.edu.clase.pc_autorent_jcoyh.application.service;

import co.edu.clase.pc_autorent_jcoyh.application.port.input.RegistrarAutoUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.input.DarBajaAutoUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.input.ActualizarMantenimientoUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.input.ActualizarTarifasAutoUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.output.AutoRepositoryPort;
import co.edu.clase.pc_autorent_jcoyh.domain.model.Auto;
import co.edu.clase.pc_autorent_jcoyh.domain.model.EstadosNegocio;
import co.edu.clase.pc_autorent_jcoyh.domain.exception.AutoException;
import co.edu.clase.pc_autorent_jcoyh.domain.exception.NoEncontradoException;
import co.edu.clase.pc_autorent_jcoyh.domain.exception.MensajesError;

import java.util.List;

public class AutoService implements RegistrarAutoUseCase, 
                                    ActualizarMantenimientoUseCase, 
                                    DarBajaAutoUseCase,
                                    ActualizarTarifasAutoUseCase {
    
    private final AutoRepositoryPort autoRepositoryPort;

    public AutoService(AutoRepositoryPort autoRepositoryPort) {
        
        this.autoRepositoryPort = autoRepositoryPort;
    }

    @Override
    public Auto registrarAuto(Auto auto) {

        if (auto.getAnio() < 2000) {
            throw new AutoException(MensajesError.AUTO_ANIO_INVALIDO);
        }
        if (auto.getPlaca() == null || auto.getPlaca().isBlank()) {
            throw new AutoException(MensajesError.AUTO_PLACA_VACIA);
        }

        String regexPlaca = "^[A-Z]{3}-[0-9]{3}$";

        if (!auto.getPlaca().trim().toUpperCase().matches(regexPlaca)) {
            throw new AutoException(MensajesError.AUTO_PLACA_INVALIDA);
        }
        if (auto.getPrecioPorDia() < 1) {
            throw new AutoException(MensajesError.AUTO_PRECIO_DIA_INVALIDO);
        }
        if (auto.getDeposito() < auto.getPrecioPorDia()) {
            throw new AutoException(
                String.format(MensajesError.AUTO_DEPOSITO_INVALIDO, auto.getDeposito(), auto.getPrecioPorDia())
            );
        }

        List<Auto> autosExistentes = autoRepositoryPort.buscarPorPlaca(auto.getPlaca());

        for (Auto encontrado : autosExistentes) {
            if (!EstadosNegocio.AUTO_INACTIVO.equalsIgnoreCase(encontrado.getEstado())) {
                throw new  AutoException(
                    String.format(MensajesError.AUTO_PLACA_REPETIDA, encontrado.getPlaca())
                );
            }
        }

        Auto nuevoAuto = new Auto(
                            null, 
                            auto.getPlaca(), 
                            auto.getMarca(), 
                            auto.getModelo(), 
                            auto.getAnio(), 
                            auto.getPrecioPorDia(), 
                            auto.getDeposito(), 
                            auto.getKilometraje(), 
                            EstadosNegocio.AUTO_DISPONIBLE
                        );

        return autoRepositoryPort.guardar(nuevoAuto);
    }

    @Override
    public Auto actualizarMantenimiento(String placa, 
                                        boolean enMantenimiento, 
                                        Double kilometraje) {

        Auto auto = autoRepositoryPort
                        .buscarPorPlaca(placa)
                        .stream()
                        .filter(a -> !EstadosNegocio.AUTO_INACTIVO.equalsIgnoreCase(a.getEstado()))
                        .findFirst()
                        .orElseThrow(() -> new NoEncontradoException(
                                String.format(MensajesError.AUTO_PLACA_NO_REGISTRADO, placa)
                            )
                        );
                            
        if (enMantenimiento) {
            if (EstadosNegocio.AUTO_MANTENIMIENTO.equals(auto.getEstado())) {
                throw new AutoException(
                    String.format(MensajesError.AUTO_MANTENIMIENTO_DUPLICADO, auto.getPlaca())
                );
            }
            if (!EstadosNegocio.AUTO_DISPONIBLE.equals(auto.getEstado())) {
                throw new AutoException(
                    String.format(MensajesError.AUTO_MANTENIMIENTO_ESTADO_INVALIDO, auto.getPlaca(), auto.getEstado())
                );
            }

            auto.setEstado(EstadosNegocio.AUTO_MANTENIMIENTO);
        } else {
            if (!EstadosNegocio.AUTO_MANTENIMIENTO.equals(auto.getEstado())) {
                throw new AutoException(
                    String.format(MensajesError.AUTO_MANTENIMIENTO_SALIDA_INVALIDA, auto.getPlaca())
                );
            }
            if (kilometraje == null) {
                throw new AutoException(MensajesError.AUTO_NO_KILOMETRAJE);
            }
            if (kilometraje < auto.getKilometraje()) {
                throw new AutoException(
                    String.format(MensajesError.AUTO_KM_MENOR, kilometraje, auto.getKilometraje())
                );
            }

            auto.setKilometraje(kilometraje);
            auto.setEstado(EstadosNegocio.AUTO_DISPONIBLE);
        }
        return autoRepositoryPort.guardar(auto);
    }

    @Override
    public Auto darDeBajaAuto(Long id) {
        Auto auto = autoRepositoryPort.buscarPorId(id)
            .orElseThrow(() -> new NoEncontradoException(
                    String.format(MensajesError.AUTO_NO_REGISTRADO, id)
                )
            );

        if (EstadosNegocio.AUTO_ALQUILADO.equals(auto.getEstado())) {
            throw new AutoException(
                String.format(MensajesError.AUTO_BAJA_CONTRATO_ACTIVO, auto.getPlaca())
            );
        }

        auto.setEstado(EstadosNegocio.AUTO_INACTIVO);
        return autoRepositoryPort.guardar(auto);
    }

    @Override
    public Auto actualizarTarifasAuto(Long id, 
                                    double nuevoPrecioPorDia, 
                                    double nuevoDeposito) {

        Auto auto = autoRepositoryPort.buscarPorId(id)
            .orElseThrow(() -> new NoEncontradoException(
                String.format(MensajesError.AUTO_NO_REGISTRADO, id)
            ));

        if (EstadosNegocio.AUTO_INACTIVO.equalsIgnoreCase(auto.getEstado())) {
            throw new AutoException(
                String.format(MensajesError.AUTO_NO_DISPONIBLE, auto.getPlaca(), auto.getEstado())
            );
        }
        if (nuevoPrecioPorDia < 1) {
            throw new AutoException(MensajesError.AUTO_PRECIO_DIA_INVALIDO);
        }
        if (nuevoDeposito < nuevoPrecioPorDia) {
            throw new AutoException(
                String.format(MensajesError.AUTO_DEPOSITO_INVALIDO, nuevoDeposito, nuevoPrecioPorDia)
            );
        }

        auto.setPrecioPorDia(nuevoPrecioPorDia);
        auto.setDeposito(nuevoDeposito);

        return autoRepositoryPort.guardar(auto);
    }

}
