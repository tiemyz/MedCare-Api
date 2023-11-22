package br.com.fiap.MedCare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.MedCare.models.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    // You can add custom query methods for specific filtering if needed

    // Example of a custom query method for finding users by type
    Page<Usuario> findByTipoUsuario(String tipoUsuario, Pageable pageable);
}