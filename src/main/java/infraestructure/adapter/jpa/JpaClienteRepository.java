package infraestructure.adapter.jpa;

import domain.model.ClientData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface JpaClienteRepository extends JpaRepository<ClientData, Long> {
}