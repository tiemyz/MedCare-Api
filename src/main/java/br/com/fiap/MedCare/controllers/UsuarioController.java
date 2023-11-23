package br.com.fiap.MedCare.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.MedCare.models.Credencial;
import br.com.fiap.MedCare.models.Usuario;
import br.com.fiap.MedCare.repository.UsuarioRepository;
import br.com.fiap.MedCare.service.TokenService;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:19006", allowedHeaders = "*")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    AuthenticationManager manager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    TokenService tokenService;

    @PostMapping("/registrar")
    public ResponseEntity<Object> registrar(@RequestBody @Valid Usuario usuario){
        String tipoUsuario = getTipoUsuario(usuario.getEmail());
        usuario.setTipoUsuario(tipoUsuario);

        usuario.setSenha(encoder.encode(usuario.getSenha()));

        repository.save(usuario);

        Map<String, Object> response = new HashMap<>();
        response.put("tipoUsuario", tipoUsuario);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Credencial credencial) {
        try {
            Authentication auth = manager.authenticate(credencial.toAuthentication());
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
    
            String tipoUsuario = getTipoUsuario(userDetails.getUsername());
            var token = tokenService.generateToken(credencial);
    
            Map<String, Object> response = new HashMap<>();
            response.put("tipoUsuario", tipoUsuario);
            response.put("token", token);
            response.put("nome", userDetails.getUsername()); // Adicione o nome do usuário
    
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a solicitação");
        }
    }
    

    @GetMapping
    public ResponseEntity<Page<Usuario>> getAllUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Usuario> usuarios = repository.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUsuario(@PathVariable Long id, @RequestBody @Valid Usuario updatedUsuario) {
        return repository.findById(id)
                .map(existingUsuario -> {
                    // Update fields of existingUsuario with the values from updatedUsuario
                    existingUsuario.setNome(updatedUsuario.getNome());
                    existingUsuario.setEmail(updatedUsuario.getEmail());
                    existingUsuario.setDataNascimento(updatedUsuario.getDataNascimento());
                    existingUsuario.setCpfCrm(updatedUsuario.getCpfCrm());

                    repository.save(existingUsuario);

                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUsuario(@PathVariable Long id) {
        return repository.findById(id)
                .map(usuario -> {
                    repository.delete(usuario);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private String getTipoUsuario(String email) {
        return email.contains("@medico.com") ? "medico" : "paciente";
    }
}