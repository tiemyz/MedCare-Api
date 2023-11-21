package br.com.fiap.MedCare.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.MedCare.models.Credencial;
import br.com.fiap.MedCare.models.Usuario;
import br.com.fiap.MedCare.repository.UsuarioRepository;
import br.com.fiap.MedCare.service.TokenService;
import jakarta.validation.Valid;

@RestController
public class UsuarioController {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    AuthenticationManager manager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    TokenService tokenService;

    @PostMapping("/api/registrar")
    public ResponseEntity<Object> registrar(@RequestBody @Valid Usuario usuario){
        String tipoUsuario = getTipoUsuario(usuario.getEmail());
        usuario.setTipoUsuario(tipoUsuario);

        usuario.setSenha(encoder.encode(usuario.getSenha()));

        repository.save(usuario);

        Map<String, Object> response = new HashMap<>();
        response.put("tipoUsuario", tipoUsuario);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/login")
    public ResponseEntity<Object> login(@RequestBody Credencial credencial){
        try {
            Authentication auth = manager.authenticate(credencial.toAuthentication());
            UserDetails userDetails = (UserDetails) auth.getPrincipal();

            String tipoUsuario = getTipoUsuario(userDetails.getUsername());

            var token = tokenService.generateToken(credencial);
            
            Map<String, Object> response = new HashMap<>();
            response.put("tipoUsuario", tipoUsuario);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a solicitação");
        }
    }

    private String getTipoUsuario(String email) {
        return email.contains("@medico.com") ? "medico" : "paciente";
    }
}