package br.com.fiap.MedCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.MedCare.models.Informacoes;

public interface InformacoesRepository extends JpaRepository<Informacoes, Long>{
    
}
