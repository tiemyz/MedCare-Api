package br.com.fiap.MedCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fiap.MedCare.models.Mensagem;
import java.util.List;
public interface ChatRepository extends JpaRepository<Mensagem, Long> {
    List<Mensagem> findAllByOrderByDataEnvioAsc();
}
