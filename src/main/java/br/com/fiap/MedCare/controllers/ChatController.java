package br.com.fiap.MedCare.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.MedCare.models.Mensagem;
import br.com.fiap.MedCare.repository.ChatRepository;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    ChatRepository chatRepository;

    public ChatController(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @GetMapping("/mensagens")
    public ResponseEntity<List<Mensagem>> obterTodasMensagens() {
        List<Mensagem> mensagens = chatRepository.findAllByOrderByDataEnvioAsc();
        return new ResponseEntity<>(mensagens, HttpStatus.OK);
    }

    @PostMapping("/enviar")
    public ResponseEntity<Mensagem> enviarMensagem(@RequestBody Mensagem mensagem) {
        mensagem.setDataEnvio(new Date()); 
        Mensagem novaMensagem = chatRepository.save(mensagem);
        return new ResponseEntity<>(novaMensagem, HttpStatus.CREATED);
    }
}
