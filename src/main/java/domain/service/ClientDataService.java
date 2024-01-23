package domain.service;

import domain.model.ClientData;
import infraestructure.adapter.jpa.JpaClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ClientDataService {
    @Autowired
     JpaClienteRepository clienteRepository;



    public ClientDataService(JpaClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void saveAll(List<ClientData> clientes) {
        clienteRepository.saveAll(clientes);
    }

}
