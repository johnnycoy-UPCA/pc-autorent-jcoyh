package co.edu.clase.pc_autorent_jcoyh.infrastructure.config;

import co.edu.clase.pc_autorent_jcoyh.application.port.output.AlquilerRepositoryPort;
import co.edu.clase.pc_autorent_jcoyh.application.port.output.AutoRepositoryPort;
import co.edu.clase.pc_autorent_jcoyh.application.port.output.ClienteRepositoryPort;
import co.edu.clase.pc_autorent_jcoyh.application.service.AlquilerService;
import co.edu.clase.pc_autorent_jcoyh.application.service.AutoService;
import co.edu.clase.pc_autorent_jcoyh.application.service.ClienteService;
import jakarta.transaction.Transactional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfiguration {

    @Bean
    @Transactional 
    public AutoService autoService(AutoRepositoryPort autoRepositoryPort) {
        return new AutoService(autoRepositoryPort);
    }

    @Bean
    @Transactional 
    public AlquilerService alquilerService(AlquilerRepositoryPort alquilerRepositoryPort, 
                                        AutoRepositoryPort autoRepositoryPort,
                                        ClienteRepositoryPort clienteRepositoryPort) {
        return new AlquilerService(alquilerRepositoryPort, autoRepositoryPort, clienteRepositoryPort);
    }

    @Bean 
    @Transactional 
    public ClienteService clienteService(ClienteRepositoryPort clienteRepositoryPort,
                                        AlquilerRepositoryPort alquilerRepositoryPort) {
                return new ClienteService(clienteRepositoryPort, alquilerRepositoryPort);
    }
}
