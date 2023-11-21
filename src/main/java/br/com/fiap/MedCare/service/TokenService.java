package br.com.fiap.MedCare.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.fiap.MedCare.models.Credencial;
import br.com.fiap.MedCare.models.Token;
import br.com.fiap.MedCare.models.Usuario;
import br.com.fiap.MedCare.repository.UsuarioRepository;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Service
public class TokenService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Value("${jwt.secret}")
    String secret;

    private Algorithm getAlgorithm() {
        byte[] secretBytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
        return Algorithm.HMAC256(secretBytes);
    }

    public Token generateToken(Credencial credencial) {
        Algorithm alg = getAlgorithm();
        var token = JWT.create()
                    .withSubject(credencial.email())
                    .withIssuer("AuxiliaMed")
                    .withExpiresAt(Instant.now().plus(20, ChronoUnit.MINUTES))
                    .sign(alg);

        return new Token(token, "JWT", "Bearer");
    }

    public Usuario getUserByToken(String token) {
        Algorithm alg = getAlgorithm();
        var email = JWT.require(alg)
                    .withIssuer("AuxiliaMed")
                    .build()
                    .verify(token)
                    .getSubject();

        return usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new JWTVerificationException("Usuario invalido"));
    }
}