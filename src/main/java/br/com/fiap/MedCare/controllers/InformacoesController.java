package br.com.fiap.MedCare.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.MedCare.exception.RestNotFoundException;
import br.com.fiap.MedCare.models.Informacoes;
import br.com.fiap.MedCare.models.Usuario;
import br.com.fiap.MedCare.repository.InformacoesRepository;
import br.com.fiap.MedCare.repository.UsuarioRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/infos")
public class InformacoesController {
   
   @Autowired
   InformacoesRepository iRepository;
   @Autowired
   UsuarioRepository uRepository; 

   @GetMapping
   ResponseEntity<Page<Informacoes>> getAllInformacoes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Informacoes> infos = iRepository.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(infos);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Informacoes> getInformacoesById(@PathVariable Long id) {
        return ResponseEntity.ok(getInfo(id));
    }
    @PostMapping
    public ResponseEntity<Informacoes> createInfo(@RequestBody @Valid Informacoes infos){

        infos.setUsuario(uRepository.findById(infos.getUsuario().getId()).get());

        System.out.println(infos.getUsuario());

        iRepository.save(infos);

        return ResponseEntity.status(HttpStatus.CREATED).body(infos);
    }
        @DeleteMapping("{id}")
    public ResponseEntity<Informacoes> delete(@PathVariable Long id) {

         iRepository.delete(getInfo(id));

         return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateInformacoes(@PathVariable Long id, @RequestBody @Valid Informacoes updatedInformacoes) {
    getInfo(id);

    updatedInformacoes.setId(id);

    iRepository.save(updatedInformacoes);

    return ResponseEntity.ok(updatedInformacoes);
    }

    private Informacoes getInfo(Long id) {
        return iRepository.findById(id).orElseThrow(() -> new RestNotFoundException("Info não encontrada"));
    }
}
